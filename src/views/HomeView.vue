<template>
  <div class="home">
    <!-- 原有的首页内容 -->
    <div class="hero-section">
      <div class="hero-content">
        <h1 class="hero-title">AI视频推荐系统</h1>
        <p class="hero-subtitle">发现更多精彩视频内容，享受个性化推荐体验</p>
        <div class="hero-actions">
          <el-button type="primary" size="large" @click="$router.push('/videos')">
            开始探索
          </el-button>
          <el-button size="large" @click="$router.push('/upload')">
            上传视频
          </el-button>
        </div>
      </div>
    </div>

    <!-- 推荐视频区域 -->
    <div class="recommendations-section">
      <div class="section-header">
        <h2>为您推荐</h2>
        <el-button text @click="$router.push('/recommendations')">
          查看更多 <el-icon><ArrowRight /></el-icon>
        </el-button>
      </div>
      
      <div class="videos-grid">
        <VideoCard
          v-for="video in recommendations"
          :key="video.id"
          :video="video"
          @click="playVideo(video)"
        />
      </div>
    </div>

    <!-- 热门视频区域 -->
    <div class="popular-section">
      <div class="section-header">
        <h2>热门视频</h2>
        <el-button text @click="$router.push('/videos')">
          查看更多 <el-icon><ArrowRight /></el-icon>
        </el-button>
      </div>
      
      <div class="videos-grid">
        <VideoCard
          v-for="video in popularVideos"
          :key="video.id"
          :video="video"
          @click="playVideo(video)"
        />
      </div>
    </div>

    <!-- 最新视频区域 -->
    <div class="latest-section">
      <div class="section-header">
        <h2>最新发布</h2>
        <el-button text @click="$router.push('/videos')">
          查看更多 <el-icon><ArrowRight /></el-icon>
        </el-button>
      </div>
      
      <div class="videos-grid">
        <VideoCard
          v-for="video in latestVideos"
          :key="video.id"
          :video="video"
          @click="playVideo(video)"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import VideoCard from '@/components/VideoCard.vue'
import { ArrowRight } from '@element-plus/icons-vue'

export default {
  name: 'HomeView',
  components: {
    VideoCard,
    ArrowRight
  },
  setup() {
    const store = useStore()
    const router = useRouter()

    // 从store获取数据
    const recommendations = computed(() => store.getters.recommendations)
    const popularVideos = computed(() => store.getters.popularVideos)
    const latestVideos = computed(() => store.getters.latestVideos)

    // 播放视频
    const playVideo = (video) => {
      router.push(`/video/${video.id}`)
    }

    // 视频错误处理
    const handleVideoError = (event) => {
      console.error('测试视频加载错误:', event)
    }

    // 视频加载成功
    const handleVideoLoaded = (event) => {
      console.log('测试视频加载成功:', event.target.src)
    }

    // 初始化数据
    onMounted(async () => {
      try {
        // 获取推荐视频
        await store.dispatch('fetchRecommendations', 1)
        
        // 获取热门视频
        await store.dispatch('fetchPopularVideos')
        
        // 获取最新视频
        await store.dispatch('fetchLatestVideos')
      } catch (error) {
        console.error('获取首页数据失败:', error)
      }
    })

    return {
      recommendations,
      popularVideos,
      latestVideos,
      playVideo,
      handleVideoError,
      handleVideoLoaded
    }
  }
}
</script>

<style scoped>
.home {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.hero-section {
  text-align: center;
  padding: 80px 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  margin-bottom: 60px;
  color: white;
}

.hero-title {
  font-size: 3rem;
  font-weight: 700;
  margin-bottom: 20px;
}

.hero-subtitle {
  font-size: 1.2rem;
  margin-bottom: 40px;
  opacity: 0.9;
}

.hero-actions {
  display: flex;
  gap: 20px;
  justify-content: center;
}

.recommendations-section,
.popular-section,
.latest-section {
  margin-bottom: 60px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.section-header h2 {
  font-size: 2rem;
  font-weight: 600;
  color: #303133;
}

.videos-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .hero-title {
    font-size: 2rem;
  }
  
  .hero-subtitle {
    font-size: 1rem;
  }
  
  .hero-actions {
    flex-direction: column;
    align-items: center;
  }
  
  .videos-grid {
    grid-template-columns: 1fr;
  }
}
</style>
