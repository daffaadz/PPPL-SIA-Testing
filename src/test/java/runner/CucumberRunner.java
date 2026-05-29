package runner;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.*;

/**
 * CucumberRunner — JUnit Platform Suite runner for the SIA-UGN UKT test suite.
 *
 * Configuration:
 *  - Scans all .feature files under src/test/resources/features/
 *  - Glue: step definitions and hooks in com.siaugn.ukt
 *  - Plugin: pretty (console), html (target/cucumber-reports)
 *  - Tags can be overridden via -Dcucumber.filter.tags="@SmokeTest"
 *
 * Run all tests:
 *   mvn test
 *
 * Run only smoke tests:
 *   mvn test -Dcucumber.filter.tags="@SmokeTest"
 *
 * Run only student UKT tests:
 *   mvn test -Dcucumber.filter.tags="@UKT"
 *
 * Run only admin tests:
 *   mvn test -Dcucumber.filter.tags="@Admin"
 *
 * Run specific feature:
 *   mvn test -Dcucumber.filter.tags="@Login"
 *
 * Exclude negative tests:
 *   mvn test -Dcucumber.filter.tags="not @NegativeTest"
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(
        key = GLUE_PROPERTY_NAME,
        value = "steps, hooks"
)
@ConfigurationParameter(
        key = PLUGIN_PROPERTY_NAME,
        value = "pretty, html:target/cucumber-reports/cucumber-report.html, json:target/cucumber-reports/cucumber-report.json"
)
@ConfigurationParameter(
        key = FILTER_TAGS_PROPERTY_NAME,
        value = "not @Wip"
)
@ConfigurationParameter(
        key = FEATURES_PROPERTY_NAME,
        value = "src/test/resources/features"
)
public class CucumberRunner {
    // This class is intentionally empty.
    // JUnit Platform Suite discovers and executes all Cucumber scenarios automatically.
}
