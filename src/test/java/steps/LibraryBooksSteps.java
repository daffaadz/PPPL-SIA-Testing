package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import pages.LibraryBooksPage;
import pages.LoginPage;

/**
 * LibraryBooksSteps — Step definitions for library_books.feature.
 *
 * Covers:
 *  - Login as mahasiswa (Background)
 *  - Navigate to /library/books
 *  - Search by keyword (button click and Enter key)
 *  - Filter by category pill
 *  - Order book
 */
public class LibraryBooksSteps {

    private final LoginPage        loginPage        = new LoginPage();
    private final LibraryBooksPage libraryBooksPage = new LibraryBooksPage();

    // ─── Background ──────────────────────────────────────────────────────────

    /**
     * Background step: login as mahasiswa using credentials from TestConfig.
     * Re-uses LoginPage which already handles the full login flow.
     */
    @Given("saya sudah login sebagai mahasiswa")
    public void loginAsMahasiswa() {
        loginPage.login(
                config.TestConfig.STUDENT_EMAIL,
                config.TestConfig.STUDENT_PASSWORD
        );
        Assertions.assertTrue(
                loginPage.isLoginSuccessful(),
                "Login sebagai mahasiswa seharusnya berhasil."
        );
    }

    /**
     * Background step: navigate to the library books catalogue page.
     */
    @And("saya berada di halaman katalog buku")
    public void navigateToLibraryBooks() {
        libraryBooksPage.openPage();
        Assertions.assertTrue(
                libraryBooksPage.isPageLoaded(),
                "Halaman katalog buku seharusnya berhasil dimuat."
        );
    }

    // ─── Smoke Test ──────────────────────────────────────────────────────────

    @Then("halaman katalog buku ditampilkan dengan benar")
    public void verifyLibraryPageLoaded() {
        Assertions.assertTrue(
                libraryBooksPage.isPageHeadingDisplayed(),
                "Heading halaman katalog buku seharusnya ditampilkan."
        );
    }

    @And("daftar buku perpustakaan ditampilkan")
    public void verifyBooksAreListed() {
        Assertions.assertTrue(
                libraryBooksPage.areBooksDisplayed(),
                "Setidaknya satu buku seharusnya ditampilkan di katalog."
        );
    }

    // ─── Search — Keyword via Button ─────────────────────────────────────────

    /**
     * Type keyword in search bar and click "Filter Judul" button.
     */
    @When("mahasiswa mencari buku dengan kata kunci {string}")
    public void searchBookByKeyword(String keyword) {
        libraryBooksPage.typeSearchKeyword(keyword);
        libraryBooksPage.clickFilterJudul();
    }

    @Then("buku dengan kata kunci {string} ditampilkan pada hasil pencarian")
    public void verifySearchResultsContainKeyword(String keyword) {
        Assertions.assertTrue(
                libraryBooksPage.areBooksDisplayed()
                        && libraryBooksPage.isBookContainingKeywordDisplayed(keyword),
                "Hasil pencarian seharusnya menampilkan buku yang mengandung kata kunci: " + keyword
        );
    }

    @Then("pesan buku tidak ditemukan ditampilkan")
    public void verifyEmptyState() {
        Assertions.assertTrue(
                libraryBooksPage.isEmptyStateDisplayed(),
                "Pesan 'Buku tidak ditemukan' seharusnya ditampilkan untuk pencarian tanpa hasil."
        );
    }

    // ─── Search — Keyword via Enter Key ──────────────────────────────────────

    @When("mahasiswa mengetik kata kunci {string} pada kolom pencarian")
    public void typeKeywordInSearchBar(String keyword) {
        libraryBooksPage.typeSearchKeyword(keyword);
    }

    @And("mahasiswa menekan tombol Enter pada keyboard")
    public void pressEnterOnSearchBar() {
        libraryBooksPage.pressEnterOnSearch();
    }

    @Then("halaman menampilkan hasil pencarian untuk {string}")
    public void verifySearchResultsLoaded(String keyword) {
        // After pressing Enter, page should either show results or the empty state,
        // but must no longer be loading.
        Assertions.assertTrue(
                libraryBooksPage.isPageLoaded(),
                "Halaman seharusnya selesai memuat setelah pencarian dengan Enter."
        );
        // At least one of the two states should be true
        boolean hasResults = libraryBooksPage.areBooksDisplayed();
        boolean isEmpty    = libraryBooksPage.isEmptyStateDisplayed();
        Assertions.assertTrue(
                hasResults || isEmpty,
                "Halaman seharusnya menampilkan hasil buku atau pesan 'Buku tidak ditemukan' untuk kata kunci: " + keyword
        );
    }

    // ─── Filter — Category Pills ──────────────────────────────────────────────

    @When("mahasiswa memilih kategori buku {string}")
    public void selectCategoryFilter(String categoryName) {
        libraryBooksPage.clickCategoryPill(categoryName);
    }

    @And("mahasiswa menekan tombol kategori {string}")
    public void clickCategoryButton(String categoryName) {
        if ("Semua".equalsIgnoreCase(categoryName)) {
            libraryBooksPage.clickSemuaPill();
        } else {
            libraryBooksPage.clickCategoryPill(categoryName);
        }
    }

    @Then("tombol kategori {string} menjadi aktif")
    public void verifyCategoryPillIsActive(String categoryName) {
        Assertions.assertTrue(
                libraryBooksPage.isCategoryPillActive(categoryName),
                "Tombol kategori '" + categoryName + "' seharusnya menjadi aktif (warna hijau)."
        );
    }

    // ─── Order Book ───────────────────────────────────────────────────────────

    @Given("terdapat buku yang tersedia di halaman katalog")
    public void verifyAvailableBookExists() {
        Assertions.assertTrue(
                libraryBooksPage.isAnyOrderButtonVisible(),
                "Seharusnya ada minimal satu buku dengan tombol 'Pesan' yang tersedia."
        );
    }

    @When("mahasiswa memesan buku pertama yang tersedia")
    public void orderFirstAvailableBook() {
        libraryBooksPage.clickFirstOrderButton();
    }

    @Then("notifikasi pemesanan buku berhasil ditampilkan")
    public void verifyOrderSuccessNotification() {
        Assertions.assertTrue(
                libraryBooksPage.isOrderSuccessToastDisplayed(),
                "Notifikasi 'Buku berhasil dipesan' seharusnya ditampilkan setelah pemesanan."
        );
    }
}
