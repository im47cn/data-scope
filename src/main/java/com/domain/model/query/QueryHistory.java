package com.domain.model.query;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Data;

@Data
public class QueryHistory {
    private String id;
    private String dataSourceId;
    private String originalQuery;
    private String executedSql;
    private Map<String, Object> parameters;
    private String queryType; // "NL" or "SAVED"
    private String status; // "成功" or "失败"
    private String errorMessage; // 错误信息
    private long executionTime; // 执行时间（毫秒）
    private long rowCount;
    private String userId;
    private String username;
    private LocalDateTime executedAt;
    private boolean isSaved; // 是否已保存为常用查询

    public void setSql(String sql){
        this.executedSql = sql;
    }

    public void setDuration(long duration){
        this.executionTime = duration;
    }

    public void setResultCount(long count){
        this.rowCount = count;
    }

    public void setSuccess(boolean success){
        if (success)
            this.status = "成功";
        else
            this.status = "失败";
    }
    public String getExecutedSql(){
        return this.executedSql;
    }

    public Map<String, Object> getParameters(){
        return this.parameters;
    }
    public String getDataSourceId(){
        return this.dataSourceId;
    }
    public String getOriginalQuery(){
        return this.originalQuery;
    }
    public String getId(){
        return this.id;
    }
    public String getStatus(){
        return this.status;
    }
    public long getRowCount(){
        return this.rowCount;
    }
    public String getErrorMessage(){
        return this.errorMessage;
    }
    public long getExecutionTime(){
        return this.executionTime;
    }
    public boolean getIsSaved(){
        return this.isSaved;
    }
    public void setIsSaved(boolean isSaved){
        this.isSaved = isSaved;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }
     public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
     }
    public void setUserId(String userId){
        this.userId = userId;
    }

    public void  setUsername(String username){
        this.username = username;
    }
    public void setExecutedAt(LocalDateTime executedAt){
        this.executedAt = executedAt;
    }
    public void setParameters(Map<String, Object> parameters){
        this.parameters = parameters;
    }
}
