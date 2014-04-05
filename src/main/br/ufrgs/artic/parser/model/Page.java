package br.ufrgs.artic.parser.model;

/**
 * This class represents a page for a given document
 */
public class Page {

    private final double averageFontSize;
    private final int top;
    private final int left;

    /**
     * Defines the required attributes of a page
     *
     * @param averageFontSize the average font size of the given page
     * @param top             the first top location where a text was found in the page
     * @param left            the first left location where a text was found in the page
     */
    public Page(double averageFontSize, int top, int left) {

        if(averageFontSize == 0) {
            averageFontSize = 12.00;
        }

        this.averageFontSize = averageFontSize;
        this.top = top;
        this.left = left;
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
}
