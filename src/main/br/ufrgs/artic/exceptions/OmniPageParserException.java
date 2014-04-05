package br.ufrgs.artic.exceptions;

/**
 * Checked exception for problems while loading the OmniPage XML file.
 */
public class OmniPageParserException extends Exception {

    public OmniPageParserException(String message, Exception e) {
        super(message, e);
    }

}
