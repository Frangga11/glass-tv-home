# GTVH-HOME-WALLPAPER-TEXT-REFINEMENT-001 — Source Audit & Corrective Implementation Record

**Date:** 8 November 2026  
**Repository:** `Frangga11/glass-tv-home`  
**PR:** [#80](https://github.com/Frangga11/glass-tv-home/pull/80) — OPEN / DRAFT / UNMERGED  
**Branch:** `fix/home-wallpaper-text-refinement-001`  
**Corrective HEAD:** `a085be150ebcf1d48b825ed7ac43b172261f3aa0`  
**Base:** `fix/home-balanced-frame-001` at `b4abdf0bd555a0106c135add20c3f287cf10b412` (PR #79)

## 1. Trigger and strict scope

Owner approved corrective implementation for the Home wallpaper title/description, prayer timetable and daily Ayah. The operation is restricted to those visible elements and their immediate positioning/readability treatment. Protected areas: Dock/Grid geometry, icon sizes/order, scroll/focus behavior, Header/navigation/capsules, wallpaper image/crop/provider/rotation, prayer data/provider/order, global safe frame and unrelated components.

## 2. Baseline verification and root cause

- Canonical restart file read first: `docs/project-management/GTVH-CHECKPOINT-LATEST.md` on `docs/project-master-inventory`.
- PR #80 was OPEN / DRAFT / UNMERGED on branch `fix/home-wallpaper-text-refinement-001`, stacked on PR #79.
- Existing wallpaper metadata remains unchanged: location `Pagar Alam, Sumatera Selatan`; title `Kabut Pagi Dempo`; description `Udara sejuk turun perlahan, menyelimuti lereng dan kebun teh Pagar Alam.`
- Audit of `DockGeometry.shelfBottomOffsetFromTileTopPx()` confirmed 50dp total offset: 14dp tray padding + 36dp intended shelf-to-tray breathing gap.
- Prior PR #80 implementation added a 5mm downward offset = 31.496dp = 62.992px at density 2.0. This reduced the intended 36dp tray clearance to about 4.5dp, explaining the visual crowding. That downward offset is superseded.
- Prayer text was drawn with per-text gradient shaders, while runtime calls to `Typeface.DEFAULT` / `DEFAULT_BOLD` overrode the Inter family declared in XML. This combination reduced consistency and crispness. The correction removes the shader and default typeface overrides.

## 3. Corrective before / after

| Component | Prior PR #80 rendering | Corrective target |
|---|---|---|
| Wallpaper title/description | 5mm shifted down; about 4.5dp visual clearance above tray | Up about 28dp; nominal 32.5dp tray clearance, 32dp minimum |
| Daily Ayah block | Shared baseline with prayer timetable | Up 18dp |
| Prayer header/table | Shared baseline with Ayah | Up 24dp total (18dp parent + 6dp child translation) |
| Prayer names/times | Gradient shader + runtime default Typeface override | Solid high-contrast colors; retain XML Inter; 13sp names / 17sp times |
| Arabic Ayah | 17sp at 1080p | 20sp at 1080p / 22sp at 4K; preserve Amiri Quran, RTL and high-quality line breaking |
| Translation | 12.5sp at 1080p | 14sp at 1080p / 15sp at 4K |
| Right information background | Text over variable wallpaper without dedicated subtle scrim | Transparent-to-dark gradient scrim, no border/panel |
| Ayah focus | Focus-only outline and DPAD/click | Preserved unchanged |

## 4. Measured vertical geometry

At target Mi TV Stick density 320dpi, `density = 2.0`.

- Existing baseline offset from tile top: 50dp.
- Desired tray breathing gap: 32–40dp; use **32dp minimum**.
- New baseline offset: 3.5dp. Resulting shelf-to-tray clearance: 50dp − 14dp tray inset − 3.5dp = **32.5dp** nominal.
- Compared with the prior +31.5dp downward offset, new +3.5dp offset moves the left wallpaper shelf upward **28dp**.
- The right `islamicShelf` parent uses an additional 10dp bottom offset relative to the common anchor. Relative to prior PR #80, the daily Ayah rises **18dp**.
- Prayer header and table each use `translationY=-6dp`, making the prayer timetable rise **24dp** total.
- The existing Dock geometry remains the measurement reference; `DockGeometry.java` is not changed.

## 5. Implementation files

1. `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java`
   - Replace downward 5mm offset / 12dp guard with 3.5dp baseline offset / 32dp minimum gap.
   - Separate daily Ayah and prayer timetable vertical positions.
   - Add subtle transparent-to-dark gradient scrim behind the right information shelf.
   - Use solid high-contrast prayer colors and preserve XML Inter by removing runtime default typeface overrides and per-text gradient shader.
   - Increase Arabic and translation sizes while retaining native Arabic font, RTL, high-quality line breaking and existing activation/focus behavior.
2. `launcher/app/src/main/res/layout/activity_main.xml`
   - Keep location → title → description order.
   - Apply 6dp upward translation to prayer header/table.
   - Increase prayer and Ayah typography; reduce overly broad text shadows to improve edge clarity.
3. `tests/test_home_wallpaper_text_refinement_contract.py`
   - Six contracts for metadata/order, safe shelf clearance, distinct Ayah/prayer offsets, solid prayer text/native font, Arabic/translation sizing and focus/scrim behavior.

## 6. Quality checks and evidence

| Gate | Result | Evidence |
|---|---|---|
| Scope review | PASS (source diff) | Corrective source edits limited to MainActivity.java and activity_main.xml; one focused test file updated |
| Wallpaper metadata preservation | PASS (contract added) | Exact location/title/description strings remain asserted |
| Dock clearance / distinct vertical offsets | CONTRACT ADDED; CI PENDING | 32dp minimum, 3.5dp baseline, 10dp parent offset, 6dp prayer child offset |
| Prayer text rendering | CONTRACT ADDED; CI PENDING | Solid colors; no shader method or Typeface.DEFAULT override |
| Arabic/translation rendering | CONTRACT ADDED; CI PENDING | 20/14sp at 1080p, 22/15sp at 4K; Amiri Quran + RTL |
| Daily Ayah focus/activation | PRESERVED IN SOURCE; CI PENDING | Existing focus-only stroke and click/DPAD listeners remain |
| Exact-head GitHub CI | IN PROGRESS | CI #1599 / run `42497894475` on corrective HEAD `a085be1` |
| Rendered 1920×1080 screenshot | PENDING | No rendered screenshot evidence available |
| Physical Mi TV Stick acceptance | PENDING | No ADB, install or device test performed |

Prior CI run `42493469262` passed 166 Python tests (1 skipped), Android release unit tests and `assembleRelease`, but it validated the old PR #80 HEAD `c7d95c3de24bc70b671159d5aa9ef49451cbe112` only. It must not be represented as validation of corrective HEAD `a085be1`.

## 7. Regression boundaries

- No change to `DockGeometry.java`, Dock/Grid dimensions/order/scroll/focus, Header/capsules, navigation, global safe-frame values, wallpaper image/crop/provider/rotation, prayer timetable data/provider/order, or unrelated components.
- No ADB session, APK installation, device setting/default-launcher mutation, force-push or merge.
- PR #80 remains OPEN / DRAFT / UNMERGED.
- Text size and scrim appearance must still be visually checked against bright and dark wallpapers at normal TV viewing distance.

## 8. Final disposition

**SOURCE CORRECTION COMMITTED: PASS. EXACT-HEAD CI: PENDING. RENDERED / PHYSICAL ACCEPTANCE: PENDING. OVERALL: NOT FINAL.**

Next: monitor exact-head CI #1599 / run `42497894475` on `a085be150ebcf1d48b825ed7ac43b172261f3aa0`, review rendered 1920×1080 output, then wait for owner Mi TV Stick acceptance. Any further correction must remain inside the same strict visual scope.
