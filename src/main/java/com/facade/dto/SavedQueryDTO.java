package com.facade.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 查询模板DTO
 */
@Data
public class SavedQueryDTO {

    private String id;

    @NotNull(message = "数据源ID不能为空")
    private String dataSourceId;

    @NotBlank(message = "查询名称不能为空")
    @Size(max = 100, message = "查询名称长度不能超过100个字符")
    private String name;

    @Size(max = 500, message = "描述长度不能超过500个字符")
    private String description;

    @NotBlank(message = "查询语句不能为空")
    private String sql;

    private List<ParameterDTO> parameters;

    private Map<String, Object> defaultValues;

    private List<String> tags;

    private boolean isPublic;

    private String createdBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private int usageCount;

    private long averageExecutionTime;

    private LocalDateTime lastExecutedAt;
}

/**
 * 查询参数DTO
 */
@Data
class ParameterDTO {
    
    @NotBlank(message = "参数名不能为空")
    private String name;

    @NotBlank(message = "参数类型不能为空")
    private String type;

    private String description;

    private boolean required;

    private Map<String, Object> validation;

    private List<ParameterOptionDTO> options;

    private Object defaultValue;
}

/**
 * 参数选项DTO
 */
@Data
class ParameterOptionDTO {

    @NotNull(message = "选项值不能为空")
    private Object value;

    @NotBlank(message = "选项标签不能为空")
    private String label;

    private String description;
}