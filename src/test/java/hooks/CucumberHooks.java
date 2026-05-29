package hooks;

import config.TestConfig;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * CucumberHooks — Global lifecycle hooks for the test suite.
 *
 * @Before  — Initializes WebDriver before each scenario
 * @After   — Takes screenshot on failure, then quits WebDriver
 */
public class CucumberHooks {

    private static final DateTimeFormatter TIMESTAMP_FMT =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    // ─── @Before ─────────────────────────────────────────────────────────────

    public static WebDriver driver;

    /**
     * Runs before every scenario.
     * Initializes the ChromeDriver directly.
     * Navigates to the base URL to ensure a clean starting state.
     */
    @Before(order = 0)
    public void setUp(Scenario scenario) {
        System.out.println("\n========================================");
        System.out.println("▶  START: " + scenario.getName());
        System.out.println("   Tags  : " + scenario.getSourceTagNames());
        System.out.println("========================================");

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        if (TestConfig.HEADLESS) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        
        driver = new ChromeDriver(options);
        
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(TestConfig.PAGE_LOAD_TIMEOUT_SECONDS));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TestConfig.IMPLICIT_WAIT_SECONDS));
        driver.manage().window().maximize();

        driver.get(TestConfig.BASE_URL);
    }

    // ─── @After ──────────────────────────────────────────────────────────────

    /**
     * Runs after every scenario.
     * - On FAILURE: captures a screenshot and attaches it to the Cucumber report.
     * - Always: quits the WebDriver to release resources.
     */
    @After(order = 0)
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            System.out.println("✖  FAILED: " + scenario.getName());
            takeScreenshot(driver, scenario);
        } else {
            System.out.println("✔  PASSED: " + scenario.getName());
        }

        System.out.println("========================================\n");

        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    // ─── Private Helpers ──────────────────────────────────────────────────────

    /**
     * Captures a screenshot and attaches it to the Cucumber scenario report.
     * Also saves the file to {@link TestConfig#SCREENSHOT_DIR} for local inspection.
     */
    private void takeScreenshot(WebDriver driver, Scenario scenario) {
        try {
            // Capture screenshot as byte array
            byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

            // Attach to Cucumber HTML report (appears inline in the report)
            scenario.attach(screenshotBytes, "image/png", "Screenshot on Failure");

            // Also save to local file system
            String timestamp = LocalDateTime.now().format(TIMESTAMP_FMT);
            String safeName = scenario.getName()
                    .replaceAll("[^a-zA-Z0-9_\\-]", "_")
                    .substring(0, Math.min(scenario.getName().length(), 50));

            String fileName = timestamp + "_" + safeName + ".png";
            Path dir = Paths.get(TestConfig.SCREENSHOT_DIR);
            Files.createDirectories(dir);

            Path filePath = dir.resolve(fileName);
            Files.write(filePath, screenshotBytes);

            System.out.println("   📸 Screenshot saved: " + filePath.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("   ⚠ Could not save screenshot: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("   ⚠ Screenshot capture failed: " + e.getMessage());
        }
    }
}
