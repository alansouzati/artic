package br.ufrgs.artic.output;

import br.ufrgs.artic.crf.model.CRFWord;
import br.ufrgs.artic.crf.model.LineClass;
import br.ufrgs.artic.output.model.Paper;
import br.ufrgs.artic.output.model.Venue;

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

        Venue headerVenue = getVenue(crfClassificationMap.get(LineClass.HEADER));
        Venue footnoteVenue = getVenue(crfClassificationMap.get(LineClass.FOOTNOTE));

        if (headerVenue != null) {
            paper.addVenue(headerVenue);
        }

        if (footnoteVenue != null) {
            paper.addVenue(footnoteVenue);
        }

        return paper;
    }

    private static Venue getVenue(List<CRFWord> crfWords) {

        if (crfWords == null || crfWords.isEmpty()) {
            return null;
        }

        Venue venue = new Venue();

        StringBuilder nameBuilder = new StringBuilder();
        StringBuilder locationBuilder = new StringBuilder();
        for (CRFWord crfWord : crfWords) {
            String wordContent = crfWord.getWord().getContent().trim();

            switch (crfWord.getWordClass()) {
                case JOURNAL_NAME:
                    nameBuilder.append(wordContent).append(" ");
                    break;
                case CONFERENCE_LOCATION:
                    locationBuilder.append(wordContent).append(" ");
                    break;
                case JOURNAL_VOLUME:
                    venue.volume(wordContent.replaceAll("[^0-9]", ""));
                    break;
                case CONFERENCE_VOLUME:
                    venue.volume(wordContent.replaceAll("[^0-9]", ""));
                    break;
                case JOURNAL_YEAR:
                    venue.year(wordContent.replaceAll("[^0-9]", "").substring(0, 4));
                    break;
                case CONFERENCE_YEAR:
                    venue.year(wordContent.replaceAll("[^0-9]", "").substring(0, 4));
                    break;
                case JOURNAL_PAGE:
                    venue.page(wordContent);
                    break;
                case CONFERENCE_NUMBER:
                    venue.number(wordContent.replaceAll("[^0-9]", ""));
                    break;
                case CONFERENCE_PAGE:
                    venue.page(wordContent);
                    break;
                case CONFERENCE_NAME:
                    nameBuilder.append(wordContent).append(" ");
                    break;
                case CONFERENCE_DATE:
                    venue.date(wordContent.replaceAll("[,|©|;|.]", ""));
                    break;
                case PUBLISHER:
                    venue.publisher(wordContent.replaceAll("[;|\\.|,]", ""));
                    break;
                case ISSN:
                    venue.issn(wordContent.split("/")[0]);
                    break;
                case ISBN:
                    if (wordContent.endsWith(",") || wordContent.endsWith(".")) {
                        wordContent = wordContent.substring(0, wordContent.length() - 1).trim();
                    }
                    venue.isbn(wordContent.split("\\.\\.\\.")[0].split("/\\$")[0]);
                    break;
                case DOI:
                    venue.doi(wordContent);
                    break;
            }

        }
        return venue.name(nameBuilder.toString().trim()).location(locationBuilder.toString().trim());
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
