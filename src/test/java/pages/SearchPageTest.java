package pages;

import base.BasePageTest;
import base.Device;
import base.Language;
import base.NavItem;
import home.HomePage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SearchPageTest extends BasePageTest {

    private SearchPage searchPage;

    @Override
    protected void navigateToPage() {
        HomePage homePage = new HomePage(driver);
        homePage.setDevice(Device.DESKTOP);
        homePage.open();
        homePage.switchLanguage(Language.EN);
        navBar.clickDesktopNavItem(NavItem.SEARCH);
        searchPage = new SearchPage(driver);
        searchPage.waitForPageLoad();
    }

    @Test
    public void testPageTitle() {
        assertTrue(driver.getTitle().contains("Datasets"),
                "Page title should contain 'Datasets'");
    }

    @Test
    public void testSearchBarVisible() {
        assertTrue(searchPage.isSearchBarVisible(), "Search bar should be visible");
    }

    @Test
    public void testSearchButtonVisible() {
        assertTrue(searchPage.isSearchButtonVisible(), "Search button should be visible");
    }

    @Test
    public void testInitialResultsExist() {
        assertTrue(searchPage.getResultsCount() > 0,
                "Should have results on initial load");
    }

    @Test
    public void testSearchByEnterReducesResults() {
        int initialCount = searchPage.getResultsCount();
        searchPage.searchByEnter("2025");
        int filteredCount = searchPage.getResultsCount();

        assertTrue(filteredCount < initialCount,
                "Search '2025' should return fewer results than total (%d vs %d)"
                        .formatted(filteredCount, initialCount));
    }

    @Test
    public void testSearchByButtonReducesResults() {
        int initialCount = searchPage.getResultsCount();
        searchPage.searchByButton("2025");
        int filteredCount = searchPage.getResultsCount();

        assertTrue(filteredCount < initialCount,
                "Search by button '2025' should return fewer results than total (%d vs %d)"
                        .formatted(filteredCount, initialCount));
    }

    @Test
    public void testSearchUpdatesUrl() {
        searchPage.searchByEnter("2025");
        assertTrue(searchPage.getCurrentUrl().contains("query="),
                "URL should contain query parameter after search");
    }

    @Test
    public void testCategorySectionVisible() {
        assertTrue(searchPage.isCategorySectionVisible(), "Category section should be visible");
    }

    @Test
    public void testOrganizationsSectionVisible() {
        assertTrue(searchPage.isOrganizationsSectionVisible(), "Organizations section should be visible");
    }

    @Test
    public void testFormatSectionVisible() {
        assertTrue(searchPage.isFormatSectionVisible(), "Format section should be visible");
    }

    @Test
    public void testTagsSectionVisible() {
        assertTrue(searchPage.isTagsSectionVisible(), "Tags section should be visible");
    }

    @Test
    public void testCategoryMoreButtonToggles() {
        assertEquals("more", searchPage.getCategoryMoreBtnText(),
                "Category button should initially say 'more'");
        searchPage.clickCategoryMoreBtn();
        assertEquals("less", searchPage.getCategoryMoreBtnText(),
                "Category button should say 'less' after clicking");
        searchPage.clickCategoryMoreBtn();
        assertEquals("more", searchPage.getCategoryMoreBtnText(),
                "Category button should say 'more' again after toggling back");
    }

    @Test
    public void testOrganizationsMoreButtonToggles() {
        assertEquals("more", searchPage.getOrganizationsMoreBtnText(),
                "Organizations button should initially say 'more'");
        searchPage.clickOrganizationsMoreBtn();
        assertEquals("less", searchPage.getOrganizationsMoreBtnText(),
                "Organizations button should say 'less' after clicking");
    }

    @Test
    public void testTagsMoreButtonToggles() {
        assertEquals("more", searchPage.getTagsMoreBtnText(),
                "Tags button should initially say 'more'");
        searchPage.clickTagsMoreBtn();
        assertEquals("less", searchPage.getTagsMoreBtnText(),
                "Tags button should say 'less' after clicking");
    }

    @Test
    public void testCategoryFilterUpdatesUrl() {
        searchPage.clickFirstCategoryFilter();
        assertTrue(searchPage.getCurrentUrl().contains("category="),
                "URL should contain category parameter after filter applied");
    }

    @Test
    public void testCategoryFilterReducesResults() {
        int initialCount = searchPage.getResultsCount();
        searchPage.clickFirstCategoryFilter();
        int filteredCount = searchPage.getResultsCount();

        assertTrue(filteredCount < initialCount,
                "Category filter should reduce results (%d vs %d)"
                        .formatted(filteredCount, initialCount));
    }

    @Test
    public void testCategoryFilterShowsActiveTag() {
        searchPage.clickFirstCategoryFilter();
        assertTrue(searchPage.hasActiveFilters(),
                "Active filter tag should appear after applying category filter");
    }

    @Test
    public void testClearFiltersResetsResults() {
        int initialCount = searchPage.getResultsCount();
        searchPage.clickFirstCategoryFilter();
        searchPage.clearFilters();

        assertEquals(initialCount, searchPage.getResultsCount(),
                "Results should reset to initial count after clearing filters");
    }

    @Test
    public void testClearFiltersResetsUrl() {
        searchPage.clickFirstCategoryFilter();
        searchPage.clearFilters();

        assertFalse(searchPage.getCurrentUrl().contains("category=%5B%22"),
                "URL should not contain active category filter after clearing");
    }
}