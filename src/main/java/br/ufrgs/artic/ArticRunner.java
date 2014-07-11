package br.ufrgs.artic;

import br.ufrgs.artic.crf.CRFClassifier;
import br.ufrgs.artic.crf.model.CRFLine;
import br.ufrgs.artic.crf.model.CRFWord;
import br.ufrgs.artic.crf.model.LineClass;
import br.ufrgs.artic.di.ArticInjector;
import br.ufrgs.artic.exceptions.CRFClassifierException;
import br.ufrgs.artic.exceptions.ParserException;
import br.ufrgs.artic.model.Line;
import br.ufrgs.artic.output.PaperHandler;
import br.ufrgs.artic.output.model.Paper;
import br.ufrgs.artic.parser.PageParser;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Main class that will handle the execution workflow for artic project
 */
public class ArticRunner {

    private static final Logger LOGGER = Logger.getLogger("ArticRunner");

    @Inject
    private PageParser pageParser;

    @Inject
    private PaperHandler paperHandler;

    public static void main(String[] args) throws ParserException, CRFClassifierException, IOException {

        String pathToXML = args.length > 0 && args[0] != null && !args[0].isEmpty() ? args[0] : System.getProperty("user.dir");

        File[] papersInXML = {new File(pathToXML)};
        if (!pathToXML.endsWith(".xml")) {
            papersInXML = new File(pathToXML).listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.getAbsolutePath().endsWith(".xml") || file.getAbsolutePath().endsWith(".xml");
                }
            });
        }

        if (papersInXML != null && papersInXML.length > 0) {

            ArticRunner articRunner = getArticRunner(args);

            for (File xml : papersInXML) {

                LOGGER.debug(String.format("Starting generation process for %s", xml.getAbsolutePath()));

                Paper paper = articRunner.getPaper(xml);

                String fileName = xml.getName().split("\\.")[0];

                File jsonOutput = new File(xml.getParent(), fileName + ".json");

                if (!jsonOutput.exists() && !jsonOutput.createNewFile()) {
                    LOGGER.error("Could not create a new file. Check permissions");
                } else if (jsonOutput.exists() && !jsonOutput.delete()) {
                    LOGGER.error("Could not delete existing json file. Check permissions");
                }

                LOGGER.debug(String.format("Done! Writing at %s", jsonOutput.getAbsolutePath()));
                FileUtils.writeStringToFile(jsonOutput, paper.toJSON());

            }
        }


    }

    private static ArticRunner getArticRunner(String[] args) {
        String overrideProperties = args.length == 2 ? args[1] : null;
        Injector injector = Guice.createInjector(new ArticInjector(overrideProperties));
        return injector.getInstance(ArticRunner.class);
    }

    public Paper getPaper(File xml) throws ParserException, CRFClassifierException {
        List<Line> lines = pageParser.getPage(xml.getAbsolutePath()).getLines();

        List<CRFLine> crfLines = CRFClassifier.classifyFirstLevelCRF(lines);

        Map<LineClass, List<CRFWord>> wordsMapByLineClass = CRFClassifier.classifySecondLevelCRF(crfLines);

        return paperHandler.getPaper(wordsMapByLineClass);
    }
}
