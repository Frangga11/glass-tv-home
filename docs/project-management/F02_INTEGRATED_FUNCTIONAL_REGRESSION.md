# F02 — Integrated Functional Regression & Controlled Android TV Acceptance

**Checkpoint:** 3 November 2026  
**Status:** OPEN — entry audit PASS; functional regression and physical acceptance IN PROGRESS / NOT YET PASSED  
**Governing policy:** D-013 remains active. No feature PR merge, release merge, APK publication or device installation is authorized by this checkpoint.

## 1. Trigger and objective

Project F02 begins after F01 source/dependency revalidation. Its purpose is to verify that the staged integrated application behaves as one coherent Android TV launcher, not merely that individual feature branches compile.

Required coverage:
- Main tabs, subtabs, menus, buttons, dialogs, popups and focus restoration.
- Remote/D-pad Up/Down/Left/Right/OK/Back/Close.
- Global app-owned modal consistency and Glass Effect ON/OFF fallback.
- Profile, Avatar, App Lock and Child Mode.
- Weather and AQI.
- Phone-to-TV QR upload, validation, cancellation, timeout and retry.
- Wallpaper Quick Control versus Settings > Appearance management routes.
- StandBy photo persistence, process death and reboot recovery.
- 1080p and 4K viewport/layout behavior.

## 2. Candidate and source verification

Selected F02 software candidate:
- Branch: `integration/project-f-staged-full`
- HEAD: `8fe90a9fdd493c5d81ffdacbe16054630a26580a`
- F01 tested synthetic candidate: `21c2eab34a3356d37d079eba04b281cc2373386d`
- Live ref comparison: current branch is 3 commits ahead, 0 behind, with no changed files reported. The application tree is unchanged; exact-tree CI evidence may therefore be reused.

B-series staged checkpoint:
- Branch: `integration/b-series`
- HEAD: `9070894fa6d9ef8aec034f46dfe777e866709dfe`
- Its history is divergent from the full staged candidate. Do not merge or force-sync the two branches blindly.

Targeted source-level cross-check between these candidates:
- `TvUiTokens`: fallback surface values `POPUP_SURFACE` and `DOCK_SURFACE` are identical; only descriptive comment text differs.
- `TvDialogStyle`: modal material selection, dimming, centering and viewport sizing behavior are identical; differences are import/comment text.
- `TvFeedback` and `FeedbackPolicy`: exact blob matches.
- The full staged candidate contains the request/session deadline constants and bounded-read/session-active policy in `NetworkFeedbackPolicy`; the B-series checkpoint does not. Preserve the full candidate's stronger deadline behavior and C07's `UploadDeadlineInputStream` protection.
- The full staged candidate contains `SettingsFocusPath`, its JVM test, and the associated `ModernSettingsDialog` focus-path integration; these files are absent from the B-series checkpoint.
- `MainActivity` and `ModernSettingsDialog` differ materially between the two branches. The full staged candidate is the selected F02 test target; do not overwrite it with the older B-series versions without a method-level semantic review.

## 3. Reused CI evidence — exact tree only

F01 integrated CI run #716 (workflow run ID `41470304782`) was rechecked:
- Launcher build and unit tests: SUCCESS.
- Unit tests and release build step: SUCCESS.
- APK size diagnostics and size guard: SUCCESS.
- Lists/tools/docs job: SUCCESS.
- Manual TV-test APK publication step: SKIPPED.

This is source/build evidence only. It is not proof of physical remote-control, popup rendering, QR transfer, lifecycle, or 1080p/4K acceptance. No APK was published or installed.

## 4. F02 regression matrix

| ID | Test area | Acceptance target | Status |
|---|---|---|---|
| F02-SW-01 | Exact candidate/build regression | Reuse CI only while exact tree remains unchanged | PASS — F01 CI #716; tree rechecked |
| F02-SW-02 | Shared dialog/fallback contract | Shared modal sizing/material; Glass ON/OFF solid fallback | SOURCE CHECK PASS; device pending |
| F02-SW-03 | Settings focus persistence | Valid nested focus path; safe fallback for stale/malformed path | JVM test included in candidate CI; device pending |
| F02-SW-04 | Upload resilience | Preserve request deadline, idle timeout, expiry, cancellation and upload isolation | SOURCE CHECK PASS; phone/TV test pending |
| F02-TV-01 | Remote navigation | D-pad, OK, Back, Close, focus restoration across tabs/subtabs/popups | NOT RUN |
| F02-TV-02 | Global popup/dialog UI | Consistent size, centering, dimming, focus and Glass ON/OFF | NOT RUN |
| F02-TV-03 | Profile/security | Avatar, profile switch, App Lock counts, Child Mode and PIN gates | NOT RUN |
| F02-TV-04 | Weather/AQI | Home overlay, full Weather screen, AQI, reduced motion and lifecycle | NOT RUN |
| F02-TV-05 | Phone-to-TV QR | Discovery, upload, replace, cancel, timeout, retry and cleanup | NOT RUN |
| F02-TV-06 | Wallpaper routes | Quick Control stays compact Change/Shuffle/Next; Appearance retains Collection/Interval/Personal Manager; shared state remains consistent | NOT RUN |
| F02-TV-07 | StandBy | Photo add/remove, atomic persistence, rotation and fallback | NOT RUN |
| F02-TV-08 | Recovery | Activity/process death and actual TV reboot recovery | NOT RUN |
| F02-TV-09 | Viewport | 1920×1080 and 4K layout, focus reachability and modal bounds | NOT RUN |

## 5. Current decision and blockers

- F02 entry audit: PASS.
- Selected software candidate: `integration/project-f-staged-full` at `8fe90a9fdd493c5d81ffdacbe16054630a26580a`.
- No code changes were made during this preflight.
- No physical Android TV test is claimed. A distributable TV-test APK has not been published; device installation is not performed by this checkpoint.
- The next acceptance step requires a controlled test APK from the selected candidate and a physical run on the target Android TV/remote plus phone on the same LAN. Record each case as PASS/FAIL/BLOCKED with screenshots/logs and exact build SHA.
- Any reproducible defect must be assigned to its owning module after root-cause verification; only affected tests/dependencies are rerun.
- Feature PRs remain open/draft/unmerged as applicable. No main/release merge. D-013 remains binding.

## 6. Exit criteria

F02 cannot close until all applicable functional cases are evidenced, device-specific issues are repaired and retested, the final candidate SHA is frozen, and residual risks/acceptance limits are recorded. CI PASS alone is insufficient.
