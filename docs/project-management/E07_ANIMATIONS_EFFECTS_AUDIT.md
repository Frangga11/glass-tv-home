# E07 — Animations & Visual Effects Audit

**Status:** Glass ON/OFF surface fallback repair implemented; exact-head CI verification pending. Physical TV acceptance remains assigned to Project F.

## Scope
Audit the existing Animations & Visual Effects settings and the visual effects they control. Preserve the four existing controls and their preference keys:
- Icon animations — `icon_animations_enabled`
- Glass effects — `glass_effects_enabled`
- Dynamic weather overlay — `dynamic_weather_overlay`
- Icon glow effects — `icon_glow_effects`

The work also checks the shared Home/Weather screen motion policy, rendering cost, lifecycle, and D-pad focus behavior. No new visual effect or settings control was introduced.

## Baseline and findings
1. Home tile focus/entrance animation and the Glider already consume the existing `Config.reducedMotion` setting; that implementation was retained.
2. The Home dynamic weather overlay and full Weather screen's `WeatherBackgroundView` continued scheduling frame-by-frame animation even when reduced motion was enabled.
3. Weather screen focus cards animated elevation/transform regardless of the reduced-motion setting.
4. The Weather screen did not apply the existing low-memory rendering profile, while the Home weather overlay already did.
5. Weather particle counts for rain, stars, and clouds were not consistently scaled for reduced-motion/low-memory rendering.

## Repairs
- Added an explicit reduced-motion policy to `WeatherBackgroundView`: retain a static weather scene but stop its continuous animation loop. Re-enable animation only when reduced motion is disabled and the view is attached/visible.
- Apply the same reduced-motion configuration to both the Home weather overlay and full Weather screen.
- Added a lightweight `Config.isReducedMotion(Context)` lookup so WeatherActivity does not load the full wallpaper/theme configuration just to read one motion preference.
- Weather screen focus transitions now snap to their final visual state under reduced motion; normal motion retains the existing animation.
- Apply the existing 1.5 GiB low-memory threshold to the full Weather screen.
- Reduce rain, star, and cloud particle density in reduced-motion/low-memory mode. Normal mode retains the existing density.
- Existing setting preference keys, switch callbacks, weather classification, data refresh, D-pad navigation, and focus ownership are preserved.

## Cross-project boundaries
- Project B remains owner of global focus, dialog, navigation, and shared UI contracts. E07 consumes those contracts and does not replace them.
- Project E05 owns weather data and freshness; E07 changes only rendering/motion behavior.
- Project D persistence contracts and the four existing preference keys are unchanged.
- Project F owns real-device performance, frame pacing, visual and remote-control acceptance.

## Verification
- Code checkpoint: `da3135796156c26dd20795931eae15caa83458e9`.
- Exact-head CI workflow run #671 / `41443003016`: both `Launcher build and unit tests` and `Lists, tools and docs` SUCCESS.
- Release APK diagnostic: `1,563,885 bytes`; budget `1,572,864 bytes`; headroom `8,979 bytes`. Size guard passed.
- APK publication was skipped. No APK installation or physical TV test is claimed.
- Project F must validate normal/reduced motion, toggle changes while Settings is open, Home-to-Weather transitions, pause/resume, low-memory behavior, D-pad focus, and visual quality on Mi TV Stick 1080p and 4K.

## Git checkpoint
- Branch: `feature/e07-animation-effects`
- Parent: E06 exact head `570b7c83efaec3ac515b78f1c6c119b777fc3b84`
- Code checkpoint: `da3135796156c26dd20795931eae15caa83458e9`
- PR #40: Draft, open, unmerged under D-013.


## Cross-project defect — Glass Effect OFF surface fallback
A follow-up integration audit found that disabling the decorative glass material also removed structural surfaces from app-owned UI:
- `GlassDrawable` returned without painting a fallback surface, affecting top-bar pills, Home popup menus (including Wi-Fi and Quick Settings), and anchored PopupWindows.
- `DockDecoration` returned before painting the dock tray.
- `TvDialogStyle` always applied the glass dialog resource, independent of the setting.
- The setting callback invalidated the Home root but did not explicitly invalidate an already-open anchored PopupWindow.

### Repair implemented
- Added shared solid fallback surface tokens to `TvUiTokens`.
- `GlassDrawable` now paints an opaque rounded surface and border when glass is OFF, then preserves its existing focus-shape behavior.
- `DockDecoration` now paints a solid tray, shadow and border when glass is OFF, retaining D-pad focus feedback.
- `TvDialogStyle` now selects the glass resource when enabled and a rounded solid fallback when disabled.
- Toggling the setting invalidates the active app-owned PopupWindow and Home popup menu as well as the root and dock decorations.
- No preference keys, callbacks, popup geometry, row layout, navigation, or Android OS Settings surfaces were changed.

### Ownership / acceptance
- Project B owns the global surface/dialog contract and must reconcile these fallback tokens/policies into the canonical B foundation.
- E07 owns the Glass Effect toggle integration and visual behavior on the existing app surfaces.
- Project F owns device acceptance: glass ON/OFF, Wi-Fi and Quick Settings menus, anchored popups, modal dialogs, dock/top pills, focus traversal, 1080p and 4K.
- No APK installation or physical TV test was performed in this source-repair step.

### Current source checkpoint
- Branch: `feature/e07-animation-effects`
- Latest source/test commit: `3c508ebafac379853ee26591f51cdb86c5fd3519`.
- Added `GlassFallbackSurfaceTest` to guard opaque popup/dock fallback tokens.
- PR #40 remains Draft/open/unmerged under D-013.
- Exact-head CI for the source/test commit: run #678 / `41446149526`; queued at documentation update time. The final documentation commit will receive its own CI run.
