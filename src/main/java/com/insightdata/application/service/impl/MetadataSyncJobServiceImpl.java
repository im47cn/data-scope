package com.insightdata.application.service.impl;

import com.insightdata.application.service.MetadataSyncJobService;
import com.insightdata.common.enums.SyncStatus;
import com.insightdata.common.enums.SyncType;
import com.insightdata.common.exception.InsightDataException;
import com.insightdata.domain.model.metadata.MetadataSyncJob;
import com.insightdata.domain.repository.MetadataSyncJobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 元数据同步作业服务实现
 */
@Service
public class MetadataSyncJobServiceImpl implements MetadataSyncJobService {
    
    private static final Logger logger = LoggerFactory.getLogger(MetadataSyncJobServiceImpl.class);
    
    private final MetadataSyncJobRepository metadataSyncJobRepository;
    
    @Autowired
    public MetadataSyncJobServiceImpl(MetadataSyncJobRepository metadataSyncJobRepository) {
        this.metadataSyncJobRepository = metadataSyncJobRepository;
    }
    
    @Override
    @Transactional
    public MetadataSyncJob createSyncJob(Long dataSourceId, SyncType type) {
        logger.info("创建同步作业: dataSourceId={}, type={}", dataSourceId, type);
        
        // 检查是否已存在进行中的同步作业
        Optional<MetadataSyncJob> inProgressJob = metadataSyncJobRepository.findByDataSourceIdAndStatus(dataSourceId, SyncStatus.RUNNING)
                .stream().findFirst();
        if (inProgressJob.isPresent()) {
            throw new InsightDataException("该数据源已有进行中的同步作业，请等待完成或取消");
        }
        
        MetadataSyncJob syncJob = new MetadataSyncJob();
        syncJob.setId(UUID.randomUUID().toString());
        syncJob.setDataSourceId(dataSourceId);
        syncJob.setType(type);
        syncJob.setStatus(SyncStatus.PENDING);
        syncJob.setProgress(0);
        syncJob.setCreatedAt(LocalDateTime.now());
        syncJob.setUpdatedAt(LocalDateTime.now());
        
        return metadataSyncJobRepository.save(syncJob);
    }
    
    @Override
    @Transactional
    public MetadataSyncJob startSyncJob(String jobId) {
        logger.info("启动同步作业: jobId={}", jobId);
        
        MetadataSyncJob syncJob = getAndValidateJob(jobId);
        
        if (syncJob.getStatus() != SyncStatus.PENDING) {
            throw new InsightDataException("只有'待处理'状态的同步作业可以启动");
        }
        
        syncJob.setStatus(SyncStatus.RUNNING);
        syncJob.setStartTime(LocalDateTime.now());
        syncJob.setUpdatedAt(LocalDateTime.now());
        
        return metadataSyncJobRepository.save(syncJob);
    }
    
    @Override
    @Transactional
    public MetadataSyncJob updateProgress(String jobId, int progress, String message) {
        logger.info("更新同步作业进度: jobId={}, progress={}, message={}", jobId, progress, message);
        
        MetadataSyncJob syncJob = getAndValidateJob(jobId);
        
        if (syncJob.getStatus() != SyncStatus.RUNNING) {
            throw new InsightDataException("只有'运行中'状态的同步作业可以更新进度");
        }
        
        if (progress < 0 || progress > 100) {
            throw new InsightDataException("进度值必须在0-100之间");
        }
        
        syncJob.setProgress(progress);
        if (message != null && !message.trim().isEmpty()) {
            syncJob.setErrorMessage(message); // 重用errorMessage字段存储进度消息
        }
        syncJob.setUpdatedAt(LocalDateTime.now());
        
        return metadataSyncJobRepository.save(syncJob);
    }
    
    @Override
    @Transactional
    public MetadataSyncJob completeSyncJob(String jobId) {
        logger.info("完成同步作业: jobId={}", jobId);
        
        MetadataSyncJob syncJob = getAndValidateJob(jobId);
        
        if (syncJob.getStatus() != SyncStatus.RUNNING) {
            throw new InsightDataException("只有'运行中'状态的同步作业可以标记为完成");
        }
        
        syncJob.setStatus(SyncStatus.COMPLETED);
        syncJob.setProgress(100);
        syncJob.setEndTime(LocalDateTime.now());
        syncJob.setUpdatedAt(LocalDateTime.now());
        
        return metadataSyncJobRepository.save(syncJob);
    }
    
    @Override
    @Transactional
    public MetadataSyncJob failSyncJob(String jobId, String errorMessage) {
        logger.info("标记同步作业失败: jobId={}, errorMessage={}", jobId, errorMessage);
        
        MetadataSyncJob syncJob = getAndValidateJob(jobId);
        
        if (syncJob.getStatus() != SyncStatus.RUNNING && syncJob.getStatus() != SyncStatus.PENDING) {
            throw new InsightDataException("只有'运行中'或'待处理'状态的同步作业可以标记为失败");
        }
        
        syncJob.setStatus(SyncStatus.FAILED);
        syncJob.setErrorMessage(errorMessage);
        syncJob.setEndTime(LocalDateTime.now());
        syncJob.setUpdatedAt(LocalDateTime.now());
        
        return metadataSyncJobRepository.save(syncJob);
    }
    
    @Override
    @Transactional
    public MetadataSyncJob cancelSyncJob(String jobId) {
        logger.info("取消同步作业: jobId={}", jobId);
        
        MetadataSyncJob syncJob = getAndValidateJob(jobId);
        
        if (syncJob.getStatus() != SyncStatus.RUNNING && syncJob.getStatus() != SyncStatus.PENDING) {
            throw new InsightDataException("只有'运行中'或'待处理'状态的同步作业可以取消");
        }
        
        syncJob.setStatus(SyncStatus.CANCELLED);
        syncJob.setEndTime(LocalDateTime.now());
        syncJob.setUpdatedAt(LocalDateTime.now());
        
        return metadataSyncJobRepository.save(syncJob);
    }
    
    @Override
    public Optional<MetadataSyncJob> getSyncJob(String jobId) {
        logger.debug("获取同步作业: jobId={}", jobId);
        return metadataSyncJobRepository.findById(jobId);
    }
    
    @Override
    public List<MetadataSyncJob> getSyncJobsByDataSource(Long dataSourceId) {
        logger.debug("获取数据源同步作业列表: dataSourceId={}", dataSourceId);
        return metadataSyncJobRepository.findByDataSourceId(dataSourceId);
    }
    
    @Override
    public Optional<MetadataSyncJob> getLatestSyncJob(Long dataSourceId) {
        logger.debug("获取数据源最新同步作业: dataSourceId={}", dataSourceId);
        return metadataSyncJobRepository.findLatestByDataSourceId(dataSourceId);
    }
    
    @Override
    public List<MetadataSyncJob> getSyncJobsByStatus(SyncStatus status) {
        logger.debug("获取指定状态同步作业列表: status={}", status);
        return metadataSyncJobRepository.findByStatus(status);
    }
    
    @Override
    public List<MetadataSyncJob> getInProgressSyncJobs() {
        logger.debug("获取进行中同步作业列表");
        return metadataSyncJobRepository.findByStatus(SyncStatus.RUNNING);
    }
    
    /**
     * 获取并验证同步作业是否存在
     * 
     * @param jobId 同步作业ID
     * @return 同步作业
     * @throws InsightDataException 如果同步作业不存在
     */
    private MetadataSyncJob getAndValidateJob(String jobId) {
        return metadataSyncJobRepository.findById(jobId)
                .orElseThrow(() -> new InsightDataException("同步作业不存在: " + jobId));
    }
}