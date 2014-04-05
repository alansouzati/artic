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
    private final Paragraph paragraph;
    private final List<Word> words = new ArrayList<>();

    public Line(Builder builder) {

        super(builder);

        page = builder.page;
        previousLine = builder.previousLine;
        paragraph = builder.paragraph;
        content = builder.content;
        words.addAll(builder.words);

        if (content == null || content.isEmpty()) {
            content = buildContent(words);
        }
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

    public Paragraph getParagraph() {
        return paragraph;
    }

    public static class Builder extends ElementBuilder {

        //required params
        private final Page page;

        //optional params
        private List<Word> words = new ArrayList<>();
        private Line previousLine = null;
        private Paragraph paragraph = Paragraph.SAME;
        private String content = "";

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

        public Builder top(int top) {
            this.top = top;
            return this;
        }

        public Builder left(int left) {
            this.left = left;
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

        public Builder fontSize(FontSize fontSize) {
            if (fontSize != null) {
                this.fontSize = fontSize;
            }
            return this;
        }

        public Builder previousLine(Line previousLine) {
            this.previousLine = previousLine;
            return this;
        }

        public Builder paragraph(Paragraph paragraph) {
            this.paragraph = paragraph;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
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
            return new Line(this);
        }
    }


}
