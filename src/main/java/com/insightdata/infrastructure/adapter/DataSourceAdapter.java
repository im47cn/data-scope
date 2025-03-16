package com.insightdata.infrastructure.adapter;

import java.sql.Connection;
import java.util.List;

import com.insightdata.domain.model.DataSource;
import com.insightdata.domain.model.metadata.ColumnInfo;
import com.insightdata.domain.model.metadata.ForeignKeyInfo;
import com.insightdata.domain.model.metadata.IndexInfo;
import com.insightdata.domain.model.metadata.SchemaInfo;
import com.insightdata.domain.model.metadata.TableInfo;
import com.insightdata.nlquery.executor.QueryResult;

/**
 * 数据源适配器接口
 */
public interface DataSourceAdapter {
    
    /**
     * 测试连接
     */
    boolean testConnection(DataSource dataSource);
    
    /**
     * 获取连接
     */
    Connection getConnection(DataSource dataSource);
    
    /**
     * 获取模式信息
     */
    SchemaInfo getSchema(DataSource dataSource, String schemaName);
    
    /**
     * 获取表信息
     */
    TableInfo getTable(DataSource dataSource, String schemaName, String tableName);
    
    /**
     * 获取列信息列表
     */
    List<ColumnInfo> getColumns(DataSource dataSource, String schemaName, String tableName);
    
    /**
     * 获取索引信息列表
     */
    List<IndexInfo> getIndexes(DataSource dataSource, String schemaName, String tableName);
    
    /**
     * 获取外键信息列表
     */
    List<ForeignKeyInfo> getForeignKeys(DataSource dataSource, String schemaName, String tableName);
    
    /**
     * 获取所有模式列表
     */
    List<SchemaInfo> getSchemas(DataSource dataSource);
    
    /**
     * 获取所有表列表
     */
    List<TableInfo> getTables(DataSource dataSource, String schemaName);
    
    /**
     * 执行查询
     */
    QueryResult executeQuery(String sql, DataSource dataSource);
    
    /**
     * 获取表行数
     */
    long getRowCount(DataSource dataSource, String schemaName, String tableName);
    
    /**
     * 获取表数据大小
     */
    long getDataSize(DataSource dataSource, String schemaName, String tableName);
    
    /**
     * 获取表索引大小
     */
    long getIndexSize(DataSource dataSource, String schemaName, String tableName);
    
    /**
     * 检查列是否自增
     */
    boolean isAutoIncrement(DataSource dataSource, String schemaName, String tableName, String columnName);
    
    /**
     * 获取数据库版本
     */
    String getDatabaseVersion(DataSource dataSource);
    
    /**
     * 获取驱动版本
     */
    String getDriverVersion(DataSource dataSource);
    
    /**
     * 获取默认模式名
     */
    String getDefaultSchema(DataSource dataSource);
    
    /**
     * 获取系统模式列表
     */
    List<String> getSystemSchemas(DataSource dataSource);
    
    /**
     * 是否是系统模式
     */
    boolean isSystemSchema(DataSource dataSource, String schemaName);
    
    /**
     * 是否是系统表
     */
    boolean isSystemTable(DataSource dataSource, String schemaName, String tableName);
}