# GTVH-HOME-LAYOUT-FIX-001 — Cross-audit and consolidated repair

Date: 8 November 2026  
Target: Mi TV Stick / Android TV 11 / 1920×1080 landscape  
Status: TECHNICAL REPAIR IN CI; PHYSICAL DEVICE ACCEPTANCE PENDING

## Trigger and evidence
Owner's latest physical-TV photograph reports two defects: Dock/application row icons visually stack or fail to occupy distinct slots, and the next application row appears as a floating row around the middle of the screen. The photo is treated as a failed visual acceptance signal, not overridden by prior CI results.

## Cross-check of stacked PR chain
- PR #74: `fix/home-dock-visual-parity-v2`, base `feature/g4-parent-pin-hardening`.
- PR #75: `fix/home-dock-alignment-row-scroll`, stacked on PR #74.
- PR #76: `fix/home-global-layout-bounds`, stacked on PR #75.
- All three are OPEN / DRAFT / UNMERGED. Their exact-head CI does not establish physical visual acceptance.
- To avoid waiting on a stacked review/merge dependency, a consolidated branch `fix/home-layout-consolidated-001` was created from the cumulative PR #76 tree and a single Draft PR #77 was opened directly against `feature/g4-parent-pin-hardening`. PRs #74–#76 were not merged, closed, or force-reset; they remain preserved as audit history until PR #77 is reviewed.

## Source findings and corrective changes
1. `DockAdapter.onCreateViewHolder()` previously calculated holder dimensions from the parent's width at creation time only. A RecyclerView can be created before its final measured width is available, and its width can later change. That can leave root slot/card/name dimensions stale. PR #77 centralizes holder geometry in `applyItemGeometry()`, reapplies it during bind using the latest measured host width, and notifies/rebinds after the Dock host's layout width changes. The layout listener posts the notification after layout to avoid notifying RecyclerView while it is computing a layout.
2. Grid focus restoration previously retried for four frames, then scrolled and checked only one subsequent frame. A delayed callback could also steal focus after a newer DPAD target or after focus returned to the Dock. PR #77 adds a monotonically increasing request generation, invalidates pending Grid callbacks when Dock focus is requested, and uses a bounded post-scroll layout retry.
3. The one-row overflow viewport and top-controls safe-frame changes from PRs #75/#76 are retained. No profile, weather/AQI, prayer, or settings business logic is changed.

## Files changed by PR #77 over cumulative PR #76
- `launcher/app/src/main/java/com/gathin/tvhome/DockAdapter.java`
- `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java`
- `launcher/app/src/test/java/com/gathin/tvhome/DockLayoutModelTest.java`
- `.github/workflows/ci.yml` (publish APK for PR #77)

## Exact source revision
- Branch: `fix/home-layout-consolidated-001`
- Head: `bd789b6b180b6668f6d60904283ca559633d7fb1`
- Consolidated PR: https://github.com/Frangga11/glass-tv-home/pull/77
- CI run: https://github.com/Frangga11/glass-tv-home/actions/runs/42405457829
- CI run #1453 was observed queued/in progress at documentation time. No pass is claimed until both jobs complete on this exact head.

## Verification plan
- CI: Python docs/contracts; Android unit tests; release APK build; APK ZIP/integrity/size/SHA-256.
- Geometry: widths 1280, 1696, 1712, 1920px; capacities 4, 5, 6; icon counts 1..capacity.
- Device: Home initial; Dock first/last focus; DPAD Down through first/middle/last rows; DPAD Up; list refresh; app count sparse/dense; quick toggles; screen bounds; static clock/weather/prayer composition.
- Capture before/after photos/video on the actual Mi TV Stick. Emulator/CI is not physical acceptance.

## Acceptance boundary
Technical status remains PENDING until exact-head CI is complete. Physical visual and remote acceptance remains PENDING until the owner tests the APK on Mi TV Stick. No merge, APK installation, ADB, default-launcher change, or device setting mutation has been performed.
