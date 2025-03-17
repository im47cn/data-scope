package com.infrastructure.persistence.converter;

import com.domain.model.metadata.TableRelationship;
import com.infrastructure.persistence.entity.TableRelationshipEntity;
import org.springframework.stereotype.Component;

/**
 * 表关系实体与领域模型转换器
 */
@Component
public class TableRelationshipConverter {
    
    /**
     * 将实体转换为领域模型
     */
    public TableRelationship toDomain(TableRelationshipEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return TableRelationship.builder()
                .id(entity.getId())
                .dataSourceId(entity.getDataSourceId())
                .sourceTableName(entity.getSourceTable())
                .sourceColumnNames(entity.getSourceColumn())
                .targetTableName(entity.getTargetTable())
                .targetColumnNames(entity.getTargetColumn())
                .relationType(entity.getRelationshipType())
                .relationSource(entity.getRelationshipSource())
                .confidence(entity.getWeight())
                .frequency(entity.getFrequency())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
    
    /**
     * 将领域模型转换为实体
     */
    public TableRelationshipEntity toEntity(TableRelationship domain) {
        if (domain == null) {
            return null;
        }
        
        return TableRelationshipEntity.builder()
                .id(domain.getId())
                .dataSourceId(domain.getDataSourceId())
                .sourceTable(domain.getSourceTableName())
                .sourceColumn(domain.getSourceColumnNames())
                .targetTable(domain.getTargetTableName())
                .targetColumn(domain.getTargetColumnNames())
                .relationshipType(domain.getRelationType())
                .relationshipSource(domain.getRelationSource())
                .weight(domain.getConfidence())
                .frequency(domain.getFrequency())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}