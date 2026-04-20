import { api } from '@/stores/user'

export const departmentApi = {
  getAll: () => api.get('/department/list'),
  getById: (id) => api.get(`/department/${id}`),
  add: (dept) => api.post('/department/add', dept),
  update: (dept) => api.put('/department/update', dept),
  delete: (id) => api.delete(`/department/${id}`),
}
