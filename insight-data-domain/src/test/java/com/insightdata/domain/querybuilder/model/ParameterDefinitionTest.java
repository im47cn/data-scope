package com.insightdata.domain.querybuilder.model;

import com.insightdata.domain.querybuilder.api.QueryModelContract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ParameterDefinition 的单元测试类
 */
class ParameterDefinitionTest {

    private QueryModelContract mockModel;

    @BeforeEach
    void setUp() {
        mockModel = Mockito.mock(QueryModelContract.class);
    }

    @Test
    void shouldCreateEmptyParameterDefinition() {
        ParameterDefinition param = new ParameterDefinition();
        
        assertNull(param.getName());
        assertNull(param.getDataType());
        assertNull(param.getDefaultValue());
        assertFalse(param.isRequired());
        assertNull(param.getQueryModel());
    }

    @Test
    void shouldCreateParameterDefinitionWithBasicInfo() {
        ParameterDefinition param = new ParameterDefinition("userId", "INTEGER");
        
        assertEquals("userId", param.getName());
        assertEquals("INTEGER", param.getDataType());
        assertNull(param.getDefaultValue());
        assertFalse(param.isRequired());
    }

    @Test
    void shouldCreateParameterDefinitionWithFullInfo() {
        ParameterDefinition param = new ParameterDefinition("userId", "INTEGER", 
            1, true, mockModel);
        
        assertEquals("userId", param.getName());
        assertEquals("INTEGER", param.getDataType());
        assertEquals(1, param.getDefaultValue());
        assertTrue(param.isRequired());
        assertSame(mockModel, param.getQueryModel());
    }

    @Test
    void shouldValidateStringParameter() {
        ParameterDefinition param = new ParameterDefinition("name", "STRING");
        
        assertTrue(param.validateValue("test"));
        assertFalse(param.validateValue(123));
        
        param.setValidationPattern("[A-Za-z]+");
        assertTrue(param.validateValue("test"));
        assertFalse(param.validateValue("test123"));
    }

    @Test
    void shouldValidateIntegerParameter() {
        ParameterDefinition param = new ParameterDefinition("age", "INTEGER");
        
        assertTrue(param.validateValue(123));
        assertTrue(param.validateValue(123L));
        assertFalse(param.validateValue("123"));
        assertFalse(param.validateValue(123.45));
    }

    @Test
    void shouldValidateDecimalParameter() {
        ParameterDefinition param = new ParameterDefinition("price", "DECIMAL");
        
        assertTrue(param.validateValue(123.45));
        assertTrue(param.validateValue(123.45f));
        assertFalse(param.validateValue("123.45"));
        assertFalse(param.validateValue(123));
    }

    @Test
    void shouldValidateBooleanParameter() {
        ParameterDefinition param = new ParameterDefinition("active", "BOOLEAN");
        
        assertTrue(param.validateValue(true));
        assertTrue(param.validateValue(false));
        assertFalse(param.validateValue("true"));
        assertFalse(param.validateValue(1));
    }

    @Test
    void shouldValidateDateParameter() {
        ParameterDefinition param = new ParameterDefinition("birthDate", "DATE");
        
        assertTrue(param.validateValue(new Date()));
        assertTrue(param.validateValue(LocalDate.now()));
        assertFalse(param.validateValue("2025-03-21"));
        assertFalse(param.validateValue(LocalDateTime.now()));
    }

    @Test
    void shouldValidateDateTimeParameter() {
        ParameterDefinition param = new ParameterDefinition("createdAt", "DATETIME");
        
        assertTrue(param.validateValue(new Date()));
        assertTrue(param.validateValue(LocalDateTime.now()));
        assertFalse(param.validateValue("2025-03-21 12:00:00"));
        assertFalse(param.validateValue(LocalDate.now()));
    }

    @Test
    void shouldHandleRequiredParameter() {
        ParameterDefinition param = new ParameterDefinition("userId", "INTEGER");
        param.setRequired(true);
        
        assertFalse(param.validateValue(null));
        assertTrue(param.validateValue(123));
    }

    @Test
    void shouldHandleOptionalParameter() {
        ParameterDefinition param = new ParameterDefinition("userId", "INTEGER");
        param.setRequired(false);
        
        assertTrue(param.validateValue(null));
        assertTrue(param.validateValue(123));
    }

    @Test
    void shouldCopyParameterDefinition() {
        ParameterDefinition original = new ParameterDefinition("userId", "INTEGER", 
            1, true, mockModel);
        original.setDescription("User ID parameter");
        original.setValidationPattern("\\d+");

        ParameterDefinition copy = original.copy();

        assertEquals(original.getName(), copy.getName());
        assertEquals(original.getDataType(), copy.getDataType());
        assertEquals(original.getDefaultValue(), copy.getDefaultValue());
        assertEquals(original.isRequired(), copy.isRequired());
        assertEquals(original.getDescription(), copy.getDescription());
        assertEquals(original.getValidationPattern(), copy.getValidationPattern());
        assertSame(original.getQueryModel(), copy.getQueryModel());
    }

    @Test
    void shouldValidateParameterDefinition() {
        ParameterDefinition validParam = new ParameterDefinition("userId", "INTEGER");
        assertTrue(validParam.isValid());

        ParameterDefinition invalidParam1 = new ParameterDefinition(null, "INTEGER");
        assertFalse(invalidParam1.isValid());

        ParameterDefinition invalidParam2 = new ParameterDefinition("", "INTEGER");
        assertFalse(invalidParam2.isValid());

        ParameterDefinition invalidParam3 = new ParameterDefinition("userId", null);
        assertFalse(invalidParam3.isValid());

        ParameterDefinition invalidParam4 = new ParameterDefinition("userId", "");
        assertFalse(invalidParam4.isValid());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        ParameterDefinition param1 = new ParameterDefinition("userId", "INTEGER", 
            1, true, mockModel);
        ParameterDefinition param2 = new ParameterDefinition("userId", "INTEGER", 
            2, true, mockModel);
        ParameterDefinition param3 = new ParameterDefinition("userId", "STRING", 
            1, true, mockModel);

        assertEquals(param1, param2); // 默认值不影响相等性
        assertNotEquals(param1, param3);
        assertEquals(param1.hashCode(), param2.hashCode());
    }

    @Test
    void shouldImplementToString() {
        ParameterDefinition param = new ParameterDefinition("userId", "INTEGER", 
            1, true, mockModel);
        assertEquals("userId (INTEGER) [Required] = 1", param.toString());

        ParameterDefinition optionalParam = new ParameterDefinition("name", "STRING");
        assertEquals("name (STRING)", optionalParam.toString());
    }
}