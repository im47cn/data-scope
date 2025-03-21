package com.insightdata.facade.querybuilder.model;

import lombok.Data;

@Data
public class JoinDefinition {
    public enum JoinType {
        INNER,
        LEFT,
        RIGHT,
        FULL,
        CROSS
    }

    private JoinType type;
    private TableReference table;
    private String condition;
    private FilterGroup filters;
    private boolean required;
    private String[] matchedColumns;
    private double confidence;
    private String source;
}