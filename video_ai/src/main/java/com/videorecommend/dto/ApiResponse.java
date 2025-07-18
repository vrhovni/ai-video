package com.videorecommend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API响应DTO
 * 
 * @author VideoRecommend
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    
    private boolean success;
    private String message;
    private T data;
    private long timestamp;

// 泛型方法，返回一个ApiResponse对象，参数为T类型
    public static <T> ApiResponse<T> success(T data) {
        // 创建一个ApiResponse对象，参数为true、"操作成功"、data、当前时间戳
        return new ApiResponse<>(true, "操作成功", data, System.currentTimeMillis());
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, System.currentTimeMillis());
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null, System.currentTimeMillis());
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>(false, message, data, System.currentTimeMillis());
    }
} 