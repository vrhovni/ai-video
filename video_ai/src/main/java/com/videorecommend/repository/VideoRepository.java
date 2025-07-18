package com.videorecommend.repository;

import com.videorecommend.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 视频Repository接口
 * 
 * @author VideoRecommend
 */
@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    /**
     * 根据分类查找视频
     */
    Page<Video> findByCategory(Video.VideoCategory category, Pageable pageable);

    /**
     * 根据上传者ID查找视频
     */
    List<Video> findByUploaderId(Long uploaderId);

    /**
     * 查找公开视频
     */
    Page<Video> findByIsPublicTrue(Pageable pageable);

    /**
     * 查找推荐视频
     */
    Page<Video> findByIsFeaturedTrue(Pageable pageable);

    /**
     * 根据标题模糊查询
     */
    Page<Video> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    /**
     * 根据描述模糊查询
     */
    Page<Video> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);

    /**
     * 根据标签查询
     */
    @Query("SELECT v FROM Video v WHERE v.tags LIKE %:tag%")
    Page<Video> findByTag(@Param("tag") String tag, Pageable pageable);

    /**
     * 查找热门视频（按观看次数排序）
     */
    @Query("SELECT v FROM Video v WHERE v.isPublic = true ORDER BY v.viewCount DESC")
    Page<Video> findPopularVideos(Pageable pageable);

    /**
     * 查找最新视频
     */
    @Query("SELECT v FROM Video v WHERE v.isPublic = true ORDER BY v.createdAt DESC")
    Page<Video> findLatestVideos(Pageable pageable);

    /**
     * 查找指定时间范围内的视频
     */
    @Query("SELECT v FROM Video v WHERE v.createdAt BETWEEN :startDate AND :endDate AND v.isPublic = true")
    Page<Video> findVideosByDateRange(@Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate, 
                                     Pageable pageable);

    /**
     * 根据分类查找热门视频
     */
    @Query("SELECT v FROM Video v WHERE v.category = :category AND v.isPublic = true ORDER BY v.viewCount DESC")
    Page<Video> findPopularVideosByCategory(@Param("category") Video.VideoCategory category, Pageable pageable);

    /**
     * 统计视频总数
     */
    @Query("SELECT COUNT(v) FROM Video v WHERE v.isPublic = true")
    long countPublicVideos();

    /**
     * 统计分类视频数量
     */
    @Query("SELECT COUNT(v) FROM Video v WHERE v.category = :category AND v.isPublic = true")
    long countVideosByCategory(@Param("category") Video.VideoCategory category);
} 