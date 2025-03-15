package com.insightdata.infrastructure.persistence.repository;

import com.insightdata.common.enums.DataSourceType;
import com.insightdata.infrastructure.persistence.entity.DataSourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 数据源JPA仓储接口
 */
@Repository
public interface JpaDataSourceRepository extends JpaRepository<DataSourceEntity, Long> {
    
    /**
     * 根据名称查询数据源
     *
     * @param name 数据源名称
     * @return 数据源实体
     */
    Optional<DataSourceEntity> findByName(String name);
    
    /**
     * 根据类型查询数据源
     *
     * @param type 数据源类型
     * @return 数据源实体列表
     */
    List<DataSourceEntity> findByType(DataSourceType type);
    
    /**
     * 根据激活状态查询数据源
     *
     * @param active 是否激活
     * @return 数据源实体列表
     */
    List<DataSourceEntity> findByActive(boolean active);
    
    /**
     * 检查数据源名称是否已存在
     *
     * @param name 数据源名称
     * @return 是否存在
     */
    boolean existsByName(String name);
}