/**
 * 表关系列表组件
 * 用于展示和管理数据源的表关系
 */
class TableRelationshipList {
    constructor(containerId) {
        this.containerId = containerId;
        this.relationships = [];
        this.dataSourceId = null;
        this.tableNames = [];
        this.init();
    }

    /**
     * 初始化组件
     */
    init() {
        this.render();
        this.bindEvents();
    }

    /**
     * 设置数据源ID
     */
    setDataSourceId(dataSourceId) {
        this.dataSourceId = dataSourceId;
        this.loadTableRelationships();
    }

    /**
     * 加载表关系数据
     */
    loadTableRelationships() {
        if (!this.dataSourceId) {
            return;
        }

        // 显示加载中
        this.showLoading();

        // 获取表关系数据
        fetch(`/api/table-relationships?dataSourceId=${this.dataSourceId}`)
            .then(response => response.json())
            .then(data => {
                this.relationships = data;
                this.render();
                this.hideLoading();
            })
            .catch(error => {
                console.error('加载表关系失败:', error);
                this.showError('加载表关系失败，请稍后重试');
                this.hideLoading();
            });

        // 获取数据源的表列表
        fetch(`/api/data-sources/${this.dataSourceId}/tables`)
            .then(response => response.json())
            .then(data => {
                this.tableNames = data.map(table => table.name);
            })
            .catch(error => {
                console.error('加载表列表失败:', error);
            });
    }

    /**
     * 渲染组件
     */
    render() {
        const container = document.getElementById(this.containerId);
        if (!container) {
            console.error(`容器 #${this.containerId} 不存在`);
            return;
        }

        // 构建HTML
        let html = `
            <div class="card">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5>表关系管理</h5>
                    <div>
                        <button id="btn-add-relationship" class="btn btn-primary btn-sm">
                            <i class="fas fa-plus"></i> 添加关系
                        </button>
                        <button id="btn-learn-from-metadata" class="btn btn-info btn-sm">
                            <i class="fas fa-database"></i> 从元数据学习
                        </button>
                        <button id="btn-learn-from-history" class="btn btn-info btn-sm">
                            <i class="fas fa-history"></i> 从查询历史学习
                        </button>
                    </div>
                </div>
                <div class="card-body">
                    <div id="relationship-loading" class="text-center py-3" style="display: none;">
                        <div class="spinner-border text-primary" role="status">
                            <span class="visually-hidden">加载中...</span>
                        </div>
                    </div>
                    <div id="relationship-error" class="alert alert-danger" style="display: none;"></div>
                    <div id="relationship-table-container">
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>源表</th>
                                    <th>源列</th>
                                    <th>目标表</th>
                                    <th>目标列</th>
                                    <th>关系类型</th>
                                    <th>关系来源</th>
                                    <th>权重</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                ${this.renderTableRows()}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        `;

        container.innerHTML = html;
    }

    /**
     * 渲染表格行
     */
    renderTableRows() {
        if (this.relationships.length === 0) {
            return `
                <tr>
                    <td colspan="8" class="text-center">暂无表关系数据</td>
                </tr>
            `;
        }

        return this.relationships.map(relationship => `
            <tr data-id="${relationship.id}">
                <td>${relationship.sourceTable}</td>
                <td>${relationship.sourceColumn}</td>
                <td>${relationship.targetTable}</td>
                <td>${relationship.targetColumn}</td>
                <td>${this.formatRelationshipType(relationship.type)}</td>
                <td>${this.formatRelationshipSource(relationship.source)}</td>
                <td>
                    <div class="progress" style="height: 20px;">
                        <div class="progress-bar" role="progressbar" style="width: ${relationship.weight * 100}%;" 
                            aria-valuenow="${relationship.weight * 100}" aria-valuemin="0" aria-valuemax="100">
                            ${Math.round(relationship.weight * 100)}%
                        </div>
                    </div>
                </td>
                <td>
                    <button class="btn btn-sm btn-outline-primary btn-edit-relationship" data-id="${relationship.id}">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-danger btn-delete-relationship" data-id="${relationship.id}">
                        <i class="fas fa-trash"></i>
                    </button>
                </td>
            </tr>
        `).join('');
    }

    /**
     * 格式化关系类型
     */
    formatRelationshipType(type) {
        const types = {
            'ONE_TO_ONE': '一对一',
            'ONE_TO_MANY': '一对多',
            'MANY_TO_ONE': '多对一',
            'MANY_TO_MANY': '多对多'
        };
        return types[type] || type;
    }

    /**
     * 格式化关系来源
     */
    formatRelationshipSource(source) {
        const sources = {
            'METADATA': '元数据',
            'QUERY_HISTORY': '查询历史',
            'USER_FEEDBACK': '用户反馈'
        };
        return sources[source] || source;
    }

    /**
     * 绑定事件
     */
    bindEvents() {
        const container = document.getElementById(this.containerId);
        if (!container) return;

        // 添加关系按钮
        container.addEventListener('click', event => {
            if (event.target.closest('#btn-add-relationship')) {
                this.showAddRelationshipModal();
            }
        });

        // 从元数据学习按钮
        container.addEventListener('click', event => {
            if (event.target.closest('#btn-learn-from-metadata')) {
                this.learnFromMetadata();
            }
        });

        // 从查询历史学习按钮
        container.addEventListener('click', event => {
            if (event.target.closest('#btn-learn-from-history')) {
                this.learnFromQueryHistory();
            }
        });

        // 编辑按钮
        container.addEventListener('click', event => {
            const editButton = event.target.closest('.btn-edit-relationship');
            if (editButton) {
                const id = editButton.dataset.id;
                this.showEditRelationshipModal(id);
            }
        });

        // 删除按钮
        container.addEventListener('click', event => {
            const deleteButton = event.target.closest('.btn-delete-relationship');
            if (deleteButton) {
                const id = deleteButton.dataset.id;
                this.confirmDeleteRelationship(id);
            }
        });
    }

    /**
     * 显示添加关系模态框
     */
    showAddRelationshipModal() {
        // 创建模态框
        const modal = document.createElement('div');
        modal.className = 'modal fade';
        modal.id = 'add-relationship-modal';
        modal.setAttribute('tabindex', '-1');
        modal.setAttribute('aria-labelledby', 'add-relationship-modal-label');
        modal.setAttribute('aria-hidden', 'true');

        modal.innerHTML = `
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="add-relationship-modal-label">添加表关系</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="add-relationship-form">
                            <div class="mb-3">
                                <label for="source-table" class="form-label">源表</label>
                                <select class="form-select" id="source-table" required>
                                    <option value="">请选择源表</option>
                                    ${this.tableNames.map(name => `<option value="${name}">${name}</option>`).join('')}
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="source-column" class="form-label">源列</label>
                                <input type="text" class="form-control" id="source-column" required>
                            </div>
                            <div class="mb-3">
                                <label for="target-table" class="form-label">目标表</label>
                                <select class="form-select" id="target-table" required>
                                    <option value="">请选择目标表</option>
                                    ${this.tableNames.map(name => `<option value="${name}">${name}</option>`).join('')}
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="target-column" class="form-label">目标列</label>
                                <input type="text" class="form-control" id="target-column" required>
                            </div>
                            <div class="mb-3">
                                <label for="relationship-type" class="form-label">关系类型</label>
                                <select class="form-select" id="relationship-type" required>
                                    <option value="ONE_TO_ONE">一对一</option>
                                    <option value="ONE_TO_MANY">一对多</option>
                                    <option value="MANY_TO_ONE">多对一</option>
                                    <option value="MANY_TO_MANY">多对多</option>
                                </select>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                        <button type="button" class="btn btn-primary" id="btn-save-relationship">保存</button>
                    </div>
                </div>
            </div>
        `;

        document.body.appendChild(modal);

        // 显示模态框
        const modalInstance = new bootstrap.Modal(modal);
        modalInstance.show();

        // 绑定保存按钮事件
        document.getElementById('btn-save-relationship').addEventListener('click', () => {
            this.saveRelationship();
        });

        // 模态框关闭后移除
        modal.addEventListener('hidden.bs.modal', () => {
            document.body.removeChild(modal);
        });
    }

    /**
     * 保存表关系
     */
    saveRelationship() {
        const sourceTable = document.getElementById('source-table').value;
        const sourceColumn = document.getElementById('source-column').value;
        const targetTable = document.getElementById('target-table').value;
        const targetColumn = document.getElementById('target-column').value;
        const type = document.getElementById('relationship-type').value;

        if (!sourceTable || !sourceColumn || !targetTable || !targetColumn) {
            alert('请填写完整信息');
            return;
        }

        const relationship = {
            dataSourceId: this.dataSourceId,
            sourceTable,
            sourceColumn,
            targetTable,
            targetColumn,
            type
        };

        fetch('/api/table-relationships', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(relationship)
        })
            .then(response => response.json())
            .then(data => {
                // 关闭模态框
                bootstrap.Modal.getInstance(document.getElementById('add-relationship-modal')).hide();
                
                // 重新加载数据
                this.loadTableRelationships();
                
                // 显示成功消息
                this.showSuccess('添加表关系成功');
            })
            .catch(error => {
                console.error('添加表关系失败:', error);
                alert('添加表关系失败，请稍后重试');
            });
    }

    /**
     * 显示编辑关系模态框
     */
    showEditRelationshipModal(id) {
        // 查找关系
        const relationship = this.relationships.find(r => r.id == id);
        if (!relationship) {
            console.error(`未找到ID为${id}的表关系`);
            return;
        }

        // 创建模态框
        const modal = document.createElement('div');
        modal.className = 'modal fade';
        modal.id = 'edit-relationship-modal';
        modal.setAttribute('tabindex', '-1');
        modal.setAttribute('aria-labelledby', 'edit-relationship-modal-label');
        modal.setAttribute('aria-hidden', 'true');

        modal.innerHTML = `
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="edit-relationship-modal-label">编辑表关系</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="edit-relationship-form">
                            <input type="hidden" id="edit-relationship-id" value="${relationship.id}">
                            <div class="mb-3">
                                <label for="edit-source-table" class="form-label">源表</label>
                                <select class="form-select" id="edit-source-table" required>
                                    <option value="">请选择源表</option>
                                    ${this.tableNames.map(name => 
                                        `<option value="${name}" ${name === relationship.sourceTable ? 'selected' : ''}>${name}</option>`
                                    ).join('')}
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="edit-source-column" class="form-label">源列</label>
                                <input type="text" class="form-control" id="edit-source-column" value="${relationship.sourceColumn}" required>
                            </div>
                            <div class="mb-3">
                                <label for="edit-target-table" class="form-label">目标表</label>
                                <select class="form-select" id="edit-target-table" required>
                                    <option value="">请选择目标表</option>
                                    ${this.tableNames.map(name => 
                                        `<option value="${name}" ${name === relationship.targetTable ? 'selected' : ''}>${name}</option>`
                                    ).join('')}
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="edit-target-column" class="form-label">目标列</label>
                                <input type="text" class="form-control" id="edit-target-column" value="${relationship.targetColumn}" required>
                            </div>
                            <div class="mb-3">
                                <label for="edit-relationship-type" class="form-label">关系类型</label>
                                <select class="form-select" id="edit-relationship-type" required>
                                    <option value="ONE_TO_ONE" ${relationship.type === 'ONE_TO_ONE' ? 'selected' : ''}>一对一</option>
                                    <option value="ONE_TO_MANY" ${relationship.type === 'ONE_TO_MANY' ? 'selected' : ''}>一对多</option>
                                    <option value="MANY_TO_ONE" ${relationship.type === 'MANY_TO_ONE' ? 'selected' : ''}>多对一</option>
                                    <option value="MANY_TO_MANY" ${relationship.type === 'MANY_TO_MANY' ? 'selected' : ''}>多对多</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="edit-relationship-weight" class="form-label">权重 (${Math.round(relationship.weight * 100)}%)</label>
                                <input type="range" class="form-range" id="edit-relationship-weight" min="0" max="1" step="0.1" value="${relationship.weight}">
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                        <button type="button" class="btn btn-primary" id="btn-update-relationship">保存</button>
                    </div>
                </div>
            </div>
        `;

        document.body.appendChild(modal);

        // 显示模态框
        const modalInstance = new bootstrap.Modal(modal);
        modalInstance.show();

        // 绑定保存按钮事件
        document.getElementById('btn-update-relationship').addEventListener('click', () => {
            this.updateRelationship();
        });

        // 模态框关闭后移除
        modal.addEventListener('hidden.bs.modal', () => {
            document.body.removeChild(modal);
        });
    }

    /**
     * 更新表关系
     */
    updateRelationship() {
        const id = document.getElementById('edit-relationship-id').value;
        const sourceTable = document.getElementById('edit-source-table').value;
        const sourceColumn = document.getElementById('edit-source-column').value;
        const targetTable = document.getElementById('edit-target-table').value;
        const targetColumn = document.getElementById('edit-target-column').value;
        const type = document.getElementById('edit-relationship-type').value;
        const weight = document.getElementById('edit-relationship-weight').value;

        if (!sourceTable || !sourceColumn || !targetTable || !targetColumn) {
            alert('请填写完整信息');
            return;
        }

        const relationship = {
            id,
            dataSourceId: this.dataSourceId,
            sourceTable,
            sourceColumn,
            targetTable,
            targetColumn,
            type,
            weight
        };

        fetch(`/api/table-relationships/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(relationship)
        })
            .then(response => response.json())
            .then(data => {
                // 关闭模态框
                bootstrap.Modal.getInstance(document.getElementById('edit-relationship-modal')).hide();
                
                // 重新加载数据
                this.loadTableRelationships();
                
                // 显示成功消息
                this.showSuccess('更新表关系成功');
            })
            .catch(error => {
                console.error('更新表关系失败:', error);
                alert('更新表关系失败，请稍后重试');
            });
    }

    /**
     * 确认删除表关系
     */
    confirmDeleteRelationship(id) {
        if (confirm('确定要删除这个表关系吗？')) {
            this.deleteRelationship(id);
        }
    }

    /**
     * 删除表关系
     */
    deleteRelationship(id) {
        fetch(`/api/table-relationships/${id}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    // 重新加载数据
                    this.loadTableRelationships();
                    
                    // 显示成功消息
                    this.showSuccess('删除表关系成功');
                } else {
                    throw new Error('删除失败');
                }
            })
            .catch(error => {
                console.error('删除表关系失败:', error);
                alert('删除表关系失败，请稍后重试');
            });
    }

    /**
     * 从元数据学习表关系
     */
    learnFromMetadata() {
        if (!confirm('确定要从元数据学习表关系吗？这可能会添加新的表关系。')) {
            return;
        }

        // 显示加载中
        this.showLoading();

        fetch(`/api/table-relationships/learn-from-metadata?dataSourceId=${this.dataSourceId}`, {
            method: 'POST'
        })
            .then(response => response.json())
            .then(data => {
                // 重新加载数据
                this.loadTableRelationships();
                
                // 显示成功消息
                this.showSuccess(`从元数据学习成功，发现${data.length}个表关系`);
            })
            .catch(error => {
                console.error('从元数据学习失败:', error);
                this.showError('从元数据学习失败，请稍后重试');
                this.hideLoading();
            });
    }

    /**
     * 从查询历史学习表关系
     */
    learnFromQueryHistory() {
        if (!confirm('确定要从查询历史学习表关系吗？这可能会添加新的表关系。')) {
            return;
        }

        // 显示加载中
        this.showLoading();

        fetch(`/api/table-relationships/learn-from-query-history?dataSourceId=${this.dataSourceId}`, {
            method: 'POST'
        })
            .then(response => response.json())
            .then(data => {
                // 重新加载数据
                this.loadTableRelationships();
                
                // 显示成功消息
                this.showSuccess(`从查询历史学习成功，发现${data.length}个表关系`);
            })
            .catch(error => {
                console.error('从查询历史学习失败:', error);
                this.showError('从查询历史学习失败，请稍后重试');
                this.hideLoading();
            });
    }

    /**
     * 显示加载中
     */
    showLoading() {
        const loading = document.getElementById('relationship-loading');
        if (loading) {
            loading.style.display = 'block';
        }
    }

    /**
     * 隐藏加载中
     */
    hideLoading() {
        const loading = document.getElementById('relationship-loading');
        if (loading) {
            loading.style.display = 'none';
        }
    }

    /**
     * 显示错误信息
     */
    showError(message) {
        const error = document.getElementById('relationship-error');
        if (error) {
            error.textContent = message;
            error.style.display = 'block';
            
            // 3秒后自动隐藏
            setTimeout(() => {
                error.style.display = 'none';
            }, 3000);
        }
    }

    /**
     * 显示成功信息
     */
    showSuccess(message) {
        // 创建一个toast元素
        const toastId = 'toast-' + Date.now();
        const toast = document.createElement('div');
        toast.className = 'toast align-items-center text-white bg-success border-0';
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
export default TableRelationshipList;