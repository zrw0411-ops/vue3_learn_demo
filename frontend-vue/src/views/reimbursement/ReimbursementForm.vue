<template>
  <div>
    <el-card>
      <template #header><span>提交报销</span></template>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" style="max-width:600px">
        <el-form-item label="报销标题" prop="title">
          <el-input v-model="form.title" placeholder="如：北京出差报销" />
        </el-form-item>
        <el-form-item label="报销类型" prop="type">
          <el-select v-model="form.type" style="width:100%">
            <el-option v-for="(v,k) in typeMap" :key="k" :label="v" :value="Number(k)" />
          </el-select>
        </el-form-item>
        <el-form-item label="报销金额" prop="amount">
          <el-input-number v-model="form.amount" :min="0.01" :precision="2" style="width:100%" />
        </el-form-item>
        <el-form-item label="报销说明" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请详细描述报销用途" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleSubmit">提 交</el-button>
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
import { reimbursementApi } from '@/api/reimbursement'

const router = useRouter()
const formRef = ref()
const loading = ref(false)
const typeMap = { 1: '差旅费', 2: '交通费', 3: '餐饮费', 4: '办公用品', 5: '其他' }
const form = reactive({ title: '', type: 1, amount: 0, description: '' })
const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  type: [{ required: true, message: '请选择类型', trigger: 'change' }],
  amount: [{ required: true, message: '请输入金额', trigger: 'blur' }],
  description: [{ required: true, message: '请输入说明', trigger: 'blur' }]
}

const handleSubmit = async () => {
  await formRef.value.validate()
  loading.value = true
  try {
    await reimbursementApi.submit(form)
    ElMessage.success('提交成功')
    router.push('/reimbursement')
  } catch (e) {
    ElMessage.error(e.response?.data?.message || '提交失败')
  } finally {
    loading.value = false
  }
}
</script>
