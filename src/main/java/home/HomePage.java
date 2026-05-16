package home;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {

    private final By burgerButton    = By.cssSelector("button.inline-flex.items-center.justify-center");
    private final By mobileNavLinks  = By.cssSelector("div.flex.flex-col a.py-2");
    private final By desktopNavLinks = By.cssSelector("a.Header_navbarItem__EV5lB");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get("https://opendata.az/");
    }

    public boolean isBurgerButtonVisible() {
        return isElementVisible(burgerButton);
    }

    public void openMobileMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(burgerButton)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(mobileNavLinks));
    }


    public boolean isDesktopNavVisible() {
        return isElementVisible(desktopNavLinks);
    }

    public boolean isMobileNavVisible() {
        return isElementVisible(mobileNavLinks);
    }

    public int getDesktopNavLinkCount() {
        return countElements(desktopNavLinks);
    }

    public int getMobileNavLinkCount() {
        return countElements(mobileNavLinks);
    }
}
