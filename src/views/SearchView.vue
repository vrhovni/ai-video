<template>
  <div class="search-page">
    <!-- 搜索头部 -->
    <div class="search-header">
      <div class="search-container">
        <el-input
          v-model="searchQuery"
          placeholder="搜索视频、作者或标签..."
          size="large"
          class="search-input"
          @keyup.enter="performSearch"
          @input="handleSearchInput"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
          <template #append>
            <el-button type="primary" @click="performSearch">
              <el-icon><Search /></el-icon>
              搜索
            </el-button>
          </template>
        </el-input>
      </div>

      <!-- 搜索建议 -->
      <div v-if="showSuggestions && searchSuggestions.length > 0" class="search-suggestions">
        <div
          v-for="suggestion in searchSuggestions"
          :key="suggestion"
          class="suggestion-item"
          @click="selectSuggestion(suggestion)"
        >
          <el-icon><Search /></el-icon>
          <span>{{ suggestion }}</span>
        </div>
      </div>
    </div>

    <!-- 高级筛选 -->
    <div v-if="hasSearchResults" class="advanced-filters">
      <div class="filter-section">
        <h3>筛选条件</h3>
        <div class="filter-options">
          <el-select v-model="selectedType" placeholder="搜索类型" @change="applyFilters">
            <el-option label="全部" value="" />
            <el-option label="标题" value="title" />
            <el-option label="描述" value="description" />
            <el-option label="标签" value="tag" />
            <el-option label="作者" value="author" />
          </el-select>

          <el-select v-model="selectedCategory" placeholder="视频分类" @change="applyFilters">
            <el-option label="全部分类" value="" />
            <el-option
              v-for="category in categories"
              :key="category"
              :label="category"
              :value="category"
            />
          </el-select>

          <el-select v-model="selectedDuration" placeholder="时长" @change="applyFilters">
            <el-option label="全部时长" value="" />
            <el-option label="短于4分钟" value="short" />
            <el-option label="4-20分钟" value="medium" />
            <el-option label="长于20分钟" value="long" />
          </el-select>

          <el-select v-model="selectedSort" placeholder="排序方式" @change="applyFilters">
            <el-option label="相关度" value="relevance" />
            <el-option label="最新发布" value="latest" />
            <el-option label="最多观看" value="views" />
            <el-option label="最多点赞" value="likes" />
          </el-select>
        </div>
      </div>
    </div>

    <!-- 搜索结果 -->
    <div v-if="hasSearchResults" class="search-results">
      <div class="results-header">
        <h2>搜索结果</h2>
        <p>找到 {{ totalResults }} 个相关视频</p>
      </div>

      <div class="results-grid">
        <video-card
          v-for="video in searchResults"
          :key="video.id"
          :video="video"
          @click="playVideo(video)"
        />
      </div>

      <!-- 分页 -->
      <div v-if="totalPages > 1" class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="totalResults"
          :page-sizes="[12, 24, 48]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 热门搜索 -->
    <div v-else-if="!searchQuery" class="popular-searches">
      <h2>热门搜索</h2>
      <div class="search-tags">
        <el-tag
          v-for="tag in popularTags"
          :key="tag"
          class="search-tag"
          @click="searchByTag(tag)"
        >
          {{ tag }}
        </el-tag>
      </div>

      <div class="trending-searches">
        <h3>趋势搜索</h3>
        <div class="trending-list">
          <div
            v-for="(trend, index) in trendingSearches"
            :key="trend"
            class="trending-item"
            @click="searchByTag(trend)"
          >
            <span class="trending-rank">{{ index + 1 }}</span>
            <span class="trending-text">{{ trend }}</span>
            <el-icon class="trending-icon"><TrendCharts /></el-icon>
          </div>
        </div>
      </div>
    </div>

    <!-- 空搜索结果 -->
    <div v-else class="empty-results">
      <el-icon class="empty-icon"><Search /></el-icon>
      <h3>未找到相关视频</h3>
      <p>尝试使用不同的关键词或调整搜索条件</p>
      <div class="empty-actions">
        <el-button type="primary" @click="clearSearch">重新搜索</el-button>
        <el-button @click="browseVideos">浏览视频</el-button>
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="3" animated />
      <el-skeleton :rows="3" animated />
      <el-skeleton :rows="3" animated />
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted, watch } from 'vue'
// import { useStore } from 'vuex' // 暂时注释，后续可能需要使用
import { useRouter, useRoute } from 'vue-router'
import VideoCard from '@/components/VideoCard.vue'
import { Search, TrendCharts } from '@element-plus/icons-vue'

export default {
  name: 'SearchView',
  components: {
    VideoCard,
    Search,
    TrendCharts
  },
  setup() {
    // const store = useStore() // 暂时注释，后续可能需要使用
    const router = useRouter()
    const route = useRoute()

    // 响应式数据
    const searchQuery = ref('')
    const loading = ref(false)
    const showSuggestions = ref(false)
    const currentPage = ref(1)
    const pageSize = ref(12)
    const totalResults = ref(0)

    // 筛选条件
    const selectedType = ref('')
    const selectedCategory = ref('')
    const selectedDuration = ref('')
    const selectedSort = ref('relevance')

    // 搜索建议
    const searchSuggestions = ref([
      '人工智能',
      'Vue.js 教程',
      '音乐合集',
      '游戏实况',
      '美食制作',
      '科技前沿'
    ])

    // 热门标签
    const popularTags = ref([
      'AI', 'Vue.js', '音乐', '游戏', '美食', '科技', '教育', '娱乐'
    ])

    // 趋势搜索
    const trendingSearches = ref([
      '人工智能发展',
      'Vue.js 3.0 教程',
      '2024热门音乐',
      '最新游戏实况',
      '家常菜制作',
      '科技产品评测'
    ])

    // 分类选项
    const categories = [
      '娱乐', '教育', '音乐', '游戏', '科技', '生活', '体育'
    ]

    // 模拟搜索结果
    const searchResults = ref([])

    // 计算属性
    const hasSearchResults = computed(() => {
      return searchQuery.value && searchResults.value.length > 0
    })

    const totalPages = computed(() => {
      return Math.ceil(totalResults.value / pageSize.value)
    })

    // 方法
    const performSearch = async () => {
      if (!searchQuery.value.trim()) return
      
      loading.value = true
      showSuggestions.value = false
      
      try {
        // 模拟搜索API调用
        await new Promise(resolve => setTimeout(resolve, 1500))
        
        // 模拟搜索结果
        const mockResults = [
          {
            id: 1,
            title: '人工智能发展现状与未来趋势',
            description: '深度解析AI技术的最新发展动态，探讨人工智能在各个领域的应用前景',
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
            description: '从入门到精通的Vue.js学习指南，包含实战项目开发',
            thumbnail: 'https://via.placeholder.com/300x200/67c23a/ffffff?text=Vue+3',
            duration: '45:20',
            views: 8920,
            likes: 567,
            author: '前端大师',
            category: '教育'
          }
        ]
        
        searchResults.value = mockResults
        totalResults.value = mockResults.length
        currentPage.value = 1
        
        // 更新URL
        router.push({
          query: { q: searchQuery.value }
        })
      } catch (error) {
        console.error('搜索失败:', error)
      } finally {
        loading.value = false
      }
    }

    const handleSearchInput = () => {
      if (searchQuery.value.length > 0) {
        showSuggestions.value = true
      } else {
        showSuggestions.value = false
      }
    }

    const selectSuggestion = (suggestion) => {
      searchQuery.value = suggestion
      showSuggestions.value = false
      performSearch()
    }

    const searchByTag = (tag) => {
      searchQuery.value = tag
      performSearch()
    }

    const applyFilters = () => {
      performSearch()
    }

    const handleSizeChange = (size) => {
      pageSize.value = size
      currentPage.value = 1
      performSearch()
    }

    const handleCurrentChange = (page) => {
      currentPage.value = page
      performSearch()
    }

    const playVideo = (video) => {
      router.push(`/video/${video.id}`)
    }

    const clearSearch = () => {
      searchQuery.value = ''
      searchResults.value = []
      totalResults.value = 0
      router.push({ query: {} })
    }

    const browseVideos = () => {
      router.push('/videos')
    }

    // 监听路由参数
    watch(() => route.query.q, (newQuery) => {
      if (newQuery) {
        searchQuery.value = newQuery
        performSearch()
      }
    }, { immediate: true })

    // 初始化
    onMounted(() => {
      if (route.query.q) {
        searchQuery.value = route.query.q
        performSearch()
      }
    })

    return {
      searchQuery,
      loading,
      showSuggestions,
      searchSuggestions,
      popularTags,
      trendingSearches,
      categories,
      searchResults,
      selectedType,
      selectedCategory,
      selectedDuration,
      selectedSort,
      currentPage,
      pageSize,
      totalResults,
      hasSearchResults,
      totalPages,
      performSearch,
      handleSearchInput,
      selectSuggestion,
      searchByTag,
      applyFilters,
      handleSizeChange,
      handleCurrentChange,
      playVideo,
      clearSearch,
      browseVideos
    }
  }
}
</script>

<style scoped>
.search-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 搜索头部 */
.search-header {
  margin-bottom: 40px;
  position: relative;
}

.search-container {
  max-width: 600px;
  margin: 0 auto;
}

.search-input {
  width: 100%;
}

.search-input :deep(.el-input__wrapper) {
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

/* 搜索建议 */
.search-suggestions {
  position: absolute;
  top: 100%;
  left: 50%;
  transform: translateX(-50%);
  width: 100%;
  max-width: 600px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  margin-top: 8px;
  overflow: hidden;
}

.suggestion-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px 20px;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.suggestion-item:hover {
  background: rgba(64, 158, 255, 0.1);
}

.suggestion-item .el-icon {
  color: #909399;
}

/* 高级筛选 */
.advanced-filters {
  margin-bottom: 40px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 24px;
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.filter-section h3 {
  margin-bottom: 16px;
  color: #303133;
  font-size: 1.1rem;
  font-weight: 600;
}

.filter-options {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}

.filter-options .el-select {
  min-width: 140px;
}

/* 搜索结果 */
.search-results {
  margin-bottom: 40px;
}

.results-header {
  text-align: center;
  margin-bottom: 32px;
}

.results-header h2 {
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: 8px;
  color: #303133;
}

.results-header p {
  color: #606266;
  font-size: 1rem;
}

.results-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
  margin-bottom: 40px;
}

/* 热门搜索 */
.popular-searches {
  text-align: center;
  padding: 60px 20px;
}

.popular-searches h2 {
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: 24px;
  color: #303133;
}

.search-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  justify-content: center;
  margin-bottom: 40px;
}

.search-tag {
  cursor: pointer;
  transition: all 0.3s ease;
}

.search-tag:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.trending-searches {
  max-width: 400px;
  margin: 0 auto;
}

.trending-searches h3 {
  font-size: 1.3rem;
  font-weight: 600;
  margin-bottom: 20px;
  color: #303133;
}

.trending-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.trending-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.trending-item:hover {
  background: rgba(64, 158, 255, 0.1);
  transform: translateX(8px);
}

.trending-rank {
  width: 24px;
  height: 24px;
  background: linear-gradient(135deg, #409eff, #67c23a);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.8rem;
  font-weight: 600;
}

.trending-text {
  flex: 1;
  text-align: left;
  color: #303133;
}

.trending-icon {
  color: #409eff;
}

/* 空搜索结果 */
.empty-results {
  text-align: center;
  padding: 80px 20px;
  color: #909399;
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: 20px;
  opacity: 0.5;
}

.empty-results h3 {
  font-size: 1.5rem;
  margin-bottom: 12px;
  color: #606266;
}

.empty-results p {
  margin-bottom: 24px;
}

.empty-actions {
  display: flex;
  gap: 12px;
  justify-content: center;
}

/* 加载状态 */
.loading-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
}

/* 分页 */
.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}

/* 深色模式 */
:deep(.dark-mode) .search-suggestions {
  background: #1a1a1a;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

:deep(.dark-mode) .suggestion-item {
  color: #ffffff;
}

:deep(.dark-mode) .suggestion-item:hover {
  background: rgba(64, 158, 255, 0.2);
}

:deep(.dark-mode) .advanced-filters {
  background: rgba(26, 26, 26, 0.8);
  border-color: rgba(255, 255, 255, 0.1);
}

:deep(.dark-mode) .filter-section h3 {
  color: #ffffff;
}

:deep(.dark-mode) .results-header h2 {
  color: #ffffff;
}

:deep(.dark-mode) .results-header p {
  color: #a8abb2;
}

:deep(.dark-mode) .popular-searches h2 {
  color: #ffffff;
}

:deep(.dark-mode) .trending-searches h3 {
  color: #ffffff;
}

:deep(.dark-mode) .trending-item {
  background: rgba(26, 26, 26, 0.8);
  color: #ffffff;
}

:deep(.dark-mode) .trending-item:hover {
  background: rgba(64, 158, 255, 0.2);
}

:deep(.dark-mode) .trending-text {
  color: #ffffff;
}

:deep(.dark-mode) .empty-results h3 {
  color: #a8abb2;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .filter-options {
    flex-direction: column;
  }

  .filter-options .el-select {
    min-width: none;
  }

  .results-grid {
    grid-template-columns: 1fr;
  }

  .empty-actions {
    flex-direction: column;
    align-items: center;
  }
}

@media (max-width: 480px) {
  .search-page {
    padding: 0 16px;
  }

  .popular-searches h2 {
    font-size: 1.8rem;
  }

  .results-header h2 {
    font-size: 1.8rem;
  }
}
</style> 