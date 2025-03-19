package com.insightdata.application.convertor;

import com.insightdata.domain.query.model.SavedQuery;
import com.insightdata.facade.query.SavedQueryDTO;
import com.insightdata.facade.query.UpdateSavedQueryDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 保存的查询映射器
 * 负责DTO和实体对象之间的转换
 */
@Component
public class SavedQueryConvertor {

    /**
     * 将DTO转换为实体
     *
     * @param dto 查询模板DTO
     * @return 查询模板实体
     */
    public SavedQuery toEntity(SavedQueryDTO dto) {
        if (dto == null) {
            return null;
        }

        return SavedQuery.builder()
                .id(dto.getId())
                .dataSourceId(dto.getDataSourceId())
                .name(dto.getName())
                .description(dto.getDescription())
                .sql(dto.getSql())
                .tags(dto.getTags())
                .isShared(dto.isPublic())
                .createdBy(dto.getCreatedBy())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .executionCount((long) dto.getUsageCount())
                .lastExecutedAt(dto.getLastExecutedAt())
                .build();
    }

    /**
     * 将更新DTO转换为实体
     *
     * @param dto 更新查询模板DTO
     * @return 查询模板实体
     */
    public SavedQuery toEntity(UpdateSavedQueryDTO dto) {
        if (dto == null) {
            return null;
        }

        return SavedQuery.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .tags(dto.getTags())
                .isShared(dto.isPublic())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * 将实体转换为DTO
     *
     * @param entity 查询模板实体
     * @return 查询模板DTO
     */
    public SavedQueryDTO toDTO(SavedQuery entity) {
        if (entity == null) {
            return null;
        }

        SavedQueryDTO dto = new SavedQueryDTO();
        dto.setId(entity.getId());
        dto.setDataSourceId(entity.getDataSourceId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setSql(entity.getSql());
        dto.setTags(entity.getTags());
        dto.setPublic(entity.getIsShared());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setUsageCount(entity.getExecutionCount() != null ? entity.getExecutionCount().intValue() : 0);
        dto.setLastExecutedAt(entity.getLastExecutedAt());
        
        return dto;
    }

    /**
     * 将实体列表转换为DTO列表
     *
     * @param entities 查询模板实体列表
     * @return 查询模板DTO列表
     */
    public List<SavedQueryDTO> toDTOList(List<SavedQuery> entities) {
        if (entities == null) {
            return new ArrayList<>();
        }

        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}