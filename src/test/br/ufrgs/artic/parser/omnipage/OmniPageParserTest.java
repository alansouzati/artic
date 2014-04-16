package br.ufrgs.artic.parser.omnipage;

import br.ufrgs.artic.exceptions.OmniPageParserException;
import br.ufrgs.artic.model.*;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OmniPageParserTest {

    @Test
    public void itShouldCreateLinesWithValidXMLProvided() throws OmniPageParserException {

        List<Line> validLines = new OmniPageParser(getClass().getResource("/omnipage/sample.xml").getFile()).getLines();

        assertNotNull(validLines);

        Line titleLine = validLines.get(2);

        assertEquals(FontSize.BIG, titleLine.getFontSize());
        assertEquals(Alignment.CENTERED, titleLine.getAlignment());
        assertEquals(Paragraph.HEADER, titleLine.getParagraph());
        assertEquals(2, titleLine.getLeft());
        assertEquals(2, titleLine.getTop());
        assertEquals(2, titleLine.getIndex());
        assertEquals(6, titleLine.getWords().size());

        Word firstWord = titleLine.getWords().get(0);
        assertEquals(FontSize.BIG, firstWord.getFontSize());
        assertEquals(Alignment.CENTERED, firstWord.getAlignment());
        assertNotNull(firstWord.getPreviousWord());

        Word secondWord = titleLine.getWords().get(1);
        assertEquals(FontSize.BIG, secondWord.getFontSize());
        assertEquals(Alignment.CENTERED, secondWord.getAlignment());
        assertNotNull(secondWord.getPreviousWord());
        assertEquals(firstWord, secondWord.getPreviousWord());

        assertNotNull(titleLine.getPreviousLine());
    }
}
