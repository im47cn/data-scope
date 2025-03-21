package com.insightdata.domain.adapter;

import com.insightdata.domain.datasource.model.DataSource;
import com.insightdata.domain.exception.DataSourceException;
import com.insightdata.domain.metadata.model.SchemaInfo;
import com.insightdata.domain.metadata.model.TableInfo;

import java.util.List;

/**
 * Enhanced data source adapter interface providing domain object based methods.
 * 
 * This interface extends the base DataSourceAdapter interface, using domain objects
 * instead of primitive types for parameters while maintaining backward compatibility.
 */
public interface EnhancedDataSourceAdapter extends DataSourceAdapter {
    
    /**
     * Get all schema information for the specified data source
     * 
     * @param dataSource Data source configuration
     * @return List of schema information
     * @throws DataSourceException if an error occurs
     */
    List<SchemaInfo> getSchemas(DataSource dataSource) throws DataSourceException;
    
    /**
     * Get detailed schema information for the specified schema
     * 
     * @param dataSource Data source configuration
     * @param schemaName Schema name
     * @return Detailed schema information
     * @throws DataSourceException if an error occurs
     */
    SchemaInfo getSchema(DataSource dataSource, String schemaName) throws DataSourceException;
    
    /**
     * Get all table information for the specified schema
     * 
     * @param dataSource Data source configuration
     * @param schema Schema name
     * @return List of table information
     * @throws DataSourceException if an error occurs
     */
    List<TableInfo> getTables(DataSource dataSource, String schema) throws DataSourceException;
}