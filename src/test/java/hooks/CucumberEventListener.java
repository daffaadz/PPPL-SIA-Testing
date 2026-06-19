package hooks;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.WebDriver;
import utils.BugReportData;
import utils.BugReportGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CucumberEventListener implements ConcurrentEventListener {

    private static final ThreadLocal<List<String>> steps = ThreadLocal.withInitial(ArrayList::new);
    private static final ThreadLocal<String> errorMessage = new ThreadLocal<>();
    private static final List<BugReportData> failedReports = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepStarted.class, this::onTestStepStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::onTestStepFinished);
        publisher.registerHandlerFor(TestCaseStarted.class, this::onTestCaseStarted);
        publisher.registerHandlerFor(TestCaseFinished.class, this::onTestCaseFinished);
        publisher.registerHandlerFor(TestRunFinished.class, this::onTestRunFinished);
    }

    private void onTestCaseStarted(TestCaseStarted event) {
        steps.get().clear();
        errorMessage.remove();
        CucumberHooks.lastScreenshotPath.remove();
    }

    private void onTestStepStarted(TestStepStarted event) {
        if (event.getTestStep() instanceof PickleStepTestStep) {
            PickleStepTestStep pickleStep = (PickleStepTestStep) event.getTestStep();
            String stepText = pickleStep.getStep().getKeyword() + pickleStep.getStep().getText();
            steps.get().add(stepText);
        }
    }

    private void onTestStepFinished(TestStepFinished event) {
        if (event.getResult().getStatus() == Status.FAILED) {
            Throwable error = event.getResult().getError();
            if (error != null) {
                errorMessage.set(error.getMessage());
            }
        }
    }

    private void onTestCaseFinished(TestCaseFinished event) {
        if (event.getResult().getStatus() == Status.FAILED) {
            TestCase testCase = event.getTestCase();
            
            BugReportData data = new BugReportData();
            data.setTitle(testCase.getName());
            
            String errorMsg = errorMessage.get();
            data.setDescription(errorMsg != null ? errorMsg : "Test failed without an exception message.");
            
            // Extract Expectation if using JUnit Assertions
            String expectation = "Test scenario completes successfully.";
            if (errorMsg != null && errorMsg.contains("expected:")) {
                String[] parts = errorMsg.split("==>");
                if (parts.length > 0) {
                    expectation = parts[0].trim();
                }
            }
            data.setExpectation(expectation);
            
            // Platform
            String platformInfo = System.getProperty("os.name");
            try {
                WebDriver driver = CucumberHooks.driver;
                if (driver != null && driver instanceof HasCapabilities) {
                    Capabilities caps = ((HasCapabilities) driver).getCapabilities();
                    platformInfo += ", " + caps.getBrowserName() + " v." + caps.getBrowserVersion();
                }
            } catch (Exception e) {
                platformInfo += " (Failed to get browser info: " + e.getMessage() + ")";
            }
            data.setPlatform(platformInfo);
            
            data.setStepsToReproduce(new ArrayList<>(steps.get()));
            
            // Priority & Severity from Tags
            String priority = "High";
            String severity = "Critical";
            for (String tag : testCase.getTags()) {
                String t = tag.toLowerCase();
                if (t.contains("priority-")) priority = tag.substring(tag.indexOf("-") + 1);
                if (t.contains("severity-")) severity = tag.substring(tag.indexOf("-") + 1);
            }
            data.setPriority(priority);
            data.setSeverity(severity);
            
            // Screenshot
            String screenshotPath = CucumberHooks.lastScreenshotPath.get();
            data.setAttachmentPath(screenshotPath);
            
            failedReports.add(data);
            System.out.println("   [DEBUG] Added failed scenario to bug report list: " + testCase.getName());
        } else {
            System.out.println("   [DEBUG] TestCase Finished with status: " + event.getResult().getStatus());
        }
        
        steps.get().clear();
        errorMessage.remove();
        CucumberHooks.lastScreenshotPath.remove();
    }

    private void onTestRunFinished(TestRunFinished event) {
        if (!failedReports.isEmpty()) {
            String outputPath = "bug-report/bug_report.pdf";
            System.out.println("   [DEBUG] Test run finished. Generating combined PDF report at: " + outputPath);
            try {
                BugReportGenerator.generatePdfReport(new ArrayList<>(failedReports), outputPath);
                System.out.println("   [DEBUG] Combined PDF generation successful.");
            } catch (Exception e) {
                System.err.println("   [DEBUG] Exception during combined PDF generation: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("   [DEBUG] Test run finished. No failed scenarios to report.");
        }
    }
}
