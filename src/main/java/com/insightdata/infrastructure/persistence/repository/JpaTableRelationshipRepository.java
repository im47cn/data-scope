package com.insightdata.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.insightdata.infrastructure.persistence.entity.TableRelationshipEntity;

/**
 * 表关系JPA仓库接口
 */
@Repository
public interface JpaTableRelationshipRepository extends JpaRepository<TableRelationshipEntity, Long> {
    
    /**
     * 查找数据源的所有表关系
     */
    List<TableRelationshipEntity> findByDataSourceId(Long dataSourceId);
    
    /**
     * 查找指定表的所有关系
     */
    @Query("SELECT r FROM TableRelationshipEntity r WHERE r.dataSourceId = :dataSourceId AND (r.sourceTable = :tableName OR r.targetTable = :tableName)")
    List<TableRelationshipEntity> findByDataSourceIdAndTable(@Param("dataSourceId") Long dataSourceId, @Param("tableName") String tableName);
    
    /**
     * 查找两个表之间的关系
     */
    @Query("SELECT r FROM TableRelationshipEntity r WHERE r.dataSourceId = :dataSourceId AND " +
           "((r.sourceTable = :sourceTable AND r.targetTable = :targetTable) OR " +
           "(r.sourceTable = :targetTable AND r.targetTable = :sourceTable))")
    List<TableRelationshipEntity> findByDataSourceIdAndTables(@Param("dataSourceId") Long dataSourceId, 
                                                            @Param("sourceTable") String sourceTable, 
                                                            @Param("targetTable") String targetTable);
    
    /**
     * 删除数据源的所有表关系
     */
    void deleteByDataSourceId(Long dataSourceId);
}