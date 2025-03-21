package com.insightdata.domain.metadata.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 外键信息
 * 包含数据库表外键的基本信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForeignKeyInfo {
    
    /**
     * 外键ID
     */
    private String id;
    
    /**
     * 表ID
     */
    private String tableId;
    
    /**
     * 外键名称
     */
    private String name;
    
    /**
     * 源表名
     */
    private String sourceTable;
    
    /**
     * 目标表名
     */
    private String targetTable;
    
    /**
     * 外键列信息列表
     */
    private List<ForeignKeyColumnInfo> sourceColumns = new ArrayList<>();
    
    /**
     * 更新规则
     */
    private String updateRule;
    
    /**
     * 删除规则
     */
    private String deleteRule;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 获取源列名列表
     */
    public List<String> getSourceColumnNames() {
        if (sourceColumns == null) {
            return new ArrayList<>();
        }
        return sourceColumns.stream()
                .map(ForeignKeyColumnInfo::getSourceColumnName)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取目标列名列表
     */
    public List<String> getTargetColumnNames() {
        if (sourceColumns == null) {
            return new ArrayList<>();
        }
        return sourceColumns.stream()
                .map(ForeignKeyColumnInfo::getTargetColumnName)
                .collect(Collectors.toList());
    }
    
    /**
     * 添加外键列信息
     */
    public void addColumn(ForeignKeyColumnInfo column) {
        if (sourceColumns == null) {
            sourceColumns = new ArrayList<>();
        }
        sourceColumns.add(column);
    }

    /**
     * 获取源列名字符串，逗号分隔
     */
    public String getSourceColumnNamesAsString() {
        return String.join(",", getSourceColumnNames());
    }

    /**
     * 获取目标列名字符串，逗号分隔
     */
    public String getTargetColumnNamesAsString() {
        return String.join(",", getTargetColumnNames());
    }
}