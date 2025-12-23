package redhatamphitheater.events.parser.impl;

import static redhatamphitheater.events.parser.impl.CssSelectors.IMAGE_CLASS_PATTERN;
import static redhatamphitheater.events.parser.impl.CssSelectors.IMAGE_META_OG;
import static redhatamphitheater.events.parser.impl.CssSelectors.IMAGE_META_TWITTER;
import static redhatamphitheater.events.parser.impl.CssSelectors.IMAGE_SRC;
import static redhatamphitheater.events.parser.impl.HtmlConstants.CONTENT_ATTR;
import static redhatamphitheater.events.parser.impl.HtmlConstants.EMPTY;
import static redhatamphitheater.events.parser.impl.HtmlConstants.HEIGHT_ATTR;
import static redhatamphitheater.events.parser.impl.HtmlConstants.IMAGE_KEYWORD;
import static redhatamphitheater.events.parser.impl.HtmlConstants.IMAGE_URL_PATTERN;
import static redhatamphitheater.events.parser.impl.HtmlConstants.SRC_ATTR;
import static redhatamphitheater.events.parser.impl.HtmlConstants.WIDTH_ATTR;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Extracts event image URL from Red Hat Amphitheater event pages.
 *
 * <p>Finds the most relevant image for the event.
 */
public final class ImageExtractor {

    /**
     * Extracts the event image URL from a document.
     *
     * @param doc the parsed HTML document
     * @return the extracted image URL, or empty string if not found
     */
    public String extractImageUrl(final Document doc) {
        String imageUrl;

        // Strategy 1: Look for Open Graph image
        imageUrl = tryMetaImageOg(doc);
        if (imageUrl != null) {
            return imageUrl;
        }

        // Strategy 2: Look for Twitter image
        imageUrl = tryMetaImageTwitter(doc);
        if (imageUrl != null) {
            return imageUrl;
        }

        // Strategy 3: Look for images with event-related classes
        imageUrl = tryClassSelectors(doc);
        if (imageUrl != null) {
            return imageUrl;
        }

        // Strategy 4: Look for first large image
        imageUrl = tryFirstLargeImage(doc);
        if (imageUrl != null) {
            return imageUrl;
        }

        return EMPTY;
    }

    private boolean isLargeEnough(final String width, final String height) {
        try {
            final int w = width.isEmpty() ? 0 : Integer.parseInt(width);
            final int h = height.isEmpty() ? 0 : Integer.parseInt(height);
            return w >= 300 || h >= 300;
        } catch (NumberFormatException e) {
            return true; // Assume it's large if we can't parse
        }
    }

    private boolean isValidImageUrl(final String url) {
        return url.matches(IMAGE_URL_PATTERN)
            || url.contains(IMAGE_KEYWORD);
    }

    private String tryClassSelectors(final Document doc) {
        final Elements images = doc.select(IMAGE_CLASS_PATTERN);

        for (Element img : images) {
            final String src = img.absUrl(SRC_ATTR);
            if (!src.isEmpty() && isValidImageUrl(src)) {
                return src;
            }
        }
        return null;
    }

    private String tryFirstLargeImage(final Document doc) {
        final Elements images = doc.select(IMAGE_SRC);

        for (Element img : images) {
            final String src = img.absUrl(SRC_ATTR);
            final String width = img.attr(WIDTH_ATTR);
            final String height = img.attr(HEIGHT_ATTR);

            if (!src.isEmpty() && isValidImageUrl(src)
                && isLargeEnough(width, height)) {
                return src;
            }
        }
        return null;
    }

    private String tryMetaImageOg(final Document doc) {
        final Element meta = doc.selectFirst(IMAGE_META_OG);
        if (meta != null) {
            final String content = meta.attr(CONTENT_ATTR).trim();
            if (!content.isEmpty() && isValidImageUrl(content)) {
                return content;
            }
        }
        return null;
    }

    private String tryMetaImageTwitter(final Document doc) {
        final Element meta = doc.selectFirst(IMAGE_META_TWITTER);
        if (meta != null) {
            final String content = meta.attr(CONTENT_ATTR).trim();
            if (!content.isEmpty() && isValidImageUrl(content)) {
                return content;
            }
        }
        return null;
    }
}
