package com.domain.service.impl;

import com.common.enums.DataSourceType;
import com.common.exception.DataSourceException;
import com.domain.model.DataSource;
import com.domain.model.metadata.SchemaInfo;
import com.domain.model.metadata.TableInfo;
import com.domain.repository.DataSourceRepository;
import com.domain.service.CredentialEncryptionService;
import com.domain.service.DataSourceService;
import com.infrastructure.adapter.DataSourceAdapter;
import com.infrastructure.adapter.DataSourceAdapterFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 数据源服务实现类
 */
@Slf4j
@Service
public class DataSourceServiceImpl implements DataSourceService {
    
    private final DataSourceRepository dataSourceRepository;
    private final DataSourceAdapterFactory adapterFactory;
    private final CredentialEncryptionService encryptionService;
    
    @Autowired
    public DataSourceServiceImpl(
            DataSourceRepository dataSourceRepository,
            DataSourceAdapterFactory adapterFactory,
            CredentialEncryptionService encryptionService) {
        this.dataSourceRepository = dataSourceRepository;
        this.adapterFactory = adapterFactory;
        this.encryptionService = encryptionService;
    }
    
    @Override
    @Transactional
    public DataSource createDataSource(DataSource dataSource) {
        // 检查名称是否已存在
        if (dataSourceRepository.existsByName(dataSource.getName())) {
            throw DataSourceException.alreadyExists("Data source with name '" + dataSource.getName() + "' already exists");
        }
        
        // 加密密码
        if (dataSource.getEncryptedPassword() == null) {
            CredentialEncryptionService.EncryptionResult result = encryptionService.encrypt(dataSource.getPassword());
            dataSource.setEncryptedPassword(result.getEncryptedPassword());
            dataSource.setEncryptionSalt(result.getSalt());
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
        
        // 如果提供了新密码，则加密
        if (dataSource.getPassword() != null && !dataSource.getPassword().isEmpty()) {
            CredentialEncryptionService.EncryptionResult result = encryptionService.encrypt(dataSource.getPassword());
            dataSource.setEncryptedPassword(result.getEncryptedPassword());
            dataSource.setEncryptionSalt(result.getSalt());
        } else {
            // 保留原密码
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
            DataSourceAdapter adapter = adapterFactory.getAdapter(dataSource.getType());
            
            // 测试连接
            return adapter.testConnection(dataSource);
        } catch (Exception e) {
            log.error("Failed to test connection to data source: {}", dataSource.getName(), e);
            return false;
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SchemaInfo> getSchemas(String dataSourceId) {
        // 获取数据源
        DataSource dataSource = dataSourceRepository.findById(dataSourceId)
                .orElseThrow(() -> DataSourceException.notFound("Data source with ID " + dataSourceId + " not found"));
        
        // 获取适配器
        DataSourceAdapter adapter = adapterFactory.getAdapter(dataSource.getType());
        
        // 获取模式列表
        return adapter.getSchemas(dataSource);
    }

    @Override
    public SchemaInfo getSchemaInfo(String dataSourceId, String schemaName) {
        // 获取数据源
        DataSource dataSource = dataSourceRepository.findById(dataSourceId)
                .orElseThrow(() -> DataSourceException.notFound("Data source with ID " + dataSourceId + " not found"));

        // 获取适配器
        DataSourceAdapter adapter = adapterFactory.getAdapter(dataSource.getType());

        // 获取模式列表
        return adapter.getSchema(dataSource, schemaName);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TableInfo> getTables(String dataSourceId, String schemaName) {
        // 获取数据源
        DataSource dataSource = dataSourceRepository.findById(dataSourceId)
                .orElseThrow(() -> DataSourceException.notFound("Data source with ID " + dataSourceId + " not found"));
        
        // 获取适配器
        DataSourceAdapter adapter = adapterFactory.getAdapter(dataSource.getType());
        
        // 获取表列表
        return adapter.getTables(dataSource, schemaName);
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
                .collect(Collectors.toList());
    }
}