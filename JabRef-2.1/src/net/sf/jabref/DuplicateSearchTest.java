package net.sf.jabref;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import net.sf.jabref.DuplicateSearch;

class DuplicateSearchTest {
    @Test
    void testSearcherThreadFinished() {
    	assertFalse(((new DuplicateSearch(null)).new SearcherThread()).finished());
    }
}