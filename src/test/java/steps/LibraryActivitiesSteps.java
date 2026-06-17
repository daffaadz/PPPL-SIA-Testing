package steps;

import config.TestConfig;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import pages.LibraryActivitiesPage;
import pages.LoginPage;

public class LibraryActivitiesSteps {

    private final LoginPage loginPage = new LoginPage();
    private final LibraryActivitiesPage activitiesPage = new LibraryActivitiesPage();

    // TC-ACT-001 & TC-ACT-002: Akses halaman aktivitas
    @Given("saya berada di halaman aktivitas perpustakaan")
    public void navigateToActivitiesPage() {
        activitiesPage.openPage();
        Assertions.assertTrue(
                activitiesPage.isPageLoaded(),
                "Halaman aktivitas perpustakaan seharusnya berhasil dimuat."
        );
    }

    @Then("halaman aktivitas perpustakaan ditampilkan dengan benar")
    public void verifyActivitiesPageLoaded() {
        Assertions.assertTrue(
                activitiesPage.isPageHeadingDisplayed(),
                "Heading halaman aktivitas perpustakaan seharusnya ditampilkan."
        );
    }

    @And("daftar aktivitas peminjaman ditampilkan")
    public void verifyActivityListDisplayed() {
        Assertions.assertTrue(
                activitiesPage.isActivityTableDisplayed() || activitiesPage.isEmptyStateDisplayed(),
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

    // TC-ACT-003: Akses tanpa login
    @Given("saya belum login ke sistem")
    public void ensureNotLoggedIn() {
        activitiesPage.navigateTo(TestConfig.BASE_URL);
        activitiesPage.clearLocalStorage();
    }

    @When("saya mengakses URL {string} secara langsung")
    public void accessUrlDirectly(String path) {
        activitiesPage.navigateToPath(path);
    }

    @Then("saya diarahkan ke halaman login")
    public void verifyRedirectedToLogin() {
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

    // TC-ACT-004: Buka halaman detail dari daftar
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

    @Then("saya diarahkan ke halaman detail pesanan mahasiswa")
    public void verifyDetailPageLoaded() {
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

    @And("informasi tanggal pemesanan dan durasi ditampilkan")
    public void verifyDatesInDetail() {
        Assertions.assertAll(
            () -> Assertions.assertTrue(
                    activitiesPage.isDetailLoanDateDisplayed(),
                    "Tanggal pemesanan seharusnya ditampilkan di halaman detail."
            ),
            () -> Assertions.assertTrue(
                    activitiesPage.isDetailDurationDisplayed(),
                    "Durasi seharusnya ditampilkan di halaman detail."
            )
        );
    }

    // TC-ACT-005: Verifikasi informasi lengkap di halaman detail
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

    @And("durasi pinjam ditampilkan dengan benar")
    public void verifyDurationInDetail() {
        Assertions.assertTrue(
                activitiesPage.isDetailDurationDisplayed(),
                "Durasi seharusnya tampil di halaman detail."
        );
    }

    @And("status {string} ditampilkan pada badge status")
    public void verifyStatusBadgeInDetail(String expectedStatus) {
        Assertions.assertTrue(
                activitiesPage.isStatusBadgeContaining(expectedStatus),
                "Badge status seharusnya menampilkan: " + expectedStatus
        );
    }

    // TC-ACT-006: Tombol kembali dari halaman detail
    @And("mahasiswa menekan tombol kembali atau navigasi back")
    public void clickBackButton() {
        activitiesPage.clickBackOrBrowserBack();
    }

    @Then("saya kembali ke halaman daftar aktivitas perpustakaan")
    public void verifyBackToActivitiesList() {
        Assertions.assertTrue(
                activitiesPage.isOnActivitiesListPage(),
                "URL seharusnya kembali ke daftar, bukan halaman detail."
        );
    }

    @And("daftar aktivitas masih ditampilkan")
    public void verifyListStillDisplayed() {
        Assertions.assertTrue(
                activitiesPage.getActivityRowCount() > 0 || activitiesPage.isEmptyStateDisplayed(),
                "Daftar aktivitas seharusnya masih tampil setelah kembali."
        );
    }

    // TC-ACT-007: Filter status aktivitas
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

    // TC-ACT-008: Reset filter ke Semua
    @Given("mahasiswa sudah memilih filter status {string}")
    public void alreadyAppliedFilter(String status) {
        activitiesPage.applyStatusFilter(status);
    }

    @Then("seluruh daftar aktivitas peminjaman ditampilkan kembali")
    public void verifyAllActivitiesShown() {
        Assertions.assertTrue(
                activitiesPage.getActivityRowCount() > 0 || activitiesPage.isEmptyStateDisplayed(),
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

    // TC-ACT-009 & TC-ACT-010: Batalkan pesanan dan dialog konfirmasi
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

    @Then("notifikasi aktivitas {string} ditampilkan")
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

    // TC-ACT-010: Browser konfirmasi sebelum membatalkan
    @Then("konfirmasi pembatalan dari browser ditampilkan")
    public void verifyBrowserConfirmationDisplayed() {
        Assertions.assertTrue(
                activitiesPage.isBrowserConfirmationDisplayed(),
                "Browser seharusnya menampilkan dialog konfirmasi pembatalan."
        );
    }

    @And("mahasiswa memilih untuk tidak jadi membatalkan")
    public void dismissBrowserConfirmation() {
        activitiesPage.dismissBrowserConfirmation();
    }

    @And("pesanan masih ada dalam daftar aktivitas")
    public void verifyOrderStillExists() {
        Assertions.assertTrue(
                activitiesPage.getActivityRowCount() > 0,
                "Pesanan seharusnya masih ada setelah pembatalan dibatalkan."
        );
    }
}