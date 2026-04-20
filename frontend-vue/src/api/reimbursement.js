import { api } from '@/stores/user'

export const reimbursementApi = {
  getMyList: () => api.get('/reimbursement/list'),
  getPending: () => api.get('/reimbursement/pending'),
  getById: (id) => api.get(`/reimbursement/${id}`),
  submit: (data) => api.post('/reimbursement/submit', data),
  approve: (data) => api.put('/reimbursement/approve', data),
  cancel: (id) => api.put(`/reimbursement/cancel/${id}`),
}
