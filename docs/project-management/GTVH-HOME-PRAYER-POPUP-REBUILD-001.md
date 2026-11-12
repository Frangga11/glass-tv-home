# GTVH-HOME-PRAYER-POPUP-REBUILD-001

Date: 11 November 2026 (WIB)
Repository: `Frangga11/glass-tv-home`
Working branch: `rebuild/home-prayer-popup-integrated-001`
Status: OWNER TYPOGRAPHY REVISION COMMITTED — EXACT-HEAD CI / RENDERED QA / DEVICE ACCEPTANCE PENDING
PR: #135 — OPEN / DRAFT / UNMERGED
Latest implementation commit: `df0d9c06f387e41fe485c89656fe2589f0c22839`
Latest contract-test commit: `1eff187bcb3a261febb78fe9447894a92ea68c53`
Latest branch checkpoint before this metadata update: `54b7696aa4303ce4cf6e0a2198812eb7ad17b035`; exact-HEAD CI pending.
Branch: `rebuild/home-prayer-popup-integrated-001`. PR #135 remains Draft / Unmerged.

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


## 12. Owner refinement — Wi-Fi-aligned popup anchoring

Date: 11 November 2026 (WIB)

- The Prayer popup is now top-right anchored rather than vertically centered.
- Its right edge aligns with the right edge of the Home Wi-Fi capsule (btnWifi), matching that capsule's actual distance from the screen's right edge instead of using a separately guessed margin.
- Its top edge starts 14dp below the bottom edge of the Wi-Fi capsule, matching the Wi-Fi popup's anchored spacing convention.
- Placement is calculated in the Home root's coordinate space from on-screen anchor/root positions, with safe-inset and bottom-boundary clamping. If the Wi-Fi anchor is unavailable, the existing popup caller anchor is used as a fallback.
- This is a placement-only refinement; content, DPAD focus order, prayer data, Azan preferences/scheduling, sound selection, and Test Sound routing remain unchanged.

Exact source commit: 12a4d2a035a7b1b764fc79f27ab787b162c47eef.
Contract test commit: c82fe849bfc9d961e179bbae747b909f7ee4edfe.
Exact-HEAD CI and rendered/physical placement checks remain pending. PR #135 remains OPEN / DRAFT / UNMERGED.


## 13. Owner correction — Glass Dock right edge; Wi-Fi vertical reference only

Date: 11 November 2026 (WIB)

This section supersedes the horizontal Wi-Fi alignment described in Section 12.

- The Prayer popup's **right edge is aligned to the right edge of the Home Glass Dock viewport** (dockRecycler), not to the Wi-Fi capsule and not to a guessed screen margin.
- The Wi-Fi capsule (btnWifi) is consulted only for the **vertical top position**: the popup begins 14dp below the capsule, matching the requested top-edge location reference.
- There is no horizontal coordinate calculation from Wi-Fi, and no dependency on the Wi-Fi popup implementation.
- The change is limited to Prayer popup placement and its source-contract tests. No Dock geometry, Wi-Fi popup, Home layout, prayer schedule, Azan behavior, or other UI is changed.

Implementation commit: 8d80bad318e6b9dfebdb01f3993e739473b3d3f1.
Contract-test commit: e2af6312c5dd928aaceec55765c2c4fa5f3426f4.
Exact-HEAD CI and rendered/physical placement verification are pending. PR #135 remains OPEN / DRAFT / UNMERGED.


## 14. Owner revision — typography scale and 115dp timetable capsules

Date: 11 November 2026 (WIB)

Owner supplied the complete typography scale and requested 115dp-wide Subuh–Isya timetable capsules. Applied to the existing PR #135 branch.

### Exact typography (sp)

| Element | Size |
|---|---:|
| WAKTU SHOLAT title | 14 |
| Location | 11 |
| Elevation | 9 |
| Next-prayer caption | 10 |
| Next-prayer name | 17 |
| Next-prayer time | 16 |
| JADWAL HARI INI | 12 |
| Timetable prayer name | 11 |
| Timetable prayer time | 14 |
| Azan title | 12 |
| Azan description | 10 |
| Azan active/inactive status | 10 |
| Suara Azan title | 12 |
| Suara Azan description | 10 |
| Sound choice value | 10 |
| Test Sound title | 12 |
| Test Sound description | 10 |
| UJI action | 10 |
| Footer OK PILIH • BACK KEMBALI | 10 |

Control title/description sizes are centralized in `createPrayerPopupControlRow()`, so Azan, Suara Azan, and Test Sound receive the same consistent 12sp/10sp hierarchy. The status, sound value, and UJI action remain 10sp.

### Proportional geometry

- Timetable capsule width: **115dp** (was 125dp).
- Timetable capsule height: **36dp** (unchanged).
- Two-column pair: 115 + 4dp inter-cell margins + 115 = **234dp**.
- Popup width: **264dp** (was 276dp).
- Inner width after 9dp left/right panel padding: **246dp**.
- Remaining horizontal breathing room around the centered pair: **12dp total / 6dp per side**.
- This keeps the two schedule capsules visually centered with even side clearance instead of leaving the larger empty band produced by the previous 276dp panel.
- Footer height increased from 13dp to 18dp to safely accommodate the requested 10sp hint.
- Next-prayer card remains 60dp high; schedule cells remain 36dp high. All other existing content order, DPAD lanes, panel anchor, and interaction behavior are preserved.

### Scope and verification

Changed files remain limited to:
1. `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java`
2. `tests/test_home_prayer_popup_rebuild_contract.py`
3. This checkpoint document.

The popup panel width and cell width were changed together as a deliberate proportional pair. No changes to Home layout, Dock/Grid, wallpaper, shelves, Wi-Fi, microphone, Weather/AQI, prayer calculations, Azan persistence/scheduling, audio assets, or AzanActivity are intended.

Exact-HEAD CI, rendered 1920×1080 inspection, clipping verification, and Mi TV Stick physical acceptance are pending for this owner typography revision. PR #135 remains OPEN / DRAFT / UNMERGED. Do not claim runtime PASS until those checks complete.


## 15. Owner refinement — tighter typography and denser timetable geometry

Date: 11 November 2026 (WIB)

Owner-directed revision applied on PR #135:

### Typography (sp)
- WAKTU SHOLAT title: **13sp**.
- Next-prayer name: **15sp**.
- Next-prayer time: **14sp**.
- Each timetable prayer time: **13sp**.
- Azan, Suara Azan, and Test Sound descriptions: **9sp**.
- UJI action and OK PILIH • BACK KEMBALI footer: **9sp**.
- Existing location/elevation, caption, timetable prayer-name, section-heading, control-title, and status sizes remain unchanged unless explicitly listed above.

### Compact geometry (dp)
- Subuh–Isya capsules: **105dp wide × 31dp high** (reduced from 115 × 36dp).
- Two-column pair including 4dp inter-cell gap: **214dp**.
- Popup width: **254dp**, retaining 9dp side padding and balanced horizontal clearance.
- Header height: **30dp**; next-prayer card: **52dp**; schedule heading: **17dp**; each timetable row: **33dp**; Azan/Suara Azan/Test Sound rows: **32dp**; footer: **16dp**.
- Panel vertical padding reduced to 5dp. Location/elevation line boxes and title divider were tightened proportionally.
- All popup text now disables Android's extra font padding and uses zero extra line spacing, reducing vertical gaps between stacked labels while preserving their font sizes and hierarchy.
- Control value column reduced from 108dp to 96dp to give the text/description column more usable width inside the narrower panel.

### Scope and validation
Only the Prayer popup source, its contract test, and this checkpoint are changed. Prayer calculations, Azan preferences/scheduling, sound assets, Test Sound routing, DPAD focus order, Home/Dock/Grid, wallpaper, shelves, and Weather/AQI remain outside scope.

This is an implementation checkpoint, not visual acceptance. Exact-HEAD CI/build, rendered 1920×1080 inspection, clipping check, and Mi TV Stick Android TV 11 acceptance remain pending. PR #135 remains OPEN / DRAFT / UNMERGED.


Implementation detail clarification for Section 15:
- Timetable prayer-time values are **13sp** (not only the next-prayer time).
- The UJI value/action is explicitly set to **9sp**; the shared sound-choice value remains 10sp.


## 16. Validation correction — compact revision CI follow-up

- The first exact-head CI attempt for the 105 × 31dp revision completed the Android launcher build/unit-test job successfully but failed one source-contract assertion in the docs/tools job. The failure was a malformed expected string in the new UJI text-size assertion, not an Android compilation or runtime-code failure.
- The contract assertion was corrected to match the actual source syntax. Font-padding and line-spacing assertions were also normalized to match source tokens exactly.
- Corrected contract test commit: `8b6a826d25f5f00cd70e29d5b6a4a7cf8b8ec968`. Exact-HEAD CI must rerun and pass before build validation is complete.
- The revised vertical content budget is approximately **334dp** before runtime measurement: header 30 + dividers 8 + next card 52 + schedule heading 18 + schedule rows 101 + three control rows/gaps 98 + footer/gap 17 + panel vertical padding 10. Actual measured Android view height remains authoritative.
- PR #135 remains OPEN / DRAFT / UNMERGED. No install, merge, or final visual PASS is claimed.



## 17. Owner refinement — balanced timetable, schedule breathing room, and status capsules

Date: 11 November 2026 (WIB)

Applied to the existing PR #135 branch in response to the owner's latest visual direction:

- **Popup/timetable horizontal balance:** the six Subuh–Isya capsules now use equal-weight columns. The two columns consume the full inner panel width, with one precise 4dp inter-column gap and no extra outer cell margins. Their left and right edges therefore align exactly with the panel's 9dp inner padding, producing equal, deliberate left/right geometry instead of fixed-width cells floating inside the panel.
- **JADWAL HARI INI spacing:** heading height is 20dp with 5dp spacing above and 5dp below, separating it cleanly from the next-prayer card and the first timetable row.
- **UJI action:** raised from 9sp to **10sp**.
- **Azan status:** AKTIF and NONAKTIF now sit inside a rounded status capsule. Active uses a restrained green-tinted fill and green outline; inactive uses a neutral silver fill and outline. The capsule background and text color update together when the existing Azan master toggle changes.
- Existing popup width (254dp), 9dp panel insets, prayer data, 24-hour times, sound choices, Azan preference/scheduling path, Test Sound route, and DPAD focus graph remain unchanged.

Changed files remain limited to:
1. `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java`
2. `tests/test_home_prayer_popup_rebuild_contract.py`
3. This checkpoint document.

Source and contract-test changes are committed; exact-HEAD CI/build and rendered 1920×1080 inspection are pending. Mi TV Stick physical acceptance is also pending. PR #135 remains OPEN / DRAFT / UNMERGED; no installation or merge is authorized by this refinement.



### CI correction — owner refinement contract assertion

- CI run #2088 identified one stale source-contract expectation: the test still required the previous fixed 105dp timetable-cell width after the implementation correctly moved to equal-weight columns.
- Android build/unit-test job was still running when this docs note was prepared; the docs/tools job failed only on that stale assertion.
- Updated the test to assert the new equal-weight cell layout, 31dp cell height, 5dp heading breathing margins, 10sp UJI action, and rounded status-capsule helper/state update.
- Contract-test correction commit: `b28a3b6e66dd11e63aa0d1cb18330dde0281f7c8`.
- Exact corrected branch CI is pending. Do not call the latest refinement PASS until both CI jobs complete successfully.



## 18. Owner rework — pixel-exact popup and capsule width contract

Date: 11 November 2026 (WIB)

The owner asked to repeat the width refinement across the complete popup, not only the Subuh–Isya timetable cells. The implementation now uses one shared geometry calculation:

- Popup target width remains 254dp, constrained by the TV safe width.
- Panel horizontal inset remains 9dp per side.
- All full-width outer capsules (next-prayer card, Azan row, Suara Azan row, and Test Sound row) use MATCH_PARENT inside the same panel content width.
- The six Subuh–Isya timetable capsules divide that exact inner width into two explicit pixel widths, with a single centered 4dp gap. The right cell receives the remaining pixel after integer division, so the pair plus gap exactly equals the inner content width and cannot drift due to LinearLayout weight rounding.
- Timetable capsule height remains 31dp; the next-prayer and control-row heights, 5dp schedule-heading breathing margins, UJI 10sp, and rounded AKTIF/NONAKTIF status chip are unchanged.
- Safe-area width/height are calculated once before view construction and reused for measurement; duplicate geometry calculation at the end of the method was removed.

This is a width precision change only. No prayer data, 24-hour formatting, Azan preference/scheduling, sound choice, Test Sound route, DPAD graph, popup anchoring, Home layout, Dock/Grid, or other modules are intentionally changed.

Changed files remain limited to:
1. `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java`
2. `tests/test_home_prayer_popup_rebuild_contract.py`
3. This checkpoint document.

Exact branch CI, Android build, and rendered 1920×1080 inspection are pending. Physical Mi TV Stick acceptance remains pending. PR #135 stays OPEN / DRAFT / UNMERGED.


## 19. Owner correction — fixed 105dp timetable capsules define all horizontal widths

Date: 11 November 2026 (WIB)

This section supersedes the equal-weight/pixel-fill geometry in Section 18.

- Each Subuh–Isya timetable capsule is **exactly 105dp wide × 31dp high**.
- The two timetable capsules are separated by an exact **4dp center gap** (2dp inner margin on each capsule).
- Combined timetable content width is therefore **214dp** (105 + 4 + 105).
- Popup horizontal inset remains **9dp on each side**.
- Popup target width is therefore **232dp** (9 + 214 + 9).
- Next-prayer, Azan, Suara Azan, and Test Sound capsules remain MATCH_PARENT within the same panel content area, so each is **214dp wide** on the target layout.
- The geometry is calculated from the fixed 105dp capsule width, not by dividing the popup width. The popup is derived from the capsule pair plus the 9dp side insets.
- On a constrained safe area, the outer popup frame is clamped to the available safe width; the intended Mi TV Stick 1920×1080 layout has ample width for the exact 232dp contract.

### Validation boundary

- Source contract now explicitly asserts the 105dp capsule width, 4dp center gap, 214dp shared inner width, 232dp popup width formula, and full-width outer capsules.
- This is not yet a visual/device PASS. Exact-HEAD CI, rendered 1920×1080 inspection, clipping check, and Mi TV Stick acceptance remain pending.
- No prayer data, 24-hour formatting, Azan preferences/scheduling, sound choice, Test Sound route, DPAD graph, popup anchoring, Home/Dock/Grid, or unrelated modules are changed.
- PR #135 remains OPEN / DRAFT / UNMERGED. No install or merge is authorized.

## 20. Owner refinement — 115dp timetable, schedule-edge alignment, Wi-Fi top anchor, and Dock clearance

Date: 12 November 2026 (WIB)

### Locked geometry
- Each Subuh–Isya timetable capsule: **115dp wide × 35dp high**.
- Two-column center gap: **6dp** (3dp inner margin on each cell).
- Shared inner content width: **236dp** (115 + 6 + 115).
- Panel horizontal inset: **12dp** per side.
- Popup target width: **260dp** (236 + 24).
- Full-width next-prayer and three Azan control rows remain MATCH_PARENT within the 236dp content width.

### Placement contract
- Popup right edge aligns to the actual right edge of Home's `prayerTable` view, i.e. the visible timetable ending after the Hijri-date heading; horizontal Dock-edge alignment is superseded.
- Popup top edge is placed **10dp below the bottom edge of the Wi-Fi capsule**.
- The visible Dock top edge is a hard lower boundary. A **10dp clearance** is reserved above the Dock; available popup height is measured from the Wi-Fi anchor to that boundary before the popup is shown.
- Safe-area fallbacks remain if anchors are unavailable. The available-height clamp is defensive; rendered QA must verify that no content is clipped.

### Validation boundary
- Source-contract assertions updated for 115dp × 35dp cells, 6dp center gap, 12dp insets, 260dp derived panel width, prayer-table right-edge alignment, 10dp Wi-Fi gap, and Dock clearance.
- Exact-HEAD CI, rendered 1920×1080 inspection, clipping/height verification, and Mi TV Stick physical acceptance remain pending.
- No prayer data, Azan preferences/scheduling, sound choices, Test Sound route, Home/Dock geometry, wallpaper, shelves, Weather/AQI, Wi-Fi behavior, or DPAD focus graph are intentionally changed.
- PR #135 remains OPEN / DRAFT / UNMERGED. No APK installation or merge is authorized.