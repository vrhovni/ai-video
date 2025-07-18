/*
 Navicat Premium Data Transfer

 Source Server         : video
 Source Server Type    : MySQL
 Source Server Version : 80035
 Source Host           : localhost:3306
 Source Schema         : video_recommend

 Target Server Type    : MySQL
 Target Server Version : 80035
 File Encoding         : 65001

 Date: 17/07/2025 12:05:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_behaviors
-- ----------------------------
DROP TABLE IF EXISTS `user_behaviors`;
CREATE TABLE `user_behaviors`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `video_id` bigint NOT NULL,
  `behavior_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `duration_watched` int NULL DEFAULT NULL,
  `watch_progress` double NULL DEFAULT NULL,
  `interaction_time` int NULL DEFAULT NULL,
  `device_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `ip_address` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_video_id`(`video_id` ASC) USING BTREE,
  INDEX `idx_behavior_type`(`behavior_type` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE,
  INDEX `idx_user_behaviors_user_video`(`user_id` ASC, `video_id` ASC) USING BTREE,
  INDEX `idx_user_behaviors_type_time`(`behavior_type` ASC, `created_at` ASC) USING BTREE,
  CONSTRAINT `FK5iykuyxnrgc9fcn737q4acv06` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKr32yp17jvdmjhydbx8v8guphc` FOREIGN KEY (`video_id`) REFERENCES `videos` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_behaviors
-- ----------------------------

-- ----------------------------
-- Table structure for user_preferences
-- ----------------------------
DROP TABLE IF EXISTS `user_preferences`;
CREATE TABLE `user_preferences`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `preference_score` double NULL DEFAULT 0,
  `interaction_count` int NULL DEFAULT 0,
  `last_interaction_at` datetime NULL DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_category`(`user_id` ASC, `category` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_category`(`category` ASC) USING BTREE,
  INDEX `idx_user_preferences_score`(`preference_score` DESC) USING BTREE,
  CONSTRAINT `FKepakpib0qnm82vmaiismkqf88` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_preferences
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `nick_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'USER',
  `is_active` tinyint(1) NULL DEFAULT 1,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `uk_email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'admin', '111111Zz', 'admin@example.com', 'admin', NULL, 'ADMIN', 1, '2025-07-13 17:21:04', '2025-07-13 17:21:04');
INSERT INTO `users` VALUES (2, 'user1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'user1@example.com', 'user1', NULL, 'USER', 1, '2025-07-13 17:21:04', '2025-07-13 17:21:04');
INSERT INTO `users` VALUES (3, 'user2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'user2@example.com', 'user2', NULL, 'USER', 1, '2025-07-13 17:21:04', '2025-07-13 17:21:04');
INSERT INTO `users` VALUES (4, 'user3', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'user3@example.com', 'user3', NULL, 'USER', 1, '2025-07-13 17:21:04', '2025-07-13 17:21:04');
INSERT INTO `users` VALUES (5, 'user4', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'user4@example.com', 'user4', NULL, 'USER', 1, '2025-07-13 17:21:04', '2025-07-13 17:21:04');
INSERT INTO `users` VALUES (9, '111111', '$2a$10$/8mtvTkhA9eZgzhLzgfGTeOVHzx1wnMnUsn8ofTb3XVcbvEItuw5y', '1320497010@qq.com', '111111', NULL, 'USER', 1, '2025-07-17 11:40:14', '2025-07-17 11:40:14');

-- ----------------------------
-- Table structure for video_tags
-- ----------------------------
DROP TABLE IF EXISTS `video_tags`;
CREATE TABLE `video_tags`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `video_id` bigint NOT NULL,
  `tag_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `tag_weight` double NULL DEFAULT 1,
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_video_tag`(`video_id` ASC, `tag_name` ASC) USING BTREE,
  INDEX `idx_video_id`(`video_id` ASC) USING BTREE,
  INDEX `idx_tag_name`(`tag_name` ASC) USING BTREE,
  INDEX `idx_video_tags_name`(`tag_name` ASC) USING BTREE,
  CONSTRAINT `FKpr6ks7ia3ilx9ec2mmwb82lb6` FOREIGN KEY (`video_id`) REFERENCES `videos` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of video_tags
-- ----------------------------
INSERT INTO `video_tags` VALUES (1, 1, '编程', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (2, 1, 'Java', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (3, 1, 'Spring Boot', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (4, 2, 'Python', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (5, 2, '机器学习', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (6, 2, '人工智能', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (7, 3, '编程', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (8, 3, 'JavaScript', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (9, 3, '前端', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (10, 4, '音乐', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (11, 4, '混音', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (12, 4, '制作', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (13, 5, '舞蹈', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (14, 5, '街舞', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (15, 5, '教学', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (16, 6, '美食', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (17, 6, '烹饪', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (18, 6, '家常菜', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (19, 7, '旅行', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (20, 7, '摄影', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (21, 7, '技巧', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (22, 8, '健身', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (23, 8, '训练', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (24, 8, '计划', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (25, 9, '篮球', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (26, 9, '技巧', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (27, 9, '训练', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (28, 10, '科技', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (29, 10, '新闻', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (30, 10, '趋势', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (31, 11, '时尚', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (32, 11, '搭配', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (33, 11, '穿搭', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (34, 12, '生活', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (35, 12, '技巧', 1, '2025-07-13 17:21:04');
INSERT INTO `video_tags` VALUES (36, 12, '窍门', 1, '2025-07-13 17:21:04');

-- ----------------------------
-- Table structure for videos
-- ----------------------------
DROP TABLE IF EXISTS `videos`;
CREATE TABLE `videos`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `video_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `thumbnail_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `duration_seconds` int NULL DEFAULT NULL,
  `view_count` bigint NULL DEFAULT 0,
  `like_count` bigint NULL DEFAULT 0,
  `dislike_count` bigint NULL DEFAULT 0,
  `share_count` bigint NULL DEFAULT 0,
  `comment_count` bigint NULL DEFAULT 0,
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `tags` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `uploader_id` bigint NULL DEFAULT NULL,
  `uploader_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `is_public` tinyint(1) NULL DEFAULT 1,
  `is_featured` tinyint(1) NULL DEFAULT 0,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category`(`category` ASC) USING BTREE,
  INDEX `idx_uploader_id`(`uploader_id` ASC) USING BTREE,
  INDEX `idx_is_public`(`is_public` ASC) USING BTREE,
  INDEX `idx_is_featured`(`is_featured` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE,
  INDEX `idx_videos_category_public`(`category` ASC, `is_public` ASC) USING BTREE,
  INDEX `idx_videos_view_count`(`view_count` DESC) USING BTREE,
  INDEX `idx_videos_like_count`(`like_count` DESC) USING BTREE,
  INDEX `idx_videos_created_at`(`created_at` DESC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of videos
-- ----------------------------
INSERT INTO `videos` VALUES (1, 'Spring Boot 入门教程', '从零开始学习Spring Boot框架', 'https://example.com/videos/spring-boot-intro.mp4', 'https://example.com/thumbnails/spring-boot-intro.jpg', 1800, 0, 0, 0, 0, 0, 'TECHNOLOGY', '编程,Java,Spring Boot', 1, 'admin', 1, 0, '2025-07-13 17:21:04', '2025-07-13 17:21:04');
INSERT INTO `videos` VALUES (2, 'Python机器学习实战', '使用Python进行机器学习项目开发', 'https://example.com/videos/python-ml.mp4', 'https://example.com/thumbnails/python-ml.jpg', 2400, 0, 0, 0, 0, 0, 'TECHNOLOGY', 'Python,机器学习,人工智能', 1, 'admin', 1, 0, '2025-07-13 17:21:04', '2025-07-13 17:21:04');
INSERT INTO `videos` VALUES (3, 'JavaScript高级教程', '深入学习JavaScript高级特性', 'https://example.com/videos/js-advanced.mp4', 'https://example.com/thumbnails/js-advanced.jpg', 1200, 0, 0, 0, 0, 0, 'TECHNOLOGY', '编程,JavaScript,前端', 1, 'admin', 1, 0, '2025-07-13 17:21:04', '2025-07-13 17:21:04');
INSERT INTO `videos` VALUES (4, '流行音乐混音教程', '学习如何制作专业的音乐混音', 'https://example.com/videos/music-mixing.mp4', 'https://example.com/thumbnails/music-mixing.jpg', 900, 0, 0, 0, 0, 0, 'MUSIC', '音乐,混音,制作', 2, 'user1', 1, 0, '2025-07-13 17:21:04', '2025-07-13 17:21:04');
INSERT INTO `videos` VALUES (5, '街舞基础教学', '从零开始学习街舞基础动作', 'https://example.com/videos/street-dance.mp4', 'https://example.com/thumbnails/street-dance.jpg', 600, 0, 0, 0, 0, 0, 'ENTERTAINMENT', '舞蹈,街舞,教学', 2, 'user1', 1, 0, '2025-07-13 17:21:04', '2025-07-13 17:21:04');
INSERT INTO `videos` VALUES (6, '家常菜制作技巧', '学习制作美味可口的家常菜', 'https://example.com/videos/home-cooking.mp4', 'https://example.com/thumbnails/home-cooking.jpg', 1500, 0, 0, 0, 0, 0, 'FOOD', '美食,烹饪,家常菜', 3, 'user2', 1, 0, '2025-07-13 17:21:04', '2025-07-13 17:21:04');
INSERT INTO `videos` VALUES (7, '旅行摄影技巧', '如何拍出令人惊艳的旅行照片', 'https://example.com/videos/travel-photography.mp4', 'https://example.com/thumbnails/travel-photography.jpg', 1800, 0, 0, 0, 0, 0, 'TRAVEL', '旅行,摄影,技巧', 3, 'user2', 1, 0, '2025-07-13 17:21:04', '2025-07-13 17:21:04');
INSERT INTO `videos` VALUES (8, '健身训练计划', '制定适合自己的健身训练计划', 'https://example.com/videos/fitness-training.mp4', 'https://example.com/thumbnails/fitness-training.jpg', 1200, 0, 0, 0, 0, 0, 'SPORTS', '健身,训练,计划', 4, 'user3', 1, 0, '2025-07-13 17:21:04', '2025-07-13 17:21:04');
INSERT INTO `videos` VALUES (9, '篮球技巧教学', '提高篮球技术的专业训练方法', 'https://example.com/videos/basketball-skills.mp4', 'https://example.com/thumbnails/basketball-skills.jpg', 900, 0, 0, 0, 0, 0, 'SPORTS', '篮球,技巧,训练', 4, 'user3', 1, 0, '2025-07-13 17:21:04', '2025-07-13 17:21:04');
INSERT INTO `videos` VALUES (10, '科技新闻周报', '本周最新科技动态和趋势分析', 'https://example.com/videos/tech-news.mp4', 'https://example.com/thumbnails/tech-news.jpg', 600, 0, 0, 0, 0, 0, 'NEWS', '科技,新闻,趋势', 5, 'user4', 1, 0, '2025-07-13 17:21:04', '2025-07-13 17:21:04');
INSERT INTO `videos` VALUES (11, '时尚搭配指南', '学习如何搭配出时尚的穿搭', 'https://example.com/videos/fashion-guide.mp4', 'https://example.com/thumbnails/fashion-guide.jpg', 1200, 0, 0, 0, 0, 0, 'FASHION', '时尚,搭配,穿搭', 5, 'user4', 1, 0, '2025-07-13 17:21:04', '2025-07-13 17:21:04');
INSERT INTO `videos` VALUES (12, '生活小技巧分享', '实用的生活小技巧和窍门', 'https://example.com/videos/life-tips.mp4', 'https://example.com/thumbnails/life-tips.jpg', 800, 0, 0, 0, 0, 0, 'LIFESTYLE', '生活,技巧,窍门', 1, 'admin', 1, 0, '2025-07-13 17:21:04', '2025-07-13 17:21:04');

SET FOREIGN_KEY_CHECKS = 1;
