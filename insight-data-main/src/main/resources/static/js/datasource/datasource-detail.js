/**
 * 数据源详情组件
 * 展示数据源信息、元数据和同步状态
 */
const DataSourceDetail = {
    props: {
        dataSourceId: {
            type: String,
            required: true
        }
    },

    data() {
        return {
            loading: false,
            dataSource: null,
            metadataStats: null,
            syncHistory: [],
            activeTab: 'basic',
            // 元数据树相关状态
            metadataLoading: false,
            metadataTree: [],
            selectedSchema: null,
            selectedTable: null,
            tableDetails: null,
            // 分页配置
            pagination: {
                current: 1,
                pageSize: 10,
                total: 0,
                showSizeChanger: true,
                showQuickJumper: true,
                showTotal: total => `共 ${total} 条记录`
            },
            // 表格列定义
            tableColumns: [
                { title: '列名', dataIndex: 'name', key: 'name', width: '20%' },
                { title: '类型', dataIndex: 'type', key: 'type', width: '15%' },
                { title: '可空', dataIndex: 'nullable', key: 'nullable', width: '10%',
                    scopedSlots: { customRender: 'nullable' } },
                { title: '主键', dataIndex: 'primaryKey', key: 'primaryKey', width: '10%',
                    scopedSlots: { customRender: 'primaryKey' } },
                { title: '默认值', dataIndex: 'defaultValue', key: 'defaultValue', width: '15%' },
                { title: '注释', dataIndex: 'comment', key: 'comment', width: '30%', ellipsis: true }
            ],
            // 同步历史表格列定义
            syncHistoryColumns: [
                { title: '开始时间', dataIndex: 'startTime', key: 'startTime', width: '20%',
                    scopedSlots: { customRender: 'startTime' } },
                { title: '耗时', dataIndex: 'duration', key: 'duration', width: '15%',
                    customRender: (text, record) => UtilService.formatDuration(record.startTime, record.endTime) },
                { title: '状态', dataIndex: 'status', key: 'status', width: '15%',
                    scopedSlots: { customRender: 'status' } },
                { title: '同步类型', dataIndex: 'syncType', key: 'syncType', width: '15%' },
                { title: '结果', dataIndex: 'result', key: 'result', width: '35%', ellipsis: true }
            ]
        };
    },

    computed: {
        isSyncing() {
            return this.dataSource?.syncStatus && 
                ['PENDING', 'RUNNING'].includes(this.dataSource.syncStatus.status);
        }
    },

    created() {
        this.debouncedRefresh = UtilService.debounce(this.fetchDataSource, 1000);
        this.fetchDataSource();
        this.fetchSyncHistory();
        this.startPolling();
    },

    beforeDestroy() {
        this.stopPolling();
    },

    methods: {
        startPolling() {
            if (this.pollingTimer) {
                clearInterval(this.pollingTimer);
            }
            this.pollingTimer = setInterval(() => {
                if (this.isSyncing) {
                    this.debouncedRefresh();
                }
            }, 3000);
        },

        stopPolling() {
            if (this.pollingTimer) {
                clearInterval(this.pollingTimer);
                this.pollingTimer = null;
            }
        },

        async fetchDataSource() {
            this.loading = true;
            try {
                const [dataSourceRes, statsRes] = await Promise.all([
                    DataSourceService.getDataSource(this.dataSourceId),
                    DataSourceService.getMetadataStats(this.dataSourceId)
                ]);
                this.dataSource = dataSourceRes.data;
                this.metadataStats = statsRes.data;
            } catch (error) {
                console.error('获取数据源详情失败:', error);
                this.$message.error('获取数据源详情失败');
            } finally {
                this.loading = false;
            }
        },

        async fetchSyncHistory() {
            try {
                const response = await DataSourceService.getSyncHistory(this.dataSourceId, {
                    page: this.pagination.current - 1,
                    size: this.pagination.pageSize
                });
                this.syncHistory = response.data.content;
                this.pagination.total = response.data.pageable.totalElements;
            } catch (error) {
                console.error('获取同步历史失败:', error);
                this.$message.error('获取同步历史失败');
            }
        },

        async handleSync() {
            if (this.isSyncing) {
                this.$message.warning('同步任务正在进行中');
                return;
            }

            try {
                await DataSourceService.syncMetadata(this.dataSourceId);
                this.$message.success('同步任务已启动');
                await this.fetchDataSource();
            } catch (error) {
                console.error('启动同步任务失败:', error);
                this.$message.error('启动同步任务失败');
            }
        },

        async handleTabChange(key) {
            this.activeTab = key;
            if (key === 'metadata' && !this.metadataTree.length) {
                await this.loadMetadataTree();
            } else if (key === 'syncHistory') {
                await this.fetchSyncHistory();
            }
        },

        async loadMetadataTree() {
            this.metadataLoading = true;
            try {
                const response = await DataSourceService.getMetadataTree(this.dataSourceId);
                this.metadataTree = this.transformMetadataTree(response.data);
            } catch (error) {
                console.error('加载元数据树失败:', error);
                this.$message.error('加载元数据树失败');
            } finally {
                this.metadataLoading = false;
            }
        },

        transformMetadataTree(data) {
            return data.map(schema => ({
                key: `schema-${schema.name}`,
                title: schema.name,
                type: 'schema',
                children: schema.tables.map(table => ({
                    key: `table-${schema.name}-${table.name}`,
                    title: table.name,
                    type: 'table',
                    schema: schema.name,
                    isLeaf: true,
                    icon: 'table'
                }))
            }));
        },

        async handleTreeSelect(selectedKeys, { node }) {
            if (node.type === 'table') {
                this.selectedSchema = node.schema;
                this.selectedTable = node.title;
                await this.loadTableDetails(node.schema, node.title);
            }
        },

        async loadTableDetails(schema, table) {
            this.metadataLoading = true;
            try {
                const response = await DataSourceService.getTableDetails(
                    this.dataSourceId,
                    schema,
                    table
                );
                this.tableDetails = response.data;
            } catch (error) {
                console.error('加载表详情失败:', error);
                this.$message.error('加载表详情失败');
            } finally {
                this.metadataLoading = false;
            }
        },

        handleEdit() {
            this.$router.push(`/datasource/edit/${this.dataSourceId}`);
        },

        handleTableChange(pagination) {
            this.pagination.current = pagination.current;
            this.pagination.pageSize = pagination.pageSize;
            this.fetchSyncHistory();
        }
    },

    template: `
        <div class="datasource-detail-container">
            <a-spin :spinning="loading">
                <div class="page-header">
                    <h1>
                        {{ dataSource?.name || '数据源详情' }}
                        <a-tag :color="dataSource?.active ? 'success' : 'default'">
                            {{ dataSource?.active ? '激活' : '禁用' }}
                        </a-tag>
                    </h1>
                    <a-space>
                        <a-button type="primary" @click="handleEdit">
                            <a-icon type="edit" />编辑
                        </a-button>
                        <a-button @click="handleSync" :loading="isSyncing">
                            <a-icon type="sync" :spin="isSyncing" />同步元数据
                        </a-button>
                    </a-space>
                </div>

                <a-tabs :activeKey="activeTab" @change="handleTabChange">
                    <!-- 基本信息 -->
                    <a-tab-pane key="basic" tab="基本信息">
                        <a-descriptions :column="2" bordered>
                            <a-descriptions-item label="数据源类型">
                                {{ dataSource?.type }}
                            </a-descriptions-item>
                            <a-descriptions-item label="连接地址">
                                {{ dataSource?.host }}:{{ dataSource?.port }}
                            </a-descriptions-item>
                            <a-descriptions-item label="数据库名称">
                                {{ dataSource?.databaseName }}
                            </a-descriptions-item>
                            <a-descriptions-item label="用户名">
                                {{ dataSource?.username }}
                            </a-descriptions-item>
                            <a-descriptions-item label="创建时间">
                                {{ UtilService.formatDateTime(dataSource?.createdAt) }}
                            </a-descriptions-item>
                            <a-descriptions-item label="最后更新">
                                {{ UtilService.formatDateTime(dataSource?.updatedAt) }}
                            </a-descriptions-item>
                            <a-descriptions-item label="最后同步">
                                {{ UtilService.formatDateTime(dataSource?.lastSyncedAt) }}
                            </a-descriptions-item>
                            <a-descriptions-item label="标签">
                                <template v-if="dataSource?.tags?.length">
                                    <a-tag v-for="tag in dataSource.tags" :key="tag">
                                        {{ tag }}
                                    </a-tag>
                                </template>
                                <span v-else>-</span>
                            </a-descriptions-item>
                            <a-descriptions-item label="同步状态" :span="2">
                                <sync-status-badge
                                    :status="dataSource?.syncStatus || { status: 'UNKNOWN' }"
                                    :showProgress="true"
                                    :showDetail="true"
                                />
                            </a-descriptions-item>
                            <a-descriptions-item label="描述" :span="2">
                                {{ dataSource?.description || '-' }}
                            </a-descriptions-item>
                        </a-descriptions>

                        <!-- 元数据统计 -->
                        <div class="metadata-stats" style="margin-top: 24px">
                            <h3>元数据统计</h3>
                            <a-row :gutter="16">
                                <a-col :span="6">
                                    <a-statistic
                                        title="模式数"
                                        :value="metadataStats?.schemaCount || 0"
                                        :valueStyle="{ color: '#1890ff' }"
                                    >
                                        <template #prefix>
                                            <a-icon type="database" />
                                        </template>
                                    </a-statistic>
                                </a-col>
                                <a-col :span="6">
                                    <a-statistic
                                        title="表数量"
                                        :value="metadataStats?.tableCount || 0"
                                        :valueStyle="{ color: '#52c41a' }"
                                    >
                                        <template #prefix>
                                            <a-icon type="table" />
                                        </template>
                                    </a-statistic>
                                </a-col>
                                <a-col :span="6">
                                    <a-statistic
                                        title="视图数量"
                                        :value="metadataStats?.viewCount || 0"
                                        :valueStyle="{ color: '#722ed1' }"
                                    >
                                        <template #prefix>
                                            <a-icon type="eye" />
                                        </template>
                                    </a-statistic>
                                </a-col>
                                <a-col :span="6">
                                    <a-statistic
                                        title="列总数"
                                        :value="metadataStats?.columnCount || 0"
                                        :valueStyle="{ color: '#fa8c16' }"
                                    >
                                        <template #prefix>
                                            <a-icon type="bars" />
                                        </template>
                                    </a-statistic>
                                </a-col>
                            </a-row>
                        </div>
                    </a-tab-pane>

                    <!-- 元数据浏览 -->
                    <a-tab-pane key="metadata" tab="元数据浏览" forceRender>
                        <a-row :gutter="16">
                            <!-- 元数据树 -->
                            <a-col :span="6">
                                <a-card :loading="metadataLoading" :bordered="false">
                                    <a-tree
                                        :treeData="metadataTree"
                                        :defaultExpandAll="true"
                                        @select="handleTreeSelect"
                                    >
                                        <template slot="title" slot-scope="{ title, type }">
                                            <a-icon :type="type === 'schema' ? 'database' : 'table'" /> {{ title }}
                                        </template>
                                    </a-tree>
                                </a-card>
                            </a-col>

                            <!-- 表详情 -->
                            <a-col :span="18">
                                <a-spin :spinning="metadataLoading">
                                    <template v-if="selectedTable">
                                        <a-card :bordered="false">
                                            <template slot="title">
                                                <span>{{ selectedSchema }}.{{ selectedTable }}</span>
                                            </template>
                                            <a-tabs>
                                                <a-tab-pane key="columns" tab="列信息">
                                                    <a-table
                                                        :columns="tableColumns"
                                                        :dataSource="tableDetails?.columns"
                                                        :pagination="false"
                                                        size="middle"
                                                    >
                                                        <template slot="nullable" slot-scope="text">
                                                            <a-badge :status="text ? 'default' : 'error'" />
                                                            {{ text ? '是' : '否' }}
                                                        </template>
                                                        <template slot="primaryKey" slot-scope="text">
                                                            <a-badge :status="text ? 'success' : 'default'" />
                                                            {{ text ? '是' : '否' }}
                                                        </template>
                                                    </a-table>
                                                </a-tab-pane>
                                                <a-tab-pane key="indexes" tab="索引">
                                                    <a-table
                                                        :columns="[
                                                            { title: '索引名称', dataIndex: 'name', key: 'name' },
                                                            { title: '类型', dataIndex: 'type', key: 'type' },
                                                            { title: '唯一性', dataIndex: 'unique', key: 'unique',
                                                                scopedSlots: { customRender: 'unique' } },
                                                            { title: '列', dataIndex: 'columns', key: 'columns',
                                                                scopedSlots: { customRender: 'columns' } }
                                                        ]"
                                                        :dataSource="tableDetails?.indexes"
                                                        :pagination="false"
                                                        size="middle"
                                                    >
                                                        <template slot="unique" slot-scope="text">
                                                            <a-badge :status="text ? 'success' : 'default'" />
                                                            {{ text ? '是' : '否' }}
                                                        </template>
                                                        <template slot="columns" slot-scope="columns">
                                                            <a-tag v-for="col in columns" :key="col">{{ col }}</a-tag>
                                                        </template>
                                                    </a-table>
                                                </a-tab-pane>
                                            </a-tabs>
                                        </a-card>
                                    </template>
                                    <template v-else>
                                        <a-empty description="请选择要查看的表" />
                                    </template>
                                </a-spin>
                            </a-col>
                        </a-row>
                    </a-tab-pane>

                    <!-- 同步历史 -->
                    <a-tab-pane key="syncHistory" tab="同步历史">
                        <a-table
                            :columns="syncHistoryColumns"
                            :dataSource="syncHistory"
                            :pagination="pagination"
                            @change="handleTableChange"
                        >
                            <template slot="startTime" slot-scope="text">
                                {{ UtilService.formatDateTime(text) }}
                            </template>
                            <template slot="status" slot-scope="text, record">
                                <sync-status-badge
                                    :status="{ status: text }"
                                    :showDetail="false"
                                />
                            </template>
                        </a-table>
                    </a-tab-pane>
                </a-tabs>
            </a-spin>
        </div>
    `
};

// 导入依赖
import DataSourceService from '../services/datasource-service.js';
import UtilService from '../services/util-service.js';

// 注册组件
Vue.component('datasource-detail', DataSourceDetail);
