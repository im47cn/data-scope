/**
 * 表关系管理页面
 * 集成表关系列表和表关系图组件
 */
import TableRelationshipList from './table-relationship-list.js';
import TableRelationshipGraph from './table-relationship-graph.js';

class TableRelationshipPage {
    constructor(containerId) {
        this.containerId = containerId;
        this.dataSourceId = null;
        this.dataSourceSelect = null;
        this.tableRelationshipList = null;
        this.tableRelationshipGraph = null;
        this.init();
    }

    /**
     * 初始化页面
     */
    init() {
        this.render();
        this.loadDataSources();
        this.initComponents();
        this.bindEvents();
    }

    /**
     * 渲染页面
     */
    render() {
        const container = document.getElementById(this.containerId);
        if (!container) {
            console.error(`容器 #${this.containerId} 不存在`);
            return;
        }

        // 构建HTML
        let html = `
            <div class="container-fluid py-3">
                <div class="row mb-3">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-body">
                                <div class="row align-items-center">
                                    <div class="col-md-3">
                                        <label for="data-source-select" class="form-label">选择数据源</label>
                                        <select id="data-source-select" class="form-select">
                                            <option value="">请选择数据源</option>
                                        </select>
                                    </div>
                                    <div class="col-md-9">
                                        <p class="mb-0">
                                            表关系管理允许您查看、编辑和管理数据库表之间的关系。这些关系可以从元数据、查询历史或用户反馈中学习，
                                            并用于自动生成JOIN语句，提高查询准确性。
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="row mb-3">
                    <div class="col-12">
                        <ul class="nav nav-tabs" id="relationship-tabs" role="tablist">
                            <li class="nav-item" role="presentation">
                                <button class="nav-link active" id="list-tab" data-bs-toggle="tab" data-bs-target="#list-tab-pane" 
                                    type="button" role="tab" aria-controls="list-tab-pane" aria-selected="true">
                                    <i class="fas fa-list"></i> 列表视图
                                </button>
                            </li>
                            <li class="nav-item" role="presentation">
                                <button class="nav-link" id="graph-tab" data-bs-toggle="tab" data-bs-target="#graph-tab-pane" 
                                    type="button" role="tab" aria-controls="graph-tab-pane" aria-selected="false">
                                    <i class="fas fa-project-diagram"></i> 图形视图
                                </button>
                            </li>
                        </ul>
                        <div class="tab-content" id="relationship-tabs-content">
                            <div class="tab-pane fade show active" id="list-tab-pane" role="tabpanel" aria-labelledby="list-tab" tabindex="0">
                                <div id="table-relationship-list-container"></div>
                            </div>
                            <div class="tab-pane fade" id="graph-tab-pane" role="tabpanel" aria-labelledby="graph-tab" tabindex="0">
                                <div id="table-relationship-graph-container"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `;

        container.innerHTML = html;
    }

    /**
     * 初始化组件
     */
    initComponents() {
        // 初始化表关系列表组件
        this.tableRelationshipList = new TableRelationshipList('table-relationship-list-container');
        
        // 初始化表关系图组件
        this.tableRelationshipGraph = new TableRelationshipGraph('table-relationship-graph-container');
        
        // 获取数据源选择器
        this.dataSourceSelect = document.getElementById('data-source-select');
    }

    /**
     * 加载数据源列表
     */
    loadDataSources() {
        fetch('/api/data-sources')
            .then(response => response.json())
            .then(data => {
                this.renderDataSourceOptions(data);
            })
            .catch(error => {
                console.error('加载数据源失败:', error);
                this.showError('加载数据源失败，请稍后重试');
            });
    }

    /**
     * 渲染数据源选项
     */
    renderDataSourceOptions(dataSources) {
        if (!this.dataSourceSelect) return;
        
        // 清空选项
        this.dataSourceSelect.innerHTML = '<option value="">请选择数据源</option>';
        
        // 添加选项
        dataSources.forEach(dataSource => {
            const option = document.createElement('option');
            option.value = dataSource.id;
            option.textContent = dataSource.name;
            this.dataSourceSelect.appendChild(option);
        });
        
        // 如果只有一个数据源，自动选择
        if (dataSources.length === 1) {
            this.dataSourceSelect.value = dataSources[0].id;
            this.onDataSourceChange();
        }
    }

    /**
     * 绑定事件
     */
    bindEvents() {
        // 数据源选择事件
        if (this.dataSourceSelect) {
            this.dataSourceSelect.addEventListener('change', () => {
                this.onDataSourceChange();
            });
        }
        
        // 标签切换事件
        document.getElementById('graph-tab').addEventListener('shown.bs.tab', () => {
            // 当切换到图形视图时，重新渲染图形
            if (this.tableRelationshipGraph && this.dataSourceId) {
                this.tableRelationshipGraph.renderGraph();
            }
        });
    }

    /**
     * 数据源变更处理
     */
    onDataSourceChange() {
        const dataSourceId = this.dataSourceSelect.value;
        if (!dataSourceId) return;
        
        this.dataSourceId = dataSourceId;
        
        // 更新组件的数据源
        if (this.tableRelationshipList) {
            this.tableRelationshipList.setDataSourceId(dataSourceId);
        }
        
        if (this.tableRelationshipGraph) {
            this.tableRelationshipGraph.setDataSourceId(dataSourceId);
        }
    }

    /**
     * 显示错误信息
     */
    showError(message) {
        // 创建一个toast元素
        const toastId = 'toast-' + Date.now();
        const toast = document.createElement('div');
        toast.className = 'toast align-items-center text-white bg-danger border-0';
        toast.id = toastId;
        toast.setAttribute('role', 'alert');
        toast.setAttribute('aria-live', 'assertive');
        toast.setAttribute('aria-atomic', 'true');
        
        toast.innerHTML = `
            <div class="d-flex">
                <div class="toast-body">
                    ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        `;
        
        // 添加到toast容器
        let toastContainer = document.querySelector('.toast-container');
        if (!toastContainer) {
            toastContainer = document.createElement('div');
            toastContainer.className = 'toast-container position-fixed bottom-0 end-0 p-3';
            document.body.appendChild(toastContainer);
        }
        
        toastContainer.appendChild(toast);
        
        // 显示toast
        const toastInstance = new bootstrap.Toast(toast);
        toastInstance.show();
        
        // toast关闭后移除
        toast.addEventListener('hidden.bs.toast', () => {
            toastContainer.removeChild(toast);
        });
    }
}

// 导出组件
export default TableRelationshipPage;