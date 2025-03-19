/**
 * 数据源管理服务
 * 使用模拟数据，因为后端API尚未实现
 */
const DataSourceService = {
    // 模拟数据
    mockDataSources: [
        {
            id: '1',
            name: 'MySQL开发环境',
            type: 'MYSQL',
            host: 'localhost',
            port: 3306,
            databaseName: 'dev_db',
            username: 'dev_user',
            active: true,
            createdAt: '2023-01-15T08:30:00Z',
            updatedAt: '2023-03-20T14:25:00Z',
            lastSyncedAt: '2023-03-20T14:25:00Z',
            description: '本地开发环境MySQL数据库',
            tags: ['开发', '测试'],
            syncStatus: { status: 'SUCCESS', progress: 100, updatedAt: '2023-03-20T14:25:00Z' }
        },
        {
            id: '2',
            name: 'PostgreSQL生产环境',
            type: 'POSTGRESQL',
            host: '192.168.1.100',
            port: 5432,
            databaseName: 'prod_db',
            username: 'prod_user',
            active: true,
            createdAt: '2022-11-05T10:15:00Z',
            updatedAt: '2023-03-18T09:10:00Z',
            lastSyncedAt: '2023-03-18T09:10:00Z',
            description: '生产环境PostgreSQL数据库',
            tags: ['生产', '核心'],
            syncStatus: { status: 'SUCCESS', progress: 100, updatedAt: '2023-03-18T09:10:00Z' }
        },
        {
            id: '3',
            name: 'Oracle财务系统',
            type: 'ORACLE',
            host: '192.168.1.200',
            port: 1521,
            databaseName: 'finance',
            username: 'finance_user',
            active: false,
            createdAt: '2022-09-20T11:30:00Z',
            updatedAt: '2023-02-10T16:45:00Z',
            lastSyncedAt: '2023-02-10T16:45:00Z',
            description: '财务部门Oracle数据库',
            tags: ['财务', '核心'],
            syncStatus: { status: 'FAILED', progress: 80, updatedAt: '2023-02-10T16:45:00Z' }
        },
        {
            id: '4',
            name: 'SQL Server客户数据',
            type: 'SQLSERVER',
            host: '192.168.1.150',
            port: 1433,
            databaseName: 'customer_db',
            username: 'crm_user',
            active: true,
            createdAt: '2022-12-01T09:00:00Z',
            updatedAt: '2023-03-15T11:20:00Z',
            lastSyncedAt: '2023-03-15T11:20:00Z',
            description: '客户关系管理系统数据库',
            tags: ['CRM', '客户'],
            syncStatus: { status: 'SUCCESS', progress: 100, updatedAt: '2023-03-15T11:20:00Z' }
        },
        {
            id: '5',
            name: 'DB2历史数据',
            type: 'DB2',
            host: '192.168.1.220',
            port: 50000,
            databaseName: 'history_db',
            username: 'history_user',
            active: true,
            createdAt: '2022-08-15T14:20:00Z',
            updatedAt: '2023-01-25T10:30:00Z',
            lastSyncedAt: '2023-01-25T10:30:00Z',
            description: '历史数据归档数据库',
            tags: ['归档', '历史'],
            syncStatus: { status: 'RUNNING', progress: 45, updatedAt: '2023-03-21T08:15:00Z' }
        }
    ],
    
    /**
     * 获取数据源列表
     * @param {Object} params 查询参数
     * @returns {Promise} 返回数据源列表
     */
    getDataSources(params) {
        return new Promise((resolve) => {
            // 模拟延迟
            setTimeout(() => {
                const { page = 0, size = 10, sort } = params || {};
                
                // 排序
                let sortedData = [...this.mockDataSources];
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
                
                resolve({
                    data: {
                        content: paginatedData,
                        pageable: {
                            totalElements: this.mockDataSources.length,
                            totalPages: Math.ceil(this.mockDataSources.length / size),
                            pageNumber: page,
                            pageSize: size
                        }
                    }
                });
            }, 300);
        });
    },

    /**
     * 获取支持的数据源类型
     * @returns {Promise} 返回数据源类型列表
     */
    getSupportedTypes() {
        return new Promise((resolve) => {
            setTimeout(() => {
                resolve({
                    data: ['MYSQL', 'POSTGRESQL', 'ORACLE', 'SQLSERVER', 'DB2']
                });
            }, 200);
        });
    },

    /**
     * 获取数据源详情
     * @param {string} id 数据源ID
     * @returns {Promise} 返回数据源详情
     */
    getDataSource(id) {
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                const dataSource = this.mockDataSources.find(ds => ds.id === id);
                if (dataSource) {
                    resolve({ data: dataSource });
                } else {
                    reject(new Error('数据源不存在'));
                }
            }, 200);
        });
    },

    /**
     * 获取元数据统计信息
     * @param {string} id 数据源ID
     * @returns {Promise} 返回元数据统计信息
     */
    getMetadataStats(id) {
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
     * 获取元数据树
     * @param {string} id 数据源ID
     * @returns {Promise} 返回元数据树
     */
    getMetadataTree(id) {
        return new Promise((resolve) => {
            setTimeout(() => {
                resolve({
                    data: [
                        {
                            name: 'public',
                            tables: [
                                { name: 'users', type: 'TABLE' },
                                { name: 'orders', type: 'TABLE' },
                                { name: 'products', type: 'TABLE' },
                                { name: 'categories', type: 'TABLE' },
                                { name: 'order_items', type: 'TABLE' }
                            ]
                        },
                        {
                            name: 'reporting',
                            tables: [
                                { name: 'sales_summary', type: 'VIEW' },
                                { name: 'user_activity', type: 'VIEW' },
                                { name: 'product_performance', type: 'VIEW' }
                            ]
                        }
                    ]
                });
            }, 300);
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
        return new Promise((resolve) => {
            setTimeout(() => {
                const newId = (this.mockDataSources.length + 1).toString();
                const newDataSource = {
                    ...dataSource,
                    id: newId,
                    createdAt: new Date().toISOString(),
                    updatedAt: new Date().toISOString(),
                    syncStatus: null
                };
                this.mockDataSources.push(newDataSource);
                resolve({ data: newDataSource });
            }, 500);
        });
    },

    /**
     * 更新数据源
     * @param {string} id 数据源ID
     * @param {Object} dataSource 数据源信息
     * @returns {Promise} 返回更新后的数据源
     */
    updateDataSource(id, dataSource) {
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                const index = this.mockDataSources.findIndex(ds => ds.id === id);
                if (index !== -1) {
                    const updatedDataSource = {
                        ...this.mockDataSources[index],
                        ...dataSource,
                        id,
                        updatedAt: new Date().toISOString()
                    };
                    this.mockDataSources[index] = updatedDataSource;
                    resolve({ data: updatedDataSource });
                } else {
                    reject(new Error('数据源不存在'));
                }
            }, 500);
        });
    },

    /**
     * 删除数据源
     * @param {string} id 数据源ID
     * @returns {Promise}
     */
    deleteDataSource(id) {
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                const index = this.mockDataSources.findIndex(ds => ds.id === id);
                if (index !== -1) {
                    this.mockDataSources.splice(index, 1);
                    resolve({ data: { success: true } });
                } else {
                    reject(new Error('数据源不存在'));
                }
            }, 500);
        });
    },

    /**
     * 测试数据源连接
     * @param {string} id 数据源ID
     * @returns {Promise} 返回连接测试结果
     */
    testConnection(id) {
        return new Promise((resolve) => {
            setTimeout(() => {
                const dataSource = this.mockDataSources.find(ds => ds.id === id);
                if (dataSource && dataSource.active) {
                    resolve({
                        data: {
                            success: true,
                            message: '连接成功',
                            databaseVersion: `${dataSource.type} 8.0.26`
                        }
                    });
                } else {
                    resolve({
                        data: {
                            success: false,
                            message: '连接失败，请检查连接信息'
                        }
                    });
                }
            }, 1000);
        });
    },

    /**
     * 测试新数据源连接
     * @param {Object} dataSource 数据源信息
     * @returns {Promise} 返回连接测试结果
     */
    testNewConnection(dataSource) {
        return new Promise((resolve) => {
            setTimeout(() => {
                // 模拟80%的概率连接成功
                const success = Math.random() > 0.2;
                if (success) {
                    resolve({
                        data: {
                            success: true,
                            message: '连接成功',
                            databaseVersion: `${dataSource.type} 8.0.26`
                        }
                    });
                } else {
                    resolve({
                        data: {
                            success: false,
                            message: '连接失败，请检查连接信息'
                        }
                    });
                }
            }, 1000);
        });
    },

    /**
     * 同步数据源元数据
     * @param {string} id 数据源ID
     * @param {Object} options 同步选项
     * @returns {Promise} 返回同步任务信息
     */
    syncMetadata(id, options = {}) {
        return new Promise((resolve) => {
            setTimeout(() => {
                const dataSource = this.mockDataSources.find(ds => ds.id === id);
                if (dataSource) {
                    dataSource.syncStatus = {
                        status: 'PENDING',
                        progress: 0,
                        updatedAt: new Date().toISOString()
                    };
                    
                    // 模拟同步进度更新
                    let progress = 0;
                    const interval = setInterval(() => {
                        progress += 10;
                        if (progress <= 100) {
                            dataSource.syncStatus.progress = progress;
                            dataSource.syncStatus.updatedAt = new Date().toISOString();
                            
                            if (progress === 100) {
                                clearInterval(interval);
                                dataSource.syncStatus.status = 'SUCCESS';
                                dataSource.lastSyncedAt = new Date().toISOString();
                            }
                        } else {
                            clearInterval(interval);
                        }
                    }, 1000);
                    
                    resolve({
                        data: {
                            id: `sync-${Date.now()}`,
                            dataSourceId: id,
                            status: 'PENDING',
                            message: '同步任务已启动'
                        }
                    });
                } else {
                    resolve({
                        data: {
                            success: false,
                            message: '数据源不存在'
                        }
                    });
                }
            }, 500);
        });
    },

    /**
     * 切换数据源状态
     * @param {string} id 数据源ID
     * @param {boolean} active 是否激活
     * @returns {Promise} 返回更新后的数据源
     */
    toggleStatus(id, active) {
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                const index = this.mockDataSources.findIndex(ds => ds.id === id);
                if (index !== -1) {
                    this.mockDataSources[index].active = active;
                    this.mockDataSources[index].updatedAt = new Date().toISOString();
                    resolve({ data: this.mockDataSources[index] });
                } else {
                    reject(new Error('数据源不存在'));
                }
            }, 300);
        });
    },

    /**
     * 检查数据源名称是否存在
     * @param {string} name 数据源名称
     * @param {string} excludeId 排除的数据源ID
     * @returns {Promise} 返回检查结果
     */
    checkNameExists(name, excludeId) {
        return new Promise((resolve) => {
            setTimeout(() => {
                const exists = this.mockDataSources.some(ds =>
                    ds.name === name && ds.id !== excludeId
                );
                resolve({ data: { exists } });
            }, 200);
        });
    }
};

// 使DataSourceService成为全局变量
window.DataSourceService = DataSourceService;
