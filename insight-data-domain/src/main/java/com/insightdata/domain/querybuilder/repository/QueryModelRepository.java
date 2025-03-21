package com.insightdata.domain.querybuilder.repository;

import java.util.List;
import java.util.Optional;

import com.insightdata.domain.querybuilder.model.QueryModel;

/**
 * 查询模型仓储接口
 * 定义查询模型的持久化操作
 */
public interface QueryModelRepository {

    /**
     * 保存查询模型
     *
     * @param model 查询模型
     * @return 保存后的查询模型
     */
    QueryModel save(QueryModel model);

    /**
     * 根据ID查找查询模型
     *
     * @param id 查询模型ID
     * @return 查询模型（如果存在）
     */
    Optional<QueryModel> findById(String id);

    /**
     * 检查查询模型是否存在
     *
     * @param id 查询模型ID
     * @return true 如果存在，false 否则
     */
    boolean existsById(String id);

    /**
     * 删除查询模型
     *
     * @param id 查询模型ID
     */
    void deleteById(String id);

    /**
     * 获取所有查询模型
     *
     * @return 查询模型列表
     */
    List<QueryModel> findAll();

    /**
     * 根据名称模糊查询查询模型
     *
     * @param name 查询模型名称
     * @return 查询模型列表
     */
    List<QueryModel> findByNameContaining(String name);

    /**
     * 分页查询所有查询模型
     *
     * @param page 页码
     * @param size 每页大小
     * @return 查询模型列表
     */
    List<QueryModel> findAll(int page, int size);

    /**
     * 获取查询模型总数
     *
     * @return 查询模型总数
     */
    long count();

    /**
     * 批量保存查询模型
     *
     * @param models 查询模型列表
     * @return 保存后的查询模型列表
     */
    List<QueryModel> saveAll(List<QueryModel> models);

    /**
     * 批量删除查询模型
     *
     * @param ids 查询模型ID列表
     */
    void deleteAllById(List<String> ids);

    /**
     * 根据创建时间范围查询
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 查询模型列表
     */
    List<QueryModel> findByCreateTimeBetween(long startTime, long endTime);

    /**
     * 根据更新时间范围查询
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 查询模型列表
     */
    List<QueryModel> findByUpdateTimeBetween(long startTime, long endTime);

    /**
     * 根据创建者查询
     *
     * @param creator 创建者
     * @return 查询模型列表
     */
    List<QueryModel> findByCreator(String creator);

    /**
     * 根据标签查询
     *
     * @param tags 标签列表
     * @return 查询模型列表
     */
    List<QueryModel> findByTags(List<String> tags);

    /**
     * 获取最近使用的查询模型
     *
     * @param limit 限制数量
     * @return 查询模型列表
     */
    List<QueryModel> findRecentlyUsed(int limit);

    /**
     * 获取最常使用的查询模型
     *
     * @param limit 限制数量
     * @return 查询模型列表
     */
    List<QueryModel> findMostUsed(int limit);

    /**
     * 更新查询模型的使用次数
     *
     * @param id 查询模型ID
     */
    void incrementUsageCount(String id);

    /**
     * 更新查询模型的最后使用时间
     *
     * @param id 查询模型ID
     * @param timestamp 时间戳
     */
    void updateLastUsedTime(String id, long timestamp);
}