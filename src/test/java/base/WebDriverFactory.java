package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class WebDriverFactory {

    private static final String SELENIUM_HUB_URL = Config.seleniumHubUrl();

    private WebDriverFactory() {
    }

    public static WebDriver createRemoteChrome(Path downloadDirectory) throws Exception {
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", downloadDirectory.toString());
        prefs.put("download.prompt_for_download", false);
        prefs.put("safebrowsing.enabled", false);
        options.setExperimentalOption("prefs", prefs);

        return new RemoteWebDriver(new URL(SELENIUM_HUB_URL), options);
    }
}
