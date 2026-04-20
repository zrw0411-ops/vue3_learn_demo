<template>
  <div>
    <el-card>
      <template #header><span>发布公告</span></template>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" style="max-width:800px">
        <el-form-item label="公告标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入公告标题" />
        </el-form-item>
        <el-form-item label="公告类型" prop="type">
          <el-select v-model="form.type" style="width:100%">
            <el-option v-for="(v,k) in typeMap" :key="k" :label="v" :value="Number(k)" />
          </el-select>
        </el-form-item>
        <el-form-item label="公告内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="8" placeholder="请输入公告内容，支持HTML" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handlePublish(1)">立即发布</el-button>
          <el-button @click="handlePublish(0)">保存草稿</el-button>
          <el-button @click="$router.back()">返 回</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { noticeApi } from '@/api/notice'

const router = useRouter()
const formRef = ref()
const loading = ref(false)
const typeMap = { 1: '系统通知', 2: '公司公告', 3: '人事通知' }
const form = reactive({ title: '', type: 2, content: '', status: 1 })
const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  type: [{ required: true, message: '请选择类型', trigger: 'change' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
}

const handlePublish = async (status) => {
  await formRef.value.validate()
  loading.value = true
  try {
    form.status = status
    await noticeApi.publish(form)
    ElMessage.success(status === 1 ? '发布成功' : '保存草稿成功')
    router.push('/notice')
  } catch (e) {
    ElMessage.error(e.response?.data?.message || '操作失败')
  } finally {
    loading.value = false
  }
}
</script>
