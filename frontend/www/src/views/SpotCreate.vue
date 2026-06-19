<template>
  <div class="create-container">
    <div class="create-header">
      <h1>{{ isEdit ? '编辑钓点' : '发布钓点' }}</h1>
      <router-link to="/spots" class="back-link">← 返回列表</router-link>
    </div>

    <form @submit.prevent="handleSubmit" class="create-form">
      <div class="form-group">
        <label>标题 *</label>
        <input v-model="form.title" type="text" placeholder="给钓点起个名字" required />
      </div>

      <div class="form-row">
        <div class="form-group">
          <label>位置 *</label>
          <input v-model="form.location" type="text" placeholder="省市区/具体地址" required />
        </div>
        <div class="form-group">
          <label>鱼种</label>
          <input v-model="form.fishTypes" type="text" placeholder="如：鲫鱼、鲤鱼、草鱼" />
        </div>
      </div>

      <div class="form-row">
        <div class="form-group">
          <label>最佳季节</label>
          <select v-model="form.bestSeason">
            <option value="">请选择</option>
            <option value="春季">春季</option>
            <option value="夏季">夏季</option>
            <option value="秋季">秋季</option>
            <option value="冬季">冬季</option>
            <option value="全年可钓">全年可钓</option>
          </select>
        </div>
        <div class="form-group">
          <label>最佳时间</label>
          <input v-model="form.bestTime" type="text" placeholder="如：清晨、傍晚" />
        </div>
      </div>

      <div class="form-group">
        <label>位置描述</label>
        <textarea v-model="form.locationDesc" rows="3" placeholder="详细描述钓点位置，如何到达..."></textarea>
      </div>

      <div class="form-group">
        <label>设施描述</label>
        <textarea v-model="form.facilityDesc" rows="2" placeholder="停车场、卫生间、餐饮等设施情况..."></textarea>
      </div>

      <div class="form-row">
        <div class="form-group">
          <label>费用信息</label>
          <input v-model="form.feeInfo" type="text" placeholder="免费/收费金额" />
        </div>
        <div class="form-group">
          <label>联系方式</label>
          <input v-model="form.contactInfo" type="text" placeholder="电话/微信（选填）" />
        </div>
      </div>

      <div class="form-group">
        <label>详细内容</label>
        <textarea v-model="form.content" rows="5" placeholder="详细介绍这个钓点的特点、钓鱼技巧、注意事项..."></textarea>
      </div>

      <div v-if="error" class="error-message">{{ error }}</div>

      <div class="form-actions">
        <button type="button" class="btn-secondary" @click="$router.back()">取消</button>
        <button type="submit" class="btn-primary" :disabled="loading">
          {{ loading ? (isEdit ? '保存中...' : '发布中...') : (isEdit ? '保存修改' : '发布钓点') }}
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
  isEdit: {
    type: Boolean,
    default: false
  },
  id: {
    type: String,
    default: ''
  }
})

const router = useRouter()
const loading = ref(false)
const error = ref('')

const form = reactive({
  title: '',
  location: '',
  locationDesc: '',
  fishTypes: '',
  bestSeason: '',
  bestTime: '',
  facilityDesc: '',
  feeInfo: '',
  contactInfo: '',
  content: ''
})

// 编辑模式下加载数据
onMounted(async () => {
  if (props.isEdit && props.id) {
    try {
      const res = await axios.get(`/api/spots/${props.id}`)
      if (res.data.code === 200) {
        const data = res.data.data
        Object.assign(form, data)
      }
    } catch (err) {
      error.value = '加载数据失败'
      console.error('Failed to load spot:', err)
    }
  }
})

const handleSubmit = async () => {
  if (!form.title.trim() || !form.location.trim()) {
    error.value = '请填写标题和位置'
    return
  }

  error.value = ''
  loading.value = true

  try {
    if (props.isEdit) {
      await axios.put(`/api/spots/${props.id}`, form)
    } else {
      await axios.post('/api/spots', form)
    }
    router.push('/spots')
  } catch (err) {
    error.value = err.response?.data?.message || (props.isEdit ? '保存失败，请稍后重试' : '发布失败，请稍后重试')
    loading.value = false
  }
}
</script>

<style scoped>
.create-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 24px;
}

.create-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.create-header h1 {
  font-size: 24px;
  color: #1a1a1a;
}

.back-link {
  color: #2563eb;
  text-decoration: none;
  font-size: 14px;
}

.back-link:hover {
  text-decoration: underline;
}

.create-form {
  background: #fff;
  padding: 32px;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-size: 14px;
  font-weight: 500;
  color: #374151;
}

.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid #e5e7eb;
  border-radius: 10px;
  font-size: 15px;
  transition: all 0.2s;
  font-family: inherit;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #2563eb;
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.1);
}

.form-group textarea {
  resize: vertical;
  min-height: 80px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.error-message {
  color: #dc2626;
  background: #fef2f2;
  padding: 12px;
  border-radius: 8px;
  margin-bottom: 20px;
  font-size: 14px;
}

.form-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid #e5e7eb;
}

.btn-secondary {
  padding: 12px 24px;
  border: 1px solid #e5e7eb;
  background: #fff;
  color: #666;
  border-radius: 10px;
  font-size: 15px;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-secondary:hover {
  background: #f3f4f6;
}

.btn-primary {
  padding: 12px 32px;
  border: none;
  background: linear-gradient(135deg, #2563eb 0%, #1e40af 100%);
  color: #fff;
  border-radius: 10px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-primary:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(37, 99, 235, 0.3);
}

.btn-primary:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

@media (max-width: 640px) {
  .form-row {
    grid-template-columns: 1fr;
  }

  .create-form {
    padding: 20px;
  }
}
</style>
