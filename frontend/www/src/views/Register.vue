<template>
  <div class="auth-container">
    <div class="auth-card">
      <h2 class="auth-title">用户注册</h2>
      <form @submit.prevent="handleRegister" class="auth-form">
        <div class="form-group">
          <label>用户名 *</label>
          <input 
            v-model="form.username" 
            type="text" 
            placeholder="3-50个字符"
            required
            minlength="3"
            maxlength="50"
          />
        </div>
        
        <div class="form-group">
          <label>密码 *</label>
          <input 
            v-model="form.password" 
            type="password" 
            placeholder="至少6个字符"
            required
            minlength="6"
          />
        </div>
        
        <div class="form-group">
          <label>昵称 *</label>
          <input 
            v-model="form.nickname" 
            type="text" 
            placeholder="显示名称"
            required
            maxlength="100"
          />
        </div>
        <div v-if="error" class="error-message">{{ error }}</div>
        <button type="submit" class="auth-btn" :disabled="loading">
          {{ loading ? '注册中...' : '注册' }}
        </button>
      </form>
      <div class="auth-footer">
        <p>已有账号？<router-link to="/login">立即登录</router-link></p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '../utils/auth.js'

const router = useRouter()
const loading = ref(false)
const error = ref('')

const form = reactive({
  username: '',
  password: '',
  nickname: ''
})

const handleRegister = async () => {
  error.value = ''
  
  // 前端校验
  if (form.username.length < 3) {
    error.value = '用户名至少需要3个字符'
    return
  }
  
  if (form.password.length < 6) {
    error.value = '密码至少需要6个字符'
    return
  }
  
  if (!form.nickname.trim()) {
    error.value = '请输入昵称'
    return
  }
  
  loading.value = true
  
  try {
    await register(form.username, form.password, form.nickname)
    router.push('/')
  } catch (err) {
    error.value = err.response?.data?.message || '注册失败，请稍后重试'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 70vh;
  padding: 20px;
}

.auth-card {
  background: #fff;
  padding: 40px;
  border-radius: 16px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.08);
  width: 100%;
  max-width: 420px;
}

.auth-title {
  text-align: center;
  margin-bottom: 32px;
  color: #1a1a1a;
  font-size: 24px;
  font-weight: 600;
}

.auth-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  font-size: 14px;
  color: #666;
  font-weight: 500;
}

.form-group input {
  padding: 12px 16px;
  border: 1px solid #e5e7eb;
  border-radius: 10px;
  font-size: 15px;
  transition: all 0.2s;
}

.form-group input:focus {
  outline: none;
  border-color: #2563eb;
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.1);
}

.error-message {
  color: #dc2626;
  font-size: 14px;
  text-align: center;
  padding: 8px;
  background: #fef2f2;
  border-radius: 8px;
}

.auth-btn {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: #fff;
  border: none;
  padding: 14px 24px;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  margin-top: 8px;
}

.auth-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(16, 185, 129, 0.3);
}

.auth-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.auth-footer {
  text-align: center;
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid #e5e7eb;
}

.auth-footer p {
  color: #666;
  font-size: 14px;
}

.auth-footer a {
  color: #2563eb;
  text-decoration: none;
  font-weight: 500;
}

.auth-footer a:hover {
  text-decoration: underline;
}
</style>
