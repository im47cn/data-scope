package com.insightdata.application.convertor;

import com.insightdata.domain.metadata.model.TableRelationship;
import com.insightdata.facade.metadata.TableRelationshipDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 表关系映射器
 * 负责DTO和实体对象之间的转换
 */
@Component
public class TableRelationshipConvertor {

    /**
     * 将DTO转换为实体
     *
     * @param dto 表关系DTO
     * @return 表关系实体
     */
    public TableRelationship toEntity(TableRelationshipDTO dto) {
        if (dto == null) {
            return null;
        }

        TableRelationship.Builder builder = TableRelationship.builder()
                .dataSourceId(dto.getDataSourceId())
                .sourceTableName(dto.getSourceTable())
                .sourceColumnNames(dto.getSourceColumn())
                .targetTableName(dto.getTargetTable())
                .targetColumnNames(dto.getTargetColumn())
                .confidence(dto.getWeight() != null ? dto.getWeight() : 0.5)
                .frequency(dto.getFrequency() != null ? dto.getFrequency() : 0)
                .createdAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now())
                .updatedAt(dto.getUpdatedAt() != null ? dto.getUpdatedAt() : LocalDateTime.now());

        // 设置ID
        if (dto.getId() != null && !dto.getId().isEmpty()) {
            builder.id(dto.getId());
        } else {
            builder.id(UUID.randomUUID().toString());
        }

        // 设置关系类型
        if (dto.getType() != null) {
            switch (dto.getType()) {
                case "ONE_TO_ONE":
                    builder.relationType(TableRelationship.RelationshipType.ONE_TO_ONE);
                    break;
                case "ONE_TO_MANY":
                    builder.relationType(TableRelationship.RelationshipType.ONE_TO_MANY);
                    break;
                case "MANY_TO_ONE":
                    builder.relationType(TableRelationship.RelationshipType.MANY_TO_ONE);
                    break;
                case "MANY_TO_MANY":
                    builder.relationType(TableRelationship.RelationshipType.MANY_TO_MANY);
                    break;
                default:
                    builder.relationType(TableRelationship.RelationshipType.UNKNOWN);
                    break;
            }
        } else {
            builder.relationType(TableRelationship.RelationshipType.UNKNOWN);
        }

        // 设置关系来源
        if (dto.getSource() != null) {
            switch (dto.getSource()) {
                case "METADATA":
                    builder.relationSource(TableRelationship.RelationshipSource.METADATA);
                    builder.explicit(true);
                    break;
                case "QUERY_HISTORY":
                    builder.relationSource(TableRelationship.RelationshipSource.LEARNING);
                    builder.explicit(false);
                    break;
                case "USER_FEEDBACK":
                    builder.relationSource(TableRelationship.RelationshipSource.USER_FEEDBACK);
                    builder.verified(true);
                    break;
                default:
                    builder.relationSource(TableRelationship.RelationshipSource.SYSTEM_GENERATED);
                    break;
            }
        } else {
            builder.relationSource(TableRelationship.RelationshipSource.SYSTEM_GENERATED);
        }

        return builder.build();
    }

    /**
     * 将实体转换为DTO
     *
     * @param entity 表关系实体
     * @return 表关系DTO
     */
    public TableRelationshipDTO toDTO(TableRelationship entity) {
        if (entity == null) {
            return null;
        }

        TableRelationshipDTO dto = new TableRelationshipDTO();
        dto.setId(entity.getId());
        dto.setDataSourceId(entity.getDataSourceId());
        dto.setSourceTable(entity.getSourceTableName());
        dto.setSourceColumn(entity.getSourceColumnNames());
        dto.setTargetTable(entity.getTargetTableName());
        dto.setTargetColumn(entity.getTargetColumnNames());
        dto.setWeight(entity.getConfidence());
        dto.setFrequency(entity.getFrequency());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        // 设置关系类型
        if (entity.getRelationType() != null) {
            dto.setType(entity.getRelationType().name());
        }

        // 设置关系来源
        if (entity.getRelationSource() != null) {
            switch (entity.getRelationSource()) {
                case METADATA:
                    dto.setSource("METADATA");
                    break;
                case LEARNING:
                    dto.setSource("QUERY_HISTORY");
                    break;
                case USER_FEEDBACK:
                    dto.setSource("USER_FEEDBACK");
                    break;
                case SYSTEM_GENERATED:
                    dto.setSource("SYSTEM");
                    break;
            }
        }

        return dto;
    }

    /**
     * 将实体列表转换为DTO列表
     *
     * @param entities 表关系实体列表
     * @return 表关系DTO列表
     */
    public List<TableRelationshipDTO> toDTOList(List<TableRelationship> entities) {
        if (entities == null) {
            return new ArrayList<>();
        }

        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}