package com.insightdata.domain.querybuilder.model;

import lombok.Data;

/**
 * 表引用
 * 表示查询中使用的表及其别名
 */
@Data
public class TableReference {
    /**
     * 表名
     */
    private String tableName;

    /**
     * 表别名
     */
    private String alias;

    /**
     * 所属的查询模型
     */
    private QueryModel queryModel;

    /**
     * 表描述
     */
    private String description;

    /**
     * 是否是主表
     */
    private boolean isPrimary;

    /**
     * 创建一个空的表引用
     */
    public TableReference() {
    }

    /**
     * 使用表名和别名创建表引用
     *
     * @param tableName 表名
     * @param alias 表别名
     */
    public TableReference(String tableName, String alias) {
        this.tableName = tableName;
        this.alias = alias;
    }

    /**
     * 使用表名、别名和查询模型创建表引用
     *
     * @param tableName 表名
     * @param alias 表别名
     * @param queryModel 查询模型
     */
    public TableReference(String tableName, String alias, QueryModel queryModel) {
        this.tableName = tableName;
        this.alias = alias;
        this.queryModel = queryModel;
    }

    /**
     * 获取完整的表引用名
     * 如果有别名，返回 "表名 AS 别名"
     * 如果没有别名，返回表名
     *
     * @return 完整的表引用名
     */
    public String getFullReference() {
        if (alias != null && !alias.trim().isEmpty()) {
            return tableName + " AS " + alias;
        }
        return tableName;
    }

    /**
     * 获取用于引用的名称
     * 如果有别名，返回别名
     * 如果没有别名，返回表名
     *
     * @return 用于引用的名称
     */
    public String getReferenceIdentifier() {
        if (alias != null && !alias.trim().isEmpty()) {
            return alias;
        }
        return tableName;
    }

    /**
     * 创建表引用的副本
     *
     * @return 新的表引用实例
     */
    public TableReference copy() {
        TableReference copy = new TableReference();
        copy.setTableName(this.tableName);
        copy.setAlias(this.alias);
        copy.setQueryModel(this.queryModel); // 注意：这里是浅复制
        copy.setDescription(this.description);
        copy.setPrimary(this.isPrimary);
        return copy;
    }

    /**
     * 验证表引用是否有效
     *
     * @return true 如果表引用有效，false 否则
     */
    public boolean isValid() {
        return tableName != null && !tableName.trim().isEmpty();
    }

    @Override
    public String toString() {
        return getFullReference();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TableReference)) return false;

        TableReference that = (TableReference) o;

        if (tableName != null ? !tableName.equals(that.tableName) : that.tableName != null) return false;
        return alias != null ? alias.equals(that.alias) : that.alias == null;
    }

    @Override
    public int hashCode() {
        int result = tableName != null ? tableName.hashCode() : 0;
        result = 31 * result + (alias != null ? alias.hashCode() : 0);
        return result;
    }
}