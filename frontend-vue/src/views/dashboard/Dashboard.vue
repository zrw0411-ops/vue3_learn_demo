<template>
  <div>
    <el-row :gutter="20" style="margin-bottom:20px">
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="display:flex;align-items:center;gap:12px">
            <el-icon size="40" color="#409EFF"><Calendar /></el-icon>
            <div>
              <div style="color:#999;font-size:12px">我的请假</div>
              <div style="font-size:28px;font-weight:bold">{{ stats.leaveCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="display:flex;align-items:center;gap:12px">
            <el-icon size="40" color="#67C23A"><Money /></el-icon>
            <div>
              <div style="color:#999;font-size:12px">我的报销</div>
              <div style="font-size:28px;font-weight:bold">￥{{ stats.reimburseAmount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="display:flex;align-items:center;gap:12px">
            <el-icon size="40" color="#F56C6C"><Bell /></el-icon>
            <div>
              <div style="color:#999;font-size:12px">待办事项</div>
              <div style="font-size:28px;font-weight:bold">{{ todoCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="display:flex;align-items:center;gap:12px">
            <el-icon size="40" color="#E6A23C"><TrendCharts /></el-icon>
            <div>
              <div style="color:#999;font-size:12px">公告数量</div>
              <div style="font-size:28px;font-weight:bold">{{ noticeCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <!-- 待办事项 -->
      <el-col :span="12">
        <el-card>
          <template #header><span>待办事项</span></template>
          <el-empty v-if="todos.length === 0" description="暂无待办" />
          <el-list v-else>
            <el-list-item v-for="todo in todos" :key="todo.id">
              <div style="display:flex;justify-content:space-between;width:100%">
                <div>
                  <el-tag size="small" type="warning">{{ todo.type === 1 ? '请假' : '报销' }}</el-tag>
                  <span style="margin-left:8px">{{ todo.title }}</span>
                </div>
                <el-button size="small" type="primary" @click="handleTodo(todo)">处理</el-button>
              </div>
            </el-list-item>
          </el-list>
        </el-card>
      </el-col>

      <!-- 最新公告 -->
      <el-col :span="12">
        <el-card>
          <template #header><span>最新公告</span></template>
          <el-empty v-if="notices.length === 0" description="暂无公告" />
          <el-list v-else>
            <el-list-item v-for="notice in notices" :key="notice.id">
              <div style="display:flex;justify-content:space-between;width:100%">
                <div>
                  <el-tag size="small" type="info">{{ typeMap[notice.type] }}</el-tag>
                  <span style="margin-left:8px">{{ notice.title }}</span>
                </div>
                <span style="color:#999;font-size:12px">{{ formatTime(notice.publishTime) }}</span>
              </div>
            </el-list-item>
          </el-list>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { leaveApi } from '@/api/leave'
import { reimbursementApi } from '@/api/reimbursement'
import { noticeApi } from '@/api/notice'
import { todoApi } from '@/api/todo'
import dayjs from 'dayjs'

const stats = reactive({ leaveCount: 0, reimburseAmount: 0 })
const todos = ref([])
const notices = ref([])
const todoCount = ref(0)
const noticeCount = ref(0)
const typeMap = { 1: '系统', 2: '公司', 3: '人事' }

const formatTime = (t) => t ? dayjs(t).format('MM-DD HH:mm') : ''

const handleTodo = (todo) => {
  if (todo.type === 1) {
    window.location.href = '/leave'
  } else {
    window.location.href = '/reimbursement'
  }
}

onMounted(async () => {
  try {
    const [leaveRes, reimbRes, noticeRes, todoRes] = await Promise.all([
      leaveApi.getMyLeaves(),
      reimbursementApi.getMyList(),
      noticeApi.getPublished(),
      todoApi.getUnread()
    ])
    stats.leaveCount = leaveRes.data.data?.length || 0
    stats.reimburseAmount = reimbRes.data.data?.reduce((s, r) => s + Number(r.amount), 0) || 0
    notices.value = noticeRes.data.data?.slice(0, 5) || []
    notices.value.forEach(n => { noticeCount.value++ })
    todos.value = todoRes.data.data?.slice(0, 5) || []
    todoCount.value = todoRes.data.data?.length || 0
  } catch (e) {
    console.error(e)
  }
})
</script>
