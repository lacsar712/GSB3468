<template>
  <div class="gear">
    <div class="page-header">
      <h1>钓具销售</h1>
      <router-link v-if="isLoggedIn" to="/gear/create" class="create-btn" data-testid="gear-create-btn">
        + 发布钓具
      </router-link>
      <router-link v-else to="/login" class="create-btn" data-testid="gear-login-btn">
        登录后发布
      </router-link>
    </div>
    
    <div class="gear-list">
      <div v-for="item in gearList" :key="item.id" class="gear-card">
        <h3>{{ item.title }}</h3>
        
        <div class="price-row">
          <span class="price">¥{{ item.price }}</span>
          <span class="condition">{{ item.conditionStatus }}</span>
        </div>
        
        <p><strong>分类：</strong>{{ item.category || '未分类' }}</p>
        <p><strong>联系方式：</strong>{{ item.contact || '暂无' }}</p>
        
        <p class="description">{{ item.description }}</p>
        
        <span class="date">{{ formatDate(item.createdAt) }}</span>
      </div>
    </div>
    
    <div v-if="gearList.length === 0" class="empty">
      暂无钓具信息
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { isLoggedIn } from '../utils/auth.js'

const gearList = ref([])
const API_URL = '/api'

onMounted(async () => {
  try {
    const res = await fetch(`${API_URL}/gear`)
    const data = await res.json()
    if (data.code === 200) {
      gearList.value = data.data.content
    }
  } catch (err) {
    console.error('Failed to fetch gear:', err)
  }
})

const formatDate = (dateStr) => {
  return new Date(dateStr).toLocaleDateString('zh-CN')
}
</script>

<style scoped>
.gear {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

h1 {
  color: #2c3e50;
  margin: 0;
}

.gear-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.gear-card {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.gear-card h3 {
  color: #f39c12;
  margin-bottom: 10px;
}

.price-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.price {
  font-size: 24px;
  font-weight: bold;
  color: #e74c3c;
}

.condition {
  background: #27ae60;
  color: white;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.gear-card p {
  margin: 5px 0;
  color: #555;
}

.description {
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
  background: linear-gradient(135deg, #ec4899 0%, #db2777 100%);
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
  box-shadow: 0 4px 12px rgba(236, 72, 153, 0.3);
}
</style>
