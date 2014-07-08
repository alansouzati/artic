package br.ufrgs.artic.exceptions;

/**
 * Checked exception for problems while loading the OmniPage XML file.
 */
public class ParserException extends Exception {

    public ParserException(String message, Exception e) {
        super(message, e);
    }

}
