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

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Main class that will handle the execution workflow for artic project
 */
public class ArticRunner {

    @Inject
    private PageParser pageParser;

    @Inject
    private PaperHandler paperHandler;

    private static final Injector injector = Guice.createInjector(new ArticInjector());

    public static void main(String[] args) throws ParserException, CRFClassifierException, IOException {

        File[] papersInXML = new File(args[0]).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getAbsolutePath().endsWith(".xml") || file.getAbsolutePath().endsWith(".xml");
            }
        });

        if (papersInXML != null && papersInXML.length > 0) {

            ArticRunner articRunner = injector.getInstance(ArticRunner.class);

            for (File xml : papersInXML) {

                Paper paper = getPaper(xml, articRunner.pageParser, articRunner.paperHandler);

                String fileName = xml.getName().split("\\.")[0];

                File jsonOutput = new File(xml.getParent(), fileName + ".json");

                if (!jsonOutput.exists() && !jsonOutput.createNewFile()) {
                    System.err.println("Could not create a new file. Check permissions");
                }

                System.out.println("Writing in: " + jsonOutput.getAbsolutePath());
                FileUtils.writeStringToFile(jsonOutput, paper.toJSON());

            }
        }


    }

    private static Paper getPaper(File xml, PageParser pageParser, PaperHandler paperHandler) throws ParserException, CRFClassifierException {
        List<Line> lines = pageParser.getPage(xml.getAbsolutePath()).getLines();

        List<CRFLine> crfLines = CRFClassifier.classifyFirstLevelCRF(lines);

        Map<LineClass, List<CRFWord>> wordsMapByLineClass = CRFClassifier.classifySecondLevelCRF(crfLines);

        return paperHandler.getPaper(wordsMapByLineClass);
    }
}