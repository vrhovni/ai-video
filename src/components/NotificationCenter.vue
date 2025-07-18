<template>
  <div class="notification-center">
    <!-- 通知图标 -->
    <el-badge :value="unreadCount" :hidden="unreadCount === 0" class="notification-badge">
      <el-button
        class="notification-trigger"
        :icon="Bell"
        circle
        @click="toggleNotificationPanel"
      />
    </el-badge>

    <!-- 通知面板 -->
    <el-drawer
      v-model="showNotificationPanel"
      title="通知中心"
      direction="rtl"
      size="400px"
      :with-header="true"
    >
      <div class="notification-panel">
        <!-- 通知类型切换 -->
        <div class="notification-tabs">
          <el-tabs v-model="activeTab" @tab-click="handleTabChange">
            <el-tab-pane label="全部" name="all">
              <div class="tab-content">
                <div class="notification-list">
                  <div
                    v-for="notification in filteredNotifications"
                    :key="notification.id"
                    class="notification-item"
                    :class="{ 'unread': !notification.read }"
                    @click="markAsRead(notification.id)"
                  >
                    <div class="notification-avatar">
                      <el-avatar :size="40" :src="notification.sender?.avatar">
                        {{ notification.sender?.username?.charAt(0)?.toUpperCase() }}
                      </el-avatar>
                    </div>
                    <div class="notification-content">
                      <div class="notification-header">
                        <span class="notification-sender">{{ notification.sender?.username }}</span>
                        <span class="notification-time">{{ formatTime(notification.createdAt) }}</span>
                      </div>
                      <p class="notification-message">{{ notification.message }}</p>
                      <div v-if="notification.video" class="notification-video">
                        <el-image
                          :src="notification.video.thumbnail"
                          fit="cover"
                          class="video-thumbnail"
                        />
                        <span class="video-title">{{ notification.video.title }}</span>
                      </div>
                    </div>
                    <div class="notification-actions">
                      <el-button
                        v-if="!notification.read"
                        size="small"
                        text
                        @click.stop="markAsRead(notification.id)"
                      >
                        标记已读
                      </el-button>
                      <el-button
                        size="small"
                        text
                        type="danger"
                        @click.stop="deleteNotification(notification.id)"
                      >
                        删除
                      </el-button>
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane label="互动" name="interaction">
              <div class="tab-content">
                <div class="notification-list">
                  <div
                    v-for="notification in interactionNotifications"
                    :key="notification.id"
                    class="notification-item"
                    :class="{ 'unread': !notification.read }"
                    @click="markAsRead(notification.id)"
                  >
                    <div class="notification-avatar">
                      <el-avatar :size="40" :src="notification.sender?.avatar">
                        {{ notification.sender?.username?.charAt(0)?.toUpperCase() }}
                      </el-avatar>
                    </div>
                    <div class="notification-content">
                      <div class="notification-header">
                        <span class="notification-sender">{{ notification.sender?.username }}</span>
                        <span class="notification-time">{{ formatTime(notification.createdAt) }}</span>
                      </div>
                      <p class="notification-message">{{ notification.message }}</p>
                      <div class="notification-action">
                        <el-icon class="action-icon">
                          <component :is="getActionIcon(notification.type)" />
                        </el-icon>
                        <span class="action-text">{{ getActionText(notification.type) }}</span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane label="系统" name="system">
              <div class="tab-content">
                <div class="notification-list">
                  <div
                    v-for="notification in systemNotifications"
                    :key="notification.id"
                    class="notification-item"
                    :class="{ 'unread': !notification.read }"
                    @click="markAsRead(notification.id)"
                  >
                    <div class="notification-avatar">
                      <el-avatar :size="40" icon="Setting" />
                    </div>
                    <div class="notification-content">
                      <div class="notification-header">
                        <span class="notification-sender">系统通知</span>
                        <span class="notification-time">{{ formatTime(notification.createdAt) }}</span>
                      </div>
                      <p class="notification-message">{{ notification.message }}</p>
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>

        <!-- 通知设置 -->
        <div class="notification-settings">
          <el-divider />
          <h4>通知设置</h4>
          <div class="settings-list">
            <div class="setting-item">
              <span>新关注通知</span>
              <el-switch v-model="settings.newFollower" />
            </div>
            <div class="setting-item">
              <span>点赞通知</span>
              <el-switch v-model="settings.likeNotification" />
            </div>
            <div class="setting-item">
              <span>评论通知</span>
              <el-switch v-model="settings.commentNotification" />
            </div>
            <div class="setting-item">
              <span>分享通知</span>
              <el-switch v-model="settings.shareNotification" />
            </div>
            <div class="setting-item">
              <span>系统通知</span>
              <el-switch v-model="settings.systemNotification" />
            </div>
          </div>
        </div>

        <!-- 操作按钮 -->
        <div class="notification-actions-footer">
          <el-button @click="markAllAsRead" :disabled="unreadCount === 0">
            全部标记已读
          </el-button>
          <el-button @click="clearAllNotifications" type="danger">
            清空通知
          </el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { ref, computed, onMounted, onUnmounted } from 'vue'
// import { useStore } from 'vuex' // 暂时注释，后续可能需要使用
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  ThumbsUp,
  ChatDotRound,
  Share,
  User,
  Setting
} from '@element-plus/icons-vue'

export default {
  name: 'NotificationCenter',
  setup() {
    // const store = useStore() // 暂时注释，后续可能需要使用

    // 响应式数据
    const showNotificationPanel = ref(false)
    const activeTab = ref('all')
    const notifications = ref([])
    const settings = ref({
      newFollower: true,
      likeNotification: true,
      commentNotification: true,
      shareNotification: true,
      systemNotification: true
    })

    // 计算属性
    const unreadCount = computed(() => 
      notifications.value.filter(n => !n.read).length
    )

    const filteredNotifications = computed(() => {
      return notifications.value
    })

    const interactionNotifications = computed(() => {
      return notifications.value.filter(n => 
        ['like', 'comment', 'share', 'follow'].includes(n.type)
      )
    })

    const systemNotifications = computed(() => {
      return notifications.value.filter(n => n.type === 'system')
    })

    // 方法
    const toggleNotificationPanel = () => {
      showNotificationPanel.value = !showNotificationPanel.value
      if (showNotificationPanel.value) {
        fetchNotifications()
      }
    }

    const fetchNotifications = async () => {
      try {
        // 暂时使用模拟数据，因为后端还没有实现通知接口
        notifications.value = [
          {
            id: 1,
            type: 'system',
            message: '欢迎使用AI视频推荐系统！',
            read: false,
            createdAt: new Date().toISOString(),
            sender: {
              username: '系统',
              avatar: null
            }
          }
        ]
      } catch (error) {
        console.error('获取通知失败:', error)
      }
    }

    const markAsRead = async (notificationId) => {
      try {
        // 暂时直接修改本地状态，因为后端还没有实现通知接口
        const notification = notifications.value.find(n => n.id === notificationId)
        if (notification) {
          notification.read = true
        }
      } catch (error) {
        console.error('标记已读失败:', error)
      }
    }

    const markAllAsRead = async () => {
      try {
        // 暂时直接修改本地状态，因为后端还没有实现通知接口
        notifications.value.forEach(n => n.read = true)
        ElMessage.success('已全部标记为已读')
      } catch (error) {
        console.error('标记全部已读失败:', error)
      }
    }

    const deleteNotification = async (notificationId) => {
      try {
        await ElMessageBox.confirm('确定要删除这条通知吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        // 暂时直接修改本地状态，因为后端还没有实现通知接口
        notifications.value = notifications.value.filter(n => n.id !== notificationId)
        ElMessage.success('通知已删除')
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除通知失败:', error)
        }
      }
    }

    const clearAllNotifications = async () => {
      try {
        await ElMessageBox.confirm('确定要清空所有通知吗？此操作不可恢复。', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        // 暂时直接修改本地状态，因为后端还没有实现通知接口
        notifications.value = []
        ElMessage.success('所有通知已清空')
      } catch (error) {
        if (error !== 'cancel') {
          console.error('清空通知失败:', error)
        }
      }
    }

    const handleTabChange = () => {
      // 切换标签页时的处理
    }

    const getActionIcon = (type) => {
      const iconMap = {
        like: ThumbsUp,
        comment: ChatDotRound,
        share: Share,
        follow: User,
        system: Setting
      }
      return iconMap[type] || Setting
    }

    const getActionText = (type) => {
      const textMap = {
        like: '点赞了您的视频',
        comment: '评论了您的视频',
        share: '分享了您的视频',
        follow: '关注了您',
        system: '系统通知'
      }
      return textMap[type] || '未知操作'
    }

    const formatTime = (dateString) => {
      if (!dateString) return ''
      
      const date = new Date(dateString)
      const now = new Date()
      const diff = now - date
      const minutes = Math.floor(diff / (1000 * 60))
      const hours = Math.floor(diff / (1000 * 60 * 60))
      const days = Math.floor(diff / (1000 * 60 * 60 * 24))
      
      if (minutes < 1) return '刚刚'
      if (minutes < 60) return `${minutes}分钟前`
      if (hours < 24) return `${hours}小时前`
      if (days < 7) return `${days}天前`
      
      return date.toLocaleDateString()
    }

    // 模拟实时通知
    const simulateRealTimeNotifications = () => {
      // 在实际项目中，这里应该使用WebSocket连接
      setInterval(() => {
        // 模拟新通知
        if (Math.random() < 0.1) { // 10%概率产生新通知
          const newNotification = {
            id: Date.now(),
            type: ['like', 'comment', 'follow'][Math.floor(Math.random() * 3)],
            message: '您有新的互动通知',
            read: false,
            createdAt: new Date().toISOString(),
            sender: {
              username: '用户' + Math.floor(Math.random() * 1000),
              avatar: null
            }
          }
          notifications.value.unshift(newNotification)
        }
      }, 30000) // 每30秒检查一次
    }

    // 生命周期
    onMounted(() => {
      fetchNotifications()
      simulateRealTimeNotifications()
    })

    onUnmounted(() => {
      // 清理定时器
    })

    return {
      showNotificationPanel,
      activeTab,
      notifications,
      settings,
      unreadCount,
      filteredNotifications,
      interactionNotifications,
      systemNotifications,
      toggleNotificationPanel,
      markAsRead,
      markAllAsRead,
      deleteNotification,
      clearAllNotifications,
      handleTabChange,
      getActionIcon,
      getActionText,
      formatTime
    }
  }
}
</script>

<style scoped>
.notification-center {
  position: relative;
}

.notification-badge {
  margin-right: 8px;
}

.notification-trigger {
  border: none;
  background: transparent;
}

.notification-panel {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.notification-tabs {
  flex: 1;
  overflow: hidden;
}

.tab-content {
  height: 100%;
  overflow-y: auto;
}

.notification-list {
  padding: 0;
}

.notification-item {
  display: flex;
  gap: 12px;
  padding: 16px;
  border-bottom: 1px solid var(--el-border-color-light);
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.notification-item:hover {
  background-color: var(--el-fill-color-light);
}

.notification-item.unread {
  background-color: var(--el-color-primary-light-9);
}

.notification-avatar {
  flex-shrink: 0;
}

.notification-content {
  flex: 1;
  min-width: 0;
}

.notification-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.notification-sender {
  font-weight: 600;
  color: var(--el-text-color-primary);
  font-size: 14px;
}

.notification-time {
  font-size: 12px;
  color: var(--el-text-color-secondary);
}

.notification-message {
  margin: 0 0 8px 0;
  color: var(--el-text-color-regular);
  font-size: 14px;
  line-height: 1.4;
}

.notification-video {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px;
  background: var(--el-fill-color-light);
  border-radius: 4px;
  margin-top: 8px;
}

.video-thumbnail {
  width: 40px;
  height: 24px;
  border-radius: 4px;
}

.video-title {
  font-size: 12px;
  color: var(--el-text-color-secondary);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.notification-action {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-top: 8px;
}

.action-icon {
  font-size: 14px;
  color: var(--el-color-primary);
}

.action-text {
  font-size: 12px;
  color: var(--el-text-color-secondary);
}

.notification-actions {
  display: flex;
  flex-direction: column;
  gap: 4px;
  flex-shrink: 0;
}

.notification-settings {
  padding: 20px 0;
}

.notification-settings h4 {
  margin: 0 0 16px 0;
  color: var(--el-text-color-primary);
}

.settings-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.setting-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
}

.setting-item span {
  color: var(--el-text-color-regular);
  font-size: 14px;
}

.notification-actions-footer {
  display: flex;
  gap: 12px;
  padding: 20px 0;
  border-top: 1px solid var(--el-border-color-light);
}

.notification-actions-footer .el-button {
  flex: 1;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .notification-item {
    padding: 12px;
  }
  
  .notification-actions-footer {
    flex-direction: column;
  }
}
</style> 