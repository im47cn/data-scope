package com.insightdata.infrastructure.persistence.converter;

import com.insightdata.domain.model.metadata.MetadataSyncJob;
import com.insightdata.infrastructure.persistence.entity.MetadataSyncJobEntity;
import org.springframework.stereotype.Component;

/**
 * 元数据同步作业实体和领域模型之间的转换器
 */
@Component
public class MetadataSyncJobConverter {
    
    /**
     * 将实体转换为领域模型
     *
     * @param entity 元数据同步作业实体
     * @return 元数据同步作业领域模型
     */
    public MetadataSyncJob toDomain(MetadataSyncJobEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return MetadataSyncJob.builder()
                .id(entity.getId())
                .dataSourceId(entity.getDataSourceId())
                .type(entity.getType())
                .status(entity.getStatus())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .progress(entity.getProgress())
                .totalItems(entity.getTotalItems())
                .processedItems(entity.getProcessedItems())
                .parameters(entity.getParameters())
                .errorMessage(entity.getErrorMessage())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
    
    /**
     * 将领域模型转换为实体
     *
     * @param domain 元数据同步作业领域模型
     * @return 元数据同步作业实体
     */
    public MetadataSyncJobEntity toEntity(MetadataSyncJob domain) {
        if (domain == null) {
            return null;
        }
        
        return MetadataSyncJobEntity.builder()
                .id(domain.getId())
                .dataSourceId(domain.getDataSourceId())
                .type(domain.getType())
                .status(domain.getStatus())
                .startTime(domain.getStartTime())
                .endTime(domain.getEndTime())
                .progress(domain.getProgress())
                .totalItems(domain.getTotalItems())
                .processedItems(domain.getProcessedItems())
                .parameters(domain.getParameters())
                .errorMessage(domain.getErrorMessage())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}