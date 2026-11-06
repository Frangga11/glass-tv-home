# Glass TV Home — UI Refresh Roadmap
**Document:** GTVH-UIR-001  
**Version:** 1.0  
**Status:** LOCKED BASELINE — v1.0  
**Date:** 6 November 2026

## 1. Objective
Refresh the UI without causing functional regression, navigation failure, data loss, lifecycle faults, security/privacy regressions, or unacceptable performance/compatibility changes. Use Lean UI Engineering: source-backed scope, small work units, parallel module work only after shared contracts are approved, and evidence-based gates.

## 2. Execution roadmap

| Stage | Work | Measurable output / exit gate |
|---|---|---|
| **P0 — Baseline & compatibility** | Reconcile live E02/E06 heads and dependencies against the approved integration baseline; inspect shared B01 contracts; record Android/API/device/resolution assumptions, current APK/resource budget and known defects. | Frozen source baseline + compatibility/dependency register; no stale SHA or undocumented dependency. |
| **P1 — Functional inventory** | Map every screen, tab, submenu, control, data source, action, permission, persistence point and navigation destination. Include loading, empty, success, error, offline, denied-permission, no-profile and interrupted-operation states where applicable. | 100% of in-scope UI elements mapped to owner, behavior, data, state and test. |
| **P2 — Shared design & interaction contract** | Lock typography, contrast, spacing, sizing, glass/transparency, day/normal/night behavior, dialog geometry, animation/reduced-motion behavior, focus indicators, overscan-safe margins, text scaling and localization rules. Reuse B01 tokens/dialog/focus contracts. | Approved design tokens + component rules + D-pad/focus contract; no competing global style system. |
| **P3 — Prototype & review** | Produce source-informed 1920×1080 screen simulations, including all six Dock combinations (horizontal/vertical × capacity 4/5/6), focus paths, overflow, dialogs and failure states. Review readability at TV distance and daylight glare. | Approved screen-by-screen prototype and traceability from each control to P1 inventory; no unresolved critical UX/focus issue. |
| **P4 — Controlled implementation** | Implement in small vertical slices on owner branches. E02 owns Profile/Avatar/Child Mode; E06 owns Home/Dock. Keep Dock data/view independent from the main grid. Add safe persistence/migration, cancellation, lifecycle cleanup and rollback behavior. | Each slice has scoped diff, owner, tests and acceptance evidence; existing behavior remains available until replacement passes its gate. |
| **P5 — Verification & regression** | Run unit, integration, UI/focus, persistence, lifecycle, localization, security/privacy, offline/error-state, memory/performance and compatibility tests. Validate on supported Android API levels and actual TV hardware when authorized. | Test matrix complete; zero open critical/high defects; no data loss, focus trap, crash/ANR or reproducible regression; exact-head CI green. CI alone is not device acceptance. |
| **P6 — Integrated acceptance & release decision** | Reconcile all owner changes on isolated integration candidate; audit conflicts/shared files; run cross-module regression and exact-head CI; verify APK integrity/signature, size budget, install/upgrade/rollback plan and release documentation. | One traceable candidate SHA and evidence bundle; explicit user approval before merge, publication, installation or device mutation. |

## 3. Ownership
- **UI/UX Architecture:** P0–P3; shared visual, component, responsive/overscan and focus contracts; design review.
- **E02 Profile & Avatar:** profile screens/switching, avatar picker and GIF lifecycle, Child Mode, storage, cancellation, focus and localization.
- **E06 Home Screen & Dock:** Home layout, separate Dock model/view, orientation/capacity, overflow, D-pad/focus/scroll/reorder and settings.
- **QA/Integration:** independent test matrix, regression, dependency/conflict audit, exact-head CI, artifact and rollback evidence.
- Shared B01 UI/navigation/dialog contracts remain authoritative; module teams must reuse them.

## 4. Mandatory quality checklist
1. **Visual:** no clipping/overlap; readable contrast and focus indicator; overscan-safe; day/night and locale text fit.
2. **Functional:** every mapped control has the intended action; no dead controls or misleading status.
3. **Navigation:** D-pad directional paths, Back, initial/return focus, scroll restoration and overflow are deterministic.
4. **State & data:** loading/empty/error/offline/permission-denied/interrupted states are handled; no cross-profile leakage or silent data loss.
5. **Persistence & recovery:** settings survive restart; failed writes/migrations have safe fallback or rollback.
6. **Lifecycle:** dialogs, network requests, GIF drawables, callbacks and animations cancel/stop when dismissed, detached or profile changes.
7. **Security & privacy:** permission boundaries, local storage, network/provider constraints and attribution are respected; no unintended data exposure.
8. **Compatibility:** declared Android API/device/resolution/density assumptions are tested; unsupported conditions fail safely.
9. **Performance:** measure startup/responsiveness, memory, rendering and animation behavior; compare against baseline and investigate regressions rather than guessing.
10. **Regression:** existing launcher, settings, profile, Child Mode, app grid, Dock and shared modules continue to work.

## 5. Defect severity and stop rules
- **Critical:** crash/data loss/security exposure/launcher unusable — stop the affected workstream and block integration.
- **High:** core action, navigation, profile isolation, persistence or lifecycle broken — block that slice and integration.
- **Medium/Low:** non-blocking defect with documented workaround and owner; may proceed only with explicit acceptance and tracked follow-up.
- Never waive a failed test by labeling it “visual only” without impact analysis. No speculative fixes: reproduce, locate fault boundary, fix narrowly, add regression test, verify exact head.

## 6. Efficiency rules
- Audit once and reuse valid evidence; refresh only when the source head or dependency changes.
- Work in small, reviewable slices; E02/E06 may proceed in parallel only after P0–P3 contracts and dependencies are approved.
- Prototype before production code; test each slice before combining.
- Keep source branches isolated; no blind cherry-pick/merge. Reconcile shared-file changes semantically.
- Preserve the existing APK size ceiling and narrow recorded headroom; measure before adding assets/dependencies.
- Maintain one traceability chain: requirement → UI element → owner → change/commit → test → evidence → decision.

## 7. Authorization boundary and current status
This roadmap is locked as the governing UI Refresh baseline. Locking the roadmap does not authorize production implementation, merge, changes to integration/main, APK publication/installation, ADB, HOME/default-launcher changes or physical TV mutation. E02 (#62) and E06 (#63) remain Draft/open/unmerged and **BELUM FINAL** according to the last recorded audit. Reconcile their live heads before execution. G03 physical Mi TV Stick acceptance remains a separate explicit gate.

## 8. Approval gates
- P0–P1: baseline and complete functional map accepted.
- P2–P3: shared design/interaction contract and prototypes approved before implementation.
- P4: each module slice passes its own acceptance checklist.
- P5: test matrix and exact-head CI pass; device checks are separately evidenced.
- P6: integrated candidate, rollback plan and evidence bundle reviewed; user explicitly authorizes release actions.

**Final rule:** “UI selesai” means visual, functional, navigation, state, lifecycle, security, compatibility and regression criteria all pass—not merely that the screen looks correct or CI is green.
