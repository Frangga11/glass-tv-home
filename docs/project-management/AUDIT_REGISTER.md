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
| A-001 | P1 | Avatar integration | Upload exists only on separate avatar-audit branch; not present on current settings/gallery branch | Select integration baseline; review diff; build and device test | OPEN |
| A-002 | P1 | Avatar surfaces | Candidate custom photo is used by top bar, but switcher and profile creation still render built-in avatars | Apply custom avatar consistently to all profile surfaces | OPEN |
| A-003 | P2 | Avatar settings sidebar | Profile card initially sets built-in resource; verify it loads custom photo at dialog open | Open settings after photo set and inspect sidebar | OPEN |
| A-004 | P2 | Avatar image orientation | Candidate import uses BitmapFactory without EXIF orientation correction | Test portrait photos from common phone cameras; correct orientation if needed | OPEN |
| A-005 | P2 | Avatar picker availability | Uses ACTION_OPEN_DOCUMENT and displays a message if no picker activity exists | Test on target Mi TV Stick firmware; decide QR fallback | OPEN |
| A-006 | P2 | Avatar lifecycle | Pending profile ID is held in an Activity field; process recreation while picker is open may lose it | Test Activity recreation / process death and restore pending state | OPEN |
| A-007 | P2 | Avatar replacement | File replacement uses temp/backup rename; inspect failure windows and old-photo recovery | Fault-injection / manual replacement test; verify metadata/file consistency | OPEN |
| A-008 | P2 | Avatar management | Add-profile dialog does not offer photo upload at creation time | Decide whether post-creation upload is sufficient or add creation flow | OPEN |
| A-009 | P2 | Avatar rendering | Verify center-crop, border ring, child/adult accent, image memory, and fallback | Test landscape/portrait/large/unsupported/corrupt images | OPEN |
| A-010 | P2 | Branch control | Need determine approved baseline and whether avatar branch is ahead/behind settings/gallery branch | Compare branch ancestry and commits; avoid accidental regression | OPEN |
| A-011 | P1 | Real device | No evidence yet of actual build/install/test on user's Mi TV Stick in this audit session | Run release build, install, and structured TV test | OPEN |
| A-012 | P2 | Wallpaper | Exact default wallpaper asset and fallback behavior not yet documented | Inspect drawable/assets and wallpaper selection code | OPEN |
| A-013 | P2 | Icon animation | Animation inventory and per-icon behavior not yet mapped | Inspect adapters, focus listeners, animators, and test on TV | OPEN |
| A-014 | P2 | Information layout | Exact on-screen order and focus navigation of clock/weather/AQI/prayer/Islamic shelf not yet recorded | Map view hierarchy and compare with user-approved screenshots | OPEN |
| A-015 | P2 | Weather/AQI | Source, location fallback, cache freshness, error/offline behavior not fully audited | Inspect weather services and test online/offline/stale data | OPEN |
| A-016 | P1 | Prayer times | Regional zone, date boundary, DST/timezone and accuracy need confirmation for Pagar Alam | Validate source/time zone and compare against authoritative local timetable | OPEN |
| A-017 | P1 | Wallpaper upload | QR upload server security, token validation, file validation, port fallback, and shutdown behavior need dedicated audit | Inspect full server and test same-LAN upload / invalid upload / server stop | OPEN |
| A-018 | P1 | ADB / debloat | Package disable and HOME-default changes need backup and rollback checklist | Verify scripts and prepare recovery procedure before device changes | OPEN |

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
12. Test picker missing/unavailable and verify graceful fallback.

## QC closure rule

An audit item can be closed only with:
- exact branch and commit,
- source change or reason no change is needed,
- test procedure,
- observed result,
- evidence reference (CI log, screenshot, or device test note),
- rollback note where applicable.

No item is closed in this initial draft.
