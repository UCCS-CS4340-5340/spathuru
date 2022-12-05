package net.sf.jabref;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

public class BibtexStringTest {
    @Test
    public void testConstructor() {
        BibtexString actualBibtexString = new BibtexString("42", "Name", "Not all who wander are lost");
        actualBibtexString.setContent("Not all who wander are lost");
        actualBibtexString.setId("42");
        actualBibtexString.setName("Name");
        assertEquals("42", actualBibtexString.getId());
        assertEquals("Name", actualBibtexString.getName());
    }
    @Test
    public void testGetContent() {
        assertEquals("Not all who wander are lost",(new BibtexString("42", "Name", "Not all who wander are lost")).getContent());
        assertEquals("", (new BibtexString("42", "Name", null)).getContent());
    }
    @Test
    public void testClone() {
        assertEquals("Not all who wander are lost",((BibtexString) (new BibtexString("42", "Name", "Not all who wander are lost")).clone()).getContent());
        assertEquals("Name",((BibtexString) (new BibtexString("42", "Name", "Not all who wander are lost")).clone()).getName());
        assertEquals("42",((BibtexString) (new BibtexString("42", "Name", "Not all who wander are lost")).clone()).getId());
    }
}