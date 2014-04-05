package br.ufrgs.artic.parser.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class defines the line with rich text information coming from
 * possibly an OCR engine.
 * <p/>
 * A line without words is not considered a line.
 */
public class Line extends Element {

    private final Page page;
    private final Line previousLine;
    private final List<Word> words = new ArrayList<Word>();

    public Line(Builder builder) {

        super(builder);

        page = builder.page;
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

    public List<Word> getWords() {
        return words;
    }

    public Line getPreviousLine() {
        return previousLine;
    }

    public static class Builder extends ElementBuilder {

        //required params
        private final Page page;

        //optional params
        private List<Word> words = new ArrayList<Word>();
        private Line previousLine = null;

        public Builder(int index, Page page) {

            super(index);

            if (page == null) {
                throw new IllegalArgumentException("Please provide a page for the line.");
            }

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
