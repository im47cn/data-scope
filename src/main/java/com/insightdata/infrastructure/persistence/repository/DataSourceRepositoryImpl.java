package com.insightdata.infrastructure.persistence.repository;

import com.insightdata.common.enums.DataSourceType;
import com.insightdata.domain.model.DataSource;
import com.insightdata.domain.repository.DataSourceRepository;
import com.insightdata.infrastructure.persistence.entity.DataSourceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 数据源仓储实现类
 */
@Repository
public class DataSourceRepositoryImpl implements DataSourceRepository {
    
    private final JpaDataSourceRepository jpaDataSourceRepository;
    
    @Autowired
    public DataSourceRepositoryImpl(JpaDataSourceRepository jpaDataSourceRepository) {
        this.jpaDataSourceRepository = jpaDataSourceRepository;
    }
    
    @Override
    public DataSource save(DataSource dataSource) {
        DataSourceEntity entity = toEntity(dataSource);
        
        // 设置创建/更新时间
        LocalDateTime now = LocalDateTime.now();
        if (entity.getId() == null) {
            entity.setCreatedAt(now);
        }
        entity.setUpdatedAt(now);
        
        // 保存实体
        DataSourceEntity savedEntity = jpaDataSourceRepository.save(entity);
        
        // 转换回领域模型
        return toDomain(savedEntity);
    }
    
    @Override
    public Optional<DataSource> findById(Long id) {
        return jpaDataSourceRepository.findById(id)
                .map(this::toDomain);
    }
    
    @Override
    public Optional<DataSource> findByName(String name) {
        return jpaDataSourceRepository.findByName(name)
                .map(this::toDomain);
    }
    
    @Override
    public List<DataSource> findAll() {
        return jpaDataSourceRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<DataSource> findByType(DataSourceType type) {
        return jpaDataSourceRepository.findByType(type).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<DataSource> findByActive(boolean active) {
        return jpaDataSourceRepository.findByActive(active).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        jpaDataSourceRepository.deleteById(id);
    }
    
    @Override
    public boolean existsByName(String name) {
        return jpaDataSourceRepository.existsByName(name);
    }
    
    /**
     * 将领域模型转换为实体
     */
    private DataSourceEntity toEntity(DataSource dataSource) {
        if (dataSource == null) {
            return null;
        }
        
        return DataSourceEntity.builder()
                .id(dataSource.getId())
                .name(dataSource.getName())
                .type(dataSource.getType())
                .host(dataSource.getHost())
                .port(dataSource.getPort())
                .databaseName(dataSource.getDatabaseName())
                .username(dataSource.getUsername())
                .encryptedPassword(dataSource.getEncryptedPassword())
                .encryptionSalt(dataSource.getEncryptionSalt())
                .connectionProperties(dataSource.getConnectionProperties())
                .lastSyncTime(dataSource.getLastSyncTime())
                .active(dataSource.isActive())
                .description(dataSource.getDescription())
                .createdAt(dataSource.getCreatedAt())
                .updatedAt(dataSource.getUpdatedAt())
                .build();
    }
    
    /**
     * 将实体转换为领域模型
     */
    private DataSource toDomain(DataSourceEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return DataSource.builder()
                .id(entity.getId())
                .name(entity.getName())
                .type(entity.getType())
                .host(entity.getHost())
                .port(entity.getPort())
                .databaseName(entity.getDatabaseName())
                .username(entity.getUsername())
                .encryptedPassword(entity.getEncryptedPassword())
                .encryptionSalt(entity.getEncryptionSalt())
                .connectionProperties(entity.getConnectionProperties())
                .lastSyncTime(entity.getLastSyncTime())
                .active(entity.getActive())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}