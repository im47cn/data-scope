package com.insightdata.infrastructure.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.insightdata.common.enums.DataSourceType;
import com.insightdata.domain.model.DataSource;

/**
 * 数据源Mapper接口
 * 使用MyBatis进行数据源的CRUD操作
 */
@Mapper
public interface DataSourceMapper {

    /**
     * 插入一条数据源记录
     * 
     * @param dataSource 数据源对象
     * @return 影响的行数
     */
    int insert(DataSource dataSource);

    /**
     * 更新数据源记录
     * 
     * @param dataSource 数据源对象
     * @return 影响的行数
     */
    int update(DataSource dataSource);

    /**
     * 根据ID查询数据源
     * 
     * @param id 数据源ID
     * @return 数据源对象
     */
    DataSource selectById(@Param("id") Long id);

    /**
     * 根据名称查询数据源
     * 
     * @param name 数据源名称
     * @return 数据源对象
     */
    DataSource selectByName(@Param("name") String name);

    /**
     * 查询所有数据源
     * 
     * @return 数据源列表
     */
    List<DataSource> selectAll();

    /**
     * 根据类型查询数据源
     * 
     * @param type 数据源类型
     * @return 数据源列表
     */
    List<DataSource> selectByType(@Param("type") DataSourceType type);

    /**
     * 根据活跃状态查询数据源
     * 
     * @param active 是否活跃
     * @return 数据源列表
     */
    List<DataSource> selectByActive(@Param("active") Boolean active);

    /**
     * 根据ID删除数据源
     * 
     * @param id 数据源ID
     * @return 影响的行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据名称统计数据源数量
     * 
     * @param name 数据源名称
     * @return 数量
     */
    int countByName(@Param("name") String name);
}