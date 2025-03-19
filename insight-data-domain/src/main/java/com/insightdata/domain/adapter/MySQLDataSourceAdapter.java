package com.insightdata.domain.adapter;

import com.insightdata.domain.adapter.DataSourceAdapter;
import com.insightdata.domain.metadata.model.ColumnInfo;
import com.insightdata.domain.metadata.model.DataSource;
import com.insightdata.domain.metadata.model.SchemaInfo;
import com.insightdata.domain.metadata.model.TableInfo;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class MySQLDataSourceAdapter implements DataSourceAdapter {

    private Connection connection;

    @Override
    public void connect(DataSource config) throws Exception {
        String url = "jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDatabaseName();
        this.connection = DriverManager.getConnection(url, config.getUsername(), config.getPassword());
    }

    // 修复系统模式检查
    private boolean isSystemSchema(String schemaName) {
        String upperSchema = schemaName.toUpperCase();
        return upperSchema.equals("INFORMATION_SCHEMA") ||
                upperSchema.equals("PERFORMANCE_SCHEMA") ||
                upperSchema.equals("MYSQL");
    }

    @Override
    public void disconnect() throws Exception {
        if (this.connection != null && !this.connection.isClosed()) {
            this.connection.close();
        }
    }

    @Override
    public boolean testConnection(DataSource config) throws Exception {
        try {
            connect(config);
            return this.connection != null && !this.connection.isClosed();
        } finally {
            disconnect();
        }
    }

    @Override
    public List<String> getCatalogs() throws Exception {
        throw new UnsupportedOperationException("getCatalogs() is not yet implemented for MySQL.");
    }

    @Override
    public List<String> getSchemas(String catalog) throws Exception {
        throw new UnsupportedOperationException("getSchemas() is not yet implemented for MySQL.");
    }

    @Override
    public List<TableInfo> getTables(String catalog, String schema) throws Exception {
        throw new UnsupportedOperationException("getTables() is not yet implemented for MySQL.");
    }

    @Override
    public List<ColumnInfo> getColumns(String catalog, String schema, String table) throws Exception {
        throw new UnsupportedOperationException("getColumns() is not yet implemented for MySQL.");
    }

    @Override
    public Map<String, Long> getTableSizes(String catalog, String schema) throws Exception {
        Map<String, Long> tableSizes = new HashMap<>();
        try (Connection conn = this.connection) {
            String query = "SELECT table_name, data_length + index_length as size " +
                    "FROM information_schema.TABLES " +
                    "WHERE table_schema = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, schema);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String tableName = rs.getString("table_name");
                        long size = rs.getLong("size");
                        tableSizes.put(tableName, size);
                    }
                }
            }
        } catch (SQLException e) {
            // 修复错误的日志信息
            throw new Exception("Failed to get table sizes for MySQL database", e);
        }
        return tableSizes;
    }

    @Override
    public String getDataSourceType() {
        return "MySQL";
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }
}