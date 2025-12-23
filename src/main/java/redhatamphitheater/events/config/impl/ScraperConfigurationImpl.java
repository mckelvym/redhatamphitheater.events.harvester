package redhatamphitheater.events.config.impl;

import java.time.Duration;
import redhatamphitheater.events.config.ScraperConfiguration;

/**
 * Configuration for scraping Red Hat Amphitheater events.
 *
 * <p>Provides all necessary settings specific to the Red Hat
 * Amphitheater website structure and requirements.
 */
public class ScraperConfigurationImpl implements ScraperConfiguration {

    private static final String BASE_URL = "https://www.redhatamphitheater.com";
    private static final String EVENTS_URL = BASE_URL + "/events";
    private static final String FEED_DESCRIPTION =
            "Events at Red Hat Amphitheater in Raleigh, NC";
    private static final String FEED_TITLE = "Red Hat Amphitheater Events";
    private static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(10);
    private static final int RETENTION_DAYS = 7;
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; "
            + "Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) "
            + "Chrome/120.0.0.0 Safari/537.36";

    @Override
    public String getBaseUrl() {
        return BASE_URL;
    }

    @Override
    public String getEventsUrl() {
        return EVENTS_URL;
    }

    @Override
    public String getFeedDescription() {
        return FEED_DESCRIPTION;
    }

    @Override
    public String getFeedLink() {
        return getBaseUrl();
    }

    @Override
    public String getFeedTitle() {
        return FEED_TITLE;
    }

    @Override
    public Duration getPageLoadTimeout() {
        return PAGE_LOAD_TIMEOUT;
    }

    @Override
    public int getRetentionDays() {
        return RETENTION_DAYS;
    }

    @Override
    public String getUserAgent() {
        return USER_AGENT;
    }
}
