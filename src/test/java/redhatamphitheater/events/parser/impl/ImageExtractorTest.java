package redhatamphitheater.events.parser.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImageExtractorTest {

    private ImageExtractor extractor;

    @BeforeEach
    void setUp() {
        extractor = new ImageExtractor();
    }

    @Test
    void extractImageUrl_withOgImage_returnsContent() {
        String html = "<html><head>"
                + "<meta property=\"og:image\" content=\"https://example.com/og-image.jpg\" />"
                + "</head><body></body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractImageUrl(doc);

        assertThat(result).isEqualTo("https://example.com/og-image.jpg");
    }

    @Test
    void extractImageUrl_withTwitterImage_returnsContent() {
        String html = "<html><head>"
                + "<meta property=\"twitter:image\" content=\"https://example.com/twitter-image.jpg\" />"
                + "</head><body></body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractImageUrl(doc);

        assertThat(result).isEqualTo("https://example.com/twitter-image.jpg");
    }

    @Test
    void extractImageUrl_withEventClass_returnsAbsoluteUrl() {
        String html = "<html><body>"
                + "<img class=\"event-poster\" src=\"/images/poster.jpg\" />"
                + "</body></html>";
        Document doc = Jsoup.parse(html, "https://redhatamphitheater.com");

        String result = extractor.extractImageUrl(doc);

        assertThat(result).isEqualTo("https://redhatamphitheater.com/images/poster.jpg");
    }

    @Test
    void extractImageUrl_withHeroClass_returnsAbsoluteUrl() {
        String html = "<html><body>"
                + "<img class=\"hero-image\" src=\"/images/hero.jpg\" />"
                + "</body></html>";
        Document doc = Jsoup.parse(html, "https://redhatamphitheater.com");

        String result = extractor.extractImageUrl(doc);

        assertThat(result).isEqualTo("https://redhatamphitheater.com/images/hero.jpg");
    }

    @Test
    void extractImageUrl_withLargeImage_returnsUrl() {
        String html = "<html><body>"
                + "<img src=\"/images/large.jpg\" width=\"400\" height=\"300\" />"
                + "</body></html>";
        Document doc = Jsoup.parse(html, "https://redhatamphitheater.com");

        String result = extractor.extractImageUrl(doc);

        assertThat(result).isEqualTo("https://redhatamphitheater.com/images/large.jpg");
    }

    @Test
    void extractImageUrl_withSmallImage_skipsIt() {
        String html = "<html><body>"
                + "<img src=\"/images/small.jpg\" width=\"100\" height=\"100\" />"
                + "<img src=\"/images/large.jpg\" width=\"500\" height=\"400\" />"
                + "</body></html>";
        Document doc = Jsoup.parse(html, "https://redhatamphitheater.com");

        String result = extractor.extractImageUrl(doc);

        assertThat(result).isEqualTo("https://redhatamphitheater.com/images/large.jpg");
    }

    @Test
    void extractImageUrl_withValidImageExtension_returnsUrl() {
        String html = "<html><body>"
                + "<img src=\"https://redhatamphitheater.com/images/photo.jpg\" width=\"500\" />"
                + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractImageUrl(doc);

        // Image must be large enough (width >= 300) to be accepted
        assertThat(result).isEqualTo("https://redhatamphitheater.com/images/photo.jpg");
    }

    @Test
    void extractImageUrl_withInvalidExtension_returnsEmpty() {
        String html = "<html><body>"
                + "<img src=\"/file.pdf\" />"
                + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractImageUrl(doc);

        assertThat(result).isEmpty();
    }

    @Test
    void extractImageUrl_withQueryParams_acceptsUrl() {
        String html = "<html><head>"
                + "<meta property=\"og:image\" content=\"https://example.com/image.jpg?size=large\" />"
                + "</head></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractImageUrl(doc);

        assertThat(result).isEqualTo("https://example.com/image.jpg?size=large");
    }

    @Test
    void extractImageUrl_withMultipleStrategies_prefersOgImage() {
        String html = "<html><head>"
                + "<meta property=\"og:image\" content=\"https://example.com/og.jpg\" />"
                + "<meta property=\"twitter:image\" content=\"https://example.com/twitter.jpg\" />"
                + "</head><body>"
                + "<img class=\"event-image\" src=\"/images/event.jpg\" />"
                + "</body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractImageUrl(doc);

        assertThat(result).isEqualTo("https://example.com/og.jpg");
    }

    @Test
    void extractImageUrl_withNoImages_returnsEmptyString() {
        String html = "<html><body><p>No images</p></body></html>";
        Document doc = Jsoup.parse(html);

        String result = extractor.extractImageUrl(doc);

        assertThat(result).isEmpty();
    }
}
