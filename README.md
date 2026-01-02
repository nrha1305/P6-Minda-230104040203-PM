# 📔 Minda - Personal Journal App

**Minda** adalah aplikasi jurnal harian berbasis Android yang dirancang dengan pendekatan *privacy-first* (offline). Aplikasi ini memungkinkan pengguna untuk mencatat pikiran, perasaan (mood), dan kejadian sehari-hari dengan antarmuka modern yang nyaman.

Proyek ini dibuat sebagai implementasi **Modul Praktikum Mobile Programming #6 (SQLite - Room ORM)**.

---

##**Screenshots**
![Screenshot_2026-01-03-02-15-13-487_id antasari minda](https://github.com/user-attachments/assets/33bf05df-ea59-48a4-ab26-a65d26eec0a0)
![Screenshot_2026-01-03-02-15-24-852_id antasari minda](https://github.com/user-attachments/assets/9408ba53-3e24-4cb5-8f0d-b539be1586f2)
![Screenshot_2026-01-03-02-15-26-544_id antasari minda](https://github.com/user-attachments/assets/f36243b6-950e-4f9a-8dde-57603c0328a8)
![Screenshot_2026-01-03-02-15-28-360_id antasari minda](https://github.com/user-attachments/assets/cd403a79-efe5-4ac6-ac8c-37efa1c9d5b9)
![Screenshot_2026-01-03-02-15-31-771_id antasari minda](https://github.com/user-attachments/assets/68b96ecf-1e94-47cc-a2a4-6315c1fe533e)
![Screenshot_2026-01-03-02-15-36-408_id antasari minda](https://github.com/user-attachments/assets/b5d1b584-4a6f-4286-89bb-c7487f012fa1)
![Screenshot_2026-01-03-02-15-39-355_id antasari minda](https://github.com/user-attachments/assets/413965eb-e691-4b46-a086-a3f0a42d41d5)
![Screenshot_2026-01-03-02-15-42-088_id antasari minda](https://github.com/user-attachments/assets/ac04bd0d-b357-4c39-b57d-e84a907bf1ac)
![Screenshot_2026-01-03-02-15-43-780_id antasari minda](https://github.com/user-attachments/assets/4ca6f026-03aa-4746-8b66-755f319b6deb)
![Screenshot_2026-01-03-02-27-50-926_id antasari minda](https://github.com/user-attachments/assets/1866f3f3-e68b-4ad7-943e-ce4872fee887)


## ✨ Fitur Utama

* **📝 CRUD Jurnal**: Membuat, membaca, mengedit, dan menghapus catatan harian dengan mudah.
* **🎭 Pelacak Mood**: Menyertakan emoji mood pada setiap entri untuk melacak perasaan pengguna.
* **📅 Kalender Interaktif**: Menampilkan riwayat catatan dalam tampilan kalender bulanan.
* **📊 Insights**: Grafik sederhana untuk melihat statistik mood mingguan/bulanan.
* **🔍 Pencarian**: Fitur pencarian cepat untuk menemukan catatan berdasarkan judul atau isi.
* **🌓 Dark Mode**: Dukungan tema Gelap/Terang yang dapat diatur melalui Pengaturan (menyimpan preferensi user).
* **👋 Onboarding Flow**: Pengalaman pengenalan aplikasi yang ramah untuk pengguna baru.
* **🔒 Privasi Terjamin**: Semua data disimpan secara lokal di perangkat menggunakan **Room Database** dan **DataStore**.

---

## 🛠️ Tech Stack & Library

Aplikasi ini dibangun menggunakan teknologi Android modern (*Modern Android Development*):

* **Bahasa**: [Kotlin](https://kotlinlang.org/) (v2.0+)
* **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material Design 3)
* **Navigasi**: Navigation Compose
* **Database**: [Room Database](https://developer.android.com/training/data-storage/room) (SQLite Abstraction)
* **Penyimpanan Preferensi**: [DataStore Preferences](https://developer.android.com/topic/libraries/architecture/datastore)
* **Async & Concurrency**: Kotlin Coroutines & Flow
* **Architecture**: MVVM (Model-View-ViewModel)
* **Annotation Processing**: KSP (Kotlin Symbol Processing)

---

## 📂 Struktur Project

Berikut adalah struktur folder utama dalam package `id.antasari.minda`:

```text
📂 data
 ┣ 📜 DiaryEntry.kt          # Entity Room (Tabel database)
 ┣ 📜 DiaryDao.kt            # Data Access Object (Query SQL)
 ┣ 📜 MindaDatabase.kt       # Konfigurasi Database Room
 ┣ 📜 DiaryRepository.kt     # Penghubung antara Data & UI
 ┗ 📜 UserPrefsRepository.kt # Pengelola DataStore (Nama User, Dark Mode)

📂 ui
 ┣ 📂 calendar               # Fitur Kalender & ViewModel-nya
 ┣ 📂 navigation             # Konfigurasi Navigasi (Routes & NavHost)
 ┣ 📜 HomeScreen.kt          # Halaman Utama (List & Banner)
 ┣ 📜 NewEntryScreen.kt      # Halaman Tambah Catatan (+ Mood Picker)
 ┣ 📜 NoteDetailScreen.kt    # Halaman Baca Detail
 ┣ 📜 EditEntryScreen.kt     # Halaman Edit Catatan
 ┣ 📜 SettingsScreen.kt      # Halaman Pengaturan (Reset & Theme)
 ┣ 📜 OnboardingScreens.kt   # Layar Sambutan Awal
 ┗ 📜 ExtraScreens.kt        # Halaman Insights/Grafik

📂 util
 ┗ 📜 DateFormatter.kt       # Helper untuk format tanggal
```
## 🚀 Cara Menjalankan Project

1.  **Clone Repository** (atau salin project ini).
2.  Buka di **Android Studio** (Disarankan versi terbaru).
3.  Pastikan **JDK 17** dipilih pada *Settings > Build, Execution, Deployment > Build Tools > Gradle*.
4.  **Sync Project with Gradle Files** untuk mengunduh semua dependency.
5.  **Penting:** Siapkan resource gambar:
    * Pastikan ada file gambar bernama `banner_diary.jpg` di folder `app/src/main/res/drawable/`.
6.  Jalankan aplikasi (Run) pada Emulator atau Perangkat Fisik.
]

## 📝 Catatan Pengembang

* **Room Database**: Menggunakan **KSP** sebagai pengganti KAPT untuk performa build yang lebih cepat. Pastikan versi plugin KSP di `build.gradle.kts` sesuai dengan versi Kotlin yang digunakan.
* **Date Handling**: Menggunakan `java.time` (LocalDate/Instant). Project ini mengaktifkan `coreLibraryDesugaring` untuk kompatibilitas dengan Android versi lama (API < 26).
* **Dark Mode**: Menggunakan `isSystemInDarkTheme()` dikombinasikan dengan preferensi manual di DataStore untuk memaksa tema aplikasi.

---

Made with ❤️ by Nor Hayati (230104040203)
