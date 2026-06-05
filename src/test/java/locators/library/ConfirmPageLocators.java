package locators.library;

import org.openqa.selenium.By;

/**
 * Centralized locators for the library confirm/order detail page.
 *
 * Keep selectors here so page objects and step definitions stay readable and
 * locator changes only need to be updated in one place.
 */
public final class ConfirmPageLocators {

    public static final By PAGE_HEADING = By.cssSelector("h1, h2");
    public static final By ORDER_CONTAINER = By.cssSelector(
            "article, [data-testid='order-detail'], [data-testid='library-order-detail']"
    );
    public static final By STATUS_BADGE = By.cssSelector(
            "[data-testid='order-status'], .status-badge, [class*='status']"
    );
    public static final By PRIMARY_ACTION_BUTTON = By.cssSelector(
            "button[type='submit'], button[data-testid='confirm-button']"
    );
    public static final By CANCEL_BUTTON = By.xpath(
            "//button[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'batalkan') or contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'cancel')]"
    );
    public static final By CONFIRM_BUTTON = By.xpath(
            "//button[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'konfirmasi') or contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'confirm')]"
    );

    private ConfirmPageLocators() {
        // Utility class
    }
}
