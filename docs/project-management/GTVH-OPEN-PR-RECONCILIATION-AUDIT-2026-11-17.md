# Open PR Reconciliation Audit — 17 November 2026

Repository: `Frangga11/glass-tv-home`
Audit baseline: `main` at `c1e4a5d02233ed60935ab11c11bba1017147580b`
Latest baseline CI: workflow run `44059379657`, exact head `c1e4a5d02233ed60935ab11c11bba1017147580b`, conclusion SUCCESS.

## Executive decision

- 68 previously reviewed PRs were closed as historical/superseded records. Their branches were not deleted.
- 40 PRs remain open.
- A fresh GitHub compare was run for each of the 40 open PR heads against `main`.
- All 40 comparisons report commits ahead of `main` and a diverged history. The ahead count is the number of commits unique to the PR side since the merge base; it is **not** proof that every behavior is still needed or that the code is safe to merge.
- Because the PRs are stacked on many stale feature bases and several overlap the same Android files, no blanket merge, retarget, branch deletion, or further blanket closure is authorized by this audit.
- The safe decision is to preserve the 40 branches and treat this inventory as a review queue. Reconcile source behavior and tests before closing or merging each PR.

## Open PR inventory

| PR | Workstream | Head branch | Commits ahead of main | Audit disposition |
|---:|---|---|---:|---|
| #134 | Prayer popup compact glass / inline Azan | `feature/home-prayer-popup-compact-azans-001` | 36 | Hold; compare popup behavior with integrated PR #135 before deciding superseded vs salvageable |
| #124 | Dock UP stability / focus outline | `fix/home-focus-outline-dock-up-stability-001` | 5 | Hold; stacked on #123; inspect workflow and focus-specific changes |
| #123 | Dock glass focus overlay | `fix/home-dock-glass-focus-overlay-001` | 9 | Hold; overlap with later cumulative DPAD handoff in #153 requires semantic comparison |
| #122 | DPAD burst serialization | `fix/home-dpad-burst-focus-serialization-001` | 32 | Hold; overlaps transition/focus state machine and Glider changes |
| #121 | Glass focus soft motion | `fix/home-glass-focus-soft-motion-002` | 27 | Hold; overlaps #120 and #122; verify timing/material against accepted main behavior |
| #120 | Glass focus motion integration | `fix/home-glass-focus-motion-integrated-001` | 22 | Hold; stacked geometry and motion refinement |
| #119 | 150dp row pitch / 182x100 card | `fix/home-row-pitch-150-card-182x100-001` | 16 | Hold; geometry overlaps accepted Home layout; compare exact current constants |
| #118 | Two-row DPAD transition latency | `fix/home-two-row-dpad-transition-latency-001` | 10 | Hold; timing superseded by later candidates may still be represented in branch history |
| #117 | Stable Dock anchor | `fix/home-two-row-stable-dock-anchor-001` | 7 | Hold; compare current Dock translation/layout behavior |
| #116 | UP collapse visibility | `fix/home-two-row-up-collapse-visibility-001` | 3 | Hold; compare current UP path and regression coverage |
| #109 | Header navigation symmetry | `fix/home-header-navigation-symmetry-001` | 34 | Hold; broad stale base with shared Home source changes |
| #107 | Islamic gold/orange restore | `fix/islamic-gold-orange-prayer-ayah-001` | 1 | Hold; verify current gold/orange tokens and tests before closure |
| #99 | Spacing and capsule-height QA | `verify/home-spacing-capsule-height-refinement-001-qa` | 19 | Hold; contains layout/geometry and contract changes |
| #93 | Islamic Shelf Arabic right alignment corrective | `fix/home-islamic-shelf-arabic-right-align-corrective-001` | 14 | Hold; compare current Arabic alignment and prayer labels |
| #92 | Islamic Shelf Ayah spacing and prayer label | `fix/home-islamic-shelf-ayah-spacing-and-prayer-label-001` | 11 | Hold; overlaps #91/#93 and current Islamic Shelf |
| #91 | Islamic Shelf Arabic right alignment | `fix/home-islamic-shelf-arabic-right-align-001` | 3 | Hold; inspect exact MainActivity/layout delta |
| #90 | Islamic/prayer stack APK artifact workflow | `ci/export-islamic-prayer-stack-apk-001` | 1 | Hold; workflow-only delta must be reconciled with current CI |
| #77 | Consolidated Home layout | `fix/home-layout-consolidated-001` | 5 | Hold; workflow-only compare delta; inspect PR patch before disposition |
| #64 | P4-01 Home/Dock isolated preference foundation | `feature/ui-global-01-home-dock` | 25 | Hold; contains Dock model/preferences and tests; do not discard without API/behavior comparison |
| #63 | E06 Dock capacity preference model | `feature/e06-dock-capacity-control-v1` | 3 | Hold; overlaps Dock preference model in #64 |
| #62 | GIPHY avatar catalog integration | `feature/e02-giphy-avatar-search-v1` | 19 | Hold; adds dependencies and avatar/catalog source; separate feature review |
| #51 | D01 location broadcast authorization | `fix/d01-location-broadcast-authorization` | 6 | Hold; security-sensitive manifest and contract changes require dedicated review |
| #41 | E08 wallpaper settings redesign | `feature/e08-wallpaper-settings-redesign` | 11 | Hold; MainActivity, upload server, localization and recovery tests |
| #38 | E05 Ayah rendering / information widgets | `feature/e05-information-center-ayah` | 21 | Hold; WeatherActivity/WeatherAirQuality and resource/test changes need comparison |
| #34 | E01 device settings / HOME control | `feature/e01-device-settings-home-control` | 3 | Hold; manifest/accessibility security and audit-doc delta |
| #30 | D06 prayer alarm process-death recovery | `feature/d06-process-death-recovery` | 10 | Hold; alarm persistence and dialog/manifest changes need focused audit |
| #29 | D05 StandBy photo atomic persistence | `feature/d05-standby-photo-atomic-persistence` | 9 | Hold; overlaps D-series persistence and dialog files |
| #28 | D04 profile/avatar state sync | `feature/d04-profile-avatar-state-sync` | 38 | Hold; broad profile/settings implementation |
| #27 | D03 locked/hidden app summaries | `feature/d03-locked-app-ui-refresh` | 22 | Hold; profile/app summary implementation |
| #26 | D02 immediate settings UI propagation | `feature/d02-settings-ui-propagation` | 5 | Hold; MainActivity/dialog/tokens and tests |
| #24 | C07 upload session resilience | `feature/c07-upload-session-resilience` | 35 | Hold; security/reliability-sensitive upload and storage changes |
| #20 | C03 phone-to-TV LAN verification | `feature/c03-phone-tv-network-verification` | 8 | Hold; branch contains broad stacked C-series source changes |
| #23 | C06 upload file validation | `feature/c06-upload-file-validation` | 8 | Hold; upload validation and shared server changes |
| #18 | C01 local QR upload server foundation | `feature/c01-local-communication-audit` | 8 | Hold; broad stacked branch; review server contracts before closure |
| #17 | B07 Settings menu/submenu DPAD focus | `feature/b07-menu-focus-restoration` | 29 | Hold; Settings navigation and dialog changes |
| #16 | B06 notification/network timeout | `feature/b06-notification-feedback` | 15 | Hold; notification and network feedback behavior |
| #11 | B02 remote focus indicators | `feature/b02-remote-focus-indicators` | 2 | Hold; shared dialog/token changes need visual behavior check |
| #10 | B01 navigation/cursor persistence | `feature/b01-global-navigation-retention` | 33 | Hold; broad HomeKey/navigation/service/manifest changes |
| #6 | Avatar QR upload replacement/cleanup | `fix/avatar-qr-upload-single-photo` | 28 | Hold; upload/profile lifecycle changes |
| #5 | Custom photo avatars for profiles | `fix/avatar-photo-upload-audit` | 8 | Hold; profile/avatar and user guide changes |

## Priority order for the next source-level reconciliation

1. **P0 — Home DPAD and Dock:** #116–#124, then compare against the accepted cumulative source in #153/#154 and the user's physical-device acceptance. Preserve the exact accepted behavior; do not reintroduce older motion timings or geometry.
2. **P1 — Prayer popup:** #134 versus the rebuilt popup implementation in #135/main. Determine whether #134 contributes any still-required behavior not present in the rebuilt popup. Do not transplant its old Dock changes.
3. **P2 — Islamic Shelf and typography:** #90–#93, #99, #107, #109. Compare actual current resource values, layout, localization, and contract tests.
4. **P3 — Dock architecture:** #63/#64 and #77. Check whether preference/model APIs are still needed or whether current Dock behavior intentionally superseded them.
5. **P4 — Security, upload, persistence, profile and settings:** #51, #62, #16–#18, #20, #23–#30, #34, #38, #41. Review one functional boundary at a time; prioritize manifest, upload/session, storage, and process-death correctness.

## Rules for disposition

- Close a PR only when its behavior is demonstrably superseded, its unique commits are either represented in main or intentionally rejected, and its branch history is preserved.
- Merge only after a fresh base, source-level conflict review, focused tests, exact-head CI, and user/device acceptance where UI/DPAD behavior is involved.
- Do not force-update or delete branches during this reconciliation.
- Do not treat commit-ahead counts as a measure of unique product functionality.
- Maintain the project rule: **RE-SKIN, NOT REWIRE** unless explicitly authorized.

## Validation and limitations

This audit verifies GitHub PR state, branch ancestry/compare metadata, changed-file summaries, and the latest main CI result. It does not claim that all 40 PR diffs have been fully reviewed line-by-line or that every behavior has been tested on the Mi TV Stick. Those are the explicit remaining gates.


## Reconciliation addendum — 17 November 2026

A focused source comparison was completed for the highest-risk Home workstreams:

- The PR #116–#124 branch family is an older, stacked DPAD/Dock/Glider implementation. The accepted cumulative implementation from the #153/#154 integration chain is already on main; current `AppAdapter.java`, `Glider.java`, and `DockGeometry.java` match the cumulative integration source, while the old PR branch snapshots are older source states. The family is therefore closed as historical/superseded, not merged.
- PR #134 is an earlier alternative compact Prayer popup implementation based on the old #123 branch. The rebuilt Prayer popup from PR #135 is already contained in main, so #134 is closed as superseded. No part of #134 was transplanted.
- PRs #116–#124 and #134 were closed with explanatory comments. Their branches were retained; no branch deletion, force update, or code merge occurred.
- This resolves the P0 Home DPAD/Dock and P1 Prayer popup queue for this cleanup pass. The remaining PR inventory is still open for focused review.

### Updated counts

- 68 PRs closed in the prior cleanup pass.
- 10 additional PRs closed in this focused source comparison.
- **78 PRs closed total; 30 PRs remain open.**
- 0 branches deleted; 0 new code merges during this cleanup pass.

The table above is the original 40-PR inventory snapshot. Treat entries #116–#124 and #134 as CLOSED / HISTORICAL according to this addendum. All remaining 30 PRs still require their own source-level disposition; do not infer they are obsolete solely from age or base divergence.

### Next review queue

Continue with #90–#109 (Islamic Shelf, typography, header, spacing, and CI), then the older feature/security branches (#5–#64). Prioritize security-sensitive manifest, upload-session, storage, process-death recovery, and profile state changes. No further blanket closure is approved.


## Additional P2 reconciliation — 17 November 2026

- PR #107 was closed after confirming that Islamic gold highlighting and the black text-shadow behavior are already present in current main. Its remaining contract-test snapshot expects an older Ayah focus implementation and was not transplanted.
- PR #90 was closed as an obsolete CI-only artifact allowlist change; current main already has the later cumulative QA artifact workflow.
- PR #77 was closed after review showed its remaining delta is workflow-only and adds a separate APK release-publishing workflow with `contents:write` on `pull_request`. This is outside the approved QA-artifact-only baseline and was not integrated.
- All three branches were retained. No source code, workflow, or release automation was merged from these PRs.
- **Updated totals: 81 PRs closed, 27 PRs remain open; 0 branches deleted.**

The remaining open queue is #109, #99, #93, #92, #91, #64, #63, #62, #51, #41, #38, #34, #30, #29, #28, #27, #26, #24, #23, #20, #18, #17, #16, #11, #10, #6, and #5. Continue source-level review in the order defined above. Security-sensitive changes must not be bulk-closed or bulk-merged.


## Additional P3 source reconciliation — 17 November 2026

A further source-level comparison was completed against the then-current main head 147568d4532e8abe6f2d736b3fcdc98007ed26d6.

### Closed as superseded by main

- #91, #92, #93, #99: Home Islamic Shelf/Ayah alignment, spacing, header, and Dock geometry changes are represented by newer implementations in current main. Arabic/translation width and direction logic, 30dp safe frame and shelf-to-tray spacing were checked in current source.
- #51: main already requires android.permission.DUMP on the exported LocationBroadcastReceiver and marks LocationActivity non-exported.
- #30: main already contains RECEIVE_BOOT_COMPLETED, AzanBootReceiver and the boot recovery policy.
- #28: main already contains ProfileManager.isBuiltInAvatarSelected() and the custom-photo override/restore ring refresh in ModernSettingsDialog.
- #63: DockPreferences and its capacity/orientation model are present in main.
- #23: UploadFilePolicy is present and used by current AvatarUploadServer and WallpaperUploadServer validation paths.
- #29: StandbyPhotoStore commits the uploaded URI collection and photo-mode flag together through commitUploadedState(); the upload result maps persistence failure to a non-success response.
- #64: DockPreferences, DockLayoutModel, AppInventory and TileBinder are all present in main, including stable component identity and partitioned Dock/Grid projections. Its proposed relaxation of the APK gate to 1.6 MiB was not adopted; main retains the stricter 1.5 MiB gate.
- #24: main contains UploadDeadlineInputStream and tests, monotonic elapsedRealtime session expiry, bounded client handling, synchronized upload shutdown/commit behavior, storage reservation, and orphan upload cleanup.

Each closed PR received an explanatory GitHub comment. Branches were retained. No source code was merged from these PRs in this reconciliation pass.

### Kept open after comparison

- #109: kept open because its NAV_SILVER navigation focus treatment is not present in current main's Glider implementation; requires separate design/behavior review.
- #26: kept open because its broader shared modal styling and UI propagation deltas are not fully established as superseded by current main source. No closure based on partial overlap.
- #18: kept open pending complete comparison of its local QR server route/security foundation and shared dialog changes against the later upload stack.

### Updated counts

- Previous verified total: 81 closed / 27 open.
- Additional closures in this pass: 12 (#91, #92, #93, #99, #51, #30, #28, #63, #23, #29, #64, #24).
- **Current total: 93 PRs closed; 15 PRs remain open.**
- 0 branches deleted; 0 code merges in this cleanup pass.

The remaining open inventory must be refreshed from GitHub before the next pass. Continue with unresolved UI/header item #109, shared modal/settings work #26, and foundational QR/server work #18, then review all other remaining PRs individually. Do not treat this focused subset as a full audit of all remaining open PRs.


### P3 follow-up — foundational QR/server PR #18

- PR #18 was closed after checking the current main versions of TvDialogStyle and its AppUpdateManager/UserSwitchDialog/WeatherActivity/MainActivity call sites, plus WallpaperUploadServer's Content-Length/path validators and their regression tests. These source elements are present in the cumulative main implementation.
- PR #26 remains open. Although main contains TvDialogStyle, the StandBy failure-aware setter, and TvUiTokens additions, the full set of its MainActivity dialog-size/custom-dimming call-site changes is not established as fully superseded. Keep it for a dedicated UI audit rather than closing on partial overlap.
- Updated totals: **94 PRs closed; 14 PRs remain open; 0 branches deleted; 0 code merges in this cleanup pass**.
- Refreshed open PR inventory: #109, #62, #41, #38, #34, #27, #26, #20, #17, #16, #11, #10, #6, and #5.


## Focused reconciliation — pass 5 — 18 November 2026

### Additional closures

The following PRs were closed after source-level comparison with current main; each received an explanatory GitHub comment, and no branch was deleted:

- #18 — local QR server foundation and shared dialog styling are represented in the cumulative upload/dialog stack.
- #20 — shared active-LAN IPv4 resolver and tests are present and used by QR upload services.
- #17 — SettingsFocusPath and persisted nested-focus restoration are present.
- #16 — NetworkFeedbackPolicy and upload timeout/feedback integrations are present.
- #11 — TvFocusIndicator and shared focus styling are present.
- #10 — HomeKeyPressTracker, NavigationState, HomeKeyService, RecentAppsActivity and the internal-only manifest boundary are present.
- #38 — Ayah open hint and centered weather divider are present in current Home/weather sources.
- #27 — older hidden-app refresh/modal snapshot is superseded by current cumulative inventory refresh and Settings shell.
- #5 and #6 — local-picker and QR custom-avatar flows are present in ProfileManager/MainActivity/AvatarUploadServer, including private storage and cleanup.
- #26 — StandBy mode persistence failure is handled by the boolean setter and localized failure feedback; shared dialog style is present.
- #34 — privileged WRITE_SECURE_SETTINGS declaration and startup system-settings mutation are absent; restart uses the supported visible power menu; DeviceSettingsStatus is present.

### Retained for deliberate review

- **#109** — NAV_SILVER focus style and header symmetry refinements are not present in current Glider/main layout. Keep separate for visual/behavior review; do not fold into the accepted DPAD handoff without a scoped plan.
- **#62** — GIPHY animated avatar catalog is a distinct new feature, including API key configuration and GIF playback. It is not present in main; retain for product/security/dependency review rather than closing as obsolete.
- **#41** — wallpaper settings redesign and startup category reconciliation are a distinct UI integration. Although the dialog class exists in main, the settings entry point and full behavior are not established as adopted; retain pending call-path and device review.

### Updated counts and boundary

- Previous checkpoint: 94 closed / 14 open.
- Additional closures: 11 PRs (#18, #20, #17, #16, #11, #10, #38, #27, #5, #6, #26, #34). Correction: 12 PRs closed in this pass.
- **Current total: 105 PRs closed; 3 PRs remain open: #109, #62, #41.**
- 0 branches deleted; 0 force updates; 0 code merges during this reconciliation pass. Main source code was not changed by PR cleanup.
- Exact current-main validation is pending for the documentation update; verify the resulting workflow run against the exact main head.
