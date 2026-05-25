import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import RecruitmentPage from '../presentation/pages/RecruitmentPage.vue'
import { tokenStorage } from '../infrastructure/storage/tokenStorage'
import type { Role } from '../domain/entities/user'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/jobs',
  },
  {
    path: '/jobs',
    component: RecruitmentPage,
    meta: { section: 'jobs' },
  },
  {
    path: '/candidate',
    component: RecruitmentPage,
    meta: { role: 'CANDIDATE', section: 'candidate' },
  },
  {
    path: '/company',
    component: RecruitmentPage,
    meta: { role: 'COMPANY', section: 'company' },
  },
  {
    path: '/admin',
    component: RecruitmentPage,
    meta: { role: 'ADMIN', section: 'admin' },
  },
]

export const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to) => {
  const requiredRole = to.meta.role as Role | undefined
  if (!requiredRole) {
    return true
  }

  const token = tokenStorage.get()
  const currentRole = tokenStorage.getUserRole()
  if (!token) {
    return { path: '/jobs', query: { auth: 'login' } }
  }

  if (currentRole && currentRole !== requiredRole) {
    return { path: '/jobs' }
  }

  return true
})
