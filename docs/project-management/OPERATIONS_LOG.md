# Glass TV Home — Operational Log

**Version:** 1.0  
**Updated:** 1 November 2026  
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

| 2026-10-31 | Exact-head CI and APK verification | [PR #7](https://github.com/Frangga11/glass-tv-home/pull/7), [CI #195](https://github.com/Frangga11/glass-tv-home/actions/runs/41214569233), artifact `13047428749`, head `62b1e7ce63f34095b44afbde7fc80e19addc9b00` | Both CI jobs passed on the exact current PR head. Downloaded artifact and inspected APK: 1,419,677 bytes; exactly one image asset `assets/kabut-pagi-dempo.jpg` (230,883 bytes; 1536 × 864); embedded SHA-256 matches approved image. No duplicate image asset. | Physical Mi TV Stick + phone acceptance; keep PR #7 Draft |

| 2026-10-31 | Profile Tab source audit | [PROFILE_AUDIT.md](PROFILE_AUDIT.md), PR #6, PR #7 | Confirmed branch divergence; found avatar integration gap and P1 PIN authorization gaps in app restriction editor/profile deletion/default PIN setup. | Fix P1 findings, integrate avatar selectively, add tests; retain Draft PRs |

| 2026-10-31 | Profile security implementation | [Draft PR #8](https://github.com/Frangga11/glass-tv-home/pull/8), branch `feature/profile-security-integration`, head `f0971b17ed752a9ac0bf16afa904990723c0c616` | Added parent PIN gate for lock/hide editors and profile deletion; centralized strict child-profile exit authorization; added JVM tests for switch policy and profile key isolation. User explicitly keeps PIN 0000 temporarily and rejects failed-attempt limits. CI #196 in progress at log update. | Finish CI, inspect test/build results, then selectively integrate avatar PR #6; no merge/device install yet |

| 2026-10-31 | Profile security CI acceptance | [Draft PR #8](https://github.com/Frangga11/glass-tv-home/pull/8), [CI #197](https://github.com/Frangga11/glass-tv-home/actions/runs/41216508402), artifact `13048321564`, head `1ba744efee61fde662493cebd382743a5927aefa` | Both CI jobs passed on exact latest head; unit tests/release build, size guard, artifact publication, and lists/tools/docs checks succeeded. APK 1,420,005 bytes; SHA-256 `735be1ff6e8ff88d31bf9de73940e527a570bbaace96c5ec9a001ce95610a44e`. | Keep PR #8 Draft; selectively integrate avatar PR #6; physical TV/phone acceptance remains open |

| 2026-10-31 | Avatar + profile-security integration | [Draft PR #9](https://github.com/Frangga11/glass-tv-home/pull/9), [CI #208](https://github.com/Frangga11/glass-tv-home/actions/runs/41218171447), head `7e8392e1a76223b2c1aef314c10fa54d0b813fdc` | Selectively integrated PR #6 avatar QR into PR #8 profile-security base. Added private per-profile avatar storage, secure bounded QR server, durable replacement commit, avatar path checks, profile deletion cleanup, synchronized UI surfaces, and JVM policy/path tests. Exact-head CI #208 passed both jobs. APK artifact 13048951728: 1,427,681 bytes; SHA-256 `f19584332514df03dbf54149f3f446211ab43c2dce00eae545ac768ded1ce315`. | Keep PR #8/#9 Draft; complete physical Mi TV Stick + phone acceptance before merge; no APK installed |


| 2026-11-01 | Post-test repair roadmap approved | [BUG_REPAIR_ROADMAP.md](BUG_REPAIR_ROADMAP.md), decision D-008 | User approved the official A–G sequence: preparation/audit; global foundations; communication; state/persistence; specific modules; integration/testing; finalization/release. A01–A03 complete as planning tasks; no source audit or code repair started. | Begin A04 repository/branch/source/worktree audit; then match tested APK and establish baseline/rollback |


| 2026-11-01 | B01 global navigation audit and remediation | [Draft PR #10](https://github.com/Frangga11/glass-tv-home/pull/10), branch `feature/b01-global-navigation-retention`, base PR #9 head `7e8392e1a76223b2c1aef314c10fa54d0b813fdc`, [CI #209](https://github.com/Frangga11/glass-tv-home/actions/runs/41228348247) | Source audit found process-only home cursor retention, Settings tab reset to Profile, unsafe empty-grid focus fallback, and a scroll-settle fallback that could focus the wrong tile. Added persisted stable app identity/slot, tab retention, safe empty-grid fallback, and JVM navigation-state tests. Lists/tools/docs job passed; Android unit tests/release build were running at log update. | Verify exact-head CI; complete Settings right-pane cursor audit; then physical Mi TV Stick D-pad/Back/process-restart tests. Keep PR #10 Draft |


| 2026-11-01 | B01 top-bar focus restoration and exact-head validation | [Draft PR #10](https://github.com/Frangga11/glass-tv-home/pull/10), head `0f1c727da4c89576ba15a87238d17c1965a8a239`, [CI #212](https://github.com/Frangga11/glass-tv-home/actions/runs/41228628063) | Extended B01 to preserve the last focused top-bar pill when returning from external Android screens; it falls back to the app grid if the pill is unavailable. Added persisted top-pill ID/state. Exact-head CI #212: Lists/tools/docs PASS; Android unit tests/release build still running at this checkpoint. | Confirm CI #212; then record artifact/build and proceed to physical-device acceptance only after A05/A06 safety gates |


| 2026-11-01 | B01 exact-head CI and release artifact | [Draft PR #10](https://github.com/Frangga11/glass-tv-home/pull/10), [CI #212](https://github.com/Frangga11/glass-tv-home/actions/runs/41228628063), head `0f1c727da4c89576ba15a87238d17c1965a8a239`, artifact `13052019782` | Both CI jobs passed: Android unit tests, release build, APK size budget (1,394 KB; below 1.5 MiB), artifact publication, and lists/tools/docs checks. | Keep PR #10 Draft; physical Mi TV Stick remote and process-restart acceptance still required; do not install until A05/A06 gates are completed |


| 2026-11-01 | B01 second-pass audit and hardening | [Draft PR #10](https://github.com/Frangga11/glass-tv-home/pull/10), source head `1613fef4d83102369a87178a881832a29295b8d7`, [CI #224](https://github.com/Frangga11/glass-tv-home/actions/runs/41229617890) | Second-pass audit found resource-ID coupling for top-bar focus, transient RecyclerView rebind callbacks that could overwrite persisted focus, empty-list index normalization, and focus-settle fallback gaps. Replaced numeric view ID persistence with semantic keys; snapshot/restore full navigation state after rebind; normalized empty-list state; hardened failed-focus fallback; centralized Settings tab identifiers; expanded JVM tests. CI #224 queued at checkpoint. CI #213 was a temporary intermediate-commit mismatch (helper renamed before dependent references updated); subsequent complete snapshots #214–#221 passed. | Wait for exact-head CI #224; continue B02 only after B01 source snapshot passes; no TV installation/testing until all A–F audit/fix work is complete. Final APK publication is now manual opt-in only |


| 2026-11-01 | B01 current-head safe fallback and CI checkpoint | [Draft PR #10](https://github.com/Frangga11/glass-tv-home/pull/10), head `bf899c7c0300a40ad47f2d34c6cf0275c6bacdc5`, [CI #225](https://github.com/Frangga11/glass-tv-home/actions/runs/41229763017) | Final source review found that a visible Wi-Fi control could still reject focus without attempting Settings. Fallback now tries Wi-Fi only when shown/focusable and then Settings. Exact-head CI #224 passed the preceding snapshot; #225 is running against the current head. | Wait for #225 PASS before moving to B02; no TV testing or TV-test artifact publication until all source audits/fixes and final integration are complete |


| 2026-11-01 | B01 final second-pass source/CI gate | [Draft PR #10](https://github.com/Frangga11/glass-tv-home/pull/10), head `bf899c7c0300a40ad47f2d34c6cf0275c6bacdc5`, [CI #225](https://github.com/Frangga11/glass-tv-home/actions/runs/41229763017) | Exact-head CI #225 PASS, both jobs. `testReleaseUnitTest` and `assembleRelease` succeeded; APK size guard passed at 1,395 KB (<1.5 MiB). Manual-only TV-test artifact step was skipped; CI run produced no artifacts. B01 source-level audit and CI gate are PASS. | Proceed to B02 source audit; defer all physical TV acceptance to final stage F02 after A–E audit/fix work and F integration |


| 2026-11-01 | B02 focus indicator remediation | [Draft PR #11](https://github.com/Frangga11/glass-tv-home/pull/11), branch `feature/b02-remote-focus-indicators`, head `51be4c373ca985002ce8b940ab6e4ac6f679b257`, [CI #232](https://github.com/Frangga11/glass-tv-home/actions/runs/41230307222) | Audit found avatar focus/selection conflation, static QR-upload/reset buttons, static profile-name input borders, and a focusable Azan card without visible focus feedback. Added shared `TvFocusIndicator`, separate cyan avatar focus rings, focused button/input states, and Azan focus stroke. CI #232 pending. | Verify exact-head CI and source diff; no TV testing until all A–E audits/fixes and final F integration |


| 2026-11-01 | B02 daily forecast focus contrast | [Draft PR #11](https://github.com/Frangga11/glass-tv-home/pull/11), head `68e87eaeead948367665470fa08503daeddbcaef`, exact-head CI pending | Extended the audit to WeatherActivity: daily forecast row focus was a low-opacity fill without an outline, unlike hourly/metric controls. Added a white focus stroke and stronger focused fill without changing row size. | Validate current head with CI; continue source-level review before B03; no TV test |


| 2026-11-01 | B02 avatar focus ring z-order refinement | [Draft PR #11](https://github.com/Frangga11/glass-tv-home/pull/11), head `01cbef2c2fc90498295971b22c369a72cdfa734a`, [CI #234](https://github.com/Frangga11/glass-tv-home/actions/runs/41230495879) | Final source review moved the separate cyan avatar focus ring above the image layer so the ring cannot be occluded by the avatar image. Exact-head CI #234 PASS on the recorded current head; both jobs passed and manual TV-test artifact publication was skipped. | Pause further B02 work pending A05/A06; keep PR Draft; no TV test |


| 2026-11-01 | A04 repository/branch/source audit | [Repository](https://github.com/Frangga11/glass-tv-home), [Draft PR #10](https://github.com/Frangga11/glass-tv-home/pull/10), [Draft PR #11](https://github.com/Frangga11/glass-tv-home/pull/11) | Verified fork identity, 13 branch heads, PR #8→#9→#10→#11 dependency chain, B02 tree (647 entries; 496 files/151 directories), PR #9 baseline tree (644 entries), Gradle/app/manifest/CI configuration and source ownership. A04 remote audit PASS; no local checkout was opened or modified. Also verified B01 CI #225 and B02 CI #234 both passed exact heads; manual TV-test artifact step skipped. | Pause further B02 work; obtain installed APK identity for A05, then create A06 reproducible baseline and rollback package |


| 2026-11-01 | A05 tested APK identity | User-uploaded `glass-tv-home-profile-avatar-test.apk`; [CI #208](https://github.com/Frangga11/glass-tv-home/actions/runs/41218171447); [artifact #13048951728](https://github.com/Frangga11/glass-tv-home/actions/runs/41218171447/artifacts/13048951728); PR #9 | Downloaded and extracted the CI ZIP artifact. Its `app-release.apk` is byte-for-byte identical to the uploaded tested APK: 1,427,681 bytes; SHA-256 `f19584332514df03dbf54149f3f446211ab43c2dce00eae545ac768ded1ce315`. ZIP archive: 1,340,733 bytes; SHA-256 `85475980a94b35a1a356cd5b5156bae3b50e7b405bc76ca1c22cd1e9557d32d6`. CI #208 metadata binds artifact/run to PR #9 branch `feature/profile-avatar-security-integration`, head `7e8392e1a76223b2c1aef314c10fa54d0b813fdc`. A05 three-way match PASS. | Preserve the exact source/APK checkpoint; capture device state for A06 |
| 2026-11-01 | A06 source checkpoint | `checkpoint/a06-tested-apk-pr9-7e8392e1` | Preservation branch created directly from exact tested source commit; checkpoint manifest added. Historical chat evidence recovered: `com.mitv.tvhome` appeared disabled; `com.google.android.tvlauncher` and `com.gathin.tvhome` appeared enabled; `get-home-activities` was `unknown`; `set-home-activity` returned `Success`; `am start` said launcher already running. These observations do not establish current HOME ownership or installed APK hash. Fresh device-state snapshot remains pending. | Obtain fresh read-only Bugjaeger/ADB baseline before any device change |


| 2026-11-01 | A06 rollback readiness documentation | [A06 checkpoint manifest](https://github.com/Frangga11/glass-tv-home/blob/checkpoint/a06-tested-apk-pr9-7e8392e1/docs/project-management/checkpoints/A06_TESTED_APK_BASELINE.md); [rollback SOP](https://github.com/Frangga11/glass-tv-home/blob/checkpoint/a06-tested-apk-pr9-7e8392e1/docs/project-management/checkpoints/A06_ROLLBACK_PROCEDURE.md) | Reaffirmed PR #9 APK as the user's accepted known-good tested baseline; preserved exact APK/source hashes and immutable source checkpoint. Added conservative rollback steps that avoid guessed HOME components and destructive package/data operations. Historical ADB observations remain historical only. | A06 remains IN PROGRESS until a fresh read-only device snapshot establishes current HOME resolver/package state before any device mutation. No retest requested; no device settings changed; no PR merged |


| 2026-11-01 | A06 live device snapshot and closure | [A06 checkpoint manifest](https://github.com/Frangga11/glass-tv-home/blob/checkpoint/a06-tested-apk-pr9-7e8392e1/docs/project-management/checkpoints/A06_TESTED_APK_BASELINE.md) | Received complete read-only Bugjaeger output for MiTV-AYFR0 / Android 11 API 30. Captured Glass TV Home package path/version, enabled/disabled package lists, HOME resolver, accessibility, input method, and power setting. Resolver returned stock `com.google.android.tvlauncher/.MainActivity`, while dumpsys preferred-activity table lists `com.gathin.tvhome/.MainActivity`; logged as E01 carry-forward. Installed APK bytes/hash not extracted; package version 4.0.0/code 40 matches baseline metadata only. A06 marked DONE; no device mutation performed. | Resume B01/B02 source-level work on isolated Draft PRs. Keep all physical TV testing deferred to F02/final candidate approval |


| 2026-11-01 | B02 second-pass source and CI review | [Draft PR #11](https://github.com/Frangga11/glass-tv-home/pull/11), head `01cbef2c2fc90498295971b22c369a72cdfa734a`, [CI #234](https://github.com/Frangga11/glass-tv-home/actions/runs/41230495879) | Reviewed all five changed files and relevant focus-listener context. Confirmed helper treatment for avatar focus rings, upload/reset buttons, profile-name fields, Azan card, and daily forecast selector; no conflicting listener identified in the inspected paths. CI #234 passed both jobs, size guard passed, manual TV-test artifact step skipped. | Keep PR #11 Draft; proceed with B03 source audit; defer all physical TV acceptance to F02 |


| 2026-11-01 | B03 dialog/popup opacity remediation started | [Draft PR #12](https://github.com/Frangga11/glass-tv-home/pull/12), branch `feature/b03-dialog-opacity-layering`, head `3f11618b34f7edb8618c74e6b2fe48dd7c9e6ccf`, [CI #235](https://github.com/Frangga11/glass-tv-home/actions/runs/41233304847) | Source audit found translucent shared dialog gradient (90–94%), shared glass panel (80%), and phone QR upload surface (94%). Changed those three surfaces to fully opaque dark backgrounds while retaining rounded borders/top sheen. Kept blurred-wallpaper GlassDrawable popups unchanged by design. Lists/tools/docs CI job passed; Launcher build/unit tests still running at log time. | Finish exact-head CI and complete B03 dialog focus/geometry/dismissal audit; no APK install, device change, or PR merge |


| 2026-11-01 | B03 focus and viewport sizing extension | [Draft PR #12](https://github.com/Frangga11/glass-tv-home/pull/12), current head `56a4e1995aa8b8600614e6b5c4dca69d01c530c3`, [CI #239](https://github.com/Frangga11/glass-tv-home/actions/runs/41233548406) | Added explicit D-pad focus selectors for standard AlertDialog action buttons and list rows; bounded the QR upload dialog to 90% of TV viewport with D-pad vertical scrolling; clamped Now Playing and card-prompt widths. Exact-head CI #239: Lists/tools/docs PASS; Launcher build/unit-test job running. | Finish CI and complete B03 source audit; no APK install, device changes, or PR merge |


| 2026-11-01 | B03 exact-head source/CI gate | [Draft PR #12](https://github.com/Frangga11/glass-tv-home/pull/12), head `56a4e1995aa8b8600614e6b5c4dca69d01c530c3`, [CI #239](https://github.com/Frangga11/glass-tv-home/actions/runs/41233548406) | CI #239 PASS, both jobs. Launcher unit tests/release build succeeded; release APK size 1,398 KB (<1.5 MiB). Lists/tools/docs checks passed. Manual final TV-test artifact step was skipped; no artifact published. Source scope includes opaque dialog/popup panels, D-pad dialog selectors, bounded QR scroll dialog, and width clamps for custom dialogs. | Keep PR #12 Draft; physical device verification deferred to F02; proceed with B04 source audit |


| 2026-11-01 | B04 initial global responsiveness audit | Source branch reviewed: `feature/b03-dialog-opacity-layering` head `56a4e1995aa8b8600614e6b5c4dca69d01c530c3` | Reviewed home grid tile sizing, hero/Islamic shelf width calculations, settings dialog bounds, weather ScrollView/bento dimensions, Recent Apps card geometry, and Nightstand typography. Home/settings are responsive in source; potential viewport-sensitive areas are fixed Recent Apps 420×250dp cards with 64dp gutters and Nightstand 110sp clock. No change made without actual TV display size/density. | Request read-only `wm size` and `wm density`; retain physical visual acceptance for F02 |

## Renewed B01–B03 source QC and B04/B05 audit start — 1 November 2026

- Rechecked exact PR heads and diffs for B01 (#10), B02 (#11), and B03 (#12); verified branch ancestry and no merge.
- Exact-head CI #230 (B01, 1,395 KB), #234 (B02, 1,396 KB), and #239 (B03, 1,398 KB) all passed launcher unit tests/release build, APK size guard, and lists/tools/docs checks. Manual TV-test artifact step skipped; artifact list empty.
- Source QC disposition: B01–B03 PASS at source/CI level; keep roadmap tasks IN PROGRESS until F02 integrated and physical acceptance. No APK installed and no TV setting changed.
- B04 source audit: home grid/settings/dialogs already responsive or bounded; Recent Apps fixed 420×250dp card plus 64dp padding and Nightstand 110sp clock remain viewport-sensitive. No speculative change pending read-only wm size/density.
- B05 source audit: AppLanguage ID/EN preference exists but resource localization and hardcoded strings are incomplete. First-pass scan of 12 high-traffic Java files flagged 187 user-facing string-call patterns for classification. B05 moved to IN PROGRESS.


## 2026-11-01 — renewed final B01–B03 source QC and expanded B04/B05 inventory

**B01–B03 recheck**
- Re-read PR metadata, changed-file patches, key implementation paths, and exact branch heads for PR #10/#11/#12. All remain Draft/open/unmerged and preserve the approved B01 → B02 → B03 base chain.
- B01 head `bf899c7c0300a40ad47f2d34c6cf0275c6bacdc5`: reviewed NavigationState persistence/tests, stable Home component identity + fallback slot, semantic top-bar focus keys, RecyclerView reload snapshot, empty-list normalization/fallback, and Settings tab/sidebar restoration. No source blocker found; right-pane action-return focus remains B07.
- B02 head `01cbef2c2fc90498295971b22c369a72cdfa734a`: reviewed shared TvFocusIndicator and its call sites in avatar frames, rounded text controls, upload/reset buttons, Azan overlay, and weather forecast rows. Focus indicator remains distinct from selected state; no listener collision found in the touched paths.
- B03 head `56a4e1995aa8b8600614e6b5c4dca69d01c530c3`: reviewed opaque dialog/panel surfaces, AlertDialog button/list selectors, QR dialog viewport/scroll/focus/dismissal cleanup, and custom dialog width clamps. Blurred-wallpaper GlassDrawable popups remain intentionally unchanged.
- Exact-head CI evidence: B01 #230 / run `41230264083` PASS (1,395 KB); B02 #234 / run `41230495879` PASS (1,396 KB); B03 #239 / run `41233548406` PASS (1,398 KB). Logs confirm app and installer `testReleaseUnitTest`, `assembleRelease`, size gate, and Lists/tools/docs checks. All manual TV-test artifact steps were skipped; artifact lists are empty.
- Disposition: B01/B02/B03 source + CI gate PASS. Keep roadmap status IN PROGRESS until F01/F02 integrated and physical acceptance. No candidate APK installed, no device setting/package changed, no PR merged.

**B04 expanded source audit**
- Reviewed 66 app Java sources and the relevant home/recent-apps/weather/nightstand layouts.
- Home tile width is derived from available grid width/column count (180dp design cap, 80dp floor, 180:101 ratio).
- Recent Apps card remains fixed 420×250dp with 64dp RecyclerView side padding; Nightstand clock remains 110sp with wrap-content centered layout; Weather uses a ScrollView but has a fixed 385dp Bento body and ~91–92dp metric rows.
- No clipping defect can be proven without actual logical viewport/density. No speculative layout changes made. Need user-provided read-only `wm size` and `wm density` outputs before changing fixed geometry.

**B05 expanded localization inventory**
- Audited all 66 app Java source files, 18 app/installer layout XML files, 13 app string-resource XML files, AppLanguage, locale config, manifest, and app Gradle dependencies.
- AppLanguage has 90 static translation/helper methods; the saved preference supports ID/EN and migrates legacy MS to ID. Many dormant LANG_MS branches remain.
- Resource inventory: 44 keys in default values/strings.xml, default strings mix Indonesian/English; no values-id/strings.xml or generic values-en/strings.xml; values-en-rUS overrides only date_format. Other language folders are partial. localeConfig advertises 12 locales but omits Indonesian, while the app selector offers ID/EN only.
- Language selection updates the Islamic shelf/date and toast but does not apply a locale to Android Resources or refresh/recreate all visible screens.
- Static scan found at least 95 direct literal UI-setter calls across seven high-traffic Java files, plus hardcoded text in eight launcher layout XML files. This is a lower bound, not a complete unique-string count; dynamic/assembled text, notification messages, and installer text require separate classification.
- No localization code/resources changed in this pass. Recommended direction: make ID/EN the supported language set; unify persisted preference and locale-aware contexts for API 26–32 (target API 30), synchronize API 33+ app locale where appropriate, create complete ID/EN resources, migrate hardcoded UI strings, preserve focus on language change, and add resource parity/normalization tests. Keep companion installer scope separate unless explicitly added.

**Documentation correction**
- Roadmap A06 task row corrected to DONE. A06 checkpoint manifest and rollback SOP were re-opened and verified on branch `checkpoint/a06-tested-apk-pr9-7e8392e1`; the earlier device-snapshot-pending text in INVENTORY was corrected. A06 residuals remain assigned to E01, not blockers for A06 closure.


## 2026-11-01 — B04 target viewport metrics received; B05 implementation planning

**B04 target metrics supplied by user**
- Bugjaeger read-only \`wm size\`: Physical size \`1920x1080\`.
- Bugjaeger read-only \`wm density\`: Physical density \`320\`.
- Derived logical viewport: \`960×540 dp\` (density scale 2.0).
- Recent Apps: two 420dp cards plus 64dp left/right padding total 968dp, only 8dp beyond viewport before inter-card spacing. This is a near-fit horizontal carousel, not proof of a defect. Confirm item spacing, partial-card affordance, and D-pad scroll at F02.
- Nightstand: 110sp clock and 19sp date are not inherently oversized at 960dp width; long date/font-scale acceptance remains F02.
- Weather: 385dp Bento body is about 71.3% of 540dp height and is hosted in a full-screen ScrollView/fillViewport. No clipping defect is established; verify remote focus/scroll and visual proportions at F02.
- Disposition: no measured source-level overflow defect; no speculative layout changes. B04 source + target-metric audit PASS; retain F02 physical acceptance. B04 remains IN PROGRESS in the roadmap only until its integrated/physical gate is satisfied.

**B05 architecture checkpoint — implementation not yet modified**
- Scope is app-facing Indonesian/English only; installer localization is a separate scope unless explicitly added.
- The target is Android 11/API 30, so the implementation must not depend solely on Android 13 per-app language APIs.
- AppLanguage remains the single source of truth for persisted ID/EN selection and migration of legacy MS to ID. Remove unreachable Malay fallbacks from user-visible output as files are safely migrated; do not silently alter preference keys or existing saved selection.
- Implement locale-aware contexts for Activity/resource-backed surfaces on API 26–32, synchronize API 33+ system per-app locale where supported, and ensure newly opened screens/dialogs use the selected language.
- Add complete ID and EN resource coverage, correct localeConfig to advertise only actual supported locales, migrate XML/Java hardcoded labels/messages to resources or a consistent translation layer, and cover dynamic/errors/notifications.
- Language change must refresh the UI while preserving the current tab/sidebar/focus position and must not reset profile, wallpaper, prayer, weather, or other user data.
- Before implementation, inventory every Activity/service lifecycle entry point and current language call site; add unit tests for preference normalization/migration and resource key/placeholder parity. No source files modified in this planning pass.


## 2026-11-01 — B05 localization implementation checkpoint

- Created Draft PR [#14](https://github.com/Frangga11/glass-tv-home/pull/14), branch `feature/b05-id-en-localization`, stacked on B03 exact head `56a4e1995aa8b8600614e6b5c4dca69d01c530c3`; latest audited head at this checkpoint: `cce889f231aeff3a28805fd517853a3f0a91e79c`.
- Added complete 158-key default/Indonesian/English string sets. Static parity audit: 158/158/158 keys, no duplicate keys, no key-set differences, no placeholder differences.
- Restricted `locales_config.xml` to `id` and `en`; added locale-aware base contexts to five user-facing Activities and ClockDream. Added API 33+ per-app locale synchronization and API 26–32 resource-context handling.
- Updated the language-switch flow to recreate on API 26–32 and reopen Settings; B01 persists the selected Settings tab/sidebar cursor. No right-pane action focus restoration is claimed.
- Migrated static strings in eight launcher layouts (Home, Recent Apps, Weather, media hub, OTA/support, close-all, daily weather row, Nightstand) and programmatic text in profile, PIN, weather-condition, and update/install flows.
- Added tests for language normalization and resource key/placeholder parity. Static resource audit passed; Gradle tests/build have NOT yet been run for this head.
- A fresh Actions query showed no workflow run associated with the B05 head. Do not treat source parity as build/CI pass. CI must be triggered and exact-head logs inspected before source gate can pass.
- Remaining work: migrate hardcoded Java strings in MainActivity/ModernSettingsDialog/WeatherActivity and remaining dynamic/error/notification paths; complete lifecycle/focus review; run exact-head CI; update final audit.
- PR #14 remains Draft/open/unmerged. No artifact publication, APK install, device setting/package change, or physical TV test.

## 2026-11-01 — B04/B05 integrated CI, defect repair, and merge checkpoint

- User requested direct execution and quick escalation only if user action was required. User manually dispatched CI once the workflow trigger was made available on `main`.
- Added `workflow_dispatch` to `.github/workflows/ci.yml` on `main` (commit `6eaf787c56226606b1d2e650c59540c553756c62`). User selected `integration/b04-b05-reconciled`; APK publish input remained false.
- Initial integrated CI #244 failed on resource apostrophe parsing, then follow-up runs exposed a localized progress-message type mismatch and a localization test placeholder-list type mismatch. All three defects were corrected on the integration branch.
- Final integration head `5932cf60e19640bc5fc648f43105f86c8752813c`: CI run `41277076284` PASS; both jobs PASS. Matching push run `41277072693` PASS. No APK artifact was published.
- PR #15 merged as `ec5ff4fce06fe0d2daf0e3fc49fe84fef9ad9838`. PR #13 passed exact-head CI run `41277353285` and merged as `d7b76c4a22c9eadffb8171a616283f3ad647d488`. GitHub reports PR #14 merged as `73817ce41e4ae5b034922f190052a95ac45886d9`. Current B03 continuation branch head is `d7b76c4a22c9eadffb8171a616283f3ad647d488`.
- Next: B06 audit/implementation, then B07. Final runtime/device/QR/TV tests remain deferred until final overall B-series acceptance. No APK install or device setting change occurred.
