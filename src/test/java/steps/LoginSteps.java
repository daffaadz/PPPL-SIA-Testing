package steps;

import pages.LoginPage;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;

/**
 * LoginSteps — Step definitions for login.feature.
 */
public class LoginSteps {

    private final LoginPage loginPage = new LoginPage();

    // ─── Given ───────────────────────────────────────────────────────────────

    @Given("saya membuka halaman login SIA-UGN")
    public void iOpenTheLoginPage() {
        loginPage.openLoginPage();
        Assertions.assertTrue(loginPage.isLoginFormDisplayed(),
                "Halaman login seharusnya menampilkan form login.");
    }

    // ─── When ────────────────────────────────────────────────────────────────

    @When("saya memasukkan email {string} dan password {string}")
    public void iEnterEmailAndPassword(String email, String password) {
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
    }

    @When("saya menekan tombol Login")
    public void iClickLoginButton() {
        loginPage.clickLoginButton();
    }

    @When("saya tidak mengisi email dan password")
    public void iDoNotFillEmailAndPassword() {
        // Leave fields empty — just don't type anything
    }

    // ─── Then ────────────────────────────────────────────────────────────────

    @Then("saya berhasil login dan diarahkan keluar dari halaman login")
    public void iSuccessfullyLoginAndRedirected() {
        Assertions.assertTrue(loginPage.isLoginSuccessful(),
                "Login seharusnya berhasil dan URL bukan /login.");
    }

    @Then("saya melihat pesan error pada halaman login")
    public void iSeeErrorMessageOnLoginPage() {
        Assertions.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Seharusnya ada pesan error setelah login gagal.");
    }

    @Then("halaman login masih ditampilkan")
    public void loginPageIsStillDisplayed() {
        Assertions.assertTrue(loginPage.isLoginFormDisplayed(),
                "Halaman login seharusnya masih ditampilkan.");
    }
}
