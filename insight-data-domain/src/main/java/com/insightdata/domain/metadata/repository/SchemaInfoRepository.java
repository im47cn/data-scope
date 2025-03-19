package com.insightdata.domain.metadata.repository;

import com.insightdata.domain.metadata.model.SchemaInfo;

import java.util.Optional;

/**
 * Schema信息仓储接口
 */
public interface SchemaInfoRepository {
    
    /**
     * 保存Schema信息
     *
     * @param schemaInfo Schema信息
     * @return 保存后的Schema信息
     */
    SchemaInfo save(SchemaInfo schemaInfo);
    
    /**
     * 根据ID查询Schema信息
     *
     * @param id Schema ID
     * @return Schema信息
     */
    Optional<SchemaInfo> findById(String id);
    
    /**
     * 根据数据源ID查询Schema信息列表
     *
     * @param dataSourceId 数据源ID
     * @return Schema信息列表
     */
    java.util.List<SchemaInfo> findByDataSourceId(Long dataSourceId);
    
    /**
     * 删除Schema信息
     *
     * @param schemaInfo Schema信息
     */
    void delete(SchemaInfo schemaInfo);
}