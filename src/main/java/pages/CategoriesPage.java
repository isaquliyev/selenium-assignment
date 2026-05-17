package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class CategoriesPage extends BasePage {

    private final By categoryCards     = By.cssSelector("div.categories a");
    private final By categoriesHeading = By.xpath("//h3[contains(.,'Categories')]");

    public CategoriesPage(WebDriver driver) {
        super(driver);
    }

    public void waitForPageLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(categoriesHeading));
        wait.until(ExpectedConditions.visibilityOfElementLocated(categoryCards));
    }

    public int getCategoryCount() {
        return driver.findElements(categoryCards).size();
    }

    public int getHeadingCategoryCount() {
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(categoriesHeading)).getText();
        return Integer.parseInt(text.trim().split("\\s+")[0]);
    }

    public List<WebElement> getAllCategoryCards() {
        return driver.findElements(categoryCards);
    }

    public WebElement getCategoryCard(int index) {
        return getAllCategoryCards().get(index);
    }

    public String getBackgroundColor(WebElement element) {
        return (String) ((JavascriptExecutor) driver)
                .executeScript("return window.getComputedStyle(arguments[0]).backgroundColor;", element);
    }

    public void hoverOver(WebElement element) {
        new Actions(driver).moveToElement(element).perform();
    }


    public String clickCategoryAndGetUrl(int index) {
        WebElement card = getCategoryCard(index);
        String expectedSlug = card.getAttribute("href");
        card.click();
        wait.until(ExpectedConditions.urlContains("category="));
        return driver.getCurrentUrl();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}