package net.sf.jabref;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.GridBagConstraints;

import org.junit.Test;

import net.sf.jabref.CheckBoxMessage;

class CheckBoxMessageTest {
    @Test
    public void testConstructor() {
        CheckBoxMessage actualCheckBoxMessage = new CheckBoxMessage("Not all who wander are lost", "Cb Text", true);
        assertNull(actualCheckBoxMessage.getGbl().columnWidths);
        GridBagConstraints gridBagConstraints = actualCheckBoxMessage.getCon();
        assertEquals(0, gridBagConstraints.ipady);
        assertEquals(17, gridBagConstraints.anchor);
        assertEquals(0, gridBagConstraints.gridwidth);
        assertEquals(10, gridBagConstraints.insets.top);
    }
    @Test
    public void testIsSelected() {
        assertTrue((new CheckBoxMessage("Not all who wander are lost", "Cb Text", true)).isSelected());
        assertFalse((new CheckBoxMessage("Not all who wander are lost", "Cb Text", false)).isSelected());
    }
}