/**
 * 仪表盘组件
 */
const DashboardComponent = {
    data() {
        return {
            loading: true,
            stats: {
                dataSources: 0,
                tables: 0,
                queries: 0,
                apis: 0
            },
            recentActivities: [],
            favoriteQueries: [],
            dataSourceHealth: []
        };
    },

    mounted() {
        this.fetchDashboardData();
    },

    methods: {
        async fetchDashboardData() {
            this.loading = true;
            try {
                // 获取数据源统计信息
                await this.fetchDataSourceStats();
                
                // 获取最近活动
                await this.fetchRecentActivities();
                
                // 获取收藏查询
                await this.fetchFavoriteQueries();
                
                // 获取数据源健康状态
                await this.fetchDataSourceHealth();
            } catch (error) {
                console.error('获取仪表盘数据失败:', error);
                this.$message.error('获取仪表盘数据失败');
            } finally {
                this.loading = false;
            }
        },

        async fetchDataSourceStats() {
            try {
                // 获取数据源数量
                const dataSourcesResponse = await DataSourceService.getDataSources({
                    page: 0,
                    size: 1
                });
                this.stats.dataSources = dataSourcesResponse.data.pageable.totalElements;

                // 使用模拟数据
                this.stats.tables = 125;
                this.stats.queries = 78;
                this.stats.apis = 42;
            } catch (error) {
                console.error('获取统计数据失败:', error);
                // 使用默认值
                this.stats = {
                    dataSources: 0,
                    tables: 0,
                    queries: 0,
                    apis: 0
                };
            }
        },

        async fetchRecentActivities() {
            try {
                // 使用模拟数据
                this.recentActivities = [
                    {
                        id: '1',
                        title: '创建了新的数据源连接',
                        timestamp: new Date(Date.now() - 1000 * 60 * 30).toISOString(),
                        icon: 'database',
                        iconColor: '#1890ff'
                    },
                    {
                        id: '2',
                        title: '执行了SQL查询',
                        timestamp: new Date(Date.now() - 1000 * 60 * 120).toISOString(),
                        icon: 'search',
                        iconColor: '#722ed1'
                    },
                    {
                        id: '3',
                        title: '更新了数据源配置',
                        timestamp: new Date(Date.now() - 1000 * 60 * 240).toISOString(),
                        icon: 'edit',
                        iconColor: '#52c41a'
                    },
                    {
                        id: '4',
                        title: '创建了新的API',
                        timestamp: new Date(Date.now() - 1000 * 60 * 360).toISOString(),
                        icon: 'api',
                        iconColor: '#fa8c16'
                    }
                ];
            } catch (error) {
                console.error('获取最近活动失败:', error);
                this.recentActivities = [];
            }
        },

        async fetchFavoriteQueries() {
            try {
                // 使用模拟数据
                this.favoriteQueries = [
                    {
                        id: '1',
                        name: '月度销售报表',
                        dataSourceName: 'MySQL开发环境',
                        lastRunAt: new Date(Date.now() - 1000 * 60 * 60 * 24).toISOString()
                    },
                    {
                        id: '2',
                        name: '客户分析',
                        dataSourceName: 'PostgreSQL生产环境',
                        lastRunAt: new Date(Date.now() - 1000 * 60 * 60 * 48).toISOString()
                    },
                    {
                        id: '3',
                        name: '库存状态',
                        dataSourceName: 'MySQL开发环境',
                        lastRunAt: new Date(Date.now() - 1000 * 60 * 60 * 72).toISOString()
                    },
                    {
                        id: '4',
                        name: '用户活跃度',
                        dataSourceName: 'PostgreSQL生产环境',
                        lastRunAt: new Date(Date.now() - 1000 * 60 * 60 * 96).toISOString()
                    }
                ];
            } catch (error) {
                console.error('获取收藏查询失败:', error);
                this.favoriteQueries = [];
            }
        },

        async fetchDataSourceHealth() {
            try {
                const response = await DataSourceService.getDataSources({
                    page: 0,
                    size: 10,
                    sort: 'updatedAt,desc'
                });
                this.dataSourceHealth = response.data.content;
            } catch (error) {
                console.error('获取数据源健康状态失败:', error);
                this.dataSourceHealth = [];
            }
        },

        formatDateTime(dateTime) {
            return UtilService.formatDateTime(dateTime);
        },

        handleRunQuery(query) {
            this.$router.push(`/query-builder?id=${query.id}`);
        },

        handleViewDataSource(dataSource) {
            this.$router.push(`/datasource/view/${dataSource.id}`);
        },

        handleAddDataSource() {
            this.$router.push('/datasource/add');
        },

        handleCreateQuery() {
            this.$router.push('/query-builder');
        },

        handleNLQuery() {
            this.$router.push('/nl-query');
        },

        handleCreateAPI() {
            this.$router.push('/low-code');
        }
    },

    template: `
        <div class="dashboard-container">
            <a-spin :spinning="loading">
                <!-- 统计卡片 -->
                <div class="stats-cards">
                    <a-row :gutter="16">
                        <a-col :span="6">
                            <a-card>
                                <a-statistic
                                    title="数据源"
                                    :value="stats.dataSources"
                                    :valueStyle="{ color: '#3f8600' }"
                                >
                                    <template #prefix>
                                        <a-icon type="database" />
                                    </template>
                                </a-statistic>
                            </a-card>
                        </a-col>
                        <a-col :span="6">
                            <a-card>
                                <a-statistic
                                    title="表"
                                    :value="stats.tables"
                                    :valueStyle="{ color: '#1890ff' }"
                                >
                                    <template #prefix>
                                        <a-icon type="table" />
                                    </template>
                                </a-statistic>
                            </a-card>
                        </a-col>
                        <a-col :span="6">
                            <a-card>
                                <a-statistic
                                    title="查询"
                                    :value="stats.queries"
                                    :valueStyle="{ color: '#722ed1' }"
                                >
                                    <template #prefix>
                                        <a-icon type="search" />
                                    </template>
                                </a-statistic>
                            </a-card>
                        </a-col>
                        <a-col :span="6">
                            <a-card>
                                <a-statistic
                                    title="API"
                                    :value="stats.apis"
                                    :valueStyle="{ color: '#fa8c16' }"
                                >
                                    <template #prefix>
                                        <a-icon type="api" />
                                    </template>
                                </a-statistic>
                            </a-card>
                        </a-col>
                    </a-row>
                </div>

                <!-- 最近活动和收藏查询 -->
                <a-row :gutter="16" style="margin-top: 16px;">
                    <a-col :span="12">
                        <a-card title="最近活动" :bordered="false">
                            <a-list
                                itemLayout="horizontal"
                                :dataSource="recentActivities"
                                :loading="loading"
                            >
                                <a-list-item slot="renderItem" slot-scope="item">
                                    <a-list-item-meta>
                                        <a-avatar slot="avatar" :style="{ backgroundColor: item.iconColor }">
                                            <a-icon :type="item.icon" />
                                        </a-avatar>
                                        <template slot="title">
                                            {{ item.title }}
                                        </template>
                                        <template slot="description">
                                            {{ formatDateTime(item.timestamp) }}
                                        </template>
                                    </a-list-item-meta>
                                </a-list-item>
                                <div slot="footer" class="view-all-link">
                                    <a-button type="link">查看全部</a-button>
                                </div>
                            </a-list>
                        </a-card>
                    </a-col>
                    <a-col :span="12">
                        <a-card title="收藏查询" :bordered="false">
                            <a-list
                                itemLayout="horizontal"
                                :dataSource="favoriteQueries"
                                :loading="loading"
                            >
                                <a-list-item slot="renderItem" slot-scope="item">
                                    <a-list-item-meta>
                                        <a-avatar slot="avatar" style="backgroundColor: #faad14">
                                            <a-icon type="star" />
                                        </a-avatar>
                                        <template slot="title">
                                            {{ item.name }}
                                        </template>
                                        <template slot="description">
                                            {{ item.dataSourceName }} - 上次运行: {{ formatDateTime(item.lastRunAt) }}
                                        </template>
                                    </a-list-item-meta>
                                    <a-button
                                        type="primary"
                                        shape="circle"
                                        icon="play-circle"
                                        @click="handleRunQuery(item)"
                                    />
                                </a-list-item>
                                <div slot="footer" class="view-all-link">
                                    <a-button type="link">管理收藏</a-button>
                                </div>
                            </a-list>
                        </a-card>
                    </a-col>
                </a-row>

                <!-- 数据源健康状态 -->
                <a-card title="数据源健康状态" style="margin-top: 16px;">
                    <a-table
                        :columns="[
                            { title: '数据源名称', dataIndex: 'name', key: 'name' },
                            { title: '类型', dataIndex: 'type', key: 'type' },
                            { title: '状态', dataIndex: 'active', key: 'active', scopedSlots: { customRender: 'status' } },
                            { title: '上次同步', dataIndex: 'syncStatus', key: 'syncStatus', scopedSlots: { customRender: 'syncStatus' } },
                            { title: '表数量', dataIndex: 'tableCount', key: 'tableCount' },
                            { title: '操作', key: 'action', scopedSlots: { customRender: 'action' } }
                        ]"
                        :dataSource="dataSourceHealth"
                        :pagination="false"
                        :loading="loading"
                    >
                        <template slot="status" slot-scope="text">
                            <a-badge :status="text ? 'success' : 'default'" />
                            <span>{{ text ? '活跃' : '禁用' }}</span>
                        </template>
                        
                        <template slot="syncStatus" slot-scope="text, record">
                            <span v-if="record.syncStatus">
                                {{ formatDateTime(record.syncStatus.updatedAt) }}
                            </span>
                            <span v-else>-</span>
                        </template>
                        
                        <template slot="action" slot-scope="text, record">
                            <a-button type="link" @click="handleViewDataSource(record)">
                                查看
                            </a-button>
                        </template>
                    </a-table>
                </a-card>

                <!-- 快速操作 -->
                <a-card title="快速操作" style="margin-top: 16px;">
                    <a-row :gutter="16">
                        <a-col :span="6">
                            <a-card hoverable @click="handleAddDataSource">
                                <a-icon type="plus-circle" style="font-size: 24px; color: #1890ff" />
                                <p>添加数据源</p>
                            </a-card>
                        </a-col>
                        <a-col :span="6">
                            <a-card hoverable @click="handleCreateQuery">
                                <a-icon type="search" style="font-size: 24px; color: #722ed1" />
                                <p>新建查询</p>
                            </a-card>
                        </a-col>
                        <a-col :span="6">
                            <a-card hoverable @click="handleNLQuery">
                                <a-icon type="message" style="font-size: 24px; color: #52c41a" />
                                <p>自然语言查询</p>
                            </a-card>
                        </a-col>
                        <a-col :span="6">
                            <a-card hoverable @click="handleCreateAPI">
                                <a-icon type="api" style="font-size: 24px; color: #fa8c16" />
                                <p>创建API</p>
                            </a-card>
                        </a-col>
                    </a-row>
                </a-card>
            </a-spin>
        </div>
    `
};

// 注册组件
Vue.component('dashboard-view', DashboardComponent);