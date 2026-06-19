import { createRouter, createWebHistory } from 'vue-router'
import { isLoggedIn } from '../utils/auth.js'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue')
  },
  {
    path: '/spots',
    name: 'Spots',
    component: () => import('../views/Spots.vue')
  },
  {
    path: '/spots/create',
    name: 'SpotCreate',
    component: () => import('../views/SpotCreate.vue'),
    meta: { requiresAuth: true },
    props: { isEdit: false }
  },
  {
    path: '/spots/:id/edit',
    name: 'SpotEdit',
    component: () => import('../views/SpotCreate.vue'),
    meta: { requiresAuth: true },
    props: route => ({ isEdit: true, id: route.params.id })
  },
  {
    path: '/catches',
    name: 'Catches',
    component: () => import('../views/Catches.vue')
  },
  {
    path: '/catches/create',
    name: 'CatchCreate',
    component: () => import('../views/CatchCreate.vue'),
    meta: { requiresAuth: true },
    props: { isEdit: false }
  },
  {
    path: '/catches/:id/edit',
    name: 'CatchEdit',
    component: () => import('../views/CatchCreate.vue'),
    meta: { requiresAuth: true },
    props: route => ({ isEdit: true, id: route.params.id })
  },
  {
    path: '/life',
    name: 'Life',
    component: () => import('../views/Life.vue')
  },
  {
    path: '/life/create',
    name: 'LifeCreate',
    component: () => import('../views/LifeCreate.vue'),
    meta: { requiresAuth: true },
    props: { isEdit: false }
  },
  {
    path: '/life/:id/edit',
    name: 'LifeEdit',
    component: () => import('../views/LifeCreate.vue'),
    meta: { requiresAuth: true },
    props: route => ({ isEdit: true, id: route.params.id })
  },
  {
    path: '/friends',
    name: 'Friends',
    component: () => import('../views/Friends.vue')
  },
  {
    path: '/friends/create',
    name: 'FriendCreate',
    component: () => import('../views/FriendCreate.vue'),
    meta: { requiresAuth: true },
    props: { isEdit: false }
  },
  {
    path: '/friends/:id/edit',
    name: 'FriendEdit',
    component: () => import('../views/FriendCreate.vue'),
    meta: { requiresAuth: true },
    props: route => ({ isEdit: true, id: route.params.id })
  },
  {
    path: '/gear',
    name: 'Gear',
    component: () => import('../views/Gear.vue')
  },
  {
    path: '/gear/create',
    name: 'GearCreate',
    component: () => import('../views/GearCreate.vue'),
    meta: { requiresAuth: true },
    props: { isEdit: false }
  },
  {
    path: '/gear/:id/edit',
    name: 'GearEdit',
    component: () => import('../views/GearCreate.vue'),
    meta: { requiresAuth: true },
    props: route => ({ isEdit: true, id: route.params.id })
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { guestOnly: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: { guestOnly: true }
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('../views/Profile.vue'),
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const loggedIn = isLoggedIn()

  // 需要登录的页面
  if (to.meta.requiresAuth && !loggedIn) {
    next('/login')
    return
  }

  // 仅访客可访问的页面（登录后不能访问登录/注册页）
  if (to.meta.guestOnly && loggedIn) {
    next('/')
    return
  }

  next()
})

export default router
