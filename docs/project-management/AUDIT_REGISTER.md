# 04 — Audit and Quality Register

**Version:** 0.1-draft  
**Date:** 31 October 2026  
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
