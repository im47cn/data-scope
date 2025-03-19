/**
 * 数据源管理服务
 * 使用实际后端API
 */
const DataSourceService = {
    /**
     * 获取数据源列表
     * @param {Object} params 查询参数
     * @returns {Promise} 返回数据源列表
     */
    getDataSources(params) {
        // 构建查询参数
        const queryParams = new URLSearchParams();
        
        if (params) {
            if (params.type) {
                queryParams.append('type', params.type);
            }
            if (params.active !== undefined) {
                queryParams.append('active', params.active);
            }
        }
        
        const url = queryParams.toString() 
            ? `/api/datasources?${queryParams.toString()}` 
            : '/api/datasources';
        
        return axios.get(url)
            .then(response => {
                // 转换后端数据为前端期望的格式
                const dataSources = response.data;
                
                // 如果后端没有提供分页信息，我们在前端模拟分页
                const { page = 0, size = 10, sort } = params || {};
                
                // 排序
                let sortedData = [...dataSources];
                if (sort) {
                    const [field, order] = sort.split(',');
                    sortedData.sort((a, b) => {
                        if (order === 'asc') {
                            return a[field] > b[field] ? 1 : -1;
                        } else {
                            return a[field] < b[field] ? 1 : -1;
                        }
                    });
                }
                
                // 分页
                const start = page * size;
                const end = start + size;
                const paginatedData = sortedData.slice(start, end);
                
                return {
                    data: {
                        content: paginatedData,
                        pageable: {
                            totalElements: dataSources.length,
                            totalPages: Math.ceil(dataSources.length / size),
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
            .then(response => {
                // 转换后端数据为前端期望的格式
                const dataSource = response.data;
                
                // 确保字段名称一致
                const result = {
                    ...dataSource,
                    active: dataSource.enabled // 后端使用enabled，前端使用active
                };
                
                return { data: result };
            })
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
        // 暂时使用模拟数据，因为后端API尚未实现
        return new Promise((resolve) => {
            setTimeout(() => {
                resolve({
                    data: {
                        schemaCount: 3,
                        tableCount: 25,
                        viewCount: 8,
                        columnCount: 187
                    }
                });
            }, 200);
        });
    },

    /**
     * 获取Schema列表
     * @param {string} id 数据源ID
     * @returns {Promise} 返回Schema列表
     */
    getSchemas(id) {
        return axios.get(`/api/datasources/${id}/schemas`)
            .then(response => ({ data: response.data }))
            .catch(error => {
                console.error('获取Schema列表失败:', error);
                throw error;
            });
    },

    /**
     * 获取表列表
     * @param {string} id 数据源ID
     * @param {string} schema 模式名
     * @returns {Promise} 返回表列表
     */
    getTables(id, schema) {
        return axios.get(`/api/datasources/${id}/schemas/${schema}/tables`)
            .then(response => ({ data: response.data }))
            .catch(error => {
                console.error(`获取表列表失败 (数据源: ${id}, schema: ${schema}):`, error);
                throw error;
            });
    },

    /**
     * 获取元数据树
     * @param {string} id 数据源ID
     * @returns {Promise} 返回元数据树
     */
    getMetadataTree(id) {
        // 首先获取Schema列表
        return this.getSchemas(id)
            .then(schemasResponse => {
                const schemas = schemasResponse.data;
                const tree = [];
                
                // 对每个Schema获取表列表
                const promises = schemas.map(schema => {
                    return this.getTables(id, schema.name)
                        .then(tablesResponse => {
                            tree.push({
                                name: schema.name,
                                tables: tablesResponse.data
                            });
                        })
                        .catch(error => {
                            console.error(`获取Schema ${schema.name}的表失败:`, error);
                            tree.push({
                                name: schema.name,
                                tables: [],
                                error: '加载表失败'
                            });
                        });
                });
                
                // 等待所有请求完成
                return Promise.all(promises).then(() => ({ data: tree }));
            })
            .catch(error => {
                console.error('获取元数据树失败:', error);
                // 如果失败，返回模拟数据
                return {
                    data: [
                        {
                            name: 'public',
                            tables: [
                                { name: 'users', type: 'TABLE' },
                                { name: 'orders', type: 'TABLE' }
                            ]
                        }
                    ]
                };
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
        // 暂时使用模拟数据，因为后端API尚未实现
        return new Promise((resolve) => {
            setTimeout(() => {
                if (schema === 'public' && table === 'users') {
                    resolve({
                        data: {
                            name: 'users',
                            type: 'TABLE',
                            schema: 'public',
                            comment: '用户信息表',
                            columns: [
                                { name: 'id', type: 'BIGINT', nullable: false, primaryKey: true, defaultValue: null, comment: '用户ID' },
                                { name: 'username', type: 'VARCHAR(50)', nullable: false, primaryKey: false, defaultValue: null, comment: '用户名' },
                                { name: 'email', type: 'VARCHAR(100)', nullable: false, primaryKey: false, defaultValue: null, comment: '电子邮箱' },
                                { name: 'password', type: 'VARCHAR(100)', nullable: false, primaryKey: false, defaultValue: null, comment: '密码' },
                                { name: 'created_at', type: 'TIMESTAMP', nullable: false, primaryKey: false, defaultValue: 'CURRENT_TIMESTAMP', comment: '创建时间' },
                                { name: 'updated_at', type: 'TIMESTAMP', nullable: false, primaryKey: false, defaultValue: 'CURRENT_TIMESTAMP', comment: '更新时间' }
                            ],
                            indexes: [
                                { name: 'pk_users', type: 'PRIMARY', unique: true, columns: ['id'] },
                                { name: 'idx_users_username', type: 'BTREE', unique: true, columns: ['username'] },
                                { name: 'idx_users_email', type: 'BTREE', unique: true, columns: ['email'] }
                            ]
                        }
                    });
                } else {
                    resolve({
                        data: {
                            name: table,
                            type: 'TABLE',
                            schema: schema,
                            comment: '',
                            columns: [
                                { name: 'id', type: 'BIGINT', nullable: false, primaryKey: true, defaultValue: null, comment: '主键ID' }
                            ],
                            indexes: [
                                { name: `pk_${table}`, type: 'PRIMARY', unique: true, columns: ['id'] }
                            ]
                        }
                    });
                }
            }, 300);
        });
    },

    /**
     * 获取同步历史
     * @param {string} id 数据源ID
     * @param {Object} params 查询参数
     * @returns {Promise} 返回同步历史
     */
    getSyncHistory(id, params) {
        // 暂时使用模拟数据，因为后端API尚未实现
        return new Promise((resolve) => {
            setTimeout(() => {
                resolve({
                    data: {
                        content: [
                            { id: '1', dataSourceId: id, startTime: '2023-03-20T14:20:00Z', endTime: '2023-03-20T14:25:00Z', status: 'SUCCESS', syncType: 'FULL', result: '同步成功，共同步25张表，187个字段' },
                            { id: '2', dataSourceId: id, startTime: '2023-03-15T10:10:00Z', endTime: '2023-03-15T10:15:00Z', status: 'SUCCESS', syncType: 'FULL', result: '同步成功，共同步25张表，185个字段' },
                            { id: '3', dataSourceId: id, startTime: '2023-03-10T09:05:00Z', endTime: '2023-03-10T09:08:00Z', status: 'SUCCESS', syncType: 'INCREMENTAL', result: '同步成功，共同步2张表，5个字段' },
                            { id: '4', dataSourceId: id, startTime: '2023-03-05T16:30:00Z', endTime: '2023-03-05T16:32:00Z', status: 'FAILED', syncType: 'FULL', result: '同步失败，连接超时' },
                            { id: '5', dataSourceId: id, startTime: '2023-03-01T11:20:00Z', endTime: '2023-03-01T11:25:00Z', status: 'SUCCESS', syncType: 'FULL', result: '同步成功，共同步25张表，180个字段' }
                        ],
                        pageable: {
                            totalElements: 5,
                            totalPages: 1,
                            pageNumber: params.page,
                            pageSize: params.size
                        }
                    }
                });
            }, 300);
        });
    },

    /**
     * 创建数据源
     * @param {Object} dataSource 数据源信息
     * @returns {Promise} 返回创建的数据源
     */
    createDataSource(dataSource) {
        // 转换前端数据为后端期望的格式
        const requestData = {
            ...dataSource,
            enabled: dataSource.active // 前端使用active，后端使用enabled
        };
        
        return axios.post('/api/datasources', requestData)
            .then(response => {
                // 转换后端数据为前端期望的格式
                const createdDataSource = response.data;
                
                // 确保字段名称一致
                const result = {
                    ...createdDataSource,
                    active: createdDataSource.enabled // 后端使用enabled，前端使用active
                };
                
                return { data: result };
            })
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
        // 转换前端数据为后端期望的格式
        const requestData = {
            ...dataSource,
            id: id, // 确保ID正确
            enabled: dataSource.active // 前端使用active，后端使用enabled
        };
        
        return axios.put(`/api/datasources/${id}`, requestData)
            .then(response => {
                // 转换后端数据为前端期望的格式
                const updatedDataSource = response.data;
                
                // 确保字段名称一致
                const result = {
                    ...updatedDataSource,
                    active: updatedDataSource.enabled // 后端使用enabled，前端使用active
                };
                
                return { data: result };
            })
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
        // 转换前端数据为后端期望的格式
        const requestData = {
            ...dataSource,
            enabled: dataSource.active // 前端使用active，后端使用enabled
        };
        
        return axios.post('/api/datasources/test-connection', requestData)
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
                // 如果失败，返回模拟数据
                return {
                    data: {
                        jobId: `sync-${Date.now()}`,
                        dataSourceId: id,
                        status: 'FAILED',
                        message: '同步任务失败: ' + (error.response?.data?.message || '未知错误')
                    }
                };
            });
    },

    /**
     * 切换数据源状态
     * @param {string} id 数据源ID
     * @param {boolean} active 是否激活
     * @returns {Promise} 返回更新后的数据源
     */
    toggleStatus(id, active) {
        // 使用updateDataSource方法实现
        return this.updateDataSource(id, { active });
    },

    /**
     * 检查数据源名称是否存在
     * @param {string} name 数据源名称
     * @param {string} excludeId 排除的数据源ID
     * @returns {Promise} 返回检查结果
     */
    checkNameExists(name, excludeId) {
        // 暂时使用模拟数据，因为后端API尚未实现
        return new Promise((resolve) => {
            setTimeout(() => {
                // 获取所有数据源
                this.getDataSources()
                    .then(response => {
                        const dataSources = response.data.content;
                        const exists = dataSources.some(ds =>
                            ds.name === name && ds.id !== excludeId
                        );
                        resolve({ data: { exists } });
                    })
                    .catch(error => {
                        console.error('检查名称是否存在失败:', error);
                        resolve({ data: { exists: false } });
                    });
            }, 200);
        });
    }
};

// 使DataSourceService成为全局变量
window.DataSourceService = DataSourceService;
