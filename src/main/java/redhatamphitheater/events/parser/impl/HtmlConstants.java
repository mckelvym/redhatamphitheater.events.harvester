package redhatamphitheater.events.parser.impl;

/**
 * Constants for HTML element names, attribute names, and common values.
 *
 * <p>This class centralizes HTML-related string constants used throughout
 * the parser implementation to avoid magic strings and improve maintainability.
 */
public final class HtmlConstants {

    // Empty string constant
    public static final String EMPTY = "";

    // HTML tag names
    public static final String NAV_TAG = "nav";
    public static final String HEADER_TAG = "header";
    public static final String FOOTER_TAG = "footer";
    public static final String DIV_TAG = "div";
    public static final String SPAN_TAG = "span";

    // HTML attribute names
    public static final String CONTENT_ATTR = "content";
    public static final String SRC_ATTR = "src";
    public static final String HREF_ATTR = "href";
    public static final String WIDTH_ATTR = "width";
    public static final String HEIGHT_ATTR = "height";

    // Class name patterns (for contains checks)
    public static final String NAV_CLASS = "nav";
    public static final String MENU_CLASS = "menu";
    public static final String HEADER_CLASS = "header";
    public static final String FOOTER_CLASS = "footer";

    // Regex patterns
    public static final String HEADING_TAG_PATTERN = "h[1-6]";

    // Common text values
    public static final String SITE_MENU_TEXT = "Site menu";
    public static final String UNKNOWN_EVENT = "Unknown Event";
    public static final String TRUNCATION_SUFFIX = "...";

    // URL prefixes
    public static final String HTTP_PREFIX = "http";

    // Title separator patterns (for splitting)
    public static final String PIPE_SEPARATOR_REGEX = "\\|";
    public static final String DASH_SEPARATOR = "-";

    // Image validation
    public static final String IMAGE_KEYWORD = "image";
    public static final String IMAGE_URL_PATTERN =
        "(?i).*\\.(jpg|jpeg|png|gif|webp)(\\?.*)?$";

    private HtmlConstants() {
        // Utility class - prevent instantiation
    }
}
