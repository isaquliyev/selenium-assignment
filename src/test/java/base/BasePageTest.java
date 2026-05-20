package base;

import download.DownloadDirectory;
import navigation.NavigationBar;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.WebDriver;

import java.nio.file.Path;

public abstract class BasePageTest {

    protected WebDriver driver;
    protected NavigationBar navBar;
    private static Path downloadDirectory;

    @RegisterExtension
    final ScreenshotOnFailure screenshotOnFailure = new ScreenshotOnFailure(() -> driver);

    @BeforeEach
    public void setUp() throws Exception {
        downloadDirectory = DownloadDirectory.defaultPath();
        DownloadDirectory.ensureExists(downloadDirectory);
        DownloadDirectory.clear(downloadDirectory);

        driver = WebDriverFactory.createRemoteChrome(downloadDirectory);
        navBar = new NavigationBar(driver);
        navigateToPage();
    }

    public static Path getDownloadDirectory() {
        return downloadDirectory;
    }

    protected abstract void navigateToPage();

    @AfterEach
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
