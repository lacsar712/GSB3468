<template>
  <div class="manage-page">
    <div class="page-header">
      <h2>用户管理</h2>
    </div>

    <el-table :data="users" stripe style="width: 100%" v-loading="loading" class="dark-table">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="username" label="用户名" width="150" />
      <el-table-column prop="nickname" label="昵称" width="150" />
      <el-table-column prop="role" label="角色" width="120">
        <template #default="{ row }">
          <el-tag :type="row.role === 'ADMIN' ? 'danger' : 'info'" size="small">
            {{ row.role === 'ADMIN' ? '管理员' : '普通用户' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="注册时间" width="180">
        <template #default="{ row }">
          {{ formatDate(row.createdAt) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button
            size="small"
            :type="row.role === 'ADMIN' ? 'warning' : 'primary'"
            @click="toggleRole(row)"
          >
            {{ row.role === 'ADMIN' ? '降为用户' : '升为管理员' }}
          </el-button>
          <el-button size="small" type="danger" @click="deleteUser(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination
        v-model:current-page="page"
        :page-size="pageSize"
        :total="total"
        layout="prev, pager, next"
        @current-change="fetchUsers"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const users = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = 10
const total = ref(0)

const fetchUsers = async () => {
  loading.value = true
  try {
    const res = await axios.get('/api/admin/users', { params: { page: page.value - 1, size: pageSize } })
    if (res.data.code === 200) {
      users.value = res.data.data.content
      total.value = res.data.data.totalElements
    }
  } catch (err) {
    ElMessage.error('加载用户列表失败')
  } finally {
    loading.value = false
  }
}

const toggleRole = async (row) => {
  const newRole = row.role === 'ADMIN' ? 'USER' : 'ADMIN'
  try {
    await ElMessageBox.confirm(
      `确定将 ${row.username} 的角色变更为 ${newRole === 'ADMIN' ? '管理员' : '普通用户'} 吗？`,
      '角色变更',
      { type: 'warning' }
    )
    await axios.put(`/api/admin/users/${row.id}/role?role=${newRole}`)
    ElMessage.success('角色变更成功')
    fetchUsers()
  } catch (err) {
    if (err !== 'cancel') ElMessage.error('角色变更失败')
  }
}

const deleteUser = async (row) => {
  try {
    await ElMessageBox.confirm(`确定删除用户 ${row.username} 吗？此操作不可撤销。`, '删除确认', { type: 'error' })
    await axios.delete(`/api/admin/users/${row.id}`)
    ElMessage.success('删除成功')
    fetchUsers()
  } catch (err) {
    if (err !== 'cancel') ElMessage.error('删除失败')
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return '—'
  return new Date(dateStr).toLocaleString('zh-CN')
}

onMounted(() => fetchUsers())
</script>

<style scoped>
.manage-page {
  max-width: 1100px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-header h2 {
  font-size: 22px;
  font-weight: 700;
  color: #f1f5f9;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

:deep(.el-table) {
  --el-table-bg-color: rgba(30, 41, 59, 0.6);
  --el-table-tr-bg-color: rgba(30, 41, 59, 0.6);
  --el-table-header-bg-color: rgba(15, 23, 42, 0.8);
  --el-table-header-text-color: #94a3b8;
  --el-table-text-color: #e2e8f0;
  --el-table-border-color: rgba(148, 163, 184, 0.08);
  --el-table-row-hover-bg-color: rgba(59, 130, 246, 0.05);
  border-radius: 12px;
  overflow: hidden;
}

:deep(.el-table--striped .el-table__body tr.el-table__row--striped td.el-table__cell) {
  background: rgba(15, 23, 42, 0.4);
}

:deep(.el-pagination) {
  --el-pagination-bg-color: transparent;
  --el-pagination-text-color: #94a3b8;
  --el-pagination-button-disabled-color: #475569;
}

:deep(.el-pager li) {
  background: transparent !important;
  color: #94a3b8;
}

:deep(.el-pager li.is-active) {
  color: #60a5fa;
}
</style>
