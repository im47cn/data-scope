package com.insightdata.facade.rest.dto;

import com.insightdata.domain.model.query.NLQueryRequest;
import com.insightdata.domain.model.query.QueryResult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 保存查询请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveQueryRequestDTO {
    
    /**
     * 查询名称
     */
    private String name;
    
    /**
     * 自然语言查询请求
     */
    private NLQueryRequest request;
    
    /**
     * 查询结果
     */
    private QueryResult result;
    
    // 手动添加getter方法
    public String getName() {
        return name;
    }
    
    public NLQueryRequest getRequest() {
        return request;
    }
    
    public QueryResult getResult() {
        return result;
    }
    
    // 手动添加setter方法
    public void setName(String name) {
        this.name = name;
    }
    
    public void setRequest(NLQueryRequest request) {
        this.request = request;
    }
    
    public void setResult(QueryResult result) {
        this.result = result;
    }
}