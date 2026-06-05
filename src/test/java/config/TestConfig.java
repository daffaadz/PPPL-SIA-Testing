package config;

/**
 * Centralized configuration for SIA-UGN UKT Test Suite.
 * Modify the values here to match your deployment environment.
 */
public class TestConfig {

    // ===== Base URL (deployed frontend) =====
    // Sesuaikan dengan URL deployment frontend SIA-UGN
    public static final String BASE_URL = "https://fe-sia-ugn.vercel.app";

    // Alternatif jika masih lokal:
    // public static final String BASE_URL = "http://localhost:3000";

    // ===== Test Credentials — Mahasiswa =====
    public static final String STUDENT_EMAIL    = "handoko@gmail.com";
    public static final String STUDENT_PASSWORD = "hanan123";

    // ===== Test Credentials — Admin/Manager =====
    public static final String ADMIN_EMAIL    = "manager@gmail.com";
    public static final String ADMIN_PASSWORD = "manager123";

    // ===== Wait Strategy =====
    /** Implicit wait in seconds (should be 0 when using explicit waits) */
    public static final int IMPLICIT_WAIT_SECONDS = 0;
    /** Default explicit wait timeout in seconds */
    public static final int EXPLICIT_WAIT_SECONDS = 15;
    /** Page load timeout in seconds */
    public static final int PAGE_LOAD_TIMEOUT_SECONDS = 30;

    // ===== Browser Configuration =====
    /** Browser to use: "chrome" | "firefox" | "edge" */
    public static final String BROWSER = "chrome";
    /** Run in headless mode (false = visible browser) */
    public static final boolean HEADLESS = false;

    // ===== Screenshot =====
    /** Directory for failure screenshots */
    public static final String SCREENSHOT_DIR = "target/screenshots";

    // ===== Midtrans Sandbox Simulator =====
    public static final String MIDTRANS_SIMULATOR_URL = "https://simulator.sandbox.midtrans.com";

    // ===== UKT Paths =====
    public static final String PATH_LOGIN               = "/loginpage";

    // ===== Library Paths =====
    public static final String PATH_LIBRARY_BOOKS       = "/library/books";
    public static final String PATH_LIBRARY_SUGGESTIONS = "/library/suggestions";

    private TestConfig() {
        // Utility class — no instantiation
    }
}
