package redhatamphitheater.events.config;

import java.time.Duration;

/**
 * Configuration contract for web scraping operations.
 *
 * <p>Defines all configurable parameters needed by the scraper,
 * including URLs, timeouts, and filtering thresholds.
 */
public interface ScraperConfiguration {

    /**
     * Gets the base URL of the website to scrape.
     *
     * @return the base URL
     */
    String getBaseUrl();

    /**
     * Gets the description of the RSS feed.
     *
     * @return the feed description
     */
    String getFeedDescription();

    /**
     * Gets the RSS feed link.
     *
     * @return the feed link
     */
    String getFeedLink();

    /**
     * Gets the title of the RSS feed.
     *
     * @return the feed title
     */
    String getFeedTitle();

    /**
     * Gets the maximum time to wait for page elements to load.
     *
     * @return the page load timeout
     */
    Duration getPageLoadTimeout();

    /**
     * Gets the number of days to retain events in the feed.
     *
     * <p>Events older than this threshold will be filtered out.
     *
     * @return the retention period in days
     */
    int getRetentionDays();

    /**
     * Gets the user agent string for web requests.
     *
     * @return the user agent
     */
    String getUserAgent();

    /**
     * Gets the starting URL for event discovery.
     *
     * @return the calendar or events listing URL
     */
    String getEventsUrl();
}
