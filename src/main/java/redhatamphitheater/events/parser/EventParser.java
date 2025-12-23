package redhatamphitheater.events.parser;


import java.util.Optional;
import org.jsoup.nodes.Document;
import redhatamphitheater.events.domain.EventItem;

/**
 * Interface for parsing event information from HTML elements.
 */
public interface EventParser {

    /**
     * Parses an event from an HTML document.
     *
     * @param doc      the parsed HTML document
     * @param eventUrl the URL of the event page
     * @return an Optional containing the parsed event item, or empty if parsing fails
     */
    Optional<EventItem> parseEvent(Document doc, String eventUrl);
}
