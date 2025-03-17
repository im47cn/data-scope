package com.domain.model.metadata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 表信息实体类
 * 表示数据库中的表及其相关元数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
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
     * 表名称
     */
    private String name;
    
    /**
     * 表类型
     * 例如: TABLE, VIEW, SYSTEM TABLE, GLOBAL TEMPORARY, LOCAL TEMPORARY, ALIAS, SYNONYM等
     */
    private String type;
    
    /**
     * 表的备注/描述
     */
    private String description;
    
    /**
     * 表的列信息
     */
    @Builder.Default
    private List<ColumnInfo> columns = new ArrayList<>();
    
    /**
     * 表的索引信息
     */
    @Builder.Default
    private List<IndexInfo> indexes = new ArrayList<>();
    
    /**
     * 表的外键信息
     */
    @Builder.Default
    private List<ForeignKeyInfo> foreignKeys = new ArrayList<>();
    
    /**
     * 表的主键信息
     */
    private IndexInfo primaryKey;
    
    /**
     * 表中的记录数
     */
    private Long rowCount;

    private Long dataSize;

    private Long indexSize;

    /**
     * 表的存储引擎
     */
    private String engine;
    
    /**
     * 表的默认字符集
     */
    private String characterSet;
    
    /**
     * 表的默认排序规则
     */
    private String collation;
    
    /**
     * 表的创建日期
     */
    private LocalDateTime tableCreatedAt;
    
    /**
     * 表的最后更新日期
     */
    private LocalDateTime tableUpdatedAt;
    
    /**
     * 记录的创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 记录的更新时间
     */
    private LocalDateTime updatedAt;

    private String remarks;

    /**
     * 添加表列
     */
    public void addColumn(ColumnInfo column) {
        if (columns == null) {
            columns = new ArrayList<>();
        }
        columns.add(column);
    }
    
    /**
     * 添加表索引
     */
    public void addIndex(IndexInfo index) {
        if (indexes == null) {
            indexes = new ArrayList<>();
        }
        indexes.add(index);
        
        // 如果是主键索引，设置为主键
        if (index.isPrimaryKey()) {
            this.primaryKey = index;
        }
    }
    
    /**
     * 添加表外键
     */
    public void addForeignKey(ForeignKeyInfo foreignKey) {
        if (foreignKeys == null) {
            foreignKeys = new ArrayList<>();
        }
        foreignKeys.add(foreignKey);
    }
    
    /**
     * 根据列名查找列
     */
    public Optional<ColumnInfo> findColumnByName(String columnName) {
        if (columns == null) {
            return Optional.empty();
        }
        return columns.stream()
                .filter(column -> column.getName().equalsIgnoreCase(columnName))
                .findFirst();
    }
    
    /**
     * 根据索引名查找索引
     */
    public Optional<IndexInfo> findIndexByName(String indexName) {
        if (indexes == null) {
            return Optional.empty();
        }
        return indexes.stream()
                .filter(index -> index.getName().equalsIgnoreCase(indexName))
                .findFirst();
    }
    
    /**
     * 根据外键名查找外键
     */
    public Optional<ForeignKeyInfo> findForeignKeyByName(String foreignKeyName) {
        if (foreignKeys == null) {
            return Optional.empty();
        }
        return foreignKeys.stream()
                .filter(fk -> fk.getName().equalsIgnoreCase(foreignKeyName))
                .findFirst();
    }
    
    /**
     * 获取所有列名
     */
    public List<String> getColumnNames() {
        if (columns == null) {
            return new ArrayList<>();
        }
        return columns.stream()
                .map(ColumnInfo::getName)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取主键列
     */
    public List<ColumnInfo> getPrimaryKeyColumns() {
        if (columns == null) {
            return new ArrayList<>();
        }
        return columns.stream()
                .filter(ColumnInfo::isPrimaryKey)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取外键列
     */
    public List<ColumnInfo> getForeignKeyColumns() {
        if (columns == null) {
            return new ArrayList<>();
        }
        return columns.stream()
                .filter(ColumnInfo::isForeignKey)
                .collect(Collectors.toList());
    }
    
    /**
     * 表是否有主键
     */
    public boolean hasPrimaryKey() {
        return primaryKey != null || (columns != null && columns.stream().anyMatch(ColumnInfo::isPrimaryKey));
    }
    
    /**
     * 表是否有外键
     */
    public boolean hasForeignKeys() {
        return foreignKeys != null && !foreignKeys.isEmpty();
    }
    
    /**
     * 获取表的完全限定名
     * 例如：schema_name.table_name
     */
    public String getQualifiedName() {
        if (schemaName != null && !schemaName.isEmpty()) {
            return schemaName + "." + name;
        }
        return name;
    }
    
    /**
     * 检查表是否包含指定列
     */
    public boolean containsColumn(String columnName) {
        return findColumnByName(columnName).isPresent();
    }
    
    /**
     * 获取表的显示信息（带备注）
     */
    public String getDisplayInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        
        if (description != null && !description.isEmpty()) {
            sb.append(" (").append(description).append(")");
        }
        
        return sb.toString();
    }
}