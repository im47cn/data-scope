// 仪表盘页面组件
const Dashboard = {
  template: `
    <div class="p-6">
      <div class="mb-6">
        <h2 class="text-2xl font-bold text-gray-800">欢迎使用数据洞察平台</h2>
        <p class="text-gray-600">查看关键指标和最近活动</p>
      </div>
      
      <!-- 统计卡片 -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center">
            <div class="p-3 rounded-full bg-indigo-100 mr-4">
              <i class="fas fa-database text-indigo-600"></i>
            </div>
            <div>
              <p class="text-gray-500 text-sm">数据源</p>
              <p class="text-2xl font-semibold">{{ stats.datasources }}</p>
            </div>
          </div>
          <div class="mt-4">
            <div class="flex items-center">
              <span class="text-green-500 mr-2"><i class="fas fa-arrow-up"></i> 2</span>
              <span class="text-sm text-gray-600">较上月</span>
            </div>
          </div>
        </div>
        
        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center">
            <div class="p-3 rounded-full bg-blue-100 mr-4">
              <i class="fas fa-table text-blue-600"></i>
            </div>
            <div>
              <p class="text-gray-500 text-sm">数据表</p>
              <p class="text-2xl font-semibold">{{ stats.tables }}</p>
            </div>
          </div>
          <div class="mt-4">
            <div class="flex items-center">
              <span class="text-green-500 mr-2"><i class="fas fa-arrow-up"></i> 15</span>
              <span class="text-sm text-gray-600">较上月</span>
            </div>
          </div>
        </div>
        
        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center">
            <div class="p-3 rounded-full bg-green-100 mr-4">
              <i class="fas fa-search text-green-600"></i>
            </div>
            <div>
              <p class="text-gray-500 text-sm">查询次数</p>
              <p class="text-2xl font-semibold">{{ stats.queries }}</p>
            </div>
          </div>
          <div class="mt-4">
            <div class="flex items-center">
              <span class="text-green-500 mr-2"><i class="fas fa-arrow-up"></i> 32%</span>
              <span class="text-sm text-gray-600">较上月</span>
            </div>
          </div>
        </div>
        
        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center">
            <div class="p-3 rounded-full bg-purple-100 mr-4">
              <i class="fas fa-users text-purple-600"></i>
            </div>
            <div>
              <p class="text-gray-500 text-sm">活跃用户</p>
              <p class="text-2xl font-semibold">{{ stats.users }}</p>
            </div>
          </div>
          <div class="mt-4">
            <div class="flex items-center">
              <span class="text-green-500 mr-2"><i class="fas fa-arrow-up"></i> 12%</span>
              <span class="text-sm text-gray-600">较上月</span>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 图表和活动 -->
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <!-- 查询趋势图表 -->
        <div class="lg:col-span-2 bg-white rounded-lg shadow p-6">
          <div class="flex justify-between items-center mb-4">
            <h3 class="text-lg font-semibold text-gray-800">查询趋势</h3>
            <div>
              <select class="border border-gray-300 rounded px-2 py-1 text-sm focus:outline-none focus:ring-2 focus:ring-primary">
                <option>最近7天</option>
                <option>最近30天</option>
                <option>最近90天</option>
              </select>
            </div>
          </div>
          <div class="h-64 flex items-center justify-center bg-gray-50 rounded">
            <p class="text-gray-500">图表区域 - 实际应用中这里会显示查询趋势图表</p>
          </div>
        </div>
        
        <!-- 最近活动 -->
        <div class="bg-white rounded-lg shadow p-6">
          <h3 class="text-lg font-semibold text-gray-800 mb-4">最近活动</h3>
          <div class="space-y-4">
            <div v-for="(activity, index) in recentActivities" :key="index" class="border-b border-gray-100 pb-3 last:border-0 last:pb-0">
              <div class="flex">
                <div class="mr-3">
                  <div class="w-8 h-8 rounded-full bg-indigo-100 flex items-center justify-center">
                    <i :class="activity.icon" class="text-indigo-600"></i>
                  </div>
                </div>
                <div>
                  <p class="text-sm font-medium">{{ activity.title }}</p>
                  <p class="text-xs text-gray-500">{{ activity.time }}</p>
                </div>
              </div>
            </div>
          </div>
          <div class="mt-4 text-center">
            <a href="#" class="text-indigo-600 text-sm hover:underline">查看所有活动</a>
          </div>
        </div>
      </div>
      
      <!-- 常用查询和数据源状态 -->
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mt-6">
        <!-- 常用查询 -->
        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex justify-between items-center mb-4">
            <h3 class="text-lg font-semibold text-gray-800">常用查询</h3>
            <a href="#" class="text-indigo-600 text-sm hover:underline">查看全部</a>
          </div>
          <div class="space-y-2">
            <div v-for="(query, index) in frequentQueries" :key="index" class="p-3 hover:bg-gray-50 rounded cursor-pointer">
              <div class="flex justify-between items-center">
                <div>
                  <p class="font-medium">{{ query.name }}</p>
                  <p class="text-xs text-gray-500">{{ query.description }}</p>
                </div>
                <div class="flex space-x-2">
                  <button class="text-gray-500 hover:text-indigo-600">
                    <i class="fas fa-edit"></i>
                  </button>
                  <button class="text-gray-500 hover:text-indigo-600">
                    <i class="fas fa-play"></i>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 数据源状态 -->
        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex justify-between items-center mb-4">
            <h3 class="text-lg font-semibold text-gray-800">数据源状态</h3>
            <a href="#" class="text-indigo-600 text-sm hover:underline">管理数据源</a>
          </div>
          <div class="space-y-3">
            <div v-for="(source, index) in datasourceStatus" :key="index" class="flex justify-between items-center p-3 border-b border-gray-100 last:border-0">
              <div class="flex items-center">
                <div class="w-2 h-2 rounded-full mr-2" :class="source.status === 'online' ? 'bg-green-500' : 'bg-red-500'"></div>
                <span>{{ source.name }}</span>
              </div>
              <div class="text-sm text-gray-500">
                <span v-if="source.status === 'online'">正常</span>
                <span v-else class="text-red-500">离线</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  data() {
    return {
      stats: {
        datasources: 12,
        tables: 156,
        queries: 2453,
        users: 28
      },
      recentActivities: [
        { title: '李四更新了销售数据查询', time: '10分钟前', icon: 'fas fa-search' },
        { title: '系统同步了CRM数据库元数据', time: '1小时前', icon: 'fas fa-sync' },
        { title: '张三创建了新的数据源连接', time: '3小时前', icon: 'fas fa-database' },
        { title: '王五导出了客户分析报表', time: '昨天 15:30', icon: 'fas fa-file-export' },
        { title: '赵六修改了订单查询条件', time: '昨天 11:20', icon: 'fas fa-edit' }
      ],
      frequentQueries: [
        { name: '月度销售报表', description: '按地区统计月度销售额和同比增长' },
        { name: '客户购买行为分析', description: '分析客户购买频率和产品偏好' },
        { name: '库存预警查询', description: '显示库存低于安全水平的产品' },
        { name: '员工绩效统计', description: '统计销售人员的业绩和转化率' }
      ],
      datasourceStatus: [
        { name: '销售数据库', status: 'online' },
        { name: 'CRM数据库', status: 'online' },
        { name: '仓储数据库', status: 'online' },
        { name: 'HR数据库', status: 'offline' }
      ]
    }
  },
  mounted() {
    console.log('Dashboard component mounted')
  }
}