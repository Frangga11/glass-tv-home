# P0 — Baseline & Compatibility

**Document:** GTVH-UIR-P0-001  
**Roadmap:** GTVH-UIR-001 v1.0 — LOCKED BASELINE  
**Audit date:** 6 November 2026  
**Disposition:** **PASS — BASELINE VERIFIED** (source/integration acceptance remains separate)  
**Execution boundary:** read-only source/CI audit and documentation only; no production code or device changes.

## 1. Trigger and objective

User instruction: start P0 — Baseline & Compatibility. Freeze the actual source baseline, reconcile owner refs and dependencies against the approved integration baseline, inspect B01 shared contracts, verify platform assumptions and APK budget, and establish the known-defect baseline.

## 2. Inputs reviewed

Canonical documents read on `docs/project-master-inventory`:
- `UI_REFRESH_ROADMAP.md` — GTVH-UIR-001 v1.0, LOCKED BASELINE, 6 November 2026.
- `HANDOVER.md`
- `MASTER_PROJECT.md`
- `INVENTORY.md`
- `DECISION_LOG.md`
- `AUDIT_REGISTER.md`
- Existing `E02_E06_GIF_AVATAR_DOCK_AUDIT_2026-11-05.md`, plus E02 profile audit and relevant B01 source.

No change to the locked roadmap or prior decisions was needed.

## 3. Frozen live source baseline

GitHub refs were compared live. Branch-name-to-SHA equality was verified for the owner heads and integration head.

| Role | Live ref | Exact SHA | Status / evidence |
|---|---|---|---|
| Approved integration baseline | `integration/b-series` | `562cde0751de93748eb4e0d4cdece4fa1672ac4d` | Live ref compares IDENTICAL to this SHA |
| Main (protected; untouched) | `main` | `6eaf787c56226606b1d2e650c59540c553756c62` | Integration is diverged (1 main-only commit); no main update |
| E01 current owner ref | `feature/e01-device-settings-home-control` | `edd6461c932705a109ed9768649cae7e3c162c59` | Current ref verified |
| E02 PR #62 head | `feature/e02-giphy-avatar-search-v1` | `588456144df24f2f6c015a01053ad700a04bee39` | OPEN / DRAFT / UNMERGED; live branch equals SHA |
| E02 PR #62 base | `feature/e02-profile-avatar-lock-ui` | `3ab9b55f3cfe03ad9841a119590a9183cf114e96` | Exact base head |
| E06 PR #63 head | `feature/e06-dock-capacity-control-v1` | `ecdeb0eba9fc7a574f1a6c9930205517687cd922` | OPEN / DRAFT / UNMERGED; live branch equals SHA |
| E06 PR #63 base | `feature/e06-home-screen-subtabs` | `570b7c83efaec3ac515b78f1c6c119b777fc3b84` | Exact base head |

### Dependency and divergence register

- E02 head is 19 commits ahead of its own base, with zero behind; E06 head is 3 commits ahead of its own base, with zero behind.
- E02 base is an ancestor of E06 base (E06 base is 29 commits ahead, zero behind).
- Both owner bases are ancestors of the frozen integration tree: integration is 326 commits ahead of E02 base and 297 commits ahead of E06 base.
- Compared directly with integration, E02 head is **DIVERGED: 19 ahead / 326 behind**; E06 head is **DIVERGED: 3 ahead / 297 behind**. Neither owner PR head is integrated or integration-validated.
- E01 current head and both E02/E06 bases share merge base `090a956a9860c82eb36c2d96848d47046fba0f0c`; E01 has 3 commits not contained in either owner base. The differing source paths include `ChildModeManager.java`, `MainActivity.java`, `ModernSettingsDialog.java`, `ProfileManager.java`, `UserSwitchDialog.java`, locale resources and Child Mode tests. E06's branch history additionally differs in `AppLanguage.java`, `ClockDream.java`, `WallpaperMetadata.java`, `activity_main.xml` and related tests.
- These are known ancestry/source-reconciliation risks, not proof of a runtime defect. No branch was rebased, merged, cherry-picked or force-updated. Before P4/integration, perform a three-way semantic reconciliation against the frozen integration tree and preserve owner-specific changes.

## 4. B01 shared contract inspection

The authoritative comparison target is the frozen integration source tree at `562cde...`, not a stale standalone B01 branch snapshot.

| Contract | Integration source evidence | P0 finding |
|---|---|---|
| Shared UI tokens | `TvUiTokens.java`, blob `aa0ff84f823af66b8386a5dac0469ad1636c0f79` | Includes shell/surface/text/focus/nav/card tokens plus `POPUP_SURFACE` and `DOCK_SURFACE` |
| Modal/dialog policy | `TvDialogStyle.java`, blob `f21372e612097ef6f3bbc24d7237611319406d64` | Central dialog surface, dim amount 0.68, viewport cap 88%, centered sizing, glass/solid fallback |
| D-pad focus | `TvFocusIndicator.java`, blob `ca6df5ab3c875146dc0e7a10b2248d4503c7093f` | Shared high-contrast focus ring and focus treatment |
| Responsive sizing | `TvResponsiveLayout.java`, blob `d5534771862d0042c24232f3477f921b29cd4c93` | Existing helpers cover Recent Apps and Nightstand; this is not a complete global overscan/text-scaling contract |

Neither PR #62 nor PR #63 changes `TvUiTokens.java` or `TvDialogStyle.java` in its own scoped diff. E02 adds profile/GIPHY UI and lifecycle code; E06 adds only the Dock preference model and its unit tests. Reuse the integration B01 contract; do not fork a competing global style/modal system. P2 must still define the missing general overscan and text-scaling rules.

## 5. Platform and device compatibility baseline

### Source configuration (verified)

- Application ID: `com.gathin.tvhome`
- `compileSdk 34`, `minSdk 26`, `targetSdk 34`
- Java source/target compatibility: Java 17
- Current source version: `versionCode 40`, `versionName 4.0.0`
- Manifest declares Leanback, touchscreen, Wi-Fi and GPS as optional features; Home/Leanback entry points are declared; MainActivity is landscape.
- Existing runtime dependencies include AndroidX RecyclerView 1.3.2 and DynamicAnimation 1.0.0. E02's GIPHY implementation adds no external runtime decoder/library; it uses platform `Movie`.
- E02 build config obtains `GIPHY_API_KEY` from Gradle property or environment and places it in BuildConfig. The key is extractable from a distributed APK and is **not a secret**; no key is committed. Quota/abuse controls and current provider policy must be verified before feature acceptance.

### Recorded target device (not physically re-probed in this P0)

The last recorded Mi TV Stick baseline is Android 11 / API 30, 1920×1080, density 320. API 30 is above minSdk 26, so the source-level minimum supports it. This does not prove targetSdk 34 runtime behavior, OEM remote behavior, rendering, memory, or installation compatibility. No ADB/device session was run in this P0; actual device identity/configuration must be re-captured during the separately authorized device gate.

## 6. APK budget baseline

Hard ceiling: **1,572,864 bytes**. CI workflow enforces a strict less-than check.

| Build evidence | Exact APK size | Headroom to 1,572,864 B | Result |
|---|---:|---:|---|
| Last accepted integrated G02 baseline, `562cde...` | 1,572,293 B | 571 B | PASS, extremely narrow |
| E02 PR #62 exact head, CI #1204 | 1,566,569 B | 6,295 B | PASS, isolated branch only |
| E06 PR #63 exact head, CI #1198 | 1,563,741 B | 9,123 B | PASS, isolated branch only |

The E02/E06 sizes are separate builds, not additive and not an integrated-size prediction. Combined integration must rebuild and measure the exact candidate; avoid unmeasured assets/dependencies.

## 7. Live exact-head CI verification

| PR | Exact head | Run | Required jobs | Build / size gate | Publication |
|---|---|---:|---|---|---|
| E02 #62 | `588456144df24f2f6c015a01053ad700a04bee39` | #1204 / `41895866368` | Launcher build & unit tests SUCCESS; Lists/tools/docs SUCCESS | `testReleaseUnitTest`, `assembleRelease`, APK diagnostics and size gate SUCCESS | Skipped |
| E06 #63 | `ecdeb0eba9fc7a574f1a6c9930205517687cd922` | #1198 / `41890835096` | Launcher build & unit tests SUCCESS; Lists/tools/docs SUCCESS | `testReleaseUnitTest`, `assembleRelease`, APK diagnostics and size gate SUCCESS | Skipped |

E02 run #1204 supersedes the stale 5 November HANDOVER note that still described its head/CI as pending. CI proves isolated build/unit-test success only. No cross-branch integration test or physical-device acceptance is claimed.

## 8. Baseline defect and risk register

### E02 — Profile / GIF Avatar

**Verified present:** bounded G-rated GIPHY search (14 results), HTTPS/GIPHY host checks, bounded JSON (512 KiB), bounded GIF download (8 MiB), no redirects, MIME/signature validation, private profile staging, Indonesian/English strings, Powered By GIPHY attribution, platform `Movie` playback, and bounded still-frame previews (up to 1 MiB, three preview workers).

**Open acceptance gaps / risks:**
1. Result cells show still-frame thumbnails, not animated previews.
2. Cancelling Futures/shutting down executors does not guarantee immediate disconnect of an active blocking HTTP connection; there is no shared connection-owning cancellation handle.
3. Commit is guarded by dialog/profile state checks, but cancellation/profile-switch races still require deterministic tests and lifecycle ownership.
4. Drawable visibility/callback scheduling is implemented; attach/detach/replacement and profile lifecycle need runtime and regression verification.
5. DPAD/focus, preview recycling/stale results, malformed/large GIF, memory, storage rollback, profile switching and localization/attribution tests remain incomplete.
6. Current GIPHY terms, rating and attribution requirements need a current provider-policy check.

### E06 — Home Screen / Dock

**Verified present:** `DockPreferences` persists HORIZONTAL/VERTICAL orientation and capacity 4/5/6 (default 5) in a separate preference namespace; normalization and basic model behavior have unit coverage.

**Root architectural gap:** Home still uses one RecyclerView/GridLayoutManager and one `COLUMNS` value for grid columns, Dock membership, AppAdapter and DockDecoration. The current default grid column config is 5. DockPreferences is not yet wired to a separate Dock view/adapter or settings UI.

**Open acceptance gaps:** independent ordered pinned-app model; separate horizontal-row/vertical-rail Dock presentation; visible capacity and overflow behavior; settings UI; independent DPAD/focus/scroll/reorder/restore; app removal/hidden-app transitions; proof main-grid columns remain unchanged; executable 1920×1080 simulations for all six combinations.

### Severity / disposition

- No new Critical defect was reproduced during this read-only P0 audit.
- E02 cancellation/lifecycle and E06 Dock/grid coupling are **implementation/acceptance blockers**; do not waive them on the basis of CI.
- Remaining UI completeness and provider-policy items are tracked as open; severity must be reassessed when the exact behavior is reproduced or implemented.
- Physical Mi TV Stick, phone/LAN QR and OEM remote evidence remain separate; no device PASS is claimed.

## 9. Method and quality check

Applied: Trigger → Input → Verifikasi → Analisis → Langkah Kerja → Decision Point → Output → Quality Check → Final.

- Verified live branch refs and exact SHAs by compare operations.
- Compared owner PR heads, their bases, E01 and integration ancestry.
- Inspected B01 token/dialog/focus/responsive source and E02/E06 source diffs.
- Retrieved exact-head CI runs and job steps/logs; confirmed build, unit tests, size gate and skipped publication.
- Reused existing audit evidence; no duplicate production audit or speculative fix.
- No code, branch ref, PR state, integration/main, APK artifact, device package, HOME/default launcher, ADB or TV setting was changed.

## 10. Decision and next gate

**P0 result: PASS — baseline verified and frozen at integration SHA `562cde0751de93748eb4e0d4cdece4fa1672ac4d`.** This is a baseline/source audit PASS only, not feature acceptance, integration readiness, or device acceptance.

P1 has **NOT STARTED**. The next authorized sequence is P1 Functional Inventory, mapping every in-scope screen/control/state to owner, behavior, data, persistence and test. Keep PR #62/#63 Draft/open/unmerged. P2/P3 design and prototype approval remains mandatory before P4 production implementation.
