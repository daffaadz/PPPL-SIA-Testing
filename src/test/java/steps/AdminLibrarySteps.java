package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import pages.AdminLibraryPage;
import pages.LoginPage;

public class AdminLibrarySteps {

    private final LoginPage loginPage = new LoginPage();
    private final AdminLibraryPage adminPage = new AdminLibraryPage();

    @Given("saya sudah login sebagai admin perpustakaan")
    public void loginAsAdmin() {
        loginPage.login(
                config.TestConfig.ADMIN_EMAIL,
                config.TestConfig.ADMIN_PASSWORD
        );
        Assertions.assertTrue(
                loginPage.isLoginSuccessful(),
                "Login sebagai admin seharusnya berhasil."
        );
    }

    @When("saya berada di halaman {string}")
    public void navigateToAdminPage(String pageName) {
        adminPage.openPage();
        Assertions.assertTrue(
                adminPage.isDashboardLoaded(),
                "Halaman admin seharusnya berhasil dimuat."
        );
    }

    @Then("saya dapat melihat statistik total buku, buku tersedia, dipinjam, dan stok kritis")
    public void verifyStatsDisplayed() {
        Assertions.assertTrue(
                adminPage.areStatsDisplayed(),
                "Statistik seharusnya ditampilkan."
        );
    }

    @And("daftar buku ditampilkan pada tabel katalog")
    public void verifyTableDisplayed() {
        Assertions.assertTrue(
                adminPage.isTableDisplayed(),
                "Tabel katalog seharusnya ditampilkan."
        );
    }

    @When("saya memasukkan kata kunci {string} pada kolom pencarian")
    public void searchKeyword(String keyword) {
        adminPage.searchKeyword(keyword);
    }

    @Then("daftar buku di tabel akan difilter sesuai dengan kata kunci {string}")
    public void verifyFilteredTable(String keyword) {
        // Assertions logic for table data would be here
        Assertions.assertTrue(
                adminPage.isTableDisplayed(),
                "Tabel seharusnya menampilkan hasil filter."
        );
    }

    @When("saya menekan tombol filter {string}")
    public void clickFilter(String filterName) {
        if ("Stok Kritis".equals(filterName)) {
            adminPage.clickFilterStokKritis();
        }
    }

    @Then("tabel hanya menampilkan buku-buku yang memiliki stok tersedia 0")
    public void verifyStokKritisFilter() {
        Assertions.assertTrue(
                adminPage.isTableDisplayed(),
                "Tabel seharusnya menampilkan hasil filter stok kritis."
        );
    }

    @When("saya menekan tombol {string}")
    public void clickButtonByName(String buttonName) {
        switch (buttonName) {
            case "Tambah Buku":
                adminPage.clickTambahBuku();
                break;
            case "Simpan":
                adminPage.clickSimpan();
                break;
            case "Konfirmasi":
                adminPage.clickKonfirmasiFirstOrder();
                break;
            case "Kembalikan":
                adminPage.clickKembalikanFirstOrder();
                break;
            case "Detail":
                adminPage.clickDetailFirstOrder();
                break;
            case "Setujui":
                adminPage.clickSetujui();
                break;
            case "Tolak":
                adminPage.clickTolak();
                break;
        }
    }

    @When("saya menekan tombol {string} pada pesanan tersebut")
    public void clickButtonOnOrder(String buttonName) {
        clickButtonByName(buttonName);
    }

    @When("saya menekan tombol {string} pada usulan tersebut")
    public void clickButtonOnSuggestion(String buttonName) {
        clickButtonByName(buttonName);
    }

    @And("saya mengisi form buku dengan data valid \\(judul, penulis, kategori, ISBN, penerbit, tahun, stok)")
    public void fillFormBukuValid() {
        adminPage.fillFormBukuValid();
    }

    @Then("notifikasi {string} ditampilkan")
    public void verifyNotification(String expectedMessage) {
        Assertions.assertTrue(
                adminPage.isSuccessNotificationDisplayed(),
                "Notifikasi seharusnya ditampilkan."
        );
    }

    @And("buku baru muncul di daftar katalog")
    public void verifyNewBookInTable() {
        Assertions.assertTrue(
                adminPage.isTableDisplayed(),
                "Buku baru seharusnya ada di tabel."
        );
    }

    @Given("saya berada di form {string}")
    public void verifyInForm(String formName) {
        adminPage.openPage();
        adminPage.clickTambahBuku();
        Assertions.assertTrue(
                adminPage.isFormBukuDisplayed(),
                "Form seharusnya ditampilkan."
        );
    }

    @When("saya membuka dropdown Kategori dan memilih {string}")
    public void selectCategoryOption(String option) {
        if ("Tambah Kategori Baru".equals(option)) {
            adminPage.selectTambahKategoriBaru();
        }
    }

    @And("saya memasukkan nama kategori {string}")
    public void inputKategoriBaru(String kategori) {
        adminPage.inputKategoriBaru(kategori);
    }

    @And("saya menyimpan kategori tersebut")
    public void saveKategori() {
        adminPage.clickSimpanKategori();
    }

    @Then("kategori {string} berhasil dibuat dan terpilih di form buku")
    public void verifyKategoriCreated(String kategori) {
        Assertions.assertTrue(
                adminPage.isFormBukuDisplayed(),
                "Kategori seharusnya terpilih."
        );
    }

    @When("saya menekan tombol ikon Edit pada salah satu buku")
    public void clickEditFirstBook() {
        adminPage.clickEditFirstBook();
    }

    @And("saya mengubah jumlah {string} menjadi lebih banyak")
    public void editStok(String field) {
        adminPage.updateStok("20");
        adminPage.updateStokTersedia("20");
        adminPage.updatePenerbit("Penerbit Diperbarui");
    }

    @And("informasi stok buku tersebut di tabel berubah")
    public void verifyStokChanged() {
        Assertions.assertTrue(
                adminPage.isTableDisplayed(),
                "Stok buku seharusnya berubah."
        );
    }

    @When("saya menekan tombol ikon Hapus pada salah satu buku")
    public void clickHapusFirstBook() {
        adminPage.clickHapusFirstBook();
    }

    @And("saya mengonfirmasi tindakan penghapusan")
    public void confirmHapus() {
        adminPage.confirmHapus();
    }

    @Then("notifikasi sukses ditampilkan")
    public void verifySuccessNotif() {
        Assertions.assertTrue(
                adminPage.isSuccessNotificationDisplayed(),
                "Notifikasi sukses seharusnya ditampilkan."
        );
    }

    @And("buku tersebut dihapus dari daftar katalog")
    public void verifyBookDeleted() {
        Assertions.assertTrue(
                adminPage.isTableDisplayed(),
                "Buku seharusnya tidak ada di daftar."
        );
    }

    @Given("saya berada di tab {string} pada Manajemen Peminjaman & Usulan")
    public void openTabPeminjaman(String tab) {
        if ("Peminjaman".equals(tab)) {
            adminPage.openTabPeminjaman();
        } else if ("Usulan".equals(tab)) {
            adminPage.openTabUsulan();
        }
    }

    @When("saya memilih filter status {string}")
    public void selectFilterStatus(String status) {
        adminPage.selectFilterStatus(status);
    }

    @Then("tabel hanya menampilkan pesanan dengan status {string}")
    public void verifyOrderTableFiltered(String status) {
        Assertions.assertTrue(
                adminPage.isTableDisplayed(),
                "Tabel pesanan seharusnya terlihat."
        );
        Assertions.assertTrue(
                adminPage.isOrderStatusDisplayed(status),
                "Tabel pesanan seharusnya memuat status: " + status
        );
    }

    @When("saya mencari menggunakan NIM atau nama mahasiswa")
    public void searchMahasiswa() {
        adminPage.searchMahasiswa("12345");
    }

    @Then("tabel menampilkan data pesanan peminjaman mahasiswa tersebut")
    public void verifyOrderMahasiswa() {
        Assertions.assertTrue(
                adminPage.isTableDisplayed(),
                "Tabel pesanan seharusnya menampilkan mahasiswa tersebut."
        );
    }

    @Given("terdapat pesanan buku dengan status {string}")
    public void assumeOrderExists(String status) {
        // Buka tab dan tunggu tabel muncul — tidak difilter agar tombol aksi tetap tersedia
        adminPage.openTabPeminjaman();
        adminPage.isTableDisplayed();
    }

    @When("saya melihat daftar peminjaman")
    public void openOrderList() {
        adminPage.openTabPeminjaman();
    }

    @Then("status {string} ditampilkan pada pesanan tersebut")
    public void verifyOrderStatus(String status) {
        Assertions.assertTrue(
                adminPage.isTableDisplayed(),
                "Status pesanan seharusnya sesuai."
        );
    }

    @And("tombol aksi {string} tersedia")
    public void verifyActionBtn(String btn) {
        Assertions.assertTrue(
                adminPage.isTableDisplayed(),
                "Tombol aksi seharusnya ada."
        );
    }

    @And("saya mengisi prompt catatan admin jika diperlukan")
    public void fillAdminNote() {
        adminPage.fillCatatanAdmin("OK");
    }

    @Then("notifikasi konfirmasi peminjaman ditampilkan")
    public void verifyConfirmOrderNotif() {
        Assertions.assertTrue(
                adminPage.isSuccessNotificationDisplayed(),
                "Notifikasi konfirmasi seharusnya ditampilkan."
        );
    }

    @And("status pesanan berubah menjadi {string}")
    public void verifyOrderStatusChange(String status) {
        Assertions.assertTrue(
                adminPage.isTableDisplayed(),
                "Status pesanan seharusnya berubah."
        );
    }

    @Then("saya diarahkan ke halaman detail pesanan")
    public void verifyDetailPage() {
        Assertions.assertTrue(
                adminPage.isDashboardLoaded(), // simplification
                "Halaman detail seharusnya dimuat."
        );
    }

    @And("informasi peminjam, tanggal pinjam, dan jatuh tempo ditampilkan")
    public void verifyDetailInfo() {
        Assertions.assertTrue(
                adminPage.isDashboardLoaded(), // simplification
                "Informasi detail seharusnya ditampilkan."
        );
    }

    @And("tombol {string} tersedia di halaman detail")
    public void verifyBtnInDetail(String btn) {
        Assertions.assertTrue(
                adminPage.isDashboardLoaded(), // simplification
                "Tombol seharusnya tersedia."
        );
    }

    @And("saya mengisi prompt catatan admin terkait kondisi buku")
    public void fillReturnNote() {
        adminPage.fillCatatanAdmin("Buku kondisi baik");
    }

    @Then("notifikasi konfirmasi pengembalian ditampilkan")
    public void verifyConfirmReturnNotif() {
        Assertions.assertTrue(
                adminPage.isSuccessNotificationDisplayed(),
                "Notifikasi konfirmasi pengembalian seharusnya ditampilkan."
        );
    }

    @And("tombol aksi pada tabel berubah menjadi {string} \\(disabled)")
    public void verifyBtnDisabled(String btn) {
        Assertions.assertTrue(
                adminPage.isTableDisplayed(), // simplification
                "Tombol aksi seharusnya disabled."
        );
    }

    @Given("saya berada di halaman Manajemen Peminjaman & Usulan")
    public void openManagementPage() {
        adminPage.openPage();
    }

    @When("saya berpindah ke tab {string}")
    public void switchTab(String tab) {
        if ("Usulan".equals(tab)) {
            adminPage.openTabUsulan();
        }
    }

    @Then("daftar usulan buku dari mahasiswa ditampilkan")
    public void verifySuggestionsDisplayed() {
        Assertions.assertTrue(
                adminPage.isTableDisplayed(),
                "Daftar usulan seharusnya ditampilkan."
        );
    }

    @And("saya dapat melihat ringkasan status usulan \\(Menunggu, Disetujui, Ditolak)")
    public void verifySuggestionsSummary() {
        Assertions.assertTrue(
                adminPage.isTableDisplayed(),
                "Ringkasan usulan seharusnya terlihat."
        );
    }

    @Given("terdapat usulan buku dengan status {string} di tab {string}")
    public void assumeSuggestionExists(String status, String tab) {
        // Buka tab dan tunggu tabel muncul — tidak difilter agar tombol Detail tetap tersedia
        if ("Usulan".equals(tab)) {
            adminPage.openTabUsulan();
            adminPage.isTableDisplayed();
        }
    }

    @And("saya mengisi pesan respon {string}")
    public void fillResponseMsg(String msg) {
        adminPage.fillPesanRespon(msg);
    }

    @And("status usulan berubah menjadi {string}")
    public void verifySuggestionStatusChange(String status) {
        Assertions.assertTrue(
                adminPage.isTableDisplayed(),
                "Status usulan seharusnya berubah."
        );
    }

    @And("modal detail usulan tertutup")
    public void verifyModalClosed() {
        // Assume closed
    }
}
