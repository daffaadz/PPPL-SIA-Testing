package pages;

import config.TestConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Map;

public class LibraryActivitiesPage extends BasePage {

    // Page State
    private static final By PAGE_HEADING = By.cssSelector("h1, h2");

    private static final By LOADING_STATE = By.xpath(
            "//*[contains(text(),'Memuat') or contains(text(),'Loading')]"
    );

    private static final By EMPTY_STATE = By.xpath(
            "//*[contains(text(),'Belum ada aktivitas')" +
            " or contains(text(),'Tidak ada aktivitas')" +
            " or contains(text(),'Data tidak ditemukan')]"
    );

    // Table
    private static final By ACTIVITY_TABLE = By.cssSelector("table");

    private static final By TABLE_ROWS = By.cssSelector("table tbody tr");

    private static final By STATUS_BADGES_IN_TABLE = By.cssSelector(
            "table tbody tr span[class*='rounded-full']"
    );

    // Filter
    private static final By FILTER_SELECT = By.cssSelector("select");

    // Navigation
    private static final By BACK_BUTTON = By.xpath(
            "//a[contains(., 'Kembali')]"
            + " | //button[contains(., 'Kembali')]"
    );

    // Detail Page
    private static final By DETAIL_BOOK_TITLE = By.cssSelector("article h2");

    private static final By DETAIL_STATUS = By.cssSelector(
            "article span[class*='rounded-full']"
    );

    private static final By DETAIL_BORROWER_NAME = By.xpath(
            "//*[contains(normalize-space(),'Pemesan')]"
    );

    private static final By DETAIL_LOAN_DATE = By.xpath(
            "//*[contains(normalize-space(),'Dipesan')]"
    );

    private static final By DETAIL_DURATION = By.xpath(
            "//*[contains(normalize-space(),'Durasi')]"
    );

    // Status Value Map
    private static final Map<String, String> STATUS_MAP = Map.of(
            "Semua", "",
            "Semua Status", "",
            "Dipesan", "ordered",
            "Menunggu Konfirmasi", "ordered",
            "Dipinjam", "borrowed",
            "Dikembalikan", "returned",
            "Dibatalkan", "cancelled"
    );

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
            if (isEmptyStateDisplayed()) return 0;
            return driver.findElements(TABLE_ROWS).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isColumnHeaderDisplayed(String columnName) {
        By locator = By.xpath("//table//thead//*[contains(text(),'" + columnName + "')]");
        return isDisplayed(locator);
    }

    public boolean waitForAndVerifyLoginRedirect() {
        try {
            waitForUrlContains("/loginpage");
            return getCurrentUrl().contains("/loginpage");
        } catch (Exception e) {
            return getCurrentUrl().contains("/loginpage");
        }
    }

    public boolean waitForAndVerifyDetailPage() {
        try {
            waitForUrlContains("/library/activities/");
            return getCurrentUrl().matches(".*\\/library\\/activities\\/\\d+.*");
        } catch (Exception e) {
            return getCurrentUrl().matches(".*\\/library\\/activities\\/\\d+.*");
        }
    }

    public boolean isOnActivitiesListPage() {
        try {
            waitForUrlContains("/library/activities");
            return !getCurrentUrl().matches(".*\\/library\\/activities\\/\\d+.*");
        } catch (Exception e) {
            return !getCurrentUrl().matches(".*\\/library\\/activities\\/\\d+.*");
        }
    }

    public void applyStatusFilter(String statusLabel) {
        String value = STATUS_MAP.getOrDefault(statusLabel, "");
        try {
            WebElement selectEl = waitForVisibility(FILTER_SELECT);
            new Select(selectEl).selectByValue(value);
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception ignored) {}
    }

    public boolean isOnlyStatusDisplayed(String expectedStatusLabel) {
        if ("Semua".equalsIgnoreCase(expectedStatusLabel)
                || "Semua Status".equalsIgnoreCase(expectedStatusLabel)) return true;

        List<WebElement> badges = driver.findElements(STATUS_BADGES_IN_TABLE);
        if (badges.isEmpty()) return isEmptyStateDisplayed();

        for (WebElement badge : badges) {
            String text = badge.getText().trim();
            if (!text.isEmpty() && !text.contains(expectedStatusLabel)) return false;
        }
        return true;
    }

    public boolean isOtherStatusDisplayed(String expectedStatusLabel) {
        List<WebElement> badges = driver.findElements(STATUS_BADGES_IN_TABLE);
        for (WebElement badge : badges) {
            String text = badge.getText().trim();
            if (!text.isEmpty() && !text.contains(expectedStatusLabel)) return true;
        }
        return false;
    }

    public void clickButtonOnFirstRow(String buttonLabel) {
        By locator = By.xpath(
                "(//table//tbody//tr[1]//button[contains(., '" + buttonLabel + "')]"
                + " | //table//tbody//tr[1]//a[contains(., '" + buttonLabel + "')])[1]"
        );
        click(locator);
    }

    public boolean isButtonAvailableOnFirstRow(String buttonLabel) {
        By locator = By.xpath(
                "(//table//tbody//tr[1]//button[contains(., '" + buttonLabel + "')]"
                + " | //table//tbody//tr[1]//a[contains(., '" + buttonLabel + "')])[1]"
        );
        return isDisplayed(locator) && isEnabled(locator);
    }

    public boolean isBrowserConfirmationDisplayed() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    public void dismissBrowserConfirmation() {
        try {
            driver.switchTo().alert().dismiss();
        } catch (NoAlertPresentException ignored) {}
    }

    public void clickConfirmOnDialog() {
        try {
            driver.switchTo().alert().accept();
            Thread.sleep(1500);
        } catch (NoAlertPresentException ignored) {
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean isDetailPageLoaded() {
        return getCurrentUrl().matches(".*\\/library\\/activities\\/\\d+.*");
    }

    public boolean isDetailBookTitleDisplayed() {
        try {
            waitForVisibility(DETAIL_BOOK_TITLE);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDetailStatusDisplayed() {
        return isDisplayed(DETAIL_STATUS);
    }

    public boolean isDetailLoanDateDisplayed() {
        try {
            waitForVisibility(DETAIL_LOAN_DATE);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDetailDurationDisplayed() {
        try {
            waitForVisibility(DETAIL_DURATION);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDetailBorrowerNameDisplayed() {
        try {
            waitForVisibility(DETAIL_BORROWER_NAME);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isStatusBadgeContaining(String status) {
        By badgeLocator = By.xpath(
                "//span[contains(@class,'rounded-full')][contains(text(),'" + status + "')]"
        );
        if (isDisplayed(badgeLocator)) return true;

        By textLocator = By.xpath("//*[normalize-space(text())='" + status + "']");
        return isDisplayed(textLocator);
    }

    public void waitForStatusBadge(String status) {
        By locator = By.xpath("//*[contains(text(),'" + status + "')]");
        try {
            waitForVisibility(locator);
        } catch (Exception ignored) {}
    }

    public void clickBackOrBrowserBack() {
        try {
            waitForVisibility(BACK_BUTTON);
            click(BACK_BUTTON);
        } catch (Exception e) {
            driver.navigate().back();
        }
        try {
            waitFor(d -> getCurrentUrl().endsWith("/library/activities"));
        } catch (Exception ignored) {}
        try {
            waitForInvisibility(LOADING_STATE);
        } catch (Exception ignored) {}
        waitFor(d -> isDisplayed(ACTIVITY_TABLE) || isDisplayed(EMPTY_STATE));
    }
}