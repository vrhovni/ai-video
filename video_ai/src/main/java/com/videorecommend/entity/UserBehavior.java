package com.videorecommend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 用户行为实体类
 * 
 * @author VideoRecommend
 */
@Data
@Entity
@Table(name = "user_behaviors")
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = false)
public class UserBehavior {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "用户ID不能为空")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull(message = "视频ID不能为空")
    @Column(name = "video_id", nullable = false)
    private Long videoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BehaviorType behaviorType;

    @Column(name = "duration_watched")
    private Integer durationWatched; // 观看时长（秒）

    @Column(name = "watch_progress")
    private Double watchProgress; // 观看进度（0-1）

    @Column(name = "interaction_time")
    private Integer interactionTime; // 交互时长（秒）

    @Column(name = "device_info")
    private String deviceInfo; // 设备信息

    @Column(name = "ip_address")
    private String ipAddress; // IP地址

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", insertable = false, updatable = false)
    private Video video;

    public enum BehaviorType {
        VIEW,           // 观看
        LIKE,           // 点赞
        DISLIKE,        // 点踩
        SHARE,          // 分享
        COMMENT,        // 评论
        FAVORITE,       // 收藏
        SEARCH,         // 搜索
        CLICK,          // 点击
        SCROLL,         // 滚动
        PAUSE,          // 暂停
        RESUME,         // 恢复播放
        SEEK            // 跳转播放
    }
} 