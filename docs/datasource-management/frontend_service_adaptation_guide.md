# 前端数据源服务适配指南

## 概述

本文档提供了将前端数据源服务`datasource-service.js`从使用模拟数据转换为调用实际后端API的详细指南。前端服务需要进行适配以匹配后端API的数据格式和调用约定。

## 当前状态

前端目前使用以下文件来处理数据源功能：

1. `js/services/datasource-service.js`: 数据源服务，目前使用模拟数据
2. `js/datasource/datasource-list.js`: 数据源列表组件
3. `js/datasource/datasource-form.js`: 数据源表单组件
4. `js/datasource/datasource-detail.js`: 数据源详情组件

这些文件需要调整以适配后端API。

## 实施步骤

### 1. 更新数据源服务

首先，我们需要修改`datasource-service.js`文件，将模拟数据替换为实际API调用：

```javascript
/**
 * 数据源管理服务
 * 适配后端实际API
 */
const DataSourceService = {
    /**
     * 获取数据源列表
     * @param {Object} params 查询参数
     * @returns {Promise} 返回数据源列表
     */
    getDataSources(params) {
        const { page = 0, size = 10, sort, search, type, active } = params || {};
        
        // 构建查询参数
        const queryParams = new URLSearchParams();
        queryParams.append('page', page);
        queryParams.append('size', size);
        
        if (sort) {
            const [field, order] = sort.split(',');
            queryParams.append('sort', `${field},${order}`);
        }
        
        if (search) {
            queryParams.append('search', search);
        }
        
        if (type) {
            queryParams.append('type', type);
        }
        
        if (active !== undefined) {
            queryParams.append('active', active);
        }
        
        return axios.get(`/api/datasources?${queryParams.toString()}`)
            .then(response => {
                // 转换后端分页格式为前端使用的格式
                return {
                    data: {
                        content: response.data,
                        pageable: {
                            totalElements: response.headers['x-total-count'] || response.data.length,
                            totalPages: Math.ceil((response.headers['x-total-count'] || response.data.length) / size),
                            pageNumber: page,
                            pageSize: size
                        }
                    }
                };
            })
            .catch(error => {
                console.error('获取数据源列表失败:', error);
                throw error;
            });
    },

    /**
     * 获取支持的数据源类型
     * @returns {Promise} 返回数据源类型列表
     */
    getSupportedTypes() {
        return axios.get('/api/datasources/types')
            .then(response => ({ data: response.data }))
            .catch(error => {
                console.error('获取数据源类型失败:', error);
                throw error;
            });
    },

    /**
     * 获取数据源详情
     * @param {string} id 数据源ID
     * @returns {Promise} 返回数据源详情
     */
    getDataSource(id) {
        return axios.get(`/api/datasources/${id}`)
            .then(response => ({ data: response.data }))
            .catch(error => {
                console.error('获取数据源详情失败:', error);
                throw error;
            });
    },

    /**
     * 获取元数据统计信息
     * @param {string} id 数据源ID
     * @returns {Promise} 返回元数据统计信息
     */
    getMetadataStats(id) {
        // 通过查询Schema和表信息计算统计数据
        return Promise.all([
            axios.get(`/api/datasources/${id}/schemas`),
            this.getMetadataTree(id)
        ])
            .then(([schemasResponse, treeResponse]) => {
                const schemas = schemasResponse.data;
                const tree = treeResponse.data;
                
                // 计算统计信息
                let tableCount = 0;
                let viewCount = 0;
                let columnCount = 0;
                
                tree.forEach(schema => {
                    schema.tables.forEach(table => {
                        if (table.type === 'TABLE') {
                            tableCount++;
                        } else if (table.type === 'VIEW') {
                            viewCount++;
                        }
                        // 列统计在详情API中处理
                    });
                });
                
                return {
                    data: {
                        schemaCount: schemas.length,
                        tableCount,
                        viewCount,
                        columnCount: 0 // 需要额外API获取
                    }
                };
            })
            .catch(error => {
                console.error('获取元数据统计信息失败:', error);
                throw error;
            });
    },

    /**
     * 获取元数据树
     * @param {string} id 数据源ID
     * @returns {Promise} 返回元数据树
     */
    getMetadataTree(id) {
        return axios.get(`/api/datasources/${id}/schemas`)
            .then(response => {
                const schemas = response.data;
                
                // 构建树形结构
                const tree = [];
                
                // 按顺序加载每个schema的表
                const loadTablesForSchemas = async () => {
                    for (const schema of schemas) {
                        try {
                            const tablesResponse = await axios.get(
                                `/api/datasources/${id}/schemas/${schema.name}/tables`
                            );
                            
                            tree.push({
                                name: schema.name,
                                tables: tablesResponse.data.map(table => ({
                                    name: table.name,
                                    type: table.type
                                }))
                            });
                        } catch (error) {
                            console.error(`获取schema ${schema.name}的表失败:`, error);
                            tree.push({
                                name: schema.name,
                                tables: [],
                                error: '加载表失败'
                            });
                        }
                    }
                    
                    return { data: tree };
                };
                
                return loadTablesForSchemas();
            })
            .catch(error => {
                console.error('获取元数据树失败:', error);
                throw error;
            });
    },

    /**
     * 获取表详情
     * @param {string} id 数据源ID
     * @param {string} schema 模式名
     * @param {string} table 表名
     * @returns {Promise} 返回表详情
     */
    getTableDetails(id, schema, table) {
        return axios.get(`/api/datasources/${id}/schemas/${schema}/tables/${table}`)
            .then(response => ({ data: response.data }))
            .catch(error => {
                console.error('获取表详情失败:', error);
                throw error;
            });
    },

    /**
     * 获取同步历史
     * @param {string} id 数据源ID
     * @param {Object} params 查询参数
     * @returns {Promise} 返回同步历史
     */
    getSyncHistory(id, params) {
        const { page = 0, size = 10 } = params || {};
        
        // 构建查询参数
        const queryParams = new URLSearchParams();
        queryParams.append('page', page);
        queryParams.append('size', size);
        
        return axios.get(`/api/datasources/${id}/sync-history?${queryParams.toString()}`)
            .then(response => ({
                data: {
                    content: response.data,
                    pageable: {
                        totalElements: response.headers['x-total-count'] || response.data.length,
                        totalPages: Math.ceil((response.headers['x-total-count'] || response.data.length) / size),
                        pageNumber: page,
                        pageSize: size
                    }
                }
            }))
            .catch(error => {
                console.error('获取同步历史失败:', error);
                throw error;
            });
    },

    /**
     * 创建数据源
     * @param {Object} dataSource 数据源信息
     * @returns {Promise} 返回创建的数据源
     */
    createDataSource(dataSource) {
        return axios.post('/api/datasources', dataSource)
            .then(response => ({ data: response.data }))
            .catch(error => {
                console.error('创建数据源失败:', error);
                throw error;
            });
    },

    /**
     * 更新数据源
     * @param {string} id 数据源ID
     * @param {Object} dataSource 数据源信息
     * @returns {Promise} 返回更新后的数据源
     */
    updateDataSource(id, dataSource) {
        return axios.put(`/api/datasources/${id}`, dataSource)
            .then(response => ({ data: response.data }))
            .catch(error => {
                console.error('更新数据源失败:', error);
                throw error;
            });
    },

    /**
     * 删除数据源
     * @param {string} id 数据源ID
     * @returns {Promise}
     */
    deleteDataSource(id) {
        return axios.delete(`/api/datasources/${id}`)
            .then(() => ({ data: { success: true } }))
            .catch(error => {
                console.error('删除数据源失败:', error);
                throw error;
            });
    },

    /**
     * 测试数据源连接
     * @param {string} id 数据源ID
     * @returns {Promise} 返回连接测试结果
     */
    testConnection(id) {
        return axios.post(`/api/datasources/${id}/test-connection`)
            .then(response => ({ data: response.data }))
            .catch(error => {
                console.error('测试连接失败:', error);
                return {
                    data: {
                        success: false,
                        message: error.response?.data?.message || '连接失败，请检查连接信息'
                    }
                };
            });
    },

    /**
     * 测试新数据源连接
     * @param {Object} dataSource 数据源信息
     * @returns {Promise} 返回连接测试结果
     */
    testNewConnection(dataSource) {
        return axios.post('/api/datasources/test-connection', dataSource)
            .then(response => ({ data: response.data }))
            .catch(error => {
                console.error('测试连接失败:', error);
                return {
                    data: {
                        success: false,
                        message: error.response?.data?.message || '连接失败，请检查连接信息'
                    }
                };
            });
    },

    /**
     * 同步数据源元数据
     * @param {string} id 数据源ID
     * @param {Object} options 同步选项
     * @returns {Promise} 返回同步任务信息
     */
    syncMetadata(id, options = {}) {
        return axios.post(`/api/datasources/${id}/sync`, options)
            .then(response => ({ data: response.data }))
            .catch(error => {
                console.error('同步元数据失败:', error);
                throw error;
            });
    },

    /**
     * 切换数据源状态
     * @param {string} id 数据源ID
     * @param {boolean} active 是否激活
     * @returns {Promise} 返回更新后的数据源
     */
    toggleStatus(id, active) {
        // 实际实现转为调用updateDataSource
        return this.updateDataSource(id, { enabled: active });
    },

    /**
     * 检查数据源名称是否存在
     * @param {string} name 数据源名称
     * @param {string} excludeId 排除的数据源ID
     * @returns {Promise} 返回检查结果
     */
    checkNameExists(name, excludeId) {
        const queryParams = new URLSearchParams();
        queryParams.append('name', name);
        
        if (excludeId) {
            queryParams.append('excludeId', excludeId);
        }
        
        return axios.get(`/api/datasources/check-name?${queryParams.toString()}`)
            .then(response => ({ data: response.data }))
            .catch(error => {
                console.error('检查名称是否存在失败:', error);
                throw error;
            });
    }
};

// 使DataSourceService成为全局变量
window.DataSourceService = DataSourceService;
```

### 2. 调整数据源列表组件

`datasource-list.js`需要一些小的调整来适配后端数据格式：

```javascript
// 在fetchDataSources方法中:
async fetchDataSources() {
    this.loading = true;
    try {
        // 构建查询参数
        const params = {
            page: this.pagination.current - 1, // 后端从0开始计数
            size: this.pagination.pageSize,
            sort: `${this.sorter.field},${this.sorter.order === 'ascend' ? 'asc' : 'desc'}`
        };

        if (this.searchQuery) {
            params.search = this.searchQuery;
        }
        if (this.typeFilter) {
            params.type = this.typeFilter;
        }
        if (this.statusFilter !== '') {
            params.active = this.statusFilter;
        }

        const response = await DataSourceService.getDataSources(params);
        
        // 字段映射调整
        this.dataSources = response.data.content.map(ds => ({
            ...ds,
            // 确保字段匹配
            active: ds.enabled, // 后端使用enabled，前端使用active
            // 其他字段映射...
        }));
        
        this.pagination.total = response.data.pageable.totalElements;
    } catch (error) {
        console.error('获取数据源列表失败:', error);
        this.$message.error('获取数据源列表失败');
    } finally {
        this.loading = false;
    }
}

// 在handleToggleStatus方法中:
async handleToggleStatus(record) {
    const newStatus = !record.active;
    const action = newStatus ? '激活' : '禁用';
    
    this.$confirm({
        title: `确认${action}`,
        content: `确定要${action}数据源 "${record.name}" 吗？`,
        okText: '确认',
        cancelText: '取消',
        onOk: async () => {
            try {
                // 调整为使用enabled字段
                await DataSourceService.toggleStatus(record.id, newStatus);
                this.$message.success(`${action}成功`);
                this.fetchDataSources();
            } catch (error) {
                console.error(`${action}数据源失败:`, error);
                this.$message.error(`${action}数据源失败`);
            }
        }
    });
}
```

### 3. 调整数据源表单组件

`datasource-form.js`需要调整以适配后端模型：

```javascript
// 在提交表单方法中:
async handleSubmit() {
    try {
        const formData = { ...this.form };
        
        // 字段映射
        formData.enabled = formData.active; // 转换active到enabled
        delete formData.active;
        
        // 根据是新建还是编辑调用不同方法
        if (this.isEdit) {
            await DataSourceService.updateDataSource(this.dataSourceId, formData);
            this.$message.success('更新数据源成功');
        } else {
            await DataSourceService.createDataSource(formData);
            this.$message.success('创建数据源成功');
        }
        
        // 返回列表页
        this.$router.push('/datasource');
    } catch (error) {
        console.error('保存数据源失败:', error);
        this.$message.error('保存数据源失败: ' + (error.response?.data?.message || error.message));
    }
}

// 在加载数据方法中:
async loadDataSource() {
    this.loading = true;
    try {
        const response = await DataSourceService.getDataSource(this.dataSourceId);
        const data = response.data;
        
        // 字段映射
        this.form = {
            ...data,
            active: data.enabled, // 转换enabled到active
            // 避免显示密码
            password: '', // 清空密码，不从后端获取
        };
    } catch (error) {
        console.error('加载数据源失败:', error);
        this.$message.error('加载数据源失败');
    } finally {
        this.loading = false;
    }
}
```

### 4. 调整数据源详情组件

`datasource-detail.js`需要调整以适配后端数据和API：

```javascript
// 在加载数据源方法中:
async loadDataSource() {
    this.loading = true;
    try {
        const [dataSourceResponse, metadataStatsResponse] = await Promise.all([
            DataSourceService.getDataSource(this.dataSourceId),
            DataSourceService.getMetadataStats(this.dataSourceId)
        ]);
        
        // 字段映射
        this.dataSource = {
            ...dataSourceResponse.data,
            active: dataSourceResponse.data.enabled, // 转换enabled到active
        };
        this.metadataStats = metadataStatsResponse.data;
        
        // 加载元数据树和同步历史
        this.loadMetadataTree();
        this.loadSyncHistory();
    } catch (error) {
        console.error('加载数据源详情失败:', error);
        this.$message.error('加载数据源详情失败');
    } finally {
        this.loading = false;
    }
}

// 在同步元数据方法中:
async handleSync() {
    try {
        await DataSourceService.syncMetadata(this.dataSourceId);
        this.$message.success('同步任务已启动，请稍后刷新查看结果');
        
        // 定时刷新同步状态
        this.startSyncStatusPolling();
    } catch (error) {
        console.error('启动同步任务失败:', error);
        this.$message.error('启动同步任务失败');
    }
}

// 添加轮询方法监控同步状态
startSyncStatusPolling() {
    // 取消现有轮询
    this.stopSyncStatusPolling();
    
    // 设置轮询间隔，每3秒检查一次
    this.syncStatusPollingInterval = setInterval(async () => {
        try {
            // 刷新数据源信息和同步历史
            const dataSourceResponse = await DataSourceService.getDataSource(this.dataSourceId);
            this.dataSource = {
                ...dataSourceResponse.data,
                active: dataSourceResponse.data.enabled,
            };
            
            // 刷新同步历史
            this.loadSyncHistory();
            
            // 如果同步完成，停止轮询
            const lastSyncJob = this.syncHistory[0];
            if (lastSyncJob && ['SUCCESS', 'FAILED'].includes(lastSyncJob.status)) {
                this.stopSyncStatusPolling();
                
                // 刷新元数据统计和树形结构
                this.loadMetadataStats();
                this.loadMetadataTree();
                
                // 显示结果消息
                const message = lastSyncJob.status === 'SUCCESS' 
                    ? '同步成功完成' 
                    : `同步失败: ${lastSyncJob.result || '未知错误'}`;
                    
                this.$message[lastSyncJob.status === 'SUCCESS' ? 'success' : 'error'](message);
            }
        } catch (error) {
            console.error('轮询同步状态失败:', error);
            this.stopSyncStatusPolling();
        }
    }, 3000);
}

stopSyncStatusPolling() {
    if (this.syncStatusPollingInterval) {
        clearInterval(this.syncStatusPollingInterval);
        this.syncStatusPollingInterval = null;
    }
}

// 组件销毁时清理
beforeDestroy() {
    this.stopSyncStatusPolling();
}
```

## 数据格式映射表

下面是前后端数据格式映射表，用于帮助理解字段对应关系：

| 前端字段 | 后端字段 | 说明 |
|----------|----------|------|
| id | id | 数据源ID |
| name | name | 数据源名称 |
| type | type | 数据源类型 |
| host | host | 数据库主机 |
| port | port | 数据库端口 |
| databaseName | databaseName | 数据库名称 |
| username | username | 用户名 |
| password | password | 密码（只用于创建和更新） |
| active | enabled | 是否激活/启用 |
| description | description | 描述信息 |
| lastSyncedAt | lastSyncTime | 最后同步时间 |
| createdAt | createdAt | 创建时间 |
| updatedAt | updatedAt | 更新时间 |
| tags | tags | 标签 |
| syncStatus | - | 前端计算的同步状态（基于最新同步任务） |

## 错误处理和重试机制

为了增强前端的健壮性，建议实现以下错误处理和重试机制：

### 1. 全局错误处理

```javascript
// 在main.js或app.js中添加
axios.interceptors.response.use(
    response => response,
    error => {
        // 统一处理HTTP错误
        const status = error.response ? error.response.status : null;
        
        if (status === 401) {
            // 未授权，重定向到登录页
            window.location.href = '/login';
        } else if (status === 403) {
            // 权限不足
            Vue.prototype.$message.error('您没有权限执行此操作');
        } else if (status === 404) {
            // 资源不存在
            Vue.prototype.$message.error('请求的资源不存在');
        } else if (status === 500) {
            // 服务器错误
            Vue.prototype.$message.error('服务器内部错误，请稍后重试');
        } else {
            // 其他错误
            Vue.prototype.$message.error(error.response?.data?.message || '请求失败，请重试');
        }
        
        return Promise.reject(error);
    }
);
```

### 2. 添加请求重试功能

```javascript
// 在DataSourceService中添加通用的重试函数
function retryRequest(requestFn, maxRetries = 3, delay = 1000) {
    return new Promise((resolve, reject) => {
        let retries = 0;
        
        function attempt() {
            requestFn()
                .then(resolve)
                .catch(error => {
                    retries++;
                    if (retries < maxRetries) {
                        // 延迟后重试
                        console.log(`请求失败，${delay/1000}秒后第${retries}次重试...`);
                        setTimeout(attempt, delay);
                    } else {
                        reject(error);
                    }
                });
        }
        
        attempt();
    });
}

// 使用示例
async getDataSource(id) {
    return retryRequest(
        () => axios.get(`/api/datasources/${id}`),
        3,  // 最多重试3次
        1000 // 每次间隔1秒
    )
        .then(response => ({ data: response.data }))
        .catch(error => {
            console.error('获取数据源详情失败:', error);
            throw error;
        });
}
```

## 缓存策略

为了提高性能，可以实现以下缓存策略：

```javascript
// 在DataSourceService中添加缓存功能
const cache = {
    data: new Map(),
    
    get(key) {
        const item = this.data.get(key);
        if (!item) return null;
        
        // 检查缓存是否过期
        if (item.expiry && item.expiry < Date.now()) {
            this.data.delete(key);
            return null;
        }
        
        return item.value;
    },
    
    set(key, value, ttl = 60000) { // 默认缓存1分钟
        const expiry = ttl ? Date.now() + ttl : null;
        this.data.set(key, { value, expiry });
    },
    
    delete(key) {
        this.data.delete(key);
    },
    
    clear() {
        this.data.clear();
    }
};

// 使用缓存的例子
async getSupportedTypes() {
    const cacheKey = 'datasource-types';
    const cachedData = cache.get(cacheKey);
    
    if (cachedData) {
        return Promise.resolve({ data: cachedData });
    }
    
    return axios.get('/api/datasources/types')
        .then(response => {
            cache.set(cacheKey, response.data, 3600000); // 缓存1小时
            return { data: response.data };
        })
        .catch(error => {
            console.error('获取数据源类型失败:', error);
            throw error;
        });
}
```

## 测试策略

为确保前端服务适配的正确性，建议进行以下测试：

1. **单元测试**：测试各API方法的调用和数据处理
2. **集成测试**：测试前端组件与后端API的交互
3. **端到端测试**：模拟用户交互，验证完整流程

### 单元测试示例

```javascript
// datasource-service.spec.js
describe('DataSourceService', () => {
    let axiosGetSpy, axiosPostSpy, axiosPutSpy, axiosDeleteSpy;
    
    beforeEach(() => {
        // Mock Axios
        axiosGetSpy = jest.spyOn(axios, 'get').mockImplementation(() => Promise.resolve({ data: [] }));
        axiosPostSpy = jest.spyOn(axios, 'post').mockImplementation(() => Promise.resolve({ data: {} }));
        axiosPutSpy = jest.spyOn(axios, 'put').mockImplementation(() => Promise.resolve({ data: {} }));
        axiosDeleteSpy = jest.spyOn(axios, 'delete').mockImplementation(() => Promise.resolve({ data: {} }));
    });
    
    afterEach(() => {
        jest.restoreAllMocks();
    });
    
    test('getDataSources should call correct API with parameters', async () => {
        const params = { page: 1, size: 10, sort: 'name,asc', type: 'MYSQL' };
        await DataSourceService.getDataSources(params);
        
        expect(axiosGetSpy).toHaveBeenCalledWith(
            expect.stringContaining('/api/datasources?page=1&size=10&sort=name%2Casc&type=MYSQL')
        );
    });
    
    test('createDataSource should send data to correct endpoint', async () => {
        const dataSource = { name: 'Test DB', type: 'MYSQL', host: 'localhost' };
        await DataSourceService.createDataSource(dataSource);
        
        expect(axiosPostSpy).toHaveBeenCalledWith('/api/datasources', dataSource);
    });
    
    // 其他测试...
});
```

## 调试技巧

在API适配过程中，使用以下调试技巧可以帮助快速定位问题：

1. **添加请求/响应日志**：
```javascript
// 在main.js中添加
axios.interceptors.request.use(config => {
    console.log('Request:', config.method.toUpperCase(), config.url, config.data || config.params);
    return config;
});

axios.interceptors.response.use(
    response => {
        console.log('Response:', response.status, response.config.url, response.data);
        return response;
    },
    error => {
        console.error('Error:', error.response?.status, error.config?.url, error.response?.data);
        return Promise.reject(error);
    }
);
```

2. **添加开发模式切换**：
```javascript
// 添加开发模式切换，在开发环境中可使用模拟数据
const useDevMode = localStorage.getItem('devMode') === 'true';

// 在各API方法中
getDataSources(params) {
    if (useDevMode) {
        // 使用原来的模拟数据实现
        return this._mockGetDataSources(params);
    }
    
    // 使用实际API调用
    // ...
}

// 保留原模拟实现，重命名为_mockGetDataSources
_mockGetDataSources(params) {
    // 原模拟数据实现
}
```

## 结论

通过按照本指南进行调整，前端数据源服务将从使用模拟数据转变为调用实际的后端API。这一过程需要注意数据格式的匹配、错误处理以及性能优化。在实施过程中，建议分阶段进行，首先完成基本的CRUD操作，然后再实现更复杂的功能如元数据同步和浏览。

针对大数据量的情况，特别是满足2万条数据响应时间TP99<100ms的性能要求，前端需要实现虚拟滚动、分页加载以及适当的缓存策略，以确保良好的用户体验。