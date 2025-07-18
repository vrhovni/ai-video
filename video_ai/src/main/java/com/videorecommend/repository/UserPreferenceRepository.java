package com.videorecommend.repository;

import com.videorecommend.entity.UserPreference;
import com.videorecommend.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户偏好Repository接口
 * 
 * @author VideoRecommend
 */
@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {

    /**
     * 根据用户ID查找偏好记录
     */
    List<UserPreference> findByUserId(Long userId);

    /**
     * 根据用户ID和分类查找偏好记录
     */
    Optional<UserPreference> findByUserIdAndCategory(Long userId, Video.VideoCategory category);

    /**
     * 查找用户最喜欢的分类（按偏好分数排序）
     */
    @Query("SELECT up FROM UserPreference up WHERE up.userId = :userId ORDER BY up.preferenceScore DESC")
    List<UserPreference> findUserPreferencesByScore(@Param("userId") Long userId);

    /**
     * 查找用户最活跃的分类（按交互次数排序）
     */
    @Query("SELECT up FROM UserPreference up WHERE up.userId = :userId ORDER BY up.interactionCount DESC")
    List<UserPreference> findUserPreferencesByInteractionCount(@Param("userId") Long userId);

    /**
     * 查找用户最近交互的分类（按最后交互时间排序）
     */
    @Query("SELECT up FROM UserPreference up WHERE up.userId = :userId ORDER BY up.lastInteractionAt DESC")
    List<UserPreference> findUserPreferencesByLastInteraction(@Param("userId") Long userId);

    /**
     * 统计用户偏好的分类数量
     */
    @Query("SELECT COUNT(up) FROM UserPreference up WHERE up.userId = :userId AND up.preferenceScore > 0")
    long countUserPreferences(@Param("userId") Long userId);

    /**
     * 查找所有用户对特定分类的偏好
     */
    List<UserPreference> findByCategory(Video.VideoCategory category);

    /**
     * 查找偏好分数大于指定值的记录
     */
    @Query("SELECT up FROM UserPreference up WHERE up.userId = :userId AND up.preferenceScore >= :minScore")
    List<UserPreference> findByUserIdAndMinScore(@Param("userId") Long userId, @Param("minScore") Double minScore);

    /**
     * 查找交互次数大于指定值的记录
     */
    @Query("SELECT up FROM UserPreference up WHERE up.userId = :userId AND up.interactionCount >= :minCount")
    List<UserPreference> findByUserIdAndMinInteractionCount(@Param("userId") Long userId, @Param("minCount") Integer minCount);
} 