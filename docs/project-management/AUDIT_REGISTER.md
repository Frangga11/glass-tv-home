# 04 — Audit and Quality Register

**Version:** 0.1-draft  
**Date:** 1 November 2026  
**Status:** Open register; not a final QC certificate

## Severity definitions

- **P0 — Critical:** data loss, boot failure, security exposure, or launcher cannot recover.
- **P1 — High:** core feature broken, unusable by remote, crash, or serious persistence issue.
- **P2 — Medium:** visible inconsistency, incomplete workflow, or degraded behavior with workaround.
- **P3 — Low:** polish, wording, minor alignment, or non-blocking enhancement.

## Current findings

| ID | Severity | Area | Finding / risk | Required verification | Status |
|---|---|---|---|---|---|
| A-001 | P1 | Avatar integration | Avatar QR source selectively integrated into Draft PR #9 on top of profile-security PR #8 | Complete physical TV/phone acceptance, then review merge order PR #8 → PR #9 | SOURCE + CI PASS — device gate open |
| A-002 | P1 | Avatar surfaces | Shared profile-scoped resolver is wired to top bar, Settings sidebar, active profile row, and profile switcher; add-profile starts with built-in avatar | Verify all surfaces and focus behavior on target TV | SOURCE + CI PASS — device verification open |
| A-003 | P2 | Avatar settings sidebar | Settings profile card now resolves the active custom photo | Verify after QR upload and after closing/reopening Settings | OPEN — device verification |
| A-004 | P2 | Avatar image orientation | EXIF-aware orientation handling and bounded decode are integrated | Verify portrait/landscape and mirrored EXIF samples on TV | SOURCE + CI PASS — device verification open |
| A-005 | P2 | Avatar QR availability | Dedicated QR endpoint replaces the picker flow; same-LAN reachability, port selection, token expiry, and dialog shutdown need device testing | Scan QR from phone, test same/different Wi-Fi, close dialog, and expiry | OPEN — device verification |
| A-006 | P2 | Avatar lifecycle | QR session is profile-scoped and short-lived; interrupted upload temp files are cleaned on next launcher start | Force-stop during upload and verify cleanup and profile identity | OPEN — device verification |
| A-007 | P2 | Avatar replacement | New photo is fsynced and profile reference committed before old unreferenced private file cleanup; failed metadata commit preserves old photo | Test two successive replacements and simulated storage/commit failure | SOURCE + CI PASS — device/failure-injection open |
| A-008 | P2 | Avatar management | New profiles start with built-in avatars; custom QR upload is available after selecting the profile in Settings | Deferred by design; verify post-creation flow; inline upload at creation is not release-critical | DEFERRED |
| A-009 | P2 | Avatar rendering | Downsampled rendering, center-crop, child/adult ring, EXIF orientation, and built-in fallback are implemented | Test portrait, landscape, large, unsupported, and corrupt images | OPEN — device verification |
| A-010 | P2 | Branch control | Avatar source selectively integrated in Draft PR #9 based on PR #8; PR #6/#7 not merged directly | Review merge order and complete device gates | OPEN — Draft PR review |
| A-011 | P1 | Real device | CI release APK exists, but no evidence yet of installation/test on user's actual Mi TV Stick | Install artifact from workflow run 41199199372 and execute AVATAR_AUDIT.md device checklist | OPEN — USER DEVICE TEST REQUIRED |
| A-012 | P1 | Wallpaper | Default wallpaper asset is packaged and verified in exact-head release APK; runtime behavior on clean install/offline/upgrade still unproven | Install artifact from CI #195; test offline cold start and legacy Malaysia/cache migration on target TV | PACKAGING PASS — device acceptance OPEN; W-F-02 |
| A-013 | P2 | Icon animation | Animation inventory and per-icon behavior not yet mapped | Inspect adapters, focus listeners, animators, and test on TV | OPEN |
| A-014 | P2 | Information layout | Exact on-screen order and focus navigation of clock/weather/AQI/prayer/Islamic shelf not yet recorded | Map view hierarchy and compare with user-approved screenshots | OPEN |
| A-015 | P2 | Weather/AQI | Source, location fallback, cache freshness, error/offline behavior not fully audited | Inspect weather services and test online/offline/stale data | OPEN |
| A-016 | P1 | Prayer times | Regional zone, date boundary, DST/timezone and accuracy need confirmation for Pagar Alam | Validate source/time zone and compare against authoritative local timetable | OPEN |
| A-017 | P1 | Wallpaper upload | Baseline QR exposed upload and gallery-management endpoints without per-session path token | Test foreign token, legacy routes, expiry, and dialog-close revocation on TV/phone | SOURCE + exact-head CI #195 PASS; device pending; W-F-01 |
| A-018 | P1 | ADB / debloat | Package disable and HOME-default changes need backup and rollback checklist | Verify scripts and prepare recovery procedure before device changes | OPEN |
| W-001 | P1 | Wallpaper/background | Source remediation, exact-head CI #195, and APK packaging inspection completed on PR #7 head `62b1e7c` | Complete TV/phone acceptance for W-AC-01–W-AC-12 | IN PROGRESS — CI + APK PASS; device acceptance pending; see WALLPAPER_AUDIT.md |

## Avatar candidate acceptance tests

1. Select a custom photo for the active profile.
2. Verify top-bar image updates immediately.
3. Close and reopen Settings; profile card and preview must show the custom photo.
4. Open profile switcher; correct custom image must appear for each profile.
5. Switch between two profiles with different custom photos; image identity must track active profile.
6. Restart launcher and reboot TV; photo remains available.
7. Replace photo; no stale or broken file remains.
8. Reset to built-in avatar; fallback animal appears everywhere and old custom file is cleaned up.
9. Delete a profile; only that profile's avatar file is deleted.
10. Test portrait, landscape, large JPEG, PNG, unsupported, corrupt, and EXIF-rotated images.
11. Test remote focus, OK, Back, and cancellation with no touch input.
12. Close the QR dialog and verify its session stops; test QR expiry after five minutes.
13. Test remote focus, OK, Back, and cancellation without touch input.
14. Confirm wallpaper and standby QR upload still work independently.

## QC closure rule

An audit item can be closed only with:
- exact branch and commit,
- source change or reason no change is needed,
- test procedure,
- observed result,
- evidence reference (CI log, screenshot, or device test note),
- rollback note where applicable.

Avatar source integration is in Draft PR #9 and exact-head CI #208 passed. No avatar item is release-closed until actual-device evidence is recorded.


## Profile Tab audit additions — 31 October 2026

| ID | Severity | Area | Finding / risk | Required verification | Status |
|---|---|---|---|---|---|
| P-001 | P1 | Avatar integration | Avatar QR implementation was isolated in PR #6 | Selective integration completed in PR #9, preserving PR #8 security/profile-scoped behavior | SOURCE + CI PASS — device acceptance open |
| P-002 | P1 | Parental controls | App lock/hide editor lacked PIN gate | Parent PIN now required before opening either editor in PR #8 | FIX IMPLEMENTED — CI #197 PASS; device verification open |
| P-003 | P1 | Profile deletion | Delete action lacked parent-PIN authorization | Parent PIN required before destructive confirmation in PR #8 | FIX IMPLEMENTED — CI #197 PASS; device verification open |
| P-004 | P1 | Parent PIN | Default PIN remains 0000 | User accepts 0000 temporarily and will change it later; no forced change in this scope | ACCEPTED TEMPORARILY — user to change later |
| P-005 | P2 | PIN security | No retry cooldown/backoff | User explicitly requires unlimited attempts; no throttle/lockout to be added | ACCEPTED BY USER — preserve unlimited attempts |
| P-006 | P2 | Avatar cleanup | Profile-scoped cleanup must not delete another user's photo or parental data | PR #9 limits avatar cleanup to unreferenced files in private avatar directory and clears only deleted profile's Child Mode keys | SOURCE + CI PASS — device verification open |
| P-007 | P2 | Regression tests | Focused profile/avatar tests were absent | PR #8 tests switch policy/key isolation; PR #9 tests QR routes, size/dimension limits and private avatar path containment | PARTIAL — CI #208 PASS; device/end-to-end verification open |
| P-008 | P2 | Device acceptance | D-pad, dialogs, profile switching, and restart persistence have no physical Mi TV evidence | Complete PROFILE_AUDIT.md device matrix | OPEN — user device required |

See [PROFILE_AUDIT.md](PROFILE_AUDIT.md) for detailed evidence and acceptance criteria.


## Profile security implementation — PR #8

| ID | Severity | Area | Finding / risk | Required verification | Status |
|---|---|---|---|---|---|
| P-009 | P1 | Profile switching | A child could potentially switch to a different child profile with weaker restrictions | Central switch gate now requires parent PIN for every switch away from an active Child Mode profile, regardless of target profile | FIX IMPLEMENTED — CI #196 pending; device verification open |

See `PROFILE_AUDIT.md` for the complete policy and device acceptance matrix. PR #8 is Draft and targets PR #7's branch; do not merge until CI, review, avatar integration, and device gates are complete.


## Latest Profile Security CI — 31 October 2026

- PR #8 exact head `1ba744efee61fde662493cebd382743a5927aefa` passed CI #197 (`41216508402`), both jobs successful.
- Release artifact `13048321564`: APK `app-release.apk`, 1,420,005 bytes; SHA-256 `735be1ff6e8ff88d31bf9de73940e527a570bbaace96c5ec9a001ce95610a44e`.
- This proves build/unit-test and packaging checks for the profile-security branch only. It does not prove physical remote behavior or avatar integration.


## PR #9 exact-head verification — 31 October 2026

| Check | Result |
|---|---|
| Exact head | `7e8392e1a76223b2c1aef314c10fa54d0b813fdc` |
| CI | [#208](https://github.com/Frangga11/glass-tv-home/actions/runs/41218171447) — PASS, both jobs |
| Release APK | 1,427,681 bytes; SHA-256 `f19584332514df03dbf54149f3f446211ab43c2dce00eae545ac768ded1ce315` |
| CI APK budget | PASS — below 1.5 MiB |
| Physical device | NOT RUN — required before merge/release |

PR #9: https://github.com/Frangga11/glass-tv-home/pull/9. Keep Draft; merge PR #8 base before PR #9 only after review and device acceptance.


## Post-installation test findings — 1 November 2026

These are user-observed findings from the latest Mi TV Stick test. They are triage inputs, not root-cause diagnoses. Task sequencing is tracked in [BUG_REPAIR_ROADMAP.md](BUG_REPAIR_ROADMAP.md). Do not close any item without reproduction, source-level diagnosis, a test procedure, and evidence.

| Finding ID | Area | User-observed symptom | Initial severity | Status |
|---|---|---|---|---|
| T-001 | Profile / avatar QR | Phone QR avatar upload fails; phone reports connection failure | P1 | OPEN — reproduce and trace QR/server/routes |
| T-002 | Profile / remote focus | No visible focus marker on avatar QR upload and built-in avatar choices | P2 | OPEN — global focus audit B02, then E02 |
| T-003 | Profile / app lock | Locked-app count remains zero/stale until leaving and reopening Settings | P2 | OPEN — state audit D02/D03, then E02 |
| T-004 | Home Screen settings | Previously available subtabs are missing from current settings UI | P2 | OPEN — compare inventory/history and restore only approved functions, E06 |
| T-005 | Appearance / wallpaper | Phone wallpaper upload fails with `Not Found`; photo management cannot be used | P1 | OPEN — trace C01–C07, D05, then E03 |
| T-006 | Information Center | Information Center / Quick Actions hierarchy and purpose are unclear; UI feels disorganized | P2 | OPEN — review information architecture, E05 |
| T-007 | Effects | Overall behavior looks good; remaining gaps/performance need review | P3 | OPEN — E07 |
| T-008 | Standby / screensaver | Upload appears possible but preview is black; location unknown; saved photos cannot be browsed clearly; overall flow fails | P1 | OPEN — trace storage/preview/location/playback, E04 |
| T-009 | Device / launcher settings | Default-home dialog layout is disorganized; launcher status/help relevance unclear; accessibility and remote actions show no handler | P1 | OPEN — inspect Android intents/TV capability and E01 |
| T-010 | Global navigation | After an action/back, focus/menu position resets and requires repeated navigation | P2 | OPEN — B01/B07 |
| T-011 | Global dialogs | Popup backgrounds are translucent enough that underlying text bleeds through | P2 | OPEN — B03 |
| T-012 | Localization | English mode still shows Indonesian strings | P2 | OPEN — B05 |
| T-013 | Wallpaper settings | User requests three subtabs: Change Wallpaper, Wallpaper Shuffle Interval, Next Wallpaper | P2 | APPROVED REQUIREMENT — implement in E08 |

Severity is provisional and may be adjusted after reproduction and impact analysis. Global dependencies must be addressed before feature-level patches where technically appropriate.


## B01 — Global navigation and cursor retention — 1 November 2026

| ID | Severity | Area | Finding / root cause | Remediation / required verification | Status |
|---|---|---|---|---|---|
| B01-001 | P2 | Home grid cursor | Focus identity and slot were held only in static process memory (`lastFocused`, `lastFocusedKey`); process death loses both | Persist stable component key + fallback slot in `navigation_state`; restore by component identity first and clamp fallback slot; verify force-stop/process recreation/reboot on Mi TV Stick | FIX IMPLEMENTED in Draft PR #10; exact-head CI #212 PASS; physical-device test OPEN |
| B01-002 | P2 | Settings navigation | Settings dialog initialized `activeTab` to Profile on every open | Persist selected tab and restore its sidebar cursor; verify each tab survives close/reopen and profile changes | FIX IMPLEMENTED in Draft PR #10; exact-head CI #212 PASS; physical-device test OPEN |
| B01-003 | P2 | Empty app grid | Empty-list focus fallback dereferenced Wi-Fi view without confirming it exists | Use null-safe fallback to Wi-Fi when visible, otherwise Settings; verify no-app/filtered-app state | FIX IMPLEMENTED in Draft PR #10; exact-head CI #212 PASS |
| B01-004 | P2 | Scroll-settle focus | If target holder was not attached, fallback could focus the first visible tile rather than the requested tile | Remove unrelated first-child fallback; retry requested, clamped position; verify long-grid navigation and rapid repeated D-pad input | FIX IMPLEMENTED in Draft PR #10; exact-head CI #212 PASS; physical-device test OPEN |
| B01-005 | P2 | Top-bar cursor | Returning from an external Android screen always restored a grid tile, even when focus had been on a top-bar pill | Persist a semantic top-bar key (inputs/wifi/assistant/candy_box/settings) and restore it if still visible/focusable; otherwise fall back to the grid; verify return behavior in final integrated test | FIX IMPLEMENTED in Draft PR #10; exact-head CI #212 PASS; physical-device test OPEN |
| B01-006 | P2 | Settings content cursor after actions | Exact focus within the Settings right-hand content pane after an action/re-render is a separate action-return/lifecycle concern | Track under B07, whose approved scope explicitly restores prior menu/subtab/focus after actions; test there rather than duplicating navigation state here | DEFERRED TO B07 — not a B01 blocker |

### B01 implementation checkpoint

- Source baseline: PR #9 exact head `7e8392e1a76223b2c1aef314c10fa54d0b813fdc`.
- Working branch: `feature/b01-global-navigation-retention`.
- Draft PR #10: https://github.com/Frangga11/glass-tv-home/pull/10.
- Code changes: `MainActivity.java`, `ModernSettingsDialog.java`, `NavigationState.java`, `NavigationStateTest.java`, and `.github/workflows/ci.yml`.
- Earlier exact-head CI #212 passed on the previous source snapshot; subsequent audit found additional focus-state edge cases and they have been corrected. The complete second-pass snapshot now passes exact-head CI #225.
- Current source head: `bf899c7c0300a40ad47f2d34c6cf0275c6bacdc5`; exact-head CI #225 PASS.
- CI #213 failed on an intermediate commit because the helper constant was renamed before dependent MainActivity references were migrated; this was corrected in subsequent commits. The final combined snapshot passed CI #225.
- `.github/workflows/ci.yml` now gates publication of the final TV-test APK behind an explicit manual workflow-dispatch boolean. Normal PR CI still builds/tests the release variant but does not publish a TV-test artifact.
- No APK has been installed on the TV and no device settings changed. Historical CI artifact #212 is a validation artifact only, not an approved TV-test build.
- B01 source remediation and exact-head CI are PASS; physical acceptance is deferred to the final integrated regression stage F02. Keep roadmap B01 IN PROGRESS until F02 acceptance. Settings right-pane action-return focus remains assigned to B07.


## B02 — Global D-pad / remote focus indicator audit — 1 November 2026

### Confirmed findings

| ID | Severity | Area | Finding / root cause | Remediation / verification | Status |
|---|---|---|---|---|---|
| B02-001 | P2 | Active-profile built-in avatar choices | Focus listener only scales the avatar frame; the white ring represents the selected avatar, not the currently focused avatar | Add a distinct high-contrast focus ring separate from the selected ring; test selected and unselected avatar focus independently | FIX IMPLEMENTED in Draft PR #11; exact-head CI #234 PASS |
| B02-002 | P2 | Avatar QR upload / reset buttons | Custom static GradientDrawable backgrounds have no focus-state change or focus listener | Apply consistent focused fill + visible outline + restrained scale; test both buttons with D-pad | FIX IMPLEMENTED in Draft PR #11; exact-head CI #234 PASS |
| B02-003 | P2 | Add-profile avatar choices | Focus listener only scales frames; selection ring and focus state are conflated | Add a separate focus ring while retaining the selected-avatar ring | FIX IMPLEMENTED in Draft PR #11; exact-head CI #234 PASS |
| B02-004 | P2 | Add/edit profile name fields | Custom static EditText backgrounds do not change border/fill on remote focus | Add visible focused border and retain text/cursor contrast; verify keyboard focus and text entry | FIX IMPLEMENTED in Draft PR #11; exact-head CI #234 PASS |
| B02-005 | P2 | Azan overlay card | Card is focusable/clickable but its custom glass background does not visibly change when focused | Add a clear focus stroke without changing the overlay's blur/opacity/lifecycle behavior | FIX IMPLEMENTED in Draft PR #11; exact-head CI #234 PASS |
| B02-006 | P2 | Weather daily forecast rows | Focus selector used only a low-opacity fill and had no outline, weaker than adjacent hourly/metric controls | Add a clear white focus stroke and slightly stronger focused fill while preserving row dimensions | FIX IMPLEMENTED in Draft PR #11; exact-head CI #234 PASS |

### Surfaces reviewed; existing focus treatment found

- Home app tiles: moving glass highlight and tile focus animation.
- Top-bar pills: shared glass/glider focus treatment.
- Main quick-action popups: `item_input.xml` uses focused selector.
- Prayer-time pill and weather pill: focused drawable selectors.
- Inline media controls: focused selectors for track info and round buttons.
- Recent Apps: app card changes its top pill on focus; Close All uses a focused cyan outline selector.
- WeatherActivity: hourly/daily items and metric cards use focused drawable selectors.
- LocationActivity: state pills and zone cards use focused state selectors.
- Settings search, profile rows, action rows and switch rows: visible focus background/border treatments.
- Profile switch cards, Add Profile container, Cancel and Save controls: visible focus treatments.

### B02 implementation checkpoint

- Source baseline: `feature/b01-global-navigation-retention` at `bf899c7c0300a40ad47f2d34c6cf0275c6bacdc5`.
- B02 must preserve B01 navigation/focus routing and must not replace existing focus listeners without preserving their behavior.
- Physical TV acceptance is deferred until all A–E audits/fixes and final integration stage F02.


### B02 implementation checkpoint

- Branch: `feature/b02-remote-focus-indicators`.
- Base: `feature/b01-global-navigation-retention` (B01 source/CI gate PASS).
- Draft PR #11: https://github.com/Frangga11/glass-tv-home/pull/11.
- Current head: `01cbef2c2fc90498295971b22c369a72cdfa734a`.
- Exact-head CI #234 PASS on current head `01cbef2c2fc90498295971b22c369a72cdfa734a`; both Launcher build/unit tests and Lists/tools/docs jobs passed; manual TV-test artifact publication was skipped. The final change moves the focus ring above the avatar image to ensure no overlap hides it.
- Files: `TvFocusIndicator.java`, `ModernSettingsDialog.java`, `UserSwitchDialog.java`, `AzanActivity.java`.
- No APK installed or published for TV testing; final device acceptance is deferred to F02.
- **B02 second-pass source review — PASS:** inspected all five changed files and relevant surrounding code at exact head `01cbef2c2fc90498295971b22c369a72cdfa734a`. The shared helper separates transient focus ring from selected-avatar marker; avatar frame animation behavior is retained in the helper; upload/reset buttons and profile-name fields receive focus styling; Azan card gets an explicit focus stroke and initial focus request; daily forecast rows retain their existing focusability/scale-elevation handling and now add a high-contrast selector outline.
- Listener interaction check: prior avatar-frame focus listeners were the simple scale-only listeners replaced by the helper; the changed EditText instances had no competing focus listener in their construction paths; Azan card had no previous focus listener; daily forecast focus animation is separate from the drawable selector. No source-level listener collision identified in the inspected paths.
- Exact-head CI run #234 / `41230495879`: Launcher build/unit tests and Lists/tools/docs jobs both PASS; release size guard PASS; manual TV-test artifact publication SKIPPED as intended. No artifact produced.
- B02 source review and CI gate: **PASS**. Keep B02 IN PROGRESS until integrated physical-device acceptance in F02.



## A04 — Repository, branch, source-tree and working-state audit — 1 November 2026

**Result: PASS — remote repository audit.**

| Check | Evidence | Result |
|---|---|---|
| Repository identity | `Frangga11/glass-tv-home`; public fork of `aqmangge/glass-tv-home`; default branch `main` | VERIFIED |
| Branch inventory | 13 remote branches and exact heads recorded in `INVENTORY.md` | VERIFIED |
| PR dependency graph | PR #8 → #9 → #10 → #11; all open Draft/unmerged | VERIFIED |
| Source tree | B02 head `01cbef2c2fc90498295971b22c369a72cdfa734a`; 647 entries, 496 files, 151 directories; not truncated | VERIFIED |
| Baseline tree | PR #9 head `7e8392e1a76223b2c1aef314c10fa54d0b813fdc`; 644 entries | VERIFIED |
| Android build configuration | Gradle root `launcher/`; AGP 8.5.2; app 4.0.0/code 40; min 26, compile/target 34; Java 17 | VERIFIED |
| Manifest and CI | HOME/Leanback declarations, permissions and components reviewed; CI build/test/size gate and manual-only TV artifact policy reviewed | VERIFIED |
| Local working tree | No local checkout/worktree was used or modified in this session; user's separate local clone cannot be inspected remotely | LIMITATION RECORDED |

**A04 result:** repository and remote source-state audit PASS. The limitation above is explicit; no claim is made that an external local clone has no uncommitted changes. Full evidence and source ownership map are in `INVENTORY.md`.

### A05 — Tested APK identity

**INITIAL STATUS — SUPERSEDED.** At the time of the first audit, the tested APK identity evidence had not yet been supplied. The later A05 section below records the completed direct artifact-to-source match.

### A06 — Baseline and rollback checkpoint

**INITIAL STATUS — SUPERSEDED.** A05 has since been completed and the exact source checkpoint branch/manifest created. The later A06 section below records the received device snapshot and completed rollback checkpoint.


## A05 — Tested APK identity match — 1 November 2026

**DONE — hash matched to exact CI artifact/source commit.**

The user uploaded `glass-tv-home-profile-avatar-test.apk` and confirmed it was the file used for the Mi TV Stick test.

| Evidence | Value |
|---|---|
| APK size | 1,427,681 bytes |
| SHA-256 | `f19584332514df03dbf54149f3f446211ab43c2dce00eae545ac768ded1ce315` |
| Matching CI artifact | `13048951728`, `glass-tv-home-pagar-alam-release` |
| CI run | #208, run ID `41218171447`; conclusion success |
| CI jobs | Launcher build/unit tests and Lists/tools/docs — both success |
| Source branch | `feature/profile-avatar-security-integration` |
| Exact source commit | `7e8392e1a76223b2c1aef314c10fa54d0b813fdc` |
| PR | #9, Draft/open/unmerged |

The CI artifact was downloaded from GitHub (artifact ID `13048951728`) and its ZIP archive was extracted. The extracted `app-release.apk` is 1,427,681 bytes and matches the user-uploaded APK byte-for-byte; both SHA-256 values are `f19584332514df03dbf54149f3f446211ab43c2dce00eae545ac768ded1ce315`. The ZIP archive is a different object: 1,340,733 bytes, SHA-256 `85475980a94b35a1a356cd5b5156bae3b50e7b405bc76ca1c22cd1e9557d32d6`. CI job logs independently show the release APK path `launcher/app/build/outputs/apk/release/app-release.apk` and report 1,394 KB (integer KB display). GitHub workflow metadata binds run #208 (`41218171447`) and artifact `13048951728` to branch `feature/profile-avatar-security-integration` and exact head `7e8392e1a76223b2c1aef314c10fa54d0b813fdc`; PR #9 has the same head. The APK contains `assets/kabut-pagi-dempo.jpg`; AGP metadata reports 8.5.2. Embedded VCS metadata says `NO_SUPPORTED_VCS_FOUND`; source attribution is therefore established by the independently verified CI run/artifact metadata and exact extracted APK match, not by embedded VCS metadata.

Evidence: https://github.com/Frangga11/glass-tv-home/actions/runs/41218171447 and https://github.com/Frangga11/glass-tv-home/pull/9.

## A06 — Baseline and rollback checkpoint — DONE

**Result: PASS — exact tested baseline, source checkpoint, rollback SOP, and read-only device snapshot recorded.**

- Official known-good tested baseline remains the user-confirmed PR #9 APK; its historical test is accepted and will not be requested again.
- APK identity: 1,427,681 bytes; SHA-256 `f19584332514df03dbf54149f3f446211ab43c2dce00eae545ac768ded1ce315`; byte-for-byte match with CI artifact #13048951728 / run #208; source head `7e8392e1a76223b2c1aef314c10fa54d0b813fdc`.
- Immutable checkpoint: `checkpoint/a06-tested-apk-pr9-7e8392e1`; rollback SOP: [A06_ROLLBACK_PROCEDURE.md](checkpoints/A06_ROLLBACK_PROCEDURE.md).
- Read-only snapshot captured 1 November 2026: `MiTV-AYFR0`, Android 11 / SDK 30, fingerprint `Xiaomi/soul/soul:11/RTT0.211222.001/1546:user/release-keys`; Glass TV Home package path, version 4.0.0/code 40, package lists, HOME resolver, accessibility/input/power settings recorded in the A06 checkpoint manifest.
- **E01 carry-forward:** HOME resolver returns `com.google.android.tvlauncher/.MainActivity`, while `dumpsys package` preferred activities lists `com.gathin.tvhome/.MainActivity`. Preserve this discrepancy for read-only investigation; do not change HOME state based on assumptions.
- Installed package version matches the known baseline's version metadata, but the installed TV APK was not pulled and hashed. Do not claim byte-for-byte installed-file identity.
- Exact package-name distinction: current disabled list contains `com.mitv.tvhome.atv`; the older historical output mentioned `com.mitv.tvhome`. Do not conflate these packages.
- No package state, HOME default, device setting, or installed APK was changed during snapshot capture.

**A06 closure:** DONE. Device mutation remains unauthorized; physical TV acceptance remains deferred to final stage F02.


## B03 — Dialog and popup opacity / layering — IN PROGRESS

**Initial source finding:** shared dialog surfaces allow underlying wallpaper/UI detail to bleed through at varying opacity levels.

| Surface | Before | Remediation | Scope |
|---|---|---|---|
| `bg_glass_dialog.xml` shared AlertDialog window | Gradient alpha ranged from 90% to 94% | Fully opaque dark gradient; rounded edge and subtle top sheen retained | All dialogs using `Theme_TvHome_GlassDialog`, including media hub and OTA dialog surfaces |
| `glass_panel.xml` | `#CC1E1E20` (80% opaque) | `#FF1E1E20` (fully opaque) | Card prompt and popup-card panels using this drawable |
| Phone QR upload panel in `MainActivity` | `0xF0131722` (94% opaque) | `0xFF131722` (fully opaque) | Avatar, wallpaper, and standby QR upload dialog content |

- Interactive `GlassDrawable` popups were inspected and intentionally left unchanged: they render a blurred copy of the wallpaper plus a subtle veil, not legible underlying application text; changing them to opaque would remove the intended glass treatment.
- Main settings and profile-switch dialog roots already use opaque custom backgrounds. Dialog dimensions, focus entry points, and dismissal/session cleanup were inspected. Added explicit D-pad selectors to AlertDialog buttons/list rows, bounded QR dialog dimensions with vertical D-pad scrolling, and clamped Now Playing/card-prompt widths to 90% of the viewport. Source and exact-head CI gate PASS; only physical TV acceptance remains for F02.
- Working branch: `feature/b03-dialog-opacity-layering`.
- Draft PR #12: https://github.com/Frangga11/glass-tv-home/pull/12; base is B02 head `01cbef2c2fc90498295971b22c369a72cdfa734a`; current head `56a4e1995aa8b8600614e6b5c4dca69d01c530c3`.
- Exact-head CI #239 / run `41233548406` on head `56a4e1995aa8b8600614e6b5c4dca69d01c530c3`: **PASS**, both jobs successful. Launcher unit tests and release build succeeded; release APK size 1,398 KB (<1.5 MiB); manual TV-test artifact publication was skipped and no artifact was produced. Manual TV-test artifact publication remains disabled unless explicitly dispatched after readiness approval.
- B03 source/CI gate: **PASS**. Keep roadmap B03 IN PROGRESS until physical acceptance in F02; no TV-test artifact was produced.


## B04 — Global layout, spacing, sizing and TV responsiveness — IN PROGRESS

**Initial source audit — no code change recommended until target display metrics are confirmed.**

| Surface | Source evidence | Assessment |
|---|---|---|
| Home grid | `AppAdapter.tileWidthPx()` calculates tile width from available grid width and configured column count; maintains 180:101 design ratio, caps at 180dp and has an 80dp floor. Grid uses weighted height and scrollable RecyclerView. | RESPONSIVE SOURCE DESIGN |
| Home hero / Islamic shelf | Hero width is derived from usable screen width (40% description / 45% title); Islamic shelf text is capped at 38% of usable width. | PROPORTIONAL WIDTH |
| Settings | Main dialog is clamped to 92% of display width/height; right pane is scrollable. Add-profile and device-settings dialogs clamp widths to viewport. | BOUNDED + SCROLLABLE |
| Weather | Full-screen ScrollView with fillViewport; bento body uses a fixed 385dp height and weighted columns/rows. Shorter viewports can scroll rather than clip the whole page. | SCROLLABLE; fixed design block requires visual check |
| Recent Apps | Horizontal RecyclerView; app cards are fixed at 420×250dp and root uses 64dp side padding. | POTENTIAL NARROW-VIEWPORT RISK; actual dp viewport not yet captured |
| Standby / Nightstand | Centered wrap-content layout; clock is fixed at 110sp and date at 19sp; no explicit width cap/auto-size in XML. | LOCALE / SMALL-VIEWPORT VISUAL CHECK |
| Dialogs | B03 now clamps custom dialog widths and QR dialog height to 90% of viewport; QR content scrolls. | BOUNDED |

- No source-level clipping defect was proven in the current target environment. The target's actual display size/density were not included in the A06 snapshot, so changing fixed card/clock design dimensions now would be speculative.
- Needed read-only device values: `wm size` and `wm density`. These are display metrics only; do not run `wm size` or `wm density` with arguments because that would change device configuration.
- B04 remains IN PROGRESS pending those two values and the final F02 visual acceptance. No layout changes were made in this initial audit.

## B01–B03 renewed final source quality check — 1 November 2026

**Disposition:** source review and exact-head CI PASS for all three modules. Physical device acceptance remains open for F02; do not mark roadmap tasks fully DONE before that gate.

| Module | Exact branch / head | Reviewed scope | CI evidence | Source QC |
|---|---|---|---|---|
| B01 | feature/b01-global-navigation-retention / bf899c7c0300a40ad47f2d34c6cf0275c6bacdc5 | Stable Home tile identity + fallback slot, semantic top-bar keys, RecyclerView reload snapshot, empty-list fallback, Settings tab/sidebar restore, navigation validation tests | CI #230 / run 41230264083; launcher unit tests + release build PASS; APK 1,395 KB; size gate PASS; publication step skipped; no artifact | PASS — source + CI |
| B02 | feature/b02-remote-focus-indicators / 01cbef2c2fc90498295971b22c369a72cdfa734a | Avatar selected-vs-focused ring separation, focus treatment for QR/reset buttons and name fields, Azan card focus, daily forecast focus; shared TvFocusIndicator | CI #234 / run 41230495879; launcher unit tests + release build PASS; APK 1,396 KB; size gate PASS; publication step skipped; no artifact | PASS — source + CI |
| B03 | feature/b03-dialog-opacity-layering / 56a4e1995aa8b8600614e6b5c4dca69d01c530c3 | Opaque dialog/panel/QR surfaces, AlertDialog button/list focus selectors, QR viewport cap + vertical scroll + dismiss cleanup, Now Playing/card-prompt width bounds | CI #239 / run 41233548406; launcher unit tests + release build PASS; APK 1,398 KB; size gate PASS; publication step skipped; no artifact | PASS — source + CI |

### Cross-module checks and carry-forwards

- Branch ancestry is intact: B01 → B02 → B03; PR #10 → #11 → #12. Each PR remains Draft/open/unmerged.
- B01 focus restoration uses stable component identity first and clamped slot fallback. Empty lists normalize the saved slot and use a safe top-bar fallback. Settings action-return focus within the right pane remains assigned to B07, not silently claimed as solved by B01.
- B02 focus styling remains independent from persistent selection styling. The shared helper is only attached to the intended avatar frames and rounded text controls; no source-level focus-listener collision was identified in the touched paths.
- B03 keeps the interactive blurred-wallpaper GlassDrawable treatment intentionally; only shared dialog/panel surfaces that need legible isolation were made opaque. The QR dialog retains its Close target and existing server-stop cleanup on dismissal.
- CI passing proves JVM tests/build/size and repository checks, not physical D-pad geometry, TV firmware behavior, or visual acceptance. All device tests remain deferred to F02.
- No candidate APK was installed, no TV settings/packages were changed, and no APK artifact was published by CI #230/#234/#239.

## B04 initial source audit — viewport-sensitive surfaces — 1 November 2026

- Home app tiles are already sized from available grid width and configured columns by AppAdapter.tileWidthPx(); Settings is clamped to 92% of display width/height; custom dialogs including OTA are clamped to viewport fractions; WeatherActivity is full-screen and vertically scrollable.
- Recent Apps uses fixed 420×250dp cards plus 64dp horizontal padding on the RecyclerView. This may exceed the usable viewport at some display densities/resolutions; it is a potential risk, not yet a proven defect.
- Nightstand view_nightstand.xml uses a fixed 110sp clock in a wrap-content centered row and has no explicit width cap/auto-size. This needs a viewport calculation and F02 visual check.
- WeatherActivity uses a fixed 385dp bento body inside a ScrollView; scrolling is available, but row/card proportions still need a visual pass.
- No speculative size changes were made. Read-only target values required: wm size and wm density with no arguments.

## B05 initial localization audit — 1 November 2026

- AppLanguage.java stores an app-level Indonesian/English preference and migrates the former ms setting to Indonesian. The selector exposes only Bahasa Indonesia and English.
- Android resources are not yet consistently localized: values/strings.xml (default) mixes Indonesian and English strings; values-en-rUS/strings.xml only overrides date_format; no values-id/strings.xml or generic values-en/strings.xml exists at the audited head. Other locale resource folders also exist and need a scope/selection audit.
- The app-level preference does not by itself switch Android resource locale. Only a subset of user-facing UI uses AppLanguage wrappers; other labels, dialogs, Toasts, dynamic states, and error messages remain hardcoded.
- A first-pass scan of 12 high-traffic Java files found 187 user-facing string-call patterns that require classification. This is a preliminary count, not a complete repository-wide inventory.
- B05 is IN PROGRESS. Required next: full inventory by screen/module, a single consistent Indonesian/English strategy, complete dynamic/error coverage, and regression tests for language switch + persistence.


## B04 expanded viewport audit — 1 November 2026

- \`AppAdapter.tileWidthPx()\` derives Home tile width from the available grid width and column count, keeps the 180:101 design ratio, caps at 180dp, and applies an 80dp minimum. On the target wide-screen TV this is likely suitable, but the minimum can become a lower-bound constraint on unusually narrow logical viewports.
- Recent Apps is the clearest fixed-geometry surface: each app card is 420×250dp; the horizontal RecyclerView also uses 64dp start/end padding, with a 64dp title/RAM-pill inset. Cards are not resized from available width in the inspected source.
- Nightstand content is centered and wrap-content; the clock is fixed at 110sp, date 19sp, with no explicit max-width or auto-size strategy. Long localized date strings are a potential width concern.
- Weather is a full-screen ScrollView with fillViewport, so the 385dp Bento body can scroll rather than forcing the entire page to fit. Its internal metric rows remain fixed-height (approximately 91–92dp); this is a visual-proportion risk on short logical viewports, not a proven clipping defect.
- No target display-size/density values were present in the A06 snapshot. No layout adjustment is justified until the user supplies read-only \`wm size\` and \`wm density\` outputs. Do not append arguments to either command.
- B04 remains IN PROGRESS; F02 remains the physical visual acceptance gate.

## B05 expanded localization inventory — 1 November 2026

**Audit scope:** exact B03 source head \`56a4e1995aa8b8600614e6b5c4dca69d01c530c3\`; all 66 app Java source files, all 18 app/installer layout XML files, all 13 app string-resource XML files, \`AppLanguage.java\`, \`locales_config.xml\`, app manifest, and app Gradle dependencies.

### Confirmed findings

- \`AppLanguage.java\` contains 90 static translation/helper methods. The preference supports \`id\` and \`en\`, defaults to Indonesian, and migrates legacy \`ms\` values to \`id\`. Many methods still retain unreachable Malay (\`LANG_MS\`) branches after normalization.
- The app has 44 default string-resource keys in \`values/strings.xml\`, but the default file mixes Indonesian and English.
- There is no \`values-id/strings.xml\`, no generic \`values-en/strings.xml\`; \`values-en-rUS/strings.xml\` contains only \`date_format\`. Other locale folders are partial: some contain 44 keys, while several contain only 17–25.
- \`locales_config.xml\` advertises 12 locales (en, de, ca, es, fr, ru, zh-CN, th, sr-Latn, pl, ar, tr) but omits Indonesian, while the in-app selector offers only Indonesian and English. This is inconsistent and advertises incomplete translations.
- The app language selector calls \`AppLanguage.setLanguage\`, refreshes the Islamic shelf/date and shows a Toast, but does not apply a locale to Android \`Resources\` or recreate/refresh all visible views. Resource-backed labels therefore do not reliably follow the selected language.
- A static scan across all 66 Java files found at least 95 direct literal UI-setter calls in seven high-traffic files (MainActivity 43, ModernSettingsDialog 17, UserSwitchDialog 15, WeatherActivity 9, AppUpdateManager 5, ChildModeManager 4, ClockDream 2). This is a lower bound: it excludes strings assembled into variables/builders, dynamic states, notification text, and XML literals.
- Eight launcher layout XML files contain hardcoded user-facing text; the installer module's layout also contains hardcoded Malay text. The installer is a separate companion-app scope and should not be silently conflated with TV launcher localization.
- The app has no AppCompat dependency. Since the target device is Android 11/API 30, relying only on Android 13 per-app language APIs would not solve the target-device requirement.

### Recommended implementation direction — not yet applied

- Keep the product language selector to Indonesian and English unless the user explicitly expands supported languages.
- Make \`AppLanguage\` the single persisted language preference and apply a locale-aware base context to every user-facing Activity/service surface on API 26–32; on API 33+, synchronize the system per-app locale where appropriate.
- Provide complete Indonesian and English resource coverage, align \`locales_config.xml\` with the actual supported languages, and migrate hardcoded XML/Java user-facing text into resources or a single translation layer.
- Language changes must refresh/recreate the relevant UI while preserving navigation/focus state; test language persistence, dynamic/error strings, notifications, dates, prayer labels, weather, and dialogs.
- Add automated resource-key/placeholder parity checks and tests for language normalization/migration. Physical cross-screen acceptance remains F06.
- B05 remains IN PROGRESS. No localization code or resource was modified during this inventory pass.
