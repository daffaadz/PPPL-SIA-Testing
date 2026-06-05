Feature: Usulan Buku Perpustakaan
  Sebagai mahasiswa SIA-UGN yang sudah login
  Saya ingin dapat mengusulkan buku baru untuk koleksi perpustakaan
  Agar perpustakaan dapat menyediakan buku yang relevan dengan kebutuhan akademik saya

  Background:
    Given saya sudah login sebagai mahasiswa
    And saya berada di halaman usulan buku

  # ─── Smoke Test ─────────────────────────────────────────────────────────────

  @Library @SmokeTest
  Scenario: Halaman usulan buku berhasil dimuat
    Then halaman usulan buku ditampilkan dengan benar
    And form usulan buku ditampilkan

  # ─── Fitur Form Usulan ───────────────────────────────────────────────────────

  @Library @FormUsulan
  Scenario: Form usulan menampilkan semua field yang diperlukan
    Then form usulan buku ditampilkan
    And field judul buku ditampilkan
    And field nama penulis ditampilkan
    And field alasan usulan ditampilkan
    And tombol kirim usulan ditampilkan

  @Library @FormUsulan
  Scenario: Counter karakter alasan bertambah sesuai teks yang diketik
    When mahasiswa mengisi alasan usulan "Buku ini sangat dibutuhkan"
    Then counter karakter menampilkan 26

  # ─── Fitur Kirim Usulan — Positif ────────────────────────────────────────────

  @Library @KirimUsulan
  Scenario: Berhasil mengirim usulan buku dengan data yang valid
    When mahasiswa mengisi judul buku "Machine Learning dengan Python"
    And mahasiswa mengisi nama penulis "Jane Author"
    And mahasiswa mengisi alasan usulan "Dibutuhkan untuk referensi mata kuliah AI dan machine learning di semester ini"
    And mahasiswa menekan tombol kirim usulan
    Then usulan buku berhasil dikirim

  @Library @KirimUsulan
  Scenario: Form direset setelah usulan buku berhasil dikirim
    When mahasiswa mengisi judul buku "Clean Code"
    And mahasiswa mengisi nama penulis "Robert C. Martin"
    And mahasiswa mengisi alasan usulan "Penting untuk meningkatkan kualitas penulisan kode program bagi mahasiswa informatika"
    And mahasiswa menekan tombol kirim usulan
    Then form usulan direset setelah pengiriman

  @Library @KirimUsulan
  Scenario: Riwayat usulan diperbarui setelah usulan berhasil dikirim
    When mahasiswa mengisi judul buku "The Pragmatic Programmer"
    And mahasiswa mengisi nama penulis "Dave Thomas"
    And mahasiswa mengisi alasan usulan "Buku ini memberikan panduan praktis untuk menjadi programmer profesional yang dibutuhkan mahasiswa"
    And mahasiswa menekan tombol kirim usulan
    Then riwayat usulan menampilkan buku "The Pragmatic Programmer"

  # ─── Fitur Kirim Usulan — Negatif ────────────────────────────────────────────

  @Library @KirimUsulan @NegativeTest
  Scenario: Gagal mengirim usulan buku tanpa mengisi judul buku
    And mahasiswa mengisi nama penulis "John Doe"
    And mahasiswa mengisi alasan usulan "Buku ini sangat dibutuhkan untuk referensi akademik mahasiswa program studi informatika"
    And mahasiswa menekan tombol kirim usulan
    Then pesan error judul ditampilkan

  @Library @KirimUsulan @NegativeTest
  Scenario: Gagal mengirim usulan buku tanpa mengisi nama penulis
    When mahasiswa mengisi judul buku "Design Patterns"
    And mahasiswa mengisi alasan usulan "Buku ini sangat dibutuhkan untuk referensi akademik mahasiswa program studi informatika"
    And mahasiswa menekan tombol kirim usulan
    Then pesan error penulis ditampilkan

  @Library @KirimUsulan @NegativeTest
  Scenario: Gagal mengirim usulan buku tanpa mengisi alasan
    When mahasiswa mengisi judul buku "Introduction to Algorithms"
    And mahasiswa mengisi nama penulis "Thomas H. Cormen"
    And mahasiswa menekan tombol kirim usulan
    Then pesan error alasan ditampilkan

  @Library @KirimUsulan @NegativeTest
  Scenario: Gagal mengirim usulan buku dengan alasan yang terlalu pendek
    When mahasiswa mengisi judul buku "The Pragmatic Programmer"
    And mahasiswa mengisi nama penulis "Dave Thomas"
    And mahasiswa mengisi alasan usulan "Bagus"
    And mahasiswa menekan tombol kirim usulan
    Then pesan error alasan ditampilkan
