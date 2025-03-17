package com.infrastructure.persistence.repository;

import com.common.enums.DataSourceType;
import com.domain.model.DataSource;
import com.domain.repository.DataSourceRepository;
import com.infrastructure.persistence.mapper.DataSourceMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * MyBatis实现的数据源仓储
 */
@Repository
public class MyBatisDataSourceRepository implements DataSourceRepository {
    
    private final DataSourceMapper dataSourceMapper;
    
    public MyBatisDataSourceRepository(DataSourceMapper dataSourceMapper) {
        this.dataSourceMapper = dataSourceMapper;
    }
    
    @Override
    public DataSource save(DataSource dataSource) {
        if (dataSource.getId() == null) {
            // 新建数据源
            dataSource.setCreatedAt(LocalDateTime.now());
            dataSource.setUpdatedAt(LocalDateTime.now());
            dataSourceMapper.insert(dataSource);
        } else {
            // 更新数据源
            dataSource.setUpdatedAt(LocalDateTime.now());
            dataSourceMapper.update(dataSource);
        }
        return dataSource;
    }
    
    @Override
    public Optional<DataSource> findById(Long id) {
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
    public void deleteById(Long id) {
        dataSourceMapper.deleteById(id);
    }
    
    @Override
    public boolean existsByName(String name) {
        return dataSourceMapper.countByName(name) > 0;
    }
}