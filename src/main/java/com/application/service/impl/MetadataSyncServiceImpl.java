package com.application.service.impl;

import com.application.service.MetadataSyncJobService;
import com.application.service.MetadataSyncService;
import com.common.enums.SyncType;
import com.domain.model.metadata.MetadataSyncJob;
import com.domain.model.metadata.SchemaInfo;
import com.domain.model.metadata.TableInfo;
import com.domain.repository.DataSourceRepository;
import com.domain.repository.SchemaInfoRepository;
import com.domain.repository.TableInfoRepository;
import com.infrastructure.adapter.DataSourceAdapter;
import com.infrastructure.adapter.DataSourceAdapterFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class MetadataSyncServiceImpl implements MetadataSyncService {

    @Autowired
    private MetadataSyncJobService metadataSyncJobService;

    @Autowired
    private DataSourceAdapterFactory dataSourceAdapterFactory;

    @Autowired
    private DataSourceRepository dataSourceRepository;

    @Autowired
    private SchemaInfoRepository schemaInfoRepository;

    @Autowired
    private TableInfoRepository tableInfoRepository;

    @Autowired
    public MetadataSyncServiceImpl(
            MetadataSyncJobService metadataSyncJobService,
            DataSourceAdapterFactory dataSourceAdapterFactory,
            DataSourceRepository dataSourceRepository,
            SchemaInfoRepository schemaInfoRepository,
            TableInfoRepository tableInfoRepository) {
        this.metadataSyncJobService = metadataSyncJobService;
        this.dataSourceAdapterFactory = dataSourceAdapterFactory;
        this.dataSourceRepository = dataSourceRepository;
        this.schemaInfoRepository = schemaInfoRepository;
        this.tableInfoRepository = tableInfoRepository;
    }

    @Override
    @Transactional
    public void syncMetadata(String dataSourceId) {
        log.info("开始同步元数据: dataSourceId={}", dataSourceId);

        // 创建同步作业
        MetadataSyncJob syncJob = metadataSyncJobService.createSyncJob(dataSourceId, SyncType.FULL);
        metadataSyncJobService.startSyncJob(syncJob.getId());

        try {
            // 获取数据源实体
            com.domain.model.DataSource dataSourceEntity = dataSourceRepository.findById(dataSourceId)
                    .orElseThrow(() -> new RuntimeException("数据源不存在"));

            // 获取数据源适配器
            DataSourceAdapter dataSourceAdapter = dataSourceAdapterFactory.getAdapter(dataSourceEntity.getType());

            // 测试连接
            dataSourceAdapter.testConnection(dataSourceEntity);

            // 同步schema和表信息
            syncSchemas(dataSourceAdapter, dataSourceEntity, syncJob);

            // 更新同步状态
            metadataSyncJobService.updateProgress(syncJob.getId(), 100, "元数据同步完成");
            metadataSyncJobService.completeSyncJob(syncJob.getId());
        } catch (Exception e) {
            log.error("元数据同步失败", e);
            metadataSyncJobService.failSyncJob(syncJob.getId(), e.getMessage());
            throw new RuntimeException("元数据同步失败", e);
        }
    }

    private void syncSchemas(DataSourceAdapter dataSourceAdapter, com.domain.model.DataSource dataSource, MetadataSyncJob syncJob)
            throws SQLException {
        log.info("开始同步schema信息");

        // 获取所有schema（通过适配器实现）
        List<SchemaInfo> schemaInfos = dataSourceAdapter.getSchemas(dataSource);

        for (SchemaInfo schemaInfo : schemaInfos) {
            schemaInfo.setDataSourceId(dataSource.getId());
            schemaInfo.setCreatedAt(LocalDateTime.now());
            schemaInfo.setUpdatedAt(LocalDateTime.now());
            schemaInfoRepository.save(schemaInfo);
            syncTables(dataSourceAdapter, dataSource, schemaInfo, syncJob);
        }
    }

    private void syncTables(DataSourceAdapter dataSourceAdapter, com.domain.model.DataSource dataSource, SchemaInfo schemaInfo, MetadataSyncJob syncJob)
            throws SQLException {
        log.info("开始同步表信息: schema={}", schemaInfo.getName());

        // 获取表信息（通过适配器实现）
        List<TableInfo> tableInfos = dataSourceAdapter.getTables(dataSource, schemaInfo.getName());
        for (TableInfo tableInfo : tableInfos) {
            tableInfo.setSchemaName(schemaInfo.getName());
            tableInfo.setDataSourceId(dataSource.getId());
            tableInfo.setCreatedAt(LocalDateTime.now());
            tableInfo.setUpdatedAt(LocalDateTime.now());
            tableInfoRepository.save(tableInfo);
        }
    }
}