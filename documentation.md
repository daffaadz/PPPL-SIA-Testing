# Dokumentasi SIA-UKT-TEST (Login Focus)

Proyek ini telah direfaktor untuk memfokuskan pengujian fungsional pada alur Login (Autentikasi). Direktori telah disederhanakan dengan memindahkan semua *package* panjang dan konfigurasi rumit.

## Struktur Direktori

Seluruh logika *test* Java dapat ditemukan tepat di bawah `src/test/java/`, tanpa struktur package *domain* tambahan (`com.siaugn.ukt`).

```text
SIA-UKT-TEST/
├── src/
│   └── test/
│       ├── java/
│       │   ├── config/
│       │   │   └── TestConfig.java        # Konfigurasi konstanta seperti BASE_URL dan Timeout
│       │   ├── hooks/
│       │   │   └── CucumberHooks.java     # Konfigurasi WebDriver global (@Before & @After)
│       │   ├── pages/
│       │   │   ├── BasePage.java          # Abstract class berisikan utils & wait methods
│       │   │   └── LoginPage.java         # Page Object spesifik untuk interaksi halaman Login
│       │   ├── runner/
│       │   │   └── CucumberRunner.java    # Titik eksekusi pengujian dengan JUnit Platform Suite
│       │   └── steps/
│       │       └── LoginSteps.java        # Step Definitions pemetaan dari syntax Gherkin
│       └── resources/
│           ├── features/
│           │   └── login.feature          # Kumpulan skenario BDD untuk fitur Login
│           └── cucumber.properties        # Konfigurasi plugin Cucumber standar
└── pom.xml                                # Konfigurasi dependensi Maven
```

## Arsitektur dan Pola (Pattern)

Pengujian ini mengimplementasikan dua pola arsitektur secara bersamaan:

### 1. Behavior-Driven Development (BDD) dengan Cucumber
- **Fitur (`features/login.feature`)**: Ditulis dalam sintaks Gherkin (Given, When, Then). Bahasa ini memudahkan pembaca non-teknis memahami perilaku aplikasi.
- **Steps (`steps/LoginSteps.java`)**: Setiap baris pada file Gherkin memiliki metode padanannya dalam Java yang terikat lewat anotasi Cucumber (`@Given`, `@When`, `@Then`).

### 2. Page Object Model (POM)
- **`pages/BasePage.java`**: Menyimpan semua pustaka utilitas Selenium seperti *explicit wait* (tunggu hingga terlihat, tunggu hingga bisa diklik) serta fungsi pembantu seperti klik, ketik, dsb. Mengambil instansiasi driver statis langsung dari `CucumberHooks`.
- **`pages/LoginPage.java`**: Merepresentasikan layar login. Di sinilah semua **lokator UI** (XPath, CSS selector) dan interaksi tombol disimpan agar step definition tetap bersih tanpa campuran kode *Selenium webdriver* langsung.

## Manajemen WebDriver (Sederhana via Chrome)

Sebagai penyederhanaan dari struktur sebelumnya, file perantara *DriverManager* telah dihapus. 

- WebDriver sekarang diinisialisasi secara **statis** menggunakan `ChromeDriver` melalui fungsi siklus hidup (hooks) Cucumber.
- File **`CucumberHooks.java`** bertanggung bertanggung jawab penuh:
  - `@Before`: Mengatur *WebDriverManager* agar secara otomatis mengambil binary driver Chrome terbaru, membentuk argumen Chrome (termasuk *headless*), lalu menginisialisasi static `driver`. Browser kemudian akan diarahkan otomatis ke `TestConfig.BASE_URL`.
  - `@After`: Mengeksekusi penutupan browser secara penuh melalui perintah `driver.quit()`. Bila scenario gagal, fungsi ini juga akan menangkap *screenshot* otomatis yang disimpan baik ke laporan maupun folder lokal.

## Cara Eksekusi

Jalankan perintah ini di *root directory* dari modul pengujian (`SIA-UKT-TEST`):

```bash
# Mengeksekusi seluruh skenario test (login)
mvn clean test
```

Anda dapat melihat laporan *testing* dalam bentuk visual/HTML yang dihasilkan secara otomatis di dalam `target/cucumber-reports/cucumber-report.html`.
