package com.insightdata.domain.query.model;

import lombok.Data;

@Data
public class NLQuery {
    private String dataSourceId;
    private String query;
    // 可以根据需要添加其他字段，例如查询参数、上下文信息等
}