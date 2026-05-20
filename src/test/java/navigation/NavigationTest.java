package navigation;

import base.Config;
import base.Device;
import base.Language;
import base.NavItem;
import home.HomePage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class NavigationTest {

    private WebDriver driver;
    private HomePage homePage;
    private NavigationBar navBar;

    @BeforeEach
    public void setUp() throws Exception {
        driver = new RemoteWebDriver(new URL(Config.seleniumHubUrl()), new ChromeOptions());
        homePage = new HomePage(driver);
        navBar = new NavigationBar(driver);

        homePage.setDevice(Device.DESKTOP);
        homePage.open(Config.baseUrl());
        homePage.switchLanguage(Language.EN);
    }

    @Test
    public void testDesktopNavItemCount() {
        assertEquals(NavItem.values().length, navBar.getDesktopNavItems().size(),
                "Desktop nav should have %d items".formatted(NavItem.values().length));
    }

    @ParameterizedTest
    @EnumSource(NavItem.class)
    public void testDesktopNavTitle(NavItem item) {
        homePage.setDevice(Device.DESKTOP);
        homePage.open(Config.baseUrl());
        homePage.switchLanguage(Language.EN);

        navBar.clickDesktopNavItem(item);

        assertTrue(driver.getTitle().contains(item.getExpectedTitle()),
                "Page title should contain '%s' but was '%s'"
                        .formatted(item.getExpectedTitle(), driver.getTitle()));
    }

    @ParameterizedTest
    @EnumSource(NavItem.class)
    public void testMobileNavTitle(NavItem item) {
        homePage.setDevice(Device.MOBILE);
        homePage.open(Config.baseUrl());
        homePage.switchLanguage(Language.EN);

        navBar.clickMobileNavItem(item);

        assertTrue(driver.getTitle().contains(item.getExpectedTitle()),
                "Page title should contain '%s' but was '%s'"
                        .formatted(item.getExpectedTitle(), driver.getTitle()));
    }

    @Test
    public void testMobileNavItemCount() {
        homePage.setDevice(Device.MOBILE);
        homePage.open(Config.baseUrl());
        homePage.switchLanguage(Language.EN);
        navBar.openBurgerMenu();

        assertEquals(NavItem.values().length, navBar.getMobileNavItems().size(),
                "Mobile nav should have %d items".formatted(NavItem.values().length));
    }

    @Test
    public void testNavItemCountConsistentAcrossDevices() {
        int desktopCount = navBar.getDesktopNavItems().size();

        homePage.setDevice(Device.MOBILE);
        homePage.open(Config.baseUrl());
        homePage.switchLanguage(Language.EN);
        navBar.openBurgerMenu();

        int mobileCount = navBar.getMobileNavItems().size();

        assertEquals(desktopCount, mobileCount,
                "Nav item count should match: desktop=%d, mobile=%d"
                        .formatted(desktopCount, mobileCount));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}