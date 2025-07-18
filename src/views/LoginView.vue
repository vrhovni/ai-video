<template>
  <div class="login-container">
    <div class="login-card">
      <!-- 左侧装饰区域 -->
      <div class="login-decoration">
        <div class="decoration-content">
          <div class="logo-section">
            <el-icon class="logo-icon"><VideoPlay /></el-icon>
            <h1>AI视频推荐</h1>
            <p>发现精彩视频，体验智能推荐</p>
          </div>
          
          <div class="feature-list">
            <div class="feature-item">
              <el-icon><Star /></el-icon>
              <span>个性化推荐算法</span>
            </div>
            <div class="feature-item">
              <el-icon><TrendCharts /></el-icon>
              <span>实时数据分析</span>
            </div>
            <div class="feature-item">
              <el-icon><Lock /></el-icon>
              <span>安全隐私保护</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧登录表单 -->
      <div class="login-form">
        <div class="form-header">
          <h2>欢迎回来</h2>
          <p>登录您的账户以继续</p>
        </div>

        <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          class="login-form-content"
          @submit.prevent="handleLogin"
        >
          <el-form-item prop="username">
            <el-input
              v-model="loginForm.username"
              placeholder="用户名或邮箱"
              size="large"
              prefix-icon="User"
            />
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="密码"
              size="large"
              prefix-icon="Lock"
              show-password
            />
          </el-form-item>

          <div class="form-options">
            <el-checkbox v-model="rememberMe">记住我</el-checkbox>
            <el-link type="primary">忘记密码？</el-link>
          </div>

          <el-form-item>
            <el-button
              type="primary"
              size="large"
              class="login-button"
              :loading="loading"
              @click="handleLogin"
            >
              {{ loading ? '登录中...' : '登录' }}
            </el-button>
          </el-form-item>
        </el-form>

        <div class="form-footer">
          <p>还没有账户？</p>
          <router-link to="/register" class="register-link">
            <el-button type="primary" plain>立即注册</el-button>
          </router-link>
        </div>

        <!-- 社交登录 -->
        <div class="social-login">
          <div class="divider">
            <span>或使用以下方式登录</span>
          </div>
          
          <div class="social-buttons">
            <el-button class="social-button wechat">
              <el-icon><ChatDotRound /></el-icon>
              微信登录
            </el-button>
            <el-button class="social-button qq">
              <el-icon><Message /></el-icon>
              QQ登录
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  VideoPlay,
  Star,
  TrendCharts,
  Lock,
      ChatDotRound,
    Message
} from '@element-plus/icons-vue'
import { userApi } from '@/services/api'

export default {
  name: 'LoginView',
  components: {
    VideoPlay,
    Star,
    TrendCharts,
    Lock,
    ChatDotRound,
    Message
  },
  setup() {
    const store = useStore()
    const router = useRouter()
    const loginFormRef = ref()
    
    const loading = ref(false)
    const rememberMe = ref(false)

    const loginForm = reactive({
      username: '',
      password: ''
    })

    const loginRules = {
      username: [
        { required: true, message: '请输入用户名或邮箱', trigger: 'blur' },
        { min: 3, message: '用户名至少3个字符', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, message: '密码至少6个字符', trigger: 'blur' }
      ]
    }

    const handleLogin = async () => {
      if (!loginFormRef.value) return
      try {
        await loginFormRef.value.validate()
        loading.value = true
        // 调用后端登录API
        const result = await userApi.login({
          username: loginForm.username,
          password: loginForm.password
        })
        
        // 检查响应状态
        if (result && result.success && result.data && result.data.id) {
          // 登录成功，保存用户信息
          store.commit('SET_USER', result.data)
          ElMessage.success('登录成功！')
          router.push('/')
        } else {
          ElMessage.error(result.message || '用户名或密码错误')
        }
      } catch (error) {
        console.error('登录失败:', error)
        ElMessage.error('登录失败，请检查用户名和密码')
      } finally {
        loading.value = false
      }
    }

    return {
      loginFormRef,
      loading,
      rememberMe,
      loginForm,
      loginRules,
      handleLogin
    }
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  display: grid;
  grid-template-columns: 1fr 1fr;
  max-width: 1000px;
  width: 100%;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  overflow: hidden;
  box-shadow: 0 32px 64px rgba(0, 0, 0, 0.1);
}

/* 左侧装饰区域 */
.login-decoration {
  background: linear-gradient(135deg, #409eff, #67c23a);
  padding: 60px 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  position: relative;
  overflow: hidden;
}

.login-decoration::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.1) 0%, transparent 70%);
  animation: rotate 20s linear infinite;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.decoration-content {
  position: relative;
  z-index: 1;
  text-align: center;
}

.logo-section {
  margin-bottom: 60px;
}

.logo-icon {
  font-size: 4rem;
  margin-bottom: 20px;
}

.logo-section h1 {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 12px;
}

.logo-section p {
  font-size: 1.1rem;
  opacity: 0.9;
}

.feature-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 1.1rem;
}

.feature-item .el-icon {
  font-size: 1.5rem;
}

/* 右侧登录表单 */
.login-form {
  padding: 60px 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.form-header {
  text-align: center;
  margin-bottom: 40px;
}

.form-header h2 {
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: 8px;
  color: #303133;
}

.form-header p {
  color: #606266;
  font-size: 1rem;
}

.login-form-content {
  margin-bottom: 32px;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.login-button {
  width: 100%;
  height: 48px;
  font-size: 1.1rem;
  font-weight: 600;
}

.form-footer {
  text-align: center;
  margin-bottom: 32px;
}

.form-footer p {
  color: #606266;
  margin-bottom: 12px;
}

.register-link {
  text-decoration: none;
}

/* 社交登录 */
.social-login {
  text-align: center;
}

.divider {
  position: relative;
  margin-bottom: 24px;
}

.divider::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 1px;
  background: rgba(0, 0, 0, 0.1);
}

.divider span {
  background: white;
  padding: 0 16px;
  color: #909399;
  font-size: 0.9rem;
}

.social-buttons {
  display: flex;
  gap: 12px;
  justify-content: center;
}

.social-button {
  flex: 1;
  max-width: 160px;
  height: 44px;
  border-radius: 8px;
  font-weight: 500;
}

.social-button.wechat {
  background: #07c160;
  border-color: #07c160;
  color: white;
}

.social-button.wechat:hover {
  background: #06ad56;
  border-color: #06ad56;
}

.social-button.qq {
  background: #12b7f5;
  border-color: #12b7f5;
  color: white;
}

.social-button.qq:hover {
  background: #0fa3d9;
  border-color: #0fa3d9;
}

/* 深色模式 */
:deep(.dark-mode) .login-card {
  background: rgba(26, 26, 26, 0.95);
}

:deep(.dark-mode) .form-header h2 {
  color: #ffffff;
}

:deep(.dark-mode) .form-header p {
  color: #a8abb2;
}

:deep(.dark-mode) .divider::before {
  background: rgba(255, 255, 255, 0.1);
}

:deep(.dark-mode) .divider span {
  background: #1a1a1a;
  color: #a8abb2;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-card {
    grid-template-columns: 1fr;
    max-width: 400px;
  }

  .login-decoration {
    display: none;
  }

  .login-form {
    padding: 40px 24px;
  }

  .social-buttons {
    flex-direction: column;
  }

  .social-button {
    max-width: none;
  }
}

@media (max-width: 480px) {
  .login-container {
    padding: 16px;
  }

  .login-form {
    padding: 32px 20px;
  }

  .form-header h2 {
    font-size: 1.8rem;
  }
}
</style> 