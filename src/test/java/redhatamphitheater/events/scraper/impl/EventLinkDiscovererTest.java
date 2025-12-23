package redhatamphitheater.events.scraper.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventLinkDiscovererTest {

    private static final String BASE_URL = "https://www.redhatamphitheater.com";
    private EventLinkDiscoverer discoverer;

    @Test
    void discoverEventLinks_withAbsoluteUrls_returnsAsIs() {
        String html = "<html><body>"
            + "<h3><a href=\"https://www.redhatamphitheater.com/events/detail/concert/\">Concert</a></h3>"
            + "</body></html>";
        Document doc = Jsoup.parse(html, BASE_URL);

        List<String> result = discoverer.discoverEventLinks(doc);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo("https://www.redhatamphitheater.com/events/detail/concert/");
    }

    @Test
    void discoverEventLinks_withEmptyDocument_returnsEmptyList() {
        String html = "<html><body></body></html>";
        Document doc = Jsoup.parse(html);

        List<String> result = discoverer.discoverEventLinks(doc);

        assertThat(result).isEmpty();
    }

    @Test
    void discoverEventLinks_withEventUrlsContainingDates_capturesThem() {
        String html = "<html><body>"
            + "<h3><a href=\"/events/detail/concert-2024-12-15/\">Concert</a></h3>"
            + "</body></html>";
        Document doc = Jsoup.parse(html, BASE_URL);

        List<String> result = discoverer.discoverEventLinks(doc);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).contains("concert-2024-12-15");
    }

    @Test
    void discoverEventLinks_withInvalidPaths_filtersThemOut() {
        String html = "<html><body>"
            + "<h3><a href=\"/events/detail/valid/\">Valid</a></h3>"
            + "<h3><a href=\"/venue/info/\">Invalid - venue</a></h3>"
            + "<h3><a href=\"/about/\">Invalid - about</a></h3>"
            + "<h3><a href=\"/events/box-office-info\">Invalid - box office</a></h3>"
            + "<h3><a href=\"/events/seating-chart\">Invalid - seating chart</a></h3>"
            + "</body></html>";
        Document doc = Jsoup.parse(html, BASE_URL);

        List<String> result = discoverer.discoverEventLinks(doc);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).contains("valid");
    }

    @Test
    void discoverEventLinks_withLinksNotInH3_ignoresThem() {
        String html = "<html><body>"
            + "<h3><a href=\"/events/detail/in-h3/\">In H3</a></h3>"
            + "<div><a href=\"/events/detail/not-in-h3/\">Not in H3</a></div>"
            + "</body></html>";
        Document doc = Jsoup.parse(html, BASE_URL);

        List<String> result = discoverer.discoverEventLinks(doc);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).contains("in-h3");
    }

    @Test
    void discoverEventLinks_withLinksWithoutHref_ignoresThem() {
        String html = "<html><body>"
            + "<h3><a>No href</a></h3>"
            + "<h3><a href=\"/events/detail/with-href/\">With href</a></h3>"
            + "</body></html>";
        Document doc = Jsoup.parse(html, BASE_URL);

        List<String> result = discoverer.discoverEventLinks(doc);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).contains("with-href");
    }

    @Test
    void discoverEventLinks_withMultipleEvents_returnsAllUrls() {
        String html = "<html><body>"
            + "<h3><a href=\"/events/detail/event-one/\">Event 1</a></h3>"
            + "<h3><a href=\"/events/detail/event-two/\">Event 2</a></h3>"
            + "<h3><a href=\"/events/detail/event-three/\">Event 3</a></h3>"
            + "</body></html>";
        Document doc = Jsoup.parse(html, BASE_URL);

        List<String> result = discoverer.discoverEventLinks(doc);

        assertThat(result).hasSize(3);
        assertThat(result).containsExactly(
            BASE_URL + "/events/detail/event-one/",
            BASE_URL + "/events/detail/event-two/",
            BASE_URL + "/events/detail/event-three/"
        );
    }

    @Test
    void discoverEventLinks_withMultipleLinksInSameH3_capturesAll() {
        String html = "<html><body>"
            + "<h3>"
            + "<a href=\"/events/detail/first/\">First</a>"
            + "<a href=\"/events/detail/second/\">Second</a>"
            + "</h3>"
            + "</body></html>";
        Document doc = Jsoup.parse(html, BASE_URL);

        List<String> result = discoverer.discoverEventLinks(doc);

        assertThat(result).hasSize(2);
    }

    @Test
    void discoverEventLinks_withNoEvents_returnsEmptyList() {
        String html = "<html><body>"
            + "<h3><a href=\"/about/\">About</a></h3>"
            + "<h3><a href=\"/contact/\">Contact</a></h3>"
            + "</body></html>";
        Document doc = Jsoup.parse(html, BASE_URL);

        List<String> result = discoverer.discoverEventLinks(doc);

        assertThat(result).isEmpty();
    }

    @Test
    void discoverEventLinks_withRelativeUrls_convertsToAbsolute() {
        String html = "<html><body>"
            + "<h3><a href=\"/events/detail/local-show/\">Local Show</a></h3>"
            + "</body></html>";
        Document doc = Jsoup.parse(html, BASE_URL);

        List<String> result = discoverer.discoverEventLinks(doc);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(BASE_URL + "/events/detail/local-show/");
    }

    @Test
    void discoverEventLinks_withSingleValidEvent_returnsOneUrl() {
        String html = "<html><body>"
            + "<h3><a href=\"/events/detail/concert-night/\">Concert Night</a></h3>"
            + "</body></html>";
        Document doc = Jsoup.parse(html, BASE_URL);

        List<String> result = discoverer.discoverEventLinks(doc);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(BASE_URL + "/events/detail/concert-night/");
    }

    @Test
    void discoverEventLinks_withRepeatedLinks_returnsEachOnceInOrder() {
        String html = "<html><body>"
            + "<h3><a href=\"/events/detail/bleachers\">Bleachers</a></h3>"
            + "<h3><a href=\"/events/detail/steve-lacy\">Steve Lacy</a></h3>"
            + "<h3><a href=\"/events/detail/bleachers\">Bleachers</a></h3>"
            + "</body></html>";
        Document doc = Jsoup.parse(html, BASE_URL);

        List<String> result = discoverer.discoverEventLinks(doc);

        assertThat(result).containsExactly(
            BASE_URL + "/events/detail/bleachers",
            BASE_URL + "/events/detail/steve-lacy");
    }

    @Test
    void discoverEventLinks_withTrailingSlashVariations_acceptsBoth() {
        String html = "<html><body>"
            + "<h3><a href=\"/events/detail/with-slash/\">With slash</a></h3>"
            + "<h3><a href=\"/events/detail/without-slash\">Without slash</a></h3>"
            + "</body></html>";
        Document doc = Jsoup.parse(html, BASE_URL);

        List<String> result = discoverer.discoverEventLinks(doc);

        assertThat(result).hasSize(2);
    }

    @BeforeEach
    void setUp() {
        discoverer = new EventLinkDiscoverer(BASE_URL);
    }
}
