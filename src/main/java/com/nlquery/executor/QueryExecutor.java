package com.nlquery.executor;

import com.domain.model.metadata.DataSource;

public interface QueryExecutor {
    QueryResult execute(String sql, DataSource dataSource);

    QueryResult execute(String sql, DataSource dataSource, QueryMetadata metadata);

    boolean cancel(String queryId);

    QueryStatus getStatus(String queryId);
}