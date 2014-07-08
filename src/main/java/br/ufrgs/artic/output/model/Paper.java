package br.ufrgs.artic.output.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds the model for the output of Artic project
 */
public class Paper {

    private final String title;
    private List<Author> authors;
    private List<Venue> venues;

    private static final Gson parser = new GsonBuilder().setPrettyPrinting().create();

    public Paper(String title) {
        this.title = title;
    }

    public Paper addAuthor(Author author) {
        if (author != null) {
            if (this.authors == null) {
                this.authors = new ArrayList<>();
            }

            authors.add(author);
        }

        return this;
    }

    public Paper addAuthors(List<Author> authors) {
        if (authors != null) {

            if (this.authors == null) {
                this.authors = new ArrayList<>();
            }

            this.authors.addAll(authors);
        }

        return this;
    }

    public Paper addVenue(Venue venue) {
        if (venue != null) {

            if (venues == null) {
                this.venues = new ArrayList<>();
            }

            venues.add(venue);
        }

        return this;
    }

    public String toJSON() {
        return parser.toJson(this);
    }

    public String getTitle() {
        return title;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public List<Venue> getVenues() {
        return venues;
    }
}
