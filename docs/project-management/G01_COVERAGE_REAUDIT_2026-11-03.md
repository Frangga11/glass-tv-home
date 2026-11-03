# G01 Project-Wide Coverage Re-Audit

**Date:** 3 November 2026 (WIB)
**Disposition:** PASS — source/host re-audit complete; isolated integration correction merged.
**Tested candidate:** `9f2da8f39a20c8e8cc43ac0ff7e42d7a82b34676`
**Integration merge:** `562cde0751de93748eb4e0d4cdece4fa1672ac4d` on `integration/b-series`
**PR:** #59 — merged.
**Exact tested-tree CI:** #41586223809 / run #1177 — SUCCESS, both required jobs.

## 1. Scope coverage against the approved 47-item A–G roadmap

| Stage | Coverage result | Boundary |
|---|---|---|
| A01–A06 | DONE | Tested APK identity and immutable A06 checkpoint retained; A06 checkpoint branch contains rollback/checkpoint documentation, not a second application implementation. |
| B01–B07 | SOURCE/CI COMPLETE | Global navigation, D-pad focus, dialogs/surfaces, responsive layout, localization, feedback and lifecycle/focus restoration are present in the combined tree. |
| C01–C07 | SOURCE/CI COMPLETE | QR, LAN/server lifecycle, endpoint contracts, upload validation, deadlines, cancellation, storage reservation and shutdown atomicity are present. |
| D01–D07 | SOURCE/CI COMPLETE | Settings ownership, immediate propagation, app-lock refresh, profile/avatar state, StandBy collection persistence, process recovery and cross-profile concurrency controls are present. |
| E01–E08 | SOURCE/CI COMPLETE | Device/launcher controls, profile, wallpaper, StandBy, information/weather, Home subtabs, animation/effects and wallpaper settings are present; E05 and E08 gaps below were reconciled in this re-audit. |
| F01–F07 | HOST/SOURCE EXIT PASS | Integrated source/host regressions and CI pass. Real phone/LAN, OEM, remote, process-death/reboot and Mi TV Stick performance acceptance remain G03. |
| G01 | PASS | Coverage re-audit, conflict resolution, isolated merge and exact tested-tree CI complete. |
| G02 | READY TO START | Build a traceable candidate from merge commit `562cde0751de93748eb4e0d4cdece4fa1672ac4d`; no APK has been published or installed. |
| G03–G05 | NOT STARTED | Physical TV acceptance, final documentation closure and continuity handover remain pending. |

The approved roadmap remains **47 work items**. E00 is a completed inventory/governance control, not an additional counted roadmap item.

## 2. Legacy baseline and branch/PR coverage

- PR #1 is merged. The legacy `indonesia-pagar-alam` branch was compared with the combined tree: Azan, Location, PrayerTimes and WallpaperShuffle source files match; the combined tree has newer Indonesian/Android per-app language handling, localized LocationActivity, Malay-category retirement and a bounded wallpaper cache.
- PR #2–#5 and #7–#8 have ancestry represented in the integration lineage. PR #6 was not merged as a whole branch; its avatar-upload work was selectively superseded/consolidated through PR #9's profile-security integration. The compared PR #9 implementation retains stricter profile-private storage, safe file containment and lifecycle/session handling.
- B01–B07, C01–C07, D01–D07 and E01–E08 owner heads were checked against the candidate. Branch divergence was not treated as proof of missing code: shared UI/lifecycle files were compared semantically, source/test presence was checked, and later F07 implementations were retained where they add bounded queues, shader reuse, stronger persistence, timeout or storage controls.
- The 26-path B-series/F07 overlap matrix remains the authoritative per-file disposition in `G01_BRANCH_RECONCILIATION_ADDENDUM.md`. No wholesale ours/theirs resolution was used.
- F02–F07 integration ancestry is preserved. The combined branch retains the F02–F07 cross-project contracts and their regression tests.

## 3. Findings corrected during this re-audit

| Finding | Root cause | Resolution |
|---|---|---|
| E05 stale weather/AQI cache across location changes | The previous combined tree had a shared freshness timestamp and no cached-coordinate ownership for each source. | Restored per-source success timestamps and cached coordinates from E05 owner head `6fe89fad078692958db67030329a6de66cd7ae71`; stale-location cache is rejected in Home and full Weather dashboard. |
| E05 weather text could follow manual visual mood instead of API condition | Candidate presentation semantics had diverged from E05's data-backed display. | Retained API-backed weather text; manual mood controls only the visual background. F07 reduced-motion and low-memory behavior is preserved. |
| E05 high/low labels were hard-coded | Owner update used literal H/L text and would fail F06 localization contract. | Uses `weather_high_low_summary` resource; missing weather-unavailable/model-reference resources added selectively in default/ID/EN without copying the older E05 locale snapshot. |
| E08 deletion-failure regression test absent from combined tree | Source reconciliation existed, but the owner regression test was not carried into the prior candidate. | Added `tests/test_wallpaper_delete_failure_feedback.py`; it covers single delete, bulk delete, keep-one/delete-others, partial-state reconciliation and QR clear-failure callback. |

E05 owner exact-head CI #41433527557: both required jobs SUCCESS. E08 deletion source behavior was already present in the combined tree; the missing regression guard was restored.

## 4. Final combined-tree verification

- PR #59 merged into `integration/b-series` at `562cde0751de93748eb4e0d4cdece4fa1672ac4d`.
- Compared merge commit to the CI-tested PR head: one merge commit ahead, **zero changed files**. The merged tree is identical to tested head `9f2da8f39a20c8e8cc43ac0ff7e42d7a82b34676`.
- CI #41586223809 / run #1177: **SUCCESS**, both jobs.
- Lists/tools/docs: **109 Python tests passed**, one intentional README-count skip.
- Android launcher/installer unit tests: PASS. Release validation build: SUCCESS.
- APK diagnostic: **1,572,293 bytes** / 1,572,864-byte cap; **571 bytes headroom**. Size gate passes, but the remaining margin is very narrow and is a G02 watch item.
- Workflow artifacts: empty. APK publication skipped; no APK installation or TV setting change.
- No separate workflow run was triggered on the merge commit; its tree is byte-for-byte the same Git tree as the exact-head CI-tested candidate.

## 5. Governance and remaining gates

- `main` remains untouched. Its only main-only commit `6eaf787c56226606b1d2e650c59540c553756c62` enables manual workflow-dispatch APK publishing and remains deliberately excluded.
- Owner branches are preserved. PR #58 and PR #59 are merged only into the isolated `integration/b-series` branch; no merge to `main`.
- G01 source/host gate: **PASS**. G02: **READY TO START** from `562cde0751de93748eb4e0d4cdece4fa1672ac4d`.
- G03 physical Mi TV Stick acceptance is NOT STARTED. No D-pad/OEM/phone-LAN/process-death/reboot/1080p/4K/thermal/soak claim is made.
- No APK publication, installation, HOME-default change, ADB/device mutation or TV setting change has occurred.