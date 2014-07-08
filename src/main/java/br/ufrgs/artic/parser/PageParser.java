package br.ufrgs.artic.parser;

import br.ufrgs.artic.exceptions.ParserException;
import br.ufrgs.artic.model.Page;

/**
 * This interface defines the required contract for any page parser.
 */
public interface PageParser {

    public Page getPage(String pathToFile) throws ParserException;
}
