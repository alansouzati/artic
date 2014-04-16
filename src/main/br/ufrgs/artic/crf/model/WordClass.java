package br.ufrgs.artic.crf.model;

/**
 * Enum that represents all the possible classes for a given word
 */
public enum WordClass {

    JOURNAL_NAME, JOURNAL_VOLUME, JOURNAL_YEAR, JOURNAL_PAGE, WEBSITE, CONFERENCE_NAME,
    CONFERENCE_YEAR, CONFERENCE_LOCATION, CONFERENCE_DATE, CONFERENCE_NUMBER,
    PUBLISHER, DOI, ISSN, ISBN,
    AFFILIATION, AUTHOR, EMAIL, OTHER;

    public static WordClass get(String clazz) {

        if (clazz != null) {
            for (WordClass currentClass : WordClass.values()) {
                if (currentClass.toString().equalsIgnoreCase(clazz)) {
                    return currentClass;
                }
            }
        }

        return WordClass.OTHER;
    }
}
