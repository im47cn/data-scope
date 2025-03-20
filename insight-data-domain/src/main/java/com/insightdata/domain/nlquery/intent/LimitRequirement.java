package com.insightdata.domain.nlquery.intent;

import lombok.Builder;
import lombok.Data;

/**
 * 限制条件类
 */
@Data
@Builder
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
     * 偏移量（用于分页）
     */
    private int offset;

    /**
     * 创建一个前N条限制
     */
    public static LimitRequirement topN(int n) {
        return LimitRequirement.builder()
                .limitType(LimitType.TOP_N)
                .limitValue(n)
                .offset(0)
                .build();
    }

    /**
     * 创建一个分页限制
     */
    public static LimitRequirement pagination(int pageSize, int pageNumber) {
        return LimitRequirement.builder()
                .limitType(LimitType.PAGINATION)
                .limitValue(pageSize)
                .offset((pageNumber - 1) * pageSize)
                .build();
    }

    /**
     * 创建一个无限制条件
     */
    public static LimitRequirement none() {
        return LimitRequirement.builder()
                .limitType(LimitType.NONE)
                .limitValue(0)
                .offset(0)
                .build();
    }
}
