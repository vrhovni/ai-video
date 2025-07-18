<template>
  <div class="category-view">
    <!-- 分类头部 -->
    <div class="category-header">
      <div class="category-info">
        <h1 class="category-title">{{ categoryName }}</h1>
        <p class="category-description">{{ categoryDescription }}</p>
        <div class="category-stats">
          <span class="stat-item">
            <el-icon><VideoPlay /></el-icon>
            {{ totalVideos }} 个视频
          </span>
          <span class="stat-item">
            <el-icon><View /></el-icon>
            {{ totalViews }} 次观看
          </span>
          <span class="stat-item">
            <el-icon><User /></el-icon>
            {{ totalCreators }} 位创作者
          </span>
        </div>
      </div>
      <div class="category-banner">
        <el-image
          :src="categoryBanner"
          fit="cover"
          class="banner-image"
        />
      </div>
    </div>

    <!-- 筛选和排序 -->
    <div class="filter-section">
      <div class="filter-controls">
        <!-- 排序选项 -->
        <el-select v-model="sortBy" placeholder="排序方式" @change="handleSortChange">
          <el-option label="最新发布" value="latest" />
          <el-option label="最多观看" value="views" />
          <el-option label="最多点赞" value="likes" />
          <el-option label="最多评论" value="comments" />
          <el-option label="最多分享" value="shares" />
        </el-select>

        <!-- 时间筛选 -->
        <el-select v-model="timeFilter" placeholder="时间范围" @change="handleTimeFilterChange">
          <el-option label="全部时间" value="all" />
          <el-option label="今天" value="today" />
          <el-option label="本周" value="week" />
          <el-option label="本月" value="month" />
          <el-option label="今年" value="year" />
        </el-select>

        <!-- 时长筛选 -->
        <el-select v-model="durationFilter" placeholder="视频时长" @change="handleDurationFilterChange">
          <el-option label="全部时长" value="all" />
          <el-option label="4分钟以下" value="short" />
          <el-option label="4-20分钟" value="medium" />
          <el-option label="20分钟以上" value="long" />
        </el-select>

        <!-- 质量筛选 -->
        <el-select v-model="qualityFilter" placeholder="视频质量" @change="handleQualityFilterChange">
          <el-option label="全部质量" value="all" />
          <el-option label="4K" value="4k" />
          <el-option label="1080p" value="1080p" />
          <el-option label="720p" value="720p" />
        </el-select>
      </div>

      <!-- 视图切换 -->
      <div class="view-controls">
        <el-button-group>
          <el-button
            :type="viewMode === 'grid' ? 'primary' : 'default'"
            @click="viewMode = 'grid'"
          >
            <el-icon><Grid /></el-icon>
            网格视图
          </el-button>
          <el-button
            :type="viewMode === 'list' ? 'primary' : 'default'"
            @click="viewMode = 'list'"
          >
            <el-icon><List /></el-icon>
            列表视图
          </el-button>
        </el-button-group>
      </div>
    </div>

    <!-- 子分类导航 -->
    <div v-if="subCategories.length" class="sub-categories">
      <el-tabs v-model="activeSubCategory" @tab-click="handleSubCategoryChange">
        <el-tab-pane
          v-for="subCategory in subCategories"
          :key="subCategory.id"
          :label="subCategory.name"
          :name="subCategory.id"
        >
          <div class="sub-category-content">
            <p class="sub-category-description">{{ subCategory.description }}</p>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 视频列表 -->
    <div class="videos-section">
      <!-- 加载状态 -->
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="6" animated />
      </div>

      <!-- 视频网格 -->
      <div v-else-if="viewMode === 'grid'" class="videos-grid">
        <VideoCard
          v-for="video in filteredVideos"
          :key="video.id"
          :video="video"
          class="video-card"
        />
      </div>

      <!-- 视频列表 -->
      <div v-else class="videos-list">
        <div
          v-for="video in filteredVideos"
          :key="video.id"
          class="video-list-item"
          @click="goToVideo(video.id)"
        >
          <div class="video-thumbnail">
            <el-image
              :src="video.thumbnail"
              fit="cover"
              class="thumbnail-image"
            />
            <div class="video-duration">{{ formatDuration(video.duration) }}</div>
          </div>
          <div class="video-info">
            <h3 class="video-title">{{ video.title }}</h3>
            <p class="video-description">{{ video.description }}</p>
            <div class="video-meta">
              <span class="author">{{ video.author.username }}</span>
              <span class="views">{{ formatNumber(video.viewCount) }} 次观看</span>
              <span class="date">{{ formatDate(video.createdAt) }}</span>
            </div>
            <div class="video-stats">
              <span class="stat">
                <el-icon><View /></el-icon>
                {{ formatNumber(video.viewCount) }}
              </span>
              <span class="stat">
                <el-icon><ThumbsUp /></el-icon>
                {{ formatNumber(video.likeCount) }}
              </span>
              <span class="stat">
                <el-icon><ChatDotRound /></el-icon>
                {{ formatNumber(video.commentCount) }}
              </span>
            </div>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="!loading && filteredVideos.length === 0" class="empty-state">
        <el-empty
          description="暂无视频"
          :image-size="200"
        >
          <el-button type="primary" @click="refreshVideos">
            刷新页面
          </el-button>
        </el-empty>
      </div>
    </div>

    <!-- 分页 -->
    <div v-if="totalPages > 1" class="pagination-container">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[12, 24, 48, 96]"
        :total="totalVideos"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 相关分类推荐 -->
    <div class="related-categories">
      <h3>相关分类</h3>
      <div class="categories-grid">
        <div
          v-for="relatedCategory in relatedCategories"
          :key="relatedCategory.id"
          class="category-card"
          @click="goToCategory(relatedCategory.id)"
        >
          <el-image
            :src="relatedCategory.banner"
            fit="cover"
            class="category-card-image"
          />
          <div class="category-card-info">
            <h4>{{ relatedCategory.name }}</h4>
            <p>{{ relatedCategory.videoCount }} 个视频</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted, watch } from 'vue'
// import { useStore } from 'vuex' // 暂时注释，后续可能需要使用
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
// 暂时注释未使用的图标，后续可能需要使用
// import {
//   Grid,
//   List
// } from '@element-plus/icons-vue'
import VideoCard from '@/components/VideoCard.vue'

export default {
  name: 'CategoryView',
  components: {
    VideoCard
  },
  setup() {
    // const store = useStore() // 暂时注释，后续可能需要使用
    const route = useRoute()
    const router = useRouter()

    // 响应式数据
    const videos = ref([])
    const loading = ref(false)
    const currentPage = ref(1)
    const pageSize = ref(24)
    const totalVideos = ref(0)
    const totalPages = ref(0)
    const viewMode = ref('grid')
    const sortBy = ref('latest')
    const timeFilter = ref('all')
    const durationFilter = ref('all')
    const qualityFilter = ref('all')
    const activeSubCategory = ref('all')

    // 分类信息
    const categoryName = ref('')
    const categoryDescription = ref('')
    const categoryBanner = ref('')
    const totalViews = ref(0)
    const totalCreators = ref(0)
    const subCategories = ref([])
    const relatedCategories = ref([])

    // 计算属性
    const filteredVideos = computed(() => {
      let filtered = [...videos.value]

      // 时间筛选
      if (timeFilter.value !== 'all') {
        const now = new Date()
        const filterDate = new Date()
        
        switch (timeFilter.value) {
          case 'today':
            filterDate.setDate(now.getDate() - 1)
            break
          case 'week':
            filterDate.setDate(now.getDate() - 7)
            break
          case 'month':
            filterDate.setMonth(now.getMonth() - 1)
            break
          case 'year':
            filterDate.setFullYear(now.getFullYear() - 1)
            break
        }
        
        filtered = filtered.filter(video => 
          new Date(video.createdAt) >= filterDate
        )
      }

      // 时长筛选
      if (durationFilter.value !== 'all') {
        filtered = filtered.filter(video => {
          const duration = video.duration
          switch (durationFilter.value) {
            case 'short':
              return duration <= 240 // 4分钟
            case 'medium':
              return duration > 240 && duration <= 1200 // 4-20分钟
            case 'long':
              return duration > 1200 // 20分钟以上
            default:
              return true
          }
        })
      }

      // 质量筛选
      if (qualityFilter.value !== 'all') {
        filtered = filtered.filter(video => 
          video.quality === qualityFilter.value
        )
      }

      // 排序
      switch (sortBy.value) {
        case 'views':
          filtered.sort((a, b) => b.viewCount - a.viewCount)
          break
        case 'likes':
          filtered.sort((a, b) => b.likeCount - a.likeCount)
          break
        case 'comments':
          filtered.sort((a, b) => b.commentCount - a.commentCount)
          break
        case 'shares':
          filtered.sort((a, b) => b.shareCount - a.shareCount)
          break
        case 'latest':
        default:
          filtered.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
          break
      }

      return filtered
    })

    // 方法
    const fetchCategoryInfo = async () => {
      try {
        const category = route.params.category
        // 后端没有分类信息接口，使用模拟数据
        categoryName.value = category
        categoryDescription.value = `${category}分类的视频内容`
        categoryBanner.value = `https://via.placeholder.com/800x200/409eff/ffffff?text=${category}`
        totalVideos.value = 0 // 将通过fetchVideos获取
        totalViews.value = 0
        totalCreators.value = 0
        subCategories.value = []
        relatedCategories.value = []
      } catch (error) {
        console.error('获取分类信息失败:', error)
        ElMessage.error('获取分类信息失败')
      }
    }

    const fetchVideos = async () => {
      loading.value = true
      try {
        const category = route.params.category
        const params = {
          page: currentPage.value - 1,
          size: pageSize.value,
          sort: sortBy.value,
          subCategory: activeSubCategory.value === 'all' ? null : activeSubCategory.value
        }
        const queryString = new URLSearchParams(params).toString()
        const response = await fetch(`http://localhost:8080/videos/category/${category}?${queryString}`)
        if (!response.ok) {
          throw new Error(`HTTP ${response.status}: ${response.statusText}`)
        }
        const data = await response.json()
        videos.value = data.content || []
        totalVideos.value = data.totalElements || 0
        totalPages.value = data.totalPages || 0
      } catch (error) {
        console.error('获取视频失败:', error)
        ElMessage.error('获取视频失败，请稍后重试')
        // 使用模拟数据作为备选
        videos.value = [
          {
            id: 1,
            title: `${route.params.category}分类视频示例`,
            description: '这是一个示例视频',
            thumbnail: 'https://via.placeholder.com/300x200/409eff/ffffff?text=Video',
            duration: '5',
            views: 1234,
            likes: 56,
            author: '示例作者',
            category: route.params.category
          }
        ]
        totalVideos.value = 1
        totalPages.value = 1
      } finally {
        loading.value = false
      }
    }

    const handleSortChange = () => {
      currentPage.value = 1
      fetchVideos()
    }

    const handleTimeFilterChange = () => {
      currentPage.value = 1
      fetchVideos()
    }

    const handleDurationFilterChange = () => {
      currentPage.value = 1
      fetchVideos()
    }

    const handleQualityFilterChange = () => {
      currentPage.value = 1
      fetchVideos()
    }

    const handleSubCategoryChange = () => {
      currentPage.value = 1
      fetchVideos()
    }

    const handleSizeChange = (size) => {
      pageSize.value = size
      currentPage.value = 1
      fetchVideos()
    }

    const handleCurrentChange = (page) => {
      currentPage.value = page
      fetchVideos()
    }

    const goToVideo = (videoId) => {
      router.push(`/video/${videoId}`)
    }

    const goToCategory = (categoryId) => {
      router.push(`/category/${categoryId}`)
    }

    const refreshVideos = () => {
      fetchVideos()
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

    const formatDuration = (seconds) => {
      const hours = Math.floor(seconds / 3600)
      const minutes = Math.floor((seconds % 3600) / 60)
      const secs = seconds % 60

      if (hours > 0) {
        return `${hours}:${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
      } else {
        return `${minutes}:${secs.toString().padStart(2, '0')}`
      }
    }

    // 监听路由变化
    watch(() => route.params.category, () => {
      if (route.params.category) {
        currentPage.value = 1
        activeSubCategory.value = 'all'
        fetchCategoryInfo()
        fetchVideos()
      }
    })

    // 初始化
    onMounted(() => {
      if (route.params.category) {
        fetchCategoryInfo()
        fetchVideos()
      }
    })

    return {
      videos,
      loading,
      currentPage,
      pageSize,
      totalVideos,
      totalPages,
      viewMode,
      sortBy,
      timeFilter,
      durationFilter,
      qualityFilter,
      activeSubCategory,
      categoryName,
      categoryDescription,
      categoryBanner,
      totalViews,
      totalCreators,
      subCategories,
      relatedCategories,
      filteredVideos,
      handleSortChange,
      handleTimeFilterChange,
      handleDurationFilterChange,
      handleQualityFilterChange,
      handleSubCategoryChange,
      handleSizeChange,
      handleCurrentChange,
      goToVideo,
      goToCategory,
      refreshVideos,
      formatNumber,
      formatDate,
      formatDuration
    }
  }
}
</script>

<style scoped>
.category-view {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.category-header {
  display: flex;
  gap: 30px;
  margin-bottom: 30px;
  padding: 30px;
  background: var(--el-bg-color);
  border-radius: 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.category-info {
  flex: 1;
}

.category-title {
  font-size: 32px;
  font-weight: 700;
  margin-bottom: 12px;
  color: var(--el-text-color-primary);
}

.category-description {
  font-size: 16px;
  line-height: 1.6;
  margin-bottom: 20px;
  color: var(--el-text-color-regular);
}

.category-stats {
  display: flex;
  gap: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 6px;
  color: var(--el-text-color-secondary);
  font-size: 14px;
}

.category-banner {
  width: 300px;
  height: 200px;
  border-radius: 8px;
  overflow: hidden;
}

.banner-image {
  width: 100%;
  height: 100%;
}

.filter-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding: 20px;
  background: var(--el-bg-color);
  border-radius: 8px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.1);
}

.filter-controls {
  display: flex;
  gap: 16px;
}

.filter-controls .el-select {
  width: 150px;
}

.sub-categories {
  margin-bottom: 30px;
}

.sub-category-content {
  padding: 20px 0;
}

.sub-category-description {
  color: var(--el-text-color-regular);
  line-height: 1.6;
}

.videos-section {
  margin-bottom: 40px;
}

.loading-container {
  padding: 40px;
}

.videos-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.video-card {
  transition: transform 0.2s ease;
}

.video-card:hover {
  transform: translateY(-2px);
}

.videos-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.video-list-item {
  display: flex;
  gap: 16px;
  padding: 16px;
  background: var(--el-bg-color);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.1);
}

.video-list-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
}

.video-thumbnail {
  position: relative;
  width: 200px;
  height: 120px;
  border-radius: 8px;
  overflow: hidden;
  flex-shrink: 0;
}

.thumbnail-image {
  width: 100%;
  height: 100%;
}

.video-duration {
  position: absolute;
  bottom: 8px;
  right: 8px;
  background: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 12px;
}

.video-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.video-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 8px;
  color: var(--el-text-color-primary);
  line-height: 1.4;
}

.video-description {
  color: var(--el-text-color-regular);
  line-height: 1.5;
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.video-meta {
  display: flex;
  gap: 16px;
  margin-bottom: 8px;
  font-size: 14px;
  color: var(--el-text-color-secondary);
}

.video-stats {
  display: flex;
  gap: 16px;
}

.stat {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
  color: var(--el-text-color-secondary);
}

.empty-state {
  padding: 60px 20px;
  text-align: center;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-bottom: 40px;
}

.related-categories {
  margin-top: 40px;
}

.related-categories h3 {
  margin-bottom: 20px;
  color: var(--el-text-color-primary);
}

.categories-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}

.category-card {
  position: relative;
  height: 120px;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.2s ease;
}

.category-card:hover {
  transform: translateY(-2px);
}

.category-card-image {
  width: 100%;
  height: 100%;
}

.category-card-info {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 16px;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.8));
  color: white;
}

.category-card-info h4 {
  margin: 0 0 4px 0;
  font-size: 16px;
  font-weight: 600;
}

.category-card-info p {
  margin: 0;
  font-size: 14px;
  opacity: 0.8;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .category-view {
    padding: 10px;
  }
  
  .category-header {
    flex-direction: column;
    padding: 20px;
  }
  
  .category-banner {
    width: 100%;
    height: 150px;
  }
  
  .filter-section {
    flex-direction: column;
    gap: 16px;
  }
  
  .filter-controls {
    flex-wrap: wrap;
    gap: 12px;
  }
  
  .filter-controls .el-select {
    width: calc(50% - 6px);
  }
  
  .video-list-item {
    flex-direction: column;
  }
  
  .video-thumbnail {
    width: 100%;
    height: 200px;
  }
  
  .categories-grid {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  }
}
</style> 