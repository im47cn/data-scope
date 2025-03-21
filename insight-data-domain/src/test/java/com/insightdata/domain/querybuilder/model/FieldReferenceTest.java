package com.insightdata.domain.querybuilder.model;

import com.insightdata.domain.querybuilder.api.QueryModelContract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FieldReference 的单元测试类
 */
class FieldReferenceTest {

    private TableReference mockTable;
    private QueryModelContract mockModel;

    @BeforeEach
    void setUp() {
        mockTable = new TableReference("users", "u");
        mockModel = Mockito.mock(QueryModelContract.class);
    }

    @Test
    void shouldCreateEmptyFieldReference() {
        FieldReference ref = new FieldReference();
        
        assertNull(ref.getTable());
        assertNull(ref.getFieldName());
        assertNull(ref.getQueryModel());
        assertNull(ref.getDataType());
    }

    @Test
    void shouldCreateFieldReferenceWithBasicInfo() {
        FieldReference ref = new FieldReference(mockTable, "name");
        
        assertSame(mockTable, ref.getTable());
        assertEquals("name", ref.getFieldName());
        assertNull(ref.getQueryModel());
    }

    @Test
    void shouldCreateFieldReferenceWithFullInfo() {
        FieldReference ref = new FieldReference(mockTable, "name", mockModel);
        
        assertSame(mockTable, ref.getTable());
        assertEquals("name", ref.getFieldName());
        assertSame(mockModel, ref.getQueryModel());
    }

    @Test
    void shouldGenerateFullReference() {
        FieldReference ref = new FieldReference(mockTable, "name");
        assertEquals("u.name", ref.getFullReference());
    }

    @Test
    void shouldHandleInvalidReference() {
        FieldReference ref1 = new FieldReference(null, "name");
        assertEquals("", ref1.getFullReference());

        FieldReference ref2 = new FieldReference(mockTable, null);
        assertEquals("", ref2.getFullReference());

        FieldReference ref3 = new FieldReference(mockTable, "");
        assertEquals("", ref3.getFullReference());

        FieldReference ref4 = new FieldReference(mockTable, " ");
        assertEquals("", ref4.getFullReference());
    }

    @Test
    void shouldCopyFieldReference() {
        FieldReference original = new FieldReference(mockTable, "name", mockModel);
        original.setDescription("Test field");
        original.setDataType("VARCHAR");

        FieldReference copy = original.copy();

        // 验证深度复制
        assertNotSame(original.getTable(), copy.getTable());
        assertEquals(original.getTable().getTableName(), copy.getTable().getTableName());
        assertEquals(original.getTable().getAlias(), copy.getTable().getAlias());
        
        // 验证其他属性
        assertEquals(original.getFieldName(), copy.getFieldName());
        assertEquals(original.getDescription(), copy.getDescription());
        assertEquals(original.getDataType(), copy.getDataType());
        
        // 验证浅复制
        assertSame(original.getQueryModel(), copy.getQueryModel());
    }

    @Test
    void shouldValidateFieldReference() {
        FieldReference validRef = new FieldReference(mockTable, "name");
        assertTrue(validRef.isValid());

        FieldReference invalidRef1 = new FieldReference(null, "name");
        assertFalse(invalidRef1.isValid());

        FieldReference invalidRef2 = new FieldReference(mockTable, null);
        assertFalse(invalidRef2.isValid());

        FieldReference invalidRef3 = new FieldReference(mockTable, "");
        assertFalse(invalidRef3.isValid());

        FieldReference invalidRef4 = new FieldReference(mockTable, " ");
        assertFalse(invalidRef4.isValid());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        FieldReference ref1 = new FieldReference(mockTable, "name");
        FieldReference ref2 = new FieldReference(mockTable, "name");
        FieldReference ref3 = new FieldReference(mockTable, "age");

        assertEquals(ref1, ref2);
        assertNotEquals(ref1, ref3);
        assertEquals(ref1.hashCode(), ref2.hashCode());
    }

    @Test
    void shouldHandleNullValuesInEqualsAndHashCode() {
        FieldReference ref1 = new FieldReference(null, null);
        FieldReference ref2 = new FieldReference(null, null);
        FieldReference ref3 = new FieldReference(mockTable, null);

        assertEquals(ref1, ref2);
        assertNotEquals(ref1, ref3);
        assertEquals(ref1.hashCode(), ref2.hashCode());
    }

    @Test
    void shouldImplementToString() {
        FieldReference ref = new FieldReference(mockTable, "name");
        assertEquals("u.name", ref.toString());
    }
}