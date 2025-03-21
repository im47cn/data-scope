// 仪表盘页面组件
const Dashboard = {
  template: `
    <div class="p-6">
      <div class="mb-6">
        <h2 class="text-2xl font-bold text-gray-800">仪表盘</h2>
        <p class="text-gray-600">数据概览和关键指标</p>
      </div>
      
      <!-- 统计卡片 -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-6">
        <div v-for="(stat, index) in stats" :key="index" class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-sm font-medium text-gray-500">{{ stat.title }}</h3>
            <div :class="'rounded-full p-2 ' + stat.iconBg">
              <i :class="stat.icon + ' text-white'"></i>
            </div>
          </div>
          <div class="flex items-end">
            <div class="text-2xl font-bold text-gray-900">{{ stat.value }}</div>
            <div :class="'ml-2 text-sm ' + (stat.change >= 0 ? 'text-green-600' : 'text-red-600')">
              <i :class="stat.change >= 0 ? 'fas fa-arrow-up' : 'fas fa-arrow-down'"></i>
              {{ Math.abs(stat.change) }}%
            </div>
          </div>
          <div class="text-sm text-gray-500 mt-1">vs 上月</div>
        </div>
      </div>
      
      <!-- 图表区域 -->
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-6">
        <!-- 趋势图 -->
        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex justify-between items-center mb-4">
            <h3 class="font-medium text-gray-700">查询趋势</h3>
            <div class="flex space-x-2">
              <button @click="trendPeriod = 'week'" :class="trendPeriod === 'week' ? 'text-primary' : 'text-gray-500 hover:text-gray-700'">
                周
              </button>
              <button @click="trendPeriod = 'month'" :class="trendPeriod === 'month' ? 'text-primary' : 'text-gray-500 hover:text-gray-700'">
                月
              </button>
              <button @click="trendPeriod = 'year'" :class="trendPeriod === 'year' ? 'text-primary' : 'text-gray-500 hover:text-gray-700'">
                年
              </button>
            </div>
          </div>
          <div class="h-64 flex items-center justify-center">
            <div class="text-center text-gray-500">
              <i class="fas fa-chart-line text-5xl mb-4"></i>
              <p>查询趋势图表</p>
            </div>
          </div>
        </div>
        
        <!-- 数据源使用情况 -->
        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex justify-between items-center mb-4">
            <h3 class="font-medium text-gray-700">数据源使用情况</h3>
            <button class="text-gray-500 hover:text-gray-700">
              <i class="fas fa-ellipsis-h"></i>
            </button>
          </div>
          <div class="h-64 flex items-center justify-center">
            <div class="text-center text-gray-500">
              <i class="fas fa-chart-pie text-5xl mb-4"></i>
              <p>数据源使用饼图</p>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 最近查询和活动 -->
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <!-- 最近查询 -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-4 border-b border-gray-200 flex justify-between items-center">
            <h3 class="font-medium text-gray-700">最近查询</h3>
            <router-link to="/query-history" class="text-indigo-600 hover:text-indigo-800 text-sm">
              查看全部
            </router-link>
          </div>
          <div class="p-4">
            <div v-for="(query, index) in recentQueries" :key="index" class="mb-4 last:mb-0">
              <div class="flex justify-between items-start">
                <div>
                  <div class="font-medium text-gray-800">{{ query.title }}</div>
                  <div class="text-sm text-gray-500 mt-1">{{ query.content }}</div>
                </div>
                <span class="px-2 py-1 text-xs rounded-full" :class="getStatusClass(query.status)">
                  {{ getStatusText(query.status) }}
                </span>
              </div>
              <div class="flex justify-between items-center mt-2 text-xs text-gray-500">
                <div>{{ query.user }}</div>
                <div>{{ query.time }}</div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 最近活动 -->
        <div class="bg-white rounded-lg shadow">
          <div class="p-4 border-b border-gray-200">
            <h3 class="font-medium text-gray-700">最近活动</h3>
          </div>
          <div class="p-4">
            <div v-for="(activity, index) in recentActivities" :key="index" class="mb-4 last:mb-0">
              <div class="flex">
                <div class="mr-4">
                  <div class="h-10 w-10 rounded-full bg-gray-200 overflow-hidden">
                    <img :src="activity.userAvatar" alt="" class="h-full w-full object-cover">
                  </div>
                </div>
                <div>
                  <div class="text-sm">
                    <span class="font-medium text-gray-900">{{ activity.user }}</span>
                    <span class="text-gray-700">{{ activity.action }}</span>
                  </div>
                  <div class="text-xs text-gray-500 mt-1">{{ activity.time }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  data() {
    return {
      trendPeriod: 'week',
      stats: [
        {
          title: '总查询次数',
          value: '4,294',
          change: 12.5,
          icon: 'fas fa-search',
          iconBg: 'bg-blue-500'
        },
        {
          title: '活跃用户',
          value: '42',
          change: 8.2,
          icon: 'fas fa-users',
          iconBg: 'bg-green-500'
        },
        {
          title: '平均查询时间',
          value: '245ms',
          change: -15.3,
          icon: 'fas fa-clock',
          iconBg: 'bg-indigo-500'
        },
        {
          title: '数据源数量',
          value: '12',
          change: 0,
          icon: 'fas fa-database',
          iconBg: 'bg-purple-500'
        }
      ],
      recentQueries: [
        {
          title: '销售数据分析',
          content: 'SELECT product_name, SUM(sales_amount) FROM sales GROUP BY product_name ORDER BY SUM(sales_amount) DESC LIMIT 10',
          status: 'success',
          user: '张三',
          time: '10分钟前'
        },
        {
          title: '库存状态查询',
          content: 'SELECT product_name, stock FROM products WHERE stock < 20 ORDER BY stock ASC',
          status: 'success',
          user: '李四',
          time: '1小时前'
        },
        {
          title: '客户分布统计',
          content: '查询各地区的客户数量和订单总额',
          status: 'error',
          user: '王五',
          time: '2小时前'
        },
        {
          title: '员工绩效报告',
          content: 'SELECT employee_name, SUM(sales_amount) FROM sales GROUP BY employee_name',
          status: 'running',
          user: '赵六',
          time: '3小时前'
        }
      ],
      recentActivities: [
        {
          user: '张三',
          action: '添加了新的数据源 "营销数据库"',
          time: '15分钟前',
          userAvatar: 'https://randomuser.me/api/portraits/men/1.jpg'
        },
        {
          user: '李四',
          action: '保存了查询 "月度销售报表"',
          time: '1小时前',
          userAvatar: 'https://randomuser.me/api/portraits/men/2.jpg'
        },
        {
          user: '王五',
          action: '更新了系统设置',
          time: '3小时前',
          userAvatar: 'https://randomuser.me/api/portraits/women/3.jpg'
        },
        {
          user: '赵六',
          action: '创建了新的数据可视化 "销售趋势图"',
          time: '5小时前',
          userAvatar: 'https://randomuser.me/api/portraits/men/4.jpg'
        }
      ]
    }
  },
  methods: {
    getStatusClass(status) {
      switch (status) {
        case 'success': return 'bg-green-100 text-green-800'
        case 'error': return 'bg-red-100 text-red-800'
        case 'running': return 'bg-blue-100 text-blue-800'
        default: return 'bg-gray-100 text-gray-800'
      }
    },
    
    getStatusText(status) {
      switch (status) {
        case 'success': return '成功'
        case 'error': return '失败'
        case 'running': return '执行中'
        default: return status
      }
    }
  }
}