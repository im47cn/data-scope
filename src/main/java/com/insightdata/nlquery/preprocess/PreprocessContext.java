package com.insightdata.nlquery.preprocess;

/**
 * 预处理上下文
 * 包含预处理过程中需要的上下文信息
 */
public class PreprocessContext {
    
    // 数据源ID
    private Long dataSourceId;
    
    // 用户ID
    private Long userId;
    
    // 会话ID
    private String sessionId;
    
    // 上一次查询
    private String previousQuery;
    
    // 领域
    private String domain;
    
    /**
     * 默认构造函数
     */
    public PreprocessContext() {
    }
    
    /**
     * 构造函数
     *
     * @param dataSourceId 数据源ID
     * @param userId 用户ID
     * @param sessionId 会话ID
     */
    public PreprocessContext(Long dataSourceId, Long userId, String sessionId) {
        this.dataSourceId = dataSourceId;
        this.userId = userId;
        this.sessionId = sessionId;
    }
    
    /**
     * 获取数据源ID
     *
     * @return 数据源ID
     */
    public Long getDataSourceId() {
        return dataSourceId;
    }
    
    /**
     * 设置数据源ID
     *
     * @param dataSourceId 数据源ID
     */
    public void setDataSourceId(Long dataSourceId) {
        this.dataSourceId = dataSourceId;
    }
    
    /**
     * 获取用户ID
     *
     * @return 用户ID
     */
    public Long getUserId() {
        return userId;
    }
    
    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    /**
     * 获取会话ID
     *
     * @return 会话ID
     */
    public String getSessionId() {
        return sessionId;
    }
    
    /**
     * 设置会话ID
     *
     * @param sessionId 会话ID
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    /**
     * 获取上一次查询
     *
     * @return 上一次查询
     */
    public String getPreviousQuery() {
        return previousQuery;
    }
    
    /**
     * 设置上一次查询
     *
     * @param previousQuery 上一次查询
     */
    public void setPreviousQuery(String previousQuery) {
        this.previousQuery = previousQuery;
    }
    
    /**
     * 获取领域
     *
     * @return 领域
     */
    public String getDomain() {
        return domain;
    }
    
    /**
     * 设置领域
     *
     * @param domain 领域
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }
}
