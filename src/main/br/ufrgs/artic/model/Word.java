package br.ufrgs.artic.model;

import org.apache.commons.lang.StringUtils;

import static br.ufrgs.artic.utils.CommonUtils.*;

/**
 * This class defines the word with rich text information coming from
 * possibly an OCR engine.
 */
public class Word extends Element {

    private final Word previousWord;
    private final Line line;
    private final int lineIndex;
    private Context context;
    private int biggestLineSpacing;
    private int smallestLineSpacing;

    public Word(Builder builder) {
        super(builder);

        previousWord = builder.previousWord;
        content = builder.content;
        line = builder.line;
        lineIndex = builder.lineIndex;
        context = builder.context;
    }

    public Word getPreviousWord() {
        return previousWord;
    }

    public Line getLine() {
        return line;
    }

    private boolean isPossibleConference() {
        String text = getContentNoSpace().replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        return "conference".equals(text) || "conf".equals(text);
    }

    private boolean isNumeral() {
        return NUMERAL_PATTERN.matcher(getContentNoSpecialCharacter()).matches();
    }

    private boolean isMonth() {
        return MONTH_LIST.contains(getContentNoSpecialCharacter().toLowerCase());
    }

    private boolean hasSpecialChar() {
        return SPECIAL_CHAR_PATTERN.matcher(getContentNoSpace()).find();
    }

    private boolean isWebsite() {
        return WEBSITE_PATTERN.matcher(getContentNoSpace()).find();
    }

    private String getCharacterSize() {
        String characterSize = "zero";
        int length = getContent().length();
        if (length >= 1 && length < 5) {
            characterSize = "few";
        } else if (length >= 5 && length < 10) {
            characterSize = "medium";
        } else if (length >= 10) {
            characterSize = "many";
        }

        return characterSize;
    }

    private boolean isNumberOnly() {
        return NUMBER_ONLY_PATTERN.matcher(getContentNoSpecialCharacter().toLowerCase()).matches();
    }

    private boolean isCountry() {
        return COUNTRY_LIST.contains(getContentNoSpecialCharacter().toLowerCase());
    }

    private boolean isYear() {
        boolean year = false;
        String wordClean = getContentNoSpace().replaceAll("c?", "").replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        if (isNumberOnly() && wordClean.length() == 4) {
            Integer possibleYear = Integer.valueOf(wordClean);

            if (possibleYear > 1850 && possibleYear <= CURRENT_YEAR) {
                year = true;
            }
        }
        return year;
    }

    private boolean isPossibleEmail() {
        return EMAIL_PATTERN.matcher(getContent().toLowerCase()).find();
    }

    private boolean isPossibleAffiliation() {
        return isPossibleUniversity() || isCountry() || isPossibleDepartment() || isPossibleContinentOrOcean();
    }

    private boolean isPossibleUniversity() {
        return UNIVERSITY_PATTERN.matcher(getContentNoSpecialCharacter().toLowerCase()).find();
    }

    private boolean isPossibleDepartment() {
        return DEPARTMENT_PATTERN.matcher(getContentNoSpecialCharacter().toLowerCase()).find();
    }

    private boolean isPossibleContinentOrOcean() {
        return CONTINENT_OCEANS_PATTERN.matcher(getContentNoSpecialCharacter().toLowerCase()).find();
    }

    private String getFormat() {
        String currentFormat = getFontSize().toString() + isBold() + isItalic() + getFontFace() + alignment;

        String previousFormat = "";
        if (previousWord != null) {
            previousFormat = previousWord.getFontSize().toString() + previousWord.isBold() +
                    previousWord.isItalic() + previousWord.getFontFace() + previousWord.alignment;
        }

        if (currentFormat.equals(previousFormat)) {
            return "same";
        } else {
            return "new";
        }
    }

    private boolean isPossibleEmailApart() {
        String contentTrim = getContentNoSpace().trim();
        return (contentTrim.startsWith("{") || contentTrim.startsWith("[")) &&
                (contentTrim.endsWith(",") || contentTrim.endsWith(";"));
    }

    private boolean isPossibleConferenceName() {
        boolean isConferenceName = false;

        for (String conference : CONFERENCE_LIST) {
            if (conference.equals(getContentNoSpace().replaceAll("[^a-zA-Z]", ""))) {
                isConferenceName = true;
                break;
            }
        }

        return (CONFERENCE_NAME_PATTERN.matcher(getContentNoSpace()).matches() || isConferenceName ||
                (getContentNoSpace().length() <= 5 && StringUtils.isAllUpperCase(getContentNoSpace()))) && !isCountry();
    }

    private boolean isDays() {
        String text = getContentNoSpace();
        for (String month : MONTH_LIST) {
            if (text.contains(month)) {
                text = text.replaceAll(month, "");
                break;
            }

        }
        return DAYS_PATTERN.matcher(text).matches();
    }

    private boolean isISBN() {
        return ISBN_PATTERN.matcher(getContentNoSpace()).matches();
    }

    private boolean isPublisher() {
        return PUBLISHER_PATTERN.matcher(getContentNoSpace().toLowerCase()).find();
    }

    private boolean isISSN() {
        return ISSN_PATTERN.matcher(getContentNoSpace()).matches();
    }

    private boolean isDOI() {
        return DOI_PATTERN.matcher(getContentNoSpace()).matches() || getContentNoSpace().toLowerCase().trim().startsWith("doi:");
    }

    public String toAuthorInformationCRF() {
        StringBuilder authorInformationWordCRF = new StringBuilder();

        authorInformationWordCRF.append(getContentNoSpace().replaceAll("\\n", "").replaceAll(" ", "")).append(" ");
        authorInformationWordCRF.append(index).append(" ");
        authorInformationWordCRF.append(lineIndex).append(" ");
        authorInformationWordCRF.append(getCharacterSize()).append(" ");
        authorInformationWordCRF.append(isPossibleEmail()).append(" ");
        authorInformationWordCRF.append(!isPossibleEmail() && isPossibleAffiliation()).append(" ");
        authorInformationWordCRF.append(isWebsite()).append(" ");
        authorInformationWordCRF.append(getFormat()).append(" ");
        authorInformationWordCRF.append(getFontSize().toString().toLowerCase()).append(" ");
        authorInformationWordCRF.append(isPossibleEmailApart()).append(" ");

        return authorInformationWordCRF.toString();
    }

    public String toHeaderCRF() {
        StringBuilder headerWordCRF = new StringBuilder();

        headerWordCRF.append(getContentNoSpace().replaceAll("\\n", "").replaceAll(" ", "")).append(" ");
        headerWordCRF.append(index).append(" ");
        headerWordCRF.append(lineIndex).append(" ");
        headerWordCRF.append(getCharacterSize()).append(" ");
        headerWordCRF.append(isNumeral()).append(" ");
        headerWordCRF.append(isPossibleConference()).append(" ");
        headerWordCRF.append(isMonth()).append(" ");
        headerWordCRF.append(isNumberOnly()).append(" ");
        headerWordCRF.append(isYear()).append(" ");
        headerWordCRF.append(isCountry()).append(" ");
        headerWordCRF.append(hasSpecialChar()).append(" ");
        headerWordCRF.append(isWebsite()).append(" ");

        return headerWordCRF.toString();
    }

    public String toFootnoteCRF() {
        StringBuilder footnoteWordCRF = new StringBuilder();

        footnoteWordCRF.append(getContentNoSpace()).append(" ");
        footnoteWordCRF.append(index).append(" ");
        footnoteWordCRF.append(lineIndex).append(" ");
        footnoteWordCRF.append(getCharacterSize()).append(" ");
        footnoteWordCRF.append(isMonth()).append(" ");
        footnoteWordCRF.append(isPossibleConferenceName()).append(" ");
        footnoteWordCRF.append(isDays()).append(" ");
        footnoteWordCRF.append(isCountry()).append(" ");
        footnoteWordCRF.append(isYear()).append(" ");
        footnoteWordCRF.append(isWebsite()).append(" ");
        footnoteWordCRF.append(isISBN()).append(" ");
        footnoteWordCRF.append(isPublisher()).append(" ");
        footnoteWordCRF.append(isPossibleEmail()).append(" ");
        footnoteWordCRF.append(isNumberOnly()).append(" ");
        footnoteWordCRF.append(isPossibleAffiliation()).append(" ");
        footnoteWordCRF.append(isISSN()).append(" ");
        footnoteWordCRF.append(isDOI()).append(" ");

        return footnoteWordCRF.toString();
    }

    public int getBiggestLineSpacing() {

        if (biggestLineSpacing == 0) {
            int previousRight = 0;
            for (Word currentWord : context.getWords()) {
                int right = currentWord.getRight();
                if (previousRight > 0) {

                    int left = currentWord.getLeft();

                    long currentDifference = left - previousRight;
                    if (currentDifference > 0 && currentDifference > biggestLineSpacing) {
                        biggestLineSpacing = (int) currentDifference;
                    }
                }

                previousRight = right;
            }
        }
        return biggestLineSpacing;
    }

    public int getSmallestLineSpacing() {
        if (smallestLineSpacing == 9999999) {
            int previousRight = 0;
            for (Word currentWord : context.getWords()) {
                int right = currentWord.getRight();
                if (previousRight > 0) {

                    int left = currentWord.getLeft();

                    long currentDifference = left - previousRight;
                    if (currentDifference > 0 && currentDifference < smallestLineSpacing) {
                        smallestLineSpacing = (int) currentDifference;
                    }
                }

                previousRight = right;
            }
        }
        return smallestLineSpacing;
    }

    public Context getContext() {
        return context;
    }

    public static class Builder extends ElementBuilder {

        //required params
        private final String content;
        private final Context context;

        //optional params
        private Word previousWord = null;
        private Line line = null;
        private int lineIndex = 0;

        public Builder(int index, String content, Context context) {

            super(index);

            if (content == null) {
                throw new IllegalArgumentException("Please provide a content for the word.");
            }

            if (context == null) {
                throw new IllegalArgumentException("Please provide the context of this word.");
            }

            this.content = content;
            this.context = context;
        }

        public Builder bold(boolean bold) {
            this.bold = bold;
            return this;
        }

        public Builder italic(boolean italic) {
            this.italic = italic;
            return this;
        }

        public Builder underline(boolean underline) {
            this.underline = underline;
            return this;
        }

        public Builder alignment(Alignment alignment) {
            this.alignment = alignment;
            return this;
        }

        public Builder fontFace(String fontFace) {
            if (fontFace != null) {
                this.fontFace = fontFace;
            }

            return this;
        }

        public Builder fontSize(FontSize fontSize) {
            if (fontSize != null) {
                this.fontSize = fontSize;
            }
            return this;
        }

        public Builder previousWord(Word previousWord) {
            this.previousWord = previousWord;
            return this;
        }

        public Builder line(Line line) {
            this.line = line;
            return this;
        }

        public Builder lineIndex(int lineIndex) {
            this.lineIndex = lineIndex;
            return this;
        }

        public Builder top(int top) {
            this.top = top;
            return this;
        }

        public Builder left(int left) {
            this.left = left;
            return this;
        }

        public Builder right(int right) {
            this.right = right;
            return this;
        }

        public Builder bottom(int bottom) {
            this.bottom = bottom;
            return this;
        }

        public Word build() {
            Word word = new Word(this);
            word.context.addWord(word);
            return word;

        }
    }
}
