package steps;

import hooks.CucumberHooks;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import pages.LibraryActivitiesPage;
import pages.LoginPage;

public class LibraryActivitiesSteps {

    WebDriver driver = CucumberHooks.driver;

    LoginPage loginPage = new LoginPage();
    LibraryActivitiesPage activitiesPage = new LibraryActivitiesPage();

    @Given("saya login sebagai mahasiswa")
    public void loginSebagaiMahasiswa() {
        loginPage.openLoginPage();
        loginPage.login("handoko@gmail.com", "hanan123");
    }

    @When("saya membuka halaman aktivitas perpustakaan")
    public void bukaHalamanAktivitas() {
        activitiesPage.openActivitiesPage();
    }

    @Then("daftar aktivitas ditampilkan")
    public void daftarAktivitasDitampilkan() {
        Assertions.assertTrue(
                activitiesPage.isActivitiesListDisplayed(),
                "Daftar aktivitas tidak tampil"
        );
    }

    @When("saya klik salah satu aktivitas")
    public void klikAktivitas() {
        activitiesPage.clickFirstActivity();
    }

    @Then("detail aktivitas perpustakaan ditampilkan")
    public void detailAktivitasDitampilkan() {
        Assertions.assertTrue(
                activitiesPage.isDetailDisplayed(),
                "Detail aktivitas tidak tampil"
        );
    }

    @And("status aktivitas terlihat")
    public void statusAktivitasTerlihat() {
        String status = activitiesPage.getStatus();
        Assertions.assertFalse(status.isEmpty(), "Status kosong");
        System.out.println("Status: " + status);
    }
}