package com.insightdata.domain.querybuilder.util;

import com.insightdata.domain.querybuilder.api.QueryModelContract;
import com.insightdata.domain.querybuilder.model.QueryModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询模型映射工具类
 * 负责在不同实现之间转换查询模型对象
 */
public class QueryModelMapper {
    
    /**
     * 将契约对象转换为领域模型
     *
     * @param contract 查询模型契约对象
     * @return 领域模型对象，如果输入为null则返回null
     */
    public static QueryModel toDomain(QueryModelContract contract) {
        if (contract == null) {
            return null;
        }
        
        QueryModel model = new QueryModel();
        copyProperties(contract, model);
        return model;
    }
    
    /**
     * 复制属性
     * 创建新的集合对象以避免共享引用
     */
    private static void copyProperties(QueryModelContract source, QueryModelContract target) {
        if (source == null || target == null) {
            return;
        }
        
        // 复制基本属性
        target.setId(source.getId());
        target.setName(source.getName());
        target.setFilter(source.getFilter());
        
        // 复制集合属性，创建新的集合实例
        target.setTables(copyList(source.getTables()));
        target.setFields(copyList(source.getFields()));
        target.setJoins(copyList(source.getJoins()));
        target.setGroupBy(copyList(source.getGroupBy()));
        target.setOrderBy(copyList(source.getOrderBy()));
        target.setParameters(copyMap(source.getParameters()));
    }
    
    /**
     * 复制列表，创建新的ArrayList实例
     */
    private static <T> List<T> copyList(List<T> source) {
        if (source == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(source);
    }
    
    /**
     * 复制Map，创建新的HashMap实例
     */
    private static <K, V> Map<K, V> copyMap(Map<K, V> source) {
        if (source == null) {
            return new HashMap<>();
        }
        return new HashMap<>(source);
    }
}