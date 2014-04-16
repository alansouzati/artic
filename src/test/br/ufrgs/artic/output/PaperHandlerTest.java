package br.ufrgs.artic.output;

import br.ufrgs.artic.crf.CRFClassifier;
import br.ufrgs.artic.crf.model.CRFLine;
import br.ufrgs.artic.crf.model.CRFWord;
import br.ufrgs.artic.crf.model.LineClass;
import br.ufrgs.artic.exceptions.CRFClassifierException;
import br.ufrgs.artic.exceptions.OmniPageParserException;
import br.ufrgs.artic.model.Line;
import br.ufrgs.artic.output.model.Paper;
import br.ufrgs.artic.parser.omnipage.OmniPageParser;
import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public class PaperHandlerTest {

    @Test
    public void itShouldCreateAValidJSONWhenProvidingAValidCRFClassificationMap() throws OmniPageParserException, CRFClassifierException, IOException, JSONException {

        List<Line> lines = new OmniPageParser(getClass().getResource("/omnipage/sample.xml").getFile()).getLines();

        List<CRFLine> crfLines = CRFClassifier.classifyFirstLevelCRF(lines);

        Map<LineClass, List<CRFWord>> wordsMapByLineClass = CRFClassifier.classifySecondLevelCRF(crfLines);

        Paper paper = PaperHandler.getPaper(wordsMapByLineClass);

        String expectedPaperJSON = new String(Files.readAllBytes(Paths.get(getClass().getResource("/output/sample.json").getFile())));

        String generatedPaperJSON = paper.toJSON();

        assertEquals(expectedPaperJSON, generatedPaperJSON, false);
    }
}
