<template>
  <div class="video-detail">
    <!-- 视频播放区域 -->
    <div class="video-player-section">
      <div class="video-container">
        <div class="video-player">
          <video
            v-if="video && video.videoUrl"
            :src="video.videoUrl"
            controls
            class="video-element"
            @play="handleVideoPlay"
            @pause="handleVideoPause"
            @ended="handleVideoEnded"
            @timeupdate="handleTimeUpdate"
            @error="handleVideoError"
            @loadeddata="handleVideoLoaded"
            preload="metadata"
          >
            您的浏览器不支持视频播放
          </video>
          <div v-else class="video-placeholder">
            <el-icon class="placeholder-icon"><VideoPlay /></el-icon>
            <p>视频加载中...</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 视频信息区域 -->
    <div class="video-info-section">
      <div class="video-header">
        <h1 class="video-title">{{ video?.title || '加载中...' }}</h1>
        <div class="video-stats">
          <span class="stat-item">
            <el-icon><View /></el-icon>
            {{ formatNumber(video?.viewCount || 0) }} 次观看
          </span>
          <span class="stat-item">
            <el-icon><Calendar /></el-icon>
            {{ formatDate(video?.createdAt) }}
          </span>
        </div>
      </div>

      <!-- 操作按钮 -->
      <div class="video-actions">
        <el-button-group>
          <el-button
            :type="video?.isLiked ? 'primary' : 'default'"
            @click="toggleLike"
            :loading="likeLoading"
          >
            <el-icon><ThumbsUp /></el-icon>
            {{ video?.likeCount || 0 }}
          </el-button>
          <el-button @click="toggleDislike" :loading="dislikeLoading">
            <el-icon><ThumbsDown /></el-icon>
            {{ video?.dislikeCount || 0 }}
          </el-button>
          <el-button @click="shareVideo">
            <el-icon><Share /></el-icon>
            分享
          </el-button>
          <el-button @click="toggleFavorite" :loading="favoriteLoading">
            <el-icon><Star /></el-icon>
            {{ video?.isFavorited ? '已收藏' : '收藏' }}
          </el-button>
        </el-button-group>
      </div>

      <!-- 作者信息 -->
      <div class="author-section">
        <div class="author-info">
          <el-avatar :size="48" :src="video?.author?.avatar">
            {{ video?.author?.username?.charAt(0)?.toUpperCase() }}
          </el-avatar>
          <div class="author-details">
            <h3 class="author-name">{{ video?.author?.username }}</h3>
            <p class="author-subscribers">{{ formatNumber(video?.author?.subscriberCount || 0) }} 订阅者</p>
          </div>
        </div>
        <el-button
          v-if="!isCurrentUserVideo"
          :type="isSubscribed ? 'default' : 'primary'"
          @click="toggleSubscribe"
          :loading="subscribeLoading"
        >
          {{ isSubscribed ? '已订阅' : '订阅' }}
        </el-button>
      </div>

      <!-- 视频描述 -->
      <div class="video-description">
        <el-collapse v-model="descriptionExpanded">
          <el-collapse-item name="description">
            <template #title>
              <span class="description-title">视频描述</span>
            </template>
            <div class="description-content">
              <p>{{ video?.description || '暂无描述' }}</p>
              <div v-if="video?.tags?.length" class="video-tags">
                <el-tag
                  v-for="tag in video.tags"
                  :key="tag"
                  size="small"
                  class="tag-item"
                >
                  {{ tag }}
                </el-tag>
              </div>
            </div>
          </el-collapse-item>
        </el-collapse>
      </div>
    </div>

    <!-- 评论区域 -->
    <div class="comments-section">
      <div class="comments-header">
        <h3>评论 ({{ comments.length }})</h3>
        <el-button @click="sortComments">
          <el-icon><Sort /></el-icon>
          {{ sortOrder === 'newest' ? '最新' : '最热' }}
        </el-button>
      </div>

      <!-- 发表评论 -->
      <div v-if="isAuthenticated" class="comment-form">
        <el-avatar :size="32" :src="currentUser?.avatar">
          {{ currentUser?.username?.charAt(0)?.toUpperCase() }}
        </el-avatar>
        <div class="comment-input-container">
          <el-input
            v-model="newComment"
            placeholder="发表评论..."
            type="textarea"
            :rows="2"
            @keyup.ctrl.enter="submitComment"
          />
          <div class="comment-actions">
            <el-button
              type="primary"
              @click="submitComment"
              :loading="commentLoading"
              :disabled="!newComment.trim()"
            >
              发表评论
            </el-button>
          </div>
        </div>
      </div>

      <!-- 评论列表 -->
      <div class="comments-list">
        <div
          v-for="comment in sortedComments"
          :key="comment.id"
          class="comment-item"
        >
          <el-avatar :size="32" :src="comment.author?.avatar">
            {{ comment.author?.username?.charAt(0)?.toUpperCase() }}
          </el-avatar>
          <div class="comment-content">
            <div class="comment-header">
              <span class="comment-author">{{ comment.author?.username }}</span>
              <span class="comment-time">{{ formatDate(comment.createdAt) }}</span>
            </div>
            <p class="comment-text">{{ comment.content }}</p>
            <div class="comment-actions">
              <el-button size="small" text @click="likeComment(comment.id)">
                <el-icon><ThumbsUp /></el-icon>
                {{ comment.likeCount || 0 }}
              </el-button>
              <el-button size="small" text @click="replyToComment(comment)">
                回复
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 相关推荐 -->
    <div class="related-videos">
      <h3>相关推荐</h3>
      <div class="related-videos-grid">
        <VideoCard
          v-for="relatedVideo in relatedVideos"
          :key="relatedVideo.id"
          :video="relatedVideo"
          class="related-video-card"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted, watch } from 'vue'
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
// 暂时注释未使用的图标，后续可能需要使用
// import {
//   Sort
// } from '@element-plus/icons-vue'
import VideoCard from '@/components/VideoCard.vue'

export default {
  name: 'VideoDetailView',
  components: {
    VideoCard
  },
  setup() {
    const store = useStore()
    const route = useRoute()
    // const router = useRouter() // 暂时注释，后续可能需要使用

    // 响应式数据
    const video = ref(null)
    const comments = ref([])
    const relatedVideos = ref([])
    const newComment = ref('')
    const descriptionExpanded = ref(['description'])
    const sortOrder = ref('newest')
    const isSubscribed = ref(false)
    
    // 加载状态
    const likeLoading = ref(false)
    const dislikeLoading = ref(false)
    const favoriteLoading = ref(false)
    const subscribeLoading = ref(false)
    const commentLoading = ref(false)

    // 计算属性
    const isAuthenticated = computed(() => store.getters.isAuthenticated)
    const currentUser = computed(() => store.getters.currentUser)
    const isCurrentUserVideo = computed(() => 
      video.value?.author?.id === currentUser.value?.id
    )
    const sortedComments = computed(() => {
      const sorted = [...comments.value]
      if (sortOrder.value === 'newest') {
        return sorted.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
      } else {
        return sorted.sort((a, b) => (b.likeCount || 0) - (a.likeCount || 0))
      }
    })

    // 方法
    const fetchVideo = async () => {
      try {
        const videoId = route.params.id
        console.log('获取视频详情，ID:', videoId)
        const videoData = await store.dispatch('fetchVideoById', videoId)
        video.value = videoData
        console.log('获取到的视频数据:', videoData)
        
        // 记录观看行为
        if (isAuthenticated.value && currentUser.value?.id) {
          await store.dispatch('recordUserBehavior', {
            userId: currentUser.value.id,
          videoId: videoId,
            behaviorType: 'VIEW'
        })
        }
      } catch (error) {
        console.error('获取视频失败:', error)
        ElMessage.error('获取视频信息失败')
      }
    }

    const fetchComments = async () => {
      try {
        // 暂时使用模拟数据，因为后端没有评论API
        comments.value = [
          {
            id: 1,
            content: '很棒的视频！',
            author: { username: '用户1', avatar: null },
            createdAt: '2024-01-01T00:00:00.000Z',
            likeCount: 5
          },
          {
            id: 2,
            content: '学到了很多，谢谢分享',
            author: { username: '用户2', avatar: null },
            createdAt: '2024-01-01T00:00:00.000Z',
            likeCount: 3
          }
        ]
      } catch (error) {
        console.error('获取评论失败:', error)
      }
    }

    const fetchRelatedVideos = async () => {
      try {
        const videoId = route.params.id
        const relatedData = await store.dispatch('fetchSimilarVideos', videoId)
        relatedVideos.value = relatedData
      } catch (error) {
        console.error('获取相关视频失败:', error)
      }
    }

    const toggleLike = async () => {
      if (!isAuthenticated.value) {
        ElMessage.warning('请先登录')
        return
      }

      likeLoading.value = true
      try {
        const videoId = route.params.id
        if (video.value.isLiked) {
          await store.dispatch('decrementVideoLike', videoId)
          video.value.likeCount--
          video.value.isLiked = false
        } else {
          await store.dispatch('incrementVideoLike', videoId)
          video.value.likeCount++
          video.value.isLiked = true
        }
        
        // 记录用户行为
        await store.dispatch('recordUserBehavior', {
          userId: currentUser.value.id,
          videoId: videoId,
          behaviorType: video.value.isLiked ? 'LIKE' : 'UNLIKE'
        })
      } catch (error) {
        console.error('操作失败:', error)
        ElMessage.error('操作失败')
      } finally {
        likeLoading.value = false
      }
    }

    const toggleDislike = async () => {
      if (!isAuthenticated.value) {
        ElMessage.warning('请先登录')
        return
      }

      dislikeLoading.value = true
      try {
        const videoId = route.params.id
        await store.dispatch('incrementVideoDislike', videoId)
        video.value.dislikeCount++
        
        // 记录用户行为
        await store.dispatch('recordUserBehavior', {
          userId: currentUser.value.id,
          videoId: videoId,
          behaviorType: 'DISLIKE'
        })
        
        ElMessage.success('反馈已记录')
      } catch (error) {
        console.error('操作失败:', error)
        ElMessage.error('操作失败')
      } finally {
        dislikeLoading.value = false
      }
    }

    const shareVideo = () => {
      const url = window.location.href
      if (navigator.share) {
        navigator.share({
          title: video.value?.title,
          url: url
        })
      } else {
        navigator.clipboard.writeText(url)
        ElMessage.success('链接已复制到剪贴板')
      }
      
      // 记录分享行为
      if (isAuthenticated.value && currentUser.value?.id) {
        store.dispatch('recordUserBehavior', {
          userId: currentUser.value.id,
          videoId: route.params.id,
          behaviorType: 'SHARE'
        })
      }
    }

    const toggleFavorite = async () => {
      if (!isAuthenticated.value) {
        ElMessage.warning('请先登录')
        return
      }

      favoriteLoading.value = true
      try {
        const videoId = route.params.id
        if (video.value.isFavorited) {
          // 暂时使用模拟操作
          video.value.isFavorited = false
          ElMessage.success('已取消收藏')
        } else {
          // 暂时使用模拟操作
          video.value.isFavorited = true
          ElMessage.success('已添加到收藏')
        }
        
        // 记录用户行为
        await store.dispatch('recordUserBehavior', {
          userId: currentUser.value.id,
          videoId: videoId,
          behaviorType: video.value.isFavorited ? 'FAVORITE' : 'UNFAVORITE'
        })
      } catch (error) {
        console.error('操作失败:', error)
        ElMessage.error('操作失败')
      } finally {
        favoriteLoading.value = false
      }
    }

    const toggleSubscribe = async () => {
      if (!isAuthenticated.value) {
        ElMessage.warning('请先登录')
        return
      }

      subscribeLoading.value = true
      try {
        if (isSubscribed.value) {
          // 暂时使用模拟操作
          isSubscribed.value = false
          ElMessage.success('已取消订阅')
        } else {
          // 暂时使用模拟操作
          isSubscribed.value = true
          ElMessage.success('已订阅')
        }
      } catch (error) {
        console.error('操作失败:', error)
        ElMessage.error('操作失败')
      } finally {
        subscribeLoading.value = false
      }
    }

    const submitComment = async () => {
      if (!newComment.value.trim()) return

      commentLoading.value = true
      try {
        // 暂时使用模拟评论
        const comment = {
          id: Date.now(),
          content: newComment.value,
          author: { username: currentUser.value.username, avatar: null },
          createdAt: new Date().toISOString(),
          likeCount: 0
        }
        comments.value.unshift(comment)
        newComment.value = ''
        ElMessage.success('评论发表成功')
        
        // 记录用户行为
        await store.dispatch('recordUserBehavior', {
          userId: currentUser.value.id,
          videoId: route.params.id,
          behaviorType: 'COMMENT'
        })
      } catch (error) {
        console.error('发表评论失败:', error)
        ElMessage.error('发表评论失败')
      } finally {
        commentLoading.value = false
      }
    }

    const sortComments = () => {
      sortOrder.value = sortOrder.value === 'newest' ? 'popular' : 'newest'
    }

    const likeComment = async (commentId) => {
      if (!isAuthenticated.value) {
        ElMessage.warning('请先登录')
        return
      }

      try {
        // 暂时使用模拟操作
        const comment = comments.value.find(c => c.id === commentId)
        if (comment) {
          comment.likeCount = (comment.likeCount || 0) + 1
        }
      } catch (error) {
        console.error('操作失败:', error)
      }
    }

    const replyToComment = (comment) => {
      newComment.value = `@${comment.author.username} `
    }

    // 视频播放事件处理
    const handleVideoPlay = () => {
      if (isAuthenticated.value && currentUser.value?.id) {
        store.dispatch('recordUserBehavior', {
          userId: currentUser.value.id,
        videoId: route.params.id,
          behaviorType: 'PLAY'
      })
      }
    }

    const handleVideoPause = () => {
      if (isAuthenticated.value && currentUser.value?.id) {
        store.dispatch('recordUserBehavior', {
          userId: currentUser.value.id,
        videoId: route.params.id,
          behaviorType: 'PAUSE'
      })
      }
    }

    const handleVideoEnded = () => {
      if (isAuthenticated.value && currentUser.value?.id) {
        store.dispatch('recordUserBehavior', {
          userId: currentUser.value.id,
        videoId: route.params.id,
          behaviorType: 'COMPLETE'
      })
      }
    }

    const handleTimeUpdate = (event) => {
      const currentTime = event.target.currentTime
      const duration = event.target.duration
      const progress = (currentTime / duration) * 100

      if (progress > 50 && !video.value.viewed) {
        video.value.viewed = true
        if (isAuthenticated.value && currentUser.value?.id) {
          store.dispatch('recordUserBehavior', {
            userId: currentUser.value.id,
          videoId: route.params.id,
            behaviorType: 'VIEW_PROGRESS'
          })
        }
      }
    }

    const handleVideoError = (event) => {
      console.error('视频加载错误:', event)
      ElMessage.error('视频加载失败，请检查网络连接或视频链接')
    }

    const handleVideoLoaded = (event) => {
      console.log('视频加载成功:', event.target.src)
    }

    // 工具函数
    const formatNumber = (num) => {
      if (num >= 1000000) {
        return (num / 1000000).toFixed(1) + 'M'
      } else if (num >= 1000) {
        return (num / 1000).toFixed(1) + 'K'
      }
      return num.toString()
    }

    const formatDate = (dateString) => {
      if (!dateString) return ''
      const date = new Date(dateString)
      const now = new Date()
      const diff = now - date
      const days = Math.floor(diff / (1000 * 60 * 60 * 24))
      
      if (days === 0) return '今天'
      if (days === 1) return '昨天'
      if (days < 7) return `${days}天前`
      if (days < 30) return `${Math.floor(days / 7)}周前`
      if (days < 365) return `${Math.floor(days / 30)}个月前`
      return `${Math.floor(days / 365)}年前`
    }

    // 监听路由变化
    watch(() => route.params.id, () => {
      if (route.params.id) {
        fetchVideo()
        fetchComments()
        fetchRelatedVideos()
      }
    })

    // 初始化
    onMounted(() => {
      if (route.params.id) {
        fetchVideo()
        fetchComments()
        fetchRelatedVideos()
      }
    })

    return {
      video,
      comments,
      relatedVideos,
      newComment,
      descriptionExpanded,
      sortOrder,
      isSubscribed,
      handleVideoError,
      handleVideoLoaded,
      likeLoading,
      dislikeLoading,
      favoriteLoading,
      subscribeLoading,
      commentLoading,
      isAuthenticated,
      currentUser,
      isCurrentUserVideo,
      sortedComments,
      toggleLike,
      toggleDislike,
      shareVideo,
      toggleFavorite,
      toggleSubscribe,
      submitComment,
      sortComments,
      likeComment,
      replyToComment,
      handleVideoPlay,
      handleVideoPause,
      handleVideoEnded,
      handleTimeUpdate,
      formatNumber,
      formatDate
    }
  }
}
</script>

<style scoped>
.video-detail {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.video-player-section {
  margin-bottom: 30px;
}

.video-container {
  position: relative;
  width: 100%;
  background: #000;
  border-radius: 8px;
  overflow: hidden;
}

.video-player {
  position: relative;
  width: 100%;
  aspect-ratio: 16/9;
}

.video-element {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.video-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  background: #f5f5f5;
  color: #666;
}

.placeholder-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.video-info-section {
  margin-bottom: 30px;
}

.video-header {
  margin-bottom: 20px;
}

.video-title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 12px;
  color: var(--el-text-color-primary);
}

.video-stats {
  display: flex;
  gap: 20px;
  color: var(--el-text-color-secondary);
  font-size: 14px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.video-actions {
  margin-bottom: 20px;
}

.author-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 0;
  border-top: 1px solid var(--el-border-color-light);
  border-bottom: 1px solid var(--el-border-color-light);
  margin-bottom: 20px;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.author-name {
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 4px 0;
  color: var(--el-text-color-primary);
}

.author-subscribers {
  margin: 0;
  color: var(--el-text-color-secondary);
  font-size: 14px;
}

.video-description {
  margin-bottom: 30px;
}

.description-title {
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.description-content {
  line-height: 1.6;
  color: var(--el-text-color-regular);
}

.video-tags {
  margin-top: 12px;
}

.tag-item {
  margin-right: 8px;
  margin-bottom: 8px;
}

.comments-section {
  margin-bottom: 30px;
}

.comments-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.comments-header h3 {
  margin: 0;
  color: var(--el-text-color-primary);
}

.comment-form {
  display: flex;
  gap: 12px;
  margin-bottom: 30px;
}

.comment-input-container {
  flex: 1;
}

.comment-actions {
  margin-top: 12px;
  display: flex;
  justify-content: flex-end;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.comment-item {
  display: flex;
  gap: 12px;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.comment-author {
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.comment-time {
  font-size: 12px;
  color: var(--el-text-color-secondary);
}

.comment-text {
  margin: 0 0 8px 0;
  line-height: 1.5;
  color: var(--el-text-color-regular);
}

.comment-actions {
  display: flex;
  gap: 16px;
}

.related-videos {
  margin-top: 40px;
}

.related-videos h3 {
  margin-bottom: 20px;
  color: var(--el-text-color-primary);
}

.related-videos-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.related-video-card {
  transition: transform 0.2s ease;
}

.related-video-card:hover {
  transform: translateY(-2px);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .video-detail {
    padding: 10px;
  }
  
  .video-title {
    font-size: 20px;
  }
  
  .author-section {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }
  
  .related-videos-grid {
    grid-template-columns: 1fr;
  }
}
</style> 