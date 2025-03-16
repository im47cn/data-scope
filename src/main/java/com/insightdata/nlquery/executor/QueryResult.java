package com.insightdata.nlquery.executor;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * 查询结果
 * 包含查询执行的结果信息、数据、元数据等
 */
@Data
public class QueryResult {

    /**
     * 执行是否成功
     */
    private boolean success;

    /**
     * 执行的SQL语句
     */
    private String sql;

    /**
     * 错误信息
     * 当success为false时有效
     */
    private String errorMessage;

    /**
     * 执行时间（毫秒）
     */
    private long executionTime;

    /**
     * 查询结果的列名
     */
    private List<String> columns;

    /**
     * 查询结果的数据
     * 每一行是一个Map，key为列名，value为对应的值
     */
    private List<Map<String, Object>> data;

    /**
     * 查询结果的元数据
     */
    private QueryMetadata metadata;

    /**
     * 总记录数
     */
    private Long totalCount;

    /**
     * 当前页码
     */
    private Integer currentPage;

    /**
     * 每页记录数
     */
    private Integer pageSize;

    /**
     * 是否还有更多数据
     */
    private Boolean hasMore;

    /**
     * 查询是否被超时中断
     */
    private boolean timeout;

    /**
     * 查询是否被限流中断
     */
    private boolean throttled;

    /**
     * 附加信息
     * 可用于传递其他扩展信息
     */
    private Map<String, Object> additionalInfo;
}