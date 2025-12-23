package redhatamphitheater.events;

import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import redhatamphitheater.events.config.ScraperConfiguration;
import redhatamphitheater.events.config.impl.ScraperConfigurationImpl;
import redhatamphitheater.events.domain.EventItem;
import redhatamphitheater.events.feed.RssFeedManager;
import redhatamphitheater.events.feed.RssFeedManagerImpl;
import redhatamphitheater.events.parser.EventParser;
import redhatamphitheater.events.parser.impl.EventParserImpl;
import redhatamphitheater.events.scraper.EventScraper;
import redhatamphitheater.events.scraper.impl.EventLinkDiscoverer;
import redhatamphitheater.events.scraper.impl.EventScraperImpl;
import redhatamphitheater.events.webdriver.ChromeDriverManager;
import redhatamphitheater.events.webdriver.PageLoader;
import redhatamphitheater.events.webdriver.WebDriverManager;

/**
 * Main application for scraping and generating RSS feed.
 */
public final class EventsHarvesterApplication {

    private static final String DEFAULT_OUTPUT_FILE = "events.xml";
    private static final Logger LOG = LoggerFactory.getLogger(EventsHarvesterApplication.class);

    private EventsHarvesterApplication() {
        // utility
    }

    private static void configureLogging() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    /**
     * Application entry point.
     *
     * @param args command line arguments (optional output file path)
     */
    public static void main(final String[] args) {
        configureLogging();

        final String outputFilePath = args.length > 0 ? args[0] : DEFAULT_OUTPUT_FILE;

        LOG.info("Starting Red Hat Amphitheater Events Harvester");
        LOG.info("Output file: {}", outputFilePath);

        try {
            // Phase 1: Initialize dependencies
            LOG.info("Phase 1: Initializing dependencies");
            final ScraperConfiguration config = new ScraperConfigurationImpl();
            final RssFeedManager feedManager = new RssFeedManagerImpl(config);

            // Phase 2: Load existing feed
            LOG.info("Loading existing feed");
            final Set<String> existingGuids = feedManager.loadExistingGuids(outputFilePath);
            LOG.info("Found {} existing events", existingGuids.size());

            final List<EventItem> newEvents;
            try (WebDriverManager driverManager = new ChromeDriverManager(config)) {
                final PageLoader pageLoader = new PageLoader(driverManager.getDriver(),
                    config.getPageLoadTimeout());
                final EventLinkDiscoverer linkDiscoverer =
                    new EventLinkDiscoverer(config.getBaseUrl());
                final EventParser eventParser = new EventParserImpl();

                EventScraper scraper = new EventScraperImpl(config, pageLoader, eventParser,
                    linkDiscoverer);

                // Phase 3: Scrape events
                LOG.info("Starting event scraping");
                newEvents = scraper.scrapeEvents(existingGuids);
            }

            // Phase 4: Generate feed
            LOG.info("Generating RSS feed");
            feedManager.generateFeed(outputFilePath, newEvents, outputFilePath);

            LOG.info("Harvesting completed successfully");
            LOG.info("Found {} new events", newEvents.size());
            LOG.info("Total events in feed: {}", existingGuids.size() + newEvents.size());

        } catch (Exception e) {
            LOG.error("Fatal error during harvesting: {}", e.getMessage(), e);
            System.exit(1);
        }
    }
}
