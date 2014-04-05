package br.ufrgs.artic.parser.model;

/**
 * This abstract class defines the required attributes for an element.
 */
public abstract class Element {

    protected final int index;
    protected String content;
    protected final Alignment alignment;
    protected final String fontFace;
    protected final double fontSize;
    protected final boolean bold;
    protected final boolean italic;
    protected final boolean underline;

    protected Element(ElementBuilder builder) {
        index = builder.index;

        alignment = builder.alignment;
        fontFace = builder.fontFace;
        fontSize = builder.fontSize;
        bold = builder.bold;
        italic = builder.italic;
        underline = builder.underline;
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

    public String getContent() {
        return content;
    }

    public int getIndex() {
        return index;
    }

    public static class ElementBuilder {
        //required params
        protected final int index;

        //optional params with default setting
        protected boolean bold = false;
        protected boolean italic = false;
        protected boolean underline = false;
        protected Alignment alignment = Alignment.LEFT;
        protected String fontFace = "Arial";
        protected double fontSize = 12.00;

        public ElementBuilder(int index) {
            this.index = index;
        }

    }
}
