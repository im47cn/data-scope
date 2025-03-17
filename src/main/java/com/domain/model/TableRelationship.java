package com.domain.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 表关系实体类
 * 用于存储两个表之间的关系信息
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TableRelationship {
    
    /**
     * 关系类型枚举
     */
    public enum RelationshipType {
        /** 一对一关系 */
        ONE_TO_ONE("一对一"),
        
        /** 一对多关系 */
        ONE_TO_MANY("一对多"),
        
        /** 多对一关系 */
        MANY_TO_ONE("多对一"),
        
        /** 多对多关系 */
        MANY_TO_MANY("多对多"),
        
        /** 未知关系 */
        UNKNOWN("未知");
        
        private final String displayName;
        
        RelationshipType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * 关系来源枚举
     */
    public enum RelationshipSource {
        /** 从元数据推断的关系 */
        METADATA("元数据"),
        
        /** 从学习算法推断的关系 */
        LEARNING("机器学习"),
        
        /** 用户反馈的关系 */
        USER_FEEDBACK("用户反馈"),
        
        /** 从查询历史推断的关系 */
        QUERY_HISTORY("查询历史"),
        
        /** 系统默认的关系 */
        SYSTEM("系统默认");
        
        private final String displayName;
        
        RelationshipSource(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * 关系ID，使用UUID
     */
    private String id;
    
    /**
     * 数据源ID
     */
    private String dataSourceId;
    
    /**
     * 源表名
     */
    private String sourceTableName;
    
    /**
     * 源列名（可以是多个列的组合）
     */
    private String sourceColumnNames;
    
    /**
     * 目标表名
     */
    private String targetTableName;
    
    /**
     * 目标列名（可以是多个列的组合）
     */
    private String targetColumnNames;
    
    /**
     * 关系类型
     */
    private RelationshipType relationType;
    
    /**
     * 关系来源
     */
    private RelationshipSource relationSource;
    
    /**
     * 置信度，表示该关系的可信程度
     * 取值范围：0.0 ~ 1.0
     */
    private Double confidence;
    
    /**
     * 使用频率，表示该关系被使用的次数
     */
    private Integer frequency;
    
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
     * 描述
     */
    private String description;
    
    /**
     * 获取反向关系类型
     * 
     * @return 反向关系类型
     */
    public RelationshipType getInverseRelationType() {
        switch (relationType) {
            case ONE_TO_ONE:
                return RelationshipType.ONE_TO_ONE;
            case ONE_TO_MANY:
                return RelationshipType.MANY_TO_ONE;
            case MANY_TO_ONE:
                return RelationshipType.ONE_TO_MANY;
            case MANY_TO_MANY:
                return RelationshipType.MANY_TO_MANY;
            default:
                return RelationshipType.UNKNOWN;
        }
    }
    
    /**
     * 创建反向关系实例
     * 
     * @return 反向关系实例
     */
    public TableRelationship createInverseRelationship() {
        return TableRelationship.builder()
                .dataSourceId(this.dataSourceId)
                .sourceTableName(this.targetTableName)
                .sourceColumnNames(this.targetColumnNames)
                .targetTableName(this.sourceTableName)
                .targetColumnNames(this.sourceColumnNames)
                .relationType(getInverseRelationType())
                .relationSource(this.relationSource)
                .confidence(this.confidence)
                .frequency(this.frequency)
                .build();
    }
    
    /**
     * 获取关系的描述信息
     * 
     * @return 关系描述
     */
    public String getRelationshipDescription() {
        StringBuilder description = new StringBuilder();
        description.append(sourceTableName)
                .append(" (").append(sourceColumnNames).append(") ")
                .append(relationType.getDisplayName())
                .append(" ")
                .append(targetTableName)
                .append(" (").append(targetColumnNames).append(")");
                
        return description.toString();
    }
    
    /**
     * 检查是否与另一个关系类似
     * 
     * @param other 另一个关系
     * @return 是否类似
     */
    public boolean isSimilarTo(TableRelationship other) {
        if (other == null) {
            return false;
        }
        
        return this.sourceTableName.equalsIgnoreCase(other.sourceTableName) &&
               this.targetTableName.equalsIgnoreCase(other.targetTableName) &&
               this.sourceColumnNames.equalsIgnoreCase(other.sourceColumnNames) &&
               this.targetColumnNames.equalsIgnoreCase(other.targetColumnNames);
    }
}