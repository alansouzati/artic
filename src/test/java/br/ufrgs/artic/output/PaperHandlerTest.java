package br.ufrgs.artic.output;

import br.ufrgs.artic.crf.CRFClassifier;
import br.ufrgs.artic.crf.model.CRFLine;
import br.ufrgs.artic.crf.model.CRFWord;
import br.ufrgs.artic.crf.model.LineClass;
import br.ufrgs.artic.exceptions.CRFClassifierException;
import br.ufrgs.artic.exceptions.ParserException;
import br.ufrgs.artic.model.Line;
import br.ufrgs.artic.output.model.Paper;
import br.ufrgs.artic.output.model.PaperBoundary;
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
    public void itShouldGenerateMetadataForElsevierSample() throws ParserException, CRFClassifierException, IOException, JSONException {

        Paper paper = getPaper("/omnipage/elsevierSample.xml");

        String expectedPaperJSON = new String(Files.readAllBytes(Paths.get(getClass().getResource("/output/elsevierSample.json").getFile())));

        assertEquals(expectedPaperJSON, paper.toJSON(), true);
    }

    @Test
    public void itShouldGenerateMetadataForACMSample() throws ParserException, CRFClassifierException, IOException, JSONException {

        Paper paper = getPaper("/omnipage/acmSample.xml");

        String expectedPaperJSON = new String(Files.readAllBytes(Paths.get(getClass().getResource("/output/acmSample.json").getFile())));

        assertEquals(expectedPaperJSON, paper.toJSON(), true);
    }

    @Test
    public void itShouldGenerateMetadataForIEEESample() throws ParserException, CRFClassifierException, IOException, JSONException {

        Paper paper = getPaper("/omnipage/ieeeSample.xml");

        String expectedPaperJSON = new String(Files.readAllBytes(Paths.get(getClass().getResource("/output/ieeeSample.json").getFile())));

        assertEquals(expectedPaperJSON, paper.toJSON(), true);
    }

    @Test
    public void itShouldGenerateMetadataForACMSampleWithSingleAuthor() throws ParserException, CRFClassifierException, IOException, JSONException {

        Paper paper = getPaper("/omnipage/acmSampleSingleAuthor.xml");

        String expectedPaperJSON = new String(Files.readAllBytes(Paths.get(getClass().getResource("/output/acmSampleSingleAuthor.json").getFile())));

        assertEquals(expectedPaperJSON, paper.toJSON(), true);
    }

    @Test
    public void itShouldGenerateMetadataForACMSampleNoFootnote() throws ParserException, CRFClassifierException, IOException, JSONException {

        Paper paper = getPaper("/omnipage/acmSampleNoFootnote.xml");

        String expectedPaperJSON = new String(Files.readAllBytes(Paths.get(getClass().getResource("/output/acmSampleNoFootnote.json").getFile())));

        assertEquals(expectedPaperJSON, paper.toJSON(), true);
    }

    @Test
    public void itShouldGenerateMetadataForACMSampleWithHeader() throws ParserException, CRFClassifierException, IOException, JSONException {

        Paper paper = getPaper("/omnipage/acmSampleWithHeader.xml");

        String expectedPaperJSON = new String(Files.readAllBytes(Paths.get(getClass().getResource("/output/acmSampleWithHeader.json").getFile())));

        assertEquals(expectedPaperJSON, paper.toJSON(), true);
    }

    private Paper getPaper(String xmlPath) throws ParserException, CRFClassifierException {
        List<Line> lines = new OmniPageParser().getPage(getClass().getResource(xmlPath).getFile()).getLines();

        List<CRFLine> crfLines = CRFClassifier.classifyFirstLevelCRF(lines);

        Map<LineClass, List<CRFWord>> wordsMapByLineClass = CRFClassifier.classifySecondLevelCRF(crfLines);

        return new PaperHandler(new PaperBoundary()).getPaper(wordsMapByLineClass);
    }
}
