package com.insightdata.domain.query.model;

import lombok.Data;

@Data
public class QueryContext {
    private String dataSourceId;
    // 可以根据需要添加其他字段，例如用户ID、会话ID、当前时间等
}