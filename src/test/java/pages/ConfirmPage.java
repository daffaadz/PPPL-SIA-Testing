package pages;

import locators.library.ConfirmPageLocators;

/**
 * ConfirmPage — page object scaffold for library order confirmation flows.
 *
 * The public methods are written in a BDD-friendly style so future Cucumber
 * step definitions can read almost like the Gherkin scenarios.
 */
public class ConfirmPage extends BasePage {

    // ─── Navigation ──────────────────────────────────────────────────────────

    /**
     * Opens the library order detail page for a given order id.
     */
    public ConfirmPage openOrderDetailPage(String orderId) {
        navigateToPath("/library/activities/" + orderId);
        waitForPresence(ConfirmPageLocators.ORDER_CONTAINER);
        return this;
    }

    /**
     * Opens the library activity list page.
     */
    public ConfirmPage openActivityListPage() {
        navigateToPath("/library/activities");
        waitForPresence(ConfirmPageLocators.PAGE_HEADING);
        return this;
    }

    // ─── State Checks ────────────────────────────────────────────────────────

    /**
     * Returns true when the order detail content is visible.
     */
    public boolean isOrderDetailVisible() {
        return isDisplayed(ConfirmPageLocators.ORDER_CONTAINER)
                || isDisplayed(ConfirmPageLocators.PAGE_HEADING);
    }

    /**
     * Returns the main page heading text when available.
     */
    public String getPageHeadingText() {
        return getText(ConfirmPageLocators.PAGE_HEADING);
    }

    /**
     * Returns the order status label text when the UI exposes a status badge.
     */
    public String getOrderStatusText() {
        return getText(ConfirmPageLocators.STATUS_BADGE);
    }

    /**
     * Checks whether a primary action button is available on the page.
     */
    public boolean isPrimaryActionAvailable() {
        return isDisplayed(ConfirmPageLocators.PRIMARY_ACTION_BUTTON)
                && isEnabled(ConfirmPageLocators.PRIMARY_ACTION_BUTTON);
    }

    /**
     * Checks whether a cancel action is visible.
     */
    public boolean isCancelActionVisible() {
        return isDisplayed(ConfirmPageLocators.CANCEL_BUTTON);
    }

    /**
     * Checks whether a confirm action is visible.
     */
    public boolean isConfirmActionVisible() {
        return isDisplayed(ConfirmPageLocators.CONFIRM_BUTTON);
    }

    // ─── Actions ──────────────────────────────────────────────────────────────

    /**
     * Clicks the primary action button if the final UI uses a single CTA.
     */
    public ConfirmPage clickPrimaryAction() {
        click(ConfirmPageLocators.PRIMARY_ACTION_BUTTON);
        return this;
    }

    /**
     * Clicks a cancel button when the page offers order cancellation.
     */
    public ConfirmPage clickCancelAction() {
        click(ConfirmPageLocators.CANCEL_BUTTON);
        return this;
    }

    /**
     * Clicks a confirm button when the page offers order confirmation.
     */
    public ConfirmPage clickConfirmAction() {
        click(ConfirmPageLocators.CONFIRM_BUTTON);
        return this;
    }

    // ─── BDD-Friendly Aliases ─────────────────────────────────────────────────

    /**
     * Alias for future Gherkin step definitions that read closer to natural language.
     */
    public ConfirmPage sayaMembukaDetailPesanan(String orderId) {
        return openOrderDetailPage(orderId);
    }

    /**
     * Alias for future step definitions that express the page expectation.
     */
    public boolean detailPesananDitampilkan() {
        return isOrderDetailVisible();
    }

    /**
     * Alias for future step definitions that check whether the order can be confirmed.
     */
    public boolean tombolKonfirmasiTersedia() {
        return isConfirmActionVisible();
    }

    /**
     * Alias for future step definitions that perform the confirmation action.
     */
    public ConfirmPage sayaMengonfirmasiPesanan() {
        return clickConfirmAction();
    }

    /**
     * Alias for future step definitions that cancel the order.
     */
    public ConfirmPage sayaMembatalkanPesanan() {
        return clickCancelAction();
    }
}
