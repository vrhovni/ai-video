package com.videorecommend.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class VideoCreateRequest {
    @NotBlank(message = "视频标题不能为空")
    private String title;
    private String description;
    @NotBlank(message = "视频URL不能为空")
    private String videoUrl;
    private String thumbnailUrl;
    private Integer durationSeconds;
    @NotBlank(message = "视频分类不能为空")
    private String category; // 用字符串接收
    private String tags;
    private Long uploaderId;
    private String uploaderName;
    private Boolean isPublic = true;
    private Boolean isFeatured = false;
} 