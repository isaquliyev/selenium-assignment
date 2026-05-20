package home;

import base.BasePage;
import base.Language;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {

    private final By burgerButton    = By.cssSelector("button.inline-flex.items-center.justify-center");
    private final By mobileNavLinks  = By.cssSelector("div.flex.flex-col a.py-2");
    private final By desktopNavLinks = By.cssSelector("a.Header_navbarItem__EV5lB");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void open(String url) {
        driver.get(url);
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

    private By languageButton(Language lang) {
        return By.xpath("//span[@role='link' and text()='" + lang.getCode() + "']");
    }

    private By activeLanguageButton() {
        return By.cssSelector("span[role='link'].Header_activeLangBg__6TcI7");
    }

    public void switchLanguage(Language lang) {
        By targetActiveButton = By.xpath(
                "//span[@role='link' and text()='" + lang.getCode() + "' and contains(@class, 'Header_activeLangBg')]"
        );

        wait.until(ExpectedConditions.elementToBeClickable(languageButton(lang))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(targetActiveButton));
    }

    public String getActiveLanguageCode() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(activeLanguageButton())).getText();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
