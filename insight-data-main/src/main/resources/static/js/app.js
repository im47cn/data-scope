// 定义路由
const routes = [
  { path: '/', component: Dashboard },
  { path: '/dashboard', component: Dashboard },
  { path: '/datasource', component: Datasource },
  { path: '/data-browse', component: DataBrowse },
  { path: '/query-builder', component: QueryBuilder },
  // 其他路由将在实现相应组件后添加
]

// 创建路由实例
const router = VueRouter.createRouter({
  history: VueRouter.createWebHashHistory(),
  routes
})

// 创建Vue应用
const app = Vue.createApp({
  data() {
    return {
      // 全局状态
      user: {
        name: '管理员',
        avatar: 'https://randomuser.me/api/portraits/men/1.jpg',
        role: 'admin'
      },
      sidebarCollapsed: false,
      darkMode: false
    }
  },
  methods: {
    toggleSidebar() {
      this.sidebarCollapsed = !this.sidebarCollapsed
    },
    toggleDarkMode() {
      this.darkMode = !this.darkMode
      document.documentElement.classList.toggle('dark', this.darkMode)
    }
  },
  mounted() {
    // 检查本地存储中的暗黑模式设置
    const savedDarkMode = localStorage.getItem('darkMode') === 'true'
    if (savedDarkMode) {
      this.darkMode = true
      document.documentElement.classList.add('dark')
    }
  },
  watch: {
    darkMode(newValue) {
      // 保存暗黑模式设置到本地存储
      localStorage.setItem('darkMode', newValue)
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