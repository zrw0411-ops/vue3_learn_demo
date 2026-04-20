<template>
  <el-container style="height: 100vh">
    <!-- 侧边栏 -->
    <el-aside width="200px" style="background:#304156">
      <div class="logo">OA系统</div>
      <el-menu
        :default-active="$route.path"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        router
      >
        <el-menu-item index="/dashboard">
          <el-icon><House /></el-icon><span>工作台</span>
        </el-menu-item>
        <el-menu-item index="/leave">
          <el-icon><Calendar /></el-icon><span>请假管理</span>
        </el-menu-item>
        <el-menu-item index="/reimbursement">
          <el-icon><Money /></el-icon><span>报销管理</span>
        </el-menu-item>
        <el-menu-item index="/notice">
          <el-icon><Bell /></el-icon><span>公告管理</span>
        </el-menu-item>
        <el-menu-item index="/organization/employee">
          <el-icon><User /></el-icon><span>员工管理</span>
        </el-menu-item>
        <el-menu-item index="/organization/department">
          <el-icon><OfficeBuilding /></el-icon><span>部门管理</span>
        </el-menu-item>
        <el-menu-item index="/system/profile">
          <el-icon><Setting /></el-icon><span>系统设置</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <!-- 顶部 -->
      <el-header style="display:flex;align-items:center;justify-content:space-between;background:#fff;border-bottom:1px solid #e6e6e6">
        <span style="font-size:16px;font-weight:bold">{{ $route.meta.title }}</span>
        <div style="display:flex;align-items:center;gap:12px">
          <el-badge :value="todoCount" v-if="todoCount > 0">
            <el-icon size="20" style="cursor:pointer" @click="$router.push('/')"><Bell /></el-icon>
          </el-badge>
          <span>{{ userInfo?.realName || userInfo?.username }}</span>
          <el-button size="small" @click="handleLogout">退出</el-button>
        </div>
      </el-header>

      <!-- 主内容 -->
      <el-main style="background:#f0f2f5">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { todoApi } from '@/api/todo'
import { ElMessageBox } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const userInfo = ref({})
const todoCount = ref(0)

onMounted(async () => {
  try {
    userInfo.value = await userStore.getUserInfo()
    const { data } = await todoApi.getUnread()
    todoCount.value = data.data?.length || 0
  } catch (e) {
    console.error(e)
  }
})

const handleLogout = () => {
  ElMessageBox.confirm('确定退出登录？', '提示', { type: 'warning' })
    .then(() => {
      userStore.logout()
      router.push('/login')
    })
    .catch(() => {})
}
</script>

<style scoped>
.el-aside .logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  background: #263445;
}
.el-aside .el-menu {
  border-right: none;
}
</style>
