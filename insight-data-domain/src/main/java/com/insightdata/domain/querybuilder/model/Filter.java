package com.insightdata.domain.querybuilder.model;

import com.insightdata.domain.querybuilder.api.QueryModelContract;
import lombok.Data;

/**
 * 过滤条件
 * 表示单个过滤条件
 */
@Data
public class Filter {
    /**
     * 字段名
     */
    private String field;

    /**
     * 操作符（=, >, <, LIKE 等）
     */
    private String operator;

    /**
     * 值
     */
    private Object value;

    /**
     * 所属的查询模型
     */
    private QueryModelContract queryModel;

    /**
     * 过滤条件描述
     */
    private String description;

    /**
     * 创建一个空的过滤条件
     */
    public Filter() {
    }

    /**
     * 使用基本信息创建过滤条件
     *
     * @param field 字段名
     * @param operator 操作符
     * @param value 值
     */
    public Filter(String field, String operator, Object value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    /**
     * 使用完整信息创建过滤条件
     *
     * @param field 字段名
     * @param operator 操作符
     * @param value 值
     * @param queryModel 查询模型
     */
    public Filter(String field, String operator, Object value, QueryModelContract queryModel) {
        this.field = field;
        this.operator = operator;
        this.value = value;
        this.queryModel = queryModel;
    }

    /**
     * 获取过滤条件表达式
     *
     * @return 过滤条件表达式
     */
    public String getExpression() {
        if (!isValid()) {
            return "";
        }

        // 处理特殊操作符
        switch (operator.toUpperCase()) {
            case "IS NULL":
                return field + " IS NULL";
            case "IS NOT NULL":
                return field + " IS NOT NULL";
            case "IN":
                return formatInCondition();
            case "BETWEEN":
                return formatBetweenCondition();
            case "LIKE":
                return formatLikeCondition();
            default:
                return formatDefaultCondition();
        }
    }

    /**
     * 格式化 IN 条件
     */
    private String formatInCondition() {
        if (!(value instanceof Iterable)) {
            return "";
        }
        StringBuilder sb = new StringBuilder(field).append(" IN (");
        Iterable<?> values = (Iterable<?>) value;
        boolean first = true;
        for (Object val : values) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(formatValue(val));
            first = false;
        }
        return sb.append(")").toString();
    }

    /**
     * 格式化 BETWEEN 条件
     */
    private String formatBetweenCondition() {
        if (!(value instanceof Object[]) || ((Object[]) value).length != 2) {
            return "";
        }
        Object[] range = (Object[]) value;
        return String.format("%s BETWEEN %s AND %s",
                field,
                formatValue(range[0]),
                formatValue(range[1]));
    }

    /**
     * 格式化 LIKE 条件
     */
    private String formatLikeCondition() {
        return String.format("%s LIKE %s",
                field,
                formatValue(value));
    }

    /**
     * 格式化默认条件
     */
    private String formatDefaultCondition() {
        return String.format("%s %s %s",
                field,
                operator,
                formatValue(value));
    }

    /**
     * 格式化值
     */
    private String formatValue(Object val) {
        if (val == null) {
            return "NULL";
        }
        if (val instanceof Number) {
            return val.toString();
        }
        if (val instanceof Boolean) {
            return val.toString();
        }
        return "'" + val.toString().replace("'", "''") + "'";
    }

    /**
     * 创建过滤条件的副本
     *
     * @return 新的过滤条件实例
     */
    public Filter copy() {
        Filter copy = new Filter();
        copy.setField(this.field);
        copy.setOperator(this.operator);
        copy.setValue(this.value); // 注意：这是浅复制
        copy.setQueryModel(this.queryModel); // 浅复制
        copy.setDescription(this.description);
        return copy;
    }

    /**
     * 验证过滤条件是否有效
     *
     * @return true 如果过滤条件有效，false 否则
     */
    public boolean isValid() {
        if (field == null || field.trim().isEmpty()) {
            return false;
        }
        if (operator == null || operator.trim().isEmpty()) {
            return false;
        }
        // IS NULL 和 IS NOT NULL 不需要值
        String op = operator.toUpperCase();
        if (op.equals("IS NULL") || op.equals("IS NOT NULL")) {
            return true;
        }
        return value != null;
    }

    @Override
    public String toString() {
        return getExpression();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Filter)) return false;

        Filter filter = (Filter) o;

        if (!field.equals(filter.field)) return false;
        if (!operator.equals(filter.operator)) return false;
        return value != null ? value.equals(filter.value) : filter.value == null;
    }

    @Override
    public int hashCode() {
        int result = field.hashCode();
        result = 31 * result + operator.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}