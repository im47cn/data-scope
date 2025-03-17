package com.infrastructure.persistence.mapper;

import com.infrastructure.persistence.entity.AppEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * App数据访问接口
 */
@Mapper
public interface AppMapper {
    
    /**
     * 插入应用
     *
     * @param entity 应用实体
     * @return 影响的行数
     */
    int insert(AppEntity entity);
    
    /**
     * 更新应用
     *
     * @param entity 应用实体
     * @return 影响的行数
     */
    int update(AppEntity entity);
    
    /**
     * 根据ID查询应用
     *
     * @param id 应用ID
     * @return 应用实体
     */
    AppEntity findById(@Param("id") Long id);
    
    /**
     * 根据应用编码查询应用
     *
     * @param code 应用编码
     * @return 应用实体
     */
    AppEntity findByCode(@Param("code") String code);
    
    /**
     * 查询所有应用
     *
     * @return 应用实体列表
     */
    List<AppEntity> findAll();
    
    /**
     * 查询所有已发布的应用
     *
     * @return 已发布的应用实体列表
     */
    List<AppEntity> findAllPublished();
    
    /**
     * 根据名称模糊查询应用
     *
     * @param name 应用名称
     * @return 应用实体列表
     */
    List<AppEntity> findByNameContaining(@Param("name") String name);
    
    /**
     * 检查应用编码是否已存在
     *
     * @param code 应用编码
     * @return 是否存在
     */
    boolean existsByCode(@Param("code") String code);
    
    /**
     * 根据ID删除应用
     *
     * @param id 应用ID
     * @return 影响的行数
     */
    int deleteById(@Param("id") Long id);
}