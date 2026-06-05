package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import pages.LibrarySuggestionsPage;
import pages.LoginPage;

/**
 * LibrarySuggestionsSteps — Step definitions untuk usulan-buku.feature.
 *
 * Covers:
 *  - Background: login sebagai mahasiswa & navigasi ke halaman usulan buku
 *  - Smoke test: halaman berhasil dimuat
 *  - Form usulan: tampilan field & counter karakter
 *  - Kirim usulan: submit sukses, form reset, riwayat diperbarui
 *  - Negatif: validasi error judul, penulis, dan alasan
 *
 * Note: Step "saya sudah login sebagai mahasiswa" sudah terdaftar di
 * LibraryBooksSteps.java — Cucumber menemukan step yang sama secara otomatis
 * sehingga step tersebut tidak perlu diduplikasi di sini.
 */
public class LibrarySuggestionsSteps {

    private final LoginPage               loginPage               = new LoginPage();
    private final LibrarySuggestionsPage  librarySuggestionsPage  = new LibrarySuggestionsPage();

    // ─── Background ──────────────────────────────────────────────────────────

    /**
     * Navigate ke halaman usulan buku dan verifikasi halaman berhasil dimuat.
     */
    @And("saya berada di halaman usulan buku")
    public void navigateToLibrarySuggestions() {
        librarySuggestionsPage.openPage();
        Assertions.assertTrue(
                librarySuggestionsPage.isPageLoaded(),
                "Halaman usulan buku seharusnya berhasil dimuat."
        );
    }

    // ─── Smoke Test ──────────────────────────────────────────────────────────

    /**
     * Verifikasi heading halaman usulan buku tampil.
     */
    @Then("halaman usulan buku ditampilkan dengan benar")
    public void verifyPageLoaded() {
        Assertions.assertTrue(
                librarySuggestionsPage.isPageHeadingDisplayed(),
                "Heading halaman 'Usulan Buku' seharusnya ditampilkan."
        );
    }

    /**
     * Verifikasi form usulan (heading "Formulir Usulan Buku") tampil di halaman.
     */
    @And("form usulan buku ditampilkan")
    public void verifyFormDisplayed() {
        Assertions.assertTrue(
                librarySuggestionsPage.isFormDisplayed(),
                "Form 'Formulir Usulan Buku' seharusnya ditampilkan."
        );
    }

    // ─── Form — Field Display ─────────────────────────────────────────────────

    /**
     * Verifikasi field input judul buku tampil di form.
     */
    @And("field judul buku ditampilkan")
    public void verifyTitleFieldDisplayed() {
        Assertions.assertTrue(
                librarySuggestionsPage.isTitleFieldDisplayed(),
                "Field 'Judul Buku' seharusnya ditampilkan di form."
        );
    }

    /**
     * Verifikasi field input nama penulis tampil di form.
     */
    @And("field nama penulis ditampilkan")
    public void verifyAuthorFieldDisplayed() {
        Assertions.assertTrue(
                librarySuggestionsPage.isAuthorFieldDisplayed(),
                "Field 'Penulis' seharusnya ditampilkan di form."
        );
    }

    /**
     * Verifikasi textarea alasan usulan tampil di form.
     */
    @And("field alasan usulan ditampilkan")
    public void verifyReasonFieldDisplayed() {
        Assertions.assertTrue(
                librarySuggestionsPage.isReasonFieldDisplayed(),
                "Field 'Alasan Usulan' seharusnya ditampilkan di form."
        );
    }

    /**
     * Verifikasi tombol "Kirim Usulan" tampil di form.
     */
    @And("tombol kirim usulan ditampilkan")
    public void verifySubmitButtonDisplayed() {
        Assertions.assertTrue(
                librarySuggestionsPage.isSubmitButtonDisplayed(),
                "Tombol 'Kirim Usulan' seharusnya ditampilkan di form."
        );
    }

    // ─── Form — Character Counter ─────────────────────────────────────────────

    /**
     * Isi textarea alasan dengan teks tertentu untuk menguji counter karakter.
     *
     * @param reason teks alasan yang akan diketik
     */
    @When("mahasiswa mengisi alasan usulan {string}")
    public void fillReasonField(String reason) {
        librarySuggestionsPage.fillReason(reason);
    }

    /**
     * Verifikasi counter karakter alasan menampilkan angka yang tepat.
     *
     * @param expectedCount jumlah karakter yang diharapkan
     */
    @Then("counter karakter menampilkan {int}")
    public void verifyCharacterCount(int expectedCount) {
        int actualCount = librarySuggestionsPage.getCharacterCount();
        Assertions.assertEquals(
                expectedCount,
                actualCount,
                "Counter karakter alasan seharusnya menampilkan " + expectedCount
                        + " tetapi menampilkan " + actualCount + "."
        );
    }

    // ─── Kirim Usulan — Form Input Steps ─────────────────────────────────────

    /**
     * Isi field judul buku.
     *
     * @param title judul buku yang akan diusulkan
     */
    @When("mahasiswa mengisi judul buku {string}")
    public void fillTitleField(String title) {
        librarySuggestionsPage.fillTitle(title);
    }

    /**
     * Isi field nama penulis.
     *
     * @param author nama penulis buku
     */
    @And("mahasiswa mengisi nama penulis {string}")
    public void fillAuthorField(String author) {
        librarySuggestionsPage.fillAuthor(author);
    }

    /**
     * Klik tombol "Kirim Usulan" untuk submit form.
     */
    @And("mahasiswa menekan tombol kirim usulan")
    public void clickSubmitButton() {
        librarySuggestionsPage.clickSubmit();
    }

    // ─── Kirim Usulan — Positive Assertions ──────────────────────────────────

    /**
     * Verifikasi toast notifikasi sukses muncul setelah usulan berhasil dikirim.
     */
    @Then("usulan buku berhasil dikirim")
    public void verifySubmitSuccess() {
        Assertions.assertTrue(
                librarySuggestionsPage.isSuccessToastDisplayed(),
                "Notifikasi 'Usulan buku berhasil dikirim' seharusnya ditampilkan setelah submit."
        );
    }

    /**
     * Verifikasi form direset (semua field kosong) setelah usulan berhasil dikirim.
     * Tunggu toast dulu agar operasi async selesai.
     */
    @Then("form usulan direset setelah pengiriman")
    public void verifyFormResetAfterSubmit() {
        // Tunggu toast sukses muncul terlebih dahulu
        librarySuggestionsPage.isSuccessToastDisplayed();

        Assertions.assertTrue(
                librarySuggestionsPage.isTitleFieldEmpty()
                        && librarySuggestionsPage.isAuthorFieldEmpty()
                        && librarySuggestionsPage.isReasonFieldEmpty(),
                "Semua field form seharusnya direset (kosong) setelah usulan berhasil dikirim."
        );
    }

    /**
     * Verifikasi judul buku tertentu muncul di sidebar riwayat usulan.
     *
     * @param title judul buku yang diharapkan muncul di riwayat
     */
    @Then("riwayat usulan menampilkan buku {string}")
    public void verifyHistoryContainsBook(String title) {
        // Tunggu toast sukses muncul dahulu (memastikan API call selesai)
        librarySuggestionsPage.isSuccessToastDisplayed();

        Assertions.assertTrue(
                librarySuggestionsPage.isBookTitleInHistory(title),
                "Sidebar riwayat usulan seharusnya menampilkan buku berjudul: '" + title + "' setelah submit."
        );
    }

    // ─── Kirim Usulan — Negative Assertions (Validation Errors) ──────────────

    /**
     * Verifikasi pesan error judul buku tampil ketika field judul tidak diisi.
     */
    @Then("pesan error judul ditampilkan")
    public void verifyTitleErrorDisplayed() {
        Assertions.assertTrue(
                librarySuggestionsPage.isTitleErrorDisplayed(),
                "Pesan error 'Judul buku wajib diisi' seharusnya ditampilkan."
        );
    }

    /**
     * Verifikasi pesan error nama penulis tampil ketika field penulis tidak diisi.
     */
    @Then("pesan error penulis ditampilkan")
    public void verifyAuthorErrorDisplayed() {
        Assertions.assertTrue(
                librarySuggestionsPage.isAuthorErrorDisplayed(),
                "Pesan error 'Nama penulis wajib diisi' seharusnya ditampilkan."
        );
    }

    /**
     * Verifikasi pesan error alasan tampil — baik karena alasan kosong
     * maupun karena alasan kurang dari 20 karakter.
     */
    @Then("pesan error alasan ditampilkan")
    public void verifyReasonErrorDisplayed() {
        Assertions.assertTrue(
                librarySuggestionsPage.isReasonErrorDisplayed(),
                "Pesan error alasan (wajib diisi / minimal 20 karakter) seharusnya ditampilkan."
        );
    }
}
