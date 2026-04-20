<template>
  <div>
    <el-card>
      <template #header>
        <div style="display:flex;justify-content:space-between">
          <span>报销管理</span>
          <el-button type="primary" @click="$router.push('/reimbursement/form')">提交报销</el-button>
        </div>
      </template>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="我的报销" name="mine">
          <el-table :data="myList" stripe>
            <el-table-column prop="title" label="标题" show-overflow-tooltip />
            <el-table-column prop="amount" label="金额" width="100">
              <template #default="{ row }">￥{{ row.amount }}</template>
            </el-table-column>
            <el-table-column prop="type" label="类型" width="80">
              <template #default="{ row }">{{ typeMap[row.type] }}</template>
            </el-table-column>
            <el-table-column prop="description" label="说明" show-overflow-tooltip />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="statusType[row.status]">{{ statusMap[row.status] }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="申请时间" />
            <el-table-column label="操作" width="100">
              <template #default="{ row }">
                <el-button size="small" v-if="row.status === 0" @click="cancel(row.id)">撤回</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="待我审批" name="pending" v-if="userInfo?.role >= 2">
          <el-table :data="pendingList" stripe>
            <el-table-column prop="userName" label="申请人" />
            <el-table-column prop="title" label="标题" />
            <el-table-column prop="amount" label="金额" width="100">
              <template #default="{ row }">￥{{ row.amount }}</template>
            </el-table-column>
            <el-table-column prop="type" label="类型" width="80">
              <template #default="{ row }">{{ typeMap[row.type] }}</template>
            </el-table-column>
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
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { reimbursementApi } from '@/api/reimbursement'

const { userInfo } = useUserStore()
const activeTab = ref('mine')
const myList = ref([])
const pendingList = ref([])
const typeMap = { 1: '差旅', 2: '交通', 3: '餐饮', 4: '办公', 5: '其他' }
const statusMap = { 0: '待审批', 1: '已通过', 2: '财务通过', 3: '已完成', '-1': '已驳回', '-2': '已撤回' }
const statusType = { 0: 'warning', 1: 'success', 2: 'success', 3: 'success', '-1': 'danger', '-2': 'info' }

const loadMy = async () => {
  try { myList.value = (await reimbursementApi.getMyList()).data.data || [] } catch (e) {}
}
const loadPending = async () => {
  if (userInfo?.role >= 2) {
    try { pendingList.value = (await reimbursementApi.getPending()).data.data || [] } catch (e) {}
  }
}
const cancel = async (id) => {
  await reimbursementApi.cancel(id)
  ElMessage.success('撤回成功')
  loadMy()
}
const approve = async (row, status) => {
  await reimbursementApi.approve({ id: row.id, status, approveComment: '同意' })
  ElMessage.success('审批成功')
  loadPending()
}
const openReject = (row) => {}

onMounted(() => { loadMy(); loadPending() })
</script>
