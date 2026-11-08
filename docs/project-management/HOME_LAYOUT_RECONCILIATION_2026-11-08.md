# Home Layout Reconciliation — 8 November 2026

## Status
Implementation in progress on `fix/home-balanced-frame-001`, stacked on PR #77 (`fix/home-layout-consolidated-001`). PR #79 is draft and must not be merged before the dependency chain and owner device verification.

## Owner-approved constraints
- Header navigation order: **Beranda → Profil → Pengaturan**. Do not reorder.
- Preserve existing Home shelves, wallpaper metadata, daily ayah, prayer schedule, weather/AQI feature, app inventory, media card, Dock glass, profile/settings actions, and the G3A/G3B/G4 foundations.
- Change only the reported defects and the explicitly requested layout balance.
- Horizontal Dock rests on its first row. App-grid overflow is hidden at rest and revealed by DPAD Down; returning focus to a Dock item collapses overflow.

## Cross-audit findings
1. PR #78 / CI #1500 was built from the old `main` baseline and did not include the newer G3A–G4 / PR #77 Home layout chain. Its APK is not a valid candidate for this correction.
2. PR #79 is based on the consolidated PR #77 branch, not old `main`.
3. The default wallpaper asset `launcher/app/src/main/assets/kabut-pagi-dempo.jpg` exists in the current source tree (230,997 bytes). Startup and category selection both stage this asset into app-private storage before applying it.
4. Header navigation is now a dedicated centered pill with owner-approved order. Width is computed between measured clock/date content and the right-side controls to avoid overlap.
5. Location and weather/AQI controls share the same left content edge. Pagar Alam has a source entry with 800 m altitude metadata.
6. Home was previously rendering a separate lightweight `HomeWeather` cache before the full `WeatherAirQuality` cache. That allowed the Home line to show temperature only even when the G3B dashboard cache contained AQI. The Home line now reads the same location-scoped G3B formatter/cache as the Weather dashboard; unavailable AQI is explicit, not silently omitted.
7. A single 34dp content safe frame is applied to top/left/right and Dock tray bottom. At the tested 1920×1080 / density 320 device mapping (2 px per dp), reducing the earlier 48dp frame to 34dp moves the Dock tray down by 28 px (approximately 1 cm) while keeping all four edge margins equal.
8. The row-2 requirement is covered by PR #77's existing overflow behavior: grid hidden at rest, exposed only on DPAD Down, and collapsed when focus returns to a Dock tile.

## Files changed in PR #79
- `activity_main.xml`: centered navigation, location pill, shared 34dp top/side inset, and weather pill anchored below location.
- `MainActivity.java`: header labels/measurement, focus restoration and explicit D-pad routes, equal-frame shelf placement, and one shared weather/AQI display source.
- `DockGeometry.java`: 34dp shared horizontal/vertical safe frame.
- `Location.java`: optional altitude field and Pagar Alam = 800 m.
- `NavigationState.java`: stable focus keys for location, weather and Home tab.
- `HomeHeaderGeometry.java` plus JVM tests: measured center navigation geometry.
- `DockGeometryTest.java`: equal 34dp safe-frame contract.
- Header vector/background drawables and CI artifact publishing for PR #79.

## Validation
- CI #1513 (pre-34dp final geometry): release build, unit tests, docs checks and artifact publication passed. APK exact size: 1,627,969 bytes.
- CI #1514/#1515: weather source consolidation and width adjustment passed.
- CI #1516–#1519: final 34dp geometry commits are running; do not treat them as final until exact-head CI completes.
- CI success is source/build validation only. It does not replace visual emulator inspection or owner testing on the TV.
- Do not use the previous PR #78 APK for device verification.
- No merge authorized.


## Final cross-audit and stale-focus correction — 8 November 2026

### Audit finding
A second source review found that the earlier focus-generation fix did not invalidate pending Grid focus requests when focus moved to the Dock or header. The first reveal callback also re-entered `focusTile()` without checking whether its request generation was still current. This left a narrow but real stale-callback path that could steal focus after a newer remote action.

### Correction applied
- Increment the Grid focus generation when a Dock tile receives focus.
- Increment the generation when any top/header pill receives focus.
- Increment the generation when Home pauses, invalidating callbacks queued before another Activity takes the window.
- Guard the deferred first-reveal callback against its captured generation before it can enqueue a follow-on focus request.
- Added a Python source-contract regression test covering Dock, header, lifecycle cancellation and first-reveal callback guarding.

### Source-control dependency check
- PR #77 head at audit time: `0ea3746f0304b7042a018eeebf850d759f357bf8`.
- PR #79 original base SHA: `0aeb3851ddce5deb47e3c5f96db44631b0343577`.
- Compare shows PR #79's base was one commit behind PR #77; that single intervening PR #77 commit changed the CI workflow. The workflow content on the two branches was inspected and is identical. This is a dependency/ancestry hygiene item to reconcile before merge, not evidence of a functional source conflict.
- Do not merge PR #79 before the PR #77 dependency is reconciled and the final exact-head CI passes.

### Validation boundary
- Previous PR #77 exact-head CI #1506: PASS.
- Previous PR #79 exact-head CI #1526: PASS; both CI jobs succeeded and the PR test-APK artifact step completed. This is not device acceptance.
- New source and regression-test commits require a fresh exact-head CI run.
- Mi TV Stick visual, DPAD, overscan and whole-Home regression acceptance remain NOT RUN.
- No APK installed; no ADB/device settings or launcher configuration changed.
- No PR merged; PR #79 remains Draft.

### Additional bilingual-content correction
A follow-up review found that the new altitude label used a hard-coded Indonesian suffix and a `±` symbol, which implies uncertainty even though the configured altitude is a known value. Replaced it with the localized `location_altitude_format` resource in all three resource sets: default/Indonesian use `%1$d mdpl`; English uses `%1$d m ASL`. Added a regression assertion for the resource values and the absence of the misleading approximation symbol. This correction also requires fresh exact-head CI.


## Resume focus-restoration race — 8 November 2026

### Finding
A deeper lifecycle review found a second queued focus path not covered by the earlier first-reveal callback fix: `restoreFocus()` posts a runnable after Home rebuilds its app list, but the runnable previously forced the saved Grid/Dock/header target without checking whether the user had already moved focus to a newer destination. This could let a delayed resume restore override a fast DPAD action.

### Correction
- Capture `gridFocusRequestGeneration` when queuing resume restoration.
- Skip the saved-focus request if Dock/header/other guarded focus work has advanced the generation before the runnable executes.
- Always complete `tilesReady` / `resuming` lifecycle bookkeeping and the card-prompt refresh even when the stale restore is skipped.
- Added `test_resume_focus_restore_does_not_steal_a_newer_user_focus_target` to the Python source-contract suite.

### Validation boundary
- Source commits: `eda610c85e9de4486bd3d9cd51e74f0647a56e0f` (guard) and `2033e06d3cc695cb812160f4e50261c853f7ee29` (readability cleanup); regression test commit `fc7b07abc043b26ab28455dc8dcd5b83f0238465`.
- Fresh exact-head CI for the final commit is queued/running; do not claim PASS until completed.
- This is source-level focus-race coverage only. Physical Mi TV Stick DPAD/return-from-app acceptance remains pending.


### Expanded cancellation coverage — 8 November 2026

The resume-restoration guard was strengthened after checking the full Home focus graph. The global focus-change observer now invalidates queued Grid/resume focus work whenever focus moves to any non-Grid destination (including prayer, media, popups and other controls), not only the header and Dock. This closes the remaining class of focus-steal paths while preserving focus movement within the Grid. Regression coverage now asserts this global invalidation rule.

Commits: `734eebb18b6095f6912e85def357d4630ccb1f5f` (source), `434d32afb424c4625a45979cc08d632be6443cc3` (test). Exact-head CI pending.


## Already-focused horizontal Grid restoration correction — 8 November 2026

### Finding
The horizontal branch of `focusTile()` delegates to `focusTileWhenLaidOut()`. That helper previously called `card.requestFocus()` unconditionally. If Android had already retained focus on the same Grid card during an Activity return/rebuild, `requestFocus()` could succeed without emitting a new focus-change callback. In that case the logical focus target could be correct while the glass highlight remained hidden (the vertical Grid and Dock restoration paths already explicitly handle already-focused cards).

### Correction
- In `focusTileWhenLaidOut()`, explicitly call `onTileFocused(position, card)` when the resolved card is already focused; otherwise request focus normally.
- Added Python source-contract regression test `test_horizontal_grid_focus_restore_reapplies_glass_for_already_focused_card`.
- Source commit: `c52a1cba8ea3190299bce994a52f010457dddbd4`.
- Test commit: `de4ae44c53e6f99bce2e8fc2d0694a8982a117a3`.
- Fresh exact-head CI is required; no PASS claimed yet. Physical Mi TV Stick resume/DPAD/visual validation remains pending.

### Dependency audit result
The sole PR #77-side commit after merge base is `0ea3746f0304b7042a018eeebf850d759f357bf8` (`ci(home): publish balanced-frame test APK for stacked PR 79`). PR #79 independently contains `f38b9663e2422ef793f075acc47f800200f677f7` (`ci(home): publish corrected Home layout test APK for PR 79`), with an identical patch and identical resulting workflow blob `5d271a43c5bfada87d85b144740a0074aea3e56b`. The apparent one-commit divergence is a duplicate equivalent CI change, not missing functional source. Do not cherry-pick or merge either duplicate commit solely to reconcile this. Both PRs remain OPEN / DRAFT / UNMERGED; no force-push or device action.
