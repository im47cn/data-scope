/**
 * 查询历史面板组件
 * 显示查询历史记录并支持重用历史查询
 */
const QueryHistory = {
    props: {
        // 数据源ID
        dataSourceId: {
            type: String,
            required: true
        },
        // 每页显示记录数
        pageSize: {
            type: Number,
            default: 20
        },
        // 是否显示详细信息
        detailed: {
            type: Boolean,
            default: true
        }
    },

    data() {
        return {
            loading: false,
            historyRecords: [],
            selectedRecord: null,
            pagination: {
                current: 1,
                pageSize: this.pageSize,
                total: 0,
                showSizeChanger: true,
                showQuickJumper: true
            },
            filters: {
                status: [],
                date: null
            },
            searchText: '',
            // 表格列定义
            columns: [
                {
                    title: '执行时间',
                    dataIndex: 'executedAt',
                    width: '160px',
                    sorter: true,
                    scopedSlots: { customRender: 'executedAt' }
                },
                {
                    title: 'SQL语句',
                    dataIndex: 'sql',
                    ellipsis: true,
                    scopedSlots: { customRender: 'sql' }
                },
                {
                    title: '执行时长',
                    dataIndex: 'executionTime',
                    width: '100px',
                    sorter: true,
                    scopedSlots: { customRender: 'executionTime' }
                },
                {
                    title: '影响行数',
                    dataIndex: 'affectedRows',
                    width: '100px',
                    sorter: true
                },
                {
                    title: '状态',
                    dataIndex: 'status',
                    width: '100px',
                    filters: [
                        { text: '成功', value: 'SUCCESS' },
                        { text: '失败', value: 'FAILED' },
                        { text: '取消', value: 'CANCELLED' }
                    ],
                    scopedSlots: { customRender: 'status' }
                },
                {
                    title: '操作',
                    key: 'action',
                    width: '150px',
                    fixed: 'right',
                    scopedSlots: { customRender: 'action' }
                }
            ]
        };
    },

    computed: {
        hasSelected() {
            return !!this.selectedRecord;
        }
    },

    created() {
        this.debouncedSearch = UtilService.debounce(this.handleSearch, 300);
        this.loadHistory();
    },

    methods: {
        // 加载历史记录
        async loadHistory() {
            this.loading = true;
            try {
                const params = {
                    dataSourceId: this.dataSourceId,
                    page: this.pagination.current - 1,
                    size: this.pagination.pageSize,
                    sort: 'executedAt,desc'
                };

                if (this.searchText) {
                    params.search = this.searchText;
                }

                if (this.filters.status.length > 0) {
                    params.status = this.filters.status;
                }

                if (this.filters.date) {
                    params.startDate = this.filters.date[0];
                    params.endDate = this.filters.date[1];
                }

                const response = await QueryService.getQueryHistory(params);
                this.historyRecords = response.data.content;
                this.pagination.total = response.data.total;
            } catch (error) {
                console.error('加载查询历史失败:', error);
                this.$message.error('加载查询历史失败');
            } finally {
                this.loading = false;
            }
        },

        // 处理表格变化
        handleTableChange(pagination, filters, sorter) {
            this.pagination.current = pagination.current;
            this.pagination.pageSize = pagination.pageSize;
            
            if (filters.status) {
                this.filters.status = filters.status;
            }

            this.loadHistory();
        },

        // 处理搜索
        handleSearch() {
            this.pagination.current = 1;
            this.loadHistory();
        },

        // 处理日期范围变更
        handleDateRangeChange(dates) {
            this.filters.date = dates;
            this.handleSearch();
        },

        // 查看详情
        handleViewDetails(record) {
            this.selectedRecord = record;
            this.$emit('select', record);
        },

        // 重用查询
        handleReuseQuery(record) {
            this.$emit('reuse', record);
        },

        // 删除历史记录
        handleDelete(record) {
            this.$confirm({
                title: '确认删除',
                content: '确定要删除这条查询历史吗？',
                okText: '确认',
                okType: 'danger',
                cancelText: '取消',
                onOk: async () => {
                    try {
                        await QueryService.deleteQueryHistory(record.id);
                        this.$message.success('删除成功');
                        this.loadHistory();
                        if (this.selectedRecord?.id === record.id) {
                            this.selectedRecord = null;
                        }
                    } catch (error) {
                        console.error('删除查询历史失败:', error);
                        this.$message.error('删除查询历史失败');
                    }
                }
            });
        },

        // 保存为收藏查询
        async handleSaveQuery(record) {
            try {
                await QueryService.saveQuery({
                    name: `保存的查询 ${UtilService.formatDateTime(record.executedAt, 'YYYY-MM-DD HH:mm')}`,
                    sql: record.sql,
                    dataSourceId: this.dataSourceId,
                    description: '从查询历史保存'
                });
                this.$message.success('保存成功');
            } catch (error) {
                console.error('保存查询失败:', error);
                this.$message.error('保存查询失败');
            }
        },

        // 复制SQL
        async handleCopySQL(sql) {
            try {
                await UtilService.copyToClipboard(sql);
                this.$message.success('SQL已复制到剪贴板');
            } catch (error) {
                this.$message.error('复制失败，请手动复制');
            }
        }
    },

    template: `
        <div class="query-history">
            <!-- 搜索工具栏 -->
            <div class="history-toolbar">
                <a-input-search
                    v-model="searchText"
                    placeholder="搜索SQL语句"
                    style="width: 300px"
                    @search="handleSearch"
                    allowClear
                />
                <a-range-picker
                    v-model="filters.date"
                    style="width: 300px; margin-left: 16px"
                    @change="handleDateRangeChange"
                    allowClear
                />
            </div>

            <!-- 历史记录表格 -->
            <a-table
                :columns="columns"
                :dataSource="historyRecords"
                :pagination="pagination"
                :loading="loading"
                @change="handleTableChange"
                :rowKey="record => record.id"
                :scroll="{ x: 1200 }"
            >
                <!-- 执行时间列 -->
                <template slot="executedAt" slot-scope="text">
                    {{ UtilService.formatDateTime(text) }}
                </template>

                <!-- SQL语句列 -->
                <template slot="sql" slot-scope="text">
                    <a-tooltip :title="text" placement="topLeft">
                        <span class="sql-preview">{{ text }}</span>
                    </a-tooltip>
                </template>

                <!-- 执行时长列 -->
                <template slot="executionTime" slot-scope="text">
                    {{ text.toFixed(2) }}秒
                </template>

                <!-- 状态列 -->
                <template slot="status" slot-scope="status">
                    <a-tag :color="
                        status === 'SUCCESS' ? 'success' :
                        status === 'FAILED' ? 'error' :
                        'default'
                    ">
                        {{ 
                            status === 'SUCCESS' ? '成功' :
                            status === 'FAILED' ? '失败' :
                            status === 'CANCELLED' ? '取消' : 
                            status
                        }}
                    </a-tag>
                </template>

                <!-- 操作列 -->
                <template slot="action" slot-scope="text, record">
                    <a-space>
                        <a-tooltip title="查看详情">
                            <a-button 
                                type="link" 
                                size="small"
                                @click="handleViewDetails(record)"
                            >
                                <a-icon type="eye" />
                            </a-button>
                        </a-tooltip>
                        <a-tooltip title="重用查询">
                            <a-button 
                                type="link" 
                                size="small"
                                @click="handleReuseQuery(record)"
                            >
                                <a-icon type="redo" />
                            </a-button>
                        </a-tooltip>
                        <a-tooltip title="复制SQL">
                            <a-button 
                                type="link" 
                                size="small"
                                @click="handleCopySQL(record.sql)"
                            >
                                <a-icon type="copy" />
                            </a-button>
                        </a-tooltip>
                        <a-tooltip title="保存查询">
                            <a-button 
                                type="link" 
                                size="small"
                                @click="handleSaveQuery(record)"
                            >
                                <a-icon type="save" />
                            </a-button>
                        </a-tooltip>
                        <a-tooltip title="删除">
                            <a-button 
                                type="link" 
                                size="small"
                                @click="handleDelete(record)"
                            >
                                <a-icon type="delete" />
                            </a-button>
                        </a-tooltip>
                    </a-space>
                </template>
            </a-table>

            <!-- 详情抽屉 -->
            <a-drawer
                title="查询详情"
                placement="right"
                width="600"
                :visible="hasSelected"
                @close="selectedRecord = null"
            >
                <template v-if="selectedRecord">
                    <a-descriptions bordered :column="1">
                        <a-descriptions-item label="执行时间">
                            {{ UtilService.formatDateTime(selectedRecord.executedAt) }}
                        </a-descriptions-item>
                        <a-descriptions-item label="执行时长">
                            {{ selectedRecord.executionTime.toFixed(2) }}秒
                        </a-descriptions-item>
                        <a-descriptions-item label="影响行数">
                            {{ selectedRecord.affectedRows }}
                        </a-descriptions-item>
                        <a-descriptions-item label="状态">
                            <a-tag :color="
                                selectedRecord.status === 'SUCCESS' ? 'success' :
                                selectedRecord.status === 'FAILED' ? 'error' :
                                'default'
                            ">
                                {{ 
                                    selectedRecord.status === 'SUCCESS' ? '成功' :
                                    selectedRecord.status === 'FAILED' ? '失败' :
                                    selectedRecord.status === 'CANCELLED' ? '取消' : 
                                    selectedRecord.status
                                }}
                            </a-tag>
                        </a-descriptions-item>
                        <a-descriptions-item label="SQL语句">
                            <pre class="sql-code">{{ selectedRecord.sql }}</pre>
                        </a-descriptions-item>
                        <a-descriptions-item v-if="selectedRecord.error" label="错误信息">
                            <pre class="error-message">{{ selectedRecord.error }}</pre>
                        </a-descriptions-item>
                    </a-descriptions>

                    <div class="drawer-footer">
                        <a-space>
                            <a-button
                                type="primary"
                                @click="handleReuseQuery(selectedRecord)"
                            >
                                重用查询
                            </a-button>
                            <a-button
                                @click="handleCopySQL(selectedRecord.sql)"
                            >
                                复制SQL
                            </a-button>
                            <a-button
                                @click="handleSaveQuery(selectedRecord)"
                            >
                                保存查询
                            </a-button>
                        </a-space>
                    </div>
                </template>
            </a-drawer>
        </div>
    `
};

// 导入依赖
import QueryService from '../services/query-service.js';
import UtilService from '../services/util-service.js';

// 注册组件
Vue.component('query-history', QueryHistory);