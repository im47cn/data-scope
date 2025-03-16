package com.insightdata.infrastructure.persistence.mapper;

import com.insightdata.infrastructure.persistence.entity.TableRelationshipEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TableRelationshipMapper {
    
    /**
     * 插入表关系
     */
    void insert(TableRelationshipEntity entity);
    
    /**
     * 根据ID查询表关系
     */
    TableRelationshipEntity selectById(@Param("id") Long id);
    
    /**
     * 根据数据源ID和表名查询关系
     */
    List<TableRelationshipEntity> selectByDataSourceIdAndTableName(
            @Param("dataSourceId") Long dataSourceId,
            @Param("tableName") String tableName);
    
    /**
     * 根据数据源ID和关系类型查询
     */
    List<TableRelationshipEntity> selectByDataSourceIdAndType(
            @Param("dataSourceId") Long dataSourceId,
            @Param("type") String type);
    
    /**
     * 删除指定数据源和来源的关系
     */
    void deleteByDataSourceIdAndSource(
            @Param("dataSourceId") Long dataSourceId,
            @Param("source") String source);
    
    /**
     * 更新关系使用频率
     */
    void incrementFrequency(@Param("id") Long id);
    
    /**
     * 更新表关系
     */
    void update(TableRelationshipEntity entity);
    
    /**
     * 删除表关系
     */
    void deleteById(@Param("id") Long id);
}