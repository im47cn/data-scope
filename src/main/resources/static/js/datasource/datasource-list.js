/**
 * 数据源列表页面脚本
 */
const DataSourceList = {
    data() {
        return {
            dataSources: [],
            loading: false,
            searchQuery: '',
            typeFilter: '',
            statusFilter: '',
            supportedTypes: [],
            pagination: {
                current: 1,
                pageSize: 10,
                total: 0
            },
            columns: [
                {
                    title: '名称',
                    dataIndex: 'name',
                    key: 'name',
                    sorter: true
                },
                {
                    title: '类型',
                    dataIndex: 'type',
                    key: 'type',
                    filters: []
                },
                {
                    title: '主机',
                    dataIndex: 'host',
                    key: 'host'
                },
                {
                    title: '端口',
                    dataIndex: 'port',
                    key: 'port'
                },
                {
                    title: '数据库',
                    dataIndex: 'databaseName',
                    key: 'databaseName'
                },
                {
                    title: '状态',
                    dataIndex: 'active',
                    key: 'active',
                    scopedSlots: { customRender: 'status' },
                    filters: [
                        { text: '激活', value: true },
                        { text: '禁用', value: false }
                    ]
                },
                {
                    title: '最后同步时间',
                    dataIndex: 'lastSyncTime',
                    key: 'lastSyncTime',
                    sorter: true,
                    scopedSlots: { customRender: 'lastSyncTime' }
                },
                {
                    title: '操作',
                    key: 'action',
                    scopedSlots: { customRender: 'action' }
                }
            ]
        };
    },
    mounted() {
        this.fetchSupportedTypes();
        this.fetchDataSources();
    },
    methods: {
        fetchSupportedTypes() {
            axios.get('/datasources/types')
                .then(response => {
                    this.supportedTypes = response.data;
                    // 更新类型过滤器
                    this.columns.find(col => col.key === 'type').filters = 
                        this.supportedTypes.map(type => ({ text: type, value: type }));
                })
                .catch(error => {
                    console.error('获取数据源类型失败:', error);
                    this.$message.error('获取数据源类型失败');
                });
        },
        fetchDataSources() {
            this.loading = true;
            
            // 构建查询参数
            let params = {};
            if (this.typeFilter) {
                params.type = this.typeFilter;
            }
            if (this.statusFilter !== '') {
                params.active = this.statusFilter;
            }
            
            axios.get('/datasources', { params })
                .then(response => {
                    this.dataSources = response.data;
                    this.pagination.total = response.data.length;
                })
                .catch(error => {
                    console.error('获取数据源列表失败:', error);
                    this.$message.error('获取数据源列表失败');
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        handleTableChange(pagination, filters, sorter) {
            this.pagination.current = pagination.current;
            
            // 处理过滤
            this.typeFilter = filters.type && filters.type.length > 0 ? filters.type[0] : '';
            this.statusFilter = filters.active && filters.active.length > 0 ? filters.active[0] : '';
            
            this.fetchDataSources();
        },
        handleSearch() {
            this.pagination.current = 1;
            this.fetchDataSources();
        },
        handleAdd() {
            this.$router.push('/datasource/add');
        },
        handleEdit(record) {
            this.$router.push(`/datasource/edit/${record.id}`);
        },
        handleView(record) {
            this.$router.push(`/datasource/view/${record.id}`);
        },
        handleDelete(record) {
            this.$confirm({
                title: '确认删除',
                content: `确定要删除数据源 "${record.name}" 吗？`,
                okText: '确认',
                okType: 'danger',
                cancelText: '取消',
                onOk: () => {
                    axios.delete(`/datasources/${record.id}`)
                        .then(() => {
                            this.$message.success('删除成功');
                            this.fetchDataSources();
                        })
                        .catch(error => {
                            console.error('删除数据源失败:', error);
                            this.$message.error('删除数据源失败');
                        });
                }
            });
        },
        handleSync(record) {
            this.$confirm({
                title: '确认同步',
                content: `确定要同步数据源 "${record.name}" 的元数据吗？`,
                okText: '确认',
                cancelText: '取消',
                onOk: () => {
                    axios.post(`/datasources/${record.id}/sync`)
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
        handleToggleStatus(record) {
            const newStatus = !record.active;
            const action = newStatus ? '激活' : '禁用';
            
            this.$confirm({
                title: `确认${action}`,
                content: `确定要${action}数据源 "${record.name}" 吗？`,
                okText: '确认',
                cancelText: '取消',
                onOk: () => {
                    // 构建更新请求
                    const data = {
                        ...record,
                        active: newStatus
                    };
                    
                    axios.put(`/datasources/${record.id}`, data)
                        .then(() => {
                            this.$message.success(`${action}成功`);
                            this.fetchDataSources();
                        })
                        .catch(error => {
                            console.error(`${action}数据源失败:`, error);
                            this.$message.error(`${action}数据源失败`);
                        });
                }
            });
        },
        formatDate(date) {
            if (!date) return '-';
            return moment(date).format('YYYY-MM-DD HH:mm:ss');
        }
    },
    template: `
        <div class="datasource-list-container">
            <div class="page-header">
                <h1>数据源管理</h1>
                <a-button type="primary" @click="handleAdd">
                    <a-icon type="plus" />添加数据源
                </a-button>
            </div>
            
            <div class="search-bar">
                <a-input-search
                    placeholder="搜索数据源名称"
                    v-model="searchQuery"
                    style="width: 300px"
                    @search="handleSearch"
                />
                <a-select
                    placeholder="数据源类型"
                    v-model="typeFilter"
                    style="width: 200px; margin-left: 16px"
                    allowClear
                    @change="handleSearch"
                >
                    <a-select-option v-for="type in supportedTypes" :key="type" :value="type">
                        {{ type }}
                    </a-select-option>
                </a-select>
                <a-select
                    placeholder="状态"
                    v-model="statusFilter"
                    style="width: 120px; margin-left: 16px"
                    allowClear
                    @change="handleSearch"
                >
                    <a-select-option :value="true">激活</a-select-option>
                    <a-select-option :value="false">禁用</a-select-option>
                </a-select>
            </div>
            
            <a-table
                :columns="columns"
                :dataSource="dataSources"
                :rowKey="record => record.id"
                :pagination="pagination"
                :loading="loading"
                @change="handleTableChange"
            >
                <template slot="status" slot-scope="text">
                    <a-badge :status="text ? 'success' : 'default'" />
                    <span>{{ text ? '激活' : '禁用' }}</span>
                </template>
                
                <template slot="lastSyncTime" slot-scope="text">
                    {{ formatDate(text) }}
                </template>
                
                <template slot="action" slot-scope="text, record">
                    <a-button-group>
                        <a-button type="primary" size="small" @click="handleView(record)">
                            <a-icon type="eye" />查看
                        </a-button>
                        <a-button type="default" size="small" @click="handleEdit(record)">
                            <a-icon type="edit" />编辑
                        </a-button>
                        <a-button 
                            :type="record.active ? 'danger' : 'default'" 
                            size="small" 
                            @click="handleToggleStatus(record)"
                        >
                            <a-icon :type="record.active ? 'stop' : 'play-circle'" />
                            {{ record.active ? '禁用' : '激活' }}
                        </a-button>
                        <a-button type="default" size="small" @click="handleSync(record)">
                            <a-icon type="sync" />同步
                        </a-button>
                        <a-button type="danger" size="small" @click="handleDelete(record)">
                            <a-icon type="delete" />删除
                        </a-button>
                    </a-button-group>
                </template>
            </a-table>
        </div>
    `
};

// 注册组件
Vue.component('datasource-list', DataSourceList);
