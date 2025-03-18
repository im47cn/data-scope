/**
 * 数据库模式浏览器组件
 * 提供数据库表结构和关系的可视化浏览功能
 */
const SchemaExplorer = {
    props: {
        // 数据源ID
        dataSourceId: {
            type: String,
            required: true
        },
        // 选中的模式
        selectedSchema: {
            type: String,
            default: 'public'
        },
        // 初始加载的表
        initialTables: {
            type: Array,
            default: () => []
        }
    },

    data() {
        return {
            loading: false,
            schemas: [],
            tables: [],
            selectedTable: null,
            expandedTables: new Set(),
            searchText: '',
            viewMode: 'list', // list, diagram
            // 图表实例
            diagram: null,
            // 表排序和过滤选项
            sortBy: 'name', // name, type, size
            sortOrder: 'asc',
            filterOptions: {
                showSystem: false,
                showEmpty: true,
                showViews: true
            },
            // 表信息缓存
            tableCache: new Map(),
            // 加载状态跟踪
            loadingStates: new Map(),
            // 统计信息
            statistics: {
                totalTables: 0,
                totalColumns: 0,
                totalRelationships: 0,
                lastUpdate: null
            }
        };
    },

    computed: {
        // 过滤后的表列表
        filteredTables() {
            let tables = [...this.tables];

            // 应用搜索过滤
            if (this.searchText) {
                const searchLower = this.searchText.toLowerCase();
                tables = tables.filter(table => 
                    table.name.toLowerCase().includes(searchLower) ||
                    table.comment?.toLowerCase().includes(searchLower)
                );
            }

            // 应用类型过滤
            if (!this.filterOptions.showSystem) {
                tables = tables.filter(table => !table.isSystem);
            }
            if (!this.filterOptions.showEmpty) {
                tables = tables.filter(table => table.rowCount > 0);
            }
            if (!this.filterOptions.showViews) {
                tables = tables.filter(table => table.type === 'TABLE');
            }

            // 应用排序
            tables.sort((a, b) => {
                let comparison = 0;
                switch (this.sortBy) {
                    case 'name':
                        comparison = a.name.localeCompare(b.name);
                        break;
                    case 'type':
                        comparison = a.type.localeCompare(b.type);
                        break;
                    case 'size':
                        comparison = (a.size || 0) - (b.size || 0);
                        break;
                }
                return this.sortOrder === 'asc' ? comparison : -comparison;
            });

            return tables;
        },

        // 是否显示关系图
        showDiagram() {
            return this.viewMode === 'diagram' && this.expandedTables.size > 0;
        }
    },

    watch: {
        selectedSchema: {
            handler: 'loadTables',
            immediate: true
        },

        showDiagram(newValue) {
            if (newValue) {
                this.$nextTick(() => {
                    this.initDiagram();
                });
            }
        }
    },

    created() {
        this.loadSchemas();
        this.debouncedSearch = UtilService.debounce(this.handleSearch, 300);
    },

    beforeDestroy() {
        if (this.diagram) {
            this.diagram.destroy();
        }
    },

    methods: {
        // 加载数据库模式列表
        async loadSchemas() {
            this.loading = true;
            try {
                const response = await DataSourceService.getSchemas(this.dataSourceId);
                this.schemas = response.data;
                this.updateStatistics();
            } catch (error) {
                console.error('加载数据库模式失败:', error);
                this.$message.error('加载数据库模式失败');
            } finally {
                this.loading = false;
            }
        },

        // 加载表列表
        async loadTables() {
            if (!this.selectedSchema) return;

            this.loading = true;
            try {
                const response = await DataSourceService.getTables(
                    this.dataSourceId,
                    this.selectedSchema
                );
                this.tables = response.data;
                
                // 加载初始表的详细信息
                if (this.initialTables.length > 0) {
                    await Promise.all(
                        this.initialTables.map(table => this.loadTableDetails(table))
                    );
                }

                this.updateStatistics();
            } catch (error) {
                console.error('加载表列表失败:', error);
                this.$message.error('加载表列表失败');
            } finally {
                this.loading = false;
            }
        },

        // 加载表详情
        async loadTableDetails(table) {
            if (this.tableCache.has(table.name)) {
                return this.tableCache.get(table.name);
            }

            const loadingKey = `table-${table.name}`;
            if (this.loadingStates.get(loadingKey)) {
                return;
            }

            this.loadingStates.set(loadingKey, true);
            try {
                const response = await DataSourceService.getTableDetails(
                    this.dataSourceId,
                    this.selectedSchema,
                    table.name
                );
                
                const details = response.data;
                this.tableCache.set(table.name, details);
                this.updateTableInList(details);
                return details;
            } catch (error) {
                console.error('加载表详情失败:', error);
                this.$message.error(`加载表 ${table.name} 详情失败`);
            } finally {
                this.loadingStates.set(loadingKey, false);
            }
        },

        // 更新列表中的表信息
        updateTableInList(tableDetails) {
            const index = this.tables.findIndex(t => t.name === tableDetails.name);
            if (index !== -1) {
                this.tables.splice(index, 1, {
                    ...this.tables[index],
                    ...tableDetails
                });
            }
        },

        // 处理表展开/折叠
        async handleTableExpand(table) {
            if (this.expandedTables.has(table.name)) {
                this.expandedTables.delete(table.name);
            } else {
                this.expandedTables.add(table.name);
                await this.loadTableDetails(table);
            }

            if (this.showDiagram) {
                this.updateDiagram();
            }
        },

        // 处理表选择
        async handleTableSelect(table) {
            this.selectedTable = table;
            await this.loadTableDetails(table);
            this.$emit('select', table);
        },

        // 初始化关系图
        initDiagram() {
            if (!this.$refs.diagram || !window.mxGraph) return;

            // 使用 mxGraph 初始化图表
            const container = this.$refs.diagram;
            this.diagram = new mxGraph(container);
            
            // 配置图表样式和行为
            this.diagram.setEnabled(true);
            this.diagram.setPanning(true);
            this.diagram.setTooltips(true);
            this.diagram.panningHandler.useLeftButtonForPanning = true;

            // 设置默认样式
            const style = this.diagram.getStylesheet().getDefaultVertexStyle();
            style[mxConstants.STYLE_FILLCOLOR] = '#ffffff';
            style[mxConstants.STYLE_STROKECOLOR] = '#000000';
            style[mxConstants.STYLE_FONTCOLOR] = '#000000';
            style[mxConstants.STYLE_ROUNDED] = true;
            style[mxConstants.STYLE_SHADOW] = true;

            this.updateDiagram();
        },

        // 更新关系图
        updateDiagram() {
            if (!this.diagram) return;

            const graph = this.diagram;
            const parent = graph.getDefaultParent();

            graph.getModel().beginUpdate();
            try {
                // 清除现有内容
                graph.removeCells(graph.getChildVertices(parent));

                // 创建表节点
                const tableNodes = new Map();
                const expandedTables = Array.from(this.expandedTables)
                    .map(name => this.tables.find(t => t.name === name))
                    .filter(Boolean);

                expandedTables.forEach((table, index) => {
                    const node = graph.insertVertex(
                        parent,
                        null,
                        this.createTableHtml(table),
                        50 + index * 200,
                        50,
                        180,
                        30 + table.columns.length * 20,
                        'table'
                    );
                    tableNodes.set(table.name, node);
                });

                // 创建关系连线
                expandedTables.forEach(table => {
                    if (!table.relationships) return;

                    table.relationships.forEach(rel => {
                        const targetTable = expandedTables.find(t => 
                            t.name === rel.targetTable
                        );
                        if (!targetTable) return;

                        const source = tableNodes.get(table.name);
                        const target = tableNodes.get(targetTable.name);
                        
                        if (source && target) {
                            graph.insertEdge(
                                parent,
                                null,
                                rel.type,
                                source,
                                target,
                                `relationship ${rel.type.toLowerCase()}`
                            );
                        }
                    });
                });

                // 应用自动布局
                new mxHierarchicalLayout(graph).execute(parent);
            } finally {
                graph.getModel().endUpdate();
            }
        },

        // 创建表HTML
        createTableHtml(table) {
            const rows = table.columns.map(col => `
                <tr>
                    <td class="column-name">
                        ${col.name}
                        ${col.primaryKey ? '🔑' : ''}
                    </td>
                    <td class="column-type">${col.type}</td>
                </tr>
            `).join('');

            return `
                <div class="table-node">
                    <div class="table-header">${table.name}</div>
                    <table class="table-columns">
                        <tbody>${rows}</tbody>
                    </table>
                </div>
            `;
        },

        // 处理搜索
        handleSearch() {
            // 搜索逻辑已通过计算属性实现
        },

        // 更新统计信息
        updateStatistics() {
            this.statistics = {
                totalTables: this.tables.length,
                totalColumns: this.tables.reduce((sum, table) => 
                    sum + (table.columns?.length || 0), 0
                ),
                totalRelationships: this.tables.reduce((sum, table) => 
                    sum + (table.relationships?.length || 0), 0
                ),
                lastUpdate: new Date()
            };
        },

        // 导出表结构
        async exportSchema(format = 'json') {
            try {
                const schema = {
                    dataSource: this.dataSourceId,
                    schema: this.selectedSchema,
                    tables: this.tables.map(table => ({
                        name: table.name,
                        type: table.type,
                        columns: table.columns || [],
                        relationships: table.relationships || [],
                        comment: table.comment
                    }))
                };

                if (format === 'json') {
                    const json = JSON.stringify(schema, null, 2);
                    await UtilService.downloadFile(
                        `data:text/json;charset=utf-8,${encodeURIComponent(json)}`,
                        `schema_${this.selectedSchema}.json`
                    );
                } else if (format === 'html') {
                    const html = this.generateSchemaHtml(schema);
                    await UtilService.downloadFile(
                        `data:text/html;charset=utf-8,${encodeURIComponent(html)}`,
                        `schema_${this.selectedSchema}.html`
                    );
                }

                this.$message.success('导出成功');
            } catch (error) {
                console.error('导出模式失败:', error);
                this.$message.error('导出模式失败');
            }
        },

        // 生成模式HTML文档
        generateSchemaHtml(schema) {
            return `
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="utf-8">
                    <title>数据库模式文档 - ${schema.schema}</title>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 20px; }
                        .table { margin-bottom: 30px; }
                        .table-name { font-size: 1.2em; font-weight: bold; }
                        table { border-collapse: collapse; width: 100%; }
                        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                        th { background-color: #f5f5f5; }
                    </style>
                </head>
                <body>
                    <h1>数据库模式文档</h1>
                    <p>模式名称: ${schema.schema}</p>
                    <p>生成时间: ${new Date().toLocaleString()}</p>
                    
                    ${schema.tables.map(table => `
                        <div class="table">
                            <div class="table-name">${table.name}</div>
                            <p>${table.comment || ''}</p>
                            <table>
                                <thead>
                                    <tr>
                                        <th>列名</th>
                                        <th>类型</th>
                                        <th>主键</th>
                                        <th>可空</th>
                                        <th>默认值</th>
                                        <th>注释</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    ${table.columns.map(col => `
                                        <tr>
                                            <td>${col.name}</td>
                                            <td>${col.type}</td>
                                            <td>${col.primaryKey ? '是' : ''}</td>
                                            <td>${col.nullable ? '是' : '否'}</td>
                                            <td>${col.defaultValue || ''}</td>
                                            <td>${col.comment || ''}</td>
                                        </tr>
                                    `).join('')}
                                </tbody>
                            </table>
                            
                            ${table.relationships.length ? `
                                <h3>关系</h3>
                                <ul>
                                    ${table.relationships.map(rel => `
                                        <li>${rel.type}: ${table.name}.${rel.sourceColumn} -> 
                                            ${rel.targetTable}.${rel.targetColumn}</li>
                                    `).join('')}
                                </ul>
                            ` : ''}
                        </div>
                    `).join('')}
                </body>
                </html>
            `;
        }
    },

    template: `
        <div class="schema-explorer">
            <!-- 工具栏 -->
            <div class="explorer-toolbar">
                <div class="toolbar-left">
                    <a-select
                        v-model="selectedSchema"
                        :loading="loading"
                        style="width: 200px"
                    >
                        <a-select-option
                            v-for="schema in schemas"
                            :key="schema.name"
                            :value="schema.name"
                        >
                            {{ schema.name }}
                        </a-select-option>
                    </a-select>

                    <a-input-search
                        v-model="searchText"
                        placeholder="搜索表"
                        style="width: 200px; margin-left: 16px"
                        @search="handleSearch"
                        allowClear
                    />
                </div>

                <div class="toolbar-right">
                    <a-space>
                        <a-radio-group v-model="viewMode" button-style="solid">
                            <a-radio-button value="list">
                                <a-icon type="unordered-list" />列表
                            </a-radio-button>
                            <a-radio-button value="diagram">
                                <a-icon type="deployment-unit" />关系图
                            </a-radio-button>
                        </a-radio-group>

                        <a-dropdown>
                            <a-button>
                                导出 <a-icon type="down" />
                            </a-button>
                            <a-menu slot="overlay">
                                <a-menu-item @click="exportSchema('json')">
                                    <a-icon type="code" />导出JSON
                                </a-menu-item>
                                <a-menu-item @click="exportSchema('html')">
                                    <a-icon type="html5" />导出HTML文档
                                </a-menu-item>
                            </a-menu>
                        </a-dropdown>
                    </a-space>
                </div>
            </div>

            <!-- 过滤选项 -->
            <div class="explorer-filters">
                <a-space>
                    <a-select
                        v-model="sortBy"
                        style="width: 120px"
                    >
                        <a-select-option value="name">按名称</a-select-option>
                        <a-select-option value="type">按类型</a-select-option>
                        <a-select-option value="size">按大小</a-select-option>
                    </a-select>

                    <a-button
                        type="link"
                        @click="sortOrder = sortOrder === 'asc' ? 'desc' : 'asc'"
                    >
                        <a-icon :type="sortOrder === 'asc' ? 'sort-ascending' : 'sort-descending'" />
                    </a-button>

                    <a-divider type="vertical" />

                    <a-checkbox
                        v-model="filterOptions.showSystem"
                    >
                        显示系统表
                    </a-checkbox>

                    <a-checkbox
                        v-model="filterOptions.showEmpty"
                    >
                        显示空表
                    </a-checkbox>

                    <a-checkbox
                        v-model="filterOptions.showViews"
                    >
                        显示视图
                    </a-checkbox>
                </a-space>
            </div>

            <!-- 主体内容 -->
            <div class="explorer-main">
                <!-- 列表视图 -->
                <div v-show="viewMode === 'list'" class="table-list">
                    <a-list
                        :dataSource="filteredTables"
                        :loading="loading"
                    >
                        <a-list-item slot="renderItem" slot-scope="table">
                            <div class="table-item" @click="handleTableSelect(table)">
                                <div class="table-info">
                                    <span class="table-name">
                                        <a-icon 
                                            :type="table.type === 'TABLE' ? 'table' : 'container'"
                                        />
                                        {{ table.name }}
                                    </span>
                                    <span v-if="table.comment" class="table-comment">
                                        {{ table.comment }}
                                    </span>
                                </div>

                                <div class="table-meta">
                                    <a-tag>{{ table.type }}</a-tag>
                                    <span v-if="table.rowCount !== undefined">
                                        {{ table.rowCount.toLocaleString() }} 行
                                    </span>
                                    <span v-if="table.size !== undefined">
                                        {{ UtilService.formatFileSize(table.size) }}
                                    </span>
                                </div>

                                <div class="table-actions">
                                    <a-button
                                        type="link"
                                        @click.stop="handleTableExpand(table)"
                                    >
                                        <a-icon :type="expandedTables.has(table.name) ? 'minus' : 'plus'" />
                                    </a-button>
                                </div>
                            </div>

                            <!-- 展开的表详情 -->
                            <div 
                                v-show="expandedTables.has(table.name)"
                                class="table-details"
                            >
                                <a-spin :spinning="loadingStates.get(`table-${table.name}`)">
                                    <a-tabs>
                                        <a-tab-pane key="columns" tab="列">
                                            <a-table
                                                :columns="[
                                                    { title: '名称', dataIndex: 'name' },
                                                    { title: '类型', dataIndex: 'type' },
                                                    { title: '可空', dataIndex: 'nullable' },
                                                    { title: '默认值', dataIndex: 'defaultValue' },
                                                    { title: '注释', dataIndex: 'comment' }
                                                ]"
                                                :dataSource="table.columns || []"
                                                size="small"
                                                :pagination="false"
                                            >
                                                <template slot="name" slot-scope="name, column">
                                                    {{ name }}
                                                    <a-icon v-if="column.primaryKey" type="key" />
                                                </template>
                                            </a-table>
                                        </a-tab-pane>

                                        <a-tab-pane key="relationships" tab="关系">
                                            <a-empty 
                                                v-if="!table.relationships?.length"
                                                description="无关系"
                                            />
                                            <a-timeline v-else>
                                                <a-timeline-item
                                                    v-for="rel in table.relationships"
                                                    :key="rel.targetTable + rel.targetColumn"
                                                >
                                                    {{ rel.type }}: {{ table.name }}.{{ rel.sourceColumn }}
                                                    -> {{ rel.targetTable }}.{{ rel.targetColumn }}
                                                </a-timeline-item>
                                            </a-timeline>
                                        </a-tab-pane>

                                        <a-tab-pane key="indexes" tab="索引">
                                            <a-empty 
                                                v-if="!table.indexes?.length"
                                                description="无索引"
                                            />
                                            <a-list v-else size="small">
                                                <a-list-item
                                                    v-for="index in table.indexes"
                                                    :key="index.name"
                                                >
                                                    <a-tag :color="index.unique ? 'blue' : ''">
                                                        {{ index.name }}
                                                    </a-tag>
                                                    {{ index.columns.join(', ') }}
                                                </a-list-item>
                                            </a-list>
                                        </a-tab-pane>
                                    </a-tabs>
                                </a-spin>
                            </div>
                        </a-list-item>
                    </a-list>
                </div>

                <!-- 关系图视图 -->
                <div
                    v-show="viewMode === 'diagram'"
                    class="relationship-diagram"
                >
                    <div v-if="!expandedTables.size" class="diagram-placeholder">
                        <a-empty description="请展开表以查看关系图" />
                    </div>
                    <div v-else ref="diagram" class="diagram-container"></div>
                </div>
            </div>

            <!-- 统计信息 -->
            <div class="explorer-footer">
                <a-row :gutter="16">
                    <a-col :span="8">
                        <a-statistic
                            title="表数量"
                            :value="statistics.totalTables"
                        >
                            <template #prefix>
                                <a-icon type="table" />
                            </template>
                        </a-statistic>
                    </a-col>
                    <a-col :span="8">
                        <a-statistic
                            title="总字段数"
                            :value="statistics.totalColumns"
                        >
                            <template #prefix>
                                <a-icon type="field-binary" />
                            </template>
                        </a-statistic>
                    </a-col>
                    <a-col :span="8">
                        <a-statistic
                            title="关系数"
                            :value="statistics.totalRelationships"
                        >
                            <template #prefix>
                                <a-icon type="deployment-unit" />
                            </template>
                        </a-statistic>
                    </a-col>
                </a-row>
            </div>
        </div>
    `
};

// 导入依赖
import DataSourceService from '../services/datasource-service.js';
import UtilService from '../services/util-service.js';

// 注册组件
Vue.component('schema-explorer', SchemaExplorer);