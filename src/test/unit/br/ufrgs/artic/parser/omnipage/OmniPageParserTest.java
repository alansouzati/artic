package unit.br.ufrgs.artic.parser.omnipage;

import br.ufrgs.artic.exceptions.OmniPageParserException;
import br.ufrgs.artic.parser.model.Alignment;
import br.ufrgs.artic.parser.model.FontSize;
import br.ufrgs.artic.parser.model.Line;
import br.ufrgs.artic.parser.model.Paragraph;
import br.ufrgs.artic.parser.omnipage.OmniPageParser;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class OmniPageParserTest {

    @Test
    public void itShouldCreateLinesWithValidXMLProvided() throws OmniPageParserException {

        List<Line> validLines = new OmniPageParser(getClass().getResource("/omnipage/sample.xml").getFile()).getLines();

        assertNotNull(validLines);

        Line firstLine = validLines.get(0);

        assertEquals(FontSize.BIG, firstLine.getFontSize());
        assertEquals(Alignment.CENTERED, firstLine.getAlignment());
        assertEquals(Paragraph.HEADER, firstLine.getParagraph());
        assertEquals(3, firstLine.getLeft());
        assertEquals(0, firstLine.getTop());
        assertEquals(0, firstLine.getIndex());
        assertNull(firstLine.getPreviousLine());

        Line secondLine = validLines.get(1);

        assertEquals(FontSize.MEDIUM, secondLine.getFontSize());
        assertEquals(Alignment.CENTERED, secondLine.getAlignment());
        assertEquals(Paragraph.HEADER, secondLine.getParagraph());
        assertEquals(1, secondLine.getIndex());
        assertNotNull(secondLine.getPreviousLine());
        assertEquals(firstLine, secondLine.getPreviousLine());

        Line farAwayLine = validLines.get(9);

        assertEquals(FontSize.MEDIUM, farAwayLine.getFontSize());
        assertEquals(Alignment.LEFT, farAwayLine.getAlignment());
        assertEquals(Paragraph.NEW, farAwayLine.getParagraph());
        assertEquals(9, farAwayLine.getIndex());

        Line veryFarAwayLine = validLines.get(24);

        assertEquals(FontSize.NORMAL, veryFarAwayLine.getFontSize());
        assertEquals(Alignment.JUSTIFIED, veryFarAwayLine.getAlignment());
        assertEquals(24, veryFarAwayLine.getIndex());
    }
}
