package com.insightdata.facade.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 外键响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForeignKeyResponse {
    
    /**
     * ID
     */
    private Long id;
    
    /**
     * 表ID
     */
    private Long tableId;
    
    /**
     * 外键名称
     */
    private String name;
    
    /**
     * 源表名
     */
    private String sourceTableName;
    
    /**
     * 目标表名
     */
    private String targetTableName;
    
    /**
     * 外键列列表
     */
    @Builder.Default
    private List<ForeignKeyColumnResponse> columns = new ArrayList<>();
    
    /**
     * 更新规则
     */
    private String updateRule;
    
    /**
     * 删除规则
     */
    private String deleteRule;
    
    /**
     * 是否启用
     */
    private boolean enabled;
    
    /**
     * 是否延迟验证
     */
    private boolean deferrable;
    
    /**
     * 是否初始延迟
     */
    private boolean initiallyDeferred;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 添加外键列
     */
    public ForeignKeyResponse addColumn(ForeignKeyColumnResponse column) {
        columns.add(column);
        return this;
    }
    
    /**
     * 获取源列名列表
     */
    public List<String> getSourceColumnNames() {
        List<String> names = new ArrayList<>();
        for (ForeignKeyColumnResponse column : columns) {
            names.add(column.getSourceColumnName());
        }
        return names;
    }
    
    /**
     * 获取目标列名列表
     */
    public List<String> getTargetColumnNames() {
        List<String> names = new ArrayList<>();
        for (ForeignKeyColumnResponse column : columns) {
            names.add(column.getTargetColumnName());
        }
        return names;
    }
    
    /**
     * 获取完整的外键定义
     */
    public String getForeignKeyDefinition() {
        StringBuilder sb = new StringBuilder();
        
        // 添加外键名称
        sb.append("CONSTRAINT ").append(name).append(" FOREIGN KEY ");
        
        // 添加源列
        sb.append("(");
        List<String> sourceColumns = getSourceColumnNames();
        sb.append(String.join(", ", sourceColumns));
        sb.append(") ");
        
        // 添加目标表和列
        sb.append("REFERENCES ").append(targetTableName).append(" (");
        List<String> targetColumns = getTargetColumnNames();
        sb.append(String.join(", ", targetColumns));
        sb.append(") ");
        
        // 添加更新规则
        if (updateRule != null && !updateRule.isEmpty()) {
            sb.append("ON UPDATE ").append(updateRule).append(" ");
        }
        
        // 添加删除规则
        if (deleteRule != null && !deleteRule.isEmpty()) {
            sb.append("ON DELETE ").append(deleteRule).append(" ");
        }
        
        // 添加延迟属性
        if (deferrable) {
            sb.append("DEFERRABLE ");
            if (initiallyDeferred) {
                sb.append("INITIALLY DEFERRED ");
            } else {
                sb.append("INITIALLY IMMEDIATE ");
            }
        }
        
        return sb.toString().trim();
    }
}