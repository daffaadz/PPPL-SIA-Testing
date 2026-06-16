# Data Seeding — Library Activities

Base URL: `https://be-sia-ugn-production.up.railway.app`

---

## Step 1 — Login Mahasiswa

```
POST /api/auth/login
Content-Type: application/json

{ "email": "handoko@gmail.com", "password": "hanan123" }
```

Simpan `token` dari response.

---

## Step 2 — Cek ID Buku Tersedia

```
GET /api/library/books
Authorization: Bearer <token>
```

Catat `id_book` yang `is_available: true`.

---

## Step 3 — Pesan Buku (→ status: ordered)

```
POST /api/library/books/{id_book}/order
Authorization: Bearer <token>
```

Catat `id_book_order` dari response.

---

## Step 4 — Login Admin

```
POST /api/auth/login
Content-Type: application/json

{ "email": "admin@gmail.com", "password": "admin123" }
```

Simpan `token` admin.

---

## Step 5 — Konfirmasi Peminjaman (→ status: borrowed)

```
PATCH /api/admin/library/orders/{id_book_order}/confirm-borrow
Authorization: Bearer <token_admin>
Content-Type: application/json

{ "admin_note": "OK" }
```

---

> **Catatan:** TC-ACT-009 akan menghabiskan 1 pesanan `ordered`.
> Ulangi Step 1–3 jika ingin test ulang TC-ACT-009.
