# PPPL - Pengujian Sistem Informasi Akademik (SIA-UGN)

> Repositori ini merupakan bagian dari tugas mata kuliah **Pengujian dan Perawatan Perangkat Lunak (PPPL)** yang berfokus pada pengujian fungsional Sistem Informasi Akademik (SIA) modul **Perpustakaan** menggunakan framework **Cucumber + Selenium** dengan pola desain **Page Object Model (POM)**.

---

## System Under Test (SUT)

**Sistem Informasi Akademik (SIA) — Modul Perpustakaan** adalah aplikasi berbasis web yang digunakan untuk mengelola layanan perpustakaan perguruan tinggi secara digital. Sistem ini mencakup fitur autentikasi pengguna, manajemen koleksi buku, pemesanan, usulan buku oleh mahasiswa, serta pengelolaan data oleh admin perpustakaan.

Fitur utama yang menjadi cakupan pengujian:

| Modul                          | Deskripsi                                                |
| ------------------------------ | -------------------------------------------------------- |
| Login                          | Autentikasi pengguna (mahasiswa & admin) ke dalam sistem |
| Aktivitas Perpustakaan         | Riwayat peminjaman dan pengembalian buku oleh pengguna   |
| Katalog Buku & Pemesanan       | Pencarian, filter, dan pemesanan koleksi buku            |
| Manajemen Perpustakaan (Admin) | Pengelolaan data buku dan anggota oleh admin             |
| Usulan Buku                    | Pengajuan buku baru oleh mahasiswa                       |

---

## Test Suite

Test suite pada proyek ini dibangun menggunakan **Cucumber (BDD)** dan **Selenium WebDriver** dengan bahasa pemrograman Java. Skenario pengujian ditulis dalam format **Gherkin** (`.feature`) yang memisahkan logika bisnis dari implementasi teknis.

### Framework & Tools

| Komponen           | Teknologi                         |
| ------------------ | --------------------------------- |
| Bahasa Pemrograman | Java                              |
| BDD Framework      | Cucumber                          |
| UI Automation      | Selenium WebDriver                |
| Test Runner        | JUnit (via `CucumberRunner.java`) |
| Pola Desain        | Page Object Model (POM)           |

### Feature Files (Skenario Pengujian)

| File                          | Modul yang Diuji               |
| ----------------------------- | ------------------------------ |
| `login.feature`               | Login pengguna                 |
| `library-activities.feature`  | Aktivitas perpustakaan         |
| `search-filter-order.feature` | Katalog buku & pemesanan       |
| `admin-perpustakaan.feature`  | Manajemen perpustakaan (admin) |
| `usulan-buku.feature`         | Usulan buku                    |

---

## Pembagian Tugas Kelompok

| Nama        | Modul                          | Feature File                                  | Steps & Pages                                                                                    |
| ----------- | ------------------------------ | --------------------------------------------- | ------------------------------------------------------------------------------------------------ |
| **Alvista** | Login & Aktivitas              | `login.feature`, `library-activities.feature` | `LoginSteps.java`, `LibraryActivitiesSteps.java`, `LoginPage.java`, `LibraryActivitiesPage.java` |
| **Daffa**   | Katalog Buku & Pemesanan       | `search-filter-order.feature`                 | `LibraryBooksSteps.java`, `LibraryBooksPage.java`                                                |
| **Dimas**   | Manajemen Perpustakaan (Admin) | `admin-perpustakaan.feature`                  | `AdminLibrarySteps.java`, `AdminLibraryPage.java`                                                |
| **Riski**   | Usulan Buku                    | `usulan-buku.feature`                         | `LibrarySuggestionsSteps.java`, `LibrarySuggestionsPage.java`                                    |

---

## Struktur Repositori

```
PPPL-SIA-Testing/
│
├── data/                                    # Data pendukung pengujian (misal: file CSV, JSON)
│
├── src/
│   ├── main/java/org/example/               # Source code utama aplikasi (jika ada)
│   │
│   └── test/
│       ├── java/
│       │   ├── config/
│       │   │   └── TestConfig.java             # Konfigurasi global pengujian (base URL, driver, dll.)
│       │   │
│       │   ├── hooks/
│       │   │   └── CucumberHooks.java          # Setup & teardown sebelum/sesudah skenario (@Before, @After)
│       │   │
│       │   ├── locators/library/               # Kumpulan locator elemen UI (By/XPath/CSS)
│       │   │
│       │   ├── pages/                          # Page Object Model — representasi halaman web
│       │   │   ├── BasePage.java               # Base class dengan method umum (click, type, wait, dll.)
│       │   │   ├── LoginPage.java              # Halaman login
│       │   │   ├── LibraryActivitiesPage.java  # Halaman aktivitas perpustakaan
│       │   │   ├── LibraryBooksPage.java       # Halaman katalog & pemesanan buku
│       │   │   ├── LibrarySuggestionsPage.java # Halaman usulan buku
│       │   │   └── AdminLibraryPage.java       # Halaman manajemen admin perpustakaan
│       │   │
│       │   ├── runner/
│       │   │   └── CucumberRunner.java         # Entry point eksekusi seluruh test suite
│       │   │
│       │   └── steps/                          # Step definitions — implementasi langkah Gherkin
│       │       ├── LoginSteps.java
│       │       ├── LibraryActivitiesSteps.java
│       │       ├── LibraryBooksSteps.java
│       │       ├── LibrarySuggestionsSteps.java
│       │       └── AdminLibrarySteps.java
│       │
│       └── resources/
│           ├── features/                       # File skenario Gherkin (.feature)
│           │   ├── login.feature
│           │   ├── library-activities.feature
│           │   ├── search-filter-order.feature
│           │   ├── admin-perpustakaan.feature
│           │   └── usulan-buku.feature
│           │
│           └── cucumber.properties             # Konfigurasi Cucumber (plugin, glue, dsb.)
│
├── .gitignore
└── README.md
```

---

## Arsitektur dan Pola (Pattern)

Pengujian ini mengimplementasikan dua pola arsitektur secara bersamaan:

### 1. Behavior-Driven Development (BDD) dengan Cucumber
- **Fitur**: Ditulis dalam sintaks Gherkin (Given, When, Then). Bahasa ini memudahkan pembaca non-teknis memahami perilaku aplikasi.
- **Steps**: Setiap baris pada file Gherkin memiliki metode padanannya dalam Java yang terikat lewat anotasi Cucumber (`@Given`, `@When`, `@Then`).

### 2. Page Object Model (POM)
- **BasePage**: Menyimpan semua pustaka utilitas Selenium seperti explicit wait serta fungsi pembantu seperti klik, ketik, dsb. Mengambil instansiasi driver statis langsung dari `CucumberHooks`.
- **Pages**: Merepresentasikan layar aplikasi. Di sinilah semua lokator UI dan interaksi tombol disimpan agar step definition tetap bersih tanpa campuran kode Selenium webdriver secara langsung.

---

## Manajemen WebDriver

WebDriver diinisialisasi secara statis menggunakan ChromeDriver melalui fungsi siklus hidup (hooks) Cucumber.
File `CucumberHooks.java` bertanggung jawab penuh:
- `@Before`: Mengatur WebDriverManager agar secara otomatis mengambil binary driver Chrome terbaru, membentuk argumen Chrome, lalu menginisialisasi static driver. Browser kemudian akan diarahkan otomatis ke `TestConfig.BASE_URL`.
- `@After`: Mengeksekusi penutupan browser secara penuh melalui perintah `driver.quit()`. Bila scenario gagal, fungsi ini juga akan menangkap screenshot otomatis yang disimpan baik ke laporan maupun folder lokal.

---

## Dependensi Utama

Proyek ini menggunakan dependensi berikut yang diatur di `pom.xml`:

| Dependensi                                                       | Versi  |
| ---------------------------------------------------------------- | ------ |
| `selenium-java`                                                  | 4.21.0 |
| `webdrivermanager`                                               | 5.8.0  |
| `cucumber-java` & `cucumber-junit-platform-engine`               | 7.18.0 |
| `junit-jupiter`                                                  | 5.10.2 |
| `junit-platform-suite`                                           | 1.10.2 |

---

## Cara Menjalankan Pengujian

1. **Clone** repositori:
   ```bash
   git clone https://github.com/daffaadz/PPPL-SIA-Testing.git
   cd PPPL-SIA-Testing
   ```

2. Pastikan **Java JDK**, **Maven**, dan **browser driver** (ChromeDriver) sudah terinstal dan sesuai versi browser.

3. Jalankan seluruh test suite:
   ```bash
   mvn test
   ```

4. Untuk menjalankan skenario pada modul tertentu saja, gunakan tag Cucumber:
   ```bash
   mvn test -Dcucumber.filter.tags="@login"
   ```

---

## Konvensi Git

- Setiap anggota bekerja pada branch masing-masing dengan format:
  ```
  feature/<nama>-<modul>
  ```
  Contoh: `feature/alvista-login`, `feature/daffa-katalog`, `feature/dimas-admin`, `feature/riski-usulan`

- Setelah selesai, buat **Pull Request** ke branch `main` untuk direview sebelum di-merge.
