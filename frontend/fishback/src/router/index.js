import { createRouter, createWebHistory } from 'vue-router'
import { isLoggedIn, isAdmin } from '../utils/auth.js'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/',
    name: 'Dashboard',
    component: () => import('../views/Dashboard.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/users',
    name: 'UserManage',
    component: () => import('../views/manage/UserManage.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/spots',
    name: 'SpotManage',
    component: () => import('../views/manage/SpotManage.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/catches',
    name: 'CatchManage',
    component: () => import('../views/manage/CatchManage.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/life',
    name: 'LifeManage',
    component: () => import('../views/manage/LifeManage.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/friends',
    name: 'FriendManage',
    component: () => import('../views/manage/FriendManage.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/gear',
    name: 'GearManage',
    component: () => import('../views/manage/GearManage.vue'),
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth) {
    if (!isLoggedIn() || !isAdmin()) {
      next('/login')
      return
    }
  }

  // Already logged in as admin, skip login page
  if (to.path === '/login' && isLoggedIn() && isAdmin()) {
    next('/')
    return
  }

  next()
})

export default router
