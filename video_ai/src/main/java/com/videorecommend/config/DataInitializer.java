package com.videorecommend.config;

import com.videorecommend.entity.User;
import com.videorecommend.entity.Video;
import com.videorecommend.entity.VideoTag;
import com.videorecommend.repository.UserRepository;
import com.videorecommend.repository.VideoRepository;
import com.videorecommend.repository.VideoTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 数据初始化配置类
 * 在应用启动时自动添加测试数据
 * 
 * @author VideoRecommend
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final VideoTagRepository videoTagRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("开始初始化测试数据...");
        
        // 初始化用户数据
        initializeUsers();
        
        // 初始化视频标签
        initializeVideoTags();
        
        // 初始化视频数据
        initializeVideos();
        
        log.info("测试数据初始化完成！");
    }

    /**
     * 初始化用户数据
     */
    private void initializeUsers() {
        if (userRepository.count() > 0) {
            log.info("用户数据已存在，跳过初始化");
            return;
        }

        List<User> users = Arrays.asList(
            createUser("admin", "admin@example.com", "admin123", User.UserRole.ADMIN),
            createUser("user1", "user1@example.com", "password123", User.UserRole.USER),
            createUser("user2", "user2@example.com", "password123", User.UserRole.USER),
            createUser("user3", "user3@example.com", "password123", User.UserRole.USER),
            createUser("user4", "user4@example.com", "password123", User.UserRole.USER)
        );

        userRepository.saveAll(users);
        log.info("初始化了 {} 个用户", users.size());
    }

    /**
     * 创建用户
     */
    private User createUser(String username, String email, String password, User.UserRole role) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setNickName(username);
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }

    /**
     * 初始化视频标签
     */
    private void initializeVideoTags() {
        if (videoTagRepository.count() > 0) {
            log.info("视频标签数据已存在，跳过初始化");
            return;
        }

        // 由于VideoTag需要关联到具体的视频，这里暂时跳过标签初始化
        // 标签将在视频创建时自动生成
        log.info("视频标签将在视频创建时自动生成");
    }

    /**
     * 初始化视频数据
     */
    private void initializeVideos() {
        if (videoRepository.count() > 0) {
            log.info("视频数据已存在，跳过初始化");
            return;
        }

        List<Video> videos = Arrays.asList(
            createVideo("Spring Boot 入门教程", "从零开始学习Spring Boot框架", 
                "https://example.com/videos/spring-boot-intro.mp4",
                "https://example.com/thumbnails/spring-boot-intro.jpg",
                1800, Video.VideoCategory.TECHNOLOGY, "编程,Java,Spring Boot", 1L, "admin"),
            
            createVideo("Python机器学习实战", "使用Python进行机器学习项目开发", 
                "https://example.com/videos/python-ml.mp4",
                "https://example.com/thumbnails/python-ml.jpg",
                2400, Video.VideoCategory.TECHNOLOGY, "Python,机器学习,人工智能", 1L, "admin"),
            
            createVideo("JavaScript高级教程", "深入学习JavaScript高级特性", 
                "https://example.com/videos/js-advanced.mp4",
                "https://example.com/thumbnails/js-advanced.jpg",
                1200, Video.VideoCategory.TECHNOLOGY, "编程,JavaScript,前端", 1L, "admin"),
            
            createVideo("流行音乐混音教程", "学习如何制作专业的音乐混音", 
                "https://example.com/videos/music-mixing.mp4",
                "https://example.com/thumbnails/music-mixing.jpg",
                900, Video.VideoCategory.MUSIC, "音乐,混音,制作", 2L, "user1"),
            
            createVideo("街舞基础教学", "从零开始学习街舞基础动作", 
                "https://example.com/videos/street-dance.mp4",
                "https://example.com/thumbnails/street-dance.jpg",
                600, Video.VideoCategory.ENTERTAINMENT, "舞蹈,街舞,教学", 2L, "user1"),
            
            createVideo("家常菜制作技巧", "学习制作美味可口的家常菜", 
                "https://example.com/videos/home-cooking.mp4",
                "https://example.com/thumbnails/home-cooking.jpg",
                1500, Video.VideoCategory.FOOD, "美食,烹饪,家常菜", 3L, "user2"),
            
            createVideo("旅行摄影技巧", "如何拍出令人惊艳的旅行照片", 
                "https://example.com/videos/travel-photography.mp4",
                "https://example.com/thumbnails/travel-photography.jpg",
                1800, Video.VideoCategory.TRAVEL, "旅行,摄影,技巧", 3L, "user2"),
            
            createVideo("健身训练计划", "制定适合自己的健身训练计划", 
                "https://example.com/videos/fitness-training.mp4",
                "https://example.com/thumbnails/fitness-training.jpg",
                1200, Video.VideoCategory.SPORTS, "健身,训练,计划", 4L, "user3"),
            
            createVideo("篮球技巧教学", "提高篮球技术的专业训练方法", 
                "https://example.com/videos/basketball-skills.mp4",
                "https://example.com/thumbnails/basketball-skills.jpg",
                900, Video.VideoCategory.SPORTS, "篮球,技巧,训练", 4L, "user3"),
            
            createVideo("科技新闻周报", "本周最新科技动态和趋势分析", 
                "https://example.com/videos/tech-news.mp4",
                "https://example.com/thumbnails/tech-news.jpg",
                600, Video.VideoCategory.NEWS, "科技,新闻,趋势", 5L, "user4"),
            
            createVideo("时尚搭配指南", "学习如何搭配出时尚的穿搭", 
                "https://example.com/videos/fashion-guide.mp4",
                "https://example.com/thumbnails/fashion-guide.jpg",
                1200, Video.VideoCategory.FASHION, "时尚,搭配,穿搭", 5L, "user4"),
            
            createVideo("生活小技巧分享", "实用的生活小技巧和窍门", 
                "https://example.com/videos/life-tips.mp4",
                "https://example.com/thumbnails/life-tips.jpg",
                800, Video.VideoCategory.LIFESTYLE, "生活,技巧,窍门", 1L, "admin")
        );

        videoRepository.saveAll(videos);
        log.info("初始化了 {} 个视频", videos.size());
    }

    /**
     * 创建视频
     */
    private Video createVideo(String title, String description, String videoUrl, 
                             String thumbnailUrl, Integer durationSeconds, 
                             Video.VideoCategory category, String tags, 
                             Long uploaderId, String uploaderName) {
        Video video = new Video();
        video.setTitle(title);
        video.setDescription(description);
        video.setVideoUrl(videoUrl);
        video.setThumbnailUrl(thumbnailUrl);
        video.setDurationSeconds(durationSeconds);
        video.setCategory(category);
        video.setTags(tags);
        video.setUploaderId(uploaderId);
        video.setUploaderName(uploaderName);
        video.setIsPublic(true);
        video.setIsFeatured(false);
        video.setViewCount(0L);
        video.setLikeCount(0L);
        video.setDislikeCount(0L);
        video.setShareCount(0L);
        video.setCommentCount(0L);
        video.setCreatedAt(LocalDateTime.now());
        video.setUpdatedAt(LocalDateTime.now());
        return video;
    }
} 