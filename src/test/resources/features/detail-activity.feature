Feature: Detail Aktivitas Perpustakaan
  Sebagai mahasiswa SIA-UGN yang sudah login
  Saya ingin melihat detail aktivitas pemesanan atau peminjaman buku
  Agar saya dapat mengetahui status dan informasi lengkap aktivitas saya

  Background:
    Given saya sudah login sebagai mahasiswa
    And saya berada di halaman aktivitas perpustakaan

  # ─── Smoke Test ─────────────────────────────────────────────────────────────

  @Library @SmokeTest
  Scenario: Halaman detail aktivitas berhasil dimuat
    When mahasiswa membuka salah satu detail aktivitas
    Then halaman detail aktivitas ditampilkan dengan benar
    And informasi buku ditampilkan
    And informasi pemesanan ditampilkan

  # ─── Fitur Detail Informasi ──────────────────────────────────────────────────

  @Library @Detail
  Scenario: Menampilkan informasi detail aktivitas pemesanan
    When mahasiswa membuka detail aktivitas
    Then judul buku ditampilkan
    And nama penulis buku ditampilkan
    And nama pemesan ditampilkan
    And ID pesanan ditampilkan
    And tanggal pemesanan ditampilkan
    And status pesanan ditampilkan

  # ─── Fitur Status Aktivitas ──────────────────────────────────────────────────

  @Library @Status
  Scenario: Menampilkan status Dipesan
    Given aktivitas memiliki status "Dipesan"
    When mahasiswa membuka detail aktivitas
    Then status "Dipesan" ditampilkan
    And tanggal peminjaman kosong
    And durasi peminjaman kosong
    And tombol "Batalkan Pesanan" ditampilkan

  @Library @Status
  Scenario: Menampilkan status Dipinjam
    Given aktivitas memiliki status "Dipinjam"
    When mahasiswa membuka detail aktivitas
    Then status "Dipinjam" ditampilkan
    And tanggal peminjaman ditampilkan
    And durasi peminjaman ditampilkan
    And tombol "Batalkan Pesanan" tidak ditampilkan

  # ─── Fitur Pembatalan Pesanan ────────────────────────────────────────────────

  @Library @Cancel
  Scenario: Membatalkan pesanan buku
    Given aktivitas memiliki status "Dipesan"
    When mahasiswa menekan tombol "Batalkan Pesanan"
    And mahasiswa mengonfirmasi pembatalan
    Then pesanan berhasil dibatalkan
    And status berubah menjadi "Dibatalkan"

  @Library @Cancel @NegativeTest
  Scenario: Gagal membatalkan pesanan tanpa konfirmasi
    Given aktivitas memiliki status "Dipesan"
    When mahasiswa menekan tombol "Batalkan Pesanan"
    And mahasiswa tidak mengonfirmasi pembatalan
    Then pesanan tidak dibatalkan

  # ─── Navigasi ────────────────────────────────────────────────────────────────

  @Library @Navigation
  Scenario: Kembali ke halaman aktivitas
    When mahasiswa menekan tombol "Kembali ke Aktivitas"
    Then mahasiswa diarahkan ke halaman aktivitas perpustakaan

  # ─── Negative Test ───────────────────────────────────────────────────────────

  @Library @NegativeTest
  Scenario: Gagal membuka detail aktivitas dengan ID tidak valid
    When mahasiswa membuka detail aktivitas dengan ID tidak valid
    Then sistem menampilkan pesan "Aktivitas tidak ditemukan"

  @Library @NegativeTest
  Scenario: Akses detail aktivitas tanpa login
    Given mahasiswa belum login
    When mahasiswa mengakses halaman detail aktivitas
    Then mahasiswa diarahkan ke halaman login