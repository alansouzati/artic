package br.ufrgs.artic.exceptions;

/**
 * Checked exception for problems while running the Conditional Random Fields classifier.
 */
public class CRFClassifierException extends Exception {

    public CRFClassifierException(String message, Exception e) {
        super(message, e);
    }

}
