import { createRouter, createWebHistory } from 'vue-router'
import { useStore } from 'vuex'

const routes = [
  {
    path: '/',
    name: 'home',
    component: () => import('../views/HomeView.vue'),
    meta: { title: '首页' }
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/LoginView.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('../views/RegisterView.vue'),
    meta: { title: '注册' }
  },
  {
    path: '/videos',
    name: 'videos',
    component: () => import('../views/VideosView.vue'),
    meta: { title: '视频列表' }
  },
  {
    path: '/video/:id',
    name: 'video-detail',
    component: () => import('../views/VideoDetailView.vue'),
    meta: { title: '视频详情' }
  },
  {
    path: '/search',
    name: 'search',
    component: () => import('../views/SearchView.vue'),
    meta: { title: '搜索' }
  },
  {
    path: '/category/:category',
    name: 'category',
    component: () => import('../views/CategoryView.vue'),
    meta: { title: '分类' }
  },
  {
    path: '/recommendations',
    name: 'recommendations',
    component: () => import('../views/RecommendationsView.vue'),
    meta: { title: '推荐', requiresAuth: true }
  },
  {
    path: '/profile',
    name: 'profile',
    component: () => import('../views/ProfileView.vue'),
    meta: { title: '个人资料', requiresAuth: true }
  },
  {
    path: '/upload',
    name: 'upload',
    component: () => import('../views/UploadView.vue'),
    meta: { title: '上传视频', requiresAuth: true }
  },
  {
    path: '/about',
    name: 'about',
    component: () => import('../views/AboutView.vue'),
    meta: { title: '关于' }
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  }
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - AI视频推荐` : 'AI视频推荐'
  
  // 检查是否需要认证
  if (to.meta.requiresAuth) {
    const store = useStore()
    if (!store.getters.isAuthenticated) {
      next('/login')
      return
    }
  }
  
  next()
})

export default router
