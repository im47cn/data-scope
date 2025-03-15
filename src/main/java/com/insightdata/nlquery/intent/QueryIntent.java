package com.insightdata.nlquery.intent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询意图
 * 表示用户自然语言查询的意图
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryIntent {
    
    /**
     * 查询类型
     */
    private QueryType queryType;
    
    /**
     * 查询目的
     */
    private QueryPurpose queryPurpose;
    
    /**
     * 时间范围
     */
    private TimeRange timeRange;
    
    /**
     * 排序要求
     */
    private List<SortRequirement> sortRequirements;
    
    /**
     * 限制条件
     */
    private LimitRequirement limitRequirement;
    
    /**
     * 置信度
     */
    private double confidence;
    
    /**
     * 添加排序要求
     */
    public void addSortRequirement(SortRequirement sortRequirement) {
        if (sortRequirements == null) {
            sortRequirements = new ArrayList<>();
        }
        sortRequirements.add(sortRequirement);
    }
}
