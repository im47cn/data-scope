package com.insightdata.facade.rest;

import com.insightdata.application.service.DataSourceService;
import com.insightdata.common.enums.DataSourceType;
import com.insightdata.domain.model.DataSource;
import com.insightdata.domain.model.metadata.SchemaInfo;
import com.insightdata.domain.model.metadata.TableInfo;
import com.insightdata.facade.rest.dto.ConnectionTestRequest;
import com.insightdata.facade.rest.dto.ConnectionTestResponse;
import com.insightdata.facade.rest.dto.DataSourceCreateRequest;
import com.insightdata.facade.rest.dto.DataSourceResponse;
import com.insightdata.facade.rest.dto.DataSourceUpdateRequest;
import com.insightdata.facade.rest.dto.SchemaResponse;
import com.insightdata.facade.rest.dto.TableResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据源控制器
 */
@RestController
@RequestMapping("/datasources")
@Tag(name = "数据源管理", description = "数据源管理相关接口")
public class DataSourceController {
    
    private final DataSourceService dataSourceService;
    
    @Autowired
    public DataSourceController(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }
    
    @PostMapping
    @Operation(summary = "创建数据源", description = "创建新的数据源")
    public ResponseEntity<DataSourceResponse> createDataSource(@Valid @RequestBody DataSourceCreateRequest request) {
        // 转换请求为领域模型
        DataSource dataSource = convertToDataSource(request);
        
        // 创建数据源
        DataSource createdDataSource = dataSourceService.createDataSource(dataSource);
        
        // 转换响应
        DataSourceResponse response = convertToDataSourceResponse(createdDataSource);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新数据源", description = "更新现有数据源")
    public ResponseEntity<DataSourceResponse> updateDataSource(
            @Parameter(description = "数据源ID") @PathVariable Long id,
            @Valid @RequestBody DataSourceUpdateRequest request) {
        // 检查数据源是否存在
        DataSource existingDataSource = dataSourceService.getDataSourceById(id)
                .orElse(null);
        
        if (existingDataSource == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // 转换请求为领域模型
        DataSource dataSource = convertToDataSource(request);
        dataSource.setId(id);
        
        // 更新数据源
        DataSource updatedDataSource = dataSourceService.updateDataSource(dataSource);
        
        // 转换响应
        DataSourceResponse response = convertToDataSourceResponse(updatedDataSource);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "获取数据源", description = "根据ID获取数据源")
    public ResponseEntity<DataSourceResponse> getDataSource(
            @Parameter(description = "数据源ID") @PathVariable Long id) {
        // 获取数据源
        DataSource dataSource = dataSourceService.getDataSourceById(id)
                .orElse(null);
        
        if (dataSource == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // 转换响应
        DataSourceResponse response = convertToDataSourceResponse(dataSource);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping
    @Operation(summary = "获取数据源列表", description = "获取所有数据源或根据类型/状态筛选")
    public ResponseEntity<List<DataSourceResponse>> getDataSources(
            @Parameter(description = "数据源类型") @RequestParam(required = false) DataSourceType type,
            @Parameter(description = "是否激活") @RequestParam(required = false) Boolean active) {
        // 获取数据源列表
        List<DataSource> dataSources;
        
        if (type != null) {
            dataSources = dataSourceService.getDataSourcesByType(type);
        } else if (active != null) {
            dataSources = dataSourceService.getDataSourcesByActive(active);
        } else {
            dataSources = dataSourceService.getAllDataSources();
        }
        
        // 转换响应
        List<DataSourceResponse> response = dataSources.stream()
                .map(this::convertToDataSourceResponse)
                .collect(Collectors.toList());
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除数据源", description = "根据ID删除数据源")
    public ResponseEntity<Void> deleteDataSource(
            @Parameter(description = "数据源ID") @PathVariable Long id) {
        // 检查数据源是否存在
        if (!dataSourceService.getDataSourceById(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // 删除数据源
        dataSourceService.deleteDataSource(id);
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @PostMapping("/test-connection")
    @Operation(summary = "测试连接", description = "测试数据源连接")
    public ResponseEntity<ConnectionTestResponse> testConnection(@Valid @RequestBody ConnectionTestRequest request) {
        // 转换请求为领域模型
        DataSource dataSource = convertToDataSource(request);
        
        // 测试连接
        boolean success = dataSourceService.testConnection(dataSource);
        
        // 构建响应
        ConnectionTestResponse response = new ConnectionTestResponse();
        response.setSuccess(success);
        
        if (success) {
            response.setMessage("Connection successful");
        } else {
            response.setMessage("Connection failed");
        }
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/{id}/schemas")
    @Operation(summary = "获取模式列表", description = "获取数据源的所有模式")
    public ResponseEntity<List<SchemaResponse>> getSchemas(
            @Parameter(description = "数据源ID") @PathVariable Long id) {
        // 检查数据源是否存在
        if (!dataSourceService.getDataSourceById(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // 获取模式列表
        List<SchemaInfo> schemas = dataSourceService.getSchemas(id);
        
        // 转换响应
        List<SchemaResponse> response = schemas.stream()
                .map(this::convertToSchemaResponse)
                .collect(Collectors.toList());
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/{id}/schemas/{schemaName}/tables")
    @Operation(summary = "获取表列表", description = "获取指定模式下的所有表")
    public ResponseEntity<List<TableResponse>> getTables(
            @Parameter(description = "数据源ID") @PathVariable Long id,
            @Parameter(description = "模式名称") @PathVariable String schemaName) {
        // 检查数据源是否存在
        if (!dataSourceService.getDataSourceById(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // 获取表列表
        List<TableInfo> tables = dataSourceService.getTables(id, schemaName);
        
        // 转换响应
        List<TableResponse> response = tables.stream()
                .map(this::convertToTableResponse)
                .collect(Collectors.toList());
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping("/{id}/sync")
    @Operation(summary = "同步元数据", description = "同步数据源元数据")
    public ResponseEntity<String> syncMetadata(
            @Parameter(description = "数据源ID") @PathVariable Long id) {
        // 检查数据源是否存在
        if (!dataSourceService.getDataSourceById(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // 同步元数据
        String syncJobId = dataSourceService.syncMetadata(id);
        
        return new ResponseEntity<>(syncJobId, HttpStatus.ACCEPTED);
    }
    
    @GetMapping("/types")
    @Operation(summary = "获取支持的数据源类型", description = "获取系统支持的所有数据源类型")
    public ResponseEntity<List<String>> getSupportedTypes() {
        // 获取支持的数据源类型
        List<String> types = dataSourceService.getSupportedTypes().stream()
                .map(DataSourceType::name)
                .collect(Collectors.toList());
        
        return new ResponseEntity<>(types, HttpStatus.OK);
    }
    
    /**
     * 将创建请求转换为领域模型
     */
    private DataSource convertToDataSource(DataSourceCreateRequest request) {
        return DataSource.builder()
                .name(request.getName())
                .type(request.getType())
                .host(request.getHost())
                .port(request.getPort())
                .databaseName(request.getDatabaseName())
                .username(request.getUsername())
                .password(request.getPassword())
                .connectionProperties(request.getConnectionProperties())
                .description(request.getDescription())
                .build();
    }
    
    /**
     * 将更新请求转换为领域模型
     */
    private DataSource convertToDataSource(DataSourceUpdateRequest request) {
        return DataSource.builder()
                .name(request.getName())
                .type(request.getType())
                .host(request.getHost())
                .port(request.getPort())
                .databaseName(request.getDatabaseName())
                .username(request.getUsername())
                .password(request.getPassword())
                .connectionProperties(request.getConnectionProperties())
                .active(request.isActive())
                .description(request.getDescription())
                .build();
    }
    
    /**
     * 将连接测试请求转换为领域模型
     */
    private DataSource convertToDataSource(ConnectionTestRequest request) {
        return DataSource.builder()
                .type(request.getType())
                .host(request.getHost())
                .port(request.getPort())
                .databaseName(request.getDatabaseName())
                .username(request.getUsername())
                .password(request.getPassword())
                .connectionProperties(request.getConnectionProperties())
                .build();
    }
    
    /**
     * 将领域模型转换为响应
     */
    private DataSourceResponse convertToDataSourceResponse(DataSource dataSource) {
        DataSourceResponse response = new DataSourceResponse();
        response.setId(dataSource.getId());
        response.setName(dataSource.getName());
        response.setType(dataSource.getType());
        response.setHost(dataSource.getHost());
        response.setPort(dataSource.getPort());
        response.setDatabaseName(dataSource.getDatabaseName());
        response.setUsername(dataSource.getUsername());
        response.setConnectionProperties(dataSource.getConnectionProperties());
        response.setLastSyncTime(dataSource.getLastSyncTime());
        response.setActive(dataSource.isActive());
        response.setDescription(dataSource.getDescription());
        response.setCreatedAt(dataSource.getCreatedAt());
        response.setUpdatedAt(dataSource.getUpdatedAt());
        return response;
    }
    
    /**
     * 将模式信息转换为响应
     */
    private SchemaResponse convertToSchemaResponse(SchemaInfo schema) {
        SchemaResponse response = new SchemaResponse();
        response.setId(schema.getId());
        response.setName(schema.getName());
        response.setDescription(schema.getDescription());
        response.setCreatedAt(schema.getCreatedAt());
        response.setUpdatedAt(schema.getUpdatedAt());
        return response;
    }
    
    /**
     * 将表信息转换为响应
     */
    private TableResponse convertToTableResponse(TableInfo table) {
        TableResponse response = new TableResponse();
        response.setId(table.getId());
        response.setName(table.getName());
        response.setType(table.getType());
        response.setDescription(table.getDescription());
        response.setEstimatedRowCount(table.getEstimatedRowCount());
        response.setDataSize(table.getDataSize());
        response.setIndexSize(table.getIndexSize());
        response.setLastAnalyzed(table.getLastAnalyzed());
        response.setCreatedAt(table.getCreatedAt());
        response.setUpdatedAt(table.getUpdatedAt());
        return response;
    }
}