package br.ufrgs.artic.model;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class LineTest {

    @Test
    public void itShouldCreateLineWhenRequiredParamsProvided() {

        Line validLine = new Line.Builder(0, new Page(0.00, 0, 0)).build();

        assertNotNull(validLine);
        //line tests for required params
        assertEquals(0, validLine.getIndex());
        assertEquals(12.00, validLine.getPage().getAverageFontSize(), 2);
        assertEquals(1, validLine.getPage().getTop());
        assertEquals(1, validLine.getPage().getLeft());
        assertEquals(0, validLine.getWords().size());
        assertEquals("", validLine.getContent());

        //line tests for default params
        assertEquals(Alignment.LEFT, validLine.getAlignment());
        assertEquals(Paragraph.SAME, validLine.getParagraph());
        assertEquals("Arial", validLine.getFontFace());
        assertEquals(FontSize.NORMAL, validLine.getFontSize());
        assertFalse(validLine.isBold());
        assertFalse(validLine.isItalic());
        assertFalse(validLine.isUnderline());
        assertNull(validLine.getPreviousLine());
    }

    @Test
    public void itShouldCreateLineWhenOptionalParamsProvided() {

        ArrayList<Word> words = new ArrayList<>();
        words.add(new Word.Builder(0, "Testing", new Context()).build());
        words.add(new Word.Builder(1, "Testing", new Context()).build());

        Line previousLine = new Line.Builder(0, new Page(0.00, 0, 0)).addWord(new Word.Builder(0, "Testing", new Context()).build()).build();

        Line validLine = new Line.Builder(0, new Page(18.00, 1, 1)).addAllWords(words).alignment(Alignment.CENTERED)
                .fontFace("Times New Roman").fontSize(FontSize.BIG).bold(true)
                .italic(true).underline(true).previousLine(previousLine).paragraph(Paragraph.NEW).build();

        assertNotNull(validLine);
        //line tests for required params
        assertEquals(0, validLine.getIndex());
        assertEquals(18.00, validLine.getPage().getAverageFontSize(), 2);
        assertEquals(1, validLine.getPage().getTop());
        assertEquals(1, validLine.getPage().getLeft());
        assertEquals(0, validLine.getTopNormalized());
        assertEquals(0, validLine.getLeftNormalized());
        assertEquals(2, validLine.getWords().size());
        assertEquals("Testing Testing", validLine.getContent());

        //line tests for default params
        assertEquals(Alignment.CENTERED, validLine.getAlignment());
        assertEquals(Paragraph.NEW, validLine.getParagraph());
        assertEquals("Times New Roman", validLine.getFontFace());
        assertEquals(FontSize.BIG, validLine.getFontSize());
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

}
