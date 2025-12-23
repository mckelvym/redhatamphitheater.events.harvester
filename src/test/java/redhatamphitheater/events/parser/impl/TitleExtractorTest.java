package redhatamphitheater.events.parser.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TitleExtractorTest {

    private TitleExtractor extractor;

    @BeforeEach
    void setUp() {
        extractor = new TitleExtractor();
    }

    @Test
    void extractTitle_withH1OutsideNav_returnsTitle() {
        String html = "<html><body>"
                + "<main><h1>Concert Event</h1></main>"
                + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractTitle(doc);

        assertThat(result).isEqualTo("Concert Event");
    }

    @Test
    void extractTitle_withH1Whitespace_returnsTrimmedTitle() {
        String html = "<html><body>"
                + "<main><h1>  Show Title  </h1></main>"
                + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractTitle(doc);

        assertThat(result).isEqualTo("Show Title");
    }

    @Test
    void extractTitle_withH2OutsideNav_returnsTitle() {
        String html = "<html><body>"
                + "<main><h2>Event Title</h2></main>"
                + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractTitle(doc);

        assertThat(result).isEqualTo("Event Title");
    }

    @Test
    void extractTitle_withH3OutsideNav_returnsTitle() {
        String html = "<html><body>"
                + "<main><h3>Show Details</h3></main>"
                + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractTitle(doc);

        assertThat(result).isEqualTo("Show Details");
    }

    @Test
    void extractTitle_withH4OutsideNav_returnsTitle() {
        String html = "<html><body>"
                + "<main><h4>Performance</h4></main>"
                + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractTitle(doc);

        assertThat(result).isEqualTo("Performance");
    }

    @Test
    void extractTitle_ignoresH1InsideNav_usesH2() {
        String html = "<html><body>"
                + "<nav><h1>Navigation Title</h1></nav>"
                + "<main><h2>Page Title</h2></main>"
                + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractTitle(doc);

        assertThat(result).isEqualTo("Page Title");
    }

    @Test
    void extractTitle_ignoresH1InsideHeader_usesH2() {
        String html = "<html><body>"
                + "<header><h1>Header Title</h1></header>"
                + "<main><h2>Main Title</h2></main>"
                + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractTitle(doc);

        assertThat(result).isEqualTo("Main Title");
    }

    @Test
    void extractTitle_ignoresH1InsideFooter_usesH2() {
        String html = "<html><body>"
                + "<main><h2>Main Title</h2></main>"
                + "<footer><h1>Footer Title</h1></footer>"
                + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractTitle(doc);

        assertThat(result).isEqualTo("Main Title");
    }

    @Test
    void extractTitle_ignoresNavClassElements_usesH2() {
        String html = "<html><body>"
                + "<div class=\"navbar\"><h1>Nav Item</h1></div>"
                + "<main><h2>Main Title</h2></main>"
                + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractTitle(doc);

        assertThat(result).isEqualTo("Main Title");
    }

    @Test
    void extractTitle_ignoresHeaderClassElements_usesH2() {
        String html = "<html><body>"
                + "<div class=\"header\"><h1>Header Item</h1></div>"
                + "<main><h2>Main Title</h2></main>"
                + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractTitle(doc);

        assertThat(result).isEqualTo("Main Title");
    }

    @Test
    void extractTitle_ignoresSiteMenuText_usesValidTitle() {
        String html = "<html><body>"
                + "<nav><h1>Site menu</h1></nav>"
                + "<main><h1>Event Title</h1></main>"
                + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractTitle(doc);

        assertThat(result).isEqualTo("Event Title");
    }

    @Test
    void extractTitle_ignoresTooLongTitles_usesClassSelector() {
        String html = "<html><body>"
                + "<main><h1>" + "a".repeat(250) + "</h1></main>"
                + "<div class=\"event-title\">Event Title</div>"
                + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractTitle(doc);

        assertThat(result).isEqualTo("Event Title");
    }

    @Test
    void extractTitle_withClassSelector_returnsTitle() {
        String html = "<html><body>"
                + "<h1>   </h1>"
                + "<div class=\"event-title\">Event Title</div>"
                + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractTitle(doc);

        assertThat(result).isEqualTo("Event Title");
    }

    @Test
    void extractTitle_withTitleClass_returnsTitle() {
        String html = "<html><body>"
                + "<h1>   </h1>"
                + "<span class=\"title\">Show Title</span>"
                + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractTitle(doc);

        assertThat(result).isEqualTo("Show Title");
    }

    @Test
    void extractTitle_withNameClass_returnsTitle() {
        String html = "<html><body>"
                + "<h1>   </h1>"
                + "<div class=\"event-name\">Event Name</div>"
                + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractTitle(doc);

        assertThat(result).isEqualTo("Event Name");
    }

    @Test
    void extractTitle_withPageTitle_returnsCleanedTitle() {
        String html = "<html><head><title>Event Title | Site Name</title></head><body>"
                + "<h1>   </h1>"
                + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractTitle(doc);

        assertThat(result).isEqualTo("Event Title");
    }

    @Test
    void extractTitle_withPageTitleDashSeparator_returnsCleanedTitle() {
        String html = "<html><head><title>Event Title - Site Name</title></head><body>"
                + "<h1>   </h1>"
                + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractTitle(doc);

        assertThat(result).isEqualTo("Event Title");
    }

    @Test
    void extractTitle_withNoElements_returnsDefaultTitle() {
        String html = "<html><body><p>Some content</p></body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractTitle(doc);

        assertThat(result).isEqualTo("Unknown Event");
    }

    @Test
    void extractTitle_withBlankPageTitle_returnsDefaultTitle() {
        String html = "<html><head><title>   </title></head><body>"
                + "<h1>   </h1>"
                + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractTitle(doc);

        assertThat(result).isEqualTo("Unknown Event");
    }

    @Test
    void extractTitle_withEmptyPageTitle_returnsDefaultTitle() {
        String html = "<html><head><title></title></head><body>"
                + "<h1>   </h1>"
                + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractTitle(doc);

        assertThat(result).isEqualTo("Unknown Event");
    }

    @Test
    void extractTitle_withMultipleH1_returnsFirstValidOne() {
        String html = "<html><body>"
                + "<nav><h1>Nav Title</h1></nav>"
                + "<main><h1>Event Title</h1></main>"
                + "<h1>Another Title</h1>"
                + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractTitle(doc);

        assertThat(result).isEqualTo("Event Title");
    }

    @Test
    void extractTitle_withClassSelectorInsideNav_ignored() {
        String html = "<html><body>"
                + "<h1>   </h1>"
                + "<nav><div class=\"event-title\">Nav Title</div></nav>"
                + "<main><div class=\"event-title\">Event Title</div></main>"
                + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractTitle(doc);

        assertThat(result).isEqualTo("Event Title");
    }
}
