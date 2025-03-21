// 低代码集成页面组件
const LowCode = {
  template: `
    <div class="p-6">
      <div class="mb-6">
        <h2 class="text-2xl font-bold text-gray-800">低代码集成</h2>
        <p class="text-gray-600">管理和配置低代码平台集成</p>
      </div>
      
      <!-- 操作栏 -->
      <div class="mb-6 flex justify-between items-center">
        <div class="flex space-x-2">
          <button @click="showCreateConfigModal = true" class="bg-primary hover:bg-indigo-700 text-white py-2 px-4 rounded-lg flex items-center transition-all">
            <i class="fas fa-plus mr-2"></i>
            <span>创建配置</span>
          </button>
          <button @click="refreshConfigs" class="bg-white border border-gray-300 text-gray-700 py-2 px-4 rounded-lg flex items-center hover:bg-gray-50 transition-all">
            <i class="fas fa-sync-alt mr-2"></i>
            <span>刷新</span>
          </button>
        </div>
        <div class="relative">
          <input 
            v-model="searchQuery" 
            type="text" 
            placeholder="搜索配置..." 
            class="border border-gray-300 rounded-lg px-3 py-2 pl-10 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
          >
          <i class="fas fa-search absolute left-3 top-3 text-gray-400"></i>
        </div>
      </div>
      
      <!-- 配置列表 -->
      <div class="bg-white rounded-lg shadow">
        <!-- 加载状态 -->
        <div v-if="loading" class="p-6 flex items-center justify-center">
          <div class="text-center">
            <i class="fas fa-spinner fa-spin text-primary text-3xl mb-4"></i>
            <p class="text-gray-500">加载配置...</p>
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
            <button @click="loadConfigs" class="mt-2 text-red-700 hover:text-red-900">
              <i class="fas fa-redo mr-1"></i> 重试
            </button>
          </div>
        </div>
        
        <!-- 空状态 -->
        <div v-else-if="filteredConfigs.length === 0" class="p-6 flex items-center justify-center">
          <div class="text-center text-gray-500">
            <i class="fas fa-code text-gray-300 text-5xl mb-4"></i>
            <p>暂无低代码配置</p>
            <button @click="showCreateConfigModal = true" class="mt-4 text-indigo-600 hover:text-indigo-800">
              创建配置
            </button>
          </div>
        </div>
        
        <!-- 配置卡片列表 -->
        <div v-else class="p-6 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <div 
            v-for="config in filteredConfigs" 
            :key="config.id" 
            class="bg-white border border-gray-200 rounded-lg shadow-sm hover:shadow-md transition-shadow"
          >
            <div class="p-4 border-b border-gray-200">
              <div class="flex justify-between items-start">
                <h3 class="text-lg font-medium text-gray-900">{{ config.name }}</h3>
                <div class="flex space-x-1">
                  <button @click="editConfig(config)" class="text-gray-500 hover:text-gray-700">
                    <i class="fas fa-edit"></i>
                  </button>
                  <button @click="deleteConfig(config)" class="text-gray-500 hover:text-red-600">
                    <i class="fas fa-trash"></i>
                  </button>
                </div>
              </div>
              <p class="mt-1 text-sm text-gray-600 line-clamp-2">{{ config.description }}</p>
            </div>
            
            <div class="p-4">
              <div class="flex items-center text-sm text-gray-500 mb-3">
                <i class="fas fa-database mr-2"></i>
                <span>{{ getDataSourceName(config.queryId) }}</span>
              </div>
              
              <div class="flex items-center text-sm text-gray-500 mb-3">
                <i class="fas fa-chart-bar mr-2"></i>
                <span>{{ getDisplayType(config.displayConfig?.type) }}</span>
              </div>
              
              <div class="flex items-center text-sm text-gray-500">
                <i class="fas fa-calendar-alt mr-2"></i>
                <span>{{ formatDate(config.createdAt) }}</span>
              </div>
            </div>
            
            <div class="p-4 bg-gray-50 border-t border-gray-200">
              <div class="flex justify-between">
                <button @click="previewConfig(config)" class="text-indigo-600 hover:text-indigo-800 text-sm">
                  <i class="fas fa-eye mr-1"></i> 预览
                </button>
                <button @click="copyEmbedCode(config)" class="text-indigo-600 hover:text-indigo-800 text-sm">
                  <i class="fas fa-code mr-1"></i> 获取嵌入代码
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
      // 配置列表
      configs: [],
      filteredConfigs: [],
      searchQuery: '',
      
      // 保存的查询
      savedQueries: [],
      
      // 编辑状态
      showCreateConfigModal: false,
      editingConfig: null,
      previewingConfig: null,
      
      // 配置表单
      configForm: {
        name: '',
        description: '',
        queryId: '',
        formConfig: {
          layout: 'standard',
          fields: []
        },
        displayConfig: {
          type: 'table',
          columns: []
        },
        chartConfig: {
          type: 'bar',
          title: '',
          xField: '',
          yField: '',
          color: '#4c51bf'
        }
      },
      
      // 配置选项卡
      activeConfigTab: 'form',
      configTabs: [
        { id: 'form', name: '表单配置', icon: 'fas fa-list-alt' },
        { id: 'display', name: '显示配置', icon: 'fas fa-table' },
        { id: 'chart', name: '图表配置', icon: 'fas fa-chart-bar' },
        { id: 'ai', name: 'AI辅助', icon: 'fas fa-magic' }
      ],
      
      // AI辅助
      aiDescription: '',
      aiPreferredDisplayType: 'table',
      aiSuggestion: null,
      
      // 状态
      loading: false,
      error: null
    }
  },
  methods: {
    // 加载配置列表
    async loadConfigs() {
      this.loading = true
      this.error = null
      
      try {
        // 使用API服务获取配置列表
        const configs = await LowCodeService.getConfigs()
        this.configs = configs
        this.filterConfigs()
      } catch (error) {
        console.error('加载配置失败:', error)
        this.error = error.message || '加载配置失败，请稍后重试'
      } finally {
        this.loading = false
      }
    },
    
    // 加载保存的查询
    async loadSavedQueries() {
      try {
        // 使用API服务获取保存的查询
        const queries = await QueryService.getSavedQueries()
        this.savedQueries = queries
      } catch (error) {
        console.error('加载保存的查询失败:', error)
        this.error = error.message || '加载保存的查询失败，请稍后重试'
      }
    },
    
    // 过滤配置
    filterConfigs() {
      if (!this.searchQuery) {
        this.filteredConfigs = this.configs
        return
      }
      
      const search = this.searchQuery.toLowerCase()
      this.filteredConfigs = this.configs.filter(config => 
        config.name.toLowerCase().includes(search) || 
        (config.description && config.description.toLowerCase().includes(search))
      )
    },
    
    // 刷新配置
    refreshConfigs() {
      this.loadConfigs()
    },
    
    // 获取数据源名称
    getDataSourceName(queryId) {
      const query = this.savedQueries.find(q => q.id === queryId)
      if (!query) return '未知查询'
      
      const dataSource = query.dataSourceName || `数据源 #${query.dataSourceId}`
      return `${query.name} (${dataSource})`
    },
    
    // 获取显示类型文本
    getDisplayType(type) {
      switch (type) {
        case 'table': return '表格'
        case 'card': return '卡片'
        case 'list': return '列表'
        default: return type || '未设置'
      }
    },
    
    // 格式化日期
    formatDate(dateString) {
      if (!dateString) return '未知时间'
      return new Date(dateString).toLocaleString()
    },
    
    // 编辑配置
    editConfig(config) {
      this.editingConfig = config
      this.configForm = JSON.parse(JSON.stringify(config))
      this.activeConfigTab = 'form'
    },
    
    // 删除配置
    async deleteConfig(config) {
      if (!confirm(`确定要删除配置 "${config.name}" 吗？`)) return
      
      try {
        // 使用API服务删除配置
        await LowCodeService.deleteConfig(config.id)
        
        // 从列表中移除
        this.configs = this.configs.filter(c => c.id !== config.id)
        this.filterConfigs()
        
        alert('配置已删除')
      } catch (error) {
        console.error('删除配置失败:', error)
        alert('删除配置失败: ' + (error.message || '未知错误'))
      }
    },
    
    // 预览配置
    async previewConfig(config) {
      this.previewingConfig = config
      
      try {
        // 使用API服务渲染配置
        const result = await LowCodeService.renderConfig(config.id, {})
        this.previewResult = result
      } catch (error) {
        console.error('预览配置失败:', error)
        this.previewError = error.message || '预览配置失败，请稍后重试'
      }
    },
    
    // 复制嵌入代码
    copyEmbedCode(config) {
      const embedCode = `<iframe src="${window.location.origin}/api/v1/lowcode/configs/${config.id}/embed" width="100%" height="600" frameborder="0"></iframe>`
      
      navigator.clipboard.writeText(embedCode)
        .then(() => {
          alert('嵌入代码已复制到剪贴板')
        })
        .catch(err => {
          console.error('复制失败:', err)
          alert('复制失败，请手动复制')
        })
    },
    
    // 添加表单字段
    addFormField() {
      if (!this.configForm.formConfig.fields) {
        this.configForm.formConfig.fields = []
      }
      
      this.configForm.formConfig.fields.push({
        id: '',
        label: '',
        type: 'text',
        required: false,
        defaultValue: ''
      })
    },
    
    // 移除表单字段
    removeFormField(index) {
      this.configForm.formConfig.fields.splice(index, 1)
    },
    
    // 添加选项
    addOption(field) {
      if (!field.options) {
        field.options = []
      }
      
      field.options.push({
        label: '',
        value: ''
      })
    },
    
    // 移除选项
    removeOption(field, index) {
      field.options.splice(index, 1)
    },
    
    // 添加列
    addColumn() {
      if (!this.configForm.displayConfig.columns) {
        this.configForm.displayConfig.columns = []
      }
      
      this.configForm.displayConfig.columns.push({
        field: '',
        header: '',
        width: 150,
        sortable: true
      })
    },
    
    // 移除列
    removeColumn(index) {
      this.configForm.displayConfig.columns.splice(index, 1)
    },
    
    // 生成AI配置
    async generateAIConfig() {
      if (!this.configForm.queryId || !this.aiDescription) {
        alert('请选择查询并填写需求描述')
        return
      }
      
      try {
        // 使用API服务获取AI建议
        const suggestion = await LowCodeService.getAISuggestion(
          this.configForm.queryId,
          this.aiPreferredDisplayType,
          this.aiDescription
        )
        
        this.aiSuggestion = suggestion
      } catch (error) {
        console.error('获取AI建议失败:', error)
        alert('获取AI建议失败: ' + (error.message || '未知错误'))
      }
    },
    
    // 应用AI建议
    applyAISuggestion() {
      if (!this.aiSuggestion) return
      
      // 应用表单配置
      if (this.aiSuggestion.formConfig) {
        this.configForm.formConfig = this.aiSuggestion.formConfig
      }
      
      // 应用显示配置
      if (this.aiSuggestion.displayConfig) {
        this.configForm.displayConfig = this.aiSuggestion.displayConfig
      }
      
      // 应用图表配置
      if (this.aiSuggestion.chartConfig) {
        this.configForm.chartConfig = this.aiSuggestion.chartConfig
      }
      
      alert('AI建议已应用')
    },
    
    // 保存配置
    async saveConfig() {
      // 验证表单
      if (!this.configForm.name) {
        alert('请输入配置名称')
        return
      }
      
      if (!this.configForm.queryId) {
        alert('请选择关联查询')
        return
      }
      
      try {
        if (this.editingConfig) {
          // 更新配置
          await LowCodeService.updateConfig(this.editingConfig.id, this.configForm)
          
          // 更新列表
          const index = this.configs.findIndex(c => c.id === this.editingConfig.id)
          if (index !== -1) {
            this.configs[index] = { ...this.editingConfig, ...this.configForm }
          }
          
          alert('配置已更新')
        } else {
          // 创建配置
          const newConfig = await LowCodeService.createConfig(this.configForm)
          
          // 添加到列表
          this.configs.push(newConfig)
          
          alert('配置已创建')
        }
        
        // 重置表单和状态
        this.cancelConfigEdit()
        
        // 刷新列表
        this.filterConfigs()
      } catch (error) {
        console.error('保存配置失败:', error)
        alert('保存配置失败: ' + (error.message || '未知错误'))
      }
    },
    
    // 取消编辑
    cancelConfigEdit() {
      this.showCreateConfigModal = false
      this.editingConfig = null
      this.configForm = {
        name: '',
        description: '',
        queryId: '',
        formConfig: {
          layout: 'standard',
          fields: []
        },
        displayConfig: {
          type: 'table',
          columns: []
        },
        chartConfig: {
          type: 'bar',
          title: '',
          xField: '',
          yField: '',
          color: '#4c51bf'
        }
      }
      this.activeConfigTab = 'form'
      this.aiDescription = ''
      this.aiPreferredDisplayType = 'table'
      this.aiSuggestion = null
    }
  },
  watch: {
    searchQuery() {
      this.filterConfigs()
    }
  },
  mounted() {
    // 加载配置列表和保存的查询
    this.loadConfigs()
    this.loadSavedQueries()
  }
}
