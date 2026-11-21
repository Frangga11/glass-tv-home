# Settings Popup Master Design Audit — 21 November 2026

## Status
- Workstream: `GTVH-SETTINGS-POPUP-UNIFICATION-001`
- Phase: **B — Master design approved; popup-family implementation authorized**
- Document status: **APPROVED MASTER DESIGN — owner confirmation recorded 21 November 2026**
- Scope: Settings popup, Wi-Fi, Input, Language, Restart TV, Power Off, Cache confirmation/result, Wallpaper controls, Interval, and Wallpaper management.
- Boundary: **RE-SKIN, NOT REWIRE.** No behavior, persistence, service, navigation contract, or business logic changes are authorized by this audit.

## Repository and change-safety verification
- Repository: `Frangga11/glass-tv-home`
- Audited base: `main` at `0662c80bf80a6df06248262eeb0599a93aaba3e5`.
- A live compare of `0662c80...` against `main` returned `identical`; this is the exact current main baseline for this audit.
- PR #156: OPEN / DRAFT / UNMERGED; branch `feature/quick-settings-single-frame-glass-001`; current PR head `c1fcb6c3fccf75990415336b463fcc5b1c6548d3`.
- PR #156's recorded implementation/code head: `e4b6bd39c2b7e54879dfd33c6b7c7225e73ae437`; its 19 changed files include the redesigned `QuickSettingsDialog.java`, Input handoff, focus graph, tests, and checkpoint documentation.
- PR #156 checkpoint records exact-code-head CI run #2459 as passing, but explicitly leaves physical Mi TV Stick acceptance outstanding. Do not treat CI as visual/device acceptance.
- This audit is on a separate documentation branch. No source file, PR state, branch protection, device installation, or APK was changed.

## Naming clarification
The intended master is the **Quick Settings / Popup Pengaturan panel** opened from the Settings navigation control. It is **not** `ModernSettingsDialog`, which is the separate full Global Settings modal with a two-column sidebar/content layout. The Wi-Fi popup is a sibling surface and must follow the Popup Pengaturan design language, not act as its reference.

## Source inventory and current implementation

| Surface | Current source / entry point | Current presentation and behavior |
|---|---|---|
| Master candidate — Popup Pengaturan | PR #156 `QuickSettingsDialog.java` | Anchored single-frame glass panel; fixed 255 × 320dp candidate geometry; 10dp insets; shared Glider focus treatment. Not yet merged/device-accepted. |
| Wi-Fi | `WifiSettingsDialog.java` via `MainActivity.showWifiPopup()` | Separate anchored dialog below Wi-Fi control; responsive width/height fractions; network list scroll; static `TvDialogStyle.createDialogSurface()` background; 0.08 dim. |
| Input selector | `MainActivity.showInputs()` / `showMenu()` | Home-window anchored menu using `GlassDrawable`, `Backdrop.watch`, and Home Glider; dynamic width/height and source-dependent rows. Keep real TV input discovery and empty-state logic unchanged. |
| Language | `MainActivity.showLanguageDialog()` | Standard AlertDialog using shared `styleGlassDialog()` styling. |
| Restart TV | `MainActivity.showRestartDialog()` | Standard confirmation AlertDialog; existing restart service call and feedback must remain unchanged. |
| Power Off | `MainActivity.showTurnOffDialog()` | Standard confirmation AlertDialog; existing power-menu handoff must remain unchanged. |
| Cache | `MainActivity.showCleanTvCacheDialog()` | Confirmation and result AlertDialogs; currently uses shared device-settings modal styling; cache-cleaning flow must remain unchanged. |
| Wallpaper control / Kelola | `WallpaperSettingsDialog.show()` via `MainActivity.showWallpaperDialog()` | Separate 3-tab dialog (Change / Shuffle / Next), with its own tab and action-card styling. |
| Wallpaper collection | `MainActivity.showWallpaperCollectionDialog()` | Scrollable list dialog, 560 × 500dp max window, active-category indicator, collection and custom-photo actions. |
| Wallpaper interval | `MainActivity.showWallpaperIntervalDialog()` | Scrollable list dialog, 560 × 500dp max window, active interval indicator and persisted interval selection. |

## Measured master-candidate specification — PR #156

These values are directly declared in the PR #156 candidate source/checkpoint; they are a **candidate master baseline**, not a claim of physical-device acceptance.

| Element | Candidate value |
|---|---:|
| Panel width | 255dp |
| Panel height | 320dp |
| Inner content lane | 235dp |
| Side inset | 10dp |
| Top / bottom inset | 10dp |
| Main action capsule height | 36dp |
| Half-width capsule | 115dp |
| Wide capsule | 235dp |
| Gap between capsules / sections | 5dp |
| Wallpaper heading lane | 166dp |
| Gap before Kelola | 5dp |
| Kelola capsule | 64 × 20dp |
| Standard capsule corner radius | 10dp |
| Kelola corner radius | 7dp |
| Panel corner radius | 24dp (`TvUiTokens.POPUP_RADIUS_DP`) |
| Header height | 26dp |
| Header title | 15sp, Inter Bold |
| Header icon | 18dp |
| Main capsule text | 13sp, Inter |
| Main capsule icon | 16dp |
| Kelola text | 12sp, Inter Bold |
| Wallpaper heading | 12sp, Inter Bold; painted-edge alignment correction retained |
| Focus stroke | 1.5dp; shared spring-driven GLASS Glider |
| Panel placement | Anchored to Settings gear; 5dp below anchor and 5dp above Dock |
| Overflow policy | Refuse to render if fixed content cannot fit; no compression, clipping, or Dock overlap |

The source uses `GlassDrawable` with `Backdrop.watch(content)`, a 0x54 glass veil, and opaque fallback surface/border tokens. The candidate's standard capsules use translucent idle fill/border and the shared moving GLASS focus treatment.

## Measured Wi-Fi differences

The current Wi-Fi popup is a distinct anchored surface, not a fixed-size copy of Quick Settings:
- Width = 22.5% of screen pixel width.
- Height = 69% of screen pixel height minus 35dp.
- At the recorded Mi TV Stick profile (1920 × 1080, density 320 / 2.0), this resolves to approximately **216dp × 337.5dp** before any device/window inset effects.
- Anchored to the Wi-Fi control, with a pointer, top/right gravity, and 0.08 dim amount.
- Header title 17sp; section heading 13sp; metadata 11sp; footer 13sp.
- Network rows 35dp high with 4dp row gap; connected-status icon 22dp; SSID text is currently set in pixels (25px), not sp.
- Wi-Fi surface uses `TvDialogStyle.createDialogSurface()` / `bg_glass_dialog`, whereas the master candidate uses live `GlassDrawable` + `Backdrop.watch`. This is a material inconsistency to resolve while preserving Wi-Fi's anchored geometry and scrollable network list.

## Shared modal baseline observed in the source

- `TvDialogStyle` owns standard modal window width, height, dimming, and glass/solid fallback.
- `MainActivity.styleGlassDialog()` applies Inter typography: title 20sp, message 15sp, action buttons 15sp; list rows 15.5sp.
- Default generic modal width is typically 520dp; Cache uses 600dp.
- Wallpaper control dialog uses 680 × 420dp max; collection and interval use 560 × 500dp max.
- The shared window style does not normalize each custom content view's internal spacing, capsule geometry, icons, footer layout, or DPAD graph. Thus a shared window helper alone is insufficient to make the family visually uniform.

## Audit findings

1. **Material mismatch:** the master candidate uses live backdrop-aware glass; Wi-Fi uses a static gradient surface; many other dialogs rely on the standard AlertDialog window surface; Input uses Home-window glass.
2. **Geometry mismatch:** the master candidate is fixed and anchored; Wi-Fi is screen-fraction based; generic dialogs use width caps and wrap-content height; Wallpaper dialogs use larger scrollable layouts; Input is content-sized and anchored.
3. **Typography mismatch:** master candidate uses 12–15sp Inter; Wi-Fi uses 11–17sp plus 25px SSID text; generic modal defaults use 15–20sp; Wallpaper collection/interval rows use larger 18sp titles.
4. **Control mismatch:** master capsules are 36dp high with a defined 10dp radius and moving GLASS focus. Other surfaces use separate row/card backgrounds, min-heights, and focus implementations.
5. **Navigation mismatch:** the master candidate has a deterministic focus graph; Wi-Fi maintains a separate focus order; Input uses the Home Glider/menu navigation; standard dialogs rely on Android list/button focus; Wallpaper lists use scrollable focusable rows.
6. **Behavioral risk boundary:** Wi-Fi scanning/permissions, Input discovery, language persistence, restart/power calls, cache cleanup, wallpaper selection/interval/shuffle and management must remain owned by existing modules. Only their presentation layer is in scope.

## Approved master design rules — owner approval recorded 21 November 2026

- **Owner-approved:** use the PR #156 Popup Pengaturan as the visual authority and canonical design reference. Its visual design is considered complete/paripurna by the owner; do not reopen or redesign it. PR #156 remains a draft implementation branch until separately reviewed and accepted for integration.
- Share one glass material recipe and fallback policy across all popup shells. Preserve distinct placement models: anchored Quick Settings, anchored Wi-Fi, anchored Input menu, and centered modal dialogs.
- Normalize the visual tokens for surface, border, corner radius, title/body/meta typography, icon sizing, capsule height, internal spacing, and focus treatment.
- Keep content-specific geometry where needed: Wi-Fi retains its network list/scroll viewport; Input retains source-dependent rows; confirmation dialogs retain clear primary/secondary actions; Cache retains its result state; Wallpaper retains its collection and interval content.
- Standardize header/footer hierarchy and action semantics without forcing identical panel dimensions.
- Use the same DPAD focus visual language, but keep each popup's navigation graph appropriate to its controls and test every entry/exit path.
- Prefer shared presentation helpers/tokens over duplicating per-dialog styling. Do not move business logic into a new shared popup component.

## Implementation gates

1. **Completed:** owner approved the Popup Pengaturan master design as final visual reference on 21 November 2026. This approval locks the reference design; it does not automatically approve merging PR #156.
2. Approve the shared popup tokens and any intentional exceptions.
3. Implement presentation-only changes on a scoped branch; do not alter PR #156's unrelated Input service/remote-key logic.
4. Add/adjust unit and static contract tests for geometry, style tokens, and DPAD graph invariants.
5. Run exact-head CI; then perform visual and DPAD acceptance on the Mi TV Stick at 1920 × 1080 / density 320.
6. Compare all popup screenshots side by side, correct deviations, document exceptions, and create a final integration checkpoint.
7. Do not merge PR #156 or any follow-on PR without explicit owner approval and the required device acceptance.

## Explicit non-actions in the audit phase
- No source-code changes in the audit phase.
- No merge, rebase, cherry-pick, close, or retarget of PR #156.
- No modification of PR #62 (deferred).
- No APK install, ADB session, device setting changes, or claim of physical-device validation.

## Execution direction after approval
- Create a separate popup-family implementation branch from current `main`; keep PR #156 unchanged and unmerged.
- Apply the approved master visual language to sibling popups in bounded presentation-only changes.
- Preserve each popup's existing layout role (anchored, centered modal, scrollable list) and all existing behavior/state/service contracts.
- Begin with Wi-Fi material/focus treatment, then Input and the language/restart/power/cache dialogs, then Wallpaper control/collection/interval.
- Validate source diffs and static tests after each bounded batch. Physical Mi TV Stick validation remains a required gate before merge.
