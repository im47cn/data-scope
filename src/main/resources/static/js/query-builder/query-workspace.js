/**
 * 查询构建器工作区组件
 * 提供可视化查询构建界面
 */
const QueryWorkspace = {
    props: {
        // 数据源ID
        dataSourceId: {
            type: String,
            required: true
        },
        // 初始查询配置（用于加载已保存的查询）
        initialQuery: {
            type: Object,
            default: () => null
        }
    },

    data() {
        return {
            loading: false,
            schemas: [],
            selectedSchema: null,
            tables: [],
            // 查询配置
            queryConfig: {
                tables: [],
                joins: [],
                conditions: {
                    logic: 'AND',
                    conditions: []
                },
                groupBy: [],
                orderBy: [],
                limit: 100
            },
            // 工作区状态
            workspace: {
                scale: 1,
                position: { x: 0, y: 0 },
                selectedNode: null,
                draggingNode: null,
                creatingJoin: null,
                nodes: new Map(), // 存储节点位置信息
                edges: new Map()  // 存储连接线信息
            },
            // 预览状态
            preview: {
                loading: false,
                sql: '',
                valid: true,
                errors: [],
                warnings: []
            },
            // 自动布局配置
            layout: {
                type: 'dagre',
                rankdir: 'LR',
                align: 'UL',
                ranksep: 50,
                nodesep: 50,
                edgesep: 10
            }
        };
    },

    computed: {
        hasQuery() {
            return this.queryConfig.tables.length > 0;
        },

        canExecute() {
            return this.hasQuery && this.preview.valid && !this.preview.loading;
        },

        workspaceStyle() {
            return {
                transform: `scale(${this.workspace.scale}) 
                           translate(${this.workspace.position.x}px, ${this.workspace.position.y}px)`
            };
        }
    },

    watch: {
        dataSourceId: {
            immediate: true,
            handler: 'loadSchemas'
        },

        selectedSchema(newSchema) {
            if (newSchema) {
                this.loadTables(newSchema);
            }
        },

        'queryConfig.tables': {
            deep: true,
            handler: 'handleQueryConfigChange'
        },

        'queryConfig.joins': {
            deep: true,
            handler: 'handleQueryConfigChange'
        },

        'queryConfig.conditions': {
            deep: true,
            handler: 'handleQueryConfigChange'
        }
    },

    created() {
        this.debouncedUpdatePreview = UtilService.debounce(this.updatePreview, 500);
        if (this.initialQuery) {
            this.loadSavedQuery(this.initialQuery);
        }
    },

    mounted() {
        this.initWorkspace();
        window.addEventListener('resize', this.handleResize);
    },

    beforeDestroy() {
        window.removeEventListener('resize', this.handleResize);
    },

    methods: {
        // 加载模式列表
        async loadSchemas() {
            if (!this.dataSourceId) return;
            
            this.loading = true;
            try {
                const response = await DataSourceService.getSchemas(this.dataSourceId);
                this.schemas = response.data;
                if (this.schemas.length > 0) {
                    this.selectedSchema = this.schemas[0].name;
                }
            } catch (error) {
                console.error('加载模式列表失败:', error);
                this.$message.error('加载模式列表失败');
            } finally {
                this.loading = false;
            }
        },

        // 加载表列表
        async loadTables(schema) {
            this.loading = true;
            try {
                const response = await DataSourceService.getTables(
                    this.dataSourceId,
                    schema
                );
                this.tables = response.data;
            } catch (error) {
                console.error('加载表列表失败:', error);
                this.$message.error('加载表列表失败');
            } finally {
                this.loading = false;
            }
        },

        // 加载已保存的查询
        loadSavedQuery(query) {
            this.queryConfig = UtilService.deepClone(query);
            this.$nextTick(() => {
                this.updateWorkspaceLayout();
            });
        },

        // 初始化工作区
        initWorkspace() {
            // 初始化画布大小和缩放
            this.handleResize();
            // 初始化事件监听
            this.initEventListeners();
        },

        // 初始化事件监听
        initEventListeners() {
            const workspace = this.$refs.workspace;
            if (!workspace) return;

            // 平移
            let isDragging = false;
            let startX = 0;
            let startY = 0;

            workspace.addEventListener('mousedown', (e) => {
                if (e.target === workspace) {
                    isDragging = true;
                    startX = e.clientX - this.workspace.position.x;
                    startY = e.clientY - this.workspace.position.y;
                }
            });

            workspace.addEventListener('mousemove', (e) => {
                if (isDragging) {
                    this.workspace.position.x = e.clientX - startX;
                    this.workspace.position.y = e.clientY - startY;
                }
            });

            workspace.addEventListener('mouseup', () => {
                isDragging = false;
            });

            // 缩放
            workspace.addEventListener('wheel', (e) => {
                if (e.ctrlKey) {
                    e.preventDefault();
                    const delta = e.deltaY > 0 ? -0.1 : 0.1;
                    this.workspace.scale = Math.max(0.5, Math.min(2, this.workspace.scale + delta));
                }
            });
        },

        // 处理窗口大小变化
        handleResize() {
            const workspace = this.$refs.workspace;
            if (!workspace) return;

            // 更新画布大小
            workspace.style.width = `${window.innerWidth}px`;
            workspace.style.height = `${window.innerHeight - 200}px`;

            // 更新布局
            this.updateWorkspaceLayout();
        },

        // 更新工作区布局
        updateWorkspaceLayout() {
            if (this.queryConfig.tables.length === 0) return;

            // 使用 dagre 布局算法计算节点位置
            const g = new dagre.graphlib.Graph();
            g.setGraph(this.layout);
            g.setDefaultEdgeLabel(() => ({}));

            // 添加节点
            this.queryConfig.tables.forEach(table => {
                g.setNode(table.id, { width: 200, height: 300 });
            });

            // 添加边
            this.queryConfig.joins.forEach(join => {
                g.setEdge(join.leftTableId, join.rightTableId);
            });

            // 计算布局
            dagre.layout(g);

            // 更新节点位置
            g.nodes().forEach(v => {
                const node = g.node(v);
                this.workspace.nodes.set(v, {
                    x: node.x,
                    y: node.y
                });
            });

            // 更新边位置
            this.workspace.edges.clear();
            g.edges().forEach(e => {
                const edge = g.edge(e);
                this.workspace.edges.set(`${e.v}-${e.w}`, {
                    points: edge.points
                });
            });
        },

        // 添加表到查询
        async addTable(table) {
            // 检查表是否已添加
            if (this.queryConfig.tables.some(t => t.name === table.name)) {
                this.$message.warning(`表 ${table.name} 已添加到查询中`);
                return;
            }

            try {
                // 获取表详情
                const response = await DataSourceService.getTableDetails(
                    this.dataSourceId,
                    this.selectedSchema,
                    table.name
                );

                // 创建表配置
                const tableId = UtilService.generateUniqueId();
                const tableConfig = {
                    id: tableId,
                    name: table.name,
                    alias: '',
                    columns: response.data.columns.map(column => ({
                        name: column.name,
                        alias: '',
                        selected: true,
                        aggregateFunction: ''
                    }))
                };

                // 添加到查询配置
                this.queryConfig.tables.push(tableConfig);

                // 如果已有表，尝试推断关系
                if (this.queryConfig.tables.length > 1) {
                    await this.inferTableRelationships(tableId);
                }

                // 更新布局
                this.$nextTick(() => {
                    this.updateWorkspaceLayout();
                });
            } catch (error) {
                console.error('添加表失败:', error);
                this.$message.error(`添加表 ${table.name} 失败`);
            }
        },

        // 推断表关系
        async inferTableRelationships(newTableId) {
            const newTable = this.queryConfig.tables.find(t => t.id === newTableId);
            if (!newTable) return;

            for (const existingTable of this.queryConfig.tables) {
                if (existingTable.id === newTableId) continue;

                try {
                    const response = await QueryService.inferTableRelationship(
                        this.dataSourceId,
                        existingTable.name,
                        newTable.name
                    );

                    if (response.data.relationships?.length > 0) {
                        // 使用置信度最高的关系
                        const bestRelationship = response.data.relationships[0];

                        // 创建连接配置
                        const joinConfig = {
                            id: UtilService.generateUniqueId(),
                            leftTableId: existingTable.id,
                            leftColumn: bestRelationship.leftColumn,
                            rightTableId: newTableId,
                            rightColumn: bestRelationship.rightColumn,
                            type: 'INNER'
                        };

                        // 添加到查询配置
                        this.queryConfig.joins.push(joinConfig);

                        this.$notification.info({
                            message: '表关系推荐',
                            description: `已自动添加表 ${existingTable.name} 和 ${newTable.name} 之间的关系`
                        });
                    }
                } catch (error) {
                    console.error('推断表关系失败:', error);
                }
            }
        },

        // 移除表
        removeTable(tableId) {
            // 找到表
            const tableIndex = this.queryConfig.tables.findIndex(t => t.id === tableId);
            if (tableIndex === -1) return;

            const table = this.queryConfig.tables[tableIndex];

            // 移除相关的连接
            this.queryConfig.joins = this.queryConfig.joins.filter(
                join => join.leftTableId !== tableId && join.rightTableId !== tableId
            );

            // 移除相关的条件
            this.removeConditionsForTable(tableId);

            // 移除相关的分组和排序
            this.queryConfig.groupBy = this.queryConfig.groupBy.filter(
                group => group.tableId !== tableId
            );
            this.queryConfig.orderBy = this.queryConfig.orderBy.filter(
                order => order.tableId !== tableId
            );

            // 从查询配置中移除表
            this.queryConfig.tables.splice(tableIndex, 1);

            // 移除节点和边的位置信息
            this.workspace.nodes.delete(tableId);
            this.workspace.edges.forEach((_, key) => {
                if (key.includes(tableId)) {
                    this.workspace.edges.delete(key);
                }
            });

            // 更新布局
            this.$nextTick(() => {
                this.updateWorkspaceLayout();
            });
        },

        // 移除表相关的条件
        removeConditionsForTable(tableId) {
            const removeConditionsRecursive = (conditionGroup) => {
                if (!conditionGroup || !conditionGroup.conditions) return conditionGroup;

                const filteredConditions = conditionGroup.conditions.filter(condition => {
                    if (condition.conditions) {
                        return removeConditionsRecursive(condition);
                    } else {
                        return condition.tableId !== tableId;
                    }
                });

                conditionGroup.conditions = filteredConditions;
                return conditionGroup;
            };

            this.queryConfig.conditions = removeConditionsRecursive(this.queryConfig.conditions);
        },

        // 处理查询配置变化
        handleQueryConfigChange() {
            this.debouncedUpdatePreview();
        },

        // 更新SQL预览
        async updatePreview() {
            if (this.queryConfig.tables.length === 0) {
                this.preview.sql = '';
                this.preview.valid = false;
                return;
            }

            this.preview.loading = true;
            try {
                const response = await QueryService.generateSql({
                    dataSourceId: this.dataSourceId,
                    schema: this.selectedSchema,
                    ...this.queryConfig
                });

                this.preview.sql = response.data.sql;
                this.preview.valid = response.data.valid;
                this.preview.errors = response.data.errors;
                this.preview.warnings = response.data.warnings;

                // 发出更新事件
                this.$emit('preview-updated', {
                    sql: this.preview.sql,
                    valid: this.preview.valid
                });
            } catch (error) {
                console.error('生成SQL预览失败:', error);
                this.preview.valid = false;
                this.preview.errors = [error.message || '生成SQL失败'];
            } finally {
                this.preview.loading = false;
            }
        },

        // 获取当前查询配置
        getQueryConfig() {
            return {
                dataSourceId: this.dataSourceId,
                schema: this.selectedSchema,
                ...UtilService.deepClone(this.queryConfig)
            };
        }
    },

    template: `
        <div class="query-workspace-container">
            <!-- 工具栏 -->
            <div class="workspace-toolbar">
                <a-select
                    v-model="selectedSchema"
                    placeholder="选择模式"
                    style="width: 200px"
                    :loading="loading"
                >
                    <a-select-option 
                        v-for="schema in schemas"
                        :key="schema.name"
                        :value="schema.name"
                    >
                        {{ schema.name }}
                    </a-select-option>
                </a-select>
            </div>

            <!-- 主工作区 -->
            <div class="workspace-main">
                <!-- 表列表侧边栏 -->
                <div class="workspace-sidebar">
                    <div class="sidebar-header">
                        <h3>可用表</h3>
                        <a-input-search
                            v-model="tableFilter"
                            placeholder="搜索表"
                            style="margin-bottom: 16px"
                        />
                    </div>
                    <div class="table-list">
                        <a-spin :spinning="loading">
                            <a-list
                                :dataSource="tables"
                                :locale="{ emptyText: '无可用表' }"
                            >
                                <a-list-item
                                    slot="renderItem"
                                    slot-scope="table"
                                    :key="table.name"
                                    class="table-list-item"
                                    draggable="true"
                                    @dragstart="handleTableDragStart(table)"
                                    @click="addTable(table)"
                                >
                                    <a-icon type="table" />
                                    <span>{{ table.name }}</span>
                                </a-list-item>
                            </a-list>
                        </a-spin>
                    </div>
                </div>

                <!-- 工作区画布 -->
                <div 
                    ref="workspace"
                    class="workspace-canvas"
                    @drop="handleTableDrop"
                    @dragover.prevent
                >
                    <div 
                        class="workspace-content"
                        :style="workspaceStyle"
                    >
                        <!-- 表节点 -->
                        <div
                            v-for="table in queryConfig.tables"
                            :key="table.id"
                            class="table-node"
                            :style="getNodeStyle(table.id)"
                            @mousedown="handleNodeMouseDown(table, $event)"
                        >
                            <div class="table-header">
                                <span class="table-name">{{ table.name }}</span>
                                <a-input
                                    v-model="table.alias"
                                    placeholder="别名"
                                    class="table-alias"
                                    @click.stop
                                />
                                <a-button
                                    type="link"
                                    size="small"
                                    @click.stop="removeTable(table.id)"
                                >
                                    <a-icon type="close" />
                                </a-button>
                            </div>
                            <div class="table-columns">
                                <div
                                    v-for="column in table.columns"
                                    :key="column.name"
                                    class="column-item"
                                    :class="{ selected: column.selected }"
                                    @click.stop="toggleColumn(table, column)"
                                >
                                    <a-checkbox
                                        v-model="column.selected"
                                        @click.stop
                                    />
                                    <span class="column-name">{{ column.name }}</span>
                                    <a-select
                                        v-if="column.selected"
                                        v-model="column.aggregateFunction"
                                        style="width: 90px"
                                        size="small"
                                        @click.stop
                                    >
                                        <a-select-option value="">无聚合</a-select-option>
                                        <a-select-option value="COUNT">COUNT</a-select-option>
                                        <a-select-option value="SUM">SUM</a-select-option>
                                        <a-select-option value="AVG">AVG</a-select-option>
                                        <a-select-option value="MAX">MAX</a-select-option>
                                        <a-select-option value="MIN">MIN</a-select-option>
                                    </a-select>
                                </div>
                            </div>
                        </div>

                        <!-- 连接线 -->
                        <svg class="joins-svg">
                            <g>
                                <path
                                    v-for="join in queryConfig.joins"
                                    :key="join.id"
                                    :d="getJoinPath(join)"
                                    class="join-path"
                                    @click="editJoin(join)"
                                />
                            </g>
                        </svg>
                    </div>
                </div>
            </div>
        </div>
    `
};

// 导入依赖
import DataSourceService from '../services/datasource-service.js';
import QueryService from '../services/query-service.js';
import UtilService from '../services/util-service.js';

// 注册组件
Vue.component('query-workspace', QueryWorkspace);