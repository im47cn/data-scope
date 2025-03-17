/**
 * 查询构建器页面组件
 * 集成所有查询构建相关的功能组件
 */
const QueryBuilderPage = {
    data() {
        return {
            // 当前数据源
            dataSource: null,
            // 当前查询ID
            queryId: null,
            // 查询配置
            queryConfig: {
                sql: '',
                parameters: {},
                pageSize: 100,
                timeout: 30000
            },
            // 执行状态
            executionStatus: {
                running: false,
                startTime: null,
                endTime: null,
                error: null
            },
            // 活动面板
            activePanel: 'workspace', // workspace, history, savedQueries
            // 面板显示状态
            panels: {
                parameters: false,
                planView: false
            },
            // 布局配置
            layout: {
                previewHeight: 300,
                resizing: false
            }
        };
    },

    computed: {
        canExecute() {
            return this.dataSource && 
                   this.queryConfig.sql && 
                   !this.executionStatus.running &&
                   this.isParametersValid;
        },

        isParametersValid() {
            // 检查所有参数是否有效
            return !this.panels.parameters || 
                   this.$refs.parameterInput?.isComplete;
        },

        executionTime() {
            if (!this.executionStatus.startTime || !this.executionStatus.endTime) {
                return null;
            }
            return (this.executionStatus.endTime - this.executionStatus.startTime) / 1000;
        }
    },

    watch: {
        'queryConfig.sql': {
            handler(newSql) {
                // 检测是否需要显示参数面板
                this.detectParameters(newSql);
            }
        }
    },

    created() {
        // 从路由参数获取数据源ID
        const dataSourceId = this.$route.params.dataSourceId;
        if (dataSourceId) {
            this.loadDataSource(dataSourceId);
        }

        // 恢复上次的查询
        this.restoreLastQuery();
    },

    methods: {
        // 加载数据源信息
        async loadDataSource(dataSourceId) {
            try {
                const response = await DataSourceService.getDataSource(dataSourceId);
                this.dataSource = response.data;
            } catch (error) {
                console.error('加载数据源失败:', error);
                this.$message.error('加载数据源失败');
            }
        },

        // 检测SQL中的参数
        detectParameters(sql) {
            const hasParameters = /:(\w+)|[?]/g.test(sql);
            this.panels.parameters = hasParameters;
        },

        // 执行查询
        async executeQuery() {
            if (!this.canExecute) return;

            this.executionStatus = {
                running: true,
                startTime: Date.now(),
                endTime: null,
                error: null
            };

            try {
                const response = await QueryService.executeQuery({
                    dataSourceId: this.dataSource.id,
                    sql: this.queryConfig.sql,
                    parameters: this.queryConfig.parameters,
                    pageSize: this.queryConfig.pageSize,
                    timeout: this.queryConfig.timeout
                });

                this.queryId = response.data.queryId;
                this.panels.planView = true;
                
                // 保存到本地历史
                this.saveToLocalHistory();
            } catch (error) {
                console.error('执行查询失败:', error);
                this.executionStatus.error = error.response?.data?.message || '执行查询失败';
                this.$message.error(this.executionStatus.error);
            } finally {
                this.executionStatus.running = false;
                this.executionStatus.endTime = Date.now();
            }
        },

        // 取消查询
        async cancelQuery() {
            if (!this.queryId || !this.executionStatus.running) return;

            try {
                await QueryService.cancelQuery(this.queryId);
                this.$message.success('查询已取消');
            } catch (error) {
                console.error('取消查询失败:', error);
                this.$message.error('取消查询失败');
            }
        },

        // 处理SQL变更
        handleSqlChange(sql) {
            this.queryConfig.sql = sql;
        },

        // 处理参数变更
        handleParametersChange(parameters) {
            this.queryConfig.parameters = parameters;
        },

        // 处理参数验证
        handleParametersValidation(validation) {
            this.parametersValidation = validation;
        },

        // 保存查询
        async saveQuery() {
            try {
                await QueryService.saveQuery({
                    name: '未命名查询',
                    dataSourceId: this.dataSource.id,
                    sql: this.queryConfig.sql,
                    parameters: this.queryConfig.parameters,
                    description: ''
                });
                this.$message.success('查询已保存');
            } catch (error) {
                console.error('保存查询失败:', error);
                this.$message.error('保存查询失败');
            }
        },

        // 从历史记录加载查询
        loadFromHistory(record) {
            this.queryConfig = {
                ...this.queryConfig,
                sql: record.sql,
                parameters: record.parameters || {}
            };
            this.activePanel = 'workspace';
        },

        // 保存到本地历史
        saveToLocalHistory() {
            const history = {
                timestamp: Date.now(),
                dataSourceId: this.dataSource.id,
                sql: this.queryConfig.sql,
                parameters: this.queryConfig.parameters,
                executionTime: this.executionTime
            };

            let localHistory = JSON.parse(
                localStorage.getItem('queryHistory') || '[]'
            );
            
            localHistory.unshift(history);
            // 只保留最近50条记录
            localHistory = localHistory.slice(0, 50);
            
            localStorage.setItem('queryHistory', JSON.stringify(localHistory));
        },

        // 恢复上次的查询
        restoreLastQuery() {
            try {
                const localHistory = JSON.parse(
                    localStorage.getItem('queryHistory') || '[]'
                );
                
                if (localHistory.length > 0) {
                    const lastQuery = localHistory[0];
                    this.queryConfig = {
                        ...this.queryConfig,
                        sql: lastQuery.sql,
                        parameters: lastQuery.parameters || {}
                    };
                }
            } catch (error) {
                console.error('恢复上次查询失败:', error);
            }
        },

        // 调整预览区域高度
        startResizing(e) {
            this.layout.resizing = true;
            this.layout.startY = e.clientY;
            this.layout.startHeight = this.layout.previewHeight;
            
            document.addEventListener('mousemove', this.handleResizing);
            document.addEventListener('mouseup', this.stopResizing);
        },

        handleResizing(e) {
            if (!this.layout.resizing) return;
            
            const delta = this.layout.startY - e.clientY;
            this.layout.previewHeight = Math.max(200, this.layout.startHeight + delta);
        },

        stopResizing() {
            this.layout.resizing = false;
            document.removeEventListener('mousemove', this.handleResizing);
            document.removeEventListener('mouseup', this.stopResizing);
        }
    },

    beforeDestroy() {
        // 确保清理所有事件监听器
        if (this.layout.resizing) {
            this.stopResizing();
        }
    },

    template: `
        <div class="query-builder-page">
            <!-- 头部工具栏 -->
            <div class="query-header">
                <div class="header-left">
                    <h2>{{ dataSource?.name || '查询构建器' }}</h2>
                </div>
                <div class="header-right">
                    <a-space>
                        <a-button
                            type="primary"
                            :loading="executionStatus.running"
                            :disabled="!canExecute"
                            @click="executeQuery"
                        >
                            <a-icon :type="executionStatus.running ? 'loading' : 'play-circle'" />
                            执行查询
                        </a-button>
                        <a-button
                            v-if="executionStatus.running"
                            @click="cancelQuery"
                        >
                            <a-icon type="stop" />
                            取消
                        </a-button>
                        <a-button @click="saveQuery">
                            <a-icon type="save" />
                            保存
                        </a-button>
                    </a-space>
                </div>
            </div>

            <!-- 主体内容 -->
            <div class="query-main">
                <!-- 左侧面板 -->
                <div class="left-panel">
                    <a-tabs v-model="activePanel">
                        <a-tab-pane key="workspace" tab="工作区">
                            <!-- 查询工作区 -->
                            <query-workspace
                                ref="workspace"
                                :dataSourceId="dataSource?.id"
                                v-model="queryConfig.sql"
                                @change="handleSqlChange"
                            />
                        </a-tab-pane>
                        
                        <a-tab-pane key="history" tab="历史记录">
                            <!-- 查询历史 -->
                            <query-history
                                :dataSourceId="dataSource?.id"
                                @select="loadFromHistory"
                            />
                        </a-tab-pane>

                        <a-tab-pane key="savedQueries" tab="已保存查询">
                            <!-- 已保存查询列表 -->
                            <saved-queries
                                :dataSourceId="dataSource?.id"
                                @select="loadFromHistory"
                            />
                        </a-tab-pane>
                    </a-tabs>
                </div>

                <!-- 右侧面板 -->
                <div class="right-panel">
                    <!-- 参数输入面板 -->
                    <a-collapse
                        v-model="panels.parameters"
                        v-if="panels.parameters"
                    >
                        <a-collapse-panel key="parameters" header="查询参数">
                            <parameter-input
                                ref="parameterInput"
                                :sql="queryConfig.sql"
                                v-model="queryConfig.parameters"
                                @validation="handleParametersValidation"
                            />
                        </a-collapse-panel>
                    </a-collapse>

                    <!-- SQL预览 -->
                    <sql-preview
                        :sql="queryConfig.sql"
                        :loading="executionStatus.running"
                    />

                    <!-- 分隔条 -->
                    <div 
                        class="resize-handle"
                        @mousedown="startResizing"
                    ></div>

                    <!-- 结果预览 -->
                    <div 
                        class="preview-panel"
                        :style="{ height: layout.previewHeight + 'px' }"
                    >
                        <a-tabs>
                            <a-tab-pane key="results" tab="查询结果">
                                <results-preview
                                    v-if="queryId"
                                    :queryId="queryId"
                                    :autoLoad="true"
                                />
                            </a-tab-pane>
                            
                            <a-tab-pane 
                                key="plan" 
                                tab="执行计划"
                                v-if="panels.planView"
                            >
                                <query-plan
                                    v-if="queryId"
                                    :queryId="queryId"
                                    :sql="queryConfig.sql"
                                />
                            </a-tab-pane>
                        </a-tabs>
                    </div>
                </div>
            </div>

            <!-- 状态栏 -->
            <div class="query-status-bar">
                <div class="status-left">
                    <template v-if="executionStatus.error">
                        <a-icon type="warning" style="color: #ff4d4f" />
                        <span class="error-message">{{ executionStatus.error }}</span>
                    </template>
                    <template v-else-if="executionTime">
                        <a-icon type="clock-circle" />
                        <span>执行时间: {{ executionTime.toFixed(2) }}秒</span>
                    </template>
                </div>
                <div class="status-right">
                    <a-space>
                        <a-tooltip title="每页记录数">
                            <a-input-number
                                v-model="queryConfig.pageSize"
                                :min="1"
                                :max="1000"
                                style="width: 90px"
                            />
                        </a-tooltip>
                        <a-tooltip title="查询超时时间（秒）">
                            <a-input-number
                                v-model="queryConfig.timeout"
                                :min="1"
                                :max="300"
                                style="width: 90px"
                            />
                        </a-tooltip>
                    </a-space>
                </div>
            </div>
        </div>
    `
};

// 导入依赖
import DataSourceService from '../services/datasource-service.js';
import QueryService from '../services/query-service.js';

// 注册组件
Vue.component('query-builder-page', QueryBuilderPage);