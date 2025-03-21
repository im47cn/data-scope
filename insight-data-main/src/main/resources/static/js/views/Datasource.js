// 数据源管理页面组件
export const Datasource = {
  template: `
    <div class="p-6">
      <div class="mb-6">
        <h2 class="text-2xl font-bold text-gray-800">数据源管理</h2>
        <p class="text-gray-600">管理和配置数据库连接</p>
      </div>
      
      <!-- 操作栏 -->
      <div class="mb-6 flex justify-between items-center">
        <div class="flex space-x-2">
          <button @click="showAddDataSourceModal = true" class="bg-primary hover:bg-indigo-700 text-white py-2 px-4 rounded-lg flex items-center transition-all">
            <i class="fas fa-plus mr-2"></i>
            <span>添加数据源</span>
          </button>
          <button @click="refreshDataSources" class="bg-white border border-gray-300 text-gray-700 py-2 px-4 rounded-lg flex items-center hover:bg-gray-50 transition-all">
            <i class="fas fa-sync-alt mr-2"></i>
            <span>刷新</span>
          </button>
        </div>
        <div class="relative">
          <input 
            v-model="searchQuery" 
            type="text" 
            placeholder="搜索数据源..." 
            class="border border-gray-300 rounded-lg px-3 py-2 pl-10 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
          >
          <i class="fas fa-search absolute left-3 top-3 text-gray-400"></i>
        </div>
      </div>
      
      <!-- 加载状态 -->
      <div v-if="loading" class="text-center py-8">
        <i class="fas fa-spinner fa-spin text-primary text-4xl mb-4"></i>
        <p class="text-gray-600">加载数据源...</p>
      </div>
      
      <!-- 错误状态 -->
      <div v-else-if="error" class="bg-red-50 text-red-700 p-4 rounded-lg mb-6">
        <div class="flex items-center">
          <i class="fas fa-exclamation-circle text-xl mr-2"></i>
          <div>
            <h3 class="font-medium">加载数据源时出错</h3>
            <p>{{ error }}</p>
          </div>
        </div>
        <button @click="refreshDataSources" class="mt-2 text-red-700 hover:text-red-900">
          <i class="fas fa-redo mr-1"></i> 重试
        </button>
      </div>
      
      <!-- 数据源列表 -->
      <div v-else class="bg-white rounded-lg shadow overflow-hidden">
        <div v-if="filteredDataSources.length === 0" class="text-center py-8 text-gray-500">
          <i class="fas fa-database text-gray-300 text-5xl mb-4"></i>
          <p>没有找到匹配的数据源</p>
          <button @click="showAddDataSourceModal = true" class="mt-2 text-indigo-600 hover:text-indigo-800">
            添加数据源
          </button>
        </div>
        
        <div v-else>
          <div class="overflow-x-auto">
            <table class="min-w-full divide-y divide-gray-200">
              <thead>
                <tr>
                  <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">名称</th>
                  <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">类型</th>
                  <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">主机/URL</th>
                  <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">数据库</th>
                  <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
                  <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">上次同步</th>
                  <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
                </tr>
              </thead>
              <tbody class="bg-white divide-y divide-gray-200">
                <tr v-for="ds in filteredDataSources" :key="ds.id" @click="viewDataSourceDetails(ds)" class="hover:bg-gray-50 cursor-pointer">
                  <td class="px-6 py-4 whitespace-nowrap">
                    <div class="flex items-center">
                      <div class="flex-shrink-0 h-10 w-10 flex items-center justify-center rounded-full bg-indigo-100 text-indigo-600">
                        <i :class="getDataSourceIcon(ds.type)"></i>
                      </div>
                      <div class="ml-4">
                        <div class="text-sm font-medium text-gray-900">{{ ds.name }}</div>
                        <div class="text-sm text-gray-500">{{ ds.description }}</div>
                      </div>
                    </div>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <span class="px-2 py-1 text-xs rounded-full" :class="getTypeClass(ds.type)">
                      {{ getTypeText(ds.type) }}
                    </span>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {{ ds.host }}
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {{ ds.database }}
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <span class="px-2 py-1 text-xs rounded-full" :class="getStatusClass(ds.active)">
                      {{ ds.active ? '已连接' : '未连接' }}
                    </span>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {{ ds.lastSyncTime || '未同步' }}
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm font-medium" @click.stop>
                    <div class="flex space-x-2">
                      <button @click="testConnection(ds)" class="text-indigo-600 hover:text-indigo-900">
                        <i class="fas fa-plug" title="测试连接"></i>
                      </button>
                      <button @click="syncMetadata(ds)" class="text-indigo-600 hover:text-indigo-900">
                        <i class="fas fa-sync" title="同步元数据"></i>
                      </button>
                      <button @click="editDataSource(ds)" class="text-indigo-600 hover:text-indigo-900">
                        <i class="fas fa-edit" title="编辑"></i>
                      </button>
                      <button @click="deleteDataSource(ds)" class="text-red-600 hover:text-red-900">
                        <i class="fas fa-trash" title="删除"></i>
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
      
      <!-- 添加数据源模态框 -->
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
                    添加数据源
                  </h3>
                  <div class="mt-2 space-y-4">
                    <div>
                      <label class="block text-sm font-medium text-gray-700 mb-1">数据源名称</label>
                      <input v-model="newDataSource.name" type="text" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary">
                    </div>
                    <div>
                      <label class="block text-sm font-medium text-gray-700 mb-1">描述</label>
                      <input v-model="newDataSource.description" type="text" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary">
                    </div>
                    <div>
                      <label class="block text-sm font-medium text-gray-700 mb-1">数据库类型</label>
                      <select v-model="newDataSource.type" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary">
                        <option v-for="type in dataSourceTypes" :key="type.code" :value="type.code">{{ type.name }}</option>
                      </select>
                    </div>
                    <div>
                      <label class="block text-sm font-medium text-gray-700 mb-1">主机/URL</label>
                      <input v-model="newDataSource.host" type="text" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary">
                    </div>
                    <div>
                      <label class="block text-sm font-medium text-gray-700 mb-1">端口</label>
                      <input v-model="newDataSource.port" type="text" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary">
                    </div>
                    <div>
                      <label class="block text-sm font-medium text-gray-700 mb-1">数据库名称</label>
                      <input v-model="newDataSource.database" type="text" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary">
                    </div>
                    <div>
                      <label class="block text-sm font-medium text-gray-700 mb-1">用户名</label>
                      <input v-model="newDataSource.username" type="text" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary">
                    </div>
                    <div>
                      <label class="block text-sm font-medium text-gray-700 mb-1">密码</label>
                      <input v-model="newDataSource.password" type="password" class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary">
                    </div>
                    <div class="flex items-center">
                      <input 
                        type="checkbox" 
                        id="ssl-toggle" 
                        v-model="newDataSource.properties.useSSL"
                        class="h-4 w-4 text-primary focus:ring-primary border-gray-300 rounded"
                      >
                      <label for="ssl-toggle" class="ml-2 block text-sm text-gray-900">
                        使用SSL连接
                      </label>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
              <button @click="addDataSource" type="button" class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-primary text-base font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary sm:ml-3 sm:w-auto sm:text-sm">
                添加
              </button>
              <button @click="testNewConnection" type="button" class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm">
                测试连接
              </button>
              <button @click="showAddDataSourceModal = false" type="button" class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm">
                取消
              </button>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 数据源详情模态框 -->
      <div v-if="selectedDataSource" class="fixed inset-0 overflow-y-auto z-50">
        <div class="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
          <div class="fixed inset-0 transition-opacity" aria-hidden="true" @click="selectedDataSource = null">
            <div class="absolute inset-0 bg-gray-500 opacity-75"></div>
          </div>
          <span class="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>
          <div class="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-4xl sm:w-full">
            <div class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
              <div class="sm:flex sm:items-start">
                <div class="mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left w-full">
                  <div class="flex justify-between items-center mb-4">
                    <h3 class="text-lg leading-6 font-medium text-gray-900">
                      数据源详情: {{ selectedDataSource.name }}
                    </h3>
                    <span class="px-2 py-1 text-xs rounded-full" :class="getStatusClass(selectedDataSource.active)">
                      {{ selectedDataSource.active ? '已连接' : '未连接' }}
                    </span>
                  </div>
                  
                  <div class="mt-4 space-y-6">
                    <!-- 基本信息 -->
                    <div>
                      <h4 class="text-sm font-medium text-gray-700 mb-2">基本信息</h4>
                      <div class="bg-gray-50 p-4 rounded-lg">
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                          <div>
                            <div class="text-xs text-gray-500">数据源名称</div>
                            <div class="text-sm text-gray-900">{{ selectedDataSource.name }}</div>
                          </div>
                          <div>
                            <div class="text-xs text-gray-500">数据库类型</div>
                            <div class="text-sm text-gray-900">{{ getTypeText(selectedDataSource.type) }}</div>
                          </div>
                          <div>
                            <div class="text-xs text-gray-500">主机/URL</div>
                            <div class="text-sm text-gray-900">{{ selectedDataSource.host }}</div>
                          </div>
                          <div>
                            <div class="text-xs text-gray-500">端口</div>
                            <div class="text-sm text-gray-900">{{ selectedDataSource.port }}</div>
                          </div>
                          <div>
                            <div class="text-xs text-gray-500">数据库名称</div>
                            <div class="text-sm text-gray-900">{{ selectedDataSource.database }}</div>
                          </div>
                          <div>
                            <div class="text-xs text-gray-500">用户名</div>
                            <div class="text-sm text-gray-900">{{ selectedDataSource.username }}</div>
                          </div>
                          <div>
                            <div class="text-xs text-gray-500">SSL连接</div>
                            <div class="text-sm text-gray-900">{{ selectedDataSource.properties?.useSSL ? '启用' : '禁用' }}</div>
                          </div>
                          <div>
                            <div class="text-xs text-gray-500">创建时间</div>
                            <div class="text-sm text-gray-900">{{ selectedDataSource.createdAt }}</div>
                          </div>
                        </div>
                      </div>
                    </div>
                    
                    <!-- 元数据统计 -->
                    <div v-if="selectedDataSource.schemaCount !== undefined">
                      <h4 class="text-sm font-medium text-gray-700 mb-2">元数据统计</h4>
                      <div class="bg-gray-50 p-4 rounded-lg">
                        <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
                          <div class="bg-white p-3 rounded-lg shadow-sm">
                            <div class="text-xs text-gray-500">模式数量</div>
                            <div class="text-xl font-bold text-gray-900">{{ selectedDataSource.schemaCount }}</div>
                          </div>
                          <div class="bg-white p-3 rounded-lg shadow-sm">
                            <div class="text-xs text-gray-500">表数量</div>
                            <div class="text-xl font-bold text-gray-900">{{ selectedDataSource.tableCount }}</div>
                          </div>
                          <div class="bg-white p-3 rounded-lg shadow-sm">
                            <div class="text-xs text-gray-500">上次同步</div>
                            <div class="text-sm font-bold text-gray-900">{{ selectedDataSource.lastSyncTime || '未同步' }}</div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
              <button @click="editDataSource(selectedDataSource)" type="button" class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-primary text-base font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary sm:ml-3 sm:w-auto sm:text-sm">
                编辑
              </button>
              <button @click="syncMetadata(selectedDataSource)" type="button" class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm">
                同步元数据
              </button>
              <button @click="selectedDataSource = null" type="button" class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm">
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
      searchQuery: '',
      dataSources: [],
      dataSourceTypes: [],
      loading: true,
      error: null,
      showAddDataSourceModal: false,
      newDataSource: this.getEmptyDataSource(),
      selectedDataSource: null
    }
  },
  computed: {
    filteredDataSources() {
      if (!this.searchQuery) return this.dataSources
      
      const search = this.searchQuery.toLowerCase()
      return this.dataSources.filter(ds => 
        ds.name.toLowerCase().includes(search) || 
        ds.description.toLowerCase().includes(search) || 
        ds.host.toLowerCase().includes(search) || 
        ds.database.toLowerCase().includes(search)
      )
    }
  },
  methods: {
    getEmptyDataSource() {
      return {
        name: '',
        description: '',
        type: '',
        host: '',
        port: '',
        database: '',
        username: '',
        password: '',
        active: true,
        properties: {
          useSSL: false,
          autoReconnect: true
        }
      }
    },
    
    async loadDataSources() {
      this.loading = true
      this.error = null
      
      try {
        // 使用API服务获取数据源列表
        this.dataSources = await DataSourceService.getAllDataSources()
      } catch (error) {
        console.error('加载数据源失败:', error)
        this.error = error.message || '加载数据源失败，请稍后重试'
      } finally {
        this.loading = false
      }
    },
    
    async loadDataSourceTypes() {
      try {
        // 使用API服务获取数据源类型
        this.dataSourceTypes = await DataSourceService.getDataSourceTypes()
      } catch (error) {
        console.error('加载数据源类型失败:', error)
        this.dataSourceTypes = [
          { code: 'MYSQL', name: 'MySQL' },
          { code: 'POSTGRESQL', name: 'PostgreSQL' },
          { code: 'ORACLE', name: 'Oracle' },
          { code: 'SQLSERVER', name: 'SQL Server' },
          { code: 'DB2', name: 'IBM DB2' }
        ]
      }
    },
    
    getDataSourceIcon(type) {
      return 'fas fa-database'
    },
    
    getTypeClass(type) {
      switch (type) {
        case 'MYSQL': return 'bg-blue-100 text-blue-800'
        case 'POSTGRESQL': return 'bg-indigo-100 text-indigo-800'
        case 'ORACLE': return 'bg-red-100 text-red-800'
        case 'SQLSERVER': return 'bg-purple-100 text-purple-800'
        case 'DB2': return 'bg-green-100 text-green-800'
        default: return 'bg-gray-100 text-gray-800'
      }
    },
    
    getTypeText(type) {
      switch (type) {
        case 'MYSQL': return 'MySQL'
        case 'POSTGRESQL': return 'PostgreSQL'
        case 'ORACLE': return 'Oracle'
        case 'SQLSERVER': return 'SQL Server'
        case 'DB2': return 'IBM DB2'
        default: return type
      }
    },
    
    getStatusClass(active) {
      return active ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
    },
    
    async refreshDataSources() {
      await this.loadDataSources()
    },
    
    async viewDataSourceDetails(dataSource) {
      try {
        // 使用API服务获取数据源详情
        const details = await DataSourceService.getDataSource(dataSource.id)
        this.selectedDataSource = details
      } catch (error) {
        console.error('获取数据源详情失败:', error)
        alert('获取数据源详情失败: ' + (error.message || '未知错误'))
      }
    },
    
    async testConnection(dataSource) {
      try {
        // 使用API服务测试连接
        const result = await DataSourceService.testConnection(dataSource.id)
        if (result.connected) {
          alert(`连接成功: ${result.message || '数据库连接正常'}`)
        } else {
          alert(`连接失败: ${result.message || '无法连接到数据库'}`)
        }
      } catch (error) {
        console.error('测试连接失败:', error)
        alert('测试连接失败: ' + (error.message || '未知错误'))
      }
    },
    
    async syncMetadata(dataSource) {
      try {
        // 使用API服务同步元数据
        const result = await MetadataService.syncMetadata(dataSource.id, {
          syncType: 'FULL',
          options: {
            includeSchemas: true,
            includeTables: true,
            includeColumns: true,
            includeIndexes: true,
            includeForeignKeys: true
          }
        })
        
        alert(`元数据同步任务已启动，任务ID: ${result.jobId}`)
        
        // 刷新数据源列表
        this.refreshDataSources()
      } catch (error) {
        console.error('同步元数据失败:', error)
        alert('同步元数据失败: ' + (error.message || '未知错误'))
      }
    },
    
    async editDataSource(dataSource) {
      // 这里应该打开编辑模态框，但为简化示例，我们只显示一个提示
      alert(`编辑数据源: ${dataSource.name}`)
    },
    
    async deleteDataSource(dataSource) {
      if (confirm(`确定要删除数据源 ${dataSource.name} 吗？`)) {
        try {
          // 使用API服务删除数据源
          await DataSourceService.deleteDataSource(dataSource.id)
          
          // 从列表中移除
          this.dataSources = this.dataSources.filter(ds => ds.id !== dataSource.id)
          
          // 如果当前选中的是被删除的数据源，则关闭详情模态框
          if (this.selectedDataSource && this.selectedDataSource.id === dataSource.id) {
            this.selectedDataSource = null
          }
          
          alert('数据源已成功删除')
        } catch (error) {
          console.error('删除数据源失败:', error)
          alert('删除数据源失败: ' + (error.message || '未知错误'))
        }
      }
    },
    
    async testNewConnection() {
      // 这里应该调用API测试新数据源连接，但为简化示例，我们只显示一个提示
      alert('测试连接成功')
    },
    
    async addDataSource() {
      try {
        // 使用API服务创建数据源
        const createdDataSource = await DataSourceService.createDataSource(this.newDataSource)
        
        // 添加到列表
        this.dataSources.push(createdDataSource)
        
        // 关闭模态框并重置表单
        this.showAddDataSourceModal = false
        this.newDataSource = this.getEmptyDataSource()
        
        alert('数据源添加成功')
      } catch (error) {
        console.error('添加数据源失败:', error)
        alert('添加数据源失败: ' + (error.message || '未知错误'))
      }
    }
  },
  mounted() {
    // 加载数据源列表和类型
    this.loadDataSources()
    this.loadDataSourceTypes()
  }
}
