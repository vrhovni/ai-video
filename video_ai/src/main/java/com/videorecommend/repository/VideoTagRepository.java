package com.videorecommend.repository;

import com.videorecommend.entity.VideoTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 视频标签Repository接口
 * 
 * @author VideoRecommend
 */
@Repository
public interface VideoTagRepository extends JpaRepository<VideoTag, Long> {

    /**
     * 根据视频ID查找标签
     */
    List<VideoTag> findByVideoId(Long videoId);

    /**
     * 根据标签名称查找
     */
    List<VideoTag> findByTagName(String tagName);

    /**
     * 根据标签名称模糊查询
     */
    List<VideoTag> findByTagNameContainingIgnoreCase(String tagName);

    /**
     * 查找权重最高的标签
     */
    @Query("SELECT vt FROM VideoTag vt ORDER BY vt.tagWeight DESC")
    List<VideoTag> findTopTags();

    /**
     * 根据视频ID和标签名称查找
     */
    VideoTag findByVideoIdAndTagName(Long videoId, String tagName);

    /**
     * 统计标签使用次数
     */
    @Query("SELECT COUNT(vt) FROM VideoTag vt WHERE vt.tagName = :tagName")
    long countByTagName(@Param("tagName") String tagName);
} 