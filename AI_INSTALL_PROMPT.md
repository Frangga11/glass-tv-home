# 🤖 Glass TV v4.0 — Dedicated AI Agent Installation & Environment Audit Prompt

> Salin dan tampal (copy & paste) keseluruhan prompt di bawah ke mana-mana pembantu AI coding (**Google Antigravity**, **Claude Code**, **Cursor**, atau **Copilot**) yang disambungkan ke rangkaian Wi-Fi yang sama dengan Android TV anda.

---

```text
Saya mahu pasang Glass TV Home v4.0 pada Android TV rumah saya daripada repositori:
https://github.com/aqmangge/glass-tv-home

PANDUAN KHAS KEPADA AI:
Jangan pasang APK secara melulu tanpa mengkaji persekitaran TV terlebih dahulu. Sila ikuti 4 Fasa terperinci di bawah langkah demi langkah:

========================================================================
FASA 1: KAJI PERSEKITARAN TV SECARA MENDALAM (DEEP TV ENVIRONMENT AUDIT)
========================================================================
1. Minta IP TV daripada saya jika belum diketahui, dan sambung melalui ADB:
   adb connect <TV_IP>:5555

2. Siasat Versi Sistem Pengendalian & Model TV:
   - Jalankan: adb shell getprop ro.build.version.release (Versi Android: contoh Android 9, 10, 11, 12, atau 14)
   - Jalankan: adb shell getprop ro.build.version.sdk (API Level)
   - Jalankan: adb shell getprop ro.product.model & adb shell getprop ro.product.manufacturer
   * PERHATIAN: Jika peranti adalah Samsung Tizen atau LG webOS, maklumkan ia tidak disokong dan hentikan pemasangan.

3. Siasat Resolusi Paparan TV (1080p vs 4K UHD vs 720p) & Refresh Rate:
   - Jalankan: adb shell dumpsys display | grep -E "mBaseDisplayInfo|supportedModes|mode"
   - Jalankan: adb shell wm size (Resolusi UI canvas/framebuffer)
   - Jalankan: adb shell wm density (DPI ketumpatan skrin)
   * Laporkan kepada saya sama ada panel TV adalah 4K fizikal dengan framebuffer 1080p 60fps lancar.

4. Siasat IP Rangkaian Tempatan (LAN / Wi-Fi) TV:
   - Jalankan: adb shell ip -f inet addr show wlan0 (atau eth0)
   - Sahkan alamat IP berada dalam subnet Wi-Fi rumah yang sama untuk memastikan fungsi Muat Naik Wallpaper Telefon (QR HTTP Server) berfungsi tanpa sebarang sekatan firewall.

5. Kenal Pasti Pelancar Asal (Stock Launcher) Semasa:
   - Jalankan: adb shell cmd package resolve-activity -c android.intent.category.HOME -a android.intent.action.MAIN
   - Catat package name pelancar asal (contohnya: com.google.android.apps.tv.launcherx, com.google.android.tvlauncher, com.xiaomi.mitv.tvhome, dsb).

========================================================================
FASA 2: PENGESAHAN KEUPAYAAN SYSTEM SELF-ADAPTATION (GLASS TV v4.0)
========================================================================
Glass TV v4.0 dilengkapi dengan enjin penyesuaian kendiri (self-adapting) secara automatik:
- Resolusi & DPI: Menggunakan vektor dinamik dan dp-scaling yang menyesuaikan saiz UI secara automatik pada TV 720p, 1080p, dan 4K tanpa terpotong (zero clipping).
- Pengurusan Memori Wallpaper: Foto telefon beresolusi tinggi (48MP/8K) yang dimuat naik akan dikecilkan mengikut saiz paparan menggunakan inSampleSize dan RGB_565 supaya TV dengan RAM 1GB / 2GB tidak mengalami Out-Of-Memory.
- Dynamic Port Failover HTTP Server: Pelayan muat naik wallpaper QR cuba port 8080 -> 8888 -> 8081 -> dynamic OS port 0 jika port tertentu disekat oleh aplikasi lain pada TV.
- Universal Typography: Antara muka v4.0 menggunakan fon moden "Inter" sepenuhnya.

========================================================================
FASA 3: PEMASANGAN, SYSTEM DEBLOAT & KUNCI BUTANG HOME REMOTE
========================================================================
AMARAN: Jangan biarkan TV melayan Glass TV sebagai aplikasi biasa! Pengguna yang memasang APK biasa akan mendapati butang Home remote tetap kembali ke pelancar asal Android TV / Google TV.

1. Sandarkan (Backup) senarai pakej TV sebelum mengubah apa-apa:
   adb shell pm list packages -f > backup_packages.txt

2. Pasang APK versi 4.0:
   adb install -r release/glass-tv-home.apk
   (Atau bina menggunakan ./gradlew :app:assembleRelease jika repo diklon)

3. Kunci Glass TV Home sebagai Pelancar Utama Kekal (Bypass Google TV Home Hijack):
   - Jalankan arahan rasmi Android:
     adb shell cmd package set-home-activity com.gathin.tvhome/.MainActivity
   - Jika firmware pengeluar (seperti Xiaomi PatchWall atau Google TV LauncherX) masih cuba memintas butang Home, nyahdayakan pelancar asal:
     adb shell pm disable-user --user 0 <stock_launcher_pkg_yang_dikesan_dalam_Fasa_1>

4. Lakukan Debloat Iklan & Telemetri Pengeluar:
   - Nyahdayakan perkhidmatan pengiklanan dan penjejakan pihak ketiga untuk melegakan RAM TV sebanyak 300MB - 600MB.
   - PENTING: Jangan sentuh perkhidmatan asas seperti HDMI/Input TV, Bluetooth Remote, Wi-Fi, Audio, dan Google Play Store.

5. Konfigurasi Lokasi Pengguna (Jabatan Alam Sekitar JAS & Waktu Solat JAKIM):
   - Tanya saya lokasi di Malaysia (contoh: Putrajaya, Cheras, Shah Alam, Melaka, JB, Penang, Kuantan, Kuching, KK).
   - Hantar broadcast konfigurasi:
     adb shell am broadcast -a com.gathin.tvhome.SET_LOCATION -p com.gathin.tvhome --es query "<LOKASI>"

========================================================================
FASA 4: PELANCARAN & PENGESAHAN VISUAL (VERIFICATION)
========================================================================
1. Lancarkan Glass TV Home:
   adb shell am start -n com.gathin.tvhome/.MainActivity

2. Ambil tangkapan skrin (screenshot) dari TV dan periksa secara visual:
   adb shell screencap -p /sdcard/tv_verify.png && adb pull /sdcard/tv_verify.png ./tv_verify.png
   - Pastikan muka jam, tarikh, widget cuaca, dan ayat harian dipaparkan dengan fon Inter.
   - Pastikan butang profil pengguna di bar atas memaparkan ikon avatar haiwan bulat aktif.
   - Pastikan dock aplikasi tersusun rapi.

3. Uji butang Home remote:
   adb shell input keyevent KEYCODE_HOME
   - Sahkan skrin tetap berada pada Glass TV Home dan tidak kembali ke pelancar lama.

4. Berikan saya ringkasan status pemasangan dan kaedah pemulihan (restore command) jika diperlukan.
```
