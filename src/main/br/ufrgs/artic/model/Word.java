package br.ufrgs.artic.model;

import static br.ufrgs.artic.utils.CommonUtils.*;

/**
 * This class defines the word with rich text information coming from
 * possibly an OCR engine.
 */
public class Word extends Element {

    private final Word previousWord;
    private final Line line;
    private final int lineIndex;

    public Word(Builder builder) {
        super(builder);

        previousWord = builder.previousWord;
        content = builder.content;
        line = builder.line;
        lineIndex = builder.lineIndex;
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

    public static class Builder extends ElementBuilder {

        //required params
        private final String content;

        //optional params
        private Word previousWord = null;
        private Line line = null;
        private int lineIndex = 0;

        public Builder(int index, String content) {

            super(index);

            if (content == null) {
                throw new IllegalArgumentException("Please provide a content for the word.");
            }

            this.content = content;
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

        public Word build() {
            return new Word(this);
        }
    }
}
