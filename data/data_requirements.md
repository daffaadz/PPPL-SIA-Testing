# Kebutuhan Data (Seeder) untuk Testing SIA-UGN Modul Perpustakaan

Berikut adalah daftar data yang perlu ada (diseed) di database sebelum menjalankan *test suite*.

## 1. Data Akun (User)
**Catatan:** Pembuatan user mungkin berada di modul otentikasi inti SIA, bukan API perpustakaan.
- **Akun Mahasiswa**: Email: `handoko@gmail.com` | Password: `hanan123`
- **Akun Admin**: Email: `admin@gmail.com` | Password: `admin123`

---

## 2. Data Kategori Buku
Diperlukan untuk membuat data buku dan memvalidasi fitur filter kategori.

**// Kategori "Informatika"**
endpoint : `POST /api/admin/library/categories`
body:
```json
{
    "name": "Informatika",
    "slug": "informatika",
    "description": "Buku-buku terkait ilmu komputer dan pemrograman"
}
```

---

## 3. Data Buku
Dibutuhkan setidaknya 3 buku dengan karakteristik stok dan judul yang berbeda.

**// Data buku tersedia (Kategori Informatika)**
endpoint : `POST /api/admin/library/books`
body:
```json
{
    "title": "Pemrograman Web dengan Laravel",
    "author": "John Doe",
    "publisher": "Penerbit UGN",
    "year": 2024,
    "isbn": "978-602-1234-56-7",
    "id_book_category": 1,
    "total_stock": 5
}
```

**// Data buku dengan stok kosong**
endpoint : `POST /api/admin/library/books`
body:
```json
{
    "title": "Out of Stock Book",
    "author": "Jane Doe",
    "publisher": "Penerbit UGN",
    "year": 2023,
    "isbn": "978-602-9999-56-7",
    "id_book_category": 1,
    "total_stock": 0
}
```

**// Data buku untuk fitur pencarian umum**
endpoint : `POST /api/admin/library/books`
body:
```json
{
    "title": "Rekayasa Perangkat Lunak",
    "author": "Ian Sommerville",
    "publisher": "Pearson",
    "year": 2021,
    "isbn": "978-123-4567-89-0",
    "id_book_category": 1,
    "total_stock": 10
}
```

---

## 4. Data Peminjaman (Pesanan Buku)
Untuk memastikan data ini valid, mahasiswa harus terlebih dahulu memesan buku via API, lalu diubah statusnya via API Admin.

**Langkah 1: Mahasiswa Memesan 4 Buku Berbeda (Butuh 4 eksekusi untuk ID Buku yang berbeda)**
endpoint : `POST /api/library/books/{id_book}/order` (Akses Mahasiswa)
body: `{}`

**Langkah 2: Mengubah Status Masing-masing Pesanan (Akses Admin)**

**// Data peminjaman status "Dipesan" (Tidak perlu diapa-apakan setelah dipesan)**
*(Biarkan status tetap "ordered")*

**// Data peminjaman status "Dipinjam"**
endpoint : `PATCH /api/admin/library/orders/{id_order_2}/confirm-borrow`
body:
```json
{
    "admin_note": "Silakan ambil di perpustakaan"
}
```

**// Data peminjaman status "Dikembalikan"**
endpoint : `PATCH /api/admin/library/orders/{id_order_3}/confirm-return`
*(Note: Pesanan harus berstatus "borrowed" terlebih dahulu sebelum dikonfirmasi pengembaliannya)*
body:
```json
{
    "admin_note": "Buku telah diterima dalam kondisi baik"
}
```

**// Data peminjaman status "Dibatalkan" (Akses Mahasiswa)**
endpoint : `PATCH /api/library/activities/{id_order_4}/cancel`
body: `{}`

---

## 5. Data Usulan Buku
Mahasiswa mengusulkan buku, kemudian status dibiarkan "Menunggu" (pending). 
Buat minimal 2 usulan agar skenario Admin (Setujui dan Tolak) masing-masing memiliki data "Menunggu".

**// Data usulan pertama (Untuk disetujui admin)**
endpoint : `POST /api/library/suggestions` (Akses Mahasiswa)
body:
```json
{
    "title": "Artificial Intelligence A Modern Approach",
    "author": "Stuart Russell",
    "reason": "Sangat penting untuk referensi skripsi"
}
```

**// Data usulan kedua (Untuk ditolak admin)**
endpoint : `POST /api/library/suggestions` (Akses Mahasiswa)
body:
```json
{
    "title": "Komik Detektif Conan Vol 100",
    "author": "Gosho Aoyama",
    "reason": "Untuk hiburan di kala suntuk belajar"
}
```
