# 视频推荐系统 API 接口文档

## 基础信息
- **服务地址**: http://localhost:8080
- **Content-Type**: application/json

## 1. 用户管理接口

### 1.1 用户注册
- **请求方式**: POST
- **路径**: `/users/register`
- **请求体**:
```json
{
  "username": "用户名",
  "email": "邮箱",
  "password": "密码"
}
```
- **响应**:
```json
{
  "success": true,
  "message": "用户注册成功",
  "data": {
    "id": 1,
    "username": "用户名",
    "email": "邮箱",
    "createdAt": "2025-07-13T15:00:00"
  }
}
```

### 1.2 用户登录
- **请求方式**: POST
- **路径**: `/users/login`
- **请求体**:
```json
{
  "username": "用户名",
  "password": "密码"
}
```
- **响应**:
```json
{
  "success": true,
  "message": "登录成功",
  "data": {
    "id": 1,
    "username": "用户名",
    "email": "邮箱",
    "token": "JWT_TOKEN"
  }
}
```

### 1.3 获取用户信息
- **请求方式**: GET
- **路径**: `/users/{userId}`
- **响应**:
```json
{
  "success": true,
  "data": {
    "id": 1,
    "username": "用户名",
    "email": "邮箱",
    "createdAt": "2025-07-13T15:00:00"
  }
}
```

### 1.4 更新用户信息
- **请求方式**: PUT
- **路径**: `/users/{userId}`
- **请求体**:
```json
{
  "username": "新用户名",
  "email": "新邮箱"
}
```

### 1.5 删除用户
- **请求方式**: DELETE
- **路径**: `/users/{userId}`

## 2. 视频管理接口

### 2.1 获取公开视频列表
- **请求方式**: GET
- **路径**: `/videos/public`
- **查询参数**:
  - `page`: 页码 (默认: 0)
  - `size`: 每页大小 (默认: 10)
  - `category`: 分类筛选
  - `tags`: 标签筛选
- **响应**:
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": 1,
        "title": "视频标题",
        "description": "视频描述",
        "videoUrl": "视频URL",
        "thumbnailUrl": "缩略图URL",
        "durationSeconds": 120,
        "viewCount": 1000,
        "likeCount": 50,
        "dislikeCount": 5,
        "commentCount": 20,
        "shareCount": 10,
        "category": "教育",
        "tags": "标签1,标签2",
        "uploaderName": "上传者",
        "isPublic": true,
        "isFeatured": false,
        "createdAt": "2025-07-13T15:00:00"
      }
    ],
    "totalElements": 100,
    "totalPages": 10,
    "currentPage": 0,
    "size": 10
  }
}
```

### 2.2 获取视频详情
- **请求方式**: GET
- **路径**: `/videos/{videoId}`
- **响应**:
```json
{
  "success": true,
  "data": {
    "id": 1,
    "title": "视频标题",
    "description": "视频描述",
    "videoUrl": "视频URL",
    "thumbnailUrl": "缩略图URL",
    "durationSeconds": 120,
    "viewCount": 1000,
    "likeCount": 50,
    "dislikeCount": 5,
    "commentCount": 20,
    "shareCount": 10,
    "category": "教育",
    "tags": "标签1,标签2",
    "uploaderName": "上传者",
    "isPublic": true,
    "isFeatured": false,
    "createdAt": "2025-07-13T15:00:00",
    "updatedAt": "2025-07-13T15:00:00"
  }
}
```

### 2.3 上传视频
- **请求方式**: POST
- **路径**: `/videos`
- **请求体**:
```json
{
  "title": "视频标题",
  "description": "视频描述",
  "videoUrl": "视频URL",
  "thumbnailUrl": "缩略图URL",
  "durationSeconds": 120,
  "category": "教育",
  "tags": "标签1,标签2",
  "uploaderId": 1,
  "uploaderName": "上传者",
  "isPublic": true
}
```

### 2.4 更新视频信息
- **请求方式**: PUT
- **路径**: `/videos/{videoId}`
- **请求体**:
```json
{
  "title": "新标题",
  "description": "新描述",
  "category": "新分类",
  "tags": "新标签",
  "isPublic": true
}
```

### 2.5 删除视频
- **请求方式**: DELETE
- **路径**: `/videos/{videoId}`

### 2.6 增加视频观看次数
- **请求方式**: POST
- **路径**: `/videos/{videoId}/view`

### 2.7 点赞视频
- **请求方式**: POST
- **路径**: `/videos/{videoId}/like`

### 2.8 取消点赞
- **请求方式**: DELETE
- **路径**: `/videos/{videoId}/like`

### 2.9 点踩视频
- **请求方式**: POST
- **路径**: `/videos/{videoId}/dislike`

### 2.10 取消点踩
- **请求方式**: DELETE
- **路径**: `/videos/{videoId}/dislike`

## 3. 用户行为接口

### 3.1 记录用户行为
- **请求方式**: POST
- **路径**: `/user-behaviors`
- **请求体**:
```json
{
  "userId": 1,
  "videoId": 1,
  "behaviorType": "VIEW",
  "duration": 30
}
```

### 3.2 获取用户行为历史
- **请求方式**: GET
- **路径**: `/user-behaviors/user/{userId}`
- **查询参数**:
  - `page`: 页码
  - `size`: 每页大小
  - `behaviorType`: 行为类型筛选

### 3.3 删除用户行为记录
- **请求方式**: DELETE
- **路径**: `/user-behaviors/{behaviorId}`

## 4. 用户偏好接口

### 4.1 设置用户偏好
- **请求方式**: POST
- **路径**: `/user-preferences`
- **请求体**:
```json
{
  "userId": 1,
  "category": "教育",
  "tags": "编程,技术",
  "preferenceScore": 0.8
}
```

### 4.2 获取用户偏好
- **请求方式**: GET
- **路径**: `/user-preferences/user/{userId}`

### 4.3 更新用户偏好
- **请求方式**: PUT
- **路径**: `/user-preferences/{preferenceId}`
- **请求体**:
```json
{
  "category": "新分类",
  "tags": "新标签",
  "preferenceScore": 0.9
}
```

### 4.4 删除用户偏好
- **请求方式**: DELETE
- **路径**: `/user-preferences/{preferenceId}`

## 5. 推荐系统接口

### 5.1 获取个性化推荐
- **请求方式**: GET
- **路径**: `/recommendations/user/{userId}`
- **查询参数**:
  - `limit`: 推荐数量 (默认: 10)
  - `category`: 分类筛选
- **响应**:
```json
{
  "success": true,
  "data": {
    "userId": 1,
    "recommendations": [
      {
        "videoId": 1,
        "title": "推荐视频标题",
        "description": "推荐视频描述",
        "thumbnailUrl": "缩略图URL",
        "score": 0.85,
        "reason": "基于您的观看历史推荐"
      }
    ],
    "totalCount": 10
  }
}
```

### 5.2 获取热门推荐
- **请求方式**: GET
- **路径**: `/recommendations/popular`
- **查询参数**:
  - `limit`: 推荐数量 (默认: 10)
  - `category`: 分类筛选
  - `timeRange`: 时间范围 (day, week, month)

### 5.3 获取相似视频推荐
- **请求方式**: GET
- **路径**: `/recommendations/similar/{videoId}`
- **查询参数**:
  - `limit`: 推荐数量 (默认: 5)

### 5.4 获取基于标签的推荐
- **请求方式**: GET
- **路径**: `/recommendations/by-tags`
- **查询参数**:
  - `tags`: 标签列表 (逗号分隔)
  - `limit`: 推荐数量 (默认: 10)

## 6. 视频标签接口

### 6.1 获取所有标签
- **请求方式**: GET
- **路径**: `/video-tags`
- **响应**:
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "编程",
      "category": "技术",
      "usageCount": 50
    }
  ]
}
```

### 6.2 创建标签
- **请求方式**: POST
- **路径**: `/video-tags`
- **请求体**:
```json
{
  "name": "新标签",
  "category": "分类"
}
```

### 6.3 更新标签
- **请求方式**: PUT
- **路径**: `/video-tags/{tagId}`
- **请求体**:
```json
{
  "name": "新标签名",
  "category": "新分类"
}
```

### 6.4 删除标签
- **请求方式**: DELETE
- **路径**: `/video-tags/{tagId}`

## 7. 错误响应格式

当API调用失败时，会返回以下格式的错误响应：

```json
{
  "success": false,
  "message": "错误描述",
  "error": "详细错误信息",
  "timestamp": "2025-07-13T15:00:00"
}
```

## 8. 状态码说明

- **200**: 请求成功
- **201**: 创建成功
- **400**: 请求参数错误
- **401**: 未授权
- **403**: 禁止访问
- **404**: 资源不存在
- **500**: 服务器内部错误

## 9. 测试示例

### 使用curl测试注册接口
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'
```

### 使用curl测试获取公开视频
```bash
curl -X GET "http://localhost:8080/api/videos/public?page=0&size=5"
```

## 10. 注意事项

1. 所有POST/PUT请求的Content-Type必须设置为application/json
2. 用户相关的敏感操作建议添加认证机制
3. 分页查询的页码从0开始
4. 时间格式统一使用ISO 8601格式
5. 文件上传功能需要单独实现 