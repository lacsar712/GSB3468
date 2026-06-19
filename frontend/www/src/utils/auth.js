import axios from 'axios'

const AUTH_TOKEN_KEY = 'fishfrineds_token'
const AUTH_USER_KEY = 'fishfrineds_user'

// 获取 token
export function getToken() {
  return localStorage.getItem(AUTH_TOKEN_KEY)
}

// 设置 token
export function setToken(token) {
  localStorage.setItem(AUTH_TOKEN_KEY, token)
  // 设置 axios 默认 header
  axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
}

// 移除 token
export function removeToken() {
  localStorage.removeItem(AUTH_TOKEN_KEY)
  delete axios.defaults.headers.common['Authorization']
}

// 获取用户信息
export function getUser() {
  const userStr = localStorage.getItem(AUTH_USER_KEY)
  return userStr ? JSON.parse(userStr) : null
}

// 设置用户信息
export function setUser(user) {
  localStorage.setItem(AUTH_USER_KEY, JSON.stringify(user))
}

// 移除用户信息
export function removeUser() {
  localStorage.removeItem(AUTH_USER_KEY)
}

// 是否已登录
export function isLoggedIn() {
  return !!getToken()
}

// 退出登录
export function logout() {
  removeToken()
  removeUser()
}

// 初始化 axios 拦截器
export function initAuth() {
  const token = getToken()
  if (token) {
    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
  }

  // 响应拦截器处理 401
  axios.interceptors.response.use(
    response => response,
    error => {
      if (error.response && error.response.status === 401) {
        logout()
        window.location.href = '/login'
      }
      return Promise.reject(error)
    }
  )
}

// 登录
export async function login(username, password) {
  const response = await axios.post('/api/auth/login', { username, password })
  if (response.data.token) {
    setToken(response.data.token)
    setUser({
      username: response.data.username,
      role: response.data.role
    })
  }
  return response.data
}

// 注册
export async function register(username, password, nickname) {
  const response = await axios.post('/api/auth/register', { username, password, nickname })
  if (response.data.token) {
    setToken(response.data.token)
    setUser({
      username: response.data.username,
      role: response.data.role
    })
  }
  return response.data
}
