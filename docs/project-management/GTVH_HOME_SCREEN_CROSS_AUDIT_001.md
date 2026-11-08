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
| Weather / AQI pill | PARTIAL | Shared G3B cache/formatter and WeatherActivity click path are wired. Its dedicated focus-scale listener is overwritten by the later shared Header listener. |
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
- **No code correction made in this audit.
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
6. Only after owner authorization, perform rendered screenshot, DPAD, lifecycle/resume, weather/prayer refresh, Now Playing pause/resume, and visual acceptance on Mi TV Stick.

## Safety boundary
- No APK installed.
- No ADB commands executed.
- No device settings or default launcher changed.
- No physical TV test performed.
- No PR merged or closed.
- PR #79 remains OPEN / DRAFT / UNMERGED; PR #77 remains unmerged.

**Overall:** HOME SCREEN AUDIT = **PARTIAL / NOT PASS**. The four findings above must be resolved or explicitly accepted before claiming the entire Home screen is fully verified.
