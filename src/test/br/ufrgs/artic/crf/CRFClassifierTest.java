package br.ufrgs.artic.crf;

import br.ufrgs.artic.crf.model.CRFLine;
import br.ufrgs.artic.crf.model.CRFWord;
import br.ufrgs.artic.crf.model.LineClass;
import br.ufrgs.artic.crf.model.WordClass;
import br.ufrgs.artic.exceptions.CRFClassifierException;
import br.ufrgs.artic.exceptions.OmniPageParserException;
import br.ufrgs.artic.model.Line;
import br.ufrgs.artic.model.Word;
import br.ufrgs.artic.parser.omnipage.OmniPageParser;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CRFClassifierTest {

    @Test
    public void itShouldParseLinesToCRFWhenProvidingValidLineList() throws OmniPageParserException, IOException {

        List<Line> lines = new OmniPageParser(getClass().getResource("/omnipage/sample.xml").getFile()).getLines();

        String crfLines = CRFClassifier.getCRFLinesAsString(lines);

        String expectedCrfLines = new String(Files.readAllBytes(Paths.get(getClass().getResource("/crf/firstLevel.unclassified.sample.crf").getFile())));

        assertNotNull(crfLines);
        assertEquals(expectedCrfLines, crfLines);
    }

    @Test
    public void itShouldClassifyLineWhenProvidingValidLineList() throws OmniPageParserException, CRFClassifierException, IOException {

        List<Line> lines = new OmniPageParser(getClass().getResource("/omnipage/sample.xml").getFile()).getLines();

        List<CRFLine> crfLines = CRFClassifier.classifyLines(lines);

        assertNotNull(crfLines);
        assertEquals(lines.size(), crfLines.size());
        assertEquals(LineClass.TITLE.toString(), crfLines.get(0).getLineClass().toString());
        assertEquals(lines.get(0), crfLines.get(0).getLine());

        StringBuilder crfLinesClassifiedSB = new StringBuilder();

        for (CRFLine crfLine : crfLines) {
            crfLinesClassifiedSB.append(crfLine.toCRF()).append("\n");
        }

        String crfLinesClassified = crfLinesClassifiedSB.toString();
        String expectedCrfLines = new String(Files.readAllBytes(Paths.get(getClass().getResource("/crf/firstLevel.classified.sample.crf").getFile())));

        assertEquals(expectedCrfLines, crfLinesClassified);
    }

    @Test
    public void itShouldParseHeaderWordsToCRFWhenProvidingValidCRFLineList() throws OmniPageParserException, IOException {

        List<Line> lines = new OmniPageParser(getClass().getResource("/omnipage/sampleWithHeader.xml").getFile()).getLines();

        List<Word> words = new ArrayList<>();
        words.addAll(lines.get(0).getWords());
        words.addAll(lines.get(1).getWords());

        String crfWords = CRFClassifier.getHeaderCRFWordsAsString(words);

        String expectedHeaderCRFWords = new String(Files.readAllBytes(Paths.get(getClass().getResource("/crf/headerSecondLevel.unclassified.sample.crf").getFile())));

        assertNotNull(crfWords);
        assertEquals(expectedHeaderCRFWords, crfWords);
    }

    @Test
    public void itShouldClassifyWordsWhenProvidingValidCRFLineList() throws OmniPageParserException, CRFClassifierException, IOException {

        List<Line> lines = new OmniPageParser(getClass().getResource("/omnipage/sampleWithHeader.xml").getFile()).getLines();

        List<CRFLine> crfLines = CRFClassifier.classifyLines(lines);

        Map<LineClass, List<CRFWord>> wordsMapByLineClass = CRFClassifier.classifyWords(crfLines);

        assertNotNull(wordsMapByLineClass);
        List<CRFWord> headerWords = wordsMapByLineClass.get(LineClass.HEADER);
        assertEquals(6, headerWords.size());
        assertEquals(WordClass.JOURNAL_NAME, headerWords.get(0).getWordClass());
        assertEquals(crfLines.get(0).getLine().getWords().get(0), headerWords.get(0).getWord());

        StringBuilder headerCRFClassifiedSB = new StringBuilder();

        for (CRFWord crfWord : wordsMapByLineClass.get(LineClass.HEADER)) {
            headerCRFClassifiedSB.append(crfWord.toHeaderCRF()).append("\n");
        }

        String headerCRFWordsClassified = headerCRFClassifiedSB.toString();
        String expectedHeaderCRFWords = new String(Files.readAllBytes(Paths.get(getClass().getResource("/crf/headerSecondLevel.classified.sample.crf").getFile())));

        assertEquals(expectedHeaderCRFWords, headerCRFWordsClassified);
    }
}
