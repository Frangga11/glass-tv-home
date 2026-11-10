# AUTHORITATIVE CHECKPOINT — GTVH-HOME-LUXURY-GLASS-DPAD-CURSOR-001
**Checkpoint date:** 10 November 2026 (WIB)  
**Repository:** [Frangga11/glass-tv-home](https://github.com/Frangga11/glass-tv-home)  
**Source PR:** [#110 — Luxury Glass DPAD Cursor + Islamic Gold](https://github.com/Frangga11/glass-tv-home/pull/110)  
**Source branch:** `feature/home-luxury-realistic-glass-focus-001`  
**Exact source HEAD:** `537a8d12f0a72e4b29ffaa8e5b82b7aeb031c5cb`  
**Base branch / parent:** `fix/home-header-typography-focus-001` / PR #108 exact base HEAD `9b49f26180430dc63ef2ab8cf20bcba845e067ea`  
**Disposition:** OPEN / DRAFT / UNMERGED — BELUM FINAL.

## Owner clarification — authoritative color separation
The owner clarified that “focus” means the **DPAD cursor/highlight**, not merely the visual state of focused components.

1. **Global cursor:** all focus cursor states use the approved Frosted Silver-Cyan Glass treatment. Silver veil `#35DCE5ED`, polished silver border `#FFDCE5ED`; cyan remains only a restrained glass reflection/accent.
2. **Islamic content gold (strictly scoped):**
   - Ayat Pilihan: Qur'anic Arabic text only — gold-yellow-orange `#FFFFC45A` with black shadow `#CC000000`.
   - Jadwal Salat: marked prayer-time label/time only — same gold and black shadow.
3. The cursor remains a separate overlay from content colors. When it lands on the prayer schedule or Ayat Pilihan, the cursor is silver glass and the underlying Islamic content remains gold. Gold is not a cursor color and is not generalized to icons or other labels.

## Implemented source changes
- `Glider.java`, `GlassView.java`, and shared `TvFocusIndicator` token consumers: DPAD cursor border/halo now use the global silver focus tokens; navigation/focus graph and geometry are untouched.
- `TvUiTokens.java`: `FOCUS_BORDER = #FFDCE5ED`; `FOCUS_HALO = #35DCE5ED`. Existing `ACCENT_CYAN` remains electric cyan and separate from the focus-border semantic token.
- `values/colors.xml`: introduced `tv_ui_focus_border` as the XML mirror for the new silver focus-border token; retained `tv_ui_focus_cyan` as the independent electric-cyan accent.
- Focus selectors: header navigation, prayer pill, location/weather, app pill, dialog, media, input, recent-card and shared TV focus selectors now use the silver border token and restrained silver halo.
- `header_icon_tint.xml`: focused header icons use a soft silver-white tint; idle state remains neutral silver.
- `GlassDrawable.java` and `GlassRim.java`: refined realistic frosted-glass veil, persistent glass under cursor, stronger silver-cyan reflection and subtle window-light glints.
- `MainActivity.java`: background-only frosted surface behind the existing Islamic Shelf; no child geometry, content, RTL or spacing changes.
- Regression contracts updated for global cursor palette, XML/Java token synchronization, and the separate Islamic gold treatment.

## Protected Islamic palette
- `ISLAMIC_GOLD = 0xFFFFC45A` (#FFFFC45A).
- `ISLAMIC_TEXT_SHADOW = 0xCC000000` (#CC000000).
- Gold remains limited to the marked prayer label/time and Ayat Pilihan Arabic text; translation and unrelated metadata retain their existing colors.
- No prayer calculation, prayer data, Ayah content/cache, translation behavior or RTL changes.

## Strict exclusions
RE-SKIN, NOT REWIRE. Preserve IDs, listeners, handlers, navigation order, DPAD graph, data providers, persistence, lifecycle, approved geometry, Arabic RTL, Weather/AQI logic, Weather V4 dashboard, and accepted Wi-Fi popup. No changes to wallpaper, Dock/Grid, Hero Shelf, WeatherActivity, or unrelated modules. No merge, ADB, TV installation, default-launcher change, or release publication.

## Validation ledger
| Gate | Status | Evidence |
|---|---|---|
| Source commits | COMPLETE | Exact HEAD `537a8d12f0a72e4b29ffaa8e5b82b7aeb031c5cb` |
| Scope / diff review | PENDING | Re-fetch final PR diff and changed-file list at exact HEAD |
| Exact-head CI | **PASS** | Run `42739183589` on exact HEAD `537a8d12f0a72e4b29ffaa8e5b82b7aeb031c5cb`; both required jobs SUCCESS |
| Android unit tests + release build | **PASS** | `testReleaseUnitTest assembleRelease` completed successfully |
| APK size diagnostic | PASS WITH WARNING | APK `1,630,477 bytes` (1,592 KB), about 55 KB above historical 1.5 MiB reference; non-blocking |
| QA APK artifact upload | **PASS** | Artifact ID `13727693796`, name `glass-tv-home-dock-visual-parity-dc542b546a8c5fd8cbe5a7c9d529d46b65e39f99`, archive `1,431,188 bytes`, SHA-256 `d639b7dbcef1d852026c5be749f25c96bff8d96fd83b1d0f7e0e31ab9cb359f5`; [download from Actions run](https://github.com/Frangga11/glass-tv-home/actions/runs/42739183589) |
| 1920×1080 rendered visual review | PENDING | Must inspect rendered output |
| Live DPAD/focus acceptance | PENDING | Must inspect on Mi TV Stick |
| Physical device acceptance | PENDING | No ADB/install authorized |
| Merge/release | NOT AUTHORIZED | PR #110 remains Draft/unmerged |

## Next action
The artifact allowlist correction is verified: run `42739183589` completed SUCCESS on the exact current source HEAD, and the Home layout QA APK was uploaded successfully (artifact ID `13727693796`). The release APK size remains 1,630,477 bytes (1,592 KB), above the historical 1.5 MiB reference; treat this as a non-blocking size warning, not an optimization pass. Next, retrieve the exact artifact, verify ZIP integrity and extracted APK size/hash, then perform the authorized 1920×1080 visual review. Mi TV Stick installation/DPAD acceptance remains a separate owner-authorized gate. If it fails, inspect evidence and correct before acceptance. Keep PR #110 OPEN / DRAFT / UNMERGED; no merge, ADB, device install, default-launcher change, or release publication.

**Single restart instruction:** “Lanjutkan dari GTVH-CHECKPOINT-LATEST.md — GTVH-HOME-LUXURY-GLASS-DPAD-CURSOR-001.”

---

# GTVH-HOME-HEADER-TYPOGRAPHY-FOCUS-001 — SINGLE AUTHORITATIVE CHECKPOINT
**Checkpoint date:** 10 November 2026 (WIB)  
**Repository:** [Frangga11/glass-tv-home](https://github.com/Frangga11/glass-tv-home)  
**Canonical restart file:** `docs/project-management/GTVH-CHECKPOINT-LATEST.md` on `docs/project-master-inventory`  
**Restart instruction:** “Lanjutkan Glass TV Home dari GTVH-CHECKPOINT-LATEST.md”.

## Verified current state
- **Workstream:** `GTVH-HOME-HEADER-TYPOGRAPHY-FOCUS-001`.
- **Source PR:** [#108](https://github.com/Frangga11/glass-tv-home/pull/108) — **OPEN / DRAFT / UNMERGED**.
- **Source branch / exact PR HEAD:** `fix/home-header-typography-focus-001` / `9b49f26180430dc63ef2ab8cf20bcba845e067ea`.
- **Protected PR base:** `fix/islamic-gold-orange-prayer-ayah-001`, exact base SHA `1ea32bb2a49be6fb9aaee586f2eab2dae417d052`.
- **Exact-head CI:** [CI #1858 / run 42712168556](https://github.com/Frangga11/glass-tv-home/actions/runs/42712168556) — **SUCCESS** on the exact PR HEAD above. Both jobs passed: “Lists, tools and docs” and “Launcher build and unit tests”.
- **Build evidence:** “Unit tests and release build (validation only)” PASS; “APK size diagnostics (read-only)” PASS; “Report release APK size” PASS; “Publish Home layout test APK” PASS.
- **APK artifact:** artifact ID `13715838479`, artifact name `glass-tv-home-dock-visual-parity-28939680bb7931f7ee66262fa4fafef9ac37213a`, uploaded by run #1858. Archive contains `app-release.apk`.
- **APK verified locally:** 1,630,085 bytes; SHA-256 `a095485fb9eef71e88b665c4a97b46e46917ac064e1a7158de4b4ca15c167f38`. ZIP listing and extracted APK size were checked. This is the exact PR #108 artifact, not an older build.
- Artifact expiry reported by GitHub: 16 November 2026 22:30 UTC (16 Nov 2026 05:30 WIB).

## Implemented scope
1. Header text targets standardized to **14sp, Inter SemiBold (wght 600)**: AM/PM, date (including Shamsi runtime override), location/altitude, weather/AQI, Beranda, Profil, Pengaturan, and Wi-Fi label.
2. Header capsules remain **30dp** high.
3. Navigation focus selector now renders on the tab background, above the shared capsule and below icon/text, so focus is visible.
4. Home and Profile icon-label spacing tightened to **1dp**; Settings remains **4dp** unchanged.
5. `btnInputs` excluded; microphone remains icon-only.
6. Regression contract and legacy contract expectations updated to enforce the intended typography, focus, and spacing behavior.

## Explicitly preserved / not changed
- **Wi-Fi popup/dialog implementation, layout, dimensions, DPAD, scan, permission, and connection behavior were not modified.**
- No weather/AQI provider or data logic, location source, prayer data, wallpaper, Home dock/grid, navigation order, or unrelated feature logic changed.
- No PR merge, retarget, reset, force-push, or device-side installation was performed.

## Quality gate ledger
| Gate | Status | Evidence / boundary |
|---|---|---|
| Exact PR-head CI | **PASS** | CI #1858 / run 42712168556 on `9b49f261` |
| Unit tests + Release build | **PASS** | Android validation step succeeded |
| APK artifact upload and retrieval | **PASS** | Artifact 13715838479; APK extracted and size/hash verified |
| Visual review at 1920×1080 | **PENDING** | Requires owner review on Mi TV Stick |
| Live DPAD/focus acceptance | **PENDING** | Requires physical remote/device test |
| Full device acceptance | **PENDING** | Not inferred from CI; user feedback required |
| Merge/release | **NOT AUTHORIZED** | PR #108 remains Draft and unmerged |

## Next action / continuation boundary
Install and inspect this APK on the Mi TV Stick at 1920×1080. Review 14sp typography, capsule proportions, Home/Profile/Settings spacing, and visible DPAD focus. Check that the Wi-Fi popup remains exactly as previously approved. Capture photos/video and report findings. Only then conduct the next evidence-based audit/fix cycle. Do not mark visual/device acceptance PASS before evidence arrives; do not merge PR #108 without explicit approval.

**This file is the only restart authority.** Supporting project records below are synchronized to this same checkpoint; historical entries remain historical and must not override this status.

---

## AUTHORITATIVE CHECKPOINT UPDATE — 9 NOVEMBER 2026 — GTVH GLOBAL FROSTED SILVER GLASS

- PR #104 remains OPEN / DRAFT / UNMERGED: https://github.com/Frangga11/glass-tv-home/pull/104
- Exact current PR HEAD: `4575b09b262ad664f6766ce286d5e5ed90e92780`; the newest commit adds audit documentation only.
- Latest source-code HEAD: `27eca44f42b17fc9b3f7cf711b5bbf488ef4e12a`; protected PR #103 base SHA: `a3d697c793e23a59f9f9953e2074d83daa210cf9`.
- G4 correction: `DockBreathingBorderView.java` now derives glow and lower rim-gradient RGB from `TvUiTokens.ACCENT_CYAN` (#00E5FF), replacing legacy #38BDF8. Animation timing, alpha, stroke widths, geometry, lifecycle and DPAD behavior unchanged.
- Regression guard: `test_dock_breathing_rim_uses_shared_electric_cyan_token`.
- Exact source-head CI #1760 / run 42608935919: SUCCESS on `27eca44f42b17fc9b3f7cf711b5bbf488ef4e12a`; Python unittest discovery and Android `testReleaseUnitTest assembleRelease` jobs both passed. Artifact ID 13670145336 is the workflow Home layout visual-parity artifact; exact APK byte size for this head was not captured.
- Current docs-only PR-head CI #1761 / run 42609316862: IN PROGRESS on `4575b09b262ad664f6766ce286d5e5ed90e92780`; do not claim exact-current-head CI until it completes.
- G1 inventory cross-check: 141 app drawable/values resources enumerated; 36 changed, 105 unchanged. Unchanged resources require semantic classification; unchanged does not automatically mean defect. Full stale-resource sweep remains PARTIAL.
- Remaining gates: full G1/G4 source-resource sweep; matched 1920×1080 visual review on bright/dark/mixed wallpapers; full runtime functional regression; live DPAD focus path; GPU/performance profiling; owner-authorized physical Mi TV Stick acceptance.
- Preserve RE-SKIN NOT REWIRE, PR #103 ancestry, IDs/listeners/handlers/data/navigation/focus graph/geometry/wallpaper/Arabic RTL. No merge, retarget, reset, force-push, ADB/install or default-launcher change.
- Overall status: source-code correction and CI #1760 PASS; exact current docs-head CI pending; runtime acceptance pending; **BELUM FINAL**.

**Next action:** wait for CI #1761 on current PR HEAD. Then continue remaining G1/G4 stale-resource classification and scoped fixes; do not advance to device acceptance until audit/implementation gates and owner authorization are satisfied. This is the single authoritative restart point.

---

## AUTHORITATIVE CONTINUATION SYNC — GTVH-GLOBAL-FROSTED-SILVER-GLASS-001 — 9 NOVEMBER 2026

- Active PR #104: OPEN / DRAFT / UNMERGED — https://github.com/Frangga11/glass-tv-home/pull/104
- Source branch: `feature/global-frosted-silver-glass-001`; current exact PR HEAD: `27eca44f42b17fc9b3f7cf711b5bbf488ef4e12a`.
- Protected parent PR #103 exact base SHA: `a3d697c793e23a59f9f9953e2074d83daa210cf9`; preserve all parent ancestry and cumulative Arabic RTL, prayer, Home and spacing fixes.
- New G4 corrective finding: `DockBreathingBorderView.java` still used legacy cyan RGB (56,189,248 / #38BDF8) for the dock breathing glow and lower rim gradient. Both now derive RGB channels from `TvUiTokens.ACCENT_CYAN` (#00E5FF). Animation timing, alpha envelope, stroke widths, geometry, lifecycle and focus/navigation behavior were not changed.
- Added regression contract `test_dock_breathing_rim_uses_shared_electric_cyan_token` to prevent legacy RGB drift and require the shared semantic token.
- Commits: `0f90e90cb0d99ccd9c34ec1e36f5976af4fb9120` (source correction); `27eca44f42b17fc9b3f7cf711b5bbf488ef4e12a` (regression contract).
- Exact-head CI #1760 / run 42608935919 is **IN PROGRESS** on HEAD `27eca44f42b17fc9b3f7cf711b5bbf488ef4e12a`. Lists/tools/docs job PASS; Android launcher/unit-test/release-build job still running. Do not claim final CI PASS or APK diagnostic for this head until the run completes.
- Prior exact-head CI #1758 on `b737b617aa53fbfc112ccdfe8a7cd4959043d3ec` remains PASS (235 Python passed, 1 skipped; Android unit tests/release build successful). That evidence does not validate the new dock color correction.
- G1 resource cross-check: 141 drawable/values files in scope; 36 changed and 105 unchanged. Unchanged assets/localization/semantic resources are not automatically defects. The full stale-resource sweep remains PARTIAL; the dock breathing cyan was one evidenced gap and is now corrected.
- Runtime/visual gates remain NOT RUN: rendered 1920×1080 bright/dark/mixed wallpaper review, full end-to-end functional regression, live DPAD path, GPU/performance profiling and owner-authorized Mi TV Stick acceptance.
- Guardrails: RE-SKIN, NOT REWIRE. Preserve IDs, listeners, handlers, data, persistence, navigation, focus graph, geometry, wallpaper, Arabic shaping/RTL and PR #103 ancestry. No merge, rebase, reset, force-push, retarget, ADB/install or default-launcher change.
- Overall disposition: source correction committed; exact-head CI pending; G1/G4 partial; overall **BELUM FINAL**. Keep PR #104 OPEN / DRAFT / UNMERGED.

**Next action:** wait for exact-head CI #1760. If it passes, record its test/build/APK evidence, update the PR audit report and all six canonical documents in one synchronization pass. Then continue the remaining G1/G4 stale-resource sweep. The checkpoint remains the only restart authority.

---

## STATIC DIFF CROSS-CHECK — 9 NOVEMBER 2026, 18:26 WIB

- Re-read the complete PR #104 unified diff: **65 changed files**.
- Static interaction-preservation scan: no added/removed `setOnClickListener`, `setOnKeyListener`, `requestFocus`, `setFocusable` or `setClickable` calls in the diff.
- Fourteen prior `setOnFocusChangeListener` blocks were removed. Review of their hunks shows they handled only focus surface/text styling and visual scaling; their presentation is now represented by state-list backgrounds or the shared geometry-neutral text-focus helper. The existing component action/key handlers remain in place.
- Focus-scale cleanup removes prior scale animations; one app-label vertical translation is neutralized to `translationY(0f)`. No horizontal translation changes were found.
- This is a **static diff/scope cross-check only**, not runtime functional or DPAD acceptance. Do not promote the functional/DPAD gates to PASS.
- No application source was changed during this cross-check.

**Current source candidate remains:** PR #104 OPEN / DRAFT / UNMERGED, branch `feature/global-frosted-silver-glass-001`, exact HEAD `b737b617aa53fbfc112ccdfe8a7cd4959043d3ec`; base PR #103 exact SHA `a3d697c793e23a59f9f9953e2074d83daa210cf9`. Exact-head CI #1758 / run 42605960427 PASS (235 Python PASS, 1 skipped; Android unit tests and release build PASS). Overall BELUM FINAL; visual/runtime/performance/device gates remain pending.

---

<!-- CURRENT AUTHORITATIVE CHECKPOINT — RECOVERY SYNC 18:19 WIB -->
## AUTHORITATIVE RECOVERY SYNC — GTVH-GLOBAL-FROSTED-SILVER-GLASS-001 — 9 NOVEMBER 2026, 18:19 WIB

- **Active source PR:** [#104](https://github.com/Frangga11/glass-tv-home/pull/104), OPEN / DRAFT / UNMERGED.
- **Source branch / exact PR HEAD:** `feature/global-frosted-silver-glass-001` / `b737b617aa53fbfc112ccdfe8a7cd4959043d3ec`.
- **Protected base:** PR #103, `fix/home-wallpaper-prayer-spacing-arabic-rtl-102`, exact SHA `a3d697c793e23a59f9f9953e2074d83daa210cf9`. Preserve ancestry and all inherited RTL, prayer, Arabic alignment and Home corrections.
- **Force-close recovery:** corrected `TvFocusIndicator.createOvalStateBackground()` parameter type; updated the obsolete Home weather/header focus contract to the shared listener/no-resize behavior; aligned wallpaper interval selection with shared state semantics; removed no-op WeatherActivity scale animations; completed shared focus styling for dynamic settings/profile surfaces; added regression coverage. No test was disabled or weakened.
- **Exact PR-head CI:** [#1758 / run 42605960427](https://github.com/Frangga11/glass-tv-home/actions/runs/42605960427) SUCCESS; both required jobs succeeded. Python regression **235 passed, 1 skipped**; Android unit tests and release build **BUILD SUCCESSFUL**.
- **APK diagnostic:** 1,628,165 bytes (1,590 KB), non-blocking size diagnostic. Artifact `13668172465` is a Home layout test artifact, **not** the release APK.
- **Implemented:** neutral cool-silver Java/XML tokens; Electric Cyan DPAD outline; distinct selected/active state; geometry-stable focus; shared Home/dock/dialog/location/weather/prayer/media/Recent Apps/settings/profile/installer reskin; Arabic RTL/data/navigation preserved.
- **Still NOT PASS / pending:** full remaining source/resource stale-style sweep; rendered 1920×1080 comparison on bright/dark/mixed wallpapers; full runtime functional regression; live DPAD path; GPU/performance profiling; owner-authorized Mi TV Stick acceptance.
- **Guardrails:** RE-SKIN, NOT REWIRE. Preserve IDs, listeners, handlers, data, persistence, navigation, focus graph, geometry, wallpaper and Arabic RTL. No merge, rebase, reset, force-push, retarget, ADB/install, default-launcher change or device test until required audit gates and explicit owner authorization.
- **Disposition:** automated source/build CI PASS; overall workstream **BELUM FINAL**. Keep PR #104 OPEN / DRAFT / UNMERGED.

**Next action:** continue G1/G4 source-resource audit and static regression review without touching the protected base. Then complete rendered visual review; only after G1/G4 are complete and owner authorizes, proceed to Mi TV Stick acceptance. This checkpoint remains the single restart authority.

---

<!-- CURRENT AUTHORITATIVE SYNC — 9 NOVEMBER 2026 — GLOBAL FROSTED SILVER GLASS. Supersedes older current-status blocks below; historical records retained. -->
## CURRENT AUTHORITATIVE PROJECT SYNC — GTVH-GLOBAL-FROSTED-SILVER-GLASS-001

**Single source of truth:** this checkpoint on `docs/project-master-inventory`. The five supporting records are synchronized in this same pass.

**Active PR:** [#104](https://github.com/Frangga11/glass-tv-home/pull/104) OPEN / DRAFT / UNMERGED. Branch `feature/global-frosted-silver-glass-001`; current exact HEAD `e6712777ca3ecd5c3e1714c5e2725aa9c5cddc3e`; base PR #103 branch `fix/home-wallpaper-prayer-spacing-arabic-rtl-102`, exact base SHA `a3d697c793e23a59f9f9953e2074d83daa210cf9`; 69 commits, 50 changed files. The only commit after CI-validated source HEAD `03408e7585e1cacd3fe32727502bca61752c5438` updates the audit markdown; application source is unchanged.

**Exact-head source CI:** [run #1703](https://github.com/Frangga11/glass-tv-home/actions/runs/42600150812) SUCCESS on source HEAD `03408e7`. Python regression: 223 passed, 1 skipped. Android unit tests and release build: BUILD SUCCESSFUL.

**APK artifact:** 1,627,069 bytes (1,588 KB), SHA256 `e146f9e80c333acbb72c34bb64f96b2be385583e60400239ab04b5c6f5474671`. Artifact ID `13664798205`; artifact ZIP SHA256 `34da6ef190bde1aa7a49c3bc7eb564e969d0107c6e8f4e514b957486784f3a2e`. Size delta vs parent PR #103's 1,626,637 bytes: +432 bytes (~0.03%). APK is archived for later acceptance; **do not install/test on TV until G1 audit and G4 implementation are complete and owner authorizes it.**

**Protected parent:** PR #103 remains OPEN / DRAFT / UNMERGED at HEAD `a3d697c793e23a59f9f9953e2074d83daa210cf9`, base PR #101 HEAD `08b521cadb8c913b49fce42acb4e536e3eb7e9ea`. Preserve its RTL, Ayah spacing, prayer and cumulative Home corrections.

**Progress:** G1 inventory counts and major surface map established; full stale-resource/source sweep remains in progress. G2 shared silver/cyan tokens implemented. G3 initial Home/dock, popup and focus prototype implementation exists in source; rendered visual validation pending. G4 global implementation is partial. G5 integration, G6 full QA and G7 final acceptance remain open.

**Baseline inventory at PR #103:** 17 app layout XML, 112 app drawable resources, 19 app values XML, 100 app Java source files, 59 app Java unit-test files, 35 Python regression-test files. Counts are files, not unique glass components.

**Implemented initial pass:** synchronized Java/XML palette; neutral silver/cyan GlassRim; Electric Cyan outline in Glider and GlassView; focus scale neutralized while preserving 1.07 AppAdapter layout reservation; selected state separated from focus; shared Home, dock, popup, location, weather, prayer, media, recent-app, installer and dynamic settings surfaces; regression contracts; CI artifact allowlist. Detailed evidence: `GTVH-GLOBAL-FROSTED-SILVER-GLASS-001-AUDIT.md` on PR #104.

**Quality gates:** Source Audit PARTIAL / IN PROGRESS; Design System IN PROGRESS; Build & CI PASS on exact source HEAD #1703; Functional QA NOT RUN; Visual QA NOT RUN; DPAD runtime QA NOT RUN; Performance runtime QA NOT RUN; Device Acceptance PENDING. No unverified PASS claims.

**Next:** complete remaining source/resource inventory; validate rendered 1920×1080 prototypes on bright, dark and mixed wallpapers; run functional, DPAD, typography and performance regression; only then obtain physical Mi TV Stick acceptance. Keep PR #104 Draft and unmerged until all gates and explicit owner approval.

---

<!-- CURRENT AUTHORITATIVE SYNC — 9 NOVEMBER 2026 — GTVH GLOBAL FROSTED SILVER GLASS. Supersedes every older current-status block below; history retained. -->
## CURRENT AUTHORITATIVE PROJECT SYNC — GTVH-GLOBAL-FROSTED-SILVER-GLASS-001

**Single source of truth:** this checkpoint on `docs/project-master-inventory`. All five supporting project records are synchronized in the same documentation pass.

**Active PR:** [#104](https://github.com/Frangga11/glass-tv-home/pull/104) — OPEN / DRAFT / UNMERGED. Branch `feature/global-frosted-silver-glass-001`; current exact HEAD `03408e7585e1cacd3fe32727502bca61752c5438`; base PR #103 branch `fix/home-wallpaper-prayer-spacing-arabic-rtl-102`, exact base SHA `a3d697c793e23a59f9f9953e2074d83daa210cf9`; 68 commits, 50 changed files. The latest source-code validation head is `7535a311639c07ab77abd004664510fca216b00b`; commit `03408e7` adds only the audit/design evidence document, not application source.

**CI:** [run #1702](https://github.com/Frangga11/glass-tv-home/actions/runs/42599811383) SUCCESS on source HEAD `7535a311`. Python regression: 223 passed, 1 skipped. Android unit tests and release build: BUILD SUCCESSFUL. CI #1703 / [42600150812](https://github.com/Frangga11/glass-tv-home/actions/runs/42600150812) is IN PROGRESS on docs-only HEAD `03408e7`; do not claim exact-current-head CI until it completes.

**APK from successful run #1702:** 1,627,069 bytes (1,588 KB), SHA256 `e5e5dfbcff4a2944c4bc8d3be38deb27743b1b358b542bf82a6a0effab530054`. Artifact ID `13665501684`; artifact ZIP SHA256 `a570dc0585502c75cbf0862e3c8546263eccb6f5e3f6886cee7c8761ac6a5684`. Size delta vs parent PR #103's 1,626,637 bytes: +432 bytes (~0.03%). APK is available for owner testing; no device acceptance has occurred.

**Protected parent:** PR #103 remains OPEN / DRAFT / UNMERGED at HEAD `a3d697c793e23a59f9f9953e2074d83daa210cf9`, base PR #101 HEAD `08b521cadb8c913b49fce42acb4e536e3eb7e9ea`. Preserve its RTL, Ayah spacing, prayer and cumulative Home corrections.

**Progress:** G1 inventory counts and major surface map established; full stale-resource/source sweep remains in progress. G2 shared silver/cyan tokens implemented. G3 initial Home/dock, popup and focus prototype implementation is in source; rendered visual validation pending. G4 global implementation is partial. G5 integration, G6 full QA and G7 final acceptance remain open.

**Baseline inventory at PR #103:** 17 app layout XML, 112 app drawable resources, 19 app values XML, 100 app Java source files, 59 app Java unit-test files, 35 Python regression-test files. Counts are files, not unique glass components.

**Implemented initial pass:** synchronized Java/XML palette; neutral silver/cyan GlassRim; Electric Cyan outline in Glider and GlassView; focus scale neutralized while preserving the 1.07 AppAdapter layout reservation; selected state separated from focus; shared Home, dock, popup, location, weather, prayer, media, recent-app, installer and dynamic settings surfaces; regression contracts; CI artifact allowlist. Detailed evidence: `GTVH-GLOBAL-FROSTED-SILVER-GLASS-001-AUDIT.md` on PR #104.

**Quality gates:** Source Audit PARTIAL / IN PROGRESS; Design System IN PROGRESS; Build & CI PASS on source HEAD #1702, docs-only HEAD #1703 IN PROGRESS; Functional QA NOT RUN; Visual QA NOT RUN; DPAD runtime QA NOT RUN; Performance runtime QA NOT RUN; Device Acceptance PENDING. No unverified PASS claims.

**Next:** finish CI #1703; inspect the complete remaining source/resource inventory; perform matched 1920×1080 visual review on bright, dark and mixed wallpapers; run functional, DPAD, typography and performance regression; obtain physical Mi TV Stick acceptance. Keep PR #104 Draft and unmerged until all gates and explicit owner approval.

---

<!-- CURRENT AUTHORITATIVE SYNC — 9 NOVEMBER 2026, 17:01 WIB — GLOBAL FROSTED SILVER GLASS UPDATE. This block supersedes every earlier current-status block below. Historical records remain intact. -->
## CURRENT AUTHORITATIVE PROJECT SYNC — GTVH-GLOBAL-FROSTED-SILVER-GLASS-001

**Single source of truth:** this file on `docs/project-master-inventory`. Supporting docs below are synchronized detail; they do not create alternate restart points.

**Active workstream:** `GTVH-GLOBAL-FROSTED-SILVER-GLASS-001` — global Frosted Silver Glass + Electric Cyan DPAD Focus, RE-SKIN NOT REWIRE.

**Active PR:** [#104](https://github.com/Frangga11/glass-tv-home/pull/104), OPEN / DRAFT / UNMERGED. Branch `feature/global-frosted-silver-glass-001`; exact HEAD `b34d785dced9b5e47e65adf9865b1f4deada532d`; base PR #103 branch `fix/home-wallpaper-prayer-spacing-arabic-rtl-102`, exact base SHA `a3d697c793e23a59f9f9953e2074d83daa210cf9`; 51 commits, 0 behind, 40 changed files. No merge, retarget, flatten, reset, force-push, install, ADB or default-launcher change.

**Protected parent:** PR [#103](https://github.com/Frangga11/glass-tv-home/pull/103) remains OPEN / DRAFT / UNMERGED at HEAD `a3d697c793e23a59f9f9953e2074d83daa210cf9`; base PR #101 HEAD `08b521cadb8c913b49fce42acb4e536e3eb7e9ea`. Its exact-head CI #1689 passed; APK 1,626,637 bytes. Visual and physical Mi TV Stick acceptance remain pending. Preserve ancestry and all Arabic RTL, Ayah spacing, prayer and cumulative Home work.

**Latest CI for PR #104:** run #1691 / [42599100302](https://github.com/Frangga11/glass-tv-home/actions/runs/42599100302) is IN PROGRESS on exact HEAD `b34d785dced9b5e47e65adf9865b1f4deada532d`. Previous-head run #1690 / [42599046834](https://github.com/Frangga11/glass-tv-home/actions/runs/42599046834) is also IN PROGRESS at `377ccf4dcbbfaeb78d2c7bba42f42f64c8bde02c`. No CI PASS or APK size is claimed for PR #104 yet. PR #104 is included in the CI APK artifact allowlist.

**Progress:** G1 inventory counts and major surface map established; full stale-resource/source sweep continues. G2 shared silver/cyan tokens and material foundation implemented. G3 initial Home/dock, popup and focus prototype implementation exists in source; rendered visual validation is pending. G4 global implementation is partial. G5 integration, G6 full QA and G7 final acceptance are not complete.

**Baseline inventory at PR #103:** 17 app layout XML, 112 app drawable resources, 19 app values XML, 100 app Java source files, 59 app Java unit-test files and 35 Python regression-test files. Counts are files, not unique glass components.

**Implemented initial pass:** shared Java/XML palette; neutral silver/cyan GlassRim; Electric Cyan outline in Glider and GlassView; focus scale neutralized while preserving AppAdapter's 1.07 layout reservation; selected state separated from focus; shared Home, dock, popup, location, weather, prayer, media, recent-app and installer glass resources; LocationActivity and AzanActivity material; new regression contracts; CI artifact allowlist. Audit/design record: `docs/project-management/GTVH-GLOBAL-FROSTED-SILVER-GLASS-001-AUDIT.md` on the PR #104 branch.

**Quality gates:** Source Audit PARTIAL / IN PROGRESS; Design System IN PROGRESS; Build & CI IN PROGRESS; Functional QA NOT RUN; Visual QA NOT RUN; DPAD runtime QA NOT RUN; Performance runtime QA NOT RUN; Device Acceptance PENDING. No unverified PASS claims.

**Next action:** inspect exact-head CI #1691; fix any failures without widening scope; complete the remaining source/resource inventory; validate prototype screenshots at 1920×1080 on bright, dark and mixed wallpapers; then run functional, DPAD, typography, performance and physical Mi TV Stick acceptance. Keep PR #104 Draft and unmerged until all gates and explicit owner approval.

---

<!-- CURRENT AUTHORITATIVE SYNC — 9 NOVEMBER 2026. This block supersedes older status snapshots below where they conflict. Historical entries are retained as history, not current instructions. -->
## CURRENT AUTHORITATIVE PROJECT SYNC — 9 NOVEMBER 2026

**Single source of truth:** `docs/project-management/GTVH-CHECKPOINT-LATEST.md` on `docs/project-master-inventory`. Every session starts there. This file alone governs current status, active workstream, dependency order, decisions, quality gates, and next action. Supporting documents provide detailed evidence/history and must not create competing restart points.

**Current source candidate:** PR [#103](https://github.com/Frangga11/glass-tv-home/pull/103), `GTVH-HOME-WALLPAPER-PRAYER-ZERO-GAP-ARABIC-RTL-FIX-103`; OPEN / DRAFT / UNMERGED. Branch `fix/home-wallpaper-prayer-spacing-arabic-rtl-102`; exact HEAD `a3d697c793e23a59f9f9953e2074d83daa210cf9`; base PR #101 at `08b521cadb8c913b49fce42acb4e536e3eb7e9ea`; 7 commits ahead / 0 behind; 5 changed files (2 production, 3 regression tests).

**Latest verified automation:** exact-head CI #1689 / run [42566072365](https://github.com/Frangga11/glass-tv-home/actions/runs/42566072365) SUCCESS; both required jobs passed; Gradle release build successful. APK size 1,626,637 bytes; 53,773 bytes above historical 1.5 MiB reference, diagnostic/non-blocking. No PR artifact ZIP was published for this Draft run.

**Remaining gates:** genuine rendered 1920×1080 visual review and physical Mi TV Stick acceptance are NOT RUN / PENDING. No ADB, installation, launcher-setting change, or merge. Overall PR #103 is NOT FINAL. Do not claim visual/device acceptance.

**Cumulative base:** PR #101 branch `integration/home-islamic-prayer-spacing-cumulative-101`, base HEAD `08b521cadb8c913b49fce42acb4e536e3eb7e9ea`, remains unmerged. PR #103 is a corrective child candidate; do not flatten, retarget, merge, or alter its ancestry without an explicit reconciliation and owner approval.

**Newly approved future workstream:** `GTVH-GLOBAL-FROSTED-SILVER-GLASS-001` — global visual reskin using Frosted Silver Glass + Electric Cyan DPAD Focus. Scope includes Home Screen, dock, Beranda–Profil–Pengaturan navigation, Wi-Fi/microphone, location/altitude, weather/AQI, popups/dialogs, settings/profile, prayer schedule, selected Ayah, Arabic and translation, and all other audited glass surfaces. Design/prompt approved by owner; implementation has NOT started; no branch, PR, source modification, prototype, or QA result is claimed.

**Global execution constraints:** RE-SKIN, NOT REWIRE. Preserve working IDs, listeners, events, data logic, navigation, DPAD behavior, wallpaper logic, component geometry, and prior approved work. Only change geometry where an audited, evidenced requirement is approved. Prioritize function, readability, stability, and device performance over decorative effects. Do not merge or install without the required evidence and explicit owner approval.

**Document roles:** CHECKPOINT = sole status/restart authority; MASTER_PROJECT = scope/architecture/roadmap; INVENTORY = files/components/branches/PR/dependencies; DECISION_LOG = approved decisions and constraints; AUDIT_REGISTER = evidence, findings, quality gates; HANDOVER = exact resume instructions. Update all six in the same documentation synchronization pass. Keep chronology; mark superseded statements rather than deleting history.

---

# GTVH-CHECKPOINT-LATEST


## Latest corrective update — location/weather capsule spacing 4dp — 9 November 2026

- Owner requested reducing the vertical gap between the Home location/altitude capsule and weather/AQI capsule from 8dp to **4dp**.
- Implemented on the existing cumulative Home candidate PR #101, branch `integration/home-islamic-prayer-spacing-cumulative-101`; latest source HEAD: `08b521cadb8c913b49fce42acb4e536e3eb7e9ea`.
- `launcher/app/src/main/res/layout/activity_main.xml`: `weatherAirQuality` remains positioned below `locationPill`; only `android:layout_marginTop` changed from `8dp` to `4dp`. Both capsules remain 30dp high; widths, horizontal alignment, content, data providers, click/focus/DPAD behavior and other layout geometry are untouched.
- `tests/test_home_spacing_capsule_height_refinement_contract.py`: added regression contract verifying both capsules remain 30dp, weather remains below location, and exact vertical margin is 4dp.
- Two sequential commits pushed to PR #101 branch: layout correction `1dde90ae1059571bf174fcab757504dfe417ece0`; regression test `08b521cadb8c913b49fce42acb4e536e3eb7e9ea`.
- Exact source verification after push confirms `layout_marginTop="4dp"` and the new regression assertion. Workflow-run lookup for the final commit has not yet returned a PR-triggered run; CI result is therefore **PENDING**, not PASS. Rendered 1920×1080 visual review and physical Mi TV Stick acceptance remain pending. No APK installation, ADB, merge or PR state transition performed.
- Next gate: wait for exact-head PR #101 CI; inspect any failure; then perform rendered visual review. Keep PR #101 OPEN / DRAFT / UNMERGED.
**Status:** CANONICAL / SINGLE ENTRY POINT  
**Checkpoint date:** 9 November 2026  
**Repository:** Frangga11/glass-tv-home  
**Canonical documentation branch:** `docs/project-master-inventory`  
**Canonical path:** `docs/project-management/GTVH-CHECKPOINT-LATEST.md`  
**Current cumulative Home implementation PR:** [PR #100](https://github.com/Frangga11/glass-tv-home/pull/100) — spacing/capsule refinement reapplied on the complete PR #84–#89 stack; PR #100 OPEN / DRAFT / UNMERGED. Source PR #99 remains OPEN / DRAFT / UNMERGED on its isolated PR #83 base.  
**Latest documentation sync:** cumulative PR #100 CI/APK audit, inventory, decision log, master project and handover synchronized on `docs/project-master-inventory`; this checkpoint records the current authoritative state.
## CURRENT CRITICAL RECONCILIATION — PR #91–#93 NOT YET IN PR #100 — 9 NOVEMBER 2026

**Status: AUDIT GAP CONFIRMED / NOT FINAL.** Live GitHub metadata, exact commit comparisons, changed-file inventories and patches establish that PR #100 (2de055a3b08fa9a0e5c4d2bb86d2884dbd71f43b) contains PR #84–#89 plus geometry/capsule changes, but does not contain the later PR #91–#93 Islamic Shelf / Arabic alignment sequence.

- PR #91 HEAD ee8bfb34db8c00a801ac260428f52ead1c7c6662: Arabic RTL + right gravity in Home Shelf and full reader; Indonesian translation LTR + left gravity.
- PR #92 branch fix/home-islamic-shelf-ayah-spacing-and-prayer-label-001, final test commit 59d63a3c9854b646876aade98e26fc74a39d3140: Arabic top 1dp, line spacing -2dp/0.92, Arab-to-translation gap 0dp, translation line spacing 0dp/1.04, hint gap 3dp, font padding retained; prayer source labels removed from header/summary while calculations/fallback remain untouched.
- PR #93 branch fix/home-islamic-shelf-arabic-right-align-corrective-001, HEAD 5b46ef0cea39fa439316e48bf85a3cbbb0944820: shared fixed runtime line-box width for Arabic preview and translation to give RTL a stable right edge. PR #93 CI #1656 / run 42529068497 passed on its own #92 ancestry, not on PR #100.
- PR #100 does not change AppLanguage.java, PrayerTimes.java, or the PR #92/#93 regression files. It overlaps MainActivity/layout/CI, so a blind cherry-pick is unsafe.

**Mandatory next step:** create a new cumulative candidate containing PR #84–#89 + PR #91–#93 + geometry PR #100, manually reconcile overlapping MainActivity/XML/CI changes, then rerun full tests/build/artifact checks. PR #100 is now a verified but incomplete candidate, not the latest complete build. Keep all source PRs Draft/unmerged; no ADB, TV install or merge before rendered 1920×1080 audit and explicit owner approval. See GTVH-HOME-CUMULATIVE-RECONCILIATION-003.md.

## CURRENT AUTHORITATIVE UPDATE — CUMULATIVE HOME SPACING/CAPSULE REFINEMENT — 9 NOVEMBER 2026

**Workstream:** `GTVH-HOME-SPACING-AND-CAPSULE-HEIGHT-REFINEMENT-002`  
**Status:** CUMULATIVE SOURCE + AUTOMATED CI + RELEASE APK VERIFIED / RENDERED VISUAL ACCEPTANCE PENDING — NOT FINAL.

- Active cumulative PR: [#100](https://github.com/Frangga11/glass-tv-home/pull/100), OPEN / DRAFT / UNMERGED.
- Source branch: `integration/home-spacing-capsule-height-pr89-cumulative-001`.
- Current exact HEAD: `2de055a3b08fa9a0e5c4d2bb86d2884dbd71f43b`.
- Exact base: PR #89 branch `fix/daily-ayah-offline-retry-backoff-001`, HEAD `4038c08f4825f7bacf965bc6708505d3c8c30ebc`.
- This candidate inherits the complete sequential stack PR #84 → #85 → #86 → #87 → #88 → #89, then reapplies PR #99 geometry/capsule changes. PR #99 itself remains OPEN / DRAFT / UNMERGED and is not the cumulative candidate.
- Candidate compare against exact PR #89 base: 2 commits ahead, 0 behind; exactly 11 changed files (3 production, 7 regression tests, 1 CI workflow). No out-of-scope production files.
- The overlapping Islamic-shelf contract was reconciled deliberately: the obsolete 27.5dp assertion was replaced with the 30dp measured-tray assertion while retaining PR #84's additional single-bottom-anchor regression test.
- CI workflow APK artifact allowlist now explicitly includes PR #100.
- Exact-head CI run #1677 / run `42533185178`, head `2de055a3b08fa9a0e5c4d2bb86d2884dbd71f43b`: both required jobs SUCCESS. Lists/tools/docs job ID `128891684465`; Launcher build/unit-test job ID `128891684759`. Python suite **208 tests, OK (1 skipped)**. Gradle `testReleaseUnitTest assembleRelease`: **BUILD SUCCESSFUL** (99 actionable tasks).
- Release APK: **1,626,857 bytes**. Artifact #13638675606, ZIP size **1,428,688 bytes**, ZIP SHA-256 `e1428796d54847cd7e33c5c761b7339ad76c6eea004b8e26c39f6499d9502521`; ZIP contains exactly one `app-release.apk`. Extracted APK SHA-256 `b7ae8aa3919e0818b0879b8996427ea6eab9b91f14bf0fcff84b80fad32f292a`. APK is 53,993 bytes above the historical 1.5 MiB reference; warning is non-blocking. The earlier run `42533180445` is a duplicate PR-trigger run on the same exact HEAD; do not use it as a separate source candidate.
- Dedicated audit report: [GTVH-HOME-SPACING-AND-CAPSULE-HEIGHT-REFINEMENT-002-AUDIT.md](GTVH-HOME-SPACING-AND-CAPSULE-HEIGHT-REFINEMENT-002-AUDIT.md).
- Rendered 1920×1080 visual audit and physical Mi TV Stick acceptance remain PENDING. No ADB, TV installation, device-setting change, or merge has been performed.
- Keep PR #84–#89, PR #99 and PR #100 OPEN / DRAFT / UNMERGED until all reconciliation, CI, artifact verification, visual acceptance and owner approval gates are complete.

### Approved requirements retained
- Left/right/bottom safe frame 30dp; top remains 29dp.
- MIC/Assistant, Wi-Fi and Beranda/Profile/Settings capsule height 30dp; Inputs excluded.
- Beranda/Profile icon-label gap 2dp; Settings remains 4dp.
- Wallpaper description and prayer-table visible lower edges each 30dp above the measured horizontal Glass Dock tray.
- Preserve prayer/Ayah logic and content, typography, icons, glass, blur, colors, shadows, focus/click/DPAD, Dock/Grid sizing/order, preferences and storage.

### Next action
Perform rendered 1920×1080 visual review against the approved safe-frame, capsule-height and shelf-to-tray measurements. Physical Mi TV Stick acceptance remains blocked until visual review passes. No ADB or installation before that gate.

---

## HISTORICAL INTEGRATION BLOCKER — PR #99 DID NOT CONTAIN THE ISLAMIC SHELF / PRAYER / DAILY AYAH STACK — 9 NOVEMBER 2026

> **Resolution:** The owner authorized repeating PR #99 from PR #89. The cumulative candidate is now PR #100 as documented above. The original findings below remain as historical root-cause evidence; their “next action after owner approval” is superseded by PR #100's current status.

Root cause confirmed by live PR metadata and changed-file inventories:
- PR #99 base SHA is PR #83 HEAD `947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803`. Its APK/CI run `42531805446` was built from PR #99 HEAD `443d8491109c416915955c8a9ceb202860916a6b`; therefore it contains the PR #83 ancestry plus PR #99 spacing/capsule changes, but not the separate subsequent Islamic/prayer/daily-ayah chain.
- The omitted chain is sequential and all OPEN / DRAFT / UNMERGED: PR #84 `d821842c2211cb6e65bbde506253cde7b3929771` (Islamic shelf anchor) → #85 `a5eaae843f8998c53238870609102e4c20b05ded` (Arabic typography) → #86 `746beaba2648b167dc174dfd3a61f5e859ed9fc2` (prayer refresh/next Subuh) → #87 `91f1152361d9b4db70a74f8f6b30ad38f60b4211` (daily Ayah cache/label) → #88 `a4b979540def5fc1414e66eec6df516a1ef22fe8` (prayer source label) → #89 `4038c08f4825f7bacf965bc6708505d3c8c30ebc` (daily Ayah offline retry). PR #89 base is PR #88; #84 base is PR #83.
- Confirmed file overlap: PR #84/#86/#87/#88/#89 modify `MainActivity.java`; PR #85 modifies `activity_main.xml`; PR #99 also modifies `MainActivity.java` and `activity_main.xml`. Therefore a blind cherry-pick or simple base retarget is unsafe; integrate by applying/rebasing PR #99's scoped geometry changes onto the PR #89 head, resolve overlap deliberately, then rerun all regression/Android/release checks.
- The PR #99 QA APK is technically built and CI-passing but is **not a cumulative integration build** for the Islamic/prayer/daily-Ayah work. Do not treat it as the current complete Home candidate or install it expecting PR #84–#89 behavior.
- Superseded next action: the owner approved this integration and the cumulative candidate is PR #100, documented in the current authoritative update above. The source PR #99 remains an isolated historical candidate; use PR #100 for all cumulative QA.

---
## CURRENT AUTHORITATIVE UPDATE — HOME SPACING/CAPSULE REFINEMENT — 9 NOVEMBER 2026

**This update supersedes older pending-status statements in the historical sections below for workstream GTVH-HOME-SPACING-AND-CAPSULE-HEIGHT-REFINEMENT-001 only.**

- Active source PR: [#99](https://github.com/Frangga11/glass-tv-home/pull/99), OPEN / DRAFT / UNMERGED; branch `verify/home-spacing-capsule-height-refinement-001-qa`; exact HEAD `443d8491109c416915955c8a9ceb202860916a6b`; base PR #83 source at `947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803`.
- Exact-head CI #1672 / run `42531805446`: both required jobs SUCCESS. Launcher job ID `128887415014`; Lists/tools/docs job ID `128887415159`. Duplicate PR-trigger run #1671 / `42531795756` also completed both jobs SUCCESS.
- Python suite: **190 tests, OK (1 skipped)**. Android `testReleaseUnitTest` and `assembleRelease`: **BUILD SUCCESSFUL**.
- Release APK size: **1,625,337 bytes**. GitHub workflow artifact #13637424249, ZIP size **1,426,876 bytes**, SHA-256 `1d2955027a07a2a80e7eb3398821639b6595542768860b35a4b6d185f5e38ff4`, expires 15 November 2026 UTC. Downloaded ZIP contains exactly one `app-release.apk`.
- Extracted APK SHA-256: `96d2bae44ac33a7c250467e37cb2d35c4d4cb85fc767993dc9625e6bd1e0eead`; file identification confirms Android APK with APK Signing Block. Local QA file: `/mnt/data/pr99_extract/app-release.apk`.
- Exact changed-file inventory: 11 files — 3 production files (`activity_main.xml`, `MainActivity.java`, `DockGeometry.java`), 7 regression-test files, and 1 CI workflow allowlist adjustment. No unrelated production file appears in the PR changed-file list.
- Source/layout and automated checks are PASS. The warning that APK exceeds the historical 1.5 MiB reference is diagnostic/non-blocking; it is not a build failure.
- **Still pending:** rendered 1920×1080 visual audit and physical Mi TV Stick acceptance. CI source assertions do not prove final rendered pixels. No ADB, installation, device-setting change, or merge has been performed.
- **Decision:** automated gates PASS; artifact verified; overall task remains **BELUM FINAL** until rendered visual review is completed. Keep PR #99 draft/unmerged and do not ask for device installation before that review.

---

---

## 0. Mandatory restart rule — one source, one door

**Every future Glass TV Home session MUST begin by reading this file first.** This is the only canonical checkpoint entry point. Do not select a starting point from chat summaries, old checkpoint names, historical sections in other documents, PR descriptions, branch names, or previous APK artifacts.

Authority order:
1. This file: current checkpoint, decisions, active source candidate, outstanding gates, PR inventory and next actions.
2. The seven supporting project documents listed in Section 8: detailed history and supporting evidence only. Their older status statements are historical and are superseded by this checkpoint.
3. Source code and CI evidence at the exact SHAs stated here.
4. Other PRs/branches: candidate or historical work only until explicitly reconciled into the canonical path.

**Single source of truth:** GitHub repository + canonical docs branch above. ChatGPT conversation/memory is a pointer and convenience copy, never a competing source of project status. Do not create parallel checkpoint documents or alternate restart routes. Any future update must first update this file, then synchronize the supporting documents in the same documentation pass.

**Checkpoint invocation phrase:** “Lanjutkan Glass TV Home dari GTVH-CHECKPOINT-LATEST.”  
The response to this phrase must always load this exact file from `docs/project-management` on `docs/project-master-inventory` before any code change.

## 1. Current project state — authoritative snapshot

### Home Header / whole Home screen
- Workstream: `GTVH-HOME-HEADER-REFINEMENT-001` + whole-Home source cross-audit.
- Active source branch: `fix/home-balanced-frame-001`.
- Active PR: #79, OPEN / DRAFT / UNMERGED; base `fix/home-layout-consolidated-001` (PR #77).
- Current PR #79 HEAD: `bf65621ae46b170294e4333928886fbafa0898b6` (30% shared width + identical 30dp pill geometry; CI #1590 PASS).
- Previous Home implementation baseline: `97f7e0c96d1c1dcd431cdc6edec31bf4837e11a5`; latest pill-size source commits: `d6fa94b913ed2e694a0c3ab4c6990afbdc31d604` (30% width), `370b85b525014c7726262ee8b740e34f4d061f58` (height), `bf65621ae46b170294e4333928886fbafa0898b6` (final XML geometry correction).
- Latest regression-test commit: `1d38edf07fbb894b169d00ccfc0f975f2a9fde58`; current PR tip includes subsequent final XML correction `bf65621ae46b170294e4333928886fbafa0898b6`.
- Prior PR-branch documentation sync head: `c15e4383d7dd3f148b31a19f5e9b85423936b56d`; subsequent commits are the four weather/AQI refinement commits listed below.
- PR #79 remains open, draft and unmerged. Do not merge or retarget without a separately verified reconciliation and owner authorization.

### Approved layout constraints — locked
- Safe frame: **29dp on top, bottom, left and right**. At Mi TV Stick logical density 320dpi (2px/dp), each edge = **58px**.
- Header navigation: **Home → Profile → Settings**, one shared glass capsule, each segment independently clickable/focusable.
- Header glass pill radius 21dp; shared nav capsule radius 20dp; inner segment radius 14dp.
- Preserve existing Dock/Grid card sizing, Grid sizing, app order, shelf content and established working behavior. Do not rework correct components to solve an unrelated issue.
- Header groups are aligned from measured geometry; no arbitrary independent offsets.
- Clock/date visual weight is reduced; time/date formatting and data logic remain unchanged.
- Indonesian/English Header labels and navigation behavior are covered by source contracts.
- Pagar Alam location metadata currently records 800m altitude.

## 2. Fixes completed and evidence

### HOME-001 through HOME-005
1. **HOME-001 Now Playing:** supported active media sessions remain eligible in PLAYING, PAUSED, BUFFERING and CONNECTING; playback icon reflects actual state.
2. **HOME-002 Prayer timetable:** visible timetable is focusable/clickable; click, DPAD Center and Enter open the detail popup; explicit DPAD routes include entry and return paths.
3. **HOME-003 Dock/Grid focus race:** generation-guarded deferred focus; stale callbacks invalidated on newer non-Grid destinations and lifecycle pause.
4. **HOME-004 Weather/AQI focus feedback:** scale and Header highlight are composed in one listener, preventing listener replacement/collision.
5. **HOME-005 atmospheric wallpaper overlay:** Home resume and active minute tick trigger a separate due-gated refresh; 15-minute due gate, overlap guard, dynamic-effects preference guard and refresh-on-enable behavior retained. This remains separate from the G3B Weather/AQI text cache.

### Home / Header source cross-audit
- 63 unique IDs in `activity_main.xml`; no duplicate ID declarations.
- Header actions: Home updates active tab; Profile opens profile popup; Settings opens quick-control popup.
- Location pill click and DPAD Center/Enter route to prayer-zone dialog.
- Weather/AQI click and DPAD routes checked at source level.
- Header center capsule width recomputes from measured left/right/header groups.
- Visible prayer table activation and navigation routes checked.
- Existing Home shelves, daily Ayah, prayer data, media card, app inventory, Dock behavior, wallpaper flow and profile/settings actions were reviewed for source preservation.
- Three additional regression contracts cover location click/DPAD, Home/Profile/Settings actions and Header width recalculation after measured-group changes.

## 3. CI / build — exact evidence

| Gate | Exact commit / run | Result |
|---|---|---|
| Implementation CI #1581 | `97f7e0c96d1c1dcd431cdc6edec31bf4837e11a5` / run `42486917618` | PASS; both required jobs, Android tests and release build |
| Test-only CI #1582 | `49f2777cee006a5afd50a33b85cb90a61a077e40` / run `42487572059` | PASS; 160 Python tests, 1 skipped; all 3 new tests passed; Android unit tests + release build |
| Docs-only tip CI #1583 | `c15e4383d7dd3f148b31a19f5e9b85423936b56d` / run `42487822397` | PASS; both jobs completed SUCCESS |
| Weather/AQI width refinement CI #1587 | `65544d9eea9bd0a5ae5156a9bd58724b1817fd2a` / run `42489445766` | PASS; both jobs, 161 Python tests (1 skipped), Android unit tests + release build |
| 30% / identical 30dp pill geometry CI #1590 | `bf65621ae46b170294e4333928886fbafa0898b6` / run `42490240670` | PASS; both jobs, 161 Python tests (1 skipped), pill regression PASS, Android unit tests + release build |

- Latest release APK diagnostic from CI #1590: **1,627,561 bytes** (artifact #13621062347; ZIP 1,426,708 bytes).
- Candidate test artifact: **#13619209786**. This is the latest candidate artifact recorded for the test-only source/test commit; no installation has occurred.
- CI #1583 validates a documentation-only PR tip; application source did not change after CI #1582.
- Release workflows #37/#38 published an older CI #1545 build for commit `c058935ed4398f5c7964d753fcf35a9cf77989bb`; they are **not** the current PR #79 candidate. Never mislabel those release assets as the latest build.

## 4. Acceptance status — do not overstate

| Area | Status |
|---|---|
| Source implementation and static cross-audit | PASS |
| HOME-001–HOME-005 source fixes | PASS |
| Automated Python regression suite | PASS (160, 1 skipped) |
| Android unit tests and release build | PASS |
| Latest docs-only CI #1583 | PASS |
| Rendered safe-frame / visual clipping on Mi TV Stick | PENDING |
| Full physical DPAD traversal / focus retention | PENDING |
| Runtime media, prayer, weather/AQI, overlay and lifecycle checks | PENDING |
| Runtime crash/log inspection | PENDING |
| Overall whole-Home acceptance | **PARTIAL — NOT FINAL** |

No APK installation, ADB session, physical Mi TV Stick test, device settings/default-launcher change, force-push or PR merge has been performed. These boundaries remain in force unless separately authorized.

## 5. PR inventory and consolidation control

GitHub currently reports **58 OPEN pull requests**. This is a repository-wide inventory, not a claim that all are active or all are defective. Several are stacked branches, parallel implementations, or historical drafts. Do not merge or close them blindly. Their code and dependencies must be compared with the canonical source path before disposition.

### Current Home consolidation cluster
- **#79** — active Home/Header candidate; current source/test/docs validation described above.
- **#77** — direct base/dependency of #79; consolidated Dock geometry and deterministic DPAD viewport work. Must be reviewed as the dependency, not merged automatically.
- **#78** — parallel H01 Header reskin branch based on `main`; competing/parallel Header work. Reconcile its unique changes against #79 before deciding superseded/retain.
- **#74, #75, #76** — earlier stacked Home/Dock layout branches. Compare their unique commits and test evidence with consolidated PR #77/#79; do not require merging all four as a chain and do not discard unreviewed unique work.

### All currently open PRs — immutable inventory snapshot
The table records the current GitHub head/base refs and head SHA prefix as observed during this checkpoint. It is an inventory for systematic reconciliation, not approval to merge.

| PR | Title | Head branch | Head SHA (12) | Base branch | State |
|---|---|---|---|---|---|
| #79 | fix(home): balance main-screen safe frame and restore header navigation | `fix/home-balanced-frame-001` | `c15e4383d7dd` | `fix/home-layout-consolidated-001` | Draft |
| #78 | feat(h01): header reskin preserving approved Home tab order | `feature/h01-header-reskin` | `aca3c6b8a9cf` | `main` | Draft |
| #77 | Home layout: consolidate Dock geometry and deterministic DPAD viewport fix | `fix/home-layout-consolidated-001` | `d6a5581d2975` | `feature/g4-parent-pin-hardening` | Draft |
| #76 | Home layout: keep Quick Toggles inside the safe screen frame | `fix/home-global-layout-bounds` | `f085c41356e2` | `fix/home-dock-alignment-row-scroll` | Draft |
| #75 | Home Dock: align full-width tray and scroll app rows in place | `fix/home-dock-alignment-row-scroll` | `21bc204a413f` | `fix/home-dock-visual-parity-v2` | Draft |
| #74 | Home Dock: adopt Patch 4.1 layout, glass and breathing focus | `fix/home-dock-visual-parity-v2` | `61d4e429be12` | `feature/g4-parent-pin-hardening` | Draft |
| #72 | G4: Harden parent PIN storage without changing retry behavior | `feature/g4-parent-pin-hardening` | `7146de589533` | `feature/g3f-global-settings` | Draft |
| #71 | G3F — Global Settings | `feature/g3f-global-settings` | `455b619c3eef` | `feature/g3e-wifi` | Draft |
| #70 | G3E — Wi-Fi V05 | `feature/g3e-wifi` | `9125043e24b8` | `feature/g3d-quick-settings` | Draft |
| #69 | G3D — Quick Settings V04 | `feature/g3d-quick-settings` | `bbc7b240d2ad` | `feature/g3c-profile-avatar-child-mode` | Draft |
| #68 | G3C — Profile, Avatar & Child Mode | `feature/g3c-profile-avatar-child-mode` | `8cf81f84bd62` | `feature/g3b-weather-aqi-v4` | Draft |
| #67 | G3B: Weather & AQI V4 dashboard | `feature/g3b-weather-aqi-v4` | `8cfc5c886950` | `feature/g3a-home-independent-dock` | Draft |
| #66 | G3A — Home Screen & Independent Dock | `feature/g3a-home-independent-dock` | `87302f52de84` | `feature/g2-shared-ui-foundation-repair` | Draft |
| #65 | G2: repair shared Global UI foundation | `feature/g2-shared-ui-foundation-repair` | `40fa0715bdbc` | `integration/b-series` | Draft |
| #64 | UI Refresh P4-01 — Home Screen & Dock: isolated preference foundation | `feature/ui-global-01-home-dock` | `e4bc99c9a482` | `integration/b-series` | Draft |
| #63 | feat(E06): add independent Dock presentation preference model | `feature/e06-dock-capacity-control-v1` | `ecdeb0eba9fc` | `feature/e06-home-screen-subtabs` | Draft |
| #62 | feat(E02): prepare upstream GIPHY avatar catalog integration | `feature/e02-giphy-avatar-search-v1` | `588456144df2` | `feature/e02-profile-avatar-lock-ui` | Draft |
| #56 | F07 — Performance, crash, freeze and stability hardening | `integration/project-f07-performance-stability` | `28e841a3475c` | `integration/project-f06-localization` | Draft |
| #55 | F06 — Bilingual localization and global UI consistency | `integration/project-f06-localization` | `2c369a0a48c7` | `integration/project-f05-persistence-recovery` | Draft |
| #54 | F05 — Persistence and Recovery Validation (Draft) | `integration/project-f05-persistence-recovery` | `1048b0b68ae5` | `integration/project-f04-feature-isolation-v2` | Draft |
| #53 | F04: verify Avatar, Wallpaper and StandBy feature isolation | `integration/project-f04-feature-isolation-v2` | `6ffc8b4c253b` | `integration/project-f02-system-validation` | Draft |
| #51 | D01 follow-up: restrict location configuration broadcasts | `fix/d01-location-broadcast-authorization` | `66352838e615` | `feature/d01-config-storage-audit` | Draft |
| #50 | F02 — Integrated System Validation (Draft) | `integration/project-f02-system-validation` | `2ede452b3137` | `integration/project-f-staged-full` | Draft |
| #41 | E08: unify wallpaper settings into three focused controls | `feature/e08-wallpaper-settings-redesign` | `edd24d72256d` | `feature/e07-animation-effects` | Draft |
| #40 | E07: audit and respect reduced motion for visual effects | `feature/e07-animation-effects` | `a0f2b0fff6ee` | `feature/e06-home-screen-subtabs` | Draft |
| #39 | E06: audit and localize Home Screen controls | `feature/e06-home-screen-subtabs` | `570b7c83efae` | `feature/e05-information-center-ayah` | Draft |
| #38 | E05: complete ayah rendering and audit information widgets | `feature/e05-information-center-ayah` | `6fe89fad0786` | `feature/e04-standby-screensaver` | Draft |
| #37 | E04: harden StandBy display power transition | `feature/e04-standby-screensaver` | `08076bb945a1` | `feature/e03-wallpaper-clean-metadata` | Draft |
| #36 | E03: separate Default wallpaper metadata from image content | `feature/e03-wallpaper-clean-metadata` | `e085f8aed6fd` | `feature/e02-profile-avatar-lock-ui` | Draft |
| #35 | E02: audit profile avatars, QR upload and app-lock UX | `feature/e02-profile-avatar-lock-ui` | `3ab9b55f3cfe` | `feature/e01-device-settings-home-control` | Draft |
| #34 | E01: improve device settings, HOME control and system actions | `feature/e01-device-settings-home-control` | `edd6461c9327` | `feature/d07-concurrent-profile-content-state` | Draft |
| #31 | D07: prevent stale and concurrent profile/content state writes | `feature/d07-concurrent-profile-content-state` | `7ce92d309112` | `feature/d06-process-death-recovery` | Draft |
| #30 | D06: restore persisted prayer alarms after TV reboot | `feature/d06-process-death-recovery` | `1bb6ce9bf04d` | `feature/d05-standby-photo-atomic-persistence` | Draft |
| #29 | D05: make StandBy photo persistence atomic and failure-aware | `feature/d05-standby-photo-atomic-persistence` | `7f043943ab0c` | `feature/d04-profile-avatar-state-sync` | Draft |
| #28 | D04: synchronize profile and avatar selection state | `feature/d04-profile-avatar-state-sync` | `302a3aa1a7fa` | `feature/d03-locked-app-ui-refresh` | Draft |
| #27 | D03: refresh locked and hidden app summaries | `feature/d03-locked-app-ui-refresh` | `7380796a69e2` | `feature/d02-settings-ui-propagation` | Draft |
| #26 | D02: audit immediate settings UI propagation | `feature/d02-settings-ui-propagation` | `909aaff5709a` | `feature/d01-config-storage-audit` | Draft |
| #22 | C05: unify and harden shared upload body reception | `feature/c05-shared-upload-pipeline` | `53cf9a883f06` | `feature/c04-http-endpoint-routing` | Draft |
| #25 | D01: audit settings and configuration storage ownership | `feature/d01-config-storage-audit` | `cd710681099f` | `feature/c07-upload-session-resilience` | Draft |
| #21 | C04: harden HTTP endpoint routing and response contracts | `feature/c04-http-endpoint-routing` | `aad9aeb163b4` | `feature/c03-phone-tv-network-verification` | Draft |
| #24 | C07: harden upload disconnect, retry, expiry and cancellation | `feature/c07-upload-session-resilience` | `2315f12472f4` | `feature/c06-upload-file-validation` | Draft |
| #20 | C03: verify phone-to-TV active LAN communication | `feature/c03-phone-tv-network-verification` | `0599ceb01a55` | `feature/c02-tv-local-server-ip` | Draft |
| #23 | C06: validate upload file types, dimensions, names and storage paths | `feature/c06-upload-file-validation` | `2bf119da4582` | `feature/c05-shared-upload-pipeline` | Draft |
| #19 | C02: audit TV local server and IP discovery | `feature/c02-tv-local-server-ip` | `b0e1ff048e36` | `feature/c01-local-communication-audit` | Draft |
| #18 | C01: harden local QR upload server foundation | `feature/c01-local-communication-audit` | `8a3a981f097e` | `feature/b07-menu-focus-restoration` | Draft |
| #17 | B07: restore Settings menu, submenu, and D-pad focus | `feature/b07-menu-focus-restoration` | `f62bdc5d9804` | `feature/b06-notification-feedback` | Draft |
| #16 | B06: harden notification feedback and network timeout handling | `feature/b06-notification-feedback` | `5b2f10095bd5` | `integration/b-series` | Draft |
| #12 | B03: improve dialog and popup background opacity | `feature/b03-dialog-opacity-layering` | `d87ffdf91485` | `feature/b02-remote-focus-indicators` | Draft |
| #11 | B02: make D-pad focus states visible and consistent | `feature/b02-remote-focus-indicators` | `c746063cb08e` | `feature/b01-global-navigation-retention` | Draft |
| #10 | B01: persist launcher navigation and cursor position | `feature/b01-global-navigation-retention` | `389e289e9392` | `feature/profile-avatar-security-integration` | Draft |
| #9 | Integrate profile avatar QR upload with profile security | `feature/profile-avatar-security-integration` | `7e8392e1a762` | `feature/profile-security-integration` | Draft |
| #8 | Profile security: parent PIN gates and isolated profile switching | `feature/profile-security-integration` | `1ba744efee61` | `fix/wallpaper-qr-session-security` | Draft |
| #7 | Wallpaper QR security and single Default Dempo wallpaper | `fix/wallpaper-qr-session-security` | `62b1e7ce63f3` | `feature/settings-reorganization-standby-gallery` | Draft |
| #6 | Avatar QR upload: single-photo replacement and cleanup | `fix/avatar-qr-upload-single-photo` | `0cb1b96e2c25` | `feature/settings-reorganization-standby-gallery` | Draft |
| #5 | Add custom photo avatars to Glass TV profiles | `fix/avatar-photo-upload-audit` | `29a5a8bb8b2f` | `feature/settings-reorganization-standby-gallery` | Draft |
| #4 | Reorganize settings and add personal-photo StandBy mode | `feature/settings-reorganization-standby-gallery` | `06f4fa2adf73` | `main` | Draft |
| #3 | Separate weather and icon glow effects; localize launcher to Indonesian | `feature/independent-visual-effects-id` | `888471fe1f6b` | `main` | Ready |
| #2 | Fix Android TV power-off accessibility selection | `fix/power-off-accessibility` | `ed320d03d5e2` | `main` | Ready |

**Disposition rule for every PR above:** classify as (A) already represented in canonical source, (B) unique changes to port/reconcile, (C) dependency needed by active work, (D) superseded duplicate to close only after diff/evidence review, or (E) unresolved defect requiring repair. Record the evidence, exact source SHA, CI status and decision in the audit register and decision log. No PR may be silently omitted. No PR is “completed” merely because it is Draft or old.

### Known dependency note
PR #79 is stacked on #77. Earlier comparison found one workflow-only commit after the common merge base on #77; it is not a functional source conflict. Preserve the dependency and reconcile ancestry before any merge; do not cherry-pick an equivalent workflow-only change just to make branch history look linear.

## 6. Single ordered next-work plan

1. **Freeze this checkpoint as the sole restart point.** Verify the canonical file and docs branch tip.
2. **PR reconciliation audit:** inspect all 58 open PRs and their unique diffs, dependencies, CI evidence and issue history. Start with #79/#77/#78/#74–76, then walk the remaining chains in dependency order.
3. **Build one canonical integration graph:** identify the authoritative branch/PR for each retained feature; eliminate duplicate/parallel paths only after proving their unique changes are represented or intentionally rejected.
4. **Resolve outstanding source defects:** only confirmed defects; narrow patches; add regression tests; CI on exact resulting HEAD.
5. **Synchronize documentation in one pass:** update this checkpoint first, then README, Master Project, Inventory, Decision Log, Audit Register, Handover and the dedicated audit report. All must point back here.
6. **Prepare physical acceptance only after source/CI/reconciliation gates pass** and after explicit owner authorization. Use the Mi TV Stick baseline: Android 11, 1920×1080, density 320dpi.
7. **Final merge/release decision:** only after dependencies, exact-head CI, PR reviews and device acceptance are all reconciled and owner-approved.

## 7. Quality-control rules — mandatory

- No speculative UI edits; preserve verified-good sizing, placement, behavior and app order.
- Never claim runtime/device acceptance from static source review or CI.
- Never use an older APK/release artifact as the current candidate.
- Never merge, close, force-push, install, ADB, or change device settings without explicit authorization.
- Record every source edit with branch + exact commit SHA + changed files + reason + tests + CI run + artifact.
- Record every unresolved issue with owner decision, severity, dependency and next action.
- Any stale or contradictory documentation must be corrected or explicitly marked historical; no parallel “latest” checkpoint.
- If any required evidence is unavailable, label the gate PENDING/UNKNOWN rather than infer PASS.

## 8. Supporting documents — detail only, not alternate entry points

All are on `docs/project-master-inventory` and must link back to this checkpoint:
- `README.md` — project overview and navigation.
- `MASTER_PROJECT.md` — goals, requirements, architecture and master history.
- `INVENTORY.md` — component/source inventory.
- `DECISION_LOG.md` — approved decisions and rationale.
- `AUDIT_REGISTER.md` — defects, evidence, remediation and dispositions.
- `HANDOVER.md` — operator handoff and restart procedure.
- `GTVH_HOME_SCREEN_CROSS_AUDIT_001.md` — detailed Home source cross-audit.
- PR-local `HOME_LAYOUT_RECONCILIATION_2026-11-08.md` is historical/implementation evidence only; it is not a restart point.

Supporting-document synchronization commits:
| File | Commit |
|---|---|
| README.md | `8ad0db3c41c4343c0081c1822d35d1d873bd193a` |
| MASTER_PROJECT.md | `fa83007db9e8ed4b521e56b96025953da76f84b6` |
| INVENTORY.md | `227c6ddae335f606a5d05ab8130f90d5bbfe77fb` |
| DECISION_LOG.md | `fc7832cb30305535fe3dcb0d3daa178d449295bc` |
| AUDIT_REGISTER.md | `7da5adf23060083f0fbec33d95f3acb01073a12c` |
| HANDOVER.md | `d680e638435311e1381a0e98c356ff732bcdab75` |
| GTVH_HOME_SCREEN_CROSS_AUDIT_001.md | `f9e9232ad75e21ac9955bdfe7c4ddbc51ea4c039` |

## 9. Active refinement — GTVH-HOME-WEATHER-PILL-WIDTH-REFINEMENT-002 (8 November 2026)

- Owner's revised decision: **both capsules use 30% of measured Header width**. Location–altitude remains the sizing reference; weather/AQI must match it exactly.
- Runtime implementation: `MainActivity.applyHeaderWidths()` computes `Math.round(width * 0.30f)` once and assigns that exact pixel width to both `locationPill` and `weatherAirQuality`. Both retain the same parent-start anchor.
- Physical geometry: both capsules are **30dp high**. Location is unchanged and remains the reference. Weather uses 30dp height, 12dp start/end padding, 0dp top/bottom padding and vertical centering, matching the location container's default vertical padding and center gravity. Both use the same `header_glass_pill_bg` background/radius. Existing 8dp vertical separation, fonts, icons, content, provider, click/DPAD/focus and animations are preserved. Weather remains single-line/end-ellipsized.
- At measured inner Header width 1,804px, 30% calculates to **541px** (rounded). At 320dpi, 30dp height corresponds to **60px**. These are source/layout calculations, not rendered-device measurements.
- Changed source files only: `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java`, `launcher/app/src/main/res/layout/activity_main.xml`, and `tests/test_home_layout_bounds_contract.py`. The regression contract checks shared 30% width, both layout width assignments, common left anchor, equal 30dp height, horizontal padding, vertical centering, weather ellipsis and content-refresh invariance.
- Current PR #79 HEAD: `bf65621ae46b170294e4333928886fbafa0898b6`. PR remains OPEN / DRAFT / UNMERGED; no merge or retarget.
- CI #1590 / run `42490240670`: **BOTH JOBS SUCCESS**. Python: **161 tests, OK (1 skipped)**; new pill-size regression PASS. Android `testReleaseUnitTest` and `assembleRelease`: SUCCESS.
- Release APK: **1,627,561 bytes**. Artifact ID `13621062347`, ZIP size 1,426,708 bytes, expires 15 November 2026. Extracted APK SHA-256: `51c6921ad7055812dd4c3de844bce26516b0ffeea6dd8401abc5d94913dcb56d`. APK delivered to owner for Mi TV Stick test.
- Physical acceptance is **PENDING**. Owner to test on Mi TV Stick, 1920×1080 / 320dpi, Indonesian and English, short/long location/weather/AQI values; verify clipping, actual capsule bounds/height, left/right edge equality, vertical gap, focus/click/DPAD and unaffected Header components. Do not claim physical success until owner reports results.

## 10. Final checkpoint decision

**Current source/CI result:** PASS; CI #1590 and release APK build successful.  
**Overall Home acceptance:** PARTIAL / NOT FINAL; APK handed to owner; physical Mi TV Stick acceptance remains pending.  
**Repository cleanup:** NOT FINAL; 58 open PRs require evidence-based reconciliation.  
**Documentation architecture:** one canonical checkpoint entry point established by this file; supporting documents are subordinate detail/history.

**Next session must start here and nowhere else.**


## Mandatory APK delivery rule — 8 November 2026 (OWNER DIRECTIVE)

**NON-NEGOTIABLE: every user-facing build delivery must be a direct Android package file ending exactly in .apk. Never deliver .bin, .zip, or a renamed archive as the installable app.**

- Build the Android release APK (assembleRelease) from the exact verified source HEAD.
- The final user-facing deliverable is the actual APK binary, directly downloadable and installable on Android TV; its filename must end in .apk.
- If GitHub Actions stores/transfers the build artifact inside a ZIP archive, that ZIP is an internal transport container only. Extract the APK, verify the extracted file is a valid Android package, and distribute only the extracted .apk to the owner.
- Never send the workflow-artifact ZIP itself to the owner. Never change an archive's extension to .apk; verify file type, size, SHA-256, and exact source commit/build run.
- Before delivery, check the final link/filename and confirm that it points directly to the APK asset, not a ZIP/BIN attachment.
- If a direct .apk cannot be produced or verified, stop and report the blocker; do not substitute ZIP/BIN.
- This rule supersedes any older handover wording that treated the workflow ZIP as the deliverable. CI artifact archive sizes are diagnostic metadata only.


## APK delivery failure follow-up — 8 November 2026 (OWNER REPORTED)

- Owner supplied a Samsung Browser screenshot showing the ChatGPT sandbox attachment `Glass-TV-Home-30pct-Pills.apk` is presented by the browser as `Glass-TV-Home-30pct-Pills.apk.bin`. The prior delivery therefore **FAILED the APK-only delivery requirement**. Do not mark delivery fixed merely because the local file is a valid APK or its local filename ends in `.apk`.
- Root cause of the repeated failure: assistant again used a `sandbox:/mnt/data/...` attachment link after the owner had already reported that this delivery path adds `.bin` in Samsung Browser. This was a process error; the documented rule was not followed.
- Audit of `.github/workflows/publish-apk-release.yml` on PR #79 found the prior publisher was hardcoded to download and publish CI #1545 artifact (`c058935...`), not the current CI #1590 candidate. Therefore the existing direct GitHub Release asset was stale and cannot be reused as the latest build.
- Corrected publisher workflow commit on PR #79 branch `fix/home-balanced-frame-001`: `16ba3bf72f9b2e080fac23510f744c4ea5efb399`. It now checks out the exact PR head, runs `testReleaseUnitTest assembleRelease`, validates the APK package, and publishes the actual APK binary as a GitHub Release asset with a tag derived from the exact source SHA. It does not upload a ZIP as the user-facing asset.
- PR #79 remains OPEN / DRAFT / UNMERGED. At documentation time, no workflow run for commit `16ba3bf72f9b2e080fac23510f744c4ea5efb399` had yet been returned by the workflow-run lookup. **Direct APK delivery is NOT YET VERIFIED/FIXED.** Do not give another sandbox attachment link. Wait for the publisher workflow to complete, verify the GitHub Release asset name ends exactly `.apk`, verify MIME `application/vnd.android.package-archive`, size and SHA-256 against the run, then provide only the direct GitHub Release asset URL.
- If the workflow does not trigger or fails, stop and report the exact blocker; do not fall back to sandbox, `.bin`, or ZIP. Never claim a new build or successful delivery until the exact-head workflow and direct asset are confirmed.


## Root-cause audit and workflow correction — 8 November 2026

- Owner reported Samsung Browser saves the prior ChatGPT sandbox attachment as `Glass-TV-Home-30pct-Pills.apk.bin`; prior delivery failed the APK-only acceptance criterion.
- Root cause verified from GitHub Actions run `42490240680` (publisher run #45) job log `128760816419`: it executed the old workflow definition and downloaded hardcoded artifact from CI #1545 (`run-id: 42479472690`, artifact `13616788353`), verified old source commit `c058935ed4398f5c7964d753fcf35a9cf77989bb`, then uploaded that stale binary to release tag `glass-tv-home-ci1545-20261108`. This is why the release never contained the latest PR #79 build.
- Exact workflow-dispatching cause: PR #79 is based on `fix/home-layout-consolidated-001`. For `pull_request` events, GitHub uses the workflow definition from the PR base branch, not the edited workflow copy on PR #79's head branch. The base-branch workflow still contained the CI #1545 hardcoding, so changing only the PR #79 head copy did not change the executed workflow. This behavior is confirmed by the run #45 logs.
- Corrected `.github/workflows/publish-apk-release.yml` on PR #79 base branch `fix/home-layout-consolidated-001`, commit `7e51a86cc83394ae47f7c9d32c22bf0c6042c9d1`: it now checks out the exact `github.event.pull_request.head.sha`, runs `testReleaseUnitTest assembleRelease`, validates the APK manifest/package, and publishes a uniquely tagged direct `.apk` asset named `Glass-TV-Home-PR79-<source-sha>.apk`. No workflow ZIP is used as the user-facing deliverable.
- Aligned the same corrected workflow into PR #79 head branch to prevent a future merge from restoring the stale publisher: commit `b4abdf0bd555a0106c135add20c3f287cf10b412`. PR #79 also contains prior trigger/diagnostic commit `4fefd3ae0640eb1559b83856d9f907dd687f7987`.
- PR #77 received a publisher workflow run `42491455206` which was **SKIPPED** by design because the workflow is scoped to PR #79; its companion CI run `42491455159` was still in progress at audit time. No new PR #79 publisher run was visible yet after the base workflow correction and head alignment. The latest Release listing still contained only the stale CI #1545 asset.
- Therefore workflow source correction is committed, but new build + direct APK release + Samsung Browser download verification remain **PENDING / NOT ACCEPTED**. Do not claim completion, do not reuse old release, and do not send sandbox links, ZIP or BIN. Next: obtain a PR #79 synchronize/dispatch event using the corrected base workflow, verify successful unit tests/build, inspect new release asset exact `.apk` name and `application/vnd.android.package-archive` MIME, verify size/SHA against workflow logs, then give only the direct GitHub Release asset URL.


## Direct APK rebuild completed — 8 November 2026

- Owner requested rebuild. The corrected PR #79 base-branch publisher ran successfully: workflow run `42491530852` (job `128764760421`, conclusion `success`).
- Exact source HEAD: `b4abdf0bd555a0106c135add20c3f287cf10b412`.
- Build command: `./gradlew --no-daemon testReleaseUnitTest assembleRelease`; both app and installer release unit-test tasks passed and `:app:assembleRelease` completed; log reports `BUILD SUCCESSFUL`.
- Verified output: `Glass-TV-Home-PR79-b4abdf0bd555a0106c135add20c3f287cf10b412.apk`.
- Size: `1,627,561` bytes. SHA-256: `2bd779dc0da3a8cbf81b7f6f7dad1ee53d6f7be949eef9261d1d0b7018c09aa6`.
- GitHub Release tag: `glass-tv-home-pr79-b4abdf0bd555a0106c135add20c3f287cf10b412`.
- Release API verification confirms exactly one asset with the exact `.apk` filename, size `1,627,561`, state `uploaded`, and content type `application/vnd.android.package-archive`. Direct asset URL: `https://github.com/Frangga11/glass-tv-home/releases/download/glass-tv-home-pr79-b4abdf0bd555a0106c135add20c3f287cf10b412/Glass-TV-Home-PR79-b4abdf0bd555a0106c135add20c3f287cf10b412.apk`.
- Separate general CI run `42491530833` was still in progress at the time of this note; the publisher workflow itself independently passed unit tests + release APK build. Device installation and Samsung Browser filename behavior remain pending owner confirmation.
- User-facing delivery rule observed: provide only the direct GitHub Release `.apk` asset URL. No sandbox attachment, ZIP, or BIN.

# CHECKPOINT RESMI TERBARU — GTVH-APK-DIRECT-DELIVERY-001

**Tanggal:** 8 November 2026  
**Status:** BUILD PASS · DIRECT APK RELEASE VERIFIED · DEVICE DOWNLOAD ACCEPTANCE PENDING

## 1. Tujuan checkpoint
Menetapkan jalur build dan distribusi APK Glass TV Home yang terbukti menghasilkan aset Android .apk langsung, serta mencegah pengulangan kesalahan pengiriman .apk.bin, ZIP, atau BIN.

## 2. Source of truth
- Repository: Frangga11/glass-tv-home
- PR aktif: https://github.com/Frangga11/glass-tv-home/pull/79 — OPEN / DRAFT / UNMERGED
- Source branch: fix/home-balanced-frame-001
- Base branch: fix/home-layout-consolidated-001
- Exact source HEAD used for successful build: b4abdf0bd555a0106c135add20c3f287cf10b412
- Corrected publisher workflow is present on the PR base branch (commit 7e51a86cc83394ae47f7c9d32c22bf0c6042c9d1) and aligned on PR head branch (commit b4abdf0bd555a0106c135add20c3f287cf10b412).

## 3. Root cause — permanently recorded
The old publisher workflow hardcoded CI #1545's artifact ID/run ID and source SHA. More importantly, for a pull_request event, GitHub used the workflow definition from the PR base branch, not the PR head branch. Editing only the PR head copy therefore did not change the workflow GitHub executed. The old run log proved it republished the stale CI #1545 APK. Correction: maintain the workflow on the PR base branch and keep the PR head copy aligned to avoid regression after merge.

## 4. Mandatory build and delivery route (DO NOT DEVIATE)
1. Confirm PR number, base/head branches, and exact current source HEAD.
2. Ensure .github/workflows/publish-apk-release.yml is corrected on the PR base branch; align the same file on PR head branch.
3. Trigger a PR #79 synchronize event (or an authorized manual workflow dispatch if available). Do not assume a commit on the head branch alone controls the workflow definition.
4. Workflow must checkout github.event.pull_request.head.sha, run ./gradlew --no-daemon testReleaseUnitTest assembleRelease, validate APK package/manifest, and calculate size and SHA-256.
5. Publish the actual APK binary as a GitHub Release asset with a unique source-SHA-derived tag and filename ending exactly .apk.
6. Verify the Release API asset: exact filename ends .apk, state=uploaded, MIME application/vnd.android.package-archive, size and SHA-256 match workflow logs.
7. Send the user only the direct GitHub Release asset URL (the URL path itself must end in .apk). Never send sandbox:/mnt/data attachments, GitHub Actions artifact ZIPs, .zip, or .bin. Never rename a ZIP to .apk.
8. If a direct APK Release asset cannot be verified, stop and report the blocker; do not substitute another format or stale build.
9. Physical Samsung Browser download test and Mi TV Stick installation/visual acceptance remain pending until owner confirms.

## 5. Latest verified build
- Publisher workflow run: https://github.com/Frangga11/glass-tv-home/actions/runs/42491530852 — SUCCESS.
- Job: 128764760421 — SUCCESS.
- Build command: ./gradlew --no-daemon testReleaseUnitTest assembleRelease.
- Unit tests and assembleRelease: PASS; log reports BUILD SUCCESSFUL.
- APK filename: Glass-TV-Home-PR79-b4abdf0bd555a0106c135add20c3f287cf10b412.apk.
- Size: 1,627,561 bytes.
- SHA-256: 2bd779dc0da3a8cbf81b7f6f7dad1ee53d6f7be949eef9261d1d0b7018c09aa6.
- Release tag: glass-tv-home-pr79-b4abdf0bd555a0106c135add20c3f287cf10b412.
- Direct APK asset: https://github.com/Frangga11/glass-tv-home/releases/download/glass-tv-home-pr79-b4abdf0bd555a0106c135add20c3f287cf10b412/Glass-TV-Home-PR79-b4abdf0bd555a0106c135add20c3f287cf10b412.apk
- Release API independently confirms one uploaded asset, exact .apk filename, MIME application/vnd.android.package-archive, matching size and digest.

## 6. Acceptance boundary / next action
- Build: PASS.
- Release asset: PASS — direct APK verified at GitHub.
- Samsung Browser filename behavior: NOT YET CONFIRMED by owner.
- Mi TV Stick installation and visual acceptance: PENDING.
- PR #79 remains OPEN / DRAFT / UNMERGED. No merge authorized by this checkpoint.

**Resume instruction:** Start from this checkpoint. Do not repeat the stale CI #1545 publisher, do not edit only the PR head workflow when the PR base workflow is authoritative, and do not send sandbox/ZIP/BIN. First obtain the owner's Samsung Browser download and Mi TV Stick test result; if another build is needed, follow section 4 exactly.

## 11. Active scoped refinement — GTVH-HOME-WALLPAPER-TEXT-REFINEMENT-001 (8 November 2026)

**Status: SOURCE IMPLEMENTATION PASS · CI PASS · VISUAL/DEVICE ACCEPTANCE PENDING · NOT FINAL**

- Scope PR: [#80](https://github.com/Frangga11/glass-tv-home/pull/80) — OPEN / DRAFT / UNMERGED.
- Source branch: `fix/home-wallpaper-text-refinement-001`.
- Exact PR #80 HEAD: `c7d95c3de24bc70b671159d5aa9ef49451cbe112`.
- Base: `fix/home-balanced-frame-001`, exact base HEAD `b4abdf0bd555a0106c135add20c3f287cf10b412` (PR #79 remains OPEN / DRAFT / UNMERGED).
- Changed files: `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java`; `launcher/app/src/main/res/layout/activity_main.xml`; new `tests/test_home_wallpaper_text_refinement_contract.py`.

### Approved implementation and measured geometry
- Wallpaper shelf order: location → title → description. Existing `WallpaperMetadata` values remain unchanged: `Pagar Alam, Sumatera Selatan`; `Kabut Pagi Dempo`; `Udara sejuk turun perlahan, menyelimuti lereng dan kebun teh Pagar Alam.` Location capitalization is now preserved as authored (not forced to uppercase).
- Both information shelves share the existing Dock-relative `heroBottom` anchor. Conversion: `5mm × 160 / 25.4 = 31.496dp`; at density `2.0` (320dpi), `62.992px`, rounded to **63px**. Existing 50dp shelf-to-tray gap becomes approximately **18.5dp** after the shift; a 12dp minimum-gap clamp prevents collision on other density/layout cases. No Dock geometry or global safe-frame values were changed.
- Prayer header, names and times receive restrained cool-to-warm gradients. Prayer data, order, provider and interaction remain unchanged.
- Daily ayah idle outline is removed and the open hint border is removed. A restrained 1dp focus outline remains while the ayah block has remote focus; existing click/DPAD activation remains intact.

### CI / build evidence
- CI run `42493469262`: both jobs SUCCESS.
- Python contract suite: **166 tests, OK (1 skipped)**; all five new GTVH wallpaper refinement contracts passed.
- Android `testReleaseUnitTest` and `assembleRelease`: PASS; log reports `BUILD SUCCESSFUL in 1m 37s`.
- Release APK diagnostic: **1,627,937 bytes** (about 1,589 KB), +376 bytes against the prior PR #79 recorded APK size of 1,627,561 bytes. The workflow emitted a warning above its historical 1.5 MiB reference; this is explicitly non-blocking.
- PR #80's APK-artifact upload step is skipped by the current workflow scope (which only publishes visual-test artifacts for PRs #74–#79). No user-facing APK release or device install was performed.

### Regression boundaries and remaining acceptance
- PR diff is limited to the two production files above plus one new regression test. `DockGeometry.java`, Dock/Grid sizes/order/scroll, Header and capsules, navigation/focus routes, global safe frame, wallpaper image/provider/rotation, prayer data and unrelated components are untouched.
- **Pending:** rendered visual review at 1920×1080; physical Mi TV Stick test for exact shelf movement, Dock collision, clipping, bright/dark wallpaper contrast, prayer legibility, daily ayah frame and remote focus.
- Do not merge PR #80, install an APK, or claim overall final acceptance until the owner completes the physical test.
- Detailed evidence: `GTVH_HOME_WALLPAPER_TEXT_REFINEMENT_001_AUDIT.md`.

**Next action:** owner visual/physical acceptance on Mi TV Stick; if corrections are required, keep changes strictly inside the approved five-component scope.


## 12. Owner-approved corrective refinement — GTVH-HOME-WALLPAPER-TEXT-REFINEMENT-001 (8 November 2026)

**Current status: SOURCE CHANGES COMMITTED · EXACT-HEAD CI PENDING · RENDERED/DEVICE ACCEPTANCE PENDING · NOT FINAL**

This section supersedes the earlier downward 5mm movement and 12dp minimum-gap values recorded in Section 11. The previous CI result applies only to the prior PR #80 HEAD, not to the corrective HEAD below.

- PR: [#80](https://github.com/Frangga11/glass-tv-home/pull/80), OPEN / DRAFT / UNMERGED.
- Source branch: `fix/home-wallpaper-text-refinement-001`.
- Corrective HEAD: `a085be150ebcf1d48b825ed7ac43b172261f3aa0`.
- Base branch / SHA: `fix/home-balanced-frame-001` / `b4abdf0bd555a0106c135add20c3f287cf10b412`.
- Changed files remain limited to two production files and one scoped regression test. The PR's existing workflow file remains part of the inherited PR diff; this corrective pass did not alter it.

### Corrective visual geometry
- The prior implementation moved the Home information shelves downward by 5mm (31.5dp at density 2.0), leaving only about 4.5dp of visual clearance above the tray. This is superseded.
- New wallpaper title/description baseline uses a 3.5dp offset against the existing 50dp Dock-relative geometry, giving a nominal **32.5dp** clearance above the tray at 320dpi. A **32dp minimum** clamp protects other density/layout cases. This moves the wallpaper shelf upward about **28dp** from the prior PR #80 rendering; the small difference from the original 24dp target is intentional to honor the approved 32–40dp Dock clearance.
- Daily Ayah parent shelf is offset so the Ayah block rises **18dp** from the prior PR #80 position.
- Prayer header and table each receive an additional **6dp** upward translation, so the timetable rises **24dp** total.
- No Dock geometry, safe-frame, grid, header or capsule measurements were changed.

### Text clarity correction
- Prayer header now uses solid high-contrast light text. Prayer names/times use solid white/warm active-prayer colors. Removed the per-glyph text gradient shader and runtime `Typeface.DEFAULT` overrides that had bypassed the XML Inter typeface.
- Prayer names across all six columns: **13sp**; times across all six columns: **17sp**. Existing timetable data, order, provider and activation remain unchanged.
- Daily Arabic: **20sp at 1080p / 22sp at 4K**, preserving Amiri Quran font, RTL direction and high-quality line breaking.
- Translation: **14sp at 1080p / 15sp at 4K**.
- Added a subtle transparent-to-dark gradient scrim behind the right information shelf; no bordered panel or idle frame was added. Existing focus-only feedback and click/DPAD activation remain.

### Verification boundary- Exact-head workflow lookup did not yet return a run for corrective HEAD `a085be1`; exact-head CI is **PENDING**, not PASS.
- Prior HEAD `c7d95c3` had CI #1592 / run `42493469262` PASS (166 Python tests, 1 skipped; Android release unit tests and assembleRelease PASS). That evidence does not validate the corrective commits.
- Rendered 1920×1080 review and physical Mi TV Stick acceptance remain PENDING.
- No APK installation, ADB session, device setting change, PR merge or force-push was performed.
- Explicitly protected: Dock/Grid geometry/order/scroll/focus, Header/navigation/capsules, wallpaper image/crop/provider/rotation, prayer data/provider/order, safe frame and unrelated components.

**Next action:** monitor exact-head CI run `42497894475` (#1599) for HEAD `a085be1`; then inspect the rendered 1920×1080 composition and wait for owner Mi TV Stick visual acceptance. Do not merge or install automatically.


## 13. Exact-head CI completed — GTVH-HOME-WALLPAPER-TEXT-REFINEMENT-001 (8 Nov 2026)

**Status: SOURCE PASS · EXACT-HEAD CI PASS · RENDERED / PHYSICAL ACCEPTANCE PENDING · NOT FINAL**

- PR #80 remains OPEN / DRAFT / UNMERGED. Current source HEAD: `a085be150ebcf1d48b825ed7ac43b172261f3aa0`; base PR #79 SHA `b4abdf0bd555a0106c135add20c3f287cf10b412`.
- Exact-head CI #1599 / run `42497894475`: **SUCCESS**. Both jobs succeeded: `Launcher build and unit tests` and `Lists, tools and docs`.
- Python regression suite: **167 tests, OK (1 skipped)**. All six wallpaper-text refinement contracts passed, including explicit checks for all six prayer labels at 13sp and all six prayer times at 17sp.
- Android command `./gradlew --no-daemon testReleaseUnitTest assembleRelease`: **BUILD SUCCESSFUL in 1m 18s**. Both `:app:testReleaseUnitTest`, `:installer:testReleaseUnitTest`, and both release assemble tasks passed.
- Release APK diagnostic size: **1,627,685 bytes**. Historical 1.5 MiB warning is non-blocking; no release APK was published. The separate publisher workflow #54 was SKIPPED by design for PR #80.
- CI generated visual-parity artifact `glass-tv-home-dock-visual-parity-4e99e1ba497ae2724c46abfdff28087f484679cb`, artifact ID `13624193020`, ZIP size `1,426,812` bytes, expires 15 Nov 2026. This is a workflow artifact ZIP, not a direct APK release asset; do not present it as a direct APK download.
- PR total diff has 4 files: CI workflow, MainActivity.java, activity_main.xml, and the scoped regression test. The CI workflow adjustment enables the Home layout visual-parity artifact for PR #80; the corrective application edits remain limited to MainActivity.java and activity_main.xml, plus the focused test. No DockGeometry/Dock/Grid/Header/capsule/navigation/safe-frame/wallpaper provider/prayer data code was edited.
- No rendered 1920×1080 screenshot has been independently reviewed and no physical Mi TV Stick test, ADB session, installation, device setting change, or merge was performed.

**Next action:** inspect rendered 1920×1080 visual output and owner acceptance on Mi TV Stick. Keep PR #80 draft and unmerged until physical review passes. No APK release delivery yet.


## 14. PR #80 APK build package retrieved — 8 November 2026

- Built from exact validated PR #80 HEAD `a085be150ebcf1d48b825ed7ac43b172261f3aa0` by CI #1599 / run `42497894475`.
- Release APK successfully assembled and retrieved from Actions artifact ID `13624193020` (artifact ZIP contained exactly `app-release.apk`).
- APK size: **1,627,685 bytes**. SHA-256: `67ecc3fddc6b4bab6c36025c6d285973fe329591eabe9f29c108fbc49d8fe602`.
- User-facing package: `Glass-TV-Home-PR80-a085be1.apk` (extracted from CI artifact and verified by file size/hash). The APK is the release build from the validated source HEAD; it is not a merged/final release.
- CI and build pass. Physical Mi TV Stick installation/acceptance remains pending and is the next step. PR #80 remains OPEN / DRAFT / UNMERGED; no device install, ADB or merge performed by assistant.


## 15. GTVH-HOME-DOCK-ISLAMIC-SHELF-NAVIGATION-REFINEMENT-001 — 8 November 2026

**Status: SOURCE PASS · EXACT-HEAD CI PASS · RELEASE APK EXTRACTED/HASHED · VISUAL AND PHYSICAL ACCEPTANCE PENDING · NOT FINAL**

- Dedicated audit: [GTVH_HOME_DOCK_ISLAMIC_SHELF_NAVIGATION_REFINEMENT_001_AUDIT.md](GTVH_HOME_DOCK_ISLAMIC_SHELF_NAVIGATION_REFINEMENT_001_AUDIT.md).
- PR #81: OPEN / DRAFT / UNMERGED. Branch: `fix/home-dock-islamic-shelf-navigation-001`.
- Exact source HEAD: `2130675f1f2723f8e7c895cbc6ac63327d2efd60`.
- Base: PR #80 exact HEAD `a085be150ebcf1d48b825ed7ac43b172261f3aa0`. PR #80 remains OPEN / DRAFT / UNMERGED and was not modified.
- Eight changed files, limited to DockAdapter, DockDecoration, DockGeometry, MainActivity, scoped tests, and PR #81 CI artifact eligibility.
- Dock scale 0.95; default five-slot Mi TV Stick card geometry 287×161px ≈143.5×80.5dp (target 144×81dp). Dock-only icon 44dp; Grid remains on original AppAdapter geometry and 48dp XML icon.
- Total horizontal Dock host height reduced approximately 8dp by card-height reduction plus spare-height reduction 18dp→14dp. Horizontal decoration offsets coordinated to 6dp/8dp; vertical offsets remain 6dp/12dp. Tray remains full width.
- Islamic shelf is anchored independently of the focused card; its bottom edge is preserved through content-height changes. Fade visibility remains separate from position.
- Horizontal Grid DPAD row motion uses signed smooth scrolling, pending focus targets and generation guards; DOWN/UP direction reversal, rapid input, final partial row and pause/resume cancellation have regression coverage.
- Exact-head CI #1620 / run `42501726878`: **SUCCESS** on exact HEAD. Python: **176 tests, OK (1 skipped)**. Android `testReleaseUnitTest` (app and installer) and `assembleRelease`: **BUILD SUCCESSFUL in 1m 19s**, 99 actionable tasks. Both CI jobs and APK artifact upload passed.
- Release APK: **1,629,469 bytes**; SHA-256 `caa3289641184e516b5758b8ce647d7bc33616ee55cb5f022576e53add572e24`. CI artifact ZIP ID `13625911589`, size 1,428,433 bytes, SHA-256 `e9dbc0307aa7823d019ccf464e21e633f6dfe1a3a5906407feee3ada6fa1ea41`. APK extracted and verified; publisher workflow skipped by design.
- No rendered 1920×1080 visual review, physical Mi TV Stick test, ADB, installation, device setting change or merge was performed.
- **Next action:** owner tests exact-head APK on Mi TV Stick using all required remote/focus/panel/capacity scenarios. Keep PR #81 DRAFT and unmerged until physical acceptance.


---

## 16. GTVH-HOME-ISLAMIC-SHELF-VERTICAL-POSITION-AND-GRID-ROW-SCROLL-FIX-002 — 9 November 2026

**Status: SOURCE PASS · EXACT-HEAD CI PASS · RELEASE APK PUBLISHED · PHYSICAL ACCEPTANCE PENDING · NOT FINAL**

- **Current PR:** [PR #82](https://github.com/Frangga11/glass-tv-home/pull/82), OPEN / DRAFT / UNMERGED.
- **Branch:** `fix/home-islamic-shelf-vertical-position-grid-row-scroll-002`.
- **Exact source HEAD:** `d6f594933b712b36b0660e119a0d4ee8b7de3134`.
- **Base:** `fix/home-dock-islamic-shelf-navigation-001`, current tip `3cd36b8a5fbc788a7b15e178e4aaf0606e919c61` (PR #81). PR #80 remains OPEN / DRAFT / UNMERGED at `a085be150ebcf1d48b825ed7ac43b172261f3aa0`; PR #81 remains OPEN / DRAFT / UNMERGED. PR #81 application source was not changed for this follow-up; only workflow eligibility was extended to support exact-head PR #82 artifact and direct APK delivery.
- **A — Islamic package:** horizontal Glass tray top is measured from laid-out Dock card coordinates and DockDecoration's tray inset in root coordinates. Islamic package lower edge targets exactly 27.5dp (55px at density 2.0) above that visual tray edge. Existing 10dp internal hero/Islamic shelf relationship and content layout are preserved. Runtime/device confirmation is pending.
- **B — Grid:** previous raw child-height smooth scroll replaced with decorated row-pitch geometry and a cancellable ValueAnimator driving actual RecyclerView.scrollBy() content movement. Expanded horizontal viewport clips to its padded content area, destination row is aligned by decorated top, focus resolves by stable app key, stale animations are invalidated, and transition/focus settlement has no scrollToPositionWithOffset jump fallback.
- **C — typography:** prayer and daily-doa headings Inter weight 700; six localized prayer-name labels uppercase and weight 650. Existing 13sp/17sp text sizes, font family, colors, positions, prayer data, Arabic/RTL and translations are unchanged.
- **Changed PR #82 files (5):** `DockGeometry.java`, `MainActivity.java`, `activity_main.xml`, `DockLayoutModelTest.java`, and `test_home_dock_islamic_shelf_navigation_refinement_contract.py`. CI and publisher workflow support is inherited from PR #81's updated base branch.
- **Exact-head CI #1630 / run `42505427017`: SUCCESS**, both jobs passed. Python **177 tests, OK (1 skipped)**. Android app/installer Release unit tests and Release assemble passed; **BUILD SUCCESSFUL in 1m37s**.
- **Exact-head publisher #85 / run `42505426954`: SUCCESS.** Release APK size **1,630,737 bytes**; SHA-256 `524963af6aad72444d38e1cd896eac25d29be05cac9e765b8819e0f69dcba735`.
- **Direct APK:** [Glass-TV-Home-PR82-d6f594933b712b36b0660e119a0d4ee8b7de3134.apk](https://github.com/Frangga11/glass-tv-home/releases/download/glass-tv-home-pr82-d6f594933b712b36b0660e119a0d4ee8b7de3134/Glass-TV-Home-PR82-d6f594933b712b36b0660e119a0d4ee8b7de3134.apk).
- CI emitted the historical 1.5 MiB APK-size reference warning; it is explicitly non-blocking. The APK is 1,268 bytes larger than PR #81's 1,629,469-byte APK; continue monitoring the size delta.
- CI visual-parity artifact ZIP ID `13627586269`, size 1,429,683 bytes, SHA-256 `3b081cde3d8fcfd5da101d5300a772bf2e6ca2bbc0371606e39e804603c62584`; this ZIP is not the direct APK asset.
- An initial compile attempt caught a missing pending app-key field declaration; it was corrected and guarded by a regression assertion. Final exact-head CI and APK publisher both pass.
- **Physical acceptance remains mandatory:** Mi TV Stick 1920×1080 / density320 measurement of 27.5dp, package stability during focus changes, DOWN/UP opposite real row motion, repeated and rapid reversals, incomplete last row, app-count change, return-to-Home focus, Dock capacities 4/5/6, typography clipping/overlap and locked-component regression.
- No assistant-side physical installation, ADB, device setting change, screenshot review or merge was performed. Keep PR #82 DRAFT / UNMERGED until owner confirms the physical tests.
- Detailed audit: [GTVH-HOME-ISLAMIC-SHELF-VERTICAL-POSITION-AND-GRID-ROW-SCROLL-FIX-002-AUDIT.md](GTVH-HOME-ISLAMIC-SHELF-VERTICAL-POSITION-AND-GRID-ROW-SCROLL-FIX-002-AUDIT.md).
- **Next action:** owner installs the exact-head APK on Mi TV Stick and reports the measured gap and remote-navigation result. Do not declare final or merge before physical acceptance.


### Session closure — 9 November 2026

**Work session status: CLOSED AT CHECKPOINT.** The FIX-002 source implementation, exact-head automated validation, APK publication and documentation sync are complete. This closure does **not** mean physical acceptance or PR merge.

- Resume from PR #82 exact HEAD `d6f594933b712b36b0660e119a0d4ee8b7de3134` on `fix/home-islamic-shelf-vertical-position-grid-row-scroll-002`.
- Resume documentation from `docs/project-management/GTVH-CHECKPOINT-LATEST.md` on `docs/project-master-inventory`.
- First next action: install the exact-head APK on the Mi TV Stick and perform the physical acceptance checklist above. Record observed 27.5dp clearance and actual DOWN/UP row movement before changing PR status.
- Leave PR #80, #81 and #82 OPEN / DRAFT / UNMERGED. Do not merge or claim final acceptance without physical evidence.
- No further source changes are authorized by this checkpoint; any discrepancy from device testing must be recorded first, then handled as a separately scoped corrective change.

---

## 17. Latest checkpoint — GTVH-HOME-UI-PROPORTION-GLASS-AND-FOCUS-REFINEMENT-003 — 9 November 2026

**Workstream status: IMPLEMENTATION COMPLETE / EXACT-HEAD CI PASS / RELEASE APK PUBLISHED / PHYSICAL ACCEPTANCE PENDING. NOT FINAL.**

### Source and PR authority
- Repository: `Frangga11/glass-tv-home`.
- Source PR: [#83 — GTVH-HOME-UI-PROPORTION-GLASS-AND-FOCUS-REFINEMENT-003](https://github.com/Frangga11/glass-tv-home/pull/83).
- PR #83: OPEN / DRAFT / UNMERGED; mergeable at last verification.
- Source branch: `fix/home-ui-proportion-glass-focus-refinement-003`.
- Exact source HEAD: `947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803`.
- Base branch: `fix/home-islamic-shelf-vertical-position-grid-row-scroll-002`.
- Exact base HEAD: `d6f594933b712b36b0660e119a0d4ee8b7de3134`; PR #82 remains unmerged.
- PR #80, #81 and #82 were not merged or rewritten. No merge was performed for PR #83.
- PR #83 is 27 commits ahead of its exact base and changes 20 files.

### Completed implementation A–G
- **A — Manual updates:** automatic launch/resume update checks and auto-update scheduling controls removed; non-manual check cannot display update prompt; Settings red badge removed. Manual check, version comparison, APK download, install permission and PackageInstaller pipeline retained.
- **B — Navigation proportions:** capsule height 40dp → 36dp; inner padding 3dp → 2dp; content remains centered.
- **C — Icon/text spacing:** Beranda, Profil and Pengaturan drawable spacing 5dp → 4dp.
- **D — Glass Dock:** horizontal Dock extra host height 14dp → 12dp; horizontal Dock icons use optical scaleX 1.045; 44dp Dock icon bounds and 48dp Grid icon bounds retained.
- **E — Islamic Shelf:** 1dp / 180dp Daily Doa divider placed inside the visible Doa block; scrim gradient changed from 0x1F/0x44 to 0x12/0x26.
- **F — Focus/glass:** all header pills share 20dp GlassDrawable radius and one Glider focus indicator; duplicate focused foreground state removed from navigation selector; selected Beranda tint retained.
- **G — Quality:** Dock geometry unit test, wallpaper scrim and localization contracts updated; dedicated UI refinement regression contract added; CI/publisher workflow eligibility extended to PR #83.

### Exact-head validation and APK
- CI #1637 / run `42509424284`: **SUCCESS**, both jobs passed.
- Python suite: **184 tests, OK (1 skipped)**.
- Android app and installer `testReleaseUnitTest`: PASS.
- Android `assembleRelease`: PASS; `BUILD SUCCESSFUL`.
- Direct APK Publisher #92 / run `42509424259`: **SUCCESS**; exact PR HEAD checked out, Release tests/build passed, APK verified and direct asset uploaded.
- APK: `Glass-TV-Home-PR83-947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803.apk`.
- Exact APK size: **1,625,213 bytes**.
- SHA-256: `2d14894da872f616188bace686be5f85021766914bbe65260cc76a7848baee26`.
- [Download / view PR #83 QA APK release](https://github.com/Frangga11/glass-tv-home/releases/tag/glass-tv-home-pr83-947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803).
- CI validation artifact ID `13628113675`; ZIP size 1,427,011 bytes; ZIP SHA-256 `7caf8c43502a4db15c61a098bf1d55559ecf6a0fdf2f8eb73eeaecf0d32dbd08`; expires 15 November 2026.
- Historical 1.5 MiB warning is non-blocking. PR #83 APK is 5,524 bytes smaller than PR #82's 1,630,737-byte APK, but remains 52,349 bytes above the 1.5 MiB diagnostic reference.

### Changed source/test/workflow files
- `.github/workflows/ci.yml`
- `.github/workflows/publish-apk-release.yml`
- `launcher/app/src/main/java/com/gathin/tvhome/AppUpdateManager.java`
- `launcher/app/src/main/java/com/gathin/tvhome/DockAdapter.java`
- `launcher/app/src/main/java/com/gathin/tvhome/DockGeometry.java`
- `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java`
- `launcher/app/src/main/java/com/gathin/tvhome/ModernSettingsDialog.java`
- `launcher/app/src/main/res/drawable/header_glass_pill_bg.xml`
- `launcher/app/src/main/res/drawable/header_nav_tab_bg.xml`
- `launcher/app/src/main/res/drawable/pill_bg.xml`
- `launcher/app/src/main/res/layout/activity_main.xml`
- `launcher/app/src/main/res/layout/dialog_ota_coffee.xml`
- `launcher/app/src/main/res/values-en/strings.xml`
- `launcher/app/src/main/res/values-id/strings.xml`
- `launcher/app/src/main/res/values/strings.xml`
- `launcher/app/src/test/java/com/gathin/tvhome/DockLayoutModelTest.java`
- `tests/test_f06_localization_ui_contract.py`
- `tests/test_home_dock_islamic_shelf_navigation_refinement_contract.py`
- `tests/test_home_ui_proportion_glass_focus_refinement_contract.py`
- `tests/test_home_wallpaper_text_refinement_contract.py`

### Physical acceptance — mandatory outstanding gate
No physical Mi TV Stick installation, ADB session, screenshot capture, device setting change or remote-control acceptance has been performed. The exact-head APK is built and published, but visual/runtime acceptance remains the owner's next action.

Test on Mi TV Stick at **1920×1080 / density 320**:
1. Confirm no update reminder on cold launch or resume and no Settings red dot.
2. Open Settings and manually check for updates; verify latest-version feedback and, if an update is available, download/permission/PackageInstaller handoff.
3. Check 36dp navigation capsule, 2dp padding, 4dp icon/text spacing, centering and no clipping.
4. Check Dock height, 1.045 icon optical width, alignment, focus and animation.
5. Check prayer/Doa divider and wallpaper visibility with the softened gradient.
6. Navigate Location, Weather, Beranda, Profil, Pengaturan, Wi-Fi and Microphone using the remote; inspect normal, focused, pressed and disabled states where applicable.
7. Run regression checks for Home components, app launch/return focus, Grid navigation, Wi-Fi, Assistant, prayer data and Islamic Shelf visibility.

### Final decision / next action
**BELUM FINAL.** Source implementation, automated tests, Android Release build, APK size/hash verification and direct APK publication are complete. Physical acceptance remains pending. Owner installs the exact-head APK and reports screenshots/observations; only then should any discrepancy be triaged. Keep PR #83 OPEN / DRAFT / UNMERGED. Do not merge or mark final without physical acceptance.

Detailed audit: [GTVH-HOME-UI-PROPORTION-GLASS-AND-FOCUS-REFINEMENT-003-AUDIT.md](GTVH-HOME-UI-PROPORTION-GLASS-AND-FOCUS-REFINEMENT-003-AUDIT.md).

---

## Pre-merge integration audit — user request to merge PR #77–#83 — 9 November 2026

**Decision status: CHECKPOINT CREATED / NO MERGE EXECUTED / TARGET-CHAIN CLARIFICATION REQUIRED.**

### User request
The owner requested a checkpoint and asked to merge PR #77 through PR #83. This section records the live GitHub state before taking merge actions. It does not authorize changing base branches, bypassing dependencies, or merging a superseded parallel implementation without reconciliation.

### Live PR state and exact refs
| PR | State | Head branch / exact HEAD | Base branch / base SHA | Exact-head CI |
|---|---|---|---|---|
| #77 | OPEN / DRAFT | `fix/home-layout-consolidated-001` / `7e51a86cc83394ae47f7c9d32c22bf0c6042c9d1` | `feature/g4-parent-pin-hardening` / `7146de58953325fdfc6c98501828676f143e64cf` | CI #1591 SUCCESS |
| #78 | OPEN / DRAFT | `feature/h01-header-reskin` / `aca3c6b8a9cfb6f30410631bfd6e178a74be2b23` | `main` / `23598f8b5202ed71fc38e8c8da5ed2eeaeac535b` | CI #1500 SUCCESS |
| #79 | OPEN / DRAFT | `fix/home-balanced-frame-001` / `b4abdf0bd555a0106c135add20c3f287cf10b412` | `fix/home-layout-consolidated-001` / recorded base SHA `0aeb3851ddce5deb47e3c5f96db44631b0343577` | CI #1592 SUCCESS |
| #80 | OPEN / DRAFT | `fix/home-wallpaper-text-refinement-001` / `a085be150ebcf1d48b825ed7ac43b172261f3aa0` | `fix/home-balanced-frame-001` / `b4abdf0bd555a0106c135add20c3f287cf10b412` | CI #1599 SUCCESS |
| #81 | OPEN / DRAFT | `fix/home-dock-islamic-shelf-navigation-001` / `3cd36b8a5fbc788a7b15e178e4aaf0606e919c61` | `fix/home-wallpaper-text-refinement-001` / `a085be150ebcf1d48b825ed7ac43b172261f3aa0` | CI #1628 SUCCESS |
| #82 | OPEN / DRAFT | `fix/home-islamic-shelf-vertical-position-grid-row-scroll-002` / `d6f594933b712b36b0660e119a0d4ee8b7de3134` | `fix/home-dock-islamic-shelf-navigation-001` / `3cd36b8a5fbc788a7b15e178e4aaf0606e919c61` | CI #1630 SUCCESS |
| #83 | OPEN / DRAFT | `fix/home-ui-proportion-glass-focus-refinement-003` / `947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803` | `fix/home-islamic-shelf-vertical-position-grid-row-scroll-002` / `d6f594933b712b36b0660e119a0d4ee8b7de3134` | CI #1637 SUCCESS |

### Dependency findings
1. PR #77 is not based on `main`; it targets `feature/g4-parent-pin-hardening`, which is the head branch of PR #72.
2. PR #72 is OPEN / DRAFT / UNMERGED and itself targets `feature/g3f-global-settings`, the head branch of PR #71.
3. PR #71 is OPEN / DRAFT / UNMERGED and targets `feature/g3e-wifi`, the head branch of PR #70. PR #70 is also OPEN / DRAFT / UNMERGED and targets `feature/g3d-quick-settings`.
4. Therefore merging #77–#83 only into their configured stacked bases will not integrate the full stack into `main`. A mainline integration needs an explicit decision about the upstream #70–#72 dependencies and their own parent chain.
5. PR #79 is stacked on #77, and PRs #80 → #81 → #82 → #83 form a sequential chain. Comparing current #77 head to #79 head reports **117 ahead / 5 behind**, merge-base `0aeb3851ddce5deb47e3c5f96db44631b0343577`; despite GitHub's `mergeable=true`, the ancestry discrepancy needs deliberate reconciliation before merge.
6. PR #78 is a separate branch directly from old `main`, and its header implementation overlaps eight source/resource files changed by PR #79. PR #79's own reconciliation notes explicitly state that PR #78 is based on the old main baseline and is not a valid candidate for the newer G3A–G4 / PR #77 Home layout chain. Do not merge #78 alongside #79 without a deliberate source reconciliation; doing so risks reintroducing old header implementations over the newer integrated Home.
7. All seven PRs are currently OPEN / DRAFT and GitHub reports mergeable metadata, but that does not resolve the dependency/ancestry issues above. Exact-head CI success is recorded as listed; physical Mi TV Stick acceptance remains pending on the Home visual/focus changes.

### Recommended safe route
- Treat PR #78 as a superseded parallel candidate unless the owner explicitly requests a separate mainline header merge after reconciliation.
- Preserve the active stack #77 → #79 → #80 → #81 → #82 → #83.
- First decide whether the target is (A) each PR's configured parent branch, leaving the upstream #70–#72 chain unmerged, or (B) full integration to `main`, which requires resolving and merging the prerequisite #70–#72 chain (and their parents) plus reconciling #77/#79 ancestry.
- Do not force-push, retarget base branches, or merge PR #78 into main as a substitute for the current integrated stack.
- The owner's merge request is explicit, but the target topology and treatment of superseded PR #78 are not yet specified. No merge has been executed in this checkpoint.

### Physical acceptance boundary
Prior project checkpoints require Mi TV Stick physical acceptance before final merge. The latest PR #83 Release APK exists and CI passes, but no physical device acceptance is recorded. Owner's latest instruction asks to merge; this is recorded as a change of requested action, but the physical acceptance gap remains visible and must not be misreported as passed.

### Current decision
**WAITING FOR ONE OWNER DECISION:** Should I merge only the active stacked chain into each PR's configured parent branch and skip superseded #78, or should I integrate the entire dependency chain into `main` (including prerequisite PRs #70–#72, with #78 reconciled separately)?


---

## Owner decision update — PR #78 removed from integration — 9 November 2026

- Owner confirmed PR #78 was created in error and instructed that work focus on the remaining PRs.
- PR #78 [closed on GitHub](https://github.com/Frangga11/glass-tv-home/pull/78); verified state CLOSED, merged=false. Its description records that it must not be merged or revived. GitHub does not provide permanent deletion of an existing PR; closing is the correct disposition.
- PR #78 is excluded from all future merge plans and active Home stack calculations. Its old-main overlap with #79 is no longer an unresolved integration choice.
- Remaining Home path under review: #77 → #79 → #80 → #81 → #82 → #83. This does not itself resolve the upstream #70–#72 prerequisites or the #77/#79 ancestry discrepancy (117 ahead / 5 behind from the recorded merge base).
- No PR among #77, #79–#83 has been merged by this action. Before merging, re-fetch live PR refs, verify exact-head CI/reviews/unresolved threads and reconcile #77/#79 ancestry. Physical Mi TV Stick acceptance remains pending and must be explicitly recorded if the owner chooses to override that gate.
- This section supersedes earlier checkpoint text that described PR #78 as awaiting owner disposition or possible reconciliation. PR #78 is CLOSED / EXCLUDED.


---

## 18. GTVH-HOME-ISLAMIC-SHELF-SINGLE-ANCHOR-FIX-004 — 9 November 2026

**Status: SOURCE FIX COMMITTED / EXACT-HEAD CI PASS / PHYSICAL ACCEPTANCE PENDING / NOT FINAL.**

- PR [#84](https://github.com/Frangga11/glass-tv-home/pull/84): OPEN / DRAFT / UNMERGED.
- Branch: `fix/home-islamic-shelf-single-anchor-004`.
- Exact current HEAD: `d821842c2211cb6e65bbde506253cde7b3929771`.
- Base: PR #83 branch `fix/home-ui-proportion-glass-focus-refinement-003`, exact base HEAD `947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803`.
- Root cause confirmed: `MainActivity` had two layout listeners writing the Islamic Shelf Y position from different baselines. The retained listener used `islamicShelfBottomAnchor`; a legacy listener used `heroBottom`. In horizontal mode, those baselines differ by the 10dp internal shelf offset. Interleaving layout and focus callbacks could therefore overwrite the measured anchor and create a vertical jump.
- Corrective change: removed only the legacy Islamic Shelf listener. Retained the one listener calling `applyIslamicShelfPosition()`, the same anchor application in `showHero()`, measured tray-edge positioning, and the exact 27.5dp gap. `heroBottom` remains in use for the independent wallpaper `heroShelf`.
- Added regression coverage requiring exactly one Islamic Shelf layout listener and forbidding `heroBottom` in that listener.
- Scope preserved: no Dock geometry, focus navigation, wallpaper safe frame, prayer calculation/data source, WeeklyAyah/Quran content, typography, Glider, or glass styling changes.
- CI #1639 / run `42523095371` was IN_PROGRESS when recorded. Do not claim CI pass until exact-head results are available.
- No physical Mi TV Stick install, ADB measurement, screenshot review, or remote acceptance performed.
- **Next:** verify exact-head CI, then physical test on Mi TV Stick at 1920×1080 / density 320. Confirm the Islamic Shelf bottom anchor and 27.5dp (55px) clearance remain unchanged through repeated focus transitions, ayah remeasurement, and Dock capacities 4/5/6. Keep PR #84 DRAFT / UNMERGED until physical acceptance.
- Detailed audit: [GTVH-HOME-ISLAMIC-SHELF-SINGLE-ANCHOR-FIX-004-AUDIT.md](GTVH-HOME-ISLAMIC-SHELF-SINGLE-ANCHOR-FIX-004-AUDIT.md).


### CI progress update — exact HEAD d821842c2211cb6e65bbde506253cde7b3929771
- CI #1639: both jobs **SUCCESS**. Python suite **184 tests, 1 skipped**; Android `testReleaseUnitTest` and `assembleRelease` **PASS**.
- Release APK diagnostic: **1,625,313 bytes**. Publisher not run for this PR checkpoint.
- Physical Mi TV Stick acceptance remains pending.


---

## 19. GTVH-HOME-ISLAMIC-SHELF-ARABIC-TYPOGRAPHY-001 — 9 November 2026

**Status: SOURCE CHANGE COMMITTED / EXACT-HEAD CI PASS / PHYSICAL VISUAL ACCEPTANCE PENDING / NOT FINAL.**

- PR [#85](https://github.com/Frangga11/glass-tv-home/pull/85): OPEN / DRAFT / UNMERGED.
- Branch: `fix/home-islamic-shelf-arabic-typography-001`.
- Exact branch HEAD at checkpoint: `a5eaae843f8998c53238870609102e4c20b05ded`.
- Base: PR #84 branch `fix/home-islamic-shelf-single-anchor-004`, exact base HEAD `d821842c2211cb6e65bbde506253cde7b3929771`.
- Updated only the `ayahArabic` TextView in `activity_main.xml`: warm yellow-orange `#FFFFC45A`; line spacing `-1dp` and multiplier `0.94`; retained Amiri Quran font, 20sp size, font padding, two-line cap, and ellipsize behavior; applied soft warm shadow `#CC3A1800` with dx 0, dy 2, radius 3.
- Translation text, label, Islamic Shelf geometry, prayer-time table, Dock, focus navigation, and wallpaper safe frame are unchanged.
- Added `tests/test_home_islamic_shelf_arabic_typography_contract.py` to lock color, line spacing, glyph padding, shadow, and translation-style isolation.
- CI #1640 / run `42523437010` on exact HEAD: **SUCCESS**; Python suite **188 tests, 1 skipped**; Android `testReleaseUnitTest` and `assembleRelease` **PASS**.
- Release APK diagnostic: **1,625,337 bytes**. Publisher workflow skipped because PR #85 remains Draft; no release asset was published by this run.
- Physical Mi TV Stick visual acceptance is pending. Must inspect both one-line and two-line Arabic text on bright/dark wallpapers and confirm that Arabic glyphs and harakat are not clipped or colliding.
- Keep PR #85 DRAFT / UNMERGED until physical visual acceptance passes.
- Detailed audit: [GTVH-HOME-ISLAMIC-SHELF-ARABIC-TYPOGRAPHY-001-AUDIT.md](GTVH-HOME-ISLAMIC-SHELF-ARABIC-TYPOGRAPHY-001-AUDIT.md).


---

## 20. GTVH-HOME-PRAYER-TIMES-REFRESH-AND-NEXT-SUBUH-FIX-001 — 9 November 2026

**Status: IMPLEMENTATION COMMITTED / EXACT-HEAD CI PASS / PHYSICAL ACCEPTANCE PENDING / NOT FINAL.**

- PR [#86](https://github.com/Frangga11/glass-tv-home/pull/86): OPEN / DRAFT / UNMERGED.
- Branch: `fix/prayer-times-refresh-next-subuh-001`.
- Exact current HEAD: `746beaba2648b167dc174dfd3a61f5e859ed9fc2`.
- Base: PR #85 branch `fix/home-islamic-shelf-arabic-typography-001`, exact base HEAD `a5eaae843f8998c53238870609102e4c20b05ded`.
- Five commits ahead / zero behind from the exact base. Four files changed: `PrayerTimes.java`, `MainActivity.java`, a new Android unit test, and a Python regression contract.
- Fixes active refresh checks on Home resume and minute tick; prevents overlapping sync jobs and adds a six-hour retry cooldown keyed by active zone and month; centralizes next-prayer selection; after Isya, the Home prayer row, compact display, and full schedule use tomorrow's Subuh time instead of highlighting today's elapsed Subuh.
- Provider URL, Kemenag validation, astronomical fallback parameters, timezone rules, shelf geometry, Arabic styling and WeeklyAyah data remain unchanged.
- Android unit tests cover skipping sunrise, selection before/after prayer times, no same-day prayer after Isya, tomorrow's Subuh and fallback timestamp. Python contracts cover sync guard/cooldown, resume/minute refresh and view consistency.
- CI #1642 / run `42524091486`: **SUCCESS** on exact HEAD `746beaba2648b167dc174dfd3a61f5e859ed9fc2`.
- Python suite: **192 tests, 1 skipped**; four new regression contracts passed. Android `testReleaseUnitTest` and `assembleRelease`: **PASS / BUILD SUCCESSFUL**.
- Release APK diagnostic: **1,625,865 bytes**. Publisher workflow #97 skipped because PR #86 is Draft; no APK release asset is claimed.
- Physical Mi TV Stick acceptance pending: validate minute/month boundary refresh, offline retry behavior, after-Isya Subuh row and consistency between compact Home timetable and full schedule.
- Keep PR #86 DRAFT / UNMERGED. No merge or physical device test has been performed.


### Detailed audit — PR #86
- [GTVH-HOME-PRAYER-TIMES-REFRESH-AND-NEXT-SUBUH-FIX-001-AUDIT.md](GTVH-HOME-PRAYER-TIMES-REFRESH-AND-NEXT-SUBUH-FIX-001-AUDIT.md) records the verified source findings, implementation, tests and outstanding acceptance gates.
- At checkpoint update, exact-head CI #1642 / run `42524091486` is still running; Python/docs job has passed, Android job remains in progress. Publisher workflow skipped because PR #86 is Draft.


---

## 21. GTVH-HOME-DAILY-AYAH-CACHE-STATUS-AND-LABEL-FIX-001 — 9 November 2026

**Status: IMPLEMENTATION COMMITTED / FIRST CI ATTEMPT FAILED ON TWO TEST ASSERTIONS / CORRECTIVE TEST UPDATES PUSHED / EXACT-HEAD CI PENDING / PHYSICAL ACCEPTANCE PENDING / NOT FINAL.**

- PR [#87](https://github.com/Frangga11/glass-tv-home/pull/87): OPEN / DRAFT / UNMERGED.
- Branch: `fix/daily-ayah-cache-status-label-001`.
- Exact current HEAD: `4c9d049461b217ed20d134f4f430f2b812c383e7`.
- Base: PR #86 branch `fix/prayer-times-refresh-next-subuh-001`, exact base HEAD `746beaba2648b167dc174dfd3a61f5e859ed9fc2`.
- 10 commits ahead / 0 behind; 8 changed files.
- Corrects misleading Doa Harian labels because the current module contains a rotating set of Quran verses only; labels now say Ayat Pilihan Harian / Daily Selected Verse.
- Cache validity now requires current day key, Arabic text and at least one translation rather than every language field. Incomplete API responses are rejected before cache replacement. Translation fallback now tries other available language resources.
- Shelf label distinguishes current content, previous saved content (`TERSIMPAN`) and local fallback (`DATA LOKAL`).
- Cross-audit discovered PR #85 XML Arabic line-spacing values were overridden by a runtime `setLineSpacing(3dp,1.08f)` call. PR #87 removes that runtime override so PR #85 XML settings can control rendering.
- First CI #1644 / run `42524504080` failed the Python/docs job on two test assertion issues (old expected popup title; contract slice included shared optional-language helper). Corrective test changes and a 52-day cache-cycle assertion have since been pushed; new exact-head CI is pending. Android job was still in progress during the first attempt. No CI pass or APK size is claimed.
- Physical Mi TV Stick acceptance remains pending.
- Detailed audit: [GTVH-HOME-DAILY-AYAH-CACHE-STATUS-AND-LABEL-FIX-001-AUDIT.md](GTVH-HOME-DAILY-AYAH-CACHE-STATUS-AND-LABEL-FIX-001-AUDIT.md).


---

## 22. GTVH-HOME-PRAYER-SOURCE-LABEL-TRANSPARENCY-001 — 9 November 2026

**Status: SOURCE CHANGE COMMITTED / CI PENDING / PHYSICAL ACCEPTANCE PENDING / NOT FINAL.**

- PR [#88](https://github.com/Frangga11/glass-tv-home/pull/88): OPEN / DRAFT / UNMERGED.
- Branch: `fix/prayer-source-label-transparency-001`.
- Exact HEAD: `a4b979540def5fc1414e66eec6df516a1ef22fe8`.
- Base: PR #87 branch, exact base HEAD `91f1152361d9b4db70a74f8f6b30ad38f60b4211`.
- Islamic Shelf header now appends a localized Kemenag RI vs local-estimate source label for Indonesian zones; compact prayer summary uses the same zone condition.
- No prayer-time calculation, API, validation or fallback parameters changed.
- Independent current-date cross-reference: Pagar Alam 9 Nov 2026 listed Subuh 04:24, Terbit 06:07, Dzuhur 11:54, Asar 15:14, Maghrib 18:07, Isya 19:14 by a Bimas Islam-sourced calendar. Sajadah documents that its JSON mirrors Kemenag without time adjustment. Direct app JSON retrieval remains unverified in the audit browser; do not treat cross-reference values as proof of the device cache.
- Detailed audit: [GTVH-HOME-PRAYER-SOURCE-LABEL-TRANSPARENCY-001-AUDIT.md](GTVH-HOME-PRAYER-SOURCE-LABEL-TRANSPARENCY-001-AUDIT.md).
- PR #88 exact-head CI #1649 / run `42524805149`: **SUCCESS**; Python suite **201 tests, 1 skipped**, all three source-label contracts passed; Android `testReleaseUnitTest` and `assembleRelease` **PASS / BUILD SUCCESSFUL**; APK diagnostic **1,626,381 bytes**. Publisher skipped because PR #88 is Draft.
- Physical Mi TV Stick visual/source-state acceptance pending.


---

## 23. GTVH-HOME-DAILY-AYAH-OFFLINE-RETRY-BACKOFF-001 — 9 November 2026

**Status: IMPLEMENTATION COMMITTED / EXACT-HEAD CI PASS / PHYSICAL ACCEPTANCE PENDING / NOT FINAL.**

- PR [#89](https://github.com/Frangga11/glass-tv-home/pull/89): OPEN / DRAFT / UNMERGED.
- Branch: `fix/daily-ayah-offline-retry-backoff-001`.
- Exact HEAD: `4038c08f4825f7bacf965bc6708505d3c8c30ebc`.
- Base: PR #88 branch, exact base HEAD `a4b979540def5fc1414e66eec6df516a1ef22fe8`.
- Adds 30-minute retry cooldown keyed by WIB day, process-wide single-flight fetch guard, and Home preflight to avoid creating worker threads during cooldown.
- New WIB day allows immediate refresh; previous valid cached verse remains visible with stale/local freshness status.
- CI #1649 / run `42525047258`: **SUCCESS**; Python suite **202 tests, 1 skipped**; Android `testReleaseUnitTest` and `assembleRelease` **PASS / BUILD SUCCESSFUL**; APK diagnostic **1,626,745 bytes**. Publisher workflow skipped because PR #89 is Draft.
- Detailed audit: [GTVH-HOME-DAILY-AYAH-OFFLINE-RETRY-BACKOFF-001-AUDIT.md](GTVH-HOME-DAILY-AYAH-OFFLINE-RETRY-BACKOFF-001-AUDIT.md).


---

## 24. Islamic Shelf & Prayer Stack Cross-Audit — 9 November 2026

**Status: SOURCE CROSS-AUDIT PASS / ALL SIX EXACT-HEAD CI RUNS PASS / PHYSICAL DEVICE ACCEPTANCE PENDING / FINAL MERGE BLOCKED.**

- Cross-audited PR #84 → #85 → #86 → #87 → #88 → #89 at their exact tested HEADs.
- All six exact-head CI runs completed successfully: #1639, #1640, #1642, #1647, #1648 (PR #88), #1649 (PR #89). Python suites passed at 184, 188, 192, 198, 201 and 202 tests respectively, each with one skipped. Android `testReleaseUnitTest` and `assembleRelease` passed on all six.
- Latest PR #89 APK diagnostic: 1,626,745 bytes. No APK release asset was published because PRs remain Draft.
- Cross-source checks at PR #89 HEAD: one Islamic Shelf position listener; no runtime Arabic line-spacing override; XML `-1dp / 0.94` retained; tomorrow Subuh shared lookup; prayer sync single-flight; exact WIB epoch-day ayah cache; 30-minute ayah retry cooldown; ayah single-flight; no worker launch during cooldown.
- All six PRs remain OPEN / DRAFT / UNMERGED. Individual mergeable flags do not resolve the known upstream PR #70–#72 dependency chain and PR #77 ancestry discrepancy.
- No further code change recommended in this scope absent a reproducible defect. Separate daily-doa module is explicitly out of scope per user decision.
- Physical Mi TV Stick acceptance is still mandatory before marking final or planning merges.
- Detailed record: [GTVH-HOME-ISLAMIC-SHELF-AND-PRAYER-STACK-CROSS-AUDIT-001.md](GTVH-HOME-ISLAMIC-SHELF-AND-PRAYER-STACK-CROSS-AUDIT-001.md).

---

## 25. GTVH-HOME-ISLAMIC-SHELF-AND-PRAYER-STACK-CHECKPOINT / BUILD — 9 November 2026

**Status: CHECKPOINT UPDATED / EXACT-HEAD RELEASE BUILD VALIDATED / APK FILE NOT EXPORTED BY CI / PHYSICAL ACCEPTANCE PENDING / FINAL MERGE BLOCKED.**

- Scope: consolidated candidate PR #84 → #85 → #86 → #87 → #88 → #89. Separate Daily Doa module remains out of scope; current module stays labelled **Ayat Pilihan Harian / Daily Selected Verse**.
- Build source: PR #89 branch `fix/daily-ayah-offline-retry-backoff-001`.
- Exact source HEAD: `4038c08f4825f7bacf965bc6708505d3c8c30ebc`.
- PR #89 remains OPEN / DRAFT / UNMERGED; no PR was merged, retargeted or force-pushed.
- Exact-head CI #1649 / run `42525047258`: **SUCCESS**.
  - Launcher build and unit tests job: SUCCESS.
  - Lists, tools and docs job: SUCCESS.
  - Android `testReleaseUnitTest` and `assembleRelease`: PASS / BUILD SUCCESSFUL.
  - Python/docs suite: 202 tests, 1 skipped.
  - APK size diagnostic: **1,626,745 bytes**.
- **Artifact limitation:** the CI run reports no downloadable workflow artifacts. The Android job explicitly labels the build as validation-only; APK publishing step was skipped because the PR is Draft. Therefore this run proves that an APK was built successfully in CI, but it does **not** provide an APK file for download or installation. Do not describe the diagnostic byte count as a downloadable APK.
- Publisher workflow run `42525047240`, run number 104: SKIPPED. No release asset is claimed.
- Attempt to clone the public repository into the execution environment was blocked because outbound DNS/network access to GitHub was unavailable. No local build was falsely claimed.
- Prior six-PR cross-audit remains valid: source checks pass and all exact-head CI runs pass. No additional source edits were made during this checkpoint/build request.
- **Acceptance gates still open:** physical Mi TV Stick install and DPAD traversal; Islamic Shelf anchor stability; Arabic one-/two-line rendering and harakat; prayer timetable across day boundaries and after Isya; Kemenag/local-estimate label behavior; offline Ayah cooldown and online recovery.
- **Decision:** keep PRs #84–#89 OPEN / DRAFT / UNMERGED. Automated source/build gate PASS; downloadable APK artifact NOT AVAILABLE from current CI; physical acceptance PENDING; final merge BLOCKED.
- **Next operational step:** provide a controlled APK-export path (CI artifact upload or an authorized non-Draft release workflow), then verify APK SHA-256 and install/test on Mi TV Stick. Do not change PR readiness or merge state merely to bypass the artifact gate.

---

## 26. APK artifact export correction — 9 November 2026

**Status: CI EXPORT FIX COMMITTED / DRAFT PR OPEN / VALIDATION PENDING.**

- Root cause: the existing CI workflow built `app-release.apk`, but its artifact-upload condition only listed PR #74–#83. The latest Islamic Shelf/prayer PRs #84–#89 were excluded, so their successful builds produced diagnostics but no downloadable artifact.
- Created isolated CI-only branch `ci/export-islamic-prayer-stack-apk-001` from PR #89 exact HEAD `4038c08f4825f7bacf965bc6708505d3c8c30ebc`.
- Workflow change commit: `aba99d40b8f95f7d6a81711d0c74576ec340b3b9`. It extends the existing upload allowlist to PRs #84–#90 and preserves the existing artifact name pattern and 7-day retention.
- Draft PR [#90](https://github.com/Frangga11/glass-tv-home/pull/90), base `fix/daily-ayah-offline-retry-backoff-001` (PR #89 source branch), head `ci/export-islamic-prayer-stack-apk-001`. One workflow file changed; no app source/runtime code, signing, dependencies, release workflow, or PR readiness state changed.
- PR #90 is OPEN / DRAFT / UNMERGED. At checkpoint update, its CI run/status had not yet appeared in the Actions query. Do not claim artifact upload or successful export until a run completes and the artifact is visible.
- Expected output: `launcher/app/build/outputs/apk/release/app-release.apk`, uploaded by the existing CI job with 7-day retention.- Once CI completes, verify exact HEAD, both jobs, artifact listing, APK size and SHA-256. Then download the artifact and provide the APK file.
- Keep PR #90 draft/unmerged and do not merge it solely to obtain an artifact. PRs #84–#89 also remain OPEN / DRAFT / UNMERGED.


---

## 27. PR #90 APK export validation and verified download — 9 November 2026

**Status: EXACT-HEAD CI PASS / APK ARTIFACT DOWNLOADED AND VERIFIED / PHYSICAL DEVICE ACCEPTANCE PENDING / PRs UNMERGED.**

- PR [#90](https://github.com/Frangga11/glass-tv-home/pull/90), branch `ci/export-islamic-prayer-stack-apk-001`, exact HEAD `aba99d40b8f95f7d6a81711d0c74576ec340b3b9`; base remains PR #89 source branch. PR remains OPEN / DRAFT / UNMERGED.
- CI #1650 / run `42525914841`: both jobs completed SUCCESS. Android “Launcher build and unit tests” passed, including “Unit tests and release build (validation only)”, APK size diagnostics and artifact upload. “Lists, tools and docs” also passed.
- Workflow artifact #13634728567, ZIP name `glass-tv-home-pr90-apk-artifact.zip`, size 1,428,646 bytes, created 8 Nov 2026 UTC, expiry 15 Nov 2026 UTC. Artifact SHA-256: `92b0aff385d3c221dd897c8847f9229aa1471c0b998c69463e925e9c20b188af`; matches GitHub artifact digest.
- Downloaded ZIP and inspected contents: exactly one `app-release.apk`, 1,626,745 bytes. APK SHA-256: `835e4284035a8bbf6b4b0261e46536c840ac959fbc316b4c49176f46fac43b18`. File identification confirms Android package/APK with APK Signing Block.
- Verified local execution artifacts: `/mnt/data/glass-tv-home-pr90-apk/app-release.apk` and `/mnt/data/glass-tv-home-pr90-apk-artifact.zip`. These are available in the current conversation session; preserve hashes above when transferring or checking integrity.
- This APK was built from PR #89 source content plus the CI-only artifact-export workflow change in PR #90; no app runtime/source change was introduced by PR #90.
- Physical Mi TV Stick install, ADB verification, visual/focus acceptance and runtime checks remain PENDING. CI success and APK file verification do not equal device acceptance.
- **Decision:** PR #90 and PRs #84–#89 remain OPEN / DRAFT / UNMERGED. No merge, retarget, force-push, device installation or default-launcher change performed.


---

## 28. GTVH-HOME-ISLAMIC-SHELF-ARABIC-RIGHT-ALIGN-001 — 9 November 2026

**Status: IMPLEMENTATION COMMITTED / EXACT-HEAD CI PASS / PHYSICAL ACCEPTANCE PENDING / NOT FINAL.**

- User-approved design rule: Quran Arabic is right-aligned with true RTL direction; Indonesian translation is left-aligned with LTR direction.
- Audit at PR #89 exact source HEAD `4038c08f4825f7bacf965bc6708505d3c8c30ebc` found:
  - Home Shelf Arabic had RTL/right alignment in XML, but runtime did not explicitly enforce right gravity.
  - Home Shelf Indonesian translation inherited right alignment from XML.
  - Full daily-ayah reader centered Arabic text, while its translation was already left-aligned.
- Implemented on branch `fix/home-islamic-shelf-arabic-right-align-001`, based directly on PR #89 exact HEAD.
- Draft PR [#91](https://github.com/Frangga11/glass-tv-home/pull/91): OPEN / DRAFT / UNMERGED. Exact HEAD `ee8bfb34db8c00a801ac260428f52ead1c7c6662`; base `fix/daily-ayah-offline-retry-backoff-001` at `4038c08f4825f7bacf965bc6708505d3c8c30ebc`.
- Three commits, three files changed:
  - `MainActivity.java`: enforce RTL + `Gravity.END` for Arabic in the Home Shelf and full reader; enforce LTR + `Gravity.START` for translation in both.
  - `activity_main.xml`: Home Shelf translation is explicitly LTR and start-aligned; Arabic remains RTL and end-aligned.
  - `test_daily_ayah_cache_and_label_contract.py`: add regression contract for XML and runtime alignment in both displays.
- Diff against PR #89 base: 3 files, 23 additions, 2 deletions; no unrelated app scope changed. No ayah content, cache/network, font/color/line spacing, size, max-line limits, Shelf geometry, prayer timetable, focus/navigation, or worker behavior changed.
- CI #1651 / run `42526843055`: **SUCCESS**; both Launcher build/unit tests and Lists/tools/docs jobs passed. Android release build and unit tests passed. Publisher run `42526843044` / #106: SKIPPED because PR #91 is Draft; no APK artifact is claimed.
- PR #91 remains OPEN / DRAFT / UNMERGED. No merge, retarget, force-push, or device install performed.
- Physical Mi TV Stick acceptance remains pending: verify Arabic glyphs/harakat and right edge alignment for one-/two-line verses, Indonesian translation left alignment, and no clipping on the 1920×1080 Android 11 reference device.
- Decision: source implementation committed; automated validation pending; physical acceptance pending; final merge blocked.


---

## 29. GTVH-HOME-ISLAMIC-SHELF-AYAH-SPACING-AND-PRAYER-LABEL-001 — 9 November 2026

**Status: EXACT-HEAD CI PASS / APK ARTIFACT DOWNLOADED AND VERIFIED / PHYSICAL MI TV STICK ACCEPTANCE PENDING / NOT FINAL.**

- User requested tighter vertical rhythm for the daily selected ayah preview and removal of the added local/official prayer-source labels.
- Based on PR #91 exact HEAD `ee8bfb34db8c00a801ac260428f52ead1c7c6662`, branch `fix/home-islamic-shelf-ayah-spacing-and-prayer-label-001`.
- Draft PR [#92](https://github.com/Frangga11/glass-tv-home/pull/92): OPEN / DRAFT / UNMERGED. Exact application-source HEAD before CI-only export fix: `59d63a3c9854b646876aade98e26fc74a39d3140`; latest branch HEAD after CI workflow export fix: `51cba1bc8bdaa2d80f017b3e83653c9a94f21904`. Base PR #91 branch at exact HEAD `ee8bfb34db8c00a801ac260428f52ead1c7c6662`. Seven app/test files plus one CI workflow file changed across the branch.
- App implementation:
  - `activity_main.xml`: Arabic top margin 3dp→1dp; Arabic line spacing -1dp/0.94→-2dp/0.92; Arabic font padding remains enabled. Translation top margin 2dp→0dp; translation line spacing 2dp→0dp and multiplier 1.04. “Baca ayat lengkap” hint top margin 7dp→3dp.
  - `MainActivity.java`: runtime translation line spacing set to 0px / 1.04; prayer header restored to location + Hijri date without source suffix.
  - `PrayerTimes.java`: remove visible KEMENAG RI / PERKIRAAN suffix from compact prayer summary; prayer calculations and source validation untouched.
  - `AppLanguage.java`: remove now-unused prayer source label helper.
  - Regression tests updated for compact Shelf spacing and restored prayer display.
- No Quran content, translation selection, cache/network/retry behavior, prayer-time calculation, Kemenag validation, local fallback calculations, Shelf geometry, focus/navigation, or Dock behavior changed.
- PR #91 exact-head CI #1651 / run `42526843055`: **SUCCESS** (Android build/unit tests and Python/docs jobs passed).
- Initial PR #92 CI #1652 / run `42527386710`: Lists/tools/docs failed because an existing Arabic typography contract expected former `-1dp / 0.94` values; the stale expectation was corrected.
- Corrected app-source HEAD `59d63a3c9854b646876aade98e26fc74a39d3140`, CI #1653 / run `42527522848`: **SUCCESS**; Lists/tools/docs passed (204 tests, 1 skipped); Android release build, unit tests and APK diagnostics passed. This first corrected run did not publish an artifact because PR #92 was not included in the artifact allowlist.
- CI-only artifact export fix: commit `51cba1bc8bdaa2d80f017b3e83653c9a94f21904` adds PR #92 to the existing CI artifact-upload allowlist in `.github/workflows/ci.yml`. No app/runtime source changes in this commit.
- Latest CI #1654 / run `42528160811` on exact latest branch HEAD `51cba1bc8bdaa2d80f017b3e83653c9a94f21904`: **SUCCESS**; Android launcher build/unit tests, release assemble, APK size diagnostics and artifact upload passed; Lists/tools/docs passed. Publisher workflow remains skipped because PR is Draft.
- Workflow artifact #13635882633, name `glass-tv-home-dock-visual-parity-83d63facff8d36902fa851d283fd7b9f253a6426` (GitHub artifact digest SHA-256 `37d4f4087eacf577f7d7c252be03439152376da1adff130c6b25167415694394`), expires 15 November 2026 UTC. Downloaded ZIP inspected: contains exactly `app-release.apk`, 1,626,505 bytes.
- Verified APK file: `/mnt/data/Glass-TV-Home-PR92.apk`; SHA-256 `c2eea02caec2b2f8c5d48e318efa0efb32949284074c57e4e60b48b5b1b18d86`. This is the APK from the latest CI #1654 commit, not a prior build.
- Physical Mi TV Stick visual acceptance remains pending. Specifically inspect two-line Arabic for harakat/glyph collision, Arabic-to-translation gap (currently 0dp; evaluate visual breathing room), translation line spacing, spacing to “Baca ayat lengkap”, and prayer header/summary with no source suffix.
- PR #92 remains OPEN / DRAFT / UNMERGED. No merge, retarget, force-push, device install, ADB session, or default-launcher change performed.
- **Decision:** exact-head automated validation PASS; APK artifact downloaded and SHA-256 verified; physical device acceptance PENDING; final merge BLOCKED until user tests and reports results.

---

## 30. GTVH-HOME-ISLAMIC-SHELF-ARABIC-RIGHT-ALIGN-CORRECTIVE-001 — 9 November 2026

**Status: EXACT-HEAD CI PASS / APK ARTIFACT DOWNLOADED AND SHA-256 VERIFIED / PHYSICAL MI TV STICK ACCEPTANCE PENDING / NOT FINAL.**

- User requested a corrective refinement to make the Home Islamic Shelf Arabic verse reliably right-aligned, consistent with the approved RTL design rule.
- Draft PR [#93](https://github.com/Frangga11/glass-tv-home/pull/93): OPEN / DRAFT / UNMERGED. Branch `fix/home-islamic-shelf-arabic-right-align-corrective-001`; latest exact HEAD `5b46ef0cea39fa439316e48bf85a3cbbb0944820`; base PR #92 branch `fix/home-islamic-shelf-ayah-spacing-and-prayer-label-001`, exact base HEAD `51cba1bc8bdaa2d80f017b3e83653c9a94f21904`.
- Implementation changes:
  - `MainActivity.java`: Home Shelf Arabic TextView and Indonesian translation TextView receive the same explicit runtime line-box width derived from `islamicMaxWidth`; Arabic explicit RTL + `Gravity.END` remains intact, translation explicit LTR + `Gravity.START` remains intact.
  - `tests/test_home_islamic_shelf_arabic_typography_contract.py`: regression contract checks shared line-box width and explicit Arabic RTL/right alignment.
  - `.github/workflows/ci.yml`: PR #93 added to the existing Home layout APK artifact upload allowlist; workflow-only change, no application runtime behavior change.
- Source typography and visual settings retained: Arabic font, warm gold color, -2dp / 0.92 line spacing, font padding enabled, two-line maximum; translation spacing and Shelf geometry unchanged. No Quran content, prayer calculation, navigation, focus, or Dock behavior changed.
- Exact-head CI #1656 / run `42529068497` on HEAD `5b46ef0cea39fa439316e48bf85a3cbbb0944820`: **SUCCESS**. Both jobs completed SUCCESS: Launcher build/unit tests/release build/APK size diagnostics/artifact upload; Lists/tools/docs checks.
- Workflow artifact #13636433567, ZIP name `glass-tv-home-dock-visual-parity-e1a4b4949edd56981b8e8c893cc41f66f9b6c324`, size 1,428,120 bytes, digest `sha256:a9a71e64bce13856b0187d3d3da569cc21fdc6ca31ad3739b9e5f24a872fcbdd`, expires 15 November 2026 UTC. Artifact downloaded and inspected; contains `app-release.apk`.
- Verified APK: `/mnt/data/Glass-TV-Home-PR93.apk`, size **1,626,537 bytes**, SHA-256 `304359db1deb6ffcb87c0fc67bc60cd0a035fe6224ea7d1a1c789fb0bc53eff9`. The file is an Android APK with APK Signing Block. This is the artifact built from the exact PR #93 HEAD above.
- PR #93 remains OPEN / DRAFT / UNMERGED. No merge, retarget, force-push, device installation, ADB session, or default-launcher change performed.
- Physical Mi TV Stick acceptance remains pending. Test one-line and two-line Arabic verses, right-edge alignment, harakat/glyph clipping, Indonesian translation left alignment, and preservation of the spacing/hint/prayer display refinements from PR #92.
- **Decision:** automated source/build gate PASS; APK downloaded and hash verified; device visual acceptance PENDING; final merge BLOCKED until user reports device results.
- **Next step:** install this exact PR #93 APK on the Mi TV Stick and inspect the Arabic right edge and glyph/harakat integrity. If any row still appears off, capture a straight-on screenshot and record the specific verse/layout state before another source change.


## Active refinement — GTVH-HOME-SPACING-AND-CAPSULE-HEIGHT-REFINEMENT-001 — 9 November 2026

- Active source PR: [#97](https://github.com/Frangga11/glass-tv-home/pull/97), OPEN / DRAFT / UNMERGED. Base: PR #83 source branch at exact SHA `947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803`.
- Source branch: `verify/home-spacing-capsule-height-refinement-001-validated`; exact HEAD: `bb10228f930cc0b0e8c3dbf526aad9b0c94112fd`.
- Superseded PRs #94, #95 and #96 are CLOSED / NOT MERGED. They were replaced to ensure exact-head CI ran against the final implementation and corrected regression contracts. Do not reopen or merge them.
- Owner-approved boundaries: horizontal safe margins 30dp; bottom safe margin 30dp in horizontal and vertical Dock modes; top safe margin remains 29dp. MIC/Assistant, Wi-Fi and the full Beranda/Profile/Settings navigation capsule are 30dp high. Inputs is intentionally untouched. Beranda/Profile drawable spacing is 2dp; Settings remains 4dp.
- Home wallpaper description bottom is anchored to the measured horizontal Glass tray top at 30dp. Prayer table retains its existing -6dp translation; the Islamic shelf anchor compensates the actual translation so the visible prayer-table lower edge is also 30dp from the tray.
- At 320dpi (density 2.0), 30dp = 60px. Runtime uses display density, not fixed pixel constants.
- Production files changed: `activity_main.xml`, `MainActivity.java`, `DockGeometry.java`. Regression files changed: new `test_home_spacing_capsule_height_refinement_contract.py` plus four existing contracts updated from superseded 29dp/36dp/27.5dp expectations. Total PR diff: 8 files; no other production files changed.
- CI #1668 / run `42531491295`: Lists/tools/docs job PASS. Python suite: **190 tests, OK (1 skipped)**. Android unit tests + Release build still IN PROGRESS at checkpoint capture; do not claim PASS until exact-head run completes.
- Publisher workflow skipped; no APK QA asset is available yet. No rendered 1920×1080 visual audit, physical Mi TV Stick test, ADB, installation, setting change or PR merge has been performed.
- **Status: BELUM FINAL.** Gates pending: exact-head Android unit tests and assembleRelease; APK artifact/size/hash verification; rendered 1920×1080 layout inspection; source/diff audit final pass; owner device acceptance after all gates.
- Detailed report: `GTVH-HOME-SPACING-AND-CAPSULE-HEIGHT-REFINEMENT-001-AUDIT.md`.
- Next action: complete exact-head CI #1668, verify APK QA artifact and inspect final diff. Keep PR #97 draft/unmerged; do not install on TV or request device testing before the automated and visual audits pass.


## CI correction checkpoint — PR #98 — 9 November 2026

- PR #97 is now CLOSED / NOT MERGED and superseded by [PR #98](https://github.com/Frangga11/glass-tv-home/pull/98). PR #98 is OPEN / DRAFT / UNMERGED; base remains PR #83 at `947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803`.
- Active source branch: `verify/home-spacing-capsule-height-refinement-001-android-tests`; exact HEAD: `4817edd2c7109fa7ab8c253bbb5d8037ec55a511`.
- PR #98 retains the three production files and adds two Android unit-test updates to the five Python test changes: total 10 files (3 production, 7 test). No unrelated production files added.
- Exact-head CI #1670 / run `42531675366` and #1669 / run `42531662425` are in progress at checkpoint capture. Previous exact-head Python suite passed 190 tests (1 skipped); Lists/tools/docs passed. Previous Android unit tests compiled production Java and found four stale test assertions (old 29dp safe frame and 27.5dp shelf gap). PR #98 updates those two Android test files to assert 30dp geometry; exact-head Android retest remains PENDING.
- No APK QA asset, rendered 1920×1080 visual audit, or physical Mi TV Stick acceptance is available. No ADB, installation, device setting changes or merge.
- **Status remains BELUM FINAL.** Await exact-head Android unit tests and release build, APK size/hash verification, then rendered visual audit. Do not install or request TV testing before all automated and visual gates pass.


## QA artifact and Android regression checkpoint — PR #99 — 9 November 2026

- Active source PR: [#99](https://github.com/Frangga11/glass-tv-home/pull/99), OPEN / DRAFT / UNMERGED. Exact base: PR #83 branch SHA `947f33cf7181bb06c77dcdba1cf7b7ff3e7d9803`.
- Active source branch: `verify/home-spacing-capsule-height-refinement-001-qa`; exact HEAD: `443d8491109c416915955c8a9ceb202860916a6b`.
- PR #99 adds one QA workflow adjustment to the PR #98 candidate: .github/workflows/ci.yml now allows this refinement PR to publish the existing Home layout QA APK artifact. Production source remains exactly 3 files; regression coverage remains 7 files. Total 11 files.
- Exact-head CI #1672 / run `42531805446` and #1671 / run `42531795756` are in progress. Earlier Python suite result: 190 tests, OK (1 skipped); Lists/tools/docs job PASS. Android unit-test and release-build outcome on PR #99 is pending.
- Publisher workflow is skipped by design; the CI workflow's Home layout test APK artifact step is enabled for PR #99. Artifact publication and APK verification are pending.
- No rendered 1920×1080 visual review, physical Mi TV Stick acceptance, ADB, installation, setting change or merge.
- **Status BELUM FINAL.** Complete exact-head Python/Android tests, release build, QA artifact size/hash and rendered visual audit before any device installation or final declaration.


### Exact-head CI update — PR #99 — 9 November 2026

- CI #1672 / run `42531805446`: Lists/tools/docs job completed SUCCESS; Python suite **190 tests, OK (1 skipped)** on PR #99 exact HEAD `443d8491109c416915955c8a9ceb202860916a6b`.
- Launcher build/unit-test job remains IN PROGRESS; Android `testReleaseUnitTest`, `assembleRelease`, APK size diagnostics and QA artifact upload are not yet verified.
- Status remains **BELUM FINAL**. No APK installation, ADB, device setting change, visual acceptance or merge.


## Cumulative integration checkpoint — PR #101 — 9 November 2026

- New cumulative candidate: [PR #101](https://github.com/Frangga11/glass-tv-home/pull/101), OPEN / DRAFT / UNMERGED.
- Branch: `integration/home-islamic-prayer-spacing-cumulative-101`.
- Current exact HEAD after corrective follow-up: `b8389e1f90b9fb7f65d91cfd0d7a41782f343d83`.
- Base: `fix/daily-ayah-offline-retry-backoff-001`, exact base SHA `4038c08f4825f7bacf965bc6708505d3c8c30ebc`.
- PR #101 is 4 commits ahead, 0 behind its exact base; 16 changed files: 5 production source/layout/workflow files and 11 regression/test files. No unrelated files are in the PR changed-file list.
- Integrated content: PR #100 cumulative Home geometry/capsule changes; PR #91 explicit Arabic RTL/right alignment and LTR translation; PR #92 Ayah spacing refinements and removal of prayer-source labels from Home presentation; PR #93 shared fixed runtime line-box width for Arabic and translation. CI APK artifact allowlist includes PR #90–#93 and PR #101.
- First exact-head CI #1679 / run `42535145121` failed: 211 Python tests, 1 skipped, 1 failure. Root cause was a stale prayer-source label block still present in the cumulative MainActivity header, caught by `test_islamic_shelf_header_is_restored_without_source_label`.
- Corrective commit removed that remaining block. Current exact HEAD `b8389e1f90b9fb7f65d91cfd0d7a41782f343d83` verified from branch file; `getPrayerDataSourceLabel` no longer occurs in MainActivity. Compare against base confirms 4 commits ahead / 0 behind.
- Retest CI #1681 / run `42535212323` is running on the corrected exact HEAD. Lists/tools/docs job PASS; Launcher build and unit tests job still IN PROGRESS at checkpoint capture. Python/Android test result, release build, APK artifact/size/hash remain PENDING.
- PR #101 API currently reports OPEN / DRAFT and mergeable; this is not authorization to merge. Source PRs remain untouched.
- No rendered 1920×1080 visual review, physical Mi TV Stick acceptance, ADB, installation, launcher setting change or merge has been performed.
- **Status: BELUM FINAL.** Continue from exact HEAD above; wait for run #1681 to finish, inspect all failure logs if any, verify APK artifact and hashes, then run rendered visual audit and final scope/behavior audit. Device acceptance only after all automated and visual gates pass and owner explicitly approves.


## Cumulative integration QA update — PR #101 — 9 November 2026

- Corrected source HEAD: `b8389e1f90b9fb7f65d91cfd0d7a41782f343d83`; base remains `4038c08f4825f7bacf965bc6708505d3c8c30ebc`. PR #101 remains OPEN / DRAFT / UNMERGED; 4 commits ahead, 0 behind; 16 changed files (5 production/source/workflow, 11 regression/test). Source PRs remain untouched.
- Exact-head CI retest #1681 / run [42535212323](https://github.com/Frangga11/glass-tv-home/actions/runs/42535212323) completed SUCCESS. Both jobs passed: Lists/tools/docs **211 tests, OK (1 skipped)**; Launcher build and unit tests **SUCCESS**, including `testReleaseUnitTest` for app and installer and `assembleRelease` (`BUILD SUCCESSFUL`).
- PR artifact published: artifact ID `13639167072`, name `glass-tv-home-dock-visual-parity-569500eacf0669f8d7bc5f191328a9d49790cf1e`, ZIP 1,428,388 bytes, SHA-256 `6ceb83fcf6a9530cd22afb24c2ada37d88a2df15aa2c9d688193c7e79f943139`. It contains `app-release.apk` (not a layout screenshot/test APK). Extracted APK: 1,626,525 bytes; SHA-256 `5423f7746d6b771fcb0c76ca3fcdca5186c5957d2967f66437554cb6e7d62398`; ZIP integrity PASS; manifest, DEX and resources present.
- Size diagnostic: 1,626,525 bytes is 53,661 bytes above the workflow's historical 1.5 MiB warning threshold. However, compared with the earlier PR #83 recorded release APK (1,625,213 bytes), the increase is only 1,312 bytes (+0.081%). This is a non-blocking warning, not a CI failure; retain for size trend monitoring, no unscoped optimization.
- CI artifact is built from GitHub's PR merge ref `569500eacf0669f8d7bc5f191328a9d49790cf1e`, which merges source HEAD above into base. It is not a production release publication; the separate release-publisher workflow was skipped by design for a PR.
- Final static package integrity and changed-file scope checks are recorded. Rendered 1920×1080 visual audit is NOT RUN: current available execution environment has no Android emulator, ADB, aapt/apkanalyzer or screenshot renderer. Physical Mi TV Stick acceptance remains NOT RUN. No ADB, installation, launcher setting mutation, or merge.
- **Status: AUTOMATED QA PASS; VISUAL / DEVICE ACCEPTANCE PENDING; OVERALL BELUM FINAL.** Keep PR #101 draft/unmerged. Next gate is a genuine rendered 1920×1080 visual inspection; only after it passes may owner-approved Mi TV Stick acceptance proceed. No installation or merge without explicit owner authorization.

## Cumulative integration QA update — PR #101 — 9 November 2026

- Corrected source HEAD: `b8389e1f90b9fb7f65d91cfd0d7a41782f343d83`; base `4038c08f4825f7bacf965bc6708505d3c8c30ebc`. PR #101 remains OPEN / DRAFT / UNMERGED; 4 commits ahead, 0 behind; 16 changed files (5 production/source/workflow, 11 regression/test). No source PR modified or merged.
- Exact-head CI retest #1681 / run [42535212323](https://github.com/Frangga11/glass-tv-home/actions/runs/42535212323) completed SUCCESS. Lists/tools/docs: **211 tests, OK (1 skipped)**. Launcher build/unit tests: SUCCESS; `testReleaseUnitTest` and `assembleRelease` completed; Gradle reported BUILD SUCCESSFUL. Both jobs passed.
- CI artifact #13639167072 (`glass-tv-home-dock-visual-parity-569500eacf0669f8d7bc5f191328a9d49790cf1e`), ZIP 1,428,388 bytes, SHA-256 `6ceb83fcf6a9530cd22afb24c2ada37d88a2df15aa2c9d688193c7e79f943139`. ZIP contains exactly `app-release.apk`; extracted APK size 1,626,525 bytes; SHA-256 `5423f7746d6b771fcb0c76ca3fcdca5186c5957d2967f66437554cb6e7d62398`. ZIP integrity PASS; AndroidManifest.xml, DEX and resources present. This is a CI QA artifact, not a production release.
- APK size warning: 1,626,525 bytes exceeds the historical 1.5 MiB warning threshold by 53,661 bytes. Compared with the previously recorded PR #83 APK (1,625,213 bytes), increase is 1,312 bytes (+0.081%). Warning is non-blocking; no broad/unscoped optimization authorized.
- Static changed-file audit: 16 files, exactly 5 production/source/workflow + 11 test/regression; no unrelated file reported. Cumulative content retains PR #100 Home geometry/capsule, PR #91 Arabic RTL/right alignment + LTR translation, PR #92 Ayah spacing and prayer-source label removal, PR #93 shared runtime line-box width. No prayer-time calculation/data/fallback changes intended.
- Important CI nuance: artifact build was checked out from PR merge ref `569500eacf0669f8d7bc5f191328a9d49790cf1e`, which includes source HEAD above plus base. Separate release-publisher workflow was skipped because PR is Draft.
- **Visual audit is NOT RUN**: available execution environment has no Android emulator, ADB, aapt/apkanalyzer or screenshot renderer. Artifact is an APK, not a rendered screenshot. Physical Mi TV Stick acceptance is also NOT RUN. Do not claim 1920×1080 visual pass.
- No ADB, install, launcher setting mutation, default-launcher change, PR merge, or PR state transition performed. PR #101 stays OPEN / DRAFT / UNMERGED.
- **Status: AUTOMATED QA PASS; PACKAGE INTEGRITY PASS; VISUAL / DEVICE ACCEPTANCE PENDING; OVERALL BELUM FINAL.** Remaining gate: produce a genuine rendered 1920×1080 visual audit, then seek explicit owner authorization for Mi TV Stick acceptance. Merge remains separately gated by explicit owner approval.


## Wallpaper / prayer shelf spacing and Arabic physical-right alignment — PR #103 — 9 November 2026

- Active corrective candidate: [PR #103](https://github.com/Frangga11/glass-tv-home/pull/103), OPEN / DRAFT / UNMERGED. Source branch: `fix/home-wallpaper-prayer-spacing-arabic-rtl-102`; exact HEAD: `f32918129ebc847190266d3acc659c39fda5ddb8`. Base: cumulative PR #101 branch `integration/home-islamic-prayer-spacing-cumulative-101`, exact base SHA `08b521cadb8c913b49fce42acb4e536e3eb7e9ea`. Compare: 4 commits ahead / 0 behind; 4 changed files (2 production files, 2 regression tests).
- Wallpaper title top margin restored to **0dp**. Wallpaper title-to-description margin set to **5dp**.
- Ayah-block-to-prayer-header effective visual gap is **0dp**: Ayah block bottom margin is 6dp to cancel the existing prayerHeader `translationY=-6dp`; prayerHeader top margin remains 0dp. This preserves the prayer header/table anchored position while the two visible edges meet. Existing prayerTable `translationY=-6dp` remains unchanged; bottom tray compensation logic remains unchanged.
- Arabic alignment root cause addressed: `Gravity.END` is relative to the View's layout direction; setting `textDirection=RTL` does not itself set layout direction. Arabic TextView now explicitly uses LTR view layout direction and absolute `Gravity.RIGHT | Gravity.TOP`, while Arabic shaping remains RTL and text alignment follows gravity. Ayah block/shelf and divider use physical-right gravity to keep the shared line box's right edge consistent. Translation remains LTR.
- Regression contracts added for the exact wallpaper margins, zero effective Ayah/header gap, and separation of Arabic RTL shaping from physical right alignment.
- Automated CI / Python / Android unit tests / release APK build are **PENDING** at checkpoint time. No successful CI result is claimed. Rendered 1920×1080 visual audit and physical Mi TV Stick acceptance are also **NOT RUN**. No ADB, device install, launcher setting change, or merge.
- PR #102 was superseded and closed unmerged during branch realignment; PR #103 is the active candidate. Source PR #101 remains unmerged.
- **Status: IMPLEMENTATION COMMITTED; AUTOMATED QA PENDING; VISUAL / DEVICE ACCEPTANCE PENDING; OVERALL BELUM FINAL.** Keep PR #103 draft/unmerged until exact-head CI passes, screenshot review confirms all specified edges and unchanged surrounding positions, and owner explicitly authorizes any device acceptance/merge.


### Exact-head QA correction update — PR #103 — 9 November 2026

- Current source HEAD: `a3d697c793e23a59f9f9953e2074d83daa210cf9`; base remains `08b521cadb8c913b49fce42acb4e536e3eb7e9ea`. Compare: **7 commits ahead / 0 behind**. Final changed-file set is 5 files: `MainActivity.java`, `activity_main.xml`, and 3 regression test files. No other production modules are in scope.
- First CI attempt #1687 / run `42565964380` detected 3 stale regression expectations: one expected the old Arabic TextView relative `Gravity.END`, one expected the same legacy setter in the typography contract, and one expected the divider's original `layout_gravity=end`. Root cause was test contracts not updated for the scoped Arabic override and an unnecessary change to divider gravity.
- Corrective refinement restores the original shelf parent gravity and divider `layout_gravity=end`; only the Ayah block is given LTR layout direction so its relative child alignment resolves physically right. Arabic TextView keeps XML `gravity=end` under explicit LTR view layout direction, while runtime uses absolute `Gravity.RIGHT | Gravity.TOP` and RTL text direction. Translation, divider, schedule table and other shelf geometry remain unchanged. The legacy daily-Ayah and typography test assertions were updated to expect the new Arabic runtime gravity.
- Latest exact-head CI #1689 / run [42566072365](https://github.com/Frangga11/glass-tv-home/actions/runs/42566072365) is **IN PROGRESS** at checkpoint capture. No passing result is claimed yet. Release publisher is skipped as expected for a draft PR.
- PR #103 remains OPEN / DRAFT / UNMERGED. PR #102 is superseded/closed unmerged. PR #101 remains unmerged. No APK artifact has yet been verified for PR #103; rendered 1920×1080 screenshot audit and physical Mi TV Stick acceptance remain NOT RUN. No ADB, installation, launcher-setting change or merge.
- **Status: IMPLEMENTATION COMMITTED; FIRST CI FAILED ON STALE TEST CONTRACTS; CORRECTIVE CI RUN IN PROGRESS; VISUAL / DEVICE ACCEPTANCE PENDING; OVERALL BELUM FINAL.**


### Exact-head CI completion — PR #103 — 9 November 2026

- Exact source head validated: `a3d697c793e23a59f9f9953e2074d83daa210cf9`. CI run **#1689 / 42566072365: SUCCESS** ([run details](https://github.com/Frangga11/glass-tv-home/actions/runs/42566072365)). Both jobs passed: **Lists, tools and docs** and **Launcher build and unit tests**. Android Gradle unit-test/release-build validation completed successfully; Gradle reports `BUILD SUCCESSFUL` (99 actionable tasks).
- Release APK generated by CI at `launcher/app/build/outputs/apk/release/app-release.apk`; exact size **1,626,637 bytes (1,588 KB)**. APK size is **53,773 bytes above** the historical 1.5 MiB (1,572,864-byte) reference; workflow explicitly treats this as a non-blocking diagnostic, not a quality gate. Compared with PR #101's reported 1,626,533 bytes, this is +104 bytes. No artifact ZIP was published by this PR run; the PR artifact-publish step was skipped because the PR remains Draft. Do not present a downloadable APK from this run.
- The separate release publisher workflow was skipped for the Draft PR. CI success does not constitute rendered UI or hardware acceptance.
- PR #103 remains OPEN / DRAFT / UNMERGED; branch `fix/home-wallpaper-prayer-spacing-arabic-rtl-102`; source HEAD unchanged. Base remains PR #101 cumulative head `08b521cadb8c913b49fce42acb4e536e3eb7e9ea`; compare is 7 commits ahead / 0 behind, with 5 changed files (2 production files, 3 regression test files).
- Final implementation values remain: wallpaper title top margin **0dp**; title-to-description **5dp**; Ayah-block-to-prayer-header effective visual gap **0dp** via 6dp block margin cancelling existing -6dp header translation. Arabic text keeps RTL shaping and explicit physical-right alignment. Existing divider gravity, translation, prayer table geometry/typography, capsule dimensions and other Home positions remain preserved by scoped diff.
- First CI #1687 failed on stale test expectations; the test contracts and over-broad divider/shelf gravity changes were corrected. The final exact-head CI #1689 now passes. This earlier failure is resolved; no open automated CI failure remains.
- **Still pending:** genuine rendered 1920×1080 screenshot review for the exact edges and all unaffected surrounding elements; visual confirmation that Arabic glyphs align to the right edge; physical Mi TV Stick acceptance (only with owner authorization). No ADB, device install, launcher-setting change or merge performed.
- **Status: SOURCE AUDIT PASS; EXACT-HEAD CI PASS; RELEASE BUILD PASS; APK SIZE WARNING (NON-BLOCKING); VISUAL / DEVICE ACCEPTANCE PENDING; OVERALL BELUM FINAL.** Keep PR #103 Draft/open/unmerged until visual review is complete and explicit owner approval is given.

## DOCUMENT RECONCILIATION & SINGLE-DOOR SYNC — 9 NOVEMBER 2026

- Reconciled current status against live PR #103 metadata and the latest exact-head CI evidence.
- Current authoritative state is the PR #103 snapshot in the sync block above. Earlier PR #100/#101/#99 and older integration-gap entries remain historical evidence only; they must not be interpreted as current active restart instructions where superseded by PR #103.
- The current next action is documentation synchronization complete → preserve PR #103 as Draft/unmerged → await/perform genuine rendered visual review → only then seek owner-authorized physical Mi TV Stick acceptance. No implementation of the new Silver Glass workstream begins before this handover point is acknowledged.
- Frosted Silver Glass is approved as a future scoped workstream, not yet implemented. Its approved master prompt is the execution brief; this checkpoint governs sequencing and safeguards.
- Supporting docs synchronized in the same documentation pass: MASTER_PROJECT.md, INVENTORY.md, DECISION_LOG.md, AUDIT_REGISTER.md, HANDOVER.md.
- No application source, branch, PR state, device, or launcher setting was changed by this documentation reconciliation.


## Final documentation synchronization checkpoint — 9 November 2026

**Documentation audit result: COMPLETE for the project-management directory inventory.**

- Enumerated the complete `docs/project-management/` directory: **96 Markdown files + 1 `prototypes/` directory containing 6 entries**.
- Verified programmatically that all 96 Markdown filenames appear in the documentation catalog in `INVENTORY.md`; missing catalog entries: **0**.
- Synchronized/clarified 15 documents in one controlled documentation pass: `GTVH-CHECKPOINT-LATEST.md`, `MASTER_PROJECT.md`, `INVENTORY.md`, `DECISION_LOG.md`, `AUDIT_REGISTER.md`, `HANDOVER.md`, `README.md`, `DOCUMENTATION_WORKFLOW.md`, `UI_REFRESH_ROADMAP.md`, `GLOBAL_UI_DESIGN_SYSTEM_AUDIT_2026-11-06.md`, `GTVH-HOME-CUMULATIVE-RECONCILIATION-003.md`, `PROJECT_G_READINESS_CHECKPOINT.md`, `OPERATIONS_LOG.md`, `MERGE_QUALITY_POLICY.md`, and `BUG_REPAIR_ROADMAP.md`.
- Older evidence files were not rewritten merely to make their dates appear current. They are catalogued, retained, and explicitly subordinate to this checkpoint. Contradictory historical PR #100/#101 and old Project G/UI Refresh snapshots are marked as historical/superseded in the relevant navigation or reconciliation documents.
- The single-door rule is now explicit across the checkpoint, master, inventory, decision, audit, handover, README, and documentation workflow: **all current status and restart instructions resolve to this file only**.
- New Frosted Silver Glass direction is recorded as OWNER-APPROVED / NOT STARTED. It does not alter current PR #103 status or authorize source implementation before its audit/baseline sequence.
- No application source, active PR, branch ancestry, PR state, APK, physical device, ADB session, launcher setting, or merge state was changed during documentation synchronization.

**Verification:** all six core documents were re-fetched from `docs/project-master-inventory` after update; each contains the same current-authority block and the approved Silver Glass workstream reference. Inventory catalog verification returned 96/96 files present.

**Final documentation synchronization commit:** generated by this checkpoint update on `docs/project-master-inventory`. The immediately preceding completed catalog commit is `b0ed1cbffaca2f84c4828848cc4d9f2b917ac732`; the final checkpoint commit SHA is reported in the session handover/response.

**Project status remains:** PR #103 OPEN / DRAFT / UNMERGED; exact-head CI PASS; visual review and physical Mi TV Stick acceptance PENDING; overall NOT FINAL.

## AUTHORITATIVE CURRENT CHECKPOINT — GTVH-GLOBAL-FROSTED-SILVER-GLASS-001 — 9 NOVEMBER 2026

**This section supersedes prior restart/status instructions in older sections of this file.** This file remains the single source of truth for the current project state. All supporting documents are subordinate to this checkpoint.

### 1. Active workstream and dependency
- Workstream: GTVH-GLOBAL-FROSTED-SILVER-GLASS-001 — Frosted Silver Glass + Electric Cyan DPAD Focus.
- Repository: Frangga11/glass-tv-home.
- Active implementation PR: [#104](https://github.com/Frangga11/glass-tv-home/pull/104), OPEN / DRAFT / UNMERGED.
- Source branch: feature/global-frosted-silver-glass-001.
- Base branch: fix/home-wallpaper-prayer-spacing-arabic-rtl-102 (PR #103 dependency).
- Base HEAD: a3d697c793e23a59f9f9953e2074d83daa210cf9.
- Current PR/source HEAD: b737b617aa53fbfc112ccdfe8a7cd4959043d3ec.
- Last application-source HEAD: 263b693b5be7e05fa37949259142f5bc8f091699. The final commit from source HEAD to current PR HEAD updates only the workstream audit markdown evidence.
- PR #103's cumulative Home, prayer, Arabic RTL/right-alignment and spacing corrections remain protected. Do not retarget, merge, flatten, reset, rebase, force-push, or rewrite branch ancestry.

### 2. Recovery work completed after app force-close
- Fixed TvFocusIndicator.createOvalStateBackground() parameter type (normalFill is int), resolving the Java float-to-int compile failure.
- Corrected the stale weather/header regression contract to require the shared geometry-stable focus listener and preserve top-highlight/focus-generation behavior.
- Wallpaper interval rows now use the shared selectable state background with independent Normal, Focused, Selected, Pressed and Disabled states; selected interval remains selected without DPAD zoom.
- WeatherActivity focus now changes elevation only; removed no-op scale animations.
- Dynamic settings search, edit-name cards, settings rows/cards, Done/Close actions, profile selector and avatar actions use shared silver/cyan focus states.
- Active profile and wallpaper selection remain independent from cyan DPAD focus.
- Recent Apps and Close All focus no longer zoom; Recent Apps has a dedicated cyan focus outline.
- Media popup and Home header/weather focus retain existing focus/navigation behavior without geometry changes.
- Dynamic Islamic/Ayah/prayer visual colors use the cool-silver token system; Arabic RTL shaping and physical right alignment remain protected.
- Added/updated regression contracts covering selector state precedence, profile selection, wallpaper interval, weather/header focus, Recent Apps, Arabic typography and XML validity.

### 3. Exact-head automated validation
- First corrective CI #1753 / run 42605112203 failed for two root causes: an incorrect Java parameter type and a stale test expecting old weather focus scaling. Both were fixed; tests were not disabled.
- Exact PR HEAD b737b617aa53fbfc112ccdfe8a7cd4959043d3ec: CI #1758 / run [42605960427](https://github.com/Frangga11/glass-tv-home/actions/runs/42605960427) — **SUCCESS**.
- Lists/tools/docs job: SUCCESS.
- Python regression: **235 passed, 1 skipped**.
- Android unit tests and release build: **BUILD SUCCESSFUL**.
- Release APK build diagnostic: **1,628,165 bytes (1,590 KB)**. APK size is diagnostic, not a blocking threshold.
- Workflow artifact ID 13668172465, 1,429,137 bytes, is the Home layout test artifact, not the release APK. Do not present it as the release APK.
- Automated CI PASS does not establish visual, functional, performance or physical-device acceptance.

### 4. Hard constraints
- RE-SKIN, NOT REWIRE. Preserve functioning IDs, listeners, handlers, data logic, navigation, DPAD lifecycle, approved geometry, wallpaper behavior, Islamic shelf, prayer schedule and Arabic text behavior.
- DPAD focus must not scale, translate, resize or reposition its component. Keep the historical 1.07 tile layout reservation so existing grid geometry does not move.
- Focus state and persistent selected/active state must remain visually distinct.
- No real-time blur, unbounded animation allocation, or unnecessary layout changes.
- Keep PR #104 Draft/open/unmerged. Do not merge or install an APK on the Mi TV Stick without explicit owner authorization.
- No ADB/device install/default-launcher setting change was performed in this recovery pass.

### 5. Acceptance gates still PENDING / NOT RUN
1. Rendered 1920×1080 screenshot review of Home, dock, shared header, dialog, Wi-Fi, profile, location, weather/AQI, prayer and Islamic surfaces.
2. Bright, dark and mixed wallpaper readability review, including primary/secondary text and glass fallback contrast.
3. Full end-to-end functional regression and live DPAD navigation/focus-path test.
4. Performance/GPU profiling for Mi TV Stick Android 11.
5. Physical Mi TV Stick acceptance, only after the owner explicitly authorizes device testing.
6. Final integration/merge-readiness audit and explicit owner approval.

### 6. Exact restart sequence
- Start from this checkpoint, then verify live PR #104 metadata and exact HEAD before touching source.
- Continue the remaining visual/functional/performance audit; do not repeat already-passed source fixes or CI work without evidence.
- Record every new change in the workstream audit file, rerun exact-head CI after the final source/documentation change, and synchronize this checkpoint plus supporting documents.
- Only after acceptance evidence is complete, request owner approval for device acceptance and later merge. Never mark the whole workstream FINAL while any acceptance gate remains pending.

**Current status:** implementation and exact-head automated CI PASS; visual/functional/performance/device acceptance PENDING; PR #104 OPEN / DRAFT / UNMERGED; overall **BELUM FINAL**.


## Final single-door documentation verification — 9 November 2026
- Re-fetched and verified the six current-authority documents on docs/project-master-inventory: GTVH-CHECKPOINT-LATEST.md, MASTER_PROJECT.md, INVENTORY.md, DECISION_LOG.md, AUDIT_REGISTER.md and HANDOVER.md.
- Each supporting document points back to this canonical checkpoint; each current-status record identifies PR #104, branch feature/global-frosted-silver-glass-001 and source HEAD b737b617aa53fbfc112ccdfe8a7cd4959043d3ec. The detailed CI run identifier 42605960427 is recorded in the checkpoint, master, audit register and handover; the inventory and decision log retain their shorter summary entries.
- The workstream audit file remains in docs/project-management/GTVH-GLOBAL-FROSTED-SILVER-GLASS-001-AUDIT.md and is catalogued in INVENTORY.md.
- This documentation pass changes no application source, PR ancestry, PR state, device state or launcher setting. PR #104 remains OPEN / DRAFT / UNMERGED.

## Continuation checkpoint — CI #1761 closure and targeted source color audit — 9 November 2026

- Active workstream: GTVH-GLOBAL-FROSTED-SILVER-GLASS-001.
- PR #104: OPEN / DRAFT / UNMERGED; branch `feature/global-frosted-silver-glass-001`; current PR HEAD after audit-note update: `05d50e73e7da41fa900c5ba8e3c86f60392750ba`; protected PR #103 base remains `a3d697c793e23a59f9f9953e2074d83daa210cf9`.
- Latest source-code HEAD remains `27eca44f42b17fc9b3f7cf711b5bbf488ef4e12a`; latest PR-head CI #1761 / run `42609316862` completed SUCCESS on immediately preceding PR HEAD `4575b09b262ad664f6766ce286d5e5ed90e92780`. Both Lists/tools/docs and Android unit-test/release-build jobs passed. The subsequent HEAD `05d50e73...` is audit-document-only and must receive its own CI before being called exact-head validated.
- Targeted Java hard-coded-color classification: no additional source change justified. Specular silver/cyan highlights, shadows, app-brand icon colors, semantic success/warning/danger, current-location status green and devotional gold accent were distinguished from global interactive palette values. The dock's former legacy cyan drift remains corrected and regression-guarded.
- Full 141-resource classification remains PARTIAL. Rendered 1920×1080 bright/dark/mixed wallpaper review, end-to-end runtime function regression, live DPAD path, GPU/performance profiling and owner-authorized Mi TV Stick acceptance remain NOT RUN / PENDING.
- No merge, rebase, reset, force-push, ADB, TV install or launcher-setting mutation. RE-SKIN NOT REWIRE. Overall **BELUM FINAL**.
- Workstream audit: `docs/project-management/GTVH-GLOBAL-FROSTED-SILVER-GLASS-001-AUDIT.md`, Section 11.
- Next execution: complete drawable/values resource classification, then obtain genuine rendered visual evidence before any further color changes; follow with runtime/DPAD/performance gates and device acceptance only with explicit owner approval.

## Latest continuation — XML resource and geometry risk scan — 9 November 2026
PR #104 current HEAD: `6660b9721f0d03575370ba8570a07b40c1425f4c` (audit-only commit); source HEAD remains `27eca44f42b17fc9b3f7cf711b5bbf488ef4e12a`. CI #1762 / run `42610588965` SUCCESS on exact preceding PR HEAD `05d50e73e7da41fa900c5ba8e3c86f60392750ba`, both jobs passed. New audit-only HEAD requires its own CI.
- Changed XML resource diff reviewed: neutral silver/cyan state system broadly consistent; component alpha variants and app-module/installer-module resource separation noted.
- Open geometry/visual flags: bg_ram_pill intrinsic padding shrank 20dp/9dp to 12dp/5dp (verify usage and restore unless owner-approved); bg_recent_card radius 24dp→26dp (verify rendered radius consistency); static Ayah color gold→white plus shadow change (verify readability/intent); installer theme window/status/navigation background remains #0C0E14 while content root is #2C333B (verify startup transition).
- Full unchanged-resource semantic classification, rendered 1920×1080 bright/dark/mixed wallpaper review, runtime functional/DPAD, GPU/performance and physical Mi TV Stick acceptance remain pending. PR #104 OPEN / DRAFT / UNMERGED; no device install or merge. Overall BELUM FINAL.
- Detailed findings: GTVH-GLOBAL-FROSTED-SILVER-GLASS-001-AUDIT.md Section 12.

## Recovery continuation — force-close backlog resumed — 9 November 2026
- PR #104 remains OPEN / DRAFT / UNMERGED; current HEAD `1d480afe88072094cd8169ab8ed8d2d8d9ae0bbe`; protected PR #103 base remains `a3d697c793e23a59f9f9953e2074d83daa210cf9`.
- CI #1763 / run `42610968348` SUCCESS on prior audit HEAD `6660b9721f0d03575370ba8570a07b40c1425f4c`.
- Restored Recent Apps RAM pill drawable padding from unintended 12dp/5dp back to original 20dp horizontal / 9dp vertical after confirming `activity_recent_apps.xml` uses `@drawable/bg_ram_pill`. Added regression guard `test_recent_apps_ram_pill_preserves_original_content_insets`.
- Source/test HEAD `75481d456556655076c51735f33f19a131e17190`; CI #1764 / run `42611753895`: Lists/tools/docs PASS; Android unit-test/release-build still in progress at checkpoint time. The subsequent audit-only HEAD `1d480afe...` needs its own CI.
- Remaining: rendered review of Recent Apps card 26dp radius; Ayah white text/black shadow contrast; installer startup window background; full unchanged-resource classification; rendered 1920×1080 bright/dark/mixed wallpaper; runtime/DPAD/performance; owner-authorized Mi TV Stick acceptance.
- No merge, ADB, install or launcher setting changes. Detailed audit Section 13. Overall BELUM FINAL.


## Continuation checkpoint — CI verification and installer surface correction — 9 November 2026
- Required CI #1764 / run `42611753895`: both jobs completed SUCCESS (Launcher build and unit tests; Lists, tools and docs).
- Required CI #1766 / run `42611799280`: both jobs completed SUCCESS (same two jobs). All reported steps completed successfully.
- PR #104 remains OPEN / DRAFT / UNMERGED on protected base SHA `a3d697c793e23a59f9f9953e2074d83daa210cf9`. Previous audit HEAD was `1d480afe88072094cd8169ab8ed8d2d8d9ae0bbe`.
- Recent Apps source: normal background radius 26dp; focus/pressed/disabled selector radii 26dp. Runtime adapter constructs a 26dp GradientDrawable and replaces XML background, so runtime clipping/outline and normal stroke need screenshot confirmation.
- Islamic Ayah: white Arabic text, black shadow, Amiri Quran, RTL and right gravity confirmed; WeeklyAyah data/cache/translation logic unchanged. Wallpaper contrast remains unverified without rendered captures.
- Installer mismatch confirmed: theme windowBackground #0C0E14 vs activity root #2C333B. Corrected only windowBackground to #2C333B; status/navigation bars remain #0C0E14. Added Python regression test for window/root parity and system-bar preservation.
- Corrective source commit: `e5a4f47aabda630af984a315969637e67d0627c9`. PR #104 current HEAD is this commit. CI #1767 / run `42612524215`: Lists/tools/docs SUCCESS; Android launcher build/unit-test job IN PROGRESS at checkpoint update. Do not mark exact-head CI PASS until complete.
- Resource inventory: 113 app drawable XML files classified by role (28 glass surfaces/backgrounds; 14 state/status/text selectors; 15 weather/AQI artwork; 56 content/navigation icons). Six installer drawables tracked separately. App values: 19 files = 4 base values XMLs, 14 locale-specific strings overlays, 1 API-28 styles overlay. Color taxonomy distinguishes global tokens, semantic state/status, brand/content accents, specular highlights, shadows/depth and alpha/transparency variants. No mechanical recoloring.
- Pending: rendered 1920×1080 bright/dark/mixed wallpaper review; functional and live DPAD; Recent Apps runtime clipping/focus; installer startup transition observation; GPU/performance; physical Mi TV Stick acceptance only with explicit authorization. No ADB, install or merge. Overall BELUM FINAL.


## Exact-head closeout — 9 November 2026
- PR #104 HEAD: `93977dd887e042a0c30113140bbd9fc838886281`; branch `feature/global-frosted-silver-glass-001`; OPEN / DRAFT / UNMERGED. Protected base SHA remains `a3d697c793e23a59f9f9953e2074d83daa210cf9`.
- Source/test correction commit: `e5a4f47aabda630af984a315969637e67d0627c9` (Installer window background aligned to root #2C333B; system bars unchanged; regression test added).
- CI #1764 run `42611753895`: PASS, both jobs complete. CI #1766 run `42611799280`: PASS, both jobs complete. CI #1767 run `42612524215`: PASS, both jobs complete, validating source/test commit. CI #1768 run `42612984819`: PASS, both jobs complete, validating latest PR HEAD `93977dd...` including Audit Section 14.
- Static source audit: Recent Apps XML normal/focus radii 26dp; runtime adapter applies 26dp dynamic radius but overrides XML background, so normal border retention, clipping and visible focus ring remain visual/runtime checks. No layout size change attributable to foreground outline identified.
- Islamic Ayah source: Arabic white, black shadow, Amiri Quran, RTL and right alignment retained; translation near-white with black shadow. WeeklyAyah data/cache/translation logic unchanged. Wallpaper contrast not visually verified.
- Installer source correction is CI-validated; actual startup transition/flash behavior still requires runtime observation. Status/navigation bars remain #0C0E14 by design.
- Resource inventory completed by role: 113 app drawable XMLs (28 surfaces/backgrounds, 14 selectors/status/text, 15 weather/AQI artwork, 56 content/navigation icons); 6 installer drawables separate; 19 app values files (4 base, 14 locale strings, 1 API-28 styles overlay). Color taxonomy: global tokens; semantic state/status; brand/content accents; shadow/depth; specular highlights; alpha/transparency; icon/weather artwork. No mechanical recoloring.
- Six canonical management documents synchronized on `docs/project-master-inventory` at commit to be recorded by this update. Detailed source evidence is in PR branch audit Section 14.
- Remaining gates: rendered 1920×1080 bright/dark/mixed wallpaper visual QA; five-state Normal/Focused/Selected/Pressed/Disabled screenshots; full functional and live DPAD runtime; Recent Apps clipping/outline; installer startup transition; GPU/performance profiling; physical Mi TV Stick acceptance only with explicit owner authorization.
- No PR merge, no ADB, no APK install, no default launcher change, and no PR #103 modification. Overall workstream remains BELUM FINAL despite source/static audit and automated CI PASS.


# AUTHORITATIVE VISUAL QA CONTINUATION — GTVH-GLOBAL-FROSTED-SILVER-GLASS-001 — 9 NOVEMBER 2026

This is the current single-door checkpoint. Supporting management documents are synchronized to this same checkpoint ID and remain subordinate to this file.

## Trigger / identity
- Repository: Frangga11/glass-tv-home.
- Workstream: GTVH-GLOBAL-FROSTED-SILVER-GLASS-001.
- PR #104: OPEN / DRAFT / UNMERGED.
- Branch: feature/global-frosted-silver-glass-001.
- Exact PR HEAD at checkpoint: b14ba7a35659ea404b88a56175e706e458c8459f.
- Protected base: a3d697c793e23a59f9f9953e2074d83daa210cf9.
- PR #103 remains protected and unchanged.
- Latest source correction: 811bccae770d225218583138968f85f083fed516.
- Regression tests: 9a1ad0e3911afe63d3076240f7b9bac02e6e1cea (Recent Apps dynamic border), 3f1d21b7be61e80c894c3f09346f10f6b5dcf8cc (Islamic Ayah font/contrast contract).
- Audit evidence update: b14ba7a35659ea404b88a56175e706e458c8459f.

## Verified and corrected
1. Recent Apps source: XML normal border was being lost because AppViewHolder.bind() replaced bg_recent_card.xml with a dynamic GradientDrawable that set only fill and radius. Restored 1dp #99DCE5ED stroke on the dynamic drawable. Radius remains 26dp; package-specific fill, dimensions, foreground focus selector and Electric Cyan focus ring remain unchanged.
2. Added regression assertions for dynamic Recent Apps stroke and 26dp radius.
3. Added Islamic Ayah regression assertions for Amiri Quran font, Arabic RTL/right alignment and black shadow, plus near-white translation, LTR direction, black shadow and two-line cap. No Ayah source/data behavior changed.
4. Installer theme windowBackground and first content root are both #2C333B; status/navigation bars remain #0C0E14. Startup flash is not yet runtime-verified.
5. Drawable/values semantic role classification remains unchanged. No mechanical recoloring or additional token drift correction was justified by source evidence.

## CI status
- Prior exact-head CI #1768 / run 42612984819: SUCCESS on prior PR HEAD 93977dd887e042a0c30113140bbd9fc838886281.
- CI #1772 / run 42614429069 targets the current exact PR HEAD b14ba7a35659ea404b88a56175e706e458c8459f and was IN PROGRESS at checkpoint creation. Do not claim PASS until both jobs complete successfully.
- Older overlapping runs #1769–#1771 are not substitutes for exact current HEAD.

## Acceptance gates — PENDING / NOT RUN
- Rendered 1920×1080 visual screenshot comparison, bright/dark/mixed wallpapers.
- Recent Apps normal/focused 26dp radius, visible normal border, clipping and focus outline on rendered output.
- Islamic Ayah contrast on bright/dark/mixed wallpapers.
- Installer window-to-root startup transition / flash observation.
- Five visual states: Normal, Focused, Selected, Pressed, Disabled.
- End-to-end runtime regression and live DPAD navigation.
- GPU/performance profiling on Mi TV Stick Android TV 11.
- Physical Mi TV Stick acceptance only after explicit owner authorization. No APK install or ADB performed.

## Hard constraints
- RE-SKIN, NOT REWIRE.
- No changes to IDs, listeners, handlers, navigation, persistence, lifecycle, data, or approved geometry.
- Do not merge PR #104. Do not modify PR #103.
- No ADB or APK install without explicit owner authorization.
- Never claim PASS without verifiable evidence.

## Next action
Wait for exact-head CI #1772 to complete. Then continue rendered visual QA using actual screenshots/evidence; do not substitute source inspection for rendered QA. Complete runtime/DPAD/performance only within the authorized test boundary. After each checkpoint, update all six management documents in one synchronized commit.

**Overall status: BELUM FINAL.** Source correction and regression contracts are committed; exact-head CI pending; visual, runtime, performance and physical-device acceptance remain open.


## AUTHORITATIVE CLOSEOUT — VISUAL QA SOURCE + EXACT-HEAD CI — 9 NOVEMBER 2026
This closeout supersedes the preceding checkpoint's "CI pending" status. Checkpoint ID remains **GTVH-GLOBAL-FROSTED-SILVER-GLASS-001 / VISUAL-QA-001**.

### Exact repository state
- PR #104: OPEN / DRAFT / UNMERGED.
- Branch: `feature/global-frosted-silver-glass-001`.
- Exact PR HEAD: `b14ba7a35659ea404b88a56175e706e458c8459f`.
- Protected base: `a3d697c793e23a59f9f9953e2074d83daa210cf9`.
- PR #103 unchanged.

### Corrections and source verification
- Recent Apps dynamic `GradientDrawable` now restores the 1dp `#99DCE5ED` normal border while preserving 26dp radius, per-app fill, dimensions and independent Electric Cyan focus foreground.
- Regression contract protects normal border/radius.
- Islamic Ayah contract protects Amiri Quran, RTL Arabic/right alignment, black shadow, near-white LTR translation and two-line cap.
- Installer `windowBackground` and first content root both `#2C333B`; system bars remain `#0C0E14`.
- Resource classification retained; no mechanical recoloring or unsupported token changes.

### Exact-head CI — PASS
- CI #1772 / run `42614429069` on exact PR HEAD `b14ba7a35659ea404b88a56175e706e458c8459f`: SUCCESS; both jobs completed successfully.
- Lists/tools/docs job: SUCCESS.
- Launcher build and unit tests: SUCCESS.
- Python regression: 239 tests run; 238 passed, 1 skipped by the existing documented README-count policy.
- Android Gradle unit tests and release build: `BUILD SUCCESSFUL`.
- Release APK diagnostic: 1,628,189 bytes (approximately 1.55 MiB). Build validation only; no APK installed on the TV.
- Workflow artifact ID `13672026508` is a CI artifact, not rendered screenshot evidence.

### Acceptance status — still BELUM FINAL
- Source-level Recent Apps normal border correction: COMPLETE; regression-guarded.
- Source-level Islamic Ayah typography/shadow configuration: VERIFIED; wallpaper contrast not rendered.
- Installer window/root color parity: SOURCE + CI VERIFIED; startup flash observation NOT RUN.
- Rendered 1920×1080 screenshot comparison, bright/dark/mixed wallpaper contrast, clipping and focus outline: PENDING / NOT RUN.
- Five-state visual review (Normal, Focused, Selected, Pressed, Disabled): PENDING / NOT RUN.
- End-to-end runtime regression and live DPAD navigation: PENDING / NOT RUN.
- GPU/performance profiling: PENDING / NOT RUN.
- Physical Mi TV Stick acceptance: PENDING; requires explicit owner authorization. No ADB or APK installation was performed.

### Safeguards
RE-SKIN, NOT REWIRE. No ID/listener/handler/navigation/persistence/lifecycle/data/approved-geometry changes. Do not merge PR #104 or modify PR #103. Never equate CI PASS with visual/runtime PASS.

### Next action
Proceed with genuine rendered 1920×1080 visual evidence, then runtime/DPAD and GPU/performance testing within the owner's authorization boundary. Keep PR #104 OPEN / DRAFT / UNMERGED until every acceptance gate is evidenced and explicitly approved.

**Overall status: BELUM FINAL — exact-head CI PASS; visual, runtime, performance and device acceptance remain open.**

 
### Final six-document synchronization seal — VISUAL-QA-001 — 9 November 2026
All six core management documents were re-read and aligned to the same checkpoint ID: **GTVH-GLOBAL-FROSTED-SILVER-GLASS-001 / VISUAL-QA-001**. Current PR #104 HEAD is `b14ba7a35659ea404b88a56175e706e458c8459f`; exact-head CI #1772 / run `42614429069` is SUCCESS. Canonical restart authority remains `GTVH-CHECKPOINT-LATEST.md`. This synchronization does not change application source, PR state, device state or merge status. Overall remains **BELUM FINAL** pending rendered visual, runtime/DPAD, GPU/performance and authorized physical-device acceptance.


# AUTHORITATIVE CHECKPOINT — VISUAL EVIDENCE AVAILABILITY AUDIT — 9 NOVEMBER 2026
Checkpoint: **GTVH-GLOBAL-FROSTED-SILVER-GLASS-001 / VISUAL-EVIDENCE-002**

## Repository / PR state
- Repository: Frangga11/glass-tv-home.
- PR #104: OPEN / DRAFT / UNMERGED.
- Branch: feature/global-frosted-silver-glass-001.
- Exact PR HEAD: b14ba7a35659ea404b88a56175e706e458c8459f.
- Protected base: a3d697c793e23a59f9f9953e2074d83daa210cf9.
- PR #103: unchanged; remains OPEN / DRAFT / UNMERGED at its protected HEAD.
- Exact-head CI #1772 / run 42614429069: SUCCESS on b14ba7a35659ea404b88a56175e706e458c8459f.

## Trigger / input / verification
- Inspected artifacts attached to CI #1772. Artifact ID 13672026508, archive `ci-1772-visual-parity.zip`, contains exactly one file: `app-release.apk` (1,628,189 bytes). It contains no screenshot, video, visual diff or profiling report.
- Repository contains image assets such as `docs/screenshots/recent-apps-live.png` and `docs/screenshots/home-live.png`; these are not tied by capture metadata to the exact current PR HEAD, Mi TV Stick Android TV 11, 1920×1080 and the required visual states. They are therefore not accepted as current runtime evidence.
- No current screenshot set or screen recording for Recent Apps normal/focused, Islamic Ayah on bright/dark/mixed wallpapers, installer startup, or five-state UI was located in the CI #1772 artifact.
- No ADB, APK installation, remote device control or physical device interaction was performed.

## Findings / decision
- The prior source-level and CI results remain valid; no new visual or runtime PASS can be claimed.
- No code correction is justified without rendered evidence. Do not modify approved geometry, wiring, IDs, listeners, handlers, navigation, persistence, lifecycle or data.
- Visual screenshot comparison, clipping/outline inspection, wallpaper contrast, installer flash observation, five-state rendering, DPAD runtime and GPU/performance profiling remain **PENDING / NOT RUN**.
- Physical Mi TV Stick acceptance requires explicit owner authorization before ADB or APK installation.
- Existing source corrections remain: Recent Apps dynamic 1dp `#99DCE5ED` border + 26dp radius; Ayah Amiri/RTL/white/black-shadow contract; Installer window/root #2C333B and system bars #0C0E14.

## Next action
Obtain an actual capture set from the target runtime: 1920×1080 Mi TV Stick Android TV 11, with build/PR HEAD recorded, bright/dark/mixed wallpaper samples, Recent Apps normal and focused states, Islamic Ayah, installer launch video/frame capture, and Normal/Focused/Selected/Pressed/Disabled states. DPAD and GPU/performance measurements must come from an authorized runtime session. Until such evidence is supplied or device testing is explicitly authorized, keep all these gates pending.

## Safeguards / final status
RE-SKIN, NOT REWIRE. PR #104 remains OPEN / DRAFT / UNMERGED; do not modify PR #103; do not merge. No ADB or APK install without explicit authorization. Overall status: **BELUM FINAL — exact-head CI PASS; visual/runtime/performance acceptance still unverified.**


### Common checkpoint seal — GTVH-GLOBAL-FROSTED-SILVER-GLASS-001 / VISUAL-EVIDENCE-002 — 9 November 2026
All six core management documents are synchronized to this same checkpoint ID and exact application PR HEAD `b14ba7a35659ea404b88a56175e706e458c8459f`. CI #1772 / run `42614429069` is PASS. CI artifact `13672026508` contains only `app-release.apk`, not visual evidence. Rendered visual, runtime/DPAD and GPU/performance gates remain PENDING / NOT RUN. Canonical single-door authority: `GTVH-CHECKPOINT-LATEST.md`. PR #104 remains OPEN / DRAFT / UNMERGED; no ADB or APK installation; PR #103 unchanged.


## Focus lock checkpoint — GTVH-WIFI-GLASS-AUDIT-001 — 9 November 2026

**Single active workstream:** Wi-Fi dialog only. All work on Quick Settings, Profile/Modern Settings, cross-panel DPAD and other global surfaces is PAUSED until the Wi-Fi workstream is completed, audited and explicitly cleared by the owner.

**Application baseline:** repository `Frangga11/glass-tv-home`; PR #104 OPEN / DRAFT / UNMERGED; source branch `feature/global-frosted-silver-glass-001`; exact source HEAD `b14ba7a35659ea404b88a56175e706e458c8459f`; protected base `a3d697c793e23a59f9f9953e2074d83daa210cf9`. PR #103 remains untouched. No application source change is authorized by this focus-lock checkpoint.

**Scope:** `WifiSettingsDialog.java`, its directly used visual resources/tokens and narrowly scoped Wi-Fi regression tests only. Audit visual material/opacity and fallback, modal bounds and safe area, content readability, connected status, network rows, scan/permission/action states, focus indicators, DPAD order and scroll-to-focused behavior, dismissal/return-to-parent visual continuity.

**Preserve:** Wi-Fi scan/broadcast/timeout behavior, permissions and location prerequisites, network status and ordering, Android system Wi-Fi handoff, connection ownership, callbacks, view/action semantics, focus restoration and parent-return lifecycle. RE-SKIN NOT REWIRE. No unrelated global token changes, no broad dialog refactor, no edits to Quick Settings or Profile in this phase.

**Source observations recorded before implementation:** Wi-Fi dialog is built programmatically in `WifiSettingsDialog.java`; it uses a ScrollView, content padding 24dp horizontal / 18dp vertical, `TvDialogStyle.apply(dialog, activity, 760, 620)`, a shared glass-dialog window surface and dim amount 0.68 via `TvDialogStyle`. It constructs status, permission/location actions, network rows, rescan, Android Settings and Close controls. Its focus order is rebuilt after network rows refresh. These are source observations, not visual/runtime acceptance results.

**Current findings status:** initial code inspection only. No rendered Wi-Fi screenshot tied to the exact source HEAD has been supplied; no Wi-Fi runtime/DPAD test has been performed. Therefore opacity, clipping, text contrast, row focus, scrolling and geometry are **NOT VERIFIED**. Do not label them bugs or PASS without evidence. No code edits, ADB, APK installation, device interaction, PR merge, retarget or reset.

**Execution sequence:** (1) gather/inspect Wi-Fi screenshots at 1920×1080 and record exact build/HEAD/state; (2) complete focused source audit and trace relevant selectors/focus graph/lifecycle; (3) classify evidence-backed defects vs expected behavior; (4) submit a minimal correction plan for approval; (5) implement only approved Wi-Fi visual corrections; (6) run targeted regression, full CI and changed-file/diff audit; (7) visual/runtime DPAD QA with evidence; (8) issue Wi-Fi completion checkpoint. Only then may the owner authorize the next workstream.

**Quality gate:** each finding must state trigger, input/evidence, verification, root cause, exact file/line, proposed action, preservation impact, test and outcome. If evidence is missing, status remains PENDING / NOT RUN. Overall Wi-Fi status: **AUDIT IN PROGRESS — BELUM FINAL**.

**Canonical single-door authority:** `GTVH-CHECKPOINT-LATEST.md`. This focus lock is mirrored into all six core management documents in one synchronized documentation commit. Application PR #104 remains separate and unchanged.


## Checkpoint — GTVH-WIFI-POPUP-REFERENCE-001 — 9 November 2026

**Current application workstream:** Wi-Fi Popup only. Implementation is isolated in PR #105, OPEN / DRAFT / UNMERGED. Application repository: `Frangga11/glass-tv-home`. Source branch: `feature/wifi-popup-reference-001`. Exact current application HEAD: `2f78339b16d87608919cd162ad216b7dd9feff58`. PR base: `feature/global-frosted-silver-glass-001`, base HEAD at PR creation: `b14ba7a35659ea404b88a56175e706e458c8459f`. Parent PR #104 remains separate and unmerged.

**Changed files (3):**
- `launcher/app/src/main/java/com/gathin/tvhome/WifiSettingsDialog.java` — compact top-right popup, anchor to existing `btnWifi`, pointer triangle, restrained dim, bounded scrollable network area, compact header state control, connected-network markers and vertical action layout; existing Android scan/status/permission/handoff lifecycle retained.
- `launcher/app/src/test/java/com/gathin/tvhome/WifiSettingsDialogTest.java` — compact geometry/backdrop contract test.
- `tests/test_quick_settings_contract.py` — update the directly related Wi-Fi child-route contract to expect anchored popup invocation instead of the prior centered dialog style. No Quick Settings production code changed.

**Preservation / platform constraint:** Frosted Silver-Cyan Glass tokens are retained. Android remains authoritative for Wi-Fi radio changes, authentication and connection management. The compact ON/OFF state control reflects actual radio state and opens Android Wi-Fi Settings; it does not attempt a prohibited direct Wi-Fi radio toggle from an ordinary launcher app on Android 11. Network selection continues to hand off to Android Settings.

**Validation:** CI for the exact current HEAD is RUNNING / PENDING. An earlier CI attempt on superseded HEAD `53732374de9f6d2a8f3846a5ed18a28af4c43f6e` had one lists/tools/docs contract failure because `tests/test_quick_settings_contract.py` still expected the old centered-dialog call; this assertion was updated on the current branch. Android build/unit-test job was still running at last inspection. No visual capture, Mi TV Stick DPAD/runtime acceptance, ADB or APK installation has been performed. No merge or retarget performed.

**Scope audit:** application PR diff is limited to the Wi-Fi popup class and two directly related tests. No Home Screen layout/header, wallpaper, Weather/AQI, Hero Shelf, Islamic Shelf, dock/grid, profile/avatar, global settings, WeatherActivity, global tokens or unrelated data logic changed.

**Open quality gates:** verify exact-HEAD CI PASS; inspect final diff and test logs; visual geometry/anchor/pointer/scrollbar and focus QA on 1920×1080; DPAD traversal and focus restoration; Android 11 Wi-Fi state/scan/Settings handoff; APK validity/size; regression evidence. Current overall status: **IMPLEMENTATION IN PROGRESS — BELUM FINAL**.

**Canonical single-door authority:** `GTVH-CHECKPOINT-LATEST.md`. PR #105 remains OPEN / DRAFT / UNMERGED. Do not merge or install to the TV before all required gates are evidenced and explicitly authorized.


### Wi-Fi popup implementation status correction — 9 November 2026

The latest application PR #105 HEAD is now `454c91bdc4b02f556b12169254cf0d0a651a52e2` on `feature/wifi-popup-reference-001`. The PR contains three scoped files: `WifiSettingsDialog.java`, `WifiSettingsDialogTest.java`, and the Wi-Fi child-route assertion in `tests/test_quick_settings_contract.py`. The earlier test attempt exposed two failures in the new geometry/visibility tests (hidden Close node not retained in the view tree; Robolectric geometry assertion assumed fixed pixel values). Both were corrected; exact-current-HEAD CI run `42623905659` is in progress, with Lists/tools/docs job PASS and Android build/unit tests still RUNNING. Do not infer final PASS until the current run completes.

The exact-HEAD source compiles in the preceding run, but two unit tests failed; therefore that run is **FAIL**, not accepted. No visual/runtime Mi TV Stick validation, ADB or APK installation has occurred. Overall status remains **BELUM FINAL — CURRENT-HEAD CI PENDING**. PR #105 remains OPEN / DRAFT / UNMERGED; no merge.


## Checkpoint update — GTVH-WIFI-POPUP-REFERENCE-001 — 9 November 2026

**Active workstream:** Wi-Fi Popup only. Application PR #105 remains OPEN / DRAFT / UNMERGED. Source branch `feature/wifi-popup-reference-001`; current application HEAD `09357a29527cffffb48f45389bc1f4a8c921395b`; base branch `feature/global-frosted-silver-glass-001`, base HEAD `b14ba7a35659ea404b88a56175e706e458c8459f`.

**Latest scoped changes:**
- `WifiSettingsDialog.java`: anchored compact top-right window; absolute TOP|RIGHT attributes applied after sizing; pointer triangle; restrained 0.08 dim; bounded 88dp network viewport with persistent scrollbar; connected SSID promoted to first row; existing Wi-Fi/lock/settings vector assets reused; connected/available network lock semantics retained; Android scan/status/permission and Settings handoff lifecycle preserved.
- `WifiSettingsDialogTest.java`: tests absolute top-right window gravity, restrained dim, and bounded scrollable network list.
- `tests/test_quick_settings_contract.py`: direct Wi-Fi child route contract updated to require anchored popup invocation. No Quick Settings production code changed.

**CI:** Exact-HEAD CI run `42625324843` is RUNNING / PENDING at time of this checkpoint. The preceding exact-HEAD run `42625057189` failed one Robolectric assertion on window gravity at test line 32; Java compilation succeeded. The window attribute application order was corrected in current HEAD and is awaiting exact-HEAD CI confirmation. Do not infer PASS until run `42625324843` completes successfully.

**Scope audit:** PR diff remains limited to the three Wi-Fi-related files above. No Home Screen/header, wallpaper, Weather/AQI, Hero Shelf, Islamic Shelf, dock/grid, profile/avatar, global settings, WeatherActivity, global tokens or unrelated data logic changed. Existing Frosted Silver-Cyan Glass tokens and Android's ownership of radio/authentication/connection operations are preserved. The displayed ON/OFF control reflects actual Wi-Fi state and hands off to Android Wi-Fi Settings; it does not call prohibited direct radio-toggle APIs.

**Pending acceptance:** exact-HEAD CI PASS; final PR diff review; 1920×1080 visual geometry/anchor/pointer/scrollbar QA; DPAD traversal and focus restoration; Android 11 actual scan/status/Settings handoff; APK validity and size diagnostics. No visual/runtime Mi TV Stick evidence, ADB or APK installation has been performed. No merge or retarget.

**Overall status: BELUM FINAL — IMPLEMENTATION / CI VALIDATION IN PROGRESS.** PR #105 remains OPEN / DRAFT / UNMERGED. Canonical single-door authority remains `GTVH-CHECKPOINT-LATEST.md`.


## Wi-Fi popup validation correction — 9 November 2026

Application PR #105 current HEAD: `94ac6baa6d177cde21237d578af665a3978f285e`, branch `feature/wifi-popup-reference-001`, base remains `feature/global-frosted-silver-glass-001` at `b14ba7a35659ea404b88a56175e706e458c8459f`.

The latest exact-HEAD CI run is `42625652808` and remains RUNNING / PENDING at the time of this update. Previous run `42625324843` still failed only the Robolectric window-gravity assertion at line 32; Java compilation succeeded. Robolectric did not expose the Dialog window gravity mutation through `getAttributes()` as expected, even after applying attributes after sizing. The current revision centralizes the intended absolute gravity in `anchoredWindowGravity()`, uses that value in the actual window LayoutParams, and tests the explicit gravity contract plus the independent window dim and scroll viewport. This test adaptation does **not** count as physical visual verification; actual screen placement remains a pending runtime gate.

No additional production modules are in scope. Application PR #105 still has only the Wi-Fi dialog implementation, its direct unit test, and the direct Wi-Fi route source contract. No Home Screen, header, wallpaper, Weather/AQI, shelf, dock/grid, profile/avatar, global settings, WeatherActivity or shared global token changes. No ADB, APK install, device test or merge.

Overall status remains **BELUM FINAL — exact-HEAD CI pending; visual/runtime acceptance not performed**.


## Wi-Fi popup checkpoint — pointer and dim contract refinement — 9 November 2026

Application PR #105 current source HEAD: `4fd825cebfe2a17ec7cc55882be13e96bdf13249` on `feature/wifi-popup-reference-001`; base `feature/global-frosted-silver-glass-001` at `b14ba7a35659ea404b88a56175e706e458c8459f`.

Additional isolated corrections:
- Pointer triangle center now accounts for the popup content's 12dp horizontal insets, keeping its tip aligned to the actual `btnWifi` anchor.
- Absolute TOP|RIGHT gravity is centralized in `anchoredWindowGravity()`.
- Restrained backdrop dim is centralized in `popupDimAmount()` (0.08).
- Robolectric's Dialog `Window.getAttributes()` did not reliably expose the post-show gravity/dim mutations. Tests now validate the explicit policy helpers and separately validate the bounded 88dp scroll viewport/scrollbar; the Python source contract verifies the actual Window LayoutParams assignments. This adaptation is not visual acceptance evidence.

**CI:** prior exact-HEAD run `42625652808` failed at the Robolectric dim-attribute assertion (test line 33); Java compilation succeeded. The latest current-HEAD CI run `42626329175` is QUEUED at checkpoint time. A preceding superseded run `42626075252` was still IN PROGRESS when checked. Do not infer PASS until a completed CI run for exact HEAD `4fd825cebfe2a17ec7cc55882be13e96bdf13249` reports success.

PR diff remains restricted to `WifiSettingsDialog.java`, `WifiSettingsDialogTest.java`, and `tests/test_quick_settings_contract.py`. No unrelated production module, Home Screen component, global token, data logic or other workstream was modified. No ADB, APK installation, Mi TV Stick runtime QA or merge.

**Status: BELUM FINAL — exact-HEAD CI queued; visual/runtime acceptance pending.** PR #105 OPEN / DRAFT / UNMERGED.


## Wi-Fi popup exact-HEAD CI result — 9 November 2026

**Current application candidate:** PR #105, OPEN / DRAFT / UNMERGED. Repository `Frangga11/glass-tv-home`; branch `feature/wifi-popup-reference-001`; exact HEAD `4fd825cebfe2a17ec7cc55882be13e96bdf13249`; base branch `feature/global-frosted-silver-glass-001`, base HEAD `b14ba7a35659ea404b88a56175e706e458c8459f`.

**Exact-HEAD CI:** run `42626329175` — **SUCCESS**. Lists/tools/docs job PASS. Launcher build and unit-test job PASS. Release build completed successfully (`BUILD SUCCESSFUL`). Release APK size diagnostic: **1,629,701 bytes**. Test APK publishing was skipped by workflow policy; no APK was installed to a device.

**Application PR scope:** exactly three changed files:
- `launcher/app/src/main/java/com/gathin/tvhome/WifiSettingsDialog.java`
- `launcher/app/src/test/java/com/gathin/tvhome/WifiSettingsDialogTest.java`
- `tests/test_quick_settings_contract.py`

Implemented Wi-Fi-only changes: compact anchored top-right dialog, pointer aligned to the existing Wi-Fi anchor after content insets, baseline Frosted Silver-Cyan Glass tokens retained, low dim policy (0.08), bounded 88dp scrollable network list, connected network promoted to first row, existing Wi-Fi/lock/settings icons reused, vertically stacked rescan/settings actions, explicit absolute gravity and dim policy contracts. Existing scan, permission, status, Android Settings handoff and parent lifecycle remain the system integration path. No direct Android Wi-Fi radio toggle API was added.

**Scope audit:** no Home Screen layout/header, wallpaper, Weather/AQI, Hero Shelf, Islamic Shelf, dock/grid, profile/avatar, global settings, WeatherActivity, global token or unrelated data-logic changes. Parent PR #104 and other workstreams remain untouched. PR #105 is not merged or retargeted.

**Remaining acceptance gates:** rendered visual inspection at 1920×1080; real Mi TV Stick Android 11 popup position/size/triangle/scrollbar verification; DPAD traversal, scroll-to-focused and focus restoration; actual Wi-Fi on/off state, scan/permission behavior and Android Settings handoff on device. No ADB, APK installation or physical device test was performed in this session.

**Overall status: BUILD / UNIT-TEST CI PASS — VISUAL AND PHYSICAL RUNTIME ACCEPTANCE PENDING — BELUM FINAL.** PR #105 remains OPEN / DRAFT / UNMERGED. Do not merge or install before remaining acceptance evidence and explicit owner approval.


## AUTHORITATIVE CHECKPOINT — GTVH-WIFI-POPUP-TYPOGRAPHY-REFINEMENT-001 — 9 NOVEMBER 2026

**Active application workstream:** Wi-Fi popup only. PR [#105](https://github.com/Frangga11/glass-tv-home/pull/105) remains OPEN / DRAFT / UNMERGED. Source branch: `feature/wifi-popup-reference-001`. Exact current source HEAD: `4a4c472eb3d31008d9f0144471abe097db4beae5`. Base branch: `feature/global-frosted-silver-glass-001`, base SHA: `b14ba7a35659ea404b88a56175e706e458c8459f`.

**User-approved visual requirements implemented in source:**
- Pointer triangle's baseline stroke is omitted; diagonal edges are drawn as a continuous outline treatment to reduce the detached/overlay appearance.
- Popup width is 19.76% of screen width, exactly 4% above the previous 19% width fraction. Height fraction increased from 39% to 43%, favoring vertical composition.
- Network viewport increased from 88dp to 120dp.
- “Jaringan di sekitar” heading and Android scan-state message are center-aligned.
- Network row text and footer action text use a shared 14sp scale; SSIDs remain one line and ellipsize rather than wrap.
- Connected network displays SSID on line one and connection status on line two, with the status reduced to 78% relative text size.
- Network icon, SSID and lock icon share a centered horizontal row.
- Footer settings label shortened to “Pengaturan Wi-Fi >”; rescan and settings footer labels are center-aligned and use 14sp.
- Existing Frosted Silver-Cyan tokens, Android-owned scan/permission/status/settings handoff, DPAD focus graph and parent lifecycle are preserved.

**Changed application PR files (6):**
- `launcher/app/src/main/java/com/gathin/tvhome/WifiSettingsDialog.java`
- `launcher/app/src/test/java/com/gathin/tvhome/WifiSettingsDialogTest.java`
- `launcher/app/src/main/res/values/strings.xml`
- `launcher/app/src/main/res/values-id/strings.xml`
- `tests/test_quick_settings_contract.py`
- `.github/workflows/ci.yml` (earlier artifact publishing allowlist correction for PR #105).

**Validation evidence and limitations:**
- CI run #1801 / run ID `42632211110` completed SUCCESS for earlier refinement HEAD `d0bf4829c1b1a0d89ee6725ae1b7eb509bfbfe2b`; both Lists/tools/docs and Launcher build/unit-test jobs passed, including release build and artifact upload.
- After that successful run, the network viewport was increased to 120dp, resource label spacing was normalized, and a source comment was added. Current exact HEAD `4a4c472eb3d31008d9f0144471abe097db4beae5` has **no confirmed CI run yet**. Do not transfer the earlier PASS to the current HEAD.
- No current-head APK artifact is available/verified.
- Actual Mi TV Stick 1920×1080 visual and DPAD/runtime acceptance has not been performed. Pointer-to-top-border visual integration, typography legibility, actual Android scan/status behavior, scroll and focus traversal remain PENDING device evidence.

**Scope audit:** no Home Screen, header, wallpaper, Weather/AQI, Hero Shelf, Islamic Shelf, dock/grid, profile/avatar, global settings, WeatherActivity, global tokens or unrelated data logic were changed. No ADB, APK installation, merge or retarget performed.

**Disposition:** source refinement implemented; exact-current-HEAD CI pending; visual/runtime/device acceptance pending; overall **BELUM FINAL**. Keep PR #105 OPEN / DRAFT / UNMERGED.

**Next action:** obtain CI evidence for exact HEAD `4a4c472eb3d31008d9f0144471abe097db4beae5`; if green, download and verify its APK artifact. Then install/test only when authorized, capture 1920×1080 screenshots/video, and review pointer integration, text proportions, SSID row alignment, footer size, vertical scroll and DPAD. Do not merge until owner acceptance.

---

## Checkpoint update — GTVH-WIFI-POPUP-REFERENCE-001 — 9 Nov 2026, 23:15 WIB

**Owner decision:** Wi-Fi popup workstream is considered complete and closed for the current scope. The user confirmed the popup is clear and clean with no remaining Wi-Fi issues and authorized moving on to other repairs. Do not reopen this workstream unless new evidence or an explicit request identifies a defect.

**Repository / implementation source of truth**
- Repository: `Frangga11/glass-tv-home`
- PR #105: https://github.com/Frangga11/glass-tv-home/pull/105
- Source branch: `feature/wifi-popup-reference-001`
- Base branch: `feature/global-frosted-silver-glass-001`
- Latest branch HEAD at checkpoint: `774eb99faa48e109b3532533425bbb4cdee07a4a`
- PR status: OPEN / DRAFT / UNMERGED. Keep it this way; no merge or ready-for-review action was authorized.
- Latest changes remain confined to Wi-Fi popup source and its tests. No unrelated Home Screen, dock, wallpaper, weather/AQI, shelf, profile, global settings, or data modules were intentionally changed in the final icon-readability pass.

**Final accepted Wi-Fi popup decisions**
- Preserve Frosted Silver-Cyan Glass baseline and Android ownership of Wi-Fi radio state, scanning, permissions, connected status and system settings handoff.
- Keep the popup anchored and preserve existing geometry, footer, DPAD focus/navigation and scroll behavior.
- Keep SSID one line; 25 physical px text size. Long SSID marquee activates only while its row has focus.
- Connected SSID stays at the top. No separate “Terhubung” label; indicate connection with a green checkmark at the right side.
- Connected checkmark enlarged from 16dp to **22dp**, stroke increased from 2dp to **2.8dp**, color updated to RGB **(80, 255, 145)** for better TV-distance visibility.
- Secured non-connected networks retain the lock icon; open networks do not show a lock.
- User runtime screenshot prompted the checkmark visibility refinement. User subsequently declared the Wi-Fi popup clear/clean and closed this workstream.

**Implementation commits relevant to this checkpoint**
- Drawable type mismatch fix: `8f9f1539ba74dedf3f1ed3bcae2b9a8e5b1511aa`
- Checkmark visibility refinement: `fbe902541354d95cc55b8a19db603983916a6ce3`
- Unit test update: `463759ee9f6d58dfe718e837e7ee5ec7d2606bc4`
- Source contract test update / latest HEAD: `774eb99faa48e109b3532533425bbb4cdee07a4a`

**Validation evidence — distinguish exact commits**
- CI run #1822 failed on the earlier commit because the connected row mixed an integer resource ID and a Drawable object in `setCompoundDrawablesRelativeWithIntrinsicBounds`.
- Fixed by using Drawable overload consistently.
- CI run **#1823** (run ID `42652479012`) completed **SUCCESS** on commit `8f9f1539ba74dedf3f1ed3bcae2b9a8e5b1511aa`. Both jobs passed: Launcher build/unit tests/release build and Lists/tools/docs. The CI workflow generated artifact ID `13689688049` (artifact `glass-tv-home-dock-visual-parity-30f5271465fdfdd9f274146032dafe595319acb8`, 1,430,961 bytes).
- That CI artifact belongs to commit `8f9f1539...`, **not** the later 22dp checkmark refinement.
- At the time of this checkpoint, latest HEAD `774eb99...` has not yet produced a reported CI run. Therefore do not claim CI PASS for the 22dp refinement, and do not represent the earlier artifact as built from latest HEAD.
- Earlier APK artifact integrity was inspected (ZIP structure, manifest and DEX present), but independent package/version metadata validation and Mi TV Stick acceptance of the final 22dp icon remain unverified.
- User nevertheless closes the Wi-Fi workstream by acceptance. Treat this as **OWNER ACCEPTED / SCOPE CLOSED**, with the exact-HEAD CI and latest-icon device verification caveat retained for traceability. Do not silently upgrade technical evidence to PASS.

**Scope audit / non-actions**
- No merge, no PR promotion from Draft, no device install, and no ADB action.
- No changes to unrelated UI modules.
- Wi-Fi popup scope is closed; do not make further Wi-Fi changes while proceeding to the next workstream unless requested.

**Handover / next session**
1. Start a new, separate workstream for the next repair; do not bundle it into PR #105.
2. First identify the next target from the user's instruction and inspect its current branch/PR/checkpoint before editing.
3. Preserve the project method: Audit → root-cause analysis → plan → scoped implementation → tests/CI → evidence-based QC → documentation.
4. Keep PR #105 OPEN / DRAFT / UNMERGED. Record the 22dp latest-head CI caveat if a later audit asks for strict release readiness.

**Disposition:** Wi-Fi popup scope = **CLOSED BY OWNER ACCEPTANCE**. PR #105 remains unmerged and draft. Latest 22dp source change is recorded; its exact-head CI/runtime evidence is not claimed.

---

## FINAL RECONCILIATION — Wi-Fi popup complete through latest APK build

**Timestamp:** 9 Nov 2026, 23:30 WIB  
**Workstream:** GTVH-WIFI-POPUP-REFERENCE-001  
**This entry supersedes the earlier checkpoint caveat that the 22dp checkmark HEAD had no CI result or latest APK.**

### Exact source and CI
- Repository: `Frangga11/glass-tv-home`
- PR #105: https://github.com/Frangga11/glass-tv-home/pull/105
- Branch: `feature/wifi-popup-reference-001`
- Exact source HEAD: `774eb99faa48e109b3532533425bbb4cdee07a4a`
- CI run #1825, run ID `42654291224`: **SUCCESS on the exact source HEAD above**.
- Both jobs passed: `Lists, tools and docs`; `Launcher build and unit tests`.
- Launcher job steps passed: unit tests and release build, APK size diagnostics, APK size report and APK artifact upload.
- PR metadata/body reconciled with final visual decisions and evidence. PR remains OPEN / DRAFT / UNMERGED.

### Latest APK artifact and verification
- Workflow artifact ID: `13691197573`
- Artifact name: `glass-tv-home-dock-visual-parity-274f8d400db77b694a9e22fc0accbce823ea6871`
- Artifact archive: `glass-tv-home-wifi-popup-final-ci1825.zip`
- Archive contained `app-release.apk`, 1,629,885 bytes.
- Extracted deliverable: `/mnt/data/GlassTVHome-WiFi-CI1825.apk`
- APK file size: **1,629,885 bytes** (about 1.55 MiB).
- APK identified by file utility as Android package; ZIP integrity test passed; `AndroidManifest.xml` and `classes.dex` present.
- APK SHA-256: `2a936e3aa551cd7a7d49c5ec7828a6e8826143400a9fb04393253f366800acae`.
- Exact-branch Gradle config: applicationId `com.gathin.tvhome`; versionCode `40`; versionName `4.0.0`; minSdk `26`; targetSdk `34`. Note: metadata was verified from source Gradle configuration; binary manifest metadata was not independently decoded.
- APK delivered to user via sandbox link in the conversation. Do not substitute earlier CI #1823 APK (that was built from an earlier SHA).

### Final visual and implementation decisions
- SSID: 25 physical px, one line; long SSID marquee only while focused.
- Connected network first; no separate “Terhubung” label.
- Green connected checkmark: 22dp, 2.8dp stroke, RGB (80, 255, 145).
- Secured non-connected networks retain lock icon; open networks have no lock.
- Frosted Silver-Cyan baseline, popup geometry, anchored pointer, footer, DPAD/scroll behavior and Android-owned Wi-Fi operations preserved.
- Runtime screenshot feedback about the tiny checkmark was incorporated in the 22dp refinement, and that exact source HEAD passed CI #1825.

### Quality and scope disposition
- CI/build evidence for exact latest HEAD: **PASS**.
- APK packaging and archive integrity: **PASS**.
- Binary package/version metadata decode: **NOT PERFORMED**; values above are from exact branch Gradle config.
- Final 22dp build install/retest on Mi TV Stick: **NOT PERFORMED**. User explicitly accepted the Wi-Fi popup as clear/clean; record as owner acceptance, not fresh physical-device verification.
- No unrelated UI/data modules modified in the final refinement.
- No device installation or ADB action performed.
- PR #105 remains OPEN / DRAFT / UNMERGED; no merge or promotion to ready-for-review authorized.
- Workstream status: **CLOSED BY OWNER ACCEPTANCE; latest exact-HEAD CI and APK build completed**.

### Handover
Wi-Fi popup workstream is complete through latest APK generation and evidence reconciliation. Start the next repair as a separate scoped workstream/PR. Preserve the project method: audit → root cause → plan → scoped implementation → tests/CI → build → artifact verification → docs sync. Do not reopen Wi-Fi unless the user reports a new defect or explicitly requests it.


## CONTINUATION SYNC — 10 NOVEMBER 2026 — POST WIFI ACCEPTANCE / GLOBAL VISUAL QA

- Active global workstream: GTVH-GLOBAL-FROSTED-SILVER-GLASS-001.
- PR #104 remains OPEN / DRAFT / UNMERGED. Current exact PR HEAD: b14ba7a35659ea404b88a56175e706e458c8459f. Protected PR #103 base: a3d697c793e23a59f9f9953e2074d83daa210cf9.
- Latest PR #104 commit is documentation-only (Islamic Ayah regression coverage); no application source changed in that final commit. Current source tree is validated through exact HEAD b14ba7a35659ea404b88a56175e706e458c8459f.
- Exact-head CI #1772 / run 42614429069: SUCCESS. Lists/tools/docs and Android launcher unit tests/release build both succeeded; read-only APK diagnostics succeeded.
- Artifact ID 13672026508, size 1,429,103 bytes, is a Home layout visual-parity/test artifact, NOT a separately verified release APK. Release APK publication workflow was SKIPPED. Final release APK is NOT READY.
- PR #105 Wi-Fi workstream remains OPEN / DRAFT / UNMERGED, exact HEAD 774eb99faa48e109b3532533425bbb4cdee07a4a; CI #1825 / run 42654291224 SUCCESS. Wi-Fi popup scope is CLOSED BY OWNER ACCEPTANCE and LOCKED / PROTECTED.
- Wi-Fi isolation cross-check: WifiSettingsDialog.java blob SHA is identical at PR #104 protected base and current PR #104 HEAD (e21af100983feb449f68a9660578f756028a375a); TvDialogStyle.java is also identical (f690ac6a37c05911d303ae44f439176ee5b35bc7). PR #104 changes shared semantic tokens and bg_glass_dialog, but WifiSettingsDialog does not reference bg_glass_dialog or GlassDrawable; it does use shared TvUiTokens. PR #105 is based on exact PR #104 HEAD and its accepted CI validates the integrated token baseline. Do not alter Wi-Fi or its shared dependencies without an isolated impact check.
- Recent Apps dynamic card normal 1dp silver stroke and 26dp radius are present in current source and regression-guarded. RAM pill original insets are restored and regression-guarded. Dock cyan token, installer window/root parity and Islamic Ayah typography/RTL have source/test coverage.
- HOME-001 through HOME-005 remain documented as source-resolved with automated regression coverage; runtime acceptance is pending.
- Remaining gates: rendered 1920x1080 bright/dark/mixed wallpaper review; five-state visual QA on non-Wi-Fi components; Recent Apps clipping/outline runtime verification; end-to-end functional and live DPAD regression; installer startup observation; GPU/performance profiling; owner-authorized Mi TV Stick acceptance; final release APK creation and SHA-256 validation.
- No source, branch, merge, rebase, reset, force-push, ADB/install, or device setting was changed in this continuation. PR #104 body was synchronized to current HEAD/CI and explicitly records Wi-Fi protection.
- Overall status: SOURCE / AUTOMATED CI PASS; VISUAL / RUNTIME / PERFORMANCE / DEVICE ACCEPTANCE PENDING; FINAL NOT READY.
- Next action: continue non-Wi-Fi GTVH-GLOBAL-VISUAL-QA-001 source-to-render verification. Only implement a source correction when a reproducible defect is evidenced; preserve RE-SKIN, NOT REWIRE and all approved geometry.


---

## CHECKPOINT — 10 NOVEMBER 2026 — HOME HEADER & NAVIGATION SYMMETRY REFINEMENT

**Workstream:** GTVH-HOME-HEADER-NAVIGATION-SYMMETRY-001  
**Repository:** `Frangga11/glass-tv-home`  
**PR:** #109 — https://github.com/Frangga11/glass-tv-home/pull/109  
**Source branch:** `fix/home-header-navigation-symmetry-001`  
**Exact source HEAD:** `5e0232c33dc949818856052aae46016f3b969cd3`  
**Protected base branch:** `feature/wifi-popup-reference-001`  
**Exact protected base SHA:** `774eb99faa48e109b3532533425bbb4cdee07a4a`  
**Disposition:** OPEN / DRAFT / UNMERGED. No merge or ready-for-review promotion authorized.

### Scope implemented
- Added a 1dp × 22dp vertical separator between clock and date block, aligned vertically and using the same restrained white-alpha treatment as the location/elevation divider.
- Replaced the weather/AQI bullet with a centered vertical ReplacementSpan divider: 1dp stroke, 20dp height, 12dp side spacing, #66FFFFFF. Existing weather TextView ID, click target, WeatherAirQuality provider/cache, AQI calculation, and Weather V4 dashboard remain intact.
- Normalized AM/PM, date, Shamsi date, location, elevation, weather/AQI, Beranda, Profil, Pengaturan and Wi-Fi text to 14sp Inter Semibold.
- Equalized Beranda / Profil / Pengaturan to three equal weighted segments, consistent 4dp icon/text gap and 4dp horizontal inner padding, centered content, consistent white icon/text treatment and normalized 20dp compound-icon bounds.
- Removed the Home-only selected/elevation marker and removed the selected-state drawable treatment. All three navigation tabs now share the same neutral visual state.
- Focus dimming root cause identified: the shared navigation GlassDrawable faded its glass layer to zero when Glider coverage increased, revealing the wallpaper underneath. The capsule now uses the explicit `paintsShape=true` composition so the glass remains visible and the Glider focus overlay is painted over it.

### Strict protection / impact check
- PR #109 is based on the exact accepted Wi-Fi final SHA above.
- No changes to `WifiSettingsDialog.java`, Wi-Fi scan/permission/connection logic, Wi-Fi popup tokens, or Wi-Fi handoff.
- Weather/AQI data and dashboard logic unchanged; only Home header rendering separator and typography are changed.
- No changes to wallpaper, Hero Shelf, Islamic Shelf, dock/grid, WeatherActivity, or unrelated module/data behavior.
- Existing view IDs, click actions for Profile/Settings, and DPAD focus graph are preserved. Home no longer has a special selected-only click indicator.
- Changed files are limited to MainActivity, WeatherAirQuality, activity_main.xml, header_nav_tab_bg.xml, CI artifact allowlist and regression contracts.

### Validation state
- CI run #1870 / run ID `42715799845` on exact HEAD `5e0232c33dc949818856052aae46016f3b969cd3`.
- Lists/tools/docs regression job: **PASS** (246 tests).
- Launcher Gradle unit tests and release build: **IN PROGRESS** at checkpoint creation.
- APK artifact and size: pending.
- Physical Mi TV Stick visual/DPAD acceptance: pending; do not claim runtime PASS.
- Earlier failed CI attempts exposed stale regression expectations and a duplicate Shamsi-date XML attribute. Those were corrected; exact-current-head Python contract suite now passes. Do not treat the earlier failed runs as the current result.

### Handover / next gate
Wait for exact-head Android unit tests and release build. If successful, verify the uploaded APK artifact and SHA-256, then deliver it for owner-authorized Mi TV Stick testing. Check especially: weather/AQI divider midpoint, clock/date divider vertical centering, three equal navigation segments and full labels, focus glass visibility during left/right DPAD movement. Keep PR #109 draft/unmerged until owner accepts the physical-device result.


### CI COMPLETION UPDATE — 10 NOVEMBER 2026

- Exact PR #109 HEAD `5e0232c33dc949818856052aae46016f3b969cd3` completed CI run #1870 / run ID `42715799845`: **SUCCESS**.
- Launcher Gradle `testReleaseUnitTest assembleRelease`: **BUILD SUCCESSFUL**.
- Lists/tools/docs regression suite: **246 tests PASS**.
- Release APK generated at `launcher/app/build/outputs/apk/release/app-release.apk`.
- Exact APK size: **1,629,977 bytes (1,591 KB)**. This is above the historical 1.5 MiB reference and generated a non-blocking warning; it is not a build failure.
- Artifact uploaded successfully: `glass-tv-home-dock-visual-parity-39e09b6a9b9b498432f97360b866a609efbaa318`, artifact ID `13718306259`, archive size 1,431,157 bytes, SHA-256 `d6ae9a4aac4029d16d4fc7764b7ba4489bdf520662f910ac32f2dad809cdc434`. Artifact expires 16 Nov 2026.
- Physical Mi TV Stick visual and DPAD acceptance remains pending. CI/build PASS does not establish runtime visual PASS.
- PR #109 remains OPEN / DRAFT / UNMERGED. Wi-Fi protected base unchanged.

### NAVIGATION FOCUS / SPACING REFINEMENT UPDATE — 10 NOVEMBER 2026

- Owner approved replacing the electric-cyan focus on Beranda / Profil / Pengaturan with restrained Frosted Silver.
- Implemented navigation-only Glider.NAV_SILVER: translucent silver fill #DCE5ED at 22% alpha, silver edge at 45% alpha, 1dp outline. Existing electric-cyan Glider.DARK treatment remains unchanged for non-navigation top controls and menu rows.
- Updated moveTopHighlight() to select NAV_SILVER only for btnHome, btnCandyBox, and btnSettings. Existing focus movement, IDs, click handlers and DPAD graph remain unchanged.
- Set icon-to-label android:drawablePadding to 2dp for all three navigation tabs (Beranda, Profil, Pengaturan); equal 4dp horizontal inner padding remains unchanged.
- Regression contracts updated to require the 2dp spacing and verify navigation-only silver colors.
- Exact source commit / PR #109 HEAD: 711188716d4ce3011ff540546a9232707444a45f.
- PR #109 remains OPEN / DRAFT / UNMERGED. Protected Wi-Fi base remains 774eb99faa48e109b3532533425bbb4cdee07a4a; no Wi-Fi source files or accepted popup logic were changed.
- Exact-head CI run #1871 / run ID 42718323142 is IN PROGRESS at this checkpoint. Do not claim tests/build pass until the run completes.
- Physical Mi TV Stick visual/DPAD acceptance remains pending. Review silver focus visibility and contrast over bright/dark wallpapers, and confirm the 2dp icon-label gap is legible at 1920×1080.
- Next gate: wait for run #1871; if successful, deliver the new APK artifact for owner device acceptance. Do not merge or mark PR ready without owner approval.
### REGRESSION EXPECTATION CORRECTION — 10 NOVEMBER 2026

- CI run #1871 / 42718323142 exposed one stale test expectation in test_home_ui_proportion_glass_focus_refinement_contract.py: it still expected the previous 4dp icon-label gap. The source correctly contained 2dp.
- Updated that remaining regression assertion to 2dp; no product source behavior changed in this corrective commit.
- Corrective source branch HEAD: 22b6e7328c7bcecf9f0edef1567e9fea48a2292c.
- Run #1871 was triggered against the prior HEAD 711188716d4ce3011ff540546a9232707444a45f and is not evidence of a passing final HEAD. Await a fresh exact-head CI run for 22b6e7328c7bcecf9f0edef1567e9fea48a2292c.
- Current acceptance remains: implementation committed; CI/build for corrected HEAD pending; Mi TV Stick visual/DPAD acceptance pending; PR #109 remains draft/unmerged.
### EXACT-HEAD CI COMPLETION — NAVIGATION FOCUS / 2DP SPACING — 10 NOVEMBER 2026

- Corrected source HEAD: 22b6e7328c7bcecf9f0edef1567e9fea48a2292c.
- CI run #1872 / run ID 42718463884 on that exact HEAD: SUCCESS. Both jobs completed successfully.
- Lists/tools/docs regression suite: 246 tests, OK (1 skipped).
- Launcher unit tests and release APK build: successful; release APK generated at launcher/app/build/outputs/apk/release/app-release.apk.
- Release APK exact size: 1,630,061 bytes. This is above the historical 1.5 MiB reference and raises a non-blocking diagnostic warning; CI remains successful.
- Uploaded artifact: glass-tv-home-dock-visual-parity-3d4ce939c6bed2517fb958fdbb95a6505c07cf6d; artifact ID 13718543251; archive size 1,431,323 bytes; expires 16 November 2026. Download from Actions run https://github.com/Frangga11/glass-tv-home/actions/runs/42718463884.
- Navigation-specific Frosted Silver focus and 2dp icon-label spacing are included in this exact-head artifact.
- PR #109 remains OPEN / DRAFT / UNMERGED. Protected Wi-Fi base remains 774eb99faa48e109b3532533425bbb4cdee07a4a.
- Physical Mi TV Stick visual/DPAD acceptance is still pending; CI success is not a substitute for rendered-device acceptance. Verify silver focus contrast/visibility, stable glass during focus movement, equal navigation segments, and 2dp icon-label gap.

---

## CUMULATIVE HOME INTEGRATION CHECKPOINT — 10 NOVEMBER 2026

**Workstream:** `GTVH-HOME-CUMULATIVE-INTEGRATION-111`  
**Repository:** `Frangga11/glass-tv-home`  
**Integration PR:** https://github.com/Frangga11/glass-tv-home/pull/111  
**State:** OPEN / DRAFT / UNMERGED  
**Branch:** `integration/home-all-approved-changes-001`  
**Exact HEAD:** `b461e35561ba1bc44d366fdab351e46221150873`  
**Base:** `fix/home-header-typography-focus-001` @ `9b49f26180430dc63ef2ab8cf20bcba845e067ea`

### Cross-audit decisions
- The cumulative source ancestry retains the approved Home/Dock/Islamic Shelf and G3/G4 chain through PR #77–#89, #100 → #101 → #103 → #104 → #105 → #107 → #108 → #110.
- PR #91–#93 were parallel implementations. Their RTL/LTR alignment, prayer-label cleanup, compact spacing and shared Ayah line-box behavior were cross-checked against the later #101/#110 implementation and are already represented there; no duplicate replay.
- PR #99 spacing/safe-frame work is superseded by cumulative #100/#101 already in the source.
- PR #90 artifact workflow allowance is represented in the cumulative workflow.
- PR #109 overlaps MainActivity/Glider/navigation selector. Retain #110 global silver focus, current DPAD graph and active Home state. Selectively integrate #109 clock/date divider, centered weather/AQI divider and uniform navigation spacing.
- PR #51 location receiver DUMP permission + non-exported LocationActivity and its regression test are present in current source; the test file matches exactly.
- PR #41 WallpaperSettingsDialog is an exact source-file match; current MainActivity/ModernSettingsDialog/WallpaperUploadServer/WeatherActivity and localized strings contain the unified wallpaper flow, startup recovery and upload-timeout behavior.
- PR #35 atomic built-in avatar/app-policy package filtering and PR #17 SettingsFocusPath are represented in the current source. No duplicate cherry-picks.
- PR #63/#64 Dock partition/inventory foundation remains a separate unfinished workstream. PR #64 is the stronger duplicate, but its audit explicitly leaves dedicated Dock/Grid renderers, settings UI and DPAD acceptance incomplete; it refactors AppAdapter and is excluded from this visual integration to protect Dock/Grid behavior.
- PR #62 GIPHY avatar catalog is a separate unfinished optional feature with cancellation/lifecycle/provider-policy acceptance gaps; excluded from this integration candidate.

### Added integration changes
- Added 1dp × 22dp clock/date divider and removed redundant dateBlock margin.
- Standardized Beranda/Profil/Pengaturan drawable gap to 2dp and horizontal padding to 4dp.
- Added centered vertical Weather/AQI divider span; data/provider/cache/refresh logic unchanged.
- Added regression contract for the new separators and spacing.
- Updated three existing tests whose old 1dp/4dp expectations contradicted the selected uniform 2dp design.
- Added dedicated PR #111 QA artifact upload step.

### Exact-head CI and APK verification
- CI run #1898 / ID `42750375871` on exact HEAD `b461e35561ba1bc44d366fdab351e46221150873`: **SUCCESS**.
- Lists/tools/docs job: PASS; **255 tests PASS**.
- Launcher `testReleaseUnitTest assembleRelease`: PASS; `BUILD SUCCESSFUL`.
- APK artifact upload: PASS.
- APK size: **1,630,769 bytes (1,592 KB)**; non-blocking warning above historical 1.5 MiB reference.
- Artifact ID `13733830327`; name `glass-tv-home-cumulative-integration-111-b461e35561ba1bc44d366fdab351e46221150873`; archive size 1,431,843 bytes; archive SHA-256 `2d367f674a5d95acb16ffb92ddfa1ebe108ccd8bd6ac79dcdadd12b7aa39da97`.
- Extracted APK: `app-release.apk`, 1,630,769 bytes; SHA-256 `369d80d6c5f3f8a369f1b083a0d1eeabc944a001334eeb5294905d65d3179b3c`. ZIP integrity and APK identification PASS.
- Actions artifact: https://github.com/Frangga11/glass-tv-home/actions/runs/42750375871/artifacts/13733830327

### Acceptance still pending
- Visual review at 1920×1080.
- Physical Mi TV Stick visual/DPAD acceptance, especially silver focus contrast on bright/dark wallpapers, glass continuity during focus movement, header separator legibility and navigation spacing.
- APK size optimization is a non-blocking follow-up; do not remove approved assets solely to meet the historical 1.5 MiB reference.
- No merge, ADB/device installation, default launcher change or release publication has occurred. Keep PR #111 OPEN / DRAFT / UNMERGED until visual and physical acceptance are recorded.

**Next restart prompt:** `Lanjutkan dari GTVH-CHECKPOINT-LATEST.md — GTVH-HOME-CUMULATIVE-INTEGRATION-111.`


## FLOATING ISLAMIC CONTENT / RIGHT GRADIENT — 10 NOVEMBER 2026

**Workstream:** `GTVH-HOME-FLOATING-CONTENT-RIGHT-GRADIENT-001`  
**Repository:** `Frangga11/glass-tv-home`  
**PR:** https://github.com/Frangga11/glass-tv-home/pull/112  
**State:** OPEN / DRAFT / UNMERGED  
**Source branch:** `fix/home-floating-content-right-gradient-001`  
**Base:** `integration/home-all-approved-changes-001` @ `b461e35561ba1bc44d366fdab351e46221150873`  
**Current exact HEAD:** `56f5af83b79eaa704d7616d111501f41f5c52e28`  
**Ancestry:** direct descendant of PR #111 exact HEAD; compare confirms 3 commits ahead / 0 behind.

### Root cause
- MainActivity initially assigned a rounded LEFT_RIGHT scrim to `islamicShelf`, then the global glass setup replaced it with `GlassDrawable` at 18dp radius and 0.55 glass strength.
- This produced the unified rounded glass panel visible in the newer user photo.
- `prayerSectionDivider` also supplied a visible 180dp horizontal rule between Ayat and prayer timetable.

### Scoped implementation
- `launcher/app/src/main/java/com/gathin/tvhome/GlassDrawable.java`: added an `R.id.islamicShelf`-only draw path before the generic glass preference/fallback paths. It paints a non-rounded local horizontal gradient, transparent to 0x1A000000 (about 10% alpha), with stops at 0%, 48%, 78%, 100%; no blur fill, veil panel, rim, or rounded rectangle is painted for this owner. Fade alpha remains respected.
- `launcher/app/src/main/res/layout/activity_main.xml`: set `prayerSectionDivider` alpha to 0 while retaining its original 180dp × 1dp dimensions and margins.
- Added `tests/test_home_floating_content_right_gradient_contract.py` with five contracts for gradient-only rendering, divider invisibility/layout retention, key typography/shadows, six prayer label/time IDs, and interaction view IDs.
- Exactly three files changed; MainActivity, all data providers, wallpaper, Hero Shelf, Dock/Grid, WeatherActivity, IDs/listeners/DPAD and prayer/Ayah data logic were not changed.

### Typography and readability lock
- Existing XML typography and soft text shadows are retained; the patch does not modify font family, weight, size, color, letter/line spacing, positions, margins or alignment.
- The gradient is a constant low-opacity right fade; no wallpaper-luminance sampler or runtime adaptive contrast adjustment has been introduced in this first scoped pass. This remains an explicit visual-review gate if bright-wallpaper legibility needs further local adjustment. Do not describe adaptive luminance behavior as implemented until evidenced.
- The visual reference comparison at 1920×1080 (including bright and dark wallpaper) remains pending. CI is not visual acceptance.

### Exact-head CI state at checkpoint
- PR #112 exact HEAD `56f5af83b79eaa704d7616d111501f41f5c52e28`.
- CI run #1901 / ID `42818986917` on exact HEAD `56f5af83b79eaa704d7616d111501f41f5c52e28`: **SUCCESS**.
- Lists/tools/docs regression suite: **260 tests, OK (1 skipped)**. All five new floating-content contracts passed.
- Launcher unit tests and `assembleRelease`: **SUCCESS**; Gradle reported `BUILD SUCCESSFUL`.
- Release APK exact size: **1,630,941 bytes (1,592 KB)**. This is above the historical 1.5 MiB reference; the warning is non-blocking.
- Artifact ID `13765339250`; archive size `1,431,913 bytes`; archive SHA-256 `e44a3eb0b7fd144ff3cdebd344a53553cc3e7895dbda4834fc06166e51ff8bec`.
- Artifact: https://github.com/Frangga11/glass-tv-home/actions/runs/42818986917/artifacts/13765339250
- No ADB, physical device install, default launcher change, merge, or release was performed.

### Next steps
1. Poll CI run #1901 for exact HEAD; record regression count, unit-test result, Release Build, APK size and artifact ID/link.
2. If any failure, fix only within scope and wait for a new exact-head run.
3. Complete visual review at 1920×1080 on bright and dark wallpaper. Verify no panel/corners/rules, typography parity, gradient direction and readability.
4. If true adaptive luminance contrast is required by the visual review, design a minimal, cached local brightness response without touching text geometry or data logic.
5. Keep PR #112 OPEN / DRAFT / UNMERGED; no ADB/device install or merge/release without authorization.

**Next restart prompt:** `Lanjutkan GTVH-HOME-FLOATING-CONTENT-RIGHT-GRADIENT-001 dari PR #112 dan exact HEAD 56f5af83b79eaa704d7616d111501f41f5c52e28; periksa CI run #1901 lalu lanjutkan visual acceptance.`


### FOLLOW-UP AUDIT / REDRAW EFFICIENCY — 10 NOVEMBER 2026

- Re-opened PR #112 against exact current source. PR remains OPEN / DRAFT / UNMERGED; base is still PR #111 exact HEAD `b461e35561ba1bc44d366fdab351e46221150873`.
- Visual source audit confirms `islamicShelf` is a `wrap_content` right-aligned shelf, not a full-screen overlay. Its background now paints only a horizontal gradient from transparent to max alpha 0x1A (about 10%); no rounded panel, blur fill, veil, rim, or fallback surface is drawn for this view. The old rounded initial scrim is overwritten by the GlassDrawable during setup, and the prayer divider alpha is zero.
- Found one performance improvement: the initial implementation allocated a new LinearGradient shader on every Drawable redraw. Updated it to cache the shader and rebuild only when the shelf bounds change. Added a source regression assertion for this cache contract. Appearance values and geometry remain unchanged.
- Current exact PR HEAD: `377aace505536ae94fee11727a0d5691e342e720`; direct compare with PR #111 HEAD is 5 commits ahead / 0 behind; exactly 3 files remain changed.
- Previous HEAD `56f5af83b79eaa704d7616d111501f41f5c52e28` CI #1901 was SUCCESS (260 tests, 1 skipped; Release Build PASS; APK 1,630,941 bytes). This result does not validate the latest shader-cache commit.
- New exact-head CI #1902 / run ID `42819883026` is QUEUED/PENDING. Await exact-head test/build and new artifact/size.
- Adaptive contrast is not yet dynamic: no wallpaper luminance sampling has been implemented. Existing text shadows + constant subtle gradient remain. Do not call adaptive contrast complete.
- 1920×1080 bright/dark wallpaper visual acceptance remains pending. No ADB, device install, merge or release.
- Next: wait for CI #1902, then provide current-head QA artifact for owner visual check; ask for direct TV screenshot(s) of bright and dark wallpaper after owner installs manually. Do not claim visual PASS based on CI.


### EXACT-HEAD CI #1902 RESULT — 10 NOVEMBER 2026

- Exact PR HEAD `377aace505536ae94fee11727a0d5691e342e720`, CI run #1902 / ID `42819883026`: **SUCCESS**. Both jobs completed successfully.
- Lists/tools/docs suite: `Ran 260 tests`; OK. All five floating-content tests passed, including the cached shader contract.
- Android launcher unit tests and Release build: **PASS**, Gradle `BUILD SUCCESSFUL` (99 actionable tasks).
- Exact Release APK size: `1,630,989 bytes` (`1,592 KB`), non-blocking warning above historical 1.5 MiB reference.
- Exact-head cumulative QA artifact ID `13764799974`; ZIP size `1,432,027 bytes`; SHA-256 `970f274d000107229ef1202fe4c6016e2b83b795abf391474233286d32e1ceaf`. Artifact URL: https://github.com/Frangga11/glass-tv-home/actions/runs/42819883026/artifacts/13764799974
- Current PR #112 remains OPEN / DRAFT / UNMERGED; base PR #111 unchanged. CI/source validation PASS is not visual acceptance.
- Next decision gate: owner downloads/installs the exact-head QA APK manually and supplies 1920×1080 Home screenshots on bright and dark wallpapers for visual comparison. No ADB, installation, merge, or release performed by assistant.


---

## FLOATING CONTENT OVAL GRADIENT / AYAH HINT / PRAYER DIVIDER — 10 NOVEMBER 2026

**Workstream:** `GTVH-HOME-FLOATING-CONTENT-OVAL-DIVIDER-002`  
**Repository:** `Frangga11/glass-tv-home`  
**PR:** https://github.com/Frangga11/glass-tv-home/pull/113  
**State:** OPEN / DRAFT / UNMERGED  
**Source branch:** `fix/home-floating-content-oval-divider-002`  
**Stacked base:** PR #112 branch `fix/home-floating-content-right-gradient-001`  
**Exact current source HEAD:** `964a802c72e9f4c8468d528a95f60e5f47cd24ab`  
**Parent PR #112 exact HEAD:** `377aace505536ae94fee11727a0d5691e342e720`

### Owner-requested changes implemented
1. `GlassDrawable.java`: replace the horizontal rectangular fade with a cached `RadialGradient` anchored at the upper-right corner and flattened vertically using a local matrix. The subtle opacity levels remain at 0x1A / 0x10 / 0x06 / transparent; only the gradient shape and falloff change.
2. `activity_main.xml`: reduce only the “BACA AYAT LENGKAP” hint from 10sp to 9sp; horizontal padding 10dp → 8dp; vertical padding 5dp → 4dp.
3. `MainActivity.java`: slightly reduce the hint capsule radius 8dp → 6dp and soften its stroke from 0x55DCE5ED to 0x44DCE5ED.
4. `MainActivity.java`: prayer schedule normal-state outline is transparent (0x00000000). Existing focus/pressed states, focus border, click listener and DPAD behavior remain intact.
5. `activity_main.xml`: reveal the existing 180dp × 1dp `prayerSectionDivider` with restrained #66FFFFFF at 0.65 alpha, preserving its original layout slot and margins.

### Explicit non-scope
No changes to Arabic or translation text, Ayah main typography, prayer labels/times/data/calculation, shelf geometry/position, wallpaper, navigation, weather/AQI, Hero Shelf, Dock/Grid, IDs, action listeners, or state logic. PR #112 and its source branch remain unchanged. No merge, ADB/device install, or release.

### Regression / CI status
- Updated `test_home_floating_content_right_gradient_contract.py` for the radial oval shader/cache, visible separator, 9sp/8dp/4dp hint capsule, and transparent normal prayer outline while retaining focus feedback.
- Updated the legacy `test_home_wallpaper_text_refinement_contract.py` assertion to the intentionally softened 0x44DCE5ED hint border.
- Initial PR CI #1903 / run ID `42822115893` was triggered on the pre-correction HEAD `9a72213192b32ab583298426605dde3f6ad4e631`. The Lists/tools/docs job found one stale assertion still expecting the previous 0x55DCE5ED capsule border. This was a regression-test expectation only; the source change was intentional.
- Corrected exact current source HEAD: `964a802c72e9f4c8468d528a95f60e5f47cd24ab`. A fresh exact-head CI run has not yet been confirmed; do not claim CI PASS.
- Physical Mi TV Stick visual acceptance remains pending. The visual review must confirm the oval gradient remains subtle, the smaller capsule remains readable at 1920×1080, the prayer outline is absent when idle but focus remains visible, and the new separator is balanced.

### Next gate
Confirm a fresh exact-head CI run on `964a802c72e9f4c8468d528a95f60e5f47cd24ab`. If successful, retrieve the QA artifact and provide it for owner-controlled TV visual review. Keep PR #113 and parent PR #112 OPEN / DRAFT / UNMERGED until acceptance and explicit merge authorization.

**Restart instruction:** `Lanjutkan GTVH-HOME-FLOATING-CONTENT-OVAL-DIVIDER-002 dari PR #113 dan exact HEAD 964a802c72e9f4c8468d528a95f60e5f47cd24ab.`
