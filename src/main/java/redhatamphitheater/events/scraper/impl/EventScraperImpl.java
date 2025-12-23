package redhatamphitheater.events.scraper.impl;


import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redhatamphitheater.events.config.ScraperConfiguration;
import redhatamphitheater.events.domain.EventItem;
import redhatamphitheater.events.parser.EventParser;
import redhatamphitheater.events.scraper.EventScraper;
import redhatamphitheater.events.webdriver.PageLoader;

/**
 * Scrapes events using loop-based pagination.
 * Implements the unified 3-phase flow: discover, filter, parse.
 */
public record EventScraperImpl(ScraperConfiguration config, PageLoader pageLoader,
                               EventParser eventParser,
                               EventLinkDiscoverer linkDiscoverer) implements EventScraper {

    private static final Logger LOG = LoggerFactory.getLogger(EventScraperImpl.class);

    public EventScraperImpl {
        requireNonNull(config, "config must not be null");
        requireNonNull(pageLoader, "pageLoader must not be null");
        requireNonNull(eventParser, "eventParser must not be null");
        requireNonNull(linkDiscoverer, "linkDiscoverer must not be null");
    }

    /**
     * Phase 1: Discovers all event URLs from the calendar page.
     *
     * @return list of discovered event URLs
     */
    private List<String> discoverEventUrls() {
        final Document calendarPage = pageLoader.loadPage(config.getEventsUrl());
        final List<String> eventUrls = linkDiscoverer.discoverEventLinks(calendarPage);

        LOG.info("Discovered {} event URLs", eventUrls.size());
        return eventUrls;
    }

    /**
     * Phase 2: Filters event URLs to only those not already in the feed.
     *
     * @param allUrls       list of all discovered URLs
     * @param existingGuids set of GUIDs already in the feed
     * @return filtered list of new URLs
     */
    private List<String> filterNewUrls(final List<String> allUrls,
                                       final Set<String> existingGuids) {
        final List<String> newUrls =
            allUrls.stream().filter(url -> !existingGuids.contains(url)).toList();

        LOG.info("Filtered to {} new events (skipped {})", newUrls.size(),
            allUrls.size() - newUrls.size());
        return newUrls;
    }

    /**
     * Parses a single event and adds it to the collection.
     *
     * @param eventUrl   the URL of the event to parse
     * @param events     the collection to add the parsed event to
     * @param totalCount the total number of events being processed
     */
    private void parseAndAddEvent(final String eventUrl, final List<EventItem> events,
                                  final int totalCount) {
        try {
            final Document eventPage = pageLoader.loadPage(eventUrl);
            final Optional<EventItem> eventOpt = eventParser.parseEvent(eventPage, eventUrl);

            if (eventOpt.isPresent()) {
                final EventItem event = eventOpt.get();
                events.add(event);
                LOG.info("Event {}/{}: {} ({})", events.size(), totalCount, event.title(),
                    event.eventDateStart());
            } else {
                LOG.warn("No event returned for: {}", eventUrl);
            }
        } catch (Exception e) {
            LOG.warn("Failed to parse event {}: {}", eventUrl, e.getMessage());
        }
    }

    /**
     * Phase 3: Parses events from individual event pages.
     *
     * @param eventUrls list of event URLs to parse
     * @return list of successfully parsed events
     */
    private List<EventItem> parseEvents(final List<String> eventUrls) {
        final List<EventItem> events = new ArrayList<>();

        for (final String eventUrl : eventUrls) {
            parseAndAddEvent(eventUrl, events, eventUrls.size());
        }

        LOG.info("Successfully scraped {} new events", events.size());
        return events;
    }

    @Override
    public List<EventItem> scrapeEvents(final Set<String> existingGuids) {
        requireNonNull(existingGuids, "existingGuids must not be null");

        try {
            // Phase 1: Discover all event URLs
            final List<String> allEventLinks = discoverEventUrls();
            LOG.info("Phase 1 complete: Discovered {} event links", allEventLinks.size());

            // Phase 2: Filter out already-processed events
            final List<String> newEventLinks = filterNewUrls(allEventLinks, existingGuids);
            LOG.info("Phase 2 complete: {} new events after filtering", newEventLinks.size());

            // Phase 3: Parse events from individual pages
            List<EventItem> events = parseEvents(newEventLinks);
            LOG.info("Phase 3 complete: Parsed {} events", events.size());

            return events;
        } catch (Exception e) {
            LOG.error("Unable to parse events", e);
            return List.of();
        }
    }
}
