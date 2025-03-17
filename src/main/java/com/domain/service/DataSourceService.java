package com.domain.service;

import java.util.List;
import java.util.Optional;

import com.domain.model.DataSource;
import com.domain.model.metadata.SchemaInfo;

/**
 * 数据源服务接口
 */
public interface DataSourceService {
    
    /**
     * 根据ID获取数据源
     */
    Optional<DataSource> getDataSourceById(String id);
    
    /**
     * 获取所有数据源
     */
    List<DataSource> getAllDataSources();
    
    /**
     * 创建数据源
     */
    DataSource createDataSource(DataSource dataSource);
    
    /**
     * 更新数据源
     */
    DataSource updateDataSource(DataSource dataSource);
    
    /**
     * 删除数据源
     */
    void deleteDataSource(String id);
    
    /**
     * 测试数据源连接
     */
    boolean testConnection(DataSource dataSource);
    
    /**
     * 获取数据源的schema信息
     */
    SchemaInfo getSchemaInfo(String dataSourceId, String schemaName);
    
    /**
     * 获取数据源的所有schema信息
     */
    List<SchemaInfo> getAllSchemaInfo(String dataSourceId);
    
    /**
     * 同步数据源元数据
     */
    void syncMetadata(String dataSourceId);
}