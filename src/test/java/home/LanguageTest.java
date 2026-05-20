package home;

import base.Config;
import base.Device;
import base.Language;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class LanguageTest {

    private WebDriver driver;
    private HomePage homePage;

    @BeforeEach
    public void setUp() throws Exception {
        driver = new RemoteWebDriver(new URL(Config.seleniumHubUrl()), new ChromeOptions());
        homePage = new HomePage(driver);
        homePage.setDevice(Device.DESKTOP);
        homePage.open(Config.baseUrl());
    }

    @Test
    public void testDefaultLanguageIsAzeri() {
        assertEquals("AZ", homePage.getActiveLanguageCode(), "Default language should be AZ");
        assertTrue(homePage.getCurrentUrl().equals("https://opendata.az/")
                        || homePage.getCurrentUrl().equals("https://opendata.az"),
                "Default URL should have no language suffix");
    }

    @Test
    public void testSwitchToRussian() {
        homePage.switchLanguage(Language.RU);

        assertEquals("RU", homePage.getActiveLanguageCode(), "Active language should be RU");
        assertTrue(homePage.getCurrentUrl().contains("/ru"), "URL should contain /ru");
    }

    @Test
    public void testSwitchToEnglish() {
        homePage.switchLanguage(Language.EN);

        assertEquals("EN", homePage.getActiveLanguageCode(), "Active language should be EN");
        assertTrue(homePage.getCurrentUrl().contains("/en"), "URL should contain /en");
    }

    @Test
    public void testSwitchBackToAzeri() {
        homePage.switchLanguage(Language.EN);
        homePage.switchLanguage(Language.AZ);

        assertEquals("AZ", homePage.getActiveLanguageCode(), "Active language should return to AZ");
        assertFalse(homePage.getCurrentUrl().contains("/en"), "URL should no longer contain /en");
    }

    @Test
    public void testAllLanguagesAvailable() {
        for (Language lang : Language.values()) {
            homePage.switchLanguage(lang);
            assertEquals(lang.getCode(), homePage.getActiveLanguageCode(),
                    "Language %s should be selectable".formatted(lang.getCode()));
        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
