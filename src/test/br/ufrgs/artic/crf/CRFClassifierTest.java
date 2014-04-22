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

        List<Line> lines = new OmniPageParser(getClass().getResource("/omnipage/elsevierSample.xml").getFile()).getLines();

        String crfLines = CRFClassifier.getCRFLinesAsString(lines);

        String expectedCrfLines = new String(Files.readAllBytes(Paths.get(getClass().getResource("/crf/firstLevel.unclassified.sample.crf").getFile())));

        assertNotNull(crfLines);
        assertEquals(expectedCrfLines, crfLines);
    }

    @Test
    public void itShouldClassifyLineWhenProvidingValidLineList() throws OmniPageParserException, CRFClassifierException, IOException {

        List<Line> lines = new OmniPageParser(getClass().getResource("/omnipage/elsevierSample.xml").getFile()).getLines();

        List<CRFLine> crfLines = CRFClassifier.classifyFirstLevelCRF(lines);

        assertNotNull(crfLines);
        assertEquals(lines.size(), crfLines.size());
        assertEquals(LineClass.TITLE.toString(), crfLines.get(2).getLineClass().toString());
        assertEquals(lines.get(2), crfLines.get(2).getLine());

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

        List<Line> lines = new OmniPageParser(getClass().getResource("/omnipage/elsevierSample.xml").getFile()).getLines();

        List<Word> words = new ArrayList<>();
        words.addAll(lines.get(0).getWords());
        words.addAll(lines.get(1).getWords());

        String crfWords = CRFClassifier.getHeaderCRFWordsAsString(words);

        String expectedHeaderCRFWords = new String(Files.readAllBytes(Paths.get(getClass().getResource("/crf/headerSecondLevel.unclassified.sample.crf").getFile())));

        assertNotNull(crfWords);
        assertEquals(expectedHeaderCRFWords, crfWords);
    }

    @Test
    public void itShouldParseAuthorInformationWordsToCRFWhenProvidingValidCRFLineList() throws OmniPageParserException, IOException {

        List<Line> lines = new OmniPageParser(getClass().getResource("/omnipage/elsevierSample.xml").getFile()).getLines();

        List<Word> words = new ArrayList<>();
        words.addAll(lines.get(3).getWords());
        words.addAll(lines.get(4).getWords());
        words.addAll(lines.get(5).getWords());

        String crfWords = CRFClassifier.getAuthorInformationCRFWordsAsString(words);

        String expectedAuthorInformationWords = new String(Files.readAllBytes(Paths.get(getClass().getResource("/crf/authorInformationSecondLevel.unclassified.sample.crf").getFile())));

        assertNotNull(crfWords);
        assertEquals(expectedAuthorInformationWords, crfWords);
    }

    @Test
    public void itShouldParseFootnoteWordsToCRFWhenProvidingValidCRFLineList() throws OmniPageParserException, IOException {

        List<Line> lines = new OmniPageParser(getClass().getResource("/omnipage/elsevierSample.xml").getFile()).getLines();

        List<Word> words = new ArrayList<>();
        words.addAll(lines.get(38).getWords());
        words.addAll(lines.get(39).getWords());
        words.addAll(lines.get(40).getWords());
        words.addAll(lines.get(41).getWords());
        words.addAll(lines.get(42).getWords());

        String crfWords = CRFClassifier.getFootnoteCRFWordsAsString(words);

        String expectedFootnoteWords = new String(Files.readAllBytes(Paths.get(getClass().getResource("/crf/footnoteSecondLevel.unclassified.sample.crf").getFile())));

        assertNotNull(crfWords);
        assertEquals(expectedFootnoteWords, crfWords);
    }

    @Test
    public void itShouldClassifyWordsWhenProvidingValidCRFLineList() throws OmniPageParserException, CRFClassifierException, IOException {

        List<Line> lines = new OmniPageParser(getClass().getResource("/omnipage/elsevierSample.xml").getFile()).getLines();

        List<CRFLine> crfLines = CRFClassifier.classifyFirstLevelCRF(lines);

        Map<LineClass, List<CRFWord>> wordsMapByLineClass = CRFClassifier.classifySecondLevelCRF(crfLines);

        assertNotNull(wordsMapByLineClass);

        //HEADER VALIDATION
        List<CRFWord> headerWords = wordsMapByLineClass.get(LineClass.HEADER);
        assertNotNull(headerWords);
        assertEquals(6, headerWords.size());
        assertEquals(WordClass.JOURNAL_NAME, headerWords.get(0).getWordClass());
        assertEquals(crfLines.get(0).getLine().getWords().get(0), headerWords.get(0).getWord());

        StringBuilder headerCRFClassifiedSB = new StringBuilder();

        for (CRFWord crfWord : headerWords) {
            headerCRFClassifiedSB.append(crfWord.toHeaderCRF()).append("\n");
        }

        String headerCRFWordsClassified = headerCRFClassifiedSB.toString();
        String expectedHeaderCRFWords = new String(Files.readAllBytes(Paths.get(getClass().getResource("/crf/headerSecondLevel.classified.sample.crf").getFile())));

        assertEquals(expectedHeaderCRFWords, headerCRFWordsClassified);

        //AUTHOR VALIDATION
        List<CRFWord> authorInformationWords = wordsMapByLineClass.get(LineClass.AUTHOR_INFORMATION);
        assertNotNull(authorInformationWords);
        assertEquals(32, authorInformationWords.size());
        assertEquals(WordClass.AUTHOR, authorInformationWords.get(0).getWordClass());
        assertEquals(crfLines.get(3).getLine().getWords().get(0), authorInformationWords.get(0).getWord());

        StringBuilder authorInformationCRFClassifiedSB = new StringBuilder();

        for (CRFWord crfWord : authorInformationWords) {
            authorInformationCRFClassifiedSB.append(crfWord.toAuthorInformationCRF()).append("\n");
        }

        String authorInformationCRFWordsClassified = authorInformationCRFClassifiedSB.toString();
        String expectedAuthorInformationCRFWords = new String(Files.readAllBytes(Paths.get(getClass().getResource("/crf/authorInformationSecondLevel.classified.sample.crf").getFile())));

        assertEquals(expectedAuthorInformationCRFWords, authorInformationCRFWordsClassified);

        //FOOTNOTE VALIDATION
        List<CRFWord> footnoteWords = wordsMapByLineClass.get(LineClass.FOOTNOTE);
        assertNotNull(footnoteWords);
        assertEquals(28, footnoteWords.size());
        assertEquals(WordClass.OTHER, footnoteWords.get(0).getWordClass());
        assertEquals(crfLines.get(38).getLine().getWords().get(0), footnoteWords.get(0).getWord());

        StringBuilder footnoteCRFClassifiedSB = new StringBuilder();

        for (CRFWord crfWord : footnoteWords) {
            footnoteCRFClassifiedSB.append(crfWord.toFootnoteCRF()).append("\n");
        }

        String footnoteCRFWordsClassified = footnoteCRFClassifiedSB.toString();
        String expectedFootnoteCRFWords = new String(Files.readAllBytes(Paths.get(getClass().getResource("/crf/footnoteSecondLevel.classified.sample.crf").getFile())));

        assertEquals(expectedFootnoteCRFWords, footnoteCRFWordsClassified);
    }

}
