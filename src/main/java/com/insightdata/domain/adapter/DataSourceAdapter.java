package com.insightdata.domain.adapter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.insightdata.common.enums.DataSourceType;
import com.insightdata.domain.model.DataSource;
import com.insightdata.domain.model.metadata.ColumnInfo;
import com.insightdata.domain.model.metadata.ForeignKeyInfo;
import com.insightdata.domain.model.metadata.IndexInfo;
import com.insightdata.domain.model.metadata.SchemaInfo;
import com.insightdata.domain.model.metadata.TableInfo;

/**
 * 数据源适配器接口
 */
public interface DataSourceAdapter {
    
    /**
     * 获取数据源类型
     *
     * @return 数据源类型
     */
    DataSourceType getType();
    
    /**
     * 测试连接
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
     * @throws SQLException SQL异常
     */
    Connection getConnection(DataSource dataSource) throws SQLException;
    
    /**
     * 获取所有Schema
     *
     * @param dataSource 数据源
     * @return Schema列表
     */
    List<SchemaInfo> getSchemas(DataSource dataSource);
    
    /**
     * 获取指定Schema
     *
     * @param dataSource 数据源
     * @param schemaName Schema名称
     * @return Schema信息
     */
    SchemaInfo getSchema(DataSource dataSource, String schemaName);
    
    /**
     * 获取Schema信息
     *
     * @param dataSource 数据源
     * @param schemaName Schema名称
     * @return Schema信息
     */
    SchemaInfo getSchemaInfo(DataSource dataSource, String schemaName);
    
    /**
     * 获取所有表
     *
     * @param dataSource 数据源
     * @param schemaName Schema名称
     * @return 表列表
     */
    List<TableInfo> getTables(DataSource dataSource, String schemaName);
    
    /**
     * 获取指定表
     *
     * @param dataSource 数据源
     * @param schemaName Schema名称
     * @param tableName 表名
     * @return 表信息
     */
    TableInfo getTable(DataSource dataSource, String schemaName, String tableName);
    
    /**
     * 获取所有列
     *
     * @param dataSource 数据源
     * @param schemaName Schema名称
     * @param tableName 表名
     * @return 列列表
     */
    List<ColumnInfo> getColumns(DataSource dataSource, String schemaName, String tableName);
    
    /**
     * 获取所有索引
     *
     * @param dataSource 数据源
     * @param schemaName Schema名称
     * @param tableName 表名
     * @return 索引列表
     */
    List<IndexInfo> getIndexes(DataSource dataSource, String schemaName, String tableName);
    
    /**
     * 获取所有外键
     *
     * @param dataSource 数据源
     * @param schemaName Schema名称
     * @param tableName 表名
     * @return 外键列表
     */
    List<ForeignKeyInfo> getForeignKeys(DataSource dataSource, String schemaName, String tableName);
    
    /**
     * 获取表大小(字节)
     *
     * @param dataSource 数据源
     * @param schemaName Schema名称
     * @param tableName 表名
     * @return 表大小
     */
    long getDataSize(DataSource dataSource, String schemaName, String tableName);
    
    /**
     * 获取表行数
     *
     * @param dataSource 数据源
     * @param schemaName Schema名称
     * @param tableName 表名
     * @return 表行数
     */
    long getRowCount(DataSource dataSource, String schemaName, String tableName);
    
    /**
     * 获取表估计行数
     *
     * @param dataSource 数据源
     * @param schemaName Schema名称
     * @param tableName 表名
     * @return 表估计行数
     */
    long getEstimatedRowCount(DataSource dataSource, String schemaName, String tableName);
    
    /**
     * 获取索引大小
     *
     * @param dataSource 数据源
     * @param schemaName Schema名称
     * @param tableName 表名
     * @return 索引大小
     */
    long getIndexSize(DataSource dataSource, String schemaName, String tableName);
    
    /**
     * 执行查询
     *
     * @param dataSource 数据源
     * @param sql SQL语句
     * @param params 参数
     * @return 查询结果
     */
    List<List<Object>> executeQuery(DataSource dataSource, String sql, Object... params);
}