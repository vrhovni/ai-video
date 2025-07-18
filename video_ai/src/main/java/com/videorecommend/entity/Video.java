package com.videorecommend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 视频实体类
 * 
 * @author VideoRecommend
 */
@Data
@Entity
@Table(name = "videos")
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = false)
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "视频标题不能为空")
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "video_url", nullable = false)
    private String videoUrl;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "view_count")
    private Long viewCount = 0L;

    @Column(name = "like_count")
    private Long likeCount = 0L;

    @Column(name = "dislike_count")
    private Long dislikeCount = 0L;

    @Column(name = "share_count")
    private Long shareCount = 0L;

    @Column(name = "comment_count")
    private Long commentCount = 0L;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VideoCategory category;

    @Column(name = "tags")
    private String tags; // 逗号分隔的标签

    @Column(name = "uploader_id")
    private Long uploaderId;

    @Column(name = "uploader_name")
    private String uploaderName;

    @Column(name = "is_public")
    private Boolean isPublic = true;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Set<UserBehavior> behaviors = new HashSet<>();

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Set<VideoTag> videoTags = new HashSet<>();

    public enum VideoCategory {
        ENTERTAINMENT,    // 娱乐
        EDUCATION,        // 教育
        SPORTS,          // 体育
        NEWS,            // 新闻
        MUSIC,           // 音乐
        GAMING,          // 游戏
        TECHNOLOGY,      // 科技
        LIFESTYLE,       // 生活
        TRAVEL,          // 旅行
        FOOD,            // 美食
        FASHION,         // 时尚
        OTHER            // 其他
    }
} 