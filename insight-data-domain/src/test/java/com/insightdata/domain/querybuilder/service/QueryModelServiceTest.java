package com.insightdata.domain.querybuilder.service;

import com.insightdata.domain.querybuilder.api.QueryModelContract;
import com.insightdata.domain.querybuilder.model.*;
import com.insightdata.domain.querybuilder.repository.QueryModelRepository;
import com.insightdata.domain.querybuilder.service.impl.QueryModelServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * QueryModelService 的单元测试类
 */
class QueryModelServiceTest {

    @Mock
    private QueryModelRepository repository;

    private QueryModelApplicationService service;
    private QueryModel testModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new QueryModelServiceImpl(repository);
        
        // 创建测试数据
        testModel = new QueryModel();
        testModel.setId("test-id");
        testModel.setName("test-model");
        testModel.setTables(Arrays.asList("users", "orders"));
        testModel.setFields(Arrays.asList("name", "email"));
    }

    @Test
    void shouldCreateValidModel() {
        when(repository.save(any(QueryModel.class))).thenReturn(testModel);

        QueryModelContract result = service.create(testModel);

        assertNotNull(result);
        assertEquals(testModel.getId(), result.getId());
        verify(repository).save(any(QueryModel.class));
    }

    @Test
    void shouldNotCreateInvalidModel() {
        testModel.setName("");
        
        assertThrows(IllegalArgumentException.class, () -> {
            service.create(testModel);
        });
        
        verify(repository, never()).save(any(QueryModel.class));
    }

    @Test
    void shouldUpdateExistingModel() {
        when(repository.existsById(anyString())).thenReturn(true);
        when(repository.save(any(QueryModel.class))).thenReturn(testModel);

        QueryModelContract result = service.update(testModel);

        assertNotNull(result);
        assertEquals(testModel.getId(), result.getId());
        verify(repository).save(any(QueryModel.class));
    }

    @Test
    void shouldNotUpdateNonExistingModel() {
        when(repository.existsById(anyString())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            service.update(testModel);
        });

        verify(repository, never()).save(any(QueryModel.class));
    }

    @Test
    void shouldFindModelById() {
        when(repository.findById(anyString())).thenReturn(Optional.of(testModel));

        Optional<QueryModelContract> result = service.findById("test-id");

        assertTrue(result.isPresent());
        assertEquals(testModel.getId(), result.get().getId());
    }

    @Test
    void shouldFindModelsByName() {
        when(repository.findByNameContaining(anyString()))
            .thenReturn(Arrays.asList(testModel));

        List<QueryModelContract> results = service.findByName("test");

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(testModel.getName(), results.get(0).getName());
    }

    @Test
    void shouldDeleteModel() {
        doNothing().when(repository).deleteById(anyString());

        service.delete("test-id");

        verify(repository).deleteById("test-id");
    }

    @Test
    void shouldValidateModel() {
        assertTrue(service.validate(testModel));

        testModel.setName(null);
        assertFalse(service.validate(testModel));

        testModel.setName("test");
        testModel.setTables(Collections.emptyList());
        assertFalse(service.validate(testModel));

        testModel.setTables(Arrays.asList("users"));
        testModel.setFields(Collections.emptyList());
        assertFalse(service.validate(testModel));
    }

    @Test
    void shouldCopyModel() {
        when(repository.findById(anyString())).thenReturn(Optional.of(testModel));
        when(repository.save(any(QueryModel.class))).thenReturn(testModel);

        QueryModelContract copy = service.copy("test-id", "new-name");

        assertNotNull(copy);
        assertEquals("new-name", copy.getName());
        verify(repository).save(any(QueryModel.class));
    }

    @Test
    void shouldNotCopyNonExistingModel() {
        when(repository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            service.copy("test-id", "new-name");
        });

        verify(repository, never()).save(any(QueryModel.class));
    }

    @Test
    void shouldValidateParameters() {
        Map<String, Object> validParams = new HashMap<>();
        validParams.put("userId", 123);
        validParams.put("status", "active");

        assertTrue(service.validateParameters("test-id", validParams));
    }

    @Test
    void shouldPaginateResults() {
        when(repository.findAll(anyInt(), anyInt()))
            .thenReturn(Arrays.asList(testModel));
        when(repository.count()).thenReturn(1L);

        List<QueryModelContract> results = service.findAll(0, 10);
        long count = service.count();

        assertEquals(1, results.size());
        assertEquals(1L, count);
    }

    @Test
    void shouldGetSqlPreview() {
        when(repository.findById(anyString())).thenReturn(Optional.of(testModel));

        String sql = service.getSqlPreview("test-id");

        assertNotNull(sql);
        verify(repository).findById("test-id");
    }

    @Test
    void shouldGetExecutionPlan() {
        when(repository.findById(anyString())).thenReturn(Optional.of(testModel));

        String plan = service.getExecutionPlan("test-id");

        assertNotNull(plan);
        verify(repository).findById("test-id");
    }
}