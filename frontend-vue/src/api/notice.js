import { api } from '@/stores/user'

export const noticeApi = {
  getPublished: () => api.get('/notice/public/list'),
  getAll: () => api.get('/notice/list'),
  getById: (id) => api.get(`/notice/${id}`),
  publish: (notice) => api.post('/notice/publish', notice),
  update: (notice) => api.put('/notice/update', notice),
  delete: (id) => api.delete(`/notice/${id}`),
}
