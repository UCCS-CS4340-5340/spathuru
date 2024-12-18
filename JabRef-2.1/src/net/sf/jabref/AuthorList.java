package net.sf.jabref;

import java.util.Vector;
import java.util.WeakHashMap;

/**
 * This is an immutable class representing information of either
 * <CODE>author</CODE> or <CODE>editor</CODE> field in bibtex record.
 * <p>
 * Constructor performs parsing of raw field text and stores preformatted data.
 * Various accessor methods return author/editor field in different formats.
 * <p>
 * Parsing algorithm is designed to satisfy two requirements:
 * (a) when author's name is typed correctly, the result should
 *     coincide with the one of BiBTeX;
 * (b) for erroneous names, output should be reasonable (but may differ
 *     from BiBTeX output).
 * The following rules are used:
 * <ol>
 * <li> 'author field' is a sequence of tokens;
 *    <ul>
 *    <li> tokens are separated by sequences of whitespaces
 *         (<CODE>Character.isWhitespace(c)==true</CODE>), commas (,),
 *         dashes (-), and tildas (~);
 *    <li> every comma separates tokens, while sequences of other separators
 *         are equivalent to a single separator; for example:
 *         "a - b" consists of 2 tokens ("a" and "b"), while
 *         "a,-,b" consists of 3 tokens ("a", "", and "b")
 *    <li> anything enclosed in braces belonges to a single token; for example:
 *         "abc x{a,b,-~ c}x" consists of 2 tokens, while
 *         "abc xa,b,-~ cx" consists of 4 tokens ("abc", "xa","b", and "cx");
 *    <li> a token followed immediately by a dash is "dash-terminated" token,
 *         and all other tokens are "space-terminated" tokens; for example:
 *         in "a-b- c - d" tokens "a" and "b" are dash-terminated and
 *         "c" and "d" are space-terminated;
 *    <li> for the purposes of splitting of 'author name' into parts and
 *         construction of abbreviation of first name,
 *         one needs definitions of first latter of a token, case of a token,
 *         and abbreviation of a token:
 *         <ul>
 *         <li> 'first letter' of a token is the first letter character
 *              (<CODE>Character.isLetter(c)==true</CODE>) that does not
 *              belong to a sequence of letters that immediately follows
 *              "\" character, with one exception:
 *              if "\" is followed by "aa", "AA", "ae", "AE", "l", "L", "o", "O",
 *              "oe", "OE", "i", or "j" followed by non-letter, the 'first letter'
 *              of a token is a letter that follows "\"; for example:
 *              in "a{x}b" 'first letter' is "a",
 *              in "{\"{U}}bel" 'first letter' is "U",
 *              in "{\noopsort{\"o}}xyz" 'first letter' is "o",
 *              in "{\AE}x" 'first letter' is "A",
 *              in "\aex\ijk\Oe\j" 'first letter' is "j";
 *              if there is no letter satisfying the above rule, 'first letter'
 *              is undefined;
 *         <li> token is "lower-case" token, if its first letter id defined and
 *              is lower-case (<CODE>Character.isLowerCase(c)==true</CODE>),
 *              and token is "upper-case" token otherwise;
 *         <li> 'abbreviation' of a token is the shortest prefix of the token
 *              that (a) contains 'first letter' and (b) is braces-balanced;
 *              if 'first letter' is undefined, 'abbreviation' is the token
 *              itself; in the above examples, 'abbreviation's are
 *              "a", "{\"{U}}", "{\noopsort{\"o}}", "{\AE}", "\aex\ijk\Oe\j";
 *         </ul>
 *    <li> the behavior based on the above definitions will be erroneous only
 *         in one case: if the first-name-token is "{\noopsort{A}}john",
 *         we abbreviate it as "{\noopsort{A}}.", while BiBTeX produces "j.";
 *         fixing this problem, however, requires processing of the preabmle;
 *    </ul>
 * <li> 'author name's in 'author field' are subsequences of tokens separated
 *      by token "and" ("and" is case-insensitive); if 'author name' is an
 *      empty sequence of tokens, it is ignored; for examle, both
 *      "John Smith and Peter Black" and "and and John Smith and and Peter Black"
 *      consists of 2 'author name's "Johm Smith" and "Peter Black"
 *      (in erroneous situations, this is a bit different from BiBTeX behavior);
 * <li> 'author name' consists of 'first-part', 'von-part', 'last-part', and
 *      'junior-part', each of which is a sequence of tokens;
 *      how a sequence of tokens has to be splitted into these parts, depends
 *      the number of commas:
 *     <ul>
 *     <li> no commas, all tokens are upper-case:
 *          'junior-part' and 'von-part' are empty, 'last-part' consist
 *          of the last token, 'first-part' consists of all other tokens
 *          ('first-part' is empty, if 'author name' consists of a single token);
 *          for example, in "John James Smith", 'last-part'="Smith" and
 *          'first-part'="John James";
 *     <li> no commas, there exists lower-case token:
 *          'junior-part' is empty, 'first-part' consists of all upper-case
 *          tokens before the first lower-case token, 'von-part' consists of
 *          lower-case tokens starting the first lower-case token and ending
 *          the lower-case token that is followed by upper-case token,
 *          'last-part' consists of the rest of tokens;
 *          note that both 'first-part' and 'latst-part' may be empty and
 *          'last-part' may contain lower-case tokens; for example:
 *          in "von der", 'first-part'='last-part'="", 'von-part'="von der";
 *          in "Charles Louis Xavier Joseph de la Vall{\'e}e la Poussin",
 *          'first-part'="Charles Louis Xavier Joseph", 'von-part'="de la",
 *          'last-part'="Vall{\'e}e la Poussin";
 *     <li> one comma:
 *          'junior-part' is empty, 'first-part' consists of all tokens after comma,
 *          'von-part' consists of the longest sequence of lower-case tokens
 *          in the very beginning, 'last-part' consists of all tokens after
 *          'von-part' and before comma; note that any part can be empty;
 *          for example: in "de la Vall{\'e}e la Poussin, Charles Louis Xavier Joseph",
 *          'first-part'="Charles Louis Xavier Joseph", 'von-part'="de la",
 *          'last-part'="Vall{\'e}e la Poussin";
 *          in "Joseph de la Vall{\'e}e la Poussin, Charles Louis Xavier",
 *          'first-part'="Charles Louis Xavier", 'von-part'="",
 *          'last-part'="Joseph de la Vall{\'e}e la Poussin";
 *     <li> two or more commas (any comma after the second one is ignored;
 *          it merely separates tokens):
 *          'junior-part' consists of all tokens between first and second commas,
 *          'first-part' consists of all tokens after the second comma,
 *          tokens before the first comma are splitted into 'von-part' and
 *          'last-part' similarly to the case of one comma; for example:
 *          in "de la Vall{\'e}e Poussin, Jr., Charles Louis Xavier Joseph",
 *          'first-part'="Charles Louis Xavier Joseph", 'von-part'="de la",
 *          'last-part'="Vall{\'e}e la Poussin", and 'junior-part'="Jr.";
 *     </ul>
 * <li> when 'first-part', 'last-part', 'von-part', or 'junior-part' is reconstructed
 *      from tokens, tokens in a part are separated either by space or by dash,
 *      depending on whether the token before the separator was space-terminated
 *      or dash-terminated; for the last token in a part it does not matter
 *      whether it was dash- or space-terminated;
 * <li> when 'first-part' is abbreviated, each token is replaced by its abbreviation
 *      followed by a period; separators are the same as in the case of non-abbreviated
 *      name; for example: in "Heinrich-{\"{U}}bel Kurt von Minich",
 *      'first-part'="Heinrich-{\"{U}}bel Kurt", and its abbreviation is
 *      "H.-{\"{U}}. K."
 * </ol>
 */
public class AuthorList {

    private Vector authors;     // of Author

    // Variables for storing computed strings, so they only need be created once:
    private String authorsNatbib=null, authorsLastOnly=null, authorsLastFirst=null, authorLastFirstAnds=null,
        authorsFirstFirst=null, authorsFirstFirstAnds=null, authorsAlph=null;


    // The following variables are used only during parsing

    private String orig;        // the raw bibtex author/editor field
    // the following variables are updated by getToken procedure
    private int token_start;    // index in orig
    private int token_end;      // to point 'abc' in '  abc xyz', start=2 and end=5
    // the following variables are valid only if getToken returns TOKEN_WORD
    private int token_abbr;     // end of token abbreviation (always: token_start < token_abbr <= token_end)
    private char token_term;    // either space or dash
    private boolean token_case; // true if upper-case token, false if lower-case token

    // Tokens of one author name.
    // Each token occupies TGL consecutive entries in this vector (as described below)
    private Vector tokens;
    private static final int TOKEN_GROUP_LENGTH = 4;   // number of entries for a token
    // the following are offsets of an entry in a group of entries for one token
    private static final int OFFSET_TOKEN = 0;         // String -- token itself;
    private static final int OFFSET_TOKEN_ABBR = 1;    // String -- token abbreviation;
    private static final int OFFSET_TOKEN_TERM = 2;    // Character -- token terminator (either " " or "-")
    //private static final int OFFSET_TOKEN_CASE = 3;    // Boolean -- true=uppercase, false=lowercase
    // the following are indices in 'tokens' vector created during parsing of author name
    // and later used to properly split author name into parts
    private int von_start;   // token after second comma (-1 if no commas or only one comma)

	private int last_start;

	private int comma_first;

	private int comma_second;

    // Token types (returned by getToken procedure)
    private static final int TOKEN_EOF = 0;
    private static final int TOKEN_AND = 1;
    private static final int TOKEN_COMMA = 2;
    private static final int TOKEN_WORD = 3;

    // Constant Hashtable containing names of TeX special characters
    private static final java.util.Hashtable tex_names = new java.util.Hashtable();
    // and static constructor to initialize it
    static {
        tex_names.put("aa","aa");   // only keys are important in this table
        tex_names.put("ae","ae");
        tex_names.put("l","l");
        tex_names.put("o","o");
        tex_names.put("oe","oe");
        tex_names.put("i","i");
        tex_names.put("AA","AA");
        tex_names.put("AE","AE");
        tex_names.put("L","L");
        tex_names.put("O","O");
        tex_names.put("OE","OE");
        tex_names.put("j","j");
    }

    static WeakHashMap authorCache = new WeakHashMap();

    /**
     * Parses the parameter strings and stores preformatted author information.
     * @param bibtex_authors contents of either <CODE>author</CODE> or
     * <CODE>editor</CODE> bibtex field.
     */
    public AuthorList (String bibtex_authors) {
        authors = new Vector(5);        // 5 seems to be reasonable initial size
        orig = bibtex_authors;              // initialization
        token_start = 0; token_end = 0;     // of parser
        while (token_start < orig.length()) {
            Author author = getAuthor();
            if (author != null) authors.add(author);
        }
        // clean-up
        orig = null; tokens = null;
    }

       public static String fixAuthor_Natbib(final String inOrig) {
        AuthorList authors = new AuthorList(inOrig);
        return authors.getAuthorsNatbib();
    }

    public static AuthorList getAuthorList(String inOrig) {
        Object o = authorCache.get(inOrig);
        if (o == null) {
            AuthorList authors = new AuthorList(inOrig);
            authorCache.put(inOrig, authors);
            return authors;
        } else
            return (AuthorList)o;
    }

    public static String fixAuthor_firstNameFirstCommas(final String inOrig, final boolean abbr) {
        AuthorList authors = getAuthorList(inOrig);
        return authors.getAuthorsFirstFirst(abbr);
    }

    public static String fixAuthor_firstNameFirst(final String inOrig) {
        AuthorList authors = getAuthorList(inOrig);
        return authors.getAuthorsFirstFirstAnds();
    }

    public static String fixAuthor_lastNameFirstCommas(final String inOrig, final boolean abbr) {
        AuthorList authors = getAuthorList(inOrig);
        return authors.getAuthorsLastFirst(abbr);
    }

    public static String fixAuthor_lastNameFirst(final String inOrig) {
        AuthorList authors = getAuthorList(inOrig);
        return authors.getAuthorsLastFirstAnds();
    }

    public static String fixAuthor_lastNameOnlyCommas(final String inOrig) {
        AuthorList authors = getAuthorList(inOrig);
        return authors.getAuthorsLastOnly();
    }

    public static String fixAuthorForAlphabetization(final String inOrig) {
        AuthorList authors = getAuthorList(inOrig);
      return authors.getAuthorsForAlphabetization();

    }


    /**
     * Parses one author name and returns preformatted information.
     * @return Preformatted author name; <CODE>null</CODE> if author name is empty.
     */
    private Author getAuthor() {

        tokens = new Vector();      // initialization
        setVon_start(-1);  setLast_start(-1);  setComma_first(-1);  setComma_second(-1);

        // First step: collect tokens in 'tokens' Vector and calculate indices
    token_loop:
        while (true) {
            int token = getToken();
    cases:    switch (token) {
                case TOKEN_EOF : case TOKEN_AND : break token_loop;
                case TOKEN_COMMA :
                    if (getComma_first() < 0) setComma_first(tokens.size());
                    else if (getComma_second() < 0) setComma_second(tokens.size());
                    break cases;
                case TOKEN_WORD :
                    tokens.add(orig.substring(token_start, token_end));
                    tokens.add(orig.substring(token_start, token_abbr));
                    tokens.add(new Character(token_term));
                    tokens.add(Boolean.valueOf(token_case));
                    if (getComma_first() >= 0) break cases;
                    if (getLast_start() >= 0) break cases;
                    if (getVon_start() < 0) {
                        if (!token_case) {
                            setVon_start(tokens.size()-TOKEN_GROUP_LENGTH); break cases;
                        }
                    } else if (getLast_start() < 0 && token_case) {
                        setLast_start(tokens.size()-TOKEN_GROUP_LENGTH); break cases;
                    }
    }
        }// end token_loop

        // Second step: split name into parts (here: calculate indices
        // of parts in 'tokens' Vector)
        if (tokens.size()==0) return null;  // no author information

        // the following negatives indicate absence of the corresponding part
        int first_part_start=-1, von_part_start=-1, last_part_start=-1, jr_part_start=-1;
        int first_part_end=0, von_part_end=0, last_part_end=0, jr_part_end=0;
        if (getComma_first()<0) {            // no commas
            if (getVon_start()<0) {              // no 'von part'
                last_part_end = tokens.size();
                last_part_start = tokens.size() - TOKEN_GROUP_LENGTH;
                first_part_end = last_part_start;
                if (first_part_end>0) first_part_start = 0;
            } else {                        // 'von part' is present
                if (getLast_start()>=0) {
                    last_part_end = tokens.size();
                    last_part_start = getLast_start();
                    von_part_end = last_part_start;
                } else {
                    von_part_end = tokens.size();
                }
                von_part_start = getVon_start();
                first_part_end = von_part_start;
                if (first_part_end>0) first_part_start = 0;
            }
        } else {    // commas are present: it affects only 'first part' and 'junior part'
            first_part_end = tokens.size();
            if (getComma_second()<0) {    // one comma
                if (getComma_first() < first_part_end) first_part_start = getComma_first();
            } else {                 // two or more commas
                if (getComma_second() < first_part_end) first_part_start = getComma_second();
                jr_part_end = getComma_second();
                if (getComma_first() < jr_part_end) jr_part_start = getComma_first();
            }
            if (getVon_start()!=0) {     // no 'von part'
                last_part_end = getComma_first();
                if (last_part_end>0) last_part_start = 0;
            } else {                // 'von part' is present
                if (getLast_start()<0) {
                    von_part_end = getComma_first();
                } else {
                    last_part_end = getComma_first();
                    last_part_start = getLast_start();
                    von_part_end = last_part_start;
                }
                von_part_start = 0;
            }
        }

        // Third step: do actual splitting, construct Author object
        return new Author(
          (first_part_start<0 ? null : concatTokens(first_part_start,first_part_end,OFFSET_TOKEN,false)),
          (first_part_start<0 ? null : concatTokens(first_part_start,first_part_end,OFFSET_TOKEN_ABBR,true)),
          (  von_part_start<0 ? null : concatTokens(  von_part_start,  von_part_end,OFFSET_TOKEN,false)),
          ( last_part_start<0 ? null : concatTokens( last_part_start, last_part_end,OFFSET_TOKEN,false)),
          (   jr_part_start<0 ? null : concatTokens(   jr_part_start,   jr_part_end,OFFSET_TOKEN,false))
        );
    }

    /**
     * Concatenates list of tokens from 'tokens' Vector.
     * Tokens are separated by spaces or dashes, dependeing on stored in 'tokens'.
     * Callers always ensure that start < end; thus, there exists at least
     * one token to be concatenated.
     * @param start index of the first token to be concatenated in 'tokens' Vector
     * (always divisible by TOKEN_GROUP_LENGTH).
     * @param end index of the first token not to be concatenated in 'tokens' Vector
     * (always divisible by TOKEN_GROUP_LENGTH).
     * @param offset offset within token group (used to request concatenation of
     * either full tokens or abbreviation).
     * @param dot_after <CODE>true</CODE> -- add period after each token,
     * <CODE>false</CODE> -- do not add.
     * @return the result of concatenation.
     */
    private String concatTokens(int start, int end, int offset, boolean dot_after) {
        StringBuffer res = new StringBuffer();
        // Here we always have start < end
        res.append((String) tokens.get(start+offset));
        if (dot_after) res.append('.');
        start += TOKEN_GROUP_LENGTH;
        while (start < end) {
            res.append(tokens.get(start-TOKEN_GROUP_LENGTH+OFFSET_TOKEN_TERM));
            res.append((String) tokens.get(start+offset));
            if (dot_after) res.append('.');
            start += TOKEN_GROUP_LENGTH;
        }
        return res.toString();
    }

    /**
     * Parses the next token.
     * <p>
     * The string being parsed is stored in global variable <CODE>orig</CODE>,
     * and position which parsing has to start from is stored in global
     * variable <CODE>token_end</CODE>; thus, <CODE>token_end</CODE> has to be
     * set to 0 before the first invocation. Procedure updates <CODE>token_end</CODE>;
     * thus, subsequent invocations do not require any additional variable settings.
     * <p>
     * The type of the token is returned; if it is <CODE>TOKEN_WORD</CODE>, additional
     * information is given  in global variables <CODE>token_start</CODE>, <CODE>token_end</CODE>,
     * <CODE>token_abbr</CODE>, <CODE>token_term</CODE>, and <CODE>token_case</CODE>;
     * namely: <CODE>orig.substring(token_start,token_end)</CODE> is the thext of the token,
     * <CODE>orig.substring(token_start,token_abbr)</CODE> is the token abbreviation,
     * <CODE>token_term</CODE> contains token terminator (space or dash),
     * and <CODE>token_case</CODE> is <CODE>true</CODE>, if token is upper-case
     * and <CODE>false</CODE> if token is lower-case.
     * @return <CODE>TOKEN_EOF</CODE> -- no more tokens,
     * <CODE>TOKEN_COMMA</CODE> -- token is comma,
     * <CODE>TOKEN_AND</CODE> -- token is the word "and" (or "And", or "aND", etc.),
     * <CODE>TOKEN_WORD</CODE> -- token is a word; additional information is given
     * in global variables <CODE>token_start</CODE>, <CODE>token_end</CODE>,
     * <CODE>token_abbr</CODE>, <CODE>token_term</CODE>, and <CODE>token_case</CODE>.
     */
    private int getToken() {
        token_start = token_end;
        while (token_start < orig.length()) {
            char c = orig.charAt(token_start);
            if ( !(c=='~' || c=='-' || Character.isWhitespace(c)) ) break;
            token_start++;
        }
        token_end = token_start;
        if (token_start >= orig.length()) return TOKEN_EOF;
        if (orig.charAt(token_start)==',') { token_end++; return TOKEN_COMMA; }
        token_abbr = -1;
        token_term = ' ';
        token_case = true;
        int braces_level = 0;
        int current_backslash = -1;
        boolean first_letter_is_found = false;
        while (token_end < orig.length()) {
            char c = orig.charAt(token_end);
            if (c=='{') { braces_level++; }
            if (braces_level > 0) if (c=='}') braces_level--;
            if (first_letter_is_found && token_abbr<0 && braces_level==0) token_abbr = token_end;
            if (!first_letter_is_found && current_backslash<0 && Character.isLetter(c)) {
                token_case = Character.isUpperCase(c); first_letter_is_found = true;
            }
            if (current_backslash>=0 && !Character.isLetter(c)) {
                if (!first_letter_is_found) {
                    String tex_cmd_name = orig.substring(current_backslash+1, token_end);
                    if (tex_names.get(tex_cmd_name)!=null) {
                        token_case = Character.isUpperCase(tex_cmd_name.charAt(0));
                        first_letter_is_found = true;
                    }
                }
                current_backslash = -1;
            }
            if (c=='\\') current_backslash = token_end;
            if (braces_level==0)
                if (c==',' || c=='~' || /*c=='-' ||*/ Character.isWhitespace(c)) break;
            // Morten Alver 18 Apr 2006: Removed check for hyphen '-' above to prevent
            // problems with names like Bailey-Jones getting broken up and sorted wrong.
            token_end++;
        }
        if (token_abbr<0) token_abbr = token_end;
        if (token_end<orig.length() && orig.charAt(token_end)=='-') token_term='-';
        if (orig.substring(token_start,token_end).equalsIgnoreCase("and"))
            return TOKEN_AND; else return TOKEN_WORD;
    }

    /**
     * Returns the number of author names in this object.
     * @return the number of author names in this object.
     */
    public int size() { return authors.size(); }
    /**
     * Returns the <CODE>Author</CODE> object for ith author.
     * @param i index of the author (from 0 to <CODE>size()-1</CODE>).
     * @return the <CODE>Author</CODE> object.
     */
    public Author getAuthor(int i) { return (Author) authors.get(i); }
    /**
     * Returns the list of authors in "natbib" format.
     * <p>
     * "John Smith" ==> "Smith";
     * "John Smith and Black Brown, Peter" ==> "Smith and Black Brown";
     * "John von Neumann and John Smith and Black Brown, Peter" ==> "von Neumann et al.".
     * @return formatted list of authors.
     */
    public String getAuthorsNatbib() {
        // Check if we've computed this before:
        if (authorsNatbib != null)
            return authorsNatbib;

        StringBuffer res = new StringBuffer();
        if (size()>0) {
            res.append(getAuthor(0).getLastOnly());
            if (size()==2) {
                res.append(" and ");
                res.append(getAuthor(1).getLastOnly());
            } else if (size()>2) {
                res.append(" et al.");
            }
        }
        authorsNatbib = res.toString();
        return authorsNatbib;
    }
        /**
     * Returns the list of authors separated by commas with last name only;
     * If the list consists of three or more authors, "and" is inserted before
     * the last author's name.
     * <p>
     * "John Smith" ==> "Smith";
     * "John Smith and Black Brown, Peter" ==> "Smith and Black Brown" ;
     * "John von Neumann and John Smith and Black Brown, Peter" ==>
     * "von Neumann, Smith and Black Brown".
     * @return formatted list of authors.
     */
        public String getAuthorsLastOnly() {
            // Check if we've computed this before:
            if (authorsLastOnly != null)
                return authorsLastOnly;
            StringBuffer res = new StringBuffer();
            if (size() > 0) {
                res.append(getAuthor(0).getLastOnly());
                int i = 1;
                while (i < size() - 1) {
                    res.append(", ");
                    res.append(getAuthor(i).getLastOnly());
                    i++;
                }
                if (size() > 2) res.append(",");
                if (size() > 1) {
                    res.append(" and ");
                    res.append(getAuthor(i).getLastOnly());
                }
            }
            authorsLastOnly = res.toString();
            return authorsLastOnly;
        }
    /**
     * Returns the list of authors separated by commas with first names after last name;
     * first names are abbreviated or not depending on parameter.
     * If the list consists of three or more authors, "and" is inserted before
     * the last author's name.
     * <p>
     * "John Smith" ==> "Smith, John" or "Smith, J.";
     * "John Smith and Black Brown, Peter" ==>
     * "Smith, John and Black Brown, Peter" or "Smith, J. and Black Brown, P.";
     * "John von Neumann and John Smith and Black Brown, Peter" ==>
     * "von Neumann, John, Smith, John and Black Brown, Peter" or
     * "von Neumann, J., Smith, J. and Black Brown, P.".
     * @param abbr <CODE>true</CODE> -- abbreviate first names,
     * <CODE>false</CODE> -- do not abbreviate.
     * @return formatted list of authors.
     */
    public String getAuthorsLastFirst(boolean abbr) {
        // Check if we've computed this before:
        if (authorsLastFirst != null)
            return authorsLastFirst;

        StringBuffer res = new StringBuffer();
        if (size()>0) {
            res.append(getAuthor(0).getLastFirst(abbr));
            int i = 1;
            while (i < size()-1) {
                res.append(", ");
                res.append(getAuthor(i).getLastFirst(abbr));
                i++;
            }
            if (size() > 2) res.append(",");
            if (size() > 1) {
                res.append(" and ");
                res.append(getAuthor(i).getLastFirst(abbr));
            }
        }
        authorsLastFirst = res.toString();
        return authorsLastFirst;
    }
    /**
     * Returns the list of authors separated by "and"s with first names after last name;
     * first names are not abbreviated.
     * <p>
     * "John Smith" ==> "Smith, John";
     * "John Smith and Black Brown, Peter" ==> "Smith, John and Black Brown, Peter";
     * "John von Neumann and John Smith and Black Brown, Peter" ==>
     * "von Neumann, John and Smith, John and Black Brown, Peter".
     * @return formatted list of authors.
     */
    public String getAuthorsLastFirstAnds() {
        // Check if we've computed this before:
        if (authorLastFirstAnds != null)
            return authorLastFirstAnds;

        StringBuffer res = new StringBuffer();
        if (size()>0) {
            res.append(getAuthor(0).getLastFirst(false));
            for (int i=1; i<size(); i++) {
                res.append(" and ");
                res.append(getAuthor(i).getLastFirst(false));
            }
        }

        authorLastFirstAnds = res.toString();
        return authorLastFirstAnds;
    }
    /**
     * Returns the list of authors separated by commas with first names before last name;
     * first names are abbreviated or not depending on parameter.
     * If the list consists of three or more authors, "and" is inserted before
     * the last author's name.
     * <p>
     * "John Smith" ==> "John Smith" or "J. Smith";
     * "John Smith and Black Brown, Peter" ==>
     * "John Smith and Peter Black Brown" or "J. Smith and P. Black Brown";
     * "John von Neumann and John Smith and Black Brown, Peter" ==>
     * "John von Neumann, John Smith and Peter Black Brown" or
     * "J. von Neumann, J. Smith and P. Black Brown".
     * @param abbr <CODE>true</CODE> -- abbreviate first names,
     * <CODE>false</CODE> -- do not abbreviate.
     * @return formatted list of authors.
     */
    public String getAuthorsFirstFirst(boolean abbr) {
        // Check if we've computed this before:
        if (authorsFirstFirst != null)
            return authorsFirstFirst;

        StringBuffer res = new StringBuffer();
        if (size()>0) {
            res.append(getAuthor(0).getFirstLast(abbr));
            int i = 1;
            while (i < size()-1) {
                res.append(", ");
                res.append(getAuthor(i).getFirstLast(abbr));
                i++;
            }
            if (size() > 2) res.append(",");
            if (size() > 1) {
                res.append(" and ");
                res.append(getAuthor(i).getFirstLast(abbr));
            }
        }
        authorsFirstFirst = res.toString();
        return authorsFirstFirst;
    }
    /**
     * Returns the list of authors separated by "and"s with first names before last name;
     * first names are not abbreviated.
     * <p>
     * "John Smith" ==> "John Smith";
     * "John Smith and Black Brown, Peter" ==> "John Smith and Peter Black Brown";
     * "John von Neumann and John Smith and Black Brown, Peter" ==>
     * "John von Neumann and John Smith and Peter Black Brown".
     * @return formatted list of authors.
     */
    public String getAuthorsFirstFirstAnds() {
        // Check if we've computed this before:
        if (authorsFirstFirstAnds != null)
            return authorsFirstFirstAnds;

        StringBuffer res = new StringBuffer();
        if (size()>0) {
            res.append(getAuthor(0).getFirstLast(false));
            for (int i=1; i<size(); i++) {
                res.append(" and ");
                res.append(getAuthor(i).getFirstLast(false));
            }
        }
        authorsFirstFirstAnds = res.toString();
        return authorsFirstFirstAnds;
    }

    /**
     * Returns the list of authors in a form suitable for alphabetization. This means that
     * last names come first, never preceded by "von" particles, and that any braces are
     * removed. First names are abbreviated so the same name is treated similarly if abbreviated
     * in one case and not in another.
     * This form is not intended to be suitable for presentation, only for sorting.
     *
     * <p>
     * "John Smith" ==> "Smith, J.";
     * "John von Neumann
     * @return formatted list of authors
     */
    public String getAuthorsForAlphabetization() {
         if (authorsAlph != null)
            return authorsAlph;

         StringBuffer res = new StringBuffer();
        if (size()>0) {
            res.append(getAuthor(0).getNameForAlphabetization());
            for (int i=1; i<size(); i++) {
                res.append(" and ");
                res.append(getAuthor(i).getNameForAlphabetization());
            }
        }
        authorsAlph = res.toString();
        return authorsAlph;
    }



    public int getVon_start() {
		return von_start;
	}

	public void setVon_start(int von_start) {
		this.von_start = von_start;
	}



	public int getLast_start() {
		return last_start;
	}

	public void setLast_start(int last_start) {
		this.last_start = last_start;
	}



	public int getComma_first() {
		return comma_first;
	}

	public void setComma_first(int comma_first) {
		this.comma_first = comma_first;
	}



	public int getComma_second() {
		return comma_second;
	}

	public void setComma_second(int comma_second) {
		this.comma_second = comma_second;
	}



	/**
     *  This is an immutable class that keeps information regarding single author.
     *  It is just a container for the information, with very simple methods
     *  to access it.
     *  <p>
     *  Current usage: only methods <code>getLastOnly</code>,
     *  <code>getFirstLast</code>, and <code>getLastFirst</code> are used;
     *  all other methods are provided for completeness.
     */
    private static class Author {
        private final String first_part;
        private final String first_abbr;
        private final String von_part;
        private final String last_part;
        private final String jr_part;
        /**
         * Creates the Author object.
         * If any part of the name is absent, <CODE>null</CODE> must be passes;
         * otherwise other methods may return erroneous results.
         * @param first the first name of the author (may consist of several tokens,
         * like "Charles Louis Xavier Joseph" in
         * "Charles Louis Xavier Joseph de la Vall{\'e}e Poussin")
         * @param firstabbr the abbreviated first name of the author (may consist
         * of several tokens, like "C. L. X. J." in
         * "Charles Louis Xavier Joseph de la Vall{\'e}e Poussin").
         * It is a responsibility of the caller to create a reasonable
         * abbreviation of the first name.
         * @param von the von part of the author's name (may consist of
         * several tokens, like "de la" in
         * "Charles Louis Xavier Joseph de la Vall{\'e}e Poussin")
         * @param last the lats name of the author (may consist of several tokens,
         * like "Vall{\'e}e Poussin" in
         * "Charles Louis Xavier Joseph de la Vall{\'e}e Poussin")
         * @param jr the junior part of the author's name (may consist of
         * several tokens, like "Jr. III" in
         * "Smith, Jr. III, John")
         */
        public Author(String first, String firstabbr, String von, String last, String jr) {
            first_part = first;
            first_abbr = firstabbr;
            von_part = von;
            last_part = last;
            jr_part = jr;
        }
        /**
         * Retunrns the first name of the author stored in this object.
         * @return first name of the author (may consist of several tokens)
         */
        public String getFirst() { return first_part; }
        /**
         * Retunrns the abbreviated first name of the author stored in this object.
         * @return abbreviated first name of the author (may consist of several tokens)
         */
        public String getFirstAbbr() { return first_abbr; }
        /**
         * Retunrns the von part of the author's name stored in this object.
         * @return von part of the author's name (may consist of several tokens)
         */
        public String getVon() { return von_part; }
        /**
         * Retunrns the last name of the author stored in this object.
         * @return last name of the author (may consist of several tokens)
         */
        public String getLast() { return last_part; }
        /**
         * Retunrns the junior part of the author's name stored in this object.
         * @return junior part of the author's name (may consist of several tokens)
         */
        public String getJr() { return jr_part; }
        /**
         * Returns von part followed by last name.
         * If both fields were specified as <CODE>null</CODE>,
         * the empty string <CODE>""</CODE> is returned.
         * @return 'von Last'
         */
        public String getLastOnly() {
            if (von_part==null) {
                return (last_part==null ? "" : last_part);
            } else {
                return (last_part==null ? von_part : von_part + " " + last_part);
            }
        }
        /**
         * Returns the author's name in form 'von Last, Jr., First' with the first name
         * full or abbreviated depending on parameter.
         * @param abbr <CODE>true</CODE> - abbreviate first name,
         * <CODE>false</CODE> - do not abbreviate
         * @return 'von Last, Jr., First' (if <CODE>abbr==false</CODE>) or
         * 'von Last, Jr., F.' (if <CODE>abbr==true</CODE>)
         */
        public String getLastFirst(boolean abbr) {
            String res = getLastOnly();
            if (jr_part != null) res += ", " + jr_part;
            if (abbr) {
                if (first_abbr != null) res += ", " + first_abbr;
            } else {
                if (first_part != null) res += ", " + first_part;
            }
            return res;
        }
        /**
         * Returns the author's name in form 'First von Last, Jr.' with the first name
         * full or abbreviated depending on parameter.
         * @param abbr <CODE>true</CODE> - abbreviate first name,
         * <CODE>false</CODE> - do not abbreviate
         * @return 'First von Last, Jr.' (if <CODE>abbr==false</CODE>) or
         * 'F. von Last, Jr.' (if <CODE>abbr==true</CODE>)
         */
        public String getFirstLast(boolean abbr) {
            String res = getLastOnly();
            if (abbr) {
                res = (first_abbr==null ? "" : first_abbr + " ") + res;
            } else {
                res = (first_part==null ? "" : first_part + " ") + res;
            }
            if (jr_part != null) res += ", " + jr_part;
            return res;
        }

        public String getNameForAlphabetization() {
            StringBuffer res = new StringBuffer();
            if (last_part != null)
                res.append(last_part);
            if (jr_part != null) {
                res.append(", ");
                res.append(jr_part);
            }
            if (first_abbr != null) {
                res.append(", ");
                res.append(first_abbr);
            }
            while ((res.length() > 0) && (res.charAt(0) == '{'))
                res.deleteCharAt(0);
            return res.toString();

        }
    }//end Author
}//end AuthorList
