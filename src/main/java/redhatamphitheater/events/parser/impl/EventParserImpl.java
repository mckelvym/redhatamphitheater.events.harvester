package redhatamphitheater.events.parser.impl;


import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.Optional;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redhatamphitheater.events.domain.EventItem;
import redhatamphitheater.events.parser.EventParser;

/**
 * Parses and extracts event fields.
 */
public final class EventParserImpl implements EventParser {

    private static final Logger LOG =
        LoggerFactory.getLogger(EventParserImpl.class);
    private final DateExtractor dateExtractor;
    private final DescriptionExtractor descriptionExtractor;
    private final ImageExtractor imageExtractor;
    private final TitleExtractor titleExtractor;

    /**
     * Creates a new EventParserImpl.
     */
    public EventParserImpl() {
        this.titleExtractor = new TitleExtractor();
        this.dateExtractor = new DateExtractor();
        this.descriptionExtractor = new DescriptionExtractor();
        this.imageExtractor = new ImageExtractor();
    }

    @Override
    public Optional<EventItem> parseEvent(final Document doc, final String eventUrl) {
        requireNonNull(doc, "doc must not be null");
        requireNonNull(eventUrl, "eventUrl must not be null");
        final String title = titleExtractor.extractTitle(doc);
        final LocalDate date = dateExtractor.extractLocalDate(doc);
        final String description = descriptionExtractor.extract(doc);
        final String imageUrl = imageExtractor.extractImageUrl(doc);

        if (title == null || title.isBlank()) {
            LOG.warn("Skipping event with missing title: {}", eventUrl);
            return Optional.empty();
        }

        final EventItem event = new EventItem(
            eventUrl,  // id - use link as id
            title,
            eventUrl,
            description,
            date,      // eventDateStart
            null,      // eventDateEnd
            imageUrl,
            null       // location
        );

        return Optional.of(event);
    }
}
