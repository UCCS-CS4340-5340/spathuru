package net.sf.jabref;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class BibtexStringComparatorTest {
    @Test
    void testConstructor() {
        assertTrue((new BibtexStringComparator(true)).isConsiderRefs());
    }
    @Test
    void testCompare() {
        BibtexStringComparator bibtexStringComparator = new BibtexStringComparator(true);
        BibtexString one = new BibtexString("42", "Name", "Not all who wander are lost");
        assertEquals(0, bibtexStringComparator.compare(one, new BibtexString("42", "Name", "Not all who wander are lost")));
    }
    @Test
    void testCompare4() {
        BibtexStringComparator bibtexStringComparator = new BibtexStringComparator(true);
        BibtexString one = new BibtexString("42", "42", "Not all who wander are lost");
        assertEquals(-58, bibtexStringComparator.compare(one, new BibtexString("42", "Name", "Not all who wander are lost")));
    }
    @Test
    void testCompare5() {
        BibtexStringComparator bibtexStringComparator = new BibtexStringComparator(false);
        BibtexString one = new BibtexString("42", "Name", "Not all who wander are lost");
        assertEquals(0, bibtexStringComparator.compare(one, new BibtexString("42", "Name", "Not all who wander are lost")));
    }
    @Test
    void testCompare6() {
        BibtexStringComparator bibtexStringComparator = new BibtexStringComparator(true);
        BibtexString one = new BibtexString("42", "Name", "#UUU#");
        assertEquals(1, bibtexStringComparator.compare(one, new BibtexString("42", "Name", "Not all who wander are lost")));
    }
    @Test
    void testCompare7() {
        BibtexStringComparator bibtexStringComparator = new BibtexStringComparator(true);
        BibtexString one = new BibtexString("42", "Name", "Not all who wander are lost");
        assertEquals(75, bibtexStringComparator.compare(one, new BibtexString("42", "#UUU#", "Not all who wander are lost")));
    }
    @Test
    void testCompare8() {
        BibtexStringComparator bibtexStringComparator = new BibtexStringComparator(false);
        BibtexString one = new BibtexString("42", "42", "Not all who wander are lost");
        assertEquals(-58, bibtexStringComparator.compare(one, new BibtexString("42", "Name", "Not all who wander are lost")));
    }
}