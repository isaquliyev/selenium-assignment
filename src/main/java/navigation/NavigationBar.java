package navigation;

import base.BasePage;
import base.NavItem;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class NavigationBar extends BasePage {

    private final By desktopNavLinks = By.cssSelector("a.Header_navbarItem__EV5lB");
    private final By burgerButton   = By.cssSelector("button.inline-flex.items-center.justify-center");
    private final By mobileNavLinks = By.cssSelector("div.flex.flex-col a.py-2");

    public NavigationBar(WebDriver driver) {
        super(driver);
    }

    public void clickDesktopNavItem(NavItem item) {
        By link = By.cssSelector("a.Header_navbarItem__EV5lB[href$='" + item.getPath() + "']");
        wait.until(ExpectedConditions.elementToBeClickable(link)).click();
        wait.until(ExpectedConditions.titleContains(item.getExpectedTitle()));
    }

    public List<WebElement> getDesktopNavItems() {
        return driver.findElements(desktopNavLinks);
    }

    public void openBurgerMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(burgerButton)).click();
    }

    public void clickMobileNavItem(NavItem item) {
        openBurgerMenu();
        By link = By.cssSelector("div.flex.flex-col a.py-2[href$='" + item.getPath() + "']");
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(link));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        wait.until(ExpectedConditions.titleContains(item.getExpectedTitle()));
    }

    public List<WebElement> getMobileNavItems() {
        wait.until(ExpectedConditions.numberOfElementsToBe(mobileNavLinks, NavItem.values().length));
        return driver.findElements(mobileNavLinks);
    }
}