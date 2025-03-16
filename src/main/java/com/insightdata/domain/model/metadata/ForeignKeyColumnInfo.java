package com.insightdata.domain.model.metadata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 外键列信息模型
 * 用于表示外键中列的映射关系
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForeignKeyColumnInfo {
    
    /**
     * 所属外键名称
     */
    private String foreignKeyName;
    
    /**
     * 源表名（外键所在的表）
     */
    private String sourceTableName;
    
    /**
     * 目标表名（外键引用的表）
     */
    private String targetTableName;
    
    /**
     * 源列名（外键所在表的列）
     */
    private String sourceColumnName;
    
    /**
     * 目标列名（外键引用表的列）
     */
    private String targetColumnName;
    
    /**
     * 序号位置（在外键中的位置）
     */
    private Integer ordinalPosition;
    
    /**
     * 所属模式（Schema）名称
     */
    private String schemaName;
    
    /**
     * 目标表的模式（Schema）名称
     */
    private String targetSchemaName;
    
    /**
     * 所属数据源ID
     */
    private Long dataSourceId;
    
    /**
     * 判断是否是复合外键的一部分
     * 
     * @return 如果是复合外键的一部分返回true，否则返回false
     */
    public boolean isPartOfCompositeKey() {
        // 如果序号位置大于1，则说明是复合外键的一部分
        return ordinalPosition != null && ordinalPosition > 1;
    }
    
    /**
     * 获取完整的源列标识
     * 
     * @return 格式为"schema.table.column"的完整标识
     */
    public String getFullSourceColumnIdentifier() {
        StringBuilder sb = new StringBuilder();
        if (schemaName != null && !schemaName.isEmpty()) {
            sb.append(schemaName).append(".");
        }
        if (sourceTableName != null) {
            sb.append(sourceTableName).append(".");
        }
        if (sourceColumnName != null) {
            sb.append(sourceColumnName);
        }
        return sb.toString();
    }
    
    /**
     * 获取完整的目标列标识
     * 
     * @return 格式为"schema.table.column"的完整标识
     */
    public String getFullTargetColumnIdentifier() {
        StringBuilder sb = new StringBuilder();
        if (targetSchemaName != null && !targetSchemaName.isEmpty()) {
            sb.append(targetSchemaName).append(".");
        }
        if (targetTableName != null) {
            sb.append(targetTableName).append(".");
        }
        if (targetColumnName != null) {
            sb.append(targetColumnName);
        }
        return sb.toString();
    }
}