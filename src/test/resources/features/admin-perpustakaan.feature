Feature: Manajemen Perpustakaan (Admin)
  Sebagai admin SIA-UGN yang sudah login
  Saya ingin mengelola katalog buku, peminjaman, dan usulan buku
  Agar operasional perpustakaan berjalan rapi dan terkontrol

  Background:
    Given saya sudah login sebagai admin perpustakaan

  # ==========================================
  # MANAJEMEN KATALOG BUKU
  # ==========================================

  @Library @Admin @Catalog
  Scenario: TC-ADM-001 - Melihat dashboard dan daftar buku di katalog
    When saya berada di halaman "Manajemen Perpustakaan"
    Then saya dapat melihat statistik total buku, buku tersedia, dipinjam, dan stok kritis
    And daftar buku ditampilkan pada tabel katalog

  @Library @Admin @Catalog @Search
  Scenario: TC-ADM-002 - Mencari buku di katalog
    Given saya berada di halaman "Manajemen Perpustakaan"
    When saya memasukkan kata kunci "Rekayasa Perangkat Lunak" pada kolom pencarian
    Then daftar buku di tabel akan difilter sesuai dengan kata kunci "Rekayasa Perangkat Lunak"

  @Library @Admin @Catalog @Filter
  Scenario: TC-ADM-003 - Memfilter buku berdasarkan stok kritis
    Given saya berada di halaman "Manajemen Perpustakaan"
    When saya menekan tombol filter "Stok Kritis"
    Then tabel hanya menampilkan buku-buku yang memiliki stok tersedia 0

  @Library @Admin @Catalog @Add
  Scenario: TC-ADM-004 - Menambahkan buku baru ke katalog
    Given saya berada di halaman "Manajemen Perpustakaan"
    When saya menekan tombol "Tambah Buku"
    And saya mengisi form buku dengan data valid (judul, penulis, kategori, ISBN, penerbit, tahun, stok)
    And saya menekan tombol "Simpan"
    Then notifikasi "Buku berhasil ditambahkan" ditampilkan pada halaman admin
    And buku baru muncul di daftar katalog

  @Library @Admin @Catalog @Category
  Scenario: TC-ADM-005 - Menambahkan kategori buku baru saat menambah buku
    Given saya berada di form "Tambah Buku Baru"
    When saya membuka dropdown Kategori dan memilih "Tambah Kategori Baru"
    And saya memasukkan nama kategori "Kecerdasan Buatan"
    And saya menyimpan kategori tersebut
    Then kategori "Kecerdasan Buatan" berhasil dibuat dan terpilih di form buku

  @Library @Admin @Catalog @Edit
  Scenario: TC-ADM-006 - Mengedit informasi buku yang sudah ada
    Given saya berada di halaman "Manajemen Perpustakaan"
    When saya menekan tombol ikon Edit pada salah satu buku
    And saya mengubah jumlah "Total Buku" menjadi lebih banyak
    And saya menekan tombol "Simpan"
    Then notifikasi "Buku berhasil diperbarui" ditampilkan pada halaman admin
    And informasi stok buku tersebut di tabel berubah

  @Library @Admin @Catalog @Delete
  Scenario: TC-ADM-007 - Menghapus atau menonaktifkan buku
    Given saya berada di halaman "Manajemen Perpustakaan"
    When saya menekan tombol ikon Hapus pada salah satu buku
    And saya mengonfirmasi tindakan penghapusan
    Then notifikasi sukses ditampilkan
    And buku tersebut dihapus dari daftar katalog

  # ==========================================
  # MANAJEMEN PEMINJAMAN BUKU
  # ==========================================

  @Library @Admin @Order @Filter
  Scenario: TC-ADM-008 - Memfilter daftar peminjaman berdasarkan status
    Given saya berada di tab "Peminjaman" pada Manajemen Peminjaman & Usulan
    When saya memilih filter status "Dipinjam"
    Then tabel hanya menampilkan pesanan dengan status "Dipinjam"

  @Library @Admin @Order @Search
  Scenario: TC-ADM-009 - Mencari pesanan peminjaman mahasiswa
    Given saya berada di tab "Peminjaman" pada Manajemen Peminjaman & Usulan
    When saya mencari menggunakan NIM atau nama mahasiswa
    Then tabel menampilkan data pesanan peminjaman mahasiswa tersebut

  @Library @Admin @Order @Status
  Scenario: TC-ADM-010 - Melihat status peminjaman sebelum dikonfirmasi
    Given terdapat pesanan buku dengan status "Dipesan"
    When saya melihat daftar peminjaman
    Then status "Dipesan" ditampilkan pada pesanan tersebut
    And tombol aksi "Konfirmasi" tersedia

  @Library @Admin @Order @ConfirmBorrow
  Scenario: TC-ADM-011 - Mengonfirmasi peminjaman buku (Admin memberikan buku ke mahasiswa)
    Given terdapat pesanan buku dengan status "Dipesan"
    When saya menekan tombol "Konfirmasi" pada pesanan tersebut
    And saya mengisi prompt catatan admin jika diperlukan
    Then notifikasi konfirmasi peminjaman ditampilkan
    And status pesanan admin berubah menjadi "Dipinjam"

  @Library @Admin @Order @Detail
  Scenario: TC-ADM-012 - Melihat detail pesanan buku yang sedang dipinjam
    Given terdapat pesanan buku dengan status "Dipinjam"
    When saya menekan tombol "Detail" pada pesanan tersebut
    Then saya diarahkan ke halaman detail pesanan admin
    And informasi peminjam, tanggal pinjam, dan jatuh tempo ditampilkan
    And tombol "Konfirmasi Kembali" tersedia di halaman detail

  @Library @Admin @Order @ConfirmReturn
  Scenario: TC-ADM-013 - Mengonfirmasi pengembalian buku (Mahasiswa mengembalikan buku)
    Given terdapat pesanan buku dengan status "Dipinjam"
    When saya menekan tombol "Kembalikan" pada pesanan tersebut
    And saya mengisi prompt catatan admin terkait kondisi buku
    Then notifikasi konfirmasi pengembalian ditampilkan
    And status pesanan admin berubah menjadi "Dikembalikan"
    And tombol aksi pada tabel berubah menjadi "Selesai" (disabled)

  # ==========================================
  # MANAJEMEN USULAN BUKU
  # ==========================================

  @Library @Admin @Suggestions
  Scenario: TC-ADM-014 - Melihat daftar usulan buku mahasiswa
    Given saya berada di halaman Manajemen Peminjaman & Usulan
    When saya berpindah ke tab "Usulan"
    Then daftar usulan buku dari mahasiswa ditampilkan
    And saya dapat melihat ringkasan status usulan (Menunggu, Disetujui, Ditolak)

  @Library @Admin @Suggestions @Approve
  Scenario: TC-ADM-015 - Menyetujui usulan buku baru dari mahasiswa
    Given terdapat usulan buku dengan status "Menunggu" di tab "Usulan"
    When saya menekan tombol "Detail" pada usulan tersebut
    And saya mengisi pesan respon "Usulan diterima, buku akan segera dipesan"
    And saya menekan tombol "Setujui"
    Then notifikasi "Usulan berhasil disetujui" ditampilkan pada halaman admin
    And status usulan berubah menjadi "Disetujui"
    And modal detail usulan tertutup

  @Library @Admin @Suggestions @Reject
  Scenario: TC-ADM-016 - Menolak usulan buku baru dari mahasiswa
    Given terdapat usulan buku dengan status "Menunggu" di tab "Usulan"
    When saya menekan tombol "Detail" pada usulan tersebut
    And saya mengisi pesan respon "Usulan ditolak karena buku di luar konteks prodi"
    And saya menekan tombol "Tolak"
    Then notifikasi "Usulan berhasil ditolak" ditampilkan pada halaman admin
    And status usulan berubah menjadi "Ditolak"
    And modal detail usulan tertutup
