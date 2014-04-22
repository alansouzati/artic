package br.ufrgs.artic.runner;

import br.ufrgs.artic.crf.CRFClassifier;
import br.ufrgs.artic.crf.model.CRFLine;
import br.ufrgs.artic.crf.model.CRFWord;
import br.ufrgs.artic.crf.model.LineClass;
import br.ufrgs.artic.exceptions.CRFClassifierException;
import br.ufrgs.artic.exceptions.OmniPageParserException;
import br.ufrgs.artic.model.Line;
import br.ufrgs.artic.output.PaperHandler;
import br.ufrgs.artic.output.model.Paper;
import br.ufrgs.artic.output.model.PaperBoundary;
import br.ufrgs.artic.parser.omnipage.OmniPageParser;
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

    public static void main(String[] args) throws OmniPageParserException, CRFClassifierException, IOException {

        File[] papersInXML = new File(args[0]).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getAbsolutePath().endsWith(".xml") || file.getAbsolutePath().endsWith(".xml");
            }
        });

        for (File xml : papersInXML) {

            List<Line> lines = new OmniPageParser(xml.getAbsolutePath()).getLines();

            List<CRFLine> crfLines = CRFClassifier.classifyFirstLevelCRF(lines);

            Map<LineClass, List<CRFWord>> wordsMapByLineClass = CRFClassifier.classifySecondLevelCRF(crfLines);

            Paper paper = new PaperHandler(new PaperBoundary()).getPaper(wordsMapByLineClass);

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
