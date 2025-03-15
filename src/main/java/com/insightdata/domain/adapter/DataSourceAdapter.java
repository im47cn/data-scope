package com.insightdata.domain.adapter;

import com.insightdata.domain.model.DataSource;
import com.insightdata.domain.model.metadata.ColumnInfo;
import com.insightdata.domain.model.metadata.ForeignKeyInfo;
import com.insightdata.domain.model.metadata.IndexInfo;
import com.insightdata.domain.model.metadata.SchemaInfo;
import com.insightdata.domain.model.metadata.TableInfo;

import java.sql.Connection;
import java.util.List;

/**
 * 数据源适配器接口
 * 用于适配不同类型的数据库系统，提供统一的元数据提取和查询功能
 */
public interface DataSourceAdapter {
    
    /**
     * 获取适配器支持的数据源类型
     *
     * @return 数据源类型
     */
    String getType();
    
    /**
     * 测试数据源连接
     *
     * @param dataSource 数据源
     * @return 是否连接成功
     */
    boolean testConnection(DataSource dataSource);
    
    /**
     * 获取数据库连接
     *
     * @param dataSource 数据源
     * @return 数据库连接
     */
    Connection getConnection(DataSource dataSource);
    
    /**
     * 获取所有模式
     *
     * @param dataSource 数据源
     * @return 模式列表
     */
    List<SchemaInfo> getSchemas(DataSource dataSource);
    
    /**
     * 获取指定模式下的所有表
     *
     * @param dataSource 数据源
     * @param schemaName 模式名称
     * @return 表列表
     */
    List<TableInfo> getTables(DataSource dataSource, String schemaName);
    
    /**
     * 获取指定表的所有列
     *
     * @param dataSource 数据源
     * @param schemaName 模式名称
     * @param tableName 表名称
     * @return 列列表
     */
    List<ColumnInfo> getColumns(DataSource dataSource, String schemaName, String tableName);
    
    /**
     * 获取指定表的所有索引
     *
     * @param dataSource 数据源
     * @param schemaName 模式名称
     * @param tableName 表名称
     * @return 索引列表
     */
    List<IndexInfo> getIndexes(DataSource dataSource, String schemaName, String tableName);
    
    /**
     * 获取指定表的所有外键
     *
     * @param dataSource 数据源
     * @param schemaName 模式名称
     * @param tableName 表名称
     * @return 外键列表
     */
    List<ForeignKeyInfo> getForeignKeys(DataSource dataSource, String schemaName, String tableName);
    
    /**
     * 执行SQL查询
     *
     * @param dataSource 数据源
     * @param sql SQL语句
     * @param params 参数
     * @return 查询结果
     */
    List<List<Object>> executeQuery(DataSource dataSource, String sql, Object... params);
    
    /**
     * 获取表的行数估计值
     *
     * @param dataSource 数据源
     * @param schemaName 模式名称
     * @param tableName 表名称
     * @return 行数估计值
     */
    long getEstimatedRowCount(DataSource dataSource, String schemaName, String tableName);
    
    /**
     * 获取表的数据大小
     *
     * @param dataSource 数据源
     * @param schemaName 模式名称
     * @param tableName 表名称
     * @return 数据大小（字节）
     */
    long getDataSize(DataSource dataSource, String schemaName, String tableName);
    
    /**
     * 获取表的索引大小
     *
     * @param dataSource 数据源
     * @param schemaName 模式名称
     * @param tableName 表名称
     * @return 索引大小（字节）
     */
    long getIndexSize(DataSource dataSource, String schemaName, String tableName);
}