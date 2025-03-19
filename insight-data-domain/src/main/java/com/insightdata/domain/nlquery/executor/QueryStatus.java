package com.insightdata.domain.nlquery.executor;

/**
 * 查询状态枚举
 * 
 * 表示查询执行的不同状态
 */
public enum QueryStatus {
    /**
     * 查询正在运行
     */
    RUNNING,
    
    /**
     * 查询已完成
     */
    COMPLETED,
    
    /**
     * 查询已取消
     */
    CANCELLED,
    
    /**
     * 查询执行失败
     */
    FAILED,
    
    /**
     * 查询状态未知
     */
    UNKNOWN
}
