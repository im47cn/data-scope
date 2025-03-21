// 查询构建器页面组件
const QueryBuilder = {
  template: `
    <div class="p-6">
      <div class="mb-6">
        <h2 class="text-2xl font-bold text-gray-800">查询构建器</h2>
        <p class="text-gray-600">可视化构建SQL查询</p>
      </div>
      
      <!-- 查询工作区 -->
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <!-- 左侧面板：数据源和表格选择 -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-4 border-b border-gray-200">
            <h3 class="font-medium text-gray-700">数据源和表</h3>
          </div>
          <div class="p-4">
            <!-- 数据源选择 -->
            <div class="mb-4">
              <label class="block text-sm font-medium text-gray-700 mb-1">数据源</label>
              <select 
                v-model="selectedDataSourceId" 
                @change="handleDataSourceChange"
                class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
              >
                <option v-for="ds in dataSources" :key="ds.id" :value="ds.id">{{ ds.name }}</option>
              </select>
            </div>
            
            <!-- 模式和表格树 -->
            <div class="mt-4">
              <div class="mb-2">
                <input 
                  v-model="tableSearch" 
                  type="text" 
                  placeholder="搜索表..." 
                  class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                >
              </div>
              
              <div class="max-h-96 overflow-y-auto mt-2">
                <div v-for="schema in filteredSchemas" :key="schema.id" class="mb-2">
                  <!-- 模式 -->
                  <div 
                    @click="toggleSchema(schema.id)"
                    class="flex items-center p-2 hover:bg-gray-100 rounded cursor-pointer"
                  >
                    <i :class="expandedSchemas.includes(schema.id) ? 'fas fa-chevron-down' : 'fas fa-chevron-right'" class="text-gray-500 mr-2"></i>
                    <i class="fas fa-folder text-yellow-500 mr-2"></i>
                    <span>{{ schema.name }}</span>
                  </div>
                  
                  <!-- 表格列表 -->
                  <div v-if="expandedSchemas.includes(schema.id)" class="ml-6">
                    <div 
                      v-for="table in getFilteredTables(schema.id)" 
                      :key="table.id"
                      @click="selectTable(schema, table)"
                      class="flex items-center p-2 hover:bg-gray-100 rounded cursor-pointer"
                      :class="{'bg-indigo-50': isTableSelected(table.id)}"
                    >
                      <i :class="table.type === 'TABLE' ? 'fas fa-table' : 'fas fa-eye'" class="text-indigo-500 mr-2"></i>
                      <span>{{ table.name }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 中间面板：查询设计 -->
        <div class="bg-white rounded-lg shadow lg:col-span-2">
          <div class="p-4 border-b border-gray-200">
            <h3 class="font-medium text-gray-700">查询设计</h3>
          </div>
          <div class="p-4">
            <!-- 选择的表格 -->
            <div v-if="selectedTables.length === 0" class="text-center py-8 text-gray-500">
              <i class="fas fa-table text-gray-300 text-5xl mb-4"></i>
              <p>从左侧选择表格开始构建查询</p>
            </div>
            
            <div v-else>
              <!-- 选择的表格标签 -->
              <div class="flex flex-wrap gap-2 mb-4">
                <div 
                  v-for="(table, index) in selectedTables" 
                  :key="index"
                  class="bg-indigo-100 text-indigo-800 px-3 py-1 rounded-full flex items-center"
                >
                  <i :class="table.type === 'TABLE' ? 'fas fa-table' : 'fas fa-eye'" class="mr-1"></i>
                  <span>{{ table.schema }}.{{ table.name }}</span>
                  <button @click="removeTable(index)" class="ml-2 text-indigo-600 hover:text-indigo-800">
                    <i class="fas fa-times"></i>
                  </button>
                </div>
              </div>
              
              <!-- 列选择 -->
              <div class="mb-4">
                <label class="block text-sm font-medium text-gray-700 mb-2">选择列</label>
                <div class="border border-gray-300 rounded-md p-3">
                  <div class="flex items-center mb-2">
                    <input 
                      type="checkbox" 
                      id="select-all-columns" 
                      v-model="selectAllColumns"
                      @change="toggleAllColumns"
                      class="h-4 w-4 text-primary focus:ring-primary border-gray-300 rounded"
                    >
                    <label for="select-all-columns" class="ml-2 text-sm text-gray-700">全选</label>
                  </div>
                </div>
              </div>
              
              <!-- 条件过滤 -->
              <div class="mb-4">
                <label class="block text-sm font-medium text-gray-700 mb-2">条件过滤</label>
                <div class="border border-gray-300 rounded-md p-3">
                  <button @click="addCondition" class="mt-2 text-indigo-600 hover:text-indigo-800 text-sm">
                    <i class="fas fa-plus mr-1"></i> 添加条件
                  </button>
                </div>
              </div>
              
              <!-- 排序 -->
              <div class="mb-4">
                <label class="block text-sm font-medium text-gray-700 mb-2">排序</label>
                <div class="border border-gray-300 rounded-md p-3">
                  <button @click="addSort" class="mt-2 text-indigo-600 hover:text-indigo-800 text-sm">
                    <i class="fas fa-plus mr-1"></i> 添加排序
                  </button>
                </div>
              </div>
              
              <!-- 限制和偏移 -->
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">限制行数 (LIMIT)</label>
                  <input 
                    v-model="limit" 
                    type="number" 
                    min="1"
                    class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                  >
                </div>
                
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">偏移量 (OFFSET)</label>
                  <input 
                    v-model="offset" 
                    type="number" 
                    min="0"
                    class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                  >
                </div>
              </div>
              
              <!-- 操作按钮 -->
              <div class="flex justify-end space-x-3">
                <button @click="clearQuery" class="border border-gray-300 text-gray-700 py-2 px-4 rounded-lg flex items-center hover:bg-gray-50 transition-all">
                  <i class="fas fa-trash mr-2"></i>
                  <span>清空</span>
                </button>
                <button @click="saveQuery" class="border border-gray-300 text-gray-700 py-2 px-4 rounded-lg flex items-center hover:bg-gray-50 transition-all">
                  <i class="fas fa-save mr-2"></i>
                  <span>保存</span>
                </button>
                <button @click="generateSQL" class="bg-primary hover:bg-indigo-700 text-white py-2 px-4 rounded-lg flex items-center transition-all">
                  <i class="fas fa-code mr-2"></i>
                  <span>生成SQL</span>
                </button>
                <button @click="executeQuery" class="bg-green-600 hover:bg-green-700 text-white py-2 px-4 rounded-lg flex items-center transition-all">
                  <i class="fas fa-play mr-2"></i>
                  <span>执行</span>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- SQL预览和结果 -->
      <div class="mt-6">
        <div class="bg-white rounded-lg shadow">
          <div class="p-4 border-b border-gray-200">
            <h3 class="font-medium text-gray-700">SQL预览</h3>
          </div>
          <div class="p-4">
            <div class="bg-gray-800 text-white p-4 rounded-lg font-mono text-sm overflow-x-auto">
              <pre>{{ generatedSQL || '-- SQL将在这里显示 --' }}</pre>
            </div>
            
            <div class="flex justify-end mt-4">
              <button 
                @click="copySQL" 
                class="border border-gray-300 text-gray-700 py-2 px-4 rounded-lg flex items-center hover:bg-gray-50 transition-all"
                :disabled="!generatedSQL"
              >
                <i class="fas fa-copy mr-2"></i>
                <span>复制SQL</span>
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
      selectedDataSourceId: null,
      
      // 模式
      schemas: [],
      expandedSchemas: [],
      
      // 表格
      tables: {},
      selectedTables: [],
      tableSearch: '',
      
      // 查询设计
      selectAllColumns: false,
      conditions: [],
      joins: [],
      sorts: [],
      limit: 1000,
      offset: 0,
      
      // SQL和结果
      generatedSQL: '',
      queryResult: null,
      
      // 保存查询
      showSaveQueryModal: false,
      savedQuery: {
        name: '',
        description: '',
        shared: false
      },
      
      // 加载状态
      loading: {
        dataSources: false,
        schemas: false,
        tables: false,
        query: false
      },
      
      // 错误状态
      error: null
    }
  },
  computed: {
    // 过滤后的模式列表
    filteredSchemas() {
      return this.schemas
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
          this.handleDataSourceChange()
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
      this.schemas = []
      this.expandedSchemas = []
      this.tables = {}
      this.selectedTables = []
      this.generatedSQL = ''
      this.queryResult = null
      
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
      } catch (error) {
        console.error('加载模式失败:', error)
        this.error = error.message || '加载模式失败，请稍后重试'
      } finally {
        this.loading.schemas = false
      }
    },
    
    // 切换模式展开/折叠
    toggleSchema(schemaId) {
      const index = this.expandedSchemas.indexOf(schemaId)
      if (index === -1) {
        this.expandedSchemas.push(schemaId)
        this.loadTables(schemaId)
      } else {
        this.expandedSchemas.splice(index, 1)
      }
    },
    
    // 加载表格列表
    async loadTables(schemaId) {
      if (!this.selectedDataSourceId || !schemaId) return
      
      this.loading.tables = true
      this.error = null
      
      try {
        // 获取选中的模式名称
        const selectedSchema = this.schemas.find(schema => schema.id === schemaId)
        if (!selectedSchema) throw new Error('未找到选中的模式')
        
        // 使用API服务获取表格列表
        const response = await MetadataService.getTables(this.selectedDataSourceId, selectedSchema.name)
        this.tables[schemaId] = response.content || response
      } catch (error) {
        console.error('加载表格失败:', error)
        this.error = error.message || '加载表格失败，请稍后重试'
      } finally {
        this.loading.tables = false
      }
    },
    
    // 获取过滤后的表格列表
    getFilteredTables(schemaId) {
      const tables = this.tables[schemaId] || []
      if (!this.tableSearch) return tables
      
      const search = this.tableSearch.toLowerCase()
      return tables.filter(table => 
        table.name.toLowerCase().includes(search) || 
        (table.description && table.description.toLowerCase().includes(search))
      )
    },
    
    // 检查表格是否已选择
    isTableSelected(tableId) {
      return this.selectedTables.some(table => table.id === tableId)
    },
    
    // 选择表格
    async selectTable(schema, table) {
      if (this.isTableSelected(table.id)) return
      
      // 加载表格列信息
      try {
        const columns = await MetadataService.getColumns(this.selectedDataSourceId, schema.name, table.name)
        
        // 添加到已选择的表格
        this.selectedTables.push({
          id: table.id,
          schema: schema.name,
          name: table.name,
          type: table.type,
          columns: columns.map(col => ({
            ...col,
            selected: true
          }))
        })
      } catch (error) {
        console.error('加载表格列失败:', error)
        this.error = error.message || '加载表格列失败，请稍后重试'
      }
    },
    
    // 移除表格
    removeTable(index) {
      this.selectedTables.splice(index, 1)
      
      // 更新连接和条件
      this.updateJoinsAfterTableRemoval()
      this.updateConditionsAfterTableRemoval()
      this.updateSortsAfterTableRemoval()
    },
    
    // 切换全选列
    toggleAllColumns() {
      this.selectedTables.forEach(table => {
        table.columns.forEach(column => {
          column.selected = this.selectAllColumns
        })
      })
    },
    
    // 添加条件
    addCondition() {
      this.conditions.push({
        column: '',
        operator: '=',
        value: '',
        connector: 'AND'
      })
    },
    
    // 移除条件
    removeCondition(index) {
      this.conditions.splice(index, 1)
    },
    
    // 添加连接
    addJoin() {
      if (this.selectedTables.length < 2) return
      
      this.joins.push({
        leftTable: 0,
        rightTable: 1,
        leftColumn: '',
        rightColumn: '',
        type: 'INNER'
      })
      
      this.updateJoinColumns(this.joins[this.joins.length - 1])
    },
    
    // 移除连接
    removeJoin(index) {
      this.joins.splice(index, 1)
    },
    
    // 更新连接列
    updateJoinColumns(join, side) {
      // 实际实现会更新可选的列
    },
    
    // 获取连接列
    getJoinColumns(tableIndex) {
      if (tableIndex === undefined || !this.selectedTables[tableIndex]) return []
      return this.selectedTables[tableIndex].columns
    },
    
    // 添加排序
    addSort() {
      this.sorts.push({
        column: '',
        direction: 'ASC'
      })
    },
    
    // 移除排序
    removeSort(index) {
      this.sorts.splice(index, 1)
    },
    
    // 更新连接（表格移除后）
    updateJoinsAfterTableRemoval() {
      // 实际实现会更新连接
    },
    
    // 更新条件（表格移除后）
    updateConditionsAfterTableRemoval() {
      // 实际实现会更新条件
    },
    
    // 更新排序（表格移除后）
    updateSortsAfterTableRemoval() {
      // 实际实现会更新排序
    },
    
    // 生成SQL
    generateSQL() {
      // 检查是否有选择的表格
      if (this.selectedTables.length === 0) {
        this.error = '请至少选择一个表格'
        return
      }
      
      try {
        // 构建SQL查询
        let sql = 'SELECT '
        
        // 选择的列
        const selectedColumns = []
        this.selectedTables.forEach(table => {
          table.columns.forEach(column => {
            if (column.selected) {
              selectedColumns.push(`${table.schema}.${table.name}.${column.name}`)
            }
          })
        })
        
        if (selectedColumns.length === 0) {
          sql += '* '
        } else {
          sql += selectedColumns.join(', ') + ' '
        }
        
        // FROM子句
        sql += `\nFROM ${this.selectedTables[0].schema}.${this.selectedTables[0].name} `
        
        // JOIN子句
        this.joins.forEach(join => {
          const leftTable = this.selectedTables[join.leftTable]
          const rightTable = this.selectedTables[join.rightTable]
          
          sql += `\n${join.type} JOIN ${rightTable.schema}.${rightTable.name} ON ${leftTable.schema}.${leftTable.name}.${join.leftColumn} = ${rightTable.schema}.${rightTable.name}.${join.rightColumn} `
        })
        
        // WHERE子句
        if (this.conditions.length > 0) {
          sql += '\nWHERE '
          
          this.conditions.forEach((condition, index) => {
            if (index > 0) {
              sql += ` ${condition.connector} `
            }
            
            if (['IS NULL', 'IS NOT NULL'].includes(condition.operator)) {
              sql += `${condition.column} ${condition.operator}`
            } else if (['IN', 'NOT IN'].includes(condition.operator)) {
              const values = condition.value.split(',').map(v => v.trim()).filter(v => v).map(v => isNaN(v) ? `'${v}'` : v).join(', ')
              sql += `${condition.column} ${condition.operator} (${values})`
            } else {
              const value = isNaN(condition.value) ? `'${condition.value}'` : condition.value
              sql += `${condition.column} ${condition.operator} ${value}`
            }
          })
        }
        
        // ORDER BY子句
        if (this.sorts.length > 0) {
          sql += '\nORDER BY '
          
          this.sorts.forEach((sort, index) => {
            if (index > 0) {
              sql += ', '
            }
            
            sql += `${sort.column} ${sort.direction}`
          })
        }
        
        // LIMIT和OFFSET子句
        if (this.limit) {
          sql += `\nLIMIT ${this.limit}`
          
          if (this.offset) {
            sql += ` OFFSET ${this.offset}`
          }
        }
        
        this.generatedSQL = sql
      } catch (error) {
        console.error('生成SQL失败:', error)
        this.error = error.message || '生成SQL失败，请检查查询设计'
      }
    },
    
    // 执行查询
    async executeQuery() {
      if (!this.generatedSQL) {
        this.generateSQL()
        if (!this.generatedSQL) return
      }
      
      this.loading.query = true
      this.error = null
      
      try {
        // 使用API服务执行查询
        const result = await QueryService.executeQuery({
          dataSourceId: this.selectedDataSourceId,
          sql: this.generatedSQL,
          options: {
            timeout: 30,
            maxRows: this.limit || 1000
          }
        })
        
        this.queryResult = result
      } catch (error) {
        console.error('执行查询失败:', error)
        this.error = error.message || '执行查询失败，请检查SQL语法'
      } finally {
        this.loading.query = false
      }
    },
    
    // 复制SQL
    copySQL() {
      if (!this.generatedSQL) return
      
      navigator.clipboard.writeText(this.generatedSQL)
        .then(() => {
          alert('SQL已复制到剪贴板')
        })
        .catch(err => {
          console.error('复制失败:', err)
          alert('复制失败，请手动复制')
        })
    },
    
    // 清空查询
    clearQuery() {
      if (confirm('确定要清空当前查询吗？')) {
        this.selectedTables = []
        this.conditions = []
        this.joins = []
        this.sorts = []
        this.limit = 1000
        this.offset = 0
        this.generatedSQL = ''
        this.queryResult = null
      }
    },
    
    // 保存查询
    saveQuery() {
      if (!this.generatedSQL) {
        this.generateSQL()
        if (!this.generatedSQL) return
      }
      
      this.showSaveQueryModal = true
    },
    
    // 确认保存查询
    async confirmSaveQuery() {
      if (!this.savedQuery.name) {
        alert('请输入查询名称')
        return
      }
      
      try {
        // 使用API服务保存查询
        await QueryService.saveQuery({
          name: this.savedQuery.name,
          description: this.savedQuery.description,
          dataSourceId: this.selectedDataSourceId,
          sql: this.generatedSQL,
          shared: this.savedQuery.shared
        })
        
        this.showSaveQueryModal = false
        this.savedQuery = {
          name: '',
          description: '',
          shared: false
        }
        
        alert('查询已保存')
      } catch (error) {
        console.error('保存查询失败:', error)
        alert('保存查询失败: ' + (error.message || '未知错误'))
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
