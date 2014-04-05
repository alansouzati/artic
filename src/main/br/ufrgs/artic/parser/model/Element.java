package br.ufrgs.artic.parser.model;

/**
 * This abstract class defines the required attributes for an element.
 */
public abstract class Element {

    protected final int index;
    protected String content;
    protected final Alignment alignment;
    protected final String fontFace;
    protected final FontSize fontSize;
    protected final boolean bold;
    protected final boolean italic;
    protected final boolean underline;
    protected final int left;
    protected final int top;

    protected Element(ElementBuilder builder) {
        index = builder.index;

        alignment = builder.alignment;
        fontFace = builder.fontFace;
        fontSize = builder.fontSize;
        bold = builder.bold;
        italic = builder.italic;
        underline = builder.underline;
        left = builder.left;
        top = builder.top;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public String getFontFace() {
        return fontFace;
    }

    public FontSize getFontSize() {
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

    public int getLeft() {
        return left;
    }

    public int getTop() {
        return top;
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
        protected FontSize fontSize = FontSize.NORMAL;
        protected int left = 0;
        protected int top = 0;


        public ElementBuilder(int index) {
            this.index = index;
        }

    }
}
