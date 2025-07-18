/* eslint-disable no-unused-vars */
import { createStore } from 'vuex'
import { userApi, videoApi, recommendationApi } from '@/services/api'

export default createStore({
  state: {
    // 用户相关
    user: JSON.parse(localStorage.getItem('user')) || null,
    isAuthenticated: !!localStorage.getItem('user'),
    
    // 视频相关
    videos: [],
    currentVideo: null,
    videoCategories: ['娱乐', '教育', '音乐', '游戏', '科技', '生活', '体育'],
    
    // 推荐相关
    recommendations: [],
    personalizedVideos: [],
    popularVideos: [],
    latestVideos: [],
    
    // UI相关
    darkMode: false,
    loading: false,
    
    // 分页
    currentPage: 1,
    pageSize: 20,
    totalPages: 0
  },
  
  mutations: {
    SET_USER(state, user) {
      state.user = user
      state.isAuthenticated = !!user
      if (user) {
        localStorage.setItem('user', JSON.stringify(user))
      } else {
        localStorage.removeItem('user')
      }
    },
    
    SET_VIDEOS(state, videos) {
      state.videos = videos
    },
    
    SET_CURRENT_VIDEO(state, video) {
      state.currentVideo = video
    },
    
    SET_RECOMMENDATIONS(state, recommendations) {
      state.recommendations = recommendations
    },
    
    SET_PERSONALIZED_VIDEOS(state, videos) {
      state.personalizedVideos = videos
    },
    
    SET_POPULAR_VIDEOS(state, videos) {
      state.popularVideos = videos
    },
    
    SET_LATEST_VIDEOS(state, videos) {
      state.latestVideos = videos
    },
    
    SET_DARK_MODE(state, darkMode) {
      state.darkMode = darkMode
      localStorage.setItem('darkMode', darkMode)
    },
    
    TOGGLE_DARK_MODE(state) {
      state.darkMode = !state.darkMode
      localStorage.setItem('darkMode', state.darkMode)
    },
    
    SET_LOADING(state, loading) {
      state.loading = loading
    },
    
    SET_PAGINATION(state, { currentPage, totalPages }) {
      state.currentPage = currentPage
      state.totalPages = totalPages
    }
  },
  
  getters: {
    isAuthenticated: state => state.isAuthenticated,
    currentUser: state => state.user,
    isLoading: state => state.loading,
    isDarkMode: state => state.darkMode,
    allVideos: state => state.videos,
    currentVideo: state => state.currentVideo,
    recommendations: state => state.recommendations,
    personalizedVideos: state => state.personalizedVideos,
    popularVideos: state => state.popularVideos,
    latestVideos: state => state.latestVideos,
    pagination: state => ({
      currentPage: state.currentPage,
      totalPages: state.totalPages,
      pageSize: state.pageSize
    })
  },
  
  actions: {
    initDarkMode({ commit }) {
      const savedDarkMode = localStorage.getItem('darkMode')
      if (savedDarkMode !== null) {
        commit('SET_DARK_MODE', savedDarkMode === 'true')
      }
    },
    
    async login({ commit }, credentials) {
      commit('SET_LOADING', true)
      try {
        const response = await fetch('/api/users/login', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(credentials)
        })
        const result = await response.json()
        if (result.success) {
          commit('SET_USER', result.data)
          return result.data
        } else {
          commit('SET_USER', null)
          throw new Error(result.message || '用户名或密码错误')
        }
      } catch (error) {
        commit('SET_USER', null)
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    },
    
    async register({ commit }, userData) {
      commit('SET_LOADING', true)
      try {
        const user = await userApi.register(userData)
        commit('SET_USER', user)
        return user
      } catch (error) {
        console.error('注册失败:', error)
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    },
    
    async fetchVideos({ commit }, params = {}) {
      commit('SET_LOADING', true)
      try {
        const data = await videoApi.getPublicVideos(params)
        commit('SET_VIDEOS', data.content)
        commit('SET_PAGINATION', {
          currentPage: data.number + 1,
          totalPages: data.totalPages
        })
        return data
      } catch (error) {
        console.error('获取视频失败:', error)
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    },
    
    async fetchVideoById({ commit }, id) {
      commit('SET_LOADING', true)
      try {
        const video = await videoApi.getVideoById(id)
        commit('SET_CURRENT_VIDEO', video)
        return video
      } catch (error) {
        console.error('获取视频详情失败:', error)
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    },
    
    async fetchPopularVideos({ commit }, params = {}) {
      commit('SET_LOADING', true)
      try {
        const data = await videoApi.getPopularVideos(params)
        commit('SET_POPULAR_VIDEOS', data.content)
        return data
      } catch (error) {
        console.error('获取热门视频失败:', error)
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    },
    
    async fetchLatestVideos({ commit }, params = {}) {
      commit('SET_LOADING', true)
      try {
        const data = await videoApi.getLatestVideos(params)
        commit('SET_LATEST_VIDEOS', data.content)
        return data
      } catch (error) {
        console.error('获取最新视频失败:', error)
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    },
    
    async fetchVideosByCategory({ commit }, category, params = {}) {
      commit('SET_LOADING', true)
      try {
        const data = await videoApi.getVideosByCategory(category, params)
        commit('SET_VIDEOS', data.content)
        commit('SET_PAGINATION', {
          currentPage: data.number + 1,
          totalPages: data.totalPages
        })
        return data
      } catch (error) {
        console.error('获取分类视频失败:', error)
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    },
    
    async searchVideos({ commit }, searchParams) {
      commit('SET_LOADING', true)
      try {
        const { type, query, ...params } = searchParams
        let data
        switch (type) {
          case 'title':
            data = await videoApi.searchVideosByTitle(query, params)
            break
          case 'description':
            data = await videoApi.searchVideosByDescription(query, params)
            break
          case 'tag':
            data = await videoApi.searchVideosByTag(query, params)
            break
          default:
            data = await videoApi.searchVideosByTitle(query, params)
        }
        commit('SET_VIDEOS', data.content)
        commit('SET_PAGINATION', {
          currentPage: data.number + 1,
          totalPages: data.totalPages
        })
        return data
      } catch (error) {
        console.error('搜索视频失败:', error)
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    },
    
    async fetchRecommendations({ commit }, userId) {
      try {
        const recommendations = await recommendationApi.getPersonalizedRecommendations(userId)
        commit('SET_RECOMMENDATIONS', recommendations)
        commit('SET_PERSONALIZED_VIDEOS', recommendations)
        return recommendations
      } catch (error) {
        console.error('获取推荐失败:', error)
        throw error
      }
    },
    
    async fetchPopularRecommendations({ commit }, limit = 20) {
      try {
        const recommendations = await recommendationApi.getPopularRecommendations(limit)
        commit('SET_POPULAR_VIDEOS', recommendations)
        return recommendations
      } catch (error) {
        console.error('获取热门推荐失败:', error)
        throw error
      }
    },
    
    async fetchLatestRecommendations({ commit }, limit = 20) {
      try {
        const recommendations = await recommendationApi.getLatestRecommendations(limit)
        commit('SET_LATEST_VIDEOS', recommendations)
        return recommendations
      } catch (error) {
        console.error('获取最新推荐失败:', error)
        throw error
      }
    },
    
    async recordUserBehavior({ commit }, { userId, videoId, behaviorType }) {
      try {
        await recommendationApi.recordUserBehavior(userId, videoId, behaviorType)
      } catch (error) {
        console.error('记录用户行为失败:', error)
      }
    },
    
    async incrementVideoView({ commit }, videoId) {
      try {
        await videoApi.incrementViewCount(videoId)
      } catch (error) {
        console.error('增加观看次数失败:', error)
      }
    },
    
    async incrementVideoLike({ commit }, videoId) {
      try {
        await videoApi.incrementLikeCount(videoId)
      } catch (error) {
        console.error('点赞失败:', error)
      }
    },
    
    async decrementVideoLike({ commit }, videoId) {
      try {
        await videoApi.decrementLikeCount(videoId)
      } catch (error) {
        console.error('取消点赞失败:', error)
      }
    },
    
    async incrementVideoDislike({ commit }, videoId) {
      try {
        await videoApi.incrementDislikeCount(videoId)
      } catch (error) {
        console.error('点踩失败:', error)
      }
    },
    
    async fetchSimilarVideos({ commit }, videoId, limit = 10) {
      try {
        const similarVideos = await recommendationApi.getSimilarVideos(videoId, limit)
        return similarVideos
      } catch (error) {
        console.error('获取相似视频失败:', error)
        // 如果获取相似视频失败，返回热门视频作为备选
        const popularVideos = await recommendationApi.getPopularRecommendations(limit)
        return popularVideos
      }
    },
    
    async createVideo({ commit }, videoData) {
      commit('SET_LOADING', true)
      try {
        const video = await videoApi.createVideo(videoData)
        return video
      } catch (error) {
        console.error('创建视频失败:', error)
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    },
    
    async updateVideo({ commit }, { id, videoData }) {
      commit('SET_LOADING', true)
      try {
        const video = await videoApi.updateVideo(id, videoData)
        return video
      } catch (error) {
        console.error('更新视频失败:', error)
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    },
    
    async deleteVideo({ commit }, id) {
      commit('SET_LOADING', true)
      try {
        await videoApi.deleteVideo(id)
      } catch (error) {
        console.error('删除视频失败:', error)
        throw error
      } finally {
        commit('SET_LOADING', false)
      }
    },
    
    logout({ commit }) {
      commit('SET_USER', null)
      commit('SET_VIDEOS', [])
      commit('SET_RECOMMENDATIONS', [])
      commit('SET_PERSONALIZED_VIDEOS', [])
      commit('SET_POPULAR_VIDEOS', [])
      commit('SET_LATEST_VIDEOS', [])
    }
  }
}) 