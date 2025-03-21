package com.insightdata.facade.querybuilder.model;

import java.util.List;

import lombok.Data;

@Data
public class QueryModel {

    private String id;
    private String dataSourceId;
    private String name;
    private String description;
    private List<TableReference> tables;
    private List<FieldReference> fields;
    private List<JoinDefinition> joins;
    private FilterGroup filters;
    private List<GroupByClause> groupBy;
    private List<OrderByClause> orderBy;
    private List<ParameterDefinition> parameters;

}