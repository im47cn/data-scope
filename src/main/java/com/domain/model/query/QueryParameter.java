package com.domain.model.query;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryParameter {
    /**
     * 参数名
     */
    private String name;

    /**
     * 参数类型
     */
    private String type;

    /**
     * 参数描述
     */
    private String description;

    /**
     * 是否必填
     */
    private boolean required;

    /**
     * 验证规则
     */
    private Map<String, Object> validation;

    /**
     * 可选值列表
     */
    private List<ParameterOption> options;

    /**
     * 默认值
     */
    private Object defaultValue;
}