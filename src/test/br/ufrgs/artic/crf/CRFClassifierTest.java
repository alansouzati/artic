package br.ufrgs.artic.crf;

import br.ufrgs.artic.exceptions.OmniPageParserException;
import br.ufrgs.artic.model.Line;
import br.ufrgs.artic.parser.omnipage.OmniPageParser;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CRFClassifierTest {

    @Test
    public void itShouldParseLinesToCRFWhenProvidingValidLineList() throws OmniPageParserException, IOException {

        List<Line> lines = new OmniPageParser(getClass().getResource("/omnipage/sample.xml").getFile()).getLines();

        String crfLines = CRFClassifier.getCRFLines(lines);

        String expectedCrfLines = new String(Files.readAllBytes(Paths.get(getClass().getResource("/crf/firstLevel.unclassified.sample.crf").getFile())));

        assertNotNull(crfLines);
        assertEquals(expectedCrfLines, crfLines);
    }
}
