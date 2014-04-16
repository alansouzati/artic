package br.ufrgs.artic.output;

import br.ufrgs.artic.crf.model.CRFWord;
import br.ufrgs.artic.crf.model.LineClass;
import br.ufrgs.artic.output.model.Paper;

import java.util.List;
import java.util.Map;

/**
 * This class is responsible to handle the post-processing steps required to create a paper instance.
 */
public final class PaperHandler {

    private PaperHandler() {
    }

    /**
     * This method is responsible for the post-processing algorithm that aims to identify the relationship between authors, email and affiliations.
     * Also, it takes the output from the CRF model and structure in a format that can be used by everyone.
     *
     * @param crfClassificationMap the map with the classification results after the execution by the CRF engine.
     * @return the instance of the paper with the logic responsible to find the relationship between authors, emails and affiliation.
     */
    public static Paper getPaper(Map<LineClass, List<CRFWord>> crfClassificationMap) {

        if (crfClassificationMap == null) {
            throw new IllegalArgumentException("The paper in the CRF instance is a required attribute.");
        }

        String title = getWordsAsString(crfClassificationMap.get(LineClass.TITLE));

        Paper paper = new Paper(title);

        return paper;
    }

    private static String getWordsAsString(List<CRFWord> crfWords) {
        StringBuilder wordsBuilder = new StringBuilder();
        int index = 0;
        for (CRFWord crfWord : crfWords) {
            wordsBuilder.append(crfWord.getWord().getContent());
            if (index++ < crfWords.size() - 1) {
                wordsBuilder.append(" ");
            }

        }
        return wordsBuilder.toString();
    }
}
