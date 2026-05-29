#language: id
Feature: Login Mahasiswa dan Admin
  Sebagai pengguna SIA-UGN
  Saya ingin dapat login ke sistem
  Agar dapat mengakses fitur sesuai peran saya

  Background:
    Given saya membuka halaman login SIA-UGN

  @Login @SmokeTest
  Scenario: Login berhasil sebagai mahasiswa
    When saya memasukkan email "handoko@gmail.com" dan password "hanan123"
    And saya menekan tombol Login
    Then saya berhasil login dan diarahkan keluar dari halaman login

  @Login @SmokeTest
  Scenario: Login berhasil sebagai admin
    When saya memasukkan email "manager@gmail.com" dan password "manager123"
    And saya menekan tombol Login
    Then saya berhasil login dan diarahkan keluar dari halaman login

  @Login @NegativeTest
  Scenario: Login gagal dengan password salah
    When saya memasukkan email "handoko@gmail.com" dan password "passwordsalah"
    And saya menekan tombol Login
    Then saya melihat pesan error pada halaman login

  @Login @NegativeTest
  Scenario: Login gagal dengan email yang tidak terdaftar
    When saya memasukkan email "tidakada@test.com" dan password "hanan123"
    And saya menekan tombol Login
    Then saya melihat pesan error pada halaman login

  @Login @NegativeTest
  Scenario: Login gagal ketika field kosong
    When saya tidak mengisi email dan password
    And saya menekan tombol Login
    Then halaman login masih ditampilkan
