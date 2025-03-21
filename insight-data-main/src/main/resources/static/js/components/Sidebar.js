// 侧边栏组件
const Sidebar = {
  template: `
    <div class="h-full bg-white border-r border-gray-200 flex flex-col" :class="{'w-64': !collapsed, 'w-16': collapsed}">
      <!-- Logo区域 -->
      <div class="flex items-center justify-center h-16 border-b border-gray-200">
        <div v-if="!collapsed" class="text-xl font-bold text-primary">数据洞察平台</div>
        <div v-else class="text-xl font-bold text-primary">DI</div>
      </div>
      
      <!-- 导航菜单 -->
      <nav class="flex-1 overflow-y-auto py-4">
        <ul>
          <li v-for="item in menuItems" :key="item.path" class="mb-1">
            <router-link 
              :to="item.path" 
              class="flex items-center px-4 py-2 text-gray-700 hover:bg-gray-100 hover:text-primary transition-colors"
              :class="{ 'bg-indigo-50 text-primary': isActive(item.path) }"
            >
              <i :class="item.icon + ' text-lg'" :class="{ 'text-primary': isActive(item.path) }"></i>
              <span v-if="!collapsed" class="ml-3">{{ item.title }}</span>
            </router-link>
          </li>
        </ul>
      </nav>
      
      <!-- 底部操作区 -->
      <div class="p-4 border-t border-gray-200">
        <button 
          @click="toggleCollapse" 
          class="w-full flex items-center justify-center p-2 text-gray-600 hover:text-primary hover:bg-gray-100 rounded transition-colors"
        >
          <i :class="collapsed ? 'fas fa-angle-right' : 'fas fa-angle-left'" class="text-lg"></i>
          <span v-if="!collapsed" class="ml-2">收起菜单</span>
        </button>
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
        { path: '/query-builder', title: '查询构建器', icon: 'fas fa-search' },
        { path: '/nl-query', title: '自然语言查询', icon: 'fas fa-comment' },
        { path: '/low-code', title: '低代码集成', icon: 'fas fa-code' },
        { path: '/query-history', title: '查询历史', icon: 'fas fa-history' },
        { path: '/settings', title: '系统设置', icon: 'fas fa-cog' }
      ]
    }
  },
  methods: {
    isActive(path) {
      return this.$route.path === path || 
        (this.$route.path === '/' && path === '/dashboard')
    },
    toggleCollapse() {
      this.$emit('toggle-collapse')
    }
  }
}