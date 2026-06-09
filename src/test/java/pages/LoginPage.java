package pages;

import config.TestConfig;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {

    private static final By EMAIL_INPUT    = By.cssSelector("input[type='email'], input[name='email'], input[id='email']");
    private static final By PASSWORD_INPUT = By.cssSelector("input[type='password'], input[name='password'], input[id='password']");
    private static final By LOGIN_BUTTON   = By.cssSelector("button[type='submit']");
    private static final By ERROR_BOX      = By.xpath("//h3[contains(text(),'Terjadi Kesalahan')]/..");
    private static final By ERROR_MESSAGE  = By.xpath("//h3[contains(text(),'Terjadi Kesalahan')]/following-sibling::p");
    private static final By PAGE_HEADING   = By.cssSelector("h1, h2");
    private static final By FORM_CONTAINER = By.cssSelector("form");

    public LoginPage openLoginPage() {
        navigateToPath(TestConfig.PATH_LOGIN);
        waitForPresence(FORM_CONTAINER);
        return this;
    }

    public LoginPage enterEmail(String email) {
        typeIn(EMAIL_INPUT, email);
        return this;
    }

    public LoginPage enterPassword(String password) {
        typeIn(PASSWORD_INPUT, password);
        return this;
    }

    public LoginPage clickLoginButton() {
        jsClick(LOGIN_BUTTON);
        waitFor(d -> {
            try {
                String btnText = d.findElement(LOGIN_BUTTON).getText();
                return !btnText.contains("Signing in");
            } catch (Exception e) {
                return true;
            }
        });
        return this;
    }

    public void login(String email, String password) {
        openLoginPage();
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }

    public boolean isLoginSuccessful() {
        try {
            waitFor(driver -> driver.getCurrentUrl().contains("/dashboard"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isErrorMessageDisplayed() {
        By byHeading  = By.xpath("//h3[normalize-space(text())='Terjadi Kesalahan']");
        By byBorder   = By.xpath("//*[contains(@style,'EF4444') or contains(@style,'ef4444')]");
        By byContains = By.xpath("//*[contains(.,'Terjadi Kesalahan')][not(self::script)][not(self::style)]");

        for (By strategy : new By[]{byHeading, byBorder, byContains}) {
            try {
                org.openqa.selenium.support.ui.WebDriverWait shortWait =
                    new org.openqa.selenium.support.ui.WebDriverWait(
                        driver, java.time.Duration.ofSeconds(5));
                shortWait.until(
                    org.openqa.selenium.support.ui.ExpectedConditions
                        .visibilityOfElementLocated(strategy));
                return true;
            } catch (Exception ignored) {}
        }
        return false;
    }

    public String getErrorMessageText() {
        try {
            return getText(ERROR_MESSAGE);
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isLoginFormDisplayed() {
        return isDisplayed(FORM_CONTAINER)
                && isDisplayed(EMAIL_INPUT)
                && isDisplayed(PASSWORD_INPUT);
    }

    public boolean isLoginButtonEnabled() {
        return isEnabled(LOGIN_BUTTON);
    }

    public String getPageHeadingText() {
        return getText(PAGE_HEADING);
    }
}
