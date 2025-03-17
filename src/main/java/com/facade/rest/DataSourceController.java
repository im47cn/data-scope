package com.facade.rest;

import java.util.List;
import java.util.stream.Collectors;

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

import com.common.enums.DataSourceType;
import com.domain.model.DataSource;
import com.domain.service.DataSourceService;
import com.facade.dto.ConnectionTestResult;
import com.facade.dto.DataSourceDTO;
import com.facade.dto.DataSourceListDTO;
import com.facade.dto.MetadataSyncResult;
import com.facade.dto.SchemaInfoDTO;
import com.facade.dto.TableInfoDTO;
import com.facade.mapper.DataSourceMapper;

/**
 * 数据源控制器
 */
@RestController
@RequestMapping("/api/datasources")
public class DataSourceController {

    private final DataSourceService dataSourceService;
    private final DataSourceMapper dataSourceMapper;

    @Autowired
    public DataSourceController(DataSourceService dataSourceService, DataSourceMapper dataSourceMapper) {
        this.dataSourceService = dataSourceService;
        this.dataSourceMapper = dataSourceMapper;
    }

    /**
     * 获取所有数据源
     */
    @GetMapping
    public ResponseEntity<List<DataSourceListDTO>> getAllDataSources(
            @RequestParam(required = false) DataSourceType type,
            @RequestParam(required = false) Boolean active) {
        
        List<DataSource> dataSources;
        
        if (type != null) {
            dataSources = dataSourceService.getDataSourcesByType(type);
        } else if (active != null) {
            dataSources = dataSourceService.getDataSourcesByActive(active);
        } else {
            dataSources = dataSourceService.getAllDataSources();
        }
        
        List<DataSourceListDTO> dtoList = dataSources.stream()
                .map(dataSourceMapper::toListDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(dtoList);
    }

    /**
     * 获取指定数据源
     */
    @GetMapping("/{id}")
    public ResponseEntity<DataSourceDTO> getDataSourceById(@PathVariable String id) {
        return dataSourceService.getDataSourceById(id)
                .map(dataSourceMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 创建数据源
     */
    @PostMapping
    public ResponseEntity<DataSourceDTO> createDataSource(@RequestBody DataSourceDTO dataSourceDTO) {
        DataSource dataSource = dataSourceMapper.toEntity(dataSourceDTO);
        DataSource createdDataSource = dataSourceService.createDataSource(dataSource);
        return new ResponseEntity<>(dataSourceMapper.toDTO(createdDataSource), HttpStatus.CREATED);
    }

    /**
     * 更新数据源
     */
    @PutMapping("/{id}")
    public ResponseEntity<DataSourceDTO> updateDataSource(
            @PathVariable String id, 
            @RequestBody DataSourceDTO dataSourceDTO) {
        
        // 设置ID
        dataSourceDTO.setId(id);
        
        DataSource dataSource = dataSourceMapper.toEntity(dataSourceDTO);
        DataSource updatedDataSource = dataSourceService.updateDataSource(dataSource);
        
        return ResponseEntity.ok(dataSourceMapper.toDTO(updatedDataSource));
    }

    /**
     * 删除数据源
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDataSource(@PathVariable String id) {
        dataSourceService.deleteDataSource(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 测试数据源连接
     */
    @PostMapping("/{id}/test-connection")
    public ResponseEntity<ConnectionTestResult> testConnection(@PathVariable String id) {
        return dataSourceService.getDataSourceById(id)
                .map(dataSource -> {
                    boolean success = dataSourceService.testConnection(dataSource);
                    ConnectionTestResult result = new ConnectionTestResult();
                    result.setSuccess(success);
                    result.setMessage(success ? "连接成功" : "连接失败");
                    return ResponseEntity.ok(result);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 获取数据源支持的类型
     */
    @GetMapping("/types")
    public ResponseEntity<List<DataSourceType>> getSupportedTypes() {
        return ResponseEntity.ok(dataSourceService.getSupportedTypes());
    }

    /**
     * 获取数据源的Schema列表
     */
    @GetMapping("/{id}/schemas")
    public ResponseEntity<List<SchemaInfoDTO>> getSchemas(@PathVariable String id) {
        return ResponseEntity.ok().build(); // TODO: 实现获取Schema列表的逻辑
    }

    /**
     * 获取指定Schema的表列表
     */
    @GetMapping("/{id}/schemas/{schemaName}/tables")
    public ResponseEntity<List<TableInfoDTO>> getTables(
            @PathVariable String id, 
            @PathVariable String schemaName) {
        return ResponseEntity.ok().build(); // TODO: 实现获取表列表的逻辑
    }

    /**
     * 同步数据源元数据
     */
    @PostMapping("/{id}/sync")
    public ResponseEntity<MetadataSyncResult> syncMetadata(@PathVariable String id) {
        return ResponseEntity.ok().build(); // TODO: 实现同步元数据的逻辑
    }
}