# Glass TV Home — Operational Log

**Version:** 1.0  
**Updated:** 31 October 2026  
**Rule:** concise events only. Detailed findings and test matrices remain in the relevant audit.

| Date | Work item | Branch / PR / evidence | Event and result | Next action |
|---|---|---|---|---|
| 2026-10-31 | Documentation foundation | `docs/project-master-inventory` | GitHub selected as canonical record; ChatGPT Library retained as milestone backup; project docs organized modularly | Apply DOCUMENTATION_WORKFLOW.md |
| 2026-10-31 | Documentation standard D-007 | `DOCUMENTATION_WORKFLOW.md` | Approved three connected layers: Master, Audit/SOP, Operational Log; one authoritative home per fact | Use for all future work |
| 2026-10-31 | Avatar audit | [Draft PR #6](https://github.com/Frangga11/glass-tv-home/pull/6) | QR single-photo avatar implementation and CI pass recorded; actual TV/phone acceptance not yet evidenced | User device acceptance |
| 2026-10-31 | Wallpaper QR security W-001 / W-F-01 | [Draft PR #7](https://github.com/Frangga11/glass-tv-home/pull/7), [CI run 41200811301](https://github.com/Frangga11/glass-tv-home/actions/runs/41200811301) | Replaced bare-root wallpaper QR routes with per-server random session-scoped routes; added JVM route tests. CI PASS. PR remains draft. | Verify foreign/legacy route rejection and dialog-close revocation on TV/phone |
| 2026-10-31 | Dempo collection category | [Draft PR #7](https://github.com/Frangga11/glass-tv-home/pull/7), source commit `5829212cfae018aeca25b07e7f9e35ec8955122b`; [CI #159](https://github.com/Frangga11/glass-tv-home/actions/runs/41202506621) | Added `Kabut Pagi di Dempo` built-in category directly after Default and before Nature & Landscapes. It reads bundled assets from `assets/wallpapers/dempo/` and uses offline rotation. CI #159 in progress at last check. | Add JPEG binary at `launcher/app/src/main/assets/wallpapers/dempo/kabut-pagi-dempo.jpg`; verify APK and offline use |
| 2026-10-31 | Dempo visual acceptance | User supplied actual-TV photograph | Composition and legibility of the approved Dempo image with clock, weather, prayer information, doa, and app row accepted for the shown screen. Do not repeat this visual review. | Retain as W-AC-07 evidence; continue remaining non-visual and offline checks |


| 2026-10-31 | Wallpaper Default scope correction | [Draft PR #7](https://github.com/Frangga11/glass-tv-home/pull/7), commits `530c2dcc` and `fbab186c` | Removed mistakenly added Dempo tab/collection. Existing Default remains one tab; legacy Malaysia selections migrate to Default; loader refreshes one private cache from the single APK asset. CI #162 in progress. | Add JPEG at `launcher/app/src/main/assets/kabut-pagi-dempo.jpg`, verify APK and clean/upgrade/offline tests |
