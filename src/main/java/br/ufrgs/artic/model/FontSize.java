package br.ufrgs.artic.model;

/**
 * Enum that defines the possible values for a font size (normalized)
 */
public enum FontSize {

    SMALL, NORMAL, MEDIUM, BIG;

    public static FontSize get(double fontSize, double averagePageFontSize) {
        if (fontSize >= averagePageFontSize + (averagePageFontSize * 0.10) && fontSize < averagePageFontSize + (averagePageFontSize * 0.45)) {
            return FontSize.MEDIUM;
        } else if (fontSize >= averagePageFontSize + (averagePageFontSize * 0.45)) {
            return FontSize.BIG;
        } else if (fontSize < averagePageFontSize - (averagePageFontSize * 0.10)) {
            return FontSize.SMALL;
        }

        return FontSize.NORMAL;
    }
}
