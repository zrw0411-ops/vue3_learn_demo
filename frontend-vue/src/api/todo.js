import { api } from '@/stores/user'

export const todoApi = {
  getMyTodos: () => api.get('/todo/list'),
  getUnread: () => api.get('/todo/unread'),
  markAsRead: (id) => api.put(`/todo/read/${id}`),
  markAsDone: (id) => api.put(`/todo/done/${id}`),
}
