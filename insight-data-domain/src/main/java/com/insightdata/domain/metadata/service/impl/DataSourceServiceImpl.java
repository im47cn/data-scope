package com.insightdata.domain.metadata.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.insightdata.domain.adapter.DataSourceAdapter;
import com.insightdata.domain.adapter.DataSourceAdapterFactory;
import com.insightdata.domain.adapter.DataSourceAdapterHelper;
import com.insightdata.domain.adapter.EnhancedDataSourceAdapter;
import com.insightdata.domain.exception.DataSourceException;
import com.insightdata.domain.metadata.enums.DataSourceType;
import com.insightdata.domain.metadata.model.DataSource;
import com.insightdata.domain.metadata.model.SchemaInfo;
import com.insightdata.domain.metadata.model.TableInfo;
import com.insightdata.domain.metadata.repository.DataSourceRepository;
import com.insightdata.domain.metadata.service.CredentialEncryptionService;
import com.insightdata.domain.metadata.service.DataSourceService;
import com.insightdata.domain.security.model.KeyInfo;
import com.insightdata.domain.security.service.KeyManagementService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据源服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DataSourceServiceImpl implements DataSourceService {

    @Autowired
    private DataSourceRepository dataSourceRepository;

    @Autowired
    private DataSourceAdapterFactory adapterFactory;

    @Autowired
    private CredentialEncryptionService encryptionService;

    @Autowired
    private KeyManagementService keyManagementService;


    @Override
    @Transactional
    public DataSource createDataSource(DataSource dataSource) {
        // 检查名称是否已存在
        if (dataSourceRepository.existsByName(dataSource.getName())) {
            throw DataSourceException.alreadyExists("Data source with name '" + dataSource.getName() + "' already exists");
        }

        // 创建密钥
        KeyInfo keyInfo = keyManagementService.createKey("datasource-credentials");
        dataSource.setKeyId(keyInfo.getId());

        // 加密密码
        if (dataSource.getPassword() != null && !dataSource.getPassword().isEmpty()) {
            CredentialEncryptionService.EncryptionResult result = encryptionService.encrypt(
                    dataSource.getPassword()
            );
            dataSource.setEncryptedPassword(result.getEncryptedPassword());
            dataSource.setEncryptionSalt(result.getSalt());
            // 清除明文密码
            dataSource.setPassword(null);
        }

        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        dataSource.setCreatedAt(now);
        dataSource.setUpdatedAt(now);

        // 保存数据源
        return dataSourceRepository.save(dataSource);
    }

    @Override
    @Transactional
    public DataSource updateDataSource(DataSource dataSource) {
        // 检查数据源是否存在
        DataSource existingDataSource = dataSourceRepository.findById(dataSource.getId())
                .orElseThrow(() -> DataSourceException.notFound("Data source with ID " + dataSource.getId() + " not found"));

        // 检查名称是否已被其他数据源使用
        Optional<DataSource> dataSourceWithSameName = dataSourceRepository.findByName(dataSource.getName());
        if (dataSourceWithSameName.isPresent() && !dataSourceWithSameName.get().getId().equals(dataSource.getId())) {
            throw DataSourceException.alreadyExists("Data source with name '" + dataSource.getName() + "' already exists");
        }

        // 如果提供了新密码，则重新加密
        if (dataSource.getPassword() != null && !dataSource.getPassword().isEmpty()) {
            // 重用现有密钥或创建新密钥
            String keyId = existingDataSource.getKeyId();
            if (keyId == null) {
                KeyInfo keyInfo = keyManagementService.createKey("datasource-credentials");
                keyId = keyInfo.getId();
                dataSource.setKeyId(keyId);
            } else {
                dataSource.setKeyId(keyId);
            }

            // 使用密钥加密密码
            CredentialEncryptionService.EncryptionResult result = encryptionService.encrypt(
                    dataSource.getPassword()
            );
            dataSource.setEncryptedPassword(result.getEncryptedPassword());
            dataSource.setEncryptionSalt(result.getSalt());
            // 清除明文密码
            dataSource.setPassword(null);
        } else {
            // 保留原密码和密钥
            dataSource.setKeyId(existingDataSource.getKeyId());
            dataSource.setEncryptedPassword(existingDataSource.getEncryptedPassword());
            dataSource.setEncryptionSalt(existingDataSource.getEncryptionSalt());
        }

        // 设置更新时间
        dataSource.setUpdatedAt(LocalDateTime.now());

        // 保留创建时间
        dataSource.setCreatedAt(existingDataSource.getCreatedAt());

        // 保存数据源
        return dataSourceRepository.save(dataSource);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DataSource> getDataSourceById(String id) {
        return dataSourceRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<DataSource> getDataSourceByName(String name) {
        return dataSourceRepository.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataSource> getAllDataSources() {
        return dataSourceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataSource> getDataSourcesByType(DataSourceType type) {
        return dataSourceRepository.findByType(type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataSource> getDataSourcesByActive(boolean active) {
        return dataSourceRepository.findByActive(active);
    }

    @Override
    @Transactional
    public void deleteDataSource(String id) {
        // 检查数据源是否存在
        if (!dataSourceRepository.findById(id).isPresent()) {
            throw DataSourceException.notFound("Data source with ID " + id + " not found");
        }

        // 删除数据源
        dataSourceRepository.deleteById(id);
    }

    @Override
    public boolean testConnection(DataSource dataSource) {
        try {
            // 获取适配器
            DataSourceAdapter adapter = adapterFactory.getAdapter(DataSourceAdapterHelper.getType(dataSource));

            // 测试连接
            return adapter.testConnection(dataSource);
        } catch (Exception e) {
            log.error("Failed to test connection to data source: {}", DataSourceAdapterHelper.getName(dataSource), e);
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SchemaInfo> getSchemas(String dataSourceId) {
        try {
            // 获取数据源
            DataSource dataSource = dataSourceRepository.findById(dataSourceId)
                    .orElseThrow(() -> DataSourceException.notFound("Data source with ID " + dataSourceId + " not found"));

            // 获取增强型适配器
            EnhancedDataSourceAdapter adapter = adapterFactory.getEnhancedAdapter(DataSourceAdapterHelper.getType(dataSource));

            // 获取模式列表
            return adapter.getSchemas(dataSource);
        } catch (Exception e) {
            log.error("Failed to get schemas for data source: {}", dataSourceId, e);
            throw new DataSourceException("Failed to get schemas: " + e.getMessage(), e);
        }
    }

    @Override
    public SchemaInfo getSchemaInfo(String dataSourceId, String schemaName) {
        try {
            // 获取数据源
            DataSource dataSource = dataSourceRepository.findById(dataSourceId)
                    .orElseThrow(() -> DataSourceException.notFound("Data source with ID " + dataSourceId + " not found"));

            // 获取增强型适配器
            EnhancedDataSourceAdapter adapter = adapterFactory.getEnhancedAdapter(DataSourceAdapterHelper.getType(dataSource));

            // 获取模式详情
            return adapter.getSchema(dataSource, schemaName);
        } catch (Exception e) {
            log.error("Failed to get schema info for data source: {}, schema: {}", dataSourceId, schemaName, e);
            throw new DataSourceException("Failed to get schema info: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<TableInfo> getTables(String dataSourceId, String schemaName) {
        try {
            // 获取数据源
            DataSource dataSource = dataSourceRepository.findById(dataSourceId)
                    .orElseThrow(() -> DataSourceException.notFound("Data source with ID " + dataSourceId + " not found"));

            // 获取增强型适配器
            EnhancedDataSourceAdapter adapter = adapterFactory.getEnhancedAdapter(DataSourceAdapterHelper.getType(dataSource));

            // 获取表列表
            return adapter.getTables(dataSource, schemaName);
        } catch (Exception e) {
            log.error("Failed to get tables for data source: {}, schema: {}", dataSourceId, schemaName, e);
            throw new DataSourceException("Failed to get tables: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public String syncMetadata(String dataSourceId) {
        // 获取数据源
        DataSource dataSource = dataSourceRepository.findById(dataSourceId)
                .orElseThrow(() -> DataSourceException.notFound("Data source with ID " + dataSourceId + " not found"));

        // 创建同步作业ID
        String syncJobId = UUID.randomUUID().toString();

        // TODO: 创建同步作业并启动异步任务

        // 更新数据源的最后同步时间
        dataSource.setLastSyncTime(LocalDateTime.now());
        dataSourceRepository.save(dataSource);

        return syncJobId;
    }

    @Transactional(readOnly = true)
    @Override
    public List<DataSourceType> getSupportedTypes() {
        return Arrays.stream(DataSourceType.values())
                .filter(type -> adapterFactory.supportsType(type))
                .collect(Collectors.toList());
    }
}