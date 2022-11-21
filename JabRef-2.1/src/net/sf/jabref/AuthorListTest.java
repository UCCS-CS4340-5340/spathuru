package net.sf.jabref;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.sf.jabref.AuthorList;
import net.sf.jabref.DuplicateResolverDialog;
import org.junit.jupiter.api.Test;

class AuthorListTest {
    @Test
    void testFixAuthor_Natbib() {
        assertEquals("Orig", AuthorList.fixAuthor_Natbib("In Orig"));
        assertEquals("foo", AuthorList.fixAuthor_Natbib("foo"));
        assertEquals("", AuthorList.fixAuthor_Natbib("and"));
        assertEquals("", AuthorList.fixAuthor_Natbib(","));
        assertEquals("42", AuthorList.fixAuthor_Natbib("42"));
        assertEquals("andIn Orig", AuthorList.fixAuthor_Natbib("andIn Orig"));
        assertEquals("", AuthorList.fixAuthor_Natbib("and,"));
        assertEquals("Orig", AuthorList.fixAuthor_Natbib("In OrigIn Orig"));
        assertEquals("In Orig", AuthorList.fixAuthor_Natbib("In Orig,"));
        assertEquals("", AuthorList.fixAuthor_Natbib(",In Orig"));
        assertEquals("", AuthorList.fixAuthor_Natbib(",,"));
        assertEquals("j", AuthorList.fixAuthor_Natbib("j,"));
        assertEquals("andIn OrigIn Orig", AuthorList.fixAuthor_Natbib("andIn OrigIn Orig"));
        assertEquals("andIn Orig", AuthorList.fixAuthor_Natbib("andIn Orig,"));
        assertEquals("In Orig", AuthorList.fixAuthor_Natbib("In Orig,,"));
        assertEquals("", AuthorList.fixAuthor_Natbib(",In Orig,"));
        assertEquals("", AuthorList.fixAuthor_Natbib(",,In Orig"));
        assertEquals("", AuthorList.fixAuthor_Natbib(",,,"));
    }
    @Test
    void testFixAuthor_firstNameFirstCommas() {
        assertEquals("I. Orig", AuthorList.fixAuthor_firstNameFirstCommas("In Orig", true));
        assertEquals("", AuthorList.fixAuthor_firstNameFirstCommas(" ", true));
        assertEquals("42", AuthorList.fixAuthor_firstNameFirstCommas("42", true));
        assertEquals("j", AuthorList.fixAuthor_firstNameFirstCommas("j", true));
        assertEquals("I. Orig", AuthorList.fixAuthor_firstNameFirstCommas("In Orig", false));
    }
    @Test
    void testFixAuthor_firstNameFirst() {
        assertEquals("In Orig", AuthorList.fixAuthor_firstNameFirst("In Orig"));
        assertEquals("foo", AuthorList.fixAuthor_firstNameFirst("foo"));
        assertEquals("", AuthorList.fixAuthor_firstNameFirst("and"));
        assertEquals("", AuthorList.fixAuthor_firstNameFirst(" "));
        assertEquals("andand", AuthorList.fixAuthor_firstNameFirst("andand"));
        assertEquals("In OrigIn Orig", AuthorList.fixAuthor_firstNameFirst("In OrigIn Orig"));
        assertEquals("", AuthorList.fixAuthor_firstNameFirst("and "));
        assertEquals("In Orig", AuthorList.fixAuthor_firstNameFirst("In Orig,"));
        assertEquals("andIn Orig", AuthorList.fixAuthor_firstNameFirst("andIn Orig"));
        assertEquals("In Orig ", AuthorList.fixAuthor_firstNameFirst(",In Orig"));
        assertEquals("42 ", AuthorList.fixAuthor_firstNameFirst(",42"));
        assertEquals("42", AuthorList.fixAuthor_firstNameFirst(" 42"));
    }
    @Test
    void testFixAuthor_lastNameFirstCommas() {
        assertEquals("Orig, I.", AuthorList.fixAuthor_lastNameFirstCommas("In Orig", true));
        assertEquals("", AuthorList.fixAuthor_lastNameFirstCommas(", ", true));
        assertEquals("42", AuthorList.fixAuthor_lastNameFirstCommas("42", true));
        assertEquals("j", AuthorList.fixAuthor_lastNameFirstCommas("j", true));
        assertEquals("Orig, I.", AuthorList.fixAuthor_lastNameFirstCommas("In Orig", false));
    }
    @Test
    void testFixAuthor_lastNameFirst() {
        assertEquals("Orig, In", AuthorList.fixAuthor_lastNameFirst("In Orig"));
        assertEquals("", AuthorList.fixAuthor_lastNameFirst(", "));
        assertEquals("42", AuthorList.fixAuthor_lastNameFirst("42"));
        assertEquals("j", AuthorList.fixAuthor_lastNameFirst("j"));
        assertEquals("", AuthorList.fixAuthor_lastNameFirst(", , "));
        assertEquals(", In Orig", AuthorList.fixAuthor_lastNameFirst(", In Orig"));
        assertEquals("", AuthorList.fixAuthor_lastNameFirst("and, "));
        assertEquals("andIn Orig", AuthorList.fixAuthor_lastNameFirst("andIn Orig"));
        assertEquals("Orig, In", AuthorList.fixAuthor_lastNameFirst(" In Orig"));
        assertEquals(", 42", AuthorList.fixAuthor_lastNameFirst(", 42"));
        assertEquals("and42", AuthorList.fixAuthor_lastNameFirst("and42"));
        assertEquals("In Orig", AuthorList.fixAuthor_lastNameFirst("In Orig, "));
        assertEquals("42", AuthorList.fixAuthor_lastNameFirst("42 "));
        assertEquals("jIn Orig", AuthorList.fixAuthor_lastNameFirst("jIn Orig"));
        assertEquals("java.lang.String", AuthorList.fixAuthor_lastNameFirst("java.lang.String,"));
        assertEquals("andIn OrigIn Orig", AuthorList.fixAuthor_lastNameFirst("andIn OrigIn Orig"));
        assertEquals("andIn Orig", AuthorList.fixAuthor_lastNameFirst("andIn Orig,"));
    }
    @Test
    void testFixAuthor_lastNameOnlyCommas() {
        assertEquals("Orig", AuthorList.fixAuthor_lastNameOnlyCommas("In Orig"));
        assertEquals("", AuthorList.fixAuthor_lastNameOnlyCommas(","));
        assertEquals("j", AuthorList.fixAuthor_lastNameOnlyCommas("j"));
        assertEquals("Orig", AuthorList.fixAuthor_lastNameOnlyCommas("In OrigIn Orig"));
        assertEquals("andand", AuthorList.fixAuthor_lastNameOnlyCommas("andand"));
        assertEquals("In Orig", AuthorList.fixAuthor_lastNameOnlyCommas("In Orig,"));
        assertEquals("andIn Orig", AuthorList.fixAuthor_lastNameOnlyCommas("andIn Orig"));
        assertEquals("", AuthorList.fixAuthor_lastNameOnlyCommas("and,"));
        assertEquals("42", AuthorList.fixAuthor_lastNameOnlyCommas(" 42"));
        assertEquals("", AuthorList.fixAuthor_lastNameOnlyCommas(",In Orig"));
    }
    @Test
    void testFixAuthorForAlphabetization() {
        assertEquals("Orig, I.", AuthorList.fixAuthorForAlphabetization("In Orig"));
        assertEquals("", AuthorList.fixAuthorForAlphabetization("foo"));
        assertEquals("", AuthorList.fixAuthorForAlphabetization("and"));
        assertEquals("", AuthorList.fixAuthorForAlphabetization(", "));
        assertEquals("42", AuthorList.fixAuthorForAlphabetization("42"));
        assertEquals("", AuthorList.fixAuthorForAlphabetization("and, "));
        assertEquals("Orig", AuthorList.fixAuthorForAlphabetization("andIn Orig"));
        assertEquals("Orig, I. O.", AuthorList.fixAuthorForAlphabetization("In OrigIn Orig"));
        assertEquals("", AuthorList.fixAuthorForAlphabetization(", , "));
        assertEquals("In Orig", AuthorList.fixAuthorForAlphabetization("In Orig,"));
        assertEquals(", I. O.", AuthorList.fixAuthorForAlphabetization(", In Orig"));
        assertEquals("", AuthorList.fixAuthorForAlphabetization("j,"));
        assertEquals("OrigIn Orig", AuthorList.fixAuthorForAlphabetization("andIn OrigIn Orig"));
        assertEquals("Orig", AuthorList.fixAuthorForAlphabetization("andIn Orig,"));
        assertEquals("In Orig", AuthorList.fixAuthorForAlphabetization("In Orig,,"));
        assertEquals("", AuthorList.fixAuthorForAlphabetization(", , , "));
        assertEquals(", I. O.", AuthorList.fixAuthorForAlphabetization(", , In Orig"));
        assertEquals(", In Orig", AuthorList.fixAuthorForAlphabetization(", In Orig, "));
    }
    @Test
    void testSize() {
        assertEquals(1, AuthorList.getAuthorList("In Orig").size());
    }
    @Test
    void testGetAuthorsNatbib() {
        assertEquals("Orig", AuthorList.getAuthorList("In Orig").getAuthorsNatbib());
        assertEquals("", AuthorList.getAuthorList(",").getAuthorsNatbib());
        assertEquals("42", AuthorList.getAuthorList("42").getAuthorsNatbib());
        assertEquals("j", AuthorList.getAuthorList("j").getAuthorsNatbib());
        assertEquals("", AuthorList.getAuthorList(",In Orig").getAuthorsNatbib());
    }
    @Test
    void testGetAuthorsLastOnly() {
        assertEquals("Orig", AuthorList.getAuthorList("In Orig").getAuthorsLastOnly());
        assertEquals("JaneDoe", (new AuthorList("JaneDoe")).getAuthorsLastOnly());
        assertEquals("authors", (new AuthorList("Bibtex authors")).getAuthorsLastOnly());
        assertEquals("", (new AuthorList(",")).getAuthorsLastOnly());
        assertEquals("", AuthorList.getAuthorList(",42").getAuthorsLastOnly());
        assertEquals("andIn Orig", AuthorList.getAuthorList("andIn Orig").getAuthorsLastOnly());
    }
    @Test
    void testGetAuthorsLastFirst() {
        assertEquals("Orig, I.", AuthorList.getAuthorList("In Orig").getAuthorsLastFirst(true));
        assertEquals("", AuthorList.getAuthorList(", ").getAuthorsLastFirst(true));
        assertEquals("JaneDoe", (new AuthorList("JaneDoe")).getAuthorsLastFirst(true));
        assertEquals("authors, B.", (new AuthorList("Bibtex authors")).getAuthorsLastFirst(true));
        assertEquals("", (new AuthorList(",")).getAuthorsLastFirst(true));
        assertEquals("JaneDoe", (new AuthorList("JaneDoe")).getAuthorsLastFirst(false));
        assertEquals("authors, Bibtex", (new AuthorList("Bibtex authors")).getAuthorsLastFirst(false));
        assertEquals(", I. O.", AuthorList.getAuthorList(", In Orig").getAuthorsLastFirst(true));
    }
    @Test
    void testGetAuthorsLastFirstAnds() {
        assertEquals("Orig, In", AuthorList.getAuthorList("In Orig").getAuthorsLastFirstAnds());
        assertEquals("JaneDoe", (new AuthorList("JaneDoe")).getAuthorsLastFirstAnds());
        assertEquals("authors, Bibtex", (new AuthorList("Bibtex authors")).getAuthorsLastFirstAnds());
        assertEquals("", (new AuthorList(",")).getAuthorsLastFirstAnds());
        assertEquals(", In Orig", AuthorList.getAuthorList(",In Orig").getAuthorsLastFirstAnds());
        assertEquals("andIn Orig", AuthorList.getAuthorList("andIn Orig").getAuthorsLastFirstAnds());
    }
    @Test
    void testGetAuthorsFirstFirst() {
        assertEquals("I. Orig", AuthorList.getAuthorList("In Orig").getAuthorsFirstFirst(true));
        assertEquals("", AuthorList.getAuthorList(" ").getAuthorsFirstFirst(true));
        assertEquals("JaneDoe", (new AuthorList("JaneDoe")).getAuthorsFirstFirst(true));
        assertEquals("B. authors", (new AuthorList("Bibtex authors")).getAuthorsFirstFirst(true));
        assertEquals("", (new AuthorList("")).getAuthorsFirstFirst(true));
        assertEquals("JaneDoe", (new AuthorList("JaneDoe")).getAuthorsFirstFirst(false));
        assertEquals("Bibtex authors", (new AuthorList("Bibtex authors")).getAuthorsFirstFirst(false));
        assertEquals("J. ", (new AuthorList(",JaneDoe")).getAuthorsFirstFirst(true));
        assertEquals("andIn Orig", AuthorList.getAuthorList("andIn Orig").getAuthorsFirstFirst(false));
    }
    @Test
    void testGetAuthorsFirstFirstAnds() {
        assertEquals("In Orig", AuthorList.getAuthorList("In Orig").getAuthorsFirstFirstAnds());
        assertEquals("JaneDoe", (new AuthorList("JaneDoe")).getAuthorsFirstFirstAnds());
        assertEquals("Bibtex authors", (new AuthorList("Bibtex authors")).getAuthorsFirstFirstAnds());
        assertEquals("", (new AuthorList("")).getAuthorsFirstFirstAnds());
        assertEquals("In Orig ", AuthorList.getAuthorList(",In Orig").getAuthorsFirstFirstAnds());
    }
    @Test
    void testGetAuthorsForAlphabetization() {
        assertEquals("Orig, I.", AuthorList.getAuthorList("In Orig").getAuthorsForAlphabetization());
        assertEquals("JaneDoe", (new AuthorList("JaneDoe")).getAuthorsForAlphabetization());
        assertEquals(", B.", (new AuthorList("Bibtex authors")).getAuthorsForAlphabetization());
        assertEquals("", (new AuthorList(",")).getAuthorsForAlphabetization());
        assertEquals("", (new AuthorList("j")).getAuthorsForAlphabetization());
    }
}