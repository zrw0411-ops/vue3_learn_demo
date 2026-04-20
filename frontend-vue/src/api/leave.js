import { api } from '@/stores/user'

export const leaveApi = {
  getMyLeaves: () => api.get('/leave/list'),
  getPending: () => api.get('/leave/pending'),
  getById: (id) => api.get(`/leave/${id}`),
  submit: (leave) => api.post('/leave/submit', leave),
  approve: (leave) => api.put('/leave/approve', leave),
  cancel: (id) => api.put(`/leave/cancel/${id}`),
}
