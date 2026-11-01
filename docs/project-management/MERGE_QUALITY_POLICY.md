# Glass TV Home — Merge & Quality Gate Policy

**Policy ID:** GTVH-MQP-001  
**Version:** 1.0  
**Approved by user:** 1 November 2026  
**Status:** ACTIVE — mandatory for all subsequent source changes, bug fixes, and releases

## 1. Non-negotiable principles

1. Quality and testability take priority over merge speed.
2. A completed code change, a clean diff, GitHub's `mergeable` flag, or a green CI run alone is not sufficient authorization to merge.
3. Every merge must be supported by evidence tied to the exact commit being merged.
4. Preserve existing behavior unless a change is intentional, justified, and documented.
5. Do not hide unresolved defects by weakening tests, removing features, suppressing errors, or marking unverified work as complete.
6. If a required gate fails or evidence is missing, the change remains unmerged and its status is recorded as BLOCKED or NOT READY.
7. Physical-device tests may be scheduled at the approved final acceptance stage; deferral is never equivalent to a pass or waiver.

## 2. Two-level merge model

### Level 1 — Stage integration

- Work in an isolated feature/repair branch.
- Open a PR into the rolling integration branch: `integration/b-series`.
- Before merge, complete the stage-specific audit, impact analysis, source/static quality checks, tests, and exact-head CI.
- Merge only when all applicable gates pass and the PR diff/base/head are rechecked.
- After merge, record the merge commit, source PR, CI run, audit evidence, and any deferred acceptance items.

### Level 2 — Final release integration

- Keep `main` as the stable release line; do not merge each stage directly into `main`.
- Merge the completed and regression-tested integration result to `main` only after the full roadmap's required gates, final acceptance, release review, and explicit user approval.
- Produce and install a release/test APK only at the approved release/device-test point. Normal CI must not publish a device APK unless explicitly authorized.

## 3. Mandatory pre-merge checklist

A PR is merge-ready only when all applicable items below are PASS:

| Gate | Required verification |
|---|---|
| Scope & traceability | Requirement/bug ID, expected behavior, affected files/modules, and acceptance criteria are clear. |
| Source audit | Changed code and nearby callers/callees/dependencies reviewed; no unrelated or accidental changes. |
| Impact analysis | Shared state, lifecycle, navigation/focus, localization, persistence, network, permissions, and UI effects considered where relevant. |
| Regression protection | Relevant existing tests updated/added; regression cases cover the changed behavior and likely side effects. |
| Static/resource QC | No compile/resource/reference errors, duplicate or mismatched localization keys/placeholders, unsafe null/lifecycle paths, or unexplained warnings in the changed scope. |
| Build & tests | Required build, unit, integration, and other stage-specific checks pass. |
| Exact-head CI | All required CI checks pass on the exact PR head intended for merge; rerun after any subsequent commit. |
| PR/diff integrity | Correct base branch, expected head SHA, no unresolved review blocker, no unintended conflict or generated-file corruption. |
| Documentation | Audit register, decision/operations log, roadmap, and handover updated when the change affects project state or decisions. |
| Readiness decision | Explicit PASS/NOT READY decision recorded; deferred device/runtime tests remain visibly pending. |

A missing, skipped, stale, or ambiguous result is NOT PASS.

## 4. Bug-fix audit method — avoid repetitive, unfocused work

For every newly discovered bug or regression, create a traceable repair record and follow this sequence:

1. **Reproduce & define:** capture symptoms, steps, expected result, actual result, environment, and evidence. If it cannot be reproduced, label it UNCONFIRMED; do not invent a root cause.
2. **Locate the fault boundary:** identify the owning module, entry point, shared services, data/state owners, callbacks, and upstream/downstream dependencies.
3. **Build an impact map:** list directly changed components, dependent components, shared contracts, and user-visible flows that may regress.
4. **Audit the relevant surface:** inspect the fault path and its dependencies, not merely the line where the symptom appears. Expand to app-wide audit only when the change affects a global/shared foundation or evidence indicates wider impact.
5. **Fix the root cause:** avoid symptom-only patches, duplicated logic, broad timeout changes, or behavior changes outside the approved scope.
6. **Add regression protection:** create a test for the original failure and relevant boundary/negative/lifecycle cases. Record any scenario that cannot be automated for later manual acceptance.
7. **Verify exact changes:** inspect the complete diff, run targeted tests, broader dependent-module tests, build, and exact-head CI.
8. **Close the loop:** record root cause, fix, files/modules, tests, CI evidence, residual risks, and rollback point. Reopen the audit if a later change touches the same contract or dependency.

### Audit expansion triggers

Expand beyond the immediate module when a change touches:
- global navigation, focus, dialogs, localization, theme/layout, or lifecycle;
- shared state, persistence, profile/content ownership, or cross-module event flow;
- common QR/server/upload/network infrastructure;
- shared resources, build configuration, manifests, permissions, or dependency versions;
- APIs/contracts consumed by multiple modules;
- a regression that appears in more than one feature or cannot be isolated to one owner.

## 5. Evidence and status rules

- Record exact branch, base SHA, PR head SHA, merge SHA, CI run IDs/conclusions, tests, audit scope, and outstanding limitations.
- Re-run exact-head checks after any code change; results from an earlier SHA do not validate a later SHA.
- Distinguish SOURCE AUDIT, STATIC QC, BUILD, AUTOMATED TEST, RUNTIME TEST, PHONE/NETWORK TEST, and PHYSICAL DEVICE ACCEPTANCE.
- Use PASS, FAIL, PENDING, DEFERRED, BLOCKED, or NOT APPLICABLE with a short reason. Never convert DEFERRED into PASS.
- A post-merge integration check is still required where branch composition can introduce conflicts or interaction defects.

## 6. Rollback and post-merge control

- Preserve the pre-merge base and merge commit as rollback references.
- If post-merge CI or integration checks fail, stop dependent work, identify the introducing change, revert or repair on a controlled branch, and repeat the same gates.
- Do not stack additional features on a known-broken integration head.
- Maintain a concise change-to-test map so later bug fixes can reuse prior audit knowledge without assuming old tests cover new code.

## 7. Current application

- Rolling integration branch established: `integration/b-series`, created from B-series continuation SHA `d7b76c4a22c9eadffb8171a616283f3ad647d488`.
- B06 branch `feature/b06-notification-feedback` is based on that same continuation point; its PR must target `integration/b-series`.
- B06 implementation has not started; only its baseline audit is complete.
- B07 and later stages follow the same policy.
- Final runtime, phone QR, and physical TV acceptance remain deferred to the approved final system-test stage, not waived.

## 8. Approval

This policy was approved by the user on 1 November 2026 and applies to all subsequent Glass TV Home work unless explicitly revised and versioned in the project Decision Log.


## 9. Revised execution model — deferred grouped merge (v1.1)

**User decision, 1 November 2026:** Starting with B06, do not merge stage work into the integration branch while the A–G audit and repair program is still underway. Preserve flexibility for cross-stage findings and follow-on changes from C through G. Keep work unmerged until all planned source audits and fixes are complete and the grouped integration plan is ready.

### Work while A–G is in progress
- Keep each work unit isolated and traceable in its own feature branch/PR; PRs may remain Draft/open and unmerged.
- Preserve dependency order by stacking the next work branch on the verified head of its prerequisite branch, or use another explicitly documented cumulative working branch. Never base dependent work on an unverified or abandoned head.
- Run scoped audits, regression tests, static QC, builds, and CI during development. Deferring merge does NOT mean deferring verification.
- If a later stage requires a correction to an earlier stage, update the earlier work branch and re-run the affected downstream checks. Record the dependency and impact map.
- `integration/b-series` is a reserved final integration/staging branch, not a rolling merge destination during active A–G repair. Do not merge B06 or subsequent stage PRs into it yet.

### Grouped integration at the end
1. Complete all A–G source audits and fixes; close open findings or explicitly classify accepted residual risks.
2. Freeze a candidate set of exact branch heads and map their dependencies.
3. Audit the combined diff and resolve conflicts in dependency order.
4. Merge into `integration/b-series` by coherent dependency groups (not necessarily one PR per roadmap letter); run exact-head CI and cross-group regression after each group.
5. Run the full integrated regression and final acceptance matrix. Any failure returns to the owning group for repair and re-verification; do not proceed to release on a failing integration head.
6. After acceptance and explicit approval, merge the verified integration result to `main`.
7. Build the final release APK from the exact final approved `main` commit; record version, commit SHA, workflow run/artifact, and SHA-256. If physical acceptance requires an installable candidate first, create a clearly labelled test candidate from the frozen integration head; it is not the final release APK and must not be confused with it.

### Merge decision rule
No stage merge is required merely to mark that stage's development complete. Development completion, verification completion, grouped integration, final acceptance, and release are separate states. The final grouped merge is allowed only after all applicable gates pass. A green stage CI is necessary evidence, not permission to merge early.

This v1.1 execution model supersedes the rolling-stage-merge instruction in v1.0 for B06 onward. The quality gates and risk-based bug audit method in v1.0 remain in force.
