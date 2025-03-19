package com.insightdata.infrastructure.adapter.mysql;

import com.insightdata.domain.adapter.DataSourceAdapter;
import com.insightdata.domain.metadata.enums.DataSourceType;
import com.insightdata.domain.metadata.model.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class MySQLDataSourceAdapterTest {

    @Autowired
    private DataSourceAdapter mysqlDataSourceAdapter; // Inject the adapter

    @Test
    void testConnectAndDisconnect() throws Exception {
        DataSource dataSource = new DataSource();
        dataSource.setHost("localhost"); // Replace with test DB details or use environment variables
        dataSource.setPort(3306);          //  "
        dataSource.setDatabaseName("test_db");  // "
        dataSource.setUsername("test_user");     // "
        dataSource.setPassword("test_password");   // "
        dataSource.setType(com.insightdata.domain.metadata.enums.DataSourceType.MYSQL);

        mysqlDataSourceAdapter.connect(dataSource);
        assertTrue(mysqlDataSourceAdapter.testConnection(dataSource));
        mysqlDataSourceAdapter.disconnect();
    }

    // Add more tests for other methods (getSchemas, getTables, getColumns, etc.)
}