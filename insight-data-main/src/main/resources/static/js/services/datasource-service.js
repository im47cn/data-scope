/**
 * 数据源管理服务
 */
const DataSourceService = {
    /**
     * 获取数据源列表
     * @param {Object} params 查询参数
     * @returns {Promise} 返回数据源列表
     */
    getDataSources(params) {
        const defaultParams = {
            page: 0,
            size: 10,
            sort: 'createdAt,desc'
        };
        
        return axios.get('/api/v1/data-sources', {
            params: { ...defaultParams, ...params }
        });
    },

    /**
     * 获取支持的数据源类型
     * @returns {Promise} 返回数据源类型列表
     */
    getSupportedTypes() {
        return axios.get('/api/v1/data-sources/types');
    },

    /**
     * 创建数据源
     * @param {Object} dataSource 数据源信息
     * @returns {Promise} 返回创建的数据源
     */
    createDataSource(dataSource) {
        return axios.post('/api/v1/data-sources', dataSource);
    },

    /**
     * 更新数据源
     * @param {string} id 数据源ID
     * @param {Object} dataSource 数据源信息
     * @returns {Promise} 返回更新后的数据源
     */
    updateDataSource(id, dataSource) {
        return axios.put(`/api/v1/data-sources/${id}`, dataSource);
    },

    /**
     * 删除数据源
     * @param {string} id 数据源ID
     * @returns {Promise}
     */
    deleteDataSource(id) {
        return axios.delete(`/api/v1/data-sources/${id}`);
    },

    /**
     * 测试数据源连接
     * @param {string} id 数据源ID
     * @returns {Promise} 返回连接测试结果
     */
    testConnection(id) {
        return axios.post(`/api/v1/data-sources/${id}/test-connection`);
    },

    /**
     * 同步数据源元数据
     * @param {string} id 数据源ID
     * @param {Object} options 同步选项
     * @returns {Promise} 返回同步任务信息
     */
    syncMetadata(id, options = {}) {
        return axios.post(`/api/v1/data-sources/${id}/sync-metadata`, options);
    },

    /**
     * 获取同步任务状态
     * @param {string} jobId 同步任务ID
     * @returns {Promise} 返回同步任务状态
     */
    getSyncStatus(jobId) {
        return axios.get(`/api/v1/metadata-sync-jobs/${jobId}`);
    },

    /**
     * 取消同步任务
     * @param {string} jobId 同步任务ID
     * @returns {Promise}
     */
    cancelSync(jobId) {
        return axios.post(`/api/v1/metadata-sync-jobs/${jobId}/cancel`);
    },

    /**
     * 切换数据源状态
     * @param {string} id 数据源ID
     * @param {boolean} active 是否激活
     * @returns {Promise} 返回更新后的数据源
     */
    toggleStatus(id, active) {
        return axios.patch(`/api/v1/data-sources/${id}/status`, { active });
    }
};

export default DataSourceService;
