package com.insightdata.domain.nlquery.executor;

import com.insightdata.domain.metadata.model.DataSource;

public interface QueryExecutor {
    QueryResult execute(String sql, DataSource dataSource);

    QueryResult execute(String sql, DataSource dataSource, QueryMetadata metadata);

    boolean cancel(String queryId);

    QueryStatus getStatus(String queryId);
}