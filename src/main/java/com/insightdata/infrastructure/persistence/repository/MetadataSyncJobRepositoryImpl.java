package com.insightdata.infrastructure.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.insightdata.common.enums.SyncStatus;
import com.insightdata.domain.model.metadata.MetadataSyncJob;
import com.insightdata.domain.repository.MetadataSyncJobRepository;
import com.insightdata.infrastructure.persistence.entity.MetadataSyncJobEntity;

/**
 * 元数据同步作业仓储实现类
 */
@Repository
public class MetadataSyncJobRepositoryImpl implements MetadataSyncJobRepository {
    
    private final JpaMetadataSyncJobRepository jpaMetadataSyncJobRepository;
    
    @Autowired
    public MetadataSyncJobRepositoryImpl(JpaMetadataSyncJobRepository jpaMetadataSyncJobRepository) {
        this.jpaMetadataSyncJobRepository = jpaMetadataSyncJobRepository;
    }
    
    @Override
    public MetadataSyncJob save(MetadataSyncJob syncJob) {
        MetadataSyncJobEntity entity = toEntity(syncJob);
        
        // 设置创建/更新时间
        LocalDateTime now = LocalDateTime.now();
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(now);
        }
        entity.setUpdatedAt(now);
        
        // 保存实体
        MetadataSyncJobEntity savedEntity = jpaMetadataSyncJobRepository.save(entity);
        
        // 转换回领域模型
        return toDomain(savedEntity);
    }
    
    @Override
    public Optional<MetadataSyncJob> findById(String id) {
        return jpaMetadataSyncJobRepository.findById(id)
                .map(this::toDomain);
    }
    
    @Override
    public List<MetadataSyncJob> findByDataSourceId(Long dataSourceId) {
        return jpaMetadataSyncJobRepository.findByDataSourceId(dataSourceId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<MetadataSyncJob> findByDataSourceIdAndStatus(Long dataSourceId, SyncStatus status) {
        return jpaMetadataSyncJobRepository.findByDataSourceIdAndStatus(dataSourceId, status).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<MetadataSyncJob> findByStatus(SyncStatus status) {
        return jpaMetadataSyncJobRepository.findByStatus(status).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<MetadataSyncJob> findLatestByDataSourceId(Long dataSourceId) {
        List<MetadataSyncJobEntity> jobs = jpaMetadataSyncJobRepository.findByDataSourceIdOrderByCreatedAtDesc(dataSourceId);
        if (jobs.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(toDomain(jobs.get(0)));
    }
    
    @Override
    public void deleteById(String id) {
        jpaMetadataSyncJobRepository.deleteById(id);
    }
    
    @Override
    public void deleteByDataSourceId(Long dataSourceId) {
        jpaMetadataSyncJobRepository.deleteByDataSourceId(dataSourceId);
    }
    
    /**
     * 将领域模型转换为实体
     */
    private MetadataSyncJobEntity toEntity(MetadataSyncJob syncJob) {
        if (syncJob == null) {
            return null;
        }
        
        MetadataSyncJobEntity entity = new MetadataSyncJobEntity();
        entity.setId(syncJob.getId());
        entity.setDataSourceId(syncJob.getDataSourceId());
        entity.setType(syncJob.getType());
        entity.setStatus(syncJob.getStatus());
        entity.setProgress(syncJob.getProgress());
        entity.setStartTime(syncJob.getStartTime());
        entity.setEndTime(syncJob.getEndTime());
        entity.setErrorMessage(syncJob.getErrorMessage());
        entity.setCreatedAt(syncJob.getCreatedAt());
        entity.setUpdatedAt(syncJob.getUpdatedAt());
        
        return entity;
    }
    
    /**
     * 将实体转换为领域模型
     */
    private MetadataSyncJob toDomain(MetadataSyncJobEntity entity) {
        if (entity == null) {
            return null;
        }
        
        MetadataSyncJob syncJob = new MetadataSyncJob();
        syncJob.setId(entity.getId());
        syncJob.setDataSourceId(entity.getDataSourceId());
        syncJob.setType(entity.getType());
        syncJob.setStatus(entity.getStatus());
        syncJob.setProgress(entity.getProgress());
        syncJob.setStartTime(entity.getStartTime());
        syncJob.setEndTime(entity.getEndTime());
        syncJob.setErrorMessage(entity.getErrorMessage());
        syncJob.setCreatedAt(entity.getCreatedAt());
        syncJob.setUpdatedAt(entity.getUpdatedAt());
        
        return syncJob;
    }
}