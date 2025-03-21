// 顶部导航栏组件
const Navbar = {
  template: `
    <header class="bg-white shadow-sm z-10">
      <div class="flex items-center justify-between p-4">
        <div class="flex items-center">
          <button class="text-gray-600 md:hidden" @click="toggleSidebar">
            <i class="fas fa-bars text-xl"></i>
          </button>
          <h2 class="text-xl font-semibold ml-4">{{ pageTitle }}</h2>
        </div>
        <div class="flex items-center">
          <div class="relative mr-4">
            <input type="text" placeholder="搜索..." class="bg-gray-100 rounded-full py-2 px-4 pl-10 focus:outline-none focus:ring-2 focus:ring-primary focus:bg-white transition-all">
            <i class="fas fa-search absolute left-3 top-3 text-gray-500"></i>
          </div>
          <div class="relative mr-4">
            <button class="relative p-2 rounded-full hover:bg-gray-100 transition-all">
              <i class="fas fa-bell text-gray-600"></i>
              <span class="absolute top-0 right-0 w-2 h-2 bg-red-500 rounded-full"></span>
            </button>
          </div>
          <div class="flex items-center">
            <img :src="user.avatar" alt="用户头像" class="w-8 h-8 rounded-full mr-2">
            <span class="text-gray-700">{{ user.name }}</span>
          </div>
        </div>
      </div>
    </header>
  `,
  computed: {
    user() {
      return this.$root.user
    },
    pageTitle() {
      const routeMap = {
        '/': '仪表盘',
        '/dashboard': '仪表盘',
        '/datasource': '数据源管理',
        '/data-browse': '数据浏览',
        '/query-builder': '查询构建器',
        '/nl-query': '自然语言查询',
        '/low-code': '低代码集成',
        '/query-history': '查询历史',
        '/settings': '系统设置'
      }
      return routeMap[this.$route.path] || '数据洞察平台'
    }
  },
  methods: {
    toggleSidebar() {
      // 在移动端切换侧边栏显示
      // 这里需要与实际的侧边栏显示逻辑结合
      console.log('Toggle sidebar')
    }
  }
}