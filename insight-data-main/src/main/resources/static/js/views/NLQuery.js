// 自然语言查询页面组件
export const NLQuery = {
  template: `
    <div class="p-6">
      <div class="mb-6">
        <h2 class="text-2xl font-bold text-gray-800">自然语言查询</h2>
        <p class="text-gray-600">使用自然语言描述您的查询需求</p>
      </div>
      
      <!-- 查询区域 -->
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <!-- 左侧面板：数据源选择和查询输入 -->
        <div class="bg-white rounded-lg shadow lg:col-span-1">
          <div class="p-4 border-b border-gray-200">
            <h3 class="font-medium text-gray-700">查询输入</h3>
          </div>
          <div class="p-4">
            <!-- 数据源选择 -->
            <div class="mb-4">
              <label class="block text-sm font-medium text-gray-700 mb-1">数据源</label>
              <select 
                v-model="selectedDataSourceId" 
                class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
              >
                <option v-for="ds in dataSources" :key="ds.id" :value="ds.id">{{ ds.name }}</option>
              </select>
            </div>
            
            <!-- 自然语言查询输入 -->
            <div class="mb-4">
              <label class="block text-sm font-medium text-gray-700 mb-1">用自然语言描述您的查询</label>
              <textarea 
                v-model="naturalLanguageQuery" 
                rows="5" 
                placeholder="例如：查询上个月销售额最高的前5个产品及其销售额"
                class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
              ></textarea>
            </div>
            
            <!-- 查询历史 -->
            <div class="mb-4">
              <div class="flex justify-between items-center mb-2">
                <h4 class="text-sm font-medium text-gray-700">查询历史</h4>
                <button @click="loadQueryHistory" class="text-indigo-600 hover:text-indigo-800 text-sm">
                  <i class="fas fa-sync-alt mr-1"></i> 刷新
                </button>
              </div>
              <div class="max-h-60 overflow-y-auto border border-gray-200 rounded-md">
                <div 
                  v-for="(query, index) in queryHistory" 
                  :key="index"
                  @click="selectHistoryQuery(query)"
                  class="p-2 border-b border-gray-200 last:border-b-0 hover:bg-gray-50 cursor-pointer"
                >
                  <div class="text-sm font-medium text-gray-700">{{ query.naturalLanguage }}</div>
                  <div class="text-xs text-gray-500 mt-1">{{ query.executedAt }}</div>
                </div>
                <div v-if="queryHistory.length === 0" class="p-3 text-center text-gray-500 text-sm">
                  暂无查询历史
                </div>
              </div>
            </div>
            
            <!-- 查询建议 -->
            <div class="mb-4">
              <h4 class="text-sm font-medium text-gray-700 mb-2">查询建议</h4>
              <div class="flex flex-wrap gap-2">
                <button 
                  v-for="(suggestion, index) in querySuggestions" 
                  :key="index"
                  @click="selectSuggestion(suggestion)"
                  class="bg-gray-100 hover:bg-gray-200 text-gray-700 text-sm px-3 py-1 rounded-full transition-colors"
                >
                  {{ suggestion }}
                </button>
              </div>
            </div>
            
            <!-- 操作按钮 -->
            <div class="flex justify-between">
              <button @click="clearQuery" class="border border-gray-300 text-gray-700 py-2 px-4 rounded-lg flex items-center hover:bg-gray-50 transition-all">
                <i class="fas fa-trash mr-2"></i>
                <span>清空</span>
              </button>
              <button 
                @click="executeQuery" 
                :disabled="!canExecuteQuery"
                class="bg-primary hover:bg-indigo-700 text-white py-2 px-4 rounded-lg flex items-center transition-all disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <i class="fas fa-play mr-2"></i>
                <span>执行查询</span>
              </button>
            </div>
          </div>
        </div>
        
        <!-- 右侧面板：查询结果 -->
        <div class="bg-white rounded-lg shadow lg:col-span-2">
          <div class="p-4 border-b border-gray-200">
            <h3 class="font-medium text-gray-700">查询结果</h3>
          </div>
          
          <!-- 加载状态 -->
          <div v-if="loading" class="p-6 flex items-center justify-center">
            <div class="text-center">
              <i class="fas fa-spinner fa-spin text-primary text-3xl mb-4"></i>
              <p class="text-gray-500">正在处理您的查询...</p>
            </div>
          </div>
          
          <!-- 错误状态 -->
          <div v-else-if="error" class="p-6">
            <div class="bg-red-50 text-red-700 p-4 rounded-lg">
              <div class="flex items-center">
                <i class="fas fa-exclamation-circle text-xl mr-2"></i>
                <div>
                  <h3 class="font-medium">查询失败</h3>
                  <p>{{ error }}</p>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 空状态 -->
          <div v-else-if="!queryResult" class="p-6 flex items-center justify-center h-96">
            <div class="text-center text-gray-500">
              <i class="fas fa-search text-5xl mb-4"></i>
              <p>输入自然语言查询并点击"执行查询"按钮</p>
            </div>
          </div>
          
          <!-- 查询结果 -->
          <div v-else class="p-4">
            <!-- 理解和SQL -->
            <div class="mb-6">
              <div class="mb-4">
                <h4 class="text-sm font-medium text-gray-700 mb-2">查询理解</h4>
                <div class="bg-gray-50 p-3 rounded-lg text-gray-700">
                  {{ queryResult.understanding || naturalLanguageQuery }}
                </div>
              </div>
              
              <div class="mb-4">
                <div class="flex justify-between items-center mb-2">
                  <h4 class="text-sm font-medium text-gray-700">生成的SQL</h4>
                  <div class="flex items-center">
                    <span class="text-xs text-gray-500 mr-2">置信度: {{ (queryResult.confidence * 100).toFixed(0) }}%</span>
                    <button @click="copySQL" class="text-indigo-600 hover:text-indigo-800">
                      <i class="fas fa-copy"></i>
                    </button>
                  </div>
                </div>
                <div class="bg-gray-800 text-white p-3 rounded-lg font-mono text-sm overflow-x-auto">
                  <pre>{{ queryResult.sql }}</pre>
                </div>
              </div>
              
              <!-- 替代SQL -->
              <div v-if="queryResult.alternativeSQLs && queryResult.alternativeSQLs.length > 0" class="mb-4">
                <h4 class="text-sm font-medium text-gray-700 mb-2">替代SQL</h4>
                <div v-for="(alt, index) in queryResult.alternativeSQLs" :key="index" class="mb-2 last:mb-0">
                  <div class="flex justify-between items-center mb-1">
                    <span class="text-xs text-gray-500">{{ alt.explanation }}</span>
                    <button @click="useAlternativeSQL(alt.sql)" class="text-xs text-indigo-600 hover:text-indigo-800">
                      使用此SQL
                    </button>
                  </div>
                  <div class="bg-gray-800 text-white p-2 rounded-lg font-mono text-xs overflow-x-auto">
                    <pre>{{ alt.sql }}</pre>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- 数据结果 -->
            <div>
              <div class="flex justify-between items-center mb-2">
                <h4 class="text-sm font-medium text-gray-700">数据结果</h4>
                <div class="text-xs text-gray-500">
                  执行时间: {{ queryResult.executionTime }}ms | 返回行数: {{ queryResult.rowCount }}
                </div>
              </div>
              
              <!-- 表格结果 -->
              <div class="overflow-x-auto">
                <table class="min-w-full divide-y divide-gray-200">
                  <thead>
                    <tr>
                      <th 
                        v-for="column in queryResult.columns" 
                        :key="column.name" 
                        class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                      >
                        {{ column.label || column.name }}
                        <div class="text-xs font-normal text-gray-400 mt-1">{{ column.type }}</div>
                      </th>
                    </tr>
                  </thead>
                  <tbody class="bg-white divide-y divide-gray-200">
                    <tr v-for="(row, rowIndex) in queryResult.rows" :key="rowIndex" class="hover:bg-gray-50">
                      <td v-for="column in queryResult.columns" :key="column.name" class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {{ formatCellValue(row[column.name], column.type) }}
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
              
              <!-- 无数据 -->
              <div v-if="queryResult.rows && queryResult.rows.length === 0" class="text-center py-8 text-gray-500">
                <i class="fas fa-inbox text-gray-300 text-5xl mb-4"></i>
                <p>查询未返回任何数据</p>
              </div>
              
              <!-- 操作按钮 -->
              <div class="flex justify-end mt-4 space-x-3">
                <button @click="saveQuery" class="border border-gray-300 text-gray-700 py-2 px-4 rounded-lg flex items-center hover:bg-gray-50 transition-all">
                  <i class="fas fa-save mr-2"></i>
                  <span>保存查询</span>
                </button>
                <button @click="exportData" class="border border-gray-300 text-gray-700 py-2 px-4 rounded-lg flex items-center hover:bg-gray-50 transition-all">
                  <i class="fas fa-download mr-2"></i>
                  <span>导出数据</span>
                </button>
              </div>
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
      
      // 查询
      naturalLanguageQuery: '',
      queryHistory: [],
      querySuggestions: [
        '查询上个月销售额最高的前5个产品',
        '统计各地区的客户数量',
        '查找库存少于20的产品',
        '分析近三个月的销售趋势',
        '查询每个销售人员的业绩排名'
      ],
      
      // 结果
      queryResult: null,
      
      // 状态
      loading: false,
      error: null
    }
  },
  computed: {
    // 是否可以执行查询
    canExecuteQuery() {
      return this.selectedDataSourceId && this.naturalLanguageQuery.trim().length > 0 && !this.loading
    }
  },
  methods: {
    // 加载数据源列表
    async loadDataSources() {
      try {
        // 使用API服务获取数据源列表
        const response = await DataSourceService.getAllDataSources()
        this.dataSources = response.content || response
        
        // 如果有数据源，默认选择第一个
        if (this.dataSources.length > 0 && !this.selectedDataSourceId) {
          this.selectedDataSourceId = this.dataSources[0].id
        }
      } catch (error) {
        console.error('加载数据源失败:', error)
        this.error = error.message || '加载数据源失败，请稍后重试'
      }
    },
    
    // 加载查询历史
    async loadQueryHistory() {
      try {
        // 使用API服务获取查询历史
        const response = await NLQueryService.getQueryHistory()
        this.queryHistory = response.content || response || []
      } catch (error) {
        console.error('加载查询历史失败:', error)
        this.error = error.message || '加载查询历史失败，请稍后重试'
      }
    },
    
    // 选择历史查询
    selectHistoryQuery(query) {
      this.naturalLanguageQuery = query.naturalLanguage
      if (query.dataSourceId) {
        this.selectedDataSourceId = query.dataSourceId
      }
    },
    
    // 选择查询建议
    selectSuggestion(suggestion) {
      this.naturalLanguageQuery = suggestion
    },
    
    // 清空查询
    clearQuery() {
      this.naturalLanguageQuery = ''
      this.queryResult = null
      this.error = null
    },
    
    // 执行查询
    async executeQuery() {
      if (!this.canExecuteQuery) return
      
      this.loading = true
      this.error = null
      this.queryResult = null
      
      try {
        // 使用API服务执行自然语言查询
        const result = await NLQueryService.executeNLQuery(
          this.selectedDataSourceId,
          this.naturalLanguageQuery,
          {
            timeout: 30,
            maxRows: 1000
          }
        )
        
        this.queryResult = result
      } catch (error) {
        console.error('执行查询失败:', error)
        this.error = error.message || '执行查询失败，请检查查询语句或稍后重试'
      } finally {
        this.loading = false
      }
    },
    
    // 使用替代SQL
    async useAlternativeSQL(sql) {
      if (!this.selectedDataSourceId) return
      
      this.loading = true
      this.error = null
      
      try {
        // 使用API服务执行SQL查询
        const result = await QueryService.executeQuery({
          dataSourceId: this.selectedDataSourceId,
          sql,
          options: {
            timeout: 30,
            maxRows: 1000
          }
        })
        
        // 保留原始的理解和置信度
        this.queryResult = {
          ...result,
          understanding: this.queryResult.understanding,
          confidence: this.queryResult.confidence,
          sql
        }
      } catch (error) {
        console.error('执行替代SQL失败:', error)
        this.error = error.message || '执行替代SQL失败，请稍后重试'
      } finally {
        this.loading = false
      }
    },
    
    // 复制SQL
    copySQL() {
      if (!this.queryResult || !this.queryResult.sql) return
      
      navigator.clipboard.writeText(this.queryResult.sql)
        .then(() => {
          alert('SQL已复制到剪贴板')
        })
        .catch(err => {
          console.error('复制失败:', err)
          alert('复制失败，请手动复制')
        })
    },
    
    // 保存查询
    async saveQuery() {
      if (!this.queryResult || !this.queryResult.sql) return
      
      const queryName = prompt('请输入查询名称:', this.naturalLanguageQuery.substring(0, 50))
      if (!queryName) return
      
      try {
        // 使用API服务保存查询
        await QueryService.saveQuery({
          name: queryName,
          description: this.naturalLanguageQuery,
          dataSourceId: this.selectedDataSourceId,
          sql: this.queryResult.sql,
          parameters: [],
          shared: false,
          tags: ['自然语言查询']
        })
        
        alert('查询已保存')
      } catch (error) {
        console.error('保存查询失败:', error)
        alert('保存查询失败: ' + (error.message || '未知错误'))
      }
    },
    
    // 导出数据
    exportData() {
      if (!this.queryResult || !this.queryResult.columns || !this.queryResult.rows) {
        alert('没有数据可导出')
        return
      }
      
      try {
        // 构建CSV内容
        const headers = this.queryResult.columns.map(col => col.name).join(',')
        const rows = this.queryResult.rows.map(row => {
          return this.queryResult.columns.map(col => {
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
        link.setAttribute('download', `nl-query-${new Date().getTime()}.csv`)
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
    // 加载数据源列表和查询历史
    this.loadDataSources()
    this.loadQueryHistory()
  }
}