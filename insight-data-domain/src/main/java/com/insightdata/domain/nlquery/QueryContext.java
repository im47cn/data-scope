package com.insightdata.domain.nlquery;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueryContext {
    private String dataSourceId;
    private String nlQuery; // The preprocessed natural language query
    private String contextId;
    // Other fields as needed, like intent, entities, etc.

    public String getNlQuery(){
        return this.nlQuery;
    }
    public String getDataSourceId(){
        return this.dataSourceId;
    }

    public String getContextId() {
        return this.contextId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public void setNlQuery(String nlQuery) {
        this.nlQuery = nlQuery;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }
}