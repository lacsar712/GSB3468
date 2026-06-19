<template>
  <div class="life">
    <div class="page-header">
      <h1>生活分享</h1>
      <router-link v-if="isLoggedIn" to="/life/create" class="create-btn" data-testid="life-create-btn">
        + 发布分享
      </router-link>
      <router-link v-else to="/login" class="create-btn" data-testid="life-login-btn">
        登录后发布
      </router-link>
    </div>
    
    <div class="life-list">
      <div v-for="item in lifeShares" :key="item.id" class="life-card">
        <h3>{{ item.title }}</h3>
        <span class="type">{{ item.shareType }}</span>
        <p class="content">{{ item.content }}</p>
        <div class="stats">
          <span>浏览 {{ item.viewCount }}</span>
          <span>点赞 {{ item.likeCount }}</span>
        </div>
        <span class="date">{{ formatDate(item.createdAt) }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { isLoggedIn } from '../utils/auth.js'

const lifeShares = ref([])
const API_URL = '/api'

onMounted(async () => {
  try {
    const res = await fetch(`${API_URL}/life`)
    const data = await res.json()
    if (data.code === 200) {
      lifeShares.value = data.data.content
    }
  } catch (err) {
    console.error('Failed to fetch life shares:', err)
  }
})

const formatDate = (dateStr) => {
  return new Date(dateStr).toLocaleDateString('zh-CN')
}
</script>

<style scoped>
.life {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

h1 {
  color: #2c3e50;
  margin: 0;
}

.life-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.life-card {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.life-card h3 {
  color: #27ae60;
  margin-bottom: 5px;
}

.type {
  display: inline-block;
  background: #3498db;
  color: white;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  margin-bottom: 10px;
}

.content {
  margin: 10px 0;
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

.stats {
  display: flex;
  gap: 15px;
  margin-top: 10px;
  font-size: 12px;
  color: #999;
}

.date {
  display: block;
  margin-top: 10px;
  font-size: 12px;
  color: #999;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.create-btn {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: #fff;
  text-decoration: none;
  padding: 10px 20px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s;
}

.create-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.3);
}
</style>
