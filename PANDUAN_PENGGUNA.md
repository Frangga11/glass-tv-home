# Panduan Pengguna Glass TV Home (Untuk Beginner) 📺✨

Selamat datang ke **Glass TV Home**! Panduan ini khas untuk pengguna baru (*beginner-friendly*) supaya anda boleh memasang, mengoptimumkan TV agar laju tanpa iklan, menggunakan remote TV, dan menikmati semua ciri menarik dengan mudah tanpa pening kepala.

---

## 🌟 1. Apa Itu Glass TV Home?

Glass TV Home ialah pengganti skrin utama (*launcher*) untuk Android TV dan Google TV (seperti Xiaomi Mi Box S, Mi TV Stick, Android TV Sharp, Sony, TCL, Philips, Mecool dsb):
* 🚀 **Tiada Iklan & Sangat Ringan**: Hanya bersaiz ~422 KB, lancar dan tidak membebankan TV (menjimatkan lebih 400 MB RAM berbanding skrin asal).
* 🪟 **Gaya Kaca Frosted (tvOS)**: Rekaan moden seperti Apple TV dengan bayang kaca terapung dan susunan aplikasi yang kemas.
* 🕌 **Waktu Solat & Ayat Harian**: Jadual 6 waktu solat Malaysia secara automatik dengan ayat al-Quran pilihan harian.
* ⛅ **Dashboard Cuaca & Jerebu (API)**: Semakan suhu semasa, ramalan 12 jam, 7 hari, serta indeks pencemaran udara rasmi (APIMS Jabatan Alam Sekitar).
* 🌙 **Screensaver Pintar & Jimat Elektrik**: Masuk screensaver selepas 10 minit rehat, dan TV padam automatik (*sleep standby*) selepas 2 jam.

---

## 🤖 2. Cara Paling Mudah: Arahkan AI Buat Semua Automatik! (Noob-Friendly)

Anda **tidak perlu tahu koding** atau menaip perintah teknikal yang rumit. Anda boleh mengarahkan pembantu AI (*AI Coding Assistant*) untuk menyambung ke TV melalui Wi-Fi, membuang iklan pengeluar, menyekat penjejak data (*tracking*), mengoptimumkan TV agar laju, dan memasang Glass TV Home terus ke TV anda.

### 💡 Pilihan AI Yang Boleh Digunakan:
1. **Google Antigravity (Percuma / Free)** ⭐ *Paling Disyorkan*:
   - AI agent rasmi daripada Google DeepMind dengan akses terminal shell penuh, alatan automasi, dan keupayaan menjalankan skrip secara percuma.
2. **Claude Code (Berbayar / Subscription Anthropic)**:
   - CLI coding agent berkuasa daripada Anthropic yang berjalan terus dalam terminal komputer anda.
3. **Cursor / Windsurf IDE (Freemium / Berbayar)**:
   - Editor kod pintar berasaskan AI yang mempunyai terminal shell bersepadu.

---

### 📋 Langkah 1: Hidupkan Developer Mode & Debugging di TV Anda
Sebelum AI boleh menyambung ke TV melalui Wi-Fi, aktifkan mod pembangun pada Android TV anda:
1. Pada remote TV anda, buka **Settings (Tetapan / ikon gear ⚙️)**.
2. Pilih **Device Preferences** (atau **System** pada Google TV baru).
3. Pilih **About (Mengenai)**.
4. Skrol ke bawah hingga jumpa baris bertulis **Build** (atau **Android TV OS Build**).
5. **Tekan butang OK pada remote sebanyak 7 kali berturut-turut** sehingga muncul mesej: *"You are now a developer!"*.
6. Tekan butang *Back* sekali. Anda akan nampak menu baharu iaitu **Developer options (Pilihan pembangun)**.
7. Masuk ke dalam *Developer options*, cari dan **HIDUPKAN**:
   - ✅ **USB debugging** (Tukar kepada ON/Aktif).
   - ✅ **Network debugging / Wireless debugging** (jika ada pada model TV anda).
8. Dapatkan alamat IP TV anda di menu **Settings $\rightarrow$ Network & Internet** (contoh: `192.168.68.102` atau `192.168.1.50`).

---

### 📋 Langkah 2: Berikan Arahan (Prompt) Ini Kepada AI
Buka **Google Antigravity**, **Claude Code**, atau **Cursor** di laptop/komputer anda yang bersambung pada **rangkaian Wi-Fi yang sama dengan TV anda**. Salin dan hantarkan arahan di bawah kepada AI:

```text
Tolong pasangkan Glass TV dari repo https://github.com/aqmangge/glass-tv-home pada Android TV saya.
Model TV saya: [Masukkan jenama & model TV anda, contoh: Xiaomi Mi Box S]
Alamat IP TV: [Masukkan IP TV anda, contoh: 192.168.68.102]

Tolong laksanakan perkara berikut secara automatik:
1. Sambung ke TV saya melalui Wi-Fi menggunakan ADB (port 5555).
2. Buat salinan sandaran (backup) senarai app dan tetapan asal TV ke folder backup terlebih dahulu.
3. Optimumkan & Buang Iklan/Tracking (Debloat):
   - Matikan servis bloatware pengeluar, iklan promosi tajaan (seperti Xiaomi PatchWall/promosi Google), dan servis tracking telemetri latar belakang menggunakan perintah 'pm disable-user --user 0'.
   - Jangan padam sistem asas yang penting (seperti HDMI, Bluetooth remote, Google Play Store, audio, dan Wi-Fi).
4. Aktifkan AdGuard Private DNS (dns.adguard-dns.com) untuk menyekat iklan dan penjejak data sistem secara automatik.
5. Compile/build fail APK Glass TV Home dan pasang sebagai skrin utama (default home launcher).
6. Aktifkan HomeKeyService untuk membolehkan butang Home kembali ke launcher dan fungsi auto-sleep 2 jam berfungsi.
7. Ambil screenshot dari TV dan tunjukkan kepada saya untuk mengesahkan semuanya berjaya dan lancar.
```

---

### 📋 Langkah 3: Tekan "Allow" Pada Skrin TV
Apabila AI mula menyambung ke TV, satu tetingkap pengesahan akan muncul di skrin TV anda:
* *"Allow USB debugging from this computer?"*
* Tandakan kotak: ☑ **"Always allow from this computer"**
* Pilih dan tekan butang **OK / Allow**.

AI akan menguruskan selebihnya: membuang bloatware yang melambatkan TV, mengaktifkan penyekat iklan, memasang Glass TV Home, dan memaparkan gambar tangkap layar (*screenshot*) TV anda yang kini sangat laju dan bersih!

---

### ↩️ Cara Kembali Ke Asal (Undo / Restore)
Semua perubahan yang dibuat oleh AI adalah **100% selamat dan boleh dikembalikan**:
* AI tidak memadam fail sistem secara kekal, hanya menyahaktifkannya.
* AI secara automatik menjana skrip pemulihan (`restore.sh` / `restore.ps1`).
* Jika anda ingin kembali ke skrin asal pengeluar pada masa akan datang, anda hanya perlu beritahu AI:
  > *"Tolong restore semula TV saya kepada tetapan dan launcher asal kilang."*
  Semua aplikasi asal akan dipulihkan serta-merta seperti sedia kala.

---

## 🎮 3. Cara Mengawal Guna Remote TV (D-Pad)

Sangat mudah dan intuitif! Anda hanya perlukan butang arah pada remote TV anda:

| Butang Remote | Tindakan / Fungsi |
| :--- | :--- |
| **Butang ATAS (▲)** | Naik ke atas untuk pilih **Kapsul Cuaca & Jerebu** atau menu Wi-Fi / Tetapan. |
| **Butang TENGAH (OK)** | • Tekan pada aplikasi untuk buka aplikasi tersebut.<br>• Tekan pada kapsul cuaca untuk buka **Dashboard Cuaca & Kualiti Udara penuh**.<br>• **Tekan & Tahan (Long-press)** pada aplikasi untuk susun atau pindahkan ikon ke dalam/luar dock. |
| **Butang BAWAH (▼)** | Turun semula ke barisan aplikasi utama (*Dock*). |
| **Butang KIRI / KANAN (◀ / ▶)** | Bergerak antara aplikasi atau beralih antara menu atas. |
| **Butang BACK (↩)** | Keluar dari Dashboard Cuaca atau Screensaver dan kembali ke skrin utama. |

---

## 📱 4. Penerangan Ciri-Ciri Utama

### A. Kapsul Cuaca & Indeks Jerebu (Bawah Jam)
* Terletak betul-betul di bawah jam utama (contoh: `☁ 28° Overcast  •  API 151 Unhealthy`).
* **Tekan butang Atas (▲) lalu tekan OK** untuk membuka skrin cuaca penuh:
  1. **Suhu Semasa & Angin**: Suhu, suhu rasa (*feels like*), kelembapan, kelajuan angin, indeks UV, waktu matahari terbit & terbenam.
  2. **Kualiti Udara (API / IPU)**: Menggunakan bacaan Jabatan Alam Sekitar (APIMS) dengan bar warna pelangi dinamik, penunjuk nilai, stesen perbandingan, dan nasihat kesihatan rasmi.
  3. **Ramalan 12 Jam**: Barisan kad jam demi jam dengan suhu dan peratus kebarangkalian hujan (`↑ %`).
  4. **Ramalan 7 Hari**: Pandangan cuaca sepanjang minggu dengan suhu maksimum/minimum.

### B. Waktu Solat & Ayat Al-Quran Harian
* **Jadual Solat 6 Waktu**: Subuh, Syuruk, Zohor, Asar, Maghrib, Isyak mengikut zon waktu Malaysia secara tepat.
* **Warna Hijau Zamrud**: Waktu solat yang sedang berlangsung akan automatik bertukar warna hijau terang supaya anda sentiasa peka waktu ibadah.
* **Ayat Al-Quran Pilihan**: Ayat al-Quran harian berserta teks Arab yang indah dan terjemahan maksud.

### C. Gambar Latar Belakang Alam Semula Jadi (Bing Nature)
* Gambar landskap definisi tinggi bertukar secara automatik dengan cerita tempat menarik (nama lokasi, negeri/negara, dan penerangan ringkas).

### D. Screensaver StandBy & Auto-Off 2 Jam (Jimat Elektrik)
* **10 Minit Dibiarkan Tanpa Butang**: TV akan automatik masuk mod *StandBy Screensaver* berlatar hitam pekat (selamat untuk mengelakkan *screen burn-in* pada TV OLED/LED), memaparkan jam besar 110sp, tarikh Hijriah (cth: `🌙 12 Jamadilawal 1448H`), dan solat seterusnya.
* **2 Jam Selepas Itu**: Jika TV terus tidak disentuh, sistem akan mematikan paparan TV (*sleep standby*) secara automatik.
* **Untuk Hidupkan Semula**: Tekan mana-mana butang pada remote untuk bangunkan TV.

---

## 🛠️ 5. Cara Pasang Manual (Tanpa Komputer / Guna Pendrive)

Jika anda tidak mahu menggunakan AI dan hanya mahu memasang fail APK secara manual:

### Kaedah A: Guna Pendrive / USB:
1. Muat turun fail `app-release.apk` (dalam folder `launcher/app/build/outputs/apk/release/`).
2. Salin fail APK tersebut ke dalam pendrive USB.
3. Cucuk pendrive pada Android TV atau Xiaomi Mi Box anda.
4. Buka aplikasi pengurus fail di TV (cth: *File Commander* atau *FX File Explorer* dari Google Play Store TV).
5. Klik pada `app-release.apk` dan tekan **Install**.
   *(Jika TV meminta izin "Install unknown apps", pilih **Allow/Benarkan**)*.
6. Selepas siap dipasang, tekan butang **Home** pada remote TV dan pilih **Glass TV Home** sebagai *Always / Sentiasa*.

### Kaedah B: Tanpa Wayar (Guna Telefon & Aplikasi 'Send Files to TV'):
1. Pasang aplikasi percuma **Send Files to TV** di telefon pintar dan di Android TV anda (dari Google Play Store).
2. Muat turun `app-release.apk` ke telefon anda.
3. Buka aplikasi pada kedua-dua peranti, hantar APK dari telefon ke TV melalui Wi-Fi.
4. Buka dan pasang fail APK di TV anda. Selesai!

---

## 📄 Lesen & Penghargaan
* Dibina berasaskan [CleanYourTV](https://github.com/Gathin23/cleanyourtv) © Gathin (Lesen MIT).
* Fon Outfit oleh SIL Open Font License.
* Data cuaca oleh Open-Meteo & data kualiti udara berasaskan piawaian APIMS Jabatan Alam Sekitar Malaysia.
