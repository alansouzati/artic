package br.ufrgs.artic;

import br.ufrgs.artic.exceptions.ArticRunnerException;
import br.ufrgs.artic.output.model.Paper;
import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertNotNull;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public class ArticRunnerTest {

    @Test
    public void testRunner() throws ArticRunnerException, IOException, JSONException {

        ArticRunner articRunner = new ArticRunner();

        Paper paper = articRunner.getPaper(ArticRunnerTest.class.getResource("/omnipage/acmSample.xml").getFile());
        assertNotNull(paper);

        String expectedPaperJSON = new String(Files.readAllBytes(Paths.get(getClass().getResource("/output/acmSample.json").getFile())));

        assertEquals(expectedPaperJSON, paper.toJSON(), true);
    }
}
