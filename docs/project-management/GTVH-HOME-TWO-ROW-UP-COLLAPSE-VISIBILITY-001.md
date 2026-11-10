# GTVH-HOME-TWO-ROW-UP-COLLAPSE-VISIBILITY-001

Date: 10 November 2026
Repository: `Frangga11/glass-tv-home`
Branch: `fix/home-two-row-up-collapse-visibility-001`
Base: `fix/home-two-row-dpad-weather-listener-001`
Source starting HEAD: `1998eff271ccacef63ea2f2e48fd5831caa3ce3c`
Status: implementation candidate; CI and physical-device verification pending.

## Trigger and supplied evidence
User supplied a 35.9-second Mi TV Stick recording. The two-row expansion is visually correct. On DPAD UP from the first overflow row, the application rows disappear while the header, weather/AQI, and wallpaper remain. DPAD DOWN can reveal the rows again.

## Root-cause analysis
The reverse transition animates both RecyclerViews downward by one measured row pitch while the expanded LayoutParams are still active. At animation end, code switches to the idle LayoutParams (reducing the Dock bottom margin by the same pitch) while retaining the compensating positive translation, then clears that translation from a plain `View.post()` callback. A post callback is not a guarantee that the requested LayoutParams have completed their next layout traversal. The transform can therefore be cleared against stale geometry or after an intermediate frame has been committed, leaving the Dock outside its intended visible frame. The overflow Grid is also hidden at this boundary, so the user sees no application row.

## Corrective change
- Keep the expanded geometry and reverse animation unchanged.
- Apply idle LayoutParams at animation completion while the positive translation still compensates for the changed anchor.
- Clear both RecyclerView translations in a one-shot `ViewTreeObserver.OnPreDrawListener`, after the new idle layout has been traversed and before the next frame is drawn.
- Commit same-column Dock focus only after this settle point.
- Retain a detached-view fallback that restores neutral translations.
- Add a Python source-contract test requiring the pre-draw settle and forbidding the previous plain-post settle path.

## Protected scope
No change to app inventory, item identities/order, five-column mapping, tile dimensions, launch behavior, Weather/AQI data/providers/cadence, wallpaper, Hero Shelf, Islamic content, prayer timetable, Wi-Fi, or the successful DOWN expansion animation.

## Validation / next actions
1. Run PR CI and inspect the generated APK against the exact head.
2. Install on Mi TV Stick Android TV 11 at 1920x1080.
3. Verify idle Dock visible; DOWN reveals overflow below; UP restores the primary row at the exact idle anchor and same column; repeat DOWN/UP several times and test rapid direction reversal.
4. Verify later overflow rows and Weather/AQI entry remain intact.
5. Keep PR draft/unmerged until physical acceptance. CI PASS is not device PASS.
