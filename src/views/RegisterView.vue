<template>
  <div class="register-container">
    <div class="register-card">
      <!-- 左侧装饰区域 -->
      <div class="register-decoration">
        <div class="decoration-content">
          <div class="logo-section">
            <el-icon class="logo-icon"><VideoPlay /></el-icon>
            <h1>加入我们</h1>
            <p>开启您的AI视频推荐之旅</p>
          </div>
          
          <div class="benefit-list">
            <div class="benefit-item">
              <el-icon><Star /></el-icon>
              <div>
                <h3>个性化推荐</h3>
                <p>基于AI算法的智能内容推荐</p>
              </div>
            </div>
            <div class="benefit-item">
              <el-icon><TrendCharts /></el-icon>
              <div>
                <h3>实时分析</h3>
                <p>深度分析您的观看偏好</p>
              </div>
            </div>
            <div class="benefit-item">
              <el-icon><Lock /></el-icon>
              <div>
                <h3>隐私保护</h3>
                <p>严格的数据安全保护措施</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧注册表单 -->
      <div class="register-form">
        <div class="form-header">
          <h2>创建账户</h2>
          <p>填写以下信息完成注册</p>
        </div>

        <el-form
          ref="registerFormRef"
          :model="registerForm"
          :rules="registerRules"
          class="register-form-content"
          @submit.prevent="handleRegister"
        >
          <el-form-item prop="username">
            <el-input
              v-model="registerForm.username"
              placeholder="用户名"
              size="large"
              prefix-icon="User"
            />
          </el-form-item>

          <el-form-item prop="email">
            <el-input
              v-model="registerForm.email"
              placeholder="邮箱地址"
              size="large"
              prefix-icon="Message"
            />
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              v-model="registerForm.password"
              type="password"
              placeholder="密码"
              size="large"
              prefix-icon="Lock"
              show-password
            />
          </el-form-item>

          <el-form-item prop="confirmPassword">
            <el-input
              v-model="registerForm.confirmPassword"
              type="password"
              placeholder="确认密码"
              size="large"
              prefix-icon="Lock"
              show-password
            />
          </el-form-item>

          <el-form-item prop="agreement">
            <el-checkbox v-model="registerForm.agreement">
              我已阅读并同意
              <el-link type="primary">服务条款</el-link>
              和
              <el-link type="primary">隐私政策</el-link>
            </el-checkbox>
          </el-form-item>

          <el-form-item>
            <el-button
              type="primary"
              size="large"
              class="register-button"
              :loading="loading"
              @click="handleRegister"
            >
              {{ loading ? '注册中...' : '立即注册' }}
            </el-button>
          </el-form-item>
        </el-form>

        <div class="form-footer">
          <p>已有账户？</p>
          <router-link to="/login" class="login-link">
            <el-button type="primary" plain>立即登录</el-button>
          </router-link>
        </div>

        <!-- 社交注册 -->
        <div class="social-register">
          <div class="divider">
            <span>或使用以下方式注册</span>
          </div>
          
          <div class="social-buttons">
            <el-button class="social-button wechat">
              <el-icon><ChatDotRound /></el-icon>
              微信注册
            </el-button>
            <el-button class="social-button qq">
              <el-icon><Message /></el-icon>
              QQ注册
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
      Message,
    ChatDotRound
} from '@element-plus/icons-vue'

export default {
  name: 'RegisterView',
  components: {
    VideoPlay,
    Star,
    TrendCharts,
    Lock,
    Message,
    ChatDotRound
  },
  setup() {
    const store = useStore()
    const router = useRouter()
    const registerFormRef = ref()
    
    const loading = ref(false)

    const registerForm = reactive({
      username: '',
      email: '',
      password: '',
      confirmPassword: '',
      agreement: false
    })

    const validateConfirmPassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== registerForm.password) {
        callback(new Error('两次输入密码不一致'))
      } else {
        callback()
      }
    }

    const validateAgreement = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请阅读并同意服务条款和隐私政策'))
      } else {
        callback()
      }
    }

    const registerRules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' },
        { pattern: /^[a-zA-Z0-9_]+$/, message: '用户名只能包含字母、数字和下划线', trigger: 'blur' }
      ],
      email: [
        { required: true, message: '请输入邮箱地址', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, message: '密码至少6个字符', trigger: 'blur' },
        { pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)/, message: '密码必须包含大小写字母和数字', trigger: 'blur' }
      ],
      confirmPassword: [
        { required: true, validator: validateConfirmPassword, trigger: 'blur' }
      ],
      agreement: [
        { required: true, validator: validateAgreement, trigger: 'change' }
      ]
    }

    const handleRegister = async () => {
      if (!registerFormRef.value) return
      
      try {
        await registerFormRef.value.validate()
        loading.value = true
        
        // 构建注册数据
        const registerData = {
          username: registerForm.username,
          email: registerForm.email,
          password: registerForm.password,
          nickName: registerForm.username,
          role: 'USER'
        }
        
        // 调用注册API
        await store.dispatch('register', registerData)
        
        ElMessage.success('注册成功！欢迎加入AI视频推荐平台')
        router.push('/')
      } catch (error) {
        console.error('注册失败:', error)
        ElMessage.error('注册失败，请检查输入信息')
      } finally {
        loading.value = false
      }
    }

    return {
      registerFormRef,
      loading,
      registerForm,
      registerRules,
      handleRegister
    }
  }
}
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.register-card {
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
.register-decoration {
  background: linear-gradient(135deg, #409eff, #67c23a);
  padding: 60px 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  position: relative;
  overflow: hidden;
}

.register-decoration::before {
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

.benefit-list {
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.benefit-item {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  text-align: left;
}

.benefit-item .el-icon {
  font-size: 2rem;
  margin-top: 4px;
}

.benefit-item h3 {
  font-size: 1.2rem;
  font-weight: 600;
  margin-bottom: 4px;
}

.benefit-item p {
  font-size: 0.9rem;
  opacity: 0.9;
  line-height: 1.4;
}

/* 右侧注册表单 */
.register-form {
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

.register-form-content {
  margin-bottom: 32px;
}

.register-button {
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

.login-link {
  text-decoration: none;
}

/* 社交注册 */
.social-register {
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
:deep(.dark-mode) .register-card {
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
  .register-card {
    grid-template-columns: 1fr;
    max-width: 400px;
  }

  .register-decoration {
    display: none;
  }

  .register-form {
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
  .register-container {
    padding: 16px;
  }

  .register-form {
    padding: 32px 20px;
  }

  .form-header h2 {
    font-size: 1.8rem;
  }
}
</style> 