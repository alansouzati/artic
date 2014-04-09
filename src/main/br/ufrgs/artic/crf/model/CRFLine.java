package br.ufrgs.artic.crf.model;

import br.ufrgs.artic.model.Line;

/**
 * Represents a line after being classified using Conditional Random Fields
 */
public class CRFLine {

    private final LineClass lineClass;
    private final Line line;

    public CRFLine(Line line, LineClass lineClass) {
        if (line == null) {
            throw new IllegalArgumentException("Line is a required attribute.");
        }

        if (lineClass == null) {
            throw new IllegalArgumentException("Line class is a required attribute.");
        }

        this.line = line;
        this.lineClass = lineClass;
    }

    public LineClass getLineClass() {
        return lineClass;
    }

    public Line getLine() {
        return line;
    }

    public String toCRF() {
        StringBuilder crfLineSB = new StringBuilder();
        crfLineSB.append(line.toCRF()).append(" ");
        crfLineSB.append(lineClass.toString());

        return crfLineSB.toString();
    }
}
