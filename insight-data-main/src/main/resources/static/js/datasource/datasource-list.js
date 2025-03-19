/**
 * 数据源列表组件
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
                total: 0,
                showSizeChanger: true,
                showQuickJumper: true,
                showTotal: total => `共 ${total} 条记录`
            },
            sorter: {
                field: 'createdAt',
                order: 'descend'
            },
            columns: [
                {
                    title: '名称',
                    dataIndex: 'name',
                    key: 'name',
                    sorter: true,
                    width: '15%',
                    ellipsis: true
                },
                {
                    title: '类型',
                    dataIndex: 'type',
                    key: 'type',
                    width: '10%',
                    filters: []
                },
                {
                    title: '主机',
                    dataIndex: 'host',
                    key: 'host',
                    width: '15%',
                    ellipsis: true
                },
                {
                    title: '端口',
                    dataIndex: 'port',
                    key: 'port',
                    width: '8%'
                },
                {
                    title: '数据库',
                    dataIndex: 'databaseName',
                    key: 'databaseName',
                    width: '12%',
                    ellipsis: true
                },
                {
                    title: '状态',
                    dataIndex: 'active',
                    key: 'active',
                    width: '8%',
                    scopedSlots: { customRender: 'status' },
                    filters: [
                        { text: '激活', value: true },
                        { text: '禁用', value: false }
                    ]
                },
                {
                    title: '同步状态',
                    dataIndex: 'syncStatus',
                    key: 'syncStatus',
                    width: '15%',
                    scopedSlots: { customRender: 'syncStatus' }
                },
                {
                    title: '最后更新',
                    dataIndex: 'updatedAt',
                    key: 'updatedAt',
                    width: '15%',
                    sorter: true,
                    scopedSlots: { customRender: 'updatedAt' }
                },
                {
                    title: '操作',
                    key: 'action',
                    fixed: 'right',
                    width: '200px',
                    scopedSlots: { customRender: 'action' }
                }
            ]
        };
    },

    mounted() {
        this.fetchSupportedTypes();
        this.fetchDataSources();
        
        // 创建防抖函数
        this.handleSearchDebounced = this.debounce(function() {
            this.pagination.current = 1;
            this.fetchDataSources();
        }, 300);
    },

    methods: {
        // 自定义防抖函数，不依赖UtilService
        debounce(func, wait = 300, immediate = false) {
            let timeout;
            return function executedFunction(...args) {
                const context = this;
                const later = () => {
                    timeout = null;
                    if (!immediate) func.apply(context, args);
                };
                const callNow = immediate && !timeout;
                clearTimeout(timeout);
                timeout = setTimeout(later, wait);
                if (callNow) func.apply(context, args);
            };
        },
        
        async fetchSupportedTypes() {
            try {
                const response = await DataSourceService.getSupportedTypes();
                this.supportedTypes = response.data;
                // 更新类型过滤器
                this.columns.find(col => col.key === 'type').filters = 
                    this.supportedTypes.map(type => ({ text: type, value: type }));
            } catch (error) {
                console.error('获取数据源类型失败:', error);
                this.$message.error('获取数据源类型失败');
            }
        },

        async fetchDataSources() {
            this.loading = true;
            try {
                // 构建查询参数
                const params = {
                    page: this.pagination.current - 1,
                    size: this.pagination.pageSize,
                    sort: `${this.sorter.field},${this.sorter.order === 'ascend' ? 'asc' : 'desc'}`
                };

                if (this.searchQuery) {
                    params.search = this.searchQuery;
                }
                if (this.typeFilter) {
                    params.type = this.typeFilter;
                }
                if (this.statusFilter !== '') {
                    params.active = this.statusFilter;
                }

                const response = await DataSourceService.getDataSources(params);
                this.dataSources = response.data.content;
                this.pagination.total = response.data.pageable.totalElements;
            } catch (error) {
                console.error('获取数据源列表失败:', error);
                this.$message.error('获取数据源列表失败');
            } finally {
                this.loading = false;
            }
        },

        handleTableChange(pagination, filters, sorter) {
            this.pagination.current = pagination.current;
            this.pagination.pageSize = pagination.pageSize;
            
            if (sorter.field && sorter.order) {
                this.sorter.field = sorter.field;
                this.sorter.order = sorter.order;
            }
            
            this.typeFilter = filters.type && filters.type.length > 0 ? filters.type[0] : '';
            this.statusFilter = filters.active && filters.active.length > 0 ? filters.active[0] : '';
            
            this.fetchDataSources();
        },

        handleSearch() {
            this.handleSearchDebounced();
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

        async handleDelete(record) {
            this.$confirm({
                title: '确认删除',
                content: `确定要删除数据源 "${record.name}" 吗？`,
                okText: '确认',
                okType: 'danger',
                cancelText: '取消',
                onOk: async () => {
                    try {
                        await DataSourceService.deleteDataSource(record.id);
                        this.$message.success('删除成功');
                        this.fetchDataSources();
                    } catch (error) {
                        console.error('删除数据源失败:', error);
                        this.$message.error('删除数据源失败');
                    }
                }
            });
        },

        async handleSync(record) {
            // 如果已经在同步中，显示提示
            if (record.syncStatus && ['PENDING', 'RUNNING'].includes(record.syncStatus.status)) {
                this.$message.warning('该数据源正在同步中');
                return;
            }

            this.$confirm({
                title: '确认同步',
                content: `确定要同步数据源 "${record.name}" 的元数据吗？`,
                okText: '确认',
                cancelText: '取消',
                onOk: async () => {
                    try {
                        await DataSourceService.syncMetadata(record.id);
                        this.$message.success('同步任务已启动');
                        this.fetchDataSources();
                    } catch (error) {
                        console.error('启动同步任务失败:', error);
                        this.$message.error('启动同步任务失败');
                    }
                }
            });
        },

        async handleToggleStatus(record) {
            const newStatus = !record.active;
            const action = newStatus ? '激活' : '禁用';
            
            this.$confirm({
                title: `确认${action}`,
                content: `确定要${action}数据源 "${record.name}" 吗？`,
                okText: '确认',
                cancelText: '取消',
                onOk: async () => {
                    try {
                        await DataSourceService.toggleStatus(record.id, newStatus);
                        this.$message.success(`${action}成功`);
                        this.fetchDataSources();
                    } catch (error) {
                        console.error(`${action}数据源失败:`, error);
                        this.$message.error(`${action}数据源失败`);
                    }
                }
            });
        },
        
        // 格式化日期时间的辅助方法
        formatDateTime(dateTime) {
            if (!dateTime) return '';
            
            const d = new Date(dateTime);
            if (isNaN(d.getTime())) return '';

            const pad = (num) => String(num).padStart(2, '0');
            
            const year = d.getFullYear();
            const month = pad(d.getMonth() + 1);
            const day = pad(d.getDate());
            const hours = pad(d.getHours());
            const minutes = pad(d.getMinutes());
            const seconds = pad(d.getSeconds());
            
            return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
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
                    allowClear
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
                :scroll="{ x: 1300 }"
            >
                <template slot="status" slot-scope="text">
                    <a-badge :status="text ? 'success' : 'default'" />
                    <span>{{ text ? '激活' : '禁用' }}</span>
                </template>
                
                <template slot="syncStatus" slot-scope="text, record">
                    <sync-status-badge
                        :status="record.syncStatus || { status: 'UNKNOWN' }"
                        :showProgress="true"
                    />
                </template>
                
                <template slot="updatedAt" slot-scope="text">
                    {{ formatDateTime(text) }}
                </template>
                
                <template slot="action" slot-scope="text, record">
                    <a-button-group>
                        <a-tooltip title="查看详情">
                            <a-button type="primary" size="small" @click="handleView(record)">
                                <a-icon type="eye" />
                            </a-button>
                        </a-tooltip>
                        <a-tooltip title="编辑">
                            <a-button type="default" size="small" @click="handleEdit(record)">
                                <a-icon type="edit" />
                            </a-button>
                        </a-tooltip>
                        <a-tooltip :title="record.active ? '禁用' : '激活'">
                            <a-button 
                                :type="record.active ? 'danger' : 'default'" 
                                size="small" 
                                @click="handleToggleStatus(record)"
                            >
                                <a-icon :type="record.active ? 'stop' : 'play-circle'" />
                            </a-button>
                        </a-tooltip>
                        <a-tooltip title="同步元数据">
                            <a-button 
                                type="default" 
                                size="small" 
                                @click="handleSync(record)"
                                :disabled="record.syncStatus && ['PENDING', 'RUNNING'].includes(record.syncStatus.status)"
                            >
                                <a-icon type="sync" :spin="record.syncStatus && ['PENDING', 'RUNNING'].includes(record.syncStatus.status)" />
                            </a-button>
                        </a-tooltip>
                        <a-tooltip title="删除">
                            <a-button type="danger" size="small" @click="handleDelete(record)">
                                <a-icon type="delete" />
                            </a-button>
                        </a-tooltip>
                    </a-button-group>
                </template>
            </a-table>
        </div>
    `
};

// 注册组件
Vue.component('datasource-list', DataSourceList);
