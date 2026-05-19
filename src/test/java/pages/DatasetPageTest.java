package pages;

import base.BasePageTest;
import base.Device;
import base.Language;
import base.NavItem;
import home.HomePage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DatasetPageTest extends BasePageTest {

    private DatasetPage datasetPage;

    @Override
    protected void navigateToPage() {
        HomePage homePage = new HomePage(driver);
        homePage.setDevice(Device.DESKTOP);
        homePage.open();
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
                        org.openqa.selenium.By.xpath("//div[@role='tab' and contains(.,'Resources')]")),
                "Resources tab should be selected by default");
    }

    @Test
    public void testTabsAreClickable() {
        org.openqa.selenium.By additionalTab =
                org.openqa.selenium.By.xpath("//div[@role='tab' and contains(.,'Additional information')]");
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
    public void testDownloadUrlExtensionMatchesFormat() {
        String format = datasetPage.getResourceFormat().toLowerCase(); // e.g. "csv"
        String downloadUrl = datasetPage.getDownloadUrl();
        String extension = datasetPage.getDownloadUrlExtension(downloadUrl);

        assertEquals(format, extension,
                "Download URL extension '%s' should match format badge '%s'"
                        .formatted(extension, format));
    }

    @Test
    public void testDownloadUrlIsAccessible() throws Exception {
        String downloadUrl = datasetPage.getDownloadUrl();
        int responseCode = datasetPage.getDownloadUrlResponseCode(downloadUrl);

        assertTrue(responseCode == 200 || responseCode == 302,
                "Download URL should return 200 or 302 but got: " + responseCode);
    }

    @Test
    public void testDownloadFileExtensionMatchesFormat() throws Exception {
        String format = datasetPage.getResourceFormat().toLowerCase();
        String downloadUrl = datasetPage.getDownloadUrl();
        String filename = datasetPage.getContentDispositionFilename(downloadUrl);

        if (!filename.isEmpty()) {
            String fileExtension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
            assertEquals(format, fileExtension,
                    "Downloaded filename extension '%s' should match format '%s'"
                            .formatted(fileExtension, format));
        } else {
            assertEquals(format, datasetPage.getDownloadUrlExtension(downloadUrl),
                    "URL extension should match format when Content-Disposition is absent");
        }
    }
}