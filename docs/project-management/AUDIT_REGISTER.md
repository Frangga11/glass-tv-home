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
| A-001 | P1 | Avatar integration | QR implementation is on dedicated branch and draft PR #6; not merged into settings/gallery baseline | Finish CI, device acceptance, then review/merge PR | OPEN — device gate; CI pass |
| A-002 | P1 | Avatar surfaces | Shared avatar resolver is wired to top bar, Settings profile card, and profile switcher; add-profile starts with built-in avatar | Verify all surfaces and profile switching on target TV | OPEN — device verification |
| A-003 | P2 | Avatar settings sidebar | Settings profile card now resolves the active custom photo | Verify after QR upload and after closing/reopening Settings | OPEN — device verification |
| A-004 | P2 | Avatar image orientation | EXIF-aware orientation handling added to avatar renderer | Verify portrait/landscape and mirrored EXIF samples on TV | OPEN — device verification |
| A-005 | P2 | Avatar QR availability | Dedicated QR endpoint replaces the picker flow; same-LAN reachability, port selection, token expiry, and dialog shutdown need device testing | Scan QR from phone, test same/different Wi-Fi, close dialog, and expiry | OPEN — device verification |
| A-006 | P2 | Avatar lifecycle | QR session is profile-scoped and short-lived; interrupted upload temp files are cleaned on next launcher start | Force-stop during upload and verify cleanup and profile identity | OPEN — device verification |
| A-007 | P2 | Avatar replacement | New avatar reference is committed before old private file is deleted; failed metadata commit preserves old photo | Test two successive replacements and simulated storage/commit failure | OPEN — failure-injection test |
| A-008 | P2 | Avatar management | New profiles start with built-in avatars; custom QR upload is available after selecting the profile in Settings | Deferred by design; verify post-creation flow; inline upload at creation is not release-critical | DEFERRED |
| A-009 | P2 | Avatar rendering | Downsampled rendering, center-crop, child/adult ring, EXIF orientation, and built-in fallback are implemented | Test portrait, landscape, large, unsupported, and corrupt images | OPEN — device verification |
| A-010 | P2 | Branch control | Branch ancestry checked; avatar candidate is eight commits ahead and has no commits behind settings/gallery baseline; QR work is isolated in draft PR #6 | Complete review and merge only after all gates pass | OPEN — PR review |
| A-011 | P1 | Real device | CI release APK exists, but no evidence yet of installation/test on user's actual Mi TV Stick | Install artifact from workflow run 41199199372 and execute AVATAR_AUDIT.md device checklist | OPEN — USER DEVICE TEST REQUIRED |
| A-012 | P1 | Wallpaper | Code expects `assets/kabut-pagi-dempo.jpg`, but CI release APK does not contain it; clean-install Default has no bundled image | Obtain/approve image, package it, verify APK asset entry, then test offline cold start | OPEN — user image/creative direction required; W-F-02 |
| A-013 | P2 | Icon animation | Animation inventory and per-icon behavior not yet mapped | Inspect adapters, focus listeners, animators, and test on TV | OPEN |
| A-014 | P2 | Information layout | Exact on-screen order and focus navigation of clock/weather/AQI/prayer/Islamic shelf not yet recorded | Map view hierarchy and compare with user-approved screenshots | OPEN |
| A-015 | P2 | Weather/AQI | Source, location fallback, cache freshness, error/offline behavior not fully audited | Inspect weather services and test online/offline/stale data | OPEN |
| A-016 | P1 | Prayer times | Regional zone, date boundary, DST/timezone and accuracy need confirmation for Pagar Alam | Validate source/time zone and compare against authoritative local timetable | OPEN |
| A-017 | P1 | Wallpaper upload | Baseline QR exposed upload and gallery-management endpoints without per-session path token | Review PR #7, then test foreign token, legacy routes, and dialog-close revocation on device | FIX IMPLEMENTED — CI PASS; device pending; W-F-01 |
| A-018 | P1 | ADB / debloat | Package disable and HOME-default changes need backup and rollback checklist | Verify scripts and prepare recovery procedure before device changes | OPEN |
| W-001 | P1 | Wallpaper/background | Dedicated audit opened; QR route remediation implemented in draft PR #7 | Complete source review, CI, then TV/phone acceptance for W-AC-01–W-AC-12 | IN PROGRESS — CI PASS; default asset/device pending; see WALLPAPER_AUDIT.md |

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

No avatar item is closed yet. The implementation has been submitted in draft PR #6; CI and actual-device evidence are still required.
