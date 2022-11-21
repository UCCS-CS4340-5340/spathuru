package net.test.jabref;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.swing.JTextField;


import org.junit.Test;

import net.sf.jabref.BrowseAction;

class BrowseActionTest {
    @Test
    void testConstructor() {
        BrowseAction actualBrowseAction = new BrowseAction(null, new JTextField(), true);
        assertNull(actualBrowseAction.getFrame());
        assertTrue(actualBrowseAction.isDir());
    }
}