package br.ufrgs.artic.crf.model;

public enum LineClass {

    HEADER, TITLE, AUTHOR_INFORMATION, BODY, FOOTNOTE, OTHER;

    public static LineClass get(String clazz) {

        if (clazz != null) {
            for (LineClass currentClass : LineClass.values()) {
                if (currentClass.toString().equalsIgnoreCase(clazz)) {
                    return currentClass;
                }
            }
        }

        return LineClass.OTHER;
    }
}
