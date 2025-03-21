package com.insightdata.domain.querybuilder.model;

import lombok.Data;

/**
 * 参数定义
 * 表示查询中使用的参数及其属性
 */
@Data
public class ParameterDefinition {
    /**
     * 参数名
     */
    private String name;

    /**
     * 参数类型
     */
    private String dataType;

    /**
     * 默认值
     */
    private Object defaultValue;

    /**
     * 是否必填
     */
    private boolean required;

    /**
     * 参数描述
     */
    private String description;

    /**
     * 验证规则（正则表达式）
     */
    private String validationPattern;

    /**
     * 所属的查询模型
     */
    private QueryModel queryModel;

    /**
     * 创建一个空的参数定义
     */
    public ParameterDefinition() {
    }

    /**
     * 使用基本信息创建参数定义
     *
     * @param name 参数名
     * @param dataType 参数类型
     */
    public ParameterDefinition(String name, String dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    /**
     * 使用完整信息创建参数定义
     *
     * @param name 参数名
     * @param dataType 参数类型
     * @param defaultValue 默认值
     * @param required 是否必填
     * @param queryModel 查询模型
     */
    public ParameterDefinition(String name, String dataType, Object defaultValue, 
                             boolean required, QueryModel queryModel) {
        this.name = name;
        this.dataType = dataType;
        this.defaultValue = defaultValue;
        this.required = required;
        this.queryModel = queryModel;
    }

    /**
     * 验证参数值是否有效
     *
     * @param value 要验证的值
     * @return true 如果值有效，false 否则
     */
    public boolean validateValue(Object value) {
        if (required && value == null) {
            return false;
        }

        if (value == null) {
            return true;
        }

        // 验证数据类型
        switch (dataType.toUpperCase()) {
            case "STRING":
                return value instanceof String && validatePattern((String) value);
            case "INTEGER":
                return value instanceof Integer || value instanceof Long;
            case "DECIMAL":
                return value instanceof Double || value instanceof Float;
            case "BOOLEAN":
                return value instanceof Boolean;
            case "DATE":
                return value instanceof java.util.Date || value instanceof java.time.LocalDate;
            case "DATETIME":
                return value instanceof java.util.Date || value instanceof java.time.LocalDateTime;
            default:
                return false;
        }
    }

    /**
     * 验证字符串值是否匹配正则表达式
     *
     * @param value 要验证的字符串
     * @return true 如果匹配或没有设置正则表达式，false 否则
     */
    private boolean validatePattern(String value) {
        if (validationPattern == null || validationPattern.trim().isEmpty()) {
            return true;
        }
        return value.matches(validationPattern);
    }

    /**
     * 创建参数定义的副本
     *
     * @return 新的参数定义实例
     */
    public ParameterDefinition copy() {
        ParameterDefinition copy = new ParameterDefinition();
        copy.setName(this.name);
        copy.setDataType(this.dataType);
        copy.setDefaultValue(this.defaultValue); // 注意：这是浅复制
        copy.setRequired(this.required);
        copy.setDescription(this.description);
        copy.setValidationPattern(this.validationPattern);
        copy.setQueryModel(this.queryModel); // 浅复制
        return copy;
    }

    /**
     * 验证参数定义是否有效
     *
     * @return true 如果参数定义有效，false 否则
     */
    public boolean isValid() {
        return name != null && !name.trim().isEmpty() &&
               dataType != null && !dataType.trim().isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" (").append(dataType).append(")");
        if (required) {
            sb.append(" [Required]");
        }
        if (defaultValue != null) {
            sb.append(" = ").append(defaultValue);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParameterDefinition)) return false;

        ParameterDefinition that = (ParameterDefinition) o;

        if (required != that.required) return false;
        if (!name.equals(that.name)) return false;
        return dataType.equals(that.dataType);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + dataType.hashCode();
        result = 31 * result + (required ? 1 : 0);
        return result;
    }
}