package br.ufrgs.artic.parser.model;

/**
 * This class defines the word with rich text information coming from
 * possibly an OCR engine.
 */
public class Word extends Element {

    private final Word previousWord;
    private final Line line;

    public Word(Builder builder) {
        super(builder);

        previousWord = builder.previousWord;
        content = builder.content;
        line = builder.line;
    }

    public Word getPreviousWord() {
        return previousWord;
    }

    public Line getLine() {
        return line;
    }

    public static class Builder extends ElementBuilder {

        //required params
        private final String content;

        //optional params
        private Word previousWord = null;
        private Line line = null;

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
