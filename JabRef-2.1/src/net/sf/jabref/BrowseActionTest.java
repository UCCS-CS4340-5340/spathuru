package net.sf.jabref;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.swing.JTextField;


import org.junit.jupiter.api.Test;

class BrowseActionTest {
    @Test
    void testConstructor() {
        BrowseAction actualBrowseAction = new BrowseAction(null, new JTextField(), true);
        assertNull(actualBrowseAction.getFrame());
        assertTrue(actualBrowseAction.isDir());
    }
}