package pages;

import base.BasePageTest;
import base.Device;
import base.Language;
import base.NavItem;
import home.HomePage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrganizationsPageTest extends BasePageTest {

    private OrganizationsPage organizationsPage;

    @Override
    protected void navigateToPage() {
        HomePage homePage = new HomePage(driver);
        homePage.setDevice(Device.DESKTOP);
        homePage.open();
        homePage.switchLanguage(Language.EN);
        navBar.clickDesktopNavItem(NavItem.ORGANIZATIONS);
        organizationsPage = new OrganizationsPage(driver);
        organizationsPage.waitForPageLoad();
    }

    @Test
    public void testPageTitle() {
        assertTrue(driver.getTitle().contains("Organizations"),
                "Page title should contain 'Organizations'");
    }


    @Test
    public void testHeadingMatchesCardCount() {
        assertEquals(
                organizationsPage.getHeadingOrgCount(),
                organizationsPage.getOrgCount(),
                "Heading count should match number of org cards"
        );
    }

    @Test
    public void testAllCardsHaveUniqueUrls() {
        long uniqueUrls = organizationsPage.getAllOrgCards()
                .stream()
                .map(card -> card.getAttribute("href"))
                .distinct()
                .count();

        assertEquals(organizationsPage.getOrgCount(), uniqueUrls,
                "Each org card should have a unique URL");
    }

    @Test
    public void testOrgCardsNotEmpty() {
        assertTrue(organizationsPage.getOrgCount() > 0,
                "There should be at least one organization");
    }

    @Test
    public void testSearchBarVisible() {
        assertTrue(organizationsPage.isSearchBarVisible(),
                "Search bar should be visible");
    }

    @Test
    public void testSearchButtonVisible() {
        assertTrue(organizationsPage.isSearchButtonVisible(),
                "Search button should be visible");
    }

    @Test
    public void testSearchByButtonFiltersResults() {
        int initialCount = organizationsPage.getOrgCount();
        organizationsPage.searchByButton("Statistika");
        int filteredCount = organizationsPage.getOrgCount();

        assertTrue(filteredCount < initialCount,
                "Search should reduce org count (%d vs %d)"
                        .formatted(filteredCount, initialCount));
    }

    @Test
    public void testSearchByEnterFiltersResults() {
        int initialCount = organizationsPage.getOrgCount();
        organizationsPage.searchByEnter("Statistika");
        int filteredCount = organizationsPage.getOrgCount();

        assertTrue(filteredCount < initialCount,
                "Search by Enter should reduce org count (%d vs %d)"
                        .formatted(filteredCount, initialCount));
    }

    @Test
    public void testSearchWithNoResultsShowsZeroCards() {
        organizationsPage.searchByButton("xyzxyzxyz123notexist");
        assertEquals(0, organizationsPage.getOrgCount(),
                "Search with no matches should show zero org cards");
    }

    @Test
    public void testClickFirstOrgGoesToSearch() {
        String url = organizationsPage.clickOrgAndGetUrl(0);

        assertTrue(url.contains("/search"), "Should navigate to search page");
        assertTrue(url.contains("org="), "URL should contain org filter");
        assertTrue(driver.getTitle().contains("Datasets"), "Page title should be Datasets");
    }

    @Test
    public void testClickLastOrgGoesToSearch() {
        int lastIndex = organizationsPage.getOrgCount() - 1;
        String url = organizationsPage.clickOrgAndGetUrl(lastIndex);

        assertTrue(url.contains("/search"), "Should navigate to search page");
        assertTrue(url.contains("org="), "URL should contain org filter");
    }
}
