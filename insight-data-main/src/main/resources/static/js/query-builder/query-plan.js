/**
 * 查询计划可视化组件
 * 用于展示和分析SQL查询执行计划
 */
const QueryPlan = {
    props: {
        // 查询ID
        queryId: {
            type: String,
            required: true
        },
        // 查询SQL
        sql: {
            type: String,
            required: true
        },
        // 显示模式：tree或graph
        mode: {
            type: String,
            default: 'tree',
            validator: value => ['tree', 'graph'].includes(value)
        }
    },

    data() {
        return {
            loading: false,
            error: null,
            planData: null,
            // 图表实例
            chart: null,
            // 图表配置
            chartOptions: {
                tree: {
                    direction: 'TB',
                    nodeSep: 50,
                    rankSep: 70,
                    align: 'UL',
                    useMaxWidth: true
                },
                graph: {
                    rankdir: 'TB',
                    align: 'UL',
                    nodesep: 50,
                    ranksep: 70,
                    marginx: 20,
                    marginy: 20
                }
            },
            // 节点统计
            statistics: null,
            // 性能指标
            metrics: null,
            // 优化建议
            suggestions: []
        };
    },

    computed: {
        hasData() {
            return !!this.planData;
        },

        // 计算总成本
        totalCost() {
            return this.statistics?.totalCost || 0;
        },

        // 计算关键路径
        criticalPath() {
            if (!this.planData) return [];
            return this.findCriticalPath(this.planData);
        },

        // 是否有优化建议
        hasSuggestions() {
            return this.suggestions.length > 0;
        }
    },

    watch: {
        queryId: {
            immediate: true,
            handler: 'loadQueryPlan'
        },

        mode(newMode) {
            if (this.hasData) {
                this.$nextTick(() => {
                    this.renderPlan();
                });
            }
        }
    },

    created() {
        this.debouncedRender = UtilService.debounce(this.renderPlan, 300);
    },

    mounted() {
        window.addEventListener('resize', this.debouncedRender);
    },

    beforeDestroy() {
        window.removeEventListener('resize', this.debouncedRender);
        if (this.chart) {
            this.chart.destroy();
        }
    },

    methods: {
        // 加载查询计划
        async loadQueryPlan() {
            this.loading = true;
            this.error = null;

            try {
                const response = await QueryService.analyzeQuery({
                    queryId: this.queryId,
                    sql: this.sql
                });

                this.planData = response.data.plan;
                this.statistics = response.data.statistics;
                this.metrics = response.data.metrics;
                this.suggestions = response.data.suggestions || [];

                this.$nextTick(() => {
                    this.renderPlan();
                });
            } catch (error) {
                console.error('加载查询计划失败:', error);
                this.error = error.response?.data?.message || '加载查询计划失败';
            } finally {
                this.loading = false;
            }
        },

        // 渲染查询计划
        renderPlan() {
            if (!this.planData || !this.$refs.chart) return;

            if (this.chart) {
                this.chart.destroy();
            }

            const container = this.$refs.chart;
            const data = this.mode === 'tree' 
                ? this.transformToTree(this.planData)
                : this.transformToGraph(this.planData);

            if (this.mode === 'tree') {
                this.renderTreeChart(container, data);
            } else {
                this.renderGraphChart(container, data);
            }
        },

        // 将计划数据转换为树形结构
        transformToTree(node) {
            return {
                name: this.formatNodeLabel(node),
                children: (node.children || []).map(child => this.transformToTree(child)),
                ...this.extractNodeProperties(node)
            };
        },

        // 将计划数据转换为图结构
        transformToGraph(node, nodes = [], edges = [], parentId = null) {
            const currentId = `node-${nodes.length}`;
            
            nodes.push({
                id: currentId,
                label: this.formatNodeLabel(node),
                ...this.extractNodeProperties(node)
            });

            if (parentId) {
                edges.push({
                    from: parentId,
                    to: currentId,
                    value: node.rows || 1
                });
            }

            (node.children || []).forEach(child => {
                this.transformToGraph(child, nodes, edges, currentId);
            });

            return { nodes, edges };
        },

        // 格式化节点标签
        formatNodeLabel(node) {
            const parts = [];
            parts.push(node.operation);
            
            if (node.table) {
                parts.push(`[${node.table}]`);
            }
            
            if (node.cost) {
                parts.push(`成本: ${node.cost.toFixed(2)}`);
            }
            
            if (node.rows) {
                parts.push(`行数: ${node.rows.toLocaleString()}`);
            }

            return parts.join('\n');
        },

        // 提取节点属性
        extractNodeProperties(node) {
            return {
                cost: node.cost,
                rows: node.rows,
                width: node.width,
                condition: node.condition,
                isCritical: this.criticalPath.includes(node)
            };
        },

        // 渲染树形图
        renderTreeChart(container, data) {
            this.chart = new D3Tree(container, {
                data,
                options: {
                    ...this.chartOptions.tree,
                    nodeColor: node => this.getNodeColor(node),
                    nodeSize: node => this.getNodeSize(node),
                    onClick: node => this.handleNodeClick(node)
                }
            });
        },

        // 渲染图形图
        renderGraphChart(container, data) {
            this.chart = new D3Graph(container, {
                data,
                options: {
                    ...this.chartOptions.graph,
                    nodeColor: node => this.getNodeColor(node),
                    nodeSize: node => this.getNodeSize(node),
                    edgeWidth: edge => Math.log(edge.value + 1) * 2,
                    onClick: node => this.handleNodeClick(node)
                }
            });
        },

        // 获取节点颜色
        getNodeColor(node) {
            if (node.isCritical) return '#f5222d';
            if (node.cost > this.statistics.avgCost) return '#faad14';
            return '#1890ff';
        },

        // 获取节点大小
        getNodeSize(node) {
            return Math.max(30, Math.log(node.rows + 1) * 10);
        },

        // 处理节点点击
        handleNodeClick(node) {
            this.$emit('node-click', node);
        },

        // 查找关键路径
        findCriticalPath(node, path = []) {
            if (!node) return path;

            path.push(node);

            if (!node.children?.length) {
                return path;
            }

            // 找出成本最高的子节点
            const maxCostChild = node.children.reduce((max, current) => 
                current.cost > max.cost ? current : max
            , node.children[0]);

            return this.findCriticalPath(maxCostChild, path);
        },

        // 复制计划详情
        async copyPlanDetails() {
            try {
                const details = JSON.stringify(this.planData, null, 2);
                await UtilService.copyToClipboard(details);
                this.$message.success('计划详情已复制到剪贴板');
            } catch (error) {
                this.$message.error('复制失败，请手动复制');
            }
        }
    },

    template: `
        <div class="query-plan">
            <!-- 工具栏 -->
            <div class="plan-toolbar">
                <div class="toolbar-left">
                    <a-radio-group v-model="mode" button-style="solid">
                        <a-radio-button value="tree">树形图</a-radio-button>
                        <a-radio-button value="graph">网络图</a-radio-button>
                    </a-radio-group>
                </div>
                <div class="toolbar-right">
                    <a-button
                        type="link"
                        :disabled="!hasData"
                        @click="copyPlanDetails"
                    >
                        <a-icon type="copy" />复制详情
                    </a-button>
                </div>
            </div>

            <!-- 性能指标 -->
            <div v-if="hasData" class="plan-metrics">
                <a-row :gutter="16">
                    <a-col :span="6">
                        <a-statistic
                            title="总成本"
                            :value="totalCost"
                            :precision="2"
                            :valueStyle="{ color: totalCost > 100 ? '#cf1322' : '#3f8600' }"
                        >
                            <template #prefix>
                                <a-icon :type="totalCost > 100 ? 'warning' : 'check-circle'" />
                            </template>
                        </a-statistic>
                    </a-col>
                    <a-col :span="6">
                        <a-statistic
                            title="扫描行数"
                            :value="metrics?.totalRows"
                            :formatter="val => val.toLocaleString()"
                        >
                            <template #prefix>
                                <a-icon type="table" />
                            </template>
                        </a-statistic>
                    </a-col>
                    <a-col :span="6">
                        <a-statistic
                            title="预估内存"
                            :value="metrics?.estimatedMemory"
                            :formatter="val => UtilService.formatFileSize(val)"
                        >
                            <template #prefix>
                                <a-icon type="database" />
                            </template>
                        </a-statistic>
                    </a-col>
                    <a-col :span="6">
                        <a-statistic
                            title="预估时间"
                            :value="metrics?.estimatedTime"
                            suffix="ms"
                        >
                            <template #prefix>
                                <a-icon type="clock-circle" />
                            </template>
                        </a-statistic>
                    </a-col>
                </a-row>
            </div>

            <!-- 图表容器 -->
            <div class="plan-chart" :class="{ loading }">
                <div ref="chart" class="chart-container"></div>
                <div v-if="loading" class="chart-loading">
                    <a-spin />
                </div>
                <a-empty 
                    v-if="!loading && !hasData"
                    :description="error || '暂无查询计划'"
                />
            </div>

            <!-- 优化建议 -->
            <div v-if="hasSuggestions" class="plan-suggestions">
                <a-collapse>
                    <a-collapse-panel key="suggestions" header="优化建议">
                        <template v-slot:extra>
                            <a-badge :count="suggestions.length" :style="{ backgroundColor: '#52c41a' }" />
                        </template>
                        <a-timeline>
                            <a-timeline-item 
                                v-for="(suggestion, index) in suggestions"
                                :key="index"
                                :color="suggestion.level === 'critical' ? 'red' : 'blue'"
                            >
                                {{ suggestion.message }}
                            </a-timeline-item>
                        </a-timeline>
                    </a-collapse-panel>
                </a-collapse>
            </div>
        </div>
    `
};

// 导入依赖
import QueryService from '../services/query-service.js';
import UtilService from '../services/util-service.js';

// 注册组件
Vue.component('query-plan', QueryPlan);