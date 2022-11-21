package net.test.jabref;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import net.sf.jabref.BibtexFields;
import net.sf.jabref.GUIGlobals;

class BibtexFieldsTest {
    @Test
    void testGetFieldExtras() {
        assertNull(BibtexFields.getFieldExtras("Name"));
        assertNull(BibtexFields.getFieldExtras(null));
        assertNull(BibtexFields.getFieldExtras(BibtexFields.GROUPSEARCH));
    }
    @SuppressWarnings("deprecation")
	@Test
    void testGetFieldWeight() {
        assertEquals(GUIGlobals.DEFAULT_FIELD_WEIGHT, BibtexFields.getFieldWeight("Name"));
        assertEquals(GUIGlobals.DEFAULT_FIELD_WEIGHT, BibtexFields.getFieldWeight(null));
        assertEquals(GUIGlobals.DEFAULT_FIELD_WEIGHT, BibtexFields.getFieldWeight(BibtexFields.GROUPSEARCH));
    }
    @Test
    void testGetFieldLength() {
        assertEquals(100, BibtexFields.getFieldLength("Name"));
        assertEquals(100, BibtexFields.getFieldLength(null));
        assertEquals(100, BibtexFields.getFieldLength(BibtexFields.GROUPSEARCH));
    }
    @Test
    void testGetFieldDisplayName() {
        assertNull(BibtexFields.getFieldDisplayName("Field Name"));
        assertNull(BibtexFields.getFieldDisplayName(null));
        assertNull(BibtexFields.getFieldDisplayName(BibtexFields.GROUPSEARCH));
    }
    @Test
    void testIsWriteableField() {
        assertTrue(BibtexFields.isWriteableField("Field"));
        assertTrue(BibtexFields.isWriteableField(null));
        assertFalse(BibtexFields.isWriteableField(BibtexFields.GROUPSEARCH));
        assertTrue(BibtexFields.isWriteableField(BibtexFields.MARKED));
    }
    @Test
    void testIsDisplayableField() {
        assertTrue(BibtexFields.isDisplayableField("Field"));
        assertTrue(BibtexFields.isDisplayableField(null));
        assertFalse(BibtexFields.isDisplayableField(BibtexFields.GROUPSEARCH));
        assertTrue(BibtexFields.isDisplayableField("abstract"));
    }
    @Test
    void testIsStandardField() {
        assertFalse(BibtexFields.isStandardField("Field"));
        assertFalse(BibtexFields.isStandardField(null));
        assertFalse(BibtexFields.isStandardField(BibtexFields.GROUPSEARCH));
        assertTrue(BibtexFields.isStandardField("address"));
    }
    @Test
    void testGetAllFieldNames() {
        assertEquals(36, BibtexFields.getAllFieldNames().length);
    }
    @Test
    void testGetFieldName() {
        assertEquals("address", BibtexFields.getFieldName(1));
    }
    @Test
    void testNumberOfPublicFields() {
        assertEquals(36, BibtexFields.numberOfPublicFields());
    }
}