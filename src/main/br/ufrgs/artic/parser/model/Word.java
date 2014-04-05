package br.ufrgs.artic.parser.model;

/**
 * This class defines the word with rich text information coming from
 * possibly an OCR engine.
 */
public class Word extends Element {

    private final Word previousWord;

    public Word(Builder builder) {
        super(builder);

        previousWord = builder.previousWord;
        content = builder.content;
    }

    public Word getPreviousWord() {
        return previousWord;
    }

    public static class Builder extends ElementBuilder {

        //required params
        private final String content;

        //optional params
        private Word previousWord = null;

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
            this.fontFace = fontFace;
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

        public Word build() {
            return new Word(this);
        }
    }
}
