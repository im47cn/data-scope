package com.insightdata.application.service;

import com.insightdata.common.enums.DataSourceType;
import com.insightdata.domain.model.DataSource;
import com.insightdata.domain.model.metadata.SchemaInfo;
import com.insightdata.domain.model.metadata.TableInfo;

import java.util.List;
import java.util.Optional;

/**
 * 数据源服务接口
 */
public interface DataSourceService {

    /**
     * 创建数据源
     *
     * @param dataSource 数据源对象
     * @return 创建后的数据源对象
     */
    DataSource createDataSource(DataSource dataSource);

    /**
     * 更新数据源
     *
     * @param dataSource 数据源对象
     * @return 更新后的数据源对象
     */
    DataSource updateDataSource(DataSource dataSource);

    /**
     * 根据ID查询数据源
     *
     * @param id 数据源ID
     * @return 数据源对象
     */
    Optional<DataSource> getDataSourceById(Long id);

    /**
     * 根据名称查询数据源
     *
     * @param name 数据源名称
     * @return 数据源对象
     */
    Optional<DataSource> getDataSourceByName(String name);

    /**
     * 查询所有数据源
     *
     * @return 数据源列表
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
     * 删除数据源
     *
     * @param id 数据源ID
     */
    void deleteDataSource(Long id);

    /**
     * 测试数据源连接
     *
     * @param dataSource 数据源对象
     * @return 是否连接成功
     */
    boolean testConnection(DataSource dataSource);

    /**
     * 获取数据源的所有模式
     *
     * @param dataSourceId 数据源ID
     * @return 模式列表
     */
    List<SchemaInfo> getSchemas(Long dataSourceId);

    /**
     * 获取数据源的指定模式
     *
     * @param dataSourceId 数据源ID
     * @param schemaName   模式名称
     * @return 模式列表
     */
    SchemaInfo getSchemaInfo(Long dataSourceId, String schemaName);

    /**
     * 获取指定模式下的所有表
     *
     * @param dataSourceId 数据源ID
     * @param schemaName   模式名称
     * @return 表列表
     */
    List<TableInfo> getTables(Long dataSourceId, String schemaName);

    /**
     * 同步数据源元数据
     *
     * @param dataSourceId 数据源ID
     * @return 同步作业ID
     */
    String syncMetadata(Long dataSourceId);

    /**
     * 获取支持的数据源类型
     *
     * @return 数据源类型列表
     */
    List<DataSourceType> getSupportedTypes();

}