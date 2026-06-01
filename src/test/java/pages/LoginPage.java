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

    // ─── Locators ─────────────────────────────────────────────────────────────
    private static final By EMAIL_INPUT    = By.cssSelector("input[type='email'], input[name='email'], input[id='email']");
    private static final By PASSWORD_INPUT = By.cssSelector("input[type='password'], input[name='password'], input[id='password']");
    private static final By LOGIN_BUTTON   = By.cssSelector("button[type='submit']");
    // ErrorMessageBox component: a <div> with red background containing <h3>Terjadi Kesalahan</h3>
    // and a <p> with the actual error text beneath it.
    private static final By ERROR_BOX      = By.xpath("//h3[contains(text(),'Terjadi Kesalahan')]/..");
    private static final By ERROR_MESSAGE  = By.xpath("//h3[contains(text(),'Terjadi Kesalahan')]/following-sibling::p");
    private static final By PAGE_HEADING   = By.cssSelector("h1, h2");
    private static final By FORM_CONTAINER = By.cssSelector("form");

    // ─── Actions ──────────────────────────────────────────────────────────────

    /**
     * Navigate directly to the login page.
     */
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
        // Use JS click to bypass Selenium's clickable check
        jsClick(LOGIN_BUTTON);
        // Wait for submission to complete: button text returns to "Sign In"
        // OR an error box appears, OR URL changes (success)
        waitFor(d -> {
            try {
                String btnText = d.findElement(LOGIN_BUTTON).getText();
                // Submission done when button no longer shows loading text
                return !btnText.contains("Signing in");
            } catch (Exception e) {
                return true; // element gone = page changed, done
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

    // ─── Assertions ───────────────────────────────────────────────────────────

    /**
     * Checks whether login was successful by verifying URL changed from /login.
     */
    public boolean isLoginSuccessful() {
        try {
            // Wait for redirect away from /login
            waitFor(driver -> !driver.getCurrentUrl().contains("/login"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns true if the "Terjadi Kesalahan" error box is displayed.
     *
     * Uses multiple XPath strategies to find the ErrorMessageBox component:
     *  1. By h3 text content (most specific)
     *  2. By the red border color style attribute (fallback)
     *
     * After clickLoginButton() waits for API completion, the error box
     * should already be visible. We do a short additional check here.
     */
    public boolean isErrorMessageDisplayed() {
        // Strategy 1: find by h3 heading text — most reliable
        By byHeading  = By.xpath("//h3[normalize-space(text())='Terjadi Kesalahan']");
        // Strategy 2: find the outer container div by border color style
        By byBorder   = By.xpath("//*[contains(@style,'EF4444') or contains(@style,'ef4444')]");
        // Strategy 3: any element whose text contains the heading phrase
        By byContains = By.xpath("//*[contains(.,'Terjadi Kesalahan')][not(self::script)][not(self::style)]");

        for (By strategy : new By[]{byHeading, byBorder, byContains}) {
            try {
                // Short timeout — API call is done by this point
                org.openqa.selenium.support.ui.WebDriverWait shortWait =
                    new org.openqa.selenium.support.ui.WebDriverWait(
                        driver, java.time.Duration.ofSeconds(5));
                shortWait.until(
                    org.openqa.selenium.support.ui.ExpectedConditions
                        .visibilityOfElementLocated(strategy));
                return true;
            } catch (Exception ignored) {
                // Try next strategy
            }
        }
        return false;
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
