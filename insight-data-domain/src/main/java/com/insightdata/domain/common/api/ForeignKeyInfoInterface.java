package com.insightdata.domain.common.api;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 外键信息接口
 * 定义外键信息的基本操作
 */
public interface ForeignKeyInfoInterface {
    /**
     * 获取外键ID
     */
    String getId();
    
    /**
     * 获取表ID
     */
    String getTableId();
    
    /**
     * 获取外键名称
     */
    String getName();
    
    /**
     * 获取源表名
     */
    String getSourceTableName();
    
    /**
     * 获取目标表名
     */
    String getTargetTableName();
    
    /**
     * 获取外键列信息列表
     */
    List<ForeignKeyColumnInfoInterface> getColumns();
    
    /**
     * 获取更新规则
     */
    String getUpdateRule();
    
    /**
     * 获取删除规则
     */
    String getDeleteRule();
    
    /**
     * 获取创建时间
     */
    LocalDateTime getCreatedAt();
    
    /**
     * 获取更新时间
     */
    LocalDateTime getUpdatedAt();
    
    /**
     * 获取源列名列表
     */
    List<String> getSourceColumnNames();
    
    /**
     * 获取目标列名列表
     */
    List<String> getTargetColumnNames();
    
    /**
     * 添加外键列信息
     */
    void addColumn(ForeignKeyColumnInfoInterface column);
}