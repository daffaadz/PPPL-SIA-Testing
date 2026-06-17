Feature: Usulan Buku Perpustakaan
  Sebagai mahasiswa SIA-UGN yang sudah login
  Saya ingin dapat mengusulkan buku baru untuk koleksi perpustakaan

  Background:
    Given saya sudah login sebagai mahasiswa
    And saya berada di halaman usulan buku

  @Library @SmokeTest
  Scenario: TC-SUG-001 - Halaman usulan buku berhasil dimuat
    Then halaman usulan buku ditampilkan dengan benar
    And form usulan buku ditampilkan

  @Library @KirimUsulan
  Scenario: TC-SUG-002 - Berhasil mengirim usulan buku dengan data yang valid
    When mahasiswa mengisi form usulan dengan judul "Machine Learning dengan Python", penulis "Jane Author", dan alasan "Dibutuhkan untuk referensi mata kuliah AI"
    And mahasiswa menekan tombol kirim usulan
    Then usulan buku berhasil dikirim
    And form usulan direset setelah pengiriman
    And riwayat usulan menampilkan buku "Machine Learning dengan Python"

  @Library @KirimUsulan @NegativeTest
  Scenario: TC-SUG-003 - Gagal mengirim usulan buku saat form tidak diisi dengan lengkap
    When mahasiswa menekan tombol kirim usulan
    Then pesan error validasi form usulan ditampilkan
