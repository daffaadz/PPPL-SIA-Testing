package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import pages.LibraryBooksPage;
import pages.LoginPage;

public class LibraryBooksSteps {

    private final LoginPage        loginPage        = new LoginPage();
    private final LibraryBooksPage libraryBooksPage = new LibraryBooksPage();

    private String lastSearchedKeyword = "";
    private int    initialStock        = 0;

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

    // TC-LIB-001: Halaman katalog dimuat
    @And("saya berada di halaman katalog buku")
    public void navigateToLibraryBooks() {
        libraryBooksPage.openPage();
        Assertions.assertTrue(
                libraryBooksPage.isPageLoaded(),
                "Halaman katalog buku seharusnya berhasil dimuat."
        );
    }

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

    // TC-LIB-002 & TC-LIB-003 & TC-LIB-004: Fitur pencarian buku
    @When("mahasiswa mencari buku dengan kata kunci {string}")
    public void searchBookByKeyword(String keyword) {
        this.lastSearchedKeyword = keyword;
        libraryBooksPage.typeSearchKeyword(keyword);
        libraryBooksPage.pressEnterOnSearch();
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
        Assertions.assertTrue(
                libraryBooksPage.isPageLoaded(),
                "Halaman seharusnya selesai memuat setelah pencarian dengan Enter."
        );
        
        boolean isReady = false;
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 15000) {
            if (libraryBooksPage.areBooksDisplayed() || libraryBooksPage.isEmptyStateDisplayed()) {
                isReady = true;
                break;
            }
            try { Thread.sleep(500); } catch (InterruptedException e) {}
        }
        
        Assertions.assertTrue(
                isReady,
                "Halaman seharusnya menampilkan hasil buku atau pesan 'Buku tidak ditemukan' untuk kata kunci: " + keyword
        );
    }

    // TC-LIB-005 & TC-LIB-006: Fitur filter kategori
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

    // TC-LIB-007 & TC-LIB-008 & TC-LIB-009: Fitur pemesanan buku
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
                libraryBooksPage.isSuccessToastDisplayed(),
                "Notifikasi 'Buku berhasil dipesan' seharusnya ditampilkan setelah pemesanan."
        );
    }

    @Then("tombol pesan pada buku tersebut tidak bisa diklik")
    public void verifyOrderButtonDisabled() {
        Assertions.assertTrue(
                libraryBooksPage.isOrderButtonDisabledForBook(lastSearchedKeyword),
                "Tombol pesan untuk buku yang stoknya kosong seharusnya tidak bisa diklik (disabled)."
        );
    }

    @Given("mahasiswa melihat jumlah stok awal buku {string}")
    public void recordInitialBookStock(String bookTitle) {
        searchBookByKeyword(bookTitle);
        initialStock = libraryBooksPage.getAvailableStockForBook(bookTitle);
    }

    @When("mahasiswa memesan buku {string}")
    public void orderSpecificBook(String bookTitle) {
        libraryBooksPage.clickOrderButtonForBook(bookTitle);
    }

    @Then("jumlah ketersediaan buku {string} berkurang satu")
    public void verifyStockDecreased(String bookTitle) {
        verifyOrderSuccessNotification();

        try {
            libraryBooksPage.waitFor(d -> libraryBooksPage.getAvailableStockForBook(bookTitle) == (initialStock - 1));
        } catch (Exception ignored) {}

        int currentStock = libraryBooksPage.getAvailableStockForBook(bookTitle);
        Assertions.assertEquals(
                initialStock - 1,
                currentStock,
                "Stok ketersediaan buku seharusnya berkurang 1 setelah dipesan."
        );
    }
}
