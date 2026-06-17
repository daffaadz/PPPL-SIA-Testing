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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CucumberHooks {

    private static final DateTimeFormatter TIMESTAMP_FMT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    public static WebDriver driver;

    private static final Logger cdpLogger = Logger.getLogger("org.openqa.selenium.devtools");
    private static final Logger chromiumLogger = Logger.getLogger("org.openqa.selenium.chromium");
    private static final Logger seleniumLogger = Logger.getLogger("org.openqa.selenium");
    private static final Logger remoteLogger = Logger.getLogger("org.openqa.selenium.remote");

    private static int passedCount = 0;
    private static int failedCount = 0;

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
        options.addArguments("--log-level=3"); // Suppress ChromeDriver console logs

        driver = new ChromeDriver(options);

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(TestConfig.PAGE_LOAD_TIMEOUT_SECONDS));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TestConfig.IMPLICIT_WAIT_SECONDS));
        driver.manage().window().maximize();

        driver.get(TestConfig.BASE_URL);
    }

    @After(order = 0)
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            failedCount++;
            System.out.println("✖  FAILED: " + scenario.getName());
            takeScreenshot(driver, scenario);
        } else {
            passedCount++;
            System.out.println("✔  PASSED: " + scenario.getName());
        }

        System.out.println("========================================\n");

        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    @io.cucumber.java.BeforeAll
    public static void setupBeforeAll() {
        System.out.println("\n========================================");
        System.out.println("   GLOBAL SETUP (Before All)");
        System.out.println("========================================");
        
        // Suppress SLF4J logs for WebDriverManager
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "error");
        
        // Suppress Selenium CDP Warnings
        System.setProperty("webdriver.chrome.silentOutput", "true");
        Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);
        Logger.getLogger("org.openqa.selenium.devtools").setLevel(Level.SEVERE);
        Logger.getLogger("org.openqa.selenium.chromium").setLevel(Level.SEVERE);
        Logger.getLogger("org.openqa.selenium.remote").setLevel(Level.SEVERE);

        try {
            System.out.println("   ⏳ Resetting library database on Railway...");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(TestConfig.API_BASE_URL + "/api/testing/reset-library"))
                    .header("X-Testing-Key", TestConfig.TESTING_SECRET_KEY)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("   ✅ Database reset & seeded successfully.");
            } else {
                System.err.println("   ⚠ Failed to reset database: HTTP " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            System.err.println("   ⚠ Error calling reset API: " + e.getMessage());
        }
        System.out.println("========================================\n");
    }

    @io.cucumber.java.AfterAll
    public static void printSummary() {
        System.out.println("\n========================================");
        System.out.println("   TEST EXECUTION SUMMARY");
        System.out.println("========================================");
        System.out.println("   ✔ PASSED : " + passedCount);
        System.out.println("   ✖ FAILED : " + failedCount);
        System.out.println("   TOTAL    : " + (passedCount + failedCount));
        System.out.println("========================================\n");
    }

    private void takeScreenshot(WebDriver driver, Scenario scenario) {
        try {
            byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshotBytes, "image/png", "Screenshot on Failure");

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
