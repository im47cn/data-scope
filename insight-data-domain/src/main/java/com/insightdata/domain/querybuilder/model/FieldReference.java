package com.insightdata.domain.querybuilder.model;

import com.insightdata.domain.querybuilder.api.QueryModelContract;
import lombok.Data;

/**
 * 字段引用
 * 表示对数据表中字段的引用
 */
@Data
public class FieldReference {
    /**
     * 表引用
     */
    private TableReference table;

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 所属的查询模型
     */
    private QueryModelContract queryModel;

    /**
     * 字段描述
     */
    private String description;

    /**
     * 字段类型
     */
    private String dataType;

    /**
     * 创建一个空的字段引用
     */
    public FieldReference() {
    }

    /**
     * 使用表引用和字段名创建字段引用
     *
     * @param table 表引用
     * @param fieldName 字段名
     */
    public FieldReference(TableReference table, String fieldName) {
        this.table = table;
        this.fieldName = fieldName;
    }

    /**
     * 使用完整信息创建字段引用
     *
     * @param table 表引用
     * @param fieldName 字段名
     * @param queryModel 查询模型
     */
    public FieldReference(TableReference table, String fieldName, QueryModelContract queryModel) {
        this.table = table;
        this.fieldName = fieldName;
        this.queryModel = queryModel;
    }

    /**
     * 获取完整的字段引用表达式
     *
     * @return 字段引用表达式
     */
    public String getFullReference() {
        if (!isValid()) {
            return "";
        }

        String tableRef = table.getReferenceIdentifier();
        return tableRef + "." + fieldName;
    }

    /**
     * 创建字段引用的副本
     *
     * @return 新的字段引用实例
     */
    public FieldReference copy() {
        FieldReference copy = new FieldReference();
        copy.setTable(this.table != null ? this.table.copy() : null);
        copy.setFieldName(this.fieldName);
        copy.setQueryModel(this.queryModel); // 浅复制
        copy.setDescription(this.description);
        copy.setDataType(this.dataType);
        return copy;
    }

    /**
     * 验证字段引用是否有效
     *
     * @return true 如果字段引用有效，false 否则
     */
    public boolean isValid() {
        return table != null && table.isValid() &&
               fieldName != null && !fieldName.trim().isEmpty();
    }

    @Override
    public String toString() {
        return getFullReference();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FieldReference)) return false;

        FieldReference that = (FieldReference) o;

        if (table != null ? !table.equals(that.table) : that.table != null) return false;
        return fieldName != null ? fieldName.equals(that.fieldName) : that.fieldName == null;
    }

    @Override
    public int hashCode() {
        int result = table != null ? table.hashCode() : 0;
        result = 31 * result + (fieldName != null ? fieldName.hashCode() : 0);
        return result;
    }
}