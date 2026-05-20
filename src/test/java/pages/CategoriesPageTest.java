package pages;

import base.BasePageTest;
import base.Config;
import base.Device;
import base.Language;
import base.NavItem;
import home.HomePage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.*;

public class CategoriesPageTest extends BasePageTest {

    private CategoriesPage categoriesPage;

    @Override
    protected void navigateToPage() {
        HomePage homePage = new HomePage(driver);
        homePage.setDevice(Device.DESKTOP);
        homePage.open(Config.baseUrl());
        homePage.switchLanguage(Language.EN);
        navBar.clickDesktopNavItem(NavItem.CATEGORIES);
        categoriesPage = new CategoriesPage(driver);
        categoriesPage.waitForPageLoad();
    }

    @Test
    public void testPageTitle() {
        assertTrue(driver.getTitle().contains("Categories"),
                "Page title should contain 'Categories'");
    }

    @Test
    public void testCategoryCardCountMatchesHeading() {
        assertEquals(
                categoriesPage.getHeadingCategoryCount(),
                categoriesPage.getCategoryCount(),
                "Number of cards should match heading count"
        );
    }

    @Test
    public void testCategoryCardsNotEmpty() {
        assertTrue(categoriesPage.getCategoryCount() > 0,
                "There should be at least one category card");
    }

    @Test
    public void testHoverChangesBackgroundColor() {
        WebElement card = categoriesPage.getCategoryCard(0);

        String defaultColor = categoriesPage.getBackgroundColor(card);
        categoriesPage.hoverOver(card);

        try { Thread.sleep(400); } catch (InterruptedException ignored) {}

        String hoverColor = categoriesPage.getBackgroundColor(card);

        assertEquals("rgb(255, 255, 255)", defaultColor,
                "Default background should be white");
        assertNotEquals(defaultColor, hoverColor,
                "Background color should change on hover");
    }

    @Test
    public void testAllCardsStartWithWhiteBackground() {
        for (WebElement card : categoriesPage.getAllCategoryCards()) {
            assertEquals("rgb(255, 255, 255)", categoriesPage.getBackgroundColor(card),
                    "All cards should start with white background");
        }
    }

    // --- Click navigation ---

    @Test
    public void testClickFirstCategoryGoesToSearch() {
        String url = categoriesPage.clickCategoryAndGetUrl(0);

        assertTrue(url.contains("/search"), "Should navigate to search page");
        assertTrue(url.contains("category="), "URL should contain category filter");
        assertTrue(driver.getTitle().contains("Datasets"), "Page title should be Datasets");
    }

    @Test
    public void testClickLastCategoryGoesToSearch() {
        int lastIndex = categoriesPage.getCategoryCount() - 1;
        String url = categoriesPage.clickCategoryAndGetUrl(lastIndex);

        assertTrue(url.contains("/search"), "Should navigate to search page");
        assertTrue(url.contains("category="), "URL should contain category filter");
    }

    @Test
    public void testEachCategoryHasUniqueUrl() {
        long uniqueHrefs = categoriesPage.getAllCategoryCards()
                .stream()
                .map(card -> card.getAttribute("href"))
                .distinct()
                .count();

        assertEquals(categoriesPage.getCategoryCount(), uniqueHrefs,
                "Each category card should have a unique URL");
    }
}
