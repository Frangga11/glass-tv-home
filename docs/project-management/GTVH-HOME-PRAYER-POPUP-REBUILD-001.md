# GTVH-HOME-PRAYER-POPUP-REBUILD-001

Date: 11 November 2026 (WIB)
Repository: `Frangga11/glass-tv-home`
Working branch: `rebuild/home-prayer-popup-integrated-001`
Status: REBUILD COMMITTED — CI IN PROGRESS / RENDERED QA / DEVICE ACCEPTANCE PENDING
PR: #135 — OPEN / DRAFT / UNMERGED
Latest implementation code HEAD: `728ffdb1a769fc2effa286f30f49b17609527338`
Latest contract-test HEAD: `acbd0bedcaa9e08410a3f6d17ff9e0820afcf1b2`
CI-validated source/test head: `acbd0bedcaa9e08410a3f6d17ff9e0820afcf1b2`
Exact branch head will advance with this checkpoint documentation commit.

## 1. Trigger and owner direction

Rebuild the Home Prayer Schedule popup from the latest cumulative integration checkpoint, using PR #129 as the Dock/DPAD compatibility reference. PR #134 is historical implementation reference only; it is not the base branch.

Owner explicitly authorized starting feature work from the latest cumulative checkpoint despite the checkpoint's earlier hold on additional feature work. This authorization does not waive CI, integration, or physical-device acceptance gates.

## 2. Baseline verification

- Baseline branch: `integration/glass-tv-home-cumulative-2026-11-11`
- Exact baseline HEAD: `6f8251ab296ca2ced00066cbe04837c0a61677f0`
- Working branch created directly from that exact SHA: `rebuild/home-prayer-popup-integrated-001`
- PR #129 source HEAD: `a87b7af2c60f79d4d83d1011ec8a6ba7f007f015`
- PR #129 merge commit: `f24169bcd02ac24f83d9c6ac61fd5d2e0832ea25`
- Compare PR #129 source HEAD -> cumulative integration branch: 9 commits ahead, 0 behind.
- Compare exact baseline SHA -> cumulative integration branch: identical (0 ahead / 0 behind).

The cumulative integration checkpoint identifies itself as a temporary integration candidate, not a canonical release baseline. It contains PR #129 ancestry and selected follow-on reconciliation:
- PR #130: closed/unmerged; its only changed file is the Dock focus integration contract test. The current cumulative tree contains the same test-file blob (`be34c2a8e1e969d98b43175aee9c0c8d79fabc5d`), so the test assertions are present without merging PR #130 itself.
- PR #131: closed/unmerged; selected burst-serialization and row-scroll-coalescing behavior was ported directly onto the cumulative candidate.
- PR #132: closed/unmerged; stable Dock anchor lineage was audited as already represented by the permanent bottom-margin anchor and translation-only motion.
- PR #133: closed/unmerged; remaining PR #123 changes were audited and superseded by the later semantic DPAD intent and shared focus model.
- PR #124 remains open/unmerged and divergent against PR #123.
- Exact latest integration CI and integrated physical Mi TV Stick acceptance were still pending at checkpoint time.

The branch is therefore an owner-authorized working baseline for this isolated popup rebuild, not a declaration that the cumulative integration candidate is fully accepted.

## 3. PR #134 audit and reuse decisions

Historical PR #134:
- Branch: `feature/home-prayer-popup-compact-azans-001`
- Historical HEAD: `936e153f21fe9c122b1998d2cf4075ebf34f6a3b`
- It is not the working branch base.

Reusable decisions:
- Dedicated in-memory Prayer popup view tree.
- Existing `PrayerTimes.getFullSchedule()` as read-only schedule source.
- Existing `Azan` preferences, master switch, scheduling, and five sound choices; the existing per-prayer preference model is not altered or surfaced as extra timetable controls.
- Existing `AzanActivity` test-audio route.
- Existing `GlassDrawable`, `Backdrop`, and Glider companion treatment.
- Silver/neutral glass surface with Islamic-gold next-prayer emphasis.

Rebuilt or changed:
- Panel dimensions and content hierarchy.
- Compact stacked next-prayer summary matching the video reference: caption, prayer name, and time aligned vertically at the left.
- Reduced schedule cell and control row heights.
- Safe-area measurement before placement.
- Right-center placement with explicit 14dp safe-edge margin.
- Focus graph, initial-focus retry, and directional navigation.
- Removed focus-time `glideToMenuRow()` calls from this fixed, no-scroll panel to prevent content translation/clipping.

Not reused:
- Fixed 430 x 500dp panel cap as the layout's controlling height.
- Historical focus graph and unbounded assumptions about content fit.
- Any unrelated PR #134 changes outside the Prayer popup path.

## 4. Video observation

Input: owner-provided `1000608935.mp4`, 31.23 seconds, approximately 30 FPS, 1080 x 1920 portrait recording of a TV.

Observed sequence:
- Popup is visible during the opening and middle portion of the recording.
- The popup includes the next-prayer panel, six schedule cells, and lower Azan settings controls.
- The lower Azan settings area is visually dense and needs a measured safe-area fit check.
- A navigation/focus indicator is visible at different positions as the user navigates.
- Around 23–25 seconds, the popup disappears and an ADZAN Maghrib overlay is shown.
- The Home surface is visible again afterwards.

### Proven popup/overlay lifecycle path

The source audit establishes why the popup disappears when another Activity, including AzanActivity, comes to the foreground:

- MainActivity.onPause() calls closeMenu(false) after Home yields its window.
- closeMenu(false) removes the in-Home popup and intentionally does not keep it displayed above another Activity.
- AzanReceiver launches AzanActivity directly and also posts a FullScreenIntent notification. AzanActivity is declared singleTask, so a second instance is not established by source alone; the actual delivery sequence still requires device logs.
- AzanActivity finishes on Back/Enter or when its audio flow completes; Home then resumes.

This means popup dismissal during the Azan overlay is consistent with the app's existing "nothing of ours stays on screen over another app" lifecycle contract. The defect to address is safe focus restoration to the popup's original Home anchor after Home resumes, not keeping the popup layered above AzanActivity.

The rebuild now remembers the Prayer popup caller when Home pauses, closes the popup through the existing lifecycle path, and requests focus back on that same visible/focusable caller after Home resumes. This is popup-specific state; shared closeMenu(), AzanReceiver, Azan, and AzanActivity remain unchanged.

The recording alone does not establish whether the Maghrib overlay came from a scheduled alarm or another trigger, nor whether the FullScreenIntent delivered a second intent to the singleTask Activity. Those details remain pending device logs.

## 5. Rebuild design

- Vertical, right-center panel.
- Width target: 430dp, constrained by actual safe width.
- Height: measured from the complete view tree, constrained by safe height; no fixed 500dp height cap.
- Existing GlassDrawable background is applied before measurement, then panel padding is set, so material insets cannot silently change the measured frame.
- Header: WAKTU SHOLAT and dynamic prayer-zone name with Pagar Alam elevation context (±800 mdpl).
- Compact next-prayer horizontal card.
- Six read-only timetable cells in 2 columns x 3 rows; the reference shows prayer names and times only, so per-prayer Azan toggles/status text were removed from the cells.
- Azan controls: master toggle, sound selector, Test Sound.
- Small footer hint.
- No ScrollView and no internal scrolling.
- Neutral/Silver Glass card treatment, existing GlassDrawable, Islamic-gold next marker; no Cyan accent token added to content.
- Panel right margin includes system safe inset plus 14dp visual breathing space.

Current target content-height budget (dp, before runtime measurement):
- Header 34
- First divider 4
- Next prayer 66
- Schedule heading 24
- Schedule rows 3 x 45 = 135
- Second divider 4
- Master Azan 40
- Sound selector 44
- Test Sound 44
- Footer 17
- Panel vertical padding 16
- Estimated total: 428dp

Runtime measurement remains authoritative; this budget is not a claim of rendered device acceptance.

## 6. Implementation

Changed:
1. `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java`
   - Replaced legacy shared Inputs-layout Prayer popup with a dedicated view tree.
   - Reused existing prayer/azan data and actions.
   - Added measured safe-area placement.
   - Added popup-local DPAD focus graph, visible focus outline, bounded initial-focus retry, Back focus restoration, and popup-caller restoration after Azan/external Activity lifecycle return.
   - Preserved the existing Test Sound Activity route.
   - Kept changes out of shared `showMenu()`, `popup_inputs.xml`, `item_input.xml`, `GlassDrawable.java`, `PrayerTimes.java`, and `Azan.java`.

2. `tests/test_home_prayer_popup_rebuild_contract.py`
   - Added source-contract checks for scope, layout, safe-area measurement, existing data/action reuse, Silver Glass, and DPAD behavior.

3. `docs/project-management/GTVH-HOME-PRAYER-POPUP-REBUILD-001.md`
   - This workstream checkpoint.

## 7. Validation

- Python source-contract and repository suite: PASS, CI run #2053.
  - 305 tests run in 0.290s.
  - 11 Prayer popup rebuild contract tests all passed.
  - Overall result: `OK (skipped=1)`.
- Android unit tests: PASS, CI run #2053.
  - `app:testReleaseUnitTest`: completed successfully.
  - `installer:testReleaseUnitTest`: completed successfully.
- Release build: PASS, CI run #2053.
  - `app:assembleRelease`: completed successfully.
  - `installer:assembleRelease`: completed successfully.
  - Gradle result: `BUILD SUCCESSFUL`.
- Exact validated source/test HEAD: `acbd0bedcaa9e08410a3f6d17ff9e0820afcf1b2`.
- Exact APK artifact: `glass-tv-home-cumulative-integration-127-178442ff3d9ea54e7a8a8ea5b415ae815fcf8e98` (CI run #2053, artifact ID `13811722753`).
- APK size: 1,635,017 bytes.
- APK SHA-256: `336d40849510f2e1b0256081829662304a2603002677c7d2bfaa453ca15e88b5`.
- Baseline integration APK from CI run #2010: 1,632,369 bytes; rebuild delta: +2,648 bytes (+0.16%).
- The workflow emitted its non-blocking historical-size warning because 1,635,017 bytes is above the 1.5 MiB reference (1,572,864 bytes) by 62,153 bytes. The size diagnostic is informational, not a CI failure.
- Rendered UI inspection at 1920x1080: pending.
- Mi TV Stick Android TV 11 physical acceptance: pending.
- Popup dismissal cause: source-proven as MainActivity.onPause() -> closeMenu(false); this is intentional overlay lifecycle behavior.
- AzanReceiver direct-launch plus FullScreenIntent delivery ordering: runtime confirmation pending.
- Azan receiver/overlay trigger timing and singleTask onNewIntent delivery: pending device logs.

The final APK is built and its checksum is recorded. It has not been installed or tested on a physical device.
 
## 8. Regression boundaries

No intended edits to Home Screen layout outside the Prayer popup, wallpaper, Hero Shelf, Islamic Shelf, Dock/Grid, Home Header, Weather/AQI, Wi-Fi, microphone, app inventory, Settings, PrayerTimes calculations/data source, Azan persistence/preferences/scheduling, audio assets, or AzanActivity behavior.

The shared `closeMenu()` is called only through its existing API for popup dismissal and focus restoration; its implementation is unchanged.

## 9. Open issues / next steps

1. Run the final contract test and full Python regression suite on the reference-aligned revision.
2. Run exact-HEAD CI and Android release build for the reference-aligned revision.
3. Inspect the complete source diff for unrelated changes.
4. Verify actual measured popup dimensions and clipping using rendered emulator/screenshot evidence.
5. Verify popup caller focus restoration after scheduled Azan overlay and after Test Sound returns.
6. Correlate AzanReceiver direct launch and FullScreenIntent delivery using device logs; do not infer duplicate Activity creation from source alone.
7. Prepare APK and checksum if build succeeds.
8. Provide device test instructions; do not claim physical acceptance until the owner tests on Mi TV Stick.
9. Keep PR Draft / Unmerged until all acceptance gates are met.


## 10. Owner revision — compact header, 30% narrower panel, 24-hour clock, and DPAD matrix

Date: 11 November 2026 (WIB)

Based on the owner's Mi TV Stick photo and explicit revision instructions:

- Header remains at the top-right of the popup, but is now informational only and excluded from the DPAD focus graph.
- Removed the header's pill/capsule treatment. The title stays left; location and altitude sit at the right with the same location and mountain icon assets used by the Home location capsule, a thin divider, left-aligned text, and a fixed `800 mdpl` value (no ± sign).
- Initial DPAD focus now starts at Subuh, the first schedule cell.
- Reduced panel target width from 430dp to 301dp (30% reduction); all child rows/cells are measured within the narrower width. Height remains content-measured against the TV safe area and vertically centered.
- Retained the right-side safe margin so the panel sits closer to the right screen edge while remaining centered vertically.
- Schedule and next-prayer time strings are normalized to Indonesian-style 24-hour `HH:mm` display.
- Azan master control now has a visible Android TV switch; row focus/OK toggles the switch and reuses the existing Azan preference + scheduling path.
- Suara Azan remains a click-to-cycle control; Test Sound retains its existing AzanActivity route.
- Timetable layout remains a 2-column × 3-row focus grid. Directional selection remains geometric/lane-constrained so Subuh ↔ Terbit, Subuh ↓ Dzuhur, Terbit ↓ Ashar, Dzuhur ↓ Maghrib, Ashar ↓ Isya, and the lower schedule row ↓ Azan controls can be traversed without diagonal jumps.

Changed files remain limited to:
1. `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java`
2. `tests/test_home_prayer_popup_rebuild_contract.py`
3. This checkpoint document.

Validation status for this owner revision:
- Source and contract assertions updated.
- Exact-HEAD CI, Android release build, rendered 1920×1080 inspection, and physical DPAD verification are pending.
- Do not claim runtime PASS until the new exact branch HEAD has completed CI and the owner has tested the APK on the TV.
- PR #135 remains OPEN / DRAFT / UNMERGED.


### Revision refinement — right-edge placement
- The owner requested the popup to sit slightly closer to the right edge. The right margin was reduced from 26dp to 14dp, in addition to the calculated system safe inset.
- Exact code commit: `26b1388b9535be50d8325f056626929132a71785`.
- Contract assertion for the 14dp margin: `92a50617647dbf382086034d2c99fa5134750ba1`.
- Exact current branch HEAD is the documentation commit that follows this entry; exact-HEAD CI is pending.


## 11. Owner refinement — header hierarchy and proportional compact sizing

Date: 11 November 2026 (WIB)

Owner-directed refinement applied on the existing PR #135 branch:
- Header row height increased to 33dp. “WAKTU SHOLAT” remains left aligned; a thin vertical separator now divides the title from the right-side information group.
- Location and elevation are stacked vertically at the upper-right. Both icon/text pairs are left-aligned; elevation uses a slightly smaller 8sp type size.
- Next-prayer capsule height increased to 60dp; its existing stacked caption/name/time hierarchy is retained for proportional vertical alignment.
- Six prayer timetable capsules use fixed 125dp widths and are horizontally centered as a two-column grid.
- Popup target width reduced to 276dp so the 2 × 125dp timetable capsules plus their 4dp inter-cell spacing fit within the 258dp inner content width with a small balanced breathing margin.
- Azan master control now displays a textual state with a status dot: green “● AKTIF” when enabled and silver “● NONAKTIF” when disabled. The row remains the single toggle action and continues to call the existing Azan preference/scheduling path.
- Sound selector and Test Sound behavior remain unchanged.

Scope remains limited to the Prayer popup layout and its contract tests. No Home layout, prayer calculation, Azan scheduling implementation, or sound assets are intentionally changed. This is source-level implementation only; exact-HEAD CI, rendered inspection, and physical Mi TV Stick acceptance remain required. PR #135 stays OPEN / DRAFT / UNMERGED.
