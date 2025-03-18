/**
 * 自然语言查询组件
 */
const NLQuery = {
    data() {
        return {
            loading: false,
            executing: false,
            dataSources: [],
            selectedDataSource: null,
            query: '',
            queryResult: null,
            queryHistory: [],
            savedQueries: [],
            showSaveModal: false,
            saveQueryName: '',
            saveQueryDescription: '',
            saveQueryPublic: false,
            activeTab: 'query',
            columns: [],
            dataSource: null,
            pagination: {
                current: 1,
                pageSize: 10,
                total: 0
            }
        };
    },
    computed: {
        hasResult() {
            return this.queryResult && this.queryResult.success;
        },
        hasError() {
            return this.queryResult && !this.queryResult.success;
        }
    },
    mounted() {
        this.fetchDataSources();
        
        // 如果URL中有数据源ID参数，则自动选择该数据源
        const dataSourceId = this.$route.query.dataSourceId;
        if (dataSourceId) {
            this.selectedDataSource = parseInt(dataSourceId);
            this.fetchQueryHistory();
            this.fetchSavedQueries();
        }
    },
    methods: {
        fetchDataSources() {
            this.loading = true;
            
            axios.get('/datasources')
                .then(response => {
                    this.dataSources = response.data;
                    
                    // 如果URL中有数据源ID参数，则自动选择该数据源
                    const dataSourceId = this.$route.query.dataSourceId;
                    if (dataSourceId) {
                        this.selectedDataSource = parseInt(dataSourceId);
                    }
                })
                .catch(error => {
                    console.error('获取数据源列表失败:', error);
                    this.$message.error('获取数据源列表失败');
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        handleDataSourceChange(value) {
            this.selectedDataSource = value;
            this.fetchQueryHistory();
            this.fetchSavedQueries();
            
            // 更新URL参数
            this.$router.replace({
                query: { ...this.$route.query, dataSourceId: value }
            });
        },
        executeQuery() {
            if (!this.selectedDataSource) {
                this.$message.warning('请选择数据源');
                return;
            }
            
            if (!this.query.trim()) {
                this.$message.warning('请输入查询语句');
                return;
            }
            
            this.executing = true;
            
            const request = {
                dataSourceId: this.selectedDataSource,
                query: this.query,
                parameters: {}
            };
            
            axios.post('/nl-queries', request)
                .then(response => {
                    this.queryResult = response.data;
                    
                    if (this.queryResult.success) {
                        this.columns = this.queryResult.columns.map(column => ({
                            title: column,
                            dataIndex: column,
                            key: column
                        }));
                        
                        // 刷新查询历史
                        this.fetchQueryHistory();
                    }
                })
                .catch(error => {
                    console.error('执行查询失败:', error);
                    this.$message.error('执行查询失败');
                    
                    this.queryResult = {
                        success: false,
                        errorMessage: error.response?.data?.message || '执行查询失败'
                    };
                })
                .finally(() => {
                    this.executing = false;
                });
        },
        fetchQueryHistory() {
            if (!this.selectedDataSource) return;
            
            axios.get(`/nl-queries/history?dataSourceId=${this.selectedDataSource}`)
                .then(response => {
                    this.queryHistory = response.data;
                })
                .catch(error => {
                    console.error('获取查询历史失败:', error);
                    this.$message.error('获取查询历史失败');
                });
        },
        fetchSavedQueries() {
            if (!this.selectedDataSource) return;
            
            axios.get(`/nl-queries/saved?dataSourceId=${this.selectedDataSource}`)
                .then(response => {
                    this.savedQueries = response.data;
                })
                .catch(error => {
                    console.error('获取保存的查询失败:', error);
                    this.$message.error('获取保存的查询失败');
                });
        },
        handleSaveQuery() {
            this.saveQueryName = '';
            this.saveQueryDescription = '';
            this.saveQueryPublic = false;
            this.showSaveModal = true;
        },
        handleSaveConfirm() {
            if (!this.saveQueryName.trim()) {
                this.$message.warning('请输入查询名称');
                return;
            }
            
            const request = {
                dataSourceId: this.selectedDataSource,
                query: this.query,
                parameters: {}
            };
            
            axios.post(`/nl-queries/save?name=${encodeURIComponent(this.saveQueryName)}`, {
                request: request,
                result: this.queryResult
            })
                .then(response => {
                    this.$message.success('保存查询成功');
                    this.showSaveModal = false;
                    
                    // 刷新保存的查询列表
                    this.fetchSavedQueries();
                })
                .catch(error => {
                    console.error('保存查询失败:', error);
                    this.$message.error('保存查询失败');
                });
        },
        handleSaveCancel() {
            this.showSaveModal = false;
        },
        handleTabChange(key) {
            this.activeTab = key;
        },
        handleHistoryClick(history) {
            this.query = history.naturalLanguageQuery;
            this.executeQuery();
        },
        handleSavedQueryClick(savedQuery) {
            this.query = savedQuery.naturalLanguageQuery;
            this.executeQuery();
        },
        handleDeleteSavedQuery(id) {
            this.$confirm({
                title: '确认删除',
                content: '确定要删除这个保存的查询吗？',
                okText: '确认',
                okType: 'danger',
                cancelText: '取消',
                onOk: () => {
                    axios.delete(`/nl-queries/saved/${id}`)
                        .then(() => {
                            this.$message.success('删除成功');
                            this.fetchSavedQueries();
                        })
                        .catch(error => {
                            console.error('删除保存的查询失败:', error);
                            this.$message.error('删除保存的查询失败');
                        });
                }
            });
        },
        formatDate(date) {
            if (!date) return '-';
            return moment(date).format('YYYY-MM-DD HH:mm:ss');
        },
        exportToExcel() {
            if (!this.queryResult || !this.queryResult.success) {
                this.$message.warning('没有可导出的数据');
                return;
            }
            
            // 创建工作簿
            const wb = XLSX.utils.book_new();
            
            // 将数据转换为工作表
            const ws = XLSX.utils.json_to_sheet(this.queryResult.data);
            
            // 将工作表添加到工作簿
            XLSX.utils.book_append_sheet(wb, ws, 'Query Result');
            
            // 导出工作簿
            XLSX.writeFile(wb, 'query_result.xlsx');
        },
        exportToCSV() {
            if (!this.queryResult || !this.queryResult.success) {
                this.$message.warning('没有可导出的数据');
                return;
            }
            
            // 将数据转换为CSV
            const csv = Papa.unparse(this.queryResult.data);
            
            // 创建Blob对象
            const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
            
            // 创建下载链接
            const link = document.createElement('a');
            const url = URL.createObjectURL(blob);
            link.setAttribute('href', url);
            link.setAttribute('download', 'query_result.csv');
            link.style.visibility = 'hidden';
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        }
    },
    template: `
        <div class="nl-query-container">
            <div class="page-header">
                <h1>自然语言查询</h1>
            </div>
            
            <a-spin :spinning="loading">
                <a-card>
                    <a-form layout="vertical">
                        <a-form-item label="数据源">
                            <a-select
                                v-model="selectedDataSource"
                                placeholder="请选择数据源"
                                style="width: 100%"
                                @change="handleDataSourceChange"
                            >
                                <a-select-option
                                    v-for="dataSource in dataSources"
                                    :key="dataSource.id"
                                    :value="dataSource.id"
                                >
                                    {{ dataSource.name }} ({{ dataSource.type }})
                                </a-select-option>
                            </a-select>
                        </a-form-item>
                        
                        <a-form-item label="自然语言查询">
                            <a-textarea
                                v-model="query"
                                placeholder="请输入自然语言查询，例如：查询最近一个月的销售额"
                                :rows="4"
                                :disabled="!selectedDataSource"
                            />
                        </a-form-item>
                        
                        <a-form-item>
                            <a-button
                                type="primary"
                                @click="executeQuery"
                                :loading="executing"
                                :disabled="!selectedDataSource"
                            >
                                执行查询
                            </a-button>
                        </a-form-item>
                    </a-form>
                </a-card>
                
                <a-tabs v-if="queryResult" :activeKey="activeTab" @change="handleTabChange" style="margin-top: 16px">
                    <a-tab-pane key="query" tab="查询结果">
                        <a-card>
                            <template v-if="hasResult">
                                <div class="query-info">
                                    <div>
                                        <strong>生成的SQL：</strong>
                                        <a-tag color="blue">{{ queryResult.sql }}</a-tag>
                                    </div>
                                    <div>
                                        <strong>执行时间：</strong>
                                        <a-tag color="green">{{ queryResult.executionTime }} ms</a-tag>
                                    </div>
                                    <div class="query-actions">
                                        <a-button type="primary" size="small" @click="handleSaveQuery">
                                            <a-icon type="save" />保存查询
                                        </a-button>
                                        <a-button size="small" @click="exportToExcel" style="margin-left: 8px">
                                            <a-icon type="file-excel" />导出Excel
                                        </a-button>
                                        <a-button size="small" @click="exportToCSV" style="margin-left: 8px">
                                            <a-icon type="file" />导出CSV
                                        </a-button>
                                    </div>
                                </div>
                                
                                <a-table
                                    :columns="columns"
                                    :dataSource="queryResult.data"
                                    :rowKey="(record, index) => index"
                                    :pagination="pagination"
                                    size="middle"
                                />
                            </template>
                            
                            <a-result
                                v-else-if="hasError"
                                status="error"
                                :title="'查询失败'"
                                :sub-title="queryResult.errorMessage"
                            >
                                <template #extra>
                                    <a-button type="primary" @click="executeQuery">
                                        重试
                                    </a-button>
                                </template>
                            </a-result>
                        </a-card>
                    </a-tab-pane>
                    
                    <a-tab-pane key="history" tab="查询历史">
                        <a-card>
                            <a-list
                                :dataSource="queryHistory"
                                :rowKey="item => item.id"
                            >
                                <a-list-item slot="renderItem" slot-scope="item">
                                    <a-list-item-meta
                                        :description="item.generatedSql"
                                    >
                                        <template slot="title">
                                            <a @click="handleHistoryClick(item)">{{ item.naturalLanguageQuery }}</a>
                                        </template>
                                        <a-avatar
                                            slot="avatar"
                                            :style="{ backgroundColor: item.status === 'SUCCESS' ? '#52c41a' : '#f5222d' }"
                                            :icon="item.status === 'SUCCESS' ? 'check' : 'close'"
                                        />
                                    </a-list-item-meta>
                                    <div class="list-item-content">
                                        <div>
                                            <a-tag color="blue">{{ formatDate(item.createdAt) }}</a-tag>
                                            <a-tag color="green">{{ item.executionTime }} ms</a-tag>
                                        </div>
                                    </div>
                                </a-list-item>
                            </a-list>
                        </a-card>
                    </a-tab-pane>
                    
                    <a-tab-pane key="saved" tab="保存的查询">
                        <a-card>
                            <a-list
                                :dataSource="savedQueries"
                                :rowKey="item => item.id"
                            >
                                <a-list-item slot="renderItem" slot-scope="item">
                                    <a-list-item-meta
                                        :description="item.description || item.naturalLanguageQuery"
                                    >
                                        <template slot="title">
                                            <a @click="handleSavedQueryClick(item)">{{ item.name }}</a>
                                        </template>
                                        <a-avatar
                                            slot="avatar"
                                            :style="{ backgroundColor: item.isPublic ? '#1890ff' : '#722ed1' }"
                                            :icon="item.isPublic ? 'global' : 'user'"
                                        />
                                    </a-list-item-meta>
                                    <div class="list-item-content">
                                        <div>
                                            <a-tag color="blue">{{ formatDate(item.createdAt) }}</a-tag>
                                        </div>
                                        <div>
                                            <a-button
                                                type="danger"
                                                size="small"
                                                @click="handleDeleteSavedQuery(item.id)"
                                            >
                                                <a-icon type="delete" />
                                            </a-button>
                                        </div>
                                    </div>
                                </a-list-item>
                            </a-list>
                        </a-card>
                    </a-tab-pane>
                </a-tabs>
                
                <a-modal
                    title="保存查询"
                    :visible="showSaveModal"
                    @ok="handleSaveConfirm"
                    @cancel="handleSaveCancel"
                    okText="保存"
                    cancelText="取消"
                >
                    <a-form layout="vertical">
                        <a-form-item label="查询名称" required>
                            <a-input
                                v-model="saveQueryName"
                                placeholder="请输入查询名称"
                            />
                        </a-form-item>
                        
                        <a-form-item label="描述">
                            <a-textarea
                                v-model="saveQueryDescription"
                                placeholder="请输入描述"
                                :rows="3"
                            />
                        </a-form-item>
                        
                        <a-form-item label="是否公开">
                            <a-switch v-model="saveQueryPublic" />
                            <span style="margin-left: 8px">{{ saveQueryPublic ? '公开' : '私有' }}</span>
                        </a-form-item>
                    </a-form>
                </a-modal>
            </a-spin>
        </div>
    `
};

// 注册组件
Vue.component('nl-query', NLQuery);
