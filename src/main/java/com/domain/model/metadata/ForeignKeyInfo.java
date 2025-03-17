package com.domain.model.metadata;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 外键信息实体类
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ForeignKeyInfo {
    
    /**
     * 外键ID，使用UUID
     */
    private String id;
    
    /**
     * 数据源ID
     */
    private String dataSourceId;
    
    /**
     * 外键名称
     */
    private String name;
    
    /**
     * 模式名
     */
    private String schemaName;
    
    /**
     * 表名
     */
    private String tableName;
    
    /**
     * 目标模式名
     */
    private String targetSchemaName;
    
    /**
     * 目标表名
     */
    private String targetTableName;
    
    /**
     * 更新规则
     * CASCADE, RESTRICT, NO ACTION, SET NULL, SET DEFAULT
     */
    private String updateRule;
    
    /**
     * 删除规则
     * CASCADE, RESTRICT, NO ACTION, SET NULL, SET DEFAULT
     */
    private String deleteRule;
    
    /**
     * 延迟性
     * INITIALLY_DEFERRED, INITIALLY_IMMEDIATE, NOT_DEFERRABLE
     */
    private String deferrability;
    
    /**
     * 外键列列表
     */
    private List<ForeignKeyColumnInfo> columns = new ArrayList<>();
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 系统生成的
     */
    private boolean systemGenerated;
    
    /**
     * 是否启用
     */
    private boolean enabled = true;
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 注释
     */
    private String remarks;
    
    /**
     * 获取外键列数量
     * 
     * @return 外键列数量
     */
    public int getColumnCount() {
        return columns.size();
    }
    
    /**
     * 获取源列名列表
     * 
     * @return 源列名列表
     */
    public List<String> getSourceColumns() {
        return columns.stream()
                .map(ForeignKeyColumnInfo::getSourceColumnName)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取目标列名列表
     * 
     * @return 目标列名列表
     */
    public List<String> getTargetColumns() {
        return columns.stream()
                .map(ForeignKeyColumnInfo::getTargetColumnName)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取外键描述信息
     * 
     * @return 外键描述信息
     */
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(tableName)
                .append(" (")
                .append(String.join(", ", getSourceColumns()))
                .append(") 引用 ")
                .append(targetTableName)
                .append(" (")
                .append(String.join(", ", getTargetColumns()))
                .append(")");
                
        if (updateRule != null && !updateRule.isEmpty()) {
            sb.append(", 更新:")
              .append(updateRule);
        }
        
        if (deleteRule != null && !deleteRule.isEmpty()) {
            sb.append(", 删除:")
              .append(deleteRule);
        }
        
        return sb.toString();
    }
    
    /**
     * 判断是否指定列是否在外键列中
     * 
     * @param columnName 列名
     * @return 是否在外键列中
     */
    public boolean containsColumn(String columnName) {
        return columns.stream()
                .anyMatch(col -> col.getSourceColumnName().equalsIgnoreCase(columnName));
    }
    
    /**
     * 根据位置获取外键列
     * 
     * @param ordinalPosition 位置（从1开始）
     * @return 外键列
     */
    public ForeignKeyColumnInfo getColumnByPosition(int ordinalPosition) {
        return columns.stream()
                .filter(col -> col.getOrdinalPosition() == ordinalPosition)
                .findFirst()
                .orElse(null);
    }
    
    /**
     * 添加外键列
     * 
     * @param columnInfo 外键列信息
     */
    public void addColumn(ForeignKeyColumnInfo columnInfo) {
        columns.add(columnInfo);
    }
    
    /**
     * 设置外键列列表
     * 
     * @param columns 外键列列表
     */
    public void setColumns(List<ForeignKeyColumnInfo> columns) {
        this.columns = columns != null ? columns : new ArrayList<>();
    }
}