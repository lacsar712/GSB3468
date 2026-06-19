<template>
  <div class="app">
    <nav class="navbar">
      <div class="nav-brand">
        <router-link to="/">Fishfrineds 钓鱼社区</router-link>
      </div>
      <div class="nav-links">
        <router-link to="/">首页</router-link>
        <router-link to="/spots">钓点</router-link>
        <router-link to="/catches">鱼获</router-link>
        <router-link to="/life">生活</router-link>
        <router-link to="/friends">钓友</router-link>
        <router-link to="/gear">钓具</router-link>
      </div>
      
      <div class="nav-user">
        <template v-if="loggedIn">
          <router-link to="/profile" class="user-info">
            <span class="user-avatar">{{ userInitial }}</span>
            <span class="user-name">{{ user?.nickname || user?.username }}</span>
          </router-link>
          <button class="logout-btn" @click="handleLogout">退出</button>
        </template>
        <template v-else>
          <router-link to="/login" class="login-link">登录</router-link>
          <router-link to="/register" class="register-btn">注册</router-link>
        </template>
      </div>
    </nav>
    
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { initAuth, isLoggedIn, getUser, logout } from './utils/auth.js'

const router = useRouter()
const loggedIn = ref(false)
const user = ref(null)

const userInitial = computed(() => {
  return user.value?.nickname?.charAt(0)?.toUpperCase() || 
         user.value?.username?.charAt(0)?.toUpperCase() || '?'
})

const updateAuthState = () => {
  loggedIn.value = isLoggedIn()
  user.value = getUser()
}

const handleLogout = () => {
  logout()
  updateAuthState()
  router.push('/')
}

onMounted(() => {
  initAuth()
  updateAuthState()
})

// 监听路由变化，更新登录状态
router.afterEach(() => {
  updateAuthState()
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
  color: #333;
  background: #f5f7fa;
}

.navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  height: 64px;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  position: sticky;
  top: 0;
  z-index: 100;
}

.nav-brand a {
  font-size: 20px;
  font-weight: 700;
  color: #2563eb;
  text-decoration: none;
}

.nav-links {
  display: flex;
  gap: 8px;
}

.nav-links a {
  color: #666;
  text-decoration: none;
  font-size: 15px;
  padding: 8px 14px;
  border-radius: 8px;
  transition: all 0.2s;
}

.nav-links a:hover,
.nav-links a.router-link-active {
  color: #2563eb;
  background: #eff6ff;
}

.nav-user {
  display: flex;
  align-items: center;
  gap: 12px;
}

.login-link {
  color: #666;
  text-decoration: none;
  font-size: 15px;
  padding: 8px 16px;
  border-radius: 8px;
  transition: all 0.2s;
}

.login-link:hover {
  color: #2563eb;
  background: #f3f4f6;
}

.register-btn {
  background: linear-gradient(135deg, #2563eb 0%, #1e40af 100%);
  color: #fff;
  text-decoration: none;
  font-size: 15px;
  padding: 8px 20px;
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.2s;
}

.register-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(37, 99, 235, 0.3);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  text-decoration: none;
  padding: 6px 12px;
  border-radius: 8px;
  transition: all 0.2s;
}

.user-info:hover {
  background: #f3f4f6;
}

.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: linear-gradient(135deg, #2563eb 0%, #1e40af 100%);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 600;
}

.user-name {
  color: #1a1a1a;
  font-size: 14px;
  font-weight: 500;
}

.logout-btn {
  background: #f3f4f6;
  color: #666;
  border: none;
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s;
}

.logout-btn:hover {
  background: #e5e7eb;
  color: #dc2626;
}

.main-content {
  max-width: 1200px;
  margin: 24px auto;
  padding: 0 16px;
}

@media (max-width: 768px) {
  .navbar {
    flex-wrap: wrap;
    height: auto;
    padding: 12px 16px;
    gap: 12px;
  }
  
  .nav-links {
    order: 3;
    width: 100%;
    justify-content: center;
    padding-top: 8px;
    border-top: 1px solid #e5e7eb;
  }
  
  .nav-links a {
    font-size: 14px;
    padding: 6px 10px;
  }
  
  .user-name {
    display: none;
  }
}
</style>
