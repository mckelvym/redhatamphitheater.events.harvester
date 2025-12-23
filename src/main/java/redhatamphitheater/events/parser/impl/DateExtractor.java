package redhatamphitheater.events.parser.impl;

import static redhatamphitheater.events.parser.impl.CssSelectors.DATE_META_EVENT_START;
import static redhatamphitheater.events.parser.impl.HtmlConstants.CONTENT_ATTR;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extracts event date from Red Hat Amphitheater event pages.
 *
 * <p>Handles various date formats and patterns.
 */
public final class DateExtractor {

    private static final Pattern DATE_PATTERN = Pattern.compile(
        "(January|February|March|April|May|June|July|August|September|"
            + "October|November|December)\\s+(\\d{1,2}),?\\s+(\\d{4})",
        Pattern.CASE_INSENSITIVE
    );
    private static final Logger LOG =
        LoggerFactory.getLogger(DateExtractor.class);
    // Pattern for abbreviated month WITHOUT year: "Jan 15" (assumes current year)
    private static final Pattern SHORT_DATE_PATTERN = Pattern.compile(
        "(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s+(\\d{1,2})(?!,?\\s*\\d{4})",
        Pattern.CASE_INSENSITIVE
    );
    // Pattern for abbreviated month WITH year: "Jan 15, 2025" or "Jan 15 2025"
    private static final Pattern SHORT_DATE_WITH_YEAR_PATTERN = Pattern.compile(
        "(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s+(\\d{1,2}),?\\s+(\\d{4})",
        Pattern.CASE_INSENSITIVE
    );
    private final DateParser dateParser;

    public DateExtractor() {
        this.dateParser = new DateParser();
    }

    /**
     * Extracts the event date from a document.
     *
     * @param doc the parsed HTML document
     * @return the extracted date, or current date if not found
     */
    public LocalDate extractLocalDate(final Document doc) {
        // Strategy 1: Look for date in meta tags
        final Element metaDate = doc.selectFirst(DATE_META_EVENT_START);
        if (metaDate != null) {
            final String content = metaDate.attr(CONTENT_ATTR);
            final LocalDate date = dateParser.parse(content);
            if (date != null) {
                return date;
            }
        }

        // Strategy 2: Look for full month date patterns in text
        final String text = doc.body().text();
        final Matcher matcher = DATE_PATTERN.matcher(text);
        if (matcher.find()) {
            final String dateStr = matcher.group(0);
            final LocalDate date = dateParser.parse(dateStr);
            if (date != null) {
                return date;
            }
        }

        // Strategy 3: Look for abbreviated month WITH year (e.g., "Jan 15, 2025")
        final Matcher shortWithYearMatcher = SHORT_DATE_WITH_YEAR_PATTERN.matcher(text);
        if (shortWithYearMatcher.find()) {
            final String dateStr = shortWithYearMatcher.group(0);
            final LocalDate date = dateParser.parse(dateStr);
            if (date != null) {
                return date;
            }
        }

        // Strategy 4: Look for short date pattern without year (assume current year)
        final Matcher shortMatcher = SHORT_DATE_PATTERN.matcher(text);
        if (shortMatcher.find()) {
            final String dateStr = shortMatcher.group(0) + " "
                + LocalDate.now().getYear();
            final LocalDate date = dateParser.parse(dateStr);
            if (date != null) {
                return date;
            }
        }

        LOG.warn("Could not extract date, using current date");
        return LocalDate.now();
    }
}
