import axios from 'axios'

const AUTH_TOKEN_KEY = 'fishback_token'
const AUTH_USER_KEY = 'fishback_user'

// Token management
export function getToken() {
  return localStorage.getItem(AUTH_TOKEN_KEY)
}

export function setToken(token) {
  localStorage.setItem(AUTH_TOKEN_KEY, token)
  axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
}

export function removeToken() {
  localStorage.removeItem(AUTH_TOKEN_KEY)
  delete axios.defaults.headers.common['Authorization']
}

// User info management
export function getUser() {
  const userStr = localStorage.getItem(AUTH_USER_KEY)
  return userStr ? JSON.parse(userStr) : null
}

export function setUser(user) {
  localStorage.setItem(AUTH_USER_KEY, JSON.stringify(user))
}

export function removeUser() {
  localStorage.removeItem(AUTH_USER_KEY)
}

// Role checks
export function isLoggedIn() {
  return !!getToken()
}

export function isAdmin() {
  const user = getUser()
  return user && user.role === 'ADMIN'
}

// Logout
export function logout() {
  removeToken()
  removeUser()
}

// Initialize axios with stored token + interceptors
export function initAuth() {
  const token = getToken()
  if (token) {
    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
  }

  axios.interceptors.response.use(
    response => response,
    error => {
      if (error.response && (error.response.status === 401 || error.response.status === 403)) {
        logout()
        window.location.href = '/login'
      }
      return Promise.reject(error)
    }
  )
}
