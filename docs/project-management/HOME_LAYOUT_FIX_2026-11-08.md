# GTVH-HOME-LAYOUT-FIX-001 — Global Home Layout Audit & Fix

**Checkpoint:** 8 November 2026  
**Target:** Mi TV Stick / Android TV 11 / 1920 × 1080 landscape  
**Disposition:** IMPLEMENTATION IN PROGRESS — SOURCE/CI QC PENDING — DEVICE ACCEPTANCE PENDING  
**Do not merge PR #74, #75 or #76 from this checkpoint.**

## 1. Trigger and input evidence

- Owner supplied a 23.14-second, 1920 × 1080 MP4 showing the physical TV display during Home navigation.
- The video is treated as pre-fix evidence. It is not a post-fix acceptance recording.
- Visible states include an empty-looking full-width glass tray with Google Play at the far left, followed by a separate five-app row above the tray. The Home information shelves remain in approximately the same screen regions in the sampled frames.
- Physical remote events are inferred only where the visual sequence supports them; no input-event trace or device log was included with this video.

## 2. Initial findings and root-cause audit

### HOME-DOCK-PARTITION / VISUAL-001 — Dock and first app row appear separated

**Evidence:** Video frames show Google Play isolated at the left edge of the lower tray while a separate row of five app cards appears above it.

**Existing source work:** PR #74 restores Patch 4.1 Dock fill-to-capacity behavior and shared inventory projection. PR #75 corrects the duplicate horizontal inset, extends the Dock host to the Home shelf's right boundary, draws the tray across the host, and changes horizontal overflow to a one-row viewport.

**Current source baseline:** PR #75 head `c72c07f9af1ee83b43ff19b34f2e704bdc570eea`; PR #75 remains OPEN/DRAFT/UNMERGED. Exact-head CI run #1441 / `42401401988` succeeded. Its APK is only the PR #75 baseline, not the new PR #76 change.

### HOME-DOCK-GEOMETRY-002 — Dock/Grid geometry consistency

- `DockGeometry.java` is the shared horizontal geometry owner for slot width, tile width, tray padding, Grid width and one-row viewport height.
- `DockAdapter.java` uses the shared Dock geometry and the shared `AppAdapter.Holder`; the tile width derives from the five-column Grid cell model.
- Existing `DockLayoutModelTest.java` covers 4/5/6 capacities, density values 1.0/1.5/1.75/2.0, 1280/1920 viewports, centered item groups and one-row viewport sizing.
- Existing DPAD source path maps Dock Down to the first overflow row; Grid Up returns to the aligned Dock slot or previous row; Grid Down advances by one row until the last row.
- This is source/test evidence only. It does not establish visual acceptance or physical DPAD correctness.

### HOME-QUICK-TOGGLE-ALIGN-001 — Top controls translated by Dock geometry

**Severity:** High / visible-boundary defect  
**Affected controls:** Wi-Fi, microphone, profile, Settings (plus Inputs).

**Confirmed source root cause:**
- `MainActivity.alignTopButtons()` found the last Dock card and shifted the entire `topButtons` row so the Settings button's right edge followed that last Dock card's right edge.
- The method was registered as a global-layout listener on both Grid and Dock.
- Therefore the top menu's position depended on Dock item count and layout state. A short Dock could pull the whole menu far left, unrelated to the Home content safe frame.
- The existing XML already anchors `topButtons` to the end of its RelativeLayout inside `mainContent`, which has 56dp start/end padding. The dynamic Dock-relative translation was unnecessary for keeping the controls inside the safe frame.

**Minimal correction implemented in PR #76:**
- Removed both `alignTopButtons` global-layout listeners.
- Removed the Dock-relative `alignTopButtons()` translation method and its dedicated fields.
- Preserved the existing XML end alignment and 56dp safe content padding. No visual redesign or new control was introduced.
- Added `tests/test_home_layout_bounds_contract.py` to assert safe end alignment, presence of Wi-Fi/microphone/profile/Settings controls, absence of Dock-relative top-menu translation, and retention of shared Dock geometry.

## 3. Changed files

PR #76:
- `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java`
- `tests/test_home_layout_bounds_contract.py`

PR #76 source branch:
- `fix/home-global-layout-bounds`
- Latest commit at this checkpoint: `cf9d661dc7e9d0520608dc771aed2b9d75bb1a01`
- Draft PR: https://github.com/Frangga11/glass-tv-home/pull/76
- Base: `fix/home-dock-alignment-row-scroll` @ `c72c07f9af1ee83b43ff19b34f2e704bdc570eea`
- PR #76 is OPEN/DRAFT/UNMERGED.

## 4. CI and test record

- PR #76 CI run #1442 / `42403641354`: Lists/tools/docs job failed because the first version of the XML test used an unsupported ElementTree XPath expression; Android build job was still running at the time of inspection.
- Corrected the test to locate XML elements by iterating the parsed tree and matching the namespaced Android ID attribute.
- Updated source HEAD: `cf9d661dc7e9d0520608dc771aed2b9d75bb1a01`.
- Exact-head CI run #1443 / `42403684594`: IN PROGRESS at this checkpoint. No PASS is claimed until both required jobs finish successfully.
- No PR #76 APK, size or SHA-256 is available yet.
- PR #75 baseline artifact: APK 1,622,469 bytes; SHA-256 `61d5705915802e65fdecab6c7b6b91973b7187c4a276a972704d6ab1bbd242b8`. This artifact does not include PR #76 and must not be represented as the final fix APK.

## 5. Visual and device validation boundary

Not performed after PR #76:
- Mi TV Stick installation or launch.
- Cold start, resume and wallpaper transition inspection.
- Physical DPAD Down/Up through Dock, first/middle/last overflow rows.
- Focus and icon-size parity between Dock and overflow rows.
- Quick Toggle bounds and DPAD focus on the physical screen.
- 1920 × 1080 left/right edge and clipping inspection after the patch.
- Comparison screenshots/video after the patch.
- OEM overscan and real remote acceptance.

No ADB command, APK installation, launcher default change, device setting change or physical device mutation was performed.

## 6. Acceptance status

| Acceptance area | Status at checkpoint |
|---|---|
| Dock/app inventory and tray source correction | Implemented in PR #74/#75; physical acceptance pending |
| One-row overflow viewport and DPAD source path | Implemented in PR #75; physical acceptance pending |
| Shared icon/tile geometry | Existing source/test coverage present; visual acceptance pending |
| Quick Toggles remain anchored in safe content frame | Source correction in PR #76; CI pending |
| Clock/weather/location/prayer/ayah composition | No source change; post-fix visual verification pending |
| Unit/contract tests and CI | PR #76 run #1443 in progress |
| Release APK, size and SHA-256 for PR #76 | Pending CI |
| Before/after visual evidence | Before video received; after evidence pending |
| Physical Mi TV Stick remote acceptance | Pending owner test |

## 7. Risks and next actions

1. Complete exact-head CI run #1443. Fix any failure and re-run on the new exact HEAD.
2. Review full cumulative PR #74 → #75 → #76 source diff for conflicts and unintended layout side effects.
3. Verify the final APK artifact belongs to the exact PR #76 head; record APK byte size and SHA-256.
4. Produce post-fix emulator/host screenshots at 1920 × 1080 for geometric inspection, while keeping them explicitly separate from physical TV acceptance.
5. Owner performs controlled Mi TV Stick / remote acceptance. Do not change default launcher.
6. Only after all technical checks and physical visual/remote acceptance pass may the work be marked PASS. If technical checks pass but physical acceptance is still outstanding, use PENDING DEVICE ACCEPTANCE.

**Decision at this checkpoint:** PARTIAL IMPLEMENTATION / QC PENDING. No merge authorized.

## 8. Follow-up source audit — Dock host and icon centering

A second source-level review found that the earlier aggregate-centering correction applied dockRecycler.setTranslationX(dockCenterOffset). DockDecoration draws the full-width tray in RecyclerView-local coordinates, so translating the RecyclerView also translated the tray host, not only the icon group. The host's position therefore was not independent from the centering offset.

**Correction committed on PR #75 source branch**
- Center the horizontal Dock icon group with symmetric RecyclerView start/end padding; keep the Dock host translation at zero so the tray remains anchored to the Home content frame.
- Reset centering padding when switching to vertical orientation or when the Dock is empty.
- In DockAdapter, calculate horizontal tile and slot widths from the full viewport width, not the reduced content width after centering padding. This preserves icon size and slot geometry.
- Add horizontalDockPaddingCentersIconsWithoutMovingTheTrayHost() coverage for 1..capacity visible items, capacities 4/5/6 and 1280/1920 viewport widths.
- Keep the existing one-row Grid viewport and DPAD row progression unchanged.
- Extend CI artifact publication to include PR #76 so its exact-head APK can be produced.

**Latest PR #75 source state**
- Branch: fix/home-dock-alignment-row-scroll.
- Latest commit at this checkpoint: 21bc204a413fce3c527e0e8761d2016bc5dc87c6.
- Additional changed files: DockAdapter.java, MainActivity.java, DockLayoutModelTest.java, and .github/workflows/ci.yml.
- CI run #1446 / 42403799908 was started on the immediately preceding source head 78539148930bc6e3613a03db0dd3997073c725ee; Lists/tools/docs passed while Android unit tests and release build were still running. The workflow-publication update is on the newer head 21bc204...; no exact-head result or APK is yet claimed for that head.

## 9. Quick Toggles follow-up — current CI state

- PR #76 source branch remains fix/home-global-layout-bounds @ cf9d661dc7e9d0520608dc771aed2b9d75bb1a01.
- CI run #1443 / 42403684594 completed SUCCESS; both required jobs passed. The artifact step was skipped because the workflow at that time only published APKs for PR #74/#75.
- The PR #75 base branch has since advanced beyond c72c07f... with the Dock host/padding correction and artifact-workflow update. PR #76 therefore requires a fresh exact cumulative CI run before its result can be treated as current.
- CI artifact publication now includes PR #76, but no PR #76 APK, byte size or SHA-256 is available yet.

## 10. Remaining evidence and acceptance

- The video is pre-fix evidence only; no post-fix visual capture exists.
- No physical Mi TV Stick installation, cold-start/resume, DPAD, focus, Quick Toggle bounds, overscan or Home-composition acceptance has been performed.
- The video shows only Google Play in the lower tray. Source defaults to five Dock slots and fills free slots from ordered app inventory, except explicitly unpinned apps. Do not override persisted pin/unpin choices speculatively; verify the device's actual Dock inventory during controlled acceptance.
- No APK from the current exact PR #75/#76 cumulative head is available yet. The older PR #75 artifact from head c72c07f... is baseline-only and must not be used as evidence for these later source changes.
- No merge, ADB, APK installation, default-launcher change or device mutation has been performed.

**Updated decision:** IMPLEMENTATION IN PROGRESS / EXACT-HEAD CI PENDING / DEVICE ACCEPTANCE PENDING. No merge authorized.

## 11. PR #75 Dock-only APK verification and cumulative CI

- Exact source head for CI run #1446 / 42403799908: 78539148930bc6e3613a03db0dd3997073c725ee. Both required jobs completed SUCCESS, including the added Dock geometry regression test and release build.
- Artifact #13585808852, ZIP 1,422,660 bytes. ZIP integrity passed; extracted app-release.apk is 1,622,497 bytes and APK ZIP integrity passed. SHA-256: 38d818902ce3ccf800b8de5cc0428125b91b7e9c27ba81810759a53925c65d0f.
- This APK includes the Dock host/padding correction, but predates the workflow-only commit 21bc204a413fce3c527e0e8761d2016bc5dc87c6 and does not include PR #76's Quick Toggles correction. It is an intermediate Dock-only candidate, not the final cumulative Home layout APK.
- PR #75 exact latest head 21bc204... has CI #1448 / 42403925244 in progress. PR #76 exact latest head ea681545bbf356105683ef2c7da6664e29a83723 has CI #1447 / 42403880493 in progress, with the updated APK publication condition. Await those exact-head runs and verify the PR #76 artifact before calling the build gate complete.

## 8. Base reconciliation follow-up — 8 November 2026

PR #75 advanced by a workflow-only commit `21bc204a413fce3c527e0e8761d2016bc5dc87c6` that includes PR #76 in the APK artifact publication condition. To avoid a stale base and duplicate workflow change, PR #76 was rebased onto that latest PR #75 head. The workflow change is inherited from the base and is no longer duplicated in PR #76.

- PR #76 remains OPEN / DRAFT / UNMERGED: https://github.com/Frangga11/glass-tv-home/pull/76
- Base branch/head: `fix/home-dock-alignment-row-scroll` @ `21bc204a413fce3c527e0e8761d2016bc5dc87c6`.
- PR #76 head branch: `fix/home-global-layout-bounds` @ `012fba1c4012c7e4a05de292b69030c17a594d90`.
- Compare base → head: 2 commits ahead, 0 behind; only `MainActivity.java` and `tests/test_home_layout_bounds_contract.py` differ.
- The source/test head `cf9d661...` previously passed CI #1443. That run is not the final exact-head CI after rebase.
- New exact-head CI run #1449 / `42404084112` is IN PROGRESS. It will use the PR #75 inherited artifact publication workflow.
- The prior run #1447 targeted a superseded branch head and is not the current acceptance evidence.
- PR #76 APK size/SHA-256 remain pending. Physical TV/remote acceptance remains not run; no merge or device mutation.

## 12. Exact cumulative CI and final test APK — 8 November 2026

- PR #75 latest head 21bc204a413fce3c527e0e8761d2016bc5dc87c6: CI #1448 / 42403925244 SUCCESS. Both required jobs passed, including unit tests, release build and APK publication. Artifact #13586239278.
- PR #76 latest head ea681545bbf356105683ef2c7da6664e29a83723: CI #1447 / 42403880493 SUCCESS. Both required jobs passed; the Lists/tools/docs job and Launcher build/unit tests succeeded. Artifact #13586598248, ZIP 1,422,482 bytes.
- Downloaded PR #76 artifact and verified ZIP integrity. Extracted app-release.apk is 1,622,153 bytes; APK ZIP integrity passed; AndroidManifest.xml, classes.dex and resources.arsc are present. SHA-256: 12dc35e31fc2e67bcba1e0ddcee548937f5f23f9bf90d654316fc6fdc34f17c7.
- This is the current cumulative PR #76 test APK, including PR #75 Dock host/padding and row-scroll work plus PR #76 Quick Toggles safe-frame correction. It is suitable for controlled owner testing, but is not visual acceptance evidence.
- Both PRs remain OPEN/DRAFT/UNMERGED. No installation, ADB, default-launcher change or device mutation has been performed.

**Updated technical disposition:** CI / unit tests / release build / APK integrity PASS. Physical Mi TV Stick visual and DPAD acceptance PENDING. Final project status is PENDING DEVICE ACCEPTANCE, not PASS.

## 9. Current exact-head checkpoint — 8 November 2026

A further integration review found PR #75 had accumulated additional Dock host-centering changes after the earlier PR #76 branch snapshot. The first rebase attempt had overwritten those latest MainActivity changes. This was corrected by reapplying only the Quick Toggle translation removal onto the latest PR #75 MainActivity, preserving the Dock's symmetric RecyclerView padding and keeping its tray host unshifted.

- Current PR #76 head: `f085c41356e2e5f53ca13f51bc0676bdb82d350a`.
- Base PR #75 head: `21bc204a413fce3c527e0e8761d2016bc5dc87c6`.
- Compare: 3 commits ahead / 0 behind. PR #76 changed files remain only `MainActivity.java` and `tests/test_home_layout_bounds_contract.py`.
- Source inspection confirms the latest Dock padding-centering implementation is preserved; the old `dockRecycler.setTranslationX(dockCenterOffset)` is absent. The Dock-relative top-menu listeners and `alignTopButtons()` method are absent.
- Exact-head CI #1450 / `42404262057` is running. The repository job passed: 137 Python tests, 1 intentional skip. Android unit tests/release build and APK publication are still in progress.
- No PR #76 APK, byte size or SHA-256 yet. No post-fix visual screenshots/video. Physical Mi TV Stick remote/visual acceptance remains NOT RUN.
- PR #76 remains OPEN/DRAFT/UNMERGED. No install, ADB, default-launcher change, device mutation or merge.

**Current decision:** PARTIAL PASS — source correction implemented and repository contract tests pass; exact-head Android build/artifact and physical device acceptance remain pending.
