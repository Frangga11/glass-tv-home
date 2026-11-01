# D02 — Settings Change / Dependent UI Propagation Audit

**Date:** 1 November 2026  
**Status:** TARGETED SOURCE REPAIR COMPLETE — EXACT-HEAD CI PENDING — DEVICE ACCEPTANCE DEFERRED  
**Branch:** `feature/d02-settings-ui-propagation`  
**Parent:** D01 exact head `cd710681099faece14905faa244f57245bae84d1`  
**Merge policy:** D-013 — isolated Draft PR; no early merge.

## 1. Objective and boundary

Verify that a successful in-app settings change is reflected immediately in every relevant live UI surface, without requiring the user to leave and re-enter Settings or restart the launcher.

This is a propagation audit, not a persistence redesign. D01 remains the authoritative storage-ownership map. No preference keys, stores, defaults, profile scope, saved data, or cross-module contracts are changed here. D03 owns the locked-app count/list refresh path; D04 owns profile/avatar synchronization in depth; D05/D07 own photo metadata and cross-store atomicity; D06 owns process-death/reboot recovery. Relevant adjacent surfaces were inspected only to confirm their live update boundary.

## 2. Verification method

For each relevant setting, trace:
1. the UI control and its callback;
2. the state owner and persisted write;
3. the live view/component update;
4. the return/re-entry path where a separate Activity is involved;
5. failure/empty-state behavior where visible.

The audit is limited to source-level call paths on the exact D01 parent. It does not claim physical Android TV interaction or API 30/33+ runtime acceptance.

## 3. Propagation matrix

| Setting / action | State owner and write path | Immediate dependent UI update | Source result |
|---|---|---|---|
| Weather widget visibility | `MainActivity.setWeatherWidgetEnabled()` writes `theme_prefs` | Updates `weatherAirQualityView` visibility in the active Home view | PASS |
| Daily prayer panel | `MainActivity.setIslamicShelfEnabled()` writes `theme_prefs` | Updates shelf visibility and refreshes its content when enabled | PASS |
| Daily Ayah visibility | `WeeklyAyah.setShown()` from Settings callback | Calls `showAyah()`; refreshes Ayah when enabled | PASS |
| Icon animations | `MainActivity.setIconAnimationsEnabled()` writes `wallpaper_prefs` | Updates adapter animation policy and reapplies focused-tile state | PASS |
| Glass effects | `MainActivity.setGlassEffectsEnabled()` writes `wallpaper_prefs` | Updates glider style, item decorations, backdrop blur and root invalidation | PASS |
| Dynamic weather overlay | `MainActivity.setDynamicWeatherEffectsEnabled()` writes `wallpaper_prefs` | Clears test override and recomputes the live weather overlay | PASS |
| Icon glow | `MainActivity.setIconGlowEffectsEnabled()` writes `wallpaper_prefs` | Applies/cancels glow and synchronizes glass glow immediately | PASS |
| Wallpaper description | `MainActivity` toggle writes `wallpaper_prefs` | Hides/shows hero shelf and refreshes current wallpaper metadata when enabled | PASS |
| Wallpaper shuffle/category/next | MainActivity wallpaper controls update wallpaper preference/cycle state | Selection flow applies next wallpaper and resets/restarts the appropriate cycle/timer | PASS |
| StandBy photo mode | `MainActivity.setStandbyPhotoMode()` delegates to `StandbyPhotoStore` | Now checks the synchronous commit result; refreshes screensaver and shows success only after persistence succeeds. Empty collection and failed commit retain the prior state and show an error; Settings pane rerenders from persisted state | PASS AFTER D02-F01 FIX |
| Profile name/avatar | `ProfileManager` mutation from Settings | Updates Settings profile labels/avatar, sidebar card and Home top-bar user icon; pane is rerendered where needed | PASS |
| Profile switch / add / delete | `UserSwitchDialog` / `ProfileManager` callbacks | Settings sidebar and right pane are rebuilt; Home's `onResume()` refreshes tiles and top-bar profile state | PASS |
| Child Mode | `ChildModeManager` profile-scoped mutation | Reloads Home tiles, refreshes top-bar identity, updates sidebar and rerenders Settings after PIN result | PASS |
| Prayer zone / location | `LocationActivity` persists selected zone and related snapshots, then returns | Home `onResume()` refreshes prayer times, Islamic shelf, weather/AQI and top-bar state; station selection in Home also calls `refreshLocationDependentData()` | PASS |
| Weather station / region / mood | `AppLocationManager` / `WeatherActivity` callbacks | Weather page repopulates after mood change or station sync; returning to Home refreshes weather/AQI | PASS |
| Language | `AppLanguage.setLanguage()` | Android 13+ per-app locale path is used; pre-Android 13 path recreates Activity and preserves the Settings reopen intent/tab state | PASS |

## 4. Key source evidence

- `MainActivity.java`: direct owner setters at lines 358–398; live Home refresh on `onResume()` at lines 1548–1586; location-dependent refresh at lines 1777–1782; live weather overlay/effect setters at lines 2399–2415; prayer shelf setter at lines 2906–2916; wallpaper description and selection callbacks around lines 1164–1169 and 4520–4550; StandBy mode and screensaver refresh around lines 1336–1380.
- `ModernSettingsDialog.java`: settings callbacks for profile/Child Mode, Home, weather widget, visual effects and StandBy at lines 895–1085. The shared switch row updates its displayed switch state and dispatches the callback at lines 1460–1520. Relevant panes are rerendered after operations that alter their contents.
- `LocationActivity.java`: commits zone, published location, prayer-zone snapshot, weather station/coordinates and Azan schedule before returning with `RESULT_OK`.
- `WeatherActivity.java`: mood changes call `populateUi()`; station selection triggers weather sync with `populateUi()` as completion callback.
- `UserSwitchDialog.java`: profile operations use explicit callbacks; Settings profile list/sidebar refresh is handled by the caller and Home performs its regular `onResume()` refresh.
- `StandbyPhotoStore.java`: D01-F11 remains relevant: uploaded photo metadata may be saved while the subsequent photo-mode commit fails. The upload-success contract is assigned to D05/D07; D02 does not mask or duplicate that repair.

## 5. Findings and disposition

**D02-F01 — CONFIRMED / FIXED (MEDIUM): StandBy mode toggle ignored persistence failure.** `StandbyPhotoStore.setPhotoMode()` returns the synchronous `commit()` result, but `MainActivity.setStandbyPhotoMode()` previously ignored it, refreshed the screensaver and emitted a success message unconditionally. On a failed write, the setting could remain unchanged while the UI had already claimed success. The setter now checks the result, leaves the live screensaver untouched on failure, emits a localized error, and returns the actual success state. The Settings pane's existing rerender reads the persisted value, restoring the switch to the real state. This does not change preference keys, storage ownership, or saved user data.

The other inspected in-app settings mutation paths use explicit owner callbacks and direct updates to dependent live views; separate-Activity changes are reconciled on Home return through `onResume()` or an explicit completion callback.

A global `SharedPreferences.OnSharedPreferenceChangeListener` or generic event bus is not justified by the evidence. Adding one would duplicate existing state ownership, introduce listener lifecycle/leak and ordering risks, and make the UI react to storage changes without knowing the semantic operation or whether it succeeded.

**Still open, owned by D05/D07:** D01-F11 — StandBy upload can report success even if photo-mode persistence fails after collection metadata has been saved. D05/D07 remain responsible for the atomic/failure-aware upload correction and regression tests. Do not alter C07's unrelated upload/session hardening.

## 6. Guardrails and deferred acceptance

- Preserve D01's store/key ownership and profile/device scope.
- Do not use a generic preference listener as a substitute for owner-level update logic.
- A callback must only present a successful state after its state mutation succeeds; D01-F11 is the identified exception to be corrected downstream.
- D03 must separately verify that locked/hidden app counts and lists refresh in the open Settings pane.
- D04 must separately verify avatar/profile propagation across all surfaces, including upload completion and profile switching.
- F-stage must verify actual remote interaction and visible updates on the target Android TV, plus API 30 and API 33+ behavior where applicable.
- Process death, reboot, backup/restore and data recovery are D06/F-stage concerns, not proven by this source audit.

## 7. Verification

- Parent source: D01 exact head `cd710681099faece14905faa244f57245bae84d1`.
- Inspected Settings callbacks, MainActivity owner setters and lifecycle refresh, profile/child-mode callbacks, LocationActivity, WeatherActivity, StandbyPhotoStore and wallpaper update paths.
- Targeted application change: `MainActivity.setStandbyPhotoMode()` now propagates commit failure correctly; localized error string added to default, Indonesian and English resources.
- No broad state refactor or data migration was performed. Exact-head CI is required for this branch; CI does not substitute for deferred device acceptance.
