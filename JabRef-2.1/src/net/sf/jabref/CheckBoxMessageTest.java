package net.sf.jabref;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.GridBagConstraints;

import org.junit.jupiter.api.Test;

class CheckBoxMessageTest {
    @Test
    void testConstructor() {
        CheckBoxMessage actualCheckBoxMessage = new CheckBoxMessage("Not all who wander are lost", "Cb Text", true);
        assertNull(actualCheckBoxMessage.getGbl().columnWidths);
        GridBagConstraints gridBagConstraints = actualCheckBoxMessage.getCon();
        assertEquals(0, gridBagConstraints.ipady);
        assertEquals(17, gridBagConstraints.anchor);
        assertEquals(0, gridBagConstraints.gridwidth);
        assertEquals(10, gridBagConstraints.insets.top);
    }
    @Test
    void testIsSelected() {
        assertTrue((new CheckBoxMessage("Not all who wander are lost", "Cb Text", true)).isSelected());
        assertFalse((new CheckBoxMessage("Not all who wander are lost", "Cb Text", false)).isSelected());
    }
}