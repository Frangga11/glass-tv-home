# GTVH-HOME-ISLAMIC-SHELF-VERTICAL-POSITION-AND-GRID-ROW-SCROLL-FIX-002 — Audit

**Date:** 9 November 2026  
**Status:** SOURCE PASS · EXACT-HEAD CI PASS · RELEASE APK PUBLISHED · PHYSICAL DEVICE ACCEPTANCE PENDING · NOT FINAL  
**Repository:** `Frangga11/glass-tv-home`  
**PR:** [#82](https://github.com/Frangga11/glass-tv-home/pull/82)  
**Branch:** `fix/home-islamic-shelf-vertical-position-grid-row-scroll-002`  
**Exact HEAD:** `d6f594933b712b36b0660e119a0d4ee8b7de3134`  
**Base branch:** `fix/home-dock-islamic-shelf-navigation-001` (PR #81; current branch tip `3cd36b8a5fbc788a7b15e178e4aaf0606e919c61`)

---

## 1. Trigger and baseline

The user reported that DPAD DOWN did not produce the intended vertical content movement: the previous row appeared to remain in place while the next row appeared from the wrong direction. The prior PR #81 smooth-scroll implementation therefore could not be treated as physically accepted.

Before implementation, the canonical checkpoint, handover, audit register, PR #80/#81 status, source at PR #81 HEAD, layout XML, DockDecoration geometry, AppAdapter, DockGeometry, PrayerTimes and AppLanguage were inspected.

### PR and branch state

- PR #80: OPEN / DRAFT / UNMERGED; HEAD `a085be150ebcf1d48b825ed7ac43b172261f3aa0`.
- PR #81: OPEN / DRAFT / UNMERGED; branch `fix/home-dock-islamic-shelf-navigation-001`; current HEAD `3cd36b8a5fbc788a7b15e178e4aaf0606e919c61`.
- PR #81 remains based on PR #80. Its application source was not changed for this follow-up; only CI and direct-APK workflow eligibility was extended so PR #82 can produce exact-head validation and a direct APK.
- PR #82: OPEN / DRAFT / UNMERGED; mergeable; branch above; exact HEAD above; stacked on PR #81.
- PR #82 is synchronized with current PR #81 base (0 commits behind; mergeable). No PR was merged and no source from PR #80/#81 was reverted.

The repository did not contain new physical-device evidence in PR #81 comments or its audit record; the user-provided Mi TV Stick observation is the reproduction baseline. Physical acceptance remains a separate gate.

---

## 2. Root-cause findings

### A — Islamic information package position

The previous horizontal anchor was reconstructed from virtual Dock card geometry and fixed offsets. It did not measure the actual drawn Glass tray top and the actual lower visual edge of the complete information package in one coordinate space. Consequently, the source could not guarantee a runtime visual clearance of exactly 27.5dp.

### B — Vertical Grid row scrolling

The previous transition used signed `smoothScrollBy()`, but calculated distance from raw `View.getHeight()` and `View.getTop()`, not decorated RecyclerView bounds. The expanded one-row viewport also included top padding while `clipToPadding=false`, allowing content to draw through the padding strip. The settle logic could therefore observe a partial previous row rather than a strictly aligned one-row viewport. Existing source-contract tests did not prove actual content displacement or final row alignment.

### C — Typography

The two headings and six prayer labels used weight 600. The prayer labels were populated from localized dynamic prayer-name data, so the rendered capitalization followed the source strings rather than the required all-capital presentation.

---

## 3. Implementation

### A — Runtime-measured 27.5dp clearance

- Added `horizontalDockTrayTopInRoot()`, which measures the minimum laid-out Dock card top in root coordinates and subtracts the same tray inset used by `DockDecoration`.
- Added `DockGeometry.islamicShelfDockGapPx(density)`; at Mi TV Stick density 320 (density 2.0), the requested 27.5dp is 55px.
- Anchored the Islamic shelf lower edge at `trayTop - gapPx`.
- Kept the existing 10dp vertical relationship between the wallpaper title/description shelf and Islamic shelf.
- Added layout-change handling so the two parent shelves preserve their bottom anchors when content remeasures.
- Dock size, Dock position, safe frame, header, left/right/bottom margins and internal panel content were not changed.

The runtime geometry now targets the exact requested gap. Actual device measurement remains pending.

### B — Deterministic real content movement

- Replaced the row transition's `smoothScrollBy()` call with a cancellable `ValueAnimator` that applies incremental `RecyclerView.scrollBy()` calls. This moves the actual content, not only focus or adapter indices.
- Measures row pitch and current offset with `GridLayoutManager.getDecoratedBoundsWithMargins()`.
- Enables `clipToPadding=true` only while the horizontal overflow Grid is expanded, so the padded viewport displays one row without exposing the previous row through its top inset.
- Measures the destination row's decorated top and settles it against `grid.getPaddingTop()` before requesting focus.
- Uses transition and focus generations to invalidate stale animations and callbacks.
- Captures the target app key and resolves it against the current adapter before focus; clamps to a valid adapter position only if the app disappeared.
- Focus settlement has no `scrollToPositionWithOffset()` jump fallback. The existing initial Grid reveal/restore alignment helper remains unchanged.
- Retains the incomplete-last-row clamp and lifecycle cancellation.

DOWN uses positive vertical content scroll so the current row exits upward and the next row enters from below. UP uses the inverse signed displacement.

### C — Typography

- `prayerHeader`: Inter variable weight 700.
- `ayahLabel`: Inter variable weight 700.
- Six localized prayer-name labels: `textAllCaps=true`, Inter variable weight 650.
- Existing 13sp prayer-name size, 17sp time size, font family, colors, positions, columns, prayer times/data, Arabic, RTL and translations remain unchanged.

Indonesian locale resolves the six requested names as SUBUH, TERBIT, DZUHUR, ASHAR, MAGHRIB and ISYA. Other app locales retain their localized words and display them in uppercase.

---

## 4. Changed files in PR #82

1. `launcher/app/src/main/java/com/gathin/tvhome/DockGeometry.java`
2. `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java`
3. `launcher/app/src/main/res/layout/activity_main.xml`
4. `launcher/app/src/test/java/com/gathin/tvhome/DockLayoutModelTest.java`
5. `tests/test_home_dock_islamic_shelf_navigation_refinement_contract.py`

CI and direct APK workflow support is inherited from the updated PR #81 base branch; these workflow files are not part of PR #82's five-file diff.

No unrelated application modules, DockAdapter, DockDecoration, wallpaper provider, prayer-time calculation/data, profile, kids mode, header, capsules, or global safe-frame code were changed.

---

## 5. Exact-head validation

**Final exact HEAD:** `d6f594933b712b36b0660e119a0d4ee8b7de3134`

### CI #1630 — run `42505427017`

**SUCCESS — both jobs passed.**

- Python: **177 tests, OK (1 skipped)**.
- Scoped contracts passed for measured 27.5dp anchoring, stable bottom anchors, decorated row geometry, real `scrollBy()` motion, focus identity, no-jump focus settlement, viewport clipping and typography.
- Android `testReleaseUnitTest`: app and installer tasks passed.
- Android `assembleRelease`: passed.
- Gradle result: **BUILD SUCCESSFUL in 1m 37s**.
- CI visual-parity artifact ID: `13627586269`; ZIP size 1,429,683 bytes; SHA-256 `3b081cde3d8fcfd5da101d5300a772bf2e6ca2bbc0371606e39e804603c62584`. This is a CI artifact ZIP, not the direct APK asset.

### Direct APK publisher #85 — run `42505426954`

**SUCCESS.** It built and verified the release APK from the exact HEAD above, then published the APK binary itself as a direct GitHub Release asset.

- APK: `Glass-TV-Home-PR82-d6f594933b712b36b0660e119a0d4ee8b7de3134.apk`
- Size: **1,630,737 bytes**
- CI emitted the historical 1.5 MiB APK-size reference warning; it is explicitly non-blocking. The APK is 1,268 bytes larger than PR #81's 1,629,469-byte APK; continue monitoring the size delta.
- SHA-256: `524963af6aad72444d38e1cd896eac25d29be05cac9e765b8819e0f69dcba735`
- Release: [Glass TV Home — PR #82 QA APK](https://github.com/Frangga11/glass-tv-home/releases/tag/glass-tv-home-pr82-d6f594933b712b36b0660e119a0d4ee8b7de3134)
- Direct APK: [Download exact-head APK](https://github.com/Frangga11/glass-tv-home/releases/download/glass-tv-home-pr82-d6f594933b712b36b0660e119a0d4ee8b7de3134/Glass-TV-Home-PR82-d6f594933b712b36b0660e119a0d4ee8b7de3134.apk)

A first build attempt failed because the pending stable-key field declaration was missing. The compiler log identified the missing declaration; it was added, a regression assertion was added, and the final exact-head CI and publisher runs both passed. Do not carry the failed attempt forward as the final result.

---

## 6. Physical acceptance — still required

No assistant-side ADB session, installation, device setting change, screenshot capture or physical test was performed. CI and APK validation do not prove the visual movement on Mi TV Stick.

Owner/device checklist:

1. Install only the exact-head PR #82 APK above on Mi TV Stick (1920×1080, density 320).
2. Measure the visual gap from the lowest visible edge of the four-part information package to the top outer edge of the Glass Dock: target **27.5dp / 55px**.
3. Change Dock focus repeatedly and confirm the information package does not jump.
4. Test Dock capacities 4, 5 and 6; confirm the Dock itself remains unchanged.
5. Test DOWN once, twice and repeatedly: each current row must move upward and the next row must enter from below into the same viewport position.
6. Test UP once, twice and repeatedly: the reverse motion must return the previous row to its exact original position.
7. Test DOWN→UP and UP→DOWN rapidly; no stale animation may override the latest destination.
8. Test the last app in a row, incomplete final row and app-count changes.
9. Open another app and return Home; verify valid focus restoration.
10. Inspect all six prayer labels, both headings, clipping, overlap, Arabic/RTL and all locked components.

Capture a short remote-navigation video and a 1920×1080 screenshot if any discrepancy appears. Record observed pixel gap and whether the row's old/new positions align.

---

## 7. Final decision

**SOURCE PASS · CI PASS · APK PUBLISHED · PHYSICAL ACCEPTANCE PENDING · NOT FINAL**

Keep PR #82 OPEN / DRAFT / UNMERGED. Do not merge PR #80, #81 or #82 as part of this task. Do not declare the requested behavior accepted until the physical Mi TV Stick checklist is completed and reviewed.
