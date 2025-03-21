package com.insightdata.domain.querybuilder.model;

import lombok.Data;

/**
 * 连接条件
 * 表示查询中表之间的连接关系
 */
@Data
public class JoinCondition {
    /**
     * 左表引用
     */
    private TableReference leftTable;

    /**
     * 右表引用
     */
    private TableReference rightTable;

    /**
     * 连接类型（INNER, LEFT, RIGHT, FULL）
     */
    private String joinType;

    /**
     * 连接条件表达式
     */
    private String condition;

    /**
     * 所属的查询模型
     */
    private QueryModel queryModel;

    /**
     * 连接描述
     */
    private String description;

    /**
     * 创建一个空的连接条件
     */
    public JoinCondition() {
        this.joinType = "INNER";
    }

    /**
     * 使用基本信息创建连接条件
     *
     * @param leftTable 左表引用
     * @param rightTable 右表引用
     * @param condition 连接条件
     */
    public JoinCondition(TableReference leftTable, TableReference rightTable, String condition) {
        this.leftTable = leftTable;
        this.rightTable = rightTable;
        this.condition = condition;
        this.joinType = "INNER";
    }

    /**
     * 使用完整信息创建连接条件
     *
     * @param leftTable 左表引用
     * @param rightTable 右表引用
     * @param joinType 连接类型
     * @param condition 连接条件
     * @param queryModel 查询模型
     */
    public JoinCondition(TableReference leftTable, TableReference rightTable, 
                        String joinType, String condition, QueryModel queryModel) {
        this.leftTable = leftTable;
        this.rightTable = rightTable;
        this.joinType = joinType != null ? joinType : "INNER";
        this.condition = condition;
        this.queryModel = queryModel;
    }

    /**
     * 获取完整的连接语句
     *
     * @return 连接语句
     */
    public String getJoinStatement() {
        StringBuilder sb = new StringBuilder();
        sb.append(joinType).append(" JOIN ");
        sb.append(rightTable.getFullReference());
        sb.append(" ON ").append(condition);
        return sb.toString();
    }

    /**
     * 创建连接条件的副本
     *
     * @return 新的连接条件实例
     */
    public JoinCondition copy() {
        JoinCondition copy = new JoinCondition();
        copy.setLeftTable(this.leftTable != null ? this.leftTable.copy() : null);
        copy.setRightTable(this.rightTable != null ? this.rightTable.copy() : null);
        copy.setJoinType(this.joinType);
        copy.setCondition(this.condition);
        copy.setQueryModel(this.queryModel); // 注意：这里是浅复制
        copy.setDescription(this.description);
        return copy;
    }

    /**
     * 验证连接条件是否有效
     *
     * @return true 如果连接条件有效，false 否则
     */
    public boolean isValid() {
        return leftTable != null && leftTable.isValid() &&
               rightTable != null && rightTable.isValid() &&
               condition != null && !condition.trim().isEmpty();
    }

    @Override
    public String toString() {
        return getJoinStatement();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JoinCondition)) return false;

        JoinCondition that = (JoinCondition) o;

        if (leftTable != null ? !leftTable.equals(that.leftTable) : that.leftTable != null) return false;
        if (rightTable != null ? !rightTable.equals(that.rightTable) : that.rightTable != null) return false;
        if (joinType != null ? !joinType.equals(that.joinType) : that.joinType != null) return false;
        return condition != null ? condition.equals(that.condition) : that.condition == null;
    }

    @Override
    public int hashCode() {
        int result = leftTable != null ? leftTable.hashCode() : 0;
        result = 31 * result + (rightTable != null ? rightTable.hashCode() : 0);
        result = 31 * result + (joinType != null ? joinType.hashCode() : 0);
        result = 31 * result + (condition != null ? condition.hashCode() : 0);
        return result;
    }
}