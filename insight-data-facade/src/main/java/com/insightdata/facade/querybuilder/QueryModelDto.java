package com.insightdata.facade.querybuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryModelDto implements QueryModelContract {
    private String id;
    private String name;
    private List<String> tables = new ArrayList<>();
    private List<String> fields = new ArrayList<>();
    private List<String> joins = new ArrayList<>();
    private String filter;
    private List<String> groupBy = new ArrayList<>();
    private List<String> orderBy = new ArrayList<>();
    private Map<String, Object> parameters = new HashMap<>();

    public QueryModelDto() {
    }

    public QueryModelDto(QueryModelContract contract) {
        if (contract != null) {
            this.id = contract.getId();
            this.name = contract.getName();
            this.tables = new ArrayList<>(contract.getTables());
            this.fields = new ArrayList<>(contract.getFields());
            this.joins = new ArrayList<>(contract.getJoins());
            this.filter = contract.getFilter();
            this.groupBy = new ArrayList<>(contract.getGroupBy());
            this.orderBy = new ArrayList<>(contract.getOrderBy());
            this.parameters = new HashMap<>(contract.getParameters());
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<String> getTables() {
        return tables;
    }

    @Override
    public void setTables(List<String> tables) {
        this.tables = tables != null ? new ArrayList<>(tables) : new ArrayList<>();
    }

    @Override
    public List<String> getFields() {
        return fields;
    }

    @Override
    public void setFields(List<String> fields) {
        this.fields = fields != null ? new ArrayList<>(fields) : new ArrayList<>();
    }

    @Override
    public List<String> getJoins() {
        return joins;
    }

    @Override
    public void setJoins(List<String> joins) {
        this.joins = joins != null ? new ArrayList<>(joins) : new ArrayList<>();
    }

    @Override
    public String getFilter() {
        return filter;
    }

    @Override
    public void setFilter(String filter) {
        this.filter = filter;
    }

    @Override
    public List<String> getGroupBy() {
        return groupBy;
    }

    @Override
    public void setGroupBy(List<String> groupBy) {
        this.groupBy = groupBy != null ? new ArrayList<>(groupBy) : new ArrayList<>();
    }

    @Override
    public List<String> getOrderBy() {
        return orderBy;
    }

    @Override
    public void setOrderBy(List<String> orderBy) {
        this.orderBy = orderBy != null ? new ArrayList<>(orderBy) : new ArrayList<>();
    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters != null ? new HashMap<>(parameters) : new HashMap<>();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final QueryModelDto dto;

        private Builder() {
            dto = new QueryModelDto();
        }

        public Builder id(String id) {
            dto.setId(id);
            return this;
        }

        public Builder name(String name) {
            dto.setName(name);
            return this;
        }

        public Builder tables(List<String> tables) {
            dto.setTables(tables);
            return this;
        }

        public Builder fields(List<String> fields) {
            dto.setFields(fields);
            return this;
        }

        public Builder joins(List<String> joins) {
            dto.setJoins(joins);
            return this;
        }

        public Builder filter(String filter) {
            dto.setFilter(filter);
            return this;
        }

        public Builder groupBy(List<String> groupBy) {
            dto.setGroupBy(groupBy);
            return this;
        }

        public Builder orderBy(List<String> orderBy) {
            dto.setOrderBy(orderBy);
            return this;
        }

        public Builder parameters(Map<String, Object> parameters) {
            dto.setParameters(parameters);
            return this;
        }

        public QueryModelDto build() {
            return dto;
        }
    }
}