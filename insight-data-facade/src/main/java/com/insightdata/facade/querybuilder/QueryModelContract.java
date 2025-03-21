package com.insightdata.facade.querybuilder;

import java.util.List;
import java.util.Map;

/**
 * Contract interface for query model
 */
public interface QueryModelContract {
    String getId();
    void setId(String id);
    
    String getName();
    void setName(String name);
    
    List<String> getTables();
    void setTables(List<String> tables);
    
    List<String> getFields();
    void setFields(List<String> fields);
    
    List<String> getJoins();
    void setJoins(List<String> joins);
    
    String getFilter();
    void setFilter(String filter);
    
    List<String> getGroupBy();
    void setGroupBy(List<String> groupBy);
    
    List<String> getOrderBy();
    void setOrderBy(List<String> orderBy);
    
    Map<String, Object> getParameters();
    void setParameters(Map<String, Object> parameters);
}