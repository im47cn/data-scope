// 系统设置页面组件
export const Settings = {
  template: `
    <div class="p-6">
      <div class="mb-6">
        <h2 class="text-2xl font-bold text-gray-800">系统设置</h2>
        <p class="text-gray-600">管理系统配置和个人偏好设置</p>
      </div>
      
      <!-- 设置选项卡 -->
      <div class="bg-white rounded-lg shadow mb-6">
        <div class="border-b border-gray-200">
          <nav class="flex -mb-px">
            <button 
              v-for="tab in tabs" 
              :key="tab.id" 
              @click="activeTab = tab.id"
              class="py-4 px-6 font-medium text-sm border-b-2 focus:outline-none"
              :class="activeTab === tab.id ? 'border-primary text-primary' : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'"
            >
              <i :class="tab.icon + ' mr-2'"></i>
              {{ tab.name }}
            </button>
          </nav>
        </div>
        
        <!-- 个人设置 -->
        <div v-if="activeTab === 'personal'" class="p-6">
          <div class="max-w-3xl">
            <h3 class="text-lg font-medium text-gray-900 mb-6">个人设置</h3>
            
            <!-- 个人信息 -->
            <div class="mb-8">
              <h4 class="text-sm font-medium text-gray-700 mb-4">个人信息</h4>
              <div class="flex items-start mb-6">
                <div class="mr-6">
                  <div class="w-24 h-24 rounded-full bg-gray-200 overflow-hidden">
                    <img :src="user.avatar" alt="用户头像" class="w-full h-full object-cover">
                  </div>
                  <button class="mt-2 text-sm text-indigo-600 hover:text-indigo-800">
                    更换头像
                  </button>
                </div>
                <div class="flex-1 space-y-4">
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">用户名</label>
                    <input 
                      v-model="user.name" 
                      type="text" 
                      class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                    >
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">电子邮箱</label>
                    <input 
                      v-model="user.email" 
                      type="email" 
                      class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                    >
                  </div>
                </div>
              </div>
            </div>
            
            <!-- 界面偏好 -->
            <div class="mb-8">
              <h4 class="text-sm font-medium text-gray-700 mb-4">界面偏好</h4>
              <div class="space-y-4">
                <div class="flex items-center justify-between">
                  <div>
                    <h5 class="font-medium text-gray-900">暗黑模式</h5>
                    <p class="text-sm text-gray-500">启用暗黑模式以减少眼睛疲劳</p>
                  </div>
                  <div class="relative inline-block w-12 h-6 rounded-full bg-gray-200">
                    <input 
                      type="checkbox" 
                      id="dark-mode-toggle" 
                      v-model="preferences.darkMode"
                      class="sr-only"
                    >
                    <label 
                      for="dark-mode-toggle" 
                      class="absolute inset-0 rounded-full cursor-pointer"
                      :class="preferences.darkMode ? 'bg-primary' : 'bg-gray-200'"
                    >
                      <span 
                        class="absolute inset-y-0 left-0 w-6 h-6 bg-white rounded-full shadow transform transition-transform"
                        :class="preferences.darkMode ? 'translate-x-6' : 'translate-x-0'"
                      ></span>
                    </label>
                  </div>
                </div>
                
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">主题颜色</label>
                  <div class="flex space-x-2">
                    <button 
                      v-for="color in themeColors" 
                      :key="color.id" 
                      @click="preferences.themeColor = color.id"
                      class="w-8 h-8 rounded-full border-2 focus:outline-none"
                      :class="[
                        color.class, 
                        preferences.themeColor === color.id ? 'ring-2 ring-offset-2 ring-gray-400' : ''
                      ]"
                    ></button>
                  </div>
                </div>
              </div>
            </div>
            
            <div class="flex justify-end">
              <button @click="savePersonalSettings" class="bg-primary hover:bg-indigo-700 text-white py-2 px-4 rounded-lg flex items-center transition-all">
                <i class="fas fa-save mr-2"></i>
                <span>保存设置</span>
              </button>
            </div>
          </div>
        </div>
        
        <!-- 系统设置 -->
        <div v-if="activeTab === 'system'" class="p-6">
          <div class="max-w-3xl">
            <h3 class="text-lg font-medium text-gray-900 mb-6">系统设置</h3>
            
            <!-- 系统配置 -->
            <div class="mb-8">
              <h4 class="text-sm font-medium text-gray-700 mb-4">系统配置</h4>
              <div class="space-y-4">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">系统名称</label>
                  <input 
                    v-model="systemSettings.name" 
                    type="text" 
                    class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                  >
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">系统描述</label>
                  <textarea 
                    v-model="systemSettings.description" 
                    rows="3"
                    class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                  ></textarea>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">默认语言</label>
                  <select 
                    v-model="systemSettings.language" 
                    class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary"
                  >
                    <option value="zh-CN">简体中文</option>
                    <option value="en-US">English (US)</option>
                    <option value="ja-JP">日本語</option>
                  </select>
                </div>
              </div>
            </div>
            
            <div class="flex justify-end">
              <button @click="saveSystemSettings" class="bg-primary hover:bg-indigo-700 text-white py-2 px-4 rounded-lg flex items-center transition-all">
                <i class="fas fa-save mr-2"></i>
                <span>保存设置</span>
              </button>
            </div>
          </div>
        </div>
        
        <!-- 用户管理 -->
        <div v-if="activeTab === 'users'" class="p-6">
          <div class="max-w-5xl">
            <div class="flex justify-between items-center mb-6">
              <h3 class="text-lg font-medium text-gray-900">用户管理</h3>
              <button @click="showAddUserModal = true" class="bg-primary hover:bg-indigo-700 text-white py-2 px-4 rounded-lg flex items-center transition-all">
                <i class="fas fa-user-plus mr-2"></i>
                <span>添加用户</span>
              </button>
            </div>
            
            <!-- 用户列表 -->
            <div class="bg-white overflow-hidden">
              <div class="overflow-x-auto">
                <table class="min-w-full divide-y divide-gray-200">
                  <thead>
                    <tr>
                      <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">用户</th>
                      <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">电子邮箱</th>
                      <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">角色</th>
                      <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
                      <th class="px-6 py-3 bg-gray-50 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
                    </tr>
                  </thead>
                  <tbody class="bg-white divide-y divide-gray-200">
                    <tr v-for="user in users" :key="user.id">
                      <td class="px-6 py-4 whitespace-nowrap">
                        <div class="flex items-center">
                          <div class="h-10 w-10 rounded-full overflow-hidden bg-gray-100">
                            <img :src="user.avatar" alt="" class="h-full w-full object-cover">
                          </div>
                          <div class="ml-4">
                            <div class="text-sm font-medium text-gray-900">{{ user.name }}</div>
                            <div class="text-sm text-gray-500">{{ user.title }}</div>
                          </div>
                        </div>
                      </td>
                      <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        {{ user.email }}
                      </td>
                      <td class="px-6 py-4 whitespace-nowrap">
                        <span class="px-2 py-1 text-xs rounded-full" :class="getRoleClass(user.role)">
                          {{ getRoleText(user.role) }}
                        </span>
                      </td>
                      <td class="px-6 py-4 whitespace-nowrap">
                        <span class="px-2 py-1 text-xs rounded-full" :class="user.active ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'">
                          {{ user.active ? '启用' : '禁用' }}
                        </span>
                      </td>
                      <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                        <button @click="editUser(user)" class="text-indigo-600 hover:text-indigo-900 mr-3">
                          编辑
                        </button>
                        <button @click="toggleUserStatus(user)" class="text-indigo-600 hover:text-indigo-900 mr-3">
                          {{ user.active ? '禁用' : '启用' }}
                        </button>
                        <button @click="deleteUser(user)" class="text-red-600 hover:text-red-900">
                          删除
                        </button>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  data() {
    return {
      activeTab: 'personal',
      tabs: [
        { id: 'personal', name: '个人设置', icon: 'fas fa-user-cog' },
        { id: 'system', name: '系统设置', icon: 'fas fa-cogs' },
        { id: 'users', name: '用户管理', icon: 'fas fa-users' }
      ],
      user: {
        name: '管理员',
        email: 'admin@example.com',
        title: '系统管理员',
        avatar: 'https://randomuser.me/api/portraits/men/1.jpg'
      },
      preferences: {
        darkMode: false,
        themeColor: 'indigo',
        notifications: {
          queryComplete: true,
          dataUpdate: true,
          system: true
        }
      },
      themeColors: [
        { id: 'indigo', class: 'bg-indigo-600' },
        { id: 'blue', class: 'bg-blue-600' },
        { id: 'green', class: 'bg-green-600' },
        { id: 'red', class: 'bg-red-600' },
        { id: 'purple', class: 'bg-purple-600' }
      ],
      systemSettings: {
        name: '数据洞察平台',
        description: '企业级数据分析和可视化平台',
        language: 'zh-CN',
        timezone: 'Asia/Shanghai'
      },
      users: [
        {
          id: 1,
          name: '管理员',
          email: 'admin@example.com',
          title: '系统管理员',
          role: 'admin',
          active: true,
          avatar: 'https://randomuser.me/api/portraits/men/1.jpg'
        },
        {
          id: 2,
          name: '张三',
          email: 'zhangsan@example.com',
          title: '数据分析师',
          role: 'editor',
          active: true,
          avatar: 'https://randomuser.me/api/portraits/men/2.jpg'
        },
        {
          id: 3,
          name: '李四',
          email: 'lisi@example.com',
          title: '市场经理',
          role: 'viewer',
          active: true,
          avatar: 'https://randomuser.me/api/portraits/women/3.jpg'
        }
      ],
      showAddUserModal: false
    }
  },
  methods: {
    savePersonalSettings() {
      alert('个人设置已保存')
      this.applyTheme(this.preferences.darkMode)
      this.applyThemeColor(this.preferences.themeColor)
      this.savePreferencesToLocalStorage()
    },
    
    saveSystemSettings() {
      alert('系统设置已保存')
    },
    
    getRoleClass(role) {
      switch (role) {
        case 'admin': return 'bg-red-100 text-red-800'
        case 'editor': return 'bg-blue-100 text-blue-800'
        case 'viewer': return 'bg-green-100 text-green-800'
        default: return 'bg-gray-100 text-gray-800'
      }
    },
    
    getRoleText(role) {
      switch (role) {
        case 'admin': return '管理员'
        case 'editor': return '编辑者'
        case 'viewer': return '查看者'
        default: return role
      }
    },
    
    editUser(user) {
      alert(`编辑用户: ${user.name}`)
    },
    
    toggleUserStatus(user) {
      user.active = !user.active
      alert(`用户 ${user.name} 已${user.active ? '启用' : '禁用'}`)
    },
    
    deleteUser(user) {
      if (confirm(`确定要删除用户 ${user.name} 吗？`)) {
        this.users = this.users.filter(u => u.id !== user.id)
      }
    },
    
    // 主题和颜色相关方法
    applyTheme(isDarkMode) {
      const htmlElement = document.documentElement
      
      if (isDarkMode) {
        htmlElement.setAttribute('data-theme', 'dark')
        this.applyDarkModeStyles()
      } else {
        htmlElement.setAttribute('data-theme', 'light')
        this.applyLightModeStyles()
      }
    },
    
    applyLightModeStyles() {
      const root = document.documentElement
      
      // 设置浅色模式的颜色变量
      root.style.setProperty('--color-text', '#1a202c')
      root.style.setProperty('--color-text-secondary', '#4a5568')
      root.style.setProperty('--color-background', '#f7fafc')
      root.style.setProperty('--color-background-secondary', '#ffffff')
      root.style.setProperty('--color-border', '#e2e8f0')
      root.style.setProperty('--color-sidebar', '#1a202c')
      root.style.setProperty('--color-sidebar-text', '#ffffff')
    },
    
    applyDarkModeStyles() {
      const root = document.documentElement
      
      // 设置深色模式的颜色变量
      root.style.setProperty('--color-text', '#f7fafc')
      root.style.setProperty('--color-text-secondary', '#cbd5e0')
      root.style.setProperty('--color-background', '#1a202c')
      root.style.setProperty('--color-background-secondary', '#2d3748')
      root.style.setProperty('--color-border', '#4a5568')
      root.style.setProperty('--color-sidebar', '#0f1521')
      root.style.setProperty('--color-sidebar-text', '#f7fafc')
    },
    
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
    },
    
    savePreferencesToLocalStorage() {
      localStorage.setItem('theme', this.preferences.darkMode ? 'dark' : 'light')
      localStorage.setItem('color', this.preferences.themeColor)
    },
    
    loadPreferencesFromLocalStorage() {
      const savedTheme = localStorage.getItem('theme')
      const savedColor = localStorage.getItem('color')
      
      if (savedTheme) {
        this.preferences.darkMode = savedTheme === 'dark'
      }
      
      if (savedColor) {
        this.preferences.themeColor = savedColor
      }
      
      // 应用保存的设置
      this.applyTheme(this.preferences.darkMode)
      this.applyThemeColor(this.preferences.themeColor)
    }
  },
  
  mounted() {
    // 在组件挂载时加载保存的设置
    this.loadPreferencesFromLocalStorage()
    
    // 监听暗黑模式切换
    this.$watch('preferences.darkMode', (newValue) => {
      this.applyTheme(newValue)
    })
    
    // 监听主题颜色切换
    this.$watch('preferences.themeColor', (newValue) => {
      this.applyThemeColor(newValue)
    })
  }
}
