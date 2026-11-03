# G01 Addendum — Project-Wide Branch & Integration Reconciliation

**Checkpoint:** GTVH-G01-BRANCH-RECON-2026-11-03  
**Date:** 3 November 2026 (WIB)  
**Disposition:** G01 COVERAGE RE-AUDIT PASS; PR #59 MERGED INTO `integration/b-series`; G02 READY TO START  
**Canonical docs branch:** `docs/project-master-inventory`

## 1. Why this addendum exists

A fresh live GitHub inventory was taken after the G01 defect audit. It found that the frozen F07 candidate is a valid host/source-tested descendant of the F-staged-full candidate, but it is **not on the current `integration/b-series` branch lineage**. Those two integration branches diverged and have a shared-file delta. Therefore the phrase “all projects combined and synchronized” would be premature until the B-series delta is reconciled with F02–F07.

This does not invalidate the exact-head CI results already recorded for the owner branches or F02–F07. It means those results do not, by themselves, prove the final cross-project combined candidate.

## 2. Live PR inventory — all pull requests #1–#56

| Live state | PRs | Interpretation |
|---|---|---|
| MERGED | #1, #13, #14, #15, #32, #33 | Existing merged history; retain and do not reapply. #13–#15 are the B04/B05 reconciliation chain; #32–#33 are documentation-only. |
| OPEN — source/feature work | #2–#12, #16–#31, #34–#41, #50–#51, #53–#56 | Preserve source ownership and exact current heads; all remain unmerged. #2 and #3 are older non-draft open PRs; do not assume they are included or excluded without checking their downstream ancestry/content. |
| CLOSED — rehearsal/CI-only | #42–#49 | Closed without merge; evidence/rehearsal branches only. Do not treat these PRs as merged production history. |
| CLOSED — superseded | #52 | Superseded by #53; no merge. |

The live open-PR inventory confirms all feature-owner and F-stage PRs remain open/unmerged under D-013. Branch existence or GitHub's “mergeable” flag is not proof that the complete candidate is synchronized.

## 3. Current integration lineage

The current F-stage chain is internally linear:

`integration/project-f-staged-full` @ `8fe90a9fdd493c5d81ffdacbe16054630a26580a`  
→ PR #50 / `integration/project-f02-system-validation` @ `2ede452b3137580bd51088661f80b6d0eeac7f4b`  
→ PR #53 / `integration/project-f04-feature-isolation-v2` @ `6ffc8b4c253beb2662d6978ba38033039c3c53f9`  
→ PR #54 / `integration/project-f05-persistence-recovery` @ `1048b0b68ae5ab5a2ad867183236b514e0c691fa`  
→ PR #55 / `integration/project-f06-localization` @ `487099013e6d9bd65cc07d72e760b1e14ab1f091`  
→ PR #56 / `integration/project-f07-performance-stability` @ `976e068d48ecc1ede2811ac455372395d382ba76`

F07 is 173 commits ahead / 0 behind its F-staged-full base. This proves the F02→F07 chain is linear.

The separately reserved B-series branch is:

`integration/b-series` @ `9070894fa6d9ef8aec034f46dfe777e866709dfe`

It was produced from `integration/f06-ci-base` @ `fec70ffa6e03e0f903186cc1491f4358a99768a7`. It is not an ancestor of the F-staged-full/F07 chain.

## 4. Objective compare evidence

| Compare | GitHub status | Ahead | Behind | Changed files | Meaning |
|---|---:|---:|---:|---:|---|
| F-staged-full → F07 | ahead | 173 | 0 | 49 | F02–F07 chain is linear. |
| F-staged-full → B-series | diverged | 60 | 466 | 26 | B-series carries a distinct set of 60 commits and 26 overlapping file changes. |
| B-series → F07 | diverged | 639 | 60 | 123 | F07 and B-series are not yet a single reconciled candidate. |
| main → F07 | ahead | 1,020 | 0 | 159 | F07 is a candidate based on main, not a release merge to main. |

Shared-ancestor for the B-series/F-staged-full compare: `d7b76c4a22c9eadffb8171a616283f3ad647d488`.

### The 26 overlapping paths that require grouped semantic reconciliation

- `launcher/app/src/main/java/com/gathin/tvhome/AppAdapter.java`
- `launcher/app/src/main/java/com/gathin/tvhome/AppUpdateManager.java`
- `launcher/app/src/main/java/com/gathin/tvhome/AvatarUploadServer.java`
- `launcher/app/src/main/java/com/gathin/tvhome/ChildModeManager.java`
- `launcher/app/src/main/java/com/gathin/tvhome/DockDecoration.java`
- `launcher/app/src/main/java/com/gathin/tvhome/FeedbackPolicy.java`
- `launcher/app/src/main/java/com/gathin/tvhome/GlassDrawable.java`
- `launcher/app/src/main/java/com/gathin/tvhome/HomeSettingsActivity.java`
- `launcher/app/src/main/java/com/gathin/tvhome/LocationBroadcastReceiver.java`
- `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java`
- `launcher/app/src/main/java/com/gathin/tvhome/ModernSettingsDialog.java`
- `launcher/app/src/main/java/com/gathin/tvhome/NetworkFeedbackPolicy.java`
- `launcher/app/src/main/java/com/gathin/tvhome/RecentAppsActivity.java`
- `launcher/app/src/main/java/com/gathin/tvhome/TvDialogStyle.java`
- `launcher/app/src/main/java/com/gathin/tvhome/TvFeedback.java`
- `launcher/app/src/main/java/com/gathin/tvhome/TvUiTokens.java`
- `launcher/app/src/main/java/com/gathin/tvhome/UserSwitchDialog.java`
- `launcher/app/src/main/java/com/gathin/tvhome/WallpaperUploadServer.java`
- `launcher/app/src/main/java/com/gathin/tvhome/WeatherActivity.java`
- `launcher/app/src/main/res/values-en/strings.xml`
- `launcher/app/src/main/res/values-id/strings.xml`
- `launcher/app/src/main/res/values/strings.xml`
- `launcher/app/src/test/java/com/gathin/tvhome/FeedbackPolicyTest.java`
- `launcher/app/src/test/java/com/gathin/tvhome/GlassSurfaceFallbackTest.java`
- `launcher/app/src/test/java/com/gathin/tvhome/NetworkFeedbackPolicyTest.java`
- `launcher/app/src/test/java/com/gathin/tvhome/TvDialogStyleTest.java`

These are not automatically merge conflicts, but they are confirmed shared-file differences. Each must be resolved by behavior/contract and owner source, not by blindly choosing “ours” or “theirs”.

## 5. Targeted F07 candidate source spot-check

The following exact F07 source/test paths were fetched from the frozen branch:
- B01 `HomeKeyService.java` hash equals the current B01 owner branch hash.
- `UploadDeadlineInputStream.java` hash equals the current C07 owner branch hash.
- F07 contains the internal-only Recent Apps manifest declaration, DUMP-protected LocationBroadcastReceiver, non-exported AzanActivity, and system-bindable InputKeyService with `BIND_ACCESSIBILITY_SERVICE`.
- F07 contains the C07/F02 synchronized Wallpaper management handlers, monotonic elapsed-realtime deadlines, process-wide storage reservation/HTTP 507 behavior, and upload shutdown atomicity tests.
- F07 contains the Robolectric `TvDialogStyle` test and Recent Apps/accessibility manifest regression tests.

The targeted spot-check is positive, but it does not close all 26 shared-file differences. For example, the B01 manifest/RecentApps, C07 Wallpaper/Avatar server, E08 MainActivity/resources, and D07 ProfileManager file hashes differ between current owner heads and F07. Such differences can be intentional downstream integration, but they require a 3-way semantic audit and explicit disposition.

## 6. Safe combine/merge plan — no main merge

1. Freeze the exact current heads of every required owner branch and the current F07 candidate. Keep PRs #1/#13–#15/#32–#33 merged history; exclude closed rehearsal PRs #42–#49 and superseded #52 from the merge list.
2. Reconcile legacy open PRs #2/#3 and the complete B01–B07/C01–C07/D01–D07/E01–E08 owner dependency chains. Do not double-apply already inherited PR ancestry.
3. Use a fresh isolated final integration candidate. Reconcile `integration/b-series` with F07 in coherent dependency groups, beginning with the 26 shared paths. Keep source-owner branches unchanged.
4. After each group, run exact-head CI, focused regression, and the complete cross-group test set. Any finding returns to its owning source branch, then is mirrored/reverified in the candidate.
5. Freeze the final candidate SHA only when every required source branch has an explicit included/superseded/excluded disposition and the integrated CI is green.
6. G02 can then build a traceable tested candidate APK. G03 is the actual Mi TV Stick installation/acceptance gate. Merge to `main` and create the final release build only after G03 acceptance and explicit user approval.

## 7. Draft integration PR #57 — conflict gate

A Draft PR was opened to expose the GitHub synthetic integration result without merging or changing either source branch:
- PR #57: `integration/project-f07-performance-stability` → `integration/b-series`.
- Exact base: `9070894fa6d9ef8aec034f46dfe777e866709dfe`.
- Exact head: `976e068d48ecc1ede2811ac455372395d382ba76`.
- GitHub reports **mergeable=false**. The PR is Draft/open/unmerged; 639 commits and 123 changed files are in its compare view. This is a hard stop for merge, not a warning to bypass.
- No conflict resolution was attempted by choosing one side wholesale. The 26 shared paths require three-way, behavior-level reconciliation and regression checks. PR #57 is only a diagnostic integration gate; it is not approved for merge.

## 8. Current decision

- **G01 defect-resolution subgate:** PASS for the previously audited source/host defects.
- **G01 project-wide integration reconciliation:** OPEN / BLOCKING. The B-series and F07 candidates are diverged; 26 overlapping paths need semantic reconciliation.
- **G02 final APK build:** NOT AUTHORIZED/NOT READY until the final isolated combined candidate has passed exact-head integrated CI.
- **G03 physical acceptance:** not started. No APK was published or installed and no TV/device state was changed.
- **No feature PR or main branch was merged in this checkpoint.** This preserves D-013 and the user's branch-ownership policy.


## 9. Semantic reconciliation checkpoint — 3 November 2026

**Disposition:** 26-path semantic review completed; isolated candidate created; exact-head candidate CI pending at checkpoint. G01 remains OPEN until the candidate CI and final cross-group review pass.

### Candidate refs and Git ancestry
- Candidate branch: `integration/g01-reconciled-candidate-20261103`
- Candidate exact head: `6ebd6284fcb21a74fed2a769868e91bc69ef7ae3`
- Candidate PR: #58, base `integration/b-series`, Draft/open/unmerged; GitHub reports `mergeable=true`.
- Candidate commit has three parents: F07 integration head `28e841a3475c144dd5ec85afb0976278274c2b5f`, B-series `9070894fa6d9ef8aec034f46dfe777e866709dfe`, and corrected F06 `2c369a0a48c77bb3bf023c292892541306e535a3`.
- Compare candidate against B-series: ahead 644 / behind 0; merge base is the exact B-series head. The branch is therefore a descendant of the B-series base and no longer has the original branch divergence.
- PR #57 remains a historical diagnostic conflict gate (mergeable=false); PR #58 is the new semantic reconciliation review surface. Neither is merged.

### 26-path semantic disposition

| # | Path | Reconciliation decision | Root-cause / regression guard |
|---:|---|---|---|
| 1 | `AppAdapter.java` | Retain F07/B-equivalent `TvFeedback.show` delivery. | Centralizes feedback delivery; same changed behavior in both branches. |
| 2 | `AppUpdateManager.java` | Retain F07 OTA implementation. | Preserves request/download timeouts, cancellation, HTTP status validation, complete-file check and sync/close ordering. Dialog width 560dp vs B-series 600dp is a presentation choice; verify on target TV. |
| 3 | `AvatarUploadServer.java` | Retain F07 implementation. | Preserves upload progress/timeout feedback and strengthens session expiry with monotonic elapsed time, per-request deadline, active-client accounting and cancellation handling. |
| 4 | `ChildModeManager.java` | Retain F07 implementation. | Includes B-series feedback call sites and adds synchronized profile-scoped state mutation/read paths; reduces profile-switch and concurrent preference races. |
| 5 | `DockDecoration.java` | Retain shared solid fallback drawing. | Both branches retain opaque tray, border, shadow and D-pad focus; only comment wording differs. |
| 6 | `FeedbackPolicy.java` | Retain identical shared helper. | Pure policy functions are unchanged between branches; covered by FeedbackPolicyTest. |
| 7 | `GlassDrawable.java` | Retain shared opaque fallback. | Both versions preserve panel surface/border when glass decoration is disabled; only comment wording differs. |
| 8 | `HomeSettingsActivity.java` | Retain shared `TvFeedback` migration. | Same feedback behavior in both branches. |
| 9 | `LocationBroadcastReceiver.java` | Retain shared `TvFeedback` migration. | Same localized status delivery; manifest permission hardening remains in F07 candidate. |
| 10 | `MainActivity.java` | Retain F07 integration version. | Preserves B-series feedback and adds D05/D07-safe StandBy collection operations, context-aware LAN address use, orphan-upload recovery and newer wallpaper/profile lifecycle behavior. |
| 11 | `ModernSettingsDialog.java` | Retain F07 integration version. | Includes B01 visual tokens/dialog contract plus focus path, tab/scroll retention and safer settings/installer/ADB flows. Replaced B-series toast fallbacks with `openDeviceSettingsOrFeedback`, phone-installer dialog and clipboard feedback. Profile dialog width differs; device visual acceptance remains open. |
| 12 | `NetworkFeedbackPolicy.java` | Retain F07 superset. | Keeps B-series timeout/download checks and adds monotonic session deadlines and shared upload-storage reservation policy. |
| 13 | `RecentAppsActivity.java` | Retain F07 version. | Preserves B-series `TvFeedback` and uses the shared HomeKeyPressTracker double-press window. |
| 14 | `TvDialogStyle.java` | Retain shared B01 modal contract. | Geometry, dimming and glass-off solid surface policy retained; only documentation comment differs. |
| 15 | `TvFeedback.java` | Retain identical shared helper. | Main-thread delivery and Activity lifecycle guard are the same in both branches. |
| 16 | `TvUiTokens.java` | Retain shared token values. | Same visual token values; comment wording differs only. |
| 17 | `UserSwitchDialog.java` | Retain F07 version. | Preserves B-series feedback/modal adoption and keeps profile actions aligned with the shared dialog contract. |
| 18 | `WallpaperUploadServer.java` | Retain F07 implementation. | Preserves B-series upload timeout/error UX and adds monotonic session/request deadlines, bounded clients, strict request framing, storage reservations, shutdown synchronization, partial-failure reporting and orphan upload cleanup. |
| 19 | `WeatherActivity.java` | Retain F07 implementation. | Includes shared modal/feedback behavior and adds reduced-motion and low-spec device behavior. |
| 20 | `values-en/strings.xml` | Retain F07 resource set with corrected Daily Verse copy. | Corrected English title/open/meaning/unavailable strings; test now checks actual values, not only key parity. |
| 21 | `values-id/strings.xml` | Retain F07 resource set with corrected Daily Verse copy. | Corrected Indonesian title/open/meaning/unavailable strings; test now checks actual values, not only key parity. |
| 22 | `values/strings.xml` | Retain Indonesian default fallback and all shared keys. | Default locale copy remains Indonesian; key and placeholder parity remain under F06 tests. |
| 23 | `FeedbackPolicyTest.java` | Retain shared test. | Same normalization, empty-message and Activity-lifecycle assertions. |
| 24 | `GlassSurfaceFallbackTest.java` | Preserve B-series test in candidate. | B-only path is not present in F07; candidate includes it. F07's distinct `GlassFallbackSurfaceTest.java` is also retained rather than silently deleting either project's coverage. |
| 25 | `NetworkFeedbackPolicyTest.java` | Retain F07 superset. | Contains B-series download/timeout assertions plus expanded deadline and storage-reservation boundary tests. |
| 26 | `TvDialogStyleTest.java` | Retain shared test. | Same width/height/WRAP_CONTENT geometry assertions. |

### Concrete defect found and corrected
The previous F06 gate checked locale key parity and a few sample strings, but did not validate all Daily Verse locale assignments. Four values were reversed/mixed:
- English resource had Indonesian title, action, meaning and unavailable text.
- Indonesian resource had English title, action, meaning and unavailable text.

Corrections were committed on F06 at:
- `60a63edcb84d94c19340dd1cdf2aaf49d6e726f6` — English resources.
- `f1dc301b5110ba2c2bf380ea6128368aa992b475` — Indonesian resources.
- `2c369a0a48c77bb3bf023c292892541306e535a3` — semantic locale assertions.

F06 exact-head CI on `2c369a0a48c77bb3bf023c292892541306e535a3`: run #41582796654, both required jobs SUCCESS; Python 89 tests / 1 intentional README-count skip; Android unit tests and release validation build PASS; APK 1,569,397 bytes / 1,572,864-byte cap; workflow artifacts empty.

### Candidate construction and checks
- Candidate tree uses the reviewed F07 implementation as the first-parent tree, adds B-series-only `GlassSurfaceFallbackTest.java`, and overlays corrected F06 locale resources and regression test.
- Candidate branch content spot-check confirms both fallback tests exist, English/Indonesian Daily Verse values are correctly assigned, and the upload monotonic deadline/storage reservation and Settings focus-retention implementations are present.
- A heuristic full-resource scan found no other obvious Indonesian phrases in English resources; three English terms in Indonesian strings are intentional technical/proper-name references (Open-Meteo, Allow from this source, US AQI).
- Candidate exact-head CI run #41583215114 (push) and #41583248216 (pull_request) was still running at this checkpoint. Do not mark G01 PASS until both required jobs on exact candidate SHA `6ebd6284fcb21a74fed2a769868e91bc69ef7ae3` finish successfully and cross-group review is complete.

### Current gate
- 26-path semantic disposition: COMPLETE at source review level; candidate tree created.
- PR #58 mergeable: TRUE; Draft/open/unmerged.
- Candidate exact-head CI: PENDING.
- G01 overall: OPEN pending exact-head CI and final review.
- G02: BLOCKED until G01 closes.
- No merge to `main`, no APK publication/installation, and no physical TV setting changes.


## 10. Integration merge and exact merge-commit acceptance — 3 November 2026

### Combine/merge result
- PR #58 (`integration/g01-reconciled-candidate-20261103` → `integration/b-series`) was merged successfully.
- Final integration merge commit: `f6f1f05b7a560381b8bb423eaa707a1d6d0e8832`.
- PR #58 state: CLOSED / MERGED. The accepted merge commit has parents B-series `9070894fa6d9ef8aec034f46dfe777e866709dfe` and reconciled candidate `6ebd6284fcb21a74fed2a769868e91bc69ef7ae3`.
- Candidate and final merge commit have identical Git tree SHA `a016951e69d99603a2a90722420cfe118c93eed1`; the merge commit introduces no source-tree drift from the CI-verified candidate.
- PR #57 was closed/superseded; its F07 head is included as an ancestor of the accepted candidate.

### Exact merge-commit CI — PASS
- Run #41583505195 on exact merge commit `f6f1f05b7a560381b8bb423eaa707a1d6d0e8832`: completed SUCCESS.
- Both required jobs SUCCESS: Launcher build and unit tests; Lists, tools and docs.
- Python suite: 99 tests, all OK.
- Android launcher and installer unit tests: PASS; release validation build: BUILD SUCCESSFUL.
- APK diagnostic: 1,571,293 bytes / 1,572,864-byte cap (1,571 bytes headroom).
- Workflow artifacts: empty. No APK published or installed.

### Main-branch divergence reviewed
- Compare `main` → `integration/b-series` remains diverged (integration has the project history; main has one commit not present in integration).
- The only main-only commit is `6eaf787c56226606b1d2e650c59540c553756c62`, message `ci: enable manual workflow dispatch from default branch`; it adds a manual `workflow_dispatch` input for publishing the TV-test APK.
- This is intentionally NOT copied into the integration branch because the accepted CI workflow is validation-only and has no artifact publication path. This is a deliberate release-safety exclusion, not an unresolved source conflict. Do not merge this main-only manual-publish change into the candidate before G03 acceptance and explicit release approval.
- `main` remains untouched.

### Final gate disposition
- 26-path semantic reconciliation: PASS.
- Isolated candidate exact-head CI: PASS.
- Integration merge into `integration/b-series`: PASS at `f6f1f05b7a560381b8bb423eaa707a1d6d0e8832`.
- Exact merge-commit CI: PASS.
- G01 source/host integration gate: **PASS**.
- G02 final tested APK candidate: **READY TO START** from the exact integration merge commit; no APK has yet been published or installed.
- G03 physical Mi TV Stick acceptance: NOT STARTED; remains a separate device-only gate.
- No merge to `main`; no TV setting changes.

## 11. Project-wide coverage re-audit and second isolated merge — 3 November 2026

### Legacy and roadmap coverage
- Approved roadmap remains 47 work items; E00 is a governance/inventory control, not an extra counted item.
- PR #2–#5 and #7–#8 ancestry is represented in the integration lineage. PR #6 is not merged as a whole branch; its avatar upload was selectively consolidated through PR #9's stronger profile-security implementation.
- The legacy `indonesia-pagar-alam` branch was checked: Azan, Location, PrayerTimes and WallpaperShuffle files match the combined tree. The combined tree has newer Android per-app language support, Indonesian localization, Malay-category retirement and bounded online-wallpaper cache.
- B/C/D/E owner branch tips that diverged by ancestry were reviewed as semantic deltas, not assumed missing. Candidate retains advanced F07 decode queues/shader reuse, D05 bounded StandBy decode, D06 boot recovery, D04 profile UUID/atomic built-in-avatar selection, C07 monotonic deadlines/storage reservation and shared UI contracts.
- The 26-path B-series/F07 disposition remains the controlling overlap matrix.

### Newly discovered gaps and resolution
- E05 current owner head `6fe89fad078692958db67030329a6de66cd7ae71` had location-aware per-source weather/AQI cache ownership not present in the prior candidate. Restored owner implementation and coordinate tests; retained F07 low-memory/reduced-motion behavior and F06 localized high/low summary.
- E08 source already contained truthful delete-failure handling and QR clear-failure callback, but its regression test was absent from the prior candidate. Added the owner regression test.
- First re-audit CI attempt #41586083313 failed on a source-contract anchor and F06 localized high/low assertion. Both were corrected; no failure remains in the final tested head.

### Final candidate and merge
- Re-audit PR #59 merged to `integration/b-series` at `562cde0751de93748eb4e0d4cdece4fa1672ac4d`.
- Tested head `9f2da8f39a20c8e8cc43ac0ff7e42d7a82b34676`; merge commit comparison: one commit ahead, zero file changes (identical tree).
- CI #41586223809 / run #1177 SUCCESS, both required jobs; 109 Python tests / one intentional skip; Android launcher/installer tests and release validation build PASS.
- APK 1,572,293 bytes / 1,572,864 cap / 571 bytes headroom. No artifacts or publication.
- Main-only commit `6eaf787c56226606b1d2e650c59540c553756c62` remains deliberately excluded; `main` untouched.

### Final gate
- G01: PASS.
- G02: READY TO START from `562cde0751de93748eb4e0d4cdece4fa1672ac4d`.
- G03–G05: NOT STARTED.
- Physical TV/phone/OEM acceptance remains G03; no APK installation/publication or TV state change has occurred.


## G01 exact-merge-commit CI gate — 3 November 2026 (latest recheck)

- Rechecked the exact integration merge commit `562cde0751de93748eb4e0d4cdece4fa1672ac4d` after the project-wide coverage audit and semantic conflict reconciliation.
- The tested PR candidate `9f2da8f39a20c8e8cc43ac0ff7e42d7a82b34676` has two completed successful CI runs: #41586223809 (run #1177) and #41586510603 (run #1178). Each run contains exactly the two required jobs, `Lists, tools and docs` and `Launcher build and unit tests`; both jobs concluded `success`.
- Git compare confirms the merge commit is one commit ahead of the tested candidate with zero changed files. This proves identical file tree, but it does **not** prove CI ran on the exact merge commit SHA.
- A direct exact-SHA workflow-run lookup for `562cde0751de93748eb4e0d4cdece4fa1672ac4d` returned no workflow runs; combined commit status returned no status entries. Therefore exact-merge-SHA CI is **NOT VERIFIED** and must not be marked PASS.
- A verification-only Draft PR #60 was created with head SHA `562cde0751de93748eb4e0d4cdece4fa1672ac4d` and base at the tested candidate. GitHub reported zero changed files and did not produce a CI run. PR #60 was closed unmerged; no source changes, main merge, APK publication/installation, or device changes occurred. Verification branch `verification/g01-exact-merge-ci-20261103` remains as an audit-only ref.
- The repository integration workflow supports push to `integration/**` and pull-request events, with two required jobs. The GitHub connector available in this session exposes no workflow-dispatch action. Do not invoke the main-branch manual publish workflow: its workflow file includes artifact publication and is intentionally excluded by release-safety policy.
- **Disposition:** coverage re-audit and semantic conflict resolution are complete; combined-tree audit is source/test PASS; candidate CI is PASS; exact merge-commit CI remains OPEN/BLOCKING. G01 is therefore **NOT FINAL**. G02 remains blocked until both required jobs complete successfully on exact SHA `562cde0751de93748eb4e0d4cdece4fa1672ac4d` (or a newly designated candidate SHA after any change). Keep owner branches and `main` untouched; no APK publication/installation or TV setting changes.
