package com.insightdata.domain.query.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

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

}
