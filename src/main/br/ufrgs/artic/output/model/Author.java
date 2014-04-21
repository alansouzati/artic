package br.ufrgs.artic.output.model;

/**
 * This class represents an author of a given paper
 */
public class Author {

    private final String name;
    private String affiliation;
    private String email;

    public Author(String name) {

        if (name == null) {
            throw new IllegalArgumentException("Author name is a required parameter");
        }

        this.name = name.replaceAll("[,:;0-9\\*\\(\\)\\[\\]\\{\\}]", "").trim();
    }

    public Author affiliation(String affiliation) {

        if (this.affiliation == null && affiliation != null && !affiliation.isEmpty()) {
            this.affiliation = "";
        }

        this.affiliation += affiliation;
        return this;
    }

    public Author email(String email) {
        if (email != null && !email.isEmpty()) {
            this.email = email;
        }

        return this;
    }

    public String getName() {
        return name;
    }
}
