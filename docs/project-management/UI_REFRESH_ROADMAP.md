# Glass TV Home — Global UI Refresh Roadmap
**Document:** GTVH-UIR-002  
**Version:** 2.0  
**Status:** NEW GOVERNING ROADMAP — replaces UI Refresh v1.0  
**Baseline checkpoint:** 6 November 2026  
**Execution principle:** Fix the complete scope first; test after implementation; repeat fix-and-test until successful and safe; perform final QC; only then close documentation and checkpoint.

## 1. Objective
Deliver the six approved Global UI surfaces as one coherent, usable Android TV experience: visually consistent, fully functional, D-pad-safe, readable at TV distance and in daylight, stable across lifecycle/state changes, and free of known critical/high defects.

## 2. Accepted inputs — do not redo without a changed source
- P0 baseline: PASS at integration baseline `562cde0751de93748eb4e0d4cdece4fa1672ac4d`.
- P1 functional inventory: ACCEPTED for progression; six references V01–V06 and functional boundaries remain authoritative.
- P2 shared design/interaction contract: COMPLETE/FROZEN. Preserve blue-tinted glass, restrained cyan focus, high-contrast typography, readable surfaces, deterministic D-pad/Back behavior, and full Weather V4 scope.
- Existing B01 shared foundations remain the implementation base: `TvUiTokens`, `TvDialogStyle`, `GlassDrawable`, `TvFocusIndicator`, `TvResponsiveLayout`, shared Settings builders, `TvFeedback`, and `AppLanguage`.
- Reuse valid audits and CI evidence. Repeat an audit only when code, branch ancestry, dependency, or test evidence changes.

## 3. Execution roadmap

| Order | Work package | Required result |
|---|---|---|
| **G1 — Visual prototype completion** | Finish the real 1920×1080 prototype/review for V01–V06. Cover six Dock combinations (horizontal/vertical × capacity 4/5/6), focus paths, overflow, dialogs, loading/empty/error states, daylight contrast, text scaling and TV-distance readability. Compare against the six approved references. | All six surfaces reviewed against references; required interactions represented; no unresolved critical visual/navigation issue. No production rollout before this gate. |
| **G2 — Shared UI foundation repair** | Bring shared tokens/components into line with the approved blue-glass direction. Complete focus coverage, dialog/surface contrast and fallback, responsive/overscan-safe layout, text scaling, localization fit, and shared state/feedback patterns. | One consistent shared visual system; no competing module-specific styles; source conformance across in-scope call sites. |
| **G3 — Surface implementation** | Implement in dependency order using small owner-scoped changes: **3A Home & independent Dock**; **3B Weather/AQI V4**; **3C Profile, Avatar & Child Mode**; **3D Quick Settings**; **3E Wi-Fi**; **3F Global Settings**. Preserve each surface's approved behavior and data requirements. | Every V01–V06 surface implemented. Dock has its own model/view/adapter and focus region; app-grid columns remain independent. Existing behavior and data are preserved. |
| **G4 — Source reconciliation & integration candidate** | Reconcile E02/PR #62, E06/PR #63, and P4-01/PR #64 with the current integration baseline. Resolve ancestry/shared-file conflicts semantically; retain owner changes; do not blindly merge, rebase, cherry-pick or force-push. Build one isolated candidate only after all scoped implementation is ready. | Clean, traceable integration candidate; no unresolved source conflict or lost owner change; APK budget measured on the combined tree. |
| **G5 — Full verification and repair loop** | After implementation and reconciliation, run the complete test matrix: unit/build; all six surfaces and controls; D-pad/focus/Back/scroll/overflow; persistence/restart/rollback; profile isolation/Child Mode; GIF/network cancellation and lifecycle; offline/permission/error states; localization; security/privacy; memory/performance; supported API/resolution and authorized TV checks; regression across existing launcher features. | Tests and exact-head CI pass. Any failure returns to implementation repair, followed by the relevant tests again. Repeat until all required tests pass and the candidate is safe. |
| **G6 — Final Quality Check** | Independently check visual parity, functional completeness, navigation, state/data integrity, lifecycle, security/privacy, compatibility, performance, regression, APK integrity/signature/size and evidence traceability. | **QC PASS** only when all mandatory criteria pass and no open Critical/High defect remains. |
| **G7 — Closeout** | Update canonical project records once with final scope, decisions, owner commits/PRs, exact candidate SHA, test evidence, QC result and remaining accepted limitations. Create the final checkpoint. | Project marked complete only with evidence; concise final report. |

## 4. Surface acceptance requirements
- **V01 Home/Dock:** independent Dock presentation and app grid; horizontal/vertical and capacity 4/5/6; default capacity 5; stable pinned order, overflow, focus and reorder; Dock changes never alter grid columns or silently drop apps.
- **V02 Weather/AQI:** preserve the complete approved Weather V4 metric set, including current conditions, feels-like/min-max, humidity, wind, pressure, dew point, visibility, sunrise/sunset, moon phase, rainfall/probability, UV and pollutant breakdown. Show truthful source, location and update/staleness information.
- **V03 Profile:** active profile is distinct from current D-pad focus; profile-scoped data, Child Mode/PIN, avatar selection and QR/GIF flows remain secure and lifecycle-safe.
- **V04 Quick Settings:** compact quick-action popup remains separate from full Settings; Next Photo uses the canonical E08 wallpaper controller.
- **V05 Wi-Fi:** show only Android-exposed status/network information; scan/list/rescan only with required permissions and Location prerequisites; use Android Settings handoff for system-managed operations; never show fake toggles or claim unsupported actions succeeded.
- **V06 Global Settings:** preserve the approved seven-category taxonomy, control-level search, left navigation/right detail layout, localization and focus restoration.

## 5. Quality and stop rules
- No known Critical/High defect may pass G5 or G6.
- No crash/ANR, data loss, cross-profile leakage, focus trap, dead control, false success state, or reproducible regression.
- Every interactive control must have a verified action and every asynchronous flow must handle cancel, failure and lifecycle exit safely.
- Visual acceptance includes contrast over wallpaper, overscan, daylight glare, 1920×1080 layout, TV-distance readability and localized text fit.
- CI PASS is necessary but does not replace visual, device or integrated acceptance.
- Do not add assets/dependencies without measuring APK impact against the 1,572,864-byte ceiling.
- Never claim 100% defect-free software; report only verified results and known residual risk.

## 6. Ownership and dependency
- **Shared UI foundation:** B01 and UI architecture.
- **Home/Dock:** E06, with E01/E05/E08 dependencies where applicable.
- **Profile/Avatar/Child Mode:** E02, with profile/security and upload dependencies.
- **Weather/AQI:** E05/E01.
- **Quick Settings/Wi-Fi:** E01 and E08 as applicable.
- **Global Settings:** shared Settings/navigation/focus owners B01–B07 plus each feature owner.
- **Verification/integration:** QA/integration workstream.
- Keep changes on owner branches until G4. Preserve existing no-early-merge and device-mutation approval boundaries.

## 7. Work discipline
1. Follow the sequence; do not reopen accepted P0/P1/P2 work without new evidence.
2. Audit to locate the fault, analyze the root cause, decide the simplest safe fix, and execute.
3. Do not produce session-by-session progress documentation. Keep working notes internal to execution.
4. Do not stop at a partial stage while known in-scope defects remain unresolved.
5. Perform full verification only after the scoped implementation/reconciliation is ready; on failure, repair and retest until successful and safe.
6. Run final QC after successful verification.
7. Only after QC PASS, update documentation and create the checkpoint.
8. Report briefly: STATUS, RESULT, EVIDENCE, REMAINING WORK (only if any).

## 8. Current starting point
- P0: PASS.
- P1: ACCEPTED.
- P2: COMPLETE/FROZEN.
- G1: COMPLETE / internal QC PASS — V6 prototype reviewed at 1920×1080; six surfaces and six Dock variants rendered; browser suite 12/12 PASS. See `UI_REFRESH_G1_CHECKPOINT_2026-11-06.md`.
- PR #64: OPEN/DRAFT/UNMERGED; CI passed on recorded head, but Home/Dock implementation is incomplete.
- PR #62 and #63: OPEN/DRAFT/UNMERGED; exact-head CI passed on recorded heads, but owner branches diverge from the frozen integration baseline and require semantic reconciliation.
- G2–G7: NOT STARTED. Resume at G2 — Shared UI Foundation Repair.
- G1 prototype completion does not equal Android runtime/physical-TV acceptance; owner visual sign-off remains separate before broad rollout.

**This GTVH-UIR-002 roadmap supersedes and retires GTVH-UIR-001 v1.0 as the execution plan. P0/P1/P2 records remain historical evidence and are not deleted or re-run by default.**