<template>
  <div class="recommendations-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1>为您推荐</h1>
        <p>基于您的兴趣和观看历史，为您精选的个性化内容</p>
      </div>
    </div>

    <!-- 推荐类型选择 -->
    <div class="recommendation-tabs">
      <el-tabs v-model="activeTab" @tab-click="handleTabChange">
        <el-tab-pane label="个性化推荐" name="personalized">
          <div class="tab-content">
            <div class="section-header">
              <h2>个性化推荐</h2>
              <p>基于您的观看历史和偏好</p>
            </div>
            <div class="videos-grid">
              <video-card
                v-for="video in personalizedVideos"
                :key="video.id"
                :video="video"
                @click="playVideo(video)"
              />
            </div>
          </div>
        </el-tab-pane>

        <el-tab-pane label="协同过滤" name="collaborative">
          <div class="tab-content">
            <div class="section-header">
              <h2>协同过滤推荐</h2>
              <p>基于相似用户的喜好</p>
            </div>
            <div class="videos-grid">
              <video-card
                v-for="video in collaborativeVideos"
                :key="video.id"
                :video="video"
                @click="playVideo(video)"
              />
            </div>
          </div>
        </el-tab-pane>

        <el-tab-pane label="内容推荐" name="content">
          <div class="tab-content">
            <div class="section-header">
              <h2>基于内容的推荐</h2>
              <p>基于视频内容和标签</p>
            </div>
            <div class="videos-grid">
              <video-card
                v-for="video in contentBasedVideos"
                :key="video.id"
                :video="video"
                @click="playVideo(video)"
              />
            </div>
          </div>
        </el-tab-pane>

        <el-tab-pane label="热门推荐" name="popular">
          <div class="tab-content">
            <div class="section-header">
              <h2>热门推荐</h2>
              <p>当前最受欢迎的视频</p>
            </div>
            <div class="videos-grid">
              <video-card
                v-for="video in popularVideos"
                :key="video.id"
                :video="video"
                @click="playVideo(video)"
              />
            </div>
          </div>
        </el-tab-pane>

        <el-tab-pane label="最新推荐" name="latest">
          <div class="tab-content">
            <div class="section-header">
              <h2>最新推荐</h2>
              <p>最新发布的优质内容</p>
            </div>
            <div class="videos-grid">
              <video-card
                v-for="video in latestVideos"
                :key="video.id"
                :video="video"
                @click="playVideo(video)"
              />
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 推荐设置 -->
    <div class="recommendation-settings">
      <el-card class="settings-card">
        <template #header>
          <div class="card-header">
            <span>推荐设置</span>
            <el-button type="primary" size="small" @click="refreshRecommendations">
              <el-icon><Refresh /></el-icon>
              刷新推荐
            </el-button>
          </div>
        </template>
        
        <div class="settings-content">
          <div class="setting-item">
            <label>推荐数量</label>
            <el-slider
              v-model="recommendationCount"
              :min="10"
              :max="50"
              :step="5"
              show-input
              @change="updateRecommendationCount"
            />
          </div>
          
          <div class="setting-item">
            <label>兴趣标签</label>
            <div class="interest-tags">
              <el-tag
                v-for="tag in interestTags"
                :key="tag"
                :closable="true"
                @close="removeInterestTag(tag)"
                class="interest-tag"
              >
                {{ tag }}
              </el-tag>
              <el-button size="small" @click="showAddTagDialog = true">
                <el-icon><Plus /></el-icon>
                添加标签
              </el-button>
            </div>
          </div>
          
          <div class="setting-item">
            <label>推荐算法偏好</label>
            <el-radio-group v-model="algorithmPreference" @change="updateAlgorithmPreference">
              <el-radio label="personalized">个性化推荐</el-radio>
              <el-radio label="collaborative">协同过滤</el-radio>
              <el-radio label="content">内容推荐</el-radio>
              <el-radio label="hybrid">混合推荐</el-radio>
            </el-radio-group>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 推荐统计 -->
    <div class="recommendation-stats">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon">
                <el-icon><VideoPlay /></el-icon>
              </div>
              <div class="stat-info">
                <h3>{{ totalRecommended }}</h3>
                <p>推荐视频总数</p>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon">
                <el-icon><View /></el-icon>
              </div>
              <div class="stat-info">
                <h3>{{ totalViews }}</h3>
                <p>观看次数</p>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon">
                <el-icon><Star /></el-icon>
              </div>
              <div class="stat-info">
                <h3>{{ totalLikes }}</h3>
                <p>点赞次数</p>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon">
                <el-icon><TrendCharts /></el-icon>
              </div>
              <div class="stat-info">
                <h3>{{ accuracyRate }}%</h3>
                <p>推荐准确率</p>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 添加标签对话框 -->
    <el-dialog
      v-model="showAddTagDialog"
      title="添加兴趣标签"
      width="400px"
    >
      <el-form :model="newTagForm" label-width="80px">
        <el-form-item label="标签名称">
          <el-input v-model="newTagForm.name" placeholder="请输入标签名称" />
        </el-form-item>
        <el-form-item label="标签权重">
          <el-slider
            v-model="newTagForm.weight"
            :min="1"
            :max="10"
            :step="1"
            show-input
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showAddTagDialog = false">取消</el-button>
          <el-button type="primary" @click="addInterestTag">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import VideoCard from '@/components/VideoCard.vue'
import {
  Refresh,
  Plus,
  VideoPlay,
  View,
  Star,
  TrendCharts
} from '@element-plus/icons-vue'

export default {
  name: 'RecommendationsView',
  components: {
    VideoCard,
    Refresh,
    Plus,
    VideoPlay,
    View,
    Star,
    TrendCharts
  },
  setup() {
    const store = useStore()
    const router = useRouter()

    // 响应式数据
    const activeTab = ref('personalized')
    const recommendationCount = ref(20)
    const algorithmPreference = ref('personalized')
    const showAddTagDialog = ref(false)
    const interestTags = ref(['科技', '教育', '音乐', '游戏'])

    // 新标签表单
    const newTagForm = reactive({
      name: '',
      weight: 5
    })

    // 模拟推荐数据
    const personalizedVideos = ref([
      {
        id: 1,
        title: '人工智能发展现状与未来趋势',
        description: '深度解析AI技术的最新发展动态',
        thumbnail: 'https://via.placeholder.com/300x200/409eff/ffffff?text=AI+Video',
        duration: '12:34',
        views: 15420,
        likes: 1234,
        author: '科技前沿',
        category: '科技'
      },
      {
        id: 2,
        title: 'Vue.js 3.0 完整教程',
        description: '从入门到精通的Vue.js学习指南',
        thumbnail: 'https://via.placeholder.com/300x200/67c23a/ffffff?text=Vue+3',
        duration: '45:20',
        views: 8920,
        likes: 567,
        author: '前端大师',
        category: '教育'
      }
    ])

    const collaborativeVideos = ref([
      {
        id: 3,
        title: '2024年最热门歌曲合集',
        description: '年度最受欢迎的音乐作品',
        thumbnail: 'https://via.placeholder.com/300x200/e6a23c/ffffff?text=Music',
        duration: '23:15',
        views: 25680,
        likes: 1890,
        author: '音乐频道',
        category: '音乐'
      }
    ])

    const contentBasedVideos = ref([
      {
        id: 4,
        title: '搞笑短视频合集',
        description: '让你笑到肚子疼的精彩瞬间',
        thumbnail: 'https://via.placeholder.com/300x200/f56c6c/ffffff?text=Funny',
        duration: '8:45',
        views: 34210,
        likes: 2345,
        author: '娱乐频道',
        category: '娱乐'
      }
    ])

    const popularVideos = ref([
      {
        id: 5,
        title: '最新游戏实况',
        description: '热门游戏的最新实况直播',
        thumbnail: 'https://via.placeholder.com/300x200/909399/ffffff?text=Game',
        duration: '1:23:45',
        views: 5670,
        likes: 234,
        author: '游戏达人',
        category: '游戏'
      }
    ])

    const latestVideos = ref([
      {
        id: 6,
        title: '美食制作教程',
        description: '简单易学的家常菜制作方法',
        thumbnail: 'https://via.placeholder.com/300x200/67c23a/ffffff?text=Cooking',
        duration: '15:30',
        views: 7890,
        likes: 456,
        author: '美食家',
        category: '生活'
      }
    ])

    // 统计数据
    const totalRecommended = ref(156)
    const totalViews = ref(125430)
    const totalLikes = ref(8920)
    const accuracyRate = ref(87)

    // 方法
    const handleTabChange = (tab) => {
      console.log('切换到:', tab.props.name)
    }

    const playVideo = (video) => {
      router.push(`/video/${video.id}`)
    }

    const refreshRecommendations = async () => {
      try {
        ElMessage.success('推荐内容已刷新！')
        // 这里可以调用API刷新推荐
      } catch (error) {
        ElMessage.error('刷新失败，请重试')
      }
    }

    const updateRecommendationCount = (value) => {
      console.log('推荐数量更新为:', value)
    }

    const removeInterestTag = (tag) => {
      const index = interestTags.value.indexOf(tag)
      if (index > -1) {
        interestTags.value.splice(index, 1)
        ElMessage.success(`已移除标签: ${tag}`)
      }
    }

    const addInterestTag = () => {
      if (newTagForm.name.trim()) {
        interestTags.value.push(newTagForm.name)
        ElMessage.success(`已添加标签: ${newTagForm.name}`)
        newTagForm.name = ''
        newTagForm.weight = 5
        showAddTagDialog.value = false
      } else {
        ElMessage.warning('请输入标签名称')
      }
    }

    const updateAlgorithmPreference = (value) => {
      console.log('算法偏好更新为:', value)
    }

    // 初始化
    onMounted(() => {
      // 获取用户推荐数据
      if (store.getters.currentUser) {
        store.dispatch('fetchRecommendations', store.getters.currentUser.id)
      }
    })

    return {
      activeTab,
      recommendationCount,
      algorithmPreference,
      showAddTagDialog,
      interestTags,
      newTagForm,
      personalizedVideos,
      collaborativeVideos,
      contentBasedVideos,
      popularVideos,
      latestVideos,
      totalRecommended,
      totalViews,
      totalLikes,
      accuracyRate,
      handleTabChange,
      playVideo,
      refreshRecommendations,
      updateRecommendationCount,
      removeInterestTag,
      addInterestTag,
      updateAlgorithmPreference
    }
  }
}
</script>

<style scoped>
.recommendations-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 页面头部 */
.page-header {
  text-align: center;
  margin-bottom: 40px;
}

.header-content h1 {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 12px;
  color: #303133;
}

.header-content p {
  font-size: 1.1rem;
  color: #606266;
}

/* 推荐类型选择 */
.recommendation-tabs {
  margin-bottom: 40px;
}

.tab-content {
  padding: 20px 0;
}

.section-header {
  text-align: center;
  margin-bottom: 32px;
}

.section-header h2 {
  font-size: 1.8rem;
  font-weight: 600;
  margin-bottom: 8px;
  color: #303133;
}

.section-header p {
  color: #606266;
}

.videos-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
}

/* 推荐设置 */
.recommendation-settings {
  margin-bottom: 40px;
}

.settings-card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.settings-content {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.setting-item {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.setting-item label {
  font-weight: 600;
  color: #303133;
}

.interest-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  align-items: center;
}

.interest-tag {
  cursor: pointer;
}

/* 推荐统计 */
.recommendation-stats {
  margin-bottom: 40px;
}

.stat-card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #409eff, #67c23a);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
}

.stat-info h3 {
  font-size: 1.8rem;
  font-weight: 700;
  margin-bottom: 4px;
  color: #303133;
}

.stat-info p {
  color: #606266;
  font-size: 0.9rem;
}

/* 深色模式 */
:deep(.dark-mode) .header-content h1 {
  color: #ffffff;
}

:deep(.dark-mode) .header-content p {
  color: #a8abb2;
}

:deep(.dark-mode) .section-header h2 {
  color: #ffffff;
}

:deep(.dark-mode) .section-header p {
  color: #a8abb2;
}

:deep(.dark-mode) .settings-card,
:deep(.dark-mode) .stat-card {
  background: rgba(26, 26, 26, 0.8);
  border-color: rgba(255, 255, 255, 0.1);
}

:deep(.dark-mode) .setting-item label {
  color: #ffffff;
}

:deep(.dark-mode) .stat-info h3 {
  color: #ffffff;
}

:deep(.dark-mode) .stat-info p {
  color: #a8abb2;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-content h1 {
    font-size: 2rem;
  }

  .videos-grid {
    grid-template-columns: 1fr;
  }

  .recommendation-stats .el-row {
    margin: 0;
  }

  .recommendation-stats .el-col {
    padding: 0;
    margin-bottom: 16px;
  }
}

@media (max-width: 480px) {
  .recommendations-page {
    padding: 0 16px;
  }

  .header-content h1 {
    font-size: 1.8rem;
  }

  .interest-tags {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style> 