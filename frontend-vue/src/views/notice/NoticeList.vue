<template>
  <div>
    <el-card>
      <template #header>
        <div style="display:flex;justify-content:space-between">
          <span>公告列表</span>
          <el-button type="primary" v-if="userInfo?.role >= 3" @click="$router.push('/notice/publish')">发布公告</el-button>
        </div>
      </template>
      <el-table :data="notices" stripe>
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">{{ typeMap[row.type] }}</template>
        </el-table-column>
        <el-table-column prop="publisherName" label="发布人" width="100" />
        <el-table-column prop="publishTime" label="发布时间" width="160" />
        <el-table-column label="操作" width="150" v-if="userInfo?.role >= 3">
          <template #default="{ row }">
            <el-button size="small" @click="openDetail(row)">查看</el-button>
            <el-button size="small" type="danger" @click="deleteNotice(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="detailVisible" :title="currentNotice?.title" width="600px">
      <div v-html="currentNotice?.content" style="line-height:1.8"></div>
      <template #footer>
        <el-button @click="detailVisible = false">关 闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { noticeApi } from '@/api/notice'

const { userInfo } = useUserStore()
const notices = ref([])
const detailVisible = ref(false)
const currentNotice = ref({})
const typeMap = { 1: '系统通知', 2: '公司公告', 3: '人事通知' }

const load = async () => {
  const res = userInfo?.role >= 3 ? await noticeApi.getAll() : await noticeApi.getPublished()
  notices.value = res.data.data || []
}
const openDetail = (row) => {
  currentNotice.value = row
  detailVisible.value = true
}
const deleteNotice = async (id) => {
  await ElMessageBox.confirm('确定删除该公告？', '提示', { type: 'warning' })
  await noticeApi.delete(id)
  ElMessage.success('删除成功')
  load()
}
onMounted(load)
</script>
