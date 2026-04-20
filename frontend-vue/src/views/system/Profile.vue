<template>
  <div>
    <el-card style="max-width:600px">
      <template #header><span>个人信息</span></template>
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">{{ form.username }}</el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="form.realName" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleSave">保 存</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { userApi } from '@/api/user'

const userStore = useUserStore()
const loading = ref(false)
const form = reactive({ id: null, username: '', realName: '', phone: '', email: '' })

onMounted(async () => {
  const info = await userStore.getUserInfo()
  Object.assign(form, info)
})

const handleSave = async () => {
  loading.value = true
  try {
    await userApi.updateUser(form)
    ElMessage.success('保存成功')
  } catch (e) {
    ElMessage.error(e.response?.data?.message || '保存失败')
  } finally {
    loading.value = false
  }
}
</script>
