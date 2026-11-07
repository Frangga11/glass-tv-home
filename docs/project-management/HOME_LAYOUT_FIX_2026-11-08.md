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
