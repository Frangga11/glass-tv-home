> **CANONICAL CHECKPOINT — READ THIS FIRST**  
> The only authoritative restart point is [GTVH-CHECKPOINT-LATEST.md](GTVH-CHECKPOINT-LATEST.md) on branch `docs/project-master-inventory`. Start every continuation there; do not resume from historical checkpoint blocks, old PR descriptions, or branch-local notes. This file is supporting detail/history and must not establish a competing current state.  
> **Checkpoint authority:** GitHub repository `Frangga11/glass-tv-home`; canonical docs branch `docs/project-master-inventory`; single entry file `docs/project-management/GTVH-CHECKPOINT-LATEST.md`. The latest checkpoint supersedes all older status statements below.  

---

# GTVH-HOME-SCREEN-CROSS-AUDIT-001 — 8 November 2026

## Current status — superseding final source review

**SOURCE IMPLEMENTATION + STATIC CROSS-AUDIT: PASS**  
**EXACT-HEAD CI FOR IMPLEMENTATION: PASS**  
**ADDITIONAL TEST-ONLY COMMIT CI #1582: PASS**  
**PHYSICAL MI TV STICK ACCEPTANCE: PENDING — NOT RUN**

- Implementation branch: `fix/home-balanced-frame-001`
- Validated implementation HEAD: `97f7e0c96d1c1dcd431cdc6edec31bf4837e11a5`
- Test-only commit: `49f2777cee006a5afd50a33b85cb90a61a077e40` (CI #1582 PASS); PR-branch docs-only tip: `c15e4383d7dd3f148b31a19f5e9b85423936b56d` (CI #1583 running).
- PR #79: OPEN / DRAFT / UNMERGED; stacked on PR #77.
- PR #79 scope: 22 changed files across Home geometry, Header, location metadata, strings, media/session behavior, focus restoration, CI and tests.
- Main Home layout: 63 unique IDs; no duplicate ID declarations.
- Source-contract test file: 26 tests. Exact-head CI #1582 ran 160 Python tests (1 skipped); all three new Header/location contracts passed.
- No APK installation, ADB, device setting/default-launcher change, physical TV test, force-push or merge.

## Quality gate and repository checks

| Check | Result | Evidence / boundary |
|---|---|---|
| Implementation exact-head CI #1581 / run `42486917618` | PASS | Both required jobs SUCCESS on merge ref containing exact implementation HEAD `97f7e0c96d1c1dcd431cdc6edec31bf4837e11a5`. |
| Release build / companion workflow #36 | PASS | Release build SUCCESS; companion release workflow run `42486917728` SUCCESS. |
| Release APK diagnostic | PASS | 1,627,577 bytes; artifact ID `13620161574`. Diagnostic only; no installation. |
| Main layout XML | PASS | 63 unique IDs, no duplicate declarations. |
| Header geometry tests | PASS | 1920, 3840, 1280 width cases plus impossible/unmeasured geometry. |
| Home source-contract suite before added tests | PASS | Included in exact-head CI #1581. |
| Three additional Header/location contract tests | PASS | All three passed in CI #1582; Python suite 160 tests, 1 skipped. |
| Physical rendering / DPAD / lifecycle | PENDING | No emulator or physical device execution. |
| Runtime crash/log inspection | PENDING | No new logcat/runtime session collected. |

## Component inventory — current source

| Component | Current source status | Function / verification |
|---|---|---|
| Clock / AM-PM / Gregorian / Shamsi date | PASS (source) | Compact sizing; existing format and update logic retained. |
| Location + altitude pill | PASS (source) | Selected town and optional altitude; localized mdpl / m ASL; click and DPAD Center/Enter route to prayer-zone dialog. |
| Weather / AQI pill | PASS (source) | Shared G3B formatter/cache; click path to dashboard; focus-scale and Header highlight composed in one listener. |
| Atmospheric wallpaper weather overlay | PASS (source) | Separate HomeWeather cache; resume + active minute-tick entry; 15-minute due gate, overlap guard and disabled-preference guard. Not the AQI text provider. |
| Now Playing | PASS (source) | Supported Spotify / YouTube Music sessions remain visible while playing, paused, buffering or connecting; playback icon follows actual state. |
| Home / Profile / Settings capsule | PASS (source) | One shared glass capsule; independent Home → Profile → Settings targets; active/focused selector; localized ID/EN labels. |
| Inputs / Wi-Fi / Assistant | PASS (source) | Click, long-click where applicable, DPAD and visibility-gated transitions wired. OEM/system intent behavior remains runtime pending. |
| Prayer timetable | PASS (source) | Visible table is focusable/clickable; click, DPAD Center and Enter open detail popup; explicit Up/Right/Down routes. |
| Daily Ayah / right shelf | PASS (source) | Existing content, visibility preferences and reader path retained; visual/network acceptance pending. |
| Hero / wallpaper / ambient overlay | PASS (source) | Existing asset, decode worker and generation protection retained; rendered composition/rotation acceptance pending. |
| App Grid / Dock / overflow | PASS (source/model) | Existing sizing and app ordering retained; generation-guarded focus retries; no device DPAD acceptance yet. |
| Focus persistence / resume | PASS (source contract) | Stale callbacks invalidated on newer destinations and lifecycle pause; retained-focus glass restored. Runtime race acceptance pending. |
| Quick controls / profile popup / module cards | PASS (source) | Existing short/long-click actions retained; popup runtime acceptance pending. |
| Back / fullscreen wallpaper / screensaver | PASS (source) | Existing distinct state paths retained; runtime remote/timing acceptance pending. |

## Resolved findings — HOME-001 to HOME-005

### HOME-001 — Now Playing paused-session visibility
**RESOLVED / SOURCE + CI PASS.** The media model snapshots an allowed active session independently from `isPlaying`. PLAYING, PAUSED, BUFFERING and CONNECTING remain eligible; the icon reflects playback state. Regression contract is present.

### HOME-002 — Visible prayer timetable entry point
**RESOLVED / SOURCE + CI PASS.** The visible `prayerTable` is focusable/clickable and opens the detailed popup by click, DPAD Center or Enter. Header Assistant → timetable, timetable Up → Assistant, Right → Inputs and Down → app area are explicitly wired and covered by source contracts.

### HOME-003 — Deferred Dock/Grid focus race
**RESOLVED / SOURCE + CI PASS.** Dock retries carry a request generation; stale work is ignored. Resume restoration captures the same generation; global focus changes to non-Grid destinations and `onPause()` invalidate queued work. Already-focused Grid/Dock cards reapply the glass selection.

### HOME-004 — Weather/AQI focus listener collision
**RESOLVED / SOURCE + CI PASS.** Scale feedback and Header highlight are composed in one listener; the former standalone listener no longer gets overwritten.

### HOME-005 — Atmospheric weather overlay refresh
**RESOLVED / SOURCE + CI PASS.** Refresh is entered on Home resume and active minute tick, keeps its 15-minute due cadence, avoids overlapping requests, respects the dynamic-effects disabled preference and refreshes when the setting is enabled. It remains separate from the G3B Weather/AQI text cache.

## Header and layout cross-audit

- Navigation order is Home → Profile → Settings; the three targets sit inside one shared glass capsule.
- Center capsule width is computed from measured left and right control bounds via `HomeHeaderGeometry.centeredWidthPx`; layout listeners recalculate after the measured groups change.
- Header tab click actions remain connected: Home active-state refresh, Profile popup, Settings quick-control popup.
- Location pill opens the prayer-zone dialog; center/enter keys invoke the same click action.
- Weather pill has a separate DPAD route to Location, Home and Now Playing/Grid.
- Main content uses 29dp top/start/end padding. Dock geometry uses 29dp horizontal/vertical safe-frame constants and derives the tray bottom placement from the same 29dp target.
- At 320dpi logical density, 29dp = 58px. Dock/Grid card dimensions and app ordering were not intentionally changed by this safe-frame refinement.
- The XML includes 63 unique IDs. Decorative wrappers/dividers and the TextClock are intentionally not interactive; the action-bearing Header and prayer controls have explicit bindings.
- No additional source-level missing-handler or new functional defect was confirmed in this static pass.

## Remaining gates and safety boundary

1. Await exact-head CI for the additional test-only commit `49f2777cee006a5afd50a33b85cb90a61a077e40`.
2. If the new tests fail, correct only the failing test contract or the proven underlying defect; do not make speculative layout changes.
3. Keep PR #79 Draft/Open/Unmerged and preserve the PR #77 stacked dependency.
4. Physical Mi TV Stick verification remains a separate acceptance gate: screenshots/rendered safe margins, header clipping/overlap, all DPAD routes, click/long-click actions, prayer popup, Now Playing pause/resume, weather/AQI focus, overlay refresh, app launch/return, cold start and resume.
5. No APK installation, ADB, default launcher or device-setting changes until separately authorized.

**Overall decision:** source implementation and current static cross-audit PASS; additional test-only CI pending; whole Home Screen acceptance remains **PARTIAL / NOT PASS** until physical Mi TV Stick acceptance is completed.

## Historical remediation log

## Remediation checkpoint — 8 November 2026

**Source branch:** `fix/home-balanced-frame-001`  
**Exact source/test HEAD:** `701487f7f708d0b4342b613205fde9fba7c0dd72`  
**PR:** #79 — OPEN / DRAFT / UNMERGED  
**Remediation state:** CODE CHANGES APPLIED; EXACT-HEAD CI PENDING; DEVICE ACCEPTANCE NOT RUN.

### Corrections applied

1. **HOME-001 — Now Playing:** `MediaInfo` now snapshots a supported active session state independently from `isPlaying`. Spotify/YouTube Music sessions in PLAYING, PAUSED, BUFFERING, or CONNECTING remain eligible for the Home card. The snapshot is derived while the media refresh worker constructs the model, avoiding a new controller-state Binder read in the UI listener. The existing play/pause icon still reflects `isPlaying`. A source-contract regression test covers the accepted states and the card's independent visibility predicate.
2. **HOME-002 — Prayer timetable:** the visible `prayerTable` is now focusable/clickable and opens the detailed popup by click, DPAD Center, or Enter. It has explicit focus feedback and retains the existing right/down/up navigation behavior. The hidden `prayerTimes` fallback remains for its text/update compatibility; it is no longer the only interaction route.
3. **HOME-003 — Dock delayed focus:** Dock focus requests now capture `gridFocusRequestGeneration`; each animation-frame retry checks that generation before attempting focus or fallback. A newer focus destination invalidates the old request. Added source-contract regression coverage.
4. **HOME-004 — Weather/AQI focus:** removed the earlier standalone weather scale listener and composed scale feedback into the shared Header focus listener. The same listener now applies both Weather/AQI scale and Header highlight behavior without listener replacement. Added source-contract regression coverage.
5. **HOME-005 — Atmospheric weather overlay:** `updateHomeWeather()` is now called from Home `onResume()`, with existing `HomeWeather.due()` cadence and stale-response/location generation protection preserved. The legacy atmospheric overlay refresh remains separate from the G3B Weather/AQI text cache. Added source-contract regression coverage.

### Validation and remaining boundary

- Source changes and regression assertions are present on the exact HEAD above.
- Exact-HEAD CI has **not yet been observed**. The GitHub workflow-run/status queries returned no checks for this new head; prior CI #1560 is not evidence for these later commits.
- Regression tests have been added but are **not yet claimed executed/passing**.
- No APK installation, ADB, device settings/default launcher changes, or physical Mi TV Stick testing.
- No PR merged or closed. PR #79 and dependency PR #77 remain unmerged.
- The five source findings have implementation patches, but the overall Home Screen remains **NOT PASS / acceptance pending** until exact-head CI and authorized runtime/device verification complete.

### Next actions

1. Obtain exact-head CI for `701487f7f708d0b4342b613205fde9fba7c0dd72`; fix any compile/test failures and repeat on the resulting exact HEAD.
2. Review the complete PR diff for unintended changes and verify the stacked PR #77 dependency remains intact.
3. After CI passes, perform static regression review and update the audit status.
4. Only after separate owner authorization, verify visible prayer popup activation, Now Playing pause/resume, Dock/header DPAD focus, Weather/AQI focus feedback, overlay refresh/lifecycle, and whole-screen rendering on Mi TV Stick.


## Exact-head CI result — 8 November 2026

- PR #79 head: `701487f7f708d0b4342b613205fde9fba7c0dd72`.
- CI #1574 / run `42486582167`: **SUCCESS** on the PR merge ref `fa5e4feb4ba451d6209762278ce18a6337edbdba`, whose merge commit message confirms it includes the exact PR head above.
- Job 1, **Lists, tools and docs**: SUCCESS.
- Job 2, **Launcher build and unit tests**: SUCCESS.
- Release build: `BUILD SUCCESSFUL`.
- Release APK size diagnostic: **1,627,417 bytes**.
- The PR #79 artifact upload step completed successfully (artifact ID `13619931751`).
- The companion APK release workflow run #29 / `42486582644` also completed SUCCESS; this is a workflow result, not authorization to install or test the APK.

**Updated assessment:** the five source-level corrections and their CI regression/build validation are **PASS**. Whole Home Screen end-to-end acceptance remains **PARTIAL / NOT PASS** until authorized Mi TV Stick rendered, DPAD, lifecycle, media, prayer, and weather-overlay verification is performed. No APK installed, no ADB/device settings/default launcher changes, and no PR merged.


### Follow-up after interrupted connection — 8 November 2026

The remediation session resumed and re-verified the PR #79 branch. Further tightening was applied after reviewing the DPAD focus graph:

- HOME-002: the visible prayerTable now opens the prayer popup on click / DPAD Center / Enter, and its DPAD Up route returns to the right-side Header controls. DPAD Down returns to the app area. DPAD Down from the visible rightmost Header control (btnAssistant) enters the visible timetable. Regression coverage asserts both entry and return routes.
- HOME-005: the atmospheric overlay refresh is now checked on Home resume and from the active Home minute tick. HomeWeather.due() retains its 15-minute cadence; homeWeatherRunning prevents overlapping fetches. The refresh continues to update only the atmospheric overlay and does not overwrite the G3B Weather/AQI pill cache.
- HOME-001, HOME-003 and HOME-004 remain as previously recorded: immutable active-session state snapshot for supported playback states; generation-guarded Dock focus retries; composed Weather/AQI scale and Header highlight feedback.

Latest source/test HEAD: 765634e7d806f48bc38014119cd170a8697ea442. PR #79 remains OPEN / DRAFT / UNMERGED, stacked on PR #77. CI #1576 was running on the immediately preceding head 9a6133c956819fa5c840cada5762b9e68ac05785; it does not validate the latest DPAD changes. Exact-head CI for 765634e7d806f48bc38014119cd170a8697ea442 is pending observation. No regression test pass is claimed until that run completes.

No APK installation, ADB, device settings/default launcher mutation, physical Mi TV Stick test, force-push, or merge.


### Weather overlay preference guard — 8 November 2026

- The Home atmospheric overlay refresh now returns without starting a network request while the dynamic weather overlay preference is disabled.
- Enabling the preference reapplies the cached line and invokes the existing due-gated refresh, so stale/missing weather data can be refreshed without waiting for the next minute tick.
- Regression coverage asserts both the disabled guard and the enable-triggered refresh.

Latest source/test HEAD: 97f7e0c96d1c1dcd431cdc6edec31bf4837e11a5. PR #79 remains OPEN / DRAFT / UNMERGED. Exact-head CI has not yet been observed for this commit; tests remain unclaimed as passing. Earlier CI #1579 is on an earlier commit and cannot validate this change. Device acceptance remains NOT RUN.


## Final follow-up validation — 8 November 2026

A follow-up source/test commit extended HOME-002 regression coverage to assert DPAD Up return routing and the Header Assistant → visible prayer timetable focus path.

- **Latest PR #79 head:** `765634e7d806f48bc38014119cd170a8697ea442`.
- **CI #1579 / run `42486810729`: SUCCESS**. The run is associated with the latest head above; its PR merge ref `09c7b98534f151cb292f1b03c50b9d9ed21c3c90` has commit message confirming it merges that exact head into the PR base.
- Both jobs passed: Lists/tools/docs; Launcher build/unit tests.
- Release build: **BUILD SUCCESSFUL**.
- Release APK diagnostic size: **1,627,581 bytes**.
- Test APK artifact upload completed (artifact ID `13620310383`).
- Companion APK release workflow run #34 / `42486810748`: SUCCESS.

**Current gate:** all five remediation findings have source corrections and latest-head CI validation PASS. Whole Home Screen end-to-end acceptance remains **PARTIAL / NOT PASS** pending separately authorized Mi TV Stick rendered, DPAD, lifecycle, Now Playing pause/resume, prayer popup, Weather/AQI focus, and atmospheric overlay verification. No APK installed, no ADB/device settings/default launcher changes, and no PR merged.


## Final latest-head validation — 8 November 2026

The final HOME-005 regression extension verifies that the atmospheric overlay refresh respects the user's dynamic-weather-effects setting and that enabling the setting reuses the cached overlay while scheduling a due refresh.

- **Latest PR #79 head:** `97f7e0c96d1c1dcd431cdc6edec31bf4837e11a5`.
- **CI #1581 / run `42486917618`: SUCCESS** on PR merge ref `5203d9cab667b976a3cd47ecaceac444ecd8e06b`; merge commit message confirms it includes the latest exact head.
- Both jobs passed: Lists/tools/docs; Launcher build/unit tests.
- Release build: **BUILD SUCCESSFUL**.
- Release APK diagnostic size: **1,627,577 bytes**.
- Test APK artifact upload succeeded (artifact ID `13620161574`).
- Companion APK release workflow run #36 / `42486917728`: SUCCESS.

**Final source/CI status:** HOME-001 through HOME-005 = **SOURCE FIXED + LATEST-HEAD CI PASS**. Overall Home Screen = **PARTIAL / NOT PASS** until separately authorized end-to-end Mi TV Stick acceptance is completed. No APK installation, ADB, device settings/default launcher changes, or PR merge.


## Exact-head CI #1582 and reconciliation-doc update — 8 November 2026

- Test-only commit `49f2777cee006a5afd50a33b85cb90a61a077e40` is **CI #1582 PASS** (run `42487572059`). The only diff from implementation HEAD `97f7e0c96d1c1dcd431cdc6edec31bf4837e11a5` is `tests/test_home_layout_bounds_contract.py` (+59 lines).
- Python CI: **160 tests, OK (1 skipped)**; all three newly added location/Header regression tests passed.
- Android CI: `testReleaseUnitTest` + `assembleRelease` **BUILD SUCCESSFUL**; APK diagnostic 1,627,577 bytes; artifact #13619209786.
- Updated PR-branch reconciliation document to replace the obsolete intermediate 34dp safe frame with the approved 29dp frame. PR-branch docs-only commit: `c15e4383d7dd3f148b31a19f5e9b85423936b56d`. CI #1583 is validating that documentation-only tip; no need to change application source.
- Release workflow #37 is explicitly **not** the current candidate: its logs show CI #1545 / older commit `c058935ed4398f5c7964d753fcf35a9cf77989bb`. Do not cite that release asset as the latest PR #79 APK.
- PR #79 remains OPEN / DRAFT / UNMERGED; physical Mi TV Stick acceptance remains NOT RUN. No APK installation, ADB, device settings/default launcher mutation or merge.


## Home Header cross-audit addendum — weather/AQI pill width (8 Nov 2026)

Audited `activity_main.xml` and `MainActivity.applyHeaderWidths()` on PR #79. Both `locationPill` and `weatherAirQuality` use `layout_alignParentStart="true"`; the width mismatch came from independent runtime formulas (34% vs 44%). The correction preserves the location reference and applies its single computed width to both controls. This makes left and right edges mathematically identical in the shared RelativeLayout coordinate space. Weather content updates still only replace text and do not alter layout parameters; the view remains single-line/end-ellipsized. Regression test checks these source/layout contracts.

Changed files: `MainActivity.java` and `tests/test_home_layout_bounds_contract.py` only. Source commit `3c9821864a08bb50f6f1b13473964f8d5b5baeb7`; current PR #79 HEAD `65544d9eea9bd0a5ae5156a9bd58724b1817fd2a`. CI #1587 run `42489445766` is partially green (Lists/tools/docs PASS; Android job IN PROGRESS). No visual/physical acceptance claim.


## CI #1587 final validation — 8 Nov 2026

CI #1587 / run `42489445766` completed **SUCCESS** on PR #79 head `65544d9eea9bd0a5ae5156a9bd58724b1817fd2a`. Both required jobs passed. Python regression suite: **161 tests, OK (1 skipped)**; the new `test_weather_aqi_pill_matches_location_pill_width_and_left_anchor` passed. Android `testReleaseUnitTest` and `assembleRelease`: **BUILD SUCCESSFUL**. Release APK diagnostic: **1,627,545 bytes**; CI artifact ID **13621187055**, ZIP size **1,426,725 bytes**. CI proves source/build/test contracts only; no rendered Mi TV Stick or physical visual acceptance is claimed. PR #79 remains OPEN / DRAFT / UNMERGED.


## Latest checkpoint — GTVH-HOME-WEATHER-PILL-WIDTH-REFINEMENT-002 (8 Nov 2026)

The approved reference is the location/altitude capsule. Both left Header capsules now use **30% of measured Header width**, the same **30dp height**, 12dp horizontal padding, 0dp vertical padding on the weather TextView (matching the location container's default zero vertical padding), centered vertical content, and the same glass-pill background drawable/radius. Both retain the same parent-start anchor and existing 8dp vertical gap. The 30% width is computed once in `MainActivity.applyHeaderWidths()` and assigned to both controls. Weather remains single-line with end ellipsis. No data provider, font/icon, focus/click/DPAD, animation, clock/date, nav, Wi-Fi/Assistant, safe-frame or other Home geometry changes intended.

Changed PR #79 branch `fix/home-balanced-frame-001`:
- `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java` — shared width formula changed from 34% to 30%.
- `launcher/app/src/main/res/layout/activity_main.xml` — weather pill height 30dp, vertical padding zero, vertical centering; location pill remains reference.
- `tests/test_home_layout_bounds_contract.py` — regression checks 30% shared width, equal 30dp height, equal horizontal padding, vertical alignment and content update invariants.

Current PR #79 HEAD / implementation source SHA: `bf65621ae46b170294e4333928886fbafa0898b6`. CI #1590 / run `42490240670`: SUCCESS, both jobs pass. Python suite: **161 tests, OK (1 skipped)**; the new pill-geometry regression test passes. Android `testReleaseUnitTest` and `assembleRelease` completed successfully. Release APK: **1,627,561 bytes**. Downloadable workflow artifact ID `13621062347` (ZIP 1,426,708 bytes; expires 15 Nov 2026). Local extracted APK SHA-256: `51c6921ad7055812dd4c3de844bce26516b0ffeea6dd8401abc5d94913dcb56d`.

PR #79 remains OPEN / DRAFT / UNMERGED. No physical device test is claimed. Owner will install/test this APK on Mi TV Stick at 1920×1080, 320dpi; Indonesian/English; short/long location, weather and AQI content; inspect clipping, exact visual height/width, common left/right edges, vertical spacing, focus/click/DPAD and unaffected Header elements. Documentation checkpoint is synced after build/test; physical acceptance remains pending.
