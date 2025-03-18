package com.insightdata.domain.metadata.model;

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
 * 视图信息实体类
 * 表示数据库中的视图及其相关元数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ViewInfo {

    /**
     * 数据源ID
     */
    private Long dataSourceId;
    
    /**
     * 模式名称
     */
    private String schemaName;
    
    /**
     * 视图名称
     */
    private String name;
    
    /**
     * 视图的备注/描述
     */
    private String description;
    
    /**
     * 视图的定义SQL
     */
    private String definition;
    
    /**
     * 视图的列信息
     */
    @Builder.Default
    private List<ColumnInfo> columns = new ArrayList<>();
    
    /**
     * 视图类型
     * 例如：VIEW, MATERIALIZED VIEW等
     */
    private String type;
    
    /**
     * 视图是否可更新
     */
    private boolean updatable;
    
    /**
     * 视图是否插入检查选项
     */
    private boolean checkOption;
    
    /**
     * 视图是否只读
     */
    private boolean readOnly;
    
    /**
     * 视图是否固定（不依赖于底层数据）
     */
    private boolean fixed;
    
    /**
     * 视图基础表
     */
    @Builder.Default
    private List<String> baseTables = new ArrayList<>();
    
    /**
     * 视图的创建日期
     */
    private LocalDateTime viewCreatedAt;
    
    /**
     * 视图的最后更新日期
     */
    private LocalDateTime viewUpdatedAt;
    
    /**
     * 记录的创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 记录的更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 添加视图列
     */
    public void addColumn(ColumnInfo column) {
        if (columns == null) {
            columns = new ArrayList<>();
        }
        columns.add(column);
    }
    
    /**
     * 添加基础表
     */
    public void addBaseTable(String tableNane) {
        if (baseTables == null) {
            baseTables = new ArrayList<>();
        }
        baseTables.add(tableNane);
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
     * 获取所有日期类型列
     */
    public List<ColumnInfo> getDateColumns() {
        if (columns == null) {
            return new ArrayList<>();
        }
        return columns.stream()
                .filter(ColumnInfo::isDateTime)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取所有数值类型列
     */
    public List<ColumnInfo> getNumericColumns() {
        if (columns == null) {
            return new ArrayList<>();
        }
        return columns.stream()
                .filter(ColumnInfo::isNumeric)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取所有字符串类型列
     */
    public List<ColumnInfo> getStringColumns() {
        if (columns == null) {
            return new ArrayList<>();
        }
        return columns.stream()
                .filter(ColumnInfo::isString)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取视图的完全限定名
     * 例如：schema_name.view_name
     */
    public String getQualifiedName() {
        if (schemaName != null && !schemaName.isEmpty()) {
            return schemaName + "." + name;
        }
        return name;
    }
    
    /**
     * 检查视图是否包含指定列
     */
    public boolean containsColumn(String columnName) {
        return findColumnByName(columnName).isPresent();
    }
    
    /**
     * 检查视图是否为物化视图
     */
    public boolean isMaterialized() {
        return type != null && type.toLowerCase().contains("materialized");
    }
    
    /**
     * 获取视图的显示信息（带备注）
     */
    public String getDisplayInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        
        if (description != null && !description.isEmpty()) {
            sb.append(" (").append(description).append(")");
        }
        
        return sb.toString();
    }
    
    /**
     * 生成视图的创建语句
     */
    public String generateCreateViewStatement() {
        StringBuilder sb = new StringBuilder();
        
        if (isMaterialized()) {
            sb.append("CREATE MATERIALIZED VIEW ");
        } else {
            sb.append("CREATE OR REPLACE VIEW ");
        }
        
        sb.append(getQualifiedName());
        
        if (columns != null && !columns.isEmpty()) {
            sb.append(" (");
            sb.append(columns.stream()
                    .map(ColumnInfo::getName)
                    .collect(Collectors.joining(", ")));
            sb.append(")");
        }
        
        sb.append(" AS\n");
        sb.append(definition);
        
        if (checkOption) {
            sb.append("\nWITH CHECK OPTION");
        }
        
        return sb.toString();
    }
}