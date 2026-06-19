<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-icon">🐟</div>
      <h2 class="login-title">Fishfrineds 管理后台</h2>
      <p class="login-subtitle">仅限管理员登录</p>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="0">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="请输入管理员账号" prefix-icon="User" size="large" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" prefix-icon="Lock" size="large" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" style="width: 100%" :loading="loading" @click="handleLogin">
            登 录
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button size="small" link @click="fillTestAccount">使用测试管理员账号</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'
import { setToken, setUser, logout } from '../utils/auth.js'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const fillTestAccount = () => {
  form.username = 'admin'
  form.password = 'password123'
}

const handleLogin = async () => {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const res = await axios.post('/api/auth/login', {
      username: form.username,
      password: form.password
    })

    const data = res.data

    // Check if user is ADMIN
    if (data.role !== 'ADMIN') {
      ElMessage.error('权限不足：仅管理员可登录后台')
      loading.value = false
      return
    }

    setToken(data.token)
    setUser({
      userId: data.userId,
      username: data.username,
      role: data.role
    })

    ElMessage.success('登录成功')
    router.push('/')
  } catch (err) {
    ElMessage.error(err.response?.data?.message || '登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 50%, #0f172a 100%);
}

.login-card {
  width: 420px;
  padding: 48px 40px;
  background: rgba(30, 41, 59, 0.8);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(148, 163, 184, 0.1);
  border-radius: 20px;
  box-shadow: 0 25px 60px rgba(0, 0, 0, 0.4);
}

.login-icon {
  text-align: center;
  font-size: 48px;
  margin-bottom: 16px;
}

.login-title {
  text-align: center;
  margin-bottom: 8px;
  color: #f1f5f9;
  font-size: 24px;
  font-weight: 700;
  letter-spacing: -0.5px;
}

.login-subtitle {
  text-align: center;
  margin-bottom: 32px;
  color: #94a3b8;
  font-size: 14px;
}

:deep(.el-input__wrapper) {
  background: rgba(15, 23, 42, 0.6);
  border: 1px solid rgba(148, 163, 184, 0.15);
  border-radius: 10px;
  box-shadow: none !important;
}

:deep(.el-input__wrapper:hover) {
  border-color: rgba(59, 130, 246, 0.4);
}

:deep(.el-input__wrapper.is-focus) {
  border-color: #3b82f6;
}

:deep(.el-input__inner) {
  color: #e2e8f0;
}

:deep(.el-input__prefix .el-icon) {
  color: #64748b;
}

:deep(.el-button--primary) {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  border: none;
  border-radius: 10px;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 2px;
}

:deep(.el-button--primary:hover) {
  background: linear-gradient(135deg, #60a5fa 0%, #3b82f6 100%);
  transform: translateY(-1px);
  box-shadow: 0 8px 24px rgba(59, 130, 246, 0.35);
}

:deep(.el-button.is-link) {
  color: #64748b;
}

:deep(.el-button.is-link:hover) {
  color: #93c5fd;
}
</style>
