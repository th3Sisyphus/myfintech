# MyFintech 💰

MyFintech adalah aplikasi manajemen keuangan pribadi berbasis Android yang dibangun menggunakan **Kotlin** dan **Jetpack Compose**. Aplikasi ini dirancang untuk membantu pengguna melacak pemasukan dan pengeluaran mereka dengan mudah, dilengkapi dengan fitur analitik dan pemindaian struk otomatis (OCR).

## Fitur Utama

* **Dashboard Keuangan**: Ringkasan saldo, pemasukan, dan pengeluaran terkini.
* **Pencatatan Transaksi**: Tambah transaksi (Income/Expense) dengan kategori yang dapat disesuaikan.
* **Smart Scan (OCR)**: Fitur pemindaian struk belanja menggunakan **Google ML Kit**. Aplikasi dapat mendeteksi total harga secara otomatis dari gambar struk.
* **Analitik**: Visualisasi data pengeluaran bulanan untuk memantau kesehatan finansial.
* **Manajemen Akun**: Edit profil pengguna dan keamanan (Ganti Password).
* **Local Database**: Data tersimpan aman secara lokal di perangkat.

## Catatan Penting (Work in Progress)

> **Status Autentikasi:**
> Saat ini, fitur **Sign In dengan Google** dan **Sign Up dengan Google** sedang **DINONAKTIFKAN** sementara untuk pemeliharaan/pengembangan lebih lanjut.
>
> Silakan gunakan fitur **Register** dan **Login** manual menggunakan Email dan Password untuk masuk ke dalam aplikasi.

## Tech Stack

Aplikasi ini dibangun menggunakan teknologi Android modern:

* **Bahasa**: [Kotlin](https://kotlinlang.org/)
* **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material3)
* **Arsitektur**: MVVM (Model-View-ViewModel)
* **Database Lokal**: [Room Database](https://developer.android.com/training/data-storage/room)
* **Asynchronous**: Coroutines & Kotlin Flow
* **Navigasi**: Navigation Compose
* **Machine Learning**: [Google ML Kit](https://developers.google.com/ml-kit) (Text Recognition) untuk fitur OCR
* **Backend/Auth**: Firebase Authentication (Integrasi Google Auth disiapkan namun saat ini disable)

## Cara Menjalankan Project

1.  **Clone Repository**
    ```bash
    git clone [https://github.com/username-anda/myfintech.git](https://github.com/username-anda/myfintech.git)
    ```
2.  **Buka di Android Studio**
    * Pastikan menggunakan Android Studio versi terbaru (mendukung Jetpack Compose).
3.  **Sync Gradle**
    * Tunggu hingga proses download dependency selesai.
4.  **Konfigurasi Firebase (Opsional untuk saat ini)**
    * Pastikan file `google-services.json` yang valid ada di folder `app/`.
5.  **Run Aplikasi**
    * Jalankan pada Emulator atau Perangkat Fisik.

## Struktur Project

* `ui/`: Berisi semua komponen antarmuka (Composable screens) seperti Login, Home, Profile, Transaction.
* `viewmodel/`: State management dan logika bisnis.
* `data/`:
    * `local/`: Implementasi Room Database (DAO, Entities).
    * `auth/`: Konfigurasi Autentikasi (GoogleAuthClient).
    * `pref/`: Session Manager.
* `domain/`: Use cases dan Repository.

---
Dikembangkan oleh **Th3SiSyphus**
