package br.ufrgs.artic.parser.model;

/**
 * Define the possible alignment values for general elements.
 */
public enum Alignment {

    LEFT, CENTERED, JUSTIFIED, RIGHT;

    public static Alignment get(String alignment) {

        for (Alignment currentAlignment : Alignment.values()) {
            if (currentAlignment.toString().equalsIgnoreCase(alignment.trim())) {
                return currentAlignment;
            }
        }

        return Alignment.LEFT;
    }
}
