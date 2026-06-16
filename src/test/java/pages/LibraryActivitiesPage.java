package pages;

import config.TestConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * LibraryActivitiesPage — Page Object Model untuk halaman Aktivitas Perpustakaan.
 *
 * URL Daftar : /library/activities
 * URL Detail : /library/activities/{id}
 *
 * Semua pemanggilan method protected BasePage (waitForUrlContains, dll.)
 * dikemas di sini agar Steps tidak perlu akses langsung ke method protected.
 */
public class LibraryActivitiesPage extends BasePage {

    // =========================================================================
    // Locators — Daftar Aktivitas
    // =========================================================================

    private static final By PAGE_HEADING = By.cssSelector("h1, h2");

    private static final By LOADING_STATE = By.xpath(
            "//*[contains(text(),'Memuat') or contains(text(),'Loading')]"
    );

    private static final By EMPTY_STATE = By.xpath(
            "//*[contains(text(),'Belum ada aktivitas') " +
                    "or contains(text(),'Tidak ada aktivitas') " +
                    "or contains(text(),'Data tidak ditemukan')]"
    );

    private static final By ACTIVITY_TABLE = By.cssSelector(
            "table, [data-testid='activity-table']"
    );

    private static final By TABLE_ROWS = By.cssSelector(
            "table tbody tr, [data-testid='activity-item']"
    );

    private static final By STATUS_BADGES_IN_TABLE = By.xpath(
            "//table//tbody//tr//*[contains(@class,'badge') " +
                    "or contains(@class,'chip') " +
                    "or contains(@class,'status') " +
                    "or contains(@class,'tag')]"
    );

    // =========================================================================
    // Locators — Filter Status
    // =========================================================================

    private static final By FILTER_DROPDOWN_TRIGGER = By.xpath(
            "//div[contains(@class,'select') or contains(@class,'dropdown') " +
                    "or contains(@class,'filter')]" +
                    "//div[contains(@class,'cursor-pointer') or @role='button']"
    );

    // =========================================================================
    // Locators — Dialog Konfirmasi Pembatalan
    // =========================================================================

    private static final By DIALOG_CONTAINER = By.xpath(
            "//div[@role='dialog' " +
                    "or @data-slot='alert-dialog-content' " +
                    "or contains(@class,'modal') " +
                    "or contains(@class,'dialog')]"
    );

    private static final By DIALOG_CONFIRM_BTN = By.xpath(
            "//div[@role='dialog' or @data-slot='alert-dialog-footer']" +
                    "//button[contains(., 'Ya') or contains(., 'Batalkan') " +
                    "or contains(., 'Konfirmasi')]"
    );

    // =========================================================================
    // Locators — Halaman Detail
    // =========================================================================

    private static final By DETAIL_BOOK_TITLE = By.xpath(
            "//*[@data-testid='detail-book-title'] " +
                    "| //article//*[self::h1 or self::h2 or self::h3 " +
                    "or contains(@class,'title')][not(contains(text(),'Aktivitas'))]"
    );

    private static final By DETAIL_STATUS = By.xpath(
            "//*[@data-testid='order-status'] " +
                    "| //*[contains(@class,'status') or contains(@class,'badge')]" +
                    "[not(ancestor::thead)]"
    );

    private static final By DETAIL_LOAN_DATE = By.xpath(
            "//*[contains(text(),'Tanggal Pinjam') or contains(text(),'Tgl Pinjam')]" +
                    "/following-sibling::* | //*[@data-testid='loan-date']"
    );

    private static final By DETAIL_DUE_DATE = By.xpath(
            "//*[contains(text(),'Jatuh Tempo') or contains(text(),'Due Date')]" +
                    "/following-sibling::* | //*[@data-testid='due-date']"
    );

    private static final By DETAIL_BORROWER_NAME = By.xpath(
            "//*[contains(text(),'Peminjam') or contains(text(),'Nama')]" +
                    "/following-sibling::* | //*[@data-testid='borrower-name']"
    );

    private static final By BACK_BUTTON = By.xpath(
            "//button[contains(., 'Kembali') or contains(., 'Back')] " +
                    "| //a[contains(., 'Kembali') " +
                    "or contains(@href,'/library/activities')]"
    );

    // =========================================================================
    // Page Navigation
    // =========================================================================

    public LibraryActivitiesPage openPage() {
        navigateToPath(TestConfig.PATH_LIBRARY_ACTIVITIES);
        waitForPageReady();
        return this;
    }

    public void waitForPageReady() {
        try {
            waitForInvisibility(LOADING_STATE);
        } catch (Exception ignored) {}
        waitFor(d -> isDisplayed(ACTIVITY_TABLE) || isDisplayed(EMPTY_STATE));
    }

    // =========================================================================
    // Page State
    // =========================================================================

    public boolean isPageLoaded() {
        return getCurrentUrl().contains("/library/activities");
    }

    public boolean isPageHeadingDisplayed() {
        return isDisplayed(PAGE_HEADING);
    }

    public boolean isActivityTableDisplayed() {
        try {
            waitForVisibility(ACTIVITY_TABLE);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isEmptyStateDisplayed() {
        return isDisplayed(EMPTY_STATE);
    }

    public int getActivityRowCount() {
        try {
            return driver.findElements(TABLE_ROWS).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isColumnHeaderDisplayed(String columnName) {
        By locator = By.xpath(
                "//table//thead//*[contains(text(),'" + columnName + "')]"
        );
        return isDisplayed(locator);
    }

    // =========================================================================
    // URL Verification — wrapper agar Steps tidak akses protected method langsung
    // =========================================================================

    /**
     * TC-ACT-003: Verifikasi redirect ke halaman login.
     * Membungkus waitForUrlContains (protected) agar dapat dipanggil dari Steps.
     */
    public boolean waitForAndVerifyLoginRedirect() {
        try {
            waitForUrlContains("/loginpage");
            return getCurrentUrl().contains("/loginpage");
        } catch (Exception e) {
            return getCurrentUrl().contains("/loginpage");
        }
    }

    /**
     * TC-ACT-004: Verifikasi sudah berada di halaman detail pesanan.
     * Membungkus waitForUrlContains (protected) agar dapat dipanggil dari Steps.
     */
    public boolean waitForAndVerifyDetailPage() {
        try {
            waitForUrlContains("/library/activities/");
            return getCurrentUrl().matches(".*\\/library\\/activities\\/\\d+.*");
        } catch (Exception e) {
            return getCurrentUrl().matches(".*\\/library\\/activities\\/\\d+.*");
        }
    }

    /**
     * TC-ACT-006: Verifikasi sudah kembali ke halaman daftar (bukan halaman detail).
     * Membungkus waitForUrlContains (protected) agar dapat dipanggil dari Steps.
     */
    public boolean isOnActivitiesListPage() {
        try {
            waitForUrlContains("/library/activities");
            return !getCurrentUrl().matches(".*\\/library\\/activities\\/\\d+.*");
        } catch (Exception e) {
            return !getCurrentUrl().matches(".*\\/library\\/activities\\/\\d+.*");
        }
    }

    // =========================================================================
    // Filter Status
    // =========================================================================

    /**
     * Menerapkan filter status melalui pill button atau dropdown.
     */
    public void applyStatusFilter(String status) {
        By pillLocator = By.xpath(
                "//button[normalize-space(text())='" + status + "'] " +
                        "| //span[normalize-space(text())='" + status + "']/parent::button"
        );

        if (isDisplayed(pillLocator)) {
            click(pillLocator);
        } else {
            try {
                click(FILTER_DROPDOWN_TRIGGER);
                By optionLocator = By.xpath(
                        "//div[contains(@class,'cursor-pointer') " +
                                "and contains(text(),'" + status + "')] " +
                                "| //li[contains(text(),'" + status + "')]"
                );
                click(optionLocator);
            } catch (Exception e) {
                System.out.println("[WARN] Filter tidak ditemukan untuk status: " + status);
            }
        }

        try { Thread.sleep(800); } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Semua badge di tabel mengandung expectedStatus (atau tabel kosong).
     */
    public boolean isOnlyStatusDisplayed(String expectedStatus) {
        if ("Semua".equalsIgnoreCase(expectedStatus)) return true;

        List<WebElement> badges = driver.findElements(STATUS_BADGES_IN_TABLE);
        if (badges.isEmpty()) return isEmptyStateDisplayed();

        for (WebElement badge : badges) {
            String text = badge.getText().trim();
            if (!text.isEmpty() && !text.contains(expectedStatus)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Ada baris dengan status SELAIN expectedStatus.
     */
    public boolean isOtherStatusDisplayed(String expectedStatus) {
        List<WebElement> badges = driver.findElements(STATUS_BADGES_IN_TABLE);
        for (WebElement badge : badges) {
            String text = badge.getText().trim();
            if (!text.isEmpty() && !text.contains(expectedStatus)) {
                return true;
            }
        }
        return false;
    }

    // =========================================================================
    // Tombol Aksi pada Baris Tabel
    // =========================================================================

    public void clickButtonOnFirstRow(String buttonLabel) {
        By locator = By.xpath(
                "(//table//tbody//tr[1]//button[contains(., '" + buttonLabel + "')] " +
                        "| //table//tbody//tr[1]//a[contains(., '" + buttonLabel + "')])[1]"
        );
        click(locator);
    }

    public boolean isButtonAvailableOnFirstRow(String buttonLabel) {
        By locator = By.xpath(
                "(//table//tbody//tr[1]//button[contains(., '" + buttonLabel + "')] " +
                        "| //table//tbody//tr[1]//a[contains(., '" + buttonLabel + "')])[1]"
        );
        return isDisplayed(locator) && isEnabled(locator);
    }

    // =========================================================================
    // Dialog Konfirmasi Pembatalan
    // =========================================================================

    public boolean isCancellationDialogDisplayed() {
        return isDisplayed(DIALOG_CONTAINER);
    }

    public boolean isButtonOnDialogDisplayed(String buttonLabel) {
        By locator = By.xpath(
                "//div[@role='dialog' or @data-slot='alert-dialog-footer']" +
                        "//button[contains(., '" + buttonLabel + "')]"
        );
        return isDisplayed(locator);
    }

    public void clickConfirmOnDialog() {
        waitForVisibility(DIALOG_CONFIRM_BTN);
        jsClick(DIALOG_CONFIRM_BTN);
    }

    // =========================================================================
    // Halaman Detail
    // =========================================================================

    public boolean isDetailPageLoaded() {
        return getCurrentUrl().matches(".*\\/library\\/activities\\/\\d+.*");
    }

    public boolean isDetailBookTitleDisplayed() {
        return isDisplayed(DETAIL_BOOK_TITLE);
    }

    public boolean isDetailStatusDisplayed() {
        return isDisplayed(DETAIL_STATUS);
    }

    public boolean isDetailLoanDateDisplayed() {
        return isDisplayed(DETAIL_LOAN_DATE);
    }

    public boolean isDetailDueDateDisplayed() {
        return isDisplayed(DETAIL_DUE_DATE);
    }

    public boolean isDetailBorrowerNameDisplayed() {
        return isDisplayed(DETAIL_BORROWER_NAME);
    }

    public boolean isStatusBadgeContaining(String status) {
        By badgeLocator = By.xpath(
                "//*[contains(@class,'badge') or contains(@class,'status') " +
                        "or contains(@class,'chip')][contains(text(),'" + status + "')]"
        );
        if (isDisplayed(badgeLocator)) return true;

        By textLocator = By.xpath(
                "//*[normalize-space(text())='" + status + "']"
        );
        return isDisplayed(textLocator);
    }

    public void waitForStatusBadge(String status) {
        By locator = By.xpath("//*[contains(text(),'" + status + "')]");
        try {
            waitForVisibility(locator);
        } catch (Exception e) {
            System.out.println("[WARN] Status badge '" + status + "' tidak ditemukan.");
        }
    }

    public void clickBackOrBrowserBack() {
        if (isDisplayed(BACK_BUTTON)) {
            click(BACK_BUTTON);
        } else {
            driver.navigate().back();
        }
        waitForPageReady();
    }
}