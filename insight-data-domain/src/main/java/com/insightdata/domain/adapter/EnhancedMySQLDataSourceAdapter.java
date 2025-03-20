package com.insightdata.domain.adapter;

import com.insightdata.domain.datasource.*;
import com.insightdata.domain.security.encryption.EncryptionService;

import java.util.List;
import java.util.Map;

/**
 * 增强的MySQL数据源适配器接口
 */
public interface EnhancedMySQLDataSourceAdapter {
    /**
     * 连接数据源
     */
    void connect(DataSource config, String keyId, EncryptionService encryptionService) throws Exception;

    /**
     * 连接数据源
     */
    void connect(DataSource config) throws Exception;

    /**
     * 测试连接
     */
    boolean testConnection() throws Exception;

    /**
     * 测试连接
     */
    boolean testConnection(DataSource config) throws Exception;

    /**
     * 获取数据库列表
     */
    List<String> getCatalogs() throws Exception;

    /**
     * 获取模式列表
     */
    List<String> getSchemas(String catalog) throws Exception;

    /**
     * 获取表列表
     */
    List<TableInfo> getTables(String catalog, String schema) throws Exception;

    /**
     * 获取列信息
     */
    List<ColumnInfo> getColumns(String catalog, String schema, String table) throws Exception;

    /**
     * 获取表大小
     */
    Map<String, Long> getTableSizes(String catalog, String schema) throws Exception;

    /**
     * 获取表大小
     */
    Map<String, Long> getTableSizes(String catalog, List<TableInfo> tables) throws Exception;

    /**
     * 获取表行数
     */
    Long getRowCount(String schema, String table) throws Exception;

    /**
     * 获取数据大小
     */
    Long getDataSize(String schema, String table) throws Exception;

    /**
     * 获取主键信息
     */
    List<IndexInfo> getPrimaryKeys(String schema, String table) throws Exception;

    /**
     * 获取索引信息
     */
    List<IndexInfo> getIndexes(String schema, String table) throws Exception;

    /**
     * 获取外键信息
     */
    List<ForeignKeyInfo> getForeignKeys(String schema, String table) throws Exception;

    /**
     * 获取视图信息
     */
    List<ViewInfo> getViews(String schema) throws Exception;

    /**
     * 获取存储过程信息
     */
    List<ProcedureInfo> getProcedures(String schema) throws Exception;

    /**
     * 获取触发器信息
     */
    List<TriggerInfo> getTriggers(String schema, String table) throws Exception;

    /**
     * 判断是否为主键
     */
    boolean isPrimaryKey(String schema, String table, String column) throws Exception;
}