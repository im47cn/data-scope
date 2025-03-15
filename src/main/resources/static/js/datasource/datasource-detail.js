/**
 * 数据源详情组件
 * 用于查看数据源的详细信息，包括模式和表信息
 */
const DataSourceDetail = {
    props: {
        // 数据源ID
        dataSourceId: {
            type: [Number, String],
            required: true
        }
    },
    data() {
        return {
            loading: false,
            schemaLoading: false,
            tableLoading: false,
            dataSource: null,
            schemas: [],
            currentSchema: null,
            tables: [],
            activeTab: 'basic',
            tableColumns: [
                {
                    title: '表名',
                    dataIndex: 'name',
                    key: 'name',
                    sorter: (a, b) => a.name.localeCompare(b.name)
                },
                {
                    title: '类型',
                    dataIndex: 'type',
                    key: 'type',
                    width: 120
                },
                {
                    title: '行数',
                    dataIndex: 'estimatedRowCount',
                    key: 'estimatedRowCount',
                    width: 120,
                    sorter: (a, b) => (a.estimatedRowCount || 0) - (b.estimatedRowCount || 0)
                },
                {
                    title: '数据大小',
                    dataIndex: 'dataSize',
                    key: 'dataSize',
                    width: 120,
                    customRender: (text) => this.formatSize(text)
                },
                {
                    title: '索引大小',
                    dataIndex: 'indexSize',
                    key: 'indexSize',
                    width: 120,
                    customRender: (text) => this.formatSize(text)
                },
                {
                    title: '最后分析时间',
                    dataIndex: 'lastAnalyzed',
                    key: 'lastAnalyzed',
                    width: 180,
                    customRender: (text) => this.formatDate(text)
                },
                {
                    title: '操作',
                    key: 'action',
                    width: 120,
                    customRender: (text, record) => (
                        <a-button-group>
                            <a-button type="primary" size="small" onClick={() => this.viewTable(record)}>
                                <a-icon type="table" />查看
                            </a-button>
                            <a-button type="default" size="small" onClick={() => this.queryTable(record)}>
                                <a-icon type="search" />查询
                            </a-button>
                        </a-button-group>
                    )
                }
            ]
        };
    },
    computed: {
        connectionPropertiesList() {
            if (!this.dataSource || !this.dataSource.connectionProperties) {
                return [];
            }
            
            return Object.entries(this.dataSource.connectionProperties)
                .map(([key, value]) => ({ key, value }));
        }
    },
    mounted() {
        this.fetchDataSource();
        this.fetchSchemas();
    },
    methods: {
        fetchDataSource() {
            this.loading = true;
            
            axios.get(`/datasources/${this.dataSourceId}`)
                .then(response => {
                    this.dataSource = response.data;
                })
                .catch(error => {
                    console.error('获取数据源详情失败:', error);
                    this.$message.error('获取数据源详情失败');
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        fetchSchemas() {
            this.schemaLoading = true;
            
            axios.get(`/datasources/${this.dataSourceId}/schemas`)
                .then(response => {
                    this.schemas = response.data;
                    
                    // 如果有模式，默认选择第一个
                    if (this.schemas.length > 0) {
                        this.selectSchema(this.schemas[0]);
                    }
                })
                .catch(error => {
                    console.error('获取模式列表失败:', error);
                    this.$message.error('获取模式列表失败');
                })
                .finally(() => {
                    this.schemaLoading = false;
                });
        },
        fetchTables(schemaName) {
            this.tableLoading = true;
            
            axios.get(`/datasources/${this.dataSourceId}/schemas/${schemaName}/tables`)
                .then(response => {
                    this.tables = response.data;
                })
                .catch(error => {
                    console.error('获取表列表失败:', error);
                    this.$message.error('获取表列表失败');
                })
                .finally(() => {
                    this.tableLoading = false;
                });
        },
        selectSchema(schema) {
            this.currentSchema = schema;
            this.fetchTables(schema.name);
        },
        handleTabChange(key) {
            this.activeTab = key;
        },
        handleEdit() {
            this.$router.push(`/datasource/edit/${this.dataSourceId}`);
        },
        handleSync() {
            this.$confirm({
                title: '确认同步',
                content: `确定要同步数据源 "${this.dataSource.name}" 的元数据吗？`,
                okText: '确认',
                cancelText: '取消',
                onOk: () => {
                    axios.post(`/datasources/${this.dataSourceId}/sync`)
                        .then(response => {
                            this.$message.success('同步任务已启动');
                            // 可以在这里处理同步作业ID
                            console.log('同步作业ID:', response.data);
                        })
                        .catch(error => {
                            console.error('启动同步任务失败:', error);
                            this.$message.error('启动同步任务失败');
                        });
                }
            });
        },
        handleBack() {
            this.$router.push('/datasource/list');
        },
        viewTable(table) {
            // 跳转到表详情页面
            this.$router.push(`/datasource/${this.dataSourceId}/schema/${this.currentSchema.name}/table/${table.name}`);
        },
        queryTable(table) {
            // 跳转到查询构建器页面
            this.$router.push({
                path: '/query-builder',
                query: {
                    dataSourceId: this.dataSourceId,
                    schema: this.currentSchema.name,
                    table: table.name
                }
            });
        },
        formatDate(date) {
            if (!date) return '-';
            return moment(date).format('YYYY-MM-DD HH:mm:ss');
        },
        formatSize(bytes) {
            if (bytes === null || bytes === undefined) return '-';
            
            const units = ['B', 'KB', 'MB', 'GB', 'TB'];
            let size = bytes;
            let unitIndex = 0;
            
            while (size >= 1024 && unitIndex < units.length - 1) {
                size /= 1024;
                unitIndex++;
            }
            
            return `${size.toFixed(2)} ${units[unitIndex]}`;
        }
    },
    template: `
        <div class="datasource-detail-container">
            <a-spin :spinning="loading">
                <div class="page-header">
                    <div class="page-title">
                        <h1>{{ dataSource ? dataSource.name : '数据源详情' }}</h1>
                        <a-tag :color="dataSource && dataSource.active ? 'green' : 'red'">
                            {{ dataSource && dataSource.active ? '激活' : '禁用' }}
                        </a-tag>
                    </div>
                    <div class="page-actions">
                        <a-button-group>
                            <a-button type="primary" @click="handleEdit">
                                <a-icon type="edit" />编辑
                            </a-button>
                            <a-button @click="handleSync">
                                <a-icon type="sync" />同步元数据
                            </a-button>
                            <a-button @click="handleBack">
                                <a-icon type="arrow-left" />返回
                            </a-button>
                        </a-button-group>
                    </div>
                </div>
                
                <a-tabs v-if="dataSource" :activeKey="activeTab" @change="handleTabChange">
                    <a-tab-pane key="basic" tab="基本信息">
                        <a-descriptions bordered :column="2">
                            <a-descriptions-item label="数据源名称">{{ dataSource.name }}</a-descriptions-item>
                            <a-descriptions-item label="数据源类型">{{ dataSource.type }}</a-descriptions-item>
                            <a-descriptions-item label="主机地址">{{ dataSource.host }}</a-descriptions-item>
                            <a-descriptions-item label="端口">{{ dataSource.port }}</a-descriptions-item>
                            <a-descriptions-item label="数据库名称">{{ dataSource.databaseName }}</a-descriptions-item>
                            <a-descriptions-item label="用户名">{{ dataSource.username }}</a-descriptions-item>
                            <a-descriptions-item label="状态">
                                <a-badge :status="dataSource.active ? 'success' : 'default'" />
                                <span>{{ dataSource.active ? '激活' : '禁用' }}</span>
                            </a-descriptions-item>
                            <a-descriptions-item label="最后同步时间">
                                {{ dataSource.lastSyncTime ? formatDate(dataSource.lastSyncTime) : '未同步' }}
                            </a-descriptions-item>
                            <a-descriptions-item label="创建时间">{{ formatDate(dataSource.createdAt) }}</a-descriptions-item>
                            <a-descriptions-item label="更新时间">{{ formatDate(dataSource.updatedAt) }}</a-descriptions-item>
                            <a-descriptions-item label="描述" :span="2">{{ dataSource.description || '-' }}</a-descriptions-item>
                        </a-descriptions>
                        
                        <a-divider orientation="left">连接属性</a-divider>
                        
                        <a-table
                            :columns="[
                                { title: '属性名', dataIndex: 'key', key: 'key' },
                                { title: '属性值', dataIndex: 'value', key: 'value' }
                            ]"
                            :dataSource="connectionPropertiesList"
                            :pagination="false"
                            size="small"
                            :rowKey="(record, index) => index"
                        />
                    </a-tab-pane>
                    
                    <a-tab-pane key="metadata" tab="元数据">
                        <a-row :gutter="16">
                            <a-col :span="6">
                                <a-spin :spinning="schemaLoading">
                                    <a-card title="模式列表" :bordered="false">
                                        <a-list
                                            size="small"
                                            :dataSource="schemas"
                                            :rowKey="item => item.id"
                                        >
                                            <a-list-item slot="renderItem" slot-scope="item">
                                                <a
                                                    @click="selectSchema(item)"
                                                    :class="{ active: currentSchema && currentSchema.id === item.id }"
                                                >
                                                    {{ item.name }}
                                                </a>
                                            </a-list-item>
                                            <div slot="header" class="list-header">
                                                <span>共 {{ schemas.length }} 个模式</span>
                                            </div>
                                            <div slot="footer" class="list-footer">
                                                <a-button
                                                    type="dashed"
                                                    size="small"
                                                    style="width: 100%"
                                                    @click="handleSync"
                                                >
                                                    <a-icon type="sync" />刷新元数据
                                                </a-button>
                                            </div>
                                        </a-list>
                                    </a-card>
                                </a-spin>
                            </a-col>
                            
                            <a-col :span="18">
                                <a-spin :spinning="tableLoading">
                                    <a-card
                                        :title="currentSchema ? \`\${currentSchema.name} 的表列表\` : '表列表'"
                                        :bordered="false"
                                    >
                                        <a-table
                                            :columns="tableColumns"
                                            :dataSource="tables"
                                            :rowKey="record => record.id"
                                            :pagination="{ pageSize: 10 }"
                                            size="middle"
                                        />
                                    </a-card>
                                </a-spin>
                            </a-col>
                        </a-row>
                    </a-tab-pane>
                </a-tabs>
            </a-spin>
        </div>
    `
};

// 注册组件
Vue.component('datasource-detail', DataSourceDetail);
