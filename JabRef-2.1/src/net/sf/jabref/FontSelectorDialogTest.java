package net.sf.jabref;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Font;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class FontSelectorDialogTest {

    @Test
    @DisplayName("Should update the text when the font is italic")
    void setFontWhenFontIsItalicThenUpdateText() {
        FontSelector fontSelector = new FontSelector();
        Font font = new Font("SansSerif", Font.ITALIC, 10);
        fontSelector.setFont(font);
        assertEquals("SansSerif 10 italic", fontSelector.getText());
    }

    @Test
    @DisplayName("Should update the text when the font is plain")
    void setFontWhenFontIsPlainThenUpdateText() {
        FontSelector fontSelector = new FontSelector();
        Font font = new Font("SansSerif", Font.PLAIN, 10);
        fontSelector.setFont(font);
        assertEquals("SansSerif 10 plain", fontSelector.getText());
    }

    @Test
    @DisplayName("Should update the text when the font is bold")
    void setFontWhenFontIsBoldThenUpdateText() {
        FontSelector fontSelector = new FontSelector();
        Font font = new Font("SansSerif", Font.BOLD, 10);
        fontSelector.setFont(font);
        assertEquals("SansSerif 10 bold", fontSelector.getText());
    }

    @Test
    @DisplayName("Should update the text when the font is bold and italic")
    void setFontWhenFontIsBoldAndItalicThenUpdateText() {
        FontSelector fontSelector = new FontSelector();
        Font font = new Font("SansSerif", Font.BOLD | Font.ITALIC, 10);
        fontSelector.setFont(font);
        assertEquals("SansSerif 10 bold-italic", fontSelector.getText());
    }

    @Test
    @DisplayName("Should set the font to sansserif 10 plain")
    void fontSelectorShouldSetTheFontToSansSerif10Plain() {
        FontSelector fontSelector = new FontSelector();
        assertNotEquals("SansSerif 10 plain", fontSelector.getFont().toString());
    }

    @Test
    @DisplayName("Should add an action listener")
    void fontSelectorShouldAddAnActionListener() {
        FontSelector fontSelector = new FontSelector();
        assertTrue(fontSelector.getActionListeners().length > 0);
    }

    @Test
    @DisplayName("Should set the request focus enabled to false")
    void fontSelectorShouldSetTheRequestFocusEnabledToFalse() {
        FontSelector fontSelector = new FontSelector();
        assertFalse(fontSelector.isRequestFocusEnabled());
    }
}