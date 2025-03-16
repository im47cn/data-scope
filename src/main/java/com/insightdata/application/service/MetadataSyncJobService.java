package com.insightdata.application.service;

import com.insightdata.common.enums.SyncStatus;
import com.insightdata.common.enums.SyncType;
import com.insightdata.domain.model.metadata.MetadataSyncJob;

import java.util.List;
import java.util.Optional;

/**
 * 元数据同步作业服务接口
 */
public interface MetadataSyncJobService {
    
    /**
     * 创建同步作业
     * 
     * @param dataSourceId 数据源ID
     * @param type 同步类型
     * @return 创建的同步作业
     */
    MetadataSyncJob createSyncJob(Long dataSourceId, SyncType type);
    
    /**
     * 启动同步作业
     * 
     * @param jobId 同步作业ID
     * @return 启动后的同步作业
     */
    MetadataSyncJob startSyncJob(String jobId);
    
    /**
     * 更新同步作业进度
     * 
     * @param jobId 同步作业ID
     * @param progress 进度值
     * @param message 进度消息
     * @return 更新后的同步作业
     */
    MetadataSyncJob updateProgress(String jobId, int progress, String message);
    
    /**
     * 完成同步作业
     * 
     * @param jobId 同步作业ID
     * @return 更新后的同步作业
     */
    MetadataSyncJob completeSyncJob(String jobId);
    
    /**
     * 标记同步作业为失败
     * 
     * @param jobId 同步作业ID
     * @param errorMessage 错误信息
     * @return 更新后的同步作业
     */
    MetadataSyncJob failSyncJob(String jobId, String errorMessage);
    
    /**
     * 取消同步作业
     * 
     * @param jobId 同步作业ID
     * @return 更新后的同步作业
     */
    MetadataSyncJob cancelSyncJob(String jobId);
    
    /**
     * 获取同步作业详情
     * 
     * @param jobId 同步作业ID
     * @return 同步作业
     */
    Optional<MetadataSyncJob> getSyncJob(String jobId);
    
    /**
     * 获取数据源的同步作业列表
     * 
     * @param dataSourceId 数据源ID
     * @return 同步作业列表
     */
    List<MetadataSyncJob> getSyncJobsByDataSource(Long dataSourceId);
    
    /**
     * 获取数据源的最新同步作业
     * 
     * @param dataSourceId 数据源ID
     * @return 最新同步作业
     */
    Optional<MetadataSyncJob> getLatestSyncJob(Long dataSourceId);
    
    /**
     * 获取指定状态的同步作业列表
     * 
     * @param status 同步状态
     * @return 同步作业列表
     */
    List<MetadataSyncJob> getSyncJobsByStatus(SyncStatus status);
    
    /**
     * 获取正在进行中的同步作业列表
     * 
     * @return 同步作业列表
     */
    List<MetadataSyncJob> getInProgressSyncJobs();
}