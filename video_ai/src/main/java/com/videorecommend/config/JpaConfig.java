package com.videorecommend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA配置类
 * 
 * @author VideoRecommend
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.videorecommend.repository")
@EnableTransactionManagement
public class JpaConfig {
} 