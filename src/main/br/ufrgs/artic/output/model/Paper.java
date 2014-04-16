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
    private final List<Author> authors = new ArrayList<>();
    private final List<Venue> venues = new ArrayList<>();

    private static final Gson parser = new GsonBuilder().setPrettyPrinting().create();

    public Paper(String title) {
        this.title = title;
    }

    public Paper addAuthor(Author author) {
        if (author != null) {
            authors.add(author);
        }

        return this;
    }

    public Paper addAuthors(List<Author> authors) {
        if (authors != null) {
            this.authors.addAll(authors);
        }

        return this;
    }

    public Paper addVenue(Venue venue) {
        if (venue != null) {
            venues.add(venue);
        }

        return this;
    }

    public String toJSON() {
        return parser.toJson(this);
    }
}
