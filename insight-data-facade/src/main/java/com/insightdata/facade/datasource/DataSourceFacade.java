package com.insightdata.facade.datasource;

import com.insightdata.facade.common.PageResponse;

/**
 * Facade interface for data source management
 */
public interface DataSourceFacade {
    /**
     * List data sources with pagination
     */
    PageResponse<DataSourceDTO> listDataSources(DataSourceQueryRequest request);
    
    /**
     * Get data source by ID
     */
    DataSourceDTO getDataSource(String id);
    
    /**
     * Create new data source
     */
    DataSourceDTO createDataSource(DataSourceCreateRequest request);
    
    /**
     * Update existing data source
     */
    DataSourceDTO updateDataSource(String id, DataSourceUpdateRequest request);
    
    /**
     * Delete data source by ID
     */
    void deleteDataSource(String id);
    
    /**
     * Batch delete data sources
     */
    BatchDeleteResult batchDeleteDataSources(String[] ids);
    
    /**
     * Test data source connection
     */
    ConnectionTestResult testConnection(ConnectionTestRequest request);
    
    /**
     * Test existing data source connection
     */
    ConnectionTestResult testConnection(String id);
    
    /**
     * Sync metadata from data source
     */
    String syncMetadata(String id, MetadataSyncRequest request);
    
    /**
     * Get metadata sync status
     */
    MetadataSyncStatus getSyncStatus(String taskId);
    
    /**
     * Get supported data source types
     */
    DataSourceType[] getSupportedTypes();
}