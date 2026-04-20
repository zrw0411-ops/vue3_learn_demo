<template>
  <div>
    <el-card style="max-width:500px">
      <template #header><span>修改密码</span></template>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="form.oldPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="form.newPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleSubmit">确认修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { userApi } from '@/api/user'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref()
const loading = ref(false)
const form = reactive({ oldPassword: '', newPassword: '', confirmPassword: '' })
const rules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }, { min: 6, message: '密码至少6位', trigger: 'blur' }],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { validator: (rule, value, callback) => {
      if (value !== form.newPassword) callback(new Error('两次密码不一致'))
      else callback()
    }, trigger: 'blur' }
  ]
}

const handleSubmit = async () => {
  await formRef.value.validate()
  loading.value = true
  try {
    await userApi.updatePassword(form.oldPassword, form.newPassword)
    ElMessage.success('密码修改成功，请重新登录')
    userStore.logout()
    router.push('/login')
  } catch (e) {
    ElMessage.error(e.response?.data?.message || '修改失败')
  } finally {
    loading.value = false
  }
}
</script>
