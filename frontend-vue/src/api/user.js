import { api } from '@/stores/user'

export const userApi = {
  login: (username, password) => api.post('/user/login', { username, password }),
  register: (user) => api.post('/user/register', user),
  getInfo: () => api.get('/user/info'),
  getAllUsers: () => api.get('/user/list'),
  getUsersByDept: (deptId) => api.get(`/user/department/${deptId}`),
  updateUser: (user) => api.put('/user/update', user),
  updatePassword: (oldPwd, newPwd) => api.put('/user/password', { oldPassword: oldPwd, newPassword: newPwd }),
}
