# E02 — Profile, Avatar & App-Lock UI Audit

**Audit date:** 2 November 2026  
**Disposition:** SOURCE AUDIT + EXACT-HEAD CI PASS; PHYSICAL DEVICE ACCEPTANCE DEFERRED TO PROJECT F  
**Feature branch:** `feature/e02-profile-avatar-lock-ui`  
**Pull request:** [#35 — E02: audit profile avatars, QR upload and app-lock UX](https://github.com/Frangga11/glass-tv-home/pull/35)  
**Base:** `feature/e01-device-settings-home-control` @ `090a956a9860c82eb36c2d96848d47046fba0f0c`  
**Final source head:** `3ab9b55f3cfe03ad9841a119590a9183cf114e96`  
**Exact-head CI:** run #623 / `41406190380` — PASS (both jobs SUCCESS)

## 1. Scope

E02 owns Profile UI and its interactions: built-in/custom avatar, QR upload entry point, profile name and profile switch/delete dialogs, Child Mode, parent PIN gates, app-lock/hidden-app policy, counts, focus and persistence. It adopts B01's canonical modal/focus contracts and preserves C07 upload and D04/D07 state protections.

## 2. Source audit and corrections

| Area | Audit result / correction |
|---|---|
| Built-in avatars | Seven built-in choices retained. Selected-state ring is distinct from the transient cyan D-pad focus ring. |
| Custom avatar | QR flow remains profile-scoped. The active avatar refreshes across top bar, Profile card/sidebar and profile switcher after successful upload. |
| Avatar upload lifecycle | Dedicated route/token, five-minute session, one successful upload per QR, 15 MB cap, image bounds/content validation, private app storage, retry after failed upload, and server revocation on dialog dismissal/activity destruction are retained. |
| Avatar replacement/reset | Profile reference is committed before superseded-photo cleanup. Failed replacement preserves the previous avatar. Built-in selection now commits the new index and custom-photo reference removal in one transaction; failed persistence restores the prior in-memory profile values. |
| Profile isolation | Each profile owns its own custom avatar and Child Mode/locked/hidden package sets. Parent PIN remains device-wide. |
| PIN authorization | Editing lock/hide policy and profile deletion require parent PIN. Switching away from a Child Mode profile requires parent PIN, including child-to-child switching. |
| App enumeration | Fixed a source gap: the picker previously queried LEANBACK_LAUNCHER and only queried LAUNCHER if the first query was empty. It now merges both categories, deduplicates by package, excludes the launcher itself, and safely handles null labels/icons. |
| App policy counts | Saved package sets are package-scoped and deduplicated. Picker count changes immediately and the Profile summary refreshes on every dismissal path, including Back. The copy clarifies that saved rules apply while Child Mode is enabled. |
| Empty state / remote | Empty app list has an explicit message and focus moves to Done. Avatar, app rows, PIN keypad, profile options and QR close action expose deterministic focus styling/targets. |
| Dialog consistency | Profile-owned options, edit-name, PIN, app-policy, delete and QR dialogs use the shared B01 `TvDialogStyle` contract; no competing modal system is introduced. |

## 3. Regression and verification

- Added package-filter tests for duplicate, null, blank and self-package exclusion.
- Existing tests retained for package-set mutation/count behavior, profile-key isolation, Child Mode switch authorization, avatar private-path containment, built-in selection state, QR route matching, upload-size boundary and image-dimension limits.
- CI run #623 / `41406190380`: `Launcher build and unit tests` PASS; `Lists, tools and docs` PASS.
- `testReleaseUnitTest`, installer release unit tests and Release build all PASS.
- Diagnostic APK exact size: **1,561,717 bytes**. Budget: **1,572,864 bytes**. Headroom: **11,147 bytes**.
- Manual TV-test APK publication skipped; artifact count **0**.
- PR #35 remains OPEN / DRAFT / UNMERGED under D-013. No merge to integration or main.

## 4. Explicit decisions retained

- Parent PIN default remains `0000` temporarily by explicit user decision; user will change it later. No forced PIN change is introduced.
- PIN retry throttling, cooldown and lockout remain intentionally absent by explicit user decision.
- App-lock/hidden-app policy is saved per profile and takes effect while Child Mode is enabled.
- Do not change C07 upload limits/session contract or D04/D07 profile-state protections without a separately justified cross-project audit.

## 5. Project F acceptance — deferred, not waived

Physical Mi TV Stick + phone acceptance remains required:
1. Scan QR on same Wi-Fi; upload, replace twice, retry after invalid/interrupted upload, and verify the old photo remains on failure.
2. Verify QR expiry, single-success behavior, dialog-close revocation and activity/process restart.
3. Verify profile-specific avatar identity on top bar, Profile card and switcher; reset/delete one profile without affecting another.
4. Verify child-to-adult and child-to-child switching require PIN; wrong/cancelled PIN cannot open lock/hide editors or delete profiles.
5. Verify lock/hide rules, live counts, profile isolation, Child Mode activation and launcher tile filtering.
6. Test D-pad focus, Back/cancel, dialog sizing/clipping, scroll and focus return on the actual remote.
7. Verify wallpaper, StandBy and avatar upload routes/storage remain isolated.

No APK was installed, no TV setting or HOME default was changed, and no physical-device acceptance is claimed by this source audit.

## 6. Handover

**E02 source/CI gate: COMPLETE.** The next Stage E work unit may proceed to E03. Keep PR #35 Draft/open/unmerged until all A–G source audits/fixes are complete and grouped integration is authorized under D-013. Project F retains the device/phone/OEM acceptance checklist above.
