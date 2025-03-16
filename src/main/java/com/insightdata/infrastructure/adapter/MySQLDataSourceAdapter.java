package com.insightdata.infrastructure.adapter;

import com.insightdata.common.enums.DataSourceType;
import com.insightdata.common.exception.DataSourceException;
import com.insightdata.domain.model.DataSource;
import com.insightdata.domain.model.metadata.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

@Slf4j
@Component
public class MySQLDataSourceAdapter implements DataSourceAdapter {

    @Override
    public DataSourceType getType() {
        return DataSourceType.MYSQL;
    }

    @Override
    public boolean testConnection(DataSource dataSource) {
        try (Connection connection = getConnection(dataSource)) {
            return connection.isValid(5);
        } catch (SQLException e) {
            log.error("Failed to test connection to MySQL database: {}", dataSource.getName(), e);
            return false;
        }
    }

    @Override
    public Connection getConnection(DataSource dataSource) throws SQLException {
        try {
            Properties props = new Properties();
            props.setProperty("user", dataSource.getUsername());
            props.setProperty("password", dataSource.getEncryptedPassword());

            if (dataSource.getConnectionProperties() != null) {
                dataSource.getConnectionProperties().forEach(props::setProperty);
            }

            return DriverManager.getConnection(
                    "jdbc:mysql://" + dataSource.getHost() + ":" + dataSource.getPort() + "/" + dataSource.getDatabaseName(),
                    props
            );
        } catch (SQLException e) {
            log.error("Failed to connect to MySQL database: {}", dataSource.getName(), e);
            throw DataSourceException.connectionError("Failed to connect to MySQL database: " + e.getMessage(), e);
        }
    }

    @Override
    public List<SchemaInfo> getSchemas(DataSource dataSource) {
        try (Connection connection = getConnection(dataSource)) {
            List<SchemaInfo> schemas = new ArrayList<>();
            try (ResultSet rs = connection.getMetaData().getSchemas()) {
                while (rs.next()) {
                    schemas.add(SchemaInfo.builder()
                            .name(rs.getString("TABLE_SCHEM"))
                            .dataSourceId(dataSource.getId())
                            .build());
                }
            }
            return schemas;
        } catch (SQLException e) {
            log.error("Failed to get schemas from MySQL database: {}", dataSource.getName(), e);
            throw DataSourceException.metadataExtractionError("Failed to get schemas: " + e.getMessage(), e);
        }
    }

    @Override
    public SchemaInfo getSchema(DataSource dataSource, String schemaName) {
        try (Connection connection = getConnection(dataSource)) {
            try (ResultSet rs = connection.getMetaData().getSchemas()) {
                while (rs.next()) {
                    String currentSchema = rs.getString("TABLE_SCHEM");
                    if (currentSchema.equals(schemaName)) {
                        return SchemaInfo.builder()
                                .name(schemaName)
                                .dataSourceId(dataSource.getId())
                                .build();
                    }
                }
            }
            return null;
        } catch (SQLException e) {
            log.error("Failed to get schema from MySQL database: {}", dataSource.getName(), e);
            throw DataSourceException.metadataExtractionError("Failed to get schema: " + e.getMessage(), e);
        }
    }

    @Override
    public SchemaInfo getSchemaInfo(DataSource dataSource, String schemaName) {
        return getSchema(dataSource, schemaName);
    }

    @Override
    public List<TableInfo> getTables(DataSource dataSource, String schemaName) {
        try (Connection connection = getConnection(dataSource)) {
            List<TableInfo> tables = new ArrayList<>();
            try (ResultSet rs = connection.getMetaData().getTables(schemaName, null, "%", new String[]{"TABLE", "VIEW"})) {
                while (rs.next()) {
                    String tableName = rs.getString("TABLE_NAME");
                    String tableType = rs.getString("TABLE_TYPE");
                    String remarks = rs.getString("REMARKS");

                    TableInfo table = TableInfo.builder()
                            .name(tableName)
                            .type(tableType)
                            .description(remarks)
                            .schemaName(schemaName)
                            .dataSourceId(dataSource.getId())
                            .build();

                    tables.add(table);
                }
            }

            // 获取表的行数和大小信息
            for (TableInfo table : tables) {
                table.setRowCount(getRowCount(dataSource, schemaName, table.getName()));
                table.setDataSize(getDataSize(dataSource, schemaName, table.getName()));
                table.setIndexSize(getIndexSize(dataSource, schemaName, table.getName()));
            }

            return tables;
        } catch (SQLException e) {
            log.error("Failed to get tables from MySQL database: {}", dataSource.getName(), e);
            throw DataSourceException.metadataExtractionError("Failed to get tables: " + e.getMessage(), e);
        }
    }

    @Override
    public TableInfo getTable(DataSource dataSource, String schemaName, String tableName) {
        try (Connection connection = getConnection(dataSource)) {
            try (ResultSet rs = connection.getMetaData().getTables(schemaName, null, tableName, new String[]{"TABLE", "VIEW"})) {
                if (rs.next()) {
                    String tableType = rs.getString("TABLE_TYPE");
                    String remarks = rs.getString("REMARKS");

                    TableInfo table = TableInfo.builder()
                            .name(tableName)
                            .type(tableType)
                            .description(remarks)
                            .schemaName(schemaName)
                            .dataSourceId(dataSource.getId())
                            .build();

                    table.setRowCount(getRowCount(dataSource, schemaName, tableName));
                    table.setDataSize(getDataSize(dataSource, schemaName, tableName));
                    table.setIndexSize(getIndexSize(dataSource, schemaName, tableName));

                    return table;
                }
            }
            return null;
        } catch (SQLException e) {
            log.error("Failed to get table from MySQL database: {}", dataSource.getName(), e);
            throw DataSourceException.metadataExtractionError("Failed to get table: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ColumnInfo> getColumns(DataSource dataSource, String schemaName, String tableName) {
        try (Connection connection = getConnection(dataSource)) {
            List<ColumnInfo> columns = new ArrayList<>();
            Map<String, Boolean> primaryKeys = new HashMap<>();

            // 获取主键信息
            try (ResultSet pkRs = connection.getMetaData().getPrimaryKeys(schemaName, null, tableName)) {
                while (pkRs.next()) {
                    primaryKeys.put(pkRs.getString("COLUMN_NAME"), true);
                }
            }

            // 获取列信息
            try (ResultSet rs = connection.getMetaData().getColumns(schemaName, null, tableName, "%")) {
                while (rs.next()) {
                    String columnName = rs.getString("COLUMN_NAME");
                    String dataType = rs.getString("TYPE_NAME");
                    int ordinalPosition = rs.getInt("ORDINAL_POSITION");
                    int columnSize = rs.getInt("COLUMN_SIZE");
                    int decimalDigits = rs.getInt("DECIMAL_DIGITS");
                    boolean nullable = rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable;
                    String defaultValue = rs.getString("COLUMN_DEF");
                    String remarks = rs.getString("REMARKS");
                    boolean isAutoIncrement = "YES".equals(rs.getString("IS_AUTOINCREMENT"));

                    ColumnInfo column = ColumnInfo.builder()
                            .name(columnName)
                            .dataType(dataType)
                            .ordinalPosition(ordinalPosition)
                            .length(columnSize)
                            .precision(columnSize)
                            .scale(decimalDigits)
                            .nullable(nullable)
                            .defaultValue(defaultValue)
                            .description(remarks)
                            .primaryKey(primaryKeys.containsKey(columnName))
                            .autoIncrement(isAutoIncrement)
                            .build();

                    columns.add(column);
                }
            }

            return columns;
        } catch (SQLException e) {
            log.error("Failed to get columns from MySQL database: {}", dataSource.getName(), e);
            throw DataSourceException.metadataExtractionError("Failed to get columns: " + e.getMessage(), e);
        }
    }

    @Override
    public List<IndexInfo> getIndexes(DataSource dataSource, String schemaName, String tableName) {
        try (Connection connection = getConnection(dataSource)) {
            Map<String, IndexInfo> indexMap = new HashMap<>();

            try (ResultSet rs = connection.getMetaData().getIndexInfo(schemaName, null, tableName, false, true)) {
                while (rs.next()) {
                    String indexName = rs.getString("INDEX_NAME");
                    if (indexName == null) {
                        continue;
                    }

                    boolean nonUnique = rs.getBoolean("NON_UNIQUE");
                    String columnName = rs.getString("COLUMN_NAME");
                    int ordinalPosition = rs.getInt("ORDINAL_POSITION");
                    String ascOrDesc = rs.getString("ASC_OR_DESC");

                    IndexInfo index = indexMap.computeIfAbsent(indexName, k ->
                            IndexInfo.builder()
                                    .name(indexName)
                                    .type("BTREE")
                                    .unique(!nonUnique)
                                    .columns(new ArrayList<>())
                                    .build()
                    );

                    IndexColumnInfo indexColumn = IndexColumnInfo.builder()
                            .columnName(columnName)
                            .ordinalPosition(ordinalPosition)
                            .sortOrder(ascOrDesc)
                            .build();

                    index.getColumns().add(indexColumn);
                }
            }

            return new ArrayList<>(indexMap.values());
        } catch (SQLException e) {
            log.error("Failed to get indexes from MySQL database: {}", dataSource.getName(), e);
            throw DataSourceException.metadataExtractionError("Failed to get indexes: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ForeignKeyInfo> getForeignKeys(DataSource dataSource, String schemaName, String tableName) {
        try (Connection connection = getConnection(dataSource)) {
            Map<String, ForeignKeyInfo> foreignKeyMap = new HashMap<>();

            try (ResultSet rs = connection.getMetaData().getImportedKeys(schemaName, null, tableName)) {
                while (rs.next()) {
                    String fkName = rs.getString("FK_NAME");
                    String pkTableName = rs.getString("PKTABLE_NAME");
                    String pkColumnName = rs.getString("PKCOLUMN_NAME");
                    String fkColumnName = rs.getString("FKCOLUMN_NAME");
                    int keySeq = rs.getInt("KEY_SEQ");
                    int updateRule = rs.getInt("UPDATE_RULE");
                    int deleteRule = rs.getInt("DELETE_RULE");

                    ForeignKeyInfo foreignKey = foreignKeyMap.computeIfAbsent(fkName, k ->
                            ForeignKeyInfo.builder()
                                    .name(fkName)
                                    .sourceTableName(tableName)
                                    .targetTableName(pkTableName)
                                    .updateRule(getForeignKeyRuleName(updateRule))
                                    .deleteRule(getForeignKeyRuleName(deleteRule))
                                    .columns(new ArrayList<>())
                                    .build()
                    );

                    ForeignKeyColumnInfo foreignKeyColumn = ForeignKeyColumnInfo.builder()
                            .sourceTableName(tableName)
                            .sourceColumnName(fkColumnName)
                            .targetTableName(pkTableName)
                            .targetColumnName(pkColumnName)
                            .ordinalPosition(keySeq)
                            .build();

                    foreignKey.getColumns().add(foreignKeyColumn);
                }
            }

            return new ArrayList<>(foreignKeyMap.values());
        } catch (SQLException e) {
            log.error("Failed to get foreign keys from MySQL database: {}", dataSource.getName(), e);
            throw DataSourceException.metadataExtractionError("Failed to get foreign keys: " + e.getMessage(), e);
        }
    }

    @Override
    public long getDataSize(DataSource dataSource, String schemaName, String tableName) {
        String sql = "SELECT data_length + index_length FROM information_schema.tables " +
                    "WHERE table_schema = ? AND table_name = ?";

        try (Connection connection = getConnection(dataSource);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, schemaName);
            stmt.setString(2, tableName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            log.warn("Failed to get data size for table {}.{}: {}", schemaName, tableName, e.getMessage());
        }

        return 0;
    }

    @Override
    public long getRowCount(DataSource dataSource, String schemaName, String tableName) {
        String sql = "SELECT COUNT(*) FROM " + schemaName + "." + tableName;

        try (Connection connection = getConnection(dataSource);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            log.warn("Failed to get row count for table {}.{}: {}", schemaName, tableName, e.getMessage());
        }

        return 0;
    }

    @Override
    public long getEstimatedRowCount(DataSource dataSource, String schemaName, String tableName) {
        String sql = "SELECT table_rows FROM information_schema.tables " +
                    "WHERE table_schema = ? AND table_name = ?";

        try (Connection connection = getConnection(dataSource);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, schemaName);
            stmt.setString(2, tableName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            log.warn("Failed to get estimated row count for table {}.{}: {}", schemaName, tableName, e.getMessage());
        }

        return 0;
    }

    @Override
    public long getIndexSize(DataSource dataSource, String schemaName, String tableName) {
        String sql = "SELECT SUM(index_length) FROM information_schema.tables " +
                    "WHERE table_schema = ? AND table_name = ?";

        try (Connection connection = getConnection(dataSource);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, schemaName);
            stmt.setString(2, tableName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            log.warn("Failed to get index size for table {}.{}: {}", schemaName, tableName, e.getMessage());
        }

        return 0;
    }

    @Override
    public List<List<Object>> executeQuery(DataSource dataSource, String sql, Object... params) {
        List<List<Object>> results = new ArrayList<>();

        try (Connection connection = getConnection(dataSource);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Add column names as first row
                List<Object> columnNames = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    columnNames.add(metaData.getColumnName(i));
                }
                results.add(columnNames);

                // Add data rows
                while (rs.next()) {
                    List<Object> row = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.add(rs.getObject(i));
                    }
                    results.add(row);
                }
            }
        } catch (SQLException e) {
            log.error("Failed to execute query on MySQL database: {}", dataSource.getName(), e);
            throw DataSourceException.metadataExtractionError("Failed to execute query: " + e.getMessage(), e);
        }

        return results;
    }

    private String getForeignKeyRuleName(int rule) {
        switch (rule) {
            case DatabaseMetaData.importedKeyCascade:
                return "CASCADE";
            case DatabaseMetaData.importedKeySetNull:
                return "SET NULL";
            case DatabaseMetaData.importedKeySetDefault:
                return "SET DEFAULT";
            case DatabaseMetaData.importedKeyRestrict:
                return "RESTRICT";
            case DatabaseMetaData.importedKeyNoAction:
                return "NO ACTION";
            default:
                return "UNKNOWN";
        }
    }
}
