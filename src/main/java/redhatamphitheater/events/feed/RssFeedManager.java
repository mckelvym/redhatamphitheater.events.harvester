package redhatamphitheater.events.feed;

import java.util.List;
import java.util.Set;
import redhatamphitheater.events.domain.EventItem;

/**
 * Interface for managing RSS feed operations.
 */
public interface RssFeedManager {

    /**
     * Generates an RSS feed with new and existing events.
     *
     * @param filePath         the path to the output RSS feed file
     * @param newEvents        the new events to add
     * @param existingFilePath the path to the existing feed file
     * @throws Exception if generation fails
     */
    void generateFeed(String filePath, List<EventItem> newEvents,
                      String existingFilePath)
        throws Exception;

    /**
     * Loads existing event GUIDs from an RSS file.
     *
     * @param filePath the path to the RSS feed file
     * @return set of existing event GUIDs
     * @throws Exception if loading fails
     */
    Set<String> loadExistingGuids(String filePath)
        throws Exception;
}
