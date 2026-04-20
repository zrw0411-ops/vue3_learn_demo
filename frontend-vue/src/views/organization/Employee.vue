<template>
  <div>
    <el-card>
      <template #header><span>员工管理</span></template>
      <el-table :data="employees" stripe>
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="realName" label="姓名" />
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="departmentId" label="部门ID" />
        <el-table-column prop="role" label="角色" width="100">
          <template #default="{ row }">{{ roleMap[row.role] }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { userApi } from '@/api/user'

const employees = ref([])
const roleMap = { 1: '员工', 2: '主管', 3: 'HR', 4: '管理员' }

onMounted(async () => {
  try {
    employees.value = (await userApi.getAllUsers()).data.data || []
  } catch (e) { console.error(e) }
})
</script>
