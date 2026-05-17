package pages;

import base.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchPage extends BasePage {

    // Search
    private final By searchInput       = By.cssSelector("input[name='query']");
    private final By searchButton      = By.cssSelector("button[type='submit']");
    // SearchPage.java
    private final By resultsCount = By.xpath("//h2[.//span[contains(.,'Datasets found')]]");

    // Refine sections
    private final By categorySection = By.xpath(
            "//section[.//h3[contains(.,'Refine by: Category')]]"
    );
    private final By organizationsSection = By.xpath(
            "//section[.//h3[contains(.,'Refine by: Organizations')]]"
    );
    private final By formatSection = By.xpath(
            "//section[.//h3[contains(.,'Refine by: Format')]]"
    );
    private final By tagsSection = By.xpath(
            "//section[.//h3[contains(.,'Refine by tags')]]"
    );

    private final By categoryMoreBtn = By.xpath(
            "//section[.//h3[contains(.,'Refine by: Category')]]//button[@type='button']"
    );
    private final By organizationsMoreBtn = By.xpath(
            "//section[.//h3[contains(.,'Refine by: Organizations')]]//button[@type='button']"
    );
    private final By tagsMoreBtn = By.xpath(
            "//section[.//h3[contains(.,'Refine by tags')]]//button[@type='button']"
    );

    private final By categoryCheckboxes = By.xpath(
            "//section[.//h3[contains(.,'Refine by: Category')]]//div[contains(@class,'cursor-pointer')]"
    );

    // Active filters & clear
    private final By activeFilterTags  = By.cssSelector("div.flex.flex-wrap p");
    private final By clearFiltersBtn   = By.xpath("//button[contains(text(),'Clear Filters')]");

    public SearchPage(WebDriver driver) {
        super(driver);
    }

    // --- Search ---

    public boolean isSearchBarVisible() {
        return isElementVisible(searchInput);
    }

    public boolean isSearchButtonVisible() {
        return isElementVisible(searchButton);
    }

    public int getResultsCount() {
        WebElement h2 = wait.until(ExpectedConditions.visibilityOfElementLocated(resultsCount));
        // Wait until the number actually appears
        wait.until(driver -> {
            String text = h2.getText();
            return Pattern.compile("\\d+").matcher(text).find();
        });
        Matcher matcher = Pattern.compile("\\d+").matcher(h2.getText());
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        throw new RuntimeException("Could not parse result count from: " + h2.getText());
    }

    public void searchByEnter(String query) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(searchInput));
        input.clear();
        input.sendKeys(query);
        input.sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.urlContains("query="));
    }

    public void searchByButton(String query) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(searchInput));
        input.clear();
        input.sendKeys(query);
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
        wait.until(ExpectedConditions.urlContains("query="));
    }

    // --- Refine sections visibility ---

    public boolean isCategorySectionVisible()      { return isElementVisible(categorySection); }
    public boolean isOrganizationsSectionVisible() { return isElementVisible(organizationsSection); }
    public boolean isFormatSectionVisible()        { return isElementVisible(formatSection); }
    public boolean isTagsSectionVisible()          { return isElementVisible(tagsSection); }

    // --- More / Less buttons ---

    public String getCategoryMoreBtnText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(categoryMoreBtn)).getText().toLowerCase();
    }

    public void clickCategoryMoreBtn() {
        wait.until(ExpectedConditions.elementToBeClickable(categoryMoreBtn)).click();
    }

    public String getOrganizationsMoreBtnText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(organizationsMoreBtn)).getText().toLowerCase();
    }

    public void clickOrganizationsMoreBtn() {
        wait.until(ExpectedConditions.elementToBeClickable(organizationsMoreBtn)).click();
    }

    public String getTagsMoreBtnText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(tagsMoreBtn)).getText().toLowerCase();
    }

    public void clickTagsMoreBtn() {
        wait.until(ExpectedConditions.elementToBeClickable(tagsMoreBtn)).click();
    }

    // --- Category filter ---

    public void clickFirstCategoryFilter() {
        List<WebElement> checkboxes = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(categoryCheckboxes)
        );
        checkboxes.get(0).click();
        wait.until(ExpectedConditions.urlContains("category="));
    }

    // --- Active filters ---

    public boolean hasActiveFilters() {
        return !driver.findElements(activeFilterTags).isEmpty();
    }

    public int getActiveFilterCount() {
        return driver.findElements(activeFilterTags).size();
    }

    public void clearFilters() {
        wait.until(ExpectedConditions.elementToBeClickable(clearFiltersBtn)).click();
        wait.until(ExpectedConditions.not(
                ExpectedConditions.urlContains("category=%5B%22")
        ));
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void waitForPageLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        wait.until(ExpectedConditions.visibilityOfElementLocated(categorySection));
    }
}