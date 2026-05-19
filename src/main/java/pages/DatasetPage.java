package pages;

import base.BasePage;
import download.DownloadDirectory;
import download.ResourceDownloader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

public class DatasetPage extends BasePage {

    private final By datasetTitle  = By.xpath("//h1//span[@class='text']");
    private final By datasetTags   = By.xpath("//div[contains(@class,'mt-[18px]') and contains(@class,'gap-[10px]')]//span");
    private final By breadcrumbNav = By.cssSelector("nav ul li");

    private final By publisher  = By.xpath("//li[.//span[contains(.,'Publisher:')]]//span[contains(@class,'font-f37saint-light')]");
    private final By createdDate = By.xpath("//li[.//span[contains(.,'Created:')]]//span[contains(@class,'font-f37saint-light')]");
    private final By formats    = By.xpath("//li[.//span[contains(.,'Formats:')]]//span[contains(@class,'font-f37saint-light')]");
    private final By downloads  = By.xpath("//li[.//span[contains(.,'Downloads:')]]//span[contains(@class,'font-f37saint-light')]");
    private final By pageViews  = By.xpath("//li[.//span[contains(.,'Page views:')]]//span[contains(@class,'font-f37saint-light')]");

    private final By exportRdf    = By.xpath("//a[contains(@href,'.rdf')]");
    private final By exportTtl    = By.xpath("//a[contains(@href,'.ttl')]");
    private final By exportJsonLd = By.xpath("//a[contains(@href,'.jsonld')]");

    private final By allTabs      = By.cssSelector("div[role='tab']");
    private final By resourcesTab = By.xpath("//div[@role='tab' and contains(.,'Resources')]");
    private final By additionalTab = By.xpath("//div[@role='tab' and contains(.,'Additional information')]");
    private final By activityTab  = By.xpath("//div[@role='tab' and contains(.,'Activity stream')]");
    private final By contactTab   = By.xpath("//div[@role='tab' and contains(.,'Contact')]");
    private final By apiTab       = By.xpath("//div[@role='tab' and contains(.,'API')]");

    private final By formatBadge    = By.cssSelector("span.border-secondary");
    private final By downloadButton = By.xpath("//div[@role='tabpanel']//a[contains(@href,'download')]");
    private final By previewButton  = By.xpath("//button[contains(.,'Preview')]");

    private final ResourceDownloader downloader;

    public DatasetPage(WebDriver driver) {
        super(driver);
        this.downloader = new ResourceDownloader(driver);
    }

    public void waitForPageLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(datasetTitle));
        wait.until(ExpectedConditions.visibilityOfElementLocated(resourcesTab));
    }

    public String getTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(datasetTitle)).getText().trim();
    }

    public List<WebElement> getTags() {
        return driver.findElements(datasetTags);
    }

    public boolean isBreadcrumbVisible() {
        return isElementVisible(breadcrumbNav);
    }

    public String getPublisher()  { return driver.findElement(publisher).getText().trim(); }
    public String getCreatedDate(){ return driver.findElement(createdDate).getText().trim(); }
    public String getFormats()    { return driver.findElement(formats).getText().trim(); }
    public String getDownloads()  { return driver.findElement(downloads).getText().trim(); }
    public String getPageViews()  { return driver.findElement(pageViews).getText().trim(); }

    public boolean isExportRdfVisible()    { return isElementVisible(exportRdf); }
    public boolean isExportTtlVisible()    { return isElementVisible(exportTtl); }
    public boolean isExportJsonLdVisible() { return isElementVisible(exportJsonLd); }

    public int getTabCount() {
        return driver.findElements(allTabs).size();
    }

    public void clickTab(By tab) {
        wait.until(ExpectedConditions.elementToBeClickable(tab)).click();
    }

    public boolean isTabSelected(By tab) {
        return "true".equals(driver.findElement(tab).getAttribute("aria-selected"));
    }

    public int getResourceCount() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(downloadButton));
        return driver.findElements(downloadButton).size();
    }

    public String getResourceFormat() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(formatBadge)).getText().trim();
    }

    public String getDownloadUrl() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(downloadButton))
                .getAttribute("href");
    }

    public boolean isPreviewButtonVisible() {
        return isElementVisible(previewButton);
    }

    public void clickDownload() {
        wait.until(ExpectedConditions.elementToBeClickable(downloadButton)).click();
    }

    public Path waitForDownloadedFile(Path directory, String extension, Duration timeout) {
        return DownloadDirectory.waitForFile(directory, extension, timeout);
    }

    public Path downloadResourceTo(Path directory, String expectedExtension) throws Exception {
        return downloader.downloadTo(directory, getDownloadUrl(), expectedExtension.toLowerCase());
    }
}
