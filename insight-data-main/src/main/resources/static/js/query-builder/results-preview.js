/**
 * 查询结果预览组件
 * 提供表格展示、排序、过滤和导出功能
 */
const ResultsPreview = {
    props: {
        // 查询ID
        queryId: {
            type: String,
            required: true
        },
        // 是否自动加载
        autoLoad: {
            type: Boolean,
            default: true
        },
        // 每页记录数选项
        pageSizeOptions: {
            type: Array,
            default: () => ['10', '20', '50', '100', '200']
        },
        // 最大导出记录数
        maxExportSize: {
            type: Number,
            default: 10000
        }
    },

    data() {
        return {
            loading: false,
            error: null,
            columns: [],
            dataSource: [],
            selectedRows: [],
            pagination: {
                current: 1,
                pageSize: 20,
                total: 0,
                showSizeChanger: true,
                showQuickJumper: true,
                pageSizeOptions: this.pageSizeOptions,
                showTotal: total => `共 ${total} 条记录`
            },
            sorter: null,
            filters: {},
            // 导出状态
            exporting: false,
            exportProgress: 0,
            // 统计信息
            stats: {
                executionTime: null,
                totalRows: 0,
                affectedRows: 0
            },
            // 轮询状态
            pollingTimer: null,
            pollingCount: 0,
            maxPollingCount: 60, // 最多轮询60次，每次3秒
            // 可视化选项
            visualOptions: {
                showRowNumbers: true,
                highlightNulls: true,
                wrapText: false,
                showStats: true
            }
        };
    },

    computed: {
        hasData() {
            return this.dataSource.length > 0;
        },

        hasError() {
            return !!this.error;
        },

        canExport() {
            return this.hasData && this.stats.totalRows <= this.maxExportSize;
        },

        showExportWarning() {
            return this.hasData && !this.canExport;
        }
    },

    watch: {
        queryId: {
            immediate: true,
            handler(newId) {
                if (newId && this.autoLoad) {
                    this.loadResults();
                }
            }
        }
    },

    created() {
        this.debouncedRefresh = UtilService.debounce(this.refresh, 300);
    },

    beforeDestroy() {
        this.stopPolling();
    },

    methods: {
        // 加载查询结果
        async loadResults() {
            this.stopPolling();
            this.loading = true;
            this.error = null;

            try {
                const response = await QueryService.getQueryStatus(this.queryId);
                const status = response.data.status;

                if (status === 'RUNNING') {
                    this.startPolling();
                } else if (status === 'COMPLETED') {
                    await this.fetchResults();
                } else if (status === 'ERROR') {
                    this.error = response.data.error;
                }
            } catch (error) {
                console.error('加载查询结果失败:', error);
                this.error = error.response?.data?.message || '加载查询结果失败';
            } finally {
                this.loading = false;
            }
        },

        // 开始轮询查询状态
        startPolling() {
            this.pollingCount = 0;
            this.pollingTimer = setInterval(() => {
                this.pollQueryStatus();
            }, 3000);
        },

        // 停止轮询
        stopPolling() {
            if (this.pollingTimer) {
                clearInterval(this.pollingTimer);
                this.pollingTimer = null;
            }
        },

        // 轮询查询状态
        async pollQueryStatus() {
            try {
                const response = await QueryService.getQueryStatus(this.queryId);
                const status = response.data.status;

                if (status === 'COMPLETED') {
                    this.stopPolling();
                    await this.fetchResults();
                } else if (status === 'ERROR') {
                    this.stopPolling();
                    this.error = response.data.error;
                } else if (++this.pollingCount >= this.maxPollingCount) {
                    this.stopPolling();
                    this.error = '查询超时';
                }
            } catch (error) {
                console.error('轮询查询状态失败:', error);
                this.stopPolling();
                this.error = '获取查询状态失败';
            }
        },

        // 获取查询结果
        async fetchResults() {
            this.loading = true;
            
            try {
                const params = {
                    page: this.pagination.current - 1,
                    size: this.pagination.pageSize
                };

                if (this.sorter) {
                    params.sort = `${this.sorter.field},${this.sorter.order === 'ascend' ? 'asc' : 'desc'}`;
                }

                if (Object.keys(this.filters).length > 0) {
                    params.filters = this.filters;
                }

                const response = await QueryService.getQueryResults(this.queryId, params);
                this.handleResultsData(response.data);
            } catch (error) {
                console.error('获取查询结果失败:', error);
                this.error = error.response?.data?.message || '获取查询结果失败';
            } finally {
                this.loading = false;
            }
        },

        // 处理结果数据
        handleResultsData(data) {
            // 处理列定义
            this.columns = data.columns.map(col => ({
                title: col.name,
                dataIndex: col.name,
                key: col.name,
                sorter: true,
                ellipsis: !this.visualOptions.wrapText,
                filters: col.distinctValues?.map(v => ({
                    text: v === null ? '(空)' : v.toString(),
                    value: v
                })),
                onFilter: (value, record) => record[col.name] === value,
                render: (text, record) => this.renderCell(text, col)
            }));

            if (this.visualOptions.showRowNumbers) {
                this.columns.unshift({
                    title: '#',
                    dataIndex: 'rowIndex',
                    width: 60,
                    fixed: 'left'
                });
            }

            // 处理数据
            this.dataSource = data.rows.map((row, index) => ({
                key: index,
                rowIndex: (this.pagination.current - 1) * this.pagination.pageSize + index + 1,
                ...row
            }));

            // 更新分页信息
            this.pagination.total = data.total;

            // 更新统计信息
            this.stats = {
                executionTime: data.executionTime,
                totalRows: data.total,
                affectedRows: data.affectedRows
            };
        },

        // 渲染单元格
        renderCell(value, column) {
            if (value === null) {
                return (
                    <span class={this.visualOptions.highlightNulls ? 'null-value' : ''}>
                        (空)
                    </span>
                );
            }

            // 根据数据类型格式化
            switch (column.type) {
                case 'TIMESTAMP':
                case 'DATE':
                    return UtilService.formatDateTime(value);
                case 'DECIMAL':
                case 'NUMERIC':
                    return value.toLocaleString();
                case 'BOOLEAN':
                    return value ? '是' : '否';
                default:
                    return value.toString();
            }
        },

        // 处理表格变化
        handleTableChange(pagination, filters, sorter) {
            this.pagination.current = pagination.current;
            this.pagination.pageSize = pagination.pageSize;
            this.sorter = sorter;
            this.filters = Object.keys(filters).reduce((acc, key) => {
                if (filters[key]?.length) {
                    acc[key] = filters[key];
                }
                return acc;
            }, {});

            this.fetchResults();
        },

        // 刷新数据
        refresh() {
            this.pagination.current = 1;
            this.loadResults();
        },

        // 导出数据
        async exportData(format = 'excel') {
            if (!this.canExport) {
                this.$message.error(`导出数据不能超过 ${this.maxExportSize} 条`);
                return;
            }

            this.exporting = true;
            this.exportProgress = 0;

            try {
                // 获取导出URL
                const url = QueryService.getExportUrl(this.queryId, format);
                
                // 开始下载
                await UtilService.downloadFile(url, `query_results.${format}`);
                
                this.$message.success('导出成功');
            } catch (error) {
                console.error('导出数据失败:', error);
                this.$message.error('导出数据失败');
            } finally {
                this.exporting = false;
                this.exportProgress = 0;
            }
        },

        // 选择行变化
        handleSelectionChange(selectedRowKeys, selectedRows) {
            this.selectedRows = selectedRows;
        },

        // 复制选中数据
        async copySelectedData() {
            if (!this.selectedRows.length) {
                this.$message.warning('请先选择要复制的数据');
                return;
            }

            try {
                const headers = this.columns.map(col => col.title).join('\t');
                const rows = this.selectedRows.map(row => 
                    this.columns.map(col => row[col.dataIndex]).join('\t')
                );
                const text = [headers, ...rows].join('\n');

                await UtilService.copyToClipboard(text);
                this.$message.success('数据已复制到剪贴板');
            } catch (error) {
                console.error('复制数据失败:', error);
                this.$message.error('复制数据失败');
            }
        }
    },

    template: `
        <div class="results-preview">
            <!-- 工具栏 -->
            <div class="results-toolbar">
                <div class="toolbar-left">
                    <a-space>
                        <a-button
                            type="primary"
                            icon="reload"
                            :loading="loading"
                            @click="refresh"
                        >
                            刷新
                        </a-button>
                        <a-dropdown>
                            <a-menu slot="overlay">
                                <a-menu-item key="excel" @click="exportData('excel')">
                                    <a-icon type="file-excel" />导出Excel
                                </a-menu-item>
                                <a-menu-item key="csv" @click="exportData('csv')">
                                    <a-icon type="file-text" />导出CSV
                                </a-menu-item>
                            </a-menu>
                            <a-button :disabled="!canExport">
                                导出 <a-icon type="down" />
                            </a-button>
                        </a-dropdown>
                        <a-button
                            :disabled="!selectedRows.length"
                            @click="copySelectedData"
                        >
                            <a-icon type="copy" />复制选中
                        </a-button>
                    </a-space>
                </div>

                <div class="toolbar-right">
                    <a-space>
                        <a-tooltip title="显示行号">
                            <a-switch
                                v-model="visualOptions.showRowNumbers"
                                size="small"
                            >
                                <a-icon slot="checkedChildren" type="ordered-list" />
                                <a-icon slot="unCheckedChildren" type="ordered-list" />
                            </a-switch>
                        </a-tooltip>
                        <a-tooltip title="高亮空值">
                            <a-switch
                                v-model="visualOptions.highlightNulls"
                                size="small"
                            >
                                <a-icon slot="checkedChildren" type="highlight" />
                                <a-icon slot="unCheckedChildren" type="highlight" />
                            </a-switch>
                        </a-tooltip>
                        <a-tooltip title="文本换行">
                            <a-switch
                                v-model="visualOptions.wrapText"
                                size="small"
                            >
                                <a-icon slot="checkedChildren" type="align-left" />
                                <a-icon slot="unCheckedChildren" type="align-left" />
                            </a-switch>
                        </a-tooltip>
                    </a-space>
                </div>
            </div>

            <!-- 统计信息 -->
            <div v-if="visualOptions.showStats && hasData" class="results-stats">
                <a-row :gutter="16">
                    <a-col :span="8">
                        <a-statistic
                            title="执行时间"
                            :value="stats.executionTime"
                            :precision="2"
                            suffix="秒"
                        >
                            <template #prefix>
                                <a-icon type="clock-circle" />
                            </template>
                        </a-statistic>
                    </a-col>
                    <a-col :span="8">
                        <a-statistic
                            title="总记录数"
                            :value="stats.totalRows"
                            :formatter="val => val.toLocaleString()"
                        >
                            <template #prefix>
                                <a-icon type="database" />
                            </template>
                        </a-statistic>
                    </a-col>
                    <a-col :span="8">
                        <a-statistic
                            title="影响行数"
                            :value="stats.affectedRows"
                            :formatter="val => val.toLocaleString()"
                        >
                            <template #prefix>
                                <a-icon type="edit" />
                            </template>
                        </a-statistic>
                    </a-col>
                </a-row>
            </div>

            <!-- 导出警告 -->
            <a-alert
                v-if="showExportWarning"
                type="warning"
                style="margin-bottom: 16px"
            >
                数据量超过 {{ maxExportSize.toLocaleString() }} 条，请添加筛选条件后导出
            </a-alert>

            <!-- 结果表格 -->
            <div class="results-table">
                <a-table
                    :columns="columns"
                    :dataSource="dataSource"
                    :pagination="pagination"
                    :loading="loading"
                    :scroll="{ x: true }"
                    :row-selection="{
                        onChange: handleSelectionChange
                    }"
                    @change="handleTableChange"
                >
                    <template v-if="!hasData && !loading" slot="emptyText">
                        <a-empty v-if="hasError" :description="error" />
                        <a-empty v-else description="暂无数据" />
                    </template>
                </a-table>
            </div>

            <!-- 导出进度 -->
            <a-modal
                v-model="exporting"
                title="正在导出"
                :closable="false"
                :maskClosable="false"
                :footer="null"
            >
                <a-progress
                    :percent="exportProgress"
                    status="active"
                />
                <div style="text-align: center; margin-top: 16px">
                    正在导出数据，请稍候...
                </div>
            </a-modal>
        </div>
    `
};

// 导入依赖
import QueryService from '../services/query-service.js';
import UtilService from '../services/util-service.js';

// 注册组件
Vue.component('results-preview', ResultsPreview);