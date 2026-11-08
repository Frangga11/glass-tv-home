# GTVH-HOME-ISLAMIC-SHELF-AND-PRAYER-STACK-CROSS-AUDIT-001

**Date:** 9 November 2026  
**Status:** SOURCE CROSS-AUDIT PASS · ALL SIX EXACT-HEAD CI RUNS PASS · PHYSICAL DEVICE ACCEPTANCE PENDING · NOT FINAL

## Scope and authority
Repository: `Frangga11/glass-tv-home`  
Audited stacked PRs: #84 → #85 → #86 → #87 → #88 → #89.  
All six PRs remain OPEN / DRAFT / UNMERGED. GitHub reports mergeable=true for each individual PR; this is not a statement that the entire dependency stack is ready to merge into main.

## Exact-head validation matrix

| PR | Exact HEAD | CI run | Python suite | Android Release build | APK diagnostic |
|---|---|---:|---:|---|---:|
| #84 | `d821842c2211cb6e65bbde506253cde7b3929771` | #1639 / `42523095371` | 184 / 1 skipped | PASS | 1,625,313 B |
| #85 | `a5eaae843f8998c53238870609102e4c20b05ded` | #1640 / `42523437010` | 188 / 1 skipped | PASS | 1,625,337 B |
| #86 | `746beaba2648b167dc174dfd3a61f5e859ed9fc2` | #1642 / `42524091486` | 192 / 1 skipped | PASS | 1,625,865 B |
| #87 | `91f1152361d9b4db70a74f8f6b30ad38f60b4211` | #1647 / `42524629431` | 198 / 1 skipped | PASS | 1,626,169 B |
| #88 | `a4b979540def5fc1414e66eec6df516a1ef22fe8` | #1649 / `42524805149` | 201 / 1 skipped | PASS | 1,626,381 B |
| #89 | `4038c08f4825f7bacf965bc6708505d3c8c30ebc` | #1649 / `42525047258` | 202 / 1 skipped | PASS | 1,626,745 B |

For all six runs, both CI jobs completed successfully. Android `testReleaseUnitTest` and `assembleRelease` completed successfully. APK sizes are diagnostics from the validation build; publisher workflows were skipped because PRs remain Draft, so no published APK release asset is claimed.

## Cross-audit source checks at PR #89 exact HEAD

| Check | Result |
|---|---|
| Islamic Shelf has one vertical-position layout listener/writer | PASS — one listener found |
| Runtime Arabic `ayahArabic.setLineSpacing()` override | PASS — absent |
| Arabic XML spacing values `-1dp / 0.94` retained | PASS |
| Prayer schedule after Isya resolves tomorrow Subuh | PASS — shared tomorrow lookup is used |
| Prayer sync uses process single-flight guard | PASS |
| Daily ayah cache uses exact WIB epoch-day identity | PASS |
| Daily ayah refresh uses persisted 30-minute retry cooldown | PASS |
| Daily ayah fetch uses process single-flight guard | PASS |
| Home avoids starting a refresh worker during cooldown | PASS |

## Workstream closure within current scope

- **PR #84:** removed the competing legacy shelf-position writer; one measured anchor remains.
- **PR #85:** adjusted Arabic color, two-line spacing and shadow. Runtime override found during cross-audit was removed in PR #87.
- **PR #86:** refreshes prayer timetable checks on resume/minute tick with cooldown and single-flight; after Isya all schedule views use tomorrow's Subuh.
- **PR #87:** accurately names the existing content Ayat Pilihan Harian, distinguishes current/saved/local fallback, validates incomplete payloads, fixes cache identity for the 52-verse rotation and restores XML Arabic line spacing. No separate daily-dua module is in scope.
- **PR #88:** makes Kemenag-vs-local-estimate source mode visible for Indonesian prayer zones, reusing the existing official-data validator.
- **PR #89:** prevents repeated offline verse requests through a persisted 30-minute cooldown and single-flight worker.

## Remaining acceptance gates — not code-complete claims

1. Physical Mi TV Stick acceptance at 1920×1080 / density 320. No physical install, ADB capture, screenshot or remote-control test was performed during this audit.
2. Verify Islamic Shelf stays vertically stable while cursor moves across every adjacent Dock icon and between Dock/Shelf.
3. Verify Arabic verse at one and two lines, including harakat, on bright and dark wallpapers; no clipping, overlap or unexpected ellipsis.
4. Verify prayer times at before Subuh, after Subuh, sunrise, Maghrib and after Isya; confirm Home compact summary, shelf rows and full schedule agree.
5. Keep device offline for more than 30 minutes; verify no minute-by-minute Quran fetch storm, saved verse label remains correct, and online recovery refreshes the current WIB day.
6. Confirm the source label says KEMENAG RI only with the validated Pagar Alam annual cache and otherwise shows local estimate.
7. Reconcile the known upstream dependency chain PR #70–#72 and the #77 ancestry discrepancy before planning any merge into main. Do not merge these six PRs merely because their individual checks are green.

## Decision
**AUTOMATED SOURCE GATE: PASS. PHYSICAL ACCEPTANCE: PENDING. FINAL MERGE: BLOCKED.**

No further code change is recommended within the current Ayat Pilihan Harian scope until physical acceptance identifies a reproducible defect. A separate daily-doa module is explicitly out of scope per the user's decision.
