// 数据源管理页面组件
const Datasource = {
  template: `
    <div class="p-6">
      <div class="mb-6 flex justify-between items-center">
        <div>
          <h2 class="text-2xl font-bold text-gray-800">数据源管理</h2>
          <p class="text-gray-600">配置和管理数据库连接</p>
        </div>
        <button @click="showAddDataSourceModal = true" class="bg-primary hover:bg-indigo-700 text-white py-2 px-4 rounded-lg flex items-center transition-all">
          <i class="fas fa-plus mr-2"></i>
          <span>添加数据源</span>
        </button>
      </div>
      
      <!-- 数据源列表 -->
      <div class="bg-white rounded-lg shadow overflow-hidden">
        <div class="p-4 border-b border-gray-200 flex justify-between items-center">
          <div class="flex items-center">
            <input v-model="searchQuery" type="text" placeholder="搜索数据源..." class="border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary">
            <div class="ml-4">
              <select v-model="typeFilter" class="border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary">
                <option value="">所有类型</option>
                <option value="MySQL">MySQL</option>
                <option value="DB2">DB2</option>
              </select>
            </div>
          </div>
          <div>
            <button class="bg-white border border-gray-300 text-gray-700 py-2 px-4 rounded-lg flex items-center hover:bg-gray-50 transition-all">
              <i class="fas fa-sync-alt mr-2"></i>
              <span>刷新</span>
            </button>
          </div>
        </div>
        
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead>
              <tr>
                <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">名称</th>
                <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">类型</th>
                <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">主机</th>
                <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">数据库</th>
                <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
                <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">同步状态</th>
                <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="(datasource, index) in filteredDatasources" :key="datasource.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="flex items-center">
                    <div class="flex-shrink-0 h-10 w-10 flex items-center justify-center rounded-full" :class="getTypeColorClass(datasource.type)">
                      <i :class="getTypeIcon(datasource.type)" class="text-white"></i>
                    </div>
                    <div class="ml-4">
                      <div class="text-sm font-medium text-gray-900">{{ datasource.name }}</div>
                      <div class="text-sm text-gray-500">{{ datasource.description }}</div>
                    </div>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full" :class="getTypeColorClass(datasource.type)">
                    {{ datasource.type }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {{ datasource.host }}:{{ datasource.port }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {{ datasource.database }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full" :class="datasource.status === 'online' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'">
                    {{ datasource.status === 'online' ? '在线' : '离线' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="flex items-center">
                    <div v-if="datasource.syncStatus === 'syncing'" class="mr-2 animate-spin">
                      <i class="fas fa-sync-alt text-blue-500"></i>
                    </div>
                    <span class="text-sm" :class="getSyncStatusClass(datasource.syncStatus)">
                      {{ getSyncStatusText(datasource.syncStatus) }}
                    </span>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <div class="flex space-x-2">
                    <button @click="editDatasource(datasource)" class="text-indigo-600 hover:text-indigo-900">
                      <i class="fas fa-edit"></i>
                    </button>
                    <button @click="syncDatasource(datasource)" class="text-blue-600 hover:text-blue-900" :disabled="datasource.syncStatus === 'syncing'">
                      <i class="fas fa-sync-alt"></i>
                    </button>
                    <button @click="testConnection(datasource)" class="text-green-600 hover:text-green-900">
                      <i class="fas fa-plug"></i>
                    </button>
                    <button @click="confirmDelete(datasource)" class="text-red-600 hover:text-red-900">
                      <i class="fas fa-trash"></i>
                    </button>
                  </div>
                </td>
              </tr>
              
              <!-- 无数据提示 -->
              <tr v-if="filteredDatasources.length === 0">
                <td colspan="7" class="px-6 py-10 text-center text-gray-500">
                  <div class="flex flex-col items-center">
                    <i class="fas fa-database text-gray-300 text-5xl mb-4"></i>
                    <p>没有找到匹配的数据源</p>
                    <button @click="showAddDataSourceModal = true" class="mt-2 text-indigo-600 hover:text-indigo-900">
                      添加新数据源
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        
        <!-- 分页 -->
        <div class="px-6 py-4 flex items-center justify-between border-t border-gray-200">
          <div>
            <p class="text-sm text-gray-700">
              显示第 <span class="font-medium">{{ startItem }}</span> 到 <span class="font-medium">{{ endItem }}</span> 条，共 <span class="font-medium">{{ totalItems }}</span> 条
            </p>
          </div>
          <div>
            <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px" aria-label="Pagination">
              <a href="#" @click.prevent="prevPage" class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50">
                <span class="sr-only">上一页</span>
                <i class="fas fa-chevron-left"></i>
              </a>
              <template v-for="page in displayedPages" :key="page">
                <a href="#" 
                   @click.prevent="goToPage(page)" 
                   :class="{'z-10 bg-primary border-primary text-white': currentPage === page, 'bg-white border-gray-300 text-gray-500 hover:bg-gray-50': currentPage !== page}"
                   class="relative inline-flex items-center px-4 py-2 border text-sm font-medium">
                  {{ page }}
                </a>
              </template>
              <a href="#" @click.prevent="nextPage" class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50">
                <span class="sr-only">下一页</span>
                <i class="fas fa-chevron-right"></i>
              </a>
            </nav>
          </div>
        </div>
      </div>
      
      <!-- 添加/编辑数据源模态框 -->
      <div v-if="showAddDataSourceModal" class="fixed inset-0 overflow-y-auto z-50">
        <div class="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
          <div class="fixed inset-0 transition-opacity" aria-hidden="true">
            <div class="absolute inset-0 bg-gray-500 opacity-75"></div>
          </div>
          <span class="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>
          <div class="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
            <div class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
              <div class="sm:flex sm:items-start">
                <div class="mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left w-full">
                  <h3 class="text-lg leading-6 font-medium text-gray-900 mb-4">
                    {{ editingDatasource ? '编辑数据源' : '添加新数据源' }}
                  </h3>
                  <div class="mt-2 space-y-4">
                    <div>
                      <label class="block text-sm font-medium text-gray-700 mb-1">数据源名称</label>
                      <input v-model="datasourceForm.name" type="text" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary">
                    </div>
                    <div>
                      <label class="block text-sm font-medium text-gray-700 mb-1">描述</label>
                      <input v-model="datasourceForm.description" type="text" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary">
                    </div>
                    <div>
                      <label class="block text-sm font-medium text-gray-700 mb-1">数据库类型</label>
                      <select v-model="datasourceForm.type" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary">
                        <option value="MySQL">MySQL</option>
                        <option value="DB2">DB2</option>
                      </select>
                    </div>
                    <div>
                      <label class="block text-sm font-medium text-gray-700 mb-1">主机地址</label>
                      <input v-model="datasourceForm.host" type="text" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary">
                    </div>
                    <div>
                      <label class="block text-sm font-medium text-gray-700 mb-1">端口</label>
                      <input v-model="datasourceForm.port" type="text" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary">
                    </div>
                    <div>
                      <label class="block text-sm font-medium text-gray-700 mb-1">数据库名称</label>
                      <input v-model="datasourceForm.database" type="text" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary">
                    </div>
                    <div>
                      <label class="block text-sm font-medium text-gray-700 mb-1">用户名</label>
                      <input v-model="datasourceForm.username" type="text" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary">
                    </div>
                    <div>
                      <label class="block text-sm font-medium text-gray-700 mb-1">密码</label>
                      <input v-model="datasourceForm.password" type="password" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary">
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
              <button @click="saveDatasource" type="button" class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-primary text-base font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary sm:ml-3 sm:w-auto sm:text-sm">
                保存
              </button>
              <button @click="showAddDataSourceModal = false" type="button" class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm">
                取消
              </button>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 删除确认模态框 -->
      <div v-if="showDeleteModal" class="fixed inset-0 overflow-y-auto z-50">
        <div class="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
          <div class="fixed inset-0 transition-opacity" aria-hidden="true">
            <div class="absolute inset-0 bg-gray-500 opacity-75"></div>
          </div>
          <span class="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>
          <div class="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
            <div class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
              <div class="sm:flex sm:items-start">
                <div class="mx-auto flex-shrink-0 flex items-center justify-center h-12 w-12 rounded-full bg-red-100 sm:mx-0 sm:h-10 sm:w-10">
                  <i class="fas fa-exclamation-triangle text-red-600"></i>
                </div>
                <div class="mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left">
                  <h3 class="text-lg leading-6 font-medium text-gray-900">
                    删除数据源
                  </h3>
                  <div class="mt-2">
                    <p class="text-sm text-gray-500">
                      您确定要删除数据源 "{{ datasourceToDelete?.name }}" 吗？此操作无法撤销，并且将删除与此数据源相关的所有元数据。
                    </p>
                  </div>
                </div>
              </div>
            </div>
            <div class="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
              <button @click="deleteDatasource" type="button" class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-red-600 text-base font-medium text-white hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 sm:ml-3 sm:w-auto sm:text-sm">
                删除
              </button>
              <button @click="showDeleteModal = false" type="button" class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm">
                取消
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  data() {
    return {
      // 数据源列表
      datasources: [
        { 
          id: 1, 
          name: '销售数据库', 
          description: '包含销售订单和客户信息',
          type: 'MySQL', 
          host: '192.168.1.100', 
          port: 3306, 
          database: 'sales_db',
          username: 'sales_user',
          status: 'online',
          syncStatus: 'synced',
          lastSyncTime: '2023-03-15 14:30:00'
        },
        { 
          id: 2, 
          name: 'CRM数据库', 
          description: '客户关系管理系统数据',
          type: 'MySQL', 
          host: '192.168.1.101', 
          port: 3306, 
          database: 'crm_db',
          username: 'crm_user',
          status: 'online',
          syncStatus: 'synced',
          lastSyncTime: '2023-03-15 15:45:00'
        },
        { 
          id: 3, 
          name: '仓储数据库', 
          description: '仓库和库存管理数据',
          type: 'DB2', 
          host: '192.168.1.102', 
          port: 50000, 
          database: 'warehouse_db',
          username: 'warehouse_user',
          status: 'online',
          syncStatus: 'syncing',
          lastSyncTime: '2023-03-15 10:20:00'
        },
        { 
          id: 4, 
          name: 'HR数据库', 
          description: '人力资源管理系统数据',
          type: 'DB2', 
          host: '192.168.1.103', 
          port: 50000, 
          database: 'hr_db',
          username: 'hr_user',
          status: 'offline',
          syncStatus: 'failed',
          lastSyncTime: '2023-03-14 16:30:00'
        }
      ],
      
      // 搜索和筛选
      searchQuery: '',
      typeFilter: '',
      
      // 分页
      currentPage: 1,
      pageSize: 10,
      
      // 模态框控制
      showAddDataSourceModal: false,
      showDeleteModal: false,
      
      // 表单数据
      datasourceForm: {
        name: '',
        description: '',
        type: 'MySQL',
        host: '',
        port: '',
        database: '',
        username: '',
        password: ''
      },
      
      // 编辑状态
      editingDatasource: null,
      
      // 删除确认
      datasourceToDelete: null
    }
  },
  computed: {
    // 过滤后的数据源列表
    filteredDatasources() {
      let result = this.datasources
      
      // 应用搜索
      if (this.searchQuery) {
        const query = this.searchQuery.toLowerCase()
        result = result.filter(ds => 
          ds.name.toLowerCase().includes(query) || 
          ds.description.toLowerCase().includes(query) ||
          ds.host.toLowerCase().includes(query) ||
          ds.database.toLowerCase().includes(query)
        )
      }
      
      // 应用类型筛选
      if (this.typeFilter) {
        result = result.filter(ds => ds.type === this.typeFilter)
      }
      
      return result
    },
    
    // 分页信息
    totalItems() {
      return this.filteredDatasources.length
    },
    startItem() {
      return (this.currentPage - 1) * this.pageSize + 1
    },
    endItem() {
      const end = this.currentPage * this.pageSize
      return end > this.totalItems ? this.totalItems : end
    },
    totalPages() {
      return Math.ceil(this.totalItems / this.pageSize)
    },
    displayedPages() {
      // 显示当前页附近的页码
      const pages = []
      const maxPagesToShow = 5
      
      if (this.totalPages <= maxPagesToShow) {
        // 如果总页数少于要显示的页数，显示所有页
        for (let i = 1; i <= this.totalPages; i++) {
          pages.push(i)
        }
      } else {
        // 否则显示当前页附近的页
        let startPage = Math.max(1, this.currentPage - Math.floor(maxPagesToShow / 2))
        let endPage = startPage + maxPagesToShow - 1
        
        if (endPage > this.totalPages) {
          endPage = this.totalPages
          startPage = Math.max(1, endPage - maxPagesToShow + 1)
        }
        
        for (let i = startPage; i <= endPage; i++) {
          pages.push(i)
        }
      }
      
      return pages
    }
  },
  methods: {
    // 获取数据库类型图标
    getTypeIcon(type) {
      return type === 'MySQL' ? 'fas fa-database' : 'fas fa-server'
    },
    
    // 获取数据库类型颜色
    getTypeColorClass(type) {
      return type === 'MySQL' ? 'bg-blue-500' : 'bg-purple-500'
    },
    
    // 获取同步状态文本
    getSyncStatusText(status) {
      switch (status) {
        case 'synced': return '已同步'
        case 'syncing': return '同步中'
        case 'failed': return '同步失败'
        default: return '未同步'
      }
    },
    
    // 获取同步状态颜色
    getSyncStatusClass(status) {
      switch (status) {
        case 'synced': return 'text-green-600'
        case 'syncing': return 'text-blue-600'
        case 'failed': return 'text-red-600'
        default: return 'text-gray-600'
      }
    },
    
    // 分页方法
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++
      }
    },
    goToPage(page) {
      this.currentPage = page
    },
    
    // 编辑数据源
    editDatasource(datasource) {
      this.editingDatasource = datasource
      this.datasourceForm = { ...datasource, password: '' }
      this.showAddDataSourceModal = true
    },
    
    // 保存数据源
    saveDatasource() {
      if (this.editingDatasource) {
        // 更新现有数据源
        const index = this.datasources.findIndex(ds => ds.id === this.editingDatasource.id)
        if (index !== -1) {
          this.datasources[index] = { 
            ...this.datasources[index], 
            ...this.datasourceForm,
            // 保留原有状态
            status: this.datasources[index].status,
            syncStatus: this.datasources[index].syncStatus,
            lastSyncTime: this.datasources[index].lastSyncTime
          }
        }
      } else {
        // 添加新数据源
        const newId = Math.max(...this.datasources.map(ds => ds.id)) + 1
        this.datasources.push({
          id: newId,
          ...this.datasourceForm,
          status: 'online',
          syncStatus: 'not_synced',
          lastSyncTime: null
        })
      }
      
      // 关闭模态框并重置表单
      this.showAddDataSourceModal = false
      this.resetForm()
    },
    
    // 重置表单
    resetForm() {
      this.datasourceForm = {
        name: '',
        description: '',
        type: 'MySQL',
        host: '',
        port: '',
        database: '',
        username: '',
        password: ''
      }
      this.editingDatasource = null
    },
    
    // 确认删除
    confirmDelete(datasource) {
      this.datasourceToDelete = datasource
      this.showDeleteModal = true
    },
    
    // 删除数据源
    deleteDatasource() {
      if (this.datasourceToDelete) {
        const index = this.datasources.findIndex(ds => ds.id === this.datasourceToDelete.id)
        if (index !== -1) {
          this.datasources.splice(index, 1)
        }
      }
      this.showDeleteModal = false
      this.datasourceToDelete = null
    },
    
    // 同步数据源元数据
    syncDatasource(datasource) {
      // 模拟同步操作
      const index = this.datasources.findIndex(ds => ds.id === datasource.id)
      if (index !== -1) {
        this.datasources[index].syncStatus = 'syncing'
        
        // 模拟异步操作
        setTimeout(() => {
          this.datasources[index].syncStatus = 'synced'
          this.datasources[index].lastSyncTime = new Date().toLocaleString()
        }, 3000)
      }
    },
    
    // 测试连接
    testConnection(datasource) {
      // 模拟测试连接
      alert(`正在测试连接到 ${datasource.name}...`)
      setTimeout(() => {
        alert(`成功连接到 ${datasource.name}`)
      }, 1000)
    }
  },
  mounted() {
    console.log('Datasource component mounted')
  }
}