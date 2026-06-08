Feature: Aktivitas Perpustakaan

  Sebagai mahasiswa
  Saya ingin melihat aktivitas perpustakaan
  Agar saya bisa melihat detail peminjaman atau pemesanan buku

  Background:
    Given saya login sebagai mahasiswa

  @Library @SmokeTest
  Scenario: Melihat daftar aktivitas perpustakaan
    When saya membuka halaman aktivitas perpustakaan
    Then daftar aktivitas ditampilkan

  @Library @Regression
  Scenario: Melihat detail aktivitas perpustakaan
    When saya membuka halaman aktivitas perpustakaan
    And saya klik salah satu aktivitas
    Then detail aktivitas perpustakaan ditampilkan
    And status aktivitas terlihat