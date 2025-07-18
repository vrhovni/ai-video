package com.videorecommend.controller;

import com.videorecommend.entity.UserBehavior;
import com.videorecommend.entity.Video;
import com.videorecommend.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 推荐控制器
 * 
 * @author VideoRecommend
 */
@Slf4j
@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RecommendationController {

    private final RecommendationService recommendationService;

    /**
     * 获取个性化推荐
     */
    @GetMapping("/personalized/{userId}")
    public ResponseEntity<List<Video>> getPersonalizedRecommendations(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "20") int limit) {
        log.info("开始获取个性化推荐，userId={}, limit={}", userId, limit);
        try {
            List<Video> recommendations = recommendationService.getPersonalizedRecommendations(userId, limit);
            log.info("获取个性化推荐成功，共{}条", recommendations.size());
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            log.error("获取个性化推荐失败，userId={}", userId, e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取协同过滤推荐
     */
    @GetMapping("/collaborative/{userId}")
    public ResponseEntity<List<Video>> getCollaborativeFilteringRecommendations(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "20") int limit) {
        log.info("开始获取协同过滤推荐，userId={}, limit={}", userId, limit);
        try {
            List<Video> recommendations = recommendationService.getCollaborativeFilteringRecommendations(userId, limit);
            log.info("获取协同过滤推荐成功，共{}条", recommendations.size());
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            log.error("获取协同过滤推荐失败，userId={}", userId, e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取基于内容的推荐
     */
    @GetMapping("/content-based/{userId}")
    public ResponseEntity<List<Video>> getContentBasedRecommendations(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "20") int limit) {
        log.info("开始获取基于内容的推荐，userId={}, limit={}", userId, limit);
        try {
            List<Video> recommendations = recommendationService.getContentBasedRecommendations(userId, limit);
            log.info("获取基于内容的推荐成功，共{}条", recommendations.size());
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            log.error("获取基于内容的推荐失败，userId={}", userId, e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取热门视频推荐
     */
    @GetMapping("/popular")
    public ResponseEntity<List<Video>> getPopularVideos(
            @RequestParam(defaultValue = "20") int limit) {
        log.info("开始获取热门视频推荐，limit={}", limit);
        try {
            List<Video> popularVideos = recommendationService.getPopularVideos(limit);
            log.info("获取热门视频推荐成功，共{}条", popularVideos.size());
            return ResponseEntity.ok(popularVideos);
        } catch (Exception e) {
            log.error("获取热门视频推荐失败", e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取最新视频推荐
     */
    @GetMapping("/latest")
    public ResponseEntity<List<Video>> getLatestVideos(
            @RequestParam(defaultValue = "20") int limit) {
        log.info("开始获取最新视频推荐，limit={}", limit);
        try {
            List<Video> latestVideos = recommendationService.getLatestVideos(limit);
            log.info("获取最新视频推荐成功，共{}条", latestVideos.size());
            return ResponseEntity.ok(latestVideos);
        } catch (Exception e) {
            log.error("获取最新视频推荐失败", e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 记录用户行为
     */
    @PostMapping("/behavior")
    public ResponseEntity<?> recordUserBehavior(
            @RequestParam Long userId,
            @RequestParam Long videoId,
            @RequestParam UserBehavior.BehaviorType behaviorType) {
        try {
            recommendationService.recordUserBehavior(userId, videoId, behaviorType);
            return ResponseEntity.ok().body("用户行为记录成功");
        } catch (Exception e) {
            log.error("记录用户行为失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 获取混合推荐（结合多种推荐算法）
     */
    @GetMapping("/hybrid/{userId}")
    public ResponseEntity<List<Video>> getHybridRecommendations(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "20") int limit) {
        try {
            // 获取个性化推荐
            List<Video> personalized = recommendationService.getPersonalizedRecommendations(userId, limit / 2);
            
            // 获取协同过滤推荐
            List<Video> collaborative = recommendationService.getCollaborativeFilteringRecommendations(userId, limit / 2);
            
            // 合并推荐结果
            List<Video> hybridRecommendations = new java.util.ArrayList<>();
            hybridRecommendations.addAll(personalized);
            hybridRecommendations.addAll(collaborative);
            
            // 去重并限制数量
            return ResponseEntity.ok(hybridRecommendations.stream()
                    .distinct()
                    .limit(limit)
                    .toList());
        } catch (Exception e) {
            log.error("获取混合推荐失败: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取分类推荐
     */
    @GetMapping("/category/{userId}/{category}")
    public ResponseEntity<List<Video>> getCategoryRecommendations(
            @PathVariable Long userId,
            @PathVariable String category,
            @RequestParam(defaultValue = "20") int limit) {
        try {
            // 这里可以根据用户对特定分类的偏好来推荐
            // 暂时返回热门视频
            List<Video> recommendations = recommendationService.getPopularVideos(limit);
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            log.error("获取分类推荐失败: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取相似视频推荐
     */
    @GetMapping("/similar/{videoId}")
    public ResponseEntity<?> getSimilarVideos(
            @PathVariable Long videoId,
            @RequestParam(defaultValue = "10") int limit) {
        log.info("开始获取相似视频推荐，videoId={}, limit={}", videoId, limit);
        try {
            // 这里可以根据视频的标签、分类等特征来推荐相似视频
            // 暂时返回热门视频
            List<Video> recommendations = recommendationService.getPopularVideos(limit);
            log.info("获取相似视频推荐成功，共{}条", recommendations.size());
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            log.error("获取相似视频推荐失败，videoId={}, 错误信息: {}", videoId, e.getMessage(), e);
            return ResponseEntity.status(500).body("获取相似视频推荐失败: " + e.getMessage());
        }
    }
} 