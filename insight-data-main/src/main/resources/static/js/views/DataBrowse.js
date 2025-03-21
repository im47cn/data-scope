// 数据浏览页面组件
const DataBrowse = {
  template: `
    <div class="flex h-full">
      <!-- 左侧数据源和表格导航 -->
      <div class="w-64 bg-white border-r border-gray-200 flex-shrink-0 flex flex-col">
        <!-- 数据源选择 -->
        <div class="p-4 border-b border-gray-200">
          <label class="block text-sm font-medium text-gray-700 mb-1">数据源</label>
          <select v-model="selectedDataSource" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary">
            <option v-for="ds in dataSources" :key="ds.id" :value="ds.id">{{ ds.name }}</option>
          </select>
        </div>
        
        <!-- 模式列表 -->
        <div class="p-4 border-b border-gray-200">
          <div class="flex justify-between items-center mb-2">
            <h4 class="font-medium text-gray-700">模式</h4>
            <input v-model="schemaSearch" type="text" placeholder="搜索..." class="text-sm border border-gray-300 rounded-md px-2 py-1 w-24 focus:outline-none focus:ring-1 focus:ring-primary focus:border-primary">
          </div>
          <div class="overflow-y-auto max-h-32">
            <div v-for="schema in filteredSchemas" :key="schema.id" 
                 class="schema-item p-2 rounded mb-1"
                 :class="{'selected-table': schema.id === selectedSchema}"
                 @click="selectSchema(schema.id)">
              <div class="flex items-center">
                <i class="fas fa-folder" :class="schema.id === selectedSchema ? 'text-primary' : 'text-gray-500'" class="mr-2"></i>
                <span>{{ schema.name }}</span>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 表格列表 -->
        <div class="p-4 flex-1 overflow-hidden flex flex-col">
          <div class="flex justify-between items-center mb-2">
            <h4 class="font-medium text-gray-700">表</h4>
            <input v-model="tableSearch" type="text" placeholder="搜索..." class="text-sm border border-gray-300 rounded-md px-2 py-1 w-24 focus:outline-none focus:ring-1 focus:ring-primary focus:border-primary">
          </div>
          <div class="overflow-y-auto flex-1">
            <div v-for="table in filteredTables" :key="table.id" 
                 class="table-item p-2 rounded mb-1"
                 :class="{'selected-table': table.id === selectedTable}"
                 @click="selectTable(table.id)">
              <div class="flex items-center">
                <i class="fas fa-table" :class="table.id === selectedTable ? 'text-primary' : 'text-gray-500'" class="mr-2"></i>
                <span>{{ table.name }}</span>
              </div>
              <div class="text-xs text-gray-500 ml-6">{{ table.description }} ({{ table.rowCount }} 行)</div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 右侧内容区 -->
      <div class="flex-1 flex flex-col overflow-hidden">
        <!-- 表格信息和操作 -->
        <div v-if="currentTable" class="bg-white p-4 border-b border-gray-200">
          <div class="flex justify-between items-center">
            <div>
              <h3 class="text-lg font-semibold text-gray-800">{{ currentTable.name }} 表</h3>
              <p class="text-sm text-gray-600">{{ currentTable.description }}</p>
            </div>
            <div class="flex space-x-2">
              <button class="bg-white border border-gray-300 text-gray-700 py-2 px-4 rounded-lg flex items-center hover:bg-gray-50 transition-all">
                <i class="fas fa-filter mr-2"></i>
                <span>筛选</span>
              </button>
              <button class="bg-white border border-gray-300 text-gray-700 py-2 px-4 rounded-lg flex items-center hover:bg-gray-50 transition-all">
                <i class="fas fa-download mr-2"></i>
                <span>导出</span>
              </button>
              <button class="bg-primary hover:bg-indigo-700 text-white py-2 px-4 rounded-lg flex items-center transition-all">
                <i class="fas fa-search mr-2"></i>
                <span>查询</span>
              </button>
            </div>
          </div>
        </div>
        
        <!-- 表格结构 -->
        <div v-if="currentTable" class="bg-white p-4 border-b border-gray-200">
          <h4 class="font-medium text-gray-700 mb-3">表结构</h4>
          <div class="overflow-x-auto">
            <table class="min-w-full divide-y divide-gray-200">
              <thead>
                <tr>
                  <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">列名</th>
                  <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">数据类型</th>
                  <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">约束</th>
                  <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">描述</th>
                </tr>
              </thead>
              <tbody class="bg-white divide-y divide-gray-200">
                <tr v-for="column in currentTable.columns" :key="column.name">
                  <td class="px-6 py-4 whitespace-nowrap">
                    <div class="flex items-center">
                      <i :class="getColumnIcon(column)" class="mr-2"></i>
                      <span>{{ column.name }}</span>
                    </div>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap">{{ column.type }}</td>
                  <td class="px-6 py-4 whitespace-nowrap">{{ column.constraint }}</td>
                  <td class="px-6 py-4 whitespace-nowrap">{{ column.description }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        
        <!-- 表格数据 -->
        <div v-if="currentTable" class="flex-1 overflow-auto bg-white">
          <div class="p-4">
            <div class="flex justify-between items-center mb-3">
              <h4 class="font-medium text-gray-700">表数据</h4>
              <div class="flex items-center">
                <span class="text-sm text-gray-600 mr-2">每页显示:</span>
                <select v-model="pageSize" class="border border-gray-300 rounded-md px-2 py-1 text-sm focus:outline-none focus:ring-1 focus:ring-primary focus:border-primary">
                  <option>10</option>
                  <option>20</option>
                  <option>50</option>
                  <option>100</option>
                </select>
              </div>
            </div>
            <div class="overflow-x-auto">
              <table class="min-w-full divide-y divide-gray-200">
                <thead>
                  <tr>
                    <th v-for="column in currentTable.columns" :key="column.name" class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      {{ column.name }}
                    </th>
                  </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                  <tr v-for="(row, index) in tableData" :key="index">
                    <td v-for="column in currentTable.columns" :key="column.name" class="px-6 py-4 whitespace-nowrap">
                      {{ row[column.name] }}
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div class="mt-4 flex items-center justify-between">
              <div>
                <p class="text-sm text-gray-700">
                  显示第 <span class="font-medium">{{ startRow }}</span> 到 <span class="font-medium">{{ endRow }}</span> 条，共 <span class="font-medium">{{ totalRows }}</span> 条
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
      // 数据源列表
      dataSources: [
        { id: 'sales_db', name: '销售数据库' },
        { id: 'crm_db', name: 'CRM数据库' },
        { id: 'warehouse_db', name: '仓储数据库' },
        { id: 'hr_db', name: 'HR数据库' }
      ],
      selectedDataSource: 'sales_db',
      
      // 模式列表
      schemas: [
        { id: 'public', name: 'public' },
        { id: 'sales', name: 'sales' },
        { id: 'marketing', name: 'marketing' }
      ],
      selectedSchema: 'public',
      schemaSearch: '',
      
      // 表格列表
      tables: [
        { 
          id: 'customers', 
          name: 'customers', 
          description: '客户表', 
          rowCount: '45K',
          schema: 'public',
          columns: [
            { name: 'customer_id', type: 'INT', constraint: 'PRIMARY KEY', description: '客户唯一标识', isPrimary: true },
            { name: 'customer_name', type: 'VARCHAR(100)', constraint: 'NOT NULL', description: '客户名称', isText: true },
            { name: 'contact_name', type: 'VARCHAR(50)', constraint: '', description: '联系人姓名', isText: true },
            { name: 'phone', type: 'VARCHAR(20)', constraint: '', description: '联系电话', isText: true },
            { name: 'email', type: 'VARCHAR(100)', constraint: '', description: '电子邮箱', isText: true },
            { name: 'address', type: 'VARCHAR(200)', constraint: '', description: '地址', isText: true },
            { name: 'region_id', type: 'INT', constraint: 'FOREIGN KEY', description: '所属地区ID', isForeign: true },
            { name: 'created_at', type: 'DATETIME', constraint: 'NOT NULL', description: '创建时间', isDate: true }
          ]
        },
        { id: 'orders', name: 'orders', description: '订单表', rowCount: '1.2M', schema: 'public' },
        { id: 'products', name: 'products', description: '产品表', rowCount: '5K', schema: 'public' },
        { id: 'order_items', name: 'order_items', description: '订单项表', rowCount: '3.5M', schema: 'public' },
        { id: 'employees', name: 'employees', description: '员工表', rowCount: '200', schema: 'public' },
        { id: 'regions', name: 'regions', description: '地区表', rowCount: '25', schema: 'public' },
        { id: 'categories', name: 'categories', description: '类别表', rowCount: '12', schema: 'public' },
        { id: 'suppliers', name: 'suppliers', description: '供应商表', rowCount: '150', schema: 'public' }
      ],
      selectedTable: 'customers',
      tableSearch: '',
      
      // 分页
      currentPage: 1,
      pageSize: 50,
      totalRows: 45000,
      
      // 表格数据（模拟）
      mockData: [
        { customer_id: 1001, customer_name: '张三企业', contact_name: '张三', phone: '13800138001', email: 'zhangsan@example.com', address: '北京市朝阳区建国路88号', region_id: 1, created_at: '2023-01-15 10:30:00' },
        { customer_id: 1002, customer_name: '李四有限公司', contact_name: '李四', phone: '13900139002', email: 'lisi@example.com', address: '上海市浦东新区陆家嘴1号', region_id: 2, created_at: '2023-01-20 14:15:00' },
        { customer_id: 1003, customer_name: '王五科技', contact_name: '王五', phone: '13700137003', email: 'wangwu@example.com', address: '深圳市南山区科技园路10号', region_id: 3, created_at: '2023-02-05 09:45:00' },
        { customer_id: 1004, customer_name: '赵六贸易', contact_name: '赵六', phone: '13600136004', email: 'zhaoliu@example.com', address: '广州市天河区体育西路5号', region_id: 4, created_at: '2023-02-10 16:20:00' },
        { customer_id: 1005, customer_name: '钱七集团', contact_name: '钱七', phone: '13500135005', email: 'qianqi@example.com', address: '成都市锦江区红星路3段', region_id: 5, created_at: '2023-03-01 11:10:00' }
      ]
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
      let tables = this.tables.filter(table => table.schema === this.selectedSchema)
      if (this.tableSearch) {
        const search = this.tableSearch.toLowerCase()
        tables = tables.filter(table => 
          table.name.toLowerCase().includes(search) || 
          table.description.toLowerCase().includes(search)
        )
      }
      return tables
    },
    
    // 当前选中的表格
    currentTable() {
      return this.tables.find(table => table.id === this.selectedTable)
    },
    
    // 表格数据
    tableData() {
      // 实际应用中，这里应该从API获取数据
      return this.mockData
    },
    
    // 分页信息
    startRow() {
      return (this.currentPage - 1) * this.pageSize + 1
    },
    endRow() {
      const end = this.currentPage * this.pageSize
      return end > this.totalRows ? this.totalRows : end
    },
    totalPages() {
      return Math.ceil(this.totalRows / this.pageSize)
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
    // 选择模式
    selectSchema(schemaId) {
      this.selectedSchema = schemaId
      // 重置表格选择
      if (this.filteredTables.length > 0) {
        this.selectedTable = this.filteredTables[0].id
      } else {
        this.selectedTable = null
      }
    },
    
    // 选择表格
    selectTable(tableId) {
      this.selectedTable = tableId
      this.currentPage = 1 // 重置到第一页
    },
    
    // 获取列图标
    getColumnIcon(column) {
      if (column.isPrimary) return 'fas fa-key text-yellow-500'
      if (column.isForeign) return 'fas fa-link text-green-500'
      if (column.isDate) return 'fas fa-calendar text-purple-500'
      if (column.isText) return 'fas fa-font text-blue-500'
      return 'fas fa-hashtag text-gray-500'
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
    }
  },
  mounted() {
    // 在组件挂载后可以加载初始数据
    console.log('DataBrowse component mounted')
  }
}