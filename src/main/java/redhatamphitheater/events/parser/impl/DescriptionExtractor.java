package redhatamphitheater.events.parser.impl;

import static java.util.Objects.requireNonNull;
import static redhatamphitheater.events.parser.impl.CssSelectors.DESC_CLASS_PATTERN;
import static redhatamphitheater.events.parser.impl.CssSelectors.DESC_EVENT_BODY;
import static redhatamphitheater.events.parser.impl.CssSelectors.DESC_META;
import static redhatamphitheater.events.parser.impl.CssSelectors.DESC_PARAGRAPH;
import static redhatamphitheater.events.parser.impl.HtmlConstants.CONTENT_ATTR;
import static redhatamphitheater.events.parser.impl.HtmlConstants.EMPTY;
import static redhatamphitheater.events.parser.impl.HtmlConstants.TRUNCATION_SUFFIX;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Extracts event description from event pages.
 *
 * <p>Attempts to find the most relevant descriptive text.
 */
public final class DescriptionExtractor {

    private static final int MAX_DESCRIPTION_LENGTH = 500;

    /**
     * Extracts the event description from a document.
     *
     * @param doc the parsed HTML document
     * @return the extracted description, or empty string if not found
     */
    public String extract(final Document doc) {
        requireNonNull(doc);
        String description;

        // Strategy 1: Event detail body (current site layout)
        description = tryEventBody(doc);
        if (description != null) {
            return description;
        }

        // Strategy 2: Look for meta description
        description = tryMetaDescription(doc);
        if (description != null) {
            return description;
        }

        // Strategy 3: Look for description-related elements
        description = tryDescriptionClasses(doc);
        if (description != null) {
            return description;
        }

        // Strategy 4: Look for paragraphs
        description = tryParagraphs(doc);
        if (description != null) {
            return description;
        }

        return EMPTY;
    }

    private String truncate(final String text) {
        if (text.length() <= MAX_DESCRIPTION_LENGTH) {
            return text;
        }
        return text.substring(0, MAX_DESCRIPTION_LENGTH) + TRUNCATION_SUFFIX;
    }

    private String tryDescriptionClasses(final Document doc) {
        final Elements elements = doc.select(DESC_CLASS_PATTERN);

        for (Element element : elements) {
            final String text = element.text().trim();
            if (text.length() > 20) {
                return truncate(text);
            }
        }
        return null;
    }

    private String tryEventBody(final Document doc) {
        final Element body = doc.selectFirst(DESC_EVENT_BODY);
        if (body != null) {
            final String text = body.text().trim();
            if (!text.isEmpty()) {
                return truncate(text);
            }
        }
        return null;
    }

    private String tryMetaDescription(final Document doc) {
        final Element metaDesc = doc.selectFirst(DESC_META);
        if (metaDesc != null) {
            final String content = metaDesc.attr(CONTENT_ATTR).trim();
            if (!content.isEmpty()) {
                return truncate(content);
            }
        }
        return null;
    }

    private String tryParagraphs(final Document doc) {
        final Elements paragraphs = doc.select(DESC_PARAGRAPH);

        for (Element p : paragraphs) {
            final String text = p.text().trim();
            if (text.length() > 50) {
                return truncate(text);
            }
        }
        return null;
    }
}
