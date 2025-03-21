package com.insightdata.application.service.impl;

import java.lang.reflect.Field;
import java.util.List;

import com.insightdata.domain.datasource.model.DataSource;
import com.insightdata.domain.metadata.model.SchemaInfo;
import com.insightdata.domain.metadata.model.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.insightdata.application.service.MetadataSyncApplicationService;
import com.insightdata.application.service.MetadataSyncJobApplicationService;
import com.insightdata.domain.adapter.DataSourceAdapterFactory;
import com.insightdata.domain.adapter.EnhancedDataSourceAdapter;
import com.insightdata.domain.metadata.model.MetadataSyncJob;
import com.insightdata.domain.datasource.repository.DataSourceRepository;
import com.insightdata.domain.metadata.repository.SchemaInfoRepository;
import com.insightdata.domain.metadata.repository.TableInfoRepository;
import com.insightdata.facade.metadata.enums.SyncType;

@Slf4j
@Service
public class MetadataSyncApplicationServiceImpl implements MetadataSyncApplicationService {

    private final MetadataSyncJobApplicationService metadataSyncJobService;
    private final DataSourceAdapterFactory dataSourceAdapterFactory;
    private final DataSourceRepository dataSourceRepository;
    private final SchemaInfoRepository schemaInfoRepository;
    private final TableInfoRepository tableInfoRepository;

    @Autowired
    public MetadataSyncApplicationServiceImpl(
            MetadataSyncJobApplicationService metadataSyncJobService,
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
        
        // 使用反射获取ID
        String jobId = getFieldValue(syncJob, "id");
        metadataSyncJobService.startSyncJob(jobId);

        try {
            // 获取数据源实体
            DataSource dataSourceEntity = dataSourceRepository.findById(dataSourceId)
                    .orElseThrow(() -> new RuntimeException("数据源不存在"));

            // 获取数据源适配器
            EnhancedDataSourceAdapter dataSourceAdapter = dataSourceAdapterFactory.getEnhancedAdapter(dataSourceEntity);

            // 测试连接
            dataSourceAdapter.testConnection(dataSourceEntity);

            // 同步schema和表信息
            syncSchemas(dataSourceAdapter, dataSourceEntity, syncJob);

            // 更新同步状态
            metadataSyncJobService.updateProgress(jobId, 100, "元数据同步完成");
            metadataSyncJobService.completeSyncJob(jobId);
        } catch (Exception e) {
            log.error("元数据同步失败", e);
            // 使用已定义的jobId变量
            metadataSyncJobService.failSyncJob(jobId, e.getMessage());
            throw new RuntimeException("元数据同步失败", e);
        }
    }

    private void syncSchemas(EnhancedDataSourceAdapter dataSourceAdapter, DataSource dataSource, MetadataSyncJob syncJob)
            throws Exception {
        log.info("开始同步schema信息");

        // 获取所有schema（通过适配器实现）
        List<SchemaInfo> schemaInfos = dataSourceAdapter.getSchemas(dataSource);

        for (SchemaInfo schemaInfo : schemaInfos) {
            // 使用反射设置字段
            String dataSourceId = getFieldValue(dataSource, "id");
            setFieldValue(schemaInfo, "dataSourceId", dataSourceId);
            
            // 保存schema信息
            schemaInfoRepository.save(schemaInfo);
            syncTables(dataSourceAdapter, dataSource, schemaInfo, syncJob);
        }
    }

    private void syncTables(EnhancedDataSourceAdapter dataSourceAdapter, DataSource dataSource, SchemaInfo schemaInfo, MetadataSyncJob syncJob)
            throws Exception {
        log.info("开始同步表信息: schema={}", schemaInfo.getName());

        // 获取表信息（通过适配器实现）
        List<TableInfo> tableInfos = dataSourceAdapter.getTables(dataSource, schemaInfo.getName());
        for (TableInfo tableInfo : tableInfos) {
            // 使用反射设置字段
            setFieldValue(tableInfo, "schemaName", schemaInfo.getName());
            
            // 保存表信息
            tableInfoRepository.save(tableInfo);
        }
    }
    
    // 使用反射获取私有字段的值
    @SuppressWarnings("unchecked")
    private <T> T getFieldValue(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (Exception e) {
            log.error("获取字段值失败: {}", fieldName, e);
            throw new RuntimeException("获取字段值失败: " + fieldName, e);
        }
    }
    
    // 使用反射设置私有字段的值
    private void setFieldValue(Object obj, String fieldName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            log.error("设置字段值失败: {}", fieldName, e);
            throw new RuntimeException("设置字段值失败: " + fieldName, e);
        }
    }
}