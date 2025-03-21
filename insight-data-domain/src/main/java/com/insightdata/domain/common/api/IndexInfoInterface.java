package com.insightdata.domain.common.api;

/**
 * 索引信息接口
 * 定义索引信息的基本操作
 */
public interface IndexInfoInterface {
    /**
     * 获取索引名称
     */
    String getName();
    
    /**
     * 获取所属模式
     */
    String getSchema();
    
    /**
     * 获取所属表名
     */
    String getTableName();
    
    /**
     * 获取索引类型
     */
    String getType();
    
    /**
     * 获取索引列名列表
     */
    String[] getColumnNames();
    
    /**
     * 是否唯一索引
     */
    boolean isUnique();
    
    /**
     * 是否主键索引
     */
    boolean isPrimaryKey();
    
    /**
     * 是否聚集索引
     */
    boolean isClustered();
    
    /**
     * 获取过滤条件
     */
    String getFilterCondition();
    
    /**
     * 获取索引大小
     */
    Long getIndexSize();
    
    /**
     * 获取索引统计信息
     */
    IndexStatsInterface getStats();
}