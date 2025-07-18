package com.videorecommend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Spring Security配置类
 * 
 * @author VideoRecommend
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 配置HttpSecurity
        http
            .cors().and() // 允许跨域请求
            .csrf().disable() // 禁用CSRF保护
            .authorizeRequests()
                .antMatchers("/").permitAll() // 允许所有用户访问根路径
                .antMatchers("/videos/upload").permitAll() // 允许所有用户访问上传API
                .antMatchers("/videos/files/**").permitAll() // 允许所有用户访问静态资源
                .antMatchers("/api/users/register", "/api/users/validate-password").permitAll() // 允许所有用户访问注册和验证密码的API
                .antMatchers("/api/videos/public", "/api/videos/featured", "/api/videos/popular", "/api/videos/latest").permitAll() // 允许所有用户访问公开、精选、热门和最新视频的API
                .antMatchers("/api/recommendations/popular", "/api/recommendations/latest").permitAll() // 允许所有用户访问热门和最新推荐的API
                .antMatchers("/api/videos/search/**", "/api/videos/category/**").permitAll() // 允许所有用户访问视频搜索和分类的API
                .antMatchers("/api/**").permitAll() // 暂时允许所有API访问
                .anyRequest().permitAll() // 允许所有用户访问其他路径
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 使用无状态会话管理策略
    }

    @Bean
    // 创建一个名为passwordEncoder的Bean，返回一个BCryptPasswordEncoder对象
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    // 创建一个CorsConfigurationSource对象
    public CorsConfigurationSource corsConfigurationSource() {
        // 创建一个CorsConfiguration对象
        CorsConfiguration configuration = new CorsConfiguration();
        // 设置允许的源为所有
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        // 设置允许的方法为GET、POST、PUT、DELETE、OPTIONS
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 设置允许的头部为所有
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // 设置允许发送Cookie
        configuration.setAllowCredentials(true);
        
        // 创建一个UrlBasedCorsConfigurationSource对象
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 注册CorsConfiguration对象到UrlBasedCorsConfigurationSource对象中
        source.registerCorsConfiguration("/**", configuration);
        // 返回UrlBasedCorsConfigurationSource对象
        return source;
    }
} 