package br.ufrgs.artic.crf;

import br.ufrgs.artic.crf.model.CRFLine;
import br.ufrgs.artic.crf.model.LineClass;
import br.ufrgs.artic.exceptions.CRFClassifierException;
import br.ufrgs.artic.model.Line;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the core class responsible for classifying the given lines using Conditional Random Fields (CRF).
 */
public final class CRFClassifier {

    private static final Logger LOGGER = Logger.getLogger("CRFClassifier");

    private CRFClassifier() {
    }

    /**
     * This method takes a list of unclassified lines and classify them using conditional random fields
     *
     * @param lines unclassified lines to be used by the CRF engine
     * @return the list of classified lines after the CRF execution
     */
    public static List<CRFLine> classify(List<Line> lines) throws CRFClassifierException {

        if (lines == null) {
            throw new IllegalArgumentException("Line is a required attribute.");
        }

        List<CRFLine> crfLines = new ArrayList<>();

        try {
            File crfPaperFile = File.createTempFile("temp", Long.toString(System.nanoTime()));
            FileUtils.writeStringToFile(crfPaperFile, getCRFLinesAsString(lines));

            ProcessBuilder process = new ProcessBuilder("crf_test", "-m",
                    CRFClassifier.class.getResource("/crf/models/firstLevel.crf").getFile(),
                    crfPaperFile.getAbsolutePath());
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

            } finally {
                if (crfPaperFile.delete()) {
                    LOGGER.error("Could not delete: " + crfPaperFile.getAbsolutePath());
                }
            }
        } catch (InterruptedException | IOException e) {
            LOGGER.error("Problem when classifying the paper lines. ", e);
            throw new CRFClassifierException("An unexpected problem occurred trying to classify the paper lines.", e);
        }

        return crfLines;
    }

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
}
