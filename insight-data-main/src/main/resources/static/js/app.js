// 导入 Vue 和 Vue Router
const { createApp } = Vue
const { createRouter, createWebHashHistory } = VueRouter

// 导入API服务
import { ApiService, DataSourceService, MetadataService, QueryService, NLQueryService, LowCodeService } from './services/api.js';


import { Navbar } from './components/Navbar.js';
import { Sidebar } from './components/Sidebar.js';
import { Dashboard } from './views/Dashboard.js';
import { Datasource } from './views/Datasource.js';
import { DataBrowse } from './views/DataBrowse.js';
import { QueryBuilder } from './views/QueryBuilder.js';
import { NLQuery } from './views/NLQuery.js';
import { LowCode } from './views/LowCode.js';
import { QueryHistory } from './views/QueryHistory.js';
import { Settings } from './views/Settings.js';

// 定义路由
const routes = [
  { path: '/', component: Dashboard },
  { path: '/dashboard', component: Dashboard },
  { path: '/datasource', component: Datasource },
  { path: '/data-browse', component: DataBrowse },
  { path: '/query-builder', component: QueryBuilder },
  { path: '/nl-query', component: NLQuery },
  { path: '/low-code', component: LowCode },
  { path: '/query-history', component: QueryHistory },
  { path: '/settings', component: Settings }
]

// 创建路由实例
const router = createRouter({
  history: createWebHashHistory(),
  routes
})

// 创建Vue应用
const app = createApp({
  data() {
    return {
      // 全局状态
      user: {
        name: '管理员',
        avatar: 'https://randomuser.me/api/portraits/men/1.jpg',
        role: 'admin'
      },
      sidebarCollapsed: false,
      darkMode: false,
      loading: false,
      error: null
    }
  },
  methods: {
    toggleSidebar() {
      this.sidebarCollapsed = !this.sidebarCollapsed
    },
    toggleDarkMode() {
      this.darkMode = !this.darkMode
      document.documentElement.classList.toggle('dark', this.darkMode)
    },
    // 全局错误处理
    handleApiError(error) {
      console.error('API错误:', error)
      
      let errorMessage = '操作失败'
      
      if (error.message) {
        errorMessage = error.message
      } else if (error.code) {
        // 根据错误代码显示友好的错误消息
        switch (error.code) {
          case 'DATASOURCE_NOT_FOUND':
            errorMessage = '数据源不存在'
            break
          case 'DATASOURCE_CONNECTION_FAILED':
            errorMessage = '数据源连接失败'
            break
          case 'QUERY_SYNTAX_ERROR':
            errorMessage = 'SQL语法错误'
            break
          case 'AUTHENTICATION_FAILED':
            errorMessage = '认证失败，请重新登录'
            // 可能需要重定向到登录页面
            break
          default:
            errorMessage = `操作失败: ${error.code}`
        }
      }
      
      // 显示错误消息
      this.error = errorMessage
      
      // 5秒后自动清除错误消息
      setTimeout(() => {
        if (this.error === errorMessage) {
          this.error = null
        }
      }, 5000)
    },
    // 显示加载状态
    showLoading() {
      this.loading = true
    },
    // 隐藏加载状态
    hideLoading() {
      this.loading = false
    }
  },
  mounted() {
    // 检查本地存储中的暗黑模式设置
    const savedDarkMode = localStorage.getItem('darkMode') === 'true'
    if (savedDarkMode) {
      this.darkMode = true
      document.documentElement.classList.add('dark')
    }
    
    // 全局API错误处理
    window.addEventListener('unhandledrejection', (event) => {
      if (event.reason && (event.reason.status || event.reason.code)) {
        this.handleApiError(event.reason)
        event.preventDefault()
      }
    })
  },
  watch: {
    darkMode(newValue) {
      // 保存暗黑模式设置到本地存储
      localStorage.setItem('darkMode', newValue)
    }
  },
  provide() {
    return {
      // 提供API服务给所有组件
      apiService: ApiService,
      dataSourceService: DataSourceService,
      metadataService: MetadataService,
      queryService: QueryService,
      nlQueryService: NLQueryService,
      lowCodeService: LowCodeService,
      
      // 提供全局方法
      handleApiError: this.handleApiError,
      showLoading: this.showLoading,
      hideLoading: this.hideLoading
    }
  }
})

// 注册全局组件
app.component('Navbar', Navbar)
app.component('Sidebar', Sidebar)

// 使用路由
app.use(router)

// 挂载应用
app.mount('#app')