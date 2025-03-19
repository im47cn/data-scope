package com.insightdata.domain.adapter;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.insightdata.domain.metadata.model.ColumnInfo;
import com.insightdata.domain.metadata.model.DataSource;
import com.insightdata.domain.metadata.model.TableInfo;

public interface DataSourceAdapter {

    void connect(DataSource config) throws Exception;

    void disconnect() throws Exception;

    boolean testConnection(DataSource config) throws Exception;

    List<String> getCatalogs() throws Exception;

    List<String> getSchemas(String catalog) throws Exception;

    List<TableInfo> getTables(String catalog, String schema) throws Exception;

    List<ColumnInfo> getColumns(String catalog, String schema, String table) throws Exception;

    Map<String, Long> getTableSizes(String catalog, String schema) throws Exception;

    String getDataSourceType();

    Connection getConnection();
}