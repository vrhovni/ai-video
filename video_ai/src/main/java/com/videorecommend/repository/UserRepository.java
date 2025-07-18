package com.videorecommend.repository;

import com.videorecommend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户Repository接口
 * 
 * @author VideoRecommend
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查找用户
     */
    Optional<User> findByEmail(String email);

    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 根据角色查找用户
     */
    List<User> findByRole(User.UserRole role);

    /**
     * 查找活跃用户
     */
    List<User> findByIsActiveTrue();

    /**
     * 根据用户名模糊查询
     */
    List<User> findByUsernameContainingIgnoreCase(String username);

    /**
     * 根据昵称模糊查询
     */
    List<User> findByNickNameContainingIgnoreCase(String nickName);

    /**
     * 查找最近注册的用户
     */
    @Query("SELECT u FROM User u ORDER BY u.createdAt DESC")
    List<User> findRecentUsers();

    /**
     * 统计用户总数
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = true")
    long countActiveUsers();
} 