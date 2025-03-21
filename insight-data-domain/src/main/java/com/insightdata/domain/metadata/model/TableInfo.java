package com.insightdata.domain.metadata.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 表信息
 * 包含数据库表的基本信息和列信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableInfo {
    
    /**
     * 表ID
     */
    private String id;
    
    /**
     * 数据源ID
     */
    private String dataSourceId;
    
    /**
     * 模式名称
     */
    private String schemaName;
    
    /**
     * 表名
     */
    private String name;
    
    /**
     * 表类型（TABLE, VIEW, SYSTEM_TABLE等）
     */
    private String type;
    
    /**
     * 表描述
     */
    private String comment;
    
    /**
     * 表的行数（估计值）
     */
    private Long rowCount;
    
    /**
     * 表的大小（字节）
     */
    private Long dataSize;
    
    /**
     * 列信息列表
     */
    private List<ColumnInfo> columns = new ArrayList<>();
    
    /**
     * 外键信息列表
     */
    private List<ForeignKeyInfo> foreignKeys = new ArrayList<>();
    
    /**
     * 索引信息列表
     */
    private List<IndexInfo> indexes = new ArrayList<>();
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 添加列信息
     */
    public void addColumn(ColumnInfo column) {
        if (columns == null) {
            columns = new ArrayList<>();
        }
        columns.add(column);
    }
    
    /**
     * 添加外键信息
     */
    public void addForeignKey(ForeignKeyInfo foreignKey) {
        if (foreignKeys == null) {
            foreignKeys = new ArrayList<>();
        }
        foreignKeys.add(foreignKey);
    }
    
    /**
     * 添加索引信息
     */
    public void addIndex(IndexInfo index) {
        if (indexes == null) {
            indexes = new ArrayList<>();
        }
        indexes.add(index);
    }
    
    /**
     * 根据列名查找列信息
     */
    public ColumnInfo findColumnByName(String columnName) {
        if (columns == null || columnName == null) {
            return null;
        }
        
        return columns.stream()
                .filter(column -> columnName.equals(column.getName()))
                .findFirst()
                .orElse(null);
    }
}