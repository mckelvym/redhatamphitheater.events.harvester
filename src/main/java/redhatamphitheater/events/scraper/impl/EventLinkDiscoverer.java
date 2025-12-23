package redhatamphitheater.events.scraper.impl;

import static redhatamphitheater.events.parser.impl.CssSelectors.EVENT_LINK_ANCHOR;
import static redhatamphitheater.events.parser.impl.CssSelectors.EVENT_LINK_H3;
import static redhatamphitheater.events.parser.impl.HtmlConstants.HREF_ATTR;
import static redhatamphitheater.events.parser.impl.HtmlConstants.HTTP_PREFIX;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Discovers event links from the Red Hat Amphitheater calendar page.
 *
 * <p>Extracts and validates event URLs from the HTML structure.
 */
public final class EventLinkDiscoverer {

    private static final Pattern EVENT_URL_PATTERN =
        Pattern.compile("/events/detail/[^/]+/?$");
    private static final Logger LOG =
        LoggerFactory.getLogger(EventLinkDiscoverer.class);
    private final String baseUrl;

    /**
     * Creates a new EventLinkDiscoverer.
     *
     * @param baseUrl the base URL of the website
     */
    public EventLinkDiscoverer(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Discovers all event links from a calendar page.
     *
     * @param doc the parsed calendar page
     * @return list of absolute event URLs
     */
    public List<String> discoverEventLinks(final Document doc) {
        // The listing repeats event links; keep each once, in page order
        final Set<String> eventLinks = new LinkedHashSet<>();

        // Find all h3 elements containing event links
        final Elements headings = doc.select(EVENT_LINK_H3);

        for (Element heading : headings) {
            final Elements links = heading.select(EVENT_LINK_ANCHOR);

            for (Element link : links) {
                final String href = link.attr(HREF_ATTR);

                if (EVENT_URL_PATTERN.matcher(href).find()) {
                    final String absoluteUrl = toAbsoluteUrl(href);
                    eventLinks.add(absoluteUrl);
                }
            }
        }

        LOG.info("Discovered {} event links", eventLinks.size());
        return new ArrayList<>(eventLinks);
    }

    private String toAbsoluteUrl(final String href) {
        if (href.startsWith(HTTP_PREFIX)) {
            return href;
        }
        return baseUrl + href;
    }
}
