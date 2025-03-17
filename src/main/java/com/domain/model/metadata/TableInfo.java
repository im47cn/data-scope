package com.domain.model.metadata;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 表信息实体类
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TableInfo {

    /**
     * 表ID，使用UUID
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
     * 表类型（表、视图、物化视图等）
     */
    private String type;
    
    /**
     * 表描述
     */
    private String description;
    
    /**
     * 表注释
     */
    private String comment;
    
    /**
     * 列信息列表
     */
    private List<ColumnInfo> columns = new ArrayList<>();
    
    /**
     * 索引信息列表
     */
    private List<IndexInfo> indexes = new ArrayList<>();
    
    /**
     * 外键信息列表
     */
    private List<ForeignKeyInfo> foreignKeys = new ArrayList<>();
    
    /**
     * 行数量
     */
    private Long rowCount;
    
    /**
     * 数据大小（字节）
     */
    private Long dataSize;
    
    /**
     * 索引大小（字节）
     */
    private Long indexSize;
    
    /**
     * 最后分析时间
     */
    private LocalDateTime lastAnalyzed;
    
    /**
     * 是否表引擎支持事务
     */
    private Boolean transactional;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 创建者
     */
    private String createdBy;
    
    /**
     * 更新者
     */
    private String updatedBy;
    
    /**
     * 获取指定名称的列信息
     * 
     * @param columnName 列名
     * @return 列信息
     */
    public ColumnInfo getColumnByName(String columnName) {
        if (columns == null || columnName == null) {
            return null;
        }
        
        return columns.stream()
                .filter(col -> columnName.equalsIgnoreCase(col.getName()))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * 获取表的主键列
     * 
     * @return 主键列列表
     */
    public List<ColumnInfo> getPrimaryKeyColumns() {
        if (columns == null) {
            return new ArrayList<>();
        }
        
        return columns.stream()
                .filter(ColumnInfo::isPrimaryKey)
                .toList();
    }
    
    /**
     * 判断表是否包含指定列
     * 
     * @param columnName 列名
     * @return 是否包含
     */
    public boolean hasColumn(String columnName) {
        return getColumnByName(columnName) != null;
    }
}