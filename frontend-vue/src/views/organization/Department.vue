<template>
  <div>
    <el-card>
      <template #header>
        <div style="display:flex;justify-content:space-between">
          <span>部门管理</span>
          <el-button type="primary" @click="openForm()">新增部门</el-button>
        </div>
      </template>
      <el-table :data="departments" stripe>
        <el-table-column prop="name" label="部门名称" />
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column prop="managerId" label="主管ID" width="100" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button size="small" @click="openForm(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteDept(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="formVisible" :title="form.id ? '编辑部门' : '新增部门'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="部门名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="部门主管">
          <el-input v-model.number="form.managerId" type="number" placeholder="用户ID" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="formVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSave">保 存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { departmentApi } from '@/api/department'

const departments = ref([])
const formVisible = ref(false)
const form = reactive({ id: null, name: '', description: '', managerId: null })

const load = async () => {
  departments.value = (await departmentApi.getAll()).data.data || []
}

const openForm = (dept = { id: null, name: '', description: '', managerId: null }) => {
  Object.assign(form, dept)
  formVisible.value = true
}

const handleSave = async () => {
  try {
    if (form.id) {
      await departmentApi.update(form)
    } else {
      await departmentApi.add(form)
    }
    ElMessage.success('保存成功')
    formVisible.value = false
    load()
  } catch (e) {
    ElMessage.error(e.response?.data?.message || '保存失败')
  }
}

const deleteDept = async (id) => {
  await ElMessageBox.confirm('确定删除该部门？', '提示', { type: 'warning' })
  await departmentApi.delete(id)
  ElMessage.success('删除成功')
  load()
}

onMounted(load)
</script>
