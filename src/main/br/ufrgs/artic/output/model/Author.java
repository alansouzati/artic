package br.ufrgs.artic.output.model;

/**
 * This class represents an author of a given paper
 */
public class Author {

    private final String name;
    private String affiliation;
    private String email;

    public Author(String name) {
        this.name = name;
    }

    public Author affiliation(String affiliation) {
        this.affiliation = affiliation;
        return this;
    }

    public Author email(String email) {
        this.email = email;
        return this;
    }
}
