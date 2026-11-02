# B03 — Global Dialog, Popup & Surface Fallback Audit

**Date:** 2 November 2026  
**Status:** SOURCE REPAIR AND EXACT-HEAD CI VERIFIED — DEVICE ACCEPTANCE PROJECT F  
**Owner:** Project B03 (global dialog/popup layering), with E07 toggle integration  
**Source branch:** `feature/b03-dialog-opacity-layering`  
**Source PR:** #12 — Draft/open/unmerged  
**Current source head:** `d87ffdf914851b8a1d2c67c2416ac9f5f1309d93`  
**Exact-head CI:** run #687 / `41447950284` — SUCCESS (both jobs passed)  
**Merge policy:** D-013; no early merge.

## 1. Trigger and impact

The E07 Glass Effect setting exposed a structural UI defect: when decorative glass was disabled, several app-owned surfaces became transparent or visually unstructured. The effect crossed top-bar pills, Home popup menus (including Wi-Fi and Quick Settings), anchored PopupWindows, the dock tray and modal dialogs.

The defect was not a Wi-Fi-only issue. The shared surface primitives were treating the optional glass material as the only background material.

## 2. Global contract

Glass Effect is a decorative material choice, not a dependency of layout or UI structure.

- **Glass ON:** retain the existing blur/backdrop, veil, rim and glass treatment.
- **Glass OFF:** render a solid opaque surface with a shared border and the same geometry.
- Preserve component bounds, corner radius, row spacing, typography, icons, focus feedback, D-pad navigation, callbacks and state.
- Do not restyle Android OS-owned Settings pages; this contract applies to app-owned UI only.
- All fallback colors are shared `TvUiTokens`; components must not invent near-duplicate local colors.

## 3. Root cause

- `GlassDrawable.draw()` returned when the preference was OFF without painting a replacement surface.
- `DockDecoration.onDraw()` returned after painting only the focus shape, omitting the dock tray.
- `TvDialogStyle.apply()` always selected the glass dialog drawable, regardless of the preference.
- E07 additionally found that a live anchored PopupWindow needed explicit invalidation after the preference changed; that toggle/lifecycle integration remains in E07's feature branch.

## 4. B03 implementation

- Added shared opaque fallback tokens `POPUP_SURFACE` and `DOCK_SURFACE` to `TvUiTokens`; the existing shared `BORDER` token is reused.
- `GlassDrawable` now paints a rounded solid popup/pill surface and border before retaining its existing focus-shape behavior when glass is OFF. The fallback fill/border also follows the existing fade alpha so popup opening/closing transitions remain intact.
- `DockDecoration` now paints the solid dock tray, shadow and border in OFF mode while preserving the existing D-pad focus shape.
- `TvDialogStyle` now chooses the existing glass dialog resource when ON and a rounded solid surface using the same shared tokens when OFF.
- Added `GlassSurfaceFallbackTest`, a JVM regression guard for opaque popup/dock/border tokens and distinct popup/dock materials. The test uses pure bit checks and does not depend on Android framework stubs.
- No preference key, layout geometry, navigation callback, focus state, or OS Settings page was changed.

## 5. Cross-project ownership

| Responsibility | Owner | Required action |
|---|---|---|
| Global tokens and fallback material contract | B01 / B03 | Keep one shared palette and common app-owned popup/dialog surface behavior |
| Glass toggle, live popup invalidation and effects settings behavior | E07 | Preserve the current preference/callback and refresh visible surfaces when toggled |
| Module-specific content and layout | E01–E08 as applicable | Consume shared primitives; no module-specific global popup fork |
| Full integration, source reconciliation and release acceptance | G | Reconcile the B03 and E07 source histories; resolve shared-file overlaps before grouped merge |
| Physical remote/visual acceptance | F | Verify on Mi TV Stick at 1920×1080 and 4K |

The B03 branch is the canonical global contract owner; E07's parallel implementation is the feature-level consumer/bridge. Their shared files must be reconciled by content and exact commit during grouped integration, not blindly merged by branch order.

## 6. Verification and residual gates

| Gate | Status |
|---|---|
| Root cause verified from source | PASS |
| B03 fallback implementation | IMPLEMENTED |
| JVM fallback-token regression test | ADDED |
| Exact-head B03 CI run #687 | PASS — both jobs succeeded; unit tests/release build/APK-size guard and Lists/tools/docs |
| E07 source/CI fallback repair | PASS — PR #40 head `7ecb45a634e6ed9792fd87f3110ffe0b6eaad1a2`, run #682 / `41446777187` SUCCESS |
| 1080p/4K visual and D-pad acceptance | PENDING Project F |
| Android OS Settings page unchanged | SOURCE SCOPE PRESERVED |
| Merge/release | DEFERRED BY D-013 |

Project F must check Glass ON and OFF, top pills, Wi-Fi menu, Quick Settings menu, anchored card PopupWindow, all app-owned modal dialog types, dock tray, focus traversal, Back/dismiss, repeated toggling while a popup is open, 1920×1080 and 4K. Check clipping, opacity/contrast, rounded corners, row alignment and focus visibility.

No APK was published or installed. No physical TV test is claimed.

## 7. Decision

B03's global surface fallback repair is implemented and exact-head CI-verified on its isolated source branch. It is not device-complete until Project F supplies physical acceptance. PR #12 remains Draft/open/unmerged under D-013.


## Exact-head CI closure — 2 November 2026
- Source/test head: `d87ffdf914851b8a1d2c67c2416ac9f5f1309d93`.
- CI run #687 / `41447950284`: **SUCCESS**. `Launcher build and unit tests` and `Lists, tools and docs` both passed.
- Gradle `testReleaseUnitTest` and `assembleRelease` passed; APK-size guard passed; manual APK publication skipped.
- Release APK diagnostic: 1503 KB, below the 1.5 MiB (1536 KiB) cap.
- PR #12 remains OPEN/DRAFT/UNMERGED and mergeable. No APK installed and no physical TV acceptance claimed.

- Follow-up refinement: the OFF-state solid surface and border now honor GlassDrawable's existing fade alpha, preserving popup open/close animation. This exact refinement is included in the tested head above.
