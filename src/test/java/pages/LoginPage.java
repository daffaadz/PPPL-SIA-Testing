package pages;

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
    private static final By ERROR_MESSAGE  = By.cssSelector("[role='alert'], .error-message, [data-sonner-toast]");
    private static final By PAGE_HEADING   = By.cssSelector("h1, h2");
    private static final By FORM_CONTAINER = By.cssSelector("form");

    // ─── Actions ──────────────────────────────────────────────────────────────

    /**
     * Navigate directly to the login page.
     */
    public LoginPage openLoginPage() {
        navigateToPath("/login");
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
     * Click the login/submit button.
     */
    public LoginPage clickLoginButton() {
        click(LOGIN_BUTTON);
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
     * Returns true if the error toast/message is displayed after failed login.
     */
    public boolean isErrorMessageDisplayed() {
        try {
            waitForToast(ERROR_MESSAGE);
            return true;
        } catch (Exception e) {
            return isDisplayed(ERROR_MESSAGE);
        }
    }

    /**
     * Get the text of the error message if displayed.
     */
    public String getErrorMessageText() {
        return getText(ERROR_MESSAGE);
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
