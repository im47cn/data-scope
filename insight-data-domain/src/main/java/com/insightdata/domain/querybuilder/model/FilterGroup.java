package com.insightdata.domain.querybuilder.model;

import com.insightdata.domain.querybuilder.api.QueryModelContract;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 过滤条件组
 * 表示一组过滤条件，支持AND/OR逻辑组合
 */
@Data
public class FilterGroup {
    /**
     * 过滤条件列表
     */
    private List<Filter> filters = new ArrayList<>();

    /**
     * 逻辑操作符（AND/OR）
     */
    private String operator = "AND";

    /**
     * 所属的查询模型
     */
    private QueryModelContract queryModel;

    /**
     * 嵌套的过滤条件组
     */
    private List<FilterGroup> groups = new ArrayList<>();

    /**
     * 创建一个空的过滤条件组
     */
    public FilterGroup() {
    }

    /**
     * 使用操作符创建过滤条件组
     *
     * @param operator 逻辑操作符
     */
    public FilterGroup(String operator) {
        this.operator = operator != null ? operator.toUpperCase() : "AND";
    }

    /**
     * 添加过滤条件
     *
     * @param filter 过滤条件
     */
    public void addFilter(Filter filter) {
        if (filter != null) {
            this.filters.add(filter);
        }
    }

    /**
     * 添加嵌套的过滤条件组
     *
     * @param group 过滤条件组
     */
    public void addGroup(FilterGroup group) {
        if (group != null) {
            this.groups.add(group);
        }
    }

    /**
     * 获取完整的过滤条件表达式
     *
     * @return 过滤条件表达式
     */
    public String getExpression() {
        List<String> expressions = new ArrayList<>();

        // 添加单个过滤条件
        expressions.addAll(filters.stream()
                .map(Filter::getExpression)
                .collect(Collectors.toList()));

        // 添加嵌套组的表达式
        expressions.addAll(groups.stream()
                .map(group -> "(" + group.getExpression() + ")")
                .collect(Collectors.toList()));

        // 使用操作符连接所有表达式
        return String.join(" " + operator + " ", expressions);
    }

    /**
     * 创建过滤条件组的副本
     *
     * @return 新的过滤条件组实例
     */
    public FilterGroup copy() {
        FilterGroup copy = new FilterGroup(this.operator);
        copy.setQueryModel(this.queryModel); // 浅复制

        // 深度复制过滤条件
        copy.setFilters(this.filters.stream()
                .map(Filter::copy)
                .collect(Collectors.toList()));

        // 深度复制嵌套组
        copy.setGroups(this.groups.stream()
                .map(FilterGroup::copy)
                .collect(Collectors.toList()));

        return copy;
    }

    /**
     * 验证过滤条件组是否有效
     *
     * @return true 如果过滤条件组有效，false 否则
     */
    public boolean isValid() {
        // 检查操作符
        if (operator == null || (!operator.equals("AND") && !operator.equals("OR"))) {
            return false;
        }

        // 检查是否至少有一个过滤条件或嵌套组
        if (filters.isEmpty() && groups.isEmpty()) {
            return false;
        }

        // 检查所有过滤条件是否有效
        if (!filters.stream().allMatch(Filter::isValid)) {
            return false;
        }

        // 检查所有嵌套组是否有效
        return groups.stream().allMatch(FilterGroup::isValid);
    }

    @Override
    public String toString() {
        return getExpression();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FilterGroup)) return false;

        FilterGroup that = (FilterGroup) o;

        if (!filters.equals(that.filters)) return false;
        if (!operator.equals(that.operator)) return false;
        return groups.equals(that.groups);
    }

    @Override
    public int hashCode() {
        int result = filters.hashCode();
        result = 31 * result + operator.hashCode();
        result = 31 * result + groups.hashCode();
        return result;
    }
}