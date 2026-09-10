# PRD: Aplikasi Manajemen Servis Elektronik
**Working Title:** ServisKu (dapat diganti sesuai preferensi)
**Platform:** Android (Kotlin + XML)
**Versi Dokumen:** 1.1 — MVP (requirement telah dikonfirmasi)
**Tanggal:** 7 September 2026

---

## 1. Latar Belakang

Pengguna aplikasi ini adalah seorang teknisi servis elektronik yang menerima pekerjaan melalui panggilan langsung dari klien. Saat ini, pencatatan tugas perbaikan kemungkinan besar masih dilakukan secara manual (catatan kertas, chat WhatsApp, atau hanya mengandalkan ingatan), yang berisiko menyebabkan:
- Tugas atau follow-up ke klien terlewat
- Status pekerjaan tidak jelas (barang siapa yang masih dikerjakan, mana yang menunggu sparepart)
- Riwayat servis pelanggan sulit dilacak kembali

Dokumen ini disusun sebagai **MVP (Minimum Viable Product)**. Sejumlah pertanyaan kebutuhan sudah dikonfirmasi langsung ke pengguna akhir (lihat Bagian 11), sehingga scope pada versi ini sudah lebih pasti dibanding draf awal.

## 2. Tujuan Produk

- Membantu teknisi mencatat dan melacak status setiap pekerjaan servis dari mulai diterima hingga selesai/diambil klien.
- Mengurangi risiko pekerjaan terlewat atau lupa follow-up ke klien.
- Menyediakan riwayat servis yang mudah dicari kembali.
- Dikembangkan cepat dengan fitur minimal namun fungsional (bisa langsung dipakai harian), dengan ruang pengembangan lanjutan setelah kebutuhan riil lebih jelas.

## 3. Target Pengguna

- **Primer:** Teknisi servis elektronik — **dikonfirmasi single-user**, hanya 1 teknisi yang memakai aplikasi ini (tidak ada rencana multi-user). Artinya tidak perlu sistem login/akun sama sekali — aplikasi langsung bisa dipakai setelah dibuka.
- Pelanggan **tidak** menjadi pengguna aplikasi (tidak ada login/akses untuk klien).

## 4. Ruang Lingkup

### In-Scope (MVP)
1. Tambah tugas servis baru
2. Manajemen status tugas
3. Daftar & pencarian/filter tugas
4. Detail tugas + riwayat perubahan status
5. Catatan tambahan per tugas
6. Estimasi & biaya servis
7. Penyimpanan data lokal (offline-first, tanpa server)
8. **Backup & restore data sederhana** *(baru — dikonfirmasi dibutuhkan)*

### Out-of-Scope (dikonfirmasi tidak dibutuhkan — final, bukan sekadar ditunda)
- Notifikasi pengingat otomatis
- Cetak/kirim invoice PDF
- Laporan/analitik pendapatan
- Login multi-user / multi-teknisi (aplikasi ini permanen single-user)

### Out-of-Scope (belum ditanyakan, dipertimbangkan jika muncul kebutuhan)
- Akses pelanggan untuk cek status servis sendiri
- Manajemen stok sparepart

## 5. Fitur Utama (Detail)

### 5.1 Tambah Tugas Servis Baru
Input yang dicatat:
- Nama pelanggan
- Nomor HP pelanggan
- Jenis barang — **dropdown/spinner** dengan pilihan tetap sesuai kategori paling sering diservis: *Kulkas, TV, Kipas Angin, AC, Mesin Cuci*, ditambah opsi **"Lainnya"** yang memunculkan kolom teks bebas untuk kasus di luar kategori tersebut. Dropdown dipilih (bukan free text) supaya data konsisten dan bisa difilter dengan rapi di daftar tugas.
- Merk & tipe barang
- Deskripsi keluhan/kerusakan
- Tanggal diterima
- Status awal (default: "Diterima")
- Foto barang (opsional — ambil dari kamera/galeri)

### 5.2 Manajemen Status Tugas
Status yang disarankan (dapat disesuaikan):
1. Diterima
2. Dicek/Diagnosa
3. Menunggu Sparepart
4. Dikerjakan
5. Selesai
6. Sudah Diambil/Dikembalikan
7. Batal

Setiap perubahan status tercatat dengan timestamp (riwayat status).

### 5.3 Daftar Tugas
- Menampilkan seluruh tugas dalam bentuk list (RecyclerView)
- Diurutkan berdasarkan tanggal terbaru
- Filter berdasarkan status (misal: tampilkan yang "Menunggu Sparepart" saja)
- Pencarian berdasarkan nama pelanggan/jenis barang

### 5.4 Detail Tugas
- Menampilkan seluruh informasi tugas
- Riwayat perubahan status
- Tombol update status
- Tombol edit data
- Tombol hapus tugas
- Tombol hubungi pelanggan langsung (buka dialer/WhatsApp dengan nomor terkait)

### 5.5 Catatan/Log Tambahan
- Kolom catatan bebas per tugas (misal: "sudah pesan sparepart tanggal X", "klien minta ditunda")

### 5.6 Estimasi & Biaya
- Kolom estimasi biaya
- Kolom biaya final (diisi saat selesai)

### 5.7 Backup & Restore Data *(baru)*
Karena hanya 1 teknisi yang memakai aplikasi dan backup "sepertinya perlu" (bukan kebutuhan kompleks seperti sinkronisasi real-time), pendekatan paling cepat dan proporsional untuk MVP adalah:

- **Android Auto Backup for Apps** (fitur bawaan Android) — mem-backup otomatis database aplikasi ke akun Google pengguna setiap perangkat melakukan backup rutin (biasanya saat idle & charging). Implementasinya sangat ringan: cukup konfigurasi `android:allowBackup="true"` dan backup rules di manifest, tanpa perlu server atau kode tambahan. Saat aplikasi di-install ulang di HP baru dengan akun Google yang sama, data akan otomatis dikembalikan.
- **Catatan keterbatasan:** backup otomatis ini tidak instan (terjadwal oleh sistem, bukan on-demand) dan bergantung pengaturan akun Google pengguna. Kalau teknisi ingin kontrol penuh (misalnya backup manual persis sebelum ganti HP), bisa ditambahkan tombol **"Export Data"** sederhana (menyimpan seluruh data ke satu file yang bisa dikirim ke Google Drive/WhatsApp) — ini opsional dan bisa menyusul di iterasi berikutnya jika waktu pengerjaan MVP terbatas.

## 6. Alur Pengguna (User Flow)

1. Klien menghubungi teknisi → teknisi buka aplikasi → tambah tugas baru → isi data → simpan (status: Diterima)
2. Teknisi mengerjakan → update status sesuai progres
3. Jika butuh sparepart → update status "Menunggu Sparepart" + catatan
4. Selesai dikerjakan → update status "Selesai" + isi biaya final
5. Barang diambil/dikembalikan → update status "Sudah Diambil"
6. Riwayat tugas tetap tersimpan dan dapat dicari kembali kapan saja

## 7. Model Data (Disederhanakan)

**RepairJob**
| Field | Tipe | Keterangan |
|---|---|---|
| id | Long | Primary key |
| customerName | String | Nama pelanggan |
| customerPhone | String | Nomor HP pelanggan |
| itemType | String | Jenis barang — diisi dari dropdown (Kulkas/TV/Kipas Angin/AC/Mesin Cuci) atau teks bebas jika pilih "Lainnya" |
| itemBrand | String | Merk/tipe barang |
| issueDescription | String | Deskripsi keluhan |
| status | Enum | Status saat ini |
| estimatedCost | Double? | Estimasi biaya |
| finalCost | Double? | Biaya final |
| notes | String? | Catatan tambahan |
| photoUri | String? | Path/URI foto barang |
| createdAt | Long | Timestamp dibuat |
| updatedAt | Long | Timestamp update terakhir |

**StatusHistory**
| Field | Tipe | Keterangan |
|---|---|---|
| id | Long | Primary key |
| repairJobId | Long | Foreign key ke RepairJob |
| status | Enum | Status pada saat itu |
| timestamp | Long | Waktu perubahan |
| note | String? | Catatan opsional |

## 8. Kebutuhan Non-Fungsional

- Aplikasi harus bisa digunakan sepenuhnya **offline**, karena teknisi sering bekerja di lokasi dengan koneksi terbatas.
- Performa ringan, waktu buka aplikasi cepat.
- UI sederhana, mudah dipakai satu tangan (sering dipakai sambil bekerja/di lapangan).

## 9. Tech Stack

- **Bahasa:** Kotlin
- **UI:** XML (Android Views)
- **Database lokal:** Room (SQLite) — pilihan tercepat untuk MVP tanpa backend
- **Arsitektur:** MVVM sederhana (ViewModel + LiveData/StateFlow) agar mudah dikembangkan lebih lanjut
- **Image loading** (jika pakai foto): Glide atau Coil (opsional)
- **Backup:** Android Auto Backup for Apps (konfigurasi manifest, bukan solusi custom) — lihat fitur 5.7

## 10. Metrik Keberhasilan (MVP)

- Aplikasi dapat mencatat, mengubah status, dan mencari tugas tanpa bug/crash
- Waktu input tugas baru < 1 menit
- Digunakan secara konsisten oleh teknisi setiap ada tugas baru

## 11. Keputusan Produk (Sudah Dikonfirmasi ke Pengguna Akhir)

| Pertanyaan | Jawaban | Dampak ke PRD |
|---|---|---|
| Single-user atau multi-user? | Hanya 1 teknisi | Tidak perlu sistem login/akun (Bagian 3) |
| Perlu backup data? | Sepertinya perlu | Ditambahkan fitur 5.7 (Android Auto Backup) |
| Perlu notifikasi pengingat otomatis? | Tidak perlu | Tetap out-of-scope (Bagian 4) |
| Perlu cetak/kirim invoice? | Tidak perlu | Tetap out-of-scope (Bagian 4) |
| Kategori barang tersering? | Kulkas, TV, Kipas Angin, AC, Mesin Cuci | Jenis barang jadi dropdown, bukan free text (Bagian 5.1) |
| Perlu laporan pendapatan? | Tidak perlu | Tetap out-of-scope (Bagian 4) |

## 12. Rencana Pengembangan Lanjutan (Post-MVP)

Fitur berikut sudah dikonfirmasi **tidak dibutuhkan** untuk saat ini (Bagian 11), jadi tidak masuk rencana kecuali kebutuhan pengguna berubah di masa depan: notifikasi pengingat, cetak invoice, laporan pendapatan, dan multi-user.

Yang masih realistis dipertimbangkan nanti jika waktu memungkinkan:
- Tombol **export data manual** (pelengkap Android Auto Backup di 5.7, untuk kontrol backup on-demand)
- Sinkronisasi cloud yang lebih andal (misal Firebase Firestore), jika suatu saat backup otomatis Android dirasa kurang cukup
- Manajemen stok sparepart, jika kebutuhan ini muncul di kemudian hari
