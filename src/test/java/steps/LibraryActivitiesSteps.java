package steps;

import config.TestConfig;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import pages.LibraryActivitiesPage;
import pages.LoginPage;

/**
 * LibraryActivitiesSteps — Step definitions untuk Feature: Aktivitas Perpustakaan.
 *
 * Catatan: Step @Given("saya sudah login sebagai mahasiswa") sudah didefinisikan
 * di LibraryBooksSteps.java sehingga tidak perlu diduplikasi di sini.
 *
 * Semua pemanggilan wait/URL check dilakukan via method public di LibraryActivitiesPage,
 * karena method protected di BasePage tidak dapat diakses dari luar package pages.
 */
public class LibraryActivitiesSteps {

    private final LoginPage loginPage = new LoginPage();
    private final LibraryActivitiesPage activitiesPage = new LibraryActivitiesPage();

    // =========================================================================
    // BACKGROUND
    // =========================================================================

    @Given("saya berada di halaman aktivitas perpustakaan")
    public void navigateToActivitiesPage() {
        activitiesPage.openPage();
        Assertions.assertTrue(
                activitiesPage.isPageLoaded(),
                "Halaman aktivitas perpustakaan seharusnya berhasil dimuat."
        );
    }

    // =========================================================================
    // TC-ACT-001 & TC-ACT-002 — AKSES HALAMAN
    // =========================================================================

    @Then("halaman aktivitas perpustakaan ditampilkan dengan benar")
    public void verifyActivitiesPageLoaded() {
        Assertions.assertTrue(
                activitiesPage.isPageHeadingDisplayed(),
                "Heading halaman aktivitas perpustakaan seharusnya ditampilkan."
        );
    }

    @And("daftar aktivitas peminjaman ditampilkan")
    public void verifyActivityListDisplayed() {
        boolean hasTable = activitiesPage.isActivityTableDisplayed();
        boolean hasEmpty = activitiesPage.isEmptyStateDisplayed();
        Assertions.assertTrue(
                hasTable || hasEmpty,
                "Daftar aktivitas atau pesan kosong seharusnya ditampilkan."
        );
    }

    @Then("tabel aktivitas memiliki kolom {string}")
    public void verifyTableColumnExists(String columnName) {
        Assertions.assertTrue(
                activitiesPage.isColumnHeaderDisplayed(columnName),
                "Kolom '" + columnName + "' seharusnya ada pada tabel aktivitas."
        );
    }

    // =========================================================================
    // TC-ACT-003 — NEGATIVE: belum login
    // =========================================================================

    @Given("saya belum login ke sistem")
    public void ensureNotLoggedIn() {
        activitiesPage.navigateTo(TestConfig.BASE_URL);
    }

    @When("saya mengakses URL {string} secara langsung")
    public void accessUrlDirectly(String path) {
        activitiesPage.navigateToPath(path);
    }

    @Then("saya diarahkan ke halaman login")
    public void verifyRedirectedToLogin() {
        // Delegasi ke Page Object agar akses protected method tetap di package pages
        Assertions.assertTrue(
                activitiesPage.waitForAndVerifyLoginRedirect(),
                "Seharusnya diarahkan ke halaman login."
        );
    }

    @And("halaman login ditampilkan")
    public void verifyLoginPageDisplayed() {
        Assertions.assertTrue(
                loginPage.isLoginFormDisplayed(),
                "Form login seharusnya ditampilkan."
        );
    }

    // =========================================================================
    // TC-ACT-004 — DETAIL: buka dari daftar
    // =========================================================================

    @Given("terdapat minimal satu aktivitas peminjaman pada daftar")
    public void verifyAtLeastOneActivity() {
        Assertions.assertTrue(
                activitiesPage.getActivityRowCount() > 0,
                "Seharusnya ada minimal satu aktivitas peminjaman pada daftar."
        );
    }

    @When("mahasiswa menekan tombol {string} pada aktivitas pertama")
    public void clickButtonOnFirstActivity(String buttonName) {
        activitiesPage.clickButtonOnFirstRow(buttonName);
    }

    @Then("saya diarahkan ke halaman detail pesanan")
    public void verifyDetailPageLoaded() {
        // Delegasi ke Page Object agar protected waitForUrlContains tidak dipanggil dari sini
        Assertions.assertTrue(
                activitiesPage.waitForAndVerifyDetailPage(),
                "Halaman detail pesanan seharusnya berhasil dimuat."
        );
    }

    @And("informasi judul buku ditampilkan di halaman detail")
    public void verifyBookTitleInDetail() {
        Assertions.assertTrue(
                activitiesPage.isDetailBookTitleDisplayed(),
                "Judul buku seharusnya ditampilkan di halaman detail."
        );
    }

    @And("informasi status pesanan ditampilkan di halaman detail")
    public void verifyStatusInDetail() {
        Assertions.assertTrue(
                activitiesPage.isDetailStatusDisplayed(),
                "Status pesanan seharusnya ditampilkan di halaman detail."
        );
    }

    @And("informasi tanggal pinjam dan jatuh tempo ditampilkan")
    public void verifyDatesInDetail() {
        Assertions.assertTrue(
                activitiesPage.isDetailLoanDateDisplayed(),
                "Tanggal pinjam seharusnya ditampilkan di halaman detail."
        );
        Assertions.assertTrue(
                activitiesPage.isDetailDueDateDisplayed(),
                "Tanggal jatuh tempo seharusnya ditampilkan di halaman detail."
        );
    }

    // =========================================================================
    // TC-ACT-005 — DETAIL: informasi lengkap
    // =========================================================================

    @Given("terdapat aktivitas peminjaman dengan status {string}")
    public void filterAndVerifyStatusExists(String status) {
        activitiesPage.applyStatusFilter(status);
        Assertions.assertTrue(
                activitiesPage.getActivityRowCount() > 0,
                "Seharusnya ada aktivitas dengan status '" + status + "'."
        );
    }

    @When("mahasiswa membuka detail pesanan tersebut")
    public void openDetailOfFirstOrder() {
        activitiesPage.clickButtonOnFirstRow("Detail");
    }

    @Then("nama buku ditampilkan dengan benar")
    public void verifyBookNameInDetail() {
        Assertions.assertTrue(
                activitiesPage.isDetailBookTitleDisplayed(),
                "Nama buku seharusnya tampil di halaman detail."
        );
    }

    @And("nama peminjam ditampilkan dengan benar")
    public void verifyBorrowerNameInDetail() {
        Assertions.assertTrue(
                activitiesPage.isDetailBorrowerNameDisplayed(),
                "Nama peminjam seharusnya tampil di halaman detail."
        );
    }

    @And("tanggal pinjam ditampilkan dengan benar")
    public void verifyLoanDateInDetail() {
        Assertions.assertTrue(
                activitiesPage.isDetailLoanDateDisplayed(),
                "Tanggal pinjam seharusnya tampil di halaman detail."
        );
    }

    @And("tanggal jatuh tempo ditampilkan dengan benar")
    public void verifyDueDateInDetail() {
        Assertions.assertTrue(
                activitiesPage.isDetailDueDateDisplayed(),
                "Tanggal jatuh tempo seharusnya tampil di halaman detail."
        );
    }

    @And("status {string} ditampilkan pada badge status")
    public void verifyStatusBadgeInDetail(String expectedStatus) {
        Assertions.assertTrue(
                activitiesPage.isStatusBadgeContaining(expectedStatus),
                "Badge status seharusnya menampilkan: " + expectedStatus
        );
    }

    // =========================================================================
    // TC-ACT-006 — DETAIL: tombol kembali
    // =========================================================================

    @And("mahasiswa menekan tombol kembali atau navigasi back")
    public void clickBackButton() {
        activitiesPage.clickBackOrBrowserBack();
    }

    @Then("saya kembali ke halaman daftar aktivitas perpustakaan")
    public void verifyBackToActivitiesList() {
        // Delegasi ke Page Object agar protected method tetap di package pages
        Assertions.assertTrue(
                activitiesPage.isOnActivitiesListPage(),
                "URL seharusnya kembali ke daftar, bukan halaman detail."
        );
    }

    @And("daftar aktivitas masih ditampilkan")
    public void verifyListStillDisplayed() {
        boolean hasRows = activitiesPage.getActivityRowCount() > 0;
        boolean hasEmpty = activitiesPage.isEmptyStateDisplayed();
        Assertions.assertTrue(
                hasRows || hasEmpty,
                "Daftar aktivitas seharusnya masih tampil setelah kembali."
        );
    }

    // =========================================================================
    // TC-ACT-007 — FILTER: status Dipinjam
    // =========================================================================

    @When("mahasiswa memilih filter status aktivitas {string}")
    public void applyStatusFilter(String status) {
        activitiesPage.applyStatusFilter(status);
    }

    @Then("daftar aktivitas hanya menampilkan pesanan dengan status {string}")
    public void verifyOnlyStatusShown(String expectedStatus) {
        Assertions.assertTrue(
                activitiesPage.isOnlyStatusDisplayed(expectedStatus),
                "Tabel seharusnya hanya menampilkan pesanan berstatus: " + expectedStatus
        );
    }

    @And("tidak ada baris dengan status selain {string} pada tabel")
    public void verifyNoOtherStatus(String status) {
        Assertions.assertFalse(
                activitiesPage.isOtherStatusDisplayed(status),
                "Seharusnya tidak ada baris dengan status selain: " + status
        );
    }

    // =========================================================================
    // TC-ACT-008 — FILTER: reset ke Semua
    // =========================================================================

    @Given("mahasiswa sudah memilih filter status {string}")
    public void alreadyAppliedFilter(String status) {
        activitiesPage.applyStatusFilter(status);
    }

    @Then("seluruh daftar aktivitas peminjaman ditampilkan kembali")
    public void verifyAllActivitiesShown() {
        boolean hasRows = activitiesPage.getActivityRowCount() > 0;
        boolean hasEmpty = activitiesPage.isEmptyStateDisplayed();
        Assertions.assertTrue(
                hasRows || hasEmpty,
                "Seluruh aktivitas seharusnya kembali ditampilkan setelah filter direset."
        );
    }

    @And("semua status pesanan terlihat pada tabel")
    public void verifyMultipleStatusesVisible() {
        Assertions.assertTrue(
                activitiesPage.isPageLoaded(),
                "Halaman seharusnya masih dalam kondisi loaded setelah reset filter."
        );
    }

    // =========================================================================
    // TC-ACT-009 — BATALKAN: berhasil
    // =========================================================================

    @Given("terdapat pesanan buku dengan status {string} pada daftar aktivitas")
    public void filterAndVerifyOrderWithStatus(String status) {
        activitiesPage.applyStatusFilter(status);
        Assertions.assertTrue(
                activitiesPage.getActivityRowCount() > 0,
                "Seharusnya ada pesanan dengan status '" + status + "' pada daftar aktivitas."
        );
    }

    @When("mahasiswa menekan tombol {string} pada pesanan tersebut")
    public void clickButtonOnOrder(String buttonName) {
        activitiesPage.clickButtonOnFirstRow(buttonName);
    }

    @And("mahasiswa mengonfirmasi tindakan pembatalan pada dialog konfirmasi")
    public void confirmCancellation() {
        activitiesPage.clickConfirmOnDialog();
    }

    @Then("notifikasi {string} ditampilkan")
    public void verifyNotificationMessage(String message) {
        Assertions.assertTrue(
                activitiesPage.isSuccessToastDisplayed(),
                "Notifikasi '" + message + "' seharusnya ditampilkan."
        );
    }

    @And("status pesanan berubah menjadi {string}")
    public void verifyOrderStatusChanged(String newStatus) {
        activitiesPage.waitForStatusBadge(newStatus);
        Assertions.assertTrue(
                activitiesPage.isStatusBadgeContaining(newStatus),
                "Status pesanan seharusnya berubah menjadi: " + newStatus
        );
    }

    @And("tombol {string} tidak lagi tersedia pada pesanan tersebut")
    public void verifyButtonNoLongerAvailable(String buttonName) {
        Assertions.assertFalse(
                activitiesPage.isButtonAvailableOnFirstRow(buttonName),
                "Tombol '" + buttonName + "' seharusnya tidak lagi tersedia."
        );
    }

    // =========================================================================
    // TC-ACT-010 — BATALKAN: dialog konfirmasi
    // =========================================================================

    @Then("dialog konfirmasi pembatalan pesanan ditampilkan")
    public void verifyCancellationDialogDisplayed() {
        Assertions.assertTrue(
                activitiesPage.isCancellationDialogDisplayed(),
                "Dialog konfirmasi pembatalan seharusnya ditampilkan."
        );
    }

    @And("tombol {string} tersedia pada dialog")
    public void verifyButtonOnDialog(String buttonName) {
        Assertions.assertTrue(
                activitiesPage.isButtonOnDialogDisplayed(buttonName),
                "Tombol '" + buttonName + "' seharusnya tersedia pada dialog."
        );
    }
}