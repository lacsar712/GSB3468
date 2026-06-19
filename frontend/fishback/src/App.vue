<template>
  <div class="admin-app">
    <!-- Login page has no layout -->
    <router-view v-if="$route.path === '/login'" />

    <!-- Admin layout with sidebar -->
    <template v-else>
      <aside class="sidebar">
        <div class="sidebar-header">
          <span class="sidebar-logo">🐟</span>
          <h1 class="sidebar-title">Fishfrineds</h1>
          <span class="sidebar-badge">管理后台</span>
        </div>

        <nav class="sidebar-nav">
          <router-link to="/" class="nav-item" exact-active-class="active">
            <span class="nav-icon">📊</span>
            <span>仪表盘</span>
          </router-link>
          <div class="nav-section-title">内容管理</div>
          <router-link to="/spots" class="nav-item" active-class="active">
            <span class="nav-icon">📍</span>
            <span>钓点管理</span>
          </router-link>
          <router-link to="/catches" class="nav-item" active-class="active">
            <span class="nav-icon">🐟</span>
            <span>鱼获管理</span>
          </router-link>
          <router-link to="/life" class="nav-item" active-class="active">
            <span class="nav-icon">📸</span>
            <span>生活分享</span>
          </router-link>
          <router-link to="/friends" class="nav-item" active-class="active">
            <span class="nav-icon">👥</span>
            <span>钓友管理</span>
          </router-link>
          <router-link to="/gear" class="nav-item" active-class="active">
            <span class="nav-icon">🎣</span>
            <span>钓具管理</span>
          </router-link>
          <div class="nav-section-title">系统管理</div>
          <router-link to="/users" class="nav-item" active-class="active">
            <span class="nav-icon">👤</span>
            <span>用户管理</span>
          </router-link>
        </nav>

        <div class="sidebar-footer">
          <div class="admin-info">
            <span class="admin-avatar">{{ userInitial }}</span>
            <span class="admin-name">{{ user?.username || '管理员' }}</span>
          </div>
          <button class="logout-btn" @click="handleLogout">退出</button>
        </div>
      </aside>

      <main class="main-area">
        <router-view />
      </main>
    </template>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { initAuth, getUser, logout } from './utils/auth.js'

const router = useRouter()
const user = ref(null)

const userInitial = computed(() => {
  return user.value?.username?.charAt(0)?.toUpperCase() || 'A'
})

const handleLogout = () => {
  logout()
  ElMessage.success('已退出登录')
  router.push('/login')
}

onMounted(() => {
  initAuth()
  user.value = getUser()
})

router.afterEach(() => {
  user.value = getUser()
})
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Inter', 'PingFang SC', 'Microsoft YaHei', sans-serif;
  background: #0f172a;
  color: #e2e8f0;
}

.admin-app {
  display: flex;
  min-height: 100vh;
}

/* Sidebar */
.sidebar {
  width: 240px;
  background: linear-gradient(180deg, #1e293b 0%, #0f172a 100%);
  border-right: 1px solid rgba(148, 163, 184, 0.08);
  display: flex;
  flex-direction: column;
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  z-index: 100;
}

.sidebar-header {
  padding: 24px 20px;
  border-bottom: 1px solid rgba(148, 163, 184, 0.08);
  text-align: center;
}

.sidebar-logo {
  font-size: 32px;
  display: block;
  margin-bottom: 8px;
}

.sidebar-title {
  font-size: 18px;
  font-weight: 700;
  color: #f1f5f9;
  letter-spacing: -0.5px;
}

.sidebar-badge {
  display: inline-block;
  margin-top: 6px;
  padding: 2px 10px;
  background: rgba(59, 130, 246, 0.15);
  color: #60a5fa;
  font-size: 11px;
  font-weight: 600;
  border-radius: 20px;
  letter-spacing: 1px;
}

/* Nav */
.sidebar-nav {
  flex: 1;
  padding: 16px 12px;
  overflow-y: auto;
}

.nav-section-title {
  color: #475569;
  font-size: 11px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 1px;
  padding: 16px 12px 8px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 12px;
  border-radius: 8px;
  color: #94a3b8;
  text-decoration: none;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.15s ease;
  margin-bottom: 2px;
}

.nav-item:hover {
  background: rgba(148, 163, 184, 0.08);
  color: #e2e8f0;
}

.nav-item.active {
  background: rgba(59, 130, 246, 0.15);
  color: #60a5fa;
}

.nav-icon {
  font-size: 16px;
  width: 24px;
  text-align: center;
}

/* Footer */
.sidebar-footer {
  padding: 16px 12px;
  border-top: 1px solid rgba(148, 163, 184, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.admin-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.admin-avatar {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
}

.admin-name {
  color: #94a3b8;
  font-size: 13px;
}

.logout-btn {
  background: rgba(239, 68, 68, 0.1);
  color: #f87171;
  border: none;
  padding: 5px 12px;
  border-radius: 6px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.15s;
}

.logout-btn:hover {
  background: rgba(239, 68, 68, 0.2);
}

/* Main content area */
.main-area {
  flex: 1;
  margin-left: 240px;
  padding: 24px 32px;
  min-height: 100vh;
  background: #0f172a;
}
</style>
