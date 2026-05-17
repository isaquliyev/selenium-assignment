package pages;

import base.BasePageTest;
import base.Device;
import base.Language;
import base.NavItem;
import home.HomePage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.*;

public class NewsPageTest extends BasePageTest {

    private NewsPage newsPage;

    @Override
    protected void navigateToPage() {
        HomePage homePage = new HomePage(driver);
        homePage.setDevice(Device.DESKTOP);
        homePage.open();
        homePage.switchLanguage(Language.EN);
        navBar.clickDesktopNavItem(NavItem.NEWS);
        newsPage = new NewsPage(driver);
        newsPage.waitForPageLoad();
    }


    @Test
    public void testPageTitle() {
        assertTrue(driver.getTitle().contains("News"),
                "Page title should contain 'News'");
    }

    @Test
    public void testNewsCardsNotEmpty() {
        assertTrue(newsPage.getCardCount() > 0,
                "There should be at least one news card");
    }

    @Test
    public void testEachCardHasTitle() {
        for (int i = 0; i < newsPage.getCardCount(); i++) {
            assertFalse(newsPage.getCardTitle(i).isEmpty(),
                    "Card %d should have a non-empty title".formatted(i + 1));
        }
    }

    @Test
    public void testEachCardHasDate() {
        for (int i = 0; i < newsPage.getCardCount(); i++) {
            assertFalse(newsPage.getCardDate(i).isEmpty(),
                    "Card %d should have a non-empty date".formatted(i + 1));
        }
    }

    @Test
    public void testEachCardHasUniqueLink() {
        long uniqueLinks = newsPage.getAllCards()
                .stream()
                .map(card -> card.findElements(
                        org.openqa.selenium.By.cssSelector("a")).get(0).getAttribute("href"))
                .distinct()
                .count();

        assertEquals(newsPage.getCardCount(), uniqueLinks,
                "Each news card should have a unique link");
    }

    @Test
    public void testHoverChangesCardTitleColor() {
        WebElement title = newsPage.getCardTitleElement(0);

        String defaultColor = newsPage.getTextColor(title);
        newsPage.hoverOver(title);

        try { Thread.sleep(400); } catch (InterruptedException ignored) {}

        String hoverColor = newsPage.getTextColor(title);

        assertEquals("rgb(0, 0, 0)", defaultColor,
                "Default title color should be black");
        assertNotEquals(defaultColor, hoverColor,
                "Title color should change on hover");
    }

    @Test
    public void testClickCardNavigatesToDetailPage() {
        newsPage.clickCardAndGetTitle(0);
        assertTrue(newsPage.getCurrentUrl().contains("/news/"),
                "URL should contain /news/ after clicking a card");
    }

    @Test
    public void testCardTitleMatchesDetailPageTitle() {
        String cardTitle = newsPage.clickCardAndGetTitle(0);
        String detailTitle = newsPage.getDetailPageTitle();
        assertEquals(cardTitle, detailTitle,
                "Card title should match detail page heading");
    }

    @Test
    public void testDetailPageHasDate() {
        newsPage.clickCardAndGetTitle(0);
        assertTrue(newsPage.isDetailDateVisible(),
                "Detail page should show the article date");
    }

    @Test
    public void testDetailPageHasPublisher() {
        newsPage.clickCardAndGetTitle(0);
        assertTrue(newsPage.isDetailPublisherVisible(),
                "Detail page should show the publisher");
    }

    @Test
    public void testDetailPageHasBreadcrumb() {
        newsPage.clickCardAndGetTitle(0);
        assertTrue(newsPage.isBreadcrumbVisible(),
                "Detail page should have breadcrumb navigation");
    }

    @Test
    public void testDetailPageHasMoreNewsSection() {
        newsPage.clickCardAndGetTitle(0);
        assertTrue(newsPage.isMoreNewsSectionVisible(),
                "Detail page should have 'More news' section");
    }

    @Test
    public void testMoreNewsSectionHasLinks() {
        newsPage.clickCardAndGetTitle(0);
        assertFalse(newsPage.getMoreNewsLinks().isEmpty(), "More news section should contain links");
    }
}