package com.insightdata.domain.querybuilder.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.insightdata.domain.querybuilder.api.QueryModelContract;

/**
 * TableReference 的单元测试类
 */
class TableReferenceTest {

    @Test
    void shouldCreateEmptyTableReference() {
        TableReference ref = new TableReference();
        
        assertNull(ref.getTableName());
        assertNull(ref.getAlias());
        assertNull(ref.getQueryModel());
        assertFalse(ref.isPrimary());
    }

    @Test
    void shouldCreateTableReferenceWithNameAndAlias() {
        TableReference ref = new TableReference("users", "u");
        
        assertEquals("users", ref.getTableName());
        assertEquals("u", ref.getAlias());
        assertNull(ref.getQueryModel());
    }

    @Test
    void shouldCreateTableReferenceWithQueryModel() {
        QueryModelContract mockModel = Mockito.mock(QueryModelContract.class);
        TableReference ref = new TableReference("users", "u", mockModel);
        
        assertEquals("users", ref.getTableName());
        assertEquals("u", ref.getAlias());
        assertSame(mockModel, ref.getQueryModel());
    }

    @Test
    void shouldGenerateFullReference() {
        TableReference ref1 = new TableReference("users", "u");
        assertEquals("users AS u", ref1.getFullReference());

        TableReference ref2 = new TableReference("users", null);
        assertEquals("users", ref2.getFullReference());

        TableReference ref3 = new TableReference("users", "");
        assertEquals("users", ref3.getFullReference());

        TableReference ref4 = new TableReference("users", " ");
        assertEquals("users", ref4.getFullReference());
    }

    @Test
    void shouldGetReferenceIdentifier() {
        TableReference ref1 = new TableReference("users", "u");
        assertEquals("u", ref1.getReferenceIdentifier());

        TableReference ref2 = new TableReference("users", null);
        assertEquals("users", ref2.getReferenceIdentifier());

        TableReference ref3 = new TableReference("users", "");
        assertEquals("users", ref3.getReferenceIdentifier());

        TableReference ref4 = new TableReference("users", " ");
        assertEquals("users", ref4.getReferenceIdentifier());
    }

    @Test
    void shouldCopyTableReference() {
        QueryModelContract mockModel = Mockito.mock(QueryModelContract.class);
        TableReference original = new TableReference("users", "u", mockModel);
        original.setDescription("User table");
        original.setPrimary(true);

        TableReference copy = original.copy();

        assertEquals(original.getTableName(), copy.getTableName());
        assertEquals(original.getAlias(), copy.getAlias());
        assertSame(original.getQueryModel(), copy.getQueryModel()); // 浅复制
        assertEquals(original.getDescription(), copy.getDescription());
        assertEquals(original.isPrimary(), copy.isPrimary());
    }

    @Test
    void shouldValidateTableReference() {
        TableReference ref1 = new TableReference("users", "u");
        assertTrue(ref1.isValid());

        TableReference ref2 = new TableReference(null, "u");
        assertFalse(ref2.isValid());

        TableReference ref3 = new TableReference("", "u");
        assertFalse(ref3.isValid());

        TableReference ref4 = new TableReference(" ", "u");
        assertFalse(ref4.isValid());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        TableReference ref1 = new TableReference("users", "u");
        TableReference ref2 = new TableReference("users", "u");
        TableReference ref3 = new TableReference("users", "u2");
        TableReference ref4 = new TableReference("customers", "u");

        assertEquals(ref1, ref2);
        assertNotEquals(ref1, ref3);
        assertNotEquals(ref1, ref4);
        assertEquals(ref1.hashCode(), ref2.hashCode());
    }

    @Test
    void shouldHandleNullValuesInEqualsAndHashCode() {
        TableReference ref1 = new TableReference(null, null);
        TableReference ref2 = new TableReference(null, null);
        TableReference ref3 = new TableReference("users", null);
        TableReference ref4 = new TableReference(null, "u");

        assertEquals(ref1, ref2);
        assertNotEquals(ref1, ref3);
        assertNotEquals(ref1, ref4);
        assertEquals(ref1.hashCode(), ref2.hashCode());
    }

    @Test
    void shouldImplementToString() {
        TableReference ref1 = new TableReference("users", "u");
        assertEquals("users AS u", ref1.toString());

        TableReference ref2 = new TableReference("users", null);
        assertEquals("users", ref2.toString());
    }
}