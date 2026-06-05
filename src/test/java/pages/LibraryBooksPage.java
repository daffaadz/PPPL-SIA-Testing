package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * LibraryBooksPage — Page Object for /library/books (Halaman Katalog Buku).
 *
 * Covers:
 *  - Search bar input & submit (via button or Enter key)
 *  - Category pill filter buttons (Semua, Informatika, dll.)
 *  - Book card listing & availability check
 *  - Order book button per card
 *  - Empty state & loading state
 *  - Toast notification after successful order
 */
public class LibraryBooksPage extends BasePage {

    // ─── Locators — Page Structure ────────────────────────────────────────────
    private static final By PAGE_HEADING       = By.cssSelector("h1, h2");
    private static final By LOADING_STATE      = By.xpath("//*[contains(text(),'Memuat katalog buku')]");
    private static final By BOOK_COUNT_TEXT    = By.xpath("//*[contains(text(),'Menampilkan')]");
    private static final By EMPTY_STATE        = By.xpath("//*[contains(text(),'Buku tidak ditemukan')]");

    // ─── Locators — Search Bar ────────────────────────────────────────────────
    private static final By SEARCH_INPUT       = By.cssSelector(
            "input[type='text'][placeholder*='Cari'], input[type='text'][placeholder*='judul']"
    );
    private static final By FILTER_BUTTON      = By.xpath(
            "//button[contains(text(),'Filter Judul') or contains(.,'Filter Judul')]"
    );

    // ─── Locators — Category Pills ────────────────────────────────────────────
    private static final By SEMUA_PILL         = By.xpath(
            "//button[normalize-space(text())='Semua']"
    );

    // ─── Locators — Book Cards ────────────────────────────────────────────────
    private static final By BOOK_GRID          = By.cssSelector(
            "section.grid"
    );
    // Generic "Pesan" button — any visible order button in the card grid
    private static final By ANY_ORDER_BUTTON   = By.xpath(
            "//button[normalize-space(text())='Pesan' or contains(normalize-space(text()),'Pesan')]"
    );

    // ─── Actions ──────────────────────────────────────────────────────────────

    /**
     * Navigate directly to /library/books and wait for the page to stabilise.
     */
    public LibraryBooksPage openPage() {
        navigateToPath(config.TestConfig.PATH_LIBRARY_BOOKS);
        waitForPageReady();
        return this;
    }

    /**
     * Wait until the loading spinner disappears and at least the counter text
     * or the empty state is visible.
     */
    public void waitForPageReady() {
        // Wait until loading message is gone
        try {
            waitForInvisibility(LOADING_STATE);
        } catch (Exception ignored) {
            // Loading state may have already disappeared
        }
        // Then ensure the book count text or empty state is present
        waitFor(d -> isDisplayed(BOOK_COUNT_TEXT) || isDisplayed(EMPTY_STATE));
    }

    /**
     * Type a keyword into the search bar input field.
     *
     * @param keyword text to search for
     */
    public void typeSearchKeyword(String keyword) {
        typeIn(SEARCH_INPUT, keyword);
    }

    /**
     * Click the "Filter Judul" button to submit the search.
     */
    public void clickFilterJudul() {
        click(FILTER_BUTTON);
        waitForPageReady();
    }

    /**
     * Press the Enter key while focus is on the search input.
     */
    public void pressEnterOnSearch() {
        WebElement input = waitForClickable(SEARCH_INPUT);
        input.sendKeys(org.openqa.selenium.Keys.ENTER);
        waitForPageReady();
    }

    /**
     * Click the category pill button with the given name (e.g., "Informatika").
     *
     * @param categoryName exact text of the category pill
     */
    public void clickCategoryPill(String categoryName) {
        By pill = By.xpath(
                "//button[normalize-space(text())='" + categoryName + "']"
        );
        click(pill);
        waitForPageReady();
    }

    /**
     * Click the "Semua" pill to clear the category filter.
     */
    public void clickSemuaPill() {
        click(SEMUA_PILL);
        waitForPageReady();
    }

    /**
     * Click the "Pesan" button on the first available book card.
     * Uses the first visible "Pesan" button in the grid.
     */
    public void clickFirstOrderButton() {
        click(ANY_ORDER_BUTTON);
    }

    // ─── Assertions ───────────────────────────────────────────────────────────

    /**
     * Returns true if the page is loaded (URL contains /library and not loading).
     */
    public boolean isPageLoaded() {
        return getCurrentUrl().contains("/library") && !isDisplayed(LOADING_STATE);
    }

    /**
     * Returns true if the page heading is visible.
     */
    public boolean isPageHeadingDisplayed() {
        return isDisplayed(PAGE_HEADING);
    }

    /**
     * Returns the text of the "Menampilkan X buku" counter.
     */
    public String getBookCountText() {
        try {
            return getText(BOOK_COUNT_TEXT);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Returns true if at least one book card is shown in the grid.
     */
    public boolean areBooksDisplayed() {
        try {
            waitForVisibility(BOOK_GRID);
            List<WebElement> cards = driver.findElements(
                    By.cssSelector("section.grid > *")
            );
            return !cards.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns true if the "Buku tidak ditemukan" empty state message is visible.
     */
    public boolean isEmptyStateDisplayed() {
        return isDisplayed(EMPTY_STATE);
    }

    /**
     * Returns true if any book card contains the given keyword in its title area.
     *
     * @param keyword text to look for (case-insensitive partial match via XPath)
     */
    public boolean isBookContainingKeywordDisplayed(String keyword) {
        By bookWithKeyword = By.xpath(
                "//*[contains(translate(normalize-space(.),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'"
                        + keyword.toUpperCase() + "')]"
        );
        return isDisplayed(bookWithKeyword);
    }

    /**
     * Returns true if the specified category pill currently has the active
     * (green/dark) styling. Active pill has inline style background #015023.
     *
     * @param categoryName exact text of the category pill
     */
    public boolean isCategoryPillActive(String categoryName) {
        try {
            By pill = By.xpath(
                    "//button[normalize-space(text())='" + categoryName + "']"
            );
            WebElement el = waitForPresence(pill);
            String style = el.getAttribute("style");
            // Active pill has background-color: rgb(1, 80, 35) OR #015023
            return (style != null && (style.contains("015023") || style.contains("rgb(1, 80, 35)")))
                    || hasClass(pill, "bg-[#015023]");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns true if at least one "Pesan" button is visible on the page,
     * indicating there is at least one available book.
     */
    public boolean isAnyOrderButtonVisible() {
        return isDisplayed(ANY_ORDER_BUTTON);
    }
}
