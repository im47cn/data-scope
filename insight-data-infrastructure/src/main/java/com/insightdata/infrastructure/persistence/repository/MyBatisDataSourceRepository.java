package com.insightdata.infrastructure.persistence.repository;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import com.insightdata.domain.metadata.enums.DataSourceType;
import com.insightdata.domain.metadata.model.DataSource;
import com.insightdata.domain.metadata.repository.DataSourceRepository;
import com.insightdata.infrastructure.persistence.mapper.DataSourceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 数据源仓储MyBatis实现类
 */
@Slf4j
@Repository
public class MyBatisDataSourceRepository implements DataSourceRepository {

    @Autowired
    private DataSourceMapper dataSourceMapper;

    @Override
    public DataSource save(DataSource dataSource) {
        if (dataSource == null) {
            throw new IllegalArgumentException("数据源不能为空");
        }
        
        // 使用反射获取id值
        String id = getFieldValue(dataSource, "id");
        boolean exists = false;
        
        if (id != null && !id.isEmpty()) {
            DataSource existing = dataSourceMapper.selectById(id);
            exists = (existing != null);
        }
        
        if (!exists) {
            // 新增数据源
            dataSourceMapper.insert(dataSource);
        } else {
            // 更新数据源
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
    
    /**
     * 使用反射获取对象的字段值
     *
     * @param obj 对象
     * @param fieldName 字段名
     * @return 字段值，如果发生异常则返回null
     */
    @SuppressWarnings("unchecked")
    private <T> T getFieldValue(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (Exception e) {
            log.error("反射获取字段值失败: " + fieldName, e);
            return null;
        }
    }
}