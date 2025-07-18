-- 视频推荐系统数据库创建脚本
-- 数据库名称: video_recommend
-- 字符集: utf8mb4
-- 排序规则: utf8mb4_unicode_ci

-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS `video_recommend` 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- 2. 使用数据库
USE `video_recommend`;

-- 3. 创建用户表
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `nick_name` varchar(50) DEFAULT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  `role` varchar(20) NOT NULL DEFAULT 'USER',
  `is_active` tinyint(1) DEFAULT '1',
  `created_at` datetime NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. 创建视频表
CREATE TABLE IF NOT EXISTS `videos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `description` text,
  `video_url` varchar(500) NOT NULL,
  `thumbnail_url` varchar(500) DEFAULT NULL,
  `duration_seconds` int DEFAULT NULL,
  `view_count` bigint DEFAULT '0',
  `like_count` bigint DEFAULT '0',
  `dislike_count` bigint DEFAULT '0',
  `share_count` bigint DEFAULT '0',
  `comment_count` bigint DEFAULT '0',
  `category` varchar(50) NOT NULL,
  `tags` text,
  `uploader_id` bigint DEFAULT NULL,
  `uploader_name` varchar(50) DEFAULT NULL,
  `is_public` tinyint(1) DEFAULT '1',
  `is_featured` tinyint(1) DEFAULT '0',
  `created_at` datetime NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_category` (`category`),
  KEY `idx_uploader_id` (`uploader_id`),
  KEY `idx_is_public` (`is_public`),
  KEY `idx_is_featured` (`is_featured`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. 创建用户行为表
CREATE TABLE IF NOT EXISTS `user_behaviors` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `video_id` bigint NOT NULL,
  `behavior_type` varchar(50) NOT NULL,
  `duration_watched` int DEFAULT NULL,
  `watch_progress` double DEFAULT NULL,
  `interaction_time` int DEFAULT NULL,
  `device_info` varchar(255) DEFAULT NULL,
  `ip_address` varchar(45) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_video_id` (`video_id`),
  KEY `idx_behavior_type` (`behavior_type`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. 创建用户偏好表
CREATE TABLE IF NOT EXISTS `user_preferences` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `category` varchar(50) NOT NULL,
  `preference_score` double DEFAULT '0',
  `interaction_count` int DEFAULT '0',
  `last_interaction_at` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_category` (`user_id`, `category`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. 创建视频标签表
CREATE TABLE IF NOT EXISTS `video_tags` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `video_id` bigint NOT NULL,
  `tag_name` varchar(100) NOT NULL,
  `tag_weight` double DEFAULT '1',
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_video_id` (`video_id`),
  KEY `idx_tag_name` (`tag_name`),
  UNIQUE KEY `uk_video_tag` (`video_id`, `tag_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8. 插入初始测试数据

-- 插入测试用户
INSERT INTO `users` (`username`, `password`, `email`, `nick_name`, `role`, `is_active`, `created_at`, `updated_at`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'admin@example.com', 'admin', 'ADMIN', 1, NOW(), NOW()),
('user1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'user1@example.com', 'user1', 'USER', 1, NOW(), NOW()),
('user2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'user2@example.com', 'user2', 'USER', 1, NOW(), NOW()),
('user3', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'user3@example.com', 'user3', 'USER', 1, NOW(), NOW()),
('user4', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'user4@example.com', 'user4', 'USER', 1, NOW(), NOW());

-- 插入测试视频
INSERT INTO `videos` (`title`, `description`, `video_url`, `thumbnail_url`, `duration_seconds`, `category`, `tags`, `uploader_id`, `uploader_name`, `is_public`, `is_featured`, `view_count`, `like_count`, `dislike_count`, `share_count`, `comment_count`, `created_at`, `updated_at`) VALUES
('Spring Boot 入门教程', '从零开始学习Spring Boot框架', 'https://example.com/videos/spring-boot-intro.mp4', 'https://example.com/thumbnails/spring-boot-intro.jpg', 1800, 'TECHNOLOGY', '编程,Java,Spring Boot', 1, 'admin', 1, 0, 0, 0, 0, 0, 0, NOW(), NOW()),
('Python机器学习实战', '使用Python进行机器学习项目开发', 'https://example.com/videos/python-ml.mp4', 'https://example.com/thumbnails/python-ml.jpg', 2400, 'TECHNOLOGY', 'Python,机器学习,人工智能', 1, 'admin', 1, 0, 0, 0, 0, 0, 0, NOW(), NOW()),
('JavaScript高级教程', '深入学习JavaScript高级特性', 'https://example.com/videos/js-advanced.mp4', 'https://example.com/thumbnails/js-advanced.jpg', 1200, 'TECHNOLOGY', '编程,JavaScript,前端', 1, 'admin', 1, 0, 0, 0, 0, 0, 0, NOW(), NOW()),
('流行音乐混音教程', '学习如何制作专业的音乐混音', 'https://example.com/videos/music-mixing.mp4', 'https://example.com/thumbnails/music-mixing.jpg', 900, 'MUSIC', '音乐,混音,制作', 2, 'user1', 1, 0, 0, 0, 0, 0, 0, NOW(), NOW()),
('街舞基础教学', '从零开始学习街舞基础动作', 'https://example.com/videos/street-dance.mp4', 'https://example.com/thumbnails/street-dance.jpg', 600, 'ENTERTAINMENT', '舞蹈,街舞,教学', 2, 'user1', 1, 0, 0, 0, 0, 0, 0, NOW(), NOW()),
('家常菜制作技巧', '学习制作美味可口的家常菜', 'https://example.com/videos/home-cooking.mp4', 'https://example.com/thumbnails/home-cooking.jpg', 1500, 'FOOD', '美食,烹饪,家常菜', 3, 'user2', 1, 0, 0, 0, 0, 0, 0, NOW(), NOW()),
('旅行摄影技巧', '如何拍出令人惊艳的旅行照片', 'https://example.com/videos/travel-photography.mp4', 'https://example.com/thumbnails/travel-photography.jpg', 1800, 'TRAVEL', '旅行,摄影,技巧', 3, 'user2', 1, 0, 0, 0, 0, 0, 0, NOW(), NOW()),
('健身训练计划', '制定适合自己的健身训练计划', 'https://example.com/videos/fitness-training.mp4', 'https://example.com/thumbnails/fitness-training.jpg', 1200, 'SPORTS', '健身,训练,计划', 4, 'user3', 1, 0, 0, 0, 0, 0, 0, NOW(), NOW()),
('篮球技巧教学', '提高篮球技术的专业训练方法', 'https://example.com/videos/basketball-skills.mp4', 'https://example.com/thumbnails/basketball-skills.jpg', 900, 'SPORTS', '篮球,技巧,训练', 4, 'user3', 1, 0, 0, 0, 0, 0, 0, NOW(), NOW()),
('科技新闻周报', '本周最新科技动态和趋势分析', 'https://example.com/videos/tech-news.mp4', 'https://example.com/thumbnails/tech-news.jpg', 600, 'NEWS', '科技,新闻,趋势', 5, 'user4', 1, 0, 0, 0, 0, 0, 0, NOW(), NOW()),
('时尚搭配指南', '学习如何搭配出时尚的穿搭', 'https://example.com/videos/fashion-guide.mp4', 'https://example.com/thumbnails/fashion-guide.jpg', 1200, 'FASHION', '时尚,搭配,穿搭', 5, 'user4', 1, 0, 0, 0, 0, 0, 0, NOW(), NOW()),
('生活小技巧分享', '实用的生活小技巧和窍门', 'https://example.com/videos/life-tips.mp4', 'https://example.com/thumbnails/life-tips.jpg', 800, 'LIFESTYLE', '生活,技巧,窍门', 1, 'admin', 1, 0, 0, 0, 0, 0, 0, NOW(), NOW());

-- 插入视频标签
INSERT INTO `video_tags` (`video_id`, `tag_name`, `tag_weight`, `created_at`) VALUES
(1, '编程', 1.0, NOW()),
(1, 'Java', 1.0, NOW()),
(1, 'Spring Boot', 1.0, NOW()),
(2, 'Python', 1.0, NOW()),
(2, '机器学习', 1.0, NOW()),
(2, '人工智能', 1.0, NOW()),
(3, '编程', 1.0, NOW()),
(3, 'JavaScript', 1.0, NOW()),
(3, '前端', 1.0, NOW()),
(4, '音乐', 1.0, NOW()),
(4, '混音', 1.0, NOW()),
(4, '制作', 1.0, NOW()),
(5, '舞蹈', 1.0, NOW()),
(5, '街舞', 1.0, NOW()),
(5, '教学', 1.0, NOW()),
(6, '美食', 1.0, NOW()),
(6, '烹饪', 1.0, NOW()),
(6, '家常菜', 1.0, NOW()),
(7, '旅行', 1.0, NOW()),
(7, '摄影', 1.0, NOW()),
(7, '技巧', 1.0, NOW()),
(8, '健身', 1.0, NOW()),
(8, '训练', 1.0, NOW()),
(8, '计划', 1.0, NOW()),
(9, '篮球', 1.0, NOW()),
(9, '技巧', 1.0, NOW()),
(9, '训练', 1.0, NOW()),
(10, '科技', 1.0, NOW()),
(10, '新闻', 1.0, NOW()),
(10, '趋势', 1.0, NOW()),
(11, '时尚', 1.0, NOW()),
(11, '搭配', 1.0, NOW()),
(11, '穿搭', 1.0, NOW()),
(12, '生活', 1.0, NOW()),
(12, '技巧', 1.0, NOW()),
(12, '窍门', 1.0, NOW());

-- 9. 创建索引优化查询性能
CREATE INDEX `idx_videos_category_public` ON `videos` (`category`, `is_public`);
CREATE INDEX `idx_videos_view_count` ON `videos` (`view_count` DESC);
CREATE INDEX `idx_videos_like_count` ON `videos` (`like_count` DESC);
CREATE INDEX `idx_videos_created_at` ON `videos` (`created_at` DESC);
CREATE INDEX `idx_user_behaviors_user_video` ON `user_behaviors` (`user_id`, `video_id`);
CREATE INDEX `idx_user_behaviors_type_time` ON `user_behaviors` (`behavior_type`, `created_at`);
CREATE INDEX `idx_user_preferences_score` ON `user_preferences` (`preference_score` DESC);
CREATE INDEX `idx_video_tags_name` ON `video_tags` (`tag_name`);

-- 10. 显示创建结果
SELECT 'Database created successfully!' as message;
SELECT COUNT(*) as user_count FROM users;
SELECT COUNT(*) as video_count FROM videos;
SELECT COUNT(*) as tag_count FROM video_tags; 