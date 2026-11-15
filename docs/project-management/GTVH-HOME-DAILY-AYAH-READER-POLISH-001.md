# GTVH-HOME-DAILY-AYAH-SINGLE-FRAME-001

## Objective
Rebuild the existing Ayat Pilihan Harian reader so the complete Arabic verse and Indonesian translation are displayed in one fixed frame, with no vertical scrolling, and a maximum panel height of 40% of the screen.

## Owner-approved visual and layout contract
- Maximum reader height: 40% of the display height, measured from runtime display metrics.
- Popup width remains a centered 72% of display width, subject to the existing safe-content window bounds.
- Arabic verse: warm Islamic gold, right-aligned, Amiri Quran, black shadow, adaptive text sizing, at most two visible lines.
- Indonesian translation: centered, Inter, adaptive text sizing, at most two visible lines.
- Both text regions are weighted inside the same fixed body region; no ScrollView, vertical scrollbar, or scroll gesture/key behavior.
- Header, content, and footer occupy separate regions with fixed header/footer heights and a weighted content region.
- Header retains title, Ayah reference, and top-right close affordance.
- Footer retains a compact, accurate “Press OK to close” hint and bottom close action in one horizontal row; no scroll instruction remains.
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
