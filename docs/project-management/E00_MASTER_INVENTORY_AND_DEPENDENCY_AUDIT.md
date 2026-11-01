# E00 — Master Inventory & Dependency Reconciliation

**Project:** Glass TV Home  
**Record:** GTVH-E00-INV-001  
**Version:** 1.0  
**Audit date:** 2 November 2026  
**Baseline source:** `feature/d07-concurrent-profile-content-state` @ `964163c918e281b68af5cf7114ca56ead2797486`  
**Documentation base:** `docs/project-master-inventory`  
**Scope:** Documentation, branch/PR inventory, dependency and acceptance-boundary reconciliation. No application source changes.

## 1. Executive disposition

E00 is a documentation and governance reconciliation, not an application refactor. The verified Project D endpoint is D07 at `964163c918e281b68af5cf7114ca56ead2797486`, with PR #31 open, Draft and unmerged. B01–B03 (#10–#12), B06–B07 (#16–#17), C01–C07 (#18–#24) and D01–D07 (#25–#31) PRs are open, Draft and unmerged under D-013. The B04/B05 source integration history is already merged within its earlier chain; this does not mean all B-series work is finally integrated or accepted.

The E00 document work is prepared on `docs/e00-inventory-reconciliation` for review against `docs/project-master-inventory`. It does not authorize application PR merges, APK publication, installation, or device-setting changes.

## 2. Verified project state

| Project | Verified source/PR state | Remaining acceptance |
|---|---|---|
| A | A01–A06 recorded DONE; A06 read-only Mi TV Stick snapshot and rollback baseline recorded | E01 resolves the HOME resolver/preferred-activity discrepancy; no device mutation before approved test |
| B | B01–B07 source/CI work recorded complete; B01–B03 PR #10–#12 and B06–B07 PR #16–#17 open/Draft/unmerged; B04/B05 PR #13–#15 merged in the earlier reconciliation chain | F-stage integrated navigation, D-pad/focus, lifecycle, localization and device checks |
| C | C01–C07 source/CI work recorded complete; PR #18–#24 open/Draft/unmerged | F-stage phone-to-TV LAN, QR, upload, retry, expiry, cancellation and storage-isolation acceptance |
| D | D01–D07 source/CI work recorded complete; PR #25–#31 open/Draft/unmerged | F-stage runtime, profile concurrency, persistence, process-death, force-stop and reboot acceptance |
| E | E00 inventory/reconciliation in progress; E01–E08 not yet started | Complete source audits/fixes, exact-head CI and cross-project impact records |
| F | Not started | Grouped integration, combined-diff review and full regression matrix |
| G | Not started | Release-blocker closure, approved APK, target-TV install/upgrade/rollback, records and handover |

**Important status distinction:** source/CI completion is not the same as integrated completion or physical-device acceptance. Deferred acceptance remains mandatory, not waived.

## 3. Branch and PR inventory

The current remote branch listing returned 36 branches. It includes the immutable A06 checkpoint, documentation branch, B/C/D feature branches, integration branches, legacy/earlier feature branches, and `main`. A branch's existence is not proof that it is current, merged, or safe to use as a base.

Verified current Draft PR sequence:

- B01–B03: #10–#12.
- B04/B05 reconciliation: #13–#15 merged.
- B06–B07: #16–#17.
- C01–C07: #18–#24.
- D01–D07: #25–#31.

PR #10–#12 and #16–#31 were queried individually on 2 November 2026 and returned OPEN, Draft, not merged. PR #13–#15 were queried and returned merged. Their source work and exact-head CI evidence are documented in the corresponding PR bodies and audit records. PR #31 identifies D07 final head `964163c918e281b68af5cf7114ca56ead2797486` and exact-final-head CI #404 / run `41313690556` as PASS.

The B04/B05 reconciliation history is not to be rewritten: its earlier merge commits and follow-up CI evidence remain part of the source history. B06 onward remains unmerged under D-013.

## 4. Protected implementation and contracts

The following must be preserved unless a later audit proves a defect and records a narrow, tested repair:

1. **B global UX:** semantic navigation/focus retention, visible D-pad focus, opaque/readable dialogs, responsive TV layout, Indonesian/English resources, lifecycle-safe feedback, and settings-pane focus/scroll restoration.
2. **C QR/local server/upload:** selected LAN interface binding, token-scoped routes, strict HTTP framing and endpoint contracts, shared bounded upload reader, image/file validation, per-operation limits/timeouts, session expiry, cancellation, retry, cleanup and StandBy batch progress.
3. **D state/persistence:** explicit storage ownership, truthful persistence feedback, Child Mode/profile isolation and PIN policy, profile/avatar synchronization, atomic StandBy collection/mode updates, reboot alarm restoration, stale-response rejection and serialized concurrent mutations.
4. **Approved user data and settings:** do not reset preferences, rename storage keys, migrate schemas, delete user photos, change PIN policy, or alter TV settings as part of E00.
5. **APK safety:** latest D07 diagnostic APK was 1,552,197 bytes against a 1,572,864-byte cap, leaving 20,667 bytes. Treat this as a tight budget; do not add libraries/assets without measured need. This size is a CI diagnostic, not a published or installed candidate.

## 5. E00 findings and corrections to master records

| ID | Finding | Disposition |
|---|---|---|
| E00-DOC-01 | Roadmap still described B07 as next and marked later B/C/D work NOT STARTED despite D07 source/CI closure | Update current status and execution point; retain final F acceptance as pending |
| E00-DOC-02 | Progress snapshot and B05 localization counts were stale | Replace old snapshot; record 569 default/ID/EN keys from static comparison on D07 source, zero duplicate keys, key-set differences or placeholder mismatches |
| E00-DOC-03 | Inventory carried obsolete B04/B05 PR #15 open/pending statements alongside later merged-chain evidence | Preserve historical checkpoints, add dated current-state reconciliation so old snapshots are not mistaken for current truth |
| E00-DOC-04 | Roadmap did not distinguish source/CI closure from integration/device acceptance consistently | Explicitly separate these gates and retain F-stage acceptance as mandatory |
| E00-ARCH-01 | A06 device snapshot showed HOME resolver returning stock launcher while preferred-activity table listed Glass TV Home | Carry to E01 as unresolved observation; do not infer default launcher ownership from one signal or change device state |
| E00-ARCH-02 | MainActivity startup helper attempts system/screen-saver/accessibility/notification-listener settings writes and suppresses exceptions | E01 must audit permission, actual result/read-back, user consent/ownership, and failure feedback; this is an audit target, not yet a proven device defect |
| E00-ARCH-03 | Wallpaper state is distributed across preference stores and related UI flows | E03/E08 must map ownership and contracts first; no broad migration or consolidation without evidence and compatibility plan |
| E00-ARCH-04 | Information Center exists, while Quick Actions placement and ownership are not yet finalized | E05 must map current actions and avoid duplicate controls or losing existing actions |
| E00-ARCH-05 | Legacy Settings organization differs from current grouped tabs; Child Mode is now grouped with Profile | E06 must compare behavior and recover only genuinely missing functions, not recreate moved controls |
| E00-ARCH-06 | Existing wallpaper selection, shuffle interval and manual Next actions are implemented in separate UI surfaces | E08 should unify access and behavior without duplicating underlying state or breaking rotation |

### Localization verification note

The resource audit compared `values/strings.xml`, `values-id/strings.xml`, and `values-en/strings.xml` on the D07 branch. Each contained 569 unique string keys; no duplicate keys, missing/extra locale keys, or printf-placeholder mismatches were found. This is a static resource-parity result only; it does not prove every runtime string is translated or every screen is visually correct.

## 6. Integration timing — authoritative sequence

D-013 remains active: do not merge B06 or later feature PRs into `integration/b-series` while A–G source audits and fixes are active. Keep the branch reserved for final grouped integration. D-014 remains binding: project boundaries are ownership/coordination boundaries, not barriers to cross-project audit and repair.

**Before F:**
- Complete A–E source audits and scoped fixes; close findings or explicitly classify accepted residual risks with rationale.
- Keep exact source heads, PR bases, dependencies, CI evidence and audit records synchronized.
- Do not merge feature PRs into `integration/b-series` yet. No candidate APK installation or physical TV test during active source repair.

**F01 — integration rehearsal, not the final grouped merge:**
1. Freeze candidate heads for A–E and record each exact SHA.
2. Assemble an isolated, temporary integration candidate only when needed to test composition; keep it separate from the reserved `integration/b-series` branch and do not merge feature PRs into that canonical integration branch.
3. Audit ancestry and the combined diff, especially shared files, Android manifest, storage contracts, QR endpoints, resources/localization, lifecycle, focus/navigation and build configuration.
4. Run exact-head CI, relevant unit/integration/regression checks and cross-project contract checks. If a group fails, stop dependent work and return the finding to its owning project.

**F02–F07 — candidate acceptance:** remote/focus/lifecycle, phone-to-TV QR and upload, avatar/wallpaper/StandBy store isolation, persistence/process death/reboot, Indonesian/English, and performance/stability on the target Mi TV Stick. Physical testing uses only a clearly labelled test candidate after the pre-device gate and explicit user approval; it is not the final release APK.

**G01–G05 — closure and release:** G01 resolves findings through their owning projects and re-verifies affected candidate paths. Only after all A–G source audits/fixes are complete and findings are closed or explicitly accepted may the exact approved heads be frozen and merged into `integration/b-series` by coherent dependency groups, with exact-head CI and cross-group regression after each group. G02 produces the traceable release candidate/artifact; G03 verifies installation, upgrade, launcher behavior and rollback on the target TV. Merge the verified result to `main` only after final acceptance, release review and explicit user approval; build/record the final APK from that exact approved `main` commit, workflow run, artifact and SHA-256. G04–G05 reconcile master documentation and handover.

## 7. E00 exit criteria

- [x] Current remote branches and PR #16–#31 status queried.
- [x] D07 exact head and exact-head CI evidence reconciled.
- [x] B/C/D source/CI closure separated from F-stage acceptance.
- [x] E01–E08 scope and cross-project dependencies inventoried at planning level.
- [x] F/G work and release gates reconciled with D-013/D-014.
- [ ] Documentation PR reviewed and integrated into the canonical documentation branch.
- [ ] E00 checkpoint re-read after merge; only then mark E00 fully closed.

## 8. Scope and limitations

This pass did not run a fresh local Gradle build, inspect every source file in all feature branches, install an APK, connect to the TV, change device settings, or perform phone-to-TV runtime tests. It is a documentation/metadata and targeted source spot-check. The E01–E08 source audits must still inspect their exact parent heads and changed-file diffs before any implementation decision.

**Safety disposition:** no application source, APK, user data, device settings, feature branch, or existing PR was modified by E00 documentation preparation.
