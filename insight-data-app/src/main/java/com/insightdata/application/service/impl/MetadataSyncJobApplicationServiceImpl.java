package com.insightdata.application.service.impl;

import com.insightdata.application.service.MetadataSyncJobApplicationService;
import com.insightdata.application.convertor.EnumConverter;
import com.insightdata.domain.exception.InsightDataException;
import com.insightdata.domain.metadata.model.MetadataSyncJob;
import com.insightdata.domain.metadata.repository.MetadataSyncJobRepository;
import com.insightdata.facade.metadata.enums.SyncStatus;
import com.insightdata.facade.metadata.enums.SyncType;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
public class MetadataSyncJobApplicationServiceImpl implements MetadataSyncJobApplicationService {
    
    private final MetadataSyncJobRepository metadataSyncJobRepository;
    
    @Autowired
    public MetadataSyncJobApplicationServiceImpl(MetadataSyncJobRepository metadataSyncJobRepository) {
        this.metadataSyncJobRepository = metadataSyncJobRepository;
    }
    
    @Override
    @Transactional
    public MetadataSyncJob createSyncJob(String dataSourceId, SyncType type) {
        log.info("创建同步作业: dataSourceId={}, type={}", dataSourceId, type);
        
        // 检查是否已存在进行中的同步作业
        Optional<MetadataSyncJob> inProgressJob = metadataSyncJobRepository.findByDataSourceIdAndStatus(
                dataSourceId,
                EnumConverter.toDomainSyncStatus(SyncStatus.RUNNING)
            ).stream().findFirst();
            
        if (inProgressJob.isPresent()) {
            throw new InsightDataException("该数据源已有进行中的同步作业，请等待完成或取消");
        }
        
        // 创建领域模型对象
        com.insightdata.domain.metadata.model.MetadataSyncJob domainSyncJob = new com.insightdata.domain.metadata.model.MetadataSyncJob();
        domainSyncJob.setId(UUID.randomUUID().toString());
        domainSyncJob.setDataSourceId(dataSourceId);
        domainSyncJob.setType(EnumConverter.toDomainSyncType(type));
        domainSyncJob.setStatus(EnumConverter.toDomainSyncStatus(SyncStatus.PENDING));
        domainSyncJob.setProgress(0);
        domainSyncJob.setCreatedAt(LocalDateTime.now());
        domainSyncJob.setUpdatedAt(LocalDateTime.now());
        
        // 保存并转换回Facade模型
        com.insightdata.domain.metadata.model.MetadataSyncJob savedDomainJob = metadataSyncJobRepository.save(domainSyncJob);
        return EnumConverter.toFacadeMetadataSyncJob(savedDomainJob);
    }
    
    @Override
    @Transactional
    public MetadataSyncJob startSyncJob(String jobId) {
        log.info("启动同步作业: jobId={}", jobId);
        
        // 获取并验证领域模型
        com.insightdata.domain.metadata.model.MetadataSyncJob domainSyncJob = getAndValidateDomainJob(jobId);
        
        // 检查状态
        if (domainSyncJob.getStatus() != com.insightdata.domain.metadata.enums.SyncStatus.PENDING) {
            throw new InsightDataException("只有'待处理'状态的同步作业可以启动");
        }
        
        // 更新状态
        domainSyncJob.setStatus(com.insightdata.domain.metadata.enums.SyncStatus.RUNNING);
        domainSyncJob.setStartTime(LocalDateTime.now());
        domainSyncJob.setUpdatedAt(LocalDateTime.now());
        
        // 保存并转换回Facade模型
        com.insightdata.domain.metadata.model.MetadataSyncJob savedDomainJob = metadataSyncJobRepository.save(domainSyncJob);
        return EnumConverter.toFacadeMetadataSyncJob(savedDomainJob);
    }
    
    @Override
    @Transactional
    public MetadataSyncJob updateProgress(String jobId, int progress, String message) {
        log.info("更新同步作业进度: jobId={}, progress={}, message={}", jobId, progress, message);
        
        // 获取并验证领域模型
        com.insightdata.domain.metadata.model.MetadataSyncJob domainSyncJob = getAndValidateDomainJob(jobId);
        
        // 检查状态
        if (domainSyncJob.getStatus() != com.insightdata.domain.metadata.enums.SyncStatus.RUNNING) {
            throw new InsightDataException("只有'运行中'状态的同步作业可以更新进度");
        }
        
        if (progress < 0 || progress > 100) {
            throw new InsightDataException("进度值必须在0-100之间");
        }
        
        // 更新进度
        domainSyncJob.setProgress(progress);
        if (message != null && !message.trim().isEmpty()) {
            domainSyncJob.setErrorMessage(message); // 重用errorMessage字段存储进度消息
        }
        domainSyncJob.setUpdatedAt(LocalDateTime.now());
        
        // 保存并转换回Facade模型
        com.insightdata.domain.metadata.model.MetadataSyncJob savedDomainJob = metadataSyncJobRepository.save(domainSyncJob);
        return EnumConverter.toFacadeMetadataSyncJob(savedDomainJob);
    }
    
    @Override
    @Transactional
    public MetadataSyncJob completeSyncJob(String jobId) {
        log.info("完成同步作业: jobId={}", jobId);
        
        // 获取并验证领域模型
        com.insightdata.domain.metadata.model.MetadataSyncJob domainSyncJob = getAndValidateDomainJob(jobId);
        
        // 检查状态
        if (domainSyncJob.getStatus() != com.insightdata.domain.metadata.enums.SyncStatus.RUNNING) {
            throw new InsightDataException("只有'运行中'状态的同步作业可以标记为完成");
        }
        
        // 更新状态
        domainSyncJob.setStatus(com.insightdata.domain.metadata.enums.SyncStatus.COMPLETED);
        domainSyncJob.setProgress(100);
        domainSyncJob.setEndTime(LocalDateTime.now());
        domainSyncJob.setUpdatedAt(LocalDateTime.now());
        
        // 保存并转换回Facade模型
        com.insightdata.domain.metadata.model.MetadataSyncJob savedDomainJob = metadataSyncJobRepository.save(domainSyncJob);
        return EnumConverter.toFacadeMetadataSyncJob(savedDomainJob);
    }
    
    @Override
    @Transactional
    public MetadataSyncJob failSyncJob(String jobId, String errorMessage) {
        log.info("标记同步作业失败: jobId={}, errorMessage={}", jobId, errorMessage);
        
        // 获取并验证领域模型
        com.insightdata.domain.metadata.model.MetadataSyncJob domainSyncJob = getAndValidateDomainJob(jobId);
        
        // 检查状态
        if (domainSyncJob.getStatus() != com.insightdata.domain.metadata.enums.SyncStatus.RUNNING &&
            domainSyncJob.getStatus() != com.insightdata.domain.metadata.enums.SyncStatus.PENDING) {
            throw new InsightDataException("只有'运行中'或'待处理'状态的同步作业可以标记为失败");
        }
        
        // 更新状态
        domainSyncJob.setStatus(com.insightdata.domain.metadata.enums.SyncStatus.FAILED);
        domainSyncJob.setErrorMessage(errorMessage);
        domainSyncJob.setEndTime(LocalDateTime.now());
        domainSyncJob.setUpdatedAt(LocalDateTime.now());
        
        // 保存并转换回Facade模型
        com.insightdata.domain.metadata.model.MetadataSyncJob savedDomainJob = metadataSyncJobRepository.save(domainSyncJob);
        return EnumConverter.toFacadeMetadataSyncJob(savedDomainJob);
    }
    
    @Override
    @Transactional
    public MetadataSyncJob cancelSyncJob(String jobId) {
        log.info("取消同步作业: jobId={}", jobId);
        
        // 获取并验证领域模型
        com.insightdata.domain.metadata.model.MetadataSyncJob domainSyncJob = getAndValidateDomainJob(jobId);
        
        // 检查状态
        if (domainSyncJob.getStatus() != com.insightdata.domain.metadata.enums.SyncStatus.RUNNING &&
            domainSyncJob.getStatus() != com.insightdata.domain.metadata.enums.SyncStatus.PENDING) {
            throw new InsightDataException("只有'运行中'或'待处理'状态的同步作业可以取消");
        }
        
        // 更新状态
        domainSyncJob.setStatus(com.insightdata.domain.metadata.enums.SyncStatus.CANCELLED);
        domainSyncJob.setEndTime(LocalDateTime.now());
        domainSyncJob.setUpdatedAt(LocalDateTime.now());
        
        // 保存并转换回Facade模型
        com.insightdata.domain.metadata.model.MetadataSyncJob savedDomainJob = metadataSyncJobRepository.save(domainSyncJob);
        return EnumConverter.toFacadeMetadataSyncJob(savedDomainJob);
    }
    
    @Override
    public Optional<MetadataSyncJob> getSyncJob(String jobId) {
        log.debug("获取同步作业: jobId={}", jobId);
        return metadataSyncJobRepository.findById(jobId);
    }
    
    @Override
    public List<MetadataSyncJob> getSyncJobsByDataSource(String dataSourceId) {
        log.debug("获取数据源同步作业列表: dataSourceId={}", dataSourceId);
        return metadataSyncJobRepository.findByDataSourceId(dataSourceId);
    }
    
    @Override
    public Optional<MetadataSyncJob> getLatestSyncJob(String dataSourceId) {
        log.debug("获取数据源最新同步作业: dataSourceId={}", dataSourceId);
        return metadataSyncJobRepository.findLatestByDataSourceId(dataSourceId);
    }
    
    @Override
    public List<MetadataSyncJob> getSyncJobsByStatus(SyncStatus status) {
        log.debug("获取指定状态同步作业列表: status={}", status);
        
        // 转换枚举类型
        com.insightdata.domain.metadata.enums.SyncStatus domainStatus = EnumConverter.toDomainSyncStatus(status);
        
        // 查询并转换结果
        List<com.insightdata.domain.metadata.model.MetadataSyncJob> domainJobs = metadataSyncJobRepository.findByStatus(domainStatus);
        return domainJobs.stream()
                .map(EnumConverter::toFacadeMetadataSyncJob)
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public List<MetadataSyncJob> getInProgressSyncJobs() {
        log.debug("获取进行中同步作业列表");
        
        // 转换枚举类型
        com.insightdata.domain.metadata.enums.SyncStatus domainStatus =
                EnumConverter.toDomainSyncStatus(SyncStatus.RUNNING);
        
        // 查询并转换结果
        List<com.insightdata.domain.metadata.model.MetadataSyncJob> domainJobs = metadataSyncJobRepository.findByStatus(domainStatus);
        return domainJobs.stream()
                .map(EnumConverter::toFacadeMetadataSyncJob)
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * 获取并验证同步作业是否存在（领域模型）
     *
     * @param jobId 同步作业ID
     * @return 领域模型同步作业
     * @throws InsightDataException 如果同步作业不存在
     */
    private com.insightdata.domain.metadata.model.MetadataSyncJob getAndValidateDomainJob(String jobId) {
        return metadataSyncJobRepository.findById(jobId)
                .orElseThrow(() -> new InsightDataException("同步作业不存在: " + jobId));
    }
    
    /**
     * 获取并验证同步作业是否存在（Facade模型）
     *
     * @param jobId 同步作业ID
     * @return Facade模型同步作业
     * @throws InsightDataException 如果同步作业不存在
     */
    private MetadataSyncJob getAndValidateJob(String jobId) {
        com.insightdata.domain.metadata.model.MetadataSyncJob domainJob = getAndValidateDomainJob(jobId);
        return EnumConverter.toFacadeMetadataSyncJob(domainJob);
    }
}