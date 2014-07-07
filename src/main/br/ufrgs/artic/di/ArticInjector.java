package br.ufrgs.artic.di;

import br.ufrgs.artic.output.PaperHandler;
import br.ufrgs.artic.output.model.PaperBoundary;
import br.ufrgs.artic.parser.PageParser;
import br.ufrgs.artic.parser.omnipage.OmniPageParser;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * This class is responsible for loading the project dependencies using google guice.
 */
public class ArticInjector extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    public PageParser providePageParser() {
        return new OmniPageParser();
    }

    @Provides
    @Singleton
    public PaperHandler providePaperHandler() {
        return new PaperHandler(new PaperBoundary());
    }
}
