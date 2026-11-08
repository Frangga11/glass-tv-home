# GTVH-HOME-HEADER-REFINEMENT-001 — Implementation Audit

**Checkpoint:** 8 November 2026  
**Source repository:** Frangga11/glass-tv-home  
**Source branch:** `fix/home-balanced-frame-001`  
**PR:** #79 — OPEN / DRAFT / UNMERGED  
**Source HEAD:** `354b855b65d95db688cf9a43af5b74e4a14a229b`  
**Stacked base:** PR #77, `fix/home-layout-consolidated-001` — OPEN / DRAFT / UNMERGED  
**Owner decision:** uniform 29dp safe frame on all four edges.

## 1. Decision and geometry

The owner superseded the prior 34dp baseline with a uniform 29dp target on the top, left, right and bottom edges.

At the Mi TV Stick logical density recorded in the project (320dpi; 2 physical pixels per dp):

| Edge | Previous | New target/source value | Pixel equivalent |
|---|---:|---:|---:|
| Top | 34dp | 29dp | 58px |
| Left | 34dp | 29dp | 58px |
| Right | 34dp | 29dp | 58px |
| Bottom | 34dp | 29dp | 58px |

Source implementation:
- Top/left/right: `mainContent` padding in `activity_main.xml`.
- Bottom: `DockGeometry.SAFE_VERTICAL_MARGIN_DP`, consumed by `safeBottomInsetPx()` and the existing Dock bottom-margin calculation.
- Horizontal safe margin: `DockGeometry.SAFE_HORIZONTAL_MARGIN_DP`, also used by Home shelves.

This changes only safe-frame placement. Dock item dimensions, Dock/Grid slot calculations, row count, and focus/overflow behavior are not intentionally changed.

## 2. Header changes

| Property | Before | After |
|---|---:|---:|
| Clock | 56sp | 44sp |
| AM/PM | 13sp | 11sp |
| Gregorian date | 13sp | 11sp |
| Shamsi date | 13sp | 11sp |
| Shared navigation capsule runtime radius | XML selector 18dp; tabs individually received GlassDrawable 21dp | 20dp single outer GlassDrawable |
| Navigation segment focus/active radius | 14dp selector plus individual glass background | 14dp selector on transparent independent tab |
| Other Header glass pills | 21dp runtime GlassDrawable | 21dp retained |

The navigation order and IDs remain Home/Profile/Settings (localized text remains governed by existing localization code). The parent `navPill` owns one glass surface. The three children remain separate focusable/clickable controls, with the existing foreground selector supplying focused/selected segment feedback. Individual tab scaling was removed so focused segments do not distort the capsule's measured geometry.

Time format attributes and `updateDateAndPeriod()` logic were not changed. No Weather/AQI, location/altitude, Wi-Fi, microphone, or translation logic was intentionally changed.

## 3. Files changed on PR #79 branch

1. `launcher/app/src/main/res/layout/activity_main.xml`
   - top/start/end content inset 34dp → 29dp;
   - clock 56sp → 44sp;
   - AM/PM, date, Shamsi date 13sp → 11sp.
2. `launcher/app/src/main/java/com/gathin/tvhome/DockGeometry.java`
   - horizontal and vertical safe margin constants 34dp → 29dp.
3. `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java`
   - one shared glass surface assigned to `navPill`;
   - tab backgrounds made transparent while retaining their foreground state selector and independent focus/click handlers;
   - per-tab focus scaling removed.
4. `launcher/app/src/main/res/drawable/header_glass_pill_bg.xml`
   - capsule fallback radius aligned to 20dp.
5. `launcher/app/src/test/java/com/gathin/tvhome/DockGeometryTest.java`
   - safe frame assertions updated to 29dp / 58px at density 2.
6. `tests/test_home_layout_bounds_contract.py`
   - safe-frame expectation updated;
   - compact clock/date and shared capsule structural regression tests added.

No files in Dock adapter, Grid adapter, wallpaper provider, Weather/AQI provider, location data provider, Wi-Fi logic, microphone logic, or localization resources were changed by this refinement.

## 4. Quality gates

| Check | Status | Evidence / limitation |
|---|---|---|
| Source branch and PR state reverified | PASS | PR #79 open, draft, unmerged; HEAD recorded above. |
| 29dp source values | PASS | XML and DockGeometry source constants. |
| 58px conversion at density 2 | PASS | Updated unit-test assertions. |
| Clock/date size source values | PASS | XML element attributes verified; time/date logic unchanged. |
| Unified capsule structure | PASS (static) | One parent GlassDrawable; tab surfaces transparent; selectors retained. |
| DPAD focus behavior | PARTIAL | Existing individual focus and key handlers retained; no runtime TV test. |
| Four-edge measured screenshot comparison | PENDING | No rendered before/after screenshot captured in this static GitHub audit. |
| Build / unit tests at updated HEAD | PENDING | CI #1559 / run 42484088913 was in progress at checkpoint capture. |
| Physical Mi TV Stick acceptance | PENDING | Explicitly deferred by owner. |
| APK install / ADB / launcher setting changes | NOT PERFORMED | Prohibited by owner. |
| PR merge | NOT PERFORMED | PR #79 and #77 remain unmerged. |

## 5. Protected components and regression boundary

The following were not directly edited:
- DockAdapter / Dock item geometry beyond the safe-bottom source value;
- App Grid adapter and column/slot algorithms;
- wallpaper and background flow;
- Weather/AQI data provider and rendering logic;
- location and altitude data;
- Wi-Fi and microphone internal functions;
- localization resources;
- profile/settings action handlers;
- global DPAD routing and focus restoration code.

The Dock's bottom placement necessarily follows the newly approved 29dp safe frame; its dimensions and behavior remain unchanged.

## 6. Remaining risks

1. Runtime rendering must confirm that the parent capsule glass and transparent tab foreground selectors layer correctly on Android TV.
2. DPAD traversal and focus feedback need runtime verification on a TV remote.
3. 44sp clock readability and 11sp date readability need viewing-distance acceptance.
4. The measured visual edge distances and corner clipping must be captured from a rendered 1920×1080 output; source values alone do not prove final rendered bounds.
5. CI #1559 must finish successfully before this APK can be considered for owner review.

## 7. Final status

**IMPLEMENTATION: PARTIAL / STATIC SOURCE CHANGES COMMITTED.**  
**BUILD: PENDING.**  
**VISUAL AND DEVICE ACCEPTANCE: PENDING.**  
**MERGE: PROHIBITED UNTIL OWNER ACCEPTANCE.**
