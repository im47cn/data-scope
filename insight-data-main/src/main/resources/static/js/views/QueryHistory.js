// 查询历史页面组件
const QueryHistory = {
  template: `
    <div class="p-6">
      <div class="mb-6">
        <h2 class="text-2xl font-bold text-gray-800">查询历史</h2>
        <p class="text-gray-600">查看和管理历史查询记录</p>
      </div>
      
      <!-- 过滤和搜索 -->
      <div class="bg-white rounded-lg shadow p-4 mb-6">
        <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
          <!-- 数据源过滤 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">数据源</label>
            <select 
              v-model="filters.dataSourceId" 
              class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
            >
              <option value="">全部数据源</option>
              <option v-for="ds in dataSources" :key="ds.id" :value="ds.id">{{ ds.name }}</option>
            </select>
          </div>
          
          <!-- 日期范围 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">开始日期</label>
            <input 
              v-model="filters.startDate" 
              type="date" 
              class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
            >
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">结束日期</label>
            <input 
              v-model="filters.endDate" 
              type="date" 
              class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
            >
          </div>
          
          <!-- 搜索 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">搜索</label>
            <div class="relative">
              <input 
                v-model="filters.search" 
                type="text" 
                placeholder="搜索SQL或结果..." 
                class="w-full border border-gray-300 rounded-md px-3 py-2 pl-10 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
              >
              <i class="fas fa-search absolute left-3 top-3 text-gray-400"></i>
            </div>
          </div>
        </div>
        
        <!-- 过滤按钮 -->
        <div class="flex justify-end mt-4">
          <button @click="resetFilters" class="border border-gray-300 text-gray-700 py-2 px-4 rounded-lg flex items-center hover:bg-gray-50 transition-all mr-2">
            <i class="fas fa-undo mr-2"></i>
            <span>重置</span>
          </button>
          <button @click="applyFilters" class="bg-primary hover:bg-indigo-700 text-white py-2 px-4 rounded-lg flex items-center transition-all">
            <i class="fas fa-filter mr-2"></i>
            <span>应用过滤</span>
          </button>
        </div>
      </div>
      
      <!-- 查询历史列表 -->
      <div class="bg-white rounded-lg shadow">
        <!-- 加载状态 -->
        <div v-if="loading" class="p-6 flex items-center justify-center">
          <div class="text-center">
            <i class="fas fa-spinner fa-spin text-primary text-3xl mb-4"></i>
            <p class="text-gray-500">加载查询历史...</p>
          </div>
        </div>
        
        <!-- 错误状态 -->
        <div v-else-if="error" class="p-6">
          <div class="bg-red-50 text-red-700 p-4 rounded-lg">
            <div class="flex items-center">
              <i class="fas fa-exclamation-circle text-xl mr-2"></i>
              <div>
                <h3 class="font-medium">加载失败</h3>
                <p>{{ error }}</p>
              </div>
            </div>
            <button @click="loadQueryHistory" class="mt-2 text-red-700 hover:text-red-900">
              <i class="fas fa-redo mr-1"></i> 重试
            </button>
          </div>
        </div>
        
        <!-- 空状态 -->
        <div v-else-if="queryHistory.length === 0" class="p-6 flex items-center justify-center">
          <div class="text-center text-gray-500">
            <i class="fas fa-history text-gray-300 text-5xl mb-4"></i>
            <p>暂无查询历史记录</p>
          </div>
        </div>
        
        <!-- 历史列表 -->
        <div v-else>
          <div class="overflow-x-auto">
            <table class="min-w-full divide-y divide-gray-200">
              <thead>
                <tr>
                  <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    查询内容
                  </th>
                  <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    数据源
                  </th>
                  <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    执行时间
                  </th>
                  <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    状态
                  </th>
                  <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    操作
                  </th>
                </tr>
              </thead>
              <tbody class="bg-white divide-y divide-gray-200">
                <tr v-for="(query, index) in queryHistory" :key="index" class="hover:bg-gray-50">
                  <td class="px-6 py-4">
                    <div class="max-w-lg">
                      <div class="text-sm font-medium text-gray-900 mb-1 truncate">
                        {{ query.sql.length > 100 ? query.sql.substring(0, 100) + '...' : query.sql }}
                      </div>
                      <div v-if="query.naturalLanguage" class="text-xs text-gray-500 italic">
                        {{ query.naturalLanguage }}
                      </div>
                    </div>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {{ getDataSourceName(query.dataSourceId) }}
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <div class="text-sm text-gray-900">{{ query.executedAt }}</div>
                    <div class="text-xs text-gray-500">{{ query.executionTime }}ms</div>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <span class="px-2 py-1 text-xs rounded-full" :class="getStatusClass(query.status)">
                      {{ getStatusText(query.status) }}
                    </span>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                    <button @click="viewQueryDetails(query)" class="text-indigo-600 hover:text-indigo-900 mr-2">
                      <i class="fas fa-eye" title="查看详情"></i>
                    </button>
                    <button @click="rerunQuery(query)" class="text-indigo-600 hover:text-indigo-900 mr-2">
                      <i class="fas fa-redo" title="重新执行"></i>
                    </button>
                    <button @click="saveQuery(query)" class="text-indigo-600 hover:text-indigo-900">
                      <i class="fas fa-save" title="保存查询"></i>
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          
          <!-- 分页 -->
          <div class="px-6 py-4 border-t border-gray-200 flex items-center justify-between">
            <div class="text-sm text-gray-700">
              显示 {{ (currentPage - 1) * pageSize + 1 }} 到 {{ Math.min(currentPage * pageSize, totalItems) }} 条，共 {{ totalItems }} 条
            </div>
            <div class="flex space-x-2">
              <button 
                @click="changePage(currentPage - 1)" 
                :disabled="currentPage === 1"
                class="border border-gray-300 rounded-md px-3 py-1 text-sm"
                :class="currentPage === 1 ? 'text-gray-400 cursor-not-allowed' : 'text-gray-700 hover:bg-gray-50'"
              >
                上一页
              </button>
              <button 
                v-for="page in displayedPages" 
                :key="page" 
                @click="changePage(page)"
                class="border rounded-md px-3 py-1 text-sm"
                :class="currentPage === page ? 'border-indigo-500 bg-indigo-500 text-white' : 'border-gray-300 text-gray-700 hover:bg-gray-50'"
              >
                {{ page }}
              </button>
              <button 
                @click="changePage(currentPage + 1)" 
                :disabled="currentPage === totalPages"
                class="border border-gray-300 rounded-md px-3 py-1 text-sm"
                :class="currentPage === totalPages ? 'text-gray-400 cursor-not-allowed' : 'text-gray-700 hover:bg-gray-50'"
              >
                下一页
              </button>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 查询详情模态框 -->
      <div v-if="selectedQuery" class="fixed inset-0 overflow-y-auto z-50">
        <div class="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
          <div class="fixed inset-0 transition-opacity" aria-hidden="true" @click="selectedQuery = null">
            <div class="absolute inset-0 bg-gray-500 opacity-75"></div>
          </div>
          <span class="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>
          <div class="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-4xl sm:w-full">
            <div class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
              <div class="sm:flex sm:items-start">
                <div class="mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left w-full">
                  <div class="flex justify-between items-center mb-4">
                    <h3 class="text-lg leading-6 font-medium text-gray-900">
                      查询详情
                    </h3>
                    <span class="px-2 py-1 text-xs rounded-full" :class="getStatusClass(selectedQuery.status)">
                      {{ getStatusText(selectedQuery.status) }}
                    </span>
                  </div>
                  
                  <div class="mt-4 space-y-6">
                    <!-- 基本信息 -->
                    <div>
                      <h4 class="text-sm font-medium text-gray-700 mb-2">基本信息</h4>
                      <div class="bg-gray-50 p-4 rounded-lg">
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                          <div>
                            <div class="text-xs text-gray-500">数据源</div>
                            <div class="text-sm text-gray-900">{{ getDataSourceName(selectedQuery.dataSourceId) }}</div>
                          </div>
                          <div>
                            <div class="text-xs text-gray-500">执行时间</div>
                            <div class="text-sm text-gray-900">{{ selectedQuery.executedAt }}</div>
                          </div>
                          <div>
                            <div class="text-xs text-gray-500">执行耗时</div>
                            <div class="text-sm text-gray-900">{{ selectedQuery.executionTime }}ms</div>
                          </div>
                          <div>
                            <div class="text-xs text-gray-500">结果行数</div>
                            <div class="text-sm text-gray-900">{{ selectedQuery.resultRows || 0 }}</div>
                          </div>
                        </div>
                      </div>
                    </div>
                    
                    <!-- SQL查询 -->
                    <div>
                      <div class="flex justify-between items-center mb-2">
                        <h4 class="text-sm font-medium text-gray-700">SQL查询</h4>
                        <button @click="copySQLFromDetails" class="text-indigo-600 hover:text-indigo-800">
                          <i class="fas fa-copy"></i>
                        </button>
                      </div>
                      <div class="bg-gray-800 text-white p-4 rounded-lg font-mono text-sm overflow-x-auto">
                        <pre>{{ selectedQuery.sql }}</pre>
                      </div>
                    </div>
                    
                    <!-- 自然语言查询 -->
                    <div v-if="selectedQuery.naturalLanguage">
                      <h4 class="text-sm font-medium text-gray-700 mb-2">自然语言查询</h4>
                      <div class="bg-gray-50 p-4 rounded-lg text-gray-700">
                        {{ selectedQuery.naturalLanguage }}
                      </div>
                    </div>
                    
                    <!-- 查询结果 -->
                    <div v-if="queryResults">
                      <h4 class="text-sm font-medium text-gray-700 mb-2">查询结果</h4>
                      <div class="overflow-x-auto">
                        <table class="min-w-full divide-y divide-gray-200">
                          <thead>
                            <tr>
                              <th 
                                v-for="column in queryResults.columns" 
                                :key="column.name" 
                                class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                              >
                                {{ column.label || column.name }}
                              </th>
                            </tr>
                          </thead>
                          <tbody class="bg-white divide-y divide-gray-200">
                            <tr v-for="(row, rowIndex) in queryResults.rows" :key="rowIndex" class="hover:bg-gray-50">
                              <td v-for="column in queryResults.columns" :key="column.name" class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                {{ row[column.name] }}
                              </td>
                            </tr>
                          </tbody>
                        </table>
                      </div>
                      
                      <!-- 无数据 -->
                      <div v-if="queryResults.rows && queryResults.rows.length === 0" class="text-center py-8 text-gray-500">
                        <p>查询未返回任何数据</p>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
              <button @click="rerunQuery(selectedQuery)" type="button" class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-primary text-base font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary sm:ml-3 sm:w-auto sm:text-sm">
                重新执行
              </button>
              <button @click="saveQuery(selectedQuery)" type="button" class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm">
                保存查询
              </button>
              <button @click="selectedQuery = null" type="button" class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm">
                关闭
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  data() {
    return {
      // 数据源
      dataSources: [],
      
      // 查询历史
      queryHistory: [],
      selectedQuery: null,
      queryResults: null,
      
      // 过滤
      filters: {
        dataSourceId: '',
        startDate: '',
        endDate: '',
        search: ''
      },
      
      // 分页
      currentPage: 1,
      pageSize: 10,
      totalItems: 0,
      totalPages: 1,
      
      // 状态
      loading: false,
      error: null
    }
  },
  computed: {
    // 显示的页码
    displayedPages() {
      const pages = []
      const maxPages = 5 // 最多显示5个页码
      
      if (this.totalPages <= maxPages) {
        // 总页数小于等于最大显示页数，显示所有页码
        for (let i = 1; i <= this.totalPages; i++) {
          pages.push(i)
        }
      } else {
        // 总页数大于最大显示页数，显示部分页码
        let startPage = Math.max(1, this.currentPage - Math.floor(maxPages / 2))
        let endPage = startPage + maxPages - 1
        
        if (endPage > this.totalPages) {
          endPage = this.totalPages
          startPage = Math.max(1, endPage - maxPages + 1)
        }
        
        for (let i = startPage; i <= endPage; i++) {
          pages.push(i)
        }
      }
      
      return pages
    }
  },
  methods: {
    // 加载数据源列表
    async loadDataSources() {
      try {
        // 使用API服务获取数据源列表
        const response = await DataSourceService.getAllDataSources()
        this.dataSources = response.content || response
      } catch (error) {
        console.error('加载数据源失败:', error)
        this.error = error.message || '加载数据源失败，请稍后重试'
      }
    },
    
    // 加载查询历史
    async loadQueryHistory() {
      this.loading = true
      this.error = null
      
      try {
        // 构建查询参数
        const params = {
          page: this.currentPage - 1, // API从0开始计数
          size: this.pageSize
        }
        
        // 添加过滤条件
        if (this.filters.dataSourceId) {
          params.dataSourceId = this.filters.dataSourceId
        }
        
        if (this.filters.startDate) {
          params.startDate = this.filters.startDate
        }
        
        if (this.filters.endDate) {
          params.endDate = this.filters.endDate
        }
        
        if (this.filters.search) {
          params.search = this.filters.search
        }
        
        // 使用API服务获取查询历史
        const response = await QueryService.getQueryHistory(params)
        
        // 更新数据
        this.queryHistory = response.content || []
        this.totalItems = response.totalElements || this.queryHistory.length
        this.totalPages = response.totalPages || Math.ceil(this.totalItems / this.pageSize)
      } catch (error) {
        console.error('加载查询历史失败:', error)
        this.error = error.message || '加载查询历史失败，请稍后重试'
      } finally {
        this.loading = false
      }
    },
    
    // 应用过滤
    applyFilters() {
      this.currentPage = 1 // 重置到第一页
      this.loadQueryHistory()
    },
    
    // 重置过滤
    resetFilters() {
      this.filters = {
        dataSourceId: '',
        startDate: '',
        endDate: '',
        search: ''
      }
      this.currentPage = 1
      this.loadQueryHistory()
    },
    
    // 切换页码
    changePage(page) {
      if (page < 1 || page > this.totalPages) return
      this.currentPage = page
      this.loadQueryHistory()
    },
    
    // 获取数据源名称
    getDataSourceName(dataSourceId) {
      const dataSource = this.dataSources.find(ds => ds.id === dataSourceId)
      return dataSource ? dataSource.name : `数据源 #${dataSourceId}`
    },
    
    // 获取状态样式类
    getStatusClass(status) {
      switch (status) {
        case 'COMPLETED': return 'bg-green-100 text-green-800'
        case 'FAILED': return 'bg-red-100 text-red-800'
        case 'RUNNING': return 'bg-blue-100 text-blue-800'
        default: return 'bg-gray-100 text-gray-800'
      }
    },
    
    // 获取状态文本
    getStatusText(status) {
      switch (status) {
        case 'COMPLETED': return '成功'
        case 'FAILED': return '失败'
        case 'RUNNING': return '执行中'
        default: return status
      }
    },
    
    // 查看查询详情
    async viewQueryDetails(query) {
      this.selectedQuery = query
      this.queryResults = null
      
      // 如果查询成功，尝试获取结果
      if (query.status === 'COMPLETED') {
        try {
          // 使用API服务获取查询结果
          const result = await QueryService.getQuery(query.id)
          this.queryResults = result.results
        } catch (error) {
          console.error('获取查询结果失败:', error)
          // 不显示错误，只是不显示结果
        }
      }
    },
    
    // 重新执行查询
    async rerunQuery(query) {
      try {
        // 使用API服务执行查询
        const result = await QueryService.executeQuery({
          dataSourceId: query.dataSourceId,
          sql: query.sql
        })
        
        // 更新结果
        this.queryResults = result
        
        // 如果模态框打开，更新选中的查询
        if (this.selectedQuery) {
          this.selectedQuery = {
            ...query,
            status: 'COMPLETED',
            executionTime: result.executionTime,
            resultRows: result.rowCount
          }
        }
        
        // 刷新查询历史
        this.loadQueryHistory()
      } catch (error) {
        console.error('执行查询失败:', error)
        alert('执行查询失败: ' + (error.message || '未知错误'))
      }
    },
    
    // 保存查询
    async saveQuery(query) {
      const queryName = prompt('请输入查询名称:', query.sql.substring(0, 30) + '...')
      if (!queryName) return
      
      try {
        // 使用API服务保存查询
        await QueryService.saveQuery({
          name: queryName,
          description: query.naturalLanguage || '从历史记录保存的查询',
          dataSourceId: query.dataSourceId,
          sql: query.sql,
          parameters: [],
          shared: false
        })
        
        alert('查询已保存')
      } catch (error) {
        console.error('保存查询失败:', error)
        alert('保存查询失败: ' + (error.message || '未知错误'))
      }
    },
    
    // 复制SQL
    copySQLFromDetails() {
      if (!this.selectedQuery || !this.selectedQuery.sql) return
      
      navigator.clipboard.writeText(this.selectedQuery.sql)
        .then(() => {
          alert('SQL已复制到剪贴板')
        })
        .catch(err => {
          console.error('复制失败:', err)
          alert('复制失败，请手动复制')
        })
    }
  },
  mounted() {
    // 加载数据源列表和查询历史
    this.loadDataSources()
    this.loadQueryHistory()
  }
}
