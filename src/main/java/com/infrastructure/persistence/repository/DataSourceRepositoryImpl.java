package com.infrastructure.persistence.repository;

import com.common.enums.DataSourceType;
import com.domain.model.DataSource;
import com.domain.repository.DataSourceRepository;
import com.infrastructure.persistence.mapper.DataSourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 数据源仓储实现类 - MyBatis实现
 * @deprecated 请直接使用 {@link MyBatisDataSourceRepository}
 */
@Repository
@Deprecated
public class DataSourceRepositoryImpl implements DataSourceRepository {
    
    private final MyBatisDataSourceRepository repository;
    
    @Autowired
    public DataSourceRepositoryImpl(DataSourceMapper dataSourceMapper) {
        this.repository = new MyBatisDataSourceRepository(dataSourceMapper);
    }
    
    @Override
    public DataSource save(DataSource dataSource) {
        return repository.save(dataSource);
    }
    
    @Override
    public Optional<DataSource> findById(Long id) {
        return repository.findById(id);
    }
    
    @Override
    public Optional<DataSource> findByName(String name) {
        return repository.findByName(name);
    }
    
    @Override
    public List<DataSource> findAll() {
        return repository.findAll();
    }
    
    @Override
    public List<DataSource> findByType(DataSourceType type) {
        return repository.findByType(type);
    }
    
    @Override
    public List<DataSource> findByActive(boolean active) {
        return repository.findByActive(active);
    }
    
    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
    
    @Override
    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }
}