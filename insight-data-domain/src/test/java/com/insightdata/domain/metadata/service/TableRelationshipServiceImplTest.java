package com.insightdata.domain.metadata.service;

import com.insightdata.domain.metadata.model.*;
import com.insightdata.domain.metadata.service.impl.TableRelationshipServiceImpl;
import com.insightdata.domain.lowcode.repository.TableRelationshipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * TableRelationshipServiceImpl集成测试
 * 验证与ForeignKeyInfo的API集成
 */
public class TableRelationshipServiceImplTest {

    @Mock
    private TableRelationshipRepository tableRelationshipRepository;

    @InjectMocks
    private TableRelationshipServiceImpl tableRelationshipService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(tableRelationshipRepository.save(any(TableRelationship.class))).thenAnswer(i -> i.getArguments()[0]);
    }

    /**
     * 测试learnFromMetadata方法是否正确使用ForeignKeyInfo的字符串格式列名
     */
    @Test
    @DisplayName("测试learnFromMetadata方法 - 正确处理ForeignKeyInfo对象")
    public void testLearnFromMetadata_CorrectlyHandlesForeignKeyInfo() {
        // 准备测试数据 - 创建包含外键的表结构
        String dataSourceId = "test-ds-001";
        
        // 创建外键列信息
        List<ForeignKeyColumnInfo> columns = Arrays.asList(
                ForeignKeyColumnInfo.builder().sourceColumnName("order_id").targetColumnName("id").build(),
                ForeignKeyColumnInfo.builder().sourceColumnName("customer_code").targetColumnName("code").build()
        );
        
        // 创建外键信息
        ForeignKeyInfo foreignKeyInfo = ForeignKeyInfo.builder()
                .name("fk_order_customer")
                .sourceTableName("orders")
                .targetTableName("customers")
                .columns(columns)
                .build();
        
        // 创建表信息
        TableInfo orderTable = new TableInfo();
        orderTable.setName("orders");
        orderTable.setForeignKeys(Collections.singletonList(foreignKeyInfo));
        orderTable.setColumns(new ArrayList<>());
        
        // 创建schema信息
        SchemaInfo schemaInfo = new SchemaInfo();
        schemaInfo.setName("test_schema");
        schemaInfo.setTables(Collections.singletonList(orderTable));
        
        // 执行测试
        List<TableRelationship> relationships = tableRelationshipService.learnFromMetadata(dataSourceId, schemaInfo);
        
        // 验证结果
        assertEquals(1, relationships.size());
        
        // 捕获保存到repository的TableRelationship对象
        ArgumentCaptor<TableRelationship> relationshipCaptor = ArgumentCaptor.forClass(TableRelationship.class);
        verify(tableRelationshipRepository, times(1)).save(relationshipCaptor.capture());
        
        TableRelationship savedRelationship = relationshipCaptor.getValue();
        
        // 验证字符串格式的列名是否正确
        assertEquals("order_id,customer_code", savedRelationship.getSourceColumnNames());
        assertEquals("id,code", savedRelationship.getTargetColumnNames());
        assertEquals("orders", savedRelationship.getSourceTableName());
        assertEquals("customers", savedRelationship.getTargetTableName());
        assertEquals(TableRelationship.RelationshipType.MANY_TO_ONE, savedRelationship.getRelationType());
        assertEquals(TableRelationship.RelationshipSource.METADATA, savedRelationship.getRelationSource());
        assertEquals(1.0, savedRelationship.getConfidence());
    }

    /**
     * 测试learnFromMetadata方法处理单列外键
     */
    @Test
    @DisplayName("测试learnFromMetadata方法 - 处理单列外键")
    public void testLearnFromMetadata_SingleColumnForeignKey() {
        // 准备测试数据
        String dataSourceId = "test-ds-001";
        
        // 创建单列外键
        List<ForeignKeyColumnInfo> columns = Collections.singletonList(
                ForeignKeyColumnInfo.builder().sourceColumnName("product_id").targetColumnName("id").build()
        );
        
        ForeignKeyInfo foreignKeyInfo = ForeignKeyInfo.builder()
                .name("fk_orderitem_product")
                .sourceTableName("order_items")
                .targetTableName("products")
                .columns(columns)
                .build();
        
        TableInfo orderItemTable = new TableInfo();
        orderItemTable.setName("order_items");
        orderItemTable.setForeignKeys(Collections.singletonList(foreignKeyInfo));
        orderItemTable.setColumns(new ArrayList<>());
        
        SchemaInfo schemaInfo = new SchemaInfo();
        schemaInfo.setName("test_schema");
        schemaInfo.setTables(Collections.singletonList(orderItemTable));
        
        // 执行测试
        List<TableRelationship> relationships = tableRelationshipService.learnFromMetadata(dataSourceId, schemaInfo);
        
        // 验证结果
        assertEquals(1, relationships.size());
        
        ArgumentCaptor<TableRelationship> relationshipCaptor = ArgumentCaptor.forClass(TableRelationship.class);
        verify(tableRelationshipRepository, times(1)).save(relationshipCaptor.capture());
        
        TableRelationship savedRelationship = relationshipCaptor.getValue();
        
        // 验证单列外键的字符串格式是否正确
        assertEquals("product_id", savedRelationship.getSourceColumnNames());
        assertEquals("id", savedRelationship.getTargetColumnNames());
    }

    /**
     * 测试learnFromMetadata方法处理多张表和多个外键
     */
    @Test
    @DisplayName("测试learnFromMetadata方法 - 处理多张表和多个外键")
    public void testLearnFromMetadata_MultipleTablesAndForeignKeys() {
        // 准备测试数据
        String dataSourceId = "test-ds-001";
        
        // 创建第一个外键 (orders -> customers)
        List<ForeignKeyColumnInfo> fk1Columns = Collections.singletonList(
                ForeignKeyColumnInfo.builder().sourceColumnName("customer_id").targetColumnName("id").build()
        );
        
        ForeignKeyInfo foreignKeyInfo1 = ForeignKeyInfo.builder()
                .name("fk_order_customer")
                .sourceTableName("orders")
                .targetTableName("customers")
                .columns(fk1Columns)
                .build();
        
        // 创建第二个外键 (order_items -> orders)
        List<ForeignKeyColumnInfo> fk2Columns = Collections.singletonList(
                ForeignKeyColumnInfo.builder().sourceColumnName("order_id").targetColumnName("id").build()
        );
        
        ForeignKeyInfo foreignKeyInfo2 = ForeignKeyInfo.builder()
                .name("fk_orderitem_order")
                .sourceTableName("order_items")
                .targetTableName("orders")
                .columns(fk2Columns)
                .build();
        
        // 创建第三个外键 (order_items -> products)
        List<ForeignKeyColumnInfo> fk3Columns = Collections.singletonList(
                ForeignKeyColumnInfo.builder().sourceColumnName("product_id").targetColumnName("id").build()
        );
        
        ForeignKeyInfo foreignKeyInfo3 = ForeignKeyInfo.builder()
                .name("fk_orderitem_product")
                .sourceTableName("order_items")
                .targetTableName("products")
                .columns(fk3Columns)
                .build();
        
        // 创建表信息
        TableInfo ordersTable = new TableInfo();
        ordersTable.setName("orders");
        ordersTable.setForeignKeys(Collections.singletonList(foreignKeyInfo1));
        ordersTable.setColumns(new ArrayList<>());
        
        TableInfo orderItemsTable = new TableInfo();
        orderItemsTable.setName("order_items");
        orderItemsTable.setForeignKeys(Arrays.asList(foreignKeyInfo2, foreignKeyInfo3));
        orderItemsTable.setColumns(new ArrayList<>());
        
        SchemaInfo schemaInfo = new SchemaInfo();
        schemaInfo.setName("test_schema");
        schemaInfo.setTables(Arrays.asList(ordersTable, orderItemsTable));
        
        // 执行测试
        List<TableRelationship> relationships = tableRelationshipService.learnFromMetadata(dataSourceId, schemaInfo);
        
        // 验证结果
        assertEquals(3, relationships.size());
        
        // 验证保存方法被调用了3次
        verify(tableRelationshipRepository, times(3)).save(any(TableRelationship.class));
    }
}
