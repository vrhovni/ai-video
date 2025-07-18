package com.videorecommend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    public Map<String, String> hello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello World!");
        response.put("status", "success");
        return response;
    }

    @GetMapping("/error-test")
    public Map<String, String> errorTest() {
        throw new RuntimeException("测试异常");
    }
} 