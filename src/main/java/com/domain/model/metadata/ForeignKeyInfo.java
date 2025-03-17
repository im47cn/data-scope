package com.domain.model.metadata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 外键信息实体类
 * 表示数据库表之间的外键关系
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ForeignKeyInfo {
    
    /**
     * 数据源ID
     */
    private Long dataSourceId;
    
    /**
     * 外键名称
     */
    private String name;
    
    /**
     * 包含外键的表（源表）
     */
    private String sourceTableName;
    
    /**
     * 外键引用的表（目标表）
     */
    private String targetTableName;
    
    /**
     * 外键的更新规则
     * 例如：CASCADE, RESTRICT, SET NULL, NO ACTION
     */
    private String updateRule;
    
    /**
     * 外键的删除规则
     * 例如：CASCADE, RESTRICT, SET NULL, NO ACTION
     */
    private String deleteRule;
    
    /**
     * 延迟规则
     */
    private String deferrability;
    
    /**
     * 外键列映射
     */
    @Builder.Default
    private List<ForeignKeyColumnInfo> columns = new ArrayList<>();
    
    /**
     * 外键是否启用
     */
    private boolean enabled;
    
    /**
     * 外键是否验证
     */
    private boolean validated;
    
    /**
     * 外键描述/注释
     */
    private String description;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 添加外键列映射
     */
    public void addColumn(ForeignKeyColumnInfo columnInfo) {
        if (columns == null) {
            columns = new ArrayList<>();
        }
        columns.add(columnInfo);
    }
    
    /**
     * 获取源表列名列表
     */
    public List<String> getSourceColumnNames() {
        if (columns == null) {
            return new ArrayList<>();
        }
        return columns.stream()
                .map(ForeignKeyColumnInfo::getSourceColumnName)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取目标表列名列表
     */
    public List<String> getTargetColumnNames() {
        if (columns == null) {
            return new ArrayList<>();
        }
        return columns.stream()
                .map(ForeignKeyColumnInfo::getTargetColumnName)
                .collect(Collectors.toList());
    }
    
    /**
     * 检查外键是否为复合外键（由多列组成）
     */
    public boolean isComposite() {
        return columns != null && columns.size() > 1;
    }
    
    /**
     * 检查外键是否为自引用（引用自己的表）
     */
    public boolean isSelfReferencing() {
        return sourceTableName != null && 
               sourceTableName.equals(targetTableName);
    }
    
    /**
     * 获取外键映射中的第一列（简单外键场景）
     */
    public ForeignKeyColumnInfo getFirstColumn() {
        if (columns == null || columns.isEmpty()) {
            return null;
        }
        return columns.get(0);
    }
    
    /**
     * 检查更新规则是否为级联
     */
    public boolean isUpdateCascade() {
        return "CASCADE".equalsIgnoreCase(updateRule);
    }
    
    /**
     * 检查删除规则是否为级联
     */
    public boolean isDeleteCascade() {
        return "CASCADE".equalsIgnoreCase(deleteRule);
    }
    
    /**
     * 获取外键的字符串表示形式
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FOREIGN KEY (");
        sb.append(String.join(", ", getSourceColumnNames()));
        sb.append(") REFERENCES ");
        sb.append(targetTableName);
        sb.append(" (");
        sb.append(String.join(", ", getTargetColumnNames()));
        sb.append(")");
        
        if (updateRule != null && !updateRule.isEmpty()) {
            sb.append(" ON UPDATE ").append(updateRule);
        }
        
        if (deleteRule != null && !deleteRule.isEmpty()) {
            sb.append(" ON DELETE ").append(deleteRule);
        }
        
        return sb.toString();
    }

}