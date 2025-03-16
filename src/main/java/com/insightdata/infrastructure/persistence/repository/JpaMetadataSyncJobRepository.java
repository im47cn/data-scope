package com.insightdata.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.insightdata.common.enums.SyncStatus;
import com.insightdata.infrastructure.persistence.entity.MetadataSyncJobEntity;

/**
 * 元数据同步作业JPA仓储接口
 */
@Repository
public interface JpaMetadataSyncJobRepository extends JpaRepository<MetadataSyncJobEntity, String> {
    
    /**
     * 根据数据源ID查询同步作业
     *
     * @param dataSourceId 数据源ID
     * @return 同步作业列表
     */
    List<MetadataSyncJobEntity> findByDataSourceId(Long dataSourceId);
    
    /**
     * 根据数据源ID和状态查询同步作业
     *
     * @param dataSourceId 数据源ID
     * @param status 同步状态
     * @return 同步作业列表
     */
    List<MetadataSyncJobEntity> findByDataSourceIdAndStatus(Long dataSourceId, SyncStatus status);
    
    /**
     * 根据状态查询同步作业
     *
     * @param status 同步状态
     * @return 同步作业列表
     */
    List<MetadataSyncJobEntity> findByStatus(SyncStatus status);
    
    /**
     * 获取数据源最近的同步作业
     *
     * @param dataSourceId 数据源ID
     * @return 同步作业
     */
    @Query("SELECT j FROM MetadataSyncJobEntity j WHERE j.dataSourceId = :dataSourceId ORDER BY j.createdAt DESC")
    List<MetadataSyncJobEntity> findByDataSourceIdOrderByCreatedAtDesc(@Param("dataSourceId") Long dataSourceId);
    
    /**
     * 删除数据源的所有同步作业
     *
     * @param dataSourceId 数据源ID
     */
    void deleteByDataSourceId(Long dataSourceId);
}