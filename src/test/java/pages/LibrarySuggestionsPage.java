package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class LibrarySuggestionsPage extends BasePage {

    // Page State
    private static final By FORM_HEADING            = By.xpath("//*[contains(text(),'Formulir Usulan Buku')]");
    private static final By PAGE_HEADING            = By.cssSelector("h1, h2");
    private static final By HISTORY_LOADING         = By.xpath("//*[contains(text(),'Memuat riwayat usulan')]");
    private static final By HISTORY_SECTION_HEADING = By.xpath("//h3[contains(text(),'Riwayat Usulan Anda')]");
    // Form Fields
    private static final By INPUT_TITLE             = By.cssSelector("input[placeholder*='judul buku']");
    private static final By INPUT_AUTHOR            = By.cssSelector("input[placeholder*='nama penulis']");
    private static final By TEXTAREA_REASON         = By.cssSelector("textarea[placeholder*='penting']");
    private static final By BUTTON_SUBMIT           = By.xpath("//button[contains(normalize-space(text()),'Kirim Usulan') or contains(.,'Kirim Usulan')]");
    // Validation Errors
    private static final By ERROR_TITLE             = By.xpath("//*[contains(text(),'Judul buku wajib diisi')]");
    private static final By ERROR_AUTHOR            = By.xpath("//*[contains(text(),'Nama penulis wajib diisi')]");
    private static final By ERROR_REASON            = By.xpath("//*[contains(text(),'Alasan usulan')]");
    private static final By ERROR_REASON_MIN_CHAR   = By.xpath("//*[contains(text(),'minimal 20 karakter')]");
    private static final By CHAR_COUNTER            = By.xpath("//p[contains(text(),'karakter') and not(contains(text(),'minimal'))]");

    public LibrarySuggestionsPage openPage() {
        navigateToPath(config.TestConfig.PATH_LIBRARY_SUGGESTIONS);
        waitForPageReady();
        return this;
    }

    public void waitForPageReady() {
        waitForVisibility(FORM_HEADING);
        try {
            waitForInvisibility(HISTORY_LOADING);
        } catch (Exception ignored) {}
    }

    public void fillTitle(String title) {
        typeIn(INPUT_TITLE, title);
    }

    public void fillAuthor(String author) {
        typeIn(INPUT_AUTHOR, author);
    }

    public void fillReason(String reason) {
        typeIn(TEXTAREA_REASON, reason);
    }

    public void clickSubmit() {
        scrollIntoView(BUTTON_SUBMIT);
        click(BUTTON_SUBMIT);
    }

    public boolean isPageLoaded() {
        return getCurrentUrl().contains("/library/suggestions") && isDisplayed(FORM_HEADING);
    }

    public boolean isPageHeadingDisplayed() {
        return isDisplayed(PAGE_HEADING);
    }

    public boolean isFormDisplayed() {
        return isDisplayed(FORM_HEADING);
    }

    public boolean isTitleFieldDisplayed() {
        return isDisplayed(INPUT_TITLE);
    }

    public boolean isAuthorFieldDisplayed() {
        return isDisplayed(INPUT_AUTHOR);
    }

    public boolean isReasonFieldDisplayed() {
        return isDisplayed(TEXTAREA_REASON);
    }

    public boolean isSubmitButtonDisplayed() {
        return isDisplayed(BUTTON_SUBMIT);
    }

    public int getCharacterCount() {
        try {
            String text = getText(CHAR_COUNTER).trim();
            return Integer.parseInt(text.replace(" karakter", "").trim());
        } catch (Exception e) {
            return -1;
        }
    }

    public boolean isTitleErrorDisplayed() {
        return isDisplayed(ERROR_TITLE);
    }

    public boolean isAuthorErrorDisplayed() {
        return isDisplayed(ERROR_AUTHOR);
    }

    public boolean isReasonErrorDisplayed() {
        return isDisplayed(ERROR_REASON);
    }

    public boolean isTitleFieldEmpty() {
        try {
            String value = getValue(INPUT_TITLE);
            return value == null || value.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAuthorFieldEmpty() {
        try {
            String value = getValue(INPUT_AUTHOR);
            return value == null || value.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isReasonFieldEmpty() {
        try {
            String value = getValue(TEXTAREA_REASON);
            return value == null || value.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isReasonMinCharErrorDisplayed() {
        return isDisplayed(ERROR_REASON_MIN_CHAR);
    }

    public boolean isHistorySectionDisplayed() {
        return isDisplayed(HISTORY_SECTION_HEADING);
    }

    public int getSuggestionHistoryCount() {
        try {
            List<WebElement> items = driver.findElements(By.cssSelector("aside article"));
            return items.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isBookTitleInHistory(String title) {
        By locator = By.xpath("//aside//article//*[contains(normalize-space(text()),'" + title + "')]");
        try {
            waitForVisibility(locator);
            return true;
        } catch (Exception e) {
            return isDisplayed(locator);
        }
    }
}
