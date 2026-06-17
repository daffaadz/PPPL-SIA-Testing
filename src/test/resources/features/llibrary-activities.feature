Feature: Aktivitas Perpustakaan
  Sebagai mahasiswa yang sudah login
  Saya ingin dapat melihat dan mengelola aktivitas peminjaman buku
  Sehingga saya dapat memantau status pinjaman dan membatalkan pesanan jika diperlukan

  Background:
    Given saya sudah login sebagai mahasiswa
    And saya berada di halaman aktivitas perpustakaan

  @LibraryActivities @Akses @Positive
  Scenario: TC-ACT-001 - Mahasiswa berhasil mengakses halaman aktivitas perpustakaan
    Then halaman aktivitas perpustakaan ditampilkan dengan benar
    And daftar aktivitas peminjaman ditampilkan

  @LibraryActivities @Akses @Positive
  Scenario: TC-ACT-002 - Halaman aktivitas menampilkan informasi kolom tabel dengan lengkap
    Then tabel aktivitas memiliki kolom "Buku"
    And tabel aktivitas memiliki kolom "Status"
    And tabel aktivitas memiliki kolom "Dipesan"
    And tabel aktivitas memiliki kolom "Dipinjam"

  @LibraryActivities @Akses @Negative
  Scenario: TC-ACT-003 - Mahasiswa yang belum login tidak dapat mengakses halaman aktivitas
    Given saya belum login ke sistem
    When saya mengakses URL "/library/activities" secara langsung
    Then saya diarahkan ke halaman login
    And halaman login ditampilkan

  @LibraryActivities @Detail @Positive
  Scenario: TC-ACT-004 - Mahasiswa berhasil melihat detail pesanan dari daftar aktivitas
    Given terdapat minimal satu aktivitas peminjaman pada daftar
    When mahasiswa menekan tombol "Detail" pada aktivitas pertama
    Then saya diarahkan ke halaman detail pesanan mahasiswa
    And informasi judul buku ditampilkan di halaman detail
    And informasi status pesanan ditampilkan di halaman detail
    And informasi tanggal pemesanan dan durasi ditampilkan

  @LibraryActivities @Detail @Positive
  Scenario: TC-ACT-005 - Halaman detail pesanan menampilkan informasi lengkap peminjaman
    Given terdapat aktivitas peminjaman dengan status "Dipinjam"
    When mahasiswa membuka detail pesanan tersebut
    Then nama buku ditampilkan dengan benar
    And nama peminjam ditampilkan dengan benar
    And tanggal pinjam ditampilkan dengan benar
    And durasi pinjam ditampilkan dengan benar
    And status "Dipinjam" ditampilkan pada badge status

  @LibraryActivities @Detail @Positive
  Scenario: TC-ACT-006 - Mahasiswa dapat kembali ke halaman daftar aktivitas dari halaman detail
    Given terdapat minimal satu aktivitas peminjaman pada daftar
    When mahasiswa menekan tombol "Detail" pada aktivitas pertama
    And mahasiswa menekan tombol kembali atau navigasi back
    Then saya kembali ke halaman daftar aktivitas perpustakaan
    And daftar aktivitas masih ditampilkan

  @LibraryActivities @Filter @Positive
  Scenario: TC-ACT-007 - Mahasiswa dapat memfilter aktivitas berdasarkan status "Dipinjam"
    When mahasiswa memilih filter status aktivitas "Dipinjam"
    Then daftar aktivitas hanya menampilkan pesanan dengan status "Dipinjam"
    And tidak ada baris dengan status selain "Dipinjam" pada tabel

  @LibraryActivities @Filter @Positive
  Scenario: TC-ACT-008 - Mahasiswa dapat mereset filter ke "Semua" untuk melihat seluruh aktivitas
    Given mahasiswa sudah memilih filter status "Dipinjam"
    When mahasiswa memilih filter status aktivitas "Semua"
    Then seluruh daftar aktivitas peminjaman ditampilkan kembali
    And semua status pesanan terlihat pada tabel

  @LibraryActivities @Batalkan @Positive
  Scenario: TC-ACT-009 - Mahasiswa berhasil membatalkan pesanan dengan status "Menunggu Konfirmasi"
    Given terdapat pesanan buku dengan status "Menunggu Konfirmasi" pada daftar aktivitas
    When mahasiswa menekan tombol "Batalkan" pada pesanan tersebut
    And mahasiswa mengonfirmasi tindakan pembatalan pada dialog konfirmasi
    Then notifikasi aktivitas "Pesanan berhasil dibatalkan" ditampilkan
    And status pesanan berubah menjadi "Dibatalkan"

  @LibraryActivities @Batalkan @Positive
  Scenario: TC-ACT-010 - Browser meminta konfirmasi sebelum pesanan dibatalkan
    Given terdapat pesanan buku dengan status "Menunggu Konfirmasi" pada daftar aktivitas
    When mahasiswa menekan tombol "Batalkan" pada pesanan tersebut
    Then konfirmasi pembatalan dari browser ditampilkan
    And mahasiswa memilih untuk tidak jadi membatalkan
    And pesanan masih ada dalam daftar aktivitas
