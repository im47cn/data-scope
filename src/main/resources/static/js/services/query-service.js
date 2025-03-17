/**
 * 查询构建器服务
 * 提供SQL生成和查询执行相关的功能
 */
const QueryService = {
    /**
     * 根据查询配置生成SQL
     * @param {Object} queryConfig 查询配置
     * @returns {Promise} 返回生成的SQL
     */
    generateSql(queryConfig) {
        return axios.post('/api/v1/query/generate-sql', { queryConfig });
    },

    /**
     * 验证SQL语句
     * @param {Object} params 验证参数
     * @returns {Promise} 返回验证结果
     */
    validateSql(params) {
        return axios.post('/api/v1/query/validate-sql', params);
    },

    /**
     * 执行查询
     * @param {Object} params 查询参数
     * @returns {Promise} 返回查询ID
     */
    executeQuery(params) {
        return axios.post('/api/v1/query/execute', params);
    },

    /**
     * 获取查询状态
     * @param {string} queryId 查询ID
     * @returns {Promise} 返回查询状态
     */
    getQueryStatus(queryId) {
        return axios.get(`/api/v1/query/${queryId}/status`);
    },

    /**
     * 取消查询
     * @param {string} queryId 查询ID
     * @returns {Promise}
     */
    cancelQuery(queryId) {
        return axios.post(`/api/v1/query/${queryId}/cancel`);
    },

    /**
     * 获取查询结果
     * @param {string} queryId 查询ID
     * @param {Object} params 分页和排序参数
     * @returns {Promise} 返回查询结果
     */
    getQueryResults(queryId, params) {
        return axios.get(`/api/v1/query/${queryId}/results`, { params });
    },

    /**
     * 导出查询结果
     * @param {string} queryId 查询ID
     * @param {string} format 导出格式
     * @returns {string} 导出文件的URL
     */
    getExportUrl(queryId, format) {
        return `/api/v1/query/${queryId}/export?format=${format}`;
    },

    /**
     * 保存查询
     * @param {Object} query 查询配置
     * @returns {Promise} 返回保存的查询
     */
    saveQuery(query) {
        return axios.post('/api/v1/saved-queries', query);
    },

    /**
     * 获取已保存的查询
     * @param {Object} params 查询参数
     * @returns {Promise} 返回查询列表
     */
    getSavedQueries(params) {
        return axios.get('/api/v1/saved-queries', { params });
    },

    /**
     * 获取查询详情
     * @param {string} queryId 查询ID
     * @returns {Promise} 返回查询详情
     */
    getSavedQuery(queryId) {
        return axios.get(`/api/v1/saved-queries/${queryId}`);
    },

    /**
     * 更新已保存的查询
     * @param {string} queryId 查询ID
     * @param {Object} query 查询配置
     * @returns {Promise} 返回更新后的查询
     */
    updateSavedQuery(queryId, query) {
        return axios.put(`/api/v1/saved-queries/${queryId}`, query);
    },

    /**
     * 删除已保存的查询
     * @param {string} queryId 查询ID
     * @returns {Promise}
     */
    deleteSavedQuery(queryId) {
        return axios.delete(`/api/v1/saved-queries/${queryId}`);
    },

    /**
     * 共享查询
     * @param {string} queryId 查询ID
     * @param {Object} params 共享参数
     * @returns {Promise} 返回共享结果
     */
    shareQuery(queryId, params) {
        return axios.post(`/api/v1/saved-queries/${queryId}/share`, params);
    },

    /**
     * 获取查询历史
     * @param {Object} params 查询参数
     * @returns {Promise} 返回历史记录
     */
    getQueryHistory(params) {
        return axios.get('/api/v1/query-history', { params });
    },

    /**
     * 推断表关系
     * @param {string} dataSourceId 数据源ID
     * @param {string} leftTable 左表名
     * @param {string} rightTable 右表名
     * @returns {Promise} 返回推断的关系
     */
    inferTableRelationship(dataSourceId, leftTable, rightTable) {
        return axios.get(`/api/v1/data-sources/${dataSourceId}/relationships/infer`, {
            params: { leftTable, rightTable }
        });
    },

    /**
     * 获取表关系
     * @param {string} dataSourceId 数据源ID
     * @param {string} schema 模式名
     * @returns {Promise} 返回表关系列表
     */
    getTableRelationships(dataSourceId, schema) {
        return axios.get(`/api/v1/data-sources/${dataSourceId}/schemas/${schema}/relationships`);
    },

    /**
     * 分析查询性能
     * @param {Object} params 查询参数
     * @returns {Promise} 返回性能分析结果
     */
    analyzeQuery(params) {
        return axios.post('/api/v1/query/analyze', params);
    }
};

export default QueryService;