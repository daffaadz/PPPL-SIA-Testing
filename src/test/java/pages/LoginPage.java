package pages;

import config.TestConfig;
import org.openqa.selenium.By;

/**
 * LoginPage — Page Object for the SIA-UGN login page.
 *
 * Covers both mahasiswa and admin login scenarios.
 * Route: /login
 */
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

    /**
     * Enter email in the email field.
     */
    public LoginPage enterEmail(String email) {
        typeIn(EMAIL_INPUT, email);
        return this;
    }

    /**
     * Enter password in the password field.
     */
    public LoginPage enterPassword(String password) {
        typeIn(PASSWORD_INPUT, password);
        return this;
    }

    /**
     * Click the login/submit button using JavaScript to avoid issues
     * where the button becomes disabled (isSubmitting=true) during API call.
     * After JS click, we wait for the button to no longer show "Signing in"
     * (i.e., the API call has completed) before returning.
     */
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

    /**
     * Full login flow — fill form and submit.
     */
    public void login(String email, String password) {
        openLoginPage();
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }

    public boolean isLoginSuccessful() {
        try {
            waitFor(driver -> {
                String url = driver.getCurrentUrl();
                return url.contains("/dashboard") || url.contains("/adminpage");
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isErrorMessageDisplayed() {
        By byHeading = By.xpath("//h3[normalize-space(text())='Terjadi Kesalahan']");
        try {
            org.openqa.selenium.support.ui.WebDriverWait shortWait =
                new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5));
            shortWait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(byHeading));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the error text from the <p> element inside the ErrorMessageBox.
     * Returns empty string if not found.
     */
    public String getErrorMessageText() {
        try {
            return getText(ERROR_MESSAGE);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Returns true if the login form is currently displayed on the page.
     */
    public boolean isLoginFormDisplayed() {
        return isDisplayed(FORM_CONTAINER)
                && isDisplayed(EMAIL_INPUT)
                && isDisplayed(PASSWORD_INPUT);
    }

    /**
     * Returns true if the login button is visible and enabled.
     */
    public boolean isLoginButtonEnabled() {
        return isEnabled(LOGIN_BUTTON);
    }

    /**
     * Returns the current page heading text.
     */
    public String getPageHeadingText() {
        return getText(PAGE_HEADING);
    }
}
