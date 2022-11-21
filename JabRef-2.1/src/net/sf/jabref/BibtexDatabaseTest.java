package net.sf.jabref;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import ca.odell.glazedlists.impl.adt.AgedNodeComparator;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import net.sf.jabref.*;
import net.sf.jabref.gui.GlazedEntrySorter;


import org.junit.Test;

class BibtexDatabaseTest {
    @Test
    void testGetEntryCount() {
        assertEquals(0, (new BibtexDatabase()).getEntryCount());
    }
    @Test
    void testGetKeySet() {
        assertTrue((new BibtexDatabase()).getKeySet().isEmpty());
    }
    @Test
    void testGetSorter() {
        BibtexDatabase bibtexDatabase = new BibtexDatabase();
        EntrySorter actualSorter = bibtexDatabase.getSorter(new AgedNodeComparator());
        assertEquals(0, actualSorter.getEntryCount());
        assertTrue(actualSorter.getSet().isEmpty());
        assertEquals(0, actualSorter.getIdArray().length);
        assertEquals(0, actualSorter.getEntryArray().length);
        assertTrue(actualSorter.getComp() instanceof AgedNodeComparator);
        assertFalse(actualSorter.isOutdated());
        assertEquals(1, bibtexDatabase.getChangeListeners().size());
    }
    @Test
    void testGetSorter2() throws KeyCollisionException {
        BibtexDatabase bibtexDatabase = new BibtexDatabase();
        bibtexDatabase.insertEntry(PreviewPrefsTab.getTestEntry());
        EntrySorter actualSorter = bibtexDatabase.getSorter(new AgedNodeComparator());
        assertEquals(1, actualSorter.getEntryCount());
        assertEquals(1, actualSorter.getSet().size());
        String[] stringArray = actualSorter.getIdArray();
        assertEquals(1, stringArray.length);
        assertEquals(1, actualSorter.getEntryArray().length);
        assertFalse(actualSorter.isOutdated());
        assertTrue(actualSorter.getComp() instanceof AgedNodeComparator);
        assertEquals("00000000", stringArray[0]);
        assertEquals(1, bibtexDatabase.getChangeListeners().size());
    }
    @Test
    void testGetEntryById() {
        assertNull((new BibtexDatabase()).getEntryById("42"));
    }
    @Test
    void testGetEntries() {
        assertTrue((new BibtexDatabase()).getEntries().isEmpty());
    }
    @Test
    void testGetEntryByKey() {
        assertNull((new BibtexDatabase()).getEntryByKey("Key"));
    }
    @Test
    void testGetEntryByKey2() throws KeyCollisionException {
        BibtexDatabase bibtexDatabase = new BibtexDatabase();
        bibtexDatabase.insertEntry(PreviewPrefsTab.getTestEntry());
        assertNull(bibtexDatabase.getEntryByKey("Key"));
    }
    @Test
    void testGetEntriesByKey() {
        assertEquals(0, (new BibtexDatabase()).getEntriesByKey("Key").length);
    }
    @Test
    void testGetEntriesByKey2() throws KeyCollisionException {
        BibtexDatabase bibtexDatabase = new BibtexDatabase();
        bibtexDatabase.insertEntry(PreviewPrefsTab.getTestEntry());
        assertEquals(0, bibtexDatabase.getEntriesByKey("Key").length);
    }
    @Test
    void testInsertEntry() throws KeyCollisionException {
        BibtexDatabase bibtexDatabase = new BibtexDatabase();
        assertFalse(bibtexDatabase.insertEntry(PreviewPrefsTab.getTestEntry()));
        assertEquals(1, bibtexDatabase.getEntryMap().size());
    }
    @Test
    void testInsertEntry2() throws KeyCollisionException {
        BibtexDatabase bibtexDatabase = new BibtexDatabase();
        bibtexDatabase.insertEntry(PreviewPrefsTab.getTestEntry());
        assertThrows(KeyCollisionException.class, () -> bibtexDatabase.insertEntry(PreviewPrefsTab.getTestEntry()));
    }
    @Test
    void testInsertEntry3() throws KeyCollisionException {
        BibtexDatabase bibtexDatabase = new BibtexDatabase();
        HashMap<Object, Object> entries = new HashMap<>();
        bibtexDatabase.addDatabaseChangeListener(new EntrySorter(entries, new AgedNodeComparator()));
        assertFalse(bibtexDatabase.insertEntry(PreviewPrefsTab.getTestEntry()));
        assertEquals(1, bibtexDatabase.getEntryMap().size());
    }
    @Test
    void testInsertEntry5() throws KeyCollisionException {
        BibtexDatabase bibtexDatabase = new BibtexDatabase();
        assertFalse(bibtexDatabase.insertEntry(new BibtexEntry("42")));
        assertEquals(1, bibtexDatabase.getEntryMap().size());
    }
    @Test
    void testInsertEntry7() throws KeyCollisionException {
        BibtexDatabase bibtexDatabase = new BibtexDatabase();
        HashMap<Object, Object> entries = new HashMap<>();
        bibtexDatabase.addDatabaseChangeListener(new GlazedEntrySorter(entries, new AgedNodeComparator()));
        assertFalse(bibtexDatabase.insertEntry(PreviewPrefsTab.getTestEntry()));
        assertEquals(1, bibtexDatabase.getEntryMap().size());
    }
    @Test
    void testRemoveEntry() throws KeyCollisionException {
        BibtexDatabase bibtexDatabase = new BibtexDatabase();
        BibtexEntry bibtexEntry = new BibtexEntry("42");
        bibtexDatabase.insertEntry(bibtexEntry);
        assertSame(bibtexEntry, bibtexDatabase.removeEntry("42"));
        assertTrue(bibtexDatabase.getEntryMap().isEmpty());
    }
    @Test
    void testAddString() throws KeyCollisionException {
        BibtexDatabase bibtexDatabase = new BibtexDatabase();
        bibtexDatabase.addString(new BibtexString("42", "Name", "Not all who wander are lost"));
        assertEquals(1, bibtexDatabase.get_strings().size());
    }
    @Test
    void testAddString2() throws KeyCollisionException {
        BibtexDatabase bibtexDatabase = new BibtexDatabase();
        bibtexDatabase.addString(new BibtexString("42", "Name", "Not all who wander are lost"));
        assertThrows(KeyCollisionException.class, () -> bibtexDatabase.addString(new BibtexString("42", "Name", "Not all who wander are lost")));
    }
    @Test
    void testAddString3() throws KeyCollisionException {
        BibtexDatabase bibtexDatabase = new BibtexDatabase();
        bibtexDatabase.addString(new BibtexString("42", "A string with this label already exists,", "Not all who wander are lost"));
        assertThrows(KeyCollisionException.class, () -> bibtexDatabase.addString(new BibtexString("42", "Name", "Not all who wander are lost")));
    }
    @Test
    void testRemoveString() {
        BibtexDatabase bibtexDatabase = new BibtexDatabase();
        bibtexDatabase.removeString("42");
        assertTrue(bibtexDatabase.get_strings().isEmpty());
    }
    @Test
    void testGetStringKeySet() {
        assertTrue((new BibtexDatabase()).getStringKeySet().isEmpty());
    }
    @Test
    void testGetString() {
        assertNull((new BibtexDatabase()).getString("42"));
    }
    @Test
    void testGetStringCount() {
        assertEquals(0, (new BibtexDatabase()).getStringCount());
    }
    @Test
    void testHasStringLabel() {
        assertFalse((new BibtexDatabase()).hasStringLabel("Label"));
    }
    @Test
    void testHasStringLabel2() throws KeyCollisionException {
        BibtexDatabase bibtexDatabase = new BibtexDatabase();
        bibtexDatabase.addString(new BibtexString("42", "Name", "Not all who wander are lost"));
        assertFalse(bibtexDatabase.hasStringLabel("Label"));
    }
    @Test
    void testHasStringLabel3() throws KeyCollisionException {
        BibtexDatabase bibtexDatabase = new BibtexDatabase();
        bibtexDatabase.addString(new BibtexString("42", "Label", "Not all who wander are lost"));
        assertTrue(bibtexDatabase.hasStringLabel("Label"));
    }
    @Test
    void testResolveForStrings() {
        assertEquals("Not all who wander are lost",(new BibtexDatabase()).resolveForStrings("Not all who wander are lost"));
        assertEquals("xx#UUU#xx", (new BibtexDatabase()).resolveForStrings("xx#UUU#xx"));
        assertEquals(".*#[^#]+#.*", (new BibtexDatabase()).resolveForStrings(".*#[^#]+#.*"));
        assertEquals("Not all who wander are lostxx#UUU#xx",(new BibtexDatabase()).resolveForStrings("Not all who wander are lostxx#UUU#xx"));
        assertEquals("Not all who wander are lost.*#[^#]+#.*",(new BibtexDatabase()).resolveForStrings("Not all who wander are lost.*#[^#]+#.*"));
    }
    @Test
    void testResolveForStrings2() throws KeyCollisionException {
        BibtexDatabase bibtexDatabase = new BibtexDatabase();
        bibtexDatabase.addString(new BibtexString("42", "xx#UUU#xx", "Not all who wander are lost"));
        assertEquals("xx#UUU#xx", bibtexDatabase.resolveForStrings("xx#UUU#xx"));
    }
    @Test
    void testResolveForStrings3() throws KeyCollisionException {
        BibtexDatabase bibtexDatabase = new BibtexDatabase();
        bibtexDatabase.addString(new BibtexString("42", "uuu", "Not all who wander are lost"));
        assertEquals("xxNot all who wander are lostxx", bibtexDatabase.resolveForStrings("xx#UUU#xx"));
    }
    @Test
    void testResolveForStrings5() throws KeyCollisionException {
        BibtexDatabase bibtexDatabase = new BibtexDatabase();
        bibtexDatabase.addString(new BibtexString("42", "xx#UUU#xx", "Not all who wander are lost"));
        assertEquals("xx#uuu#xx", bibtexDatabase.resolveForStrings("xx#uuu#xx"));
    }
    @Test
    void testResolveForStrings7() throws KeyCollisionException {
        BibtexDatabase bibtexDatabase = new BibtexDatabase();
        bibtexDatabase.addString(new BibtexString("42", "uuu", "xx#UUU#xx"));
        assertEquals("xxxx#UUU#xxxx", bibtexDatabase.resolveForStrings("xx#UUU#xx"));
    }
    @Test
    void testResolveForStrings8() throws KeyCollisionException {
        BibtexDatabase bibtexDatabase = new BibtexDatabase();
        bibtexDatabase.addString(new BibtexString("42", "xx#UUU#xx", "Not all who wander are lost"));
        assertEquals("Not all who wander are lostxx#uuu#xx",
                bibtexDatabase.resolveForStrings("Not all who wander are lostxx#uuu#xx"));
    }
    @Test
    void testCheckForDuplicateKeyAndAdd() {
        assertFalse((new BibtexDatabase()).checkForDuplicateKeyAndAdd("Old Key", "New Key", true));
        assertFalse((new BibtexDatabase()).checkForDuplicateKeyAndAdd("New Key", "New Key", true));
        assertFalse((new BibtexDatabase()).checkForDuplicateKeyAndAdd("", "New Key", true));
        assertFalse((new BibtexDatabase()).checkForDuplicateKeyAndAdd(null, "New Key", true));
        assertFalse((new BibtexDatabase()).checkForDuplicateKeyAndAdd("Old Key", "", true));
        assertFalse((new BibtexDatabase()).checkForDuplicateKeyAndAdd("Old Key", null, true));
    }
    @Test
    void testGetNumberOfKeyOccurences() {
        assertEquals(0, (new BibtexDatabase()).getNumberOfKeyOccurences("Key"));
    }
    @SuppressWarnings("rawtypes")
	@Test
    void testFireDatabaseChanged() {
        BibtexDatabase bibtexDatabase = new BibtexDatabase();
        BibtexDatabase source = new BibtexDatabase();
        DatabaseChangeEvent.ChangeType changeType = new DatabaseChangeEvent.ChangeType();
        BibtexEntry testEntry = PreviewPrefsTab.getTestEntry();
        DatabaseChangeEvent databaseChangeEvent = new DatabaseChangeEvent(source, changeType, testEntry);
        bibtexDatabase.fireDatabaseChanged(databaseChangeEvent);
        assertSame(testEntry, databaseChangeEvent.getEntry());
        assertSame(changeType, databaseChangeEvent.getType());
        Vector expectedVector = bibtexDatabase.get_strings_();
        BibtexDatabase source1 = databaseChangeEvent.getSource();
        Vector vector = source1.get_strings_();
        assertEquals(expectedVector, vector);
        assertTrue(bibtexDatabase.getChangeListeners().isEmpty());
        assertEquals(vector, bibtexDatabase.get_strings_());
        assertTrue(bibtexDatabase.get_strings().isEmpty());
        Map expectedEntryMap = source1.getEntryMap();
        assertEquals(expectedEntryMap, bibtexDatabase.getEntryMap());
    }
    @Test
    void testAddDatabaseChangeListener() {
        BibtexDatabase bibtexDatabase = new BibtexDatabase();
        HashMap<Object, Object> entries = new HashMap<>();
        bibtexDatabase.addDatabaseChangeListener(new EntrySorter(entries, new AgedNodeComparator()));
        assertEquals(1, bibtexDatabase.getChangeListeners().size());
    }
    @Test
    void testRemoveDatabaseChangeListener() {
        BibtexDatabase bibtexDatabase = new BibtexDatabase();
        HashMap<Object, Object> entries = new HashMap<>();
        bibtexDatabase.removeDatabaseChangeListener(new EntrySorter(entries, new AgedNodeComparator()));
        assertTrue(bibtexDatabase.getChangeListeners().isEmpty());
    }
    @SuppressWarnings("rawtypes")
	@Test
    void testConstructor() {
        BibtexDatabase actualBibtexDatabase = new BibtexDatabase();
        actualBibtexDatabase.setPreamble("Preamble");
        Map expectedEntryMap = actualBibtexDatabase.get_entries();
        assertSame(expectedEntryMap, actualBibtexDatabase.getEntryMap());
        assertEquals("Preamble", actualBibtexDatabase.getPreamble());
        assertTrue(actualBibtexDatabase.get_strings().isEmpty());
        assertTrue(actualBibtexDatabase.get_strings_().isEmpty());
        assertTrue(actualBibtexDatabase.getChangeListeners().isEmpty());
    }
}
