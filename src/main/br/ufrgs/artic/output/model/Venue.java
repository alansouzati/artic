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
        this.name = name;
        return this;
    }

    public Venue publisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public Venue date(String date) {
        this.date = date;
        return this;
    }

    public Venue year(String year) {
        this.year = year;
        return this;
    }

    public Venue location(String location) {
        this.location = location;
        return this;
    }

    public Venue page(String page) {
        this.page = page;
        return this;
    }

    public Venue volume(String volume) {
        this.volume = volume;
        return this;
    }

    public Venue number(String number) {
        this.number = number;
        return this;
    }

    public Venue isbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public Venue issn(String issn) {
        this.issn = issn;
        return this;
    }

    public Venue doi(String doi) {
        this.doi = doi;
        return this;
    }
}
