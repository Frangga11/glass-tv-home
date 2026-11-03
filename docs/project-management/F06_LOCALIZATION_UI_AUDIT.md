# Project F06 — Localization & Global UI Consistency Audit

**Checkpoint ID:** GTVH-F06-CP-2026-11-03  
**Date:** 3 November 2026  
**Decision:** HOST/SOURCE EXIT PASS  
**Integration PR:** #55 — Draft / Open / Unmerged  
**Integration branch:** `integration/project-f06-localization`  
**Exact head:** `487099013e6d9bd65cc07d72e760b1e14ab1f091`  
**Base:** `integration/project-f05-persistence-recovery` at `1048b0b68ae5ab5a2ad867183236b514e0c691fa`  
**Compare:** 29 commits ahead / 0 behind; nine changed paths.

## 1. Trigger and acceptance objective

F06 validates bilingual Indonesian/English application coverage and consistency with the global Android TV UI contract. The scope includes language selection and resource lookup, Home/launcher surfaces, settings and nested tabs, dialogs/popups, weather and schedule labels, and phone-based Avatar/Wallpaper/StandBy QR upload pages and their error states.

Acceptance is source/static and CI-based in F06. Physical display, remote navigation, phone/LAN and OEM behavior remain Project G acceptance gates.

## 2. Input and audit method

- Canonical source baseline: exact F05 integration head above.
- Supported language resources: `values`, `values-id`, `values-en`.
- Platform language path: `AppLanguage`, `LocaleContext`, manifest `localeConfig`, visible Activity contexts and StandBy DreamService.
- Global UI contracts: `TvDialogStyle` and `TvUiTokens`.
- QR/browser surfaces: `AvatarUploadServer` and `WallpaperUploadServer`.
- Existing localization parity test plus F06 regression guards.
- Cross-checks covered resource key parity, duplicate/empty entries, printf placeholders, known language inversions, hardcoded UI labels, modal call sites, locale context propagation, QR browser fallbacks, and gallery metadata.

## 3. Findings and repairs

| ID | Finding | Repair | Result |
|---|---|---|---|
| F06-L10N-001 | Auto-update schedule choices were Indonesian-only literals. | Moved all five visible labels into paired Indonesian/English resources. | FIXED |
| F06-L10N-002 | Weather high/low summary used hardcoded English abbreviations. | Replaced with a localized resource format string. | FIXED |
| F06-L10N-003 | Daily Ayah navigation hint and close label were assigned to the wrong locale; Indonesian Glass Effect title remained English. | Corrected Indonesian/English values and translated the Indonesian title. | FIXED |
| F06-UI-001 | Now Playing and card-prompt custom Dialog windows manually managed their own bounds/dimming rather than using the shared modal contract. | Routed both through `TvDialogStyle.applyCustom`; retained their content, hardware acceleration where applicable, and focus behavior. | FIXED |
| F06-QR-001 | Wallpaper/StandBy upload server response bodies and browser fallbacks contained English or Indonesian literals regardless of selected language. Avatar method-error body was also fixed. | Localized response bodies and JS fallbacks; reused canonical Avatar/Wallpaper error resources where meanings matched to avoid duplicate catalog growth. | FIXED |
| F06-QR-002 | Wallpaper gallery dates used the TV system default locale instead of the selected app language. | Date formatter now uses the selected app language tag. | FIXED |
| F06-QR-003 | Gallery thumbnail alternative text was a hardcoded English token. | Added a localized thumbnail accessibility label. | FIXED |
| F06-APK-001 | Initial full error-copy expansion left only 403 bytes below the APK size cap. | Consolidated equivalent messages onto existing canonical localized resources without removing user-visible error coverage. | FIXED; headroom restored |

## 4. Language and resource verification

| Check | Result |
|---|---:|
| Default resource keys | 615 |
| Indonesian resource keys | 615 |
| English resource keys | 615 |
| Duplicate keys | 0 |
| Key-set mismatches | 0 |
| Format-placeholder mismatches | 0 |
| Empty string values | 0 |
| Advertised app locales | `id`, `en` only |

- Android 13+ per-app language selection is backed by `localeConfig` and `LocaleManager`.
- Android 8–12 resource selection uses `LocaleContext.wrap`.
- Every visible manifest Activity uses the locale context; the `HomeSettingsActivity` NoDisplay trampoline is intentionally excluded. StandBy `ClockDream` also wraps its base context.
- Browser upload pages expose the selected app language in their HTML `lang` attribute and use locale-backed strings for visible errors, progress, cancellation and accessibility labels.
- Gallery date formatting now follows `AppLanguage.getLanguage(context)`.
- Identical Indonesian/English values that remain are proper product/technical names or universal tokens (Home, Wi-Fi, Ethernet, VPN, Maghrib, Google Assistant, Spotify, OK and Android version formatting).

## 5. Global UI consistency

- MainActivity standard AlertDialogs delegate through `styleGlassDialog` to `TvDialogStyle.apply`.
- The two custom MainActivity Dialog surfaces now delegate to `TvDialogStyle.applyCustom`.
- Settings navigation/cards continue using shared `TvUiTokens`; Settings modal paths use the shared dialog contract.
- Weather, profile switching, child-mode security and update dialogs were source-checked to use `TvDialogStyle.apply` or `applyCustom`.
- Anchored Settings/quick popups retain their own established presentation contract; they are not incorrectly forced into modal-window sizing.
- Phone browser upload pages remain responsive web surfaces; their text, error states, date locale and accessibility copy are localized rather than applying Android TV modal geometry to a different platform.

## 6. Exact-head CI and APK evidence

**Workflow run:** #41566633872  
**Exact head:** `487099013e6d9bd65cc07d72e760b1e14ab1f091`

| Gate | Result |
|---|---|
| Lists, tools and docs | SUCCESS |
| Python suite | 89 tests; 1 intentional README-count skip |
| Android `testReleaseUnitTest` | SUCCESS |
| Android `assembleRelease` | SUCCESS |
| APK diagnostics | SUCCESS |
| APK size enforcement | SUCCESS |
| Workflow artifacts | Empty; no APK publication |

**APK size:** 1,569,397 bytes  
**Budget:** 1,572,864 bytes  
**Headroom:** 3,467 bytes

The APK-size optimization was re-run on the final exact head. The earlier 403-byte headroom measurement belongs to the pre-consolidation intermediate head and is superseded by the final 3,467-byte headroom.

## 7. Governance and deferred acceptance

- PR #55 remains Draft / Open / Unmerged under D-013.
- No merge to the F05 base, no merge to `integration/b-series`, no APK publication or installation, and no device-setting changes.
- No physical TV or phone test is claimed.
- Project G must verify on target Android TV hardware: app-language switching on Android 11 and Android 13+, rendered Home/settings/subtab/popup text, D-pad focus and modal bounds, QR upload over real phone/LAN, and browser behavior on actual mobile browsers.
- No user data or preference schema migration was introduced.

## 8. Decision and handover

**Decision: F06 HOST/SOURCE EXIT PASS.** The bilingual resource structure, audited dynamic copy, locale-context source path, global modal integration, QR/browser language path, regression guards, Android unit tests, release build and APK size gate all pass on the exact head.

Next host/source work unit: **Project F07**, following the approved roadmap. Keep PR #55 Draft/open/unmerged until the grouped Project G final audit and explicit merge approval. Preserve the physical acceptance gates listed above.

## 9. Changed paths

1. `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java`
2. `launcher/app/src/main/java/com/gathin/tvhome/WeatherActivity.java`
3. `launcher/app/src/main/java/com/gathin/tvhome/AvatarUploadServer.java`
4. `launcher/app/src/main/java/com/gathin/tvhome/WallpaperUploadServer.java`
5. `launcher/app/src/main/res/values/strings.xml`
6. `launcher/app/src/main/res/values-id/strings.xml`
7. `launcher/app/src/main/res/values-en/strings.xml`
8. `tests/test_f06_localization_ui_contract.py`
9. `tests/test_main_activity_modal_contract.py`
