Feature: Katalog Buku Perpustakaan
  Sebagai mahasiswa SIA-UGN yang sudah login
  Saya ingin dapat mencari, memfilter, dan memesan buku
  Agar saya dapat menggunakan layanan perpustakaan universitas

  Background:
    Given saya sudah login sebagai mahasiswa
    And saya berada di halaman katalog buku

  # ─── Smoke Test ─────────────────────────────────────────────────────────────

  @Library @SmokeTest
  Scenario: Halaman katalog buku berhasil dimuat
    Then halaman katalog buku ditampilkan dengan benar
    And daftar buku perpustakaan ditampilkan

  # ─── Fitur Pencarian ─────────────────────────────────────────────────────────

  @Library @Search
  Scenario: Mencari buku dengan kata kunci yang valid
    When mahasiswa mencari buku dengan kata kunci "Pemrograman"
    Then buku dengan kata kunci "Pemrograman" ditampilkan pada hasil pencarian

  @Library @Search @NegativeTest
  Scenario: Mencari buku dengan kata kunci yang tidak ada
    When mahasiswa mencari buku dengan kata kunci "xyzabcnotexist999"
    Then pesan buku tidak ditemukan ditampilkan

  @Library @Search
  Scenario: Mencari buku menggunakan tombol Enter pada keyboard
    When mahasiswa mengetik kata kunci "Laravel" pada kolom pencarian
    And mahasiswa menekan tombol Enter pada keyboard
    Then halaman menampilkan hasil pencarian untuk "Laravel"

  # ─── Fitur Filter Kategori ───────────────────────────────────────────────────

  @Library @Filter
  Scenario: Memfilter buku berdasarkan kategori
    When mahasiswa memilih kategori buku "Informatika"
    Then tombol kategori "Informatika" menjadi aktif
    And daftar buku perpustakaan ditampilkan

  @Library @Filter
  Scenario: Kembali ke semua buku setelah filter kategori aktif
    When mahasiswa memilih kategori buku "Informatika"
    And mahasiswa menekan tombol kategori "Semua"
    Then tombol kategori "Semua" menjadi aktif
    And daftar buku perpustakaan ditampilkan

  # ─── Fitur Pemesanan Buku ────────────────────────────────────────────────────

  @Library @Order
  Scenario: Memesan buku yang tersedia
    Given terdapat buku yang tersedia di halaman katalog
    When mahasiswa memesan buku pertama yang tersedia
    Then notifikasi pemesanan buku berhasil ditampilkan

  @Library @Order @NegativeTest
  Scenario: Memesan buku yang stoknya sedang kosong (tombol disabled)
    When mahasiswa mencari buku dengan kata kunci "Out of Stock Book"
    Then tombol pesan pada buku tersebut tidak bisa diklik

  @Library @Order
  Scenario: Riwayat stok buku berkurang setelah berhasil dipesan
    Given mahasiswa melihat jumlah stok awal buku "Pemrograman Web dengan Laravel"
    When mahasiswa memesan buku "Pemrograman Web dengan Laravel"
    Then jumlah ketersediaan buku "Pemrograman Web dengan Laravel" berkurang satu
