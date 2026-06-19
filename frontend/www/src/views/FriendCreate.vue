<template>
  <div class="create-container">
    <div class="create-header">
      <h1>{{ isEdit ? '编辑信息' : '分享钓友信息' }}</h1>
      <router-link to="/friends" class="back-link">← 返回列表</router-link>
    </div>

    <form @submit.prevent="handleSubmit" class="create-form">
      <div class="form-group">
        <label>姓名/昵称 *</label>
        <input v-model="form.name" type="text" placeholder="姓名或昵称" required />
      </div>

      <div class="form-row">
        <div class="form-group">
          <label>钓龄 (年)</label>
          <input v-model="form.experienceYears" type="number" min="0" placeholder="钓了多少年" />
        </div>
        <div class="form-group">
          <label>联系方式</label>
          <input v-model="form.contact" type="text" placeholder="电话/微信（选填）" />
        </div>
      </div>

      <div class="form-group">
        <label>专长钓法</label>
        <input v-model="form.speciality" type="text" placeholder="如：台钓、路亚、海钓..." />
      </div>

      <div class="form-group">
        <label>个人简介</label>
        <textarea v-model="form.bio" rows="5" placeholder="介绍一下自己，钓鱼经历、擅长领域..."></textarea>
      </div>

      <div v-if="error" class="error-message">{{ error }}</div>

      <div class="form-actions">
        <button type="button" class="btn-secondary" @click="$router.back()">取消</button>
        <button type="submit" class="btn-primary" :disabled="loading">
          {{ loading ? (isEdit ? '保存中...' : '发布中...') : (isEdit ? '保存修改' : '发布信息') }}
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
  name: '',
  bio: '',
  experienceYears: null,
  speciality: '',
  contact: ''
})

onMounted(async () => {
  if (props.isEdit && props.id) {
    try {
      const res = await axios.get(`/api/friends/${props.id}`)
      if (res.data.code === 200) {
        Object.assign(form, res.data.data)
      }
    } catch (err) {
      error.value = '加载数据失败'
    }
  }
})

const handleSubmit = async () => {
  if (!form.name.trim()) {
    error.value = '请填写姓名或昵称'
    return
  }

  error.value = ''
  loading.value = true

  try {
    if (props.isEdit) {
      await axios.put(`/api/friends/${props.id}`, form)
    } else {
      await axios.post('/api/friends', form)
    }
    router.push('/friends')
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
.form-group input, .form-group textarea { width: 100%; padding: 12px 16px; border: 1px solid #e5e7eb; border-radius: 10px; font-size: 15px; transition: all 0.2s; }
.form-group input:focus, .form-group textarea:focus { outline: none; border-color: #2563eb; box-shadow: 0 0 0 3px rgba(37,99,235,0.1); }
.form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
.error-message { color: #dc2626; background: #fef2f2; padding: 12px; border-radius: 8px; margin-bottom: 20px; }
.form-actions { display: flex; gap: 12px; justify-content: flex-end; margin-top: 32px; padding-top: 24px; border-top: 1px solid #e5e7eb; }
.btn-secondary { padding: 12px 24px; border: 1px solid #e5e7eb; background: #fff; color: #666; border-radius: 10px; cursor: pointer; }
.btn-primary { padding: 12px 32px; border: none; background: linear-gradient(135deg, #8b5cf6 0%, #7c3aed 100%); color: #fff; border-radius: 10px; font-weight: 600; cursor: pointer; }
.btn-primary:disabled { opacity: 0.7; cursor: not-allowed; }
@media (max-width: 640px) { .form-row { grid-template-columns: 1fr; } }
</style>
