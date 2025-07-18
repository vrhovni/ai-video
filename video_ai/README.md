# 视频推荐系统

基于Spring Boot开发的智能视频推荐系统，提供个性化推荐、协同过滤、基于内容的推荐等多种推荐算法。

## 功能特性

### 用户管理
- 用户注册、登录、信息管理
- 用户角色管理（普通用户、管理员）
- 用户偏好分析

### 视频管理
- 视频上传、编辑、删除
- 视频分类管理
- 视频标签系统
- 视频统计（观看次数、点赞、评论等）

### 推荐算法
- **个性化推荐**：基于用户历史行为和偏好
- **协同过滤**：基于相似用户的推荐
- **基于内容的推荐**：基于视频特征和用户兴趣
- **热门推荐**：基于观看次数、点赞数等统计
- **最新推荐**：基于发布时间

### 用户行为分析
- 观看行为记录
- 点赞、点踩、分享、评论行为
- 用户偏好学习
- 行为数据统计

## 技术栈

- **后端框架**：Spring Boot 2.7.14
- **数据库**：MySQL 8.0
- **ORM框架**：Spring Data JPA
- **安全框架**：Spring Security
- **构建工具**：Maven
- **开发语言**：Java 11
- **其他**：Lombok、JWT、Apache Commons

## 项目结构

```
video_ai/
├── src/
│   ├── main/
│   │   ├── java/com/videorecommend/
│   │   │   ├── config/          # 配置类
│   │   │   ├── controller/      # 控制器
│   │   │   ├── dto/            # 数据传输对象
│   │   │   ├── entity/         # 实体类
│   │   │   ├── repository/     # 数据访问层
│   │   │   ├── service/        # 业务逻辑层
│   │   │   └── VideoRecommendApplication.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/                   # 测试代码
├── pom.xml
└── README.md
```

## 数据库设计

### 主要表结构

1. **users** - 用户表
2. **videos** - 视频表
3. **user_behaviors** - 用户行为表
4. **user_preferences** - 用户偏好表
5. **video_tags** - 视频标签表

## API接口

### 用户相关接口

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/users/register` | 用户注册 |
| GET | `/api/users/{id}` | 获取用户信息 |
| PUT | `/api/users/{id}` | 更新用户信息 |
| DELETE | `/api/users/{id}` | 删除用户 |

### 视频相关接口

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/videos` | 创建视频 |
| GET | `/api/videos/{id}` | 获取视频详情 |
| GET | `/api/videos/public` | 获取公开视频列表 |
| GET | `/api/videos/popular` | 获取热门视频 |
| GET | `/api/videos/latest` | 获取最新视频 |
| GET | `/api/videos/category/{category}` | 根据分类获取视频 |
| GET | `/api/videos/search/title` | 按标题搜索视频 |

### 推荐相关接口

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/recommendations/personalized/{userId}` | 个性化推荐 |
| GET | `/api/recommendations/collaborative/{userId}` | 协同过滤推荐 |
| GET | `/api/recommendations/content-based/{userId}` | 基于内容的推荐 |
| GET | `/api/recommendations/popular` | 热门推荐 |
| GET | `/api/recommendations/latest` | 最新推荐 |
| POST | `/api/recommendations/behavior` | 记录用户行为 |

## 快速开始

### 环境要求

- JDK 11+
- Maven 3.6+
- MySQL 8.0+

### 安装步骤

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd video_ai
   ```

2. **配置数据库**
   - 创建MySQL数据库：`video_recommend`
   - 修改 `application.yml` 中的数据库连接信息

3. **启动应用**
   ```bash
   mvn spring-boot:run
   ```

4. **访问应用**
   - 应用地址：http://localhost:8080/api
   - 默认端口：8080

### 数据库配置

在 `application.yml` 中修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/video_recommend?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

## 推荐算法说明

### 1. 个性化推荐
基于用户的历史行为和偏好数据，分析用户对不同视频分类的喜好程度，推荐用户可能感兴趣的视频。

### 2. 协同过滤
找到与当前用户有相似观看行为的其他用户，推荐这些相似用户喜欢但当前用户未观看的视频。

### 3. 基于内容的推荐
分析用户观看视频的特征（分类、标签等），推荐具有相似特征的视频。

### 4. 热门推荐
基于视频的观看次数、点赞数等统计数据，推荐热门视频。

## 开发指南

### 添加新的推荐算法

1. 在 `RecommendationService` 中添加新的推荐方法
2. 在 `RecommendationController` 中添加对应的API接口
3. 实现具体的推荐逻辑

### 扩展用户行为类型

1. 在 `UserBehavior.BehaviorType` 枚举中添加新的行为类型
2. 在 `RecommendationService.getScoreAdjustment()` 方法中添加对应的分数调整逻辑

### 添加新的视频分类

1. 在 `Video.VideoCategory` 枚举中添加新的分类
2. 更新相关的业务逻辑

## 部署说明

### 打包应用
```bash
mvn clean package
```

### 运行JAR文件
```bash
java -jar target/video-recommend-1.0.0.jar
```

### Docker部署
```dockerfile
FROM openjdk:11-jre-slim
COPY target/video-recommend-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 性能优化

1. **数据库优化**
   - 添加适当的索引
   - 使用数据库连接池
   - 优化SQL查询

2. **缓存策略**
   - 使用Redis缓存热门视频
   - 缓存用户偏好数据
   - 缓存推荐结果

3. **异步处理**
   - 用户行为记录异步处理
   - 推荐算法计算异步执行

## 监控和日志

- 使用Spring Boot Actuator进行应用监控
- 配置日志级别和输出格式
- 记录用户行为和系统性能指标

## 贡献指南

1. Fork 项目
2. 创建功能分支
3. 提交更改
4. 推送到分支
5. 创建 Pull Request

## 许可证

本项目采用 MIT 许可证。

## 联系方式

如有问题或建议，请联系开发团队。 