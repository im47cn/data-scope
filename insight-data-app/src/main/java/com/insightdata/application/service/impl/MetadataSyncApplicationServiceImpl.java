package com.insightdata.application.service.impl;

import com.insightdata.application.service.MetadataSyncJobApplicationService;
import com.insightdata.application.service.MetadataSyncApplicationService;
import com.insightdata.domain.adapter.DataSourceAdapter;
import com.insightdata.domain.adapter.DataSourceAdapterFactory;
import com.insightdata.domain.metadata.model.DataSource;
import com.insightdata.domain.metadata.model.MetadataSyncJob;
import com.insightdata.domain.metadata.model.SchemaInfo;
import com.insightdata.domain.metadata.model.TableInfo;
import com.insightdata.domain.repository.DataSourceRepository;
import com.insightdata.domain.repository.SchemaInfoRepository;
import com.insightdata.domain.repository.TableInfoRepository;
import com.insightdata.facade.metadata.enums.SyncType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class MetadataSyncApplicationServiceImpl implements MetadataSyncApplicationService {

    @Autowired
    private MetadataSyncJobApplicationService metadataSyncJobApplicationService;

    @Autowired
    private DataSourceAdapterFactory dataSourceAdapterFactory;

    @Autowired
    private DataSourceRepository dataSourceRepository;

    @Autowired
    private SchemaInfoRepository schemaInfoRepository;

    @Autowired
    private TableInfoRepository tableInfoRepository;

    @Autowired
    public MetadataSyncApplicationServiceImpl(
            MetadataSyncJobApplicationService metadataSyncJobApplicationService,
            DataSourceAdapterFactory dataSourceAdapterFactory,
            DataSourceRepository dataSourceRepository,
            SchemaInfoRepository schemaInfoRepository,
            TableInfoRepository tableInfoRepository) {
        this.metadataSyncJobApplicationService = metadataSyncJobApplicationService;
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
        MetadataSyncJob syncJob = metadataSyncJobApplicationService.createSyncJob(dataSourceId, SyncType.FULL);
        metadataSyncJobApplicationService.startSyncJob(syncJob.getId());

        try {
            // 获取数据源实体
            DataSource dataSourceEntity = dataSourceRepository.findById(dataSourceId)
                    .orElseThrow(() -> new RuntimeException("数据源不存在"));

            // 获取数据源适配器
            DataSourceAdapter dataSourceAdapter = dataSourceAdapterFactory.getAdapter(dataSourceEntity.getType());

            // 测试连接
            dataSourceAdapter.testConnection(dataSourceEntity);

            // 同步schema和表信息
            syncSchemas(dataSourceAdapter, dataSourceEntity, syncJob);

            // 更新同步状态
            metadataSyncJobApplicationService.updateProgress(syncJob.getId(), 100, "元数据同步完成");
            metadataSyncJobApplicationService.completeSyncJob(syncJob.getId());
        } catch (Exception e) {
            log.error("元数据同步失败", e);
            metadataSyncJobApplicationService.failSyncJob(syncJob.getId(), e.getMessage());
            throw new RuntimeException("元数据同步失败", e);
        }
    }

    private void syncSchemas(DataSourceAdapter dataSourceAdapter, DataSource dataSource, MetadataSyncJob syncJob)
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

    private void syncTables(DataSourceAdapter dataSourceAdapter, DataSource dataSource, SchemaInfo schemaInfo, MetadataSyncJob syncJob)
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