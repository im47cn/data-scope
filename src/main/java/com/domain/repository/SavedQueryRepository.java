package com.domain.repository;

import com.domain.model.query.SavedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 保存的查询仓库
 * 使用MyBatis实现，不再使用JPA
 */
@Repository
public interface SavedQueryRepository {
    
    /**
     * 保存查询
     * 
     * @param savedQuery 保存的查询对象
     * @return 保存后的查询
     */
    SavedQuery save(SavedQuery savedQuery);
    
    /**
     * 根据ID查询保存的查询
     * 
     * @param id 查询ID
     * @return 保存的查询可选结果
     */
    Optional<SavedQuery> findById(String id);
    
    /**
     * 查询所有保存的查询
     * 
     * @return 保存的查询列表
     */
    List<SavedQuery> findAll();
    
    /**
     * 根据ID删除保存的查询
     * 
     * @param id 查询ID
     */
    void deleteById(String id);
    
    /**
     * 根据数据源ID查询保存的查询
     *
     * @param dataSourceId 数据源ID
     * @return 保存的查询列表
     */
    List<SavedQuery> findByDataSourceId(String dataSourceId);
    
    /**
     * 根据名称查询保存的查询
     *
     * @param name 查询名称
     * @return 保存的查询
     */
    Optional<SavedQuery> findByName(String name);
    
}