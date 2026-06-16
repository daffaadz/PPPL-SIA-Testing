# Dokumentasi SIA-UGN-Test (Login Focus)

Modul ini berisi skenario pengujian otomatis untuk fitur **Login Mahasiswa dan Admin** pada sistem SIA-UGN. Pengujian dilakukan menggunakan pendekatan Behavior-Driven Development (BDD) dengan Cucumber dan Page Object Model (POM) dengan Selenium WebDriver.

## Struktur Direktori

SIA-UGN-Test/
├── src/
│   └── test/
│       ├── java/
│       │   ├── config/             # Konfigurasi pengujian (base URL, kredensial, timeouts)
│       │   ├── hooks/              # Setup & teardown browser (Cucumber Hooks)
│       │   ├── pages/              # Implementasi Page Object Model (LoginPage, BasePage)
│       │   └── steps/              # Definisi langkah-langkah Cucumber (LoginSteps)
│       └── resources/
│           └── features/           # File Gherkin berisi skenario login (login.feature)
├── data/
│   └── akun_SIA_UGN.md             # Catatan kredensial akun uji
└── documentation.md                # Dokumen ini

## Persyaratan Sistem

- **Java Development Kit (JDK):** Versi 17 atau yang lebih baru.
- **Maven:** Untuk manajemen dependensi.
- **Google Chrome:** Browser untuk eksekusi Selenium.
- **Koneksi Internet:** Untuk mengunduh WebDriver otomatis via WebDriverManager.

## Dependensi Utama

Proyek ini menggunakan dependensi berikut yang diatur di `pom.xml`:

- `selenium-java` (4.21.0)
- `webdrivermanager` (5.8.0)
- `cucumber-java` & `cucumber-junit-platform-engine` (7.18.0)
- `junit-jupiter` (5.10.2)
- `junit-platform-suite` (1.10.2)

## Cara Menjalankan Tes

### 1. Menjalankan Semua Tes via Maven

Jalankan perintah ini di *root directory* dari modul pengujian (`SIA-UGN-Test`):

```bash
mvn clean test
```

Anda dapat melihat laporan *testing* dalam bentuk visual/HTML yang dihasilkan secara otomatis di dalam `target/cucumber-reports/cucumber-report.html`.

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
