package base;

import navigation.NavigationBar;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public abstract class BasePageTest {

    protected WebDriver driver;
    protected NavigationBar navBar;

    @BeforeEach
    public void setUp() throws Exception {
        driver = new RemoteWebDriver(new URL("http://selenium:4444/wd/hub"), new ChromeOptions());
        navBar = new NavigationBar(driver);
        navigateToPage();
    }

    protected abstract void navigateToPage();

    @AfterEach
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
