package com.videorecommend.service;

import com.videorecommend.entity.UserBehavior;
import com.videorecommend.entity.UserPreference;
import com.videorecommend.entity.Video;
import com.videorecommend.repository.UserBehaviorRepository;
import com.videorecommend.repository.UserPreferenceRepository;
import com.videorecommend.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 推荐服务类
 * 
 * @author VideoRecommend
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RecommendationService {

    private final VideoRepository videoRepository;
    private final UserBehaviorRepository userBehaviorRepository;
    private final UserPreferenceRepository userPreferenceRepository;

    /**
     * 基于用户偏好的推荐
     */
    public List<Video> getPersonalizedRecommendations(Long userId, int limit) {
        // 获取用户偏好
        List<UserPreference> userPreferences = userPreferenceRepository.findUserPreferencesByScore(userId);
        
        if (userPreferences.isEmpty()) {
            // 如果没有偏好数据，返回热门视频
            return getPopularVideos(limit);
        }

        // 根据偏好分数排序分类
        Map<Video.VideoCategory, Double> categoryScores = userPreferences.stream()
                .collect(Collectors.toMap(
                        UserPreference::getCategory,
                        UserPreference::getPreferenceScore,
                        (existing, replacement) -> existing
                ));

        // 按偏好分数排序分类
        List<Video.VideoCategory> sortedCategories = categoryScores.entrySet().stream()
                .sorted(Map.Entry.<Video.VideoCategory, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<Video> recommendedVideos = new ArrayList<>();
        int videosPerCategory = Math.max(1, limit / sortedCategories.size());

        for (Video.VideoCategory category : sortedCategories) {
            if (recommendedVideos.size() >= limit) break;
            
            Pageable pageable = PageRequest.of(0, videosPerCategory);
            Page<Video> categoryVideos = videoRepository.findPopularVideosByCategory(category, pageable);
            recommendedVideos.addAll(categoryVideos.getContent());
        }

        // 如果推荐视频不足，补充热门视频
        if (recommendedVideos.size() < limit) {
            int remaining = limit - recommendedVideos.size();
            List<Video> popularVideos = getPopularVideos(remaining);
            recommendedVideos.addAll(popularVideos);
        }

        return recommendedVideos.stream()
                .distinct()
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * 基于协同过滤的推荐
     */
    public List<Video> getCollaborativeFilteringRecommendations(Long userId, int limit) {
        // 获取用户观看过的视频
        List<UserBehavior> userViews = userBehaviorRepository.findByUserIdAndBehaviorType(userId, UserBehavior.BehaviorType.VIEW);
        Set<Long> userViewedVideoIds = userViews.stream()
                .map(UserBehavior::getVideoId)
                .collect(Collectors.toSet());

        // 找到与用户有相似行为的其他用户
        List<Long> similarUserIds = findSimilarUsers(userId, userViewedVideoIds);

        if (similarUserIds.isEmpty()) {
            return getPopularVideos(limit);
        }

        // 获取相似用户观看但当前用户未观看的视频
        Set<Long> recommendedVideoIds = new HashSet<>();
        for (Long similarUserId : similarUserIds) {
            List<UserBehavior> similarUserViews = userBehaviorRepository.findByUserIdAndBehaviorType(similarUserId, UserBehavior.BehaviorType.VIEW);
            for (UserBehavior behavior : similarUserViews) {
                if (!userViewedVideoIds.contains(behavior.getVideoId())) {
                    recommendedVideoIds.add(behavior.getVideoId());
                }
            }
        }

        // 获取推荐视频详情
        List<Video> recommendedVideos = new ArrayList<>();
        for (Long videoId : recommendedVideoIds) {
            videoRepository.findById(videoId).ifPresent(recommendedVideos::add);
        }

        // 按观看次数排序
        recommendedVideos.sort((v1, v2) -> Long.compare(v2.getViewCount(), v1.getViewCount()));

        return recommendedVideos.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * 基于内容的推荐
     */
    public List<Video> getContentBasedRecommendations(Long userId, int limit) {
        // 获取用户最近观看的视频
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserBehavior> recentViews = userBehaviorRepository.findRecentViewsByUserId(userId, pageable);
        
        if (recentViews.isEmpty()) {
            return getPopularVideos(limit);
        }

        // 分析用户观看视频的特征
        Map<Video.VideoCategory, Integer> categoryCounts = new HashMap<>();
        Set<String> userTags = new HashSet<>();

        for (UserBehavior behavior : recentViews.getContent()) {
            videoRepository.findById(behavior.getVideoId()).ifPresent(video -> {
                // 统计分类
                categoryCounts.merge(video.getCategory(), 1, Integer::sum);
                
                // 收集标签
                if (video.getTags() != null) {
                    String[] tags = video.getTags().split(",");
                    userTags.addAll(Arrays.asList(tags));
                }
            });
        }

        // 根据用户偏好推荐相似视频
        List<Video> recommendedVideos = new ArrayList<>();
        
        // 按分类推荐
        for (Map.Entry<Video.VideoCategory, Integer> entry : categoryCounts.entrySet()) {
            Video.VideoCategory category = entry.getKey();
            int count = entry.getValue();
            
            if (count >= 2) { // 用户至少观看了2个该分类的视频
                Pageable categoryPageable = PageRequest.of(0, limit / categoryCounts.size());
                Page<Video> categoryVideos = videoRepository.findPopularVideosByCategory(category, categoryPageable);
                recommendedVideos.addAll(categoryVideos.getContent());
            }
        }

        // 按标签推荐
        for (String tag : userTags) {
            if (recommendedVideos.size() >= limit) break;
            
            Pageable tagPageable = PageRequest.of(0, limit / userTags.size());
            Page<Video> tagVideos = videoRepository.findByTag(tag.trim(), tagPageable);
            recommendedVideos.addAll(tagVideos.getContent());
        }

        return recommendedVideos.stream()
                .distinct()
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * 获取热门视频
     */
    public List<Video> getPopularVideos(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        Page<Video> popularVideos = videoRepository.findPopularVideos(pageable);
        return popularVideos.getContent();
    }

    /**
     * 获取最新视频
     */
    public List<Video> getLatestVideos(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        Page<Video> latestVideos = videoRepository.findLatestVideos(pageable);
        return latestVideos.getContent();
    }

    /**
     * 更新用户偏好
     */
    public void updateUserPreferences(Long userId, Long videoId, UserBehavior.BehaviorType behaviorType) {
        videoRepository.findById(videoId).ifPresent(video -> {
            UserPreference preference = userPreferenceRepository.findByUserIdAndCategory(userId, video.getCategory())
                    .orElse(new UserPreference());

            preference.setUserId(userId);
            preference.setCategory(video.getCategory());
            preference.setLastInteractionAt(LocalDateTime.now());

            // 根据行为类型调整偏好分数
            double scoreAdjustment = getScoreAdjustment(behaviorType);
            double newScore = Math.min(1.0, preference.getPreferenceScore() + scoreAdjustment);
            preference.setPreferenceScore(Math.max(0.0, newScore));

            preference.setInteractionCount(preference.getInteractionCount() + 1);

            userPreferenceRepository.save(preference);
        });
    }

    /**
     * 记录用户行为
     */
    public void recordUserBehavior(Long userId, Long videoId, UserBehavior.BehaviorType behaviorType) {
        UserBehavior behavior = new UserBehavior();
        behavior.setUserId(userId);
        behavior.setVideoId(videoId);
        behavior.setBehaviorType(behaviorType);
        behavior.setCreatedAt(LocalDateTime.now());

        userBehaviorRepository.save(behavior);

        // 更新用户偏好
        updateUserPreferences(userId, videoId, behaviorType);

        // 更新视频统计
        updateVideoStatistics(videoId, behaviorType);
    }

    /**
     * 查找相似用户
     */
    private List<Long> findSimilarUsers(Long userId, Set<Long> userViewedVideoIds) {
        // 获取所有观看过相同视频的用户
        Map<Long, Integer> userSimilarity = new HashMap<>();
        
        for (Long videoId : userViewedVideoIds) {
            List<UserBehavior> videoViews = userBehaviorRepository.findByVideoIdAndBehaviorType(videoId, UserBehavior.BehaviorType.VIEW);
            for (UserBehavior behavior : videoViews) {
                if (!behavior.getUserId().equals(userId)) {
                    userSimilarity.merge(behavior.getUserId(), 1, Integer::sum);
                }
            }
        }

        // 按相似度排序
        return userSimilarity.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * 获取行为类型对应的分数调整
     */
    private double getScoreAdjustment(UserBehavior.BehaviorType behaviorType) {
        switch (behaviorType) {
            case VIEW:
                return 0.1;
            case LIKE:
                return 0.3;
            case FAVORITE:
                return 0.5;
            case SHARE:
                return 0.4;
            case COMMENT:
                return 0.2;
            case DISLIKE:
                return -0.3;
            default:
                return 0.0;
        }
    }

    /**
     * 更新视频统计
     */
    private void updateVideoStatistics(Long videoId, UserBehavior.BehaviorType behaviorType) {
        switch (behaviorType) {
            case VIEW:
                videoRepository.findById(videoId).ifPresent(video -> {
                    video.setViewCount(video.getViewCount() + 1);
                    videoRepository.save(video);
                });
                break;
            case LIKE:
                videoRepository.findById(videoId).ifPresent(video -> {
                    video.setLikeCount(video.getLikeCount() + 1);
                    videoRepository.save(video);
                });
                break;
            case DISLIKE:
                videoRepository.findById(videoId).ifPresent(video -> {
                    video.setDislikeCount(video.getDislikeCount() + 1);
                    videoRepository.save(video);
                });
                break;
            case SHARE:
                videoRepository.findById(videoId).ifPresent(video -> {
                    video.setShareCount(video.getShareCount() + 1);
                    videoRepository.save(video);
                });
                break;
        }
    }
} 