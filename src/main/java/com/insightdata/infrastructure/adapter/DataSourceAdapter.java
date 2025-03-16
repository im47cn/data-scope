package com.insightdata.infrastructure.adapter;

import java.util.List;

import com.insightdata.common.exception.DataSourceException;
import com.insightdata.domain.model.DataSource;
import com.insightdata.domain.model.metadata.SchemaInfo;

public interface DataSourceAdapter {
    boolean testConnection(DataSource dataSource) throws DataSourceException;
    List<SchemaInfo> getSchemas(DataSource dataSource) throws DataSourceException;
    SchemaInfo getSchema(DataSource dataSource, String schemaName) throws DataSourceException;
}