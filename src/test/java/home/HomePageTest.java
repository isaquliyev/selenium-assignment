package home;

import base.Device;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class HomePageTest {

    private WebDriver driver;
    private HomePage homePage;

    @BeforeEach
    public void setUp() throws Exception {
        driver = new RemoteWebDriver(new URL("http://selenium:4444/wd/hub"), new ChromeOptions());
        homePage = new HomePage(driver);
    }

    @Test
    public void testOpenHomePage() {
        homePage.open();
        System.out.println("Page title: " + driver.getTitle());
    }

    @Test
    public void testDesktopNav() {
        homePage.setDevice(Device.DESKTOP);
        homePage.open();

        assertFalse(homePage.isBurgerButtonVisible(), "Burger should be hidden on desktop");
        assertTrue(homePage.isDesktopNavVisible(), "Desktop nav should be visible");
        assertEquals(6, homePage.getDesktopNavLinkCount(), "Should have 6 nav links");
    }

    @Test
    public void testMobileNav() {
        homePage.setDevice(Device.MOBILE);
        homePage.open();

        assertTrue(homePage.isBurgerButtonVisible(), "Burger should be visible on mobile");
        assertFalse(homePage.isDesktopNavVisible(), "Desktop nav should be hidden on mobile");

        homePage.openMobileMenu();
        assertEquals(6, homePage.getMobileNavLinkCount(), "Should have 6 nav links");
    }

    @Test
    public void testNavLinkCountConsistentAcrossDevices() {
        homePage.setDevice(Device.DESKTOP);
        homePage.open();
        int desktopCount = homePage.getDesktopNavLinkCount();

        homePage.setDevice(Device.MOBILE);
        homePage.open();
        homePage.openMobileMenu();
        int mobileCount = homePage.getMobileNavLinkCount();

        assertEquals(desktopCount, mobileCount,
                "Nav link count should be the same on desktop (%d) and mobile (%d)"
                        .formatted(desktopCount, mobileCount));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
