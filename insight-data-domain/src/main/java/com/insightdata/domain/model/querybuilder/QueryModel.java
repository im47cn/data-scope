package com.insightdata.domain.model.querybuilder;

import java.util.List;
import java.util.Map;

public class QueryModel {
    private String id;
    private String name;
    private List<TableReference> tables;
    private List<String> fields;
    private List<JoinCondition> joins;
    private Condition rootFilter;
    private List<String> groupBy;
    private List<String> orderBy;
    private Map<String, Object> parameters;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TableReference> getTables() {
        return tables;
    }

    public void setTables(List<TableReference> tables) {
        this.tables = tables;
    }
    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }
    public List<JoinCondition> getJoins() {
        return joins;
    }

    public void setJoins(List<JoinCondition> joins) {
        this.joins = joins;
    }

    public Condition getRootFilter() {
        return rootFilter;
    }

    public void setRootFilter(Condition rootFilter) {
        this.rootFilter = rootFilter;
    }

    public List<String> getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(List<String> groupBy) {
        this.groupBy = groupBy;
    }

    public List<String> getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(List<String> orderBy) {
        this.orderBy = orderBy;
    }
    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
}