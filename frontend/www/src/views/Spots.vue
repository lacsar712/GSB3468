<template>
  <div class="spots">
    <div class="page-header">
      <h1>钓点信息</h1>
      <router-link v-if="isLoggedIn" to="/spots/create" class="create-btn">
        + 发布钓点
      </router-link>
      <router-link v-else to="/login" class="create-btn">
        登录后发布
      </router-link>
    </div>
    <div class="spot-list">
      <div v-for="spot in spots" :key="spot.id" class="spot-card">
        <div class="card-header">
          <h3>{{ spot.title }}</h3>
          <router-link v-if="isLoggedIn" :to="`/spots/${spot.id}/edit`" class="edit-btn">编辑</router-link>
        </div>
        <p><strong>位置：</strong>{{ spot.location }}</p>
        <p><strong>鱼类：</strong>{{ spot.fishTypes }}</p>
        <p><strong>最佳季节：</strong>{{ spot.bestSeason }}</p>
        <p class="description">{{ spot.locationDesc }}</p>
        <span class="date">{{ formatDate(spot.createdAt) }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { isLoggedIn } from '../utils/auth.js'

const spots = ref([])
const API_URL = '/api'

onMounted(async () => {
  try {
    const res = await fetch(`${API_URL}/spots`)
    const data = await res.json()
    if (data.code === 200) {
      spots.value = data.data.content
    }
  } catch (err) {
    console.error('Failed to fetch spots:', err)
  }
})

const formatDate = (dateStr) => {
  return new Date(dateStr).toLocaleDateString('zh-CN')
}
</script>

<style scoped>
.spots {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

h1 {
  color: #2c3e50;
  margin-bottom: 20px;
}

.spot-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.spot-card {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.spot-card h3 {
  color: #3498db;
  margin-bottom: 10px;
}

.spot-card p {
  margin: 5px 0;
  color: #555;
}

.description {
  margin-top: 10px;
  font-size: 14px;
  color: #666;
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

.page-header h1 {
  color: #2c3e50;
  margin: 0;
}

.create-btn {
  background: linear-gradient(135deg, #2563eb 0%, #1e40af 100%);
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
  box-shadow: 0 4px 12px rgba(37, 99, 235, 0.3);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.card-header h3 {
  margin: 0;
}

.edit-btn {
  font-size: 12px;
  color: #2563eb;
  text-decoration: none;
  padding: 4px 10px;
  border: 1px solid #2563eb;
  border-radius: 4px;
}

.edit-btn:hover {
  background: #2563eb;
  color: #fff;
}
</style>
