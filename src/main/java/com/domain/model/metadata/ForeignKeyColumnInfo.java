package com.domain.model.metadata;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 外键列信息实体类
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ForeignKeyColumnInfo {
    
    /**
     * 外键列ID，使用UUID
     */
    private String id;
    
    /**
     * 外键ID
     */
    private String foreignKeyId;
    
    /**
     * 序号（从1开始）
     */
    private int ordinalPosition;
    
    /**
     * 源表名
     */
    private String sourceTableName;
    
    /**
     * 源列名
     */
    private String sourceColumnName;
    
    /**
     * 目标表名
     */
    private String targetTableName;
    
    /**
     * 目标列名
     */
    private String targetColumnName;
    
    /**
     * 列序号
     */
    private Integer keySeq;
    
    /**
     * 更新规则
     */
    private String updateRule;
    
    /**
     * 删除规则
     */
    private String deleteRule;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 是否唯一
     */
    private boolean unique;
    
    /**
     * 是否可为空
     */
    private boolean nullable;
    
    /**
     * 是否自增
     */
    private boolean autoIncrement;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 是否系统生成
     */
    private boolean systemGenerated;
    
    /**
     * 数据类型
     */
    private String dataType;
    
    /**
     * 获取完整的列描述信息
     * 
     * @return 列描述信息
     */
    public String getColumnDescription() {
        return sourceTableName + "." + sourceColumnName + " -> " + 
               targetTableName + "." + targetColumnName;
    }
    
    /**
     * 获取列的短描述
     * 
     * @return 短描述
     */
    public String getShortDescription() {
        return sourceColumnName + " -> " + targetColumnName;
    }
    
    /**
     * 创建新的外键列信息实例
     * 
     * @param foreignKeyId 外键ID
     * @param ordinalPosition 序号
     * @param sourceTableName 源表名
     * @param sourceColumnName 源列名
     * @param targetTableName 目标表名
     * @param targetColumnName 目标列名
     * @return 外键列信息实例
     */
    public static ForeignKeyColumnInfo create(
            String foreignKeyId, 
            int ordinalPosition,
            String sourceTableName, 
            String sourceColumnName,
            String targetTableName, 
            String targetColumnName) {
        LocalDateTime now = LocalDateTime.now();
        ForeignKeyColumnInfo info = new ForeignKeyColumnInfo();
        info.setId(UUID.randomUUID().toString());
        info.setForeignKeyId(foreignKeyId);
        info.setOrdinalPosition(ordinalPosition);
        info.setSourceTableName(sourceTableName);
        info.setSourceColumnName(sourceColumnName);
        info.setTargetTableName(targetTableName);
        info.setTargetColumnName(targetColumnName);
        info.setCreatedAt(now);
        info.setUpdatedAt(now);
        info.setSystemGenerated(false);
        return info;
    }
    
    /**
     * 克隆当前实例但使用新的外键ID
     * 
     * @param newForeignKeyId 新的外键ID
     * @return 克隆后的实例
     */
    public ForeignKeyColumnInfo cloneWithNewForeignKeyId(String newForeignKeyId) {
        ForeignKeyColumnInfo clone = new ForeignKeyColumnInfo();
        clone.setId(UUID.randomUUID().toString());
        clone.setForeignKeyId(newForeignKeyId);
        clone.setOrdinalPosition(this.ordinalPosition);
        clone.setSourceTableName(this.sourceTableName);
        clone.setSourceColumnName(this.sourceColumnName);
        clone.setTargetTableName(this.targetTableName);
        clone.setTargetColumnName(this.targetColumnName);
        clone.setKeySeq(this.keySeq);
        clone.setUpdateRule(this.updateRule);
        clone.setDeleteRule(this.deleteRule);
        clone.setDescription(this.description);
        clone.setUnique(this.unique);
        clone.setNullable(this.nullable);
        clone.setAutoIncrement(this.autoIncrement);
        clone.setDataType(this.dataType);
        clone.setCreatedAt(LocalDateTime.now());
        clone.setUpdatedAt(LocalDateTime.now());
        clone.setSystemGenerated(this.systemGenerated);
        return clone;
    }
    
    /**
     * 检查是否与另一个外键列匹配（源表、源列、目标表、目标列）
     * 
     * @param other 另一个外键列
     * @return 是否匹配
     */
    public boolean matches(ForeignKeyColumnInfo other) {
        if (other == null) {
            return false;
        }
        return this.sourceTableName.equalsIgnoreCase(other.sourceTableName) &&
               this.sourceColumnName.equalsIgnoreCase(other.sourceColumnName) &&
               this.targetTableName.equalsIgnoreCase(other.targetTableName) &&
               this.targetColumnName.equalsIgnoreCase(other.targetColumnName);
    }
}