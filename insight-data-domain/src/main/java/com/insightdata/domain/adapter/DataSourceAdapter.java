package com.insightdata.domain.adapter;

import com.insightdata.domain.datasource.model.ColumnInfo;
import com.insightdata.domain.datasource.model.DataSource;
import com.insightdata.domain.datasource.model.TableInfo;
import com.insightdata.domain.exception.DataSourceException;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Base interface for data source adapters providing core database operations
 */
public interface DataSourceAdapter {

    /**
     * Connect to the data source
     * @param config Data source configuration
     * @throws DataSourceException if connection fails
     */
    void connect(DataSource config) throws DataSourceException;

    /**
     * Disconnect from the data source
     * @throws DataSourceException if disconnect fails
     */
    void disconnect() throws DataSourceException;

    /**
     * Test connection to the data source
     * @param config Data source configuration
     * @return true if connection successful
     * @throws DataSourceException if test fails
     */
    boolean testConnection(DataSource config) throws DataSourceException;

    /**
     * Get list of available catalogs
     * @return List of catalog names
     * @throws DataSourceException if query fails
     */
    List<String> getCatalogs() throws DataSourceException;

    /**
     * Get list of schemas in a catalog
     * @param catalog Catalog name
     * @return List of schema names
     * @throws DataSourceException if query fails
     */
    List<String> getSchemas(String catalog) throws DataSourceException;

    /**
     * Get list of tables in a schema
     * @param catalog Catalog name
     * @param schema Schema name
     * @return List of table information
     * @throws DataSourceException if query fails
     */
    List<TableInfo> getTables(String catalog, String schema) throws DataSourceException;

    /**
     * Get list of columns in a table
     * @param catalog Catalog name
     * @param schema Schema name
     * @param table Table name
     * @return List of column information
     * @throws DataSourceException if query fails
     */
    List<ColumnInfo> getColumns(String catalog, String schema, String table) throws DataSourceException;

    /**
     * Get table sizes in a schema
     * @param catalog Catalog name
     * @param schema Schema name
     * @return Map of table name to size in bytes
     * @throws DataSourceException if query fails
     */
    Map<String, Long> getTableSizes(String catalog, String schema) throws DataSourceException;

    /**
     * Get the type of this data source adapter
     * @return Data source type identifier
     */
    String getDataSourceType();

    /**
     * Get the current database connection
     * @return Active database connection
     * @throws DataSourceException if no active connection exists
     */
    Connection getConnection() throws DataSourceException;
}