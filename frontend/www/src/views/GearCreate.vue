<template>
  <div class="create-container">
    <div class="create-header">
      <h1>{{ isEdit ? '编辑钓具' : '发布钓具' }}</h1>
      <router-link to="/gear" class="back-link">← 返回列表</router-link>
    </div>

    <form @submit.prevent="handleSubmit" class="create-form">
      <div class="form-group">
        <label>标题 *</label>
        <input v-model="form.title" type="text" placeholder="钓具名称" required />
      </div>

      <div class="form-row">
        <div class="form-group">
          <label>价格 (元) *</label>
          <input v-model="form.price" type="number" min="0" step="0.01" placeholder="0.00" required />
        </div>
        <div class="form-group">
          <label>分类</label>
          <select v-model="form.category">
            <option value="">请选择</option>
            <option value="钓竿">🎣 钓竿</option>
            <option value="卷线器">🔄 卷线器</option>
            <option value="饵料">🪱 饵料</option>
            <option value="线组">🧵 线组</option>
            <option value="配件">🔧 配件</option>
            <option value="其他">📦 其他</option>
          </select>
        </div>
      </div>

      <div class="form-row">
        <div class="form-group">
          <label>成色</label>
          <select v-model="form.conditionStatus">
            <option value="全新">✨ 全新</option>
            <option value="二手">📦 二手</option>
          </select>
        </div>
        <div class="form-group">
          <label>联系方式</label>
          <input v-model="form.contact" type="text" placeholder="电话/微信" />
        </div>
      </div>

      <div class="form-group">
        <label>描述</label>
        <textarea v-model="form.description" rows="5" placeholder="详细描述钓具的品牌、型号、规格、使用情况..."></textarea>
      </div>

      <div v-if="error" class="error-message">{{ error }}</div>

      <div class="form-actions">
        <button type="button" class="btn-secondary" @click="$router.back()">取消</button>
        <button type="submit" class="btn-primary" :disabled="loading">
          {{ loading ? (isEdit ? '保存中...' : '发布中...') : (isEdit ? '保存修改' : '发布钓具') }}
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
  description: '',
  price: null,
  category: '',
  conditionStatus: '全新',
  contact: ''
})

onMounted(async () => {
  if (props.isEdit && props.id) {
    try {
      const res = await axios.get(`/api/gear/${props.id}`)
      if (res.data.code === 200) {
        Object.assign(form, res.data.data)
      }
    } catch (err) {
      error.value = '加载数据失败'
    }
  }
})

const handleSubmit = async () => {
  if (!form.title.trim() || form.price === null || form.price < 0) {
    error.value = '请填写标题和价格'
    return
  }

  error.value = ''
  loading.value = true

  try {
    if (props.isEdit) {
      await axios.put(`/api/gear/${props.id}`, form)
    } else {
      await axios.post('/api/gear', form)
    }
    router.push('/gear')
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
.btn-primary { padding: 12px 32px; border: none; background: linear-gradient(135deg, #ec4899 0%, #db2777 100%); color: #fff; border-radius: 10px; font-weight: 600; cursor: pointer; }
.btn-primary:disabled { opacity: 0.7; cursor: not-allowed; }
@media (max-width: 640px) { .form-row { grid-template-columns: 1fr; } }
</style>
