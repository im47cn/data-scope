// 顶部导航栏组件
const Navbar = {
  template: `
    <div class="bg-white border-b border-gray-200 h-16 flex items-center justify-between px-6">
      <!-- 左侧 Logo 和菜单折叠按钮 -->
      <div class="flex items-center">
        <button @click="$emit('toggle-sidebar')" class="text-gray-500 hover:text-gray-700 focus:outline-none mr-4">
          <i class="fas fa-bars text-lg"></i>
        </button>
        <div class="text-xl font-bold text-primary hidden md:block">数据洞察平台</div>
      </div>
      
      <!-- 右侧搜索框和用户菜单 -->
      <div class="flex items-center">
        <!-- 搜索框 -->
        <div class="relative mr-4">
          <input 
            type="text" 
            placeholder="搜索..." 
            class="w-64 border border-gray-300 rounded-lg px-3 py-2 pl-10 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
          >
          <i class="fas fa-search absolute left-3 top-3 text-gray-400"></i>
        </div>
        
        <!-- 通知图标 -->
        <div class="relative mr-4">
          <button class="text-gray-500 hover:text-gray-700 focus:outline-none">
            <i class="fas fa-bell text-lg"></i>
            <span class="absolute -top-1 -right-1 bg-red-500 text-white text-xs rounded-full h-4 w-4 flex items-center justify-center">3</span>
          </button>
        </div>
        
        <!-- 用户菜单 -->
        <div class="relative">
          <button @click="toggleUserMenu" class="flex items-center focus:outline-none">
            <div class="h-8 w-8 rounded-full overflow-hidden bg-gray-200 mr-2">
              <img :src="user.avatar" alt="用户头像" class="h-full w-full object-cover">
            </div>
            <span class="hidden md:block text-sm font-medium text-gray-700">{{ user.name }}</span>
            <i class="fas fa-chevron-down text-xs ml-2 text-gray-500"></i>
          </button>
          
          <!-- 下拉菜单 -->
          <div v-if="showUserMenu" class="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-10">
            <a href="#" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">
              <i class="fas fa-user mr-2"></i> 个人资料
            </a>
            <a href="#" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">
              <i class="fas fa-cog mr-2"></i> 设置
            </a>
            <div class="border-t border-gray-100 my-1"></div>
            <a href="#" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">
              <i class="fas fa-sign-out-alt mr-2"></i> 退出登录
            </a>
          </div>
        </div>
      </div>
    </div>
  `,
  props: {
    user: {
      type: Object,
      default: () => ({
        name: '管理员',
        avatar: 'https://randomuser.me/api/portraits/men/1.jpg'
      })
    }
  },
  data() {
    return {
      showUserMenu: false
    }
  },
  methods: {
    toggleUserMenu() {
      this.showUserMenu = !this.showUserMenu
    }
  },
  mounted() {
    // 点击页面其他地方关闭用户菜单
    document.addEventListener('click', (e) => {
      if (!this.$el.contains(e.target)) {
        this.showUserMenu = false
      }
    })
  }
}