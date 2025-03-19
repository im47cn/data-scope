// 定义App组件
const App = {
  template: `
    <div id="app">
      <router-view></router-view>
    </div>
  `,
  name: 'App'
};

// 定义AppLayout组件
const AppLayout = {
  template: `
    <div class="app-layout">
      <!-- 侧边栏 -->
      <div class="sidebar" :class="{ 'collapsed': sidebarCollapsed }">
        <div class="sidebar-header">
          <div class="logo">
            <img src="/img/logo.png" alt="Logo" class="logo-img">
            <h1 v-if="!sidebarCollapsed" class="logo-text">Data Scope</h1>
          </div>
          <button class="collapse-button" @click="toggleSidebar">
            <i :class="sidebarCollapsed ? 'fas fa-angle-right' : 'fas fa-angle-left'"></i>
          </button>
        </div>
        <div class="sidebar-content">
          <nav class="sidebar-nav">
            <router-link to="/dashboard" class="nav-item" active-class="active">
              <i class="fas fa-tachometer-alt"></i>
              <span v-if="!sidebarCollapsed">仪表盘</span>
            </router-link>
            <router-link to="/datasource" class="nav-item" active-class="active">
              <i class="fas fa-database"></i>
              <span v-if="!sidebarCollapsed">数据源管理</span>
            </router-link>
            <router-link to="/data-browse" class="nav-item" active-class="active">
              <i class="fas fa-table"></i>
              <span v-if="!sidebarCollapsed">数据浏览</span>
            </router-link>
            <router-link to="/query-builder" class="nav-item" active-class="active">
              <i class="fas fa-search"></i>
              <span v-if="!sidebarCollapsed">查询构建器</span>
            </router-link>
            <router-link to="/nl-query" class="nav-item" active-class="active">
              <i class="fas fa-comment"></i>
              <span v-if="!sidebarCollapsed">自然语言查询</span>
            </router-link>
            <router-link to="/low-code" class="nav-item" active-class="active">
              <i class="fas fa-code"></i>
              <span v-if="!sidebarCollapsed">低代码集成</span>
            </router-link>
            <router-link to="/query-history" class="nav-item" active-class="active">
              <i class="fas fa-history"></i>
              <span v-if="!sidebarCollapsed">查询历史</span>
            </router-link>
            <router-link to="/settings" class="nav-item" active-class="active">
              <i class="fas fa-cog"></i>
              <span v-if="!sidebarCollapsed">系统设置</span>
            </router-link>
          </nav>
        </div>
        <div class="sidebar-footer">
          <div class="theme-toggle" @click="toggleTheme">
            <i :class="isDarkTheme ? 'fas fa-sun' : 'fas fa-moon'"></i>
            <span v-if="!sidebarCollapsed">{{ isDarkTheme ? '浅色模式' : '深色模式' }}</span>
          </div>
        </div>
      </div>

      <!-- 主内容区 -->
      <div class="main-content" :class="{ 'expanded': sidebarCollapsed }">
        <!-- 顶部导航栏 -->
        <header class="header">
          <div class="header-left">
            <h1 class="page-title">{{ pageTitle }}</h1>
          </div>
          <div class="header-right">
            <div class="search-box">
              <i class="fas fa-search"></i>
              <input type="text" placeholder="搜索..." class="search-input">
            </div>
            <div class="notifications">
              <button class="notification-button">
                <i class="fas fa-bell"></i>
                <span class="notification-badge">3</span>
              </button>
            </div>
            <div class="user-menu">
              <div class="user-info" @click="toggleUserMenu">
                <img src="https://randomuser.me/api/portraits/men/32.jpg" alt="用户头像" class="user-avatar">
                <span class="user-name">纪经理</span>
                <i class="fas fa-chevron-down"></i>
              </div>
              <div class="user-dropdown" v-if="showUserMenu">
                <div class="dropdown-item">
                  <i class="fas fa-user"></i>
                  <span>个人信息</span>
                </div>
                <div class="dropdown-item">
                  <i class="fas fa-cog"></i>
                  <span>账户设置</span>
                </div>
                <div class="dropdown-divider"></div>
                <div class="dropdown-item">
                  <i class="fas fa-sign-out-alt"></i>
                  <span>退出登录</span>
                </div>
              </div>
            </div>
          </div>
        </header>

        <!-- 内容区域 -->
        <main class="content">
          <slot></slot>
        </main>

        <!-- 页脚 -->
        <footer class="footer">
          <div class="footer-left">
            <span>© 2023 InsightData. 版本 1.0.0</span>
          </div>
          <div class="footer-right">
            <a href="#" class="footer-link">帮助</a>
            <a href="#" class="footer-link">隐私政策</a>
            <a href="#" class="footer-link">条款</a>
          </div>
        </footer>
      </div>
    </div>
  `,
  props: {
    pageTitle: {
      type: String,
      default: '仪表盘'
    }
  },
  data() {
    return {
      sidebarCollapsed: false,
      isDarkTheme: false,
      showUserMenu: false
    }
  },
  methods: {
    toggleSidebar() {
      this.sidebarCollapsed = !this.sidebarCollapsed;
    },
    toggleTheme() {
      this.isDarkTheme = !this.isDarkTheme;
      document.body.classList.toggle('dark-theme', this.isDarkTheme);
    },
    toggleUserMenu() {
      this.showUserMenu = !this.showUserMenu;
    }
  },
  mounted() {
    // 检查本地存储中的主题设置
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme === 'dark') {
      this.isDarkTheme = true;
      document.body.classList.add('dark-theme');
    }
    
    // 点击外部关闭用户菜单
    document.addEventListener('click', (event) => {
      const userMenu = this.$el.querySelector('.user-menu');
      if (this.showUserMenu && userMenu && !userMenu.contains(event.target)) {
        this.showUserMenu = false;
      }
    });
  },
  watch: {
    isDarkTheme(newValue) {
      localStorage.setItem('theme', newValue ? 'dark' : 'light');
    }
  }
};

// 定义一个加载组件的函数
const loadComponent = (name) => {
  return {
    template: `
      <app-layout :page-title="'${name}'">
        <div class="component-container">
          <h2>${name}页面</h2>
          <p>此页面正在开发中...</p>
        </div>
      </app-layout>
    `,
    components: {
      AppLayout
    }
  };
};

// 视图组件将通过script标签加载，这里只需要引用它们
// 组件已在各自的文件中注册为全局组件
const DataBrowse = loadComponent('数据浏览');
const QueryBuilder = loadComponent('查询构建器');
const NLQuery = loadComponent('自然语言查询');
const LowCode = loadComponent('低代码集成');
const QueryHistory = loadComponent('查询历史');
const Settings = loadComponent('系统设置');

// 定义路由
const routes = [
  { path: '/', redirect: '/dashboard' },
  { path: '/dashboard', component: Vue.component('dashboard-view') },
  { path: '/datasource', component: Vue.component('datasource') },
  { path: '/data-browse', component: DataBrowse },
  { path: '/query-builder', component: QueryBuilder },
  { path: '/nl-query', component: NLQuery },
  { path: '/low-code', component: LowCode },
  { path: '/query-history', component: QueryHistory },
  { path: '/settings', component: Settings },
  // 捕获所有未匹配的路由，重定向到仪表盘
  { path: '*', redirect: '/dashboard' }
];

// 创建路由实例
const router = new VueRouter({
  mode: 'hash',
  base: '/',
  routes
});

// 创建并挂载根实例
new Vue({
  router,
  render: h => h(App)
}).$mount('#app');