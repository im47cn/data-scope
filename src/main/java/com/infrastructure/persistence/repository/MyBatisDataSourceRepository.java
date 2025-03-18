package com.infrastructure.persistence.repository;

import com.common.enums.DataSourceType;
import com.domain.model.metadata.DataSource;
import com.domain.repository.DataSourceRepository;
import com.infrastructure.persistence.mapper.DataSourceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * MyBatis实现的数据源仓储
 */
@Slf4j
@Repository
public class MyBatisDataSourceRepository implements DataSourceRepository {

    @Autowired
    private DataSourceMapper dataSourceMapper;
    
    public MyBatisDataSourceRepository(DataSourceMapper dataSourceMapper) {
        this.dataSourceMapper = dataSourceMapper;
    }
    
    @Override
    public DataSource save(DataSource dataSource) {
        LocalDateTime now = LocalDateTime.now();
        
        // 查看是否已存在该ID的数据源
        boolean exists = false;
        if (dataSource.getId() != null) {
            DataSource existing = dataSourceMapper.selectById(dataSource.getId());
            exists = (existing != null);
        }
        
        if (!exists) {
            // 新数据源
            dataSource.setCreatedAt(now);
            dataSource.setUpdatedAt(now);
            dataSourceMapper.insert(dataSource);
        } else {
            // 更新
            dataSource.setUpdatedAt(now);
            dataSourceMapper.update(dataSource);
        }
        
        return dataSource;
    }
    
    @Override
    public Optional<DataSource> findById(String id) {
        return Optional.ofNullable(dataSourceMapper.selectById(id));
    }
    
    @Override
    public Optional<DataSource> findByName(String name) {
        return Optional.ofNullable(dataSourceMapper.selectByName(name));
    }
    
    @Override
    public List<DataSource> findAll() {
        return dataSourceMapper.selectAll();
    }
    
    @Override
    public List<DataSource> findByType(DataSourceType type) {
        return dataSourceMapper.selectByType(type);
    }
    
    @Override
    public List<DataSource> findByActive(boolean active) {
        return dataSourceMapper.selectByActive(active);
    }
    
    @Override
    public void deleteById(String id) {
        dataSourceMapper.deleteById(id);
    }
    
    @Override
    public boolean existsByName(String name) {
        return dataSourceMapper.countByName(name) > 0;
    }
}