package com.insightdata.domain.nlquery.intent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 限制条件
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LimitRequirement {
    
    /**
     * 限制类型
     */
    private LimitType limitType;
    
    /**
     * 限制值
     */
    private int limitValue;
    
    /**
     * 偏移量(用于分页)
     */
    private int offset;
    
    /**
     * 百分比值(用于百分比限制)
     */
    private double percentage;
    
    /**
     * 创建一个前N条限制
     */
    public static LimitRequirement topN(int n) {
        return LimitRequirement.builder()
                .limitType(LimitType.TOP_N)
                .limitValue(n)
                .build();
    }
    
    /**
     * 创建一个后N条限制
     */
    public static LimitRequirement bottomN(int n) {
        return LimitRequirement.builder()
                .limitType(LimitType.BOTTOM_N)
                .limitValue(n)
                .build();
    }
    
    /**
     * 创建一个分页限制
     */
    public static LimitRequirement pagination(int pageSize, int pageNum) {
        return LimitRequirement.builder()
                .limitType(LimitType.PAGINATION)
                .limitValue(pageSize)
                .offset((pageNum - 1) * pageSize)
                .build();
    }
    
    /**
     * 创建一个随机N条限制
     */
    public static LimitRequirement randomN(int n) {
        return LimitRequirement.builder()
                .limitType(LimitType.RANDOM_N)
                .limitValue(n)
                .build();
    }
    
    /**
     * 创建一个百分比限制
     */
    public static LimitRequirement percentage(double percentage) {
        return LimitRequirement.builder()
                .limitType(LimitType.PERCENTAGE)
                .percentage(percentage)
                .build();
    }
    
    /**
     * 创建一个无限制
     */
    public static LimitRequirement none() {
        return LimitRequirement.builder()
                .limitType(LimitType.NONE)
                .build();
    }
}
