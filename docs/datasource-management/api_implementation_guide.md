# 数据源管理API实现指南

## 概述

本文档提供了数据源管理模块中未实现API的详细实现指南。这些API主要包括元数据获取和同步相关功能，是数据源管理页面前后端联调的关键部分。

## 待实现API列表

以下API在`DataSourceController`中已定义，但实现为空或未完成：

1. **获取Schema列表** - `getSchemas`
2. **获取表列表** - `getTables`
3. **同步元数据** - `syncMetadata`

## 实现指南

### 1. 获取Schema列表

**API路径**: `/api/datasources/{id}/schemas`

**实现步骤**:

1. 修改`DataSourceController`中的`getSchemas`方法：

```java
/**
 * 获取数据源的Schema列表
 */
@GetMapping("/{id}/schemas")
public ResponseEntity<List<SchemaInfoDTO>> getSchemas(@PathVariable String id) {
    return dataSourceService.getDataSourceById(id)
            .map(dataSource -> {
                try {
                    List<com.insightdata.domain.metadata.model.SchemaInfo> schemas = 
                        dataSourceService.getSchemas(dataSource);
                    
                    List<SchemaInfoDTO> schemaDTOs = schemas.stream()
                            .map(schema -> SchemaInfoDTO.builder()
                                    .name(schema.getName())
                                    .tableCount(schema.getTableCount())
                                    .viewCount(schema.getViewCount())
                                    .description(schema.getDescription())
                                    .build())
                            .collect(Collectors.toList());
                    
                    return ResponseEntity.ok(schemaDTOs);
                } catch (Exception e) {
                    // 日志记录异常
                    log.error("获取Schema列表失败: {}", e.getMessage(), e);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            })
            .orElse(ResponseEntity.notFound().build());
}
```

2. 在`DataSourceService`接口中添加获取Schema列表的方法：

```java
/**
 * 获取数据源的Schema列表
 * @param dataSource 数据源
 * @return Schema列表
 */
List<SchemaInfo> getSchemas(DataSource dataSource);
```

3. 在`DataSourceServiceImpl`中实现该方法：

```java
@Override
public List<SchemaInfo> getSchemas(DataSource dataSource) {
    DataSourceAdapter adapter = dataSourceAdapterFactory.getAdapter(dataSource);
    return adapter.getSchemas();
}
```

4. 确保`DataSourceAdapter`接口和实现类（如`MySQLDataSourceAdapter`、`DB2DataSourceAdapter`）中实现`getSchemas`方法：

```java
// DataSourceAdapter接口
List<SchemaInfo> getSchemas();

// MySQLDataSourceAdapter实现
@Override
public List<SchemaInfo> getSchemas() {
    List<SchemaInfo> schemas = new ArrayList<>();
    
    try (Connection conn = getConnection()) {
        DatabaseMetaData metaData = conn.getMetaData();
        
        // MySQL中schema相当于database
        try (ResultSet rs = metaData.getCatalogs()) {
            while (rs.next()) {
                String schemaName = rs.getString("TABLE_CAT");
                SchemaInfo schema = new SchemaInfo();
                schema.setName(schemaName);
                
                // 获取表和视图数量
                int tableCount = 0;
                int viewCount = 0;
                
                try (ResultSet tablesRs = metaData.getTables(schemaName, null, "%", new String[]{"TABLE", "VIEW"})) {
                    while (tablesRs.next()) {
                        String tableType = tablesRs.getString("TABLE_TYPE");
                        if ("TABLE".equals(tableType)) {
                            tableCount++;
                        } else if ("VIEW".equals(tableType)) {
                            viewCount++;
                        }
                    }
                }
                
                schema.setTableCount(tableCount);
                schema.setViewCount(viewCount);
                schemas.add(schema);
            }
        }
    } catch (SQLException e) {
        throw new DataSourceException("获取MySQL Schemas失败: " + e.getMessage(), e);
    }
    
    return schemas;
}

// DB2DataSourceAdapter实现
@Override
public List<SchemaInfo> getSchemas() {
    List<SchemaInfo> schemas = new ArrayList<>();
    
    try (Connection conn = getConnection()) {
        DatabaseMetaData metaData = conn.getMetaData();
        
        // DB2使用schema而不是catalog
        try (ResultSet rs = metaData.getSchemas()) {
            while (rs.next()) {
                String schemaName = rs.getString("TABLE_SCHEM");
                // 排除系统schema
                if (schemaName.startsWith("SYS") || schemaName.equals("NULLID")) {
                    continue;
                }
                
                SchemaInfo schema = new SchemaInfo();
                schema.setName(schemaName);
                
                // 获取表和视图数量
                int tableCount = 0;
                int viewCount = 0;
                
                try (ResultSet tablesRs = metaData.getTables(null, schemaName, "%", new String[]{"TABLE", "VIEW"})) {
                    while (tablesRs.next()) {
                        String tableType = tablesRs.getString("TABLE_TYPE");
                        if ("TABLE".equals(tableType)) {
                            tableCount++;
                        } else if ("VIEW".equals(tableType)) {
                            viewCount++;
                        }
                    }
                }
                
                schema.setTableCount(tableCount);
                schema.setViewCount(viewCount);
                schemas.add(schema);
            }
        }
    } catch (SQLException e) {
        throw new DataSourceException("获取DB2 Schemas失败: " + e.getMessage(), e);
    }
    
    return schemas;
}
```

### 2. 获取表列表

**API路径**: `/api/datasources/{id}/schemas/{schemaName}/tables`

**实现步骤**:

1. 修改`DataSourceController`中的`getTables`方法：

```java
/**
 * 获取指定Schema的表列表
 */
@GetMapping("/{id}/schemas/{schemaName}/tables")
public ResponseEntity<List<TableInfoDTO>> getTables(
        @PathVariable String id, 
        @PathVariable String schemaName) {
    return dataSourceService.getDataSourceById(id)
            .map(dataSource -> {
                try {
                    List<com.insightdata.domain.metadata.model.TableInfo> tables = 
                        dataSourceService.getTables(dataSource, schemaName);
                    
                    List<TableInfoDTO> tableDTOs = tables.stream()
                            .map(table -> TableInfoDTO.builder()
                                    .name(table.getName())
                                    .type(table.getType())
                                    .schema(table.getSchema())
                                    .columnCount(table.getColumns() != null ? table.getColumns().size() : 0)
                                    .description(table.getDescription())
                                    .build())
                            .collect(Collectors.toList());
                    
                    return ResponseEntity.ok(tableDTOs);
                } catch (Exception e) {
                    log.error("获取表列表失败: {}", e.getMessage(), e);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            })
            .orElse(ResponseEntity.notFound().build());
}
```

2. 在`DataSourceService`接口中添加获取表列表的方法：

```java
/**
 * 获取指定Schema的表列表
 * @param dataSource 数据源
 * @param schemaName Schema名称
 * @return 表列表
 */
List<TableInfo> getTables(DataSource dataSource, String schemaName);
```

3. 在`DataSourceServiceImpl`中实现该方法：

```java
@Override
public List<TableInfo> getTables(DataSource dataSource, String schemaName) {
    DataSourceAdapter adapter = dataSourceAdapterFactory.getAdapter(dataSource);
    return adapter.getTables(schemaName);
}
```

4. 确保`DataSourceAdapter`接口和实现类中实现`getTables`方法：

```java
// DataSourceAdapter接口
List<TableInfo> getTables(String schemaName);

// MySQLDataSourceAdapter实现
@Override
public List<TableInfo> getTables(String schemaName) {
    List<TableInfo> tables = new ArrayList<>();
    
    try (Connection conn = getConnection()) {
        DatabaseMetaData metaData = conn.getMetaData();
        
        // MySQL中schemaName相当于catalog
        try (ResultSet rs = metaData.getTables(schemaName, null, "%", new String[]{"TABLE", "VIEW"})) {
            while (rs.next()) {
                TableInfo table = new TableInfo();
                table.setName(rs.getString("TABLE_NAME"));
                table.setType(rs.getString("TABLE_TYPE"));
                table.setSchema(schemaName);
                table.setDescription(rs.getString("REMARKS"));
                
                // 获取列数量
                int columnCount = 0;
                try (ResultSet columnsRs = metaData.getColumns(schemaName, null, table.getName(), "%")) {
                    while (columnsRs.next()) {
                        columnCount++;
                    }
                }
                
                List<ColumnInfo> columns = new ArrayList<>(columnCount);
                table.setColumns(columns);
                tables.add(table);
            }
        }
    } catch (SQLException e) {
        throw new DataSourceException("获取MySQL表列表失败: " + e.getMessage(), e);
    }
    
    return tables;
}

// DB2DataSourceAdapter实现
@Override
public List<TableInfo> getTables(String schemaName) {
    List<TableInfo> tables = new ArrayList<>();
    
    try (Connection conn = getConnection()) {
        DatabaseMetaData metaData = conn.getMetaData();
        
        // DB2使用schema参数
        try (ResultSet rs = metaData.getTables(null, schemaName, "%", new String[]{"TABLE", "VIEW"})) {
            while (rs.next()) {
                TableInfo table = new TableInfo();
                table.setName(rs.getString("TABLE_NAME"));
                table.setType(rs.getString("TABLE_TYPE"));
                table.setSchema(schemaName);
                table.setDescription(rs.getString("REMARKS"));
                
                // 获取列数量
                int columnCount = 0;
                try (ResultSet columnsRs = metaData.getColumns(null, schemaName, table.getName(), "%")) {
                    while (columnsRs.next()) {
                        columnCount++;
                    }
                }
                
                List<ColumnInfo> columns = new ArrayList<>(columnCount);
                table.setColumns(columns);
                tables.add(table);
            }
        }
    } catch (SQLException e) {
        throw new DataSourceException("获取DB2表列表失败: " + e.getMessage(), e);
    }
    
    return tables;
}
```

### 3. 同步元数据

**API路径**: `/api/datasources/{id}/sync`

**实现步骤**:

1. 修改`DataSourceController`中的`syncMetadata`方法：

```java
/**
 * 同步数据源元数据
 */
@PostMapping("/{id}/sync")
public ResponseEntity<MetadataSyncResult> syncMetadata(@PathVariable String id) {
    return dataSourceService.getDataSourceById(id)
            .map(dataSource -> {
                try {
                    // 创建同步任务
                    MetadataSyncJob syncJob = metadataSyncJobApplicationService.createSyncJob(
                            dataSource.getId(), 
                            SyncType.FULL, 
                            "手动触发同步"
                    );
                    
                    // 异步执行同步任务
                    CompletableFuture.runAsync(() -> {
                        try {
                            metadataSyncJobApplicationService.executeSyncJob(syncJob.getId());
                        } catch (Exception e) {
                            log.error("执行同步任务失败: {}", e.getMessage(), e);
                        }
                    });
                    
                    // 返回同步任务信息
                    MetadataSyncResult result = new MetadataSyncResult();
                    result.setJobId(syncJob.getId());
                    result.setDataSourceId(dataSource.getId());
                    result.setStatus(syncJob.getStatus().name());
                    result.setMessage("同步任务已创建并开始执行");
                    result.setStartTime(syncJob.getStartTime());
                    
                    return ResponseEntity.ok(result);
                } catch (Exception e) {
                    log.error("创建同步任务失败: {}", e.getMessage(), e);
                    
                    MetadataSyncResult result = new MetadataSyncResult();
                    result.setDataSourceId(dataSource.getId());
                    result.setStatus("FAILED");
                    result.setMessage("创建同步任务失败: " + e.getMessage());
                    
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
                }
            })
            .orElse(ResponseEntity.notFound().build());
}
```

2. 确保`MetadataSyncJobApplicationService`有以下方法：

```java
/**
 * 创建同步任务
 * @param dataSourceId 数据源ID
 * @param syncType 同步类型
 * @param description 描述
 * @return 创建的同步任务
 */
MetadataSyncJob createSyncJob(String dataSourceId, SyncType syncType, String description);

/**
 * 执行同步任务
 * @param syncJobId 同步任务ID
 */
void executeSyncJob(String syncJobId);
```

3. 在`MetadataSyncJobApplicationServiceImpl`中实现这些方法：

```java
@Override
public MetadataSyncJob createSyncJob(String dataSourceId, SyncType syncType, String description) {
    // 检查数据源是否存在
    DataSource dataSource = dataSourceRepository.findById(dataSourceId)
            .orElseThrow(() -> new IllegalArgumentException("数据源不存在: " + dataSourceId));
    
    // 创建同步任务
    MetadataSyncJob syncJob = new MetadataSyncJob();
    syncJob.setId(UUID.randomUUID().toString());
    syncJob.setDataSourceId(dataSourceId);
    syncJob.setSyncType(syncType);
    syncJob.setDescription(description);
    syncJob.setStatus(SyncStatus.PENDING);
    syncJob.setProgress(0);
    syncJob.setStartTime(LocalDateTime.now());
    
    return metadataSyncJobRepository.save(syncJob);
}

@Override
@Transactional
public void executeSyncJob(String syncJobId) {
    // 获取同步任务
    MetadataSyncJob syncJob = metadataSyncJobRepository.findById(syncJobId)
            .orElseThrow(() -> new IllegalArgumentException("同步任务不存在: " + syncJobId));
    
    // 获取数据源
    DataSource dataSource = dataSourceRepository.findById(syncJob.getDataSourceId())
            .orElseThrow(() -> new IllegalArgumentException("数据源不存在: " + syncJob.getDataSourceId()));
    
    try {
        // 更新任务状态为运行中
        syncJob.setStatus(SyncStatus.RUNNING);
        syncJob.setProgress(10);
        metadataSyncJobRepository.save(syncJob);
        
        // 获取数据源适配器
        DataSourceAdapter adapter = dataSourceAdapterFactory.getAdapter(dataSource);
        
        // 同步Schema
        List<SchemaInfo> schemas = adapter.getSchemas();
        syncJob.setProgress(30);
        metadataSyncJobRepository.save(syncJob);
        
        // 保存Schema信息
        for (SchemaInfo schema : schemas) {
            schemaInfoRepository.save(schema);
            
            // 同步表和视图
            List<TableInfo> tables = adapter.getTables(schema.getName());
            for (TableInfo table : tables) {
                tableInfoRepository.save(table);
                
                // 获取表的列信息
                List<ColumnInfo> columns = adapter.getColumns(schema.getName(), table.getName());
                for (ColumnInfo column : columns) {
                    // 保存列信息
                    column.setTableId(table.getId());
                    columnInfoRepository.save(column);
                }
            }
        }
        
        // 更新数据源最后同步时间
        dataSource.setLastSyncTime(LocalDateTime.now());
        dataSourceRepository.save(dataSource);
        
        // 更新任务状态为成功
        syncJob.setStatus(SyncStatus.SUCCESS);
        syncJob.setProgress(100);
        syncJob.setEndTime(LocalDateTime.now());
        syncJob.setResult("同步成功，共同步 " + schemas.size() + " 个Schema");
        metadataSyncJobRepository.save(syncJob);
    } catch (Exception e) {
        // 更新任务状态为失败
        syncJob.setStatus(SyncStatus.FAILED);
        syncJob.setEndTime(LocalDateTime.now());
        syncJob.setResult("同步失败: " + e.getMessage());
        metadataSyncJobRepository.save(syncJob);
        
        throw new RuntimeException("执行同步任务失败: " + e.getMessage(), e);
    }
}
```

4. 确保存在以下仓库接口：

```java
// SchemaInfoRepository
public interface SchemaInfoRepository extends JpaRepository<SchemaInfo, String> {
    List<SchemaInfo> findByDataSourceId(String dataSourceId);
}

// TableInfoRepository
public interface TableInfoRepository extends JpaRepository<TableInfo, String> {
    List<TableInfo> findBySchemaId(String schemaId);
}

// ColumnInfoRepository (如果需要)
public interface ColumnInfoRepository extends JpaRepository<ColumnInfo, String> {
    List<ColumnInfo> findByTableId(String tableId);
}
```

## 性能考虑

1. **异步处理**：元数据同步可能是耗时操作，应使用异步处理避免阻塞请求线程。

2. **批量处理**：对于大量数据，使用批量插入提高性能。例如：

```java
@Transactional
public void batchSaveSchemas(List<SchemaInfo> schemas) {
    for (int i = 0; i < schemas.size(); i += BATCH_SIZE) {
        int end = Math.min(i + BATCH_SIZE, schemas.size());
        List<SchemaInfo> batch = schemas.subList(i, end);
        schemaInfoRepository.saveAll(batch);
    }
}
```

3. **缓存**：考虑对频繁访问的数据（如Schema列表）进行缓存：

```java
@Cacheable(value = "schemas", key = "#dataSourceId")
public List<SchemaInfo> getSchemasByDataSourceId(String dataSourceId) {
    return schemaInfoRepository.findByDataSourceId(dataSourceId);
}

@CacheEvict(value = "schemas", key = "#dataSourceId")
public void refreshSchemaCache(String dataSourceId) {
    // 在元数据同步后调用此方法刷新缓存
}
```

## 错误处理

1. 使用具体的异常类型以提供更明确的错误信息：

```java
public class MetadataSyncException extends RuntimeException {
    private final String dataSourceId;
    private final String syncJobId;
    
    public MetadataSyncException(String message, String dataSourceId, String syncJobId, Throwable cause) {
        super(message, cause);
        this.dataSourceId = dataSourceId;
        this.syncJobId = syncJobId;
    }
    
    // getters...
}
```

2. 实现全局异常处理器捕获特定异常并返回友好的错误信息：

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MetadataSyncException.class)
    public ResponseEntity<ErrorResponse> handleMetadataSyncException(MetadataSyncException ex) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setMessage(ex.getMessage());
        error.setTimestamp(LocalDateTime.now());
        error.addDetail("dataSourceId", ex.getDataSourceId());
        error.addDetail("syncJobId", ex.getSyncJobId());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    
    // 其他异常处理...
}
```

## 测试策略

1. **单元测试**：测试各个组件的独立功能

```java
@Test
public void testGetSchemas() {
    // 准备测试数据
    DataSource dataSource = new DataSource();
    dataSource.setId("test-ds-1");
    dataSource.setType(DataSourceType.MYSQL);
    // 设置其他必要属性...
    
    // Mock依赖
    DataSourceAdapter mockAdapter = mock(MySQLDataSourceAdapter.class);
    when(dataSourceAdapterFactory.getAdapter(any(DataSource.class))).thenReturn(mockAdapter);
    
    List<SchemaInfo> expectedSchemas = Arrays.asList(
        new SchemaInfo("schema1", "test-ds-1", 10, 5),
        new SchemaInfo("schema2", "test-ds-1", 15, 3)
    );
    when(mockAdapter.getSchemas()).thenReturn(expectedSchemas);
    
    // 执行测试
    List<SchemaInfo> result = dataSourceService.getSchemas(dataSource);
    
    // 验证结果
    assertEquals(2, result.size());
    assertEquals("schema1", result.get(0).getName());
    assertEquals(10, result.get(0).getTableCount());
    assertEquals(5, result.get(0).getViewCount());
}
```

2. **集成测试**：测试API端点

```java
@SpringBootTest
@AutoConfigureMockMvc
public class DataSourceControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private DataSourceService dataSourceService;
    
    @Test
    public void testGetSchemas() throws Exception {
        // 准备测试数据
        DataSource dataSource = new DataSource();
        dataSource.setId("test-ds-1");
        // 设置其他属性...
        
        List<SchemaInfo> schemas = Arrays.asList(
            new SchemaInfo("schema1", "test-ds-1", 10, 5),
            new SchemaInfo("schema2", "test-ds-1", 15, 3)
        );
        
        // Mock服务方法
        when(dataSourceService.getDataSourceById("test-ds-1")).thenReturn(Optional.of(dataSource));
        when(dataSourceService.getSchemas(dataSource)).thenReturn(schemas);
        
        // 执行测试
        mockMvc.perform(get("/api/datasources/test-ds-1/schemas"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0].name").value("schema1"))
               .andExpect(jsonPath("$[0].tableCount").value(10))
               .andExpect(jsonPath("$[1].name").value("schema2"));
    }
}
```

## 结论

通过实现以上API，数据源管理模块将具备完整的元数据管理功能，包括获取Schema、表列表以及同步元数据。这些API是数据源管理页面前后端联调的关键部分，确保用户能够查看和操作数据源元数据。

实施时要注意处理性能和错误情况，使用异步处理和批量操作提高效率，同时提供友好的错误反馈。对于不同类型的数据库，需要在相应的适配器实现中处理特定的差异。