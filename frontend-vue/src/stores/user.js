import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from 'axios'

const api = axios.create({
  baseURL: '/api'
})

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(null)

  const login = async (username, password) => {
    const { data } = await api.post('/user/login', { username, password })
    token.value = data.data.token
    localStorage.setItem('token', data.data.token)
    return data
  }

  const getUserInfo = async () => {
    const { data } = await api.get('/user/info')
    userInfo.value = data.data
    return data.data
  }

  const logout = () => {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
  }

  return { token, userInfo, login, getUserInfo, logout }
})

export { api }
