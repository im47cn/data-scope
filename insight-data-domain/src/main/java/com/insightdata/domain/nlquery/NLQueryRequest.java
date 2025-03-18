package com.insightdata.domain.nlquery;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class NLQueryRequest {
    private String query;
    private String dataSourceId;
    private Map<String, Object> parameters;
    private List<String> tags; // Assuming tags are relevant here.

   public String getQuery(){
       return this.query;
   }

   public String getDataSourceId(){
       return this.dataSourceId;
   }
   public Map<String, Object> getParameters(){
       return this.parameters;
   }

   public List<String> getTags(){
       return this.tags;
   }

    public Integer getMaxRows() {
        return 1000;
    }

    public  Map<String, Object> getOptions(){
        return new HashMap<>();
    }

    public String getContextId(){
        return "";
    }

    public Boolean getIncludeSqlExplanation(){
        return false;
    }
}