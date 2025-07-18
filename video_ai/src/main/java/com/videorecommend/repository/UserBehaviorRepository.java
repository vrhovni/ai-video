package com.videorecommend.repository;

import com.videorecommend.entity.UserBehavior;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户行为Repository接口
 * 
 * @author VideoRecommend
 */
@Repository
public interface UserBehaviorRepository extends JpaRepository<UserBehavior, Long> {

    /**
     * 根据用户ID查找行为记录
     */
    List<UserBehavior> findByUserId(Long userId);

    /**
     * 根据视频ID查找行为记录
     */
    List<UserBehavior> findByVideoId(Long videoId);

    /**
     * 根据用户ID和行为类型查找
     */
    List<UserBehavior> findByUserIdAndBehaviorType(Long userId, UserBehavior.BehaviorType behaviorType);

    /**
     * 根据视频ID和行为类型查找
     */
    List<UserBehavior> findByVideoIdAndBehaviorType(Long videoId, UserBehavior.BehaviorType behaviorType);

    /**
     * 根据用户ID和视频ID查找行为记录
     */
    List<UserBehavior> findByUserIdAndVideoId(Long userId, Long videoId);

    /**
     * 根据用户ID、视频ID和行为类型查找
     */
    UserBehavior findByUserIdAndVideoIdAndBehaviorType(Long userId, Long videoId, UserBehavior.BehaviorType behaviorType);

    /**
     * 查找用户最近的观看记录
     */
    @Query("SELECT ub FROM UserBehavior ub WHERE ub.userId = :userId AND ub.behaviorType = 'VIEW' ORDER BY ub.createdAt DESC")
    Page<UserBehavior> findRecentViewsByUserId(@Param("userId") Long userId, Pageable pageable);

    /**
     * 查找用户最近的点赞记录
     */
    @Query("SELECT ub FROM UserBehavior ub WHERE ub.userId = :userId AND ub.behaviorType = 'LIKE' ORDER BY ub.createdAt DESC")
    Page<UserBehavior> findRecentLikesByUserId(@Param("userId") Long userId, Pageable pageable);

    /**
     * 统计用户对特定视频的观看次数
     */
    @Query("SELECT COUNT(ub) FROM UserBehavior ub WHERE ub.userId = :userId AND ub.videoId = :videoId AND ub.behaviorType = 'VIEW'")
    long countViewsByUserIdAndVideoId(@Param("userId") Long userId, @Param("videoId") Long videoId);

    /**
     * 统计视频的总观看次数
     */
    @Query("SELECT COUNT(ub) FROM UserBehavior ub WHERE ub.videoId = :videoId AND ub.behaviorType = 'VIEW'")
    long countViewsByVideoId(@Param("videoId") Long videoId);

    /**
     * 统计视频的总点赞次数
     */
    @Query("SELECT COUNT(ub) FROM UserBehavior ub WHERE ub.videoId = :videoId AND ub.behaviorType = 'LIKE'")
    long countLikesByVideoId(@Param("videoId") Long videoId);

    /**
     * 查找指定时间范围内的用户行为
     */
    @Query("SELECT ub FROM UserBehavior ub WHERE ub.userId = :userId AND ub.createdAt BETWEEN :startDate AND :endDate")
    List<UserBehavior> findUserBehaviorsByDateRange(@Param("userId") Long userId, 
                                                   @Param("startDate") LocalDateTime startDate, 
                                                   @Param("endDate") LocalDateTime endDate);

    /**
     * 查找热门视频（按观看次数）
     */
    @Query("SELECT ub.videoId, COUNT(ub) as viewCount FROM UserBehavior ub " +
           "WHERE ub.behaviorType = 'VIEW' AND ub.createdAt >= :startDate " +
           "GROUP BY ub.videoId ORDER BY viewCount DESC")
    Page<Object[]> findPopularVideosByViewCount(@Param("startDate") LocalDateTime startDate, Pageable pageable);

    /**
     * 查找用户最常观看的视频分类
     */
    @Query("SELECT v.category, COUNT(ub) as viewCount FROM UserBehavior ub " +
           "JOIN Video v ON ub.videoId = v.id " +
           "WHERE ub.userId = :userId AND ub.behaviorType = 'VIEW' " +
           "GROUP BY v.category ORDER BY viewCount DESC")
    List<Object[]> findUserPreferredCategories(@Param("userId") Long userId);
} 