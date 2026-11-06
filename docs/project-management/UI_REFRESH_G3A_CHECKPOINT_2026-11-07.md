# UI Refresh G3A Checkpoint — 7 November 2026

**Program:** GTVH-UIR-002 v2.0  
**Work package:** G3A — Home Screen & Independent Dock  
**Disposition:** COMPLETE — SOURCE/HOST QC PASS  
**Feature branch:** `feature/g3a-home-independent-dock`  
**Exact tested HEAD:** `87302f52de844954392bac5438cbe82ec5d14a30`  
**Pull request:** [#66](https://github.com/Frangga11/glass-tv-home/pull/66) — OPEN / DRAFT / UNMERGED  
**Dependency:** G2 branch `feature/g2-shared-ui-foundation-repair` @ `40fa0715bdbcd324042927fbc56ec19f91d1947a`; PR #65 remains OPEN / DRAFT / UNMERGED.

## 1. Delivered scope

- Independent dedicated DockAdapter and AppAdapter projections from the authoritative app inventory; Dock is no longer rendered as the first row of the app Grid.
- Stable component identity for Dock membership/order, focus restoration and app lifecycle.
- Horizontal one-row Dock and vertical one-column rail.
- Capacity 4/5/6, default 5; app Grid remains four columns regardless of Dock orientation/capacity.
- Persisted explicit pin order and unpin exclusions; missing/duplicate identities normalized; overflow pinned apps remain accessible in Grid.
- Empty Dock/Grid, all-pinned and full-Dock cases handled.
- D-pad navigation between Dock/Grid, region-aware move mode, reorder, cross-region pin/unpin, focus restoration and reload behavior.
- Home Settings controls for Dock orientation and capacity, with localized strings across supported resource sets.
- Geometry, tray, focus glider/ambient focus and first-frame readiness behavior implemented.

## 2. Verification evidence

- Exact-head GitHub Actions run #1296 / run ID `42266095885`: SUCCESS on exact HEAD above; both required jobs passed.
- Launcher unit tests, release validation APK build and lint-vital: PASS.
- Relevant source/test coverage reviewed for adapter separation, stable identity partitioning, pin order, overflow, missing/duplicate apps, persistence, orientation/capacity geometry, D-pad transitions, focus restoration, empty/full states and regression behavior.
- Previous CI failures (navigation focus persistence contract, missing locale keys, Catalan apostrophe escaping) were corrected before the successful exact-head run.
- APK diagnostic: 1,589,825 bytes (1,552 KB). This is informational under D-023; no required assets/features were removed to chase the former 1.5 MiB threshold.
- PR #66 description records the exact-head verification and boundaries.

## 3. Governance and verification boundary

- PR #66 remains OPEN / DRAFT / UNMERGED. G2 PR #65 remains OPEN / DRAFT / UNMERGED.
- No merge, rebase, cherry-pick, force-push, integration-branch change, APK publication/installation, ADB action or device mutation.
- No physical Android TV, remote-control or OEM overscan test was performed. No device PASS is claimed; those remain separate acceptance work.
- No new product decision was introduced. Existing G1 visual authority, G2 shared UI contracts, D-020 Dock architecture and D-023 APK-size policy remain in force.

## 4. Exit and next work

**G3A:** COMPLETE / SOURCE-HOST QC PASS.  
**Next authorized work package:** G3B — Weather & AQI V4, following the approved G3 sequence.  
G3B has not been started by this checkpoint. Preserve all existing branch and no-merge boundaries.

## 5. Resume protocol

Start G3B with an evidence-based audit of the exact owner branch, current integration dependencies, approved Weather V4 metric/state contract, shared G2 components, data/source/cache ownership and existing tests. Then analyze, plan, implement, test, repair/retest, QC and document. Do not reopen G3A absent new evidence.
