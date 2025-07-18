<template>
  <div class="videos-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1>视频列表</h1>
        <p>发现更多精彩视频内容</p>
      </div>
      
      <!-- 筛选和排序 -->
      <div class="filter-bar">
        <div class="filter-group">
          <el-select v-model="selectedCategory" placeholder="选择分类" @change="handleCategoryChange">
            <el-option label="全部分类" value="" />
            <el-option
              v-for="category in categories"
              :key="category"
              :label="category"
              :value="category"
            />
          </el-select>
          
          <el-select v-model="selectedSort" placeholder="排序方式" @change="handleSortChange">
            <el-option label="最新发布" value="latest" />
            <el-option label="最多观看" value="views" />
            <el-option label="最多点赞" value="likes" />
            <el-option label="最多分享" value="shares" />
          </el-select>
        </div>
        
        <div class="view-toggle">
          <el-button-group>
            <el-button
              :type="viewMode === 'grid' ? 'primary' : ''"
              @click="viewMode = 'grid'"
            >
              <el-icon><Grid /></el-icon>
            </el-button>
            <el-button
              :type="viewMode === 'list' ? 'primary' : ''"
              @click="viewMode = 'list'"
            >
              <el-icon><List /></el-icon>
            </el-button>
          </el-button-group>
        </div>
      </div>
    </div>

    <!-- 视频网格 -->
    <div class="videos-container">
      <div
        class="videos-grid"
        :class="{ 'list-view': viewMode === 'list' }"
      >
        <video-card
          v-for="video in filteredVideos"
          :key="video.id"
          :video="video"
          @click="playVideo(video)"
        />
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="3" animated />
        <el-skeleton :rows="3" animated />
        <el-skeleton :rows="3" animated />
      </div>

      <!-- 空状态 -->
      <div v-else-if="filteredVideos.length === 0" class="empty-state">
        <el-icon class="empty-icon"><VideoCamera /></el-icon>
        <h3>暂无视频</h3>
        <p>当前筛选条件下没有找到相关视频</p>
        <el-button type="primary" @click="resetFilters">重置筛选</el-button>
      </div>
    </div>

    <!-- 分页 -->
    <div v-if="totalPages > 1" class="pagination-container">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[12, 24, 48, 96]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted, watch } from 'vue'
import { useStore } from 'vuex'
import { useRouter, useRoute } from 'vue-router'
import VideoCard from '@/components/VideoCard.vue'
import { Grid, List, VideoCamera } from '@element-plus/icons-vue'

export default {
  name: 'VideosView',
  components: {
    VideoCard,
    Grid,
    List,
    VideoCamera
  },
  setup() {
    const store = useStore()
    const router = useRouter()
    const route = useRoute()

    // 响应式数据
    const viewMode = ref('grid')
    const selectedCategory = ref('')
    const selectedSort = ref('latest')
    const currentPage = ref(1)
    const pageSize = ref(12)

    // 分类选项
    const categories = [
      '娱乐', '教育', '音乐', '游戏', '科技', '生活', '体育'
    ]

    // 从store获取数据
    const videos = computed(() => store.getters.allVideos)
    const loading = computed(() => store.getters.isLoading)
    const pagination = computed(() => store.getters.pagination)

    // 计算属性
    const filteredVideos = computed(() => {
      let result = [...(videos.value || [])]

      // 分类筛选
      if (selectedCategory.value) {
        result = result.filter(video => video.category === selectedCategory.value)
      }

      // 排序
      result.sort((a, b) => {
        switch (selectedSort.value) {
          case 'views':
            return b.viewCount - a.viewCount
          case 'likes':
            return b.likeCount - a.likeCount
          case 'shares':
            return b.shareCount - a.shareCount
          case 'latest':
          default:
            return new Date(b.createdAt) - new Date(a.createdAt)
        }
      })

      return result
    })

    const total = computed(() => pagination.value.totalPages * pageSize.value)
    const totalPages = computed(() => pagination.value.totalPages)

    // 方法
    const handleCategoryChange = () => {
      currentPage.value = 1
      fetchVideos()
    }

    const handleSortChange = () => {
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

    const playVideo = (video) => {
      router.push(`/video/${video.id}`)
    }

    const resetFilters = () => {
      selectedCategory.value = ''
      selectedSort.value = 'latest'
      currentPage.value = 1
      fetchVideos()
    }

    const fetchVideos = async () => {
      try {
        const params = {
          page: currentPage.value - 1,
          size: pageSize.value
        }

        if (selectedCategory.value) {
          await store.dispatch('fetchVideosByCategory', selectedCategory.value, params)
        } else {
          await store.dispatch('fetchVideos', params)
        }
      } catch (error) {
        console.error('获取视频失败:', error)
      }
    }

    // 监听路由参数变化
    watch(() => route.query, (newQuery) => {
      if (newQuery.category) {
        selectedCategory.value = newQuery.category
      }
      if (newQuery.sort) {
        selectedSort.value = newQuery.sort
      }
    }, { immediate: true })

    // 初始化
    onMounted(() => {
      fetchVideos()
    })

    return {
      loading,
      viewMode,
      selectedCategory,
      selectedSort,
      currentPage,
      pageSize,
      total,
      categories,
      videos,
      filteredVideos,
      totalPages,
      handleCategoryChange,
      handleSortChange,
      handleSizeChange,
      handleCurrentChange,
      playVideo,
      resetFilters,
      fetchVideos
    }
  }
}
</script>

<style scoped>
.videos-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 页面头部 */
.page-header {
  margin-bottom: 40px;
}

.header-content {
  text-align: center;
  margin-bottom: 32px;
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

/* 筛选栏 */
.filter-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 20px;
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.filter-group {
  display: flex;
  gap: 16px;
}

.filter-group .el-select {
  min-width: 160px;
}

.view-toggle {
  display: flex;
  align-items: center;
}

/* 视频容器 */
.videos-container {
  margin-bottom: 40px;
}

.videos-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
}

.videos-grid.list-view {
  grid-template-columns: 1fr;
}

.list-view .video-card {
  display: flex;
  max-width: none;
}

.list-view .video-thumbnail {
  width: 200px;
  height: 120px;
  flex-shrink: 0;
}

.list-view .video-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

/* 加载状态 */
.loading-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 80px 20px;
  color: #909399;
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: 20px;
  opacity: 0.5;
}

.empty-state h3 {
  font-size: 1.5rem;
  margin-bottom: 12px;
  color: #606266;
}

.empty-state p {
  margin-bottom: 24px;
}

/* 分页 */
.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}

/* 深色模式 */
:deep(.dark-mode) .header-content h1 {
  color: #ffffff;
}

:deep(.dark-mode) .header-content p {
  color: #a8abb2;
}

:deep(.dark-mode) .filter-bar {
  background: rgba(26, 26, 26, 0.8);
  border-color: rgba(255, 255, 255, 0.1);
}

:deep(.dark-mode) .empty-state h3 {
  color: #a8abb2;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .filter-bar {
    flex-direction: column;
    gap: 16px;
  }

  .filter-group {
    width: 100%;
    justify-content: center;
  }

  .filter-group .el-select {
    min-width: 120px;
  }

  .videos-grid {
    grid-template-columns: 1fr;
  }

  .list-view .video-card {
    flex-direction: column;
  }

  .list-view .video-thumbnail {
    width: 100%;
    height: 200px;
  }
}

@media (max-width: 480px) {
  .videos-page {
    padding: 0 16px;
  }

  .header-content h1 {
    font-size: 2rem;
  }

  .filter-group {
    flex-direction: column;
  }

  .filter-group .el-select {
    min-width: none;
  }
}
</style> 