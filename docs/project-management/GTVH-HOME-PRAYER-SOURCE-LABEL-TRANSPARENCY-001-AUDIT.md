# GTVH-HOME-PRAYER-SOURCE-LABEL-TRANSPARENCY-001 — Audit & Implementation

**Date:** 9 November 2026  
**Status:** SOURCE CHANGE COMMITTED · EXACT-HEAD CI PENDING · PHYSICAL ACCEPTANCE PENDING · NOT FINAL

## Source authority
- Repository: `Frangga11/glass-tv-home`
- PR: [#88](https://github.com/Frangga11/glass-tv-home/pull/88), OPEN / DRAFT / UNMERGED
- Branch: `fix/prayer-source-label-transparency-001`
- Exact current HEAD: `a4b979540def5fc1414e66eec6df516a1ef22fe8`
- Base: PR #87 branch `fix/daily-ayah-cache-status-label-001`, exact base HEAD `91f1152361d9b4db70a74f8f6b30ad38f60b4211`

## Verified source findings
- Islamic Shelf header previously showed prayer header prefix, city and Hijri date only. It did not identify whether times came from the validated Kemenag timetable or the local astronomical fallback.
- The compact prayer summary already displayed `KEMENAG RI` or `PERKIRAAN` for the Pagar Alam zone, but the condition was limited to `ID_PGA`.
- `PrayerTimes.isUsingOfficialKemenagSchedule()` is the existing validation gate. This change reuses it rather than inventing a new definition of official data.

## Changes
- Add a localized prayer data-source label in `AppLanguage`.
- In `MainActivity.updateIslamicShelf()`, append the source label for Indonesian zones:
  - `KEMENAG RI` only if the annual JSON passes the existing Kemenag validator.
  - `PERKIRAAN LOKAL` / `LOCAL ESTIMATE` when the official annual timetable is not available/valid.
- Align the compact prayer summary condition to all Indonesian zones.
- Add Python source-contract tests for the visible header and localized distinction.

## Independent current-data cross-reference — 9 November 2026
- The official Bimas Islam Kemenag Jadwal Sholat page identifies the schedule as official SIHAT Kemenag RI data and displays November 2026 as the current period: https://bimasislam.kemenag.go.id/jadwal-sholat
- Sajadah's open-data documentation states its Indonesian annual JSON data is copied from Kemenag RI without rounding, interpolation or adjustment, and describes its validation rules: https://www.sajadah.org/id/data-terbuka
- A Pagar Alam November 2026 calendar page citing Bimas Islam lists 9 November as Subuh 04:24, Terbit 06:07, Dzuhur 11:54, Asar 15:14, Maghrib 18:07 and Isya 19:14: https://jadwal-sholat.tirto.id/kota-pagar-alam/2026-11
- Direct access to the exact app JSON URL was unavailable in the audit browser. Therefore these are independent cross-reference values, not proof that the current device cache contains the same JSON. Exact endpoint payload and on-device display still require verification.

## Acceptance gates
1. Exact-head CI passes Python and Android unit tests, Release build and APK diagnostics.
2. Physical Mi TV Stick review confirms the longer source-bearing header does not clip, overlap or shift the prayer table at 1920×1080 / density 320.
3. When a validated annual Kemenag dataset is present, both the Islamic Shelf header and compact prayer summary show Kemenag RI.
4. When the annual dataset is absent, damaged or invalid, both views show the local-estimate label. Never label fallback calculations as Kemenag data.
5. Confirm location selection and cache invalidation do not leave a stale official-source label.

## Decision
**BELUM FINAL.** Source label is implemented and regression contracts added. CI and physical visual/source-state acceptance remain pending. Keep PR #88 OPEN / DRAFT / UNMERGED.
