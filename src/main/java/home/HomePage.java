package home;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HomePage extends BasePage {

    private final By burgerButton = By.cssSelector("button.inline-flex.items-center.justify-center");
    private final By mobileNavLinks = By.cssSelector("div.flex.flex-col a");
    private final By desktopNavLinks = By.cssSelector("a.Header_navbarItem__EV5lB");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get("https://opendata.az/en");
    }

    public void setWindowSize(int width, int height) {
        driver.manage().window().setSize(new Dimension(width, height));
    }

    public boolean isBurgerButtonVisible() {
        List<WebElement> elements = driver.findElements(burgerButton);
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }

    public boolean isDesktopNavVisible() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(desktopNavLinks));
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isMobileNavVisible() {
        List<WebElement> elements = driver.findElements(mobileNavLinks);
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }

    public int getDesktopNavLinkCount() {
        return driver.findElements(desktopNavLinks).size();
    }

    public int getMobileNavLinkCount() {
        return driver.findElements(mobileNavLinks).size();
    }


}
