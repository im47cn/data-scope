package com.insightdata.domain.adapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.insightdata.domain.metadata.model.ColumnInfo;
import com.insightdata.domain.metadata.model.DataSource;
import com.insightdata.domain.metadata.model.TableInfo;

@Component
public class MySQLDataSourceAdapter implements DataSourceAdapter {

    private Connection connection;

    @Override
    public void connect(DataSource config) throws Exception {
        String url = "jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDatabaseName() + "?useSSL=false&serverTimezone=UTC";
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
        List<String> schemas = new java.util.ArrayList<>();
        try (Connection conn = this.connection;
             PreparedStatement stmt = conn.prepareStatement("SELECT schema_name FROM information_schema.SCHEMATA WHERE schema_name NOT IN ('mysql', 'information_schema', 'performance_schema', 'sys')");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                schemas.add(rs.getString("schema_name"));
            }
        }
        return schemas;
    }

    @Override
    public List<TableInfo> getTables(String catalog, String schema) throws Exception {
        List<TableInfo> tables = new java.util.ArrayList<>();
        try (Connection conn = this.connection;
             PreparedStatement stmt = conn.prepareStatement("SELECT table_name FROM information_schema.TABLES WHERE table_schema = ?")) {
            stmt.setString(1, schema);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TableInfo tableInfo = new TableInfo();
                    tableInfo.setName(rs.getString("table_name"));
                    tables.add(tableInfo);
                }
            }
        }
        return tables;
    }

    @Override
    public List<ColumnInfo> getColumns(String catalog, String schema, String table) throws Exception {
        List<ColumnInfo> columns = new java.util.ArrayList<>();
        try (Connection conn = this.connection;
             PreparedStatement stmt = conn.prepareStatement("SELECT column_name, data_type FROM information_schema.COLUMNS WHERE table_schema = ? AND table_name = ?")) {
            stmt.setString(1, schema);
            stmt.setString(2, table);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ColumnInfo columnInfo = ColumnInfo.builder()
                            .name(rs.getString("column_name"))
                            .dataType(rs.getString("data_type"))
                            .build();
                    columns.add(columnInfo);
                }
            }
        }
        return columns;
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
            throw new DataSourceException("Failed to get table sizes for MySQL database", e);
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