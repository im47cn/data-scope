package com.insightdata.infrastructure.adapter.mysql;

import com.insightdata.domain.adapter.DataSourceAdapter;
import com.insightdata.domain.datasource.model.DataSource;
import com.insightdata.domain.metadata.model.ColumnInfo;
import com.insightdata.domain.metadata.model.TableInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class MySQLDataSourceAdapterTest {

    @Autowired
    private DataSourceAdapter mysqlDataSourceAdapter;

    @Test
    public void testConnection() throws Exception {
        DataSource dataSource = new DataSource();
        dataSource.setHost("localhost");
        dataSource.setPort(3306);
        dataSource.setDatabaseName("datainsight");
        dataSource.setUsername("test");
        dataSource.setPassword("test");

        assertTrue(mysqlDataSourceAdapter.testConnection(dataSource));
    }

    @Test
    public void testGetSchemas() throws Exception {
        DataSource dataSource = new DataSource();
        dataSource.setHost("localhost");
        dataSource.setPort(3306);
        dataSource.setDatabaseName("datainsight");
        dataSource.setUsername("test");
        dataSource.setPassword("test");

        List<String> schemas = mysqlDataSourceAdapter.getSchemas(null);
        assertNotNull(schemas);
        assertTrue(schemas.size() > 0);
    }

    @Test
    public void testGetColumns() throws Exception {
        DataSource dataSource = new DataSource();
        dataSource.setHost("localhost");
        dataSource.setPort(3306);
        dataSource.setDatabaseName("datainsight");
        dataSource.setUsername("test");
        dataSource.setPassword("test");

        List<String> schemas = mysqlDataSourceAdapter.getSchemas(null);
        String schema = schemas.get(0);

        List<TableInfo> tables = mysqlDataSourceAdapter.getTables(null, schema);
        String table = tables.get(0).getName();

        List<ColumnInfo> columns = mysqlDataSourceAdapter.getColumns(null, schema, table);
        assertNotNull(columns);
        assertTrue(columns.size() > 0);
    }
}