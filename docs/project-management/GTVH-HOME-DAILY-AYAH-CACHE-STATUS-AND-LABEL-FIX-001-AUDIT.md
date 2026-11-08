# GTVH-HOME-DAILY-AYAH-CACHE-STATUS-AND-LABEL-FIX-001 — Audit & Implementation

**Date:** 9 November 2026  
**Status:** IMPLEMENTATION COMMITTED · CI PENDING · PHYSICAL ACCEPTANCE PENDING · NOT FINAL

## Source authority
- Repository: `Frangga11/glass-tv-home`
- PR: [#87](https://github.com/Frangga11/glass-tv-home/pull/87), OPEN / DRAFT / UNMERGED
- Branch: `fix/daily-ayah-cache-status-label-001`
- Exact current HEAD at audit: `4c9d049461b217ed20d134f4f430f2b812c383e7`
- Base: PR #86 branch `fix/prayer-times-refresh-next-subuh-001`
- Exact base HEAD: `746beaba2648b167dc174dfd3a61f5e859ed9fc2`
- Branch comparison: 10 commits ahead / 0 behind; exact base is the merge base.

## Verified findings
1. `WeeklyAyah.VERSES` is a rotating 52-verse Quran selection. The Home component does not contain a daily dua corpus, while Indonesian labels called it `DOA HARIAN • AYAT PILIHAN` and Settings advertised `Ayat & Doa Harian`.
2. `stored()` required English and Indonesian cache fields even when Malay translation was available. The translation method already used language fallbacks, so requiring every translation was stricter than the display capability and could cause unnecessary network refreshes.
3. On a failed network request, `last()` intentionally displayed the last retained verse, but the UI did not identify it as stale. The built-in local fallback was also visually indistinguishable from a current daily verse.
4. PR #85 added Arabic XML line-spacing values, but `MainActivity.showAyah()` subsequently called `ayahArabic.setLineSpacing(3dp, 1.08f)`, overriding the XML values at runtime. The requested compact spacing therefore was not guaranteed to render.

## Changes committed
- Rename the module and related Indonesian/English Settings, popup and summary copy to **Ayat Pilihan Harian / Daily Selected Verse**. No daily dua text is fabricated or implied.
- Accept a current cache when the selected day key matches, Arabic text is non-empty, and at least one translation is available. Optional language fields may be absent.
- Add `hasCached()` to distinguish a previous saved verse from the current daily verse.
- Reject incomplete Quran API payloads before replacing the existing valid cache.
- Expand translation fallback order so Indonesian and Malay users receive an available translation even if their preferred and secondary translation resources are absent.
- Display freshness state in the shelf label: current verse, `TERSIMPAN / SAVED COPY`, or `DATA LOKAL / LOCAL FALLBACK`.
- Remove the runtime `ayahArabic.setLineSpacing()` override so the XML typography settings from PR #85 control the view.
- Correct the WeeklyAyah class comment to use WIB, consistent with the existing day calculation.

## Regression coverage
- Python contracts verify optional translation cache validity, freshness labelling, truthful module naming, XML typography ownership, and rejection of incomplete API payloads.
- Android unit tests verify translation fallback to Malay/English for Indonesian, English and Malay language settings.
- Exact-head CI has not yet been confirmed for current HEAD `4c9d049461b217ed20d134f4f430f2b812c383e7`. Do not claim CI pass until the run and job logs are checked.

## Acceptance gates
1. Exact-head CI passes Python contracts, Android translation unit tests, Release unit tests/build and APK diagnostics.
2. Physical Mi TV Stick visual review confirms the Arabic XML line spacing is now actually applied; inspect one-line/two-line text and harakat on bright and dark wallpapers.
3. Simulate current-day cache, previous-day cache, empty cache, absent Indonesian translation, absent Malay translation, and offline refresh. Verify freshness labels accurately distinguish current, saved and local fallback states.
4. Confirm the Settings label and Home label consistently describe a selected Quran verse, not a daily dua corpus.

## Decision
**BELUM FINAL.** Code and regression tests are committed; exact-head CI and physical acceptance remain pending. Keep PR #87 OPEN / DRAFT / UNMERGED.
