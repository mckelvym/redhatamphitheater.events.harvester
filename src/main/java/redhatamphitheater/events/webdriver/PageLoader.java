package redhatamphitheater.events.webdriver;

import static java.util.Objects.requireNonNull;

import java.time.Duration;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loads web pages using Selenium and converts them to JSoup documents.
 */
public record PageLoader(WebDriver driver, Duration timeout) {

    /**
     * Expected value when document is fully loaded.
     *
     */
    private static final String DOCUMENT_READY_STATE_COMPLETE = "complete";
    /**
     * JavaScript to check document ready state.
     *
     */
    private static final String DOCUMENT_READY_STATE_SCRIPT = "return document.readyState";
    private static final Logger LOG = LoggerFactory.getLogger(PageLoader.class);

    /**
     * Creates a new PageLoader.
     *
     * @param driver  the WebDriver to use
     * @param timeout the page load timeout
     */
    public PageLoader(final WebDriver driver, final Duration timeout) {
        this.driver = requireNonNull(driver);
        this.timeout = requireNonNull(timeout);
    }

    /**
     * Loads a URL and returns its content as a JSoup document.
     *
     * @param url the URL to load
     * @return the parsed JSoup document
     */
    public Document loadPage(final String url) {
        requireNonNull(url, "url must not be null");
        LOG.info("Loading page: {}", url);
        driver.get(url);

        final WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(webDriver ->
            DOCUMENT_READY_STATE_COMPLETE.equals(((JavascriptExecutor) webDriver)
                .executeScript(DOCUMENT_READY_STATE_SCRIPT)));

        final String pageSource = driver.getPageSource();
        return Jsoup.parse(requireNonNull(pageSource), url);
    }
}
