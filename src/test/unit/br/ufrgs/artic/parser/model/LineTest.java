package unit.br.ufrgs.artic.parser.model;

import br.ufrgs.artic.parser.model.Alignment;
import br.ufrgs.artic.parser.model.Line;
import br.ufrgs.artic.parser.model.Page;
import br.ufrgs.artic.parser.model.Word;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class LineTest {

    @Test
    public void itShouldCreateLineWhenRequiredParamsProvided() {


        Word testingWord = new Word.Builder(0, "Testing").build();

        Line validLine = new Line.Builder(0, new Page(0.00, 0, 0))
                .addWord(testingWord).build();

        assertNotNull(validLine);
        //line tests for required params
        assertEquals(0, validLine.getIndex());
        assertEquals(12.00, validLine.getPage().getAverageFontSize(), 2);
        assertEquals(0, validLine.getPage().getTop());
        assertEquals(0, validLine.getPage().getLeft());
        assertEquals(1, validLine.getWords().size());
        assertEquals("Testing", validLine.getContent());

        //line tests for default params
        assertEquals(Alignment.LEFT, validLine.getAlignment());
        assertEquals("Arial", validLine.getFontFace());
        assertEquals(12.00, validLine.getFontSize(), 2);
        assertFalse(validLine.isBold());
        assertFalse(validLine.isItalic());
        assertFalse(validLine.isUnderline());
        assertNull(validLine.getPreviousLine());
    }

    @Test
    public void itShouldCreateLineWhenOptionalParamsProvided() {

        ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word.Builder(0, "Testing").build());
        words.add(new Word.Builder(1, "Testing").build());

        Line previousLine = new Line.Builder(0, new Page(0.00, 0, 0)).addWord(new Word.Builder(0, "Testing").build()).build();

        Line validLine = new Line.Builder(0, new Page(18.00, 1, 1)).addAllWords(words).alignment(Alignment.CENTER)
                .fontFace("Times New Roman").fontSize(20.00).bold(true)
                .italic(true).underline(true).previousLine(previousLine).build();

        assertNotNull(validLine);
        //line tests for required params
        assertEquals(0, validLine.getIndex());
        assertEquals(18.00, validLine.getPage().getAverageFontSize(), 2);
        assertEquals(1, validLine.getPage().getTop());
        assertEquals(1, validLine.getPage().getLeft());
        assertEquals(2, validLine.getWords().size());
        assertEquals("Testing Testing", validLine.getContent());

        //line tests for default params
        assertEquals(Alignment.CENTER, validLine.getAlignment());
        assertEquals("Times New Roman", validLine.getFontFace());
        assertEquals(20.00, validLine.getFontSize(), 2);
        assertTrue(validLine.isBold());
        assertTrue(validLine.isItalic());
        assertTrue(validLine.isUnderline());
        assertEquals(previousLine, validLine.getPreviousLine());
    }

    @Test
    public void itShouldFailToCreateLineWhenNoPageProvided() {

        try {
            new Line.Builder(0, null);
            fail("Cannot create a line without a page.");
        } catch (IllegalArgumentException e) {
            assertEquals("Please provide a page for the line.", e.getMessage());
        }

    }

    @Test
    public void itShouldFailToCreateLineWhenNoWordProvided() {

        try {
            new Line.Builder(0, new Page(0.00, 0, 0)).build();
            fail("Cannot create a line without words.");
        } catch (IllegalArgumentException e) {
            assertEquals("Please provide enough words.", e.getMessage());
        }

    }
}
