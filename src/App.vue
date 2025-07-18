<template>
  <div id="app" :class="{ 'dark-mode': isDarkMode }">
    <!-- 导航栏 -->
    <nav class="navbar" :class="{ 'navbar-dark': isDarkMode }">
      <div class="nav-container">
        <!-- Logo -->
        <router-link to="/" class="nav-logo">
          <el-icon class="logo-icon"><VideoPlay /></el-icon>
          <span class="logo-text">AI视频推荐</span>
        </router-link>

        <!-- 导航菜单 -->
        <div class="nav-menu" :class="{ 'nav-menu-active': isMenuOpen }">
          <router-link to="/" class="nav-link">
            <el-icon><House /></el-icon>
            <span>首页</span>
          </router-link>
          <router-link to="/videos" class="nav-link">
            <el-icon><VideoCamera /></el-icon>
            <span>视频</span>
          </router-link>
          <router-link to="/search" class="nav-link">
            <el-icon><Search /></el-icon>
            <span>搜索</span>
          </router-link>
          <router-link v-if="isAuthenticated" to="/recommendations" class="nav-link">
            <el-icon><Star /></el-icon>
            <span>推荐</span>
          </router-link>
          <router-link v-if="isAuthenticated" to="/upload" class="nav-link">
            <el-icon><Upload /></el-icon>
            <span>上传</span>
          </router-link>
        </div>

        <!-- 右侧操作区 -->
        <div class="nav-actions">
          <!-- 搜索框 -->
          <div class="search-container">
            <el-input
              v-model="searchQuery"
              placeholder="搜索视频..."
              class="search-input"
              @keyup.enter="handleSearch"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
          </div>

          <!-- 通知中心 -->
          <NotificationCenter v-if="isAuthenticated" />

          <!-- 主题切换 -->
          <el-button
            class="theme-toggle"
            :icon="isDarkMode ? 'Sunny' : 'Moon'"
            circle
            @click="toggleTheme"
          />

          <!-- 用户菜单 -->
          <div v-if="isAuthenticated" class="user-menu">
            <el-dropdown @command="handleUserCommand">
              <div class="user-avatar">
                <el-avatar :size="32" :src="currentUser?.avatar">
                  {{ currentUser?.username?.charAt(0)?.toUpperCase() }}
                </el-avatar>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">
                    <el-icon><User /></el-icon>
                    个人资料
                  </el-dropdown-item>
                  <el-dropdown-item command="settings">
                    <el-icon><Setting /></el-icon>
                    设置
                  </el-dropdown-item>
                  <el-dropdown-item divided command="logout">
                    <el-icon><SwitchButton /></el-icon>
                    退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>

          <!-- 登录/注册按钮 -->
          <div v-else class="auth-buttons">
            <router-link to="/login">
              <el-button type="primary" plain>登录</el-button>
            </router-link>
            <router-link to="/register">
              <el-button type="primary">注册</el-button>
            </router-link>
          </div>

          <!-- 移动端菜单按钮 -->
          <el-button
            class="menu-toggle"
            :icon="isMenuOpen ? 'Close' : 'Menu'"
            circle
            @click="toggleMenu"
          />
        </div>
      </div>
    </nav>

    <!-- 主要内容区域 -->
    <main class="main-content">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>

    <!-- 全局加载状态 -->
    <el-loading
      v-model:fullscreen="isLoading"
      text="加载中..."
      background="rgba(0, 0, 0, 0.8)"
    />
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import {
  House,
  VideoCamera,
  Search,
  Star,
  Upload,
  User,
  Setting,
  SwitchButton,
  VideoPlay,
  Menu,
  Close
} from '@element-plus/icons-vue'
import NotificationCenter from '@/components/NotificationCenter.vue'

export default {
  name: 'App',
  components: {
    House,
    VideoCamera,
    Search,
    Star,
    Upload,
    User,
    Setting,
    SwitchButton,
    VideoPlay,
    Menu,
    Close,
    NotificationCenter
  },
  setup() {
    const store = useStore()
    const router = useRouter()
    
    const searchQuery = ref('')
    const isMenuOpen = ref(false)

    // 计算属性
    const isAuthenticated = computed(() => store.getters.isAuthenticated)
    const currentUser = computed(() => store.getters.currentUser)
    const isLoading = computed(() => store.getters.isLoading)
    const isDarkMode = computed(() => store.getters.isDarkMode)

    // 方法
    const toggleTheme = () => {
      store.commit('TOGGLE_DARK_MODE')
    }

    const toggleMenu = () => {
      isMenuOpen.value = !isMenuOpen.value
    }

    const handleSearch = () => {
      if (searchQuery.value.trim()) {
        router.push({
          name: 'search',
          query: { q: searchQuery.value }
        })
        searchQuery.value = ''
        isMenuOpen.value = false
      }
    }

    const handleUserCommand = (command) => {
      switch (command) {
        case 'profile':
          router.push('/profile')
          break
        case 'settings':
          // 跳转到设置页面
          break
        case 'logout':
          store.dispatch('logout')
          router.push('/')
          break
      }
    }

    // 初始化
    onMounted(() => {
      store.dispatch('initDarkMode')
    })

    return {
      searchQuery,
      isMenuOpen,
      isAuthenticated,
      currentUser,
      isLoading,
      isDarkMode,
      toggleTheme,
      toggleMenu,
      handleSearch,
      handleUserCommand
    }
  }
}
</script>

<style scoped>
#app {
  min-height: 100vh;
  transition: all 0.3s ease;
}

.dark-mode {
  background-color: #1a1a1a;
  color: #ffffff;
}

/* 导航栏样式 */
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.navbar-dark {
  background: rgba(26, 26, 26, 0.95);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.nav-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 70px;
}

/* Logo样式 */
.nav-logo {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: inherit;
  font-size: 1.5rem;
  font-weight: 700;
  transition: all 0.3s ease;
}

.logo-icon {
  margin-right: 8px;
  font-size: 1.8rem;
  color: #409eff;
}

.logo-text {
  background: linear-gradient(135deg, #409eff, #67c23a);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* 导航菜单 */
.nav-menu {
  display: flex;
  align-items: center;
  gap: 20px;
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 6px;
  text-decoration: none;
  color: inherit;
  padding: 8px 16px;
  border-radius: 8px;
  transition: all 0.3s ease;
  font-weight: 500;
}

.nav-link:hover {
  background: rgba(64, 158, 255, 0.1);
  color: #409eff;
}

.nav-link.router-link-active {
  background: rgba(64, 158, 255, 0.15);
  color: #409eff;
}

/* 右侧操作区 */
.nav-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

/* 搜索框 */
.search-container {
  position: relative;
}

.search-input {
  width: 300px;
}

.search-input :deep(.el-input__wrapper) {
  border-radius: 20px;
  background: rgba(0, 0, 0, 0.05);
  border: none;
  transition: all 0.3s ease;
}

.dark-mode .search-input :deep(.el-input__wrapper) {
  background: rgba(255, 255, 255, 0.1);
}

.search-input :deep(.el-input__wrapper:hover) {
  background: rgba(0, 0, 0, 0.1);
}

.dark-mode .search-input :deep(.el-input__wrapper:hover) {
  background: rgba(255, 255, 255, 0.15);
}

/* 主题切换按钮 */
.theme-toggle {
  background: transparent;
  border: 1px solid rgba(0, 0, 0, 0.1);
  color: #606266;
  transition: all 0.3s ease;
}

.dark-mode .theme-toggle {
  border-color: rgba(255, 255, 255, 0.2);
  color: #ffffff;
}

.theme-toggle:hover {
  background: rgba(64, 158, 255, 0.1);
  color: #409eff;
}

/* 用户菜单 */
.user-menu {
  display: flex;
  align-items: center;
}

.user-avatar {
  cursor: pointer;
  transition: all 0.3s ease;
}

.user-avatar:hover {
  transform: scale(1.1);
}

/* 认证按钮 */
.auth-buttons {
  display: flex;
  gap: 12px;
}

.auth-buttons a {
  text-decoration: none;
}

/* 移动端菜单按钮 */
.menu-toggle {
  display: none;
  background: transparent;
  border: 1px solid rgba(0, 0, 0, 0.1);
  color: #606266;
}

.dark-mode .menu-toggle {
  border-color: rgba(255, 255, 255, 0.2);
  color: #ffffff;
}

/* 主要内容区域 */
.main-content {
  margin-top: 70px;
  min-height: calc(100vh - 70px);
  padding: 20px;
}

/* 页面切换动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .nav-container {
    padding: 0 16px;
  }

  .nav-menu {
    position: fixed;
    top: 70px;
    left: 0;
    right: 0;
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(20px);
    flex-direction: column;
    padding: 20px;
    gap: 12px;
    transform: translateY(-100%);
    opacity: 0;
    transition: all 0.3s ease;
  }

  .dark-mode .nav-menu {
    background: rgba(26, 26, 26, 0.95);
  }

  .nav-menu-active {
    transform: translateY(0);
    opacity: 1;
  }

  .nav-link {
    width: 100%;
    justify-content: center;
    padding: 12px 16px;
  }

  .search-container {
    display: none;
  }

  .menu-toggle {
    display: block;
  }

  .logo-text {
    display: none;
  }
}

@media (max-width: 480px) {
  .auth-buttons {
    display: none;
  }
}
</style>
