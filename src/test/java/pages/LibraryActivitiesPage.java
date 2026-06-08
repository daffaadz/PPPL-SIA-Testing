package pages;

import config.TestConfig;
import locators.library.LibraryActivitiesLocators;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class LibraryActivitiesPage extends BasePage {

    public LibraryActivitiesPage() {
        super();
    }

    public void openActivitiesPage() {
        driver.get(TestConfig.BASE_URL + TestConfig.PATH_LIBRARY_ACTIVITIES);
    }

    public boolean isActivitiesListDisplayed() {
        List<WebElement> activities = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        LibraryActivitiesLocators.ACTIVITY_LIST
                )
        );
        return activities.size() > 0;
    }

    public void clickFirstActivity() {
        wait.until(ExpectedConditions.elementToBeClickable(
                LibraryActivitiesLocators.FIRST_ACTIVITY
        )).click();
    }

    public boolean isDetailDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                LibraryActivitiesLocators.DETAIL_CONTAINER
        )).isDisplayed();
    }

    public String getDetailTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                LibraryActivitiesLocators.DETAIL_TITLE
        )).getText();
    }

    public String getStatus() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                LibraryActivitiesLocators.STATUS_LABEL
        )).getText();
    }
}