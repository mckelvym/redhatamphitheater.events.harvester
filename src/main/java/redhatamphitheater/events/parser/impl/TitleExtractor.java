package redhatamphitheater.events.parser.impl;

import static redhatamphitheater.events.parser.impl.CssSelectors.TITLE_CLASS_PATTERN;
import static redhatamphitheater.events.parser.impl.CssSelectors.TITLE_H1;
import static redhatamphitheater.events.parser.impl.CssSelectors.TITLE_H2;
import static redhatamphitheater.events.parser.impl.CssSelectors.TITLE_H3;
import static redhatamphitheater.events.parser.impl.CssSelectors.TITLE_H4;
import static redhatamphitheater.events.parser.impl.HtmlConstants.DASH_SEPARATOR;
import static redhatamphitheater.events.parser.impl.HtmlConstants.DIV_TAG;
import static redhatamphitheater.events.parser.impl.HtmlConstants.FOOTER_CLASS;
import static redhatamphitheater.events.parser.impl.HtmlConstants.FOOTER_TAG;
import static redhatamphitheater.events.parser.impl.HtmlConstants.HEADER_CLASS;
import static redhatamphitheater.events.parser.impl.HtmlConstants.HEADER_TAG;
import static redhatamphitheater.events.parser.impl.HtmlConstants.HEADING_TAG_PATTERN;
import static redhatamphitheater.events.parser.impl.HtmlConstants.MENU_CLASS;
import static redhatamphitheater.events.parser.impl.HtmlConstants.NAV_CLASS;
import static redhatamphitheater.events.parser.impl.HtmlConstants.NAV_TAG;
import static redhatamphitheater.events.parser.impl.HtmlConstants.PIPE_SEPARATOR_REGEX;
import static redhatamphitheater.events.parser.impl.HtmlConstants.SITE_MENU_TEXT;
import static redhatamphitheater.events.parser.impl.HtmlConstants.SPAN_TAG;
import static redhatamphitheater.events.parser.impl.HtmlConstants.UNKNOWN_EVENT;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extracts event title from Red Hat Amphitheater event pages.
 *
 * <p>Uses multiple fallback strategies to ensure title extraction.
 */
public final class TitleExtractor {

    private static final Logger LOG =
        LoggerFactory.getLogger(TitleExtractor.class);

    /**
     * Extracts the event title from a document.
     *
     * @param doc the parsed HTML document
     * @return the extracted title, or "Unknown Event" if not found
     */
    public String extractTitle(final Document doc) {
        String title;

        // Strategy 1: Look for h1 heading outside navigation
        title = tryHeadingsOutsideNav(doc, TITLE_H1);
        if (title != null) {
            return title;
        }

        // Strategy 2: Look for h2 heading outside navigation
        title = tryHeadingsOutsideNav(doc, TITLE_H2);
        if (title != null) {
            return title;
        }

        // Strategy 3: Look for h3 heading outside navigation
        title = tryHeadingsOutsideNav(doc, TITLE_H3);
        if (title != null) {
            return title;
        }

        // Strategy 4: Look for h4 heading outside navigation
        title = tryHeadingsOutsideNav(doc, TITLE_H4);
        if (title != null) {
            return title;
        }

        // Strategy 5: Look for elements with title-related classes
        title = tryClassSelectors(doc);
        if (title != null) {
            return title;
        }

        // Strategy 6: Use page title
        title = doc.title();
        if (!title.isEmpty()) {
            // Clean up site name from title
            title = title.split(PIPE_SEPARATOR_REGEX)[0].trim();
            title = title.split(DASH_SEPARATOR)[0].trim();
            return title;
        }

        LOG.warn("Could not extract title");
        return UNKNOWN_EVENT;
    }

    private boolean isInsideNavigation(final Element element) {
        Element current = element.parent();
        while (current != null) {
            final String tagName = current.tagName();
            if (tagName.equals(NAV_TAG) || tagName.equals(HEADER_TAG)
                || tagName.equals(FOOTER_TAG)) {
                return true;
            }
            // Check for common navigation class names
            final String className = current.className().toLowerCase();
            if (className.contains(NAV_CLASS) || className.contains(MENU_CLASS)
                || className.contains(HEADER_CLASS)
                || className.contains(FOOTER_CLASS)) {
                return true;
            }
            current = current.parent();
        }
        return false;
    }

    private String tryClassSelectors(final Document doc) {
        final Elements elements = doc.select(TITLE_CLASS_PATTERN);

        for (Element element : elements) {
            // Skip navigation elements
            if (isInsideNavigation(element)) {
                continue;
            }

            final String tag = element.tagName();
            if (tag.matches(HEADING_TAG_PATTERN) || tag.equals(DIV_TAG)
                || tag.equals(SPAN_TAG)) {
                final String text = element.text().trim();
                if (!text.isEmpty() && !text.equalsIgnoreCase(SITE_MENU_TEXT)
                    && text.length() < 200) {
                    return text;
                }
            }
        }
        return null;
    }

    private String tryHeadingsOutsideNav(final Document doc, final String tag) {
        final Elements headings = doc.select(tag);
        for (Element heading : headings) {
            // Skip if heading is inside nav, header, or footer elements
            if (isInsideNavigation(heading)) {
                continue;
            }

            final String text = heading.text().trim();
            if (!text.isEmpty() && !text.equalsIgnoreCase(SITE_MENU_TEXT)
                && text.length() < 200) {
                return text;
            }
        }
        return null;
    }
}
