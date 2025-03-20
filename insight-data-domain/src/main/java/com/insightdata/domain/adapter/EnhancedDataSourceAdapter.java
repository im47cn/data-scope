package com.insightdata.domain.adapter;

import com.insightdata.domain.datasource.model.DataSource;
import com.insightdata.domain.datasource.model.SchemaInfo;
import com.insightdata.domain.datasource.model.TableInfo;

import java.util.List;

/**
 * 增强型数据源适配器接口，提供基于领域对象的高级抽象方法
 * 
 * 此接口扩展了基础DataSourceAdapter接口，使用领域对象替代基本类型参数，
 * 提供更一致的抽象层次，同时保持向后兼容性。
 */
public interface EnhancedDataSourceAdapter extends DataSourceAdapter {
    
    /**
     * 获取指定数据源的所有模式信息
     * 
     * @param dataSource 数据源配置
     * @return 模式信息列表
     * @throws Exception 如果发生错误
     */
    List<SchemaInfo> getSchemas(DataSource dataSource) throws Exception;
    
    /**
     * 获取指定数据源中特定模式的详细信息
     * 
     * @param dataSource 数据源配置
     * @param schemaName 模式名称
     * @return 模式详细信息
     * @throws Exception 如果发生错误
     */
    SchemaInfo getSchema(DataSource dataSource, String schemaName) throws Exception;
    
    /**
     * 获取指定数据源和模式中的所有表信息
     * 
     * @param dataSource 数据源配置
     * @param schema 模式名称
     * @return 表信息列表
     * @throws Exception 如果发生错误
     */
    List<TableInfo> getTables(DataSource dataSource, String schema) throws Exception;
}