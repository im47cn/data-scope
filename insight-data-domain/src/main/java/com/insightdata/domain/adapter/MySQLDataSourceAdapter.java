package com.insightdata.domain.adapter;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Component;

import com.insightdata.domain.datasource.model.ColumnInfo;
import com.insightdata.domain.datasource.model.DataSource;
import com.insightdata.domain.datasource.model.DataSourceType;
import com.insightdata.domain.datasource.model.ForeignKeyInfo;
import com.insightdata.domain.datasource.model.IndexInfo;
import com.insightdata.domain.datasource.model.ProcedureInfo;
import com.insightdata.domain.datasource.model.SchemaInfo;
import com.insightdata.domain.datasource.model.TableInfo;
import com.insightdata.domain.datasource.model.TriggerInfo;
import com.insightdata.domain.datasource.model.ViewInfo;
import com.insightdata.domain.exception.DataSourceException;
import com.insightdata.domain.security.encryption.EncryptionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MySQLDataSourceAdapter implements EnhancedMySQLDataSourceAdapter {
    private Connection connection;

    @Override
    public String getDataSourceType() {
        return DataSourceType.MYSQL.name();
    }

    @Override
    public Connection getConnection() throws DataSourceException {
        try {
            if (connection == null || connection.isClosed()) {
                throw new DataSourceException("No active connection");
            }
            return connection;
        } catch (SQLException e) {
            throw new DataSourceException("Failed to check connection status", e);
        }
    }

    @Override
    public void connect(DataSource config) throws DataSourceException {
        try {
            Properties props = new Properties();
            props.setProperty("user", config.getUsername());
            props.setProperty("password", config.getPassword());
            
            // Add connection properties if available
            if (config.getConnectionProperties() != null) {
                props.putAll(config.getConnectionProperties());
            }
            
            String url = buildJdbcUrl(config);
            this.connection = DriverManager.getConnection(url, props);
        } catch (SQLException e) {
            throw new DataSourceException("Failed to connect to database", e);
        }
    }

    @Override
    public void connect(DataSource config, String keyId, EncryptionService encryptionService) throws DataSourceException {
        try {
            Properties props = new Properties();
            props.setProperty("user", config.getUsername());
            String decryptedPassword = encryptionService.decrypt(config.getEncryptedPassword(), keyId);
            props.setProperty("password", decryptedPassword);
            
            // Add connection properties if available
            if (config.getConnectionProperties() != null) {
                props.putAll(config.getConnectionProperties());
            }
            
            String url = buildJdbcUrl(config);
            this.connection = DriverManager.getConnection(url, props);
        } catch (SQLException e) {
            throw new DataSourceException("Failed to connect to database with encryption", e);
        }
    }

    private String buildJdbcUrl(DataSource config) {
        StringBuilder url = new StringBuilder();
        url.append("jdbc:mysql://")
           .append(config.getHost())
           .append(":")
           .append(config.getPort())
           .append("/")
           .append(config.getDatabaseName())
           .append("?useSSL=false&serverTimezone=UTC");

        // Add additional URL parameters from extended properties
        if (config.getExtendedProperties() != null) {
            config.getExtendedProperties().forEach((key, value) -> {
                url.append("&").append(key).append("=").append(value);
            });
        }

        return url.toString();
    }

    @Override
    public void disconnect() throws DataSourceException {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
        } catch (SQLException e) {
            throw new DataSourceException("Failed to disconnect", e);
        }
    }

    @Override
    public boolean testConnection(DataSource config) throws DataSourceException {
        try (Connection testConn = DriverManager.getConnection(
                buildJdbcUrl(config),
                config.getUsername(),
                config.getPassword())) {
            return testConn.isValid(config.getValidationTimeout() != null ? 
                    config.getValidationTimeout() : 5);
        } catch (SQLException e) {
            throw new DataSourceException("Connection test failed", e);
        }
    }

    @Override
    public List<String> getCatalogs() throws DataSourceException {
        List<String> catalogs = new ArrayList<>();
        try (ResultSet rs = connection.getMetaData().getCatalogs()) {
            while (rs.next()) {
                catalogs.add(rs.getString("TABLE_CAT"));
            }
            return catalogs;
        } catch (SQLException e) {
            throw new DataSourceException("Failed to get catalogs", e);
        }
    }

    @Override
    public List<String> getSchemas(String catalog) throws DataSourceException {
        List<String> schemas = new ArrayList<>();
        try (ResultSet rs = connection.getMetaData().getSchemas(catalog, null)) {
            while (rs.next()) {
                schemas.add(rs.getString("TABLE_SCHEM"));
            }
            return schemas;
        } catch (SQLException e) {
            throw new DataSourceException("Failed to get schemas", e);
        }
    }

    @Override
    public List<SchemaInfo> getSchemas(DataSource dataSource) throws DataSourceException {
        List<SchemaInfo> schemaInfos = new ArrayList<>();
        try (ResultSet rs = connection.getMetaData().getSchemas()) {
            while (rs.next()) {
                String schemaName = rs.getString("TABLE_SCHEM");
                if (!"information_schema".equals(schemaName) && !"performance_schema".equals(schemaName)) {
                    SchemaInfo schemaInfo = SchemaInfo.builder()
                        .name(schemaName)
                        .tables(getTables(dataSource, schemaName))
                        .build();
                    schemaInfos.add(schemaInfo);
                }
            }
            return schemaInfos;
        } catch (SQLException e) {
            throw new DataSourceException("Failed to get schema information", e);
        }
    }

    @Override
    public SchemaInfo getSchema(DataSource dataSource, String schemaName) throws DataSourceException {
        return SchemaInfo.builder()
            .name(schemaName)
            .tables(getTables(dataSource, schemaName))
            .build();
    }

    @Override
    public List<TableInfo> getTables(String catalog, String schema) throws DataSourceException {
        List<TableInfo> tables = new ArrayList<>();
        try (ResultSet rs = connection.getMetaData().getTables(catalog, schema, null, new String[]{"TABLE"})) {
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                TableInfo tableInfo = TableInfo.builder()
                    .name(tableName)
                    .schemaName(schema)
                    .comment(rs.getString("REMARKS"))
                    .columns(getColumns(catalog, schema, tableName))
                    .foreignKeys(getForeignKeys(schema, tableName))
                    .build();
                tables.add(tableInfo);
            }
            return tables;
        } catch (SQLException e) {
            throw new DataSourceException("Failed to get tables", e);
        }
    }

    @Override
    public List<TableInfo> getTables(DataSource dataSource, String schema) throws DataSourceException {
        return getTables(dataSource.getDatabaseName(), schema);
    }

    @Override
    public List<ColumnInfo> getColumns(String catalog, String schema, String table) throws DataSourceException {
        List<ColumnInfo> columns = new ArrayList<>();
        try (ResultSet rs = connection.getMetaData().getColumns(catalog, schema, table, null)) {
            while (rs.next()) {
                ColumnInfo columnInfo = ColumnInfo.builder()
                    .name(rs.getString("COLUMN_NAME"))
                    .type(rs.getString("TYPE_NAME"))
                    .length(rs.getLong("COLUMN_SIZE"))
                    .nullable(rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable)
                    .defaultValue(rs.getString("COLUMN_DEF"))
                    .comment(rs.getString("REMARKS"))
                    .primaryKey(isPrimaryKey(schema, table, rs.getString("COLUMN_NAME")))
                    .build();
                columns.add(columnInfo);
            }
            return columns;
        } catch (SQLException e) {
            throw new DataSourceException("Failed to get columns", e);
        }
    }

    @Override
    public Map<String, Long> getTableSizes(String catalog, String schema) throws DataSourceException {
        Map<String, Long> sizes = new HashMap<>();
        String sql = "SELECT table_name, data_length + index_length as total_size " +
                    "FROM information_schema.tables " +
                    "WHERE table_schema = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, schema);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sizes.put(rs.getString("table_name"), rs.getLong("total_size"));
                }
            }
            return sizes;
        } catch (SQLException e) {
            throw new DataSourceException("Failed to get table sizes", e);
        }
    }

    @Override
    public Long getRowCount(String schema, String table) throws DataSourceException {
        String sql = "SELECT table_rows FROM information_schema.tables " +
                    "WHERE table_schema = ? AND table_name = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, schema);
            stmt.setString(2, table);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getLong("table_rows") : 0L;
            }
        } catch (SQLException e) {
            throw new DataSourceException("Failed to get row count", e);
        }
    }

    @Override
    public Long getDataSize(String schema, String table) throws DataSourceException {
        String sql = "SELECT data_length FROM information_schema.tables " +
                    "WHERE table_schema = ? AND table_name = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, schema);
            stmt.setString(2, table);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getLong("data_length") : 0L;
            }
        } catch (SQLException e) {
            throw new DataSourceException("Failed to get data size", e);
        }
    }

    @Override
    public List<IndexInfo> getPrimaryKeys(String schema, String table) throws DataSourceException {
        List<IndexInfo> primaryKeys = new ArrayList<>();
        try (ResultSet rs = connection.getMetaData().getPrimaryKeys(null, schema, table)) {
            while (rs.next()) {
                IndexInfo indexInfo = IndexInfo.builder()
                    .name("PRIMARY")
                    .columnNames(new String[]{rs.getString("COLUMN_NAME")})
                    .tableName(table)
                    .schema(schema)
                    .primaryKey(true)
                    .build();
                primaryKeys.add(indexInfo);
            }
            return primaryKeys;
        } catch (SQLException e) {
            throw new DataSourceException("Failed to get primary keys", e);
        }
    }

    @Override
    public List<IndexInfo> getIndexes(String schema, String table) throws DataSourceException {
        List<IndexInfo> indexes = new ArrayList<>();
        try (ResultSet rs = connection.getMetaData().getIndexInfo(null, schema, table, false, false)) {
            while (rs.next()) {
                String[] columns = {rs.getString("COLUMN_NAME")};
                IndexInfo indexInfo = IndexInfo.builder()
                    .name(rs.getString("INDEX_NAME"))
                    .columnNames(columns)
                    .tableName(table)
                    .schema(schema)
                    .unique(!rs.getBoolean("NON_UNIQUE"))
                    .build();
                indexes.add(indexInfo);
            }
            return indexes;
        } catch (SQLException e) {
            throw new DataSourceException("Failed to get indexes", e);
        }
    }

    @Override
    public List<ForeignKeyInfo> getForeignKeys(String schema, String table) throws DataSourceException {
        List<ForeignKeyInfo> foreignKeys = new ArrayList<>();
        try (ResultSet rs = connection.getMetaData().getImportedKeys(null, schema, table)) {
            while (rs.next()) {
                String[] sourceColumns = {rs.getString("FKCOLUMN_NAME")};
                String[] targetColumns = {rs.getString("PKCOLUMN_NAME")};
                
                ForeignKeyInfo fkInfo = ForeignKeyInfo.builder()
                    .name(rs.getString("FK_NAME"))
                    .schema(schema)
                    .sourceTable(table)
                    .sourceColumns(sourceColumns)
                    .targetTable(rs.getString("PKTABLE_NAME"))
                    .targetColumns(targetColumns)
                    .updateRule(rs.getString("UPDATE_RULE"))
                    .deleteRule(rs.getString("DELETE_RULE"))
                    .build();
                foreignKeys.add(fkInfo);
            }
            return foreignKeys;
        } catch (SQLException e) {
            throw new DataSourceException("Failed to get foreign keys", e);
        }
    }

    @Override
    public List<ViewInfo> getViews(String schema) throws DataSourceException {
        List<ViewInfo> views = new ArrayList<>();
        try (ResultSet rs = connection.getMetaData().getTables(null, schema, null, new String[]{"VIEW"})) {
            while (rs.next()) {
                String viewName = rs.getString("TABLE_NAME");
                ViewInfo viewInfo = ViewInfo.builder()
                    .name(viewName)
                    .schema(schema)
                    .definition(getViewDefinition(schema, viewName))
                    .build();
                views.add(viewInfo);
            }
            return views;
        } catch (SQLException e) {
            throw new DataSourceException("Failed to get views", e);
        }
    }

    private String getViewDefinition(String schema, String viewName) throws DataSourceException {
        String sql = "SELECT VIEW_DEFINITION FROM information_schema.VIEWS " +
                    "WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, schema);
            stmt.setString(2, viewName);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getString("VIEW_DEFINITION") : null;
            }
        } catch (SQLException e) {
            throw new DataSourceException("Failed to get view definition", e);
        }
    }

    @Override
    public List<ProcedureInfo> getProcedures(String schema) throws DataSourceException {
        List<ProcedureInfo> procedures = new ArrayList<>();
        try (ResultSet rs = connection.getMetaData().getProcedures(null, schema, null)) {
            while (rs.next()) {
                String procName = rs.getString("PROCEDURE_NAME");
                ProcedureInfo procInfo = ProcedureInfo.builder()
                    .name(procName)
                    .schema(schema)
                    .definition(getProcedureDefinition(schema, procName))
                    .build();
                procedures.add(procInfo);
            }
            return procedures;
        } catch (SQLException e) {
            throw new DataSourceException("Failed to get procedures", e);
        }
    }

    private String getProcedureDefinition(String schema, String procedureName) throws DataSourceException {
        String sql = "SELECT ROUTINE_DEFINITION FROM information_schema.ROUTINES " +
                    "WHERE ROUTINE_SCHEMA = ? AND ROUTINE_NAME = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, schema);
            stmt.setString(2, procedureName);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getString("ROUTINE_DEFINITION") : null;
            }
        } catch (SQLException e) {
            throw new DataSourceException("Failed to get procedure definition", e);
        }
    }

    @Override
    public List<TriggerInfo> getTriggers(String schema, String table) throws DataSourceException {
        List<TriggerInfo> triggers = new ArrayList<>();
        String sql = "SELECT TRIGGER_NAME, EVENT_MANIPULATION, ACTION_STATEMENT, ACTION_TIMING " +
                    "FROM information_schema.TRIGGERS " +
                    "WHERE EVENT_OBJECT_SCHEMA = ? AND EVENT_OBJECT_TABLE = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, schema);
            stmt.setString(2, table);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TriggerInfo triggerInfo = TriggerInfo.builder()
                        .name(rs.getString("TRIGGER_NAME"))
                        .schema(schema)
                        .tableName(table)
                        .event(rs.getString("EVENT_MANIPULATION"))
                        .timing(rs.getString("ACTION_TIMING"))
                        .definition(rs.getString("ACTION_STATEMENT"))
                        .build();
                    triggers.add(triggerInfo);
                }
            }
            return triggers;
        } catch (SQLException e) {
            throw new DataSourceException("Failed to get triggers", e);
        }
    }

    @Override
    public boolean isPrimaryKey(String schema, String table, String column) throws DataSourceException {
        try (ResultSet rs = connection.getMetaData().getPrimaryKeys(null, schema, table)) {
            while (rs.next()) {
                if (column.equals(rs.getString("COLUMN_NAME"))) {
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new DataSourceException("Failed to check primary key", e);
        }
    }

    @Override
    public Map<String, Long> getTableSizes(String catalog, List<TableInfo> tables) throws DataSourceException {
        Map<String, Long> sizes = new HashMap<>();
        if (tables == null || tables.isEmpty()) {
            return sizes;
        }

        StringBuilder sql = new StringBuilder(
            "SELECT table_name, data_length + index_length as total_size " +
            "FROM information_schema.tables " +
            "WHERE table_schema = ? AND table_name IN ("
        );

        for (int i = 0; i < tables.size(); i++) {
            sql.append(i > 0 ? ",?" : "?");
        }
        sql.append(")");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            stmt.setString(1, catalog);
            int paramIndex = 2;
            for (TableInfo table : tables) {
                stmt.setString(paramIndex++, table.getName());
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sizes.put(rs.getString("table_name"), rs.getLong("total_size"));
                }
            }
            return sizes;
        } catch (SQLException e) {
            throw new DataSourceException("Failed to get table sizes", e);
        }
    }
}