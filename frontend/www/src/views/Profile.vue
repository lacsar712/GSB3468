<template>
  <div class="profile-container">
    <div class="profile-card">
      <div class="profile-header">
        <div class="avatar">{{ userInitial }}</div>
        <div class="profile-info">
          <h2>{{ user?.nickname || user?.username }}</h2>
          <p class="username">@{{ user?.username }}</p>
          <span class="role-badge" :class="user?.role?.toLowerCase()">
            {{ user?.role === 'ADMIN' ? '管理员' : '普通用户' }}
          </span>
        </div>
      </div>
      
      <div class="profile-stats">
        <div class="stat-item">
          <span class="stat-value">0</span>
          <span class="stat-label">钓点</span>
        </div>
        <div class="stat-item">
          <span class="stat-value">0</span>
          <span class="stat-label">鱼获</span>
        </div>
        <div class="stat-item">
          <span class="stat-value">0</span>
          <span class="stat-label">分享</span>
        </div>
        <div class="stat-item">
          <span class="stat-value">0</span>
          <span class="stat-label">钓具</span>
        </div>
      </div>
      
      <div class="profile-actions">
        <button class="action-btn secondary" @click="handleLogout">
          退出登录
        </button>
      </div>
    </div>
    
    <div class="content-section">
      <h3>我的发布</h3>
      <div class="tabs">
        <button 
          v-for="tab in tabs" 
          :key="tab.key"
          :class="['tab-btn', { active: currentTab === tab.key }]"
          @click="currentTab = tab.key"
        >
          {{ tab.label }}
        </button>
      </div>
      
      <div class="empty-state">
        <div class="empty-icon">📝</div>
        <p>暂无{{ currentTabLabel }}</p>
        <router-link :to="createLink" class="create-btn">
          发布第一个{{ currentTabLabel }}
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { getUser, logout } from '../utils/auth.js'

const router = useRouter()
const user = getUser()
const currentTab = ref('spots')

const tabs = [
  { key: 'spots', label: '钓点' },
  { key: 'catches', label: '鱼获' },
  { key: 'life', label: '分享' },
  { key: 'gear', label: '钓具' }
]

const currentTabLabel = computed(() => {
  return tabs.find(t => t.key === currentTab.value)?.label || ''
})

const createLink = computed(() => {
  return `/${currentTab.value}/create`
})

const userInitial = computed(() => {
  return user?.nickname?.charAt(0)?.toUpperCase() || 
         user?.username?.charAt(0)?.toUpperCase() || '?'
})

const handleLogout = () => {
  logout()
  router.push('/')
}
</script>

<style scoped>
.profile-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 24px 16px;
}

.profile-card {
  background: #fff;
  border-radius: 16px;
  padding: 32px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  margin-bottom: 24px;
}

.profile-header {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 24px;
}

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: linear-gradient(135deg, #2563eb 0%, #1e40af 100%);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  font-weight: 600;
}

.profile-info h2 {
  font-size: 24px;
  font-weight: 600;
  color: #1a1a1a;
  margin-bottom: 4px;
}

.profile-info .username {
  color: #666;
  font-size: 14px;
  margin-bottom: 8px;
}

.role-badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.role-badge.admin {
  background: #fef3c7;
  color: #d97706;
}

.role-badge.user {
  background: #eff6ff;
  color: #2563eb;
}

.profile-stats {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  padding: 24px 0;
  border-top: 1px solid #e5e7eb;
  border-bottom: 1px solid #e5e7eb;
  margin-bottom: 24px;
}

.stat-item {
  text-align: center;
}

.stat-value {
  display: block;
  font-size: 24px;
  font-weight: 700;
  color: #1a1a1a;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.profile-actions {
  display: flex;
  justify-content: center;
}

.action-btn {
  padding: 10px 24px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  border: none;
}

.action-btn.secondary {
  background: #f3f4f6;
  color: #666;
}

.action-btn.secondary:hover {
  background: #e5e7eb;
}

.content-section {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.content-section h3 {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 20px;
  color: #1a1a1a;
}

.tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 24px;
  border-bottom: 1px solid #e5e7eb;
  padding-bottom: 12px;
}

.tab-btn {
  padding: 8px 16px;
  border: none;
  background: transparent;
  color: #666;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  border-radius: 6px;
  transition: all 0.2s;
}

.tab-btn:hover {
  background: #f3f4f6;
  color: #1a1a1a;
}

.tab-btn.active {
  background: #eff6ff;
  color: #2563eb;
}

.empty-state {
  text-align: center;
  padding: 48px 20px;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.empty-state p {
  color: #666;
  margin-bottom: 16px;
}

.create-btn {
  display: inline-block;
  padding: 10px 24px;
  background: linear-gradient(135deg, #2563eb 0%, #1e40af 100%);
  color: #fff;
  text-decoration: none;
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.2s;
}

.create-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(37, 99, 235, 0.3);
}

@media (max-width: 640px) {
  .profile-stats {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .tabs {
    flex-wrap: wrap;
  }
}
</style>
