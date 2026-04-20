import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/views/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/Dashboard.vue'),
        meta: { title: '工作台' }
      },
      {
        path: 'leave',
        name: 'Leave',
        component: () => import('@/views/leave/LeaveList.vue'),
        meta: { title: '请假管理' }
      },
      {
        path: 'leave/form',
        name: 'LeaveForm',
        component: () => import('@/views/leave/LeaveForm.vue'),
        meta: { title: '提交请假' }
      },
      {
        path: 'reimbursement',
        name: 'Reimbursement',
        component: () => import('@/views/reimbursement/ReimbursementList.vue'),
        meta: { title: '报销管理' }
      },
      {
        path: 'reimbursement/form',
        name: 'ReimbursementForm',
        component: () => import('@/views/reimbursement/ReimbursementForm.vue'),
        meta: { title: '提交报销' }
      },
      {
        path: 'notice',
        name: 'Notice',
        component: () => import('@/views/notice/NoticeList.vue'),
        meta: { title: '公告管理' }
      },
      {
        path: 'notice/publish',
        name: 'NoticePublish',
        component: () => import('@/views/notice/NoticePublish.vue'),
        meta: { title: '发布公告' }
      },
      {
        path: 'organization/employee',
        name: 'Employee',
        component: () => import('@/views/organization/Employee.vue'),
        meta: { title: '员工管理' }
      },
      {
        path: 'organization/department',
        name: 'Department',
        component: () => import('@/views/organization/Department.vue'),
        meta: { title: '部门管理' }
      },
      {
        path: 'system/profile',
        name: 'Profile',
        component: () => import('@/views/system/Profile.vue'),
        meta: { title: '个人信息' }
      },
      {
        path: 'system/password',
        name: 'Password',
        component: () => import('@/views/system/Password.vue'),
        meta: { title: '修改密码' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - OA系统` : 'OA办公系统'
  const token = localStorage.getItem('token')
  if (!token && to.path !== '/login') {
    next('/login')
  } else {
    next()
  }
})

export default router
