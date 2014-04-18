package br.ufrgs.artic.output.model;

/**
 * This class holds the venue information for a given paper.
 */
public class Venue {

    private String name;
    private String publisher;
    private String date;
    private String year;
    private String location;
    private String page;
    private String volume;
    private String number;
    private String isbn;
    private String issn;
    private String doi;

    public Venue name(String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }

        return this;
    }

    public Venue publisher(String publisher) {
        if (publisher != null && !publisher.isEmpty()) {
            this.publisher = publisher;
        }

        return this;
    }

    public Venue date(String date) {
        if (date != null && !date.isEmpty()) {
            this.date = date;
        }

        return this;
    }

    public Venue year(String year) {
        if (year != null && !year.isEmpty()) {
            this.year = year;
        }

        return this;
    }

    public Venue location(String location) {
        if (location != null && !location.isEmpty()) {
            this.location = location;
        }

        return this;
    }

    public Venue page(String page) {
        if (page != null && !page.isEmpty()) {
            this.page = page;
        }

        return this;
    }

    public Venue volume(String volume) {
        if (volume != null && !volume.isEmpty()) {
            this.volume = volume;
        }

        return this;
    }

    public Venue number(String number) {
        if (number != null && !number.isEmpty()) {
            this.number = number;
        }

        return this;
    }

    public Venue isbn(String isbn) {
        if (isbn != null && !isbn.isEmpty()) {
            this.isbn = isbn;
        }

        return this;
    }

    public Venue issn(String issn) {
        if (issn != null && !issn.isEmpty()) {
            this.issn = issn;
        }

        return this;
    }

    public Venue doi(String doi) {
        if (doi != null && !doi.isEmpty()) {
            this.doi = doi;
        }

        return this;
    }
}
