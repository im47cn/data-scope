package com.insightdata.domain.repository;

import com.insightdata.domain.metadata.enums.DataSourceType;
import com.insightdata.domain.metadata.model.DataSource;

import java.util.List;
import java.util.Optional;

/**
 * 数据源仓储接口
 */
public interface DataSourceRepository {
    
    /**
     * 保存数据源
     *
     * @param dataSource 数据源对象
     * @return 保存后的数据源对象
     */
    DataSource save(DataSource dataSource);
    
    /**
     * 根据ID查询数据源
     *
     * @param id 数据源ID
     * @return 数据源对象
     */
    Optional<DataSource> findById(String id);
    
    /**
     * 根据名称查询数据源
     *
     * @param name 数据源名称
     * @return 数据源对象
     */
    Optional<DataSource> findByName(String name);
    
    /**
     * 查询所有数据源
     *
     * @return 数据源列表
     */
    List<DataSource> findAll();
    
    /**
     * 根据类型查询数据源
     *
     * @param type 数据源类型
     * @return 数据源列表
     */
    List<DataSource> findByType(DataSourceType type);
    
    /**
     * 根据激活状态查询数据源
     *
     * @param active 是否激活
     * @return 数据源列表
     */
    List<DataSource> findByActive(boolean active);
    
    /**
     * 删除数据源
     *
     * @param id 数据源ID
     */
    void deleteById(String id);
    
    /**
     * 检查数据源名称是否已存在
     *
     * @param name 数据源名称
     * @return 是否存在
     */
    boolean existsByName(String name);
}