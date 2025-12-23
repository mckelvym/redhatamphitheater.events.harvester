package redhatamphitheater.events.parser.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DescriptionExtractorTest {

    private DescriptionExtractor extractor;

    @Test
    void extractDescription_withClass_returnsText() {
        String html = "<html><body>"
            + "<div class=\"event-description\">Full event details here</div>"
            + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extract(doc);

        assertThat(result).isEqualTo("Full event details here");
    }

    @Test
    void extractDescription_withMeta_returnsContent() {
        String html = "<html><head>"
            + "<meta name=\"description\" content=\"Concert at Red Hat Amphitheater\" />"
            + "</head><body></body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extract(doc);

        assertThat(result).isEqualTo("Concert at Red Hat Amphitheater");
    }

    @Test
    void extractDescription_withOg_returnsContent() {
        String html = "<html><head>"
            + "<meta property=\"og:description\" content=\"Live music event\" />"
            + "</head><body></body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extract(doc);

        assertThat(result).isEqualTo("Live music event");
    }

    @Test
    void extract_withAboutClass_returnsText() {
        String html = "<html><body>"
            + "<div class=\"about-event\">About the event with more details</div>"
            + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extract(doc);

        assertThat(result).isEqualTo("About the event with more details");
    }

    @Test
    void extract_withDetailClass_returnsText() {
        String html = "<html><body>"
            + "<div class=\"event-detail\">Event detail information</div>"
            + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extract(doc);

        assertThat(result).isEqualTo("Event detail information");
    }

    @Test
    void extract_withEventDetailLayout_prefersEventBodyOverPageWrapper() {
        String html = "<html><head><meta name=\"description\" content=\"\" /></head><body>"
            + "<div class=\"event_detail one_sidebar_right\">Skip to content Accessibility Buy "
            + "Tickets Search Red Hat Amphitheater"
            + "<div class=\"event_description expandable\"><div class=\"description_inner\">"
            + "<p>Bleachers will be donating $1 from each ticket sold.</p></div></div></div>"
            + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extract(doc);

        assertThat(result).isEqualTo("Bleachers will be donating $1 from each ticket sold.");
    }

    @Test
    void extract_withLongText_truncatesAt500Chars() {
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 600; i++) {
            longText.append("a");
        }
        String html = "<html><head>"
            + "<meta name=\"description\" content=\"" + longText + "\" />"
            + "</head></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extract(doc);

        assertThat(result).hasSize(503); // 500 + "..."
        assertThat(result).endsWith("...");
    }

    @Test
    void extract_withMultipleStrategies_prefersMeta() {
        String html = "<html><head>"
            + "<meta name=\"description\" content=\"Meta description\" />"
            + "</head><body>"
            + "<div class=\"event-description\">Description class</div>"
            + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extract(doc);

        assertThat(result).isEqualTo("Meta description");
    }

    @Test
    void extract_withNoMatch_returnsEmptyString() {
        String html = "<html><body><span>Some content</span></body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extract(doc);

        assertThat(result).isEmpty();
    }

    @Test
    void extract_withParagraph_returnsText() {
        String html = "<html><body>"
            + "<p>This is a sufficiently long paragraph with event details</p>"
            + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extract(doc);

        assertThat(result).isEqualTo("This is a sufficiently long paragraph with event details");
    }

    @Test
    void extract_withShortParagraph_skipsIt() {
        String html = "<html><body>"
            + "<p>Short</p>"
            + "<p>This is a much longer paragraph with enough content</p>"
            + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extract(doc);

        assertThat(result).isEqualTo("This is a much longer paragraph with enough content");
    }

    @Test
    void extract_withSummaryClass_returnsText() {
        String html = "<html><body>"
            + "<div class=\"event-summary\">Event summary text with enough characters</div>"
            + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extract(doc);

        assertThat(result).isEqualTo("Event summary text with enough characters");
    }

    @BeforeEach
    void setUp() {
        extractor = new DescriptionExtractor();
    }
}
