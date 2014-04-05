package br.ufrgs.artic.parser.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class defines the line with rich text information coming from
 * possibly an OCR engine.
 * <p/>
 * A line without words is not considered a line.
 */
public class Line {

    private final int index;
    private final Page page;
    private final String content;
    private final Alignment alignment;
    private final String fontFace;
    private final double fontSize;
    private final boolean bold;
    private final boolean italic;
    private final boolean underline;

    private final Line previousLine;
    private final List<Word> words = new ArrayList<Word>();

    public Line(Builder builder) {
        index = builder.index;
        page = builder.page;

        alignment = builder.alignment;
        fontFace = builder.fontFace;
        fontSize = builder.fontSize;
        bold = builder.bold;
        italic = builder.italic;
        underline = builder.underline;
        previousLine = builder.previousLine;
        words.addAll(builder.words);

        content = buildContent(words);
    }

    private String buildContent(List<Word> words) {
        StringBuilder contentBuffer = new StringBuilder();

        int wordIndex = 0;
        for (Word word : words) {
            contentBuffer.append(word.getContent());

            if (wordIndex < words.size() - 1) {
                contentBuffer.append(" ");
            }

            wordIndex++;
        }

        return contentBuffer.toString();
    }

    public Page getPage() {
        return page;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public String getFontFace() {
        return fontFace;
    }

    public double getFontSize() {
        return fontSize;
    }

    public boolean isBold() {
        return bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public boolean isUnderline() {
        return underline;
    }

    public List<Word> getWords() {
        return words;
    }

    public String getContent() {
        return content;
    }

    public int getIndex() {
        return index;
    }

    public Line getPreviousLine() {
        return previousLine;
    }

    public static class Builder {

        //required params
        private final int index;
        private final Page page;

        //optional params with default setting
        private boolean bold = false;
        private boolean italic = false;
        private boolean underline = false;
        private Alignment alignment = Alignment.LEFT;
        private String fontFace = "Arial";
        private double fontSize = 12.00;
        private Line previousLine = null;
        private List<Word> words = new ArrayList<Word>();

        public Builder(int index, Page page) {

            if (page == null) {
                throw new IllegalArgumentException("Please provide a page for the line.");
            }

            this.index = index;
            this.page = page;
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
            this.fontFace = fontFace;
            return this;
        }

        public Builder fontSize(double fontSize) {
            if (fontSize > 0) {
                this.fontSize = fontSize;
            }
            return this;
        }

        public Builder previousLine(Line previousLine) {
            this.previousLine = previousLine;
            return this;
        }

        public Builder addWord(Word word) {
            if (word != null) {
                this.words.add(word);
            }

            return this;
        }

        public Builder addAllWords(List<Word> words) {
            if (words != null && !words.isEmpty()) {
                this.words.addAll(words);
            }

            return this;
        }

        public Line build() {
            if (words.isEmpty()) {
                throw new IllegalArgumentException("Please provide enough words.");
            }

            return new Line(this);
        }
    }


}
