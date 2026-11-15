# GTVH-HOME-DAILY-AYAH-SINGLE-FRAME-001

## Objective
Rebuild the existing Ayat Pilihan Harian reader so the complete Arabic verse and Indonesian translation are displayed in one fixed frame, with no vertical scrolling, and a maximum panel height of 40% of the screen.

## Owner-approved visual and layout contract
- Maximum reader height: 40% of the display height, measured from runtime display metrics.
- Popup width is a centered 55% of display width, subject to the existing safe-content window bounds.
- Arabic verse: warm Islamic gold, right-aligned, Amiri Quran, black shadow, adaptive text sizing, at most three visible lines.
- Indonesian translation: centered, Inter, adaptive text sizing, at most two visible lines.
- Both text regions are weighted inside the same fixed body region; no ScrollView, vertical scrollbar, or scroll gesture/key behavior.
- Header, content, and footer occupy separate regions with fixed header/footer heights and a weighted content region.
- Header title and Surah/Ayah reference are centered at the top; one top-right X capsule is the sole close control.
- Footer retains only a compact informational “Press OK to close” hint; no second close button or scroll instruction remains.
- All text sizing adapts to the constrained layout before content can collide with header/footer.
- Glass treatment follows the Home Prayer popup standard: GlassDrawable + Backdrop watcher, consistent 20dp radius, neutral silver glass material and existing focus treatment. No bespoke gradient replacement.

## Implementation
- Replaced the previous 68%-height scrolling reader with a fixed 40%-height dialog.
- Removed ScrollView and all vertical-scroll DPAD handling.
- Applied Android uniform text auto-sizing to the Arabic and translation TextViews within two-line bounds.
- Set Arabic to TvUiTokens.ISLAMIC_GOLD and absolute RIGHT gravity; translation uses centered gravity.
- Preserved existing Ayah source, language selection, reference, close/back behavior, and origin-focus restoration.
- Kept the Home preview, capsule focus, Prayer timetable, Azan behavior, Dock, wallpaper, navigation, Wi-Fi and unrelated Home surfaces outside scope.

## Validation plan
- Focused Python source-contract tests cover the 40% cap, Prayer-popup glass material, gold/right Arabic, centered translation, no-scroll policy, separated header/body/footer, and close/focus restoration.
- Exact-head Android unit tests and release build must pass.
- Rendered 1920x1080 QA must inspect both one-line and two-line Arabic and translation cases for clipping, line count, overlap, and legibility.
- Physical Mi TV Stick remote/visual acceptance remains a separate gate; automated source checks are not device acceptance.

## Validation result and integration checkpoint
- PR #138 was squash-merged into the Ayah focus/preview branch on 2026-11-15 (merge commit `5e5ebf17ade4acd68f9dd8e33ad7528e7d90638d`).
- Exact-head CI run #2203 passed: launcher unit tests, `assembleRelease`, APK diagnostics, and repository documentation/list tests.
- QA APK artifact was generated from the same source tree (pre-squash head `a1d03e80de4c65a3b3d3c47db0f568ef905190ca`); APK size 1,636,445 bytes; SHA-256 `4c615da879a6596b48853681e1c2482ecc2e1984dedb90e38ee639f6d7adf781`.
- The merged changes are now part of branch `fix/home-ayah-pill-focus-align-001`, carried by PR #137.
- Final visual acceptance at 1920x1080 and physical Mi TV Stick remote-control verification remain pending. CI/build success does not establish on-device layout correctness.

## Status
Source implementation, localized no-scroll footer copy, regression contracts, documentation, CI, and QA APK are complete. PR #138 is merged into the Ayah work branch. PR #137 remains draft/unmerged against `fix/azan-test-overlay-compact-glass-001`; do not merge the broader Ayah focus/preview work to the mainline until its wider integration and physical-device QA gates are approved.


## Owner-approved proportional refinement — 2026-11-15
- Centered popup width: 55% of display width.
- Popup height: 42% of display height (within the approved 40–44% range).
- Arabic typography enlarged with compact, non-overlapping two-line leading.
- Indonesian translation reduced in size, compactly spaced, and allowed up to three lines in the same fixed frame.
- Header title and Surah/Ayah reference centered independently of the close affordance.
- Retain exactly one close control: the existing top-right X capsule. Footer is informational only.
- Preserve one-frame/no-scroll behavior, source data, navigation, DPAD entry path, and Prayer-popup glass material.


## Owner-directed TV feedback revision — 2026-11-15
- Keep the approved 55% width and 42% height proportions.
- Header is a fixed 48dp region with centered title and centered Surah/Ayah reference; both remain single-line with end ellipsis to prevent clipping into the close area. The top-right X control is removed.
- Footer is a fixed 48dp region containing the localized “Tekan OK untuk menutup popup” instruction and a single TUTUP capsule beneath it.
- Initial DPAD focus rests on the popup panel without highlighting a control. UP, LEFT, and RIGHT are consumed with no navigation. DOWN moves focus directly to TUTUP; OK activates TUTUP and dismisses the popup. Back/Escape remain supported.
- ARTINYA is centered between two horizontal divider rules.
- Arabic/translation body remains the flexible middle region; preserve Arabic maximum two lines and Indonesian translation maximum three lines. No other Home surfaces or data/navigation logic are in scope.


## Owner-approved premium visual refinement — 2026-11-15
- Preserve the existing 55% width, 42% height, fixed header/footer, single TUTUP control, no-scroll body, and DPAD contract.
- Add restrained cool-cyan radial ambience behind the Arabic verse while retaining the existing gold Amiri Quran typography and subtle text shadow.
- Give the Surah/Ayah reference a compact translucent silver-cyan chip; keep title and reference centered in the fixed header.
- Refine ARTINYA with warm-gold lettering, subtle gold horizontal rules, and shorter visual span.
- Refine TUTUP lettering and retain the shared Electric Cyan DPAD focus treatment; do not change the focus/activation behavior.
- Animate the complete popup surface as one unit with a restrained 200ms fade/scale entrance (100ms when reduced motion is enabled); never animate individual text independently.
- Do not add decorative clutter, alter source data, or modify other Home surfaces.
- Automated CI/build and physical TV visual/remote acceptance are required before considering this refinement complete.
