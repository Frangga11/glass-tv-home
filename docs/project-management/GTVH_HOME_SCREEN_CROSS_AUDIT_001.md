# GTVH-HOME-SCREEN-CROSS-AUDIT-001 — 8 November 2026

## Status
**SOURCE CROSS-AUDIT: PARTIAL — NOT READY TO CLAIM HOME SCREEN PASS**

- Audited source branch: `fix/home-balanced-frame-001`
- Exact source HEAD: `e9fdecdb4dd44eefeed1464fddaf8edd2e2ec5e0`
- PR #79: OPEN / DRAFT / UNMERGED; stacked on PR #77.
- Scope: the complete visible MainActivity Home surface and its direct interaction/state paths, not the Home Screen Settings destination audited separately under E06.
- No source-code change was made during this audit. This checkpoint records findings only.

## Quality gate and repository checks

| Check | Result | Evidence / boundary |
|---|---|---|
| CI #1560 / run `42484233880` exact HEAD | PASS | Both jobs completed successfully: Lists/tools/docs and Launcher build/unit tests. |
| Release build | PASS | Gradle reported BUILD SUCCESSFUL. |
| Release APK size | PASS | 1,624,625 bytes; validation artifact uploaded. |
| CI artifact | PASS | Artifact `glass-tv-home-dock-visual-parity-b85cd558f614c4282bf610a81dc1bc8827f50e6d`, 1,424,041 bytes; artifact ZIP SHA-256 `5fa3ea60f7e727b8e4ac663e08ea7488564a70b7acb60e4c3a7040fd821b9f86`. |
| Main layout XML structure | PASS | XML parsed; 63 unique IDs, no duplicate `android:id` declarations. |
| MainActivity onCreate ID binding | PASS | All 52 unique `R.id` references in the audited onCreate range exist in `activity_main.xml`. |
| Static source behavior | PARTIAL | Several interaction/state defects and one focus race risk listed below. |
| Rendered UI / DPAD on Mi TV Stick | PENDING | No emulator or physical device execution in this audit. |
| Crash/error log inspection | PENDING | No runtime session/logcat was collected. |

## Component inventory

| Home component | Source audit | Notes |
|---|---|---|
| Clock / AM-PM / Gregorian date / Shamsi date | PASS | Views and update path exist; compact text sizing is source-verified. Runtime readability remains pending. |
| Location pill / altitude | PASS | Selected zone is rendered; missing/invalid zone hides the pill; click opens LocationActivity. |
| Weather / AQI pill and atmospheric overlay | PARTIAL / FAIL | G3B cache/formatter and WeatherActivity click path are wired; focus-scale listener is overwritten. Separate dynamic weather overlay refresh has no production entry point (HOME-004/005). |
| Now Playing card | FAIL | Paused/buffering session selection conflicts with `MediaInfo.hasMusic()`; see HOME-001. |
| Home / Profile / Settings navigation | PASS | Correct order and independent clickable/focusable segments; runtime DPAD acceptance pending. |
| Inputs / Wi-Fi / Assistant | PASS (source) | Visibility/availability gates and click/key routes are present; OEM/system handler behavior remains runtime pending. |
| Prayer timetable | PARTIAL | Six visible prayer columns are populated and next prayer is highlighted. The detailed popup entry point is attached only to a hidden fallback view; see HOME-002. |
| Daily Ayah / prayer shelf | PASS (source) | Visibility preferences, translation, Arabic layout and reader dialog are wired. Network refresh and visual rendering remain pending. |
| Wallpaper / hero shelf | PASS (source) | Bundled Default asset is staged atomically; decode uses a worker and generation token; safe black fallback exists. Visual and rotation timing remain pending. |
| App inventory / Grid / Dock | PASS (source/model) | App click, long-press, inventory, Dock and overflow paths exist; geometry/model tests run in CI. Device DPAD/focus acceptance remains pending. |
| Focus persistence / resume | PARTIAL | Grid/resume requests have generation guards and retained-focus glass restoration; Dock's delayed layout retry lacks equivalent cancellation. |
| Profile / quick controls / module cards | PASS (source) | Short/long press actions are wired; popup/focus acceptance remains pending. |
| Back / wallpaper fullscreen / Nightstand screensaver | PASS (source) | Distinct state paths exist; timing and remote behavior remain pending. |

## Findings

### HOME-001 — Paused music makes the Home Now Playing card disappear
- **Severity:** P2 functional defect.
- **Status:** FAIL.
- **Evidence:** `NowPlayingManager.refreshCurrentMedia()` deliberately selects PLAYING sessions first, then PAUSED / BUFFERING / CONNECTING sessions. It subsequently sets `isPlaying` true only for STATE_PLAYING. However, `MediaInfo.hasMusic()` returns `isPlaying && isAllowedMusicApp(packageName)`. `MainActivity.updateNowPlayingUi()` hides the card whenever `hasMusic()` is false.
- **Impact:** Pausing Spotify / YouTube Music hides the Home card, so the Home Play/Pause control cannot be used to resume. The paused icon branch in `getDisplayText()` is also unreachable while `hasMusic()` is false.
- **Required correction:** Separate “supported active media session exists” from “currently playing”; retain the card for supported paused/buffering sessions and derive the icon from playback state. Add regression tests for playing → paused → resumed and unsupported app sessions.
- **No code correction made in this audit.**

### HOME-002 — Prayer detail popup has no visible Home entry point
- **Severity:** P2 interaction gap; confirm intended product behavior before changing.
- **Status:** FAIL for the popup entry path; the six-column timetable itself is source-wired for display.
- **Evidence:** `activity_main.xml` declares the visible `prayerHeader` and `prayerTable`, but neither is clickable/focusable. The separate `prayerTimes` fallback TextView is explicitly `visibility="gone"`. `MainActivity` attaches click/key listeners that call `showPrayerPopup()` only to this hidden fallback view.
- **Impact:** The visible timetable cannot open the full prayer schedule / zone change / Azan settings popup. The popup implementation exists but its Home entry point is unreachable through the visible table.
- **Required correction:** If the timetable is intended to be interactive, attach a click and DPAD-activate path to a visible timetable/header container and define focus behavior. If the table is intentionally passive, mark the popup as a separate entry point and remove the misleading hidden-only binding.
- **No code correction made in this audit.**

### HOME-003 — Delayed Dock focus retry has no cancellation generation
- **Severity:** P2 intermittent focus-steal risk.
- **Status:** PARTIAL / RISK.
- **Evidence:** `focusTileWhenLaidOut()` checks `gridFocusRequestGeneration` on every retry. By contrast, `focusDockTileWhenLaidOut(position, attemptsRemaining)` posts up to three animation-frame retries without capturing/checking a generation token. Header and non-Grid focus changes increment the shared generation, but the Dock retry does not consult it.
- **Impact:** A delayed Dock request can potentially focus a Dock card after the user has already moved to another destination, producing a remote-navigation jump.
- **Required correction:** Give Dock focus requests their own generation or use the shared generation consistently; invalidate on newer focus destinations and lifecycle pause. Add a source/behavior regression test for Dock retry cancellation.
- **No code correction made in this audit.**

### HOME-004 — Weather pill focus animation listener is overwritten
- **Severity:** P3 visual/interaction consistency defect.
- **Status:** PARTIAL.
- **Evidence:** During initial weather setup, `weatherAirQualityView` receives a focus listener that scales it using `TvUiTokens`. Later, the common Header pill loop includes `R.id.weatherAirQuality` and calls `setOnFocusChangeListener()` again to move the top highlight. Android View retains only the most recently assigned listener.
- **Impact:** The weather pill keeps its click and DPAD key paths, but loses its intended focus-scale feedback.
- **Required correction:** Compose the scale and Header highlight behavior in one listener or use a non-overwriting focus callback mechanism. Add a regression assertion.
- **No code correction made in this audit.**

### HOME-005 — Home atmospheric weather overlay refresh path is dormant
- **Severity:** P3 feature freshness / visual behavior defect.
- **Status:** FAIL (automatic refresh entry path).
- **Evidence:** `MainActivity.updateHomeWeather()` is only called recursively from its own stale-response retry; there is no startup, resume, minute-tick or external production caller. `HomeWeather.refresh()` is only invoked inside that method. `WeatherActivity` refreshes `WeatherAirQuality` directly and does not populate the legacy `HomeWeather` cache. The Home atmospheric overlay is updated only by `showHomeWeather(line)`, settings-toggle refresh using `HomeWeather.last(this)`, or the test override.
- **Impact:** The Home weather/AQI text remains correctly fed by the G3B `WeatherAirQuality` cache, but the separate dynamic rain/thunderstorm wallpaper overlay has no normal automatic data-refresh path. It may remain absent or stale unless a legacy cache was populated by another path or the setting/test override is invoked.
- **Required correction:** Decide whether the atmospheric overlay remains an approved feature. If yes, call a guarded `updateHomeWeather()` from the appropriate Home lifecycle/refresh cadence and add stale-location/lifecycle tests. If no, remove the dormant refresh path and related UI setting so it cannot imply active functionality.
- **No code correction made in this audit.**

## Additional observations
- Existing E06 documentation is specifically about the four controls inside the **Home Screen Settings destination**. It is not a full Home main-screen audit; this document closes that documentation-scope gap.
- `updateHomeWeather()` appears to have no production entry point beyond its own retry call. The visible Home weather/AQI text is currently fed by `WeatherAirQuality.getFormatted()`; the legacy `HomeWeather` path only calls `updateWeatherAqi()` plus `updateWeatherOverlay()`. Confirm whether the atmospheric wallpaper overlay is still an intended active feature before wiring or removing this dormant path.
- No evidence of missing IDs or duplicate IDs was found in the main layout.
- Passing CI establishes source/build/unit-test validation, not proof that every screen element renders correctly or every remote action works on the target firmware.

## Decision / next actions
1. Fix HOME-001 and add explicit paused-session regression coverage.
2. Decide whether the visible prayer timetable should open the detailed popup; then wire and test the chosen interaction.
3. Guard Dock delayed focus retries against stale requests.
4. Restore the weather pill's combined focus feedback.
5. Restore or retire the dormant Home atmospheric weather overlay refresh path (HOME-005).
6. Run exact-head CI after any source corrections.
7. Only after owner authorization, perform rendered screenshot, DPAD, lifecycle/resume, weather/prayer refresh, Now Playing pause/resume, and visual acceptance on Mi TV Stick.

## Safety boundary
- No APK installed.
- No ADB commands executed.
- No device settings or default launcher changed.
- No physical TV test performed.
- No PR merged or closed.
- PR #79 remains OPEN / DRAFT / UNMERGED; PR #77 remains unmerged.

**Overall:** HOME SCREEN AUDIT = **PARTIAL / NOT PASS**. The four findings above must be resolved or explicitly accepted before claiming the entire Home screen is fully verified.


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
