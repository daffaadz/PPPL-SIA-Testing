package pages;

import config.TestConfig;
import org.openqa.selenium.By;

public class AdminLibraryPage extends BasePage {

    // Identifiers based on typical management dashboard
    private static final By PAGE_HEADING = By.xpath("//h1[contains(text(), 'Manajemen Perpustakaan')]");
    
    // Stats
    private static final By STAT_TOTAL_BUKU = By.xpath("//p[contains(text(), 'Total Buku')]");
    private static final By STAT_TERSEDIA = By.xpath("//p[contains(text(), 'Tersedia')]");
    private static final By STAT_DIPINJAM = By.xpath("//p[contains(text(), 'Dipinjam')]");
    private static final By STAT_STOK_KRITIS = By.xpath("//p[contains(text(), 'Stok Kritis')]");
    
    // Table
    private static final By TABLE_KATALOG = By.tagName("table");
    
    // Search & Filter
    private static final By SEARCH_INPUT = By.cssSelector("input[placeholder*='Cari']");
    private static final By FILTER_STOK_KRITIS = By.xpath("//button[contains(text(), 'Stok Kritis')]");
    
    // Add Book
    private static final By TAMBAH_BUKU_BTN = By.xpath("//button[contains(text(), 'Tambah Buku')]");
    private static final By FORM_BUKU = By.cssSelector("form");
    private static final By INPUT_JUDUL = By.xpath("//label[contains(., 'Judul Buku')]/following-sibling::input");
    private static final By INPUT_PENULIS = By.xpath("//label[contains(., 'Penulis')]/following-sibling::input");
    private static final By DROPDOWN_KATEGORI = By.xpath("//label[contains(., 'Kategori')]/following-sibling::div[@role='button']");
    private static final By OPTION_PERTAMA_KATEGORI = By.xpath("(//label[contains(., 'Kategori')]/following-sibling::div[last()]//button[not(contains(., 'Tambah Kategori Baru'))])[1]");
    private static final By OPTION_TAMBAH_KATEGORI = By.xpath("//button[contains(., 'Tambah Kategori Baru')]");
    private static final By INPUT_KATEGORI_BARU = By.id("library-category-name");
    private static final By SIMPAN_KATEGORI_BTN = By.xpath("//form[.//h2[contains(., 'Tambah Kategori Baru')]]//button[contains(., 'Simpan')]");
    private static final By INPUT_ISBN = By.xpath("//label[contains(., 'ISBN')]/following-sibling::input");
    private static final By INPUT_PENERBIT = By.xpath("//label[contains(., 'Penerbit')]/following-sibling::input");
    private static final By INPUT_TAHUN = By.xpath("//label[contains(., 'Tahun')]/following-sibling::input");
    private static final By INPUT_STOK = By.xpath("//label[contains(., 'Total Buku')]/following-sibling::input");
    private static final By INPUT_STOK_TERSEDIA = By.xpath("//label[contains(., 'Stok Tersedia')]/following-sibling::input");
    private static final By SIMPAN_BTN = By.xpath("//form[not(.//h2[contains(., 'Tambah Kategori Baru')])]//button[contains(., 'Simpan')]");
    
    // Edit & Delete Book - buttons identified by color class since lucide SVGs have no reliable class
    private static final By EDIT_BTN = By.xpath("(//button[contains(@class, 'text-blue-600')])[1]");
    private static final By HAPUS_BTN = By.xpath("(//button[contains(@class, 'text-red-500')])[1]");

    // Notifications - sonner renders toasts as <li data-sonner-toast> with text in nested elements
    private static final By TOAST_SUCCESS = By.xpath("//li[@data-sonner-toast and contains(., 'berhasil')]");
    
    // Tabs - TypeTabs renders text inside <span> child, so use dot (.) not text()
    private static final By TAB_PEMINJAMAN = By.xpath("//button[contains(., 'Peminjaman')]");
    private static final By TAB_USULAN = By.xpath("//button[contains(., 'Usulan')]");
    
    // Order Filters
    private static final By DROPDOWN_STATUS_TRIGGER = By.xpath("//div[contains(@class, 'min-w-[180px]')]//div[contains(@class, 'cursor-pointer')]");
    private static final By KONFIRMASI_BTN = By.xpath("//button[contains(text(), 'Konfirmasi')]");
    private static final By KEMBALIKAN_BTN = By.xpath("//button[contains(text(), 'Kembalikan')]");
    private static final By CATATAN_ADMIN = By.name("catatan");
    private static final By DETAIL_BTN = By.xpath("//button[contains(text(), 'Detail')]");
    
    // Suggestion Actions
    private static final By SETUJUI_BTN = By.xpath("//button[contains(text(), 'Setujui')]");
    private static final By TOLAK_BTN = By.xpath("//button[contains(text(), 'Tolak')]");
    private static final By PESAN_RESPON = By.name("respon");
    
    public AdminLibraryPage openPage() {
        navigateToPath("/adminpage/perpustakaan"); 
        return this;
    }

    public boolean isDashboardLoaded() {
        return isDisplayed(PAGE_HEADING);
    }

    public boolean areStatsDisplayed() {
        return isDisplayed(STAT_TOTAL_BUKU) &&
               isDisplayed(STAT_TERSEDIA) &&
               isDisplayed(STAT_DIPINJAM) &&
               isDisplayed(STAT_STOK_KRITIS);
    }

    public boolean isTableDisplayed() {
        try {
            waitForVisibility(TABLE_KATALOG);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void searchKeyword(String keyword) {
        typeIn(SEARCH_INPUT, keyword);
    }

    public void clickFilterStokKritis() {
        click(FILTER_STOK_KRITIS);
    }

    public void clickTambahBuku() {
        click(TAMBAH_BUKU_BTN);
    }

    public boolean isFormBukuDisplayed() {
        return isDisplayed(FORM_BUKU);
    }

    public void fillFormBukuValid() {
        long timestamp = System.currentTimeMillis();
        typeIn(INPUT_JUDUL, "Buku Test " + timestamp);
        typeIn(INPUT_PENULIS, "Penulis Test");
        
        // Select category if available
        try {
            click(DROPDOWN_KATEGORI);
            click(OPTION_PERTAMA_KATEGORI);
        } catch (Exception e) {
            // In case no category exists, we might need to click outside to close dropdown
            click(INPUT_ISBN);
        }
        
        // Use part of timestamp to ensure ISBN is within length limits but unique enough
        String uniqueIsbn = "1234" + (timestamp % 1000000);
        typeIn(INPUT_ISBN, uniqueIsbn);
        typeIn(INPUT_PENERBIT, "Penerbit Test");
        typeIn(INPUT_TAHUN, "2023");
        typeIn(INPUT_STOK, "10");
        org.openqa.selenium.WebElement stokTersedia = waitForVisibility(INPUT_STOK_TERSEDIA);
        if (stokTersedia.isEnabled()) {
            typeIn(INPUT_STOK_TERSEDIA, "10");
        }
    }

    public void clickSimpan() {
        click(SIMPAN_BTN);
    }

    public boolean isSuccessNotificationDisplayed() {
        return isSuccessToastDisplayed();
    }

    public void selectTambahKategoriBaru() {
        click(DROPDOWN_KATEGORI);
        click(OPTION_TAMBAH_KATEGORI);
    }

    public void inputKategoriBaru(String kategori) {
        typeIn(INPUT_KATEGORI_BARU, kategori);
    }

    public void clickSimpanKategori() {
        click(SIMPAN_KATEGORI_BTN);
    }

    public void clickEditFirstBook() {
        click(EDIT_BTN); // Clicks first match
    }

    public void updateStok(String stok) {
        typeIn(INPUT_STOK, stok);
    }

    public void updateStokTersedia(String stok) {
        typeIn(INPUT_STOK_TERSEDIA, stok);
    }

    public void updatePenerbit(String penerbit) {
        typeIn(INPUT_PENERBIT, penerbit);
    }

    public void clickHapusFirstBook() {
        jsClick(HAPUS_BTN); // Use jsClick to bypass any potential overlay or interception issues
    }

    public void selectFilterStatus(String status) {
        click(DROPDOWN_STATUS_TRIGGER);
        By optionLocator = By.xpath("//div[contains(@class, 'cursor-pointer') and contains(text(), '" + status + "')]");
        click(optionLocator);
    }

    public boolean isOrderStatusDisplayed(String status) {
        By locator = By.xpath("//td[contains(., '" + status + "')]");
        try {
            waitForVisibility(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void confirmHapus() {
        // The FE now uses a custom modal dialog (AlertConfirmationRedDialog) instead of window.confirm
        By confirmBtnModal = By.xpath("//div[@data-slot='alert-dialog-content']//button[contains(., 'Hapus') or contains(., 'Menghapus')]");
        waitForVisibility(confirmBtnModal);
        jsClick(confirmBtnModal);
    }

    public void openTabPeminjaman() {
        navigateToPath("/adminpage/perpustakaan/order");
        // Wait for page content before clicking tab
        waitForVisibility(By.tagName("main"));
        waitForClickable(TAB_PEMINJAMAN).click();
    }

    public void searchMahasiswa(String query) {
        typeIn(SEARCH_INPUT, query);
        waitForClickable(SEARCH_INPUT).sendKeys(org.openqa.selenium.Keys.ENTER);
    }

    public void clickKonfirmasiFirstOrder() {
        click(KONFIRMASI_BTN);
    }

    public void fillCatatanAdmin(String catatan) {
        if(isDisplayed(CATATAN_ADMIN)) {
            typeIn(CATATAN_ADMIN, catatan);
        }
    }

    public void clickDetailFirstOrder() {
        click(DETAIL_BTN);
    }

    public void clickKembalikanFirstOrder() {
        click(KEMBALIKAN_BTN);
    }

    public void openTabUsulan() {
        navigateToPath("/adminpage/perpustakaan/order");
        // Wait for page content before clicking tab
        waitForVisibility(By.tagName("main"));
        waitForClickable(TAB_USULAN).click();
    }

    public void clickSetujui() {
        click(SETUJUI_BTN);
    }

    public void clickTolak() {
        click(TOLAK_BTN);
    }

    public void fillPesanRespon(String pesan) {
        typeIn(PESAN_RESPON, pesan);
    }
}
