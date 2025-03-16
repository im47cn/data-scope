package com.insightdata.infrastructure.persistence.converter;

import com.insightdata.domain.model.metadata.TableRelationship;
import com.insightdata.infrastructure.persistence.entity.TableRelationshipEntity;
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
                .sourceTable(entity.getSourceTable())
                .sourceColumn(entity.getSourceColumn())
                .targetTable(entity.getTargetTable())
                .targetColumn(entity.getTargetColumn())
                .type(entity.getRelationshipType())
                .source(entity.getRelationshipSource())
                .weight(entity.getWeight())
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
                .sourceTable(domain.getSourceTable())
                .sourceColumn(domain.getSourceColumn())
                .targetTable(domain.getTargetTable())
                .targetColumn(domain.getTargetColumn())
                .relationshipType(domain.getType())
                .relationshipSource(domain.getSource())
                .weight(domain.getWeight())
                .frequency(domain.getFrequency())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}