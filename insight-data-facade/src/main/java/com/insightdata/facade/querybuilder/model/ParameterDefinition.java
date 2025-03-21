package com.insightdata.facade.querybuilder.model;

import lombok.Data;

@Data
public class ParameterDefinition {
    private String name;
    private String label;
    private String description;
    private DataType dataType;
    private String defaultValue;
    private boolean required;
    private boolean multiple;
    private String[] allowedValues;
    private String validationPattern;
    private String placeholder;
}