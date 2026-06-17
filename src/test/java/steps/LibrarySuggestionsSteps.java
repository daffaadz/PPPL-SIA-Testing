package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import pages.LibrarySuggestionsPage;

public class LibrarySuggestionsSteps {

    private final LibrarySuggestionsPage suggestionsPage = new LibrarySuggestionsPage();

    // TC-SUG-001: Halaman usulan dimuat
    @And("saya berada di halaman usulan buku")
    public void navigateToLibrarySuggestions() {
        suggestionsPage.openPage();
        Assertions.assertTrue(suggestionsPage.isPageLoaded(),
                "Halaman usulan buku seharusnya berhasil dimuat.");
    }

    @Then("halaman usulan buku ditampilkan dengan benar")
    public void verifyPageLoaded() {
        Assertions.assertTrue(suggestionsPage.isPageHeadingDisplayed(),
                "Heading halaman 'Usulan Buku' seharusnya ditampilkan.");
    }

    @And("form usulan buku ditampilkan")
    public void verifyFormDisplayed() {
        Assertions.assertTrue(suggestionsPage.isFormDisplayed(),
                "Form 'Formulir Usulan Buku' seharusnya ditampilkan.");
    }

    // TC-SUG-002: Kirim usulan buku valid
    @When("mahasiswa mengisi form usulan dengan judul {string}, penulis {string}, dan alasan {string}")
    public void fillSuggestionForm(String title, String author, String reason) {
        suggestionsPage.fillTitle(title);
        suggestionsPage.fillAuthor(author);
        suggestionsPage.fillReason(reason);
    }

    @And("mahasiswa menekan tombol kirim usulan")
    public void clickSubmitButton() {
        suggestionsPage.clickSubmit();
    }

    @Then("usulan buku berhasil dikirim")
    public void verifySubmitSuccess() {
        Assertions.assertTrue(suggestionsPage.isSuccessToastDisplayed(),
                "Notifikasi 'Usulan buku berhasil dikirim' seharusnya ditampilkan.");
    }

    @And("form usulan direset setelah pengiriman")
    public void verifyFormResetAfterSubmit() {
        suggestionsPage.isSuccessToastDisplayed(); // wait for toast
        Assertions.assertTrue(
                suggestionsPage.isTitleFieldEmpty()
                        && suggestionsPage.isAuthorFieldEmpty()
                        && suggestionsPage.isReasonFieldEmpty(),
                "Semua field form seharusnya kosong setelah usulan berhasil dikirim.");
    }

    @And("riwayat usulan menampilkan buku {string}")
    public void verifyHistoryContainsBook(String title) {
        suggestionsPage.isSuccessToastDisplayed(); // wait for toast
        Assertions.assertTrue(suggestionsPage.isBookTitleInHistory(title),
                "Riwayat usulan seharusnya menampilkan buku: '" + title + "'.");
    }

    // TC-SUG-003: Validasi form kosong
    @Then("pesan error validasi form usulan ditampilkan")
    public void verifyValidationErrorsDisplayed() {
        Assertions.assertTrue(
                suggestionsPage.isTitleErrorDisplayed() ||
                suggestionsPage.isAuthorErrorDisplayed() ||
                suggestionsPage.isReasonErrorDisplayed(),
                "Setidaknya satu pesan error validasi form seharusnya ditampilkan.");
    }
}
