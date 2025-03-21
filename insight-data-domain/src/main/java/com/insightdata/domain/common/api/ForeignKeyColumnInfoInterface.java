package com.insightdata.domain.common.api;

/**
 * 外键列信息接口
 * 定义外键列信息的基本操作
 */
public interface ForeignKeyColumnInfoInterface {
    /**
     * 获取序号
     */
    Integer getOrdinalPosition();
    
    /**
     * 获取源列名
     */
    String getSourceColumnName();
    
    /**
     * 获取目标列名
     */
    String getTargetColumnName();
}