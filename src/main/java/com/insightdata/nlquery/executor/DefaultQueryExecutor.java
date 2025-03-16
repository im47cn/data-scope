package com.insightdata.nlquery.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.insightdata.domain.adapter.DataSourceAdapter;
import com.insightdata.domain.model.DataSource;
import com.insightdata.infrastructure.adapter.DataSourceAdapterFactory;

/**
 * 默认查询执行器
 */
@Component
public class DefaultQueryExecutor implements QueryExecutor {

    private static final Logger log = LoggerFactory.getLogger(DefaultQueryExecutor.class);

    @Autowired
    private DataSourceAdapterFactory dataSourceAdapterFactory;

    private final Map<String, QueryStatus> queryStatusMap = new ConcurrentHashMap<>();

    @Override
    public QueryResult execute(String sql, DataSource dataSource) {
        return execute(sql, dataSource, null);
    }

    @Override
    public QueryResult execute(String sql, DataSource dataSource, QueryMetadata metadata) {
        long startTime = System.currentTimeMillis();
        String queryId = generateQueryId();
        queryStatusMap.put(queryId, QueryStatus.RUNNING);

        try {
            DataSourceAdapter adapter = dataSourceAdapterFactory.getAdapter(dataSource.getType());
            try (Connection conn = adapter.getConnection(dataSource);
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                boolean hasResultSet = stmt.execute();
                if (hasResultSet) {
                    try (ResultSet rs = stmt.getResultSet()) {
                        ResultSetMetaData rsmd = rs.getMetaData();
                        
                        List<String> columnNames = new ArrayList<>();
                        List<String> columnTypes = new ArrayList<>();
                        List<String> columnLabels = new ArrayList<>();
                        
                        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                            columnNames.add(rsmd.getColumnName(i));
                            columnTypes.add(rsmd.getColumnTypeName(i));
                            columnLabels.add(rsmd.getColumnLabel(i));
                        }
                        
                        List<Map<String, Object>> rows = new ArrayList<>();
                        while (rs.next()) {
                            Map<String, Object> row = new HashMap<>();
                            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                                row.put(rsmd.getColumnName(i), rs.getObject(i));
                            }
                            rows.add(row);
                        }
                        
                        long duration = System.currentTimeMillis() - startTime;
                        queryStatusMap.put(queryId, QueryStatus.SUCCESS);
                        
                        return QueryResult.builder()
                                .success(true)
                                .duration(duration)
                                .rowCount(rows.size())
                                .columnNames(columnNames)
                                .columnTypes(columnTypes)
                                .columnLabels(columnLabels)
                                .rows(rows)
                                .build();
                    }
                } else {
                    int updateCount = stmt.getUpdateCount();
                    long duration = System.currentTimeMillis() - startTime;
                    queryStatusMap.put(queryId, QueryStatus.SUCCESS);
                    
                    return QueryResult.builder()
                            .success(true)
                            .duration(duration)
                            .rowCount(updateCount)
                            .build();
                }
            }
        } catch (SQLException e) {
            log.error("执行查询失败", e);
            queryStatusMap.put(queryId, QueryStatus.FAILED);
            
            return QueryResult.builder()
                    .success(false)
                    .errorMessage(e.getMessage())
                    .duration(System.currentTimeMillis() - startTime)
                    .build();
        }
    }

    @Override
    public boolean cancel(String queryId) {
        if (queryStatusMap.containsKey(queryId)) {
            queryStatusMap.put(queryId, QueryStatus.CANCELLED);
            return true;
        }
        return false;
    }

    @Override
    public QueryStatus getStatus(String queryId) {
        return queryStatusMap.getOrDefault(queryId, QueryStatus.PENDING);
    }

    private String generateQueryId() {
        return String.valueOf(System.currentTimeMillis());
    }
}