package com.insightdata.domain.metadata.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 表关系实体类，用于存储和管理表之间的关联关系
 */
@Data
public class TableRelationship {
    
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
        MANY_TO_MANY,
        
        /**
         * 未知关系类型
         */
        UNKNOWN
    }
    
    /**
     * 关系来源枚举
     */
    public enum RelationshipSource {
        /**
         * 从数据库元数据推断
         */
        METADATA,
        
        /**
         * 从查询历史学习
         */
        LEARNING,
        
        /**
         * 用户手动指定
         */
        USER_FEEDBACK,
        
        /**
         * 系统自动生成
         */
        SYSTEM_GENERATED
    }
    
    /**
     * 主键ID
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
     * 源列名（可能是多列组合的复合外键）
     */
    private String sourceColumnNames;
    
    /**
     * 目标表名
     */
    private String targetTableName;
    
    /**
     * 目标列名（可能是多列组合的复合主键）
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
     * 置信度（0-1之间的值，表示关系推断的确定性）
     */
    private double confidence;
    
    /**
     * 使用频率（表示该关系在查询中使用的次数）
     */
    private int frequency;
    
    /**
     * 是否显式关系（显式表示通过外键定义的关系，隐式表示通过推断得到的关系）
     */
    private boolean explicit;
    
    /**
     * 是否已验证
     */
    private boolean verified;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 备注信息
     */
    private String remarks;
    
    /**
     * 创建人
     */
    private String createdBy;
    
    /**
     * 更新人
     */
    private String updatedBy;
    
    /**
     * 默认构造函数
     */
    public TableRelationship() {
    }
    
    /**
     * 全参数构造函数
     */
    public TableRelationship(String id, String dataSourceId, String sourceTableName, String sourceColumnNames,
                           String targetTableName, String targetColumnNames, RelationshipType relationType,
                           RelationshipSource relationSource, double confidence, int frequency, boolean explicit,
                           boolean verified, LocalDateTime createdAt, LocalDateTime updatedAt, String remarks,
                           String createdBy, String updatedBy) {
        this.id = id;
        this.dataSourceId = dataSourceId;
        this.sourceTableName = sourceTableName;
        this.sourceColumnNames = sourceColumnNames;
        this.targetTableName = targetTableName;
        this.targetColumnNames = targetColumnNames;
        this.relationType = relationType;
        this.relationSource = relationSource;
        this.confidence = confidence;
        this.frequency = frequency;
        this.explicit = explicit;
        this.verified = verified;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.remarks = remarks;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }
    
    /**
     * 创建Builder实例
     * 
     * @return Builder实例
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * 创建一个新的表关系实例
     * 
     * @param dataSourceId 数据源ID
     * @param sourceTableName 源表名
     * @param sourceColumnNames 源列名
     * @param targetTableName 目标表名
     * @param targetColumnNames 目标列名
     * @param relationType 关系类型
     * @param relationSource 关系来源
     * @param confidence 置信度
     * @return 表关系实例
     */
    public static TableRelationship create(
            String dataSourceId,
            String sourceTableName,
            String sourceColumnNames,
            String targetTableName,
            String targetColumnNames,
            RelationshipType relationType,
            RelationshipSource relationSource,
            double confidence) {
        
        TableRelationship relationship = new TableRelationship();
        relationship.id = UUID.randomUUID().toString();
        relationship.dataSourceId = dataSourceId;
        relationship.sourceTableName = sourceTableName;
        relationship.sourceColumnNames = sourceColumnNames;
        relationship.targetTableName = targetTableName;
        relationship.targetColumnNames = targetColumnNames;
        relationship.relationType = relationType;
        relationship.relationSource = relationSource;
        relationship.confidence = confidence;
        relationship.frequency = 1;
        relationship.explicit = relationSource == RelationshipSource.METADATA;
        relationship.verified = relationSource == RelationshipSource.USER_FEEDBACK;
        relationship.createdAt = LocalDateTime.now();
        relationship.updatedAt = LocalDateTime.now();
        
        return relationship;
    }
    
    /**
     * 检查两个关系是否表示相同的链接（源表/列和目标表/列相同）
     * 
     * @param other 另一个关系
     * @return 是否相同链接
     */
    public boolean isSameLink(TableRelationship other) {
        if (other == null) {
            return false;
        }
        
        return this.sourceTableName.equalsIgnoreCase(other.sourceTableName) && 
               this.sourceColumnNames.equalsIgnoreCase(other.sourceColumnNames) &&
               this.targetTableName.equalsIgnoreCase(other.targetTableName) &&
               this.targetColumnNames.equalsIgnoreCase(other.targetColumnNames);
    }
    
    /**
     * 检查是否为反向关系（源表/列和目标表/列互换）
     * 
     * @param other 另一个关系
     * @return 是否为反向关系
     */
    public boolean isReverseLink(TableRelationship other) {
        if (other == null) {
            return false;
        }
        
        return this.sourceTableName.equalsIgnoreCase(other.targetTableName) && 
               this.sourceColumnNames.equalsIgnoreCase(other.targetColumnNames) &&
               this.targetTableName.equalsIgnoreCase(other.sourceTableName) &&
               this.targetColumnNames.equalsIgnoreCase(other.sourceColumnNames);
    }
    
    /**
     * 获取关系的反向表示
     * 
     * @return 反向关系
     */
    public TableRelationship reverse() {
        TableRelationship reversed = new TableRelationship();
        reversed.id = UUID.randomUUID().toString();
        reversed.dataSourceId = this.dataSourceId;
        reversed.sourceTableName = this.targetTableName;
        reversed.sourceColumnNames = this.targetColumnNames;
        reversed.targetTableName = this.sourceTableName;
        reversed.targetColumnNames = this.sourceColumnNames;
        
        // 调整关系类型
        if (this.relationType == RelationshipType.ONE_TO_MANY) {
            reversed.relationType = RelationshipType.MANY_TO_ONE;
        } else if (this.relationType == RelationshipType.MANY_TO_ONE) {
            reversed.relationType = RelationshipType.ONE_TO_MANY;
        } else {
            reversed.relationType = this.relationType;
        }
        
        reversed.relationSource = this.relationSource;
        reversed.confidence = this.confidence;
        reversed.frequency = this.frequency;
        reversed.explicit = this.explicit;
        reversed.verified = this.verified;
        reversed.createdAt = LocalDateTime.now();
        reversed.updatedAt = LocalDateTime.now();
        reversed.remarks = "反向关系自动生成：" + this.id;
        
        return reversed;
    }
    
    /**
     * 生成关系的描述文本
     * 
     * @return 描述文本
     */
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(sourceTableName)
          .append("(")
          .append(sourceColumnNames)
          .append(") → ")
          .append(targetTableName)
          .append("(")
          .append(targetColumnNames)
          .append("): ");
          
        switch (relationType) {
            case ONE_TO_ONE:
                sb.append("一对一");
                break;
            case ONE_TO_MANY:
                sb.append("一对多");
                break;
            case MANY_TO_ONE:
                sb.append("多对一");
                break;
            case MANY_TO_MANY:
                sb.append("多对多");
                break;
            default:
                sb.append("未知类型");
                break;
        }
        
        sb.append(" [置信度: ").append(String.format("%.2f", confidence))
          .append(", 频率: ").append(frequency)
          .append("]");
        
        return sb.toString();
    }
    
    /**
     * 生成用于展示的关系文本（简短版本）
     * 
     * @return 展示文本
     */
    public String getDisplayText() {
        return sourceTableName + "." + sourceColumnNames + " → " +
               targetTableName + "." + targetColumnNames;
    }
    
    /**
     * 更新关系的置信度
     * 
     * @param newConfidence 新的置信度值
     * @return 更新后的关系实例
     */
    public TableRelationship updateConfidence(double newConfidence) {
        if (newConfidence < 0) {
            newConfidence = 0;
        } else if (newConfidence > 1) {
            newConfidence = 1;
        }
        
        this.confidence = newConfidence;
        this.updatedAt = LocalDateTime.now();
        return this;
    }
    
    /**
     * 增加使用频率
     * 
     * @return 更新后的关系实例
     */
    public TableRelationship incrementFrequency() {
        this.frequency++;
        this.updatedAt = LocalDateTime.now();
        return this;
    }
    
    /**
     * 判断该关系是否包含指定的表
     * 
     * @param tableName 表名
     * @return 是否包含指定表
     */
    public boolean containsTable(String tableName) {
        return sourceTableName.equalsIgnoreCase(tableName) || 
               targetTableName.equalsIgnoreCase(tableName);
    }
    
    /**
     * 判断该关系是否包含指定的表和列组合
     * 
     * @param tableName 表名
     * @param columnName 列名
     * @return 是否包含指定的表和列组合
     */
    public boolean containsTableColumn(String tableName, String columnName) {
        if (sourceTableName.equalsIgnoreCase(tableName)) {
            return sourceColumnNames.equalsIgnoreCase(columnName) ||
                   sourceColumnNames.contains("," + columnName + ",") ||
                   sourceColumnNames.startsWith(columnName + ",") ||
                   sourceColumnNames.endsWith("," + columnName);
        }
        
        if (targetTableName.equalsIgnoreCase(tableName)) {
            return targetColumnNames.equalsIgnoreCase(columnName) ||
                   targetColumnNames.contains("," + columnName + ",") ||
                   targetColumnNames.startsWith(columnName + ",") ||
                   targetColumnNames.endsWith("," + columnName);
        }
        
        return false;
    }
    
    /**
     * 计算关系得分（结合置信度和使用频率）
     * 
     * @return 关系得分
     */
    public double getScore() {
        // 70% 权重给置信度，30% 权重给频率归一化值
        double normalizedFrequency = Math.min(1.0, frequency / 10.0); // 假设频率10次及以上算作满分
        return confidence * 0.7 + normalizedFrequency * 0.3;
    }
    
    /**
     * Builder类，用于构建TableRelationship实例
     */
    public static class Builder {
        private String id;
        private String dataSourceId;
        private String sourceTableName;
        private String sourceColumnNames;
        private String targetTableName;
        private String targetColumnNames;
        private RelationshipType relationType;
        private RelationshipSource relationSource;
        private double confidence;
        private int frequency;
        private boolean explicit;
        private boolean verified;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String remarks;
        private String createdBy;
        private String updatedBy;
        
        public Builder id(String id) {
            this.id = id;
            return this;
        }
        
        public Builder dataSourceId(String dataSourceId) {
            this.dataSourceId = dataSourceId;
            return this;
        }
        
        public Builder sourceTableName(String sourceTableName) {
            this.sourceTableName = sourceTableName;
            return this;
        }
        
        public Builder sourceColumnNames(String sourceColumnNames) {
            this.sourceColumnNames = sourceColumnNames;
            return this;
        }
        
        public Builder targetTableName(String targetTableName) {
            this.targetTableName = targetTableName;
            return this;
        }
        
        public Builder targetColumnNames(String targetColumnNames) {
            this.targetColumnNames = targetColumnNames;
            return this;
        }
        
        public Builder relationType(RelationshipType relationType) {
            this.relationType = relationType;
            return this;
        }
        
        public Builder relationSource(RelationshipSource relationSource) {
            this.relationSource = relationSource;
            return this;
        }
        
        public Builder confidence(double confidence) {
            this.confidence = confidence;
            return this;
        }
        
        public Builder frequency(int frequency) {
            this.frequency = frequency;
            return this;
        }
        
        public Builder explicit(boolean explicit) {
            this.explicit = explicit;
            return this;
        }
        
        public Builder verified(boolean verified) {
            this.verified = verified;
            return this;
        }
        
        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }
        
        public Builder remarks(String remarks) {
            this.remarks = remarks;
            return this;
        }
        
        public Builder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }
        
        public Builder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }
        
        public TableRelationship build() {
            return new TableRelationship(
                id, dataSourceId, sourceTableName, sourceColumnNames, targetTableName, targetColumnNames,
                relationType, relationSource, confidence, frequency, explicit, verified,
                createdAt, updatedAt, remarks, createdBy, updatedBy);
        }
    }
}