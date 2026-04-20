<template>
  <div>
    <el-card>
      <template #header>
        <div style="display:flex;justify-content:space-between">
          <span>请假管理</span>
          <el-button type="primary" @click="$router.push('/leave/form')">提交请假</el-button>
        </div>
      </template>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="我的请假" name="mine">
          <el-table :data="myLeaves" stripe>
            <el-table-column prop="leaveType" label="类型" width="80">
              <template #default="{ row }">{{ typeMap[row.leaveType] }}</template>
            </el-table-column>
            <el-table-column prop="startDate" label="开始日期" />
            <el-table-column prop="endDate" label="结束日期" />
            <el-table-column prop="days" label="天数" width="70" />
            <el-table-column prop="reason" label="原因" show-overflow-tooltip />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="statusType[row.status]">{{ statusMap[row.status] }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="申请时间" />
            <el-table-column label="操作" width="100">
              <template #default="{ row }">
                <el-button size="small" v-if="row.status === 0" @click="cancelLeave(row.id)">撤回</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="待我审批" name="pending" v-if="userInfo?.role >= 2">
          <el-table :data="pendingLeaves" stripe>
            <el-table-column prop="userName" label="申请人" />
            <el-table-column prop="departmentName" label="部门" />
            <el-table-column prop="leaveType" label="类型" width="80">
              <template #default="{ row }">{{ typeMap[row.leaveType] }}</template>
            </el-table-column>
            <el-table-column prop="days" label="天数" width="70" />
            <el-table-column prop="reason" label="原因" show-overflow-tooltip />
            <el-table-column label="操作" width="150">
              <template #default="{ row }">
                <el-button size="small" type="success" @click="approve(row, 1)">通过</el-button>
                <el-button size="small" type="danger" @click="openReject(row)">驳回</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { leaveApi } from '@/api/leave'

const { userInfo } = useUserStore()
const activeTab = ref('mine')
const myLeaves = ref([])
const pendingLeaves = ref([])
const typeMap = { 1: '事假', 2: '病假', 3: '年假', 4: '婚假', 5: '丧假' }
const statusMap = { 0: '待审批', 1: '已通过', 2: '已通过', '-1': '已驳回', '-2': '已撤回' }
const statusType = { 0: 'warning', 1: 'success', 2: 'success', '-1': 'danger', '-2': 'info' }

const loadData = async () => {
  try {
    const res = await leaveApi.getMyLeaves()
    myLeaves.value = res.data.data || []
  } catch (e) { console.error(e) }
}

const loadPending = async () => {
  if (userInfo?.role >= 2) {
    try {
      const res = await leaveApi.getPending()
      pendingLeaves.value = res.data.data || []
    } catch (e) { console.error(e) }
  }
}

const cancelLeave = async (id) => {
  await leaveApi.cancel(id)
  ElMessage.success('撤回成功')
  loadData()
}

const approve = async (row, status) => {
  await leaveApi.approve({ id: row.id, status, approveComment: '同意' })
  ElMessage.success('审批成功')
  loadPending()
}

const openReject = (row) => {
  // 简化：直接驳回
}

onMounted(() => { loadData(); loadPending() })
</script>
