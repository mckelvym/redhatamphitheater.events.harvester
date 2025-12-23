package redhatamphitheater.events.parser.impl;

/**
 * Constants for CSS selectors used in HTML parsing.
 *
 * <p>This class centralizes all CSS selector strings used throughout the
 * parser implementation to avoid magic strings and improve maintainability.
 */
public final class CssSelectors {

    // Title selectors
    public static final String TITLE_H1 = "h1";
    public static final String TITLE_H2 = "h2";
    public static final String TITLE_H3 = "h3";
    public static final String TITLE_H4 = "h4";
    public static final String TITLE_CLASS_PATTERN =
            "[class*='title'], [class*='name'], [class*='event']";

    // Description selectors
    public static final String DESC_META =
            "meta[name='description'], meta[property='og:description']";
    public static final String DESC_CLASS_PATTERN =
            "[class*='description'], [class*='summary'], [class*='about'], [class*='detail']";
    public static final String DESC_PARAGRAPH = "p";
    public static final String DESC_EVENT_BODY =
            "div.event_description div.description_inner, div.event_description";

    // Image selectors
    public static final String IMAGE_META_OG = "meta[property='og:image']";
    public static final String IMAGE_META_TWITTER = "meta[property='twitter:image']";
    public static final String IMAGE_CLASS_PATTERN =
            "img[class*='event'], img[class*='hero'], img[class*='featured'], img[class*='main']";
    public static final String IMAGE_SRC = "img[src]";

    // Date selectors
    public static final String DATE_META_EVENT_START =
            "meta[property='event:start_date']";

    // Event link discovery selectors (scraper package)
    public static final String EVENT_LINK_H3 = "h3";
    public static final String EVENT_LINK_ANCHOR = "a[href]";

    private CssSelectors() {
        // Utility class - prevent instantiation
    }
}
