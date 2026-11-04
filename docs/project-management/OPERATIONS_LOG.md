# Glass TV Home — Operational Log

**Version:** 1.0  
**Updated:** 3 November 2026  
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
- Final integration head `5932cf60e19640bc5fc648f43105f86c8752813c`: CI run #254 (`41277076284`) PASS; both jobs PASS. Matching push run `41277072693` PASS. No APK artifact was published.
- PR #15 merged as `ec5ff4fce06fe0d2daf0e3fc49fe84fef9ad9838`. PR #13 passed exact-head CI run #256 (`41277353285`) and merged as `d7b76c4a22c9eadffb8171a616283f3ad647d488`. GitHub reports PR #14 merged as `73817ce41e4ae5b034922f190052a95ac45886d9`. Current B03 continuation branch head is `d7b76c4a22c9eadffb8171a616283f3ad647d488`.
- Next: B06 audit/implementation, then B07. Final runtime/device/QR/TV tests remain deferred until final overall B-series acceptance. No APK install or device setting change occurred.

## 2026-11-01 — B06 baseline audit started

- Created `feature/b06-notification-feedback` from exact B03 continuation head `d7b76c4a22c9eadffb8171a616283f3ad647d488`.
- Scanned 63 launcher Java files for transient messages, dialogs, loading/progress, delayed callbacks, and network/socket timeouts.
- Initial finding: feedback calls are distributed across Activities/managers; OTA still uses `ProgressDialog`; avatar/wallpaper QR pages expose separate browser-side statuses. No shared feedback abstraction/Snackbar usage found.
- No B06 code changes yet. Next action is to complete the feedback taxonomy and lifecycle ownership audit before implementing a shared pattern. Preserve locale, D-pad focus, and operation-specific transport timeouts.


## 1 November 2026 — B06 source/CI closure

- Completed B06 on `feature/b06-notification-feedback`, exact head `bc86a250d73a5cbb83250217d42c3c3993cf29d7`; Draft PR #16 remains unmerged per D-013.
- Centralized 108 Toast call sites across 10 app-layer Java files via `TvFeedback`; added lifecycle and duration policy tests.
- Hardened OTA check/download, indeterminate/determinate loading, cancellation, and partial APK cleanup; added Avatar/Wallpaper/Standby QR progress and timeout feedback; added wallpaper category sync result feedback.
- Preserved operation-specific timeout budgets. Static resource parity: 566 keys in default/ID/EN with zero differences or placeholder mismatches.
- Exact-head CI run #295 / `41280874732` passed both jobs; APK publication skipped. No APK/device changes.
- Final device/runtime/phone QR acceptance remains deferred to F-stage. B07 is next.
- Evidence: `B06_FEEDBACK_AUDIT.md`.


## 2026-11-01 — C03 phone-to-TV active LAN audit and repair

- Created isolated branch `feature/c03-phone-tv-network-verification` from C02 exact head `b0e1ff048e36bf21f8da1e165a427075f85a7cb3`; Draft PR #20 targets C02.
- Audited Avatar, Wallpaper, and StandBy QR network selection and URL construction. Found the app resolver selected an IPv4 address by interface-name preference rather than Android's active default network.
- Added `LocalNetworkAddress` using `ConnectivityManager`, `NetworkCapabilities`, and `LinkProperties`; only active Wi-Fi/Ethernet IPv4 is eligible. VPN-default and unusable address cases fail closed without fabricated IP fallback.
- Migrated Avatar, Wallpaper, and StandBy preflight and server-start address resolution to the shared helper. Preserved bound-address/actual-port QR URLs, independent session routes, tokens, limits, timeouts, and existing same-Wi-Fi instructions.
- Added `LocalNetworkAddressTest` with three JVM test groups for usable IPv4, invalid address classes, and invalid prefix lengths.
- Source head `a0615b2135659a515b3bea39d98bf08639248c09`: CI #337 / `41287435536` PASS. Audit-doc checkpoint head `d266287df287167e5a8e73fd675b51759b580ed6`: CI #338 / `41287563951` PASS, both jobs. `testReleaseUnitTest` and `assembleRelease` succeeded.
- APK size diagnostic: 1,546,045 bytes; below 1.5 MiB cap by 26,819 bytes. Artifact publication skipped.
- Physical phone/TV same-network acceptance is deferred to F-stage and remains mandatory. No APK published/installed, no device settings changed, no PR merged.
- Next action: begin C04 endpoint/routing/method/response-code audit without reopening C01–C03 unless a concrete dependency is found.


## 2026-11-01 — C04 HTTP endpoint/routing audit and repair

- Created isolated branch `feature/c04-http-endpoint-routing` from C03 exact head `d266287df287167e5a8e73fd675b51759b580ed6`; Draft PR #21 targets C03.
- Audited Avatar, Wallpaper, and StandBy request-line parsing, session route boundaries, method handling, query parameters, response codes, and gallery browser API behavior.
- Findings: prefix/minimum-token request parsing; incomplete headers could continue to route; unsupported methods were often 404; Wallpaper OPTIONS was broad; select/delete could report false 200; browser actions ignored HTTP status; query parameters did not reject duplicate/missing values consistently.
- Added `UploadHttpRequest` and JVM tests; both services now require a complete header and strict HTTP/1.0/1.1 request line. Route map is exact and token-scoped. Known path/wrong method returns 405 + Allow; unknown path 404; invalid line/header 400; header too large 431; oversized upload 413.
- Wallpaper API now reports select/delete/slideshow/clear failure states; list/thumbnail errors return explicit responses. Browser POST actions respect response.ok and display localized failure feedback. Added aligned default/ID/EN string.
- Exact-head CI #342 / `41288521501` PASS on `f09ee1b30b80e45e793c1678935eff8867aedae4`; both jobs SUCCESS; launcher/installer `testReleaseUnitTest` and `assembleRelease` succeeded.
- APK size: 1,547,845 bytes; 15,019 bytes below 1.5 MiB cap. Publication skipped. Avoid nonessential APK growth in later stages.
- Physical phone/TV QR/browser acceptance remains deferred to F-stage and mandatory. No APK published/installed, no device settings changed, no PR merged.
- Next: C05 shared upload/receive-file pipeline audit.


## C04 final exact-head verification — 1 November 2026

- Latest C04 feature branch head: `9094969d968c6fffef6598e1e8c5a7bd62e5efa8`.
- Exact-head CI #343 / `41288644099`: PASS; both `Launcher build and unit tests` and `Lists, tools and docs` jobs SUCCESS. `testReleaseUnitTest` and `assembleRelease` succeeded.
- APK diagnostic remains 1,547,845 bytes; 15,019 bytes below the 1.5 MiB cap. APK publication skipped.
- PR #21 remains OPEN/DRAFT/UNMERGED; base is C03 exact head `d266287df287167e5a8e73fd675b51759b580ed6`. No merge or device acceptance claimed.


## C04 final branch-head verification — 1 November 2026

- Current C04 feature head: `aad9aeb163b46ff4c13a8d93c160a3eec78f9770`; Draft PR #21 targets C03 exact head `d266287df287167e5a8e73fd675b51759b580ed6`.
- Exact-head CI #355 / `41289178858`: PASS; both jobs SUCCESS. Launcher and installer `testReleaseUnitTest` and `assembleRelease` succeeded.
- Release APK diagnostic: 1,548,097 bytes, 14,767 bytes below the 1.5 MiB cap. APK publication skipped.
- PR #21 remains OPEN/DRAFT/UNMERGED under D-013. Physical phone/TV browser acceptance remains deferred to F-stage, not waived.
- Next task: C05 shared upload/receive-file pipeline audit.


## 2026-11-01 — D01 settings/configuration storage audit

- Resumed existing D01 work; did not repeat the original storage inventory. Confirmed PR #25 is OPEN/DRAFT/UNMERGED on `feature/d01-config-storage-audit`, based on C07 exact head `1cc6d7033daf6f9371805f667467b8860edcfb8f`.
- Verified original exact-head CI #373 / run `41293666682` passed both `Launcher build and unit tests` and `Lists, tools and docs`; APK publication step was skipped.
- Re-read the D01 storage ownership report and independently spot-checked the manifest, Config, ProfileManager, ChildModeManager, StandbyPhotoStore, AppLanguage, AppLocationManager, Location, Azan, and C07 StandBy upload response path.
- Quality correction: documented Android 12+ OEM device-to-device transfer caveat for `allowBackup=false`; current target TV is Android 11/API 30.
- Added D01-F11: StandbyPhotoStore commits collection metadata, then ignores the result of the photo-mode commit; C07 WallpaperUploadServer can return HTTP 200 based on the misleading success result. Assigned targeted repair/regression coverage to D05/D07; did not change source code or weaken C07 behavior.
- D01 audit document updated on feature head `cd710681099faece14905faa244f57245bae84d1`; exact-head CI for this final head is pending. No merge, APK publication/installation, or device setting changes.



## 2026-11-01 — D01 final exact-head CI closure

- Final D01 feature head `cd710681099faece14905faa244f57245bae84d1` passed exact-head CI twice: #375 / `41304411195` and #376 / `41304430928`; both Launcher build/unit tests and Lists/tools/docs jobs SUCCESS in both runs.
- D01 source audit and CI gate are closed. PR #25 remains OPEN/DRAFT/UNMERGED under D-013; no merge, APK publication/installation, or device-setting changes.
- D01-F11 remains assigned to D05/D07 for a targeted atomic/failure-aware persistence repair and regression tests. F-stage runtime/process-death/reboot acceptance remains mandatory.
- Next project task: D02, following the approved Project D sequence.


## 2026-11-01 — D02 settings-to-UI propagation audit and targeted repair

- Created isolated branch `feature/d02-settings-ui-propagation` from D01 exact head `cd710681099faece14905faa244f57245bae84d1`; opened Draft PR #26 targeting D01. PR remains OPEN/DRAFT/UNMERGED under D-013.
- Audited Settings callbacks and dependent Home/profile/Child Mode/wallpaper/weather/location/StandBy/language UI update paths. Existing owner callbacks and lifecycle refreshes already cover the inspected successful mutation paths; no global SharedPreferences listener/event bus was justified.
- Found D02-F01: `MainActivity.setStandbyPhotoMode()` ignored the boolean commit result and showed success/refreshed screensaver even when persistence failed.
- Repaired setter to honor commit success, preserve current live UI on failure, and show a localized failure message. Added `standby_mode_save_failed` to default/ID/EN resources. No preference key/store migration or broad state refactor.
- D02 audit report: `D02_SETTINGS_UI_PROPAGATION_AUDIT.md`; source head `3bb4ae592904a49989fd76d7dabd92ed1cebd94b`.
- Exact-head CI #380 / `41305432110` and #381 / `41305453205` are still in progress. Both documentation/tool jobs passed; launcher build/unit-test job is running. Final D02 closure awaits exact-head CI.
- D01-F11 remains assigned to D05/D07 for the distinct upload collection/mode atomicity fix and regression coverage. No merge, APK publication/installation, or device setting change.


## 2026-11-01 — D02 final exact-head CI closure

- Final D02 branch head `2dbfcc181651184914422ad0697a3c5c27d15614` passed exact-head CI #382 / run `41305782187`.
- Both `Launcher build and unit tests` and `Lists, tools and docs` jobs succeeded. `testReleaseUnitTest`, `assembleRelease`, APK diagnostics and the 1.5 MiB size gate passed.
- Release APK diagnostic: 1,550,617 bytes, 12,247 bytes below the 1,572,864-byte cap. Manual APK publication was skipped.
- D02 source-audit and CI gates are closed. PR #26 remains OPEN/DRAFT/UNMERGED under D-013. No APK installation, device setting changes, or merge.
- Physical Android TV acceptance remains deferred to F-stage, not waived. D01-F11 remains assigned to D05/D07.
- Next task: D03 — locked/hidden app count and list refresh in the open Settings pane.


## 2026-11-01 — D03 locked/hidden app refresh closure

- Completed targeted D03 repair on `feature/d03-locked-app-ui-refresh`; final head `c119198e5c5ecc0d3cfdc283049ea3299d0067ca`; Draft PR #27 remains OPEN/DRAFT/UNMERGED under D-013.
- Fixed stale Profile Settings locked/hidden counts by refreshing the owning right pane when the picker dismisses. Home tiles now reload on both Done and Back; PIN cancellation before picker launch remains outside this callback path.
- Exact-head CI #385 / `41307515649`: both Launcher build/unit tests and Lists/tools/docs jobs SUCCESS. `testReleaseUnitTest` and `assembleRelease` succeeded.
- APK exact size 1,550,761 bytes; 22,103 bytes below the 1,572,864-byte gate. Size gate passed; manual APK publication skipped.
- D03 source and CI gates closed. Physical TV acceptance remains mandatory in F-stage; no APK installed and no merge performed.
- D01-F11 remains assigned to D05/D07. Preserve C07 upload/session hardening.
- Next: D04 profile/avatar state synchronization audit.

 
## 2026-11-01 — D04 profile/avatar synchronization closure

- Completed targeted D04 audit and repair on `feature/d04-profile-avatar-state-sync`; final head `ad69d229a6b3cd6ca1810a595597dc88481c3ebd`; Draft PR #28 remains OPEN/DRAFT/UNMERGED under D-013.
- Fixed D04-F01: built-in avatar selection rings now reflect the effective avatar when a custom photo overrides the stored built-in avatar; restoring default avatar restores the fallback ring. Added two JVM regression tests.
- Exact-head CI #390 / `41309327872`: both Launcher build/unit tests and Lists/tools/docs jobs SUCCESS. App/installer `testReleaseUnitTest` and `assembleRelease` succeeded.
- APK exact size 1,550,805 bytes; 22,059 bytes below the 1,572,864-byte gate. Size gate passed; manual APK publication skipped.
- D04 source and CI gates closed. Physical Android TV acceptance remains mandatory in F-stage; no APK installed and no merge performed.
- D01-F11 remains assigned to D05/D07. Preserve C07 upload/session hardening.
- Next: D05 targeted repair of D01-F11 StandBy upload persistence and truthful HTTP success response, with regression coverage.


## 2026-11-01 — D05 StandBy photo persistence repair

- Closed targeted D01-F11 source repair on `feature/d05-standby-photo-atomic-persistence`; PR #29 remains OPEN/DRAFT/UNMERGED under D-013.
- StandBy photo URI collection and photo-mode enablement now commit atomically in one synchronous SharedPreferences editor operation. Explicit upload outcomes map persistence failure to HTTP 500 and rejected input/collection to HTTP 400; HTTP 200 is reserved for successful persisted state. Duplicate uploads also ensure mode is persisted.
- On commit failure, retain the uploaded file temporarily because Android may already expose the updated preference map in memory; existing orphan cleanup removes non-durable files after restart. Existing C07 upload/session hardening preserved.
- Added regression tests for single-commit state persistence, failure propagation, and HTTP status mapping.
- Exact implementation-head CI #396 / `41311199958`: both Launcher build/unit tests and Lists/tools/docs jobs SUCCESS; app/installer `testReleaseUnitTest` and `assembleRelease` passed.
- APK exact size 1,551,005 bytes; 21,859 bytes below the 1,572,864-byte gate. Size gate passed; manual APK publication skipped.
- D05 source/CI gates closed. Runtime/process-death/reboot/physical Android TV acceptance remains mandatory in F-stage; no APK installed and no merge performed.
- Next: D06, following the exact scope in the current roadmap.

## 2026-11-01 — D06 process-death and reboot recovery closure

- Completed D06 on isolated branch `feature/d06-process-death-recovery`, head `548581e909139dd67b2ddc725bb546a838c75702`; Draft PR #30 remains OPEN/DRAFT/UNMERGED under D-013, stacked on D05 exact head.
- Audited persistence/recovery paths for app-owned settings, profiles/avatars, Child Mode, StandBy photos, wallpaper, language, caches, config overlay and scheduled prayer alarms.
- Fixed D06-F01: Android clears AlarmManager alarms on reboot. Added RECEIVE_BOOT_COMPLETED permission and non-exported AzanBootReceiver; it resolves the saved prayer zone and delegates to existing Azan.schedule(), preserving existing enable switches and schedule calculation. Four JVM regression tests added.
- Exact-head CI #398 / `41311958235`: both Launcher build/unit tests and Lists/tools/docs jobs SUCCESS. App/installer `testReleaseUnitTest` and `assembleRelease` succeeded.
- APK exact size **1,551,217 bytes**; **21,647 bytes** below the 1,572,864-byte gate. Size gate passed; manual APK publication skipped.
- D06 source and CI gates closed. Process-death, force-stop and physical Android TV reboot/alarm acceptance remains mandatory in F-stage; deferred and not waived. No APK installed and no merge performed.
- Next: D07 — read exact assignment from canonical roadmap/handover before coding.


## 2026-11-01 — D06 exact-final-head CI confirmation

- Rechecked D06 PR #30 after its audit-report commit changed the branch head. Exact-final-head CI #399 / run 41312553920 passed on 78a12e9fc196a596fa98ea272edc735501047af1; both workflow jobs succeeded.
- App and installer release unit tests and Release build passed. APK exact size 1,551,217 bytes; 21,647 bytes below the 1,572,864-byte cap; manual publication skipped.
- Updated PR #30 description to record #399 as the authoritative final-head run. PR #30 remains OPEN/DRAFT/UNMERGED under D-013. D06 source/CI gates closed; physical reboot/process-death acceptance remains in F-stage.

## 2026-11-01 — D07 concurrent profile/content-store state closure

- Completed D07 on isolated branch feature/d07-concurrent-profile-content-state, final head 964163c918e281b68af5cf7114ca56ead2797486; Draft PR #31 targets D06 final head 78a12e9fc196a596fa98ea272edc735501047af1 and remains OPEN/DRAFT/UNMERGED under D-013.
- Confirmed and fixed Child Mode package-set lost-update/profile-context risks; serialized profile-scoped reads, mutations and deletion cleanup, captured one profile ID per operation, and replaced timestamp-only new profile IDs with UUIDs. Parent PIN remains device-wide.
- Fixed StandBy picker/QR/clear races by merging picker selections against the latest collection under the store lock, rechecking capacity, committing collection+photo-mode atomically, committing clear before deleting managed files, and releasing grants from the committed URI snapshot.
- Fixed stale weather/AQI cache writes using request generations and location/language freshness checks; discarded superseded responses and retried current location.
- Added JVM regression coverage for Child Mode state/profile isolation, StandBy merge/atomic commit/clear, and weather freshness.
- CI runs #400–#402 exposed Android TextUtils calls in a pure JVM merge-policy helper; removed that Android dependency. Exact implementation-head CI #403 / 41313536393 passed, then audit report was committed and final-head CI #404 / 41313690556 passed on 964163c918e281b68af5cf7114ca56ead2797486.
- Exact-final-head CI #404: both Launcher build/unit tests and Lists/tools/docs jobs SUCCESS; app/installer testReleaseUnitTest and assembleRelease passed. APK exact size 1,552,197 bytes; 20,667 bytes below the 1,572,864-byte cap; manual publication skipped.
- D07 source-audit and exact-head CI gates closed. No preference schema migration, global state store/event bus, PIN policy change or C07 upload/session/protocol refactor. D01–D06 preserved.
- Physical Android TV concurrency, profile switching/isolation, picker/QR overlap, location/language switch, process death and reboot acceptance remain mandatory in F-stage, deferred and not waived. No APK installed or physical device test claimed.
- Project D D01–D07 source/CI work is complete. Next planned stage is E01. Grouped A–G integration, combined-diff audit and physical acceptance remain before merge/release approval.



## 2026-11-02 — E00 master inventory and document reconciliation

- Refreshed the remote branch list (36 names) and individually queried PR #16–#31. All #16–#31 are OPEN/DRAFT/UNMERGED; PR #10–#12 remain open/Draft/unmerged; B04/B05 reconciled merge history #13–#15 is retained.
- Reconciled B01–B07, C01–C07 and D01–D07 as source/CI complete while keeping integrated/runtime/physical acceptance pending in F. No project is described as fully accepted solely because its branch CI passed.
- Revalidated D07 baseline: `964163c918e281b68af5cf7114ca56ead2797486`; PR #31; exact-final-head CI #404 / `41313690556` PASS. Latest APK diagnostic 1,552,197 bytes with 20,667 bytes below the 1,572,864-byte gate; publication skipped.
- Static resource parity spot-check on D07 source: 569 unique keys in default, Indonesian and English; no duplicate keys, key-set differences or printf-placeholder mismatches. Runtime translation and UI acceptance remains F06.
- Added E00 audit report and reconciled roadmap, inventory, audit register, operations log, master project and handover. PR #32 merged into `docs/project-master-inventory` (merge commit `4ba80230cb9ddfbcc27657b7b0d07bed6905fc97`); PR #33 records official closure and the CI artifact-publication guard.
- No application source, feature PR, APK, device setting or user data changed. No merge, APK publication/installation or physical TV test performed.
- D-013 grouped merge timing and D-014 cross-project coordination standard remain binding. F01 is an isolated integration rehearsal/candidate assembly and combined-diff audit, not a merge into `integration/b-series`. F02–F07 validate the candidate. G01 closes findings through owning projects. Only after all A–G source audits/fixes are complete and findings are closed or explicitly accepted may grouped merges into `integration/b-series` begin, with exact-head CI/regression after each group. G02–G03 cover the traceable candidate/artifact and target-TV install/upgrade/rollback; final `main` merge and final APK build require final acceptance and explicit user approval.


## E00 official closure — 2 November 2026

- E00 documentation PR #32 was merged into `docs/project-master-inventory` (merge commit `4ba80230cb9ddfbcc27657b7b0d07bed6905fc97`).
- Exact PR head `541dd9accb66eb3b071367a3e4967769d742c2cd`; CI workflow run #414 / `41359874778`: PASS. Both `Launcher build and unit tests` and `Lists, tools and docs` jobs completed successfully. No review comments or change requests were present.
- Canonical E00 report, roadmap, inventory, audit register, operations log, master project and handover were re-read from `docs/project-master-inventory` after merge.
- E00 scope is closed: documentation/inventory/dependency reconciliation only. No application source, feature PR, APK, user data, TV setting, installation or physical-device acceptance was changed or claimed.
- **Next work unit: E01 — Device Settings & Launcher audit.** Begin with exact dependency heads and the A06 read-only HOME resolver/preferred-activity discrepancy. Preserve D-013/D-014; do not merge feature PRs early.


## E02 global UI consistency checkpoint — 2 November 2026

- Source-level audit and repairs completed across B01/B02/B03/B06/B07, C01/C03/C06, D03/D04 and E01. C02 installer was checked and has no app-owned modal windows; it uses inline progress/status feedback.
- Canonical `TvUiTokens` and `TvDialogStyle` are identical across the 8 Settings-owning B/D/E branches; modal helper is also identical on C01/C03/C06.
- Settings shell, sidebar tabs, selected states, shared card/switch rows, search input and modal bounds now consume the B01 global contracts. Weather, profile switch and update/permission/progress dialogs were standardized in the affected branches.
- Exact-head CI passed: B01 #563, B02 #565, B03 #567, B06 #569, B07 #571, D03 #574, D04 #576, E01 #578. Branch dependency synchronization restored B03 and B07 mergeability. No feature PR merged.
- Remaining acceptance: F-stage integrated UI review and physical Android TV/D-pad tests. Do not claim visual perfection or close runtime acceptance before this gate. Full details in `AUDIT_REGISTER.md`, E02 section.


## E02 D02 follow-up closure — 2 November 2026

- D02 PR #26 now uses canonical B01 `TvDialogStyle`/`TvUiTokens`; its shared AlertDialogs, Now Playing/card-prompt custom dialogs and QR upload window use the shared contract. The only remaining direct window sizing is the intentional 16:9 media-hub aspect-ratio layout.
- D02 exact-head CI #587 PASS; D03 #588 PASS; D04 #589 PASS. All three branches mergeable. No PR merged.
- B01/B02/B03/B06/B07, C01/C03/C06, D02/D03/D04 and E01 UI source changes have exact-head CI PASS. C02/C04/C05/C07 and D01 were checked and correctly require no additional visual component changes in their current scope.
- F-stage remains mandatory for integrated Settings tabs/subtabs and remote/modal acceptance; no physical-TV test is claimed.

## 2026-11-02 — E01 Device Settings source audit and finalization

- Audited the full Device Settings tab and its app-owned popup inventory. Removed silent Android system-setting/accessibility/notification writes; improved read-only HOME/service status; corrected restart action; retained user-managed native settings.
- Adopted B01's exact canonical TvDialogStyle/TvUiTokens for Settings and app-owned modal windows. Split Android screensaver and display-timeout controls; clarified that ClockDream shares the StandBy view/photo collection while Android owns Dream activation.
- Verified QR target assets on main. Fixed cache-trim false-success behavior by requiring the explicit pm trim-caches success marker; added CacheCleanerTest.
- Exact source head 9919dbaa0add6cc7ce2a84332354f46f9cc48d61; CI #602 / 41400508774 PASS. APK 1,560,565 bytes, 12,299 bytes below budget; artifact count zero and manual TV-test APK publication skipped.
- E01 audit report and PR #34 description updated. PR #34 remains Draft/open/unmerged; no canonical integration merge.
- E01 source audit closed; next task Stage E02 Profile. Physical/OEM acceptance carried to F: HOME resolver/button, service readback, power labels, cache ADB outcomes, phone installer, D-pad visual/focus, and DreamService/StandBy/display timeout. No TV settings, HOME default, package state, user data or installed APK changed.

## E01 final cache-feedback accuracy correction — 2 November 2026

- This later checkpoint supersedes the earlier E01 source head and APK-size figures in this document.
- Final E01 source head: b6fa70e406afc9f6e81d16a3635a69afc6c4d783; exact-head CI #609 / 41401277076 PASS.
- Final diagnostic APK: 1,560,609 bytes; 12,255 bytes below the 1,572,864-byte cap. No artifact published; manual TV-test APK step skipped.
- Cache byte totals now count only successfully deleted app-cache/temp files; free-space deltas are no longer attributed to app-cache cleanup. User-facing no-files-removed feedback is neutral. Android system trim success requires the explicit ADB success marker. Regression tests cover marker parsing and actual deleted-byte counting.
- E01 audit report and PR #34 description updated. PR #34 remains OPEN/DRAFT/UNMERGED under D-013; latest feature branch documentation commit 090a956a9860c82eb36c2d96848d47046fba0f0c.
- E01 source audit remains COMPLETE; next Stage E task is E02 Profile. F-stage physical/OEM checks remain mandatory and unchanged. No device settings, HOME default, package state, user data or installed APK changed.


## 2026-11-02 — E02 Profile, Avatar & App-Lock source audit

- Continued E02 on isolated branch `feature/e02-profile-avatar-lock-ui`, based on E01 exact branch baseline. PR #35 remains Draft/open/unmerged; no grouped integration or main merge.
- Audited profile avatar selection/QR, profile switch/delete/PIN gates, Child Mode, app-lock/hidden-app lists and counts, D-pad focus, and B01 modal adoption.
- Fixed app-policy enumeration to merge Leanback and generic Launcher categories, deduplicate package-level entries, exclude the launcher itself and tolerate null app labels/icons. Added regression coverage for package filter edge cases.
- Fixed a transactional gap in built-in avatar selection: selected avatar index and custom-photo reference reset now commit together; on persistence failure the old in-memory selection/reference is restored.
- Verified picker summary refresh on all dismiss paths including Back, profile-scoped policy state, avatar upload route/session/storage safeguards, and shared B01 dialog contract.
- Final source head `3ab9b55f3cfe03ad9841a119590a9183cf114e96`; exact-head CI #623 / `41406190380` PASS. Android app/installer release unit tests and Release build passed; lists/tools/docs job passed.
- APK 1,561,717 bytes; 1,572,864-byte cap; 11,147 bytes headroom. Manual publication skipped; artifact count 0.
- E02 source/CI gate closed. Project F retains all physical Mi TV/phone/remote acceptance. No APK installed, no TV settings/HOME default changed, no user data migrated.
- Full audit: [E02_PROFILE_AVATAR_LOCK_UI_AUDIT.md](E02_PROFILE_AVATAR_LOCK_UI_AUDIT.md).


## 2026-11-02 — E03 Default wallpaper / metadata checkpoint

- Audited the packaged Default asset and `MainActivity.setPlace()`: the JPEG contains embedded title/description while the launcher already provides a native `heroShelf` metadata UI.
- Created isolated E03 branch `feature/e03-wallpaper-clean-metadata` from E02 exact head `3ab9b55f3cfe03ad9841a119590a9183cf114e96`; PR #36 opened Draft against E02, not merged.
- Added exact-filename metadata provider for cached Default asset; preserved existing online/place providers as fallback. Added targeted isolation unit tests.
- CI run #624 / `41408606965` started on head `188bd49269a358ebf53f8ae09056c41a5eb80243`; lists/tools/docs job passed, launcher build/unit-test job was still in progress at this checkpoint.
- A clean text-free image concept was generated for review, but the binary replacement has NOT been committed. Current JPEG remains unchanged and visual duplicate-text issue is still open. Do not publish/install APK or claim E03 complete.
- No change to B01 global UI, D-pad, shuffle/index/interval, QR upload, StandBy storage or user preferences. Physical TV/phone/remote acceptance remains in Project F.
- Full checkpoint: [E03_WALLPAPER_METADATA_AUDIT.md](E03_WALLPAPER_METADATA_AUDIT.md).

- CI #624 / run `41408606965` completed **PASS** on exact source head `188bd49269a358ebf53f8ae09056c41a5eb80243`; both jobs passed, including `testReleaseUnitTest` and Release build. APK 1,561,797 bytes; 11,067 bytes below 1,572,864 cap. Manual publication skipped. Clean JPEG binary replacement remains uncommitted; E03 remains open.


## E03 asset + CI checkpoint — 2 Nov 2026

This checkpoint supersedes earlier statements that the clean Default wallpaper binary was not committed.
- Clean asset is now committed at `launcher/app/src/main/assets/kabut-pagi-dempo.jpg` on `feature/e03-wallpaper-clean-metadata`.
- Upload commit: `d6676c03474396c0f616284a3e43fc709226ed82`; exact PR head: `d6676c03474396c0f616284a3e43fc709226ed82`; PR #36 remains OPEN/DRAFT/UNMERGED, base `feature/e02-profile-avatar-lock-ui`.
- Git blob SHA `a73eb03584a706870e7b81447331ee3e47170e06` matches the locally prepared image byte-for-byte. Verified JPEG: 1920x1080, RGB, 230,997 bytes; filename and asset path match `MainActivity.getBundledDefaultWallpaper()` and cached metadata key `kabut-pagi-dempo-default.jpg`.
- Exact-head CI #625 / run `41410370222` on `d6676c03474396c0f616284a3e43fc709226ed82`: **SUCCESS**. Launcher build/unit tests and Lists/tools/docs both passed; `testReleaseUnitTest` and `assembleRelease` passed.
- Release APK: `1,561,913 bytes`; cap `1,572,864 bytes`; headroom `10,951 bytes`. Size gate passed. Manual TV-test APK publication was skipped; no APK was published or installed.
- Source path audit: Default copy uses the packaged asset and writes the exact cached filename expected by `WallpaperMetadata`; setPlace consults Default metadata first and preserves Place/Online fallback. Custom wallpaper and disabled-description suppression remain in the existing path. No B01 UI/focus, rotation/shuffle/index/interval, QR upload, StandBy or user-preference contract was intentionally changed.
- Remaining E03 acceptance: regression verification for Default/online/custom/description-toggle/Next/shuffle/restart and actual Mi TV/remote/phone visual/performance acceptance in Project F. QR/StandBy isolation remains to be included in integration checks. No physical device acceptance is claimed.
- Disposition: asset integration and CI are **PASS**; E03 overall remains **IN PROGRESS / NOT FINAL** until remaining regression and Project F acceptance. Preserve D-013: no early merge, no release approval.


## Offline Default wallpaper verification — 2 Nov 2026

- Source audit confirms Default wallpaper and its descriptive metadata are **offline-capable**: `getActiveWallpaperCategory()` defaults to `Default`; the Default startup branch calls `getBundledDefaultWallpaper()`; that method reads `kabut-pagi-dempo.jpg` from APK assets and stages it into app-private storage as `kabut-pagi-dempo-default.jpg`. It does not make a network request.
- `WallpaperMetadata.describe()` returns the title, description and location from hard-coded local metadata for the exact cached filename. `MainActivity.setPlace()` checks this provider before the Place/Online providers. No HTTP, feed, or online service is required for the Default description.
- The wallpaper and description therefore remain available after installation with Wi-Fi/internet disconnected, provided the installed APK contains the asset and the app can read/write its own files directory. A fresh install still requires the APK itself to have been installed first; no server fetch is involved at runtime.
- Visibility conditions remain unchanged: wallpaper descriptions must be enabled, and the existing hero shelf/focus behavior controls when the text is shown. Offline availability does not override the user's description setting.
- Scope caveat: online/place refreshers and other network-backed widgets/features may independently attempt background refreshes when configured. They are not dependencies for the Default asset or its metadata. This audit is a source-path verification; offline behavior has not yet been physically exercised on the Mi TV Stick. Add Wi-Fi-off cold-start and restart acceptance to Project F.
- Disposition: source-level offline dependency audit **PASS**; physical offline acceptance **PENDING Project F**. No source change was necessary; do not refactor the working Default path without a reproducible defect.


## E03 final source audit checkpoint — 2 Nov 2026

- Re-audited Default startup, bundled asset copy/cache, local metadata lookup, online/place metadata fallback, custom wallpaper suppression, description toggle, Next/shuffle index handling, interval scheduling and restart persistence.
- Found and fixed a repeat-selection UI edge case: selecting Default while Default was already active hid the hero shelf before re-applying the same metadata; the unchanged-title short-circuit could leave it hidden. Removed the premature hide in the Default branch so the existing hero state remains stable and metadata changes continue through the established `setPlace()` transition. No B01 UI implementation, focus contract, or wallpaper rotation algorithm was changed.
- Fix commit: `e085f8aed6fdc85b4793b76bb26ab8543b26b2cf` on `feature/e03-wallpaper-clean-metadata`.
- Exact-head CI #626 / run `41411445463` is currently **IN PROGRESS** at this checkpoint. Lists/tools/docs job has passed; Launcher build/unit tests and APK size gate are still running. Do not mark CI green until the final run conclusion is available.
- The prior clean asset remains 1920x1080 JPEG, 230,997 bytes; prior exact-head CI #625 passed at 1,561,913-byte APK (10,951-byte headroom). The new source commit requires its own fresh APK size result.
- Source audit disposition: code-path review complete; targeted fix committed; CI pending. Device-only acceptance (offline cold start/restart, visual sharpness/crop, focus/remote behavior, online/place/custom, Next/shuffle, QR/StandBy isolation and performance) remains assigned to Project F. No APK publication/install, no physical acceptance claim, no early merge under D-013.


## E03 exact-head CI #626 final result — 2 Nov 2026

This result supersedes the preceding note that CI #626 was in progress.
- Exact feature head: `e085f8aed6fdc85b4793b76bb26ab8543b26b2cf`.
- CI #626 / run `41411445463`: **SUCCESS**. Launcher build/unit tests and Lists/tools/docs jobs both passed.
- `testReleaseUnitTest`, `assembleRelease`, APK diagnostics and APK budget gate all passed. Manual TV-test APK publication was skipped by workflow policy.
- Exact Release APK size: `1,561,889 bytes`; budget: `1,572,864 bytes`; remaining headroom: `10,975 bytes`.
- The targeted Default reselection hero-shelf fix is included in the tested commit. No APK was published or installed; no physical device acceptance is claimed.
- E03 source + exact-head CI disposition: **PASS / SOURCE FINAL**. Project F retains physical acceptance: offline cold start/restart, image crop/sharpness, remote/focus, Default/online/place/custom, description toggle, Next/shuffle, QR/StandBy isolation and performance. Preserve D-013: PR #36 remains OPEN/DRAFT/UNMERGED; no early merge or release approval.


## E04 execution checkpoint — 2 Nov 2026

- Audited `StandbyPhotoStore`, `ClockDream`, `WallpaperUploadServer`, `AndroidManifest` and the relevant `MainActivity` photo/mode paths.
- E04 source branch created from E03 exact head `e085f8aed6fdc85b4793b76bb26ab8543b26b2cf`; no rebase required.
- Repair: removed the duplicate shell display-off command from `ClockDream`; now checks the single supported Accessibility global action. Commit `7c5e2677eac1c791675d9f5ef7d736eb47d37aca`.
- Draft PR #37 opened against E03. No merge. Exact-head CI pending; physical device acceptance remains Project F. No APK published or installed.


## E04 follow-up — 2 Nov 2026

- E04 source commits `75fa1b1` and `08076bb` remove the DreamService two-hour power override and MainActivity 10-minute/2h10m idle-power overrides. Android TV remains owner of native idle transitions.
- Custom StandBy photo QR upload/store/display remains preserved. Exact-head CI #629 pending; physical acceptance assigned to Project F. No APK publication/install or merge.

- CI #629 / `41418393199` for exact head `08076bb945a169de198905152a092f419f48aa82`: **SUCCESS**. APK 1,561,373 bytes / 1,572,864-byte budget / 11,491-byte headroom. No APK published or installed. E04 source repair and CI PASS; hardware validation remains Project F.

## 2 Nov 2026 — Global Glass Effect OFF surface repair
- Identified cross-project root cause: decorative glass OFF state omitted structural surfaces across pills, Home menus, anchored popups, dock and app dialogs.
- Implemented B03 global fallback policy on `feature/b03-dialog-opacity-layering` (PR #12, head `8cf0a846f3a7738cc1bb20f663dac9cf961e98da`): shared opaque popup/dock tokens, solid GlassDrawable and DockDecoration fallback, runtime modal material choice, and JVM token regression test. Exact-head CI #685 queued at checkpoint.
- E07 live-toggle integration independently passed exact-head CI #682 / `41446777187` at head `7ecb45a634e6ed9792fd87f3110ffe0b6eaad1a2`. Existing preference and callback retained; active app-owned popups are invalidated after change.
- Ownership: B03 global policy; E07 toggle integration; G grouped source reconciliation; F physical acceptance. D-013 preserved; no merge or APK publication/install. F 1080p/4K TV test remains pending.
- Report: [B03_DIALOG_OPACITY_AUDIT.md](B03_DIALOG_OPACITY_AUDIT.md).


## B03 exact-head verification update — 2 Nov 2026
- Supersedes the earlier pending-CI checkpoint: B03 source/test head `8cf0a846f3a7738cc1bb20f663dac9cf961e98da`, exact-head CI run #685 / `41447473358` **SUCCESS**. Both Launcher build/unit tests/release build/APK-size guard and Lists/tools/docs passed.
- Release APK diagnostic: 1503 KB (<1.5 MiB / 1536 KiB); APK publication skipped. PR #12 remains OPEN/DRAFT/UNMERGED and mergeable.
- E07 exact-head CI run #682 / `41446777187` also **SUCCESS** on head `7ecb45a634e6ed9792fd87f3110ffe0b6eaad1a2`; toggle/live-popup invalidation integration is source/CI verified.
- Source-level global fallback contract is now verified in both owner branches. G must reconcile shared-file overlap at grouped integration; F must still perform physical 1080p/4K visual and D-pad acceptance. No APK installed or device test claimed.
- Detailed matrix: [B03_DIALOG_OPACITY_AUDIT.md](B03_DIALOG_OPACITY_AUDIT.md).


## Final Glass OFF fade-preservation verification — 2 Nov 2026
- Follow-up refinement: the solid fallback fill and border in `GlassDrawable` now honor the existing fade alpha. This keeps the popup open/close animation when Glass Effect is OFF while preserving the settled opaque surface.
- B03 source/test head `d87ffdf914851b8a1d2c67c2416ac9f5f1309d93`; exact-head CI #687 / `41447950284` **SUCCESS** (Launcher build/unit tests/release/APK-size guard and Lists/tools/docs). Release APK diagnostic 1503 KB; publication skipped.
- E07 source/test head `6dc39d76e3c897e841854f425d549dbfebf1025c`; exact-head CI #688 / `41447965099` **SUCCESS** (both jobs). APK 1,564,137 bytes / 1,572,864 cap / 8,727 bytes headroom; publication skipped.
- Latest E07 PR branch head `f9779576505d1cb429b3563bd09317b59104a24c` is documentation-only after the tested source head; no source/resources/tests/build inputs changed by that follow-up. PR #12 and #40 remain Draft/open/unmerged under D-013.
- Global source/CI work is verified in both owner branches. Project F physical acceptance remains pending: glass ON/OFF, all app-owned popup/dialog/dock surfaces, focus/D-pad/Back, live toggle, 1080p and 4K. No APK installation or physical test claimed.
- Report: [B03_DIALOG_OPACITY_AUDIT.md](B03_DIALOG_OPACITY_AUDIT.md).


## Ownership clarification — B01 token catalog / B03 surface policy — 2 Nov 2026
- `TvUiTokens` remains B01-owned. B01 owns the canonical shared color/geometry token catalog; B03 does not take ownership of it.
- B03's source repair extends that catalog with `POPUP_SURFACE` and `DOCK_SURFACE` fallback entries and owns applying shared tokens to app-owned popup/dialog/dock surfaces. The existing `BORDER` token is reused.
- E07 owns the Glass Effect preference/toggle and live popup invalidation. G owns final shared-file reconciliation during D-013 grouped integration. F owns physical 1080p/4K acceptance.
- This clarification supersedes any earlier shorthand that described B03 as owner of the shared tokens themselves. Evidence and exact-head CI remain unchanged.
- B01 PR #10 description updated to preserve the two fallback token additions during grouped reconciliation; no merge or ownership transfer.


## Current final checkpoint — Glass OFF fallback source and ownership — 2 Nov 2026
- B03 source/test branch head `d87ffdf914851b8a1d2c67c2416ac9f5f1309d93`, PR #12 OPEN/DRAFT/UNMERGED; exact-head CI #687 / `41447950284` PASS. Release diagnostic 1503 KB; APK publication skipped.
- E07 source/test head `6dc39d76e3c897e841854f425d549dbfebf1025c`, exact-head CI #688 / `41447965099` PASS. APK 1,564,137 bytes; 8,727-byte headroom; publication skipped. Latest PR #40 head `a0f2b0fff6eef0c42852d21b971904d5acd29d5c` is documentation-only after that verified source/test head; PR remains OPEN/DRAFT/UNMERGED.
- B01 owns the `TvUiTokens` catalog; B03 owns applying the shared tokens to global popup/dialog/dock surface policy; E07 owns Glass Effect toggle and live popup invalidation; G owns grouped shared-file reconciliation; F owns physical acceptance.
- GlassDrawable solid OFF fallback preserves fade alpha, so popup transition behavior is retained. Custom-content dialogs with explicit root backgrounds remain content-owned; `TvDialogStyle.applyCustom()` is intentionally transparent and does not overwrite those surfaces.
- Remaining gate is Project F physical test only: glass ON/OFF, top pills, Wi-Fi/Quick Settings menus, anchored PopupWindow, shared modal dialogs, dock, D-pad/focus, Back/dismiss, live toggle, 1080p and 4K. No APK installation or physical test claimed.
- Full test matrix and source details: [B03_DIALOG_OPACITY_AUDIT.md](B03_DIALOG_OPACITY_AUDIT.md) and [E07_ANIMATIONS_EFFECTS_AUDIT.md](E07_ANIMATIONS_EFFECTS_AUDIT.md).


## E08 source/CI closure checkpoint — 2 Nov 2026
- E08 branch: `feature/e08-wallpaper-settings-redesign`; Draft PR #41 targets E07 exact parent head `a0f2b0fff6eef0c42852d21b971904d5acd29d5c`; PR remains OPEN/DRAFT/UNMERGED under D-013.
- Source/test head: `b386986f08951c4dcf12ba8d72638b091a264803`; exact-head CI #693 / `41469544565`: **SUCCESS**. Launcher build/unit tests, release build, APK-size guard, and Lists/tools/docs all passed.
- Release APK diagnostic: 1,565,397 bytes; 1,572,864-byte cap; 7,467 bytes headroom. APK publication skipped; no installation/device test claimed.
- Unified wallpaper control center now has three tabs: Change Wallpaper, Shuffle Interval, Next Wallpaper. Appearance and Quick Control route to the same entry point. Existing category selection, custom manager/QR upload, shuffle-cycle preferences, interval timer, asynchronous decoder, metadata and rotation engine are reused; no duplicate wallpaper engine or preference schema was introduced.
- Shuffle toggle preserves the currently displayed image; Next handles single-image collections explicitly; interval zero is accurately described as manual-only/no automatic change. Existing QR, deletion, slideshow, cache/provider and StandBy paths remain owned by their existing projects.
- Detailed report: [E08_WALLPAPER_SETTINGS_REDESIGN_AUDIT.md](E08_WALLPAPER_SETTINGS_REDESIGN_AUDIT.md).
- Project F still must verify D-pad/Back/focus, Glass ON/OFF, 1080p/4K, category/online/offline, shuffle cycle, interval/manual-only, Next single/multi-image, process lifecycle, QR upload and Wallpaper/StandBy storage isolation. No physical acceptance is claimed.
- E08 source/CI: PASS. Grouped integration and merge remain deferred under D-013; no release approval or merge was performed.


## E08 final exact-head verification — 2 Nov 2026
- This checkpoint supersedes the earlier E08 source/test head and APK figures above.
- Final source/test head: `dcec6afc805a9d7c78702ad8d51b20b87bd751a3`; Draft PR #41 remains OPEN/DRAFT/UNMERGED and targets E07 exact parent `a0f2b0fff6eef0c42852d21b971904d5acd29d5c` under D-013.
- Exact-head GitHub Actions CI #695 / `41470304782`: **SUCCESS**. Launcher build/unit tests, `testReleaseUnitTest`, `assembleRelease`, APK-size diagnostic/guard, and Lists/tools/docs all passed.
- Final APK diagnostic: 1,565,417 bytes; 1,572,864-byte cap; 7,447 bytes headroom. Publication skipped; no installation.
- Final D-pad refinement preserves focus on the Shuffle control after its status panel refreshes. No wallpaper engine, preference schema, QR route or storage ownership changed.
- Current feature branch head `086ad1d112b346a281c88273fa620e9348d2cccc` is a documentation-only audit follow-up after the verified source/test head; no app source/resources/tests/build inputs changed.
- Project F physical acceptance remains mandatory: D-pad/focus/Back, Glass ON/OFF, 1080p/4K, category and online/offline paths, shuffle cycle, interval/manual-only, Next one/multi-image, process lifecycle, QR upload and Wallpaper/StandBy storage isolation. No physical acceptance is claimed.
- E08 source/CI: PASS. D-013 grouped integration/merge remains deferred pending F/G and explicit user approval.


## E08 compact popup/UI consistency checkpoint — 2 Nov 2026
- E08 branch final source/test head: `fcd7d8f23a4833830a401652ef269db3282b7300`; Draft PR #41 remains OPEN/DRAFT/UNMERGED, targeting E07, under D-013.
- UI audit found bespoke styling in the new control center and older styling in category/interval/custom-photo subdialogs. These are now aligned with B01 `TvUiTokens` and B01/B03 shared `TvDialogStyle` material/viewport contract; no global token ownership change was made.
- Main control center is compact (max 680×420 dp), with short Change/Shuffle/Next tabs and concise actions. Category/interval pickers use compact single-line card rows with common selected/focus states and no redundant explanatory copy. Photo manager/confirmation/action dialogs use the same modal material and viewport sizing contract.
- Function wiring remains unchanged: category selector, shuffle/interval preferences and Next reuse the existing wallpaper engine, keys, timer, QR upload and storage owners. D-pad focus is retained after Shuffle refresh; no new engine/schema/permission/network path.
- Exact-head CI #702 / `41473357877`: **SUCCESS**. Launcher build/unit tests/release/APK-size guard and Lists/tools/docs passed. APK 1,562,709 bytes; 1,572,864-byte cap; 10,155 bytes headroom. Publication skipped.
- Detailed report: [E08_WALLPAPER_SETTINGS_REDESIGN_AUDIT.md](E08_WALLPAPER_SETTINGS_REDESIGN_AUDIT.md).
- Project F still must verify the compact modal on 1080p/4K, D-pad/focus/Back, Glass ON/OFF, category selection, shuffle/interval/Next, QR photo manager and lifecycle persistence. No physical device acceptance or APK installation is claimed.
- E08 source/UI/CI: PASS. Project F physical acceptance and D-013 grouped integration/merge remain pending.


## Decision / checkpoint: Project E closed, Project F next — 2 Nov 2026
- User decision: close Project E discussion and finalize E01–E08 feature-development stage. Project F is the next active stage.
- E08 source/UI/CI final: source/test head `fcd7d8f23a4833830a401652ef269db3282b7300`; CI #702 / `41473357877` PASS; APK 1,562,709 bytes with 10,155 bytes headroom under 1,572,864-byte cap; APK publication skipped. Latest PR #41 head `8dfdcabd19853a1140724ea68983877af17bed0c` is documentation-only after tested source/test head.
- E08 visual contract: compact Quick Control wallpaper center vs fuller Appearance wallpaper-management surface; distinct roles, shared underlying state and styling contract. Project F to confirm on physical TV.
- PR #34–#41 all remain OPEN/DRAFT/UNMERGED under D-013. No merge, APK installation, physical test or release approval was performed by this closure.
- F entry checklist: dependency/combined-diff audit; verify exact-head CI evidence; integrated build/regression; Android TV 1080p/4K acceptance; D-pad/focus/Back; popup/dialog and Glass ON/OFF; cross-module interactions; lifecycle/persistence; wallpaper QR/upload/delete/shuffle/interval/Next and Wallpaper/StandBy isolation. Any verified defect is routed to its actual owner and rechecked end-to-end.


## 3 November 2026 — Project F02 audit and remediation

- Started/continued F02 integrated system validation on `integration/project-f02-system-validation`; PR #50 remains Draft/open/unmerged.
- Confirmed and fixed D05 Settings search modal contract regression: both search input and results dialogs now use B01 `TvDialogStyle.apply`; added source regression test. Restored the B01 helper dependency into D05/D06 branches after exact-head CI exposed a missing-class compile failure.
- Expanded B01 global modal enforcement: MainActivity `styleGlassDialog` delegates to a sized overload that applies the shared B01 contract, then preserves typography/focus styling. Custom geometry remains downstream; anchored PopupWindows and transparent custom Dialogs remain documented exceptions.
- Added MainActivity modal contract regression test. CI caught a test assertion that assumed the integrated-only Device Settings wrapper existed on standalone B01/D05/D06 branches; corrected the test to conditionally verify that optional wrapper rather than weakening core assertions.
- D01 security follow-up: LocationBroadcastReceiver now requires `android.permission.DUMP`, preserving both existing actions and the documented ADB shell/system caller route. Added manifest regression test and trust-boundary documentation. Exact-head CI passed on `bbb13dd107c86cda85bd1482c2f050ca69dfc4e2`; real TV ADB compatibility remains Project G.
- D06 security follow-up: AzanActivity changed to `exported=false`; explicit app-owned notification PendingIntent path was verified in source. Added manifest regression test. D06 owner retest pending.
- F02 integration mirrored all three source fixes and regression tests. Earlier integrated head `27ecd5e4e5e283f5d1675cb28a75b2f1cbb0a6c7` passed both jobs; newer head `1658237bafdb24167391270f3ad8f694751a6c2a` queued at this checkpoint.
- B01/D05/D06 owner-branch exact-head CI was running. No finding closed; no APK published/installed; no physical Android TV test; no PR merged.


## 3 November 2026 — Additional F02 findings and fixes

- Extended exported-component audit: set LocationActivity non-exported (no external intent filter; explicit MainActivity intent retained). D01 follow-up PR #51 latest head `66352838e6155a42a9ea61477643a238f360434f` CI PASS.
- Found a Home-key functional regression: HomeKeyService consumed every Home key release as a single press, preventing documented double-Home Recent Apps routing. B01 added HomeKeyPressTracker (350 ms double window, 400 ms single delay), explicit RecentAppsActivity launch, Close All routing when already foreground, and preserved 450 ms long-press Settings. App Switch remains disabled.
- Unified RecentAppsActivity fallback threshold with HomeKeyPressTracker. Added JVM timing tests and Python source-contract guard.
- Settings-key input now cancels pending Home gesture actions to prevent delayed Home navigation overriding Settings.
- Added P3 review item: RecentAppsActivity remains exported with ACTION_MAIN; no in-repository external caller found, but external OEM/assistant/remote compatibility must be verified in Project G before changing the contract.
- Robolectric API 30 tests for AlertDialog window layout/dim behavior passed on prior F02 head `5f6b0c60949c1284e0e904e614e85c2e0ca657f2`; latest head retest pending.
- Latest B01 #10 head `867c7a9d0977e21b69c8078a400458b00ba2d2dd` and F02 #50 head `eea920469ffe5bef5ddb14e1dfd66687e82d8fed` were in CI. D05 #29, D06 #30 and D01 #51 exact-head CI passed.
- No merge, APK publication/installation, or physical Android TV test.


## 3 November 2026 — F02 continued audit: callback isolation and Recent Apps security

- Found shared Handler collision in HomeKeyService: Force Stop queue called `removeCallbacksAndMessages(null)` on the Handler also scheduling Home single/double/long-press and power-action timeout callbacks. Fixed in B01 and F02 by introducing `forceStopHandler`; all Force Stop timeout/continuation work now uses that dedicated queue, and onDestroy cleans it. Added `test_home_key_force_stop_handler_isolation.py`.
- Exported-component audit revisited RecentAppsActivity. Source showed its exported explicit `ACTION_CLOSE_ALL` path posts `onCloseAllClicked()`, which can force-stop the recent apps. This is a confirmed P2 security defect, not merely a compatibility review. Set `android:exported=false`, removed unnecessary MAIN intent filter, retained HomeKeyService explicit in-app launch, and added `test_recent_apps_manifest_security.py`.
- Latest B01 owner head `a1e743ad88c28ca3a9602dfc9d0b5da16e46285d`; latest F02 integration head `3ea479d689dc732fe46594a0677d02b7762d7527`. Both exact-head CI in progress.
- Prior F02 head `c9496d4e051980e640bc269ffce695b95ba71ad4` passed both jobs before these fixes; it is not evidence for the latest SHA.
- No merge, APK publication/installation, or physical Android TV test.


## 3 November 2026 — F02 exact-head pass and restart action consistency

- F02 integration head `5dd787ba136dfb070eaa2e833af653e57ec66015` passed both CI jobs: run #41509632784 Launcher build/unit tests; run #41509626957 Lists/tools/docs.
- B01 owner head `791620ce4628ee9e909af7ce2f4758b6df77afeb` passed both jobs (run #41509576279). A later comment-only alignment commit `389e289e939276ce9e20d7e6c82c293d3f83c70f` has a queued exact-head retest.
- Cross-branch source audit found B01 still claimed AccessibilityService action ID 12 was reboot, while F02 had already removed that invalid call. Backported the correct existing system power-menu path to B01 and added `test_home_key_power_action_contract.py` to owner and integration.
- Verified B01 and F02 `HomeKeyService.java` are now byte-identical at blob SHA `b3cd4b2b4092a90f4f1faefb1072c15e2203b314`.
- F02 findings F02-FUNC-002 (Force Stop/Home handler collision), F02-SEC-004 (exported Recent Apps Close All) and F02-PLAT-001 (invalid restart action ID) fixed and covered by exact F02 CI at 5dd787b.
- No merge, APK publication/installation, or physical Android TV test.


## 3 November 2026 — C07/F02 wallpaper mutation shutdown race

- Cross-module source audit found WallpaperUploadServer's `/api/select`, `/api/delete`, `/api/slideshow`, and `/api/clear` handlers only relied on the outer dispatcher session check. A worker already past that check could mutate wallpaper state/files after the QR server's `stop()` returned.
- Fixed on C07 owner PR #24 and mirrored to F02: all four handlers now synchronize on the same server monitor as `stop()` and recheck session validity before state/file changes. Revoked sessions return HTTP 410 Gone JSON.
- Added `tests/test_wallpaper_session_shutdown_atomicity.py` on owner and integration branches.
- C07 owner head `12b1f431f7687023727983089ea03eda0f97a73d`; F02 head `081496ca93fd0526025a21ce7cd0e3d7f9c3364a`; exact-head CI in progress.
- No API route, payload, upload limit, browser progress/cancel UI, or StandBy persistence behavior was altered.
- No merge, APK publication/installation, or physical Android TV test.


## 3 November 2026 — F02 upload deadline retest and dependency reconciliation

- Polled C07 owner and F02 integration CI after the injectable socket timeout seam. Failure in `UploadDeadlineInputStreamTest.appliesRemainingDeadlineToEachRead` was isolated to line 45: deadline 6000 ms minus fake monotonic time 2500 ms equals 3500 ms, not the asserted 3000 ms.
- Corrected the expected value to 3500 ms in C07 owner branch and F02 integration. Production timeout logic unchanged; no tests removed or weakened.
- C07 owner PR #24 head `80a92264c51631fb107c75f32a3cd8e4b8aa9d9a`: CI run #41512386827 SUCCESS, both jobs. F02 source/test head `ddf130a348182546b28ff59c292c9b5ceebacb2e`: push run #41512382568 SUCCESS, both jobs. Matching PR run #41512391849 had all substantive steps pass; final cleanup still running at last poll.
- Cross-branch diff review found F02's `WallpaperUploadServer` differs from C07 owner only in the downstream D05 StandBy persistence adapter: D05 changes `StandbyPhotoStore.addUploadedFile` to return `UploadResult`, and the F02 handler preserves the stored file on persistence failure to avoid dangling live URI references. F02 retains C07's monotonic deadline and synchronized session-mutation fixes. This is expected dependency integration; do not copy the older C07 boolean handler over it.
- Confirmed owner CI: B01 #10 head `389e289e939276ce9e20d7e6c82c293d3f83c70f` run #41509724289 PASS; D05 #29 #41505852041 PASS; D06 #30 #41505865772 PASS; D01 #51 #41506847521 PASS.
- Updated F02 findings register and integrated validation document. F02 new docs-only head `b64715156e180edc3bfae25bffba6a089544247f`; exact-head CI retest required.
- Updated PR #24 and #50 bodies with current evidence. Both remain Draft/open/unmerged. No APK publication/installation or physical Android TV testing.


## 3 November 2026 — InputKeyService manifest audit, fix and F02 retest

- Full F02 manifest inventory found `.InputKeyService` extends AccessibilityService and declares the AccessibilityService intent action but was marked `android:exported="false"`, despite the opt-in installer enabling it through INPUT_KEY_SERVICE=1 / -InputKeyService. Android's service contract restricts non-exported services to same-app/same-user binding, so the system could not bind it.
- Fixed on E01 owner PR #34: changed InputKeyService to exported=true; retained the system-only `android.permission.BIND_ACCESSIBILITY_SERVICE` permission, service metadata and opt-in behavior. Added `tests/test_accessibility_service_manifest_security.py` to require every AccessibilityService action component to be exported and permission-protected.
- Regression proof: F02 test-only commit `fc3e3fede83144c80781ce72378f9d4bab5c8df3` failed exactly because InputKeyService exported was false. After manifest correction, F02 PR run #41513906117 on `5a01785cfd4ab81e816a955bd7647c0e766c6b2f` passed both jobs; 35 Python tests passed including the new guard.
- E01 latest head `edd6461c932705a109ed9768649cae7e3c162c59`: run #41513716451 both jobs PASS.
- F02 latest head `558762994dec05a7ae03a924c33a3ed11665c224`: push run #41513940620 both jobs PASS; matching PR run #41513947375 had all substantive steps pass and cleanup pending at last poll.
- F02 APK size measured 1,564,977 bytes against 1,572,864-byte budget; headroom 7,887 bytes. R8 minify and resource shrinking are enabled. Keep as a P3 watchlist; do not remove wallpaper/font assets without quality-safe profiling.
- InputKeyService visibility, user enable/disable and KEYCODE_TV delivery on actual OEM firmware remain DEFERRED_TO_G. No physical device testing, APK publication or merge.


## 3 November 2026 — latest F02 exact-head verification

- F02 PR #50 latest head: `91ae962bae04914a3888210e5f997b24151d5a62` (findings-register documentation-only commit; no source/test changes after head `5587629`).
- PR workflow #41514480902 completed SUCCESS on this exact head: Lists/tools/docs and Launcher build/unit tests both passed. The launcher log confirms release-validation build success, 35 Python tests including the accessibility-service manifest guard, and APK size 1,564,977 bytes against 1,572,864-byte cap (7,887 bytes headroom).
- Matching push workflow #41514472847 initially failed only because Maven Central returned HTTP 429 while downloading Robolectric `nativeruntime-dist-compat-1.0.16`; this is external dependency rate limiting, not a source/test failure. Only the failed launcher job was re-run (attempt 2); it was still in progress at the time of this note. Lists/tools/docs job passed.
- PR #50 remains Draft/open/unmerged. No APK publication, installation or physical Android TV testing. Device acceptance remains Project G.

| 2026-11-03 | F04 Avatar/Wallpaper/StandBy isolation | [F04 PR #53](https://github.com/Frangga11/glass-tv-home/pull/53); [C07 PR #24](https://github.com/Frangga11/glass-tv-home/pull/24); [E08 PR #41](https://github.com/Frangga11/glass-tv-home/pull/41); CI #41560230282 | Distinct storage/QR/rendering/settings contracts verified; repaired Wallpaper delete false-success on E08 owner and partial QR API clear on C07 owner; mirrored minimal diffs into F04. F04 source head CI passed 63 Python tests (1 intentional skip), Android unit/release build and APK size gate; 1,566,045-byte APK, 6,819-byte headroom; publication skipped. C07 owner #41560200291 and E08 owner #41560600033 both passed exact-head CI. Latest F04 head `6ffc8b4c253beb2662d6978ba38033039c3c53f9` passed #41560811925 (both jobs; 65 tests / 1 intentional skip; Android unit/release build and size gate PASS). APK 1,566,045 bytes, 6,819-byte headroom; no artifacts. F04 HOST/SOURCE EXIT PASS; Project G device gates remain open. | Poll C07/E08 owner exact-head CI, finalize F04 audit, retain Project G device gates |

| 2026-11-03 08:41 WIB | F04 post-network-interruption re-verification | PR #24/#41/#53; CI #41560247050 / #41560595614 / #41560815870 | Rechecked all three latest exact heads: C07 21 tests + 1 intentional skip, APK 1,550,961 B; E08 11 tests + 1 intentional skip, APK 1,564,089 B; F04 65 tests + 1 intentional skip, APK 1,566,045 B. All jobs SUCCESS; artifact lists empty. F04 branch 21 ahead / 0 behind approved F02 base; seven-file scoped delta. Re-read delete-failure paths, QR clear callback and locale strings; no new source blocker. No source changes. | F04 HOST/SOURCE EXIT remains PASS; physical acceptance Project G, persistence lifecycle F05 |


## 3 November 2026 — Project F05 persistence/recovery audit and targeted repairs

- Created F05 integration branch `integration/project-f05-persistence-recovery` from approved F04 exact head `6ffc8b4c253beb2662d6978ba38033039c3c53f9`; opened Draft PR #54 against F04. Current checkpoint head `1048b0b68ae5ab5a2ad867183236b514e0c691fa`; 14 commits ahead / 0 behind, five scoped files.
- F05-WALL-001 reproduced with a failing source regression: process interruption between custom wallpaper file deletion and UI preference reconciliation could leave `custom_single_mode/custom_single_file` pointing at a missing image; empty Custom collection could remain selected. E08 owner repair adds startup reconciliation, validates safe file name/existence/non-zero size, clears stale pin, falls back to Natural only for an actually empty collection, and preserves state on directory read failure. Owner PR #41 head `edd24d72256d06da4578971ea7bac1802746a3a6` exact CI #41562661052 both jobs SUCCESS; 15 tests / 1 intentional skip; APK 1,564,197 bytes / 8,667-byte headroom; publication skipped. Mirrored to F05.
- F05-WALL-002 audit found custom Wallpaper QR partial `custom_*.upload` files were deleted on normal request paths but could survive process kill indefinitely; unlike Avatar and StandBy, no startup cleanup existed. C07 owner added private-path-scoped cleanup and a synchronized process-local active-upload registry so live workers are not deleted during Activity recreation; worker finally unregisters and removes incomplete temp files. MainActivity invokes cleanup at startup. C07 owner PR #24 latest head `2315f12472f469843c1441ad7d01c6898858c4c5`, run #41563429553 Lists/tools/docs PASS (23 tests / 1 intentional skip), Android build in progress. Mirrored into F05.
- Added cross-feature F05 source-contract tests for Avatar/Profile, StandBy atomic commit and cleanup, navigation/settings persistence, wallpaper recovery, QR temp upload cleanup and prayer boot receiver. F05 PR run #41563437433: 80 tests / 1 intentional skip PASS; Android unit/release build and APK size gate in progress.
- No preference schema/key migration, user-data reset, APK publication/installation, merge, device-setting change, or physical TV test. Force-stop/process-death/reboot acceptance remains Project G.


## 3 November 2026 — F05 final host/source exit and handover

- Final F05 integration PR #54 exact head `1048b0b68ae5ab5a2ad867183236b514e0c691fa`; CI #41563437433 both required jobs SUCCESS. Lists/tools/docs: 80 Python tests / 1 intentional README-count skip. Android `testReleaseUnitTest`, `assembleRelease`, APK diagnostics and size gate PASS. APK 1,566,861 bytes / 6,003-byte headroom; artifacts empty.
- C07 owner PR #24 exact head `2315f12472f469843c1441ad7d01c6898858c4c5`; CI #41563429553 both jobs SUCCESS; 23 tests / 1 intentional skip; APK 1,551,661 bytes / 21,203-byte headroom; artifacts empty.
- E08 owner PR #41 exact head `edd24d72256d06da4578971ea7bac1802746a3a6`; CI #41562661052 both jobs SUCCESS; 15 tests / 1 intentional skip; APK 1,564,197 bytes / 8,667-byte headroom; artifacts empty.
- Re-read current source and compared owner/integration snippets: E08 startup selection recovery helper and tests match exactly; C07 wallpaper upload orphan cleanup, active registry, worker-finally cleanup and tests match exactly. MainActivity startup invokes the C07 cleanup method in both owner and F05 integration.
- Rechecked PR governance: #24, #41 and #54 are all open Draft PRs, none merged. No APK publication/installation, device setting changes, or physical TV tests.
- **F05 HOST/SOURCE EXIT PASS.** No remaining reproducible host/source blocker. Force-stop/process-death/reboot, prayer alarm restoration, StandBy/DreamService, phone/LAN and OEM acceptance remain Project G. Next host/source stage: F06 Localization.


## 3 November 2026 — Project F07 performance, stability and storage audit

- Started isolated F07 branch `integration/project-f07-performance-stability` from F06 exact head `487099013e6d9bd65cc07d72e760b1e14ab1f091`. Final source/test head `976e068d48ecc1ede2811ac455372395d382ba76`; PR #56 Draft/open/unmerged; compare 19 ahead / 0 behind, ten changed paths.
- Audit found per-frame RadialGradient allocation in weather animation, repeated rain ripple allocation, MainActivity fallback and QR preview bitmap decode on UI thread, Now Playing Binder/notification polling on main thread, unbounded StandBy decode queue, Backdrop OOM/partial-array recovery weakness, forced System.gc(), and count-only online wallpaper cache.
- Repairs: cached sun/moon/cloud shaders and matrices; 16-object ripple pool; background wallpaper and QR preview decode with latest-generation guards and stale bitmap release; dedicated coalesced Now Playing worker; Backdrop OOM/rejected-worker protection and scratch-array recovery; bounded two-item StandBy decode queue at background priority; removed forced GC; Online Wallpapers cache now capped at 150 files and 512 MiB total with oldest-first pruning and metadata cleanup.
- Added Python F07 source-contract guards, Robolectric weather rendering/shader-reuse tests, and OnlineWallpapers byte/count pruning JUnit tests.
- Exact-head Actions #41568715178 on `976e068d48ecc1ede2811ac455372395d382ba76`: Lists/tools/docs and Launcher build/unit tests jobs SUCCESS. Python 99 tests PASS; launcher and installer `testReleaseUnitTest`, `assembleRelease`, APK diagnostics and size guard PASS. APK 1,571,293 bytes / 1,572,864-byte cap / 1,571-byte headroom. Artifact list empty.
- **F07 HOST/SOURCE EXIT PASS; overall device performance acceptance PARTIAL.** No APK published/installed, no TV settings changed, no physical test or device metrics claimed. Mi TV Stick RAM/FPS/frame-time/ANR/crash/thermal/long-run soak is deferred to Project G after explicit TV-test APK readiness approval.
- Full report: `F07_PERFORMANCE_STABILITY_AUDIT.md`. Next workstream: Project G final integrated audit and D-013 staged integration/merge process.


## OFFICIAL PROJECT F CLOSURE & PROJECT G HANDOVER — 3 NOVEMBER 2026

**Checkpoint ID:** GTVH-F-CLOSE-2026-11-03  
**Project F disposition:** CLOSED — HOST/SOURCE EXIT PASS (F01–F07)  
**Project G disposition:** READY FOR EXECUTION — FINAL INTEGRATION, DEVICE ACCEPTANCE & RELEASE GATES  
**Approval:** User explicitly declared Project F officially closed and directed synchronized documentation for Project G.

### Frozen Project F baseline
- F07 integration branch: `integration/project-f07-performance-stability`
- F07 exact source/test HEAD: `976e068d48ecc1ede2811ac455372395d382ba76`
- F07 base F06 HEAD: `487099013e6d9bd65cc07d72e760b1e14ab1f091`
- F07 Draft PR #56: OPEN / DRAFT / UNMERGED; preserve under D-013.
- Exact-head CI #41568715178: both required jobs SUCCESS; Python 99/99; launcher and installer unit tests PASS; release build, APK diagnostics and size gate PASS.
- Diagnostic APK size: 1,571,293 bytes / 1,572,864-byte cap / 1,571-byte headroom. Workflow artifact list empty; no APK published or installed.
- F01–F07 are closed for their approved host/source scope. This is not a claim of full real-device acceptance.

### Explicit boundary carried into Project G
No physical Mi TV Stick profiling or soak was performed during F. No measured FPS/frame time, RAM/PSS, ANR/crash rate, thermal behavior, OEM/remote behavior, phone-to-TV LAN QR acceptance, or reboot/process-death acceptance is claimed. These are mandatory Project G evidence, not waived items.

### Official Project G roadmap — approved v1.0 (3 November 2026)
Project G contains exactly five stages. The overall A–G roadmap remains 47 work items; do not add, remove, reduce, or reorder any item.
1. **G01 — Penyelesaian Bug Terakhir (Final Bug Resolution).**
2. **G02 — Build APK Final yang Sudah Teruji (Final Tested APK Build).**
3. **G03 — Uji Instalasi pada Mi TV Stick (Mi TV Stick Installation Test).**
4. **G04 — Finalisasi Dokumentasi GitHub (Final GitHub Documentation).**
5. **G05 — Finalisasi Memory dan Handover ChatGPT (Final ChatGPT Memory/Handover).**

### Non-negotiable governance
- D-013 remains binding: owner branches are preserved; integration is isolated and staged; findings are fixed at the owning source; no early merge; final merge is a Project G decision requiring explicit approval.
- No unapproved APK install, HOME-default change, ADB/device setting change, data wipe, or package-state mutation.
- Never label device-only gates PASS without device evidence. A successful CI build is not a substitute for real-device acceptance.
- Maintain the workflow Trigger → Input → Verification → Work → Decision Point → Output → Quality Check → Final/Not Final.
- At each Project G session end, update HANDOVER, OPERATIONS_LOG, MASTER, INVENTORY, AUDIT_REGISTER, DECISION_LOG and roadmap; keep README status accurate.

### Project F closure decision
Project F is formally closed at HOST/SOURCE EXIT PASS for F01–F07. The remaining physical and final integration obligations are transferred to Project G with explicit gates above. PR #56 and all owner PRs remain unmerged; the F07 baseline is frozen as evidence, not promoted to main.


## G01 final bug resolution checkpoint — 3 November 2026

- **Disposition:** HOST/SOURCE DEFECT AUDIT PASS; no newly reproducible source defect found in the remaining high-risk contracts. No speculative production edit was made.
- **Evidence:** `docs/project-management/G01_FINAL_BUG_RESOLUTION_AUDIT.md` records the root-cause review, owner PR exact heads, associated exact-head GitHub Actions runs, both required jobs, regression protection, and deferred device gates.
- **Verified contracts:** B01 Home gestures/Force Stop/power routing/Recent Apps manifest; D01 receiver authorization; D05/D06 persistence and recovery; C07 QR deadlines, session-stop atomicity and shared storage reservation; E01 AccessibilityService binding; E08 wallpaper recovery; F02 cross-module dialog/security/upload regressions; F06 localization; F07 performance/memory hardening.
- **Latest F07 evidence:** PR #56 exact head `976e068d48ecc1ede2811ac455372395d382ba76`; CI run #41568721270, both required jobs SUCCESS; 99 Python tests; launcher/installer unit tests, release build and APK budget PASS; diagnostic APK 1,571,293 / 1,572,864 bytes; artifact list empty.
- **Governance:** all owner PRs remain Draft/open/unmerged under D-013. No APK publication/installation, merge, HOME-default change, ADB/device mutation, or TV setting change.
- **Not passed:** real Mi TV Stick remote/OEM/UI, ADB-shell permission, phone/LAN QR, low-storage, process-death/reboot, RAM/FPS/ANR/thermal/soak acceptance remain G03 device gates.
- **Next approved stage:** G02 final tested APK build. TV installation remains G03 and requires explicit readiness approval.



## G01 branch reconciliation addendum — 3 November 2026

Project F is closed at HOST/SOURCE EXIT PASS. G01 defect-resolution audit is PASS, but project-wide branch reconciliation remains OPEN/BLOCKING: current B-series and F07 integration candidates have diverged, with 26 shared-file differences requiring semantic reconciliation. G02 final APK build is blocked until a clean isolated combined candidate passes exact-head integrated CI. No feature PR/main merge, APK publication/installation, or TV state change has occurred.

- Live repository inventory covers PRs #1–#56. Merged history: #1, #13–#15, #32–#33. Closed without merge: #42–#49 and superseded #52. Open/unmerged source/integration PRs: #2–#12, #16–#31, #34–#41, #50–#51, #53–#56.
- F07 chain remains linear from staged-full through #50 → #53 → #54 → #55 → #56. F07 exact head `976e068d48ecc1ede2811ac455372395d382ba76` is 173 commits ahead / 0 behind staged-full `8fe90a9fdd493c5d81ffdacbe16054630a26580a`.
- Current `integration/b-series` is `9070894fa6d9ef8aec034f46dfe777e866709dfe`, not an ancestor of F07. Compare staged-full → B-series: DIVERGED, 60 ahead / 466 behind, 26 changed paths. Compare B-series → F07: DIVERGED, 639 ahead / 60 behind, 123 changed paths. The 26 shared paths are listed in `G01_BRANCH_RECONCILIATION_ADDENDUM.md`.
- Targeted F07 checks are positive for HomeKeyService, C07 monotonic deadline/session/storage safeguards, manifest security declarations, dialog and upload regression tests. But source hashes differ on several owner/integration files, including manifest, RecentAppsActivity, WallpaperUploadServer, AvatarUploadServer, MainActivity, locale resources and ProfileManager; these require 3-way semantic reconciliation, not blind overwrite.
- G01 defect-resolution subgate PASS; G01 project-wide combine/reconciliation OPEN/BLOCKING. G02 is not ready. No feature PR or main branch merged; D-013 remains binding.
- Next work: freeze all required owner heads; reconcile B-series with F07 on an isolated candidate; run exact-head integrated CI and cross-group regression; only then build the G02 candidate APK. G03 physical acceptance remains separate.


### PR #57 integration conflict gate — 3 November 2026

- Draft PR #57 opened from `integration/project-f07-performance-stability` @ `976e068d48ecc1ede2811ac455372395d382ba76` into `integration/b-series` @ `9070894fa6d9ef8aec034f46dfe777e866709dfe`.
- GitHub reports `mergeable=false`; PR #57 is OPEN/DRAFT/UNMERGED. Compare view: 639 commits / 123 changed files. Do not merge or force-push around the conflict.
- The 26 shared B-series/F-staged-full paths require three-way semantic resolution. No conflict was resolved by wholesale side selection. Owner branches remain unchanged.
- G01 overall remains OPEN/BLOCKING for branch reconciliation; source-defect subgate remains PASS. G02 APK build is blocked pending clean combined candidate and exact-head integrated CI. `main` is untouched.


### G01 semantic reconciliation candidate — 3 November 2026

- Compared B-series and F07 from common ancestor `d7b76c4a22c9eadffb8171a616283f3ad647d488`; reviewed the 26 shared paths by owner contract and behavior. F07 was retained where it is the advanced implementation; B-only `GlassSurfaceFallbackTest.java` was preserved alongside F07's distinct fallback test.
- Found a real F06 localization defect: Daily Verse title/action/meaning/unavailable copy was reversed between `values-en` and `values-id`. Corrected on F06 and added explicit semantic value assertions. F06 exact-head run #41582796654 PASS (89 Python tests / 1 intentional README-count skip; Android unit tests and release validation build PASS; APK 1,569,397 bytes; no artifacts).
- Created isolated three-parent candidate `integration/g01-reconciled-candidate-20261103` @ `6ebd6284fcb21a74fed2a769868e91bc69ef7ae3`; parents are F07 `28e841a3475c144dd5ec85afb0976278274c2b5f`, B-series `9070894fa6d9ef8aec034f46dfe777e866709dfe`, corrected F06 `2c369a0a48c77bb3bf023c292892541306e535a3`.
- Candidate is a descendant of B-series (compare: ahead 644 / behind 0); Draft PR #58 is open and GitHub reports mergeable=true. PR #57 remains the historical unmergeable diagnostic gate.
- Candidate exact-head CI #41583215114 (push) and #41583248216 (pull_request) was still running at this checkpoint. Do not mark G01 complete until exact-head required jobs and cross-group review pass.
- No feature owner branch or `main` was rewritten/merged. No APK published/installed; no TV settings changed. G02 remains blocked; G03 physical Mi TV Stick acceptance remains separate.


### G01 merge completed — 3 November 2026

- PR #58 merged into `integration/b-series` at `f6f1f05b7a560381b8bb423eaa707a1d6d0e8832`. PR #58 is CLOSED/MERGED; PR #57 was superseded and closed after its F07 head became an ancestor of the accepted candidate.
- Final merge tree is identical to the exact-head CI-verified candidate tree `a016951e69d99603a2a90722420cfe118c93eed1`; no source drift was introduced by the merge.
- Exact merge-commit CI #41583505195: both required jobs SUCCESS; 99 Python tests all OK; Android launcher/installer unit tests PASS; release validation build SUCCESS; APK diagnostic 1,571,293 / 1,572,864 bytes; no workflow artifacts.
- Reviewed the only main-only commit: `6eaf787c56226606b1d2e650c59540c553756c62` adds manual workflow_dispatch APK publishing. Deliberately excluded from integration for release safety. `main` remains untouched.
- G01 source/host integration gate is PASS. G02 final tested APK candidate is ready to start from exact merge commit `f6f1f05b7a560381b8bb423eaa707a1d6d0e8832`. No APK published/installed; G03 physical Mi TV Stick acceptance not started.


| 2026-11-03 | G01 project-wide coverage re-audit | PR #59 → `integration/b-series` @ `562cde0751de93748eb4e0d4cdece4fa1672ac4d`; tested head `9f2da8f39a20c8e8cc43ac0ff7e42d7a82b34676`; CI #41586223809 | Re-audit found E05 location-scoped weather/AQI cache/API-display gap and missing E08 deletion-failure regression test. Reconciled E05 with F07 reduced-motion/low-memory and F06 localized high/low; restored E08 test. PR #59 merged; tested tree identical to merge tree. CI PASS: 109 Python tests / 1 intentional skip; Android unit tests/build PASS; APK 1,572,293 bytes / 571-byte headroom; no artifacts. | G01 PASS; G02 ready from exact merge commit; G03 physical TV acceptance pending. |


## G01 exact-merge-commit CI gate — 3 November 2026 (latest recheck)

- Rechecked the exact integration merge commit `562cde0751de93748eb4e0d4cdece4fa1672ac4d` after the project-wide coverage audit and semantic conflict reconciliation.
- The tested PR candidate `9f2da8f39a20c8e8cc43ac0ff7e42d7a82b34676` has two completed successful CI runs: #41586223809 (run #1177) and #41586510603 (run #1178). Each run contains exactly the two required jobs, `Lists, tools and docs` and `Launcher build and unit tests`; both jobs concluded `success`.
- Git compare confirms the merge commit is one commit ahead of the tested candidate with zero changed files. This proves identical file tree, but it does **not** prove CI ran on the exact merge commit SHA.
- A direct exact-SHA workflow-run lookup for `562cde0751de93748eb4e0d4cdece4fa1672ac4d` returned no workflow runs; combined commit status returned no status entries. Therefore exact-merge-SHA CI is **NOT VERIFIED** and must not be marked PASS.
- A verification-only Draft PR #60 was created with head SHA `562cde0751de93748eb4e0d4cdece4fa1672ac4d` and base at the tested candidate. GitHub reported zero changed files and did not produce a CI run. PR #60 was closed unmerged; no source changes, main merge, APK publication/installation, or device changes occurred. Verification branch `verification/g01-exact-merge-ci-20261103` remains as an audit-only ref.
- The repository integration workflow supports push to `integration/**` and pull-request events, with two required jobs. The GitHub connector available in this session exposes no workflow-dispatch action. Do not invoke the main-branch manual publish workflow: its workflow file includes artifact publication and is intentionally excluded by release-safety policy.
- **Disposition:** coverage re-audit and semantic conflict resolution are complete; combined-tree audit is source/test PASS; candidate CI is PASS; exact merge-commit CI remains OPEN/BLOCKING. G01 is therefore **NOT FINAL**. G02 remains blocked until both required jobs complete successfully on exact SHA `562cde0751de93748eb4e0d4cdece4fa1672ac4d` (or a newly designated candidate SHA after any change). Keep owner branches and `main` untouched; no APK publication/installation or TV setting changes.


## Exact-SHA CI execution update — 3 November 2026

- To obtain a push-triggered run on the exact integration merge SHA without changing source or `main`, created audit-only branch `integration/verify-g01-merge-ci-20261103` from tested parent `9f2da8f39a20c8e8cc43ac0ff7e42d7a82b34676` and fast-forwarded it to exact merge commit `562cde0751de93748eb4e0d4cdece4fa1672ac4d` (expected-SHA lease succeeded).
- GitHub Actions CI run #41589530683 / run #1180 is now attached to exact SHA `562cde0751de93748eb4e0d4cdece4fa1672ac4d`.
- Job `Lists, tools and docs`: SUCCESS.
- Job `Launcher build and unit tests`: IN PROGRESS at last check; `testReleaseUnitTest`, `assembleRelease`, APK diagnostics and size-budget gate have not yet been confirmed complete.
- Workflow artifact list was empty at the last check. No APK publication/installation, main merge, owner-branch change, or TV state change occurred.
- Integration/b-series still compares IDENTICAL to exact SHA `562cde...` (0 ahead / 0 behind / 0 changed files).
- **Current disposition:** G01 source coverage, conflict resolution and combined-tree audit are complete; exact-SHA CI is PARTIAL/OPEN until run #1180's second job succeeds. G01 remains NOT FINAL and G02 remains blocked. Do not freeze or promote the candidate until both required jobs in run #1180 conclude SUCCESS.


## Exact-commit CI final result — 3 November 2026

- Exact candidate SHA `562cde0751de93748eb4e0d4cdece4fa1672ac4d`, CI run #41589530683 / run #1180: **SUCCESS**.
- Both required jobs completed successfully: `Lists, tools and docs` — SUCCESS; `Launcher build and unit tests` — SUCCESS.
- Launcher job steps all succeeded: Android `testReleaseUnitTest`, `assembleRelease`, read-only APK diagnostics, and the 1.5 MiB size-budget enforcement.
- Exact APK size: 1,572,293 bytes; budget: 1,572,864 bytes; remaining headroom: 571 bytes. Size gate PASS, but margin is narrow and must be preserved; avoid unmeasured source/resource growth in G02.
- Workflow artifacts: none. APK was not published or installed.
- `integration/b-series` compares IDENTICAL to exact SHA `562cde...` (0 ahead, 0 behind, 0 changed files). `main` and owner branches remain untouched.
- **G01 disposition:** project-wide coverage audit PASS; semantic conflict reconciliation PASS; combined-tree audit PASS; exact-commit CI PASS. G01 HOST/SOURCE gate is now **PASS / READY FOR CHECKPOINT**. G02 may proceed only under the approved sequence, beginning with final APK build from exact SHA `562cde...`; no APK publication/installation or TV setting changes without the explicit G02/G03 approvals. Physical Mi TV Stick acceptance remains G03 and is not claimed by CI.


## G02 final APK build checkpoint — 3 November 2026

- Final source baseline remains exact audited integration tree: `562cde0751de93748eb4e0d4cdece4fa1672ac4d`. Build-only branch `build/g02-final-apk-artifact-20261103` changed only `.github/workflows/ci.yml`; no launcher source, owner branch, `integration/b-series`, or `main` changes.
- Workflow build hardening commit: `1960dfd7fa9dd39e41299ef091ce2bc2cf3dc4ca`. It retries the complete Gradle test/release build once with `--refresh-dependencies` only when recognized transient dependency/repository resolution errors occur; non-network/build/test failures remain fatal.
- CI #1184 (run ID `41590861573`) and CI #1185 (run ID `41590889219`), both on workflow commit `1960dfd7fa9dd39e41299ef091ce2bc2cf3dc4ca`: **both required jobs SUCCESS** in both runs. Unit tests and release build succeeded; APK diagnostics and strict size-budget gate succeeded; artifact upload succeeded.
- Both builds produced APKs of exactly 1,572,293 bytes (budget 1,572,864; headroom 571 bytes). ZIP/APK integrity check passed; manifest, DEX and resources entries present. Extracted APK SHA-256 from selected run #1185: `dc85fcd590eb2c52eb367d0bc9d4f67735cb94ef1cbf6f2b80b86f0de36de0dd`.
- Selected artifact: `glass-tv-home-final-562cde0`, artifact ID `13210715335`, from run #1185; GitHub artifact digest `sha256:669491bfc7c9f5279ecc6c30915e4a340aa492bee21531db203036caf8651c81`; retention expires 10 Nov 2026 06:37:50 UTC. Downloaded and extracted locally for integrity checks. A second successful run #1184 generated the same-size APK with different whole-file SHA; its ZIP entries have identical names and contents. Treat run #1185 artifact as the canonical handoff build.
- Local handoff file: `Glass-TV-Home-G02-Final.apk` (1,572,293 bytes; SHA-256 above). No TV/device installation or physical acceptance testing has been performed. No GitHub Release or public release was created.
- Build-only PR #61 was closed unmerged after artifact retrieval. It must not be merged into integration. `main` and all launcher source branches remain untouched.
- **G02 disposition: HOST BUILD + ARTIFACT PASS.** G03 physical Mi TV Stick acceptance remains pending and is a separate gate. APK signature verification/device compatibility have not been independently established by this host-side artifact check.


## 5 November 2026 — G03 device acceptance preflight

- Revalidated live `integration/b-series` SHA `562cde0751de93748eb4e0d4cdece4fa1672ac4d`, G02 run #1185 (both required jobs SUCCESS), PR #61 CLOSED/UNMERGED, and artifact ID `13210715335` availability/expiry.
- Re-downloaded artifact and independently matched ZIP SHA-256 to GitHub digest; extracted APK size and SHA-256 match the G02 handover record; APK archive integrity passed.
- No adb/apksigner/aapt/zipalign or connected TV available in host environment. No device/package/HOME/ADB mutation or install was performed.
- Added [G03_DEVICE_ACCEPTANCE_PREFLIGHT_2026-11-05.md](G03_DEVICE_ACCEPTANCE_PREFLIGHT_2026-11-05.md).
- Disposition: G03 preflight verified; physical acceptance NOT RUN / BLOCKED pending explicit readiness approval and device connection. Artifact expires 10 Nov 2026 06:37:50 UTC.


## 5 November 2026 — E02 GIF avatar + E06 Dock source audit

- Fetched live PR metadata and full diffs for #62 and #63; compared each feature branch with `integration/b-series`. E02 is 15 commits ahead / 326 behind with merge base `3ab9b55f3cfe03ad9841a119590a9183cf114e96`; E06 is 3 commits ahead / 297 behind with merge base `570b7c83efaec3ac515b78f1c6c119b777fc3b84`. These are dependency-diverged owner branches; no merge or rebase performed.
- Rechecked exact-head CI: E02 #1200 / run ID `41891880056` both required jobs SUCCESS; E06 #1198 / run ID `41890835096` both required jobs SUCCESS.
- E02 source audit: GIPHY client has bounded G-rated search, HTTPS host checks, redirect disabled, bounded JSON/GIF download, MIME/signature validation, private staging and attribution. Missing: thumbnail/animated preview rendering; active HTTP cancellation/disconnect; lifecycle commit gate; drawable stop lifecycle integration; broader DPAD, storage rollback, profile-switch, memory and attribution tests.
- E06 source audit: only `DockPreferences` and 31-line normalization/membership unit tests added. MainActivity remains one RecyclerView/GridLayoutManager with shared `COLUMNS` for grid columns, AppAdapter Dock threshold and DockDecoration. No independent Dock adapter/container, orientation renderers, settings UI, overflow/focus/reorder wiring or six-combination simulation.
- Created [E02_E06_GIF_AVATAR_DOCK_AUDIT_2026-11-05.md](E02_E06_GIF_AVATAR_DOCK_AUDIT_2026-11-05.md) with exact refs, verified checks, root architecture issue, implementation order and acceptance criteria.
- No source mutation, PR merge, integration/main update, APK publication/install, ADB or device evaluation performed in this audit pass. G03 remains deferred until user announces device-evaluation phase.
- **Disposition: both isolated-head CI gates PASS; both feature acceptance gates OPEN / BELUM FINAL; combined integration validation NOT RUN.**

## 5 November 2026 — E02 code remediation

- Replaced text-only GIF result tiles with bounded asynchronous still-frame thumbnails; added safe image URL/content checks, response/dimension bounds, bounded preview executor, holder recycling cancellation and stale-result guards.
- Guarded GIF avatar commit against dialog closure/interruption and active-profile switch; GIF drawable now stops scheduled frame callbacks when hidden/detached.
- CI #1201 failed at Java compilation because the rewritten dialog lacked `java.io.File` import. Root cause identified from job log and corrected. New exact head `dafcaf8a6dd8003300aca155d3c02395f1a28165`; CI #1203 / run `41895682457` queued/running, final result pending.
- No E06 implementation commit made in this pass. E06 remains an architectural refactor, not a preference-only patch. No PR merge/integration/main/device mutation.
