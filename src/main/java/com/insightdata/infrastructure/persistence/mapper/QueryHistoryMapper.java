package com.insightdata.infrastructure.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.insightdata.domain.model.query.QueryHistory;

/**
 * 查询历史Mapper接口
 * 使用MyBatis进行查询历史的CRUD操作
 */
@Mapper
public interface QueryHistoryMapper {

    /**
     * 插入一条查询历史记录
     * 
     * @param queryHistory 查询历史对象
     * @return 影响的行数
     */
    int insert(QueryHistory queryHistory);

    /**
     * 更新查询历史记录
     * 
     * @param queryHistory 查询历史对象
     * @return 影响的行数
     */
    int update(QueryHistory queryHistory);

    /**
     * 根据ID查询历史记录
     * 
     * @param id 查询历史ID
     * @return 查询历史对象
     */
    QueryHistory selectById(@Param("id") Long id);

    /**
     * 查询所有查询历史记录
     * 
     * @return 查询历史列表
     */
    List<QueryHistory> selectAll();

    /**
     * 根据数据源ID查询历史记录并按创建时间降序排列
     *
     * @param dataSourceId 数据源ID
     * @return 查询历史列表
     */
    List<QueryHistory> selectByDataSourceIdOrderByCreatedAtDesc(@Param("dataSourceId") Long dataSourceId);

    /**
     * 根据ID删除查询历史
     * 
     * @param id 查询历史ID
     * @return 影响的行数
     */
    int deleteById(@Param("id") Long id);
}