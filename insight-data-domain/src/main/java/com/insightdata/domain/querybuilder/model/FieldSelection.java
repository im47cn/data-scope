package com.insightdata.domain.querybuilder.model;

import lombok.Data;

/**
 * 字段选择
 * 表示查询中要选择的字段及其属性
 */
@Data
public class FieldSelection {
    /**
     * 字段引用
     */
    private FieldReference field;

    /**
     * 字段别名
     */
    private String alias;

    /**
     * 聚合函数（SUM, COUNT, AVG等）
     */
    private String aggregateFunction;

    /**
     * 所属的查询模型
     */
    private QueryModel queryModel;

    /**
     * 字段描述
     */
    private String description;

    /**
     * 是否为计算字段
     */
    private boolean isCalculated;

    /**
     * 计算表达式（用于计算字段）
     */
    private String expression;

    /**
     * 创建一个空的字段选择
     */
    public FieldSelection() {
    }

    /**
     * 使用字段引用创建字段选择
     *
     * @param field 字段引用
     */
    public FieldSelection(FieldReference field) {
        this.field = field;
    }

    /**
     * 使用字段引用和别名创建字段选择
     *
     * @param field 字段引用
     * @param alias 别名
     */
    public FieldSelection(FieldReference field, String alias) {
        this.field = field;
        this.alias = alias;
    }

    /**
     * 使用完整信息创建字段选择
     *
     * @param field 字段引用
     * @param alias 别名
     * @param aggregateFunction 聚合函数
     * @param queryModel 查询模型
     */
    public FieldSelection(FieldReference field, String alias, 
                         String aggregateFunction, QueryModel queryModel) {
        this.field = field;
        this.alias = alias;
        this.aggregateFunction = aggregateFunction;
        this.queryModel = queryModel;
    }

    /**
     * 获取完整的字段选择表达式
     *
     * @return 字段选择表达式
     */
    public String getExpression() {
        if (!isValid()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        if (isCalculated) {
            sb.append(this.expression);
        } else if (aggregateFunction != null && !aggregateFunction.trim().isEmpty()) {
            sb.append(aggregateFunction)
              .append("(")
              .append(field.getFullReference())
              .append(")");
        } else {
            sb.append(field.getFullReference());
        }

        if (alias != null && !alias.trim().isEmpty()) {
            sb.append(" AS ").append(alias);
        }

        return sb.toString();
    }

    /**
     * 创建字段选择的副本
     *
     * @return 新的字段选择实例
     */
    public FieldSelection copy() {
        FieldSelection copy = new FieldSelection();
        copy.setField(this.field != null ? this.field.copy() : null);
        copy.setAlias(this.alias);
        copy.setAggregateFunction(this.aggregateFunction);
        copy.setQueryModel(this.queryModel); // 浅复制
        copy.setDescription(this.description);
        copy.setCalculated(this.isCalculated);
        copy.setExpression(this.expression);
        return copy;
    }

    /**
     * 验证字段选择是否有效
     *
     * @return true 如果字段选择有效，false 否则
     */
    public boolean isValid() {
        if (isCalculated) {
            return expression != null && !expression.trim().isEmpty();
        }
        return field != null && field.isValid();
    }

    @Override
    public String toString() {
        return getExpression();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FieldSelection)) return false;

        FieldSelection that = (FieldSelection) o;

        if (isCalculated != that.isCalculated) return false;
        if (field != null ? !field.equals(that.field) : that.field != null) return false;
        if (alias != null ? !alias.equals(that.alias) : that.alias != null) return false;
        if (aggregateFunction != null ? !aggregateFunction.equals(that.aggregateFunction) : that.aggregateFunction != null)
            return false;
        return expression != null ? expression.equals(that.expression) : that.expression == null;
    }

    @Override
    public int hashCode() {
        int result = field != null ? field.hashCode() : 0;
        result = 31 * result + (alias != null ? alias.hashCode() : 0);
        result = 31 * result + (aggregateFunction != null ? aggregateFunction.hashCode() : 0);
        result = 31 * result + (isCalculated ? 1 : 0);
        result = 31 * result + (expression != null ? expression.hashCode() : 0);
        return result;
    }
}