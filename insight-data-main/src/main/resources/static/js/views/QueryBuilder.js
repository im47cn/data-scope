// 查询构建器页面组件
const QueryBuilder = {
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
                 :class="{'selected-table': selectedTables.includes(table.id)}"
                 @click="toggleTableSelection(table.id)">
              <div class="flex items-center">
                <i class="fas fa-table" :class="selectedTables.includes(table.id) ? 'text-primary' : 'text-gray-500'" class="mr-2"></i>
                <span>{{ table.name }}</span>
              </div>
              <div class="text-xs text-gray-500 ml-6">{{ table.description }} ({{ table.rowCount }} 行)</div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 右侧查询构建区域 -->
      <div class="flex-1 flex flex-col overflow-hidden">
        <!-- 工具栏 -->
        <div class="bg-white p-4 border-b border-gray-200">
          <div class="flex justify-between items-center">
            <div class="flex space-x-2">
              <button @click="executeQuery" class="bg-primary hover:bg-indigo-700 text-white py-2 px-4 rounded-lg flex items-center transition-all">
                <i class="fas fa-play mr-2"></i>
                <span>执行查询</span>
              </button>
              <button @click="saveQuery" class="bg-white border border-gray-300 text-gray-700 py-2 px-4 rounded-lg flex items-center hover:bg-gray-50 transition-all">
                <i class="fas fa-save mr-2"></i>
                <span>保存</span>
              </button>
              <button @click="showSqlView = !showSqlView" class="bg-white border border-gray-300 text-gray-700 py-2 px-4 rounded-lg flex items-center hover:bg-gray-50 transition-all">
                <i class="fas fa-code mr-2"></i>
                <span>{{ showSqlView ? '可视化视图' : 'SQL视图' }}</span>
              </button>
            </div>
            <div class="flex space-x-2">
              <button @click="clearQuery" class="bg-white border border-gray-300 text-gray-700 py-2 px-4 rounded-lg flex items-center hover:bg-gray-50 transition-all">
                <i class="fas fa-trash mr-2"></i>
                <span>清空</span>
              </button>
            </div>
          </div>
        </div>
        
        <!-- 查询构建区域 -->
        <div class="flex-1 overflow-auto bg-gray-50 p-4">
          <!-- SQL视图 -->
          <div v-if="showSqlView" class="bg-white rounded-lg shadow p-4 h-full flex flex-col">
            <div class="flex justify-between items-center mb-2">
              <h3 class="font-medium text-gray-700">SQL查询</h3>
              <div>
                <button class="text-gray-500 hover:text-indigo-600 p-1">
                  <i class="fas fa-copy"></i>
                </button>
              </div>
            </div>
            <div class="flex-1 bg-gray-800 text-white p-4 rounded font-mono text-sm overflow-auto">
              <pre>{{ generatedSql }}</pre>
            </div>
          </div>
          
          <!-- 可视化查询构建器 -->
          <div v-else class="h-full flex flex-col space-y-4">
            <!-- 选择的表格 -->
            <div v-if="selectedTables.length > 0" class="bg-white rounded-lg shadow p-4">
              <h3 class="font-medium text-gray-700 mb-3">已选择的表</h3>
              <div class="flex flex-wrap gap-2">
                <div v-for="tableId in selectedTables" :key="tableId" class="bg-indigo-100 text-indigo-800 px-3 py-1 rounded-full flex items-center">
                  <i class="fas fa-table mr-1"></i>
                  <span>{{ getTableById(tableId).name }}</span>
                  <button @click="removeTable(tableId)" class="ml-2 text-indigo-600 hover:text-indigo-800">
                    <i class="fas fa-times"></i>
                  </button>
                </div>
              </div>
            </div>
            
            <!-- 选择列 -->
            <div class="bg-white rounded-lg shadow p-4">
              <div class="flex justify-between items-center mb-3">
                <h3 class="font-medium text-gray-700">选择列</h3>
                <div>
                  <button @click="selectAllColumns" class="text-indigo-600 hover:text-indigo-800 mr-2">
                    全选
                  </button>
                  <button @click="clearSelectedColumns" class="text-indigo-600 hover:text-indigo-800">
                    清空
                  </button>
                </div>
              </div>
              <div v-if="selectedTables.length === 0" class="text-center py-4 text-gray-500">
                <p>请先选择表格</p>
              </div>
              <div v-else class="space-y-3">
                <div v-for="tableId in selectedTables" :key="tableId" class="border border-gray-200 rounded-lg p-3">
                  <div class="flex items-center mb-2">
                    <h4 class="font-medium">{{ getTableById(tableId).name }}</h4>
                    <button @click="selectAllColumnsForTable(tableId)" class="ml-2 text-xs text-indigo-600 hover:text-indigo-800">
                      全选
                    </button>
                  </div>
                  <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-2">
                    <div v-for="column in getColumnsForTable(tableId)" :key="column.name" class="flex items-center">
                      <input 
                        type="checkbox" 
                        :id="'col-' + tableId + '-' + column.name" 
                        :value="{ tableId, columnName: column.name }" 
                        v-model="selectedColumns"
                        class="mr-2 focus:ring-indigo-500 h-4 w-4 text-indigo-600 border-gray-300 rounded"
                      >
                      <label :for="'col-' + tableId + '-' + column.name" class="text-sm text-gray-700">{{ column.name }}</label>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- 条件筛选 -->
            <div class="bg-white rounded-lg shadow p-4">
              <div class="flex justify-between items-center mb-3">
                <h3 class="font-medium text-gray-700">条件筛选</h3>
                <button @click="addCondition" class="text-indigo-600 hover:text-indigo-800">
                  <i class="fas fa-plus"></i> 添加条件
                </button>
              </div>
              <div v-if="conditions.length === 0" class="text-center py-4 text-gray-500">
                <p>尚未添加筛选条件</p>
                <button @click="addCondition" class="mt-2 text-indigo-600 hover:text-indigo-800">
                  添加条件
                </button>
              </div>
              <div v-else class="space-y-3">
                <div v-for="(condition, index) in conditions" :key="index" class="border border-gray-200 rounded-lg p-3">
                  <div class="flex flex-wrap items-center gap-2">
                    <select v-if="index > 0" v-model="condition.connector" class="border border-gray-300 rounded-md px-2 py-1 text-sm focus:outline-none focus:ring-1 focus:ring-primary">
                      <option value="AND">AND</option>
                      <option value="OR">OR</option>
                    </select>
                    <select v-model="condition.tableId" class="border border-gray-300 rounded-md px-2 py-1 text-sm focus:outline-none focus:ring-1 focus:ring-primary">
                      <option v-for="tableId in selectedTables" :key="tableId" :value="tableId">{{ getTableById(tableId).name }}</option>
                    </select>
                    <span class="text-gray-500">.</span>
                    <select v-model="condition.column" class="border border-gray-300 rounded-md px-2 py-1 text-sm focus:outline-none focus:ring-1 focus:ring-primary">
                      <option v-for="column in getColumnsForTable(condition.tableId)" :key="column.name" :value="column.name">{{ column.name }}</option>
                    </select>
                    <select v-model="condition.operator" class="border border-gray-300 rounded-md px-2 py-1 text-sm focus:outline-none focus:ring-1 focus:ring-primary">
                      <option value="=">=</option>
                      <option value="!=">!=</option>
                      <option value=">">></option>
                      <option value="<"><</option>
                      <option value="LIKE">LIKE</option>
                      <option value="IS NULL">IS NULL</option>
                    </select>
                    <input 
                      v-if="!['IS NULL', 'IS NOT NULL'].includes(condition.operator)" 
                      v-model="condition.value" 
                      type="text" 
                      class="border border-gray-300 rounded-md px-2 py-1 text-sm focus:outline-none focus:ring-1 focus:ring-primary"
                      placeholder="值"
                    >
                    <button @click="removeCondition(index)" class="text-red-500 hover:text-red-700 ml-auto">
                      <i class="fas fa-trash"></i>
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 查询结果 -->
        <div v-if="queryResults.length > 0" class="bg-white border-t border-gray-200 overflow-auto" style="max-height: 40%;">
          <div class="p-4">
            <div class="flex justify-between items-center mb-3">
              <h3 class="font-medium text-gray-700">查询结果</h3>
              <div class="flex items-center">
                <span class="text-sm text-gray-600 mr-2">{{ queryResults.length }} 条结果</span>
                <button class="text-indigo-600 hover:text-indigo-800 ml-2">
                  <i class="fas fa-download"></i> 导出
                </button>
              </div>
            </div>
            <div class="overflow-x-auto">
              <table class="min-w-full divide-y divide-gray-200">
                <thead>
                  <tr>
                    <th v-for="(column, index) in queryResultColumns" :key="index" class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      {{ column }}
                    </th>
                  </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                  <tr v-for="(row, rowIndex) in queryResults" :key="rowIndex">
                    <td v-for="(column, colIndex) in queryResultColumns" :key="colIndex" class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {{ row[column] }}
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
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
        { 
          id: 'orders', 
          name: 'orders', 
          description: '订单表', 
          rowCount: '1.2M', 
          schema: 'public',
          columns: [
            { name: 'order_id', type: 'INT', constraint: 'PRIMARY KEY', description: '订单ID', isPrimary: true },
            { name: 'customer_id', type: 'INT', constraint: 'FOREIGN KEY', description: '客户ID', isForeign: true },
            { name: 'order_date', type: 'DATETIME', constraint: 'NOT NULL', description: '订单日期', isDate: true },
            { name: 'total_amount', type: 'DECIMAL(10,2)', constraint: 'NOT NULL', description: '订单总金额', isNumeric: true },
            { name: 'status', type: 'VARCHAR(20)', constraint: 'NOT NULL', description: '订单状态', isText: true }
          ]
        },
        { 
          id: 'products', 
          name: 'products', 
          description: '产品表', 
          rowCount: '5K', 
          schema: 'public',
          columns: [
            { name: 'product_id', type: 'INT', constraint: 'PRIMARY KEY', description: '产品ID', isPrimary: true },
            { name: 'product_name', type: 'VARCHAR(100)', constraint: 'NOT NULL', description: '产品名称', isText: true },
            { name: 'category_id', type: 'INT', constraint: 'FOREIGN KEY', description: '类别ID', isForeign: true },
            { name: 'price', type: 'DECIMAL(10,2)', constraint: 'NOT NULL', description: '产品价格', isNumeric: true },
            { name: 'stock', type: 'INT', constraint: 'NOT NULL', description: '库存数量', isNumeric: true }
          ]
        }
      ],
      tableSearch: '',
      
      // 查询构建器状态
      selectedTables: [],
      selectedColumns: [],
      conditions: [],
      
      // 视图控制
      showSqlView: false,
      
      // 查询结果
      queryResults: [],
      queryResultColumns: []
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
    
    // 生成的SQL查询
    generatedSql() {
      if (this.selectedTables.length === 0) {
        return '-- 请选择至少一个表格'
      }
      
      if (this.selectedColumns.length === 0) {
        return '-- 请选择至少一列'
      }
      
      // 构建SELECT部分
      let sql = 'SELECT\n'
      
      // 添加选中的列
      const columnParts = this.selectedColumns.map(col => {
        const table = this.getTableById(col.tableId)
        return `  ${table.name}.${col.columnName}`
      })
      sql += columnParts.join(',\n')
      
      // 构建FROM部分
      sql += '\nFROM\n'
      const mainTable = this.getTableById(this.selectedTables[0])
      sql += `  ${mainTable.name}`
      
      // 添加WHERE条件
      if (this.conditions.length > 0) {
        sql += '\nWHERE\n'
        this.conditions.forEach((condition, index) => {
          const table = this.getTableById(condition.tableId)
          let conditionStr = ''
          
          if (index > 0) {
            conditionStr += `  ${condition.connector} `
          } else {
            conditionStr += '  '
          }
          
          if (['IS NULL', 'IS NOT NULL'].includes(condition.operator)) {
            conditionStr += `${table.name}.${condition.column} ${condition.operator}`
          } else {
            conditionStr += `${table.name}.${condition.column} ${condition.operator} '${condition.value}'`
          }
          
          sql += conditionStr + '\n'
        })
      }
      
      return sql
    }
  },
  methods: {
    // 选择模式
    selectSchema(schemaId) {
      this.selectedSchema = schemaId
      // 重置表格选择
      this.selectedTables = []
      this.selectedColumns = []
      this.conditions = []
    },
    
    // 切换表格选择
    toggleTableSelection(tableId) {
      const index = this.selectedTables.indexOf(tableId)
      if (index === -1) {
        this.selectedTables.push(tableId)
      } else {
        this.selectedTables.splice(index, 1)
        // 移除相关的列和条件
        this.selectedColumns = this.selectedColumns.filter(col => col.tableId !== tableId)
        this.conditions = this.conditions.filter(cond => cond.tableId !== tableId)
      }
    },
    
    // 移除表格
    removeTable(tableId) {
      const index = this.selectedTables.indexOf(tableId)
      if (index !== -1) {
        this.selectedTables.splice(index, 1)
        // 移除相关的列和条件
        this.selectedColumns = this.selectedColumns.filter(col => col.tableId !== tableId)
        this.conditions = this.conditions.filter(cond => cond.tableId !== tableId)
      }
    },
    
    // 获取表格对象
    getTableById(tableId) {
      return this.tables.find(table => table.id === tableId) || {}
    },
    
    // 获取表格的列
    getColumnsForTable(tableId) {
      const table = this.getTableById(tableId)
      return table.columns || []
    },
    
    // 选择所有列
    selectAllColumns() {
      this.selectedColumns = []
      this.selectedTables.forEach(tableId => {
        const columns = this.getColumnsForTable(tableId)
        columns.forEach(column => {
          this.selectedColumns.push({ tableId, columnName: column.name })
        })
      })
    },
    
    // 为特定表选择所有列
    selectAllColumnsForTable(tableId) {
      // 先移除该表的所有列
      this.selectedColumns = this.selectedColumns.filter(col => col.tableId !== tableId)
      
      // 添加该表的所有列
      const columns = this.getColumnsForTable(tableId)
      columns.forEach(column => {
        this.selectedColumns.push({ tableId, columnName: column.name })
      })
    },
    
    // 清空选择的列
    clearSelectedColumns() {
      this.selectedColumns = []
    },
    
    // 添加条件
    addCondition() {
      if (this.selectedTables.length === 0) return
      
      const tableId = this.selectedTables[0]
      const columns = this.getColumnsForTable(tableId)
      const columnName = columns.length > 0 ? columns[0].name : ''
      
      this.conditions.push({
        connector: 'AND',
        tableId,
        column: columnName,
        operator: '=',
        value: ''
      })
    },
    
    // 移除条件
    removeCondition(index) {
      this.conditions.splice(index, 1)
    },
    
    // 执行查询
    executeQuery() {
      if (this.selectedTables.length === 0 || this.selectedColumns.length === 0) {
        alert('请选择至少一个表格和一列')
        return
      }
      
      // 模拟查询结果
      this.queryResultColumns = this.selectedColumns.map(col => {
        const table = this.getTableById(col.tableId)
        return `${table.name}.${col.columnName}`
      })
      
      // 生成模拟数据
      this.queryResults = []
      for (let i = 0; i < 5; i++) {
        const row = {}
        this.queryResultColumns.forEach(col => {
          const [tableName, columnName] = col.split('.')
          if (columnName.includes('id')) {
            row[col] = Math.floor(1000 + Math.random() * 9000)
          } else if (columnName.includes('name')) {
            row[col] = ['张三', '李四', '王五', '赵六', '钱七'][Math.floor(Math.random() * 5)]
          } else if (columnName.includes('date') || columnName.includes('time')) {
            row[col] = '2023-03-' + (10 + i) + ' 10:' + (10 + i) + ':00'
          } else if (columnName.includes('amount') || columnName.includes('price')) {
            row[col] = (Math.random() * 1000).toFixed(2)
          } else {
            row[col] = '示例数据 ' + (i + 1)
          }
        })
        this.queryResults.push(row)
      }
    },
    
    // 保存查询
    saveQuery() {
      alert('查询已保存')
    },
    
    // 清空查询
    clearQuery() {
      this.selectedTables = []
      this.selectedColumns = []
      this.conditions = []
      this.queryResults = []
      this.queryResultColumns = []
    }
  },
  mounted() {
    console.log('QueryBuilder component mounted')
  }
}
