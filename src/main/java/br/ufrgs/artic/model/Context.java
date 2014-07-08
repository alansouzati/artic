package br.ufrgs.artic.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the words that belongs to the same context.
 */
public class Context {

    private List<Word> words = new ArrayList<>();

    public void addWord(Word word) {
        if (word != null) {
            this.words.add(word);
        }
    }

    public List<Word> getWords() {
        return words;
    }
}
