# Quick Settings implementation and integration checkpoint — 19 November 2026

## Authoritative source state
- Repository: `Frangga11/glass-tv-home`
- Target branch: `main`
- Baseline: `0662c80bf80a6df06248262eeb0599a93aaba3e5`
- Active implementation: PR #156, `feature/quick-settings-single-frame-glass-001`
- Code head inspected: `e4b6bd39c2b7e54879dfd33c6b7c7225e73ae437`
- PR #156 remains OPEN / DRAFT / UNMERGED. This checkpoint does not authorize merging.

## User-approved latest Quick Settings corrections
- Wallpaper heading is left-aligned to the actual capsule lane; runtime alignment is calculated after layout using the painted glyph bounds and capsule position.
- Wallpaper heading font is 12sp (reduced as requested).
- Manage Wallpaper capsule is 64dp × 20dp, with a squarer 7dp corner radius; its right edge remains aligned to the 235dp content lane.
- Dialog Glider focus stroke is 1.5dp. It retains the shared spring-driven GLASS Glider / GliderFocusDrawable and the focus transition; focused labels retain white text with dark halo/shadow.
- DPAD graph: Kelola UP → Cache; Interval UP → Kelola; Kelola DOWN → Interval. Manage is entered from Interval UP, not from Cache DOWN or other edges.
- No unrelated Home, wallpaper renderer, Dock, weather/AQI, Prayer Popup, or Home DPAD code is in scope.

## Fixed geometry verified in source
- Panel: 255dp × 320dp; 10dp horizontal and vertical content insets.
- Inner lane: 235dp.
- Wide capsules: 235dp; half-width capsules: 115dp + 5dp gap + 115dp.
- Main action capsules: 36dp high.
- Wallpaper header: 166dp title lane + 5dp gap + 64dp Manage capsule = 235dp.
- Manage capsule height: 20dp (not 21dp).
- Fixed gaps remain 5dp. Panel refuses to render if the fixed geometry cannot fit; it does not compress or overlap the Dock.

## Input selector
- Quick Settings Input tile reuses the existing TV source selector.
- Remote Input capture remains opt-in via `INPUT_KEY_SERVICE=1`; configured keycodes route to MainActivity's Inputs popup.
- No fabricated HDMI inputs are added when the streaming stick has no real passthrough/tuner source or configured vendor source panel.
- Android disables accessibility services on reinstall; the opt-in service must be re-enabled after each reinstall.

## Automated verification evidence
- Exact code-head CI: run #2459 / `44516696991`, head `e4b6bd39c2b7e54879dfd33c6b7c7225e73ae437`: SUCCESS.
- `Lists, tools and docs`: SUCCESS.
- `Launcher build and unit tests`: SUCCESS, including release build and unit tests.
- Cumulative QA artifact: ID `14524699219`; ZIP size 1,447,092 bytes; SHA-256 `96991c9d98590573605a6d457b354bdfa8fe4e276c35a6453df1696bde273616`; expires 26 November 2026. ZIP size is not the extracted APK size.
- This CI result validates the code head before this documentation checkpoint commit. A fresh exact-head CI run is required after the checkpoint is committed.

## Acceptance boundary and next gate
- Physical Mi TV Stick validation is still outstanding for visual proportions, Wallpaper baseline alignment, Manage capsule shape/height, focus-glider movement, DPAD Kelola/Cache/Interval paths, and remote Input delivery.
- CI is not physical-device acceptance. Do not claim the popup is device-accepted or publish/install it as a final release.
- Keep PR #156 Draft and unmerged until the physical device acceptance gate is recorded and the owner explicitly approves the integration merge.
- PR #62 remains deferred and untouched. PR #153 and #154 remain closed unmerged historical candidates; PR #155 remains merged.
