package com.domain.repository;

import com.domain.model.lowcode.DataBinding;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 数据绑定仓储接口
 */
@Repository
public interface DataBindingRepository {
    
    /**
     * 保存数据绑定
     *
     * @param dataBinding 数据绑定信息
     * @return 保存后的数据绑定
     */
    DataBinding save(DataBinding dataBinding);
    
    /**
     * 根据ID查询数据绑定
     *
     * @param id 数据绑定ID
     * @return 数据绑定信息
     */
    Optional<DataBinding> findById(String id);
    
    /**
     * 根据组件ID查询数据绑定列表
     *
     * @param componentId 组件ID
     * @return 数据绑定列表
     */
    List<DataBinding> findByComponentId(String componentId);
    
    /**
     * 根据查询ID查询数据绑定列表
     *
     * @param queryId 查询ID
     * @return 数据绑定列表
     */
    List<DataBinding> findByQueryId(String queryId);
    
    /**
     * 根据ID删除数据绑定
     *
     * @param id 数据绑定ID
     */
    void deleteById(String id);
    
    /**
     * 查询所有数据绑定
     *
     * @return 数据绑定列表
     */
    List<DataBinding> findAll();
    
    /**
     * 根据组件ID删除所有数据绑定
     *
     * @param componentId 组件ID
     */
    void deleteByComponentId(String componentId);
    
    /**
     * 根据查询ID删除所有数据绑定
     *
     * @param queryId 查询ID
     */
    void deleteByQueryId(String queryId);
}