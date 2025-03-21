package com.insightdata.domain.querybuilder.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.insightdata.facade.querybuilder.QueryModelContract;

/**
 * 查询模型领域对象
 * 实现 QueryModelContract 接口，用于 Domain 层业务逻辑
 */
public class QueryModel implements QueryModelContract {
    private String id;
    private String name;
    private List<String> tables = new ArrayList<>();
    private List<String> fields = new ArrayList<>();
    private List<String> joins = new ArrayList<>();
    private String filter;
    private List<String> groupBy = new ArrayList<>();
    private List<String> orderBy = new ArrayList<>();
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
            setTables(contract.getTables());
            setFields(contract.getFields());
            setJoins(contract.getJoins());
            this.filter = contract.getFilter();
            setGroupBy(contract.getGroupBy());
            setOrderBy(contract.getOrderBy());
            setParameters(contract.getParameters());
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<String> getTables() {
        return tables;
    }

    @Override
    public void setTables(List<String> tables) {
        this.tables = tables != null ? new ArrayList<>(tables) : new ArrayList<>();
    }

    @Override
    public List<String> getFields() {
        return fields;
    }

    @Override
    public void setFields(List<String> fields) {
        this.fields = fields != null ? new ArrayList<>(fields) : new ArrayList<>();
    }

    @Override
    public List<String> getJoins() {
        return joins;
    }

    @Override
    public void setJoins(List<String> joins) {
        this.joins = joins != null ? new ArrayList<>(joins) : new ArrayList<>();
    }

    @Override
    public String getFilter() {
        return filter;
    }

    @Override
    public void setFilter(String filter) {
        this.filter = filter;
    }

    @Override
    public List<String> getGroupBy() {
        return groupBy;
    }

    @Override
    public void setGroupBy(List<String> groupBy) {
        this.groupBy = groupBy != null ? new ArrayList<>(groupBy) : new ArrayList<>();
    }

    @Override
    public List<String> getOrderBy() {
        return orderBy;
    }

    @Override
    public void setOrderBy(List<String> orderBy) {
        this.orderBy = orderBy != null ? new ArrayList<>(orderBy) : new ArrayList<>();
    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters != null ? new HashMap<>(parameters) : new HashMap<>();
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