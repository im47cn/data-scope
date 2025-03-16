package com.insightdata.nlquery.executor;

import com.insightdata.domain.model.DataSource;

/**
 * 查询执行器接口
 */
public interface QueryExecutor {
    
    /**
     * 执行查询
     *
     * @param sql SQL语句
     * @param dataSource 数据源
     * @return 查询结果
     */
    QueryResult execute(String sql, DataSource dataSource);
    
    /**
     * 执行查询(带元数据)
     *
     * @param sql SQL语句
     * @param dataSource 数据源
     * @param metadata 查询元数据
     * @return 查询结果
     */
    QueryResult execute(String sql, DataSource dataSource, QueryMetadata metadata);
    
    /**
     * 取消查询
     *
     * @param queryId 查询ID
     * @return 是否取消成功
     */
    boolean cancel(String queryId);
    
    /**
     * 获取查询状态
     *
     * @param queryId 查询ID
     * @return 查询状态
     */
    QueryStatus getStatus(String queryId);
    
    /**
     * 查询状态枚举
     */
    enum QueryStatus {
        /**
         * 等待执行
         */
        PENDING,
        
        /**
         * 正在执行
         */
        RUNNING,
        
        /**
         * 执行成功
         */
        SUCCESS,
        
        /**
         * 执行失败
         */
        FAILED,
        
        /**
         * 已取消
         */
        CANCELLED
    }
}