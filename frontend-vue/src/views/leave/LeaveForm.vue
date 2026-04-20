<template>
  <div>
    <el-card>
      <template #header><span>提交请假</span></template>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" style="max-width:600px">
        <el-form-item label="请假类型" prop="leaveType">
          <el-select v-model="form.leaveType" style="width:100%">
            <el-option v-for="(v,k) in typeMap" :key="k" :label="v" :value="Number(k)" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker v-model="form.startDate" type="date" value-format="YYYY-MM-DD" style="width:100%" />
        </el-form-item>
        <el-form-item label="结束日期" prop="endDate">
          <el-date-picker v-model="form.endDate" type="date" value-format="YYYY-MM-DD" style="width:100%" />
        </el-form-item>
        <el-form-item label="请假天数" prop="days">
          <el-input-number v-model="form.days" :min="1" :max="30" style="width:100%" />
        </el-form-item>
        <el-form-item label="请假原因" prop="reason">
          <el-input v-model="form.reason" type="textarea" :rows="4" placeholder="请输入请假原因" />
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
import { leaveApi } from '@/api/leave'

const router = useRouter()
const formRef = ref()
const loading = ref(false)
const typeMap = { 1: '事假', 2: '病假', 3: '年假', 4: '婚假', 5: '丧假' }
const form = reactive({ leaveType: 1, startDate: '', endDate: '', days: 1, reason: '' })
const rules = {
  leaveType: [{ required: true, message: '请选择类型', trigger: 'change' }],
  startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
  endDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }],
  days: [{ required: true, message: '请输入天数', trigger: 'blur' }],
  reason: [{ required: true, message: '请输入原因', trigger: 'blur' }]
}

const handleSubmit = async () => {
  await formRef.value.validate()
  loading.value = true
  try {
    await leaveApi.submit(form)
    ElMessage.success('提交成功')
    router.push('/leave')
  } catch (e) {
    ElMessage.error(e.response?.data?.message || '提交失败')
  } finally {
    loading.value = false
  }
}
</script>
