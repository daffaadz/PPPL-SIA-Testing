# PPPL - Pengujian Sistem Informasi Akademik (SIA-UGN)

## System Under Test (SUT)

**Sistem Informasi Akademik (SIA) — Modul Perpustakaan** adalah aplikasi berbasis web yang digunakan untuk mengelola layanan perpustakaan perguruan tinggi secara digital. Sistem ini mencakup fitur autentikasi pengguna, manajemen koleksi buku, pemesanan, usulan buku oleh mahasiswa, serta pengelolaan data oleh admin perpustakaan.

SUT pada proyek ini merupakan bagian dari portal akademik SIA-UGN (Universitas Graha Nusantara) yang dapat diakses oleh dua jenis pengguna: **mahasiswa** dan **admin perpustakaan**. Mahasiswa dapat mencari dan memesan buku, memantau aktivitas peminjaman, serta mengusulkan buku baru; sementara admin dapat mengelola seluruh data katalog, mengonfirmasi peminjaman dan pengembalian buku, serta merespons usulan mahasiswa.

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

Test suite pada proyek ini dibangun menggunakan pendekatan **Behavior-Driven Development (BDD)** dengan **Cucumber** dan **Selenium WebDriver** berbasis bahasa pemrograman Java. Pengujian difokuskan pada level **End-to-End (E2E)** yang mensimulasikan interaksi nyata pengguna melalui antarmuka browser.

Setiap skenario pengujian ditulis dalam format **Gherkin** (`.feature`) menggunakan sintaks `Given–When–Then` yang dapat dipahami oleh semua pihak (teknis maupun non-teknis), kemudian dihubungkan ke implementasi Java melalui anotasi Cucumber (`@Given`, `@When`, `@Then`). Struktur Page Object Model (POM) digunakan untuk memisahkan lokator elemen UI dari logika skenario.

Test suite terdiri dari **39 skenario** yang tersebar di 5 modul, mencakup skenario positif, negatif, dan smoke test. Seluruh test dijalankan secara otomatis via Maven (`mvn test`) dengan laporan hasil uji yang dihasilkan oleh plugin Cucumber.

### Framework & Tools

| Komponen           | Teknologi                         |
| ------------------ | --------------------------------- |
| Bahasa Pemrograman | Java                              |
| BDD Framework      | Cucumber                          |
| UI Automation      | Selenium WebDriver                |
| Test Runner        | JUnit (via `CucumberRunner.java`) |
| Pola Desain        | Page Object Model (POM)           |

### Feature Files (Skenario Pengujian)

| File                          | Modul yang Diuji               | Jumlah Skenario |
| ----------------------------- | ------------------------------ | :-------------: |
| `login.feature`               | Login pengguna                 | 5               |
| `library-activities.feature`  | Aktivitas perpustakaan         | 10              |
| `search-filter-order.feature` | Katalog buku & pemesanan       | 9               |
| `admin-perpustakaan.feature`  | Manajemen perpustakaan (admin) | 16              |
| `usulan-buku.feature`         | Usulan buku                    | 3               |
| **Total**                     |                                | **43**          |

---

## Tabel Test Case

### Modul 1 — Login (`login.feature`)

| ID Test Case | Nama Skenario | Precondition | Langkah Uji | Expected Result | Jenis |
|---|---|---|---|---|---|
| TC-LGN-001 | Login berhasil sebagai mahasiswa | Halaman login terbuka | 1. Masukkan email `handoko@gmail.com` dan password `hanan123` <br> 2. Tekan tombol Login | Pengguna berhasil login dan diarahkan keluar dari halaman login | Positive / Smoke |
| TC-LGN-002 | Login berhasil sebagai admin | Halaman login terbuka | 1. Masukkan email `admin@gmail.com` dan password `admin123` <br> 2. Tekan tombol Login | Pengguna berhasil login dan diarahkan keluar dari halaman login | Positive / Smoke |
| TC-LGN-003 | Login gagal dengan password salah | Halaman login terbuka | 1. Masukkan email `handoko@gmail.com` dan password `passwordsalah` <br> 2. Tekan tombol Login | Pesan error ditampilkan, pengguna tetap di halaman login | Negative |
| TC-LGN-004 | Login gagal dengan email tidak terdaftar | Halaman login terbuka | 1. Masukkan email `tidakada@test.com` dan password `hanan123` <br> 2. Tekan tombol Login | Pesan error ditampilkan, pengguna tetap di halaman login | Negative |
| TC-LGN-005 | Login gagal ketika field kosong | Halaman login terbuka | 1. Biarkan field email dan password kosong <br> 2. Tekan tombol Login | Halaman login masih ditampilkan, tidak ada perpindahan halaman | Negative |

---

### Modul 2 — Aktivitas Perpustakaan (`library-activities.feature`)

| ID Test Case | Nama Skenario | Precondition | Langkah Uji | Expected Result | Jenis |
|---|---|---|---|---|---|
| TC-ACT-001 | Akses halaman aktivitas perpustakaan | Login sebagai mahasiswa | Berada di halaman aktivitas perpustakaan | Halaman aktivitas ditampilkan dengan benar beserta daftar aktivitas peminjaman | Positive |
| TC-ACT-002 | Kelengkapan kolom tabel aktivitas | Login sebagai mahasiswa, berada di halaman aktivitas | Periksa header kolom pada tabel aktivitas | Tabel memiliki kolom: Buku, Status, Dipesan, Dipinjam | Positive |
| TC-ACT-003 | Akses ditolak untuk pengguna belum login | Belum login | Akses URL `/library/activities` secara langsung | Pengguna diarahkan ke halaman login | Negative |
| TC-ACT-004 | Melihat detail pesanan dari daftar aktivitas | Login sebagai mahasiswa, terdapat minimal 1 aktivitas | Tekan tombol "Detail" pada aktivitas pertama | Diarahkan ke halaman detail pesanan; judul buku, status, tanggal, dan durasi ditampilkan | Positive |
| TC-ACT-005 | Detail pesanan menampilkan informasi lengkap | Login sebagai mahasiswa, terdapat aktivitas berstatus "Dipinjam" | Buka detail pesanan berstatus "Dipinjam" | Nama buku, nama peminjam, tanggal pinjam, durasi, dan badge status "Dipinjam" ditampilkan | Positive |
| TC-ACT-006 | Navigasi kembali dari halaman detail | Login sebagai mahasiswa, terdapat minimal 1 aktivitas | 1. Tekan tombol "Detail" pada aktivitas pertama <br> 2. Tekan tombol kembali | Pengguna kembali ke halaman daftar aktivitas dan daftar masih ditampilkan | Positive |
| TC-ACT-007 | Filter aktivitas berdasarkan status "Dipinjam" | Login sebagai mahasiswa, berada di halaman aktivitas | Pilih filter status "Dipinjam" | Tabel hanya menampilkan baris dengan status "Dipinjam" | Positive |
| TC-ACT-008 | Reset filter ke "Semua" | Login sebagai mahasiswa, filter "Dipinjam" aktif | Pilih filter status "Semua" | Seluruh daftar aktivitas ditampilkan kembali dengan semua status | Positive |
| TC-ACT-009 | Pembatalan pesanan "Menunggu Konfirmasi" | Login sebagai mahasiswa, terdapat pesanan berstatus "Menunggu Konfirmasi" | 1. Tekan tombol "Batalkan" <br> 2. Konfirmasi tindakan pada dialog | Notifikasi "Pesanan berhasil dibatalkan" muncul; status pesanan berubah menjadi "Dibatalkan" | Positive |
| TC-ACT-010 | Konfirmasi sebelum pesanan dibatalkan | Login sebagai mahasiswa, terdapat pesanan berstatus "Menunggu Konfirmasi" | 1. Tekan tombol "Batalkan" <br> 2. Pilih untuk tidak jadi membatalkan | Dialog konfirmasi muncul; pesanan tetap ada dalam daftar setelah dibatalkan | Positive |

---

### Modul 3 — Katalog Buku & Pemesanan (`search-filter-order.feature`)

| ID Test Case | Nama Skenario | Precondition | Langkah Uji | Expected Result | Jenis |
|---|---|---|---|---|---|
| TC-LIB-001 | Halaman katalog buku berhasil dimuat | Login sebagai mahasiswa, berada di halaman katalog buku | Amati halaman katalog | Halaman katalog ditampilkan dengan benar dan daftar buku tersedia | Positive / Smoke |
| TC-LIB-002 | Mencari buku dengan kata kunci valid | Login sebagai mahasiswa, berada di halaman katalog | Masukkan kata kunci "Pemrograman" pada kolom pencarian | Hasil pencarian menampilkan buku yang mengandung kata "Pemrograman" | Positive |
| TC-LIB-003 | Mencari buku dengan kata kunci tidak ada | Login sebagai mahasiswa, berada di halaman katalog | Masukkan kata kunci "xyzabcnotexist999" pada kolom pencarian | Pesan buku tidak ditemukan ditampilkan | Negative |
| TC-LIB-004 | Pencarian menggunakan tombol Enter | Login sebagai mahasiswa, berada di halaman katalog | 1. Ketik "Laravel" pada kolom pencarian <br> 2. Tekan tombol Enter | Halaman menampilkan hasil pencarian untuk "Laravel" | Positive |
| TC-LIB-005 | Filter buku berdasarkan kategori | Login sebagai mahasiswa, berada di halaman katalog | Pilih kategori "Informatika" | Tombol kategori "Informatika" aktif; daftar buku hasil filter ditampilkan | Positive |
| TC-LIB-006 | Reset filter kategori ke "Semua" | Login sebagai mahasiswa, filter kategori "Informatika" aktif | Tekan tombol kategori "Semua" | Tombol "Semua" aktif; seluruh daftar buku ditampilkan kembali | Positive |
| TC-LIB-007 | Memesan buku yang tersedia | Login sebagai mahasiswa, terdapat buku dengan stok tersedia | Tekan tombol pesan pada buku pertama yang tersedia | Notifikasi pemesanan buku berhasil ditampilkan | Positive |
| TC-LIB-008 | Memesan buku dengan stok kosong | Login sebagai mahasiswa, buku dengan stok 0 ada di katalog | Cari buku "Out of Stock Book" dan periksa tombol pesan | Tombol pesan pada buku tersebut dalam kondisi disabled (tidak dapat diklik) | Negative |
| TC-LIB-009 | Stok buku berkurang setelah pemesanan | Login sebagai mahasiswa, catat stok awal buku "Pemrograman Web dengan Laravel" | Pesan buku "Pemrograman Web dengan Laravel" | Jumlah ketersediaan buku berkurang satu dari stok awal | Positive |

---

### Modul 4 — Manajemen Perpustakaan Admin (`admin-perpustakaan.feature`)

| ID Test Case | Nama Skenario | Precondition | Langkah Uji | Expected Result | Jenis |
|---|---|---|---|---|---|
| TC-ADM-001 | Melihat dashboard dan daftar buku | Login sebagai admin, berada di halaman Manajemen Perpustakaan | Amati halaman dashboard admin | Statistik (total buku, tersedia, dipinjam, stok kritis) dan tabel katalog buku ditampilkan | Positive |
| TC-ADM-002 | Mencari buku di katalog admin | Login sebagai admin, berada di halaman Manajemen Perpustakaan | Masukkan kata kunci "Rekayasa Perangkat Lunak" pada kolom pencarian | Tabel katalog difilter dan menampilkan buku sesuai kata kunci | Positive |
| TC-ADM-003 | Filter buku berdasarkan stok kritis | Login sebagai admin, berada di halaman Manajemen Perpustakaan | Tekan tombol filter "Stok Kritis" | Tabel hanya menampilkan buku dengan stok tersedia = 0 | Positive |
| TC-ADM-004 | Menambahkan buku baru ke katalog | Login sebagai admin, berada di halaman Manajemen Perpustakaan | 1. Tekan "Tambah Buku" <br> 2. Isi form dengan data valid (judul, penulis, kategori, ISBN, penerbit, tahun, stok) <br> 3. Tekan "Simpan" | Notifikasi "Buku berhasil ditambahkan" muncul; buku baru tampil di daftar katalog | Positive |
| TC-ADM-005 | Menambahkan kategori buku baru | Login sebagai admin, berada di form "Tambah Buku Baru" | 1. Buka dropdown Kategori, pilih "Tambah Kategori Baru" <br> 2. Masukkan nama "Kecerdasan Buatan" <br> 3. Simpan kategori | Kategori "Kecerdasan Buatan" berhasil dibuat dan terpilih pada form buku | Positive |
| TC-ADM-006 | Mengedit informasi buku yang ada | Login sebagai admin, berada di halaman Manajemen Perpustakaan | 1. Tekan ikon Edit pada salah satu buku <br> 2. Ubah jumlah "Total Buku" menjadi lebih banyak <br> 3. Tekan "Simpan" | Notifikasi "Buku berhasil diperbarui" muncul; informasi stok buku di tabel berubah | Positive |
| TC-ADM-007 | Menghapus buku dari katalog | Login sebagai admin, berada di halaman Manajemen Perpustakaan | 1. Tekan ikon Hapus pada salah satu buku <br> 2. Konfirmasi tindakan penghapusan | Notifikasi sukses ditampilkan; buku dihapus dari daftar katalog | Positive |
| TC-ADM-008 | Filter daftar peminjaman berdasarkan status | Login sebagai admin, berada di tab "Peminjaman" | Pilih filter status "Dipinjam" | Tabel hanya menampilkan pesanan dengan status "Dipinjam" | Positive |
| TC-ADM-009 | Mencari pesanan peminjaman mahasiswa | Login sebagai admin, berada di tab "Peminjaman" | Masukkan NIM atau nama mahasiswa pada kolom pencarian | Tabel menampilkan data pesanan peminjaman mahasiswa yang dicari | Positive |
| TC-ADM-010 | Melihat status peminjaman "Dipesan" | Login sebagai admin, terdapat pesanan berstatus "Dipesan" | Amati daftar peminjaman | Status "Dipesan" ditampilkan pada pesanan; tombol aksi "Konfirmasi" tersedia | Positive |
| TC-ADM-011 | Konfirmasi peminjaman buku | Login sebagai admin, terdapat pesanan berstatus "Dipesan" | 1. Tekan tombol "Konfirmasi" pada pesanan <br> 2. Isi catatan admin jika diperlukan | Notifikasi konfirmasi peminjaman muncul; status pesanan berubah menjadi "Dipinjam" | Positive |
| TC-ADM-012 | Melihat detail pesanan berstatus "Dipinjam" | Login sebagai admin, terdapat pesanan berstatus "Dipinjam" | Tekan tombol "Detail" pada pesanan tersebut | Halaman detail menampilkan informasi peminjam, tanggal pinjam, jatuh tempo, dan tombol "Konfirmasi Kembali" | Positive |
| TC-ADM-013 | Konfirmasi pengembalian buku | Login sebagai admin, terdapat pesanan berstatus "Dipinjam" | 1. Tekan tombol "Kembalikan" pada pesanan <br> 2. Isi catatan kondisi buku | Notifikasi konfirmasi pengembalian muncul; status berubah menjadi "Dikembalikan"; tombol berubah menjadi "Selesai" (disabled) | Positive |
| TC-ADM-014 | Melihat daftar usulan buku mahasiswa | Login sebagai admin, berada di halaman Manajemen Peminjaman & Usulan | Pindah ke tab "Usulan" | Daftar usulan buku dari mahasiswa ditampilkan beserta ringkasan status (Menunggu, Disetujui, Ditolak) | Positive |
| TC-ADM-015 | Menyetujui usulan buku mahasiswa | Login sebagai admin, terdapat usulan berstatus "Menunggu" | 1. Tekan "Detail" pada usulan <br> 2. Isi pesan respon <br> 3. Tekan "Setujui" | Notifikasi "Usulan berhasil disetujui" muncul; status usulan berubah menjadi "Disetujui"; modal tertutup | Positive |
| TC-ADM-016 | Menolak usulan buku mahasiswa | Login sebagai admin, terdapat usulan berstatus "Menunggu" | 1. Tekan "Detail" pada usulan <br> 2. Isi pesan respon <br> 3. Tekan "Tolak" | Notifikasi "Usulan berhasil ditolak" muncul; status usulan berubah menjadi "Ditolak"; modal tertutup | Positive |

---

### Modul 5 — Usulan Buku (`usulan-buku.feature`)

| ID Test Case | Nama Skenario | Precondition | Langkah Uji | Expected Result | Jenis |
|---|---|---|---|---|---|
| TC-SUG-001 | Halaman usulan buku berhasil dimuat | Login sebagai mahasiswa, berada di halaman usulan buku | Amati halaman usulan buku | Halaman usulan buku ditampilkan dengan benar beserta form usulan | Positive / Smoke |
| TC-SUG-002 | Berhasil mengirim usulan buku dengan data valid | Login sebagai mahasiswa, berada di halaman usulan buku | 1. Isi judul "Machine Learning dengan Python", penulis "Jane Author", alasan "Dibutuhkan untuk referensi mata kuliah AI" <br> 2. Tekan tombol kirim usulan | Usulan berhasil dikirim; form direset; riwayat usulan menampilkan buku "Machine Learning dengan Python" | Positive |
| TC-SUG-003 | Gagal mengirim usulan saat form tidak lengkap | Login sebagai mahasiswa, berada di halaman usulan buku | Tekan tombol kirim usulan tanpa mengisi form | Pesan error validasi form usulan ditampilkan | Negative |

---

## Rekapitulasi Test Case

| Modul | Jumlah TC | Positive | Negative | Smoke |
|---|:---:|:---:|:---:|:---:|
| Login | 5 | 2 | 3 | 2 |
| Aktivitas Perpustakaan | 10 | 10 | 1 | 0 |
| Katalog Buku & Pemesanan | 9 | 7 | 2 | 1 |
| Manajemen Admin | 16 | 16 | 0 | 0 |
| Usulan Buku | 3 | 2 | 1 | 1 |
| **Total** | **43** | **37** | **7** | **4** |

> **Catatan:** Beberapa skenario memiliki lebih dari satu tag jenis (misal Positive + Smoke), sehingga total per kolom bisa melebihi total keseluruhan.

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
