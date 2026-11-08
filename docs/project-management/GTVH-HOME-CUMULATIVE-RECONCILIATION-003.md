# GTVH-HOME-CUMULATIVE-RECONCILIATION-003 — Audit Silang PR #84–#100 dan Koreksi Arabic RTL

**Tanggal:** 9 November 2026  
**Repository:** Frangga11/glass-tv-home  
**Status:** AUDIT MENEMUKAN KETIDAKLENGKAPAN INTEGRASI — BELUM FINAL

## Ringkasan temuan

PR #100 (2de055a3b08fa9a0e5c4d2bb86d2884dbd71f43b) hanya dibangun dari PR #89 (4038c08f4825f7bacf965bc6708505d3c8c30ebc) ditambah dua commit geometry/capsule. PR #100 belum memuat rangkaian perubahan setelah PR #89 yang berada pada jalur PR #91 → #92 → #93.

Ini diverifikasi melalui metadata PR, perbandingan commit terhadap base #89, daftar file berubah, dan patch aktual. Karena itu APK PR #100 sebelumnya tidak boleh disebut sebagai build kumulatif lengkap untuk seluruh pekerjaan Islamic Shelf/Arabic yang lebih baru.

## Rantai perubahan yang wajib dipertahankan

| Rangkaian | Branch / HEAD | Perubahan wajib | Status terhadap PR #100 |
|---|---|---|---|
| PR #84–#89 | final #89 HEAD 4038c08f4825f7bacf965bc6708505d3c8c30ebc | Single anchor Islamic Shelf, Arabic typography, refresh jadwal/next Subuh, cache dan freshness Ayat Pilihan, prayer source label, offline retry | SUDAH TERMASUK |
| PR #91 | fix/home-islamic-shelf-arabic-right-align-001, HEAD ee8bfb34db8c00a801ac260428f52ead1c7c6662 | Arab RTL + Gravity.END di Shelf dan reader; terjemahan LTR + Gravity.START; XML dan regression test | BELUM TERMASUK |
| PR #92 | fix/home-islamic-shelf-ayah-spacing-and-prayer-label-001; final test commit 59d63a3c9854b646876aade98e26fc74a39d3140 | Jarak Arab 1dp top, -2dp/0.92; Arab-terjemahan 0dp; terjemahan 0dp/1.04; hint 3dp; font padding tetap; hapus label sumber dari header dan ringkasan jadwal tanpa mengubah kalkulasi/fallback | BELUM TERMASUK |
| PR #93 | fix/home-islamic-shelf-arabic-right-align-corrective-001, HEAD 5b46ef0cea39fa439316e48bf85a3cbbb0944820 | Lebar line-box runtime yang sama untuk preview Arab dan terjemahan agar batas kanan RTL stabil; regression test | BELUM TERMASUK |

## Bukti teknis

1. Compare PR #100 exact HEAD terhadap base #89 menunjukkan 2 commit ahead dan 11 file berubah; file produksinya hanya DockGeometry.java, MainActivity.java, dan activity_main.xml, beserta test geometry/layout dan CI.
2. Compare PR #91 terhadap base #89 menunjukkan 3 commit dan perubahan MainActivity.java, activity_main.xml, serta tests/test_daily_ayah_cache_and_label_contract.py.
3. Compare PR #92 terhadap base #89 menunjukkan 11 commit dan perubahan tambahan pada AppLanguage.java, PrayerTimes.java, MainActivity.java, activity_main.xml, tiga test, serta CI.
4. PR #93 secara eksplisit stacked di atas PR #92. Patch PR #93 menambahkan shared fixed runtime line-box pada ayahArabic dan ayahText di MainActivity.java, plus kontrak regression.
5. PR #100 tidak mengandung perubahan AppLanguage.java, PrayerTimes.java, atau test kontrak PR #92/#93. Patch PR #100 tidak memuat perubahan RTL/line-box PR #91/#93.
6. Ada overlap nyata di MainActivity.java, activity_main.xml, dan .github/workflows/ci.yml; penggabungan buta/cherry-pick tanpa rekonsiliasi berisiko menimpa jarak shelf, posisi tray, atau allowlist artifact.

## Spesifikasi yang wajib tetap ada setelah integrasi

- Arab Home Shelf dan reader: TEXT_DIRECTION_RTL + Gravity.END.
- Terjemahan Indonesia: TEXT_DIRECTION_LTR + Gravity.START.
- Preview Arab dan terjemahan memakai lebar line-box runtime yang sama (islamicMaxWidth) sehingga sisi kanan punya batas ukur yang stabil.
- Arabic typography: top margin 1dp, line spacing -2dp / 0.92, includeFontPadding=true, max 2 baris; font, warna, ukuran, dan shadow dipertahankan.
- Terjemahan: top margin 0dp, line spacing 0dp / 1.04; hint 3dp.
- Prayer header kembali ke lokasi + tanggal Hijriah; ringkasan jadwal tidak menampilkan PERKIRAAN LOKAL / PERKIRAAN / KEMENAG RI atau padanan bahasa lain.
- Penghapusan label hanya UI; kalkulasi waktu salat, validasi jadwal resmi, sumber data, dan fallback lokal tidak boleh diubah.
- Semua perubahan PR #84–#89 tetap utuh.
- Semua geometry/capsule PR #100 tetap utuh: safe frame kiri/kanan/bawah 30dp, atas 29dp; kapsul target 30dp kecuali Inputs; gap ikon-label Home/Profile 2dp dan Settings 4dp; tepi bawah shelf wallpaper/jadwal 30dp dari tray terukur.

## Status validasi

- PR #93 melaporkan CI #1656 / run 42529068497 SUCCESS pada HEAD 5b46ef0cea39fa439316e48bf85a3cbbb0944820; APK artifact #13636433567. Ini memvalidasi jalur PR #93 pada ancestry #92, bukan kandidat kumulatif dengan geometry PR #100.
- PR #100 CI #1677 / run 42533185178 PASS (208 Python tests, 1 skipped; Android unit tests dan release build PASS), tetapi hanya untuk source ancestry PR #84–#89 + geometry PR #100.
- Rendered visual 1920×1080 dan acceptance fisik Mi TV Stick masih pending.
- Tidak ada ADB, instalasi TV, perubahan launcher default, atau merge yang dilakukan.

## Keputusan integrasi / tindakan wajib

1. Jangan menyatakan PR #100 sebagai latest complete candidate; tandai sebagai kandidat kumulatif parsial.
2. Jadikan PR #93 sebagai sumber lengkap untuk PR #91–#93 di atas PR #89, lalu rekonsiliasikan geometry PR #100 di atasnya dengan mempertahankan kedua kelompok perubahan.
3. Buat satu kandidat integrasi baru yang ancestry-nya mencakup PR #84–#89 + #91–#93 + geometry #100; jangan menghapus atau menutup PR sumber.
4. Jalankan seluruh Python regression, Android unit tests, release build, audit diff, CI allowlist, APK size/hash dan pemeriksaan artifact pada exact HEAD kandidat baru.
5. Lakukan rendered 1920×1080 visual audit sebelum instalasi/ADB. Setelah lolos baru lanjutkan acceptance fisik.
6. PR tetap Draft/unmerged dan tidak ada merge tanpa persetujuan eksplisit pemilik.

**Kesimpulan:** pekerjaan sebelumnya tidak boleh dibuang. Pada audit ini ditemukan PR #91–#93 belum terintegrasi ke PR #100. Status implementasi keseluruhan tetap BELUM FINAL sampai kandidat integrasi baru dibangun dan diverifikasi.