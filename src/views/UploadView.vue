<template>
  <div class="upload-view">
    <div class="upload-container">
      <!-- 页面标题 -->
      <div class="page-header">
        <h1>上传视频</h1>
        <p>分享您的精彩内容，让更多人看到</p>
      </div>

      <!-- 上传步骤 -->
      <el-steps :active="currentStep" finish-status="success" class="upload-steps">
        <el-step title="选择视频" description="上传您的视频文件" />
        <el-step title="填写信息" description="添加标题、描述和标签" />
        <el-step title="预览发布" description="确认信息并发布" />
      </el-steps>

      <!-- 步骤1：选择视频 -->
      <div v-if="currentStep === 0" class="step-content">
        <div class="upload-area">
          <el-upload
            ref="uploadRef"
            class="video-upload"
            drag
            :auto-upload="false"
            :show-file-list="false"
            :on-change="handleFileChange"
            :before-upload="beforeUpload"
            accept="video/*"
          >
            <div class="upload-drag-area">
              <el-icon class="upload-icon"><Upload /></el-icon>
              <div class="upload-text">
                <h3>拖拽视频文件到此处或点击上传</h3>
                <p>支持 MP4, AVI, MOV, WMV 等格式，最大 500MB</p>
              </div>
            </div>
          </el-upload>
        </div>

        <!-- 文件信息 -->
        <div v-if="selectedFile" class="file-info">
          <div class="file-details">
            <el-icon class="file-icon"><VideoPlay /></el-icon>
            <div class="file-text">
              <h4>{{ selectedFile.name }}</h4>
              <p>{{ formatFileSize(selectedFile.size) }}</p>
            </div>
            <el-button @click="removeFile" type="danger" text>
              <el-icon><Delete /></el-icon>
            </el-button>
          </div>
        </div>

        <!-- 上传进度 -->
        <div v-if="uploadProgress > 0 && uploadProgress < 100" class="upload-progress">
          <el-progress
            :percentage="uploadProgress"
            :stroke-width="8"
            status="success"
          />
          <p class="progress-text">正在上传... {{ uploadProgress }}%</p>
        </div>
      </div>

      <!-- 步骤2：填写信息 -->
      <div v-if="currentStep === 1" class="step-content">
        <el-form
          ref="videoFormRef"
          :model="videoForm"
          :rules="videoRules"
          label-width="100px"
          class="video-form"
        >
          <!-- 视频预览 -->
          <div class="video-preview">
            <video
              v-if="videoPreviewUrl"
              :src="videoPreviewUrl"
              controls
              class="preview-video"
            />
            <div v-else class="preview-placeholder">
              <el-icon><VideoPlay /></el-icon>
              <p>视频预览</p>
            </div>
          </div>

          <!-- 基本信息 -->
          <el-form-item label="视频标题" prop="title">
            <el-input
              v-model="videoForm.title"
              placeholder="请输入视频标题"
              maxlength="100"
              show-word-limit
            />
          </el-form-item>

          <el-form-item label="视频描述" prop="description">
            <el-input
              v-model="videoForm.description"
              type="textarea"
              :rows="4"
              placeholder="请输入视频描述"
              maxlength="1000"
              show-word-limit
            />
          </el-form-item>

          <el-form-item label="视频分类" prop="category">
            <el-select
              v-model="videoForm.category"
              placeholder="请选择视频分类"
              style="width: 100%"
            >
              <el-option
                v-for="category in categories"
                :key="category"
                :label="category"
                :value="category"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="视频标签">
            <el-select
              v-model="videoForm.tags"
              multiple
              filterable
              allow-create
              default-first-option
              placeholder="请选择或输入标签"
              style="width: 100%"
            >
              <el-option
                v-for="tag in popularTags"
                :key="tag"
                :label="tag"
                :value="tag"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="封面图片">
            <el-upload
              class="cover-upload"
              :auto-upload="false"
              :show-file-list="false"
              :on-change="handleCoverChange"
              accept="image/*"
            >
              <div class="cover-upload-area">
                <img
                  v-if="coverPreviewUrl"
                  :src="coverPreviewUrl"
                  class="cover-preview"
                />
                <div v-else class="cover-placeholder">
                  <el-icon><Picture /></el-icon>
                  <p>上传封面</p>
                </div>
              </div>
            </el-upload>
          </el-form-item>

          <!-- 隐私设置 -->
          <el-form-item label="隐私设置">
            <el-radio-group v-model="videoForm.privacy">
              <el-radio label="public">公开</el-radio>
              <el-radio label="unlisted">不公开</el-radio>
              <el-radio label="private">私密</el-radio>
            </el-radio-group>
          </el-form-item>

          <!-- 高级设置 -->
          <el-form-item label="高级设置">
            <el-collapse>
              <el-collapse-item title="高级选项" name="advanced">
                <el-form-item label="允许评论">
                  <el-switch v-model="videoForm.allowComments" />
                </el-form-item>
                <el-form-item label="年龄限制">
                  <el-switch v-model="videoForm.ageRestricted" />
                </el-form-item>
                <el-form-item label="自动生成字幕">
                  <el-switch v-model="videoForm.autoSubtitle" />
                </el-form-item>
              </el-collapse-item>
            </el-collapse>
          </el-form-item>
        </el-form>
      </div>

      <!-- 步骤3：预览发布 -->
      <div v-if="currentStep === 2" class="step-content">
        <div class="preview-section">
          <h3>视频预览</h3>
          <div class="preview-card">
            <div class="preview-video-container">
              <video
                v-if="videoPreviewUrl"
                :src="videoPreviewUrl"
                controls
                class="preview-video"
              />
            </div>
            <div class="preview-info">
              <h4>{{ videoForm.title }}</h4>
              <p class="preview-description">{{ videoForm.description }}</p>
              <div class="preview-tags">
                <el-tag
                  v-for="tag in videoForm.tags"
                  :key="tag"
                  size="small"
                  class="preview-tag"
                >
                  {{ tag }}
                </el-tag>
              </div>
              <div class="preview-meta">
                <span>分类：{{ videoForm.category }}</span>
                <span>隐私：{{ getPrivacyText(videoForm.privacy) }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 发布选项 -->
        <div class="publish-options">
          <h3>发布选项</h3>
          <el-form-item label="发布时间">
            <el-radio-group v-model="publishOption">
              <el-radio label="now">立即发布</el-radio>
              <el-radio label="schedule">定时发布</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item v-if="publishOption === 'schedule'" label="发布时间">
            <el-date-picker
              v-model="scheduleTime"
              type="datetime"
              placeholder="选择发布时间"
              style="width: 100%"
            />
          </el-form-item>

          <el-form-item label="发布到社交媒体">
            <el-checkbox-group v-model="socialPlatforms">
              <el-checkbox label="weibo">微博</el-checkbox>
              <el-checkbox label="wechat">微信</el-checkbox>
              <el-checkbox label="douyin">抖音</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </div>
      </div>

      <!-- 步骤导航 -->
      <div class="step-navigation">
        <el-button
          v-if="currentStep > 0"
          @click="previousStep"
          :disabled="uploading"
        >
          上一步
        </el-button>
        <el-button
          v-if="currentStep < 2"
          type="primary"
          @click="nextStep"
          :disabled="!canProceed"
          :loading="uploading"
        >
          {{ currentStep === 0 ? '开始上传' : '下一步' }}
        </el-button>
        <el-button
          v-if="currentStep === 2"
          type="primary"
          @click="publishVideo"
          :loading="publishing"
        >
          发布视频
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

export default {
  name: 'UploadView',
  setup() {
    const store = useStore()
    const router = useRouter()

    // 响应式数据
    const currentStep = ref(0)
    const selectedFile = ref(null)
    const videoPreviewUrl = ref('')
    const coverPreviewUrl = ref('')
    const uploadProgress = ref(0)
    const uploading = ref(false)
    const publishing = ref(false)
    const publishOption = ref('now')
    const scheduleTime = ref('')
    const socialPlatforms = ref([])

    // 表单数据
    const videoForm = ref({
      title: '',
      description: '',
      category: '',
      tags: [],
      privacy: 'public',
      allowComments: true,
      ageRestricted: false,
      autoSubtitle: true,
      videoUrl: '' // 新增字段
    })

    // 表单ref
    const videoFormRef = ref(null)

    // 表单验证规则
    const videoRules = {
      title: [
        { required: true, message: '请输入视频标题', trigger: 'blur' },
        { min: 2, max: 100, message: '标题长度在 2 到 100 个字符', trigger: 'blur' }
      ],
      description: [
        { required: true, message: '请输入视频描述', trigger: 'blur' },
        { min: 10, max: 1000, message: '描述长度在 10 到 1000 个字符', trigger: 'blur' }
      ],
      category: [
        { required: true, message: '请选择视频分类', trigger: 'change' }
      ]
    }

    // 分类和标签数据
    const categories = [
      '娱乐', '教育', '音乐', '游戏', '科技', '生活', '体育', '新闻', '动画', '其他'
    ]

    const popularTags = [
      '搞笑', '教程', '音乐', '游戏', '科技', '生活', '美食', '旅行', '健身', '时尚'
    ]

    // 计算属性
    const canProceed = computed(() => {
      if (currentStep.value === 0) {
        return selectedFile.value !== null
      }
      if (currentStep.value === 1) {
        return videoForm.value.title && videoForm.value.description && videoForm.value.category
      }
      return true
    })

    // 方法
    const handleFileChange = (file) => {
      selectedFile.value = file.raw
      videoPreviewUrl.value = URL.createObjectURL(file.raw)
    }

    const beforeUpload = (file) => {
      const isVideo = file.type.startsWith('video/')
      const isLt500M = file.size / 1024 / 1024 < 500

      if (!isVideo) {
        ElMessage.error('只能上传视频文件!')
        return false
      }
      if (!isLt500M) {
        ElMessage.error('视频文件大小不能超过 500MB!')
        return false
      }
      return false // 阻止自动上传
    }

    const removeFile = () => {
      selectedFile.value = null
      videoPreviewUrl.value = ''
      uploadProgress.value = 0
    }

    const handleCoverChange = (file) => {
      coverPreviewUrl.value = URL.createObjectURL(file.raw)
    }

    const nextStep = async () => {
      if (currentStep.value === 0) {
        // 开始上传视频
        await uploadVideo()
      } else if (currentStep.value === 1) {
        try {
          await videoFormRef.value.validate()
          currentStep.value++
        } catch (error) {
          ElMessage.error('请完善视频信息')
        }
      }
    }

    const previousStep = () => {
      if (currentStep.value > 0) {
        currentStep.value--
      }
    }

    // 上传视频文件到后端，拿到URL
    const uploadVideo = async () => {
      if (!selectedFile.value) return
      uploading.value = true
      uploadProgress.value = 0
      try {
        const formData = new FormData()
        formData.append('file', selectedFile.value)
        const res = await fetch('http://localhost:8080/videos/upload', {
          method: 'POST',
          body: formData
        })
        if (!res.ok) throw new Error('上传失败')
        const url = await res.text()
        console.log('后端返回的URL:', url)
        videoForm.value.videoUrl = url.replace(/"/g, '') // 兼容返回json或纯文本
        console.log('最终videoForm.videoUrl:', videoForm.value.videoUrl)
        uploadProgress.value = 100
        uploading.value = false
        currentStep.value++
        ElMessage.success('视频上传成功')
      } catch (error) {
        console.error('上传失败:', error)
        ElMessage.error('视频上传失败')
        uploading.value = false
      }
    }

    // 发布视频，使用真实videoUrl
    const publishVideo = async () => {
      if (!videoForm.value.videoUrl) {
        ElMessage.error('视频未上传成功，无法发布！')
        return
      }
      publishing.value = true
      try {
        // 构建发布数据，适配后端Video实体
        const publishData = {
          title: videoForm.value.title,
          description: videoForm.value.description,
          category: mapCategoryToEnum(videoForm.value.category),
          tags: videoForm.value.tags.join(','),
          videoUrl: videoForm.value.videoUrl, // 用真实上传后的URL
          thumbnailUrl: coverPreviewUrl.value || null,
          uploaderId: store.getters.currentUser?.id || 1,
          uploaderName: store.getters.currentUser?.username || '匿名用户',
          isPublic: videoForm.value.privacy === 'public',
          durationSeconds: 120,
          viewCount: 0,
          likeCount: 0,
          dislikeCount: 0,
          shareCount: 0,
          commentCount: 0
        }
        console.log('发送视频数据:', publishData)
        const result = await store.dispatch('createVideo', publishData)
        console.log('发布结果:', result)
        ElMessage.success('视频发布成功')
        router.push('/videos')
      } catch (error) {
        console.error('发布失败:', error)
        ElMessage.error('视频发布失败: ' + (error.message || error))
      } finally {
        publishing.value = false
      }
    }

    // 将前端分类映射到后端枚举
    const mapCategoryToEnum = (category) => {
      const categoryMap = {
        '娱乐': 'ENTERTAINMENT',
        '教育': 'EDUCATION',
        '音乐': 'MUSIC',
        '游戏': 'GAMING',
        '科技': 'TECHNOLOGY',
        '生活': 'LIFESTYLE',
        '体育': 'SPORTS',
        '新闻': 'NEWS',
        '动画': 'ENTERTAINMENT',
        '其他': 'OTHER'
      }
      return categoryMap[category] || 'OTHER'
    }

    const formatFileSize = (bytes) => {
      if (bytes === 0) return '0 Bytes'
      const k = 1024
      const sizes = ['Bytes', 'KB', 'MB', 'GB']
      const i = Math.floor(Math.log(bytes) / Math.log(k))
      return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
    }

    const getPrivacyText = (privacy) => {
      const privacyMap = {
        public: '公开',
        unlisted: '不公开',
        private: '私密'
      }
      return privacyMap[privacy] || '公开'
    }

    // 初始化
    onMounted(() => {
      // 检查用户是否已登录
      if (!store.getters.isAuthenticated) {
        ElMessage.warning('请先登录')
        router.push('/login')
      }
    })

    return {
      currentStep,
      selectedFile,
      videoPreviewUrl,
      coverPreviewUrl,
      uploadProgress,
      uploading,
      publishing,
      videoForm,
      videoFormRef,
      videoRules,
      categories,
      popularTags,
      publishOption,
      scheduleTime,
      socialPlatforms,
      canProceed,
      handleFileChange,
      beforeUpload,
      removeFile,
      handleCoverChange,
      nextStep,
      previousStep,
      publishVideo,
      formatFileSize,
      getPrivacyText
    }
  }
}
</script>

<style scoped>
.upload-view {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.upload-container {
  background: var(--el-bg-color);
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.page-header {
  text-align: center;
  margin-bottom: 30px;
}

.page-header h1 {
  font-size: 28px;
  font-weight: 600;
  margin-bottom: 8px;
  color: var(--el-text-color-primary);
}

.page-header p {
  color: var(--el-text-color-secondary);
  font-size: 16px;
}

.upload-steps {
  margin-bottom: 40px;
}

.step-content {
  min-height: 400px;
}

.upload-area {
  margin-bottom: 30px;
}

.video-upload {
  width: 100%;
}

.upload-drag-area {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  border: 2px dashed var(--el-border-color);
  border-radius: 8px;
  background: var(--el-bg-color-page);
  transition: all 0.3s ease;
}

.upload-drag-area:hover {
  border-color: var(--el-color-primary);
  background: var(--el-color-primary-light-9);
}

.upload-icon {
  font-size: 48px;
  color: var(--el-color-primary);
  margin-bottom: 16px;
}

.upload-text h3 {
  margin: 0 0 8px 0;
  color: var(--el-text-color-primary);
}

.upload-text p {
  margin: 0;
  color: var(--el-text-color-secondary);
}

.file-info {
  margin-bottom: 20px;
}

.file-details {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: var(--el-bg-color-page);
  border-radius: 8px;
  border: 1px solid var(--el-border-color-light);
}

.file-icon {
  font-size: 24px;
  color: var(--el-color-primary);
}

.file-text h4 {
  margin: 0 0 4px 0;
  color: var(--el-text-color-primary);
}

.file-text p {
  margin: 0;
  color: var(--el-text-color-secondary);
  font-size: 14px;
}

.upload-progress {
  margin-top: 20px;
}

.progress-text {
  text-align: center;
  margin-top: 8px;
  color: var(--el-text-color-secondary);
}

.video-form {
  max-width: 600px;
  margin: 0 auto;
}

.video-preview {
  margin-bottom: 30px;
  text-align: center;
}

.preview-video {
  width: 100%;
  max-width: 400px;
  border-radius: 8px;
}

.preview-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 200px;
  background: var(--el-bg-color-page);
  border: 2px dashed var(--el-border-color);
  border-radius: 8px;
  color: var(--el-text-color-secondary);
}

.cover-upload-area {
  width: 200px;
  height: 120px;
  border: 2px dashed var(--el-border-color);
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
}

.cover-upload-area:hover {
  border-color: var(--el-color-primary);
}

.cover-preview {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.cover-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: var(--el-text-color-secondary);
}

.preview-section {
  margin-bottom: 30px;
}

.preview-section h3 {
  margin-bottom: 20px;
  color: var(--el-text-color-primary);
}

.preview-card {
  border: 1px solid var(--el-border-color-light);
  border-radius: 8px;
  overflow: hidden;
}

.preview-video-container {
  background: #000;
}

.preview-info {
  padding: 20px;
}

.preview-info h4 {
  margin: 0 0 12px 0;
  color: var(--el-text-color-primary);
}

.preview-description {
  margin: 0 0 16px 0;
  color: var(--el-text-color-regular);
  line-height: 1.5;
}

.preview-tags {
  margin-bottom: 16px;
}

.preview-tag {
  margin-right: 8px;
  margin-bottom: 8px;
}

.preview-meta {
  display: flex;
  gap: 20px;
  color: var(--el-text-color-secondary);
  font-size: 14px;
}

.publish-options {
  margin-bottom: 30px;
}

.publish-options h3 {
  margin-bottom: 20px;
  color: var(--el-text-color-primary);
}

.step-navigation {
  display: flex;
  justify-content: space-between;
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid var(--el-border-color-light);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .upload-view {
    padding: 10px;
  }
  
  .upload-container {
    padding: 20px;
  }
  
  .page-header h1 {
    font-size: 24px;
  }
  
  .upload-drag-area {
    padding: 40px 20px;
  }
  
  .step-navigation {
    flex-direction: column;
    gap: 16px;
  }
  
  .step-navigation .el-button {
    width: 100%;
  }
}
</style> 