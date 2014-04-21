package br.ufrgs.artic.output;

import br.ufrgs.artic.crf.model.CRFWord;
import br.ufrgs.artic.crf.model.LineClass;
import br.ufrgs.artic.crf.model.WordClass;
import br.ufrgs.artic.model.Word;
import br.ufrgs.artic.output.model.*;
import br.ufrgs.artic.utils.DynamicProgramming;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static br.ufrgs.artic.utils.CommonUtils.SEPARATOR_SIMPLE_PATTERN;

/**
 * This class is responsible to handle the post-processing steps required to create a paper instance.
 */
public final class PaperHandler {

    private PaperBoundary paperBoundary;

    public PaperHandler(PaperBoundary paperBoundary) {
        this.paperBoundary = (paperBoundary == null) ? new PaperBoundary() : paperBoundary;
    }

    /**
     * This method is responsible for the post-processing algorithm that aims to identify the relationship between authors, email and affiliations.
     * Also, it takes the output from the CRF model and structure in a format that can be used by everyone.
     *
     * @param crfClassificationMap the map with the classification results after the execution by the CRF engine.
     * @return the instance of the paper with the logic responsible to find the relationship between authors, emails and affiliation.
     */
    public Paper getPaper(Map<LineClass, List<CRFWord>> crfClassificationMap) {

        if (crfClassificationMap == null) {
            throw new IllegalArgumentException("The paper in the CRF instance is a required attribute.");
        }

        String title = getWordsAsString(crfClassificationMap.get(LineClass.TITLE));

        List<CRFWord> possibleEmailWords = crfClassificationMap.get(LineClass.AUTHOR_INFORMATION);
        possibleEmailWords.addAll(crfClassificationMap.get(LineClass.FOOTNOTE));

        List<String> emails = getEmails(possibleEmailWords);
        List<Author> authors = getAuthors(crfClassificationMap.get(LineClass.AUTHOR_INFORMATION));
        assignEmailsToAuthors(authors, emails);

        Paper paper = new Paper(title);

        if (authors != null && !authors.isEmpty()) {
            paper.addAuthors(authors);
        }

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

    private void assignEmailsToAuthors(List<Author> authors, List<String> emails) {
        if (emails != null && !emails.isEmpty()) {

            for (String email : emails) {

                String prefix = email.split("@")[0];

                if (authors != null) {
                    boolean foundEmail = false;
                    for (Author author : authors) {
                        String[] names = author.getName().split(" ");

                        String stringLeft = prefix.replaceAll("[^a-zA-Z]", "").toLowerCase().trim();
                        String stringRight = author.getName().replaceAll("[^a-zA-Z]", "").toLowerCase().trim();

                        int distance = DynamicProgramming.distance(stringLeft, stringRight);

                        int distanceRelative = (distance * 100) / (stringLeft.length() + stringRight.length());

                        for (String name : names) {
                            stringRight = name.replaceAll("[^a-zA-Z]", "").toLowerCase().trim();
                            int distanceSplit = DynamicProgramming.distance(stringLeft, stringRight);

                            int distanceSplitRelative = (distanceSplit * 100) / (stringLeft.length() + stringRight.length());
                            if (distanceSplitRelative <= 20 || distanceRelative <= 20) {
                                author.email(email.trim().toLowerCase());
                                foundEmail = true;
                                break;
                            }

                        }
                        if (foundEmail) {
                            break;
                        }
                    }
                }

            }
        }

    }

    private List<Author> getAuthors(List<CRFWord> crfWords) {

        List<CRFWord> authorWordList = new ArrayList<>();
        List<CRFWord> affiliationWordList = new ArrayList<>();
        for (CRFWord crfWord : crfWords) {
            if (WordClass.AUTHOR.equals(crfWord.getWordClass())) {
                authorWordList.add(crfWord);
            } else if (WordClass.AFFILIATION.equals(crfWord.getWordClass())) {
                affiliationWordList.add(crfWord);
            }
        }

        TreeMap<Integer, EntityGroup> authorsMap = entityGrouping(authorWordList,
                paperBoundary.getHorizontalAuthor(), paperBoundary.getVerticalAuthor());


        List<Author> authors = getAuthors(authorsMap);

        addAffiliations(affiliationWordList, authorsMap, authors);

        return authors;
    }

    private void addAffiliations(List<CRFWord> affiliationWordList, TreeMap<Integer, EntityGroup> authorsMap, List<Author> authors) {
        TreeMap<Integer, EntityGroup> affiliationsMap = entityGrouping(affiliationWordList,
                paperBoundary.getHorizontalAffiliation(), paperBoundary.getVerticalAffiliation());

        if (affiliationsMap.size() == 1) {
            for (Author author : authors) {
                author.affiliation(affiliationsMap.get(0).getText());
            }
        } else {
            for (Map.Entry<Integer, EntityGroup> currentAuthorEntry : authorsMap.entrySet()) {

                EntityGroup authorEntityGroup = currentAuthorEntry.getValue();
                Integer affiliationIndex = getGroupIndex(affiliationsMap.descendingMap(), authorEntityGroup,
                        paperBoundary.getHorizontalAuthorAffiliation(), paperBoundary.getVerticalAuthorAffiliation(), false);

                if (affiliationIndex != null) {
                    String affiliationText = affiliationsMap.get(affiliationIndex).getText();

                    String authorNames = currentAuthorEntry.getValue().getText();
                    for (Author author : authors) {
                        if (authorNames.contains(author.getName())) {
                            author.affiliation(affiliationText);
                        }
                    }

                }
            }
        }
    }

    private List<Author> getAuthors(TreeMap<Integer, EntityGroup> authorsMap) {
        List<Author> authors = new ArrayList<>();
        for (Map.Entry<Integer, EntityGroup> currentAuthorEntry : authorsMap.entrySet()) {

            StringBuilder authorName = new StringBuilder();
            EntityGroup entityGroup = currentAuthorEntry.getValue();
            for (CRFWord currentName : entityGroup.getCrfWords()) {
                String originalContent = currentName.getWord().getContent().trim();

                if (!originalContent.toLowerCase().equals("and")) {
                    authorName.append(originalContent).append(" ");

                    if (SEPARATOR_SIMPLE_PATTERN.matcher(originalContent).find()) {
                        authors.add(new Author(authorName.toString()));
                        authorName = new StringBuilder();
                    }
                } else {
                    if (authorName.length() != 0) {
                        authors.add(new Author(authorName.toString()));
                        authorName = new StringBuilder();
                    }
                }
            }

            if (authorName.length() != 0) {
                authors.add(new Author(authorName.toString()));
            }

        }
        return authors;
    }

    private TreeMap<Integer, EntityGroup> entityGrouping(List<CRFWord> crfWords, int horizontalBoundary, int verticalBoundary) {
        TreeMap<Integer, EntityGroup> groupMap = new TreeMap<>();

        for (CRFWord crfWord : crfWords) {
            EntityGroup entityGroup = EntityGroup.parse(crfWord);
            Integer index = getGroupIndex(groupMap.descendingMap(), entityGroup, horizontalBoundary, verticalBoundary, false);

            if (index == null) {
                int size = groupMap.size();
                groupMap.put(size, entityGroup);

            } else {
                EntityGroup currentEntity = groupMap.get(index);
                currentEntity.getCrfWords().add(crfWord);

                Word word = crfWord.getWord();

                int right = word.getRight();
                entityGroup.setDimension(currentEntity.getDimension());
                if (right > entityGroup.getDimension().getRight()) {
                    entityGroup.getDimension().setRight(right);
                }

                int bottom = word.getBottom();
                if (bottom > entityGroup.getDimension().getBottom()) {
                    entityGroup.getDimension().setBottom(bottom);
                }

                int left = word.getLeft();
                if (left < entityGroup.getDimension().getLeft()) {
                    entityGroup.getDimension().setLeft(left);
                }

                int top = word.getBottom();
                if (top < entityGroup.getDimension().getTop()) {
                    entityGroup.getDimension().setTop(top);
                }
            }
        }
        return groupMap;
    }

    private Integer getGroupIndex(Map<Integer, EntityGroup> groupMap, EntityGroup entityGroup,
                                  int horizontalBoundary, int verticalBoundary, boolean validateParagraph) {

        if (groupMap != null && !groupMap.isEmpty()) {

            for (Integer currentIndex : groupMap.keySet()) {
                int left = entityGroup.getDimension().getLeft();
                int right = entityGroup.getDimension().getRight();
                int top = entityGroup.getDimension().getTop();
                int bottom = entityGroup.getDimension().getBottom();

                EntityGroup currentEntityGroup = groupMap.get(currentIndex);
                int biggestLineSpacing = currentEntityGroup.getCrfWords().get(0).getWord().getBiggestLineSpacing();
                int smallestLineSpacing = currentEntityGroup.getCrfWords().get(0).getWord().getSmallestLineSpacing();
                EntityDimension currentDimension = currentEntityGroup.getDimension();
                long diff = (top - currentDimension.getTop());
                long topSpacing = Math.round((double) (1000 * diff) / (top + currentDimension.getTop()));
                if (diff < 0) {
                    topSpacing *= -1;
                }
                if (topSpacing <= 13) {
                    if (validateHorizontal(entityGroup, horizontalBoundary, left,
                            currentEntityGroup, currentDimension, biggestLineSpacing, smallestLineSpacing))
                        return currentIndex;
                } else {
                    diff = (bottom - currentDimension.getTop());
                    long verticalSpacingLeft = Math.round((double) (1000 * diff) / (currentDimension.getTop() + bottom));

                    diff = (top - currentDimension.getBottom());
                    long verticalSpacingRight = Math.round((double) (1000 * diff) / (top + currentDimension.getBottom()));

                    if (verticalSpacingLeft < 0) {
                        verticalSpacingLeft *= -1;
                    }

                    if (verticalSpacingRight < 0) {
                        verticalSpacingRight *= -1;
                    }

                    if (verticalSpacingLeft < verticalBoundary || verticalSpacingRight < verticalBoundary) {
                        if (!validateParagraph || validateHorizontal(entityGroup, horizontalBoundary, left, currentEntityGroup, currentDimension, biggestLineSpacing, smallestLineSpacing)) {
                            if ((left + (left / 100) + ((left * 5) / 100) >= currentDimension.getLeft() && right - (right / 100) - ((right * 5) / 100) <= currentDimension.getRight()) ||
                                    (currentDimension.getLeft() >= left - (left / 100) - ((left * 5) / 100) && currentDimension.getRight() <= right + (right / 100) + ((right * 5) / 100))) {
                                return currentIndex;
                            }
                        }

                    }
                }

            }
        }
        return null;
    }

    private boolean validateHorizontal(EntityGroup entityGroup,
                                       int horizontalBoundary, int left,
                                       EntityGroup currentEntityGroup, EntityDimension currentDimension,
                                       int biggestLineSpacing, int smallestLineSpacing) {
        long diff;
        long horizontalSpacing = Math.round((double) (1000 * (left - currentDimension.getRight())) / (currentDimension.getRight() + left));
        diff = horizontalSpacing - horizontalBoundary;
        if (diff < 0) {
            diff *= -1;
        }

        if (horizontalSpacing < 0) {
            horizontalSpacing *= -1;
        }

        long differenceToLineSpacing = 0;
        if (biggestLineSpacing > 0) {
            differenceToLineSpacing = Math.round((double) (100 * (left - currentDimension.getRight())) / biggestLineSpacing);
        }

        long spacingVariation = 0;
        if (biggestLineSpacing > 0) {
            spacingVariation = Math.round((double) (100 * (biggestLineSpacing - smallestLineSpacing)) / (biggestLineSpacing + smallestLineSpacing));
        }

        return ((horizontalSpacing <= horizontalBoundary)
                || ((entityGroup.getText().length() <= 4 || currentEntityGroup.getText().length() <= 4) && diff <= 5))
                && (differenceToLineSpacing <= 95 || spacingVariation < 35);
    }

    private List<String> getEmails(List<CRFWord> possibleEmailWords) {

        List<CRFWord> emailWordList = new ArrayList<>();

        for (CRFWord crfWord : possibleEmailWords) {
            if (WordClass.EMAIL.equals(crfWord.getWordClass())) {
                String content = crfWord.getWord().getContentNoSpecialCharacter().replaceAll("[^a-zA-Z]", "").toLowerCase();
                if (!"email".equals(content) && !"address".equals(content) && !"addresses".equals(content)) {
                    emailWordList.add(crfWord);
                }

            } else if (WordClass.AFFILIATION.equals(crfWord.getWordClass()) && crfWord.getWord().getContent().contains("@")) {
                emailWordList.add(crfWord);
            }
        }

        return getEmailList(emailWordList);
    }

    private List<String> getEmailList(List<CRFWord> emailWordList) {
        List<String> emails = new ArrayList<>();

        String suffix = "";
        for (int i = emailWordList.size() - 1; i >= 0; i--) {
            Word word = emailWordList.get(i).getWord();

            String originalText = word.getContentNoSpace().replaceAll("[{}\\[\\]]", "");

            if (originalText.lastIndexOf(",") == originalText.length() - 1) {
                originalText = originalText.substring(0, originalText.length() - 1);
            }

            String[] possibleEmails = originalText.split("[,\\|]");
            for (int j = possibleEmails.length - 1; j >= 0; j--) {
                String currentEmail = possibleEmails[j];
                String prefix = currentEmail;
                if (currentEmail.contains("@")) {
                    String[] emailSplit = currentEmail.split("@");
                    prefix = emailSplit[0];

                    if (emailSplit.length > 1) {
                        suffix = emailSplit[1].toLowerCase().trim();
                    }

                }


                if (originalText.length() + suffix.length() > 3 && !prefix.isEmpty()) {
                    emails.add(prefix.trim() + "@" + suffix);
                }
            }


        }

        return emails;
    }

    private Venue getVenue(List<CRFWord> crfWords) {

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

    private String getWordsAsString(List<CRFWord> crfWords) {
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
