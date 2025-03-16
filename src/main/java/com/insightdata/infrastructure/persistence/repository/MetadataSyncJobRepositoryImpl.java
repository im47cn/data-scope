package com.insightdata.infrastructure.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.insightdata.common.enums.SyncStatus;
import com.insightdata.domain.model.metadata.MetadataSyncJob;
import com.insightdata.domain.repository.MetadataSyncJobRepository;
import com.insightdata.infrastructure.persistence.entity.MetadataSyncJobEntity;
import com.insightdata.infrastructure.persistence.mapper.MetadataSyncJobMapper;

/**
 * 元数据同步作业仓储实现
 */
@Repository
public class MetadataSyncJobRepositoryImpl implements MetadataSyncJobRepository {

    @Autowired
    private MetadataSyncJobMapper metadataSyncJobMapper;
    
    @Override
    public MetadataSyncJob save(MetadataSyncJob syncJob) {
        MetadataSyncJobEntity entity = toEntity(syncJob);
        
        LocalDateTime now = LocalDateTime.now();
        
        if (entity.getId() == null || entity.getId().isEmpty()) {
            // 新增
            entity.setId(UUID.randomUUID().toString());
            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);
            metadataSyncJobMapper.insert(entity);
        } else {
            // 更新
            entity.setUpdatedAt(now);
            metadataSyncJobMapper.update(entity);
        }
        
        return toModel(entity);
    }

    @Override
    public Optional<MetadataSyncJob> findById(String id) {
        MetadataSyncJobEntity entity = metadataSyncJobMapper.selectById(id);
        return Optional.ofNullable(entity).map(this::toModel);
    }

    @Override
    public List<MetadataSyncJob> findByDataSourceId(Long dataSourceId) {
        List<MetadataSyncJobEntity> entities = metadataSyncJobMapper.selectByDataSourceId(dataSourceId);
        return entities.stream().map(this::toModel).collect(Collectors.toList());
    }

    @Override
    public List<MetadataSyncJob> findByDataSourceIdAndStatus(Long dataSourceId, SyncStatus status) {
        List<MetadataSyncJobEntity> entities = metadataSyncJobMapper.selectByDataSourceIdAndStatus(dataSourceId, status);
        return entities.stream().map(this::toModel).collect(Collectors.toList());
    }

    @Override
    public List<MetadataSyncJob> findByStatus(SyncStatus status) {
        List<MetadataSyncJobEntity> entities = metadataSyncJobMapper.selectByStatus(status);
        return entities.stream().map(this::toModel).collect(Collectors.toList());
    }

    @Override
    public Optional<MetadataSyncJob> findLatestByDataSourceId(Long dataSourceId) {
        MetadataSyncJobEntity entity = metadataSyncJobMapper.selectLatestByDataSourceId(dataSourceId);
        return Optional.ofNullable(entity).map(this::toModel);
    }

    @Override
    public void deleteById(String id) {
        metadataSyncJobMapper.deleteById(id);
    }

    @Override
    public void deleteByDataSourceId(Long dataSourceId) {
        metadataSyncJobMapper.deleteByDataSourceId(dataSourceId);
    }
    
    /**
     * 将领域模型转换为实体
     */
    private MetadataSyncJobEntity toEntity(MetadataSyncJob model) {
        if (model == null) {
            return null;
        }
        
        MetadataSyncJobEntity entity = new MetadataSyncJobEntity();
        entity.setId(model.getId());
        entity.setDataSourceId(model.getDataSourceId());
        entity.setType(model.getType());
        entity.setStatus(model.getStatus());
        entity.setStartTime(model.getStartTime());
        entity.setEndTime(model.getEndTime());
        entity.setProgress(model.getProgress());
        entity.setTotalItems(model.getTotalItems());
        entity.setProcessedItems(model.getProcessedItems());
        entity.setParameters(model.getParameters());
        entity.setErrorMessage(model.getErrorMessage());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        
        return entity;
    }
    
    /**
     * 将实体转换为领域模型
     */
    private MetadataSyncJob toModel(MetadataSyncJobEntity entity) {
        if (entity == null) {
            return null;
        }
        
        MetadataSyncJob model = new MetadataSyncJob();
        model.setId(entity.getId());
        model.setDataSourceId(entity.getDataSourceId());
        model.setType(entity.getType());
        model.setStatus(entity.getStatus());
        model.setStartTime(entity.getStartTime());
        model.setEndTime(entity.getEndTime());
        model.setProgress(entity.getProgress());
        model.setTotalItems(entity.getTotalItems());
        model.setProcessedItems(entity.getProcessedItems());
        model.setParameters(entity.getParameters());
        model.setErrorMessage(entity.getErrorMessage());
        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());
        
        return model;
    }
}