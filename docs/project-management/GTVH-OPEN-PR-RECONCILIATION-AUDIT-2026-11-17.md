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
