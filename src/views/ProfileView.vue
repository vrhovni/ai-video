<template>
  <div class="profile-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <h1>个人资料</h1>
      <p>管理您的账户信息和偏好设置</p>
    </div>

    <div class="profile-content">
      <!-- 左侧个人信息 -->
      <div class="profile-sidebar">
        <el-card class="profile-card">
          <div class="profile-avatar">
            <el-avatar :size="120" :src="userInfo.avatar">
              {{ userInfo.username?.charAt(0)?.toUpperCase() }}
            </el-avatar>
            <el-button type="primary" size="small" class="change-avatar-btn">
              <el-icon><Camera /></el-icon>
              更换头像
            </el-button>
          </div>
          
          <div class="profile-info">
            <h2>{{ userInfo.username }}</h2>
            <p class="user-email">{{ userInfo.email }}</p>
            <p class="user-join-date">加入时间：{{ formatDate(userInfo.createdAt) }}</p>
          </div>
          
          <div class="profile-stats">
            <div class="stat-item">
              <h3>{{ userStats.watchedVideos }}</h3>
              <p>观看视频</p>
            </div>
            <div class="stat-item">
              <h3>{{ userStats.likedVideos }}</h3>
              <p>点赞视频</p>
            </div>
            <div class="stat-item">
              <h3>{{ userStats.favoriteVideos }}</h3>
              <p>收藏视频</p>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 右侧设置内容 -->
      <div class="profile-main">
        <el-tabs v-model="activeTab" class="profile-tabs">
          <!-- 基本信息 -->
          <el-tab-pane label="基本信息" name="basic">
            <el-card class="tab-card">
              <template #header>
                <h3>基本信息</h3>
              </template>
              
              <el-form
                ref="basicFormRef"
                :model="basicForm"
                :rules="basicRules"
                label-width="100px"
              >
                <el-form-item label="用户名" prop="username">
                  <el-input v-model="basicForm.username" />
                </el-form-item>
                
                <el-form-item label="邮箱" prop="email">
                  <el-input v-model="basicForm.email" />
                </el-form-item>
                
                <el-form-item label="昵称" prop="nickname">
                  <el-input v-model="basicForm.nickname" />
                </el-form-item>
                
                <el-form-item label="个人简介" prop="bio">
                  <el-input
                    v-model="basicForm.bio"
                    type="textarea"
                    :rows="3"
                    placeholder="介绍一下自己..."
                  />
                </el-form-item>
                
                <el-form-item>
                  <el-button type="primary" @click="saveBasicInfo">保存更改</el-button>
                </el-form-item>
              </el-form>
            </el-card>
          </el-tab-pane>

          <!-- 安全设置 -->
          <el-tab-pane label="安全设置" name="security">
            <el-card class="tab-card">
              <template #header>
                <h3>安全设置</h3>
              </template>
              
              <div class="security-settings">
                <div class="setting-item">
                  <div class="setting-info">
                    <h4>修改密码</h4>
                    <p>定期更换密码以确保账户安全</p>
                  </div>
                  <el-button @click="showChangePasswordDialog = true">修改密码</el-button>
                </div>
                
                <div class="setting-item">
                  <div class="setting-info">
                    <h4>两步验证</h4>
                    <p>启用两步验证以提高账户安全性</p>
                  </div>
                  <el-switch v-model="securitySettings.twoFactor" />
                </div>
                
                <div class="setting-item">
                  <div class="setting-info">
                    <h4>登录通知</h4>
                    <p>在新设备登录时发送邮件通知</p>
                  </div>
                  <el-switch v-model="securitySettings.loginNotification" />
                </div>
              </div>
            </el-card>
          </el-tab-pane>

          <!-- 偏好设置 -->
          <el-tab-pane label="偏好设置" name="preferences">
            <el-card class="tab-card">
              <template #header>
                <h3>偏好设置</h3>
              </template>
              
              <div class="preference-settings">
                <div class="setting-group">
                  <h4>推荐偏好</h4>
                  <div class="preference-item">
                    <label>兴趣标签</label>
                    <div class="interest-tags">
                      <el-tag
                        v-for="tag in preferenceSettings.interests"
                        :key="tag"
                        :closable="true"
                        @close="removeInterest(tag)"
                      >
                        {{ tag }}
                      </el-tag>
                      <el-button size="small" @click="showAddInterestDialog = true">
                        <el-icon><Plus /></el-icon>
                        添加兴趣
                      </el-button>
                    </div>
                  </div>
                  
                  <div class="preference-item">
                    <label>推荐算法</label>
                    <el-radio-group v-model="preferenceSettings.algorithm">
                      <el-radio label="personalized">个性化推荐</el-radio>
                      <el-radio label="collaborative">协同过滤</el-radio>
                      <el-radio label="content">内容推荐</el-radio>
                      <el-radio label="hybrid">混合推荐</el-radio>
                    </el-radio-group>
                  </div>
                </div>
                
                <div class="setting-group">
                  <h4>界面设置</h4>
                  <div class="preference-item">
                    <label>主题模式</label>
                    <el-radio-group v-model="preferenceSettings.theme">
                      <el-radio label="light">浅色模式</el-radio>
                      <el-radio label="dark">深色模式</el-radio>
                      <el-radio label="auto">跟随系统</el-radio>
                    </el-radio-group>
                  </div>
                  
                  <div class="preference-item">
                    <label>语言设置</label>
                    <el-select v-model="preferenceSettings.language">
                      <el-option label="简体中文" value="zh-CN" />
                      <el-option label="English" value="en-US" />
                    </el-select>
                  </div>
                </div>
                
                <div class="setting-group">
                  <h4>通知设置</h4>
                  <div class="preference-item">
                    <label>推荐通知</label>
                    <el-switch v-model="preferenceSettings.recommendationNotification" />
                  </div>
                  
                  <div class="preference-item">
                    <label>点赞通知</label>
                    <el-switch v-model="preferenceSettings.likeNotification" />
                  </div>
                  
                  <div class="preference-item">
                    <label>评论通知</label>
                    <el-switch v-model="preferenceSettings.commentNotification" />
                  </div>
                </div>
              </div>
            </el-card>
          </el-tab-pane>

          <!-- 隐私设置 -->
          <el-tab-pane label="隐私设置" name="privacy">
            <el-card class="tab-card">
              <template #header>
                <h3>隐私设置</h3>
              </template>
              
              <div class="privacy-settings">
                <div class="setting-item">
                  <div class="setting-info">
                    <h4>观看历史</h4>
                    <p>允许平台记录您的观看历史以提供个性化推荐</p>
                  </div>
                  <el-switch v-model="privacySettings.watchHistory" />
                </div>
                
                <div class="setting-item">
                  <div class="setting-info">
                    <h4>搜索历史</h4>
                    <p>保存您的搜索历史以改善搜索体验</p>
                  </div>
                  <el-switch v-model="privacySettings.searchHistory" />
                </div>
                
                <div class="setting-item">
                  <div class="setting-info">
                    <h4>数据分析</h4>
                    <p>允许平台分析您的使用数据以改进服务</p>
                  </div>
                  <el-switch v-model="privacySettings.dataAnalysis" />
                </div>
                
                <div class="setting-item">
                  <div class="setting-info">
                    <h4>个性化广告</h4>
                    <p>基于您的兴趣显示个性化广告</p>
                  </div>
                  <el-switch v-model="privacySettings.personalizedAds" />
                </div>
              </div>
            </el-card>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <!-- 修改密码对话框 -->
    <el-dialog
      v-model="showChangePasswordDialog"
      title="修改密码"
      width="400px"
    >
      <el-form
        ref="passwordFormRef"
        :model="passwordForm"
        :rules="passwordRules"
        label-width="100px"
      >
        <el-form-item label="当前密码" prop="currentPassword">
          <el-input
            v-model="passwordForm.currentPassword"
            type="password"
            show-password
          />
        </el-form-item>
        
        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="passwordForm.newPassword"
            type="password"
            show-password
          />
        </el-form-item>
        
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="passwordForm.confirmPassword"
            type="password"
            show-password
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showChangePasswordDialog = false">取消</el-button>
          <el-button type="primary" @click="changePassword">确认修改</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 添加兴趣对话框 -->
    <el-dialog
      v-model="showAddInterestDialog"
      title="添加兴趣标签"
      width="400px"
    >
      <el-form :model="newInterestForm" label-width="80px">
        <el-form-item label="兴趣名称">
          <el-input v-model="newInterestForm.name" placeholder="请输入兴趣名称" />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showAddInterestDialog = false">取消</el-button>
          <el-button type="primary" @click="addInterest">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import {
  Camera,
  Plus
} from '@element-plus/icons-vue'

export default {
  name: 'ProfileView',
  components: {
    Camera,
    Plus
  },
  setup() {
    const store = useStore()

    // 响应式数据
    const activeTab = ref('basic')
    const showChangePasswordDialog = ref(false)
    const showAddInterestDialog = ref(false)

    // 用户信息
    const userInfo = reactive({
      username: 'user123',
      email: 'user123@example.com',
      avatar: 'https://via.placeholder.com/120x120/409eff/ffffff?text=U',
      nickname: '用户昵称',
      bio: '这是一个个人简介...',
      createdAt: '2024-01-01T00:00:00Z'
    })

    // 用户统计
    const userStats = reactive({
      watchedVideos: 156,
      likedVideos: 89,
      favoriteVideos: 23
    })

    // 基本信息表单
    const basicForm = reactive({
      username: userInfo.username,
      email: userInfo.email,
      nickname: userInfo.nickname,
      bio: userInfo.bio
    })

    const basicRules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
      ],
      email: [
        { required: true, message: '请输入邮箱', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
      ]
    }

    // 安全设置
    const securitySettings = reactive({
      twoFactor: false,
      loginNotification: true
    })

    // 偏好设置
    const preferenceSettings = reactive({
      interests: ['科技', '教育', '音乐'],
      algorithm: 'personalized',
      theme: 'auto',
      language: 'zh-CN',
      recommendationNotification: true,
      likeNotification: true,
      commentNotification: false
    })

    // 隐私设置
    const privacySettings = reactive({
      watchHistory: true,
      searchHistory: true,
      dataAnalysis: true,
      personalizedAds: false
    })

    // 密码表单
    const passwordForm = reactive({
      currentPassword: '',
      newPassword: '',
      confirmPassword: ''
    })

    const passwordRules = {
      currentPassword: [
        { required: true, message: '请输入当前密码', trigger: 'blur' }
      ],
      newPassword: [
        { required: true, message: '请输入新密码', trigger: 'blur' },
        { min: 6, message: '密码至少6个字符', trigger: 'blur' }
      ],
      confirmPassword: [
        { required: true, message: '请确认新密码', trigger: 'blur' },
        {
          validator: (rule, value, callback) => {
            if (value !== passwordForm.newPassword) {
              callback(new Error('两次输入密码不一致'))
            } else {
              callback()
            }
          },
          trigger: 'blur'
        }
      ]
    }

    // 新兴趣表单
    const newInterestForm = reactive({
      name: ''
    })

    // 方法
    const formatDate = (dateString) => {
      return new Date(dateString).toLocaleDateString('zh-CN')
    }

    const saveBasicInfo = () => {
      ElMessage.success('基本信息已保存')
    }

    const changePassword = () => {
      ElMessage.success('密码修改成功')
      showChangePasswordDialog.value = false
      // 重置表单
      Object.keys(passwordForm).forEach(key => {
        passwordForm[key] = ''
      })
    }

    const removeInterest = (tag) => {
      const index = preferenceSettings.interests.indexOf(tag)
      if (index > -1) {
        preferenceSettings.interests.splice(index, 1)
        ElMessage.success(`已移除兴趣: ${tag}`)
      }
    }

    const addInterest = () => {
      if (newInterestForm.name.trim()) {
        preferenceSettings.interests.push(newInterestForm.name)
        ElMessage.success(`已添加兴趣: ${newInterestForm.name}`)
        newInterestForm.name = ''
        showAddInterestDialog.value = false
      } else {
        ElMessage.warning('请输入兴趣名称')
      }
    }

    // 初始化
    onMounted(() => {
      // 获取用户信息
      const currentUser = store.getters.currentUser
      if (currentUser) {
        Object.assign(userInfo, currentUser)
        Object.assign(basicForm, {
          username: currentUser.username,
          email: currentUser.email,
          nickname: currentUser.nickname || '',
          bio: currentUser.bio || ''
        })
      }
    })

    return {
      activeTab,
      showChangePasswordDialog,
      showAddInterestDialog,
      userInfo,
      userStats,
      basicForm,
      basicRules,
      securitySettings,
      preferenceSettings,
      privacySettings,
      passwordForm,
      passwordRules,
      newInterestForm,
      formatDate,
      saveBasicInfo,
      changePassword,
      removeInterest,
      addInterest
    }
  }
}
</script>

<style scoped>
.profile-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 页面头部 */
.page-header {
  text-align: center;
  margin-bottom: 40px;
}

.page-header h1 {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 12px;
  color: #303133;
}

.page-header p {
  font-size: 1.1rem;
  color: #606266;
}

/* 主要内容 */
.profile-content {
  display: grid;
  grid-template-columns: 300px 1fr;
  gap: 40px;
}

/* 侧边栏 */
.profile-sidebar {
  position: sticky;
  top: 100px;
  height: fit-content;
}

.profile-card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.profile-avatar {
  text-align: center;
  margin-bottom: 24px;
}

.change-avatar-btn {
  margin-top: 12px;
}

.profile-info {
  text-align: center;
  margin-bottom: 24px;
}

.profile-info h2 {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 8px;
  color: #303133;
}

.user-email {
  color: #606266;
  margin-bottom: 4px;
}

.user-join-date {
  color: #909399;
  font-size: 0.9rem;
}

.profile-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.stat-item {
  text-align: center;
  padding: 16px;
  background: rgba(64, 158, 255, 0.1);
  border-radius: 8px;
}

.stat-item h3 {
  font-size: 1.5rem;
  font-weight: 700;
  color: #409eff;
  margin-bottom: 4px;
}

.stat-item p {
  color: #606266;
  font-size: 0.9rem;
}

/* 主内容区 */
.profile-main {
  min-height: 600px;
}

.tab-card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.tab-card :deep(.el-card__header) {
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
}

.tab-card h3 {
  margin: 0;
  font-size: 1.3rem;
  font-weight: 600;
  color: #303133;
}

/* 安全设置 */
.security-settings {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.setting-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 0;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
}

.setting-item:last-child {
  border-bottom: none;
}

.setting-info h4 {
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 4px;
  color: #303133;
}

.setting-info p {
  color: #606266;
  font-size: 0.9rem;
}

/* 偏好设置 */
.preference-settings {
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.setting-group h4 {
  font-size: 1.2rem;
  font-weight: 600;
  margin-bottom: 16px;
  color: #303133;
}

.preference-item {
  margin-bottom: 20px;
}

.preference-item label {
  display: block;
  font-weight: 600;
  margin-bottom: 8px;
  color: #303133;
}

.interest-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  align-items: center;
}

/* 隐私设置 */
.privacy-settings {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* 深色模式 */
:deep(.dark-mode) .page-header h1 {
  color: #ffffff;
}

:deep(.dark-mode) .page-header p {
  color: #a8abb2;
}

:deep(.dark-mode) .profile-card,
:deep(.dark-mode) .tab-card {
  background: rgba(26, 26, 26, 0.8);
  border-color: rgba(255, 255, 255, 0.1);
}

:deep(.dark-mode) .profile-info h2,
:deep(.dark-mode) .tab-card h3,
:deep(.dark-mode) .setting-info h4,
:deep(.dark-mode) .setting-group h4,
:deep(.dark-mode) .preference-item label {
  color: #ffffff;
}

:deep(.dark-mode) .user-email,
:deep(.dark-mode) .setting-info p {
  color: #a8abb2;
}

:deep(.dark-mode) .user-join-date {
  color: #909399;
}

:deep(.dark-mode) .setting-item {
  border-bottom-color: rgba(255, 255, 255, 0.1);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .profile-content {
    grid-template-columns: 1fr;
    gap: 24px;
  }

  .profile-sidebar {
    position: static;
  }

  .profile-stats {
    grid-template-columns: 1fr;
  }

  .setting-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
}

@media (max-width: 480px) {
  .profile-page {
    padding: 0 16px;
  }

  .page-header h1 {
    font-size: 2rem;
  }
}
</style> 