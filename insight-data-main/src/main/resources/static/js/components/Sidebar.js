// 侧边栏组件
export const Sidebar = {
  template: `
    <div class="sidebar" :class="{'collapsed': collapsed}">
      <div class="sidebar-header">
        <div class="logo-container">
          <img src="/images/logo.png" alt="Logo" class="logo">
          <span v-if="!collapsed" class="logo-text">数据洞察平台</span>
        </div>
        <button @click="$emit('toggle-collapse')" class="collapse-btn">
          <i :class="collapsed ? 'fas fa-angle-right' : 'fas fa-angle-left'"></i>
        </button>
      </div>
      
      <div class="sidebar-content">
        <nav class="sidebar-nav">
          <router-link 
            v-for="item in menuItems" 
            :key="item.path" 
            :to="item.path" 
            class="nav-item"
            :class="{ 'active': isActive(item.path) }"
          >
            <div class="nav-icon">
              <i :class="item.icon"></i>
            </div>
            <span v-if="!collapsed" class="nav-text">{{ item.title }}</span>
          </router-link>
        </nav>
      </div>
    </div>
  `,
  props: {
    collapsed: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      menuItems: [
        { path: '/dashboard', title: '仪表盘', icon: 'fas fa-tachometer-alt' },
        { path: '/datasource', title: '数据源管理', icon: 'fas fa-database' },
        { path: '/data-browse', title: '数据浏览', icon: 'fas fa-table' },
        { path: '/query-builder', title: '查询构建器', icon: 'fas fa-edit' },
        { path: '/nl-query', title: '自然语言查询', icon: 'fas fa-comment' },
        { path: '/low-code', title: '低代码集成', icon: 'fas fa-code' },
        { path: '/query-history', title: '查询历史', icon: 'fas fa-history' },
        { path: '/settings', title: '系统设置', icon: 'fas fa-cog' }
      ]
    }
  },
  methods: {
    isActive(path) {
      return this.$route.path === path || (path !== '/' && this.$route.path.startsWith(path))
    }
  }
}