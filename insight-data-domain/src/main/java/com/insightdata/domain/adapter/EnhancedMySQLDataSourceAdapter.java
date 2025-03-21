package com.insightdata.domain.adapter;

import java.util.List;
import java.util.Map;

import com.insightdata.domain.datasource.model.DataSource;
import com.insightdata.domain.datasource.model.ForeignKeyInfo;
import com.insightdata.domain.datasource.model.IndexInfo;
import com.insightdata.domain.datasource.model.ProcedureInfo;
import com.insightdata.domain.datasource.model.TableInfo;
import com.insightdata.domain.datasource.model.TriggerInfo;
import com.insightdata.domain.datasource.model.ViewInfo;
import com.insightdata.domain.exception.DataSourceException;
import com.insightdata.domain.security.encryption.EncryptionService;

/**
 * Enhanced MySQL data source adapter interface that extends the base enhanced adapter
 * with MySQL specific functionality
 */
public interface EnhancedMySQLDataSourceAdapter extends EnhancedDataSourceAdapter {

    /**
     * Connect to data source with encryption
     * @param config Data source configuration
     * @param keyId Encryption key ID
     * @param encryptionService Encryption service
     * @throws DataSourceException if connection fails
     */
    void connect(DataSource config, String keyId, EncryptionService encryptionService) throws DataSourceException;

    /**
     * Get row count for a table
     * @param schema Schema name
     * @param table Table name
     * @return Row count
     * @throws DataSourceException if query fails
     */
    Long getRowCount(String schema, String table) throws DataSourceException;

    /**
     * Get data size for a table
     * @param schema Schema name
     * @param table Table name
     * @return Data size in bytes
     * @throws DataSourceException if query fails
     */
    Long getDataSize(String schema, String table) throws DataSourceException;

    /**
     * Get primary key information for a table
     * @param schema Schema name
     * @param table Table name
     * @return List of primary key information
     * @throws DataSourceException if query fails
     */
    List<IndexInfo> getPrimaryKeys(String schema, String table) throws DataSourceException;

    /**
     * Get index information for a table
     * @param schema Schema name
     * @param table Table name
     * @return List of index information
     * @throws DataSourceException if query fails
     */
    List<IndexInfo> getIndexes(String schema, String table) throws DataSourceException;

    /**
     * Get foreign key information for a table
     * @param schema Schema name
     * @param table Table name
     * @return List of foreign key information
     * @throws DataSourceException if query fails
     */
    List<ForeignKeyInfo> getForeignKeys(String schema, String table) throws DataSourceException;

    /**
     * Get view information for a schema
     * @param schema Schema name
     * @return List of view information
     * @throws DataSourceException if query fails
     */
    List<ViewInfo> getViews(String schema) throws DataSourceException;

    /**
     * Get stored procedure information for a schema
     * @param schema Schema name
     * @return List of stored procedure information
     * @throws DataSourceException if query fails
     */
    List<ProcedureInfo> getProcedures(String schema) throws DataSourceException;

    /**
     * Get trigger information for a table
     * @param schema Schema name
     * @param table Table name
     * @return List of trigger information
     * @throws DataSourceException if query fails
     */
    List<TriggerInfo> getTriggers(String schema, String table) throws DataSourceException;

    /**
     * Check if a column is a primary key
     * @param schema Schema name
     * @param table Table name
     * @param column Column name
     * @return true if column is primary key
     * @throws DataSourceException if query fails
     */
    boolean isPrimaryKey(String schema, String table, String column) throws DataSourceException;

    /**
     * Get table sizes for a list of tables
     * @param catalog Catalog name
     * @param tables List of table information
     * @return Map of table name to size
     * @throws DataSourceException if query fails
     */
    Map<String, Long> getTableSizes(String catalog, List<TableInfo> tables) throws DataSourceException;
}