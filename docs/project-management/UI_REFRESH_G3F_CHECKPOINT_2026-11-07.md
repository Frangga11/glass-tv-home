# UI Refresh G3F — Global Settings Checkpoint

**Document:** GTVH-UIR-G3F-CHK-001  
**Date:** 7 November 2026  
**Status:** COMPLETE / SOURCE-HOST QC PASS  
**Device acceptance:** PENDING / NOT RUN

## 1. Exact source position

- Repository: `Frangga11/glass-tv-home`
- Owner branch: `feature/g3f-global-settings`
- Exact HEAD: `455b619c3eefe417ac0152a51f3cb12478a8f4a9`
- PR #71: [G3F — Global Settings](https://github.com/Frangga11/glass-tv-home/pull/71) — OPEN / DRAFT / UNMERGED.
- Base: `feature/g3e-wifi` @ `9125043e24b8f13232e41193ba5fe10e1821f7cd`.
- G3E PR #70 remains OPEN / DRAFT / UNMERGED and unchanged.
- Canonical design reference: G1 V6 prototype, V06.

## 2. Completed result

- V06-aligned header, brand/subtitle, global search and read-only Wi-Fi status pill.
- Persistent left category navigation and right detail pane, retaining all seven settings categories.
- Active profile identity/status summary backed by existing ProfileManager and ChildModeManager.
- Localized control-level search across Profile, Home Screen, Appearance, Widgets & Information, Effects, Standby and Device Settings.
- Search results route to the owning category and focus the selected control using stable IDs or content-description fallback.
- Legacy saved-focus path migration accounts for Search moving from the sidebar to the header and the profile summary insertion.
- Dialog dismissal restores the invoking control; external Android Settings return refreshes the existing device state and network status.
- Existing MainActivity/E01 network-state owner reused. No duplicate network subsystem, radio switch or system-setting mutation.
- Lifecycle references are weakly held and cleared on dismissal/host destruction.
- Corrected profile summary order to follow the page title; regression test added.

## 3. Verification

- Exact-head CI **#1376 / `42348837445`** on the exact HEAD above: both required jobs SUCCESS.
- Lists/tools/docs: **134 Python tests PASS**.
- App and installer `testReleaseUnitTest`: PASS.
- App and installer `assembleRelease`: PASS; BUILD SUCCESSFUL.
- Release lint-vital: PASS.
- Search-catalogue resource parity: 77 references present in default, Indonesian and English resources.
- APK diagnostic: **1,620,193 bytes** (1,582 KB), +2,820 bytes / approximately 0.17% vs G3E. Informational under D-023; no artifact published.
- Detailed audit: [UI_REFRESH_G3F_AUDIT_2026-11-07.md](UI_REFRESH_G3F_AUDIT_2026-11-07.md).

## 4. Final status and boundary

- **G3F SOURCE-HOST QC: PASS / COMPLETE.**
- **DEVICE ACCEPTANCE: PENDING / NOT RUN.** Physical Android TV/Mi TV Stick, remote D-pad/Back, OEM Settings handoff, overscan, 1080p/4K, daylight/glare and large-text acceptance remain separate.
- No APK publication/installation, ADB, physical-device mutation, merge, rebase, cherry-pick, force-push, integration/main change.
- PR #71 remains OPEN / DRAFT / UNMERGED; base G3E remains unchanged.

## 5. Next work position

G3A–G3F source-host surface work is complete at their individual recorded gates. The next step is a separately scoped G4 integration/reconciliation plan and combined-tree audit, subject to explicit approval. Do not merge or retarget the stacked PR chain as part of this checkpoint. Physical device acceptance remains a separate authorized gate.

**Resume rule:** Start from G3F exact HEAD `455b619c3eefe417ac0152a51f3cb12478a8f4a9`, PR #71 OPEN / DRAFT / UNMERGED, and this checkpoint. Do not repeat completed G3F work without new reproducible defect evidence.
