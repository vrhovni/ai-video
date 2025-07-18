package com.videorecommend.service;

import com.videorecommend.entity.User;
import com.videorecommend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用户服务类
 * 
 * @author VideoRecommend
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 用户注册
     */
    public User registerUser(User user) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (user.getEmail() != null && userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }

        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        log.info("用户注册成功: {}", savedUser.getUsername());
        return savedUser;
    }

    /**
     * 根据用户名查找用户
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 根据邮箱查找用户
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * 根据ID查找用户
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * 更新用户信息
     */
    public User updateUser(Long userId, User userDetails) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 更新基本信息
        if (userDetails.getNickName() != null) {
            user.setNickName(userDetails.getNickName());
        }
        if (userDetails.getEmail() != null) {
            user.setEmail(userDetails.getEmail());
        }
        if (userDetails.getAvatarUrl() != null) {
            user.setAvatarUrl(userDetails.getAvatarUrl());
        }

        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    /**
     * 更新用户密码
     */
    public void updatePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        log.info("用户密码更新成功: {}", user.getUsername());
    }

    /**
     * 删除用户
     */
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        user.setIsActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        log.info("用户已停用: {}", user.getUsername());
    }

    /**
     * 获取所有活跃用户
     */
    public List<User> getAllActiveUsers() {
        return userRepository.findByIsActiveTrue();
    }

    /**
     * 根据角色查找用户
     */
    public List<User> findByRole(User.UserRole role) {
        return userRepository.findByRole(role);
    }

    /**
     * 根据用户名模糊查询
     */
    public List<User> findByUsernameContaining(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }

    /**
     * 根据昵称模糊查询
     */
    public List<User> findByNickNameContaining(String nickName) {
        return userRepository.findByNickNameContainingIgnoreCase(nickName);
    }

    /**
     * 获取最近注册的用户
     */
    public List<User> getRecentUsers() {
        return userRepository.findRecentUsers();
    }

    /**
     * 统计活跃用户数量
     */
    public long countActiveUsers() {
        return userRepository.countActiveUsers();
    }

    /**
     * 验证用户密码
     */
    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
} 