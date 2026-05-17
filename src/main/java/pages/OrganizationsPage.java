package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class OrganizationsPage extends BasePage {

    private final By orgCards     = By.cssSelector("section.grid a");
    private final By orgsHeading  = By.xpath("//h3[contains(.,'Organizations')]");
    private final By searchInput  = By.cssSelector("input[type='search'][name='search']");
    private final By searchButton = By.xpath("//button[contains(.,'Search')]");

    public OrganizationsPage(WebDriver driver) {
        super(driver);
    }

    public void waitForPageLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(orgsHeading));
        wait.until(ExpectedConditions.visibilityOfElementLocated(orgCards));
    }

    // --- Org cards ---

    public int getOrgCount() {
        return driver.findElements(orgCards).size();
    }

    public int getHeadingOrgCount() {
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(orgsHeading)).getText();
        return Integer.parseInt(text.trim().split("\\s+")[0]);
    }

    public List<WebElement> getAllOrgCards() {
        return driver.findElements(orgCards);
    }

    public WebElement getOrgCard(int index) {
        return getAllOrgCards().get(index);
    }

    public WebElement getOrgCardInner(int index) {
        return getOrgCard(index).findElement(By.cssSelector("div.bg-white"));
    }

    // --- Hover ---

    public String getBoxShadow(WebElement element) {
        return (String) ((JavascriptExecutor) driver)
                .executeScript("return window.getComputedStyle(arguments[0]).boxShadow;", element);
    }

    public void hoverOver(WebElement element) {
        new Actions(driver).moveToElement(element).perform();
    }

    // --- Search ---

    public boolean isSearchBarVisible() {
        return isElementVisible(searchInput);
    }

    public boolean isSearchButtonVisible() {
        return isElementVisible(searchButton);
    }

    public void searchByButton(String query) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(searchInput));
        input.clear();
        input.sendKeys(query);
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
        waitForResultsToUpdate();
    }

    public void searchByEnter(String query) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(searchInput));
        input.clear();
        input.sendKeys(query);
        input.sendKeys(Keys.ENTER);
        waitForResultsToUpdate();
    }

    private void waitForResultsToUpdate() {
        // Wait for heading count to update
        wait.until(driver -> {
            try {
                String text = driver.findElement(orgsHeading).getText();
                return !text.isEmpty();
            } catch (Exception e) {
                return false;
            }
        });
    }

    // --- Navigation ---

    public String clickOrgAndGetUrl(int index) {
        getOrgCard(index).click();
        wait.until(ExpectedConditions.urlContains("org="));
        return driver.getCurrentUrl();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}