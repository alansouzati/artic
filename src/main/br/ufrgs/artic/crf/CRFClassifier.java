package br.ufrgs.artic.crf;

import br.ufrgs.artic.crf.model.CRFLine;
import br.ufrgs.artic.crf.model.CRFWord;
import br.ufrgs.artic.crf.model.LineClass;
import br.ufrgs.artic.crf.model.WordClass;
import br.ufrgs.artic.exceptions.CRFClassifierException;
import br.ufrgs.artic.model.Line;
import br.ufrgs.artic.model.Word;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the core class responsible for classifying the given lines using Conditional Random Fields (CRF).
 */
public final class CRFClassifier {

    private static final Logger LOGGER = Logger.getLogger("CRFClassifier");

    private CRFClassifier() {
    }

    /**
     * This method takes a list of unclassified lines and firstLevelCRF them using conditional random fields
     *
     * @param lines unclassified lines to be used by the CRF engine
     * @return the list of classified lines after the CRF execution
     */
    public static List<CRFLine> firstLevelCRF(List<Line> lines) throws CRFClassifierException {

        if (lines == null) {
            throw new IllegalArgumentException("Line is a required attribute.");
        }

        List<CRFLine> crfLines = new ArrayList<>();

        try {
            ProcessBuilder process = getProcessBuilder(getCRFLinesAsString(lines), "/crf/models/firstLevel.crf");
            Process pr = process.start();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()))) {

                String line;
                int index = 0;
                CRFLine crfLine;
                while ((line = in.readLine()) != null) {
                    if (!line.isEmpty()) {
                        String[] columns = line.split("\t");
                        String clazz = columns[columns.length - 1]; // class is the last line
                        crfLine = new CRFLine(lines.get(index++), LineClass.get(clazz));
                        crfLines.add(crfLine);
                    }

                }
                pr.waitFor();

            }
        } catch (InterruptedException | IOException e) {
            LOGGER.error("Problem when classifying the paper lines. ", e);
            throw new CRFClassifierException("An unexpected problem occurred trying to firstLevelCRF the paper lines.", e);
        }

        return crfLines;
    }

    /**
     * This method run the CRF model for the second level layer. It takes a list of classified lines and for each of them
     * it classifies the give words.
     *
     * @param crfLines all the classified CRF lines used to build the CRF words map.
     * @return the map of all classified words by line class (this map will contain only the Line classes that requires a second level)
     */
    public static Map<LineClass, List<CRFWord>> secondLevelCRF(List<CRFLine> crfLines) throws CRFClassifierException {

        Map<LineClass, List<Word>> wordsMapByLineClass = getWordsMapByLineClass(crfLines);

        Map<LineClass, List<CRFWord>> crfWords = new HashMap<>();
        for (LineClass lineClass : wordsMapByLineClass.keySet()) {
            switch (lineClass) {
                case HEADER:
                    crfWords.put(lineClass, classifyHeader(wordsMapByLineClass.get(lineClass)));
                    break;
                case AUTHOR_INFORMATION:
                    crfWords.put(lineClass, classifyAuthorInformation(wordsMapByLineClass.get(lineClass)));
                    break;
                case FOOTNOTE:
                    crfWords.put(lineClass, classifyFootnote(wordsMapByLineClass.get(lineClass)));
                    break;
            }
        }

        return crfWords;
    }

    /**
     * ============== SUPPORT METHODS =================
     */

    /**
     * This method converts a list of lines into the CRF++ format
     *
     * @param lines the lines to be converted to the CRF++ format
     * @return the string with the set of line matching CRF++ format
     */
    protected static String getCRFLinesAsString(List<Line> lines) {
        StringBuilder crfLines = new StringBuilder();

        for (Line line : lines) {
            crfLines.append(line.toCRF()).append("\n");
        }

        return crfLines.toString();
    }

    /**
     * This method converts a list of WORDS into the CRF++ format for header level
     *
     * @param words the words to be converted to the CRF++ format for the header level
     * @return the string with the set of words matching CRF++ format for the header level
     */
    protected static String getHeaderCRFWordsAsString(List<Word> words) {
        StringBuilder headerCRFWords = new StringBuilder();

        for (Word word : words) {
            headerCRFWords.append(word.toHeaderCRF()).append("\n");
        }

        return headerCRFWords.toString();
    }

    /**
     * This method converts a list of WORDS into the CRF++ format for author information level
     *
     * @param words the words to be converted to the CRF++ format for the author information level
     * @return the string with the set of words matching CRF++ format for the author information level
     */
    protected static String getAuthorInformationCRFWordsAsString(List<Word> words) {
        StringBuilder authorInformationCRFWords = new StringBuilder();

        for (Word word : words) {
            authorInformationCRFWords.append(word.toAuthorInformationCRF()).append("\n");
        }

        return authorInformationCRFWords.toString();
    }

    /**
     * This method converts a list of WORDS into the CRF++ format for the footnote level
     *
     * @param words the words to be converted to the CRF++ format for the footnote level
     * @return the string with the set of words matching CRF++ format for the footnote level
     */
    protected static String getFootnoteCRFWordsAsString(List<Word> words) {
        StringBuilder footnoteCRFWords = new StringBuilder();

        for (Word word : words) {
            footnoteCRFWords.append(word.toFootnoteCRF()).append("\n");
        }

        return footnoteCRFWords.toString();
    }

    private static List<CRFWord> classifyAuthorInformation(List<Word> words) throws CRFClassifierException {
        if (words == null) {
            throw new IllegalArgumentException("Words is a required attribute.");
        }

        return getCRFWords(words, getAuthorInformationCRFWordsAsString(words), "/crf/models/authorInformationSecondLevel.crf");
    }

    private static List<CRFWord> classifyFootnote(List<Word> words) throws CRFClassifierException {
        if (words == null) {
            throw new IllegalArgumentException("Words is a required attribute.");
        }

        return getCRFWords(words, getFootnoteCRFWordsAsString(words), "/crf/models/footnoteSecondLevel.crf");
    }


    private static List<CRFWord> classifyHeader(List<Word> words) throws CRFClassifierException {
        if (words == null) {
            throw new IllegalArgumentException("Words is a required attribute.");
        }

        return getCRFWords(words, getHeaderCRFWordsAsString(words), "/crf/models/headerSecondLevel.crf");
    }

    private static ProcessBuilder getProcessBuilder(String trainFilePath, String modelPath) throws IOException {
        File crfPaperFile = File.createTempFile("temp", Long.toString(System.nanoTime()));
        FileUtils.writeStringToFile(crfPaperFile, trainFilePath);

        return new ProcessBuilder("crf_test", "-m",
                CRFClassifier.class.getResource(modelPath).getFile(),
                crfPaperFile.getAbsolutePath());
    }

    private static Map<LineClass, List<Word>> getWordsMapByLineClass(List<CRFLine> crfLines) {
        Map<LineClass, List<Word>> wordsMapByLineClass = new HashMap<>();
        for (CRFLine crfLine : crfLines) {

            if (wordsMapByLineClass.containsKey(crfLine.getLineClass())) {
                wordsMapByLineClass.get(crfLine.getLineClass()).addAll(crfLine.getLine().getWords());
            } else {
                wordsMapByLineClass.put(crfLine.getLineClass(), crfLine.getLine().getWords());
            }

        }
        return wordsMapByLineClass;
    }

    private static List<CRFWord> getCRFWords(List<Word> words, String paperInCRFFilePath, String model) throws CRFClassifierException {
        List<CRFWord> classifiedWords = new ArrayList<>();

        try {
            ProcessBuilder process = getProcessBuilder(paperInCRFFilePath, model);
            Process pr = process.start();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()))) {

                String line;
                int index = 0;
                CRFWord crfWord;
                while ((line = in.readLine()) != null) {
                    if (!line.isEmpty()) {
                        String[] columns = line.split("\t");
                        String clazz = columns[columns.length - 1]; // class is the last line
                        Word currentWord = words.get(index++);

                        crfWord = new CRFWord(currentWord, WordClass.get(clazz));
                        classifiedWords.add(crfWord);
                    }

                }
                pr.waitFor();

            }
        } catch (InterruptedException | IOException e) {
            LOGGER.error("Problem when classifying the paper lines. ", e);
            throw new CRFClassifierException("An unexpected problem occurred trying to firstLevelCRF the paper lines.", e);
        }
        return classifiedWords;
    }
}
