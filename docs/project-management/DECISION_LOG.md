# 03 — Decision Log

**Version:** 0.1-draft  
**Date:** 31 October 2026

## 1. Confirmed project decisions

| ID | Decision | Source | Status |
|---|---|---|---|
| D-001 | GitHub is the canonical source for project documentation; ChatGPT Library is the backup archive | User explicitly selected “both” | APPROVED |
| D-002 | Keep documentation modular; do not create one giant document | User's established documentation preference | APPROVED |
| D-003 | Keep profile avatar, home wallpaper, and standby/screen-saver photo collections as separate features and data stores | Product architecture / explicit clarification in current work | APPROVED |
| D-004 | Do not call a feature final solely because code is present; distinguish code inspection, CI/build, and real-device test | User's quality expectations and current audit protocol | APPROVED |
| D-005 | Maintain a separate documentation branch rather than mixing docs with application feature changes | Documentation workflow design | APPROVED FOR THIS DOCUMENTATION PASS |
| D-006 | Avatar photo upload must use a phone-scanned QR code; accept one image per session and delete the previous avatar only after the new file and profile reference are successfully committed | User explicitly requested QR upload and no retained old photo | APPROVED |
| D-007 | Project documentation uses three connected layers: Master, Audit/SOP, and Operational Log; one authoritative home per fact, minimal cross-links, GitHub canonical and ChatGPT Library milestone backup | User explicitly approved the efficient three-layer documentation approach on 31 October 2026 | APPROVED — STANDARD |

## 2. Baseline technical facts

- App ID: `com.gathin.tvhome`.
- Gradle version: 4.0.0 / versionCode 40.
- Android build configuration: minSdk 26, compileSdk 34, targetSdk 34, Java 17.
- User target: LG 32-inch display driven by Mi TV Stick, Android 11 / API 30 (must be revalidated before release).
- Main code branch under review: `feature/settings-reorganization-standby-gallery`.
- Avatar upload candidate: `fix/avatar-photo-upload-audit`.

## 3. Decisions still open

| ID | Question | Proposed direction | Status |
|---|---|---|---|
| O-001 | Avatar upload method | QR upload from phone on the same LAN; Android picker removed from active flow | DECIDED — QR ONLY |
| O-002 | Should a new profile be able to upload its photo during profile creation? | Create profile first, then select it and upload from Profile settings; inline creation upload can be considered later | DEFERRED |
| O-003 | Should custom avatars appear everywhere (top bar, settings card, switcher, add/edit dialogs)? | Use one profile image resolver across top bar, Settings sidebar/list, and profile switcher; add-profile starts with built-in avatar and can upload after creation | IMPLEMENTED IN PR #9; CI #208 PASS; DEVICE TEST OPEN |
| O-004 | Exact final order and density of information tabs | Decide after screenshot / remote navigation review | OPEN |
| O-005 | Default wallpaper: fixed image or rotating selection | Await asset/behavior audit and user confirmation | OPEN |
| O-006 | Icon animation: always animated or only on focus / interaction | Prefer focus-driven, restrained animation pending performance test | OPEN |
| O-007 | Weather/AQI location and update policy | Confirm precise chosen location and source | OPEN |
| O-008 | Prayer time zone/source and azan behavior | Confirm regional zone and enabled audio policy | OPEN |

## 4. Operational history

Chronological implementation and verification events are recorded once in [OPERATIONS_LOG.md](OPERATIONS_LOG.md). Detailed findings remain in the corresponding audit file. Do not duplicate operational history here.


## 5. Profile security and avatar integration decisions — 31 October 2026

- Parent PIN remains device-wide; Child Mode, locked apps, hidden apps, and avatar photo path are profile-scoped.
- Switching away from any active Child Mode profile requires parent PIN, including child-to-child. Adult-to-child and adult-to-adult switching do not require PIN.
- Parent PIN is required before editing locked/hidden app lists and before profile deletion; deletion retains a separate destructive confirmation.
- User explicitly keeps default PIN `0000` temporarily and will change it later. Do not force a first-run PIN change in this scope.
- User explicitly requires unlimited wrong PIN attempts; no cooldown, lockout, or retry limit.
- Avatar QR session: selected LAN IPv4, random token, 5-minute expiry, one successful upload, 15 MB maximum, 80 MP/12,000 px image bounds, app-private storage.
- Draft PR #8 → Draft PR #9 is the intended review/merge order after actual-device acceptance. Do not merge source PR #6/#7 directly.


## 6. Approved bug repair roadmap — 1 November 2026

| ID | Decision | Source | Status |
|---|---|---|---|
| D-008 | Adopt `BUG_REPAIR_ROADMAP.md` (GTVH-BRR-001 v1.0) as the official post-test repair sequence: A preparation/audit, B global foundations, C communication, D state/persistence, E specific modules, F integration/testing, G finalization/release. GitHub remains canonical; ChatGPT memory is the continuity/handover index. | User explicitly approved the A–G roadmap on 1 November 2026 | APPROVED |

Execution starts at A04. A01–A03 are complete planning tasks; no code repair or repository source audit has begun under this roadmap. Changes to order require a recorded technical dependency/root-cause rationale and user-facing checkpoint.


## 7. B01 execution sequencing — 1 November 2026

| ID | Decision | Source | Status |
|---|---|---|---|
| D-009 | At the user's explicit instruction to begin B01, source audit and a narrowly scoped fix were started before A05 tested-APK identity and A06 full baseline/backup were complete. To contain risk, work is isolated on a new branch created from the verified PR #9 exact head; no existing branch was overwritten, no PR merged, and no TV/device setting changed. A05 and A06 remain mandatory before installation/release acceptance. | User instruction to begin B01; verified GitHub repository tree, PR #9 head, and dedicated B01 branch | APPROVED EXECUTION EXCEPTION; A05/A06 OPEN |


## 8. No TV testing or candidate APK until audit completion — 1 November 2026

| ID | Decision | Source | Status |
|---|---|---|---|
| D-010 | Do not install or physically test any candidate APK while source audits and repairs are ongoing. Complete all A–E audit/fix work and F01–F07 integration/regression first. Only then produce and publish one final TV-test APK after explicit readiness approval. CI may compile the release variant for source validation, but normal PR CI must not publish a TV-test artifact. | User's explicit instruction in conversation, 1 Nov 2026 | APPROVED — binding workflow rule |


## 9. Lean Engineering operating model — 1 November 2026

| ID | Decision | Source | Status |
|---|---|---|---|
| D-011 | Adopt Lean Engineering for execution of the approved GTVH-BRR-001 A–G roadmap. Preserve the roadmap, all existing branches/PRs/commits, source and audit evidence, and the three-layer documentation standard. Eliminate duplicate audits and unnecessary paperwork; use risk-based verification, one active work unit at a time, and milestone-based checkpointing. Do not weaken required CI, regression, physical-device, safety, or release acceptance gates. | User explicitly selected Lean Engineering on 1 November 2026 | APPROVED |

### Operating rules
- Existing verified evidence is reusable only for the exact source commit and unchanged environment/scope; otherwise perform the smallest relevant re-verification.
- Keep one primary active work unit. Other work may remain open only when a real dependency or independent review requires it.
- A finding has one ID and one authoritative audit record. Other records link to it.
- Batch documentation updates at work-unit closure; update only affected records.
- Use L1/L2/L3 risk-based verification, but never skip mandatory roadmap stage gates or user-approved device/release restrictions.
- Preserve Draft/unmerged PRs and do not publish/install a candidate APK before all A–E work and F integration/regression gates and explicit readiness approval.



## 10. Permanent merge and quality gate policy — 1 November 2026

| ID | Decision | Source | Status |
|---|---|---|---|
| D-012 | Adopt GTVH-MQP-001 v1.0 in `MERGE_QUALITY_POLICY.md` as the mandatory merge and bug-fix standard. Use a two-level model: stage PRs merge to rolling `integration/b-series` only after all applicable audit, regression, static QC, test, and exact-head CI gates pass; merge to `main` only after full integration/regression, final acceptance, release review, and explicit user approval. For future bugs, use a risk-based impact map and scoped dependency audit, expanding to app-wide review only when shared foundations or evidence require it. Missing/stale evidence is not a pass. | User explicitly approved the proposed merge policy on 1 November 2026 | APPROVED — ACTIVE |

Implementation checkpoint: `integration/b-series` created from `d7b76c4a22c9eadffb8171a616283f3ad647d488`; existing B06 feature branch shares that base and is to target this integration branch. This does not mean B06 implementation is complete.


## 11. Deferred grouped merge for B06–G — 1 November 2026

| ID | Decision | Source | Status |
|---|---|---|---|
| D-013 | Starting with B06, defer all stage merges while A–G audits and repairs remain active. Keep work isolated and traceable in dependency-ordered feature branches/Draft PRs; continue source audits, regression tests, static QC, builds, and exact-head CI during development. After all planned source work is audited and repaired, freeze exact branch heads, audit the combined diff, then merge into `integration/b-series` by coherent dependency groups with CI and cross-group regression after each group. Complete final integrated acceptance, obtain explicit approval, merge to `main`, and build the final release APK from the exact approved `main` commit. A separate labelled test candidate may be needed for physical acceptance; it is not the final release APK. This supersedes D-012's rolling stage-merge timing for B06 onward; all quality gates and bug-audit rules remain active. | User explicitly directed that merging begin only at the end, in groups, after C–G may have introduced further changes | APPROVED — ACTIVE |



## 12. Cross-project coordination, integration, and product-quality principles — 1 November 2026

| ID | Decision | Source | Status |
|---|---|---|---|
| D-014 | Adopt the Cross-Project Coordination, Integration & Product-Quality Standard for all A–G work. Projects may be developed in isolated branches and work units, but they serve one roadmap, one coherent architecture, and one final product objective: a smooth, stable, complete Glass TV Home experience in which every feature and control works as intended. Each project retains a clear primary tupoksi to prevent duplicated or uncoordinated work; **these boundaries are coordination/ownership rules, not barriers to auditing, reporting, coordinating, or fixing cross-project defects**. Any mismatch, broken behavior, missing integration, regression, inconsistent data contract, or nonfunctional feature/control found across a project boundary must be investigated to root cause, assigned to the correct component owner, coordinated with affected projects, repaired where appropriate, and re-verified end-to-end. Preserve approved data, decisions, functionality, source history, and audit evidence; never silently omit, overwrite, reinterpret, or abandon prior work. Record material changes with rationale, impact, owner/project boundary, verification, and traceable evidence. Maintain synchronization through explicit dependency contracts and checkpoints. Before and during integration, reconcile cross-project interfaces and shared behavior; freeze exact heads before grouped integration, audit the combined diff, resolve conflicts, and run exact-head CI plus relevant functional, integration, and regression checks. Independent project builds are not proof that the whole application works. | User explicitly clarified on 1 November 2026 that cross-project synchronization must trigger audits and repairs when anything does not fit; all projects must form one mutually completing product and every feature/control must work as intended. | APPROVED — BINDING FOR A–G | 

### Mandatory operating rules

- **One product direction:** A–G follow the approved roadmap and common Glass TV Home objective. No isolated project may redefine the overall architecture or acceptance target unilaterally.
- **Clear ownership, no rigid silos:** Every work unit has a defined primary scope, inputs, outputs, dependencies, and completion evidence. Do not silently take over another project's work or duplicate its implementation. However, anyone conducting an audit may inspect relevant cross-project behavior, raise findings, trace root causes, and coordinate a fix with the responsible owner. A tupoksi boundary must never be used to leave a known defect or incompatibility unresolved.
- **Cross-project repair duty:** When a finding spans project boundaries, document the observed behavior, reproduction/evidence, affected projects and interfaces, suspected/root cause, responsible implementation owner, and verification plan. Fix the component that owns the cause; update dependent contracts/documentation and notify affected projects. If the correct owner or scope is uncertain, investigate and agree ownership at a checkpoint rather than ignore the finding.
- **Data and decision integrity:** No silent deletion, omission, overwrite, or abandonment of existing data, functionality, decisions, branches, commits, or audit findings. If removal or replacement is technically necessary, document the reason, impact, migration/retention plan, and approval where required.
- **Dependency discipline:** Work in the approved sequence while remaining responsive to dependencies discovered by audit. Consume verified outputs/contracts of prerequisite projects; communicate interface or behavior changes to affected downstream projects before treating them as final. A justified cross-project repair may be scheduled where the root cause resides without changing the overall roadmap order or merging prematurely.
- **Continuous synchronization:** Keep branch/PR status, exact commit heads, audit evidence, CI results, known limitations, ownership, and deferred tests consistent across canonical project records. GitHub documentation remains authoritative; ChatGPT context is a continuity aid, not a substitute.
- **Puzzle-fit integration:** Before combining projects, freeze exact heads and audit the combined diff, shared files, APIs/contracts, data ownership, resources/localization, lifecycle, navigation/focus behavior, and build configuration. Resolve conflicts deliberately; never rely on filename overlap alone or assume independent CI proves compatibility.
- **Feature and control functionality:** Every user-visible feature, action, button, menu, dialog, navigation path, and remote-control interaction must be verified against its intended behavior and relevant states. Check the full path from input → state/action → persistence/network effect where applicable → visible result/error handling. A control that merely renders or responds visually is not considered functional if its intended operation fails.
- **Risk-based quality, not audit theater:** Avoid redundant audits and unnecessary rework, but perform the smallest sufficient audit and quality check whenever evidence indicates a mismatch, defect, regression, or cross-project risk. Expand the scope when shared foundations or root-cause evidence require it. Every change must preserve previously verified behavior or include a justified, tested replacement.
- **Required verification:** Apply relevant functional, integration, regression, persistence/recovery, lifecycle, UI/UX, accessibility/focus/remote, static, build, and exact-head CI checks according to the affected scope. Do not claim tests that were not run. A green build or CI alone is not proof of complete functionality; physical-device tests and release gates remain governed by the approved roadmap and D-010/D-013.
- **Completion evidence:** A project is complete only when its own scope is met, relevant cross-project dependencies fit, required tests and exact-head CI pass, regressions are addressed, and remaining external/device tests are explicitly recorded as pending rather than silently treated as passed.
- **Final acceptance:** The goal is one coherent, stable, usable application whose projects support and complete one another—not a collection of independently passing projects. Do not promise absolute zero defects. Transparently track discovered defects and risks; resolve them or obtain explicit acceptance with rationale and limitations before release approval.

