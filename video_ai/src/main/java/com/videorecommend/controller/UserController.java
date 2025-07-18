package com.videorecommend.controller;

import com.videorecommend.entity.User;
import com.videorecommend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import com.videorecommend.dto.LoginRequest;
import com.videorecommend.dto.ApiResponse;

/**
 * 用户控制器
 * 
 * @author VideoRecommend
 */
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            return ResponseEntity.ok(ApiResponse.success("注册成功", registeredUser));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        log.info("开始用户登录，用户名: {}", loginRequest.getUsername());
        
        Optional<User> userOpt = userService.findByUsername(loginRequest.getUsername());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            log.info("找到用户: {}", user.getUsername());
            
            // 校验密码（加密比对）
            boolean passwordValid = userService.validatePassword(loginRequest.getPassword(), user.getPassword());
            log.info("密码验证结果: {}", passwordValid);
            
            if (passwordValid) {
                // 生成简单的token（实际项目中应该使用JWT）
                String token = "token_" + user.getId() + "_" + System.currentTimeMillis();
                log.info("生成token: {}", token);
                
                // 创建包含token的响应数据
                java.util.Map<String, Object> responseData = new java.util.HashMap<>();
                responseData.put("id", user.getId());
                responseData.put("username", user.getUsername());
                responseData.put("email", user.getEmail());
                responseData.put("nickName", user.getNickName());
                responseData.put("avatarUrl", user.getAvatarUrl());
                responseData.put("role", user.getRole());
                responseData.put("isActive", user.getIsActive());
                responseData.put("createdAt", user.getCreatedAt());
                responseData.put("updatedAt", user.getUpdatedAt());
                responseData.put("token", token);
                
                log.info("登录成功，返回用户数据");
                log.info("响应数据: {}", responseData);
                return ResponseEntity.ok(ApiResponse.success("登录成功", responseData));
            } else {
                log.warn("密码验证失败");
            }
        } else {
            log.warn("用户不存在: {}", loginRequest.getUsername());
        }
        
        log.warn("登录失败，用户名或密码错误");
        return ResponseEntity
            .status(401)
            .body(ApiResponse.error("用户名或密码错误"));
    }

    /**
     * 根据ID获取用户信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 根据用户名获取用户信息
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * 更新用户密码
     */
    @PutMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable Long id, @RequestBody String newPassword) {
        try {
            userService.updatePassword(id, newPassword);
            return ResponseEntity.ok().body(ApiResponse.success("密码更新成功", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().body(ApiResponse.success("用户删除成功", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * 获取所有活跃用户
     */
    @GetMapping("/active")
    public ResponseEntity<List<User>> getActiveUsers() {
        List<User> activeUsers = userService.getAllActiveUsers();
        return ResponseEntity.ok(activeUsers);
    }

    /**
     * 根据角色获取用户
     */
    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable User.UserRole role) {
        List<User> users = userService.findByRole(role);
        return ResponseEntity.ok(users);
    }

    /**
     * 根据用户名搜索用户
     */
    @GetMapping("/search/username")
    public ResponseEntity<List<User>> searchUsersByUsername(@RequestParam String username) {
        List<User> users = userService.findByUsernameContaining(username);
        return ResponseEntity.ok(users);
    }

    /**
     * 根据昵称搜索用户
     */
    @GetMapping("/search/nickname")
    public ResponseEntity<List<User>> searchUsersByNickname(@RequestParam String nickname) {
        List<User> users = userService.findByNickNameContaining(nickname);
        return ResponseEntity.ok(users);
    }

    /**
     * 获取最近注册的用户
     */
    @GetMapping("/recent")
    public ResponseEntity<List<User>> getRecentUsers() {
        List<User> recentUsers = userService.getRecentUsers();
        return ResponseEntity.ok(recentUsers);
    }

    /**
     * 获取活跃用户数量
     */
    @GetMapping("/count/active")
    public ResponseEntity<Long> getActiveUserCount() {
        long count = userService.countActiveUsers();
        return ResponseEntity.ok(count);
    }

    /**
     * 验证用户密码
     */
    @PostMapping("/validate-password")
    public ResponseEntity<Boolean> validatePassword(@RequestParam String rawPassword, @RequestParam String encodedPassword) {
        boolean isValid = userService.validatePassword(rawPassword, encodedPassword);
        return ResponseEntity.ok(isValid);
    }
} 