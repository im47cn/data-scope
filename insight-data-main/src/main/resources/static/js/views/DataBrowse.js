// 数据浏览页面组件
const DataBrowse = {
  template: `
    <div class="flex h-full">
      <!-- 左侧数据源和表格导航 -->
      <div class="w-64 bg-white border-r border-gray-200 flex-shrink-0 flex flex-col">
        <!-- 数据源选择 -->
        <div class="p-4 border-b border-gray-200">
          <label class="block text-sm font-medium text-gray-700 mb-1">数据源</label>
          <select 
            v-model="selectedDataSourceId" 
            @change="handleDataSourceChange"
            class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
          >
            <option v-for="ds in dataSources" :key="ds.id" :value="ds.id">{{ ds.name }}</option>
          </select>
        </div>
        
        <!-- 加载状态 -->
        <div v-if="loading.schemas" class="p-4 text-center">
          <i class="fas fa-spinner fa-spin text-primary"></i>
          <p class="text-sm text-gray-500">加载模式...</p>
        </div>
        
        <!-- 模式列表 -->
        <div v-else class="p-4 border-b border-gray-200">
          <div class="flex justify-between items-center mb-2">
            <h4 class="font-medium text-gray-700">模式</h4>
            <input v-model="schemaSearch" type="text" placeholder="搜索..." class="text-sm border border-gray-300 rounded-md px-2 py-1 w-24 focus:outline-none focus:ring-1 focus:ring-primary focus:border-primary">
          </div>
          <div class="overflow-y-auto max-h-32">
            <div v-for="schema in filteredSchemas" :key="schema.id" 
                 class="schema-item p-2 rounded mb-1"
                 :class="{'bg-indigo-50 text-indigo-700': schema.id === selectedSchemaId}"
                 @click="selectSchema(schema.id)">
              <div class="flex items-center">
                <i class="fas fa-folder" :class="schema.id === selectedSchemaId ? 'text-indigo-600' : 'text-gray-500'" class="mr-2"></i>
                <span>{{ schema.name }}</span>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 加载状态 -->
        <div v-if="loading.tables" class="p-4 text-center">
          <i class="fas fa-spinner fa-spin text-primary"></i>
          <p class="text-sm text-gray-500">加载表格...</p>
        </div>
        
        <!-- 表格列表 -->
        <div v-else class="p-4 flex-1 overflow-hidden flex flex-col">
          <div class="flex justify-between items-center mb-2">
            <h4 class="font-medium text-gray-700">表</h4>
            <input v-model="tableSearch" type="text" placeholder="搜索..." class="text-sm border border-gray-300 rounded-md px-2 py-1 w-24 focus:outline-none focus:ring-1 focus:ring-primary focus:border-primary">
          </div>
          <div class="overflow-y-auto flex-1">
            <div v-for="table in filteredTables" :key="table.id" 
                 class="table-item p-2 rounded mb-1"
                 :class="{'bg-indigo-50 text-indigo-700': table.id === selectedTableId}"
                 @click="selectTable(table.id, table.name)">
              <div class="flex items-center">
                <i :class="['mr-2', table.type === 'TABLE' ? 'fas fa-table' : 'fas fa-eye', table.id === selectedTableId ? 'text-indigo-600' : 'text-gray-500']"></i>
                <span>{{ table.name }}</span>
              </div>
              <div class="text-xs text-gray-500 ml-6">{{ table.description || table.type }} ({{ table.estimatedRowCount || '?' }} 行)</div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 右侧数据内容区域 -->
      <div class="flex-1 flex flex-col overflow-hidden">
        <!-- 工具栏 -->
        <div class="bg-white p-4 border-b border-gray-200">
          <div class="flex justify-between items-center">
            <div class="flex items-center">
              <h2 class="text-lg font-medium text-gray-800">
                {{ selectedTableName || '请选择一个表' }}
              </h2>
              <span v-if="tableData && tableData.rowCount" class="ml-2 text-sm text-gray-500">
                ({{ tableData.rowCount }} 行)
              </span>
            </div>
            <div class="flex space-x-2">
              <div class="relative">
                <input 
                  v-model="searchQuery" 
                  type="text" 
                  placeholder="搜索表数据..." 
                  class="border border-gray-300 rounded-md px-3 py-2 pl-10 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                >
                <i class="fas fa-search absolute left-3 top-3 text-gray-400"></i>
              </div>
              <button @click="refreshData" class="bg-white border border-gray-300 text-gray-700 py-2 px-4 rounded-lg flex items-center hover:bg-gray-50 transition-all">
                <i class="fas fa-sync-alt mr-2"></i>
                <span>刷新</span>
              </button>
              <button @click="exportData" class="bg-white border border-gray-300 text-gray-700 py-2 px-4 rounded-lg flex items-center hover:bg-gray-50 transition-all">
                <i class="fas fa-download mr-2"></i>
                <span>导出</span>
              </button>
            </div>
          </div>
        </div>
        
        <!-- 表格数据 -->
        <div class="flex-1 overflow-auto bg-gray-50 p-4">
          <!-- 未选择表格 -->
          <div v-if="!selectedTableId" class="h-full flex items-center justify-center text-gray-500">
            <div class="text-center">
              <i class="fas fa-table text-5xl mb-4"></i>
              <p>请从左侧选择一个表格查看数据</p>
            </div>
          </div>
          
          <!-- 加载状态 -->
          <div v-else-if="loading.tableData" class="h-full flex items-center justify-center">
            <div class="text-center">
              <i class="fas fa-spinner fa-spin text-primary text-5xl mb-4"></i>
              <p class="text-gray-500">加载表格数据...</p>
            </div>
          </div>
          
          <!-- 错误状态 -->
          <div v-else-if="error" class="h-full flex items-center justify-center">
            <div class="text-center text-red-500">
              <i class="fas fa-exclamation-circle text-5xl mb-4"></i>
              <p>{{ error }}</p>
              <button @click="refreshData" class="mt-4 text-indigo-600 hover:text-indigo-800">
                <i class="fas fa-redo mr-1"></i> 重试
              </button>
            </div>
          </div>
          
          <!-- 表格内容 -->
          <div v-else-if="tableData && tableData.columns" class="bg-white rounded-lg shadow overflow-hidden">
            <!-- 表格 -->
            <div class="overflow-x-auto">
              <table class="min-w-full divide-y divide-gray-200">
                <thead>
                  <tr>
                    <th 
                      v-for="column in tableData.columns" 
                      :key="column.name" 
                      class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider cursor-pointer"
                      @click="sortBy(column.name)"
                    >
                      <div class="flex items-center">
                        {{ column.label || column.name }}
                        <span v-if="sortColumn === column.name" class="ml-1">
                          <i :class="sortDirection === 'asc' ? 'fas fa-sort-up' : 'fas fa-sort-down'"></i>
                        </span>
                        <span v-else class="ml-1 text-gray-300">
                          <i class="fas fa-sort"></i>
                        </span>
                      </div>
                      <div class="text-xs font-normal text-gray-400 mt-1">{{ column.type }}</div>
                    </th>
                  </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                  <tr v-for="(row, rowIndex) in paginatedData" :key="rowIndex" class="hover:bg-gray-50">
                    <td v-for="column in tableData.columns" :key="column.name" class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {{ formatCellValue(row[column.name], column.type) }}
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
            
            <!-- 分页 -->
            <div class="px-6 py-4 border-t border-gray-200 flex items-center justify-between">
              <div class="text-sm text-gray-700">
                显示 {{ (currentPage - 1) * pageSize + 1 }} 到 {{ Math.min(currentPage * pageSize, filteredData.length) }} 条，共 {{ filteredData.length }} 条
              </div>
              <div class="flex space-x-2">
                <button 
                  @click="currentPage--" 
                  :disabled="currentPage === 1"
                  class="border border-gray-300 rounded-md px-3 py-1 text-sm"
                  :class="currentPage === 1 ? 'text-gray-400 cursor-not-allowed' : 'text-gray-700 hover:bg-gray-50'"
                >
                  上一页
                </button>
                <button 
                  v-for="page in displayedPages" 
                  :key="page" 
                  @click="currentPage = page"
                  class="border rounded-md px-3 py-1 text-sm"
                  :class="currentPage === page ? 'border-indigo-500 bg-indigo-500 text-white' : 'border-gray-300 text-gray-700 hover:bg-gray-50'"
                >
                  {{ page }}
                </button>
                <button 
                  @click="currentPage++" 
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
        
        <!-- 无数据提示 -->
        <div v-if="!currentTable" class="flex-1 flex items-center justify-center bg-white">
          <div class="text-center">
            <i class="fas fa-table text-gray-300 text-5xl mb-4"></i>
            <p class="text-gray-500">请选择一个表格查看数据</p>
          </div>
        </div>
      </div>
    </div>
  `,
  data() {
    return {
      // 数据源
      dataSources: [],
      selectedDataSourceId: null,
      
      // 模式
      schemas: [],
      selectedSchemaId: null,
      schemaSearch: '',
      
      // 表格
      tables: [],
      selectedTableId: null,
      selectedTableName: '',
      tableSearch: '',
      
      // 表格数据
      tableData: null,
      
      // 搜索和排序
      searchQuery: '',
      sortColumn: '',
      sortDirection: 'asc',
      
      // 分页
      currentPage: 1,
      pageSize: 10,
      
      // 加载状态
      loading: {
        dataSources: false,
        schemas: false,
        tables: false,
        tableData: false
      },

      // 表格数据（模拟）
      mockData: [
        { customer_id: 1001, customer_name: '张三企业', contact_name: '张三', phone: '13800138001', email: 'zhangsan@example.com', address: '北京市朝阳区建国路88号', region_id: 1, created_at: '2023-01-15 10:30:00' },
        { customer_id: 1002, customer_name: '李四有限公司', contact_name: '李四', phone: '13900139002', email: 'lisi@example.com', address: '上海市浦东新区陆家嘴1号', region_id: 2, created_at: '2023-01-20 14:15:00' },
        { customer_id: 1003, customer_name: '王五科技', contact_name: '王五', phone: '13700137003', email: 'wangwu@example.com', address: '深圳市南山区科技园路10号', region_id: 3, created_at: '2023-02-05 09:45:00' },
        { customer_id: 1004, customer_name: '赵六贸易', contact_name: '赵六', phone: '13600136004', email: 'zhaoliu@example.com', address: '广州市天河区体育西路5号', region_id: 4, created_at: '2023-02-10 16:20:00' },
        { customer_id: 1005, customer_name: '钱七集团', contact_name: '钱七', phone: '13500135005', email: 'qianqi@example.com', address: '成都市锦江区红星路3段', region_id: 5, created_at: '2023-03-01 11:10:00' }
      ],

      // 错误状态
      error: null
    }
  },
  computed: {
    // 过滤后的模式列表
    filteredSchemas() {
      if (!this.schemaSearch) return this.schemas
      const search = this.schemaSearch.toLowerCase()
      return this.schemas.filter(schema => schema.name.toLowerCase().includes(search))
    },
    
    // 过滤后的表格列表
    filteredTables() {
      let tables = this.tables
      if (this.tableSearch) {
        const search = this.tableSearch.toLowerCase()
        tables = tables.filter(table => 
          table.name.toLowerCase().includes(search) || 
          (table.description && table.description.toLowerCase().includes(search))
        )
      }
      return tables
    },
    
    // 过滤和排序后的数据
    filteredData() {
      if (!this.tableData || !this.tableData.rows) return []
      
      let data = [...this.tableData.rows]
      
      // 应用搜索
      if (this.searchQuery) {
        const search = this.searchQuery.toLowerCase()
        data = data.filter(row => {
          return Object.values(row).some(value => 
            value !== null && String(value).toLowerCase().includes(search)
          )
        })
      }
      
      // 应用排序
      if (this.sortColumn) {
        const columnIndex = this.tableData.columns.findIndex(col => col.name === this.sortColumn)
        if (columnIndex !== -1) {
          const columnType = this.tableData.columns[columnIndex].type
          
          data.sort((a, b) => {
            let valA = a[this.sortColumn]
            let valB = b[this.sortColumn]
            
            // 处理null值
            if (valA === null && valB === null) return 0
            if (valA === null) return this.sortDirection === 'asc' ? -1 : 1
            if (valB === null) return this.sortDirection === 'asc' ? 1 : -1
            
            // 根据列类型进行排序
            if (columnType.includes('INT') || columnType.includes('DECIMAL') || columnType.includes('FLOAT') || columnType.includes('DOUBLE')) {
              // 数值类型
              valA = Number(valA)
              valB = Number(valB)
            } else if (columnType.includes('DATE') || columnType.includes('TIME')) {
              // 日期类型
              valA = new Date(valA)
              valB = new Date(valB)
            }
            
            if (valA < valB) return this.sortDirection === 'asc' ? -1 : 1
            if (valA > valB) return this.sortDirection === 'asc' ? 1 : -1
            return 0
          })
        }
      }
      
      return data
    },
    
    // 分页后的数据
    paginatedData() {
      const start = (this.currentPage - 1) * this.pageSize
      const end = start + this.pageSize
      return this.filteredData.slice(start, end)
    },
    
    // 总页数
    totalPages() {
      return Math.ceil(this.filteredData.length / this.pageSize)
    },
    
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
      this.loading.dataSources = true
      this.error = null
      
      try {
        // 使用API服务获取数据源列表
        const response = await DataSourceService.getAllDataSources()
        this.dataSources = response.content || response
        
        // 如果有数据源，默认选择第一个
        if (this.dataSources.length > 0 && !this.selectedDataSourceId) {
          this.selectedDataSourceId = this.dataSources[0].id
          await this.handleDataSourceChange()
        }
      } catch (error) {
        console.error('加载数据源失败:', error)
        this.error = error.message || '加载数据源失败，请稍后重试'
      } finally {
        this.loading.dataSources = false
      }
    },
    
    // 处理数据源变更
    async handleDataSourceChange() {
      // 重置选择的模式和表格
      this.selectedSchemaId = null
      this.selectedTableId = null
      this.selectedTableName = ''
      this.tableData = null
      
      // 加载模式列表
      await this.loadSchemas()
    },
    
    // 加载模式列表
    async loadSchemas() {
      if (!this.selectedDataSourceId) return
      
      this.loading.schemas = true
      this.error = null
      
      try {
        // 使用API服务获取模式列表
        const response = await MetadataService.getSchemas(this.selectedDataSourceId)
        this.schemas = response.content || response
        
        // 如果有模式，默认选择第一个
        if (this.schemas.length > 0 && !this.selectedSchemaId) {
          this.selectedSchemaId = this.schemas[0].id
          this.selectSchema(this.selectedSchemaId)
        }
      } catch (error) {
        console.error('加载模式失败:', error)
        this.error = error.message || '加载模式失败，请稍后重试'
      } finally {
        this.loading.schemas = false
      }
    },
    
    // 选择模式
    async selectSchema(schemaId) {
      this.selectedSchemaId = schemaId
      this.selectedTableId = null
      this.selectedTableName = ''
      this.tableData = null
      
      // 加载表格列表
      await this.loadTables()
    },
    
    // 加载表格列表
    async loadTables() {
      if (!this.selectedDataSourceId || !this.selectedSchemaId) return
      
      this.loading.tables = true
      this.error = null
      
      try {
        // 获取选中的模式名称
        const selectedSchema = this.schemas.find(schema => schema.id === this.selectedSchemaId)
        if (!selectedSchema) throw new Error('未找到选中的模式')
        
        // 使用API服务获取表格列表
        const response = await MetadataService.getTables(this.selectedDataSourceId, selectedSchema.name)
        this.tables = response.content || response
      } catch (error) {
        console.error('加载表格失败:', error)
        this.error = error.message || '加载表格失败，请稍后重试'
      } finally {
        this.loading.tables = false
      }
    },
    
    // 选择表格
    async selectTable(tableId, tableName) {
      this.selectedTableId = tableId
      this.selectedTableName = tableName
      this.currentPage = 1
      this.searchQuery = ''
      this.sortColumn = ''
      this.sortDirection = 'asc'
      
      // 加载表格数据
      await this.loadTableData()
    },
    
    // 加载表格数据
    async loadTableData() {
      if (!this.selectedDataSourceId || !this.selectedSchemaId || !this.selectedTableId) return
      
      this.loading.tableData = true
      this.error = null
      
      try {
        // 获取选中的模式和表格
        const selectedSchema = this.schemas.find(schema => schema.id === this.selectedSchemaId)
        const selectedTable = this.tables.find(table => table.id === this.selectedTableId)
        
        if (!selectedSchema || !selectedTable) throw new Error('未找到选中的模式或表格')
        
        // 构建SQL查询
        const sql = `SELECT * FROM ${selectedSchema.name}.${selectedTable.name} LIMIT 1000`
        
        // 使用API服务执行查询
        const result = await QueryService.executeQuery({
          dataSourceId: this.selectedDataSourceId,
          sql,
          options: {
            timeout: 30,
            maxRows: 1000
          }
        })
        
        this.tableData = result
      } catch (error) {
        console.error('加载表格数据失败:', error)
        this.error = error.message || '加载表格数据失败，请稍后重试'
      } finally {
        this.loading.tableData = false
      }
    },
    
    // 排序
    sortBy(column) {
      if (this.sortColumn === column) {
        // 切换排序方向
        this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc'
      } else {
        // 设置新的排序列和默认排序方向
        this.sortColumn = column
        this.sortDirection = 'asc'
      }
    },
    
    // 刷新数据
    async refreshData() {
      if (this.selectedTableId) {
        await this.loadTableData()
      } else if (this.selectedSchemaId) {
        await this.loadTables()
      } else if (this.selectedDataSourceId) {
        await this.loadSchemas()
      } else {
        await this.loadDataSources()
      }
    },
    
    // 导出数据
    exportData() {
      if (!this.tableData || !this.tableData.columns || !this.tableData.rows) {
        alert('没有数据可导出')
        return
      }
      
      try {
        // 构建CSV内容
        const headers = this.tableData.columns.map(col => col.name).join(',')
        const rows = this.filteredData.map(row => {
          return this.tableData.columns.map(col => {
            const value = row[col.name]
            // 处理包含逗号、引号或换行符的值
            if (value === null || value === undefined) return ''
            const strValue = String(value)
            if (strValue.includes(',') || strValue.includes('"') || strValue.includes('\n')) {
              return `"${strValue.replace(/"/g, '""')}"`
            }
            return strValue
          }).join(',')
        }).join('\n')
        
        const csv = `${headers}\n${rows}`
        
        // 创建下载链接
        const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' })
        const url = URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.setAttribute('download', `${this.selectedTableName || 'data'}.csv`)
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
      } catch (error) {
        console.error('导出数据失败:', error)
        alert('导出数据失败: ' + (error.message || '未知错误'))
      }
    },
    
    // 格式化单元格值
    formatCellValue(value, type) {
      if (value === null || value === undefined) return ''
      
      // 根据数据类型格式化值
      if (type && type.includes('DATE') && !type.includes('DATETIME')) {
        // 日期类型
        return new Date(value).toLocaleDateString()
      } else if (type && type.includes('DATETIME') || type.includes('TIMESTAMP')) {
        // 日期时间类型
        return new Date(value).toLocaleString()
      } else if (type && (type.includes('DECIMAL') || type.includes('FLOAT') || type.includes('DOUBLE'))) {
        // 小数类型
        return Number(value).toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 6 })
      }
      
      return value
    }
  },
  mounted() {
    // 加载数据源列表
    this.loadDataSources()
  }
}