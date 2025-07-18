package com.videorecommend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 视频标签实体类
 * 
 * @author VideoRecommend
 */
@Data
@Entity
@Table(name = "video_tags")
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = false)
public class VideoTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "视频ID不能为空")
    @Column(name = "video_id", nullable = false)
    private Long videoId;

    @NotBlank(message = "标签名称不能为空")
    @Column(name = "tag_name", nullable = false)
    private String tagName;

    @Column(name = "tag_weight")
    private Double tagWeight = 1.0; // 标签权重

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", insertable = false, updatable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Video video;
} 