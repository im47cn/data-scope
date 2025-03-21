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
      darkMode: false, // 兼容旧代码
      themeColor: 'indigo', // 默认主题颜色
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
      this.applyTheme(this.darkMode)
    },
    // 应用主题
    applyTheme(isDarkMode) {
      // 使用 data-theme 属性设置主题
      document.documentElement.setAttribute('data-theme', isDarkMode ? 'dark' : 'light')
      // 兼容旧代码
      document.documentElement.classList.toggle('dark', isDarkMode)
    },
    // 应用主题颜色
    applyThemeColor(colorName) {
      const root = document.documentElement
      const colorMap = {
        'indigo': {
          primary: '#4c51bf',
          hover: '#434190'
        },
        'blue': {
          primary: '#3182ce',
          hover: '#2c5282'
        },
        'green': {
          primary: '#38a169',
          hover: '#2f855a'
        },
        'red': {
          primary: '#e53e3e',
          hover: '#c53030'
        },
        'purple': {
          primary: '#805ad5',
          hover: '#6b46c1'
        }
      }
      
      const colorValues = colorMap[colorName] || colorMap['indigo']
      
      // 设置主色调变量
      root.style.setProperty('--color-primary', colorValues.primary)
      root.style.setProperty('--color-primary-hover', colorValues.hover)
      
      // 兼容旧变量名
      root.style.setProperty('--primary-color', colorValues.primary)
      root.style.setProperty('--primary-hover-color', colorValues.hover)
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
    const savedTheme = localStorage.getItem('theme')
    if (savedTheme) {
      this.darkMode = savedTheme === 'dark'
      this.applyTheme(this.darkMode)
    } else {
      // 兼容旧版本的设置
      const savedDarkMode = localStorage.getItem('darkMode') === 'true'
      if (savedDarkMode) {
        this.darkMode = true
        this.applyTheme(true)
      }
    }
    
    // 检查本地存储中的主题颜色设置
    const savedColor = localStorage.getItem('color')
    if (savedColor) {
      this.themeColor = savedColor
      this.applyThemeColor(this.themeColor)
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
      localStorage.setItem('theme', newValue ? 'dark' : 'light')
      localStorage.setItem('darkMode', newValue) // 兼容旧版本
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