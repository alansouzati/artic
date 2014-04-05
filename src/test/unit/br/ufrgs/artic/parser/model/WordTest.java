package unit.br.ufrgs.artic.parser.model;

import br.ufrgs.artic.parser.model.Alignment;
import br.ufrgs.artic.parser.model.FontSize;
import br.ufrgs.artic.parser.model.Word;
import org.junit.Test;

import static org.junit.Assert.*;

public class WordTest {

    @Test
    public void itShouldCreateWordWhenRequiredParamsProvided() {

        Word validWord = new Word.Builder(0, "Testing").build();

        assertNotNull(validWord);
        //line tests for required params
        assertEquals(0, validWord.getIndex());
        assertEquals("Testing", validWord.getContent());

        //line tests for default params
        assertEquals(Alignment.LEFT, validWord.getAlignment());
        assertEquals("Arial", validWord.getFontFace());
        assertEquals(FontSize.NORMAL, validWord.getFontSize());
        assertFalse(validWord.isBold());
        assertFalse(validWord.isItalic());
        assertFalse(validWord.isUnderline());
        assertNull(validWord.getPreviousWord());
    }

    @Test
    public void itShouldCreateWordWhenOptionalParamsProvided() {

        Word previousWord = new Word.Builder(0, "Testing").build();

        Word validWord = new Word.Builder(0, "Testing 2").alignment(Alignment.CENTERED)
                .fontFace("Times New Roman").fontSize(FontSize.BIG).bold(true)
                .italic(true).underline(true).previousWord(previousWord).build();

        assertNotNull(validWord);
        //line tests for required params
        assertEquals(0, validWord.getIndex());
        assertEquals("Testing 2", validWord.getContent());

        //line tests for default params
        assertEquals(Alignment.CENTERED, validWord.getAlignment());
        assertEquals("Times New Roman", validWord.getFontFace());
        assertEquals(FontSize.BIG, validWord.getFontSize());
        assertTrue(validWord.isBold());
        assertTrue(validWord.isItalic());
        assertTrue(validWord.isUnderline());
        assertEquals(previousWord, validWord.getPreviousWord());
    }

    @Test
    public void itShouldFailToCreateWordWhenNoContentProvided() {

        try {
            new Word.Builder(0, null);
            fail("Cannot create a word without content.");
        } catch (IllegalArgumentException e) {
            assertEquals("Please provide a content for the word.", e.getMessage());
        }

    }
}
