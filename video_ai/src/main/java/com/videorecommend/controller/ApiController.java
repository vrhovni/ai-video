package com.videorecommend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * API根控制器
 *
 * @author VideoRecommend
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiController {
    /**
     * API根端点
     */
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getApiInfo() {
        Map<String, Object> apiInfo = new HashMap<>();
        apiInfo.put("name", "视频推荐系统API");
        apiInfo.put("version", "1.0");
        apiInfo.put("description", "视频推荐系统后端API服务");
        apiInfo.put("status", "running");

        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("videos", "/api/videos - 视频相关API");
        endpoints.put("users", "/api/users - 用户相关API");
        endpoints.put("recommendations", "/api/recommendations - 推荐相关API");

        apiInfo.put("endpoints", endpoints);

        return ResponseEntity.ok(apiInfo);
    }

    /**
     * 健康检查端点
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("message", "推荐系统运行正常");
        return ResponseEntity.ok(health);
    }

    /**
     * API文档端点
     */
    @GetMapping("/docs")
    public ResponseEntity<Map<String, Object>> getApiDocs() {
        Map<String, Object> docs = new HashMap<>();
        docs.put("title", "视频推荐系统API文档");
        docs.put("version", "1.0");

        Map<String, Object> videoApis = new HashMap<>();
        videoApis.put("获取公开视频", "GET /api/videos/public");
        videoApis.put("获取推荐视频", "GET /api/videos/featured");
        videoApis.put("根据ID获取视频", "GET /api/videos/{id}");
        videoApis.put("创建视频", "POST /api/videos");
        videoApis.put("更新视频", "PUT /api/videos/{id}");
        videoApis.put("删除视频", "DELETE /api/videos/{id}");
        videoApis.put("搜索视频", "GET /api/videos/search/title?title={title}");
        videoApis.put("获取热门视频", "GET /api/videos/popular");
        videoApis.put("获取最新视频", "GET /api/videos/latest");

        Map<String, Object> userApis = new HashMap<>();
        userApis.put("用户注册", "POST /api/users/register");
        userApis.put("根据ID获取用户", "GET /api/users/{id}");
        userApis.put("根据用户名获取用户", "GET /api/users/username/{username}");
        userApis.put("更新用户信息", "PUT /api/users/{id}");
        userApis.put("删除用户", "DELETE /api/users/{id}");
        userApis.put("获取活跃用户", "GET /api/users/active");

        Map<String, Object> recommendationApis = new HashMap<>();
        recommendationApis.put("获取个性化推荐", "GET /api/recommendations/personalized/{userId}");
        recommendationApis.put("获取协同过滤推荐", "GET /api/recommendations/collaborative/{userId}");
        recommendationApis.put("获取基于内容的推荐", "GET /api/recommendations/content-based/{userId}");
        recommendationApis.put("获取热门推荐", "GET /api/recommendations/popular");
        recommendationApis.put("获取最新推荐", "GET /api/recommendations/latest");
        recommendationApis.put("获取混合推荐", "GET /api/recommendations/hybrid/{userId}");
        recommendationApis.put("记录用户行为", "POST /api/recommendations/behavior");

        Map<String, Object> apis = new HashMap<>();
        apis.put("videos", videoApis);
        apis.put("users", userApis);
        apis.put("recommendations", recommendationApis);

        docs.put("apis", apis);

        return ResponseEntity.ok(docs);
    }
} 