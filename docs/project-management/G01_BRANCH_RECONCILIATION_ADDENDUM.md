# G01 Addendum — Project-Wide Branch & Integration Reconciliation

**Checkpoint:** GTVH-G01-BRANCH-RECON-2026-11-03  
**Date:** 3 November 2026 (WIB)  
**Disposition:** SOURCE-FIX AUDIT PASS; FINAL COMBINE/BRANCH RECONCILIATION OPEN — G02 BLOCKED  
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

## 7. Current decision

- **G01 defect-resolution subgate:** PASS for the previously audited source/host defects.
- **G01 project-wide integration reconciliation:** OPEN / BLOCKING. The B-series and F07 candidates are diverged; 26 overlapping paths need semantic reconciliation.
- **G02 final APK build:** NOT AUTHORIZED/NOT READY until the final isolated combined candidate has passed exact-head integrated CI.
- **G03 physical acceptance:** not started. No APK was published or installed and no TV/device state was changed.
- **No feature PR or main branch was merged in this checkpoint.** This preserves D-013 and the user's branch-ownership policy.
