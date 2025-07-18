# AI视频推荐系统前端

基于Vue3+ Element Plus的现代化视频推荐平台前端应用。

## 功能特性

- 🎥 视频浏览和播放
- 🧠 AI智能推荐
- 👤 用户注册和登录
- 📤 视频上传和发布
- 🔍 视频搜索和分类
- 💬 评论和互动
- 📱 响应式设计

## 技术栈

- **前端框架**: Vue 3
- **UI组件库**: Element Plus
- **状态管理**: Vuex4路由管理**: Vue Router 4
- **HTTP客户端**: Fetch API
- **构建工具**: Vue CLI

## 项目结构

```
ai_video/
├── public/                 # 静态资源
├── src/
│   ├── components/        # 公共组件
│   ├── views/            # 页面组件
│   ├── router/           # 路由配置
│   ├── store/            # 状态管理
│   ├── services/         # API服务
│   └── assets/           # 资源文件
├── package.json          # 项目配置
└── vue.config.js         # Vue配置
```

## 快速开始

### 环境要求

- Node.js >= 14.00 npm >=60
### 安装依赖

```bash
npm install
```

### 启动开发服务器

```bash
npm run serve
```

或者使用批处理文件（Windows）：

```bash
start.bat
```

### 构建生产版本

```bash
npm run build
```

## API接口

前端已配置为连接到后端API服务器（默认端口880）。

### 主要API端点

- **用户相关**: `/api/users/*`
- **视频相关**: `/api/videos/*`
- **推荐相关**: `/api/recommendations/*`

### 配置说明

API基础URL配置在 `src/services/api.js` 中：

```javascript
const API_BASE_URL = http://localhost:8080/api```

## 后端连接

确保后端服务器正在运行：
1. 启动后端服务器（video_ai项目）
2. 确保后端运行在880口3. 启动前端开发服务器

## 开发说明

### 添加新页面

1 在 `src/views/` 创建新的Vue组件
2. 在 `src/router/index.js` 添加路由配置
3. 在 `src/store/index.js` 添加相关状态管理

### 添加新API

1 `src/services/api.js` 添加API方法
2. 在 `src/store/index.js` 添加对应的action
3. 在组件中调用store action

### 样式指南

- 使用Element Plus组件库
- 遵循响应式设计原则
- 支持深色模式切换

## 部署

### 开发环境

```bash
npm run serve
```

### 生产环境

```bash
npm run build
```

构建后的文件在 `dist/` 目录中。

## 故障排除

### 常见问题
1. **API连接失败**
   - 检查后端服务器是否运行
   - 确认API端口配置正确
   - 检查CORS设置

2. **依赖安装失败**
   - 清除npm缓存：`npm cache clean --force`
   - 删除node_modules并重新安装
3. **构建失败**
   - 检查Node.js版本
   - 更新依赖包版本

## 贡献指南
1. Fork项目
2. 创建功能分支
3. 提交更改
45. 创建Pull Request

## 许可证

MIT License
