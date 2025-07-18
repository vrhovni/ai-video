<template>
  <div class="about-page">
    <!-- 英雄区域 -->
    <section class="hero-section">
      <div class="hero-content">
        <h1>关于 AI视频推荐</h1>
        <p>基于人工智能的智能视频推荐平台，为您提供个性化的内容体验</p>
      </div>
    </section>

    <!-- 平台介绍 -->
    <section class="platform-intro">
      <div class="section-header">
        <h2>平台介绍</h2>
        <p>了解我们的使命和愿景</p>
      </div>
      
      <div class="intro-content">
        <div class="intro-text">
          <h3>我们的使命</h3>
          <p>
            AI视频推荐平台致力于通过先进的机器学习算法，为用户提供精准、个性化的视频内容推荐。
            我们相信每个人都有自己的兴趣和偏好，通过深度分析用户行为数据，我们能够为用户发现
            最符合其兴趣的优质视频内容。
          </p>
          
          <h3>我们的愿景</h3>
          <p>
            成为全球领先的智能视频推荐平台，通过持续的技术创新和用户体验优化，
            为用户创造更加智能、便捷的视频观看体验。我们希望通过AI技术，
            让每个人都能轻松发现和享受优质的内容。
          </p>
        </div>
        
        <div class="intro-visual">
          <div class="visual-card">
            <el-icon><VideoPlay /></el-icon>
            <h4>智能推荐</h4>
            <p>基于AI算法的个性化内容推荐</p>
          </div>
          <div class="visual-card">
            <el-icon><TrendCharts /></el-icon>
            <h4>实时分析</h4>
            <p>深度分析用户行为和偏好</p>
          </div>
          <div class="visual-card">
            <el-icon><Lock /></el-icon>
            <h4>隐私保护</h4>
            <p>严格的数据安全和隐私保护</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 核心功能 -->
    <section class="core-features">
      <div class="section-header">
        <h2>核心功能</h2>
        <p>探索我们平台的主要特色</p>
      </div>
      
      <div class="features-grid">
        <div class="feature-card">
          <div class="feature-icon">
            <el-icon><MagicStick /></el-icon>
          </div>
          <h3>AI智能推荐</h3>
          <p>
            采用先进的机器学习算法，分析用户的观看历史、点赞行为、搜索记录等多维度数据，
            为用户提供精准的个性化视频推荐。
          </p>
        </div>
        
        <div class="feature-card">
          <div class="feature-icon">
            <el-icon><TrendCharts /></el-icon>
          </div>
          <h3>实时数据分析</h3>
          <p>
            实时监控和分析用户行为数据，持续优化推荐算法，确保推荐结果的准确性和时效性。
          </p>
        </div>
        
        <div class="feature-card">
          <div class="feature-icon">
            <el-icon><Monitor /></el-icon>
          </div>
          <h3>多平台支持</h3>
          <p>
            支持PC、移动端、平板等多种设备，提供一致的用户体验，随时随地享受优质视频内容。
          </p>
        </div>
        
        <div class="feature-card">
          <div class="feature-icon">
            <el-icon><Lock /></el-icon>
          </div>
          <h3>隐私保护</h3>
          <p>
            采用严格的数据保护措施，确保用户个人信息和观看习惯的安全可靠，让用户放心使用。
          </p>
        </div>
        
        <div class="feature-card">
          <div class="feature-icon">
            <el-icon><Star /></el-icon>
          </div>
          <h3>内容质量</h3>
          <p>
            严格的内容审核机制，确保推荐内容的优质性和安全性，为用户提供健康向上的视频内容。
          </p>
        </div>
        
        <div class="feature-card">
          <div class="feature-icon">
            <el-icon><Setting /></el-icon>
          </div>
          <h3>个性化设置</h3>
          <p>
            提供丰富的个性化设置选项，用户可以根据自己的喜好调整推荐偏好和界面设置。
          </p>
        </div>
      </div>
    </section>

    <!-- 技术架构 -->
    <section class="tech-architecture">
      <div class="section-header">
        <h2>技术架构</h2>
        <p>了解我们使用的先进技术</p>
      </div>
      
      <div class="tech-content">
        <div class="tech-item">
          <div class="tech-icon">
            <el-icon><Cpu /></el-icon>
          </div>
          <div class="tech-info">
            <h3>机器学习算法</h3>
            <p>采用协同过滤、内容推荐、深度学习等多种算法，构建精准的推荐模型</p>
          </div>
        </div>
        
        <div class="tech-item">
          <div class="tech-icon">
            <el-icon><DataAnalysis /></el-icon>
          </div>
          <div class="tech-info">
            <h3>大数据处理</h3>
            <p>使用分布式计算框架，高效处理海量用户行为数据和视频内容数据</p>
          </div>
        </div>
        
        <div class="tech-item">
          <div class="tech-icon">
            <el-icon><CloudServer /></el-icon>
          </div>
          <div class="tech-info">
            <h3>云原生架构</h3>
            <p>基于微服务架构和容器化技术，提供高可用、可扩展的服务能力</p>
          </div>
        </div>
        
        <div class="tech-item">
          <div class="tech-icon">
            <el-icon><Shield /></el-icon>
          </div>
          <div class="tech-info">
            <h3>安全防护</h3>
            <p>多层次安全防护体系，包括数据加密、访问控制、威胁检测等</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 团队介绍 -->
    <section class="team-section">
      <div class="section-header">
        <h2>我们的团队</h2>
        <p>专业的团队为您提供优质服务</p>
      </div>
      
      <div class="team-grid">
        <div class="team-member">
          <div class="member-avatar">
            <el-avatar :size="80" src="https://via.placeholder.com/80x80/409eff/ffffff?text=CEO">
              CEO
            </el-avatar>
          </div>
          <h3>张明</h3>
          <p class="member-title">首席执行官</p>
          <p class="member-desc">拥有15年互联网产品经验，专注于用户体验和产品创新</p>
        </div>
        
        <div class="team-member">
          <div class="member-avatar">
            <el-avatar :size="80" src="https://via.placeholder.com/80x80/67c23a/ffffff?text=CTO">
              CTO
            </el-avatar>
          </div>
          <h3>李强</h3>
          <p class="member-title">首席技术官</p>
          <p class="member-desc">AI算法专家，在机器学习和推荐系统领域有丰富经验</p>
        </div>
        
        <div class="team-member">
          <div class="member-avatar">
            <el-avatar :size="80" src="https://via.placeholder.com/80x80/e6a23c/ffffff?text=CPO">
              CPO
            </el-avatar>
          </div>
          <h3>王芳</h3>
          <p class="member-title">产品总监</p>
          <p class="member-desc">资深产品经理，专注于用户需求分析和产品设计</p>
        </div>
      </div>
    </section>

    <!-- 联系我们 -->
    <section class="contact-section">
      <div class="section-header">
        <h2>联系我们</h2>
        <p>我们期待与您交流</p>
      </div>
      
      <div class="contact-content">
        <div class="contact-info">
          <div class="contact-item">
            <el-icon><Message /></el-icon>
            <div>
              <h4>邮箱</h4>
              <p>contact@aivideo.com</p>
            </div>
          </div>
          
          <div class="contact-item">
            <el-icon><Phone /></el-icon>
            <div>
              <h4>电话</h4>
              <p>400-123-4567</p>
            </div>
          </div>
          
          <div class="contact-item">
            <el-icon><Location /></el-icon>
            <div>
              <h4>地址</h4>
              <p>北京市朝阳区科技园区创新大厦</p>
            </div>
          </div>
        </div>
        
        <div class="contact-form">
          <h3>发送消息</h3>
          <el-form :model="contactForm" label-width="80px">
            <el-form-item label="姓名">
              <el-input v-model="contactForm.name" placeholder="请输入您的姓名" />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="contactForm.email" placeholder="请输入您的邮箱" />
            </el-form-item>
            <el-form-item label="主题">
              <el-input v-model="contactForm.subject" placeholder="请输入消息主题" />
            </el-form-item>
            <el-form-item label="消息">
              <el-input
                v-model="contactForm.message"
                type="textarea"
                :rows="4"
                placeholder="请输入您的消息"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="sendMessage">发送消息</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
import {
  VideoPlay,
  TrendCharts,
  Lock,
  MagicStick,
  Monitor,
  Star,
  Setting,
  Cpu,
  DataAnalysis,
  CloudServer,
  Shield,
  Message,
  Phone,
  Location
} from '@element-plus/icons-vue'

export default {
  name: 'AboutView',
  components: {
    VideoPlay,
    TrendCharts,
    Lock,
    MagicStick,
    Monitor,
    Star,
    Setting,
    Cpu,
    DataAnalysis,
    CloudServer,
    Shield,
    Message,
    Phone,
    Location
  },
  setup() {
    const contactForm = reactive({
      name: '',
      email: '',
      subject: '',
      message: ''
    })

    const sendMessage = () => {
      ElMessage.success('消息发送成功！我们会尽快回复您。')
      // 重置表单
      Object.keys(contactForm).forEach(key => {
        contactForm[key] = ''
      })
    }

    return {
      contactForm,
      sendMessage
    }
  }
}
</script>

<style scoped>
.about-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 英雄区域 */
.hero-section {
  text-align: center;
  padding: 80px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 24px;
  margin-bottom: 80px;
  color: white;
}

.hero-content h1 {
  font-size: 3rem;
  font-weight: 700;
  margin-bottom: 20px;
}

.hero-content p {
  font-size: 1.2rem;
  opacity: 0.9;
  max-width: 600px;
  margin: 0 auto;
}

/* 通用样式 */
.section-header {
  text-align: center;
  margin-bottom: 60px;
}

.section-header h2 {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 12px;
  color: #303133;
}

.section-header p {
  font-size: 1.1rem;
  color: #606266;
}

/* 平台介绍 */
.platform-intro {
  margin-bottom: 80px;
}

.intro-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
  align-items: center;
}

.intro-text h3 {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 16px;
  color: #303133;
}

.intro-text p {
  font-size: 1rem;
  line-height: 1.6;
  color: #606266;
  margin-bottom: 32px;
}

.intro-visual {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.visual-card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 24px;
  text-align: center;
  border: 1px solid rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.visual-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.1);
}

.visual-card .el-icon {
  font-size: 2.5rem;
  color: #409eff;
  margin-bottom: 16px;
}

.visual-card h4 {
  font-size: 1.2rem;
  font-weight: 600;
  margin-bottom: 8px;
  color: #303133;
}

.visual-card p {
  color: #606266;
  font-size: 0.9rem;
}

/* 核心功能 */
.core-features {
  margin-bottom: 80px;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 32px;
}

.feature-card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 32px;
  border: 1px solid rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 16px 48px rgba(0, 0, 0, 0.1);
}

.feature-icon {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #409eff, #67c23a);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 24px;
  font-size: 36px;
  color: white;
}

.feature-card h3 {
  font-size: 1.4rem;
  font-weight: 600;
  margin-bottom: 16px;
  color: #303133;
}

.feature-card p {
  color: #606266;
  line-height: 1.6;
}

/* 技术架构 */
.tech-architecture {
  margin-bottom: 80px;
}

.tech-content {
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.tech-item {
  display: flex;
  align-items: center;
  gap: 24px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 32px;
  border: 1px solid rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.tech-item:hover {
  transform: translateX(8px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.tech-icon {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #409eff, #67c23a);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36px;
  color: white;
  flex-shrink: 0;
}

.tech-info h3 {
  font-size: 1.4rem;
  font-weight: 600;
  margin-bottom: 12px;
  color: #303133;
}

.tech-info p {
  color: #606266;
  line-height: 1.6;
}

/* 团队介绍 */
.team-section {
  margin-bottom: 80px;
}

.team-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 32px;
}

.team-member {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 32px;
  text-align: center;
  border: 1px solid rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.team-member:hover {
  transform: translateY(-8px);
  box-shadow: 0 16px 48px rgba(0, 0, 0, 0.1);
}

.member-avatar {
  margin-bottom: 20px;
}

.team-member h3 {
  font-size: 1.3rem;
  font-weight: 600;
  margin-bottom: 8px;
  color: #303133;
}

.member-title {
  color: #409eff;
  font-weight: 500;
  margin-bottom: 12px;
}

.member-desc {
  color: #606266;
  line-height: 1.6;
}

/* 联系我们 */
.contact-section {
  margin-bottom: 80px;
}

.contact-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
}

.contact-info {
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 16px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border-radius: 12px;
  padding: 20px;
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.contact-item .el-icon {
  font-size: 2rem;
  color: #409eff;
}

.contact-item h4 {
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 4px;
  color: #303133;
}

.contact-item p {
  color: #606266;
}

.contact-form {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 32px;
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.contact-form h3 {
  font-size: 1.4rem;
  font-weight: 600;
  margin-bottom: 24px;
  color: #303133;
}

/* 深色模式 */
:deep(.dark-mode) .section-header h2 {
  color: #ffffff;
}

:deep(.dark-mode) .section-header p {
  color: #a8abb2;
}

:deep(.dark-mode) .intro-text h3 {
  color: #ffffff;
}

:deep(.dark-mode) .intro-text p {
  color: #a8abb2;
}

:deep(.dark-mode) .visual-card,
:deep(.dark-mode) .feature-card,
:deep(.dark-mode) .tech-item,
:deep(.dark-mode) .team-member,
:deep(.dark-mode) .contact-item,
:deep(.dark-mode) .contact-form {
  background: rgba(26, 26, 26, 0.8);
  border-color: rgba(255, 255, 255, 0.1);
}

:deep(.dark-mode) .visual-card h4,
:deep(.dark-mode) .feature-card h3,
:deep(.dark-mode) .tech-info h3,
:deep(.dark-mode) .team-member h3,
:deep(.dark-mode) .contact-item h4,
:deep(.dark-mode) .contact-form h3 {
  color: #ffffff;
}

:deep(.dark-mode) .visual-card p,
:deep(.dark-mode) .feature-card p,
:deep(.dark-mode) .tech-info p,
:deep(.dark-mode) .member-desc,
:deep(.dark-mode) .contact-item p {
  color: #a8abb2;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .hero-content h1 {
    font-size: 2.5rem;
  }

  .intro-content {
    grid-template-columns: 1fr;
    gap: 40px;
  }

  .features-grid {
    grid-template-columns: 1fr;
  }

  .tech-item {
    flex-direction: column;
    text-align: center;
  }

  .team-grid {
    grid-template-columns: 1fr;
  }

  .contact-content {
    grid-template-columns: 1fr;
    gap: 40px;
  }
}

@media (max-width: 480px) {
  .about-page {
    padding: 0 16px;
  }

  .hero-content h1 {
    font-size: 2rem;
  }

  .section-header h2 {
    font-size: 2rem;
  }
}
</style>
