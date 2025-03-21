package com.insightdata.domain.querybuilder.model;

import com.insightdata.domain.querybuilder.api.QueryModelContract;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询模型领域对象
 * 实现 QueryModelContract 接口，用于 Domain 层业务逻辑
 */
@Data
public class QueryModel implements QueryModelContract {
    /**
     * 查询模型ID
     */
    private String id;

    /**
     * 查询模型名称
     */
    private String name;

    /**
     * 查询涉及的表列表
     */
    private List<String> tables = new ArrayList<>();

    /**
     * 查询的字段列表
     */
    private List<String> fields = new ArrayList<>();

    /**
     * 表连接条件列表
     */
    private List<String> joins = new ArrayList<>();

    /**
     * 查询过滤条件
     */
    private String filter;

    /**
     * 分组字段列表
     */
    private List<String> groupBy = new ArrayList<>();

    /**
     * 排序字段列表
     */
    private List<String> orderBy = new ArrayList<>();

    /**
     * 查询参数映射
     */
    private Map<String, Object> parameters = new HashMap<>();

    /**
     * 创建一个空的查询模型对象
     */
    public QueryModel() {
        // 使用字段初始化器创建空集合
    }

    /**
     * 使用现有的 QueryModelContract 创建查询模型对象
     *
     * @param contract 查询模型契约对象
     */
    public QueryModel(QueryModelContract contract) {
        if (contract != null) {
            this.id = contract.getId();
            this.name = contract.getName();
            this.tables = new ArrayList<>(contract.getTables());
            this.fields = new ArrayList<>(contract.getFields());
            this.joins = new ArrayList<>(contract.getJoins());
            this.filter = contract.getFilter();
            this.groupBy = new ArrayList<>(contract.getGroupBy());
            this.orderBy = new ArrayList<>(contract.getOrderBy());
            this.parameters = new HashMap<>(contract.getParameters());
        }
    }

    /**
     * 添加查询表
     *
     * @param table 表名
     */
    public void addTable(String table) {
        if (table != null && !table.trim().isEmpty()) {
            this.tables.add(table.trim());
        }
    }

    /**
     * 添加查询字段
     *
     * @param field 字段名
     */
    public void addField(String field) {
        if (field != null && !field.trim().isEmpty()) {
            this.fields.add(field.trim());
        }
    }

    /**
     * 添加连接条件
     *
     * @param join 连接条件
     */
    public void addJoin(String join) {
        if (join != null && !join.trim().isEmpty()) {
            this.joins.add(join.trim());
        }
    }

    /**
     * 添加分组字段
     *
     * @param groupField 分组字段
     */
    public void addGroupBy(String groupField) {
        if (groupField != null && !groupField.trim().isEmpty()) {
            this.groupBy.add(groupField.trim());
        }
    }

    /**
     * 添加排序字段
     *
     * @param orderField 排序字段
     */
    public void addOrderBy(String orderField) {
        if (orderField != null && !orderField.trim().isEmpty()) {
            this.orderBy.add(orderField.trim());
        }
    }

    /**
     * 添加查询参数
     *
     * @param key 参数名
     * @param value 参数值
     */
    public void addParameter(String key, Object value) {
        if (key != null && !key.trim().isEmpty()) {
            this.parameters.put(key.trim(), value);
        }
    }

    /**
     * 验证查询模型是否有效
     *
     * @return true 如果模型有效，false 否则
     */
    public boolean isValid() {
        return !tables.isEmpty() && !fields.isEmpty();
    }

    /**
     * 清空所有集合
     */
    public void clear() {
        tables.clear();
        fields.clear();
        joins.clear();
        groupBy.clear();
        orderBy.clear();
        parameters.clear();
    }
}