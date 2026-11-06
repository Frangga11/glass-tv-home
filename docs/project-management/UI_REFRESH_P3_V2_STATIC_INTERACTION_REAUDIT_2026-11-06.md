# Glass TV Home — P3 V2 Static Interaction Re-Audit

**Document ID:** GTVH-UIR-P3-V2-REVIEW-ADD-001  
**Date:** 6 November 2026  
**Artifact reviewed:** `docs/project-management/prototypes/UI_REFRESH_P3_INTERACTIVE_PROTOTYPE_V2.html`  
**Artifact blob:** `2fbfe6ca66332f0ce827b63574cc26b42458b4c5`  
**Method:** read-only source/interaction-handler inspection. No browser execution or visual/device test was performed.

## 1. Result

**P3 V2 STATIC INTERACTION RE-AUDIT: PARTIAL — BLOCKERS FOUND.**

The V2 artifact covers all six surfaces and useful state simulations, but static inspection found interaction gaps that should be corrected before a structured walkthrough is accepted. These findings are prototype defects/omissions, not production-source defects.

## 2. Findings

| ID | Severity | Surface | Finding | Required correction / evidence |
|---|---|---|---|---|
| P3V2-R01 | High | Shared dialog / V05 | Nested Wi-Fi handoff calls `openDialog('wifiSystemDialog')`, which overwrites the original `origin`. Closing the child dialog hides the entire modal and attempts to restore focus to a control in the now-hidden parent dialog. Original surface/pill focus is lost. | Maintain a dialog stack or preserve root invoking origin separately from nested dialog state. Walk through Home → Wi-Fi popup → Android handoff → Back and verify return to Wi-Fi dialog, then close and verify original Home pill focus. |
| P3V2-R02 | High | Shared dialog / P3-02 | Global arrow-key handler exits whenever modal is open. Therefore D-pad/arrow navigation between dialog action buttons is not implemented. Initial focus is set to the first button, but directional traversal inside the modal is blocked. | Add modal-scoped spatial focus traversal, with disabled/edge behavior and no escape to background controls. Verify all dialog actions using arrows and Enter. |
| P3V2-R03 | Medium | V04 Quick Settings | Language and Wallpaper Interval tiles have no action handler. The interval tile is display-only; Language tile is inert. They appear actionable but do not open a control or explain unavailability. | Wire both to explicit simulated subflows or render as non-interactive status rows. Include focus, Back and result state. |
| P3V2-R04 | Medium | V04 Next Photo | The action models 0/1/2/5 image counts, disabled state and successful index change, but no unavailable-source/load-failure path or failure feedback is represented. | Add canonical-controller unavailable/failure simulation; only show success after a simulated state change. |
| P3V2-R05 | Medium | V05 Wi-Fi | Rescan in the normal/ready state sets “Memindai…” but does not complete with a result, empty, stale or failure state; it remains in the loading message. | Add bounded completion state and test success/empty/failure/throttle without inventing live scan results. |
| P3V2-R06 | Medium | V03 Profile | Profile cards are visually selectable but the non-active profile card has no switch handler/state transition. Active profile and Add Profile are represented, but the switch flow is not demonstrated. | Add explicit simulated switch, authorization/failure/cancel outcome, and active-vs-focused distinction; preserve previous active profile on failure. |
| P3V2-R07 | Medium | V04 Quick Settings | Restart and Power Off both open the same generic confirmation dialog; action identity and distinct consequence text are not preserved. | Use action-specific confirmation copy and simulated outcome; keep cancel safe and Back non-destructive. |

### 2.1 Verified positive behavior retained

- V01 Dock preview renders independent container for all six orientation/capacity combinations and labels app grid fixed at four columns. This is prototype-only, not proof of production architecture.
- V02 Weather refresh rotates simulated success/failure/offline states and does not deliberately move focus; actual browser focus retention remains untested.
- V03 Add Profile and failure dialog are represented; failure copy states prior state remains.
- V04 Next Photo is disabled for zero or one image and advances index for two/five images.
- V05 permission denied, Location disabled, offline, throttled, stale and empty cases are represented; scan results are labelled illustrative and Android handoff is explicit.
- V06 seven categories and control-level query-to-category filtering are represented; search focus is preserved during filtering and Down moves to the first match.
- Day/Normal/Night contrast selector and reduced-motion control are present.

## 3. Updated P3-01 to P3-12 disposition

| Scenario | Static source re-audit | Gate |
|---|---|---|
| P3-01 Popup close / origin focus | Partial: simple dialog restoration exists; nested dialog origin chain is broken | BLOCKED by R01 |
| P3-02 D-pad traversal | Surface traversal exists; modal traversal is blocked | BLOCKED by R02 |
| P3-03 Long label / large value | Responsive styles exist; no worst-case locale/text-scale test | PARTIAL |
| P3-04 Bright/dark wallpaper contrast | Modes exist; human visual legibility not assessed | PARTIAL |
| P3-05 Loading/success/error | Several simulations exist; action-specific completeness gaps remain | PARTIAL; R04/R05 |
| P3-06 Permission/offline/unavailable | Wi-Fi gates represented; walkthrough pending | SOURCE PRESENT |
| P3-07 Weather refresh focus stability | No intentional focus call in refresh handler; runtime test absent | SOURCE INSPECTED |
| P3-08 Wi-Fi throttled/stale | States represented; normal rescan completion missing | PARTIAL; R05 |
| P3-09 Next Photo 0/1/many | Disabled and success path present; failure path missing | PARTIAL; R04 |
| P3-10 Dock orientation × capacity | Six combinations represented; visual and production parity not proven | SOURCE PRESENT |
| P3-11 Exact-control search | Search routes to first matching category/control; keyboard walkthrough pending | SOURCE PRESENT |
| P3-12 Reduced motion | Toggle and CSS class present; transition/focus comparison untested | PARTIAL |

## 4. P1 decision items remain user/owner gates

The P1 inventory asks the user to accept the mapped E02/E06 scope and confirm that explicitly recorded verification gaps remain open rather than being represented as implemented. This cannot be accepted by an automated source audit.

Owner/decision evidence still required:
- OI-01: canonical V01–V06 reference asset/version and visual comparison traceability.
- OI-02/OI-03: E05 Weather V4 field-by-field mapping and Indonesia/provider/source-label validation.
- OI-04: user decision on V05 scope: status + Android handoff, or separate feasibility-approved enhanced scope.
- OI-05: confirm Quick Settings vs full Settings hierarchy/naming.
- OI-06/OI-07: E06 Dock control placement and independent Dock/grid architecture.
- OI-08/OI-09: B01 semantic surface, overscan, text scaling and appearance contract.
- OI-10: search scope (curated category/control index vs broader search).
- E02: HTTP cancellation/disconnect, animated-vs-still preview decision, lifecycle, QR authorization and GIPHY compliance evidence.
- E06: independent Dock model/view/focus, overflow and six-layout tests.

## 5. Recommended next work

1. Correct prototype-only findings R01–R07 in a new versioned prototype revision; preserve V2 unchanged for traceability.
2. Re-run static checks and syntax validation against the new artifact.
3. Perform browser keyboard walkthrough for P3-01–P3-12, including nested dialog and failure paths.
4. Conduct structured visual comparison against the exact canonical V01–V06 reference images; assess bright/dark wallpaper, long Indonesian labels, large values and safe margins.
5. Obtain user acceptance of P1 scope/decision items and explicit visual approval before P4.

## 6. Change boundary and final decision

No source code, prototype artifact, branch, PR, integration/main, APK, ADB or device was changed in this re-audit.

**Disposition: P3 remains PARTIAL / BELUM PASS.** R01 and R02 are high-severity prototype interaction blockers. R03–R07 are medium-priority completeness gaps. This audit does not authorize production implementation or claim user design approval.
