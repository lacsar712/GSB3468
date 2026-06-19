<template>
  <div class="manage-page">
    <div class="page-header">
      <h2>钓点管理</h2>
    </div>

    <el-table :data="items" stripe style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="title" label="标题" min-width="180" />
      <el-table-column prop="location" label="位置" width="150" />
      <el-table-column prop="fishTypes" label="鱼种" width="150" />
      <el-table-column prop="bestSeason" label="最佳季节" width="100" />
      <el-table-column prop="createdAt" label="发布时间" width="170">
        <template #default="{ row }">{{ formatDate(row.createdAt) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="100" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="danger" @click="deleteItem(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination v-model:current-page="page" :page-size="pageSize" :total="total"
        layout="prev, pager, next" @current-change="fetchItems" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const items = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = 10
const total = ref(0)

const fetchItems = async () => {
  loading.value = true
  try {
    const res = await axios.get('/api/spots', { params: { page: page.value - 1, size: pageSize } })
    if (res.data.code === 200) {
      items.value = res.data.data.content
      total.value = res.data.data.totalElements
    }
  } catch (err) { ElMessage.error('加载失败') }
  finally { loading.value = false }
}

const deleteItem = async (row) => {
  try {
    await ElMessageBox.confirm(`确定删除钓点「${row.title}」吗？`, '删除确认', { type: 'error' })
    await axios.delete(`/api/spots/${row.id}`)
    ElMessage.success('删除成功')
    fetchItems()
  } catch (err) { if (err !== 'cancel') ElMessage.error('删除失败') }
}

const formatDate = (d) => d ? new Date(d).toLocaleString('zh-CN') : '—'

onMounted(() => fetchItems())
</script>

<style scoped>
.manage-page { max-width: 1100px; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
.page-header h2 { font-size: 22px; font-weight: 700; color: #f1f5f9; }
.pagination { margin-top: 20px; display: flex; justify-content: flex-end; }
:deep(.el-table) {
  --el-table-bg-color: rgba(30,41,59,0.6); --el-table-tr-bg-color: rgba(30,41,59,0.6);
  --el-table-header-bg-color: rgba(15,23,42,0.8); --el-table-header-text-color: #94a3b8;
  --el-table-text-color: #e2e8f0; --el-table-border-color: rgba(148,163,184,0.08);
  --el-table-row-hover-bg-color: rgba(59,130,246,0.05); border-radius: 12px; overflow: hidden;
}
:deep(.el-table--striped .el-table__body tr.el-table__row--striped td.el-table__cell) { background: rgba(15,23,42,0.4); }
:deep(.el-pagination) { --el-pagination-bg-color: transparent; --el-pagination-text-color: #94a3b8; }
:deep(.el-pager li) { background: transparent !important; color: #94a3b8; }
:deep(.el-pager li.is-active) { color: #60a5fa; }
</style>
