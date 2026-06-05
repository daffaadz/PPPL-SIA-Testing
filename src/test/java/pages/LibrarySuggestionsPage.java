package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * LibrarySuggestionsPage — Page Object for /library/suggestions (Halaman Usulan Buku).
 *
 * Covers:
 *  - Form usulan buku (judul, penulis, alasan) + validasi
 *  - Counter karakter alasan
 *  - Tombol kirim usulan & status loading (submitting)
 *  - Toast notifikasi sukses (Sonner)
 *  - Sidebar riwayat usulan buku
 */
public class LibrarySuggestionsPage extends BasePage {

    // ─── Locators — Page Structure ────────────────────────────────────────────
    /** Heading utama formulir */
    private static final By FORM_HEADING = By.xpath(
            "//*[contains(text(),'Formulir Usulan Buku')]"
    );
    /** Section judul halaman (LibraryShell title) */
    private static final By PAGE_HEADING = By.xpath(
            "//*[contains(text(),'Usulan Buku')]"
    );
    /** Loading riwayat di sidebar */
    private static final By HISTORY_LOADING = By.xpath(
            "//*[contains(text(),'Memuat riwayat usulan')]"
    );
    /** Empty state riwayat di sidebar */
    private static final By HISTORY_EMPTY = By.xpath(
            "//*[contains(text(),'Belum ada usulan buku')]"
    );
    /** Section heading riwayat */
    private static final By HISTORY_SECTION_HEADING = By.xpath(
            "//h3[contains(text(),'Riwayat Usulan Anda')]"
    );

    // ─── Locators — Form Fields ───────────────────────────────────────────────
    /** Input field judul buku */
    private static final By INPUT_TITLE = By.cssSelector(
            "input[placeholder*='judul buku']"
    );
    /** Input field nama penulis */
    private static final By INPUT_AUTHOR = By.cssSelector(
            "input[placeholder*='nama penulis']"
    );
    /** Textarea alasan usulan */
    private static final By TEXTAREA_REASON = By.cssSelector(
            "textarea[placeholder*='penting']"
    );
    /** Tombol submit "Kirim Usulan" */
    private static final By BUTTON_SUBMIT = By.xpath(
            "//button[contains(normalize-space(text()),'Kirim Usulan') or contains(.,'Kirim Usulan')]"
    );
    /** Teks counter karakter di bawah textarea alasan */
    private static final By CHAR_COUNTER = By.xpath(
            "//*[contains(text(),' karakter') and not(contains(text(),'minimal'))]"
    );

    // ─── Locators — Validation Error Messages ─────────────────────────────────
    /** Error message: judul wajib diisi */
    private static final By ERROR_TITLE = By.xpath(
            "//*[contains(text(),'Judul buku wajib diisi')]"
    );
    /** Error message: penulis wajib diisi */
    private static final By ERROR_AUTHOR = By.xpath(
            "//*[contains(text(),'Nama penulis wajib diisi')]"
    );
    /**
     * Error message alasan — bisa "Alasan usulan wajib diisi"
     * atau "Alasan usulan minimal 20 karakter"
     */
    private static final By ERROR_REASON = By.xpath(
            "//*[contains(text(),'Alasan usulan')]"
    );

    // ─── Locators — Toast Notification ───────────────────────────────────────
    /** Sonner toast sukses setelah berhasil mengirim usulan */
    private static final By SUCCESS_TOAST = By.xpath(
            "//*[@data-sonner-toast] | //*[contains(text(),'berhasil dikirim') or contains(text(),'Usulan buku berhasil')]"
    );

    // ─── Actions — Navigation ─────────────────────────────────────────────────

    /**
     * Navigate langsung ke halaman /library/suggestions dan tunggu sampai siap.
     */
    public LibrarySuggestionsPage openPage() {
        navigateToPath(config.TestConfig.PATH_LIBRARY_SUGGESTIONS);
        waitForPageReady();
        return this;
    }

    /**
     * Tunggu sampai halaman selesai dimuat:
     * form heading visible dan loading riwayat selesai.
     */
    public void waitForPageReady() {
        waitForVisibility(FORM_HEADING);
        try {
            waitForInvisibility(HISTORY_LOADING);
        } catch (Exception ignored) {
            // Loading riwayat mungkin sudah selesai sangat cepat
        }
    }

    // ─── Actions — Form Interaction ───────────────────────────────────────────

    /**
     * Isi field judul buku.
     *
     * @param title judul buku yang akan diusulkan
     */
    public void fillTitle(String title) {
        typeIn(INPUT_TITLE, title);
    }

    /**
     * Isi field nama penulis.
     *
     * @param author nama penulis buku
     */
    public void fillAuthor(String author) {
        typeIn(INPUT_AUTHOR, author);
    }

    /**
     * Isi textarea alasan usulan.
     *
     * @param reason alasan mengusulkan buku
     */
    public void fillReason(String reason) {
        typeIn(TEXTAREA_REASON, reason);
    }

    /**
     * Klik tombol "Kirim Usulan" untuk submit form.
     */
    public void clickSubmit() {
        scrollIntoView(BUTTON_SUBMIT);
        click(BUTTON_SUBMIT);
    }

    /**
     * Helper: isi semua field form sekaligus dan klik submit.
     *
     * @param title  judul buku
     * @param author nama penulis
     * @param reason alasan usulan
     */
    public void submitForm(String title, String author, String reason) {
        fillTitle(title);
        fillAuthor(author);
        fillReason(reason);
        clickSubmit();
    }

    // ─── Assertions — Page & Form Display ────────────────────────────────────

    /**
     * Returns true jika halaman usulan buku berhasil dimuat
     * (URL mengandung /library/suggestions dan heading tampil).
     */
    public boolean isPageLoaded() {
        return getCurrentUrl().contains("/library/suggestions") && isDisplayed(FORM_HEADING);
    }

    /**
     * Returns true jika heading halaman (Usulan Buku) tampil.
     */
    public boolean isPageHeadingDisplayed() {
        return isDisplayed(PAGE_HEADING);
    }

    /**
     * Returns true jika form usulan (heading "Formulir Usulan Buku") tampil.
     */
    public boolean isFormDisplayed() {
        return isDisplayed(FORM_HEADING);
    }

    /**
     * Returns true jika input field judul buku tampil.
     */
    public boolean isTitleFieldDisplayed() {
        return isDisplayed(INPUT_TITLE);
    }

    /**
     * Returns true jika input field nama penulis tampil.
     */
    public boolean isAuthorFieldDisplayed() {
        return isDisplayed(INPUT_AUTHOR);
    }

    /**
     * Returns true jika textarea alasan tampil.
     */
    public boolean isReasonFieldDisplayed() {
        return isDisplayed(TEXTAREA_REASON);
    }

    /**
     * Returns true jika tombol "Kirim Usulan" tampil.
     */
    public boolean isSubmitButtonDisplayed() {
        return isDisplayed(BUTTON_SUBMIT);
    }

    // ─── Assertions — Character Counter ──────────────────────────────────────

    /**
     * Ambil angka counter karakter dari teks "N karakter" di bawah textarea alasan.
     * Returns -1 jika tidak ditemukan.
     */
    public int getCharacterCount() {
        try {
            String text = getText(CHAR_COUNTER).trim(); // contoh: "26 karakter"
            return Integer.parseInt(text.replace(" karakter", "").trim());
        } catch (Exception e) {
            return -1;
        }
    }

    // ─── Assertions — Validation Errors ──────────────────────────────────────

    /**
     * Returns true jika pesan error judul buku wajib diisi tampil.
     */
    public boolean isTitleErrorDisplayed() {
        return isDisplayed(ERROR_TITLE);
    }

    /**
     * Returns true jika pesan error nama penulis wajib diisi tampil.
     */
    public boolean isAuthorErrorDisplayed() {
        return isDisplayed(ERROR_AUTHOR);
    }

    /**
     * Returns true jika pesan error alasan tampil
     * (baik "wajib diisi" maupun "minimal 20 karakter").
     */
    public boolean isReasonErrorDisplayed() {
        return isDisplayed(ERROR_REASON);
    }

    // ─── Assertions — Form Reset ──────────────────────────────────────────────

    /**
     * Returns true jika field judul buku sudah kosong (form direset setelah submit).
     */
    public boolean isTitleFieldEmpty() {
        try {
            String value = getValue(INPUT_TITLE);
            return value == null || value.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns true jika field penulis sudah kosong.
     */
    public boolean isAuthorFieldEmpty() {
        try {
            String value = getValue(INPUT_AUTHOR);
            return value == null || value.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns true jika textarea alasan sudah kosong.
     */
    public boolean isReasonFieldEmpty() {
        try {
            String value = getValue(TEXTAREA_REASON);
            return value == null || value.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    // ─── Assertions — Toast Notification ─────────────────────────────────────

    /**
     * Returns true jika toast notifikasi sukses pengiriman usulan tampil.
     */
    public boolean isSuccessToastDisplayed() {
        try {
            waitForToast(SUCCESS_TOAST);
            return true;
        } catch (Exception e) {
            return isDisplayed(SUCCESS_TOAST);
        }
    }

    // ─── Assertions — Suggestion History ─────────────────────────────────────

    /**
     * Returns true jika section riwayat usulan tampil di sidebar.
     */
    public boolean isHistorySectionDisplayed() {
        return isDisplayed(HISTORY_SECTION_HEADING);
    }

    /**
     * Hitung jumlah item usulan yang tampil di sidebar riwayat.
     */
    public int getSuggestionHistoryCount() {
        try {
            List<WebElement> items = driver.findElements(
                    By.cssSelector("aside article")
            );
            return items.size();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Returns true jika judul tertentu muncul di sidebar riwayat usulan.
     * Berguna untuk memverifikasi usulan baru berhasil muncul setelah submit.
     *
     * @param title judul buku yang dicari di riwayat
     */
    public boolean isBookTitleInHistory(String title) {
        By locator = By.xpath(
                "//aside//article//*[contains(normalize-space(text()),'" + title + "')]"
        );
        // Tunggu sebentar agar sidebar sempat refresh setelah submit
        try {
            waitForVisibility(locator);
            return true;
        } catch (Exception e) {
            return isDisplayed(locator);
        }
    }
}
