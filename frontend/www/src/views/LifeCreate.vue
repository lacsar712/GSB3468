<template>
  <div class="create-container">
    <div class="create-header">
      <h1>{{ isEdit ? '编辑分享' : '分享生活' }}</h1>
      <router-link to="/life" class="back-link">← 返回列表</router-link>
    </div>

    <form @submit.prevent="handleSubmit" class="create-form">
      <div class="form-group">
        <label>标题 *</label>
        <input v-model="form.title" type="text" placeholder="分享标题" required />
      </div>

      <div class="form-group">
        <label>分享类型</label>
        <select v-model="form.shareType">
          <option value="">请选择类型</option>
          <option value="钓鱼日记">📝 钓鱼日记</option>
          <option value="经验分享">💡 经验分享</option>
          <option value="装备评测">🎣 装备评测</option>
          <option value="其他">📌 其他</option>
        </select>
      </div>

      <div class="form-group">
        <label>内容 *</label>
        <textarea v-model="form.content" rows="8" placeholder="分享你的钓鱼生活、心得体会..." required></textarea>
      </div>

      <div v-if="error" class="error-message">{{ error }}</div>

      <div class="form-actions">
        <button type="button" class="btn-secondary" @click="$router.back()">取消</button>
        <button type="submit" class="btn-primary" :disabled="loading">
          {{ loading ? (isEdit ? '保存中...' : '发布中...') : (isEdit ? '保存修改' : '发布分享') }}
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
  shareType: '',
  content: ''
})

onMounted(async () => {
  if (props.isEdit && props.id) {
    try {
      const res = await axios.get(`/api/life/${props.id}`)
      if (res.data.code === 200) {
        Object.assign(form, res.data.data)
      }
    } catch (err) {
      error.value = '加载数据失败'
    }
  }
})

const handleSubmit = async () => {
  if (!form.title.trim() || !form.content.trim()) {
    error.value = '请填写标题和内容'
    return
  }

  error.value = ''
  loading.value = true

  try {
    if (props.isEdit) {
      await axios.put(`/api/life/${props.id}`, form)
    } else {
      await axios.post('/api/life', form)
    }
    router.push('/life')
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
.form-group textarea { resize: vertical; min-height: 200px; }
.error-message { color: #dc2626; background: #fef2f2; padding: 12px; border-radius: 8px; margin-bottom: 20px; }
.form-actions { display: flex; gap: 12px; justify-content: flex-end; margin-top: 32px; padding-top: 24px; border-top: 1px solid #e5e7eb; }
.btn-secondary { padding: 12px 24px; border: 1px solid #e5e7eb; background: #fff; color: #666; border-radius: 10px; cursor: pointer; }
.btn-primary { padding: 12px 32px; border: none; background: linear-gradient(135deg, #10b981 0%, #059669 100%); color: #fff; border-radius: 10px; font-weight: 600; cursor: pointer; }
.btn-primary:disabled { opacity: 0.7; cursor: not-allowed; }
</style>
