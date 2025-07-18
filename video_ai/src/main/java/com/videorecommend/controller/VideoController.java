package com.videorecommend.controller;

import com.videorecommend.entity.Video;
import com.videorecommend.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.videorecommend.repository.VideoRepository;
import com.videorecommend.dto.VideoCreateRequest;
import javax.validation.Valid;

/**
 * 视频控制器
 * 
 * @author VideoRecommend
 */
@Slf4j
@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VideoController {

    private final VideoService videoService;
    private final VideoRepository videoRepository;

    // 上传目录
    private static final String UPLOAD_DIR = "E:/video/videoUpload/";

    /**
     * 创建视频
     */
    @PostMapping
    public ResponseEntity<?> createVideo(@Valid @RequestBody VideoCreateRequest req) {
        try {
            Video video = new Video();
            video.setTitle(req.getTitle());
            video.setDescription(req.getDescription());
            
            // 如果videoUrl是相对路径，转换为完整URL
            String videoUrl = req.getVideoUrl();
            if (videoUrl != null && videoUrl.startsWith("/videos/files/")) {
                videoUrl = "http://localhost:8080" + videoUrl;
            }
            video.setVideoUrl(videoUrl);
            
            video.setThumbnailUrl(req.getThumbnailUrl());
            video.setDurationSeconds(req.getDurationSeconds());
            // 字符串转枚举
            video.setCategory(Video.VideoCategory.valueOf(req.getCategory()));
            video.setTags(req.getTags());
            video.setUploaderId(req.getUploaderId());
            video.setUploaderName(req.getUploaderName());
            video.setIsPublic(req.getIsPublic());
            video.setIsFeatured(req.getIsFeatured());
            video.setCreatedAt(LocalDateTime.now());
            video.setUpdatedAt(LocalDateTime.now());
            Video createdVideo = videoService.createVideo(video);
            return ResponseEntity.ok(createdVideo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("创建视频失败: " + e.getMessage());
        }
    }

    /**
     * 上传视频文件
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadVideo(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("文件为空");
        }
        try {
            // 保证目录存在
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();

            // 保存文件
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File dest = new File(UPLOAD_DIR + fileName);
            file.transferTo(dest);

            // 生成可访问URL
            String url = "/videos/files/" + fileName;
            return ResponseEntity.ok(url);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("上传失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取视频
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getVideoById(@PathVariable Long id) {
        log.info("开始获取视频详情，id={}", id);
        try {
        Optional<Video> video = videoService.findById(id);
        if (video.isPresent()) {
                Video videoData = video.get();
                
                // 确保视频URL是完整的
                String videoUrl = videoData.getVideoUrl();
                if (videoUrl != null && videoUrl.startsWith("/videos/files/")) {
                    videoUrl = "http://localhost:8080" + videoUrl;
                    videoData.setVideoUrl(videoUrl);
                }
                
                log.info("获取视频详情成功，title={}, videoUrl={}", videoData.getTitle(), videoData.getVideoUrl());
                return ResponseEntity.ok(videoData);
        } else {
                log.warn("视频不存在，id={}", id);
            return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("获取视频详情失败，id={}, 错误信息: {}", id, e.getMessage(), e);
            return ResponseEntity.status(500).body("获取视频详情失败: " + e.getMessage());
        }
    }

    /**
     * 更新视频信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateVideo(@PathVariable Long id, @RequestBody Video videoDetails) {
        try {
            Video updatedVideo = videoService.updateVideo(id, videoDetails);
            return ResponseEntity.ok(updatedVideo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 删除视频
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVideo(@PathVariable Long id) {
        try {
            videoService.deleteVideo(id);
            return ResponseEntity.ok().body("视频删除成功");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 获取公开视频列表
     */
    @GetMapping("/public")
    public ResponseEntity<?> getPublicVideos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("开始获取公开视频，page={}, size={}", page, size);
        try {
            // 先检查数据库连接
            log.info("检查数据库连接...");
            
            // 测试数据库连接
            try {
                long totalVideos = videoRepository.count();
                log.info("数据库连接正常，总视频数: {}", totalVideos);
            } catch (Exception dbError) {
                log.error("数据库连接失败: {}", dbError.getMessage(), dbError);
                return ResponseEntity.status(500).body("数据库连接失败: " + dbError.getMessage());
            }
            
        Pageable pageable = PageRequest.of(page, size);
            log.info("创建分页对象: {}", pageable);
            
        Page<Video> videos = videoService.getPublicVideos(pageable);
            log.info("获取公开视频成功，共{}条", videos.getTotalElements());
            
            // 打印前几条视频信息用于调试
            if (!videos.getContent().isEmpty()) {
                Video firstVideo = videos.getContent().get(0);
                log.info("第一条视频: id={}, title={}, category={}", 
                    firstVideo.getId(), firstVideo.getTitle(), firstVideo.getCategory());
            }
            
        return ResponseEntity.ok(videos);
        } catch (Exception e) {
            log.error("获取公开视频失败，错误信息: {}", e.getMessage(), e);
            // 打印完整的异常堆栈
            e.printStackTrace();
            // 返回更详细的错误信息
            String errorDetails = String.format("获取公开视频失败: %s\n堆栈: %s", 
                e.getMessage(), 
                java.util.Arrays.toString(e.getStackTrace()));
            return ResponseEntity.status(500).body(errorDetails);
        }
    }

    /**
     * 获取推荐视频列表
     */
    @GetMapping("/featured")
    public ResponseEntity<Page<Video>> getFeaturedVideos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("开始获取推荐视频，page={}, size={}", page, size);
        try {
        Pageable pageable = PageRequest.of(page, size);
        Page<Video> videos = videoService.getFeaturedVideos(pageable);
            log.info("获取推荐视频成功，共{}条", videos.getTotalElements());
        return ResponseEntity.ok(videos);
        } catch (Exception e) {
            log.error("获取推荐视频失败", e);
            throw e;
        }
    }

    /**
     * 根据分类获取视频
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<Video>> getVideosByCategory(
            @PathVariable Video.VideoCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("开始获取分类视频，category={}, page={}, size={}", category, page, size);
        try {
        Pageable pageable = PageRequest.of(page, size);
        Page<Video> videos = videoService.getVideosByCategory(category, pageable);
            log.info("获取分类视频成功，共{}条", videos.getTotalElements());
        return ResponseEntity.ok(videos);
        } catch (Exception e) {
            log.error("获取分类视频失败", e);
            throw e;
        }
    }

    /**
     * 根据上传者获取视频
     */
    @GetMapping("/uploader/{uploaderId}")
    public ResponseEntity<List<Video>> getVideosByUploader(@PathVariable Long uploaderId) {
        log.info("开始获取上传者视频，uploaderId={}", uploaderId);
        try {
        List<Video> videos = videoService.getVideosByUploader(uploaderId);
            log.info("获取上传者视频成功，共{}条", videos.size());
        return ResponseEntity.ok(videos);
        } catch (Exception e) {
            log.error("获取上传者视频失败", e);
            throw e;
        }
    }

    /**
     * 搜索视频（按标题）
     */
    @GetMapping("/search/title")
    public ResponseEntity<Page<Video>> searchVideosByTitle(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("开始按标题搜索视频，title={}, page={}, size={}", title, page, size);
        try {
        Pageable pageable = PageRequest.of(page, size);
        Page<Video> videos = videoService.searchVideosByTitle(title, pageable);
            log.info("按标题搜索视频成功，共{}条", videos.getTotalElements());
        return ResponseEntity.ok(videos);
        } catch (Exception e) {
            log.error("按标题搜索视频失败", e);
            throw e;
        }
    }

    /**
     * 搜索视频（按描述）
     */
    @GetMapping("/search/description")
    public ResponseEntity<Page<Video>> searchVideosByDescription(
            @RequestParam String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("开始按描述搜索视频，description={}, page={}, size={}", description, page, size);
        try {
        Pageable pageable = PageRequest.of(page, size);
        Page<Video> videos = videoService.searchVideosByDescription(description, pageable);
            log.info("按描述搜索视频成功，共{}条", videos.getTotalElements());
        return ResponseEntity.ok(videos);
        } catch (Exception e) {
            log.error("按描述搜索视频失败", e);
            throw e;
        }
    }

    /**
     * 根据标签搜索视频
     */
    @GetMapping("/search/tag")
    public ResponseEntity<Page<Video>> searchVideosByTag(
            @RequestParam String tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("开始按标签搜索视频，tag={}, page={}, size={}", tag, page, size);
        try {
        Pageable pageable = PageRequest.of(page, size);
        Page<Video> videos = videoService.searchVideosByTag(tag, pageable);
            log.info("按标签搜索视频成功，共{}条", videos.getTotalElements());
        return ResponseEntity.ok(videos);
        } catch (Exception e) {
            log.error("按标签搜索视频失败", e);
            throw e;
        }
    }

    /**
     * 获取热门视频
     */
    @GetMapping("/popular")
    public ResponseEntity<Page<Video>> getPopularVideos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("开始获取热门视频，page={}, size={}", page, size);
        try {
        Pageable pageable = PageRequest.of(page, size);
        Page<Video> videos = videoService.getPopularVideos(pageable);
            log.info("获取热门视频成功，共{}条", videos.getTotalElements());
        return ResponseEntity.ok(videos);
        } catch (Exception e) {
            log.error("获取热门视频失败", e);
            throw e;
        }
    }

    /**
     * 获取最新视频
     */
    @GetMapping("/latest")
    public ResponseEntity<Page<Video>> getLatestVideos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("开始获取最新视频，page={}, size={}", page, size);
        try {
        Pageable pageable = PageRequest.of(page, size);
        Page<Video> videos = videoService.getLatestVideos(pageable);
            log.info("获取最新视频成功，共{}条", videos.getTotalElements());
        return ResponseEntity.ok(videos);
        } catch (Exception e) {
            log.error("获取最新视频失败", e);
            throw e;
        }
    }

    /**
     * 根据时间范围获取视频
     */
    @GetMapping("/date-range")
    public ResponseEntity<Page<Video>> getVideosByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        Page<Video> videos = videoService.getVideosByDateRange(start, end, pageable);
        return ResponseEntity.ok(videos);
    }

    /**
     * 根据分类获取热门视频
     */
    @GetMapping("/popular/category/{category}")
    public ResponseEntity<Page<Video>> getPopularVideosByCategory(
            @PathVariable Video.VideoCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Video> videos = videoService.getPopularVideosByCategory(category, pageable);
        return ResponseEntity.ok(videos);
    }

    /**
     * 增加视频观看次数
     */
    @PostMapping("/{id}/view")
    public ResponseEntity<?> incrementViewCount(@PathVariable Long id) {
        try {
            videoService.incrementViewCount(id);
            return ResponseEntity.ok().body("观看次数增加成功");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 增加视频点赞次数
     */
    @PostMapping("/{id}/like")
    public ResponseEntity<?> incrementLikeCount(@PathVariable Long id) {
        try {
            videoService.incrementLikeCount(id);
            return ResponseEntity.ok().body("点赞成功");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 减少视频点赞次数
     */
    @DeleteMapping("/{id}/like")
    public ResponseEntity<?> decrementLikeCount(@PathVariable Long id) {
        try {
            videoService.decrementLikeCount(id);
            return ResponseEntity.ok().body("取消点赞成功");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 增加视频点踩次数
     */
    @PostMapping("/{id}/dislike")
    public ResponseEntity<?> incrementDislikeCount(@PathVariable Long id) {
        try {
            videoService.incrementDislikeCount(id);
            return ResponseEntity.ok().body("点踩成功");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 增加视频分享次数
     */
    @PostMapping("/{id}/share")
    public ResponseEntity<?> incrementShareCount(@PathVariable Long id) {
        try {
            videoService.incrementShareCount(id);
            return ResponseEntity.ok().body("分享成功");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 增加视频评论次数
     */
    @PostMapping("/{id}/comment")
    public ResponseEntity<?> incrementCommentCount(@PathVariable Long id) {
        try {
            videoService.incrementCommentCount(id);
            return ResponseEntity.ok().body("评论成功");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 设置视频为推荐
     */
    @PutMapping("/{id}/featured")
    public ResponseEntity<?> setFeatured(@PathVariable Long id, @RequestParam boolean featured) {
        try {
            videoService.setFeatured(id, featured);
            return ResponseEntity.ok().body("设置推荐状态成功");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 获取公开视频数量
     */
    @GetMapping("/count/public")
    public ResponseEntity<Long> getPublicVideoCount() {
        long count = videoService.countPublicVideos();
        return ResponseEntity.ok(count);
    }

    /**
     * 获取分类视频数量
     */
    @GetMapping("/count/category/{category}")
    public ResponseEntity<Long> getVideoCountByCategory(@PathVariable Video.VideoCategory category) {
        long count = videoService.countVideosByCategory(category);
        return ResponseEntity.ok(count);
    }
} 