package com.domain.service;

import java.util.List;
import java.util.Optional;

import com.common.enums.DataSourceType;
import com.domain.model.metadata.DataSource;
import com.domain.model.metadata.SchemaInfo;
import com.domain.model.metadata.TableInfo;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据源服务接口
 */
public interface DataSourceService {
    
    /**
     * 根据ID获取数据源
     */
    Optional<DataSource> getDataSourceById(String id);

    @Transactional(readOnly = true)
    Optional<DataSource> getDataSourceByName(String name);

    /**
     * 获取所有数据源
     */
    List<DataSource> getAllDataSources();

    /**
     * 根据类型查询数据源
     *
     * @param type 数据源类型
     * @return 数据源列表
     */
    List<DataSource> getDataSourcesByType(DataSourceType type);

    /**
     * 根据激活状态查询数据源
     *
     * @param active 是否激活
     * @return 数据源列表
     */
    List<DataSource> getDataSourcesByActive(boolean active);

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
    List<SchemaInfo> getSchemas(String dataSourceId);

    @Transactional(readOnly = true)
    List<TableInfo> getTables(String dataSourceId, String schemaName);

    /**
     * 同步数据源元数据
     */
    String syncMetadata(String dataSourceId);

    @Transactional(readOnly = true)
    List<DataSourceType> getSupportedTypes();
}