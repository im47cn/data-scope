package com.insightdata.application.service;

import com.insightdata.domain.querybuilder.api.QueryModelContract;
import com.insightdata.domain.querybuilder.model.Filter;
import com.insightdata.domain.querybuilder.model.ParameterDefinition;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 查询模型服务接口
 * 提供查询模型的核心业务功能
 */
public interface QueryModelApplicationService {

    /**
     * 创建新的查询模型
     *
     * @param model 查询模型
     * @return 创建的查询模型
     */
    QueryModelContract create(QueryModelContract model);

    /**
     * 更新查询模型
     *
     * @param model 查询模型
     * @return 更新后的查询模型
     */
    QueryModelContract update(QueryModelContract model);

    /**
     * 根据ID查找查询模型
     *
     * @param id 查询模型ID
     * @return 查询模型（如果存在）
     */
    Optional<QueryModelContract> findById(String id);

    /**
     * 根据名称查找查询模型
     *
     * @param name 查询模型名称
     * @return 查询模型列表
     */
    List<QueryModelContract> findByName(String name);

    /**
     * 删除查询模型
     *
     * @param id 查询模型ID
     */
    void delete(String id);

    /**
     * 获取所有查询模型
     *
     * @return 查询模型列表
     */
    List<QueryModelContract> findAll();

    /**
     * 验证查询模型
     *
     * @param model 查询模型
     * @return 验证结果，true表示有效，false表示无效
     */
    boolean validate(QueryModelContract model);

    /**
     * 复制查询模型
     *
     * @param id      源查询模型ID
     * @param newName 新查询模型名称
     * @return 复制的查询模型
     */
    QueryModelContract copy(String id, String newName);

    /**
     * 执行查询模型
     *
     * @param id         查询模型ID
     * @param parameters 查询参数
     * @return 查询结果
     */
    List<Map<String, Object>> execute(String id, Map<String, Object> parameters);

    /**
     * 获取查询模型的参数定义
     *
     * @param id 查询模型ID
     * @return 参数定义列表
     */
    List<ParameterDefinition> getParameters(String id);

    /**
     * 添加过滤条件
     *
     * @param id     查询模型ID
     * @param filter 过滤条件
     * @return 更新后的查询模型
     */
    QueryModelContract addFilter(String id, Filter filter);

    /**
     * 移除过滤条件
     *
     * @param id          查询模型ID
     * @param filterIndex 过滤条件索引
     * @return 更新后的查询模型
     */
    QueryModelContract removeFilter(String id, int filterIndex);

    /**
     * 验证参数值
     *
     * @param id         查询模型ID
     * @param parameters 参数值
     * @return 验证结果，true表示有效，false表示无效
     */
    boolean validateParameters(String id, Map<String, Object> parameters);

    /**
     * 获取查询模型的SQL预览
     *
     * @param id 查询模型ID
     * @return SQL语句
     */
    String getSqlPreview(String id);

    /**
     * 获取查询模型的执行计划
     *
     * @param id 查询模型ID
     * @return 执行计划
     */
    String getExecutionPlan(String id);

    /**
     * 分页查询所有查询模型
     *
     * @param page 页码
     * @param size 每页大小
     * @return 查询模型列表
     */
    List<QueryModelContract> findAll(int page, int size);

    /**
     * 获取查询模型总数
     *
     * @return 查询模型总数
     */
    long count();
}