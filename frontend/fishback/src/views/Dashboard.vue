<template>
  <div class="dashboard">
    <h2 class="page-title">仪表盘</h2>

    <div class="stats-grid" v-if="!loading">
      <div class="stat-card" v-for="item in statCards" :key="item.key">
        <div class="stat-icon">{{ item.icon }}</div>
        <div class="stat-info">
          <div class="stat-value">{{ stats[item.key] ?? '—' }}</div>
          <div class="stat-label">{{ item.label }}</div>
        </div>
      </div>
    </div>

    <div v-else class="loading-state">
      <el-skeleton :rows="2" animated />
    </div>

    <div class="quick-links">
      <h3>快捷操作</h3>
      <div class="link-grid">
        <router-link to="/users" class="quick-card">
          <span class="qc-icon">👤</span>
          <span>用户管理</span>
        </router-link>
        <router-link to="/spots" class="quick-card">
          <span class="qc-icon">📍</span>
          <span>钓点管理</span>
        </router-link>
        <router-link to="/catches" class="quick-card">
          <span class="qc-icon">🐟</span>
          <span>鱼获管理</span>
        </router-link>
        <router-link to="/life" class="quick-card">
          <span class="qc-icon">📸</span>
          <span>生活分享</span>
        </router-link>
        <router-link to="/friends" class="quick-card">
          <span class="qc-icon">👥</span>
          <span>钓友管理</span>
        </router-link>
        <router-link to="/gear" class="quick-card">
          <span class="qc-icon">🎣</span>
          <span>钓具管理</span>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import axios from 'axios'

const loading = ref(true)
const stats = reactive({
  users: 0,
  spots: 0,
  catches: 0,
  lifeShares: 0,
  friends: 0,
  gear: 0
})

const statCards = [
  { key: 'users', label: '注册用户', icon: '👤' },
  { key: 'spots', label: '钓点数量', icon: '📍' },
  { key: 'catches', label: '鱼获记录', icon: '🐟' },
  { key: 'lifeShares', label: '生活分享', icon: '📸' },
  { key: 'friends', label: '钓友档案', icon: '👥' },
  { key: 'gear', label: '钓具信息', icon: '🎣' }
]

onMounted(async () => {
  try {
    const res = await axios.get('/api/admin/stats')
    if (res.data.code === 200) {
      Object.assign(stats, res.data.data)
    }
  } catch (err) {
    console.error('Failed to load stats:', err)
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.dashboard {
  max-width: 1100px;
}

.page-title {
  font-size: 24px;
  font-weight: 700;
  color: #f1f5f9;
  margin-bottom: 28px;
  letter-spacing: -0.5px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-bottom: 40px;
}

.stat-card {
  background: linear-gradient(135deg, rgba(30, 41, 59, 0.8) 0%, rgba(15, 23, 42, 0.9) 100%);
  border: 1px solid rgba(148, 163, 184, 0.08);
  border-radius: 16px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  transition: all 0.2s;
}

.stat-card:hover {
  border-color: rgba(59, 130, 246, 0.2);
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.3);
}

.stat-icon {
  font-size: 36px;
  width: 56px;
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(59, 130, 246, 0.1);
  border-radius: 14px;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #f1f5f9;
  line-height: 1.2;
}

.stat-label {
  font-size: 13px;
  color: #64748b;
  margin-top: 2px;
}

.loading-state {
  padding: 40px;
}

.quick-links h3 {
  font-size: 18px;
  color: #f1f5f9;
  margin-bottom: 16px;
  font-weight: 600;
}

.link-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.quick-card {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 16px 20px;
  background: rgba(30, 41, 59, 0.6);
  border: 1px solid rgba(148, 163, 184, 0.08);
  border-radius: 12px;
  color: #94a3b8;
  text-decoration: none;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.15s;
}

.quick-card:hover {
  background: rgba(59, 130, 246, 0.1);
  border-color: rgba(59, 130, 246, 0.2);
  color: #60a5fa;
}

.qc-icon {
  font-size: 20px;
}

@media (max-width: 768px) {
  .stats-grid,
  .link-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
