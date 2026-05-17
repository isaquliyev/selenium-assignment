package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class NewsPage extends BasePage {

    private final By blogWrapper = By.xpath("//div[contains(@class,'blog-articles')]");
    private final By newsCards = By.xpath(
            "//div[contains(@class,'blog-articles')]//div[contains(@class,'group') and contains(@class,'article-')]"
    );

    // Detail page
    private final By detailTitle     = By.xpath("//h1//span[contains(@class,'text') and not(contains(@class,'text-'))]");
    private final By detailDate      = By.xpath("//span[contains(.,'Date:')]/following-sibling::span");
    private final By detailPublisher = By.xpath("//span[contains(.,'Publisher:')]/following-sibling::span");
    private final By detailMoreNews  = By.xpath("//h1[contains(.,'More news')]");
    private final By breadcrumb      = By.xpath("//div[contains(@class,'flex') and contains(@class,'items-center') and contains(@class,'capitalize')]");

    public NewsPage(WebDriver driver) {
        super(driver);
    }

    public void waitForPageLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(blogWrapper));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(newsCards, 0));
    }

    public List<WebElement> getAllCards() {
        return driver.findElements(newsCards);
    }

    public WebElement getCard(int index) {
        return getAllCards().get(index);
    }

    public int getCardCount() {
        return getAllCards().size();
    }

    public String getCardTitle(int index) {
        return getCard(index).findElement(By.xpath(".//h4")).getText().trim();
    }

    public String getCardDate(int index) {
        return getCard(index)
                .findElement(By.xpath(".//span[contains(@class,'text-secondary')]"))
                .getText().trim();
    }

    public WebElement getCardTitleElement(int index) {
        return getCard(index).findElement(By.xpath(".//h4"));
    }

    public String getCardLinkHref(int index) {
        return getCard(index).findElement(By.xpath(".//a[@href]")).getAttribute("href");
    }

    public String getTextColor(WebElement element) {
        return (String) ((JavascriptExecutor) driver)
                .executeScript("return window.getComputedStyle(arguments[0]).color;", element);
    }

    public void hoverOver(WebElement element) {
        new Actions(driver).moveToElement(element).perform();
    }

    public String clickCardAndGetTitle(int index) {
        String cardTitle = getCardTitle(index);
        String href = getCardLinkHref(index);
        ((JavascriptExecutor) driver).executeScript("window.location.href = arguments[0];", href);
        wait.until(ExpectedConditions.visibilityOfElementLocated(detailTitle));
        return cardTitle;
    }

    public String getDetailPageTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(detailTitle)).getText().trim();
    }

    public boolean isDetailDateVisible()      { return isElementVisible(detailDate); }
    public boolean isDetailPublisherVisible() { return isElementVisible(detailPublisher); }
    public boolean isMoreNewsSectionVisible() { return isElementVisible(detailMoreNews); }
    public boolean isBreadcrumbVisible()      { return isElementVisible(breadcrumb); }

    public List<WebElement> getMoreNewsLinks() {
        return driver.findElements(
                By.xpath("//h1[contains(.,'More news')]/following-sibling::div//a")
        );
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}