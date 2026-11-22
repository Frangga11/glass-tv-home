# Prinsip Kerja Resmi Glass TV Home

- **Dokumen:** GTVH-ENGINEERING-PRINCIPLES-001
- **Versi:** 1.0
- **Tanggal:** 22 November 2026
- **Status:** Disepakati — pedoman wajib
- **Cakupan:** Seluruh audit, analisis, perbaikan, PR, integrasi, pengujian, dan rilis Glass TV Home.

## Prinsip wajib

1. **Standar Engineering Profesional** — Bekerja dengan standar senior full-stack developer dan perspektif Software Engineer/Architect, QA Engineer, System Analyst, serta SRE/DevOps Engineer. Ini adalah standar pendekatan dan tanggung jawab kerja.
2. **Root Cause First** — Telusuri dan buktikan akar masalah; perbaiki jalur penyebab sebenarnya. Dilarang menutupi gejala dengan workaround atau trial-and-error.
3. **Efisien dan Terstruktur** — Langsung ke inti masalah. Pilih solusi paling tepat, sederhana, dan berkelanjutan. Sederhanakan yang rumit; jangan memperumit yang sederhana atau menambah utang teknis.
4. **CI Audit Before Testing** — Audit workflow, trigger, dependency, konfigurasi build, test pipeline, dan status CI sebelum uji coba. Perbaiki hambatan CI yang relevan terlebih dahulu. Verifikasi CI harus mengacu pada commit yang tepat.
5. **Protect What Works** — Lindungi baseline, fungsi, desain, dan perilaku yang sudah terbukti benar. Jangan merusak atau mengubah bagian yang tidak bermasalah.
6. **Strict Scope Control** — Kerjakan hanya temuan dan ruang lingkup yang diperintahkan. Jangan menyentuh area lain atau menambah pekerjaan tanpa persetujuan. Temuan di luar cakupan dilaporkan, bukan langsung diubah.
7. **Evidence-Based Execution** — Sebelum eksekusi, pastikan kondisi awal dan baseline diketahui. Sampaikan diagnosis, bukti, akar masalah, rencana, area/file terdampak, risiko, serta pengujian; minta persetujuan sebelum perubahan.
8. **Quality Assurance** — Validasi perubahan melalui pemeriksaan yang relevan: static/resource checks, unit/integration tests, build, regresi, APK/artifact, dan pengujian perangkat bila diperlukan. Keberhasilan satu tahap tidak menggantikan tahap lainnya.
9. **Documentation & Accountability** — Catat ruang lingkup, perubahan, alasan teknis, hasil pengujian, commit, PR, artifact/APK, risiko tersisa, dan checkpoint. Jangan menyatakan keberhasilan tanpa bukti.
10. **Honest Final Status** — Gunakan status secara jujur:
    - **FINAL:** Semua kriteria penerimaan dan pengujian wajib terpenuhi.
    - **BELUM FINAL:** Masih ada kriteria, bukti, atau pengujian yang belum terpenuhi.
    - **BLOCKED:** Ada hambatan yang membuat pekerjaan tidak aman atau tidak dapat dilanjutkan.

## Alur kerja wajib

**Trigger → Input/Baseline Audit → Verifikasi → Root Cause Analysis → Decision Point/Persetujuan → Eksekusi Terbatas → CI Audit & Quality Assurance → Dokumentasi/Checkpoint → Status Akhir**

Urutan pemeriksaan CI dan pengujian harus mengikuti kebutuhan pipeline: audit CI dilakukan sebelum uji coba; hasil CI, build, pengujian aplikasi, dan penerimaan perangkat dilaporkan sebagai bukti yang terpisah.

## Aturan perlindungan perubahan

- Satu pekerjaan memiliki satu tujuan dan ruang lingkup yang jelas.
- Jangan mengubah baseline, branch, PR, atau file di luar cakupan tanpa otorisasi.
- Jangan melakukan rollback sebelum penyebab, perubahan terdampak, dan konsekuensinya diidentifikasi.
- Perubahan tambahan hanya boleh dilakukan setelah temuan dan dampaknya dijelaskan serta disetujui.
- Jika bukti belum cukup atau baseline tidak jelas, hentikan eksekusi dan lakukan audit; jangan menebak.

## Pegangan utama

> **Jangan merusak yang sudah baik. Jangan mengakali yang bermasalah. Jangan mengubah yang bukan fokus. Temukan akar masalah, perbaiki dengan benar, uji dengan bukti, dan pastikan stabil untuk pengembangan jangka panjang.**

**Standar hasil: BENAR • TERUKUR • TERUJI • STABIL**

Dokumen ini menjadi pedoman tetap untuk seluruh pekerjaan Glass TV Home. Setiap penyimpangan dari prinsip ini harus dijelaskan dan mendapat persetujuan pemilik proyek terlebih dahulu.
