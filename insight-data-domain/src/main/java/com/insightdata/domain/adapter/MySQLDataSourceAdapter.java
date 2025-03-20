package com.insightdata.domain.adapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.insightdata.domain.datasource.model.*;
import com.insightdata.security.EncryptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MySQLDataSourceAdapter implements EnhancedMySQLDataSourceAdapter {

    private Connection connection;

    @Override
    public void connect(DataSource config, String keyId, EncryptionService encryptionService) throws Exception {
        String url = "jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDatabaseName() + "?useSSL=false&serverTimezone=UTC";
        String username = config.getUsername();
        String password = config.getPassword();

        // 解密用户名和密码
        if (keyId != null && encryptionService != null) {
            username = encryptionService.decrypt(username, "datasource-credentials");
            password = encryptionService.decrypt(password, "datasource-credentials");
        }

        this.connection = DriverManager.getConnection(url, username, password);
    }

    @Override
    public void connect(DataSource config) throws Exception {
        String url = "jdbc:mysql://" + ((DataSource) config).getHost() + ":" + ((DataSource) config).getPort() + "/" + ((DataSource) config).getDatabaseName() + "?useSSL=false&serverTimezone=UTC";
        String username = ((DataSource) config).getUsername();
        String password = ((DataSource) config).getPassword();

        this.connection = DriverManager.getConnection(url, username, password);
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
    public boolean testConnection() throws Exception {
        return this.connection != null && !this.connection.isClosed();
    }

    @Override
    public List<String> getCatalogs() throws Exception {
        throw new UnsupportedOperationException("getCatalogs() is not yet implemented for MySQL.");
    }

    @Override
    public List<String> getSchemas(String catalog) throws Exception {
        List<String> schemas = new ArrayList<>();
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
        List<TableInfo> tables = new ArrayList<>();
        try (Connection conn = this.connection;
             PreparedStatement stmt = conn.prepareStatement("SELECT table_name FROM information_schema.TABLES WHERE table_schema = ?")) {
            stmt.setString(1, schema);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TableInfo tableInfo = TableInfo.builder().name(rs.getString("table_name")).build();
                    tables.add(tableInfo);
                }
            }
        }
        return tables;
    }

    @Override
    public List<ColumnInfo> getColumns(String catalog, String schema, String table) throws Exception {
        List<ColumnInfo> columns = new ArrayList<>();
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
    public Map<String, Long> getTableSizes(String catalog, List<TableInfo> tableInfos) throws Exception {
        Map<String, Long> tableSizes = new HashMap<>();
        if (tableInfos == null || tableInfos.isEmpty()) {
            return tableSizes;
        }
        StringBuilder sqlBuilder = new StringBuilder("SELECT table_name, data_length + index_length AS size FROM information_schema.TABLES WHERE table_schema = ? AND table_name IN (");
        for (int i = 0; i < tableInfos.size(); i++) {
            sqlBuilder.append("?");
            if (i < tableInfos.size() - 1) {
                sqlBuilder.append(",");
            }
        }
        sqlBuilder.append(")");

        try (Connection conn = this.connection;
             PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {
            stmt.setString(1, "datainsight"); // TODO: schema should not be hardcoded
            int parameterIndex = 2;
            for (TableInfo tableInfo : tableInfos) {
                stmt.setString(parameterIndex++, tableInfo.getName());
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tableSizes.put(rs.getString("table_name"), rs.getLong("size"));
                }
            }
        }
        return tableSizes;
    }

    @Override
    public Long getRowCount(String catalog, String table) throws Exception {
        String query = "SELECT table_rows FROM information_schema.TABLES WHERE table_schema = ? AND table_name = ?";
        try (Connection conn = this.connection;
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, catalog);
            stmt.setString(2, table);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("table_rows");
                }
            }
        }
        return null;
    }

    @Override
    public boolean isPrimaryKey(String catalog, String schema, String table, String column) throws Exception {
        String query = "SELECT COUNT(*) FROM information_schema.KEY_COLUMN_USAGE WHERE table_schema = ? AND table_name = ? AND column_name = ? AND constraint_name = 'PRIMARY'";
        try (Connection conn = this.connection;
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, schema);
            stmt.setString(2, table);
            stmt.setString(3, column);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    @Override
    public List<TriggerInfo> getTriggers(String catalog, String schema, String table) throws Exception {
        return null;
    }

    @Override
    public List<ForeignKeyInfo> getForeignKeys(String catalog, String schema, String table) throws Exception {
        return null;
    }

    @Override
    public List<ProcedureInfo> getProcedures(String catalog) throws Exception {
        return null;
    }

    @Override
    public List<IndexInfo> getIndexes(String catalog, String schema, String table) throws Exception {
        return null;
    }

    @Override
    public List<String> getPrimaryKeys(String catalog, String table) throws Exception {
        List<String> primaryKeys = new ArrayList<>();
        String query = "SELECT column_name FROM information_schema.KEY_COLUMN_USAGE WHERE table_schema = ? AND table_name = ? AND constraint_name = 'PRIMARY'";
        try (Connection conn = this.connection;
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, catalog);
            stmt.setString(2, table);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    primaryKeys.add(rs.getString("column_name"));
                }
            }
        }
        return primaryKeys;
    }

    @Override
    public Double getDataSize(String catalog, String table) throws Exception {
        String query = "SELECT data_length + index_length AS size FROM information_schema.TABLES WHERE table_schema = ? AND table_name = ?";
        try (Connection conn = this.connection;
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, catalog);
            stmt.setString(2, table);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("size");
                }
            }
        }
        return null;
    }

    @Override
    public List<ViewInfo> getViews(String catalog) throws Exception {
        return null;
    }
}