package pages;

import base.BasePageTest;
import base.Config;
import base.Device;
import base.Language;
import base.NavItem;
import home.HomePage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class DatasetPageTest extends BasePageTest {

    private DatasetPage datasetPage;

    @Override
    protected void navigateToPage() {
        HomePage homePage = new HomePage(driver);
        homePage.setDevice(Device.DESKTOP);
        homePage.open(Config.baseUrl());
        homePage.switchLanguage(Language.EN);
        navBar.clickDesktopNavItem(NavItem.SEARCH);
        SearchPage searchPage = new SearchPage(driver);
        searchPage.waitForPageLoad();
        searchPage.clickFirstResult();
        datasetPage = new DatasetPage(driver);
        datasetPage.waitForPageLoad();
    }

    @Test
    public void testDatasetTitleNotEmpty() {
        assertFalse(datasetPage.getTitle().isEmpty(),
                "Dataset title should not be empty");
    }

    @Test
    public void testBreadcrumbVisible() {
        assertTrue(datasetPage.isBreadcrumbVisible(),
                "Breadcrumb navigation should be visible");
    }

    @Test
    public void testTagsPresent() {
        assertFalse(datasetPage.getTags().isEmpty(), "Dataset should have at least one tag");
    }

    @Test
    public void testPublisherNotEmpty() {
        assertFalse(datasetPage.getPublisher().isEmpty(),
                "Publisher should not be empty");
    }

    @Test
    public void testCreatedDateNotEmpty() {
        assertFalse(datasetPage.getCreatedDate().isEmpty(),
                "Created date should not be empty");
    }

    @Test
    public void testFormatsNotEmpty() {
        assertFalse(datasetPage.getFormats().isEmpty(),
                "Formats should not be empty");
    }

    @Test
    public void testDownloadsIsNumeric() {
        String downloads = datasetPage.getDownloads();
        assertDoesNotThrow(() -> Integer.parseInt(downloads),
                "Downloads count should be a number but was: " + downloads);
    }

    @Test
    public void testExportLinksVisible() {
        assertTrue(datasetPage.isExportRdfVisible(),    "RDF export link should be visible");
        assertTrue(datasetPage.isExportTtlVisible(),    "TTL export link should be visible");
        assertTrue(datasetPage.isExportJsonLdVisible(), "JSON-LD export link should be visible");
    }

    @Test
    public void testFiveTabsPresent() {
        assertEquals(5, datasetPage.getTabCount(),
                "Dataset page should have 5 tabs");
    }

    @Test
    public void testResourcesTabSelectedByDefault() {
        assertTrue(datasetPage.isTabSelected(
                        By.xpath("//div[@role='tab' and contains(.,'Resources')]")),
                "Resources tab should be selected by default");
    }

    @Test
    public void testTabsAreClickable() {
        By additionalTab = By.xpath("//div[@role='tab' and contains(.,'Additional information')]");
        datasetPage.clickTab(additionalTab);
        assertTrue(datasetPage.isTabSelected(additionalTab),
                "Additional information tab should be selected after clicking");
    }

    @Test
    public void testResourceCountIsPositive() {
        assertTrue(datasetPage.getResourceCount() > 0,
                "Dataset should have at least one resource");
    }

    @Test
    public void testFormatBadgeVisible() {
        assertFalse(datasetPage.getResourceFormat().isEmpty(),
                "Resource format badge should not be empty");
    }

    @Test
    public void testPreviewButtonVisible() {
        assertTrue(datasetPage.isPreviewButtonVisible(),
                "Preview button should be visible");
    }

    @Test
    public void testResourceFileIsDownloaded() throws Exception {
        String format = datasetPage.getResourceFormat().toLowerCase();
        Path downloadedFile = downloadResourceWithFallback(format);

        assertTrue(Files.exists(downloadedFile),
                "Downloaded file should exist on disk: " + downloadedFile);
        assertTrue(Files.size(downloadedFile) > 0,
                "Downloaded file should not be empty");
        assertTrue(downloadedFile.getFileName().toString().toLowerCase().endsWith("." + format),
                "Downloaded file extension should match resource format '" + format + "'");
    }

    private Path downloadResourceWithFallback(String format) throws Exception {
        Path dir = getDownloadDirectory();
        datasetPage.clickDownload();
        try {
            return datasetPage.waitForDownloadedFile(dir, format, Duration.ofSeconds(10));
        } catch (TimeoutException e) {
            return datasetPage.downloadResourceTo(dir, format);
        }
    }
}
