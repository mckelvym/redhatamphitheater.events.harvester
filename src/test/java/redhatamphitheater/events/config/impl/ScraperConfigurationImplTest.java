package redhatamphitheater.events.config.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for ScraperConfigurationImpl.
 */
class ScraperConfigurationImplTest {

    private ScraperConfigurationImpl config;

    @BeforeEach
    void setUp() {
        config = new ScraperConfigurationImpl();
    }

    @Test
    void testGetBaseUrl() {
        assertThat(config.getBaseUrl())
                .isEqualTo("https://www.redhatamphitheater.com");
    }

    @Test
    void testGetEventsUrl() {
        assertThat(config.getEventsUrl())
                .isEqualTo("https://www.redhatamphitheater.com/events");
    }

    @Test
    void testGetPageLoadTimeout() {
        assertThat(config.getPageLoadTimeout().toSecondsPart())
                .isEqualTo(10);
    }

    @Test
    void testGetUserAgent() {
        assertThat(config.getUserAgent())
                .contains("Mozilla")
                .contains("Chrome");
    }

    @Test
    void testGetRetentionDays() {
        assertThat(config.getRetentionDays())
                .isEqualTo(7);
    }

    @Test
    void testGetFeedTitle() {
        assertThat(config.getFeedTitle())
                .isEqualTo("Red Hat Amphitheater Events");
    }

    @Test
    void testGetFeedDescription() {
        assertThat(config.getFeedDescription())
                .isEqualTo("Events at Red Hat Amphitheater in Raleigh, NC");
    }
}
