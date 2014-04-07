package br.ufrgs.artic.crf;

import br.ufrgs.artic.crf.model.CRFLine;
import br.ufrgs.artic.model.Line;

import java.util.List;

/**
 * This is the core class responsible for classifying the given lines using Conditional Random Fields (CRF).
 */
public final class CRFClassifier {

    private CRFClassifier() {
    }

    /**
     * This method takes a list of unclassified lines and classify them using conditional random fields
     *
     * @param lines unclassified lines to be used by the CRF engine
     * @return the list of classified lines after the CRF execution
     */
    public static List<CRFLine> classify(List<Line> lines) {

        if (lines == null) {
            throw new IllegalArgumentException("Line is a required attribute.");
        }

        getCRFLines(lines);

        return null;
    }

    protected static String getCRFLines(List<Line> lines) {
        StringBuilder crfLines = new StringBuilder();

        for (Line line : lines) {
            crfLines.append(line.toCRF()).append("\n");
        }

        return crfLines.toString();
    }
}
