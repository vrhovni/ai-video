package com.videorecommend.service;

import com.videorecommend.entity.Video;
import com.videorecommend.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 视频服务类
 * 
 * @author VideoRecommend
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class VideoService {

    private final VideoRepository videoRepository;

    /**
     * 创建视频
     */
    public Video createVideo(Video video) {
        video.setCreatedAt(LocalDateTime.now());
        video.setUpdatedAt(LocalDateTime.now());
        
        Video savedVideo = videoRepository.save(video);
        log.info("视频创建成功: {}", savedVideo.getTitle());
        return savedVideo;
    }

    /**
     * 根据ID查找视频
     */
    public Optional<Video> findById(Long id) {
        return videoRepository.findById(id);
    }

    /**
     * 更新视频信息
     */
    public Video updateVideo(Long videoId, Video videoDetails) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("视频不存在"));

        // 更新基本信息
        if (videoDetails.getTitle() != null) {
            video.setTitle(videoDetails.getTitle());
        }
        if (videoDetails.getDescription() != null) {
            video.setDescription(videoDetails.getDescription());
        }
        if (videoDetails.getThumbnailUrl() != null) {
            video.setThumbnailUrl(videoDetails.getThumbnailUrl());
        }
        if (videoDetails.getCategory() != null) {
            video.setCategory(videoDetails.getCategory());
        }
        if (videoDetails.getTags() != null) {
            video.setTags(videoDetails.getTags());
        }
        if (videoDetails.getIsPublic() != null) {
            video.setIsPublic(videoDetails.getIsPublic());
        }

        video.setUpdatedAt(LocalDateTime.now());
        return videoRepository.save(video);
    }

    /**
     * 删除视频
     */
    public void deleteVideo(Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("视频不存在"));

        videoRepository.delete(video);
        log.info("视频删除成功: {}", video.getTitle());
    }

    /**
     * 获取公开视频列表
     */
    public Page<Video> getPublicVideos(Pageable pageable) {
        log.info("开始查询公开视频，分页参数: {}", pageable);
        try {
            Page<Video> videos = videoRepository.findByIsPublicTrue(pageable);
            log.info("查询公开视频成功，总数: {}, 当前页: {}", videos.getTotalElements(), videos.getNumber());
            return videos;
        } catch (Exception e) {
            log.error("查询公开视频失败: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 获取推荐视频列表
     */
    public Page<Video> getFeaturedVideos(Pageable pageable) {
        return videoRepository.findByIsFeaturedTrue(pageable);
    }

    /**
     * 根据分类获取视频
     */
    public Page<Video> getVideosByCategory(Video.VideoCategory category, Pageable pageable) {
        return videoRepository.findByCategory(category, pageable);
    }

    /**
     * 根据上传者获取视频
     */
    public List<Video> getVideosByUploader(Long uploaderId) {
        return videoRepository.findByUploaderId(uploaderId);
    }

    /**
     * 搜索视频（按标题）
     */
    public Page<Video> searchVideosByTitle(String title, Pageable pageable) {
        return videoRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    /**
     * 搜索视频（按描述）
     */
    public Page<Video> searchVideosByDescription(String description, Pageable pageable) {
        return videoRepository.findByDescriptionContainingIgnoreCase(description, pageable);
    }

    /**
     * 根据标签搜索视频
     */
    public Page<Video> searchVideosByTag(String tag, Pageable pageable) {
        return videoRepository.findByTag(tag, pageable);
    }

    /**
     * 获取热门视频
     */
    public Page<Video> getPopularVideos(Pageable pageable) {
        return videoRepository.findPopularVideos(pageable);
    }

    /**
     * 获取最新视频
     */
    public Page<Video> getLatestVideos(Pageable pageable) {
        return videoRepository.findLatestVideos(pageable);
    }

    /**
     * 根据时间范围获取视频
     */
    public Page<Video> getVideosByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return videoRepository.findVideosByDateRange(startDate, endDate, pageable);
    }

    /**
     * 根据分类获取热门视频
     */
    public Page<Video> getPopularVideosByCategory(Video.VideoCategory category, Pageable pageable) {
        return videoRepository.findPopularVideosByCategory(category, pageable);
    }

    /**
     * 增加视频观看次数
     */
    public void incrementViewCount(Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("视频不存在"));
        
        video.setViewCount(video.getViewCount() + 1);
        videoRepository.save(video);
    }

    /**
     * 增加视频点赞次数
     */
    public void incrementLikeCount(Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("视频不存在"));
        
        video.setLikeCount(video.getLikeCount() + 1);
        videoRepository.save(video);
    }

    /**
     * 减少视频点赞次数
     */
    public void decrementLikeCount(Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("视频不存在"));
        
        if (video.getLikeCount() > 0) {
            video.setLikeCount(video.getLikeCount() - 1);
            videoRepository.save(video);
        }
    }

    /**
     * 增加视频点踩次数
     */
    public void incrementDislikeCount(Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("视频不存在"));
        
        video.setDislikeCount(video.getDislikeCount() + 1);
        videoRepository.save(video);
    }

    /**
     * 增加视频分享次数
     */
    public void incrementShareCount(Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("视频不存在"));
        
        video.setShareCount(video.getShareCount() + 1);
        videoRepository.save(video);
    }

    /**
     * 增加视频评论次数
     */
    public void incrementCommentCount(Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("视频不存在"));
        
        video.setCommentCount(video.getCommentCount() + 1);
        videoRepository.save(video);
    }

    /**
     * 设置视频为推荐
     */
    public void setFeatured(Long videoId, boolean featured) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("视频不存在"));
        
        video.setIsFeatured(featured);
        videoRepository.save(video);
    }

    /**
     * 统计公开视频数量
     */
    public long countPublicVideos() {
        return videoRepository.countPublicVideos();
    }

    /**
     * 统计分类视频数量
     */
    public long countVideosByCategory(Video.VideoCategory category) {
        return videoRepository.countVideosByCategory(category);
    }
} 