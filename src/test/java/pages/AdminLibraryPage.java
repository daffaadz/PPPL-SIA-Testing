package pages;

import config.TestConfig;
import org.openqa.selenium.By;

public class AdminLibraryPage extends BasePage {

    // Identifiers based on typical management dashboard
    private static final By PAGE_HEADING = By.xpath("//h1[contains(text(), 'Manajemen Perpustakaan')]");
    
    // Stats
    private static final By STAT_TOTAL_BUKU = By.xpath("//div[contains(text(), 'Total Buku')]");
    private static final By STAT_TERSEDIA = By.xpath("//div[contains(text(), 'Tersedia')]");
    private static final By STAT_DIPINJAM = By.xpath("//div[contains(text(), 'Dipinjam')]");
    private static final By STAT_STOK_KRITIS = By.xpath("//div[contains(text(), 'Stok Kritis')]");
    
    // Table
    private static final By TABLE_KATALOG = By.tagName("table");
    
    // Search & Filter
    private static final By SEARCH_INPUT = By.cssSelector("input[placeholder*='Cari']");
    private static final By FILTER_STOK_KRITIS = By.xpath("//button[contains(text(), 'Stok Kritis')]");
    
    // Add Book
    private static final By TAMBAH_BUKU_BTN = By.xpath("//button[contains(text(), 'Tambah Buku')]");
    private static final By FORM_BUKU = By.cssSelector("form");
    private static final By INPUT_JUDUL = By.name("judul");
    private static final By INPUT_PENULIS = By.name("penulis");
    private static final By DROPDOWN_KATEGORI = By.name("kategori");
    private static final By OPTION_TAMBAH_KATEGORI = By.xpath("//option[contains(text(), 'Tambah Kategori Baru')]");
    private static final By INPUT_KATEGORI_BARU = By.name("kategoriBaru");
    private static final By SIMPAN_KATEGORI_BTN = By.xpath("//button[contains(text(), 'Simpan Kategori')]");
    private static final By INPUT_ISBN = By.name("isbn");
    private static final By INPUT_PENERBIT = By.name("penerbit");
    private static final By INPUT_TAHUN = By.name("tahun");
    private static final By INPUT_STOK = By.name("stok");
    private static final By SIMPAN_BTN = By.xpath("//button[contains(text(), 'Simpan')]");
    
    // Edit & Delete Book
    private static final By EDIT_BTN = By.cssSelector("button[aria-label='Edit']");
    private static final By HAPUS_BTN = By.cssSelector("button[aria-label='Hapus']");
    private static final By CONFIRM_HAPUS_BTN = By.xpath("//button[contains(text(), 'Hapus')]"); // Assuming confirmation modal

    // Notifications
    private static final By TOAST_SUCCESS = By.xpath("//div[contains(@class, 'toast') and contains(text(), 'berhasil')]");
    
    // Tabs
    private static final By TAB_PEMINJAMAN = By.xpath("//button[contains(text(), 'Peminjaman')]");
    private static final By TAB_USULAN = By.xpath("//button[contains(text(), 'Usulan')]");
    
    // Order Actions
    private static final By FILTER_STATUS = By.name("statusFilter");
    private static final By SEARCH_MAHASISWA = By.cssSelector("input[placeholder*='Cari NIM']");
    private static final By ORDER_STATUS_DIPESAN = By.xpath("//td[contains(text(), 'Dipesan')]");
    private static final By KONFIRMASI_BTN = By.xpath("//button[contains(text(), 'Konfirmasi')]");
    private static final By KEMBALIKAN_BTN = By.xpath("//button[contains(text(), 'Kembalikan')]");
    private static final By CATATAN_ADMIN = By.name("catatan");
    private static final By DETAIL_BTN = By.xpath("//button[contains(text(), 'Detail')]");
    
    // Suggestion Actions
    private static final By SETUJUI_BTN = By.xpath("//button[contains(text(), 'Setujui')]");
    private static final By TOLAK_BTN = By.xpath("//button[contains(text(), 'Tolak')]");
    private static final By PESAN_RESPON = By.name("respon");
    
    public AdminLibraryPage openPage() {
        // Assuming path is /admin/library or similar, if not provided we just wait for heading
        navigateToPath("/admin/library"); 
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
        return isDisplayed(TABLE_KATALOG);
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
        typeIn(INPUT_JUDUL, "Buku Test");
        typeIn(INPUT_PENULIS, "Penulis Test");
        typeIn(INPUT_ISBN, "1234567890");
        typeIn(INPUT_PENERBIT, "Penerbit Test");
        typeIn(INPUT_TAHUN, "2023");
        typeIn(INPUT_STOK, "10");
    }

    public void clickSimpan() {
        click(SIMPAN_BTN);
    }

    public boolean isSuccessNotificationDisplayed() {
        return isDisplayed(TOAST_SUCCESS);
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

    public void clickHapusFirstBook() {
        click(HAPUS_BTN); // Clicks first match
    }

    public void confirmHapus() {
        click(CONFIRM_HAPUS_BTN);
    }

    public void openTabPeminjaman() {
        click(TAB_PEMINJAMAN);
    }

    public void filterStatus(String status) {
        typeIn(FILTER_STATUS, status); // Simplify for select
    }

    public void searchMahasiswa(String query) {
        typeIn(SEARCH_MAHASISWA, query);
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
        click(TAB_USULAN);
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
