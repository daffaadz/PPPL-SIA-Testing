package config;

import io.github.cdimascio.dotenv.Dotenv;

public class TestConfig {

    public static final String BASE_URL = "https://fe-sia-ugn-ten.vercel.app/";
    public static final String STUDENT_EMAIL = "handoko@gmail.com";
    public static final String STUDENT_PASSWORD = "hanan123";
    public static final String ADMIN_EMAIL = "admin@gmail.com";
    public static final String ADMIN_PASSWORD = "admin123";

    public static final String API_BASE_URL = "https://be-sia-ugn-prod.up.railway.app";
    public static final String TESTING_SECRET_KEY = loadSecretKey();

    public static final int IMPLICIT_WAIT_SECONDS = 0;
    public static final int EXPLICIT_WAIT_SECONDS = 15;
    public static final int PAGE_LOAD_TIMEOUT_SECONDS = 30;

    public static final String BROWSER = "chrome";
    public static final boolean HEADLESS = false;
    public static final String SCREENSHOT_DIR = "target/screenshots";

    public static final String PATH_LOGIN = "/loginpage";
    public static final String PATH_LIBRARY_BOOKS = "/library/books";
    public static final String PATH_LIBRARY_SUGGESTIONS = "/library/suggestions";
    public static final String PATH_LIBRARY_ACTIVITIES = "/library/activities";

    private static String loadSecretKey() {
        try {
            return Dotenv.load().get("TESTING_SECRET_KEY", "default-key-do-not-use-in-prod");
        } catch (Exception e) {
            return System.getenv("TESTING_SECRET_KEY") != null ? System.getenv("TESTING_SECRET_KEY") : "default-key-do-not-use-in-prod";
        }
    }

    private TestConfig() {}
}
