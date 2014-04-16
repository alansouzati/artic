package br.ufrgs.artic.output.model;

import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public class PaperTest {


    @Test
    public void itShouldCreateAValidJSONWhenProvidingAValidPaperInstance() throws IOException, JSONException {

        Paper paper = new Paper("An example of a fake paper");
        paper.addAuthors(getFakeAuthors());
        paper.addAuthor(new Author("Carlos Heuser").affiliation("UFRGS").email("heuser@inf.ufrgs.br"));
        paper.addVenue(new Venue().name("DocEng").publisher("ACM").date("September 14-20").year("2014").location("Fort Collins, USA"));

        String expectedPaperJSON = new String(Files.readAllBytes(Paths.get(getClass().getResource("/output/model/fakePaper.json").getFile())));

        String generatedPaperJSON = paper.toJSON();

        assertEquals(expectedPaperJSON, generatedPaperJSON, false);
    }

    public List<Author> getFakeAuthors() {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author("Alan Souza").affiliation("UFRGS").email("apsouza@inf.ufrgs.br"));
        authors.add(new Author("Viviane Moreira").affiliation("UFRGS").email("viviane@inf.ufrgs.br"));

        return authors;
    }
}
