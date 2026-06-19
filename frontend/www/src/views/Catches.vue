<template>
  <div class="catches">
    <div class="page-header">
      <h1>鱼获分享</h1>
      <router-link v-if="isLoggedIn" to="/catches/create" class="create-btn" data-testid="catches-create-btn">
        + 分享鱼获
      </router-link>
      <router-link v-else to="/login" class="create-btn" data-testid="catches-login-btn">
        登录后发布
      </router-link>
    </div>
    <div class="filter-bar">
      <label for="fishTypeFilter">按鱼种筛选：</label>
      <select id="fishTypeFilter" v-model="selectedFishType" @change="fetchCatches" data-testid="catch-fishtype-filter">
        <option value="">全部</option>
        <option v-for="type in fishTypes" :key="type" :value="type">{{ type }}</option>
      </select>
    </div>
    <div class="catch-list">
      <div v-for="item in catches" :key="item.id" class="catch-card">
        <h3>{{ item.title }}</h3>
        <p><strong>鱼种：</strong>{{ item.fishType }}</p>
        <p><strong>重量：</strong>{{ item.fishWeight }} kg</p>
        <p><strong>长度：</strong>{{ item.fishLength }} cm</p>
        <p><strong>钓点：</strong>{{ item.fishingSpot }}</p>
        <p><strong>钓法：</strong>{{ item.fishingMethod }}</p>
        <p v-if="item.content" class="content">{{ item.content }}</p>
        <span class="date">{{ formatDate(item.createdAt) }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { isLoggedIn } from '../utils/auth.js'

const catches = ref([])
const fishTypes = ref([])
const selectedFishType = ref('')
const API_URL = '/api'

const fetchCatches = async () => {
  try {
    let url = `${API_URL}/catches`
    if (selectedFishType.value) {
      url = `${API_URL}/catches/search?fishType=${encodeURIComponent(selectedFishType.value)}`
    }
    const res = await fetch(url)
    const data = await res.json()
    if (data.code === 200) {
      catches.value = data.data.content
    }
  } catch (err) {
    console.error('Failed to fetch catches:', err)
  }
}

const fetchFishTypes = async () => {
  try {
    const res = await fetch(`${API_URL}/catches/fish-types`)
    const data = await res.json()
    if (data.code === 200) {
      fishTypes.value = data.data
    }
  } catch (err) {
    console.error('Failed to fetch fish types:', err)
  }
}

onMounted(async () => {
  await fetchFishTypes()
  await fetchCatches()
})

const formatDate = (dateStr) => {
  return new Date(dateStr).toLocaleDateString('zh-CN')
}
</script>

<style scoped>
.catches {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

h1 {
  color: #2c3e50;
  margin-bottom: 20px;
}

.catch-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.catch-card {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.catch-card h3 {
  color: #e74c3c;
  margin-bottom: 10px;
}

.catch-card p {
  margin: 5px 0;
  color: #555;
}

.content {
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
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
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
  box-shadow: 0 4px 12px rgba(245, 158, 11, 0.3);
}

.filter-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;
  padding: 16px 20px;
  background: #f8f9fa;
  border-radius: 8px;
}

.filter-bar label {
  color: #2c3e50;
  font-weight: 500;
}

.filter-bar select {
  padding: 8px 16px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
  background: #fff;
  cursor: pointer;
  min-width: 150px;
}

.filter-bar select:focus {
  outline: none;
  border-color: #f59e0b;
  box-shadow: 0 0 0 3px rgba(245, 158, 11, 0.1);
}
</style>
