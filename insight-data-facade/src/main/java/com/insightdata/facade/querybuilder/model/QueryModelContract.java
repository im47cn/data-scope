package com.insightdata.facade.querybuilder.model;

import java.util.List;

public interface QueryModelContract {
    String getId();
    void setId(String id);
    
    String getDataSourceId();
    void setDataSourceId(String dataSourceId);
    
    String getName();
    void setName(String name);
    
    String getDescription();
    void setDescription(String description);
    
    List<TableReference> getTables();
    void setTables(List<TableReference> tables);
    
    List<FieldReference> getFields();
    void setFields(List<FieldReference> fields);
    
    List<JoinDefinition> getJoins();
    void setJoins(List<JoinDefinition> joins);
    
    FilterGroup getFilters();
    void setFilters(FilterGroup filters);
    
    List<GroupByClause> getGroupBy();
    void setGroupBy(List<GroupByClause> groupBy);
    
    List<OrderByClause> getOrderBy();
    void setOrderBy(List<OrderByClause> orderBy);
    
    List<ParameterDefinition> getParameters();
    void setParameters(List<ParameterDefinition> parameters);
}