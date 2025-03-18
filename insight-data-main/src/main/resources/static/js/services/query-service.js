/**
 * 查询服务
 * 处理查询执行、模板管理等相关操作
 */
const QueryService = {
    /**
     * 执行查询
     * @param {Object} params 查询参数
     * @returns {Promise} 查询结果
     */
    async executeQuery(params) {
        return axios.post('/api/query/execute', params);
    },

    /**
     * 取消查询
     * @param {string} queryId 查询ID
     * @returns {Promise}
     */
    async cancelQuery(queryId) {
        return axios.post(`/api/query/${queryId}/cancel`);
    },

    /**
     * 获取查询状态
     * @param {string} queryId 查询ID
     * @returns {Promise}
     */
    async getQueryStatus(queryId) {
        return axios.get(`/api/query/${queryId}/status`);
    },

    /**
     * 获取查询结果
     * @param {string} queryId 查询ID
     * @param {Object} params 分页参数
     * @returns {Promise}
     */
    async getQueryResults(queryId, params) {
        return axios.get(`/api/query/${queryId}/results`, { params });
    },

    /**
     * 获取查询执行计划
     * @param {string} queryId 查询ID
     * @returns {Promise}
     */
    async getQueryPlan(queryId) {
        return axios.get(`/api/query/${queryId}/plan`);
    },

    /**
     * 获取查询历史
     * @param {string} dataSourceId 数据源ID
     * @param {Object} params 查询参数
     * @returns {Promise}
     */
    async getQueryHistory(dataSourceId, params) {
        return axios.get(`/api/query/history/${dataSourceId}`, { params });
    },

    /**
     * 保存查询模板
     * @param {Object} template 查询模板
     * @returns {Promise}
     */
    async createQueryTemplate(template) {
        return axios.post('/api/query/templates', template);
    },

    /**
     * 更新查询模板
     * @param {string} templateId 模板ID
     * @param {Object} template 查询模板
     * @returns {Promise}
     */
    async updateQueryTemplate(templateId, template) {
        return axios.put(`/api/query/templates/${templateId}`, template);
    },

    /**
     * 获取查询模板列表
     * @param {string} dataSourceId 数据源ID
     * @param {Object} params 查询参数
     * @returns {Promise}
     */
    async getQueryTemplates(dataSourceId, params) {
        return axios.get(`/api/query/templates/${dataSourceId}`, { params });
    },

    /**
     * 删除查询模板
     * @param {string} templateId 模板ID
     * @returns {Promise}
     */
    async deleteQueryTemplate(templateId) {
        return axios.delete(`/api/query/templates/${templateId}`);
    },

    /**
     * 获取导出URL
     * @param {string} queryId 查询ID
     * @param {string} format 导出格式
     * @returns {string} 导出URL
     */
    getExportUrl(queryId, format) {
        return `/api/query/${queryId}/export?format=${format}`;
    },

    /**
     * 检查SQL语法
     * @param {Object} params SQL检查参数
     * @returns {Promise}
     */
    async checkSqlSyntax(params) {
        return axios.post('/api/query/check-syntax', params);
    },

    /**
     * 获取查询建议
     * @param {string} dataSourceId 数据源ID
     * @param {string} sql SQL语句
     * @returns {Promise}
     */
    async getQuerySuggestions(dataSourceId, sql) {
        return axios.get(`/api/query/suggestions/${dataSourceId}`, {
            params: { sql }
        });
    },

    /**
     * 预估查询资源消耗
     * @param {Object} params 查询参数
     * @returns {Promise}
     */
    async estimateQueryResources(params) {
        return axios.post('/api/query/estimate-resources', params);
    },

    /**
     * 获取查询模板参数建议值
     * @param {string} templateId 模板ID
     * @param {string} paramName 参数名
     * @returns {Promise}
     */
    async getTemplateParamSuggestions(templateId, paramName) {
        return axios.get(`/api/query/templates/${templateId}/params/${paramName}/suggestions`);
    },

    /**
     * 获取查询统计信息
     * @param {string} dataSourceId 数据源ID
     * @returns {Promise}
     */
    async getQueryStatistics(dataSourceId) {
        return axios.get(`/api/query/statistics/${dataSourceId}`);
    }
};

export default QueryService;