# GTVH-HOME-GLASS-FOCUS-MOTION-INTEGRATION-001

Date: 11 November 2026
Repository: `Frangga11/glass-tv-home`
Branch: `fix/home-glass-focus-motion-integrated-001`
Base: `fix/home-row-pitch-150-card-182x100-001`
Status: implementation candidate; CI and physical-device validation pending.

## Scope integrated
- Retain the cumulative Silver-Cyan glass foundation from PR #104 and its descendants: neutral silver rim, Electric Cyan focus, cached wallpaper-backed glass, and opaque fallback surfaces.
- Retain the approved 150dp two-row pitch and 182x100dp card design with 42dp item extra from PR #119.
- Keep the permanent Dock LayoutParams anchor and translation-only two-row movement from PR #117.
- Keep the 190ms normal / 120ms reduced-motion timing candidate from PR #118.

## Corrective integration
- Expansion and collapse now both use the same fixed 150dp row pitch. Previously expansion used 150dp while collapse recomputed a viewport-derived pitch; that asymmetry could cause a mismatched return trajectory.
- Both expansion and collapse use the same PathInterpolator(0.22, 1, 0.36, 1) easing curve for a consistent, natural ease-out glide.
- Added a regression contract asserting pitch symmetry, matching easing, and no viewport-derived pitch in collapse.

## Protected behavior
No intentional changes to wallpaper/data providers, Weather/AQI cadence, app identity/order, DPAD mapping, focus ownership, selection state, Wi-Fi behavior, or Islamic/prayer content.

## Validation gates
- Run complete Python and Android CI on this exact branch head.
- Resolve any stale geometry contracts against the approved 182x100dp / 42dp / 150dp design without weakening runtime coverage.
- Install the cumulative QA APK on Mi TV Stick Android TV 11 (1920x1080).
- Repeat at least 10 DOWN/UP cycles, rapid direction reversals, and focus transitions across row boundaries.
- Inspect glass rim/material over bright, dark, and mixed wallpapers; confirm no blur-only substitution or loss of opaque fallback.
- Do not mark PASS or merge until CI is green and physical-device acceptance is recorded.
