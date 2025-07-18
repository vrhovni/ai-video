// API基础配置
const API_BASE_URL = 'http://localhost:8080';

// 用户相关API
export const userApi = {
  // 用户注册
  register: async (userData) => {
    const response = await fetch(`${API_BASE_URL}/users/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(userData),
    });
    return response.json();
  },

  // 根据ID获取用户信息
  getUserById: async (id) => {
    const response = await fetch(`${API_BASE_URL}/users/${id}`);
    return response.json();
  },

  // 根据用户名获取用户信息
  getUserByUsername: async (username) => {
    const response = await fetch(`${API_BASE_URL}/users/username/${username}`);
    return response.json();
  },

  // 更新用户信息
  updateUser: async (id, userData) => {
    const response = await fetch(`${API_BASE_URL}/users/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(userData),
    });
    return response.json();
  },

  // 获取活跃用户
  getActiveUsers: async () => {
    const response = await fetch(`${API_BASE_URL}/users/active`);
    return response.json();
  },

  // 用户登录
  login: async (loginData) => {
    const response = await fetch(`${API_BASE_URL}/users/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(loginData),
    });
    return response.json();
  },
};

// 视频相关API
export const videoApi = {
  // 获取公开视频列表
  getPublicVideos: async (params = {}) => {
    const queryString = new URLSearchParams(params).toString();
    const response = await fetch(`${API_BASE_URL}/videos/public?${queryString}`);
    return response.json();
  },

  // 获取推荐视频列表
  getFeaturedVideos: async (params = {}) => {
    const queryString = new URLSearchParams(params).toString();
    const response = await fetch(`${API_BASE_URL}/videos/featured?${queryString}`);
    return response.json();
  },

  // 根据ID获取视频详情
  getVideoById: async (id) => {
    const response = await fetch(`${API_BASE_URL}/videos/${id}`);
    return response.json();
  },

  // 创建视频
  createVideo: async (videoData) => {
    const response = await fetch(`${API_BASE_URL}/videos`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(videoData),
    });
    return response.json();
  },

  // 更新视频
  updateVideo: async (id, videoData) => {
    const response = await fetch(`${API_BASE_URL}/videos/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(videoData),
    });
    return response.json();
  },

  // 删除视频
  deleteVideo: async (id) => {
    const response = await fetch(`${API_BASE_URL}/videos/${id}`, {
      method: 'DELETE',
    });
    return response.json();
  },

  // 搜索视频（按标题）
  searchVideosByTitle: async (title, params = {}) => {
    const searchParams = new URLSearchParams({ title, ...params });
    const response = await fetch(`${API_BASE_URL}/videos/search/title?${searchParams}`);
    return response.json();
  },

  // 搜索视频（按描述）
  searchVideosByDescription: async (description, params = {}) => {
    const searchParams = new URLSearchParams({ description, ...params });
    const response = await fetch(`${API_BASE_URL}/videos/search/description?${searchParams}`);
    return response.json();
  },

  // 根据标签搜索视频
  searchVideosByTag: async (tag, params = {}) => {
    const searchParams = new URLSearchParams({ tag, ...params });
    const response = await fetch(`${API_BASE_URL}/videos/search/tag?${searchParams}`);
    return response.json();
  },

  // 获取热门视频
  getPopularVideos: async (params = {}) => {
    const queryString = new URLSearchParams(params).toString();
    const response = await fetch(`${API_BASE_URL}/videos/popular?${queryString}`);
    return response.json();
  },

  // 获取最新视频
  getLatestVideos: async (params = {}) => {
    const queryString = new URLSearchParams(params).toString();
    const response = await fetch(`${API_BASE_URL}/videos/latest?${queryString}`);
    return response.json();
  },

  // 根据分类获取视频
  getVideosByCategory: async (category, params = {}) => {
    const queryString = new URLSearchParams(params).toString();
    const response = await fetch(`${API_BASE_URL}/videos/category/${category}?${queryString}`);
    return response.json();
  },

  // 增加视频观看次数
  incrementViewCount: async (id) => {
    const response = await fetch(`${API_BASE_URL}/videos/${id}/view`, {
      method: 'POST',
    });
    return response.json();
  },

  // 增加视频点赞次数
  incrementLikeCount: async (id) => {
    const response = await fetch(`${API_BASE_URL}/videos/${id}/like`, {
      method: 'POST',
    });
    return response.json();
  },

  // 减少视频点赞次数
  decrementLikeCount: async (id) => {
    const response = await fetch(`${API_BASE_URL}/videos/${id}/unlike`, {
      method: 'POST',
    });
    return response.json();
  },

  // 增加视频点踩次数
  incrementDislikeCount: async (id) => {
    const response = await fetch(`${API_BASE_URL}/videos/${id}/dislike`, {
      method: 'POST',
    });
    return response.json();
  },

  // 增加视频分享次数
  incrementShareCount: async (id) => {
    const response = await fetch(`${API_BASE_URL}/videos/${id}/share`, {
      method: 'POST',
    });
    return response.json();
  },

  // 增加视频评论次数
  incrementCommentCount: async (id) => {
    const response = await fetch(`${API_BASE_URL}/videos/${id}/comment`, {
      method: 'POST',
    });
    return response.json();
  },
};

// 推荐相关API
export const recommendationApi = {
  // 获取个性化推荐
  getPersonalizedRecommendations: async (userId, limit = 20) => {
    const response = await fetch(`${API_BASE_URL}/recommendations/personalized/${userId}?limit=${limit}`);
    return response.json();
  },

  // 获取协同过滤推荐
  getCollaborativeFilteringRecommendations: async (userId, limit = 20) => {
    const response = await fetch(`${API_BASE_URL}/recommendations/collaborative/${userId}?limit=${limit}`);
    return response.json();
  },

  // 获取基于内容的推荐
  getContentBasedRecommendations: async (userId, limit = 20) => {
    const response = await fetch(`${API_BASE_URL}/recommendations/content-based/${userId}?limit=${limit}`);
    return response.json();
  },

  // 获取热门视频推荐
  getPopularRecommendations: async (limit = 20) => {
    const response = await fetch(`${API_BASE_URL}/recommendations/popular?limit=${limit}`);
    return response.json();
  },

  // 获取最新视频推荐
  getLatestRecommendations: async (limit = 20) => {
    const response = await fetch(`${API_BASE_URL}/recommendations/latest?limit=${limit}`);
    return response.json();
  },

  // 获取混合推荐
  getHybridRecommendations: async (userId, limit = 20) => {
    const response = await fetch(`${API_BASE_URL}/recommendations/hybrid/${userId}?limit=${limit}`);
    return response.json();
  },

  // 记录用户行为
  recordUserBehavior: async (userId, videoId, behaviorType) => {
    const response = await fetch(`${API_BASE_URL}/recommendations/behavior`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        userId,
        videoId,
        behaviorType
      }),
    });
    return response.json();
  },

  // 获取相似视频推荐
  getSimilarVideos: async (videoId, limit = 10) => {
    const response = await fetch(`${API_BASE_URL}/recommendations/similar/${videoId}?limit=${limit}`);
    return response.json();
  },
};

// 通用API工具
export const api = {
  // 设置认证token
  setAuthToken: (token) => {
    localStorage.setItem('authToken', token);
  },

  // 获取认证token
  getAuthToken: () => {
    return localStorage.getItem('authToken');
  },

  // 清除认证token
  clearAuthToken: () => {
    localStorage.removeItem('authToken');
  },

  // 检查是否已认证
  isAuthenticated: () => {
    return !!localStorage.getItem('authToken');
  },

  // 健康检查
  healthCheck: async () => {
    const response = await fetch(`${API_BASE_URL}/health`);
    return response.json();
  },

  // 获取API信息
  getApiInfo: async () => {
    const response = await fetch(`${API_BASE_URL}`);
    return response.json();
  },

  // 获取API文档
  getApiDocs: async () => {
    const response = await fetch(`${API_BASE_URL}/docs`);
    return response.json();
  },
}; 