package com.insightdata.facade.querybuilder.model;

import lombok.Data;

@Data
public class GroupByClause {
    private String field;
    private String expression;
    private String rollup;
    private String cube;
    private String[] groupingSets;
    private FilterGroup having;
    private String format;
    private String[] tags;
}