package br.ufrgs.artic.crf.model;

import br.ufrgs.artic.model.Word;

/**
 * This class represents a Word that has been classified by the CRF model.
 */
public class CRFWord {

    private final WordClass wordClass;
    private final Word word;

    public CRFWord(Word word, WordClass wordClass) {

        this.word = word;
        this.wordClass = wordClass;
    }

    public WordClass getWordClass() {
        return wordClass;
    }

    public Word getWord() {
        return word;
    }

    public String toHeaderCRF() {
        StringBuilder crfLineSB = new StringBuilder();
        crfLineSB.append(word.toHeaderCRF()).append(" ");
        crfLineSB.append(wordClass.toString());

        return crfLineSB.toString();
    }

    public String toAuthorInformationCRF() {
        StringBuilder crfLineSB = new StringBuilder();
        crfLineSB.append(word.toAuthorInformationCRF()).append(" ");
        crfLineSB.append(wordClass.toString());

        return crfLineSB.toString();
    }

    public String toFootnoteCRF() {
        StringBuilder crfLineSB = new StringBuilder();
        crfLineSB.append(word.toFootnoteCRF()).append(" ");
        crfLineSB.append(wordClass.toString());

        return crfLineSB.toString();
    }
}
