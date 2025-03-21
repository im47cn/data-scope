package com.insightdata.domain.querybuilder.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;

/**
 * 连接定义
 * 表示查询中的表连接关系定义
 */
@Data
public class JoinDefinition {
    /**
     * 主表引用
     */
    private TableReference primaryTable;

    /**
     * 连接条件列表
     */
    private List<JoinCondition> joins = new ArrayList<>();

    /**
     * 所属的查询模型
     */
    private QueryModel queryModel;

    /**
     * 连接描述
     */
    private String description;

    /**
     * 是否强制使用内连接
     */
    private boolean forceInnerJoin;

    /**
     * 创建一个空的连接定义
     */
    public JoinDefinition() {
    }

    /**
     * 使用主表创建连接定义
     *
     * @param primaryTable 主表引用
     */
    public JoinDefinition(TableReference primaryTable) {
        this.primaryTable = primaryTable;
    }

    /**
     * 使用完整信息创建连接定义
     *
     * @param primaryTable 主表引用
     * @param queryModel 查询模型
     * @param forceInnerJoin 是否强制使用内连接
     */
    public JoinDefinition(TableReference primaryTable, QueryModel queryModel,
                         boolean forceInnerJoin) {
        this.primaryTable = primaryTable;
        this.queryModel = queryModel;
        this.forceInnerJoin = forceInnerJoin;
    }

    /**
     * 添加连接条件
     *
     * @param join 连接条件
     */
    public void addJoin(JoinCondition join) {
        if (join != null && join.isValid()) {
            if (forceInnerJoin) {
                join.setJoinType("INNER");
            }
            this.joins.add(join);
        }
    }

    /**
     * 获取完整的连接语句
     *
     * @return 连接语句
     */
    public String getJoinStatement() {
        if (!isValid()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(primaryTable.getFullReference());

        // 按照添加顺序生成连接语句
        for (JoinCondition join : joins) {
            sb.append("\n").append(join.getJoinStatement());
        }

        return sb.toString();
    }

    /**
     * 获取所有涉及的表引用
     *
     * @return 表引用列表
     */
    public List<TableReference> getAllTables() {
        List<TableReference> tables = new ArrayList<>();
        tables.add(primaryTable);
        
        for (JoinCondition join : joins) {
            tables.add(join.getRightTable());
        }
        
        return tables;
    }

    /**
     * 创建连接定义的副本
     *
     * @return 新的连接定义实例
     */
    public JoinDefinition copy() {
        JoinDefinition copy = new JoinDefinition();
        copy.setPrimaryTable(this.primaryTable != null ? this.primaryTable.copy() : null);
        copy.setJoins(this.joins.stream()
                .map(JoinCondition::copy)
                .collect(Collectors.toList()));
        copy.setQueryModel(this.queryModel); // 浅复制
        copy.setDescription(this.description);
        copy.setForceInnerJoin(this.forceInnerJoin);
        return copy;
    }

    /**
     * 验证连接定义是否有效
     *
     * @return true 如果连接定义有效，false 否则
     */
    public boolean isValid() {
        if (primaryTable == null || !primaryTable.isValid()) {
            return false;
        }

        // 如果没有连接条件，只有主表的情况也是有效的
        if (joins.isEmpty()) {
            return true;
        }

        // 验证所有连接条件
        return joins.stream().allMatch(JoinCondition::isValid);
    }

    /**
     * 验证是否存在循环连接
     *
     * @return true 如果存在循环连接，false 否则
     */
    public boolean hasCircularJoins() {
        List<String> tableNames = new ArrayList<>();
        tableNames.add(primaryTable.getTableName());

        for (JoinCondition join : joins) {
            String tableName = join.getRightTable().getTableName();
            if (tableNames.contains(tableName)) {
                return true;
            }
            tableNames.add(tableName);
        }

        return false;
    }

    @Override
    public String toString() {
        return getJoinStatement();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JoinDefinition)) return false;

        JoinDefinition that = (JoinDefinition) o;

        if (forceInnerJoin != that.forceInnerJoin) return false;
        if (!primaryTable.equals(that.primaryTable)) return false;
        return joins.equals(that.joins);
    }

    @Override
    public int hashCode() {
        int result = primaryTable.hashCode();
        result = 31 * result + joins.hashCode();
        result = 31 * result + (forceInnerJoin ? 1 : 0);
        return result;
    }
}