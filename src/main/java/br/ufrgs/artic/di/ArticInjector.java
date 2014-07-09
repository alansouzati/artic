package br.ufrgs.artic.di;

import br.ufrgs.artic.output.PaperHandler;
import br.ufrgs.artic.output.model.PaperBoundary;
import br.ufrgs.artic.parser.PageParser;
import br.ufrgs.artic.parser.omnipage.OmniPageParser;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static br.ufrgs.artic.utils.CommonUtils.getTempFile;

/**
 * This class is responsible for loading the project dependencies using google guice.
 */
public class ArticInjector extends AbstractModule {

    private static final Logger LOGGER = Logger.getLogger("ArticInjector");
    private File overrideProperties;
    private Properties properties = new Properties();

    public ArticInjector(String overrideProperties) {
        if (overrideProperties != null && !overrideProperties.isEmpty()) {
            this.overrideProperties = new File(overrideProperties);
        }
    }

    @Override
    protected void configure() {
        try {

            properties.load(new FileReader(getTempFile("artic.properties")));

            if (overrideProperties == null) {
                overrideProperties = new File(System.getProperty("user.dir"), "artic.properties");
            }

            if (overrideProperties.exists()) {
                properties.load(new FileReader(overrideProperties));
            }

        } catch (IOException e) {
            LOGGER.error("Problem loading properties file.", e);
        }
    }

    @Provides
    @Singleton
    public PageParser providePageParser() {
        String pageParserInstance = properties.getProperty("page.parser.instance");

        try {
            return (PageParser) Class.forName(pageParserInstance).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            LOGGER.error("Could not create an instance of page parser", e);
        }

        return new OmniPageParser();
    }

    @Provides
    @Singleton
    public PaperBoundary providePaperBoundary() {
        int horizontalAuthor = Integer.valueOf(properties.getProperty("author.page.boundary.horizontal"));
        int verticalAuthor = Integer.valueOf(properties.getProperty("author.page.boundary.vertical"));

        int horizontalAffiliation = Integer.valueOf(properties.getProperty("affiliation.page.boundary.horizontal"));
        int verticalAffiliation = Integer.valueOf(properties.getProperty("affiliation.page.boundary.vertical"));

        int horizontalAuthorAffiliation = Integer.valueOf(properties.getProperty("authorAffiliation.page.boundary.horizontal"));
        int verticalAuthorAffiliation = Integer.valueOf(properties.getProperty("authorAffiliation.page.boundary.vertical"));

        return new PaperBoundary(horizontalAuthor, verticalAuthor,
                horizontalAffiliation, verticalAffiliation,
                horizontalAuthorAffiliation, verticalAuthorAffiliation);
    }

    @Provides
    @Singleton
    public PaperHandler providePaperHandler(PaperBoundary pageBoundary) {
        return new PaperHandler(pageBoundary);
    }
}
