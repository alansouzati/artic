package br.ufrgs.artic.output.model;

/**
 * Configuration class that holds the boundaries for the paper.
 * <p/>
 * TODO: read the boundary from the configuration file
 */
public class PaperBoundary {

    private int horizontalAuthor;
    private int verticalAuthor;
    private int horizontalAffiliation;
    private int verticalAffiliation;
    private int horizontalAuthorAffiliation;
    private int verticalAuthorAffiliation;

    public PaperBoundary() {
        this.horizontalAuthor = 20;
        this.verticalAuthor = 33;

        this.horizontalAffiliation = 17;
        this.verticalAffiliation = 40;

        this.horizontalAuthorAffiliation = 17;
        this.verticalAuthorAffiliation = 70;
    }

    public PaperBoundary(int horizontalAuthor, int verticalAuthor,
                         int horizontalAffiliation, int verticalAffiliation,
                         int horizontalAuthorAffiliation, int verticalAuthorAffiliation) {

        this.horizontalAuthor = (horizontalAuthor > 0) ? horizontalAuthor : 20;
        this.verticalAuthor = (verticalAuthor > 0) ? verticalAuthor : 33;

        this.horizontalAffiliation = (horizontalAffiliation > 0) ? horizontalAffiliation : 17;
        this.verticalAffiliation = (verticalAffiliation > 0) ? verticalAffiliation : 40;

        this.horizontalAuthorAffiliation = (horizontalAuthorAffiliation > 0) ? horizontalAuthorAffiliation : 17;
        this.verticalAuthorAffiliation = (verticalAuthorAffiliation > 0) ? verticalAuthorAffiliation : 70;
    }

    public int getHorizontalAuthor() {
        return horizontalAuthor;
    }

    public int getVerticalAuthor() {
        return verticalAuthor;
    }

    public int getHorizontalAffiliation() {
        return horizontalAffiliation;
    }

    public int getVerticalAffiliation() {
        return verticalAffiliation;
    }

    public int getHorizontalAuthorAffiliation() {
        return horizontalAuthorAffiliation;
    }

    public int getVerticalAuthorAffiliation() {
        return verticalAuthorAffiliation;
    }
}
