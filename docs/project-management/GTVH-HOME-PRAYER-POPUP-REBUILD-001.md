# GTVH-HOME-PRAYER-POPUP-REBUILD-001

Date: 11 November 2026 (WIB)
Repository: `Frangga11/glass-tv-home`
Working branch: `rebuild/home-prayer-popup-integrated-001`
Status: IMPLEMENTATION COMMITTED — AUTOMATED VALIDATION / RENDERED QA / DEVICE ACCEPTANCE PENDING
PR: To be opened as Draft / Unmerged after source-contract review.

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
- PR #131: selected burst-serialization behavior ported.
- PR #132: redundant stable Dock anchor behavior already represented.
- PR #133: remaining PR #123 work audited and closed as superseded.
- PR #124 remains open/unmerged and divergent from PR #123.
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
- Existing `Azan` preferences, master switch, per-prayer switch, scheduling, and five sound choices.
- Existing `AzanActivity` test-audio route.
- Existing `GlassDrawable`, `Backdrop`, and Glider companion treatment.
- Silver/neutral glass surface with Islamic-gold next-prayer emphasis.

Rebuilt or changed:
- Panel dimensions and content hierarchy.
- Compact horizontal next-prayer summary.
- Reduced schedule cell and control row heights.
- Safe-area measurement before placement.
- Right-center placement with explicit safe-edge margin.
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
- Header: WAKTU SHOLAT and dynamic prayer-zone name with Pagar Alam elevation context (±800 mdpl).
- Compact next-prayer horizontal card.
- Six timetable cells in 2 columns x 3 rows.
- Azan controls: master toggle, sound selector, Test Sound.
- Small footer hint.
- No ScrollView and no internal scrolling.
- Neutral/Silver Glass card treatment, existing GlassDrawable, Islamic-gold next marker; no Cyan accent token added to content.
- Panel right margin includes system safe inset plus 26dp visual breathing space.

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

Not yet claimed:
- Python contract tests: pending execution on exact branch.
- Full repository Python suite: pending.
- Android unit tests: pending.
- Release build: pending.
- Exact-head CI: pending.
- APK size/checksum: pending.
- Rendered UI inspection at 1920x1080: pending.
- Mi TV Stick Android TV 11 physical acceptance: pending.
- Popup dismissal cause: source-proven as MainActivity.onPause() -> closeMenu(false); this is intentional overlay lifecycle behavior.
- AzanReceiver direct-launch plus FullScreenIntent delivery ordering: runtime confirmation pending.
- Azan receiver/overlay trigger timing and singleTask onNewIntent delivery: pending device logs.

## 8. Regression boundaries

No intended edits to Home Screen layout outside the Prayer popup, wallpaper, Hero Shelf, Islamic Shelf, Dock/Grid, Home Header, Weather/AQI, Wi-Fi, microphone, app inventory, Settings, PrayerTimes calculations/data source, Azan persistence/preferences/scheduling, audio assets, or AzanActivity behavior.

The shared `closeMenu()` is called only through its existing API for popup dismissal and focus restoration; its implementation is unchanged.

## 9. Open issues / next steps

1. Run the new contract test and full Python regression suite.
2. Run exact-HEAD CI and Android release build.
3. Inspect the complete source diff for unrelated changes.
4. Verify actual measured popup dimensions and clipping using rendered emulator/screenshot evidence.
5. Verify popup caller focus restoration after scheduled Azan overlay and after Test Sound returns.
6. Correlate AzanReceiver direct launch and FullScreenIntent delivery using device logs; do not infer duplicate Activity creation from source alone.
7. Prepare APK and checksum if build succeeds.
8. Provide device test instructions; do not claim physical acceptance until the owner tests on Mi TV Stick.
9. Keep PR Draft / Unmerged until all acceptance gates are met.
