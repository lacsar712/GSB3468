<template>
  <div class="friends">
    <div class="page-header">
      <h1>钓友信息</h1>
      <router-link v-if="isLoggedIn" to="/friends/create" class="create-btn" data-testid="friends-create-btn">
        + 分享信息
      </router-link>
      <router-link v-else to="/login" class="create-btn" data-testid="friends-login-btn">
        登录后发布
      </router-link>
    </div>
    
    <div class="friends-list">
      <div v-for="friend in friends" :key="friend.id" class="friend-card">
        <div class="header">
          <h3>{{ friend.name }}</h3>
          <span class="experience">{{ friend.experienceYears }} 年钓龄</span>
        </div>
        
        <p><strong>专长：</strong>{{ friend.speciality || '暂无' }}</p>
        <p><strong>联系方式：</strong>{{ friend.contact || '暂无' }}</p>
        
        <p class="bio">{{ friend.bio }}</p>
        
        <span class="date">{{ formatDate(friend.createdAt) }}</span>
      </div>
    </div>
    
    <div v-if="friends.length === 0" class="empty">
      暂无钓友信息
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { isLoggedIn } from '../utils/auth.js'

const friends = ref([])
const API_URL = '/api'

onMounted(async () => {
  try {
    const res = await fetch(`${API_URL}/friends`)
    const data = await res.json()
    if (data.code === 200) {
      friends.value = data.data.content
    }
  } catch (err) {
    console.error('Failed to fetch friends:', err)
  }
})

const formatDate = (dateStr) => {
  return new Date(dateStr).toLocaleDateString('zh-CN')
}
</script>

<style scoped>
.friends {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

h1 {
  color: #2c3e50;
  margin: 0;
}

.friends-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.friend-card {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.friend-card h3 {
  color: #9b59b6;
  margin: 0;
}

.experience {
  background: #e74c3c;
  color: white;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.friend-card p {
  margin: 5px 0;
  color: #555;
}

.bio {
  margin-top: 10px;
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

.date {
  display: block;
  margin-top: 10px;
  font-size: 12px;
  color: #999;
}

.empty {
  text-align: center;
  padding: 40px;
  color: #999;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.create-btn {
  background: linear-gradient(135deg, #8b5cf6 0%, #7c3aed 100%);
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
  box-shadow: 0 4px 12px rgba(139, 92, 246, 0.3);
}
</style>
