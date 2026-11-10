# GTVH-HOME-TWO-ROW-DPAD-TRANSITION-LATENCY-001

Date: 11 November 2026
Repository: `Frangga11/glass-tv-home`
Branch: `fix/home-two-row-dpad-transition-latency-001`
Base: `fix/home-two-row-stable-dock-anchor-001`
Status: implementation candidate; CI and physical-device validation pending.

## Trigger
Physical Mi TV Stick acceptance feedback reports that two-row DPAD navigation is now visually correct and Dock restoration works, but the transition feels somewhat delayed.

## Scoped optimization
- Reduce normal transition duration from 260 ms to 190 ms.
- Reduce reduced-motion duration from 140 ms to 120 ms.
- Keep the existing DecelerateInterpolator, translation geometry, alpha behavior, and focus-after-settle sequencing.
- Apply the same timing to DOWN expansion and UP collapse.
- Do not change layout anchors, row pitch, focus mapping, app identity/order, tile sizing, or protected Home surfaces/data.

## Rationale
A 190 ms transition shortens perceived navigation latency while retaining enough frames for a visible, controlled slide on a 60 Hz TV display. The 120 ms reduced-motion path remains shorter. These are candidate timings and must be judged on the actual Mi TV Stick.

## Validation
- Run CI and contract tests against the exact PR HEAD.
- Install the cumulative QA APK on Mi TV Stick Android TV 11 at 1920x1080.
- Repeat DOWN/UP at least 10 cycles and test rapid direction reversal.
- Confirm no focus race, Dock drift, missing tiles, stutter, or Weather/AQI regression.
- Keep draft/unmerged until physical acceptance passes. CI PASS is not device PASS.
