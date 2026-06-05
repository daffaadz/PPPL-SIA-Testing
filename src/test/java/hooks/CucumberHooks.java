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

public class CucumberHooks {

    private static final DateTimeFormatter TIMESTAMP_FMT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    public static WebDriver driver;

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
