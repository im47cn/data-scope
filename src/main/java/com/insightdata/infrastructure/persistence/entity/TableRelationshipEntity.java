package com.insightdata.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.insightdata.domain.model.metadata.TableRelationship;
import com.insightdata.domain.model.metadata.TableRelationship.RelationshipSource;
import com.insightdata.domain.model.metadata.TableRelationship.RelationshipType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 表关系实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "table_relationship")
public class TableRelationshipEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "data_source_id", nullable = false)
    private Long dataSourceId;
    
    @Column(name = "source_table", nullable = false)
    private String sourceTable;
    
    @Column(name = "source_column", nullable = false)
    private String sourceColumn;
    
    @Column(name = "target_table", nullable = false)
    private String targetTable;
    
    @Column(name = "target_column", nullable = false)
    private String targetColumn;
    
    @Column(name = "relationship_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RelationshipType type;
    
    @Column(name = "relationship_source", nullable = false)
    @Enumerated(EnumType.STRING)
    private RelationshipSource source;
    
    @Column(name = "weight", nullable = false)
    private double weight;
    
    @Column(name = "frequency", nullable = false)
    private int frequency;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    /**
     * 转换为领域模型
     */
    public TableRelationship toDomain() {
        return TableRelationship.builder()
                .id(id)
                .dataSourceId(dataSourceId)
                .sourceTable(sourceTable)
                .sourceColumn(sourceColumn)
                .targetTable(targetTable)
                .targetColumn(targetColumn)
                .type(type)
                .source(source)
                .weight(weight)
                .frequency(frequency)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
    
    /**
     * 从领域模型创建
     */
    public static TableRelationshipEntity fromDomain(TableRelationship relationship) {
        return TableRelationshipEntity.builder()
                .id(relationship.getId())
                .dataSourceId(relationship.getDataSourceId())
                .sourceTable(relationship.getSourceTable())
                .sourceColumn(relationship.getSourceColumn())
                .targetTable(relationship.getTargetTable())
                .targetColumn(relationship.getTargetColumn())
                .type(relationship.getType())
                .source(relationship.getSource())
                .weight(relationship.getWeight())
                .frequency(relationship.getFrequency())
                .createdAt(relationship.getCreatedAt())
                .updatedAt(relationship.getUpdatedAt())
                .build();
    }
}