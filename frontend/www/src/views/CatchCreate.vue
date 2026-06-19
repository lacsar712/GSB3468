<template>
  <div class="create-container">
    <div class="create-header">
      <h1>{{ isEdit ? '编辑鱼获' : '分享鱼获' }}</h1>
      <router-link to="/catches" class="back-link">← 返回列表</router-link>
    </div>

    <form @submit.prevent="handleSubmit" class="create-form">
      <div class="form-group">
        <label>标题 *</label>
        <input v-model="form.title" type="text" placeholder="给这次钓鱼起个标题" required />
      </div>

      <div class="form-row">
        <div class="form-group">
          <label>鱼种 *</label>
          <input v-model="form.fishType" type="text" placeholder="如：鲤鱼、鲫鱼" required />
        </div>
        <div class="form-group">
          <label>钓点</label>
          <input v-model="form.fishingSpot" type="text" placeholder="在哪里钓的？" />
        </div>
      </div>

      <div class="form-row">
        <div class="form-group">
          <label>重量 (kg)</label>
          <input v-model="form.fishWeight" type="number" step="0.01" placeholder="0.00" />
        </div>
        <div class="form-group">
          <label>长度 (cm)</label>
          <input v-model="form.fishLength" type="number" step="0.1" placeholder="0.0" />
        </div>
      </div>

      <div class="form-row">
        <div class="form-group">
          <label>钓法</label>
          <input v-model="form.fishingMethod" type="text" placeholder="如：台钓、路亚" />
        </div>
        <div class="form-group">
          <label>饵料</label>
          <input v-model="form.baitUsed" type="text" placeholder="使用的饵料" />
        </div>
      </div>

      <div class="form-row">
        <div class="form-group">
          <label>天气</label>
          <input v-model="form.weather" type="text" placeholder="如：晴天、阴天" />
        </div>
        <div class="form-group">
          <label>钓获日期</label>
          <input v-model="form.catchDate" type="date" />
        </div>
      </div>

      <div class="form-group">
        <label>内容描述</label>
        <textarea v-model="form.content" rows="5" placeholder="分享你的钓鱼经历、心得..."></textarea>
      </div>

      <div v-if="error" class="error-message">{{ error }}</div>

      <div class="form-actions">
        <button type="button" class="btn-secondary" @click="$router.back()">取消</button>
        <button type="submit" class="btn-primary" :disabled="loading">
          {{ loading ? (isEdit ? '保存中...' : '发布中...') : (isEdit ? '保存修改' : '分享鱼获') }}
        </button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const props = defineProps({
  isEdit: { type: Boolean, default: false },
  id: { type: String, default: '' }
})

const router = useRouter()
const loading = ref(false)
const error = ref('')

const form = reactive({
  title: '',
  fishType: '',
  fishWeight: null,
  fishLength: null,
  fishingSpot: '',
  fishingMethod: '',
  baitUsed: '',
  weather: '',
  catchDate: new Date().toISOString().split('T')[0],
  content: ''
})

onMounted(async () => {
  if (props.isEdit && props.id) {
    try {
      const res = await axios.get(`/api/catches/${props.id}`)
      if (res.data.code === 200) {
        const data = res.data.data
        Object.assign(form, data)
      }
    } catch (err) {
      error.value = '加载数据失败'
    }
  }
})

const handleSubmit = async () => {
  if (!form.title.trim() || !form.fishType.trim()) {
    error.value = '请填写标题和鱼种'
    return
  }

  error.value = ''
  loading.value = true

  try {
    if (props.isEdit) {
      await axios.put(`/api/catches/${props.id}`, form)
    } else {
      await axios.post('/api/catches', form)
    }
    router.push('/catches')
  } catch (err) {
    error.value = err.response?.data?.message || '操作失败'
    loading.value = false
  }
}
</script>

<style scoped>
.create-container { max-width: 800px; margin: 0 auto; padding: 24px; }
.create-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
.create-header h1 { font-size: 24px; color: #1a1a1a; }
.back-link { color: #2563eb; text-decoration: none; font-size: 14px; }
.create-form { background: #fff; padding: 32px; border-radius: 16px; box-shadow: 0 2px 12px rgba(0,0,0,0.06); }
.form-group { margin-bottom: 20px; }
.form-group label { display: block; margin-bottom: 8px; font-size: 14px; font-weight: 500; color: #374151; }
.form-group input, .form-group select, .form-group textarea { width: 100%; padding: 12px 16px; border: 1px solid #e5e7eb; border-radius: 10px; font-size: 15px; transition: all 0.2s; }
.form-group input:focus, .form-group select:focus, .form-group textarea:focus { outline: none; border-color: #2563eb; box-shadow: 0 0 0 3px rgba(37,99,235,0.1); }
.form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
.error-message { color: #dc2626; background: #fef2f2; padding: 12px; border-radius: 8px; margin-bottom: 20px; }
.form-actions { display: flex; gap: 12px; justify-content: flex-end; margin-top: 32px; padding-top: 24px; border-top: 1px solid #e5e7eb; }
.btn-secondary { padding: 12px 24px; border: 1px solid #e5e7eb; background: #fff; color: #666; border-radius: 10px; cursor: pointer; }
.btn-primary { padding: 12px 32px; border: none; background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%); color: #fff; border-radius: 10px; font-weight: 600; cursor: pointer; }
.btn-primary:disabled { opacity: 0.7; cursor: not-allowed; }
@media (max-width: 640px) { .form-row { grid-template-columns: 1fr; } }
</style>
