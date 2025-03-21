// API服务 - 处理与后端API的通信
const API_BASE_URL = '/api/v1';

// 基础API服务
const ApiService = {
  // 通用请求方法
  async request(method, url, data = null, options = {}) {
    const requestOptions = {
      method,
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      },
      ...options
    };

    // 添加认证信息
    const token = localStorage.getItem('token');
    if (token) {
      requestOptions.headers['Authorization'] = `Bearer ${token}`;
    }

    // 添加请求体
    if (data && (method === 'POST' || method === 'PUT' || method === 'PATCH')) {
      requestOptions.body = JSON.stringify(data);
    }

    try {
      const response = await fetch(`${API_BASE_URL}${url}`, requestOptions);
      
      // 处理非2xx响应
      if (!response.ok) {
        const errorData = await response.json();
        throw {
          status: response.status,
          code: errorData.error?.code || 'UNKNOWN_ERROR',
          message: errorData.error?.message || '请求失败',
          details: errorData.error?.details || null
        };
      }
      
      // 检查响应内容类型
      const contentType = response.headers.get('content-type');
      if (contentType && contentType.includes('application/json')) {
        const jsonResponse = await response.json();
        return jsonResponse.data; // 根据API文档，成功响应中的数据在data字段中
      }
      
      return await response.text();
    } catch (error) {
      console.error('API请求错误:', error);
      throw error;
    }
  },

  // 便捷方法
  get(url, options = {}) {
    return this.request('GET', url, null, options);
  },

  post(url, data, options = {}) {
    return this.request('POST', url, data, options);
  },

  put(url, data, options = {}) {
    return this.request('PUT', url, data, options);
  },

  patch(url, data, options = {}) {
    return this.request('PATCH', url, data, options);
  },

  delete(url, options = {}) {
    return this.request('DELETE', url, null, options);
  },

  // 构建查询字符串
  buildQueryString(params) {
    if (!params) return '';
    
    const queryParams = new URLSearchParams();
    
    Object.keys(params).forEach(key => {
      if (params[key] !== null && params[key] !== undefined && params[key] !== '') {
        queryParams.append(key, params[key]);
      }
    });
    
    const queryString = queryParams.toString();
    return queryString ? `?${queryString}` : '';
  }
};

// 数据源服务
const DataSourceService = {
  // 获取所有数据源
  getAllDataSources(params = {}) {
    const queryString = ApiService.buildQueryString(params);
    return ApiService.get(`/datasources${queryString}`);
  },

  // 获取单个数据源
  getDataSource(id) {
    return ApiService.get(`/datasources/${id}`);
  },

  // 创建数据源
  createDataSource(dataSource) {
    return ApiService.post('/datasources', dataSource);
  },

  // 更新数据源
  updateDataSource(id, dataSource) {
    return ApiService.put(`/datasources/${id}`, dataSource);
  },

  // 删除数据源
  deleteDataSource(id) {
    return ApiService.delete(`/datasources/${id}`);
  },

  // 测试数据源连接
  testConnection(id) {
    return ApiService.post(`/datasources/${id}/test`);
  },

  // 获取支持的数据源类型
  getDataSourceTypes() {
    return ApiService.get('/datasources/types');
  }
};

// 元数据服务
const MetadataService = {
  // 同步元数据
  syncMetadata(dataSourceId, options = {}) {
    return ApiService.post(`/datasources/${dataSourceId}/metadata/sync`, options);
  },

  // 获取同步状态
  getSyncStatus(dataSourceId, jobId) {
    return ApiService.get(`/datasources/${dataSourceId}/metadata/sync/${jobId}`);
  },

  // 取消同步任务
  cancelSync(dataSourceId, jobId) {
    return ApiService.delete(`/datasources/${dataSourceId}/metadata/sync/${jobId}`);
  },

  // 获取数据源模式列表
  getSchemas(dataSourceId, params = {}) {
    const queryString = ApiService.buildQueryString(params);
    return ApiService.get(`/datasources/${dataSourceId}/schemas${queryString}`);
  },

  // 获取模式中的表列表
  getTables(dataSourceId, schemaName, params = {}) {
    const queryString = ApiService.buildQueryString(params);
    return ApiService.get(`/datasources/${dataSourceId}/schemas/${schemaName}/tables${queryString}`);
  },

  // 获取表的列信息
  getColumns(dataSourceId, schemaName, tableName) {
    return ApiService.get(`/datasources/${dataSourceId}/schemas/${schemaName}/tables/${tableName}/columns`);
  },

  // 获取表的索引信息
  getIndexes(dataSourceId, schemaName, tableName) {
    return ApiService.get(`/datasources/${dataSourceId}/schemas/${schemaName}/tables/${tableName}/indexes`);
  },

  // 获取表的外键信息
  getForeignKeys(dataSourceId, schemaName, tableName) {
    return ApiService.get(`/datasources/${dataSourceId}/schemas/${schemaName}/tables/${tableName}/foreignkeys`);
  },

  // 获取元数据树结构
  getMetadataTree(dataSourceId, params = {}) {
    const queryString = ApiService.buildQueryString(params);
    return ApiService.get(`/datasources/${dataSourceId}/metadata/tree${queryString}`);
  }
};

// 查询服务
const QueryService = {
  // 执行SQL查询
  executeQuery(query) {
    return ApiService.post('/queries/execute', query);
  },

  // 验证SQL查询
  validateQuery(query) {
    return ApiService.post('/queries/validate', query);
  },

  // 保存查询
  saveQuery(query) {
    return ApiService.post('/queries', query);
  },

  // 执行保存的查询
  executeSavedQuery(id, params) {
    return ApiService.post(`/queries/${id}/execute`, params);
  },

  // 获取查询历史
  getQueryHistory(params = {}) {
    const queryString = ApiService.buildQueryString(params);
    return ApiService.get(`/queries/history${queryString}`);
  },

  // 获取保存的查询
  getSavedQueries() {
    return ApiService.get('/queries/saved');
  },

  // 获取单个查询
  getQuery(id) {
    return ApiService.get(`/queries/${id}`);
  },

  // 删除查询
  deleteQuery(id) {
    return ApiService.delete(`/queries/${id}`);
  }
};

// 自然语言查询服务
const NLQueryService = {
  // 自然语言转SQL
  translateNLToSQL(dataSourceId, naturalLanguage, context = {}) {
    return ApiService.post('/nl2sql', {
      dataSourceId,
      naturalLanguage,
      context
    });
  },

  // 执行自然语言查询
  executeNLQuery(dataSourceId, naturalLanguage, options = {}) {
    return ApiService.post('/nl2sql/execute', {
      dataSourceId,
      naturalLanguage,
      options
    });
  }
};

// 低代码集成服务
const LowCodeService = {
  // 创建低代码配置
  createConfig(config) {
    return ApiService.post('/lowcode/configs', config);
  },

  // 获取AI辅助配置建议
  getAISuggestion(queryId, preferredDisplayType, description) {
    return ApiService.post('/lowcode/ai-suggestion', {
      queryId,
      preferredDisplayType,
      description
    });
  },

  // 渲染低代码配置
  renderConfig(id, parameters) {
    return ApiService.post(`/lowcode/configs/${id}/render`, {
      parameters
    });
  },

  // 获取低代码配置列表
  getConfigs(params = {}) {
    const queryString = ApiService.buildQueryString(params);
    return ApiService.get(`/lowcode/configs${queryString}`);
  },

  // 获取单个低代码配置
  getConfig(id) {
    return ApiService.get(`/lowcode/configs/${id}`);
  },

  // 更新低代码配置
  updateConfig(id, config) {
    return ApiService.put(`/lowcode/configs/${id}`, config);
  },

  // 删除低代码配置
  deleteConfig(id) {
    return ApiService.delete(`/lowcode/configs/${id}`);
  }
};

// 导出服务
export {
  ApiService,
  DataSourceService,
  MetadataService,
  QueryService,
  NLQueryService,
  LowCodeService
};