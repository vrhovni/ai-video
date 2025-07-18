<template>
  <div class="video-card" @click="$emit('click')">
    <!-- 视频缩略图 -->
    <div class="video-thumbnail">
      <img :src="video.thumbnailUrl || '/default-thumbnail.jpg'" :alt="video.title" />
      <div class="video-duration">{{ formatDuration(video.durationSeconds) }}</div>
      <div class="video-overlay">
        <el-button type="primary" circle class="play-button">
          <el-icon><VideoPlay /></el-icon>
        </el-button>
      </div>
    </div>

    <!-- 视频信息 -->
    <div class="video-info">
      <div class="video-header">
        <h3 class="video-title">{{ video.title }}</h3>
        <div class="video-category">{{ video.category }}</div>
      </div>
      
      <p class="video-description">{{ video.description }}</p>
      
      <div class="video-meta">
        <div class="video-author">
          <el-avatar :size="24" :src="video.authorAvatar">
            {{ video.uploaderName?.charAt(0)?.toUpperCase() }}
          </el-avatar>
          <span>{{ video.uploaderName }}</span>
        </div>
        
        <div class="video-stats">
          <div class="stat-item">
            <el-icon><View /></el-icon>
            <span>{{ formatNumber(video.viewCount) }}</span>
          </div>
          <div class="stat-item">
            <el-icon><Star /></el-icon>
            <span>{{ formatNumber(video.likeCount) }}</span>
          </div>
        </div>
      </div>

      <!-- 操作按钮 -->
      <div class="video-actions">
        <el-button
          type="text"
          size="small"
          @click.stop="toggleLike"
          :class="{ 'liked': isLiked }"
        >
          <el-icon><Star /></el-icon>
          {{ isLiked ? '已点赞' : '点赞' }}
        </el-button>
        
        <el-button
          type="text"
          size="small"
          @click.stop="shareVideo"
        >
          <el-icon><Share /></el-icon>
          分享
        </el-button>
        
        <el-button
          type="text"
          size="small"
          @click.stop="addToFavorites"
        >
          <el-icon><Collection /></el-icon>
          收藏
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import {
  VideoPlay,
  View,
  Star,
  Share,
  Collection
} from '@element-plus/icons-vue'

export default {
  name: 'VideoCard',
  components: {
    VideoPlay,
    View,
    Star,
    Share,
    Collection
  },
  props: {
    video: {
      type: Object,
      required: true
    }
  },
  emits: ['click'],
  setup(props) {
    const store = useStore()
    const isLiked = ref(false)

    // 格式化数字
    const formatNumber = (num) => {
      if (num == null || isNaN(num)) return '0'
      if (num >= 1000000) {
        return (num / 1000000).toFixed(1) + 'M'
      } else if (num >= 1000) {
        return (num / 1000).toFixed(1) + 'K'
      }
      return num.toString()
    }

    // 格式化视频时长
    const formatDuration = (seconds) => {
      if (!seconds || isNaN(seconds)) return '0:00'
      const minutes = Math.floor(seconds / 60)
      const remainingSeconds = seconds % 60
      return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`
    }

    // 点赞/取消点赞
    const toggleLike = async () => {
      try {
        if (isLiked.value) {
          await store.dispatch('unlikeVideo', props.video.id)
        } else {
          await store.dispatch('likeVideo', props.video.id)
        }
        isLiked.value = !isLiked.value
        
        // 记录用户行为
        store.dispatch('recordBehavior', {
          userId: store.getters.currentUser?.id,
          videoId: props.video.id,
          behaviorType: isLiked.value ? 'LIKE' : 'UNLIKE'
        })
      } catch (error) {
        console.error('操作失败:', error)
      }
    }

    // 分享视频
    const shareVideo = async () => {
      try {
        await store.dispatch('shareVideo', props.video.id)
        
        // 记录用户行为
        store.dispatch('recordBehavior', {
          userId: store.getters.currentUser?.id,
          videoId: props.video.id,
          behaviorType: 'SHARE'
        })
        
        // 显示分享成功提示
        ElMessage.success('分享成功！')
      } catch (error) {
        console.error('分享失败:', error)
      }
    }

    // 添加到收藏
    const addToFavorites = () => {
      // 记录用户行为
      store.dispatch('recordBehavior', {
        userId: store.getters.currentUser?.id,
        videoId: props.video.id,
        behaviorType: 'FAVORITE'
      })
      
      ElMessage.success('已添加到收藏！')
    }

    return {
      isLiked,
      formatNumber,
      formatDuration,
      toggleLike,
      shareVideo,
      addToFavorites
    }
  }
}
</script>

<style scoped>
.video-card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.video-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 16px 48px rgba(0, 0, 0, 0.15);
}

/* 缩略图区域 */
.video-thumbnail {
  position: relative;
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.video-thumbnail img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.video-card:hover .video-thumbnail img {
  transform: scale(1.05);
}

.video-duration {
  position: absolute;
  bottom: 8px;
  right: 8px;
  background: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.8rem;
  font-weight: 500;
}

.video-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.video-card:hover .video-overlay {
  opacity: 1;
}

.play-button {
  width: 60px;
  height: 60px;
  font-size: 24px;
}

/* 视频信息区域 */
.video-info {
  padding: 16px;
}

.video-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 8px;
}

.video-title {
  font-size: 1.1rem;
  font-weight: 600;
  margin: 0;
  line-height: 1.4;
  color: #303133;
  flex: 1;
  margin-right: 12px;
}

.video-category {
  background: linear-gradient(135deg, #409eff, #67c23a);
  color: white;
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 0.7rem;
  font-weight: 500;
  white-space: nowrap;
}

.video-description {
  color: #606266;
  font-size: 0.9rem;
  line-height: 1.4;
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 视频元信息 */
.video-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.video-author {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.9rem;
  color: #606266;
}

.video-stats {
  display: flex;
  gap: 16px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 0.8rem;
  color: #909399;
}

.stat-item .el-icon {
  font-size: 14px;
}

/* 操作按钮 */
.video-actions {
  display: flex;
  gap: 8px;
  border-top: 1px solid rgba(0, 0, 0, 0.05);
  padding-top: 12px;
}

.video-actions .el-button {
  flex: 1;
  justify-content: center;
  color: #606266;
  font-size: 0.8rem;
}

.video-actions .el-button:hover {
  color: #409eff;
}

.video-actions .el-button.liked {
  color: #e6a23c;
}

.video-actions .el-button.liked:hover {
  color: #f56c6c;
}

/* 深色模式 */
:deep(.dark-mode) .video-card {
  background: rgba(26, 26, 26, 0.8);
  border-color: rgba(255, 255, 255, 0.1);
}

:deep(.dark-mode) .video-title {
  color: #ffffff;
}

:deep(.dark-mode) .video-description {
  color: #a8abb2;
}

:deep(.dark-mode) .video-author {
  color: #a8abb2;
}

:deep(.dark-mode) .stat-item {
  color: #909399;
}

:deep(.dark-mode) .video-actions {
  border-top-color: rgba(255, 255, 255, 0.1);
}

:deep(.dark-mode) .video-actions .el-button {
  color: #a8abb2;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .video-info {
    padding: 12px;
  }
  
  .video-title {
    font-size: 1rem;
  }
  
  .video-actions {
    flex-direction: column;
    gap: 4px;
  }
  
  .video-actions .el-button {
    justify-content: flex-start;
  }
}
</style> 