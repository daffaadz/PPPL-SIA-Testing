Feature: Katalog Buku Perpustakaan
  Sebagai mahasiswa SIA-UGN yang sudah login
  Saya ingin dapat mencari, memfilter, dan memesan buku
  Agar saya dapat menggunakan layanan perpustakaan universitas

  Background:
    Given saya sudah login sebagai mahasiswa
    And saya berada di halaman katalog buku

  # ─── Smoke Test ─────────────────────────────────────────────────────────────

  @Library @SmokeTest
  Scenario: TC-LIB-001 - Halaman katalog buku berhasil dimuat
    Then halaman katalog buku ditampilkan dengan benar
    And daftar buku perpustakaan ditampilkan

  # ─── Fitur Pencarian ─────────────────────────────────────────────────────────

  @Library @Search
  Scenario: TC-LIB-002 - Mencari buku dengan kata kunci yang valid
    When mahasiswa mencari buku dengan kata kunci "Pemrograman"
    Then buku dengan kata kunci "Pemrograman" ditampilkan pada hasil pencarian

  @Library @Search @NegativeTest
  Scenario: TC-LIB-003 - Mencari buku dengan kata kunci yang tidak ada
    When mahasiswa mencari buku dengan kata kunci "xyzabcnotexist999"
    Then pesan buku tidak ditemukan ditampilkan

  @Library @Search
  Scenario: TC-LIB-004 - Mencari buku menggunakan tombol Enter pada keyboard
    When mahasiswa mengetik kata kunci "Laravel" pada kolom pencarian
    And mahasiswa menekan tombol Enter pada keyboard
    Then halaman menampilkan hasil pencarian untuk "Laravel"

  # ─── Fitur Filter Kategori ───────────────────────────────────────────────────

  @Library @Filter
  Scenario: TC-LIB-005 - Memfilter buku berdasarkan kategori
    When mahasiswa memilih kategori buku "Informatika"
    Then tombol kategori "Informatika" menjadi aktif
    And daftar buku perpustakaan ditampilkan

  @Library @Filter
  Scenario: TC-LIB-006 - Kembali ke semua buku setelah filter kategori aktif
    When mahasiswa memilih kategori buku "Informatika"
    And mahasiswa menekan tombol kategori "Semua"
    Then tombol kategori "Semua" menjadi aktif
    And daftar buku perpustakaan ditampilkan

  # ─── Fitur Pemesanan Buku ────────────────────────────────────────────────────

  @Library @Order
  Scenario: TC-LIB-007 - Memesan buku yang tersedia
    Given terdapat buku yang tersedia di halaman katalog
    When mahasiswa memesan buku pertama yang tersedia
    Then notifikasi pemesanan buku berhasil ditampilkan

  @Library @Order @NegativeTest
  Scenario: TC-LIB-008 - Memesan buku yang stoknya sedang kosong (tombol disabled)
    When mahasiswa mencari buku dengan kata kunci "Out of Stock Book"
    Then tombol pesan pada buku tersebut tidak bisa diklik

  @Library @Order
  Scenario: TC-LIB-009 - Riwayat stok buku berkurang setelah berhasil dipesan
    Given mahasiswa melihat jumlah stok awal buku "Pemrograman Web dengan Laravel"
    When mahasiswa memesan buku "Pemrograman Web dengan Laravel"
    Then jumlah ketersediaan buku "Pemrograman Web dengan Laravel" berkurang satu
