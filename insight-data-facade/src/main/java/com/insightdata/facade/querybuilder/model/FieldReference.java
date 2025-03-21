package com.insightdata.facade.querybuilder.model;

import lombok.Data;

@Data
public class FieldReference {
    private String tableAlias;
    private String name;
    private String alias;
    private String description;
    private DataType dataType;
    private int length;
    private int precision;
    private int scale;
    private boolean nullable;
    private String defaultValue;
    private boolean autoIncrement;
    private String expression;
    private String format;
    private String[] tags;
}