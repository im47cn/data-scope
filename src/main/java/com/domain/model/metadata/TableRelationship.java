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
import java.util.stream.Stream;

/**
 * 表关系实体类
 * 表示数据库表之间的关系（可能是外键关系，也可能是推断出的关系）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TableRelationship {

    /**
     * 关系ID
     */
    private String id;
    
    /**
     * 数据源ID
     */
    private String dataSourceId;
    
    /**
     * 源模式名
     */
    private String sourceSchemaName;
    
    /**
     * 源表名
     */
    private String sourceTableName;
    
    /**
     * 源列名（可以是多个列，用逗号分隔）
     */
    private String sourceColumnNames;
    
    /**
     * 目标模式名
     */
    private String targetSchemaName;
    
    /**
     * 目标表名
     */
    private String targetTableName;
    
    /**
     * 目标列名（可以是多个列，用逗号分隔）
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
     * 关系强度/置信度（0.0-1.0）
     * 用于表示自动推断关系的可信度
     */
    private double confidence;

    /**
     * 使用频率
     * 表示该关系在查询中被使用的次数
     */
    private int frequency;

    /**
     * 关系是否已验证
     */
    private boolean validated;
    
    /**
     * 关系是否已启用
     */
    private boolean enabled;
    
    /**
     * 关系描述/备注
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
     * 获取源表的完全限定名
     */
    public String getSourceFullName() {
        if (sourceSchemaName != null && !sourceSchemaName.isEmpty()) {
            return sourceSchemaName + "." + sourceTableName;
        }
        return sourceTableName;
    }
    
    /**
     * 获取目标表的完全限定名
     */
    public String getTargetFullName() {
        if (targetSchemaName != null && !targetSchemaName.isEmpty()) {
            return targetSchemaName + "." + targetTableName;
        }
        return targetTableName;
    }
    
    /**
     * 获取源列名列表
     */
    public List<String> getSourceColumnList() {
        if (sourceColumnNames == null || sourceColumnNames.isEmpty()) {
            return new ArrayList<>();
        }
        
        return splitColumnNames(sourceColumnNames);
    }
    
    /**
     * 获取目标列名列表
     */
    public List<String> getTargetColumnList() {
        if (targetColumnNames == null || targetColumnNames.isEmpty()) {
            return new ArrayList<>();
        }
        
        return splitColumnNames(targetColumnNames);
    }
    
    /**
     * 设置源列名列表
     */
    public TableRelationship setSourceColumnList(List<String> columnList) {
        this.sourceColumnNames = String.join(",", columnList);
        return this;
    }
    
    /**
     * 设置目标列名列表
     */
    public TableRelationship setTargetColumnList(List<String> columnList) {
        this.targetColumnNames = String.join(",", columnList);
        return this;
    }
    
    /**
     * 判断是否为外键关系
     */
    public boolean isForeignKeyRelationship() {
        return "FOREIGN_KEY".equals(relationSource);
    }
    
    /**
     * 判断是否为推断关系
     */
    public boolean isInferredRelationship() {
        return "INFERENCE".equals(relationSource);
    }
    
    /**
     * 判断是否为手动添加的关系
     */
    public boolean isManualRelationship() {
        return "MANUAL".equals(relationSource);
    }
    
    /**
     * 判断是否为一对一关系
     */
    public boolean isOneToOne() {
        return "ONE_TO_ONE".equals(relationType);
    }
    
    /**
     * 判断是否为一对多关系
     */
    public boolean isOneToMany() {
        return "ONE_TO_MANY".equals(relationType);
    }
    
    /**
     * 判断是否为多对一关系
     */
    public boolean isManyToOne() {
        return "MANY_TO_ONE".equals(relationType);
    }
    
    /**
     * 判断是否为多对多关系
     */
    public boolean isManyToMany() {
        return "MANY_TO_MANY".equals(relationType);
    }
    
    /**
     * 检查此关系是否涉及给定的表
     */
    public boolean involvesTable(String tableName) {
        return sourceTableName.equalsIgnoreCase(tableName) || 
               targetTableName.equalsIgnoreCase(tableName);
    }
    
    /**
     * 获取与给定表相关的另一个表
     */
    public String getRelatedTable(String tableName) {
        if (sourceTableName.equalsIgnoreCase(tableName)) {
            return targetTableName;
        } else if (targetTableName.equalsIgnoreCase(tableName)) {
            return sourceTableName;
        }
        return null;
    }
    
    /**
     * 验证关系（将置信度设为100，标记为已验证）
     */
    public TableRelationship validate() {
        this.confidence = 100;
        this.validated = true;
        return this;
    }
    
    /**
     * 将列名字符串拆分为列表
     */
    private List<String> splitColumnNames(String columnNamesStr) {
        return Stream.of(columnNamesStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * 获取关系的描述性文本
     */
    public String getRelationshipDescription() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(getSourceFullName())
          .append(" (")
          .append(sourceColumnNames)
          .append(") ")
          .append(getRelationTypeSymbol())
          .append(" ")
          .append(getTargetFullName())
          .append(" (")
          .append(targetColumnNames)
          .append(")");
        
        return sb.toString();
    }
    
    /**
     * 获取关系类型符号
     */
    private String getRelationTypeSymbol() {
        switch (relationType) {
            case ONE_TO_ONE:
                return "1:1";
            case ONE_TO_MANY:
                return "1:N";
            case MANY_TO_ONE:
                return "N:1";
            case MANY_TO_MANY:
                return "N:M";
            default:
                return "→";
        }
    }

    /**
     * 关系类型枚举
     */
    public enum RelationshipType {
        /**
         * 一对一关系
         */
        ONE_TO_ONE,

        /**
         * 一对多关系
         */
        ONE_TO_MANY,

        /**
         * 多对一关系
         */
        MANY_TO_ONE,

        /**
         * 多对多关系
         */
        MANY_TO_MANY
    }

    /**
     * 关系来源枚举
     */
    public enum RelationshipSource {
        /**
         * 从数据库元数据中提取（如外键约束）
         */
        METADATA,

        /**
         * 通过推理算法推断（如命名规则、数据模式）
         */
        INFERENCE,

        /**
         * 用户手动定义
         */
        USER_FEEDBACK,

        /**
         * 从查询历史学习
         */
        LEARNING
    }

}