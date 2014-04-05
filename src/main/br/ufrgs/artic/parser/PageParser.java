package br.ufrgs.artic.parser;

import br.ufrgs.artic.parser.model.Line;

import java.util.List;

/**
 * This interface defines the required contract for any page parser.
 */
public interface PageParser {

    public List<Line> getLines();
}
