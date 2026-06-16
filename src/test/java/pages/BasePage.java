package pages;

import hooks.CucumberHooks;
import config.TestConfig;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage — Abstract base class for all Page Object Models.
 */
public abstract class BasePage {

    private static final By SUCCESS_TOAST = By.xpath(
            "//*[@data-sonner-toast] | //*[contains(text(),'berhasil') or contains(text(),'berhasil')]"
    );

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage() {
        this.driver = CucumberHooks.driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(TestConfig.EXPLICIT_WAIT_SECONDS));
        PageFactory.initElements(driver, this);
    }

    public void navigateTo(String url) {
        driver.get(url);
    }

    /**
     * Navigate to a path relative to BASE_URL.
     */
    public void navigateToPath(String path) {
        driver.get(TestConfig.BASE_URL + path);
    }

    /**
     * Returns the current browser URL.
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Returns the current page title.
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    protected WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait until an element is clickable.
     */
    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Wait until an element is present in the DOM (not necessarily visible).
     */
    protected WebElement waitForPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Wait until the current URL contains the given substring.
     */
    protected void waitForUrlContains(String urlSubstring) {
        wait.until(ExpectedConditions.urlContains(urlSubstring));
    }

    /**
     * Wait until an element is no longer visible on the page.
     */
    protected void waitForInvisibility(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Wait until a specific text appears in an element.
     */
    protected void waitForTextInElement(By locator, String text) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    /**
     * Wait for a custom ExpectedCondition.
     */
    public <T> T waitFor(ExpectedCondition<T> condition) {
        return wait.until(condition);
    }

    protected void click(By locator) {
        waitForClickable(locator).click();
    }

    /**
     * Clear an input field and type text into it.
     */
    protected void typeIn(By locator, String text) {
        WebElement element = waitForClickable(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Get trimmed text content of an element.
     */
    protected String getText(By locator) {
        return waitForVisibility(locator).getText().trim();
    }

    /**
     * Get the value attribute of an input element.
     */
    protected String getValue(By locator) {
        return waitForVisibility(locator).getAttribute("value");
    }

    /**
     * Check whether an element is currently displayed on the page.
     */
    protected boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Check whether an element is enabled (e.g., a button is not disabled).
     */
    protected boolean isEnabled(By locator) {
        try {
            return waitForVisibility(locator).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Scroll element into viewport using JavaScript.
     */
    protected void scrollIntoView(By locator) {
        WebElement el = waitForPresence(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
    }

    /**
     * Click an element using JavaScript — useful when normal click is intercepted.
     */
    protected void jsClick(By locator) {
        WebElement el = waitForPresence(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    /**
     * Check if a CSS class is present on an element.
     */
    protected boolean hasClass(By locator, String cssClass) {
        String classes = waitForPresence(locator).getAttribute("class");
        return classes != null && classes.contains(cssClass);
    }

    /**
     * Check if element's attribute contains a specific value.
     */
    protected boolean attributeContains(By locator, String attribute, String value) {
        String attr = waitForPresence(locator).getAttribute(attribute);
        return attr != null && attr.contains(value);
    }

    protected WebElement waitForToast(By locator) {
        WebDriverWait toastWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        return toastWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public boolean isSuccessToastDisplayed() {
        try {
            waitForToast(SUCCESS_TOAST);
            return true;
        } catch (Exception e) {
            return isDisplayed(SUCCESS_TOAST);
        }
    }

    public void clearLocalStorage() {
        ((JavascriptExecutor) driver).executeScript(
            "localStorage.clear(); sessionStorage.clear();" +
            "document.cookie.split(';').forEach(function(c) {" +
            "  document.cookie = c.trim().split('=')[0] + '=;expires=Thu, 01 Jan 1970 00:00:00 UTC;path=/';" +
            "});"
        );
    }
}
