package net.sf.jabref;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class DuplicateSearchTest {
    @Test
    void testSearcherThreadFinished() {
        assertFalse(((new DuplicateSearch(null)).new SearcherThread()).finished());
    }
}