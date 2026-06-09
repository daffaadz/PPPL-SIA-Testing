package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.List;

public class LibraryBooksPage extends BasePage {

    private static final By PAGE_HEADING       = By.cssSelector("h1, h2");
    private static final By LOADING_STATE      = By.xpath("//*[contains(text(),'Memuat katalog buku')]");
    private static final By BOOK_COUNT_TEXT    = By.xpath("//*[contains(text(),'Menampilkan')]");
    private static final By EMPTY_STATE        = By.xpath("//*[contains(text(),'Buku tidak ditemukan')]");
    private static final By SEARCH_INPUT       = By.cssSelector("input[type='text'][placeholder*='Cari'], input[type='text'][placeholder*='judul']");
    private static final By FILTER_BUTTON      = By.xpath("//button[contains(text(),'Filter Judul') or contains(.,'Filter Judul')]");
    private static final By SEMUA_PILL         = By.xpath("//button[normalize-space(text())='Semua']");
    private static final By BOOK_GRID          = By.cssSelector("section.grid");
    private static final By ANY_ORDER_BUTTON   = By.xpath("//button[normalize-space(text())='Pesan' or contains(normalize-space(text()),'Pesan')]");

    public LibraryBooksPage openPage() {
        navigateToPath(config.TestConfig.PATH_LIBRARY_BOOKS);
        waitForPageReady();
        return this;
    }

    public void waitForPageReady() {
        try {
            waitForInvisibility(LOADING_STATE);
        } catch (Exception ignored) {}
        waitFor(d -> isDisplayed(BOOK_COUNT_TEXT) || isDisplayed(EMPTY_STATE));
    }

    public void typeSearchKeyword(String keyword) {
        typeIn(SEARCH_INPUT, keyword);
    }

    public void clickFilterJudul() {
        click(FILTER_BUTTON);
        waitForPageReady();
    }

    public void pressEnterOnSearch() {
        WebElement input = waitForClickable(SEARCH_INPUT);
        input.sendKeys(org.openqa.selenium.Keys.ENTER);
        waitForPageReady();
    }

    public void clickCategoryPill(String categoryName) {
        By pill = By.xpath("//button[normalize-space(text())='" + categoryName + "']");
        click(pill);
        waitForPageReady();
    }

    public void clickSemuaPill() {
        click(SEMUA_PILL);
        waitForPageReady();
    }

    public void clickFirstOrderButton() {
        click(ANY_ORDER_BUTTON);
    }

    public int getAvailableStockForBook(String bookTitle) {
        By stockLocator = By.xpath(
                "//article[.//a[normalize-space(text())='" + bookTitle + "'] or .//h3[normalize-space(text())='" + bookTitle + "']]" +
                "//p[contains(normalize-space(text()), 'Stok:')]/span"
        );
        String stockText = getText(stockLocator);
        try {
            return Integer.parseInt(stockText.split("/")[0].trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public void clickOrderButtonForBook(String bookTitle) {
        By buttonLocator = By.xpath(
                "//article[.//a[normalize-space(text())='" + bookTitle + "'] or .//h3[normalize-space(text())='" + bookTitle + "']]" +
                "//button[contains(translate(normalize-space(text()),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'PESAN') or contains(translate(normalize-space(text()),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'STOK HABIS')]"
        );
        click(buttonLocator);
    }

    public boolean isOrderButtonDisabledForBook(String bookTitle) {
        By buttonLocator = By.xpath(
                "//article[.//a[normalize-space(text())='" + bookTitle + "'] or .//h3[normalize-space(text())='" + bookTitle + "']]" +
                "//button"
        );
        return !isEnabled(buttonLocator);
    }

    public boolean isPageLoaded() {
        return getCurrentUrl().contains("/library") && !isDisplayed(LOADING_STATE);
    }

    public boolean isPageHeadingDisplayed() {
        return isDisplayed(PAGE_HEADING);
    }

    public String getBookCountText() {
        try {
            return getText(BOOK_COUNT_TEXT);
        } catch (Exception e) {
            return "";
        }
    }

    public boolean areBooksDisplayed() {
        try {
            waitForVisibility(BOOK_GRID);
            List<WebElement> cards = driver.findElements(By.cssSelector("section.grid > *"));
            return !cards.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isEmptyStateDisplayed() {
        return isDisplayed(EMPTY_STATE);
    }

    public boolean isBookContainingKeywordDisplayed(String keyword) {
        By bookWithKeyword = By.xpath(
                "//*[contains(translate(normalize-space(.),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'"
                        + keyword.toUpperCase() + "')]"
        );
        return isDisplayed(bookWithKeyword);
    }

    public boolean isCategoryPillActive(String categoryName) {
        try {
            By pill = By.xpath("//button[normalize-space(text())='" + categoryName + "']");
            WebElement el = waitForPresence(pill);
            String style = el.getAttribute("style");
            return (style != null && (style.contains("015023") || style.contains("rgb(1, 80, 35)")))
                    || hasClass(pill, "bg-[#015023]");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAnyOrderButtonVisible() {
        return isDisplayed(ANY_ORDER_BUTTON);
    }
}
