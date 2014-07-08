package br.ufrgs.artic.model;

import java.util.List;

/**
 * This class represents a page for a given document
 */
public class Page {

    private final double averageFontSize;
    private final int top;
    private final int left;

    private List<Line> lines;

    /**
     * Defines the required attributes of a page
     *
     * @param averageFontSize the average font size of the given page
     * @param top             the biggest top location where a text was found in the page
     * @param left            the biggest left location where a text was found in the page
     */
    public Page(double averageFontSize, int top, int left) {

        if (averageFontSize == 0) {
            averageFontSize = 12.00;
        }

        this.averageFontSize = averageFontSize;
        this.top = (top / 8) + 1;
        this.left = (left / 8) + 1;
    }

    public double getAverageFontSize() {
        return averageFontSize;
    }

    public int getTop() {
        return top;
    }

    public int getLeft() {
        return left;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }
}
