package com.insightdata.facade.querybuilder.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryModel implements QueryModelContract {
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

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getDataSourceId() {
        return this.dataSourceId;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public List<TableReference> getTables() {
        return this.tables;
    }

    @Override
    public List<FieldReference> getFields() {
        return this.fields;
    }

    @Override
    public List<JoinDefinition> getJoins() {
        return this.joins;
    }

    @Override
    public FilterGroup getFilters() {
        return this.filters;
    }

    @Override
    public List<GroupByClause> getGroupBy() {
        return this.groupBy;
    }

    @Override
    public List<OrderByClause> getOrderBy() {
        return this.orderBy;
    }

    @Override
    public List<ParameterDefinition> getParameters() {
        return this.parameters;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setTables(List<TableReference> tables) {
        this.tables = tables;
    }

    @Override
    public void setFields(List<FieldReference> fields) {
        this.fields = fields;
    }

    @Override
    public void setJoins(List<JoinDefinition> joins) {
        this.joins = joins;
    }

    @Override
    public void setFilters(FilterGroup filters) {
        this.filters = filters;
    }

    @Override
    public void setGroupBy(List<GroupByClause> groupBy) {
        this.groupBy = groupBy;
    }

    @Override
    public void setOrderBy(List<OrderByClause> orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public void setParameters(List<ParameterDefinition> parameters) {
        this.parameters = parameters;
    }
}