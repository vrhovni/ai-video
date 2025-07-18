package com.videorecommend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 视频推荐系统主应用程序类
 * 
 * @author VideoRecommend
 * @version 1.0.0
 */
@SpringBootApplication
@EnableScheduling
public class VideoRecommendApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoRecommendApplication.class, args);
        System.out.println("=== 视频推荐系统启动成功 ===");
        System.out.println("=== 访问地址: http://localhost:8080/api ===");
    }
} 