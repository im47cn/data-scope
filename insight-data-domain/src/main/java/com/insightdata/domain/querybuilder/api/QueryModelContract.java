package com.insightdata.domain.querybuilder.api;

import java.util.List;
import java.util.Map;

/**
 * 查询模型契约接口
 * 定义查询模型的基本结构，供 Domain 和 Facade 层共同使用
 * 
 * 此接口作为 Domain 和 Facade 层之间的桥梁，确保两层之间的数据结构一致性。
 * Facade 层的 DTO 和 Domain 层的模型类都实现此接口，避免直接依赖。
 */
public interface QueryModelContract {
    /**
     * 获取查询模型ID
     */
    String getId();

    /**
     * 设置查询模型ID
     */
    void setId(String id);
    
    /**
     * 获取查询模型名称
     */
    String getName();

    /**
     * 设置查询模型名称
     */
    void setName(String name);
    
    /**
     * 获取查询涉及的表列表
     */
    List<String> getTables();

    /**
     * 设置查询涉及的表列表
     */
    void setTables(List<String> tables);
    
    /**
     * 获取查询的字段列表
     */
    List<String> getFields();

    /**
     * 设置查询的字段列表
     */
    void setFields(List<String> fields);
    
    /**
     * 获取表连接条件列表
     */
    List<String> getJoins();

    /**
     * 设置表连接条件列表
     */
    void setJoins(List<String> joins);
    
    /**
     * 获取查询过滤条件
     */
    String getFilter();

    /**
     * 设置查询过滤条件
     */
    void setFilter(String filter);
    
    /**
     * 获取分组字段列表
     */
    List<String> getGroupBy();

    /**
     * 设置分组字段列表
     */
    void setGroupBy(List<String> groupBy);
    
    /**
     * 获取排序字段列表
     */
    List<String> getOrderBy();

    /**
     * 设置排序字段列表
     */
    void setOrderBy(List<String> orderBy);
    
    /**
     * 获取查询参数映射
     */
    Map<String, Object> getParameters();

    /**
     * 设置查询参数映射
     */
    void setParameters(Map<String, Object> parameters);
}