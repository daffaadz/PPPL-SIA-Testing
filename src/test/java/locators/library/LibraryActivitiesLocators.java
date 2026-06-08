package locators.library;

import org.openqa.selenium.By;

public class LibraryActivitiesLocators {

    // List aktivitas
    public static final By ACTIVITY_LIST = By.cssSelector(
            "[data-testid='activity-item'], .activity-item, table tbody tr"
    );

    // Klik aktivitas pertama
    public static final By FIRST_ACTIVITY = By.cssSelector(
            "[data-testid='activity-item']:first-child, table tbody tr:first-child"
    );

    // Detail page
    public static final By DETAIL_CONTAINER = By.cssSelector(
            "[data-testid='activity-detail'], .activity-detail, article"
    );

    public static final By DETAIL_TITLE = By.cssSelector("h1, h2");

    public static final By STATUS_LABEL = By.cssSelector(
            "[data-testid='status'], .status"
    );
}