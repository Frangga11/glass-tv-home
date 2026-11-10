# GTVH-HOME-TWO-ROW-DPAD-TRANSITION-LATENCY-001

Date: 11 November 2026
Repository: `Frangga11/glass-tv-home`
Branch: `fix/home-two-row-dpad-transition-latency-001`
Base: `fix/home-two-row-stable-dock-anchor-001`
Status: geometry refinement candidate; CI and physical-device validation pending.

## Trigger
Physical Mi TV Stick acceptance feedback reports that two-row DPAD navigation is now visually correct and Dock restoration works, but the transition feels somewhat delayed.

## Scoped optimization
- Reduce normal transition duration from 260 ms to 190 ms.
- Reduce reduced-motion duration from 140 ms to 120 ms.
- Keep the existing DecelerateInterpolator, translation geometry, alpha behavior, and focus-after-settle sequencing.
- Apply the same timing to DOWN expansion and UP collapse.
- Set two-row start-to-start Row Pitch to exactly 150dp.
- Set AppAdapter design card size to 182×100dp (width×height); preserve proportional down-scaling when the viewport requires it.
- Reduce AppAdapter item extra height from 45dp to 42dp.
- Keep the measured one-row viewport, Dock anchors, focus mapping, app identity/order, and protected Home surfaces/data unchanged.

## Rationale
A 190 ms transition shortens perceived navigation latency while retaining enough frames for a visible, controlled slide on a 60 Hz TV display. The 120 ms reduced-motion path remains shorter. These are candidate timings and must be judged on the actual Mi TV Stick.

## Validation
- Run CI and contract tests against the exact PR HEAD.
- Install the cumulative QA APK on Mi TV Stick Android TV 11 at 1920x1080.
- Repeat DOWN/UP at least 10 cycles and test rapid direction reversal.
- Confirm no focus race, Dock drift, missing tiles, stutter, or Weather/AQI regression.
- Keep draft/unmerged until physical acceptance passes. CI PASS is not device PASS.


## Geometry refinement (11 November 2026)
- Row Pitch is now an explicit 150dp constant, independent of the measured viewport height and its existing top padding.
- Card design ratio is 182:100; the card may still scale down to fit the five-column viewport.
- Item extra height is 42dp. At the nominal design size, card height + item extra = 142dp; the remaining 8dp is the intended row rhythm to reach 150dp when no extra top inset is present.
- Physical edge-to-edge gap must be confirmed from the Mi TV Stick render because the actual fitted card width and existing grid top padding can change the visible blank gap.
