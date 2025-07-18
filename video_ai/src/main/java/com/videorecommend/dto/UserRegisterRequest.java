package com.videorecommend.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户注册请求DTO
 * 
 * @author VideoRecommend
 */
@Data
public class UserRegisterRequest {
    
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码长度不能少于6个字符")
    private String password;
    
    @Email(message = "邮箱格式不正确")
    private String email;
    
    private String nickName;
    
    private String avatarUrl;
} 