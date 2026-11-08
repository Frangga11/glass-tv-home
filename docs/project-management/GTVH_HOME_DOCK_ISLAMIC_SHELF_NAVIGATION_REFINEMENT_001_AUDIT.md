# GTVH-HOME-DOCK-ISLAMIC-SHELF-NAVIGATION-REFINEMENT-001
## Audit, implementation, and exact-head validation record

**Date:** 8 November 2026  
**Repository:** Frangga11/glass-tv-home  
**Work branch:** fix/home-dock-islamic-shelf-navigation-001  
**PR:** [#81](https://github.com/Frangga11/glass-tv-home/pull/81) — OPEN / DRAFT / UNMERGED  
**Base:** fix/home-wallpaper-text-refinement-001 (PR #80), exact base HEAD a085be150ebcf1d48b825ed7ac43b172261f3aa0  
**Exact implementation HEAD:** 2130675f1f2723f8e7c895cbc6ac63327d2efd60  
**Status:** SOURCE PASS / EXACT-HEAD CI PASS / RELEASE APK EXTRACTED / VISUAL AND PHYSICAL ACCEPTANCE PENDING / NOT FINAL

---

## 1. Trigger and acceptance intent

The owner requested four scoped improvements without disturbing the approved Home composition:

1. Reduce Dock card/icon proportions.
2. Reduce total horizontal Dock host height by approximately 8dp without clipping content or glass effects.
3. Stabilize prayer timetable and daily Ayah shelf position when focus changes.
4. Make DOWN/UP row navigation natural, opposite in direction, deterministic, and safe under rapid remote input.

The protected areas remain unchanged: Header and top navigation, clock/date/location/altitude/weather/AQI, global safe frame, wallpaper image/crop/provider/rotation, wallpaper title/description content, prayer data/order, Ayah Arabic/RTL/translation, application order/favourites, profile/child mode, click/long-press and unrelated remote behavior.

## 2. Repository and dependency verification

- PR #80 was verified OPEN / DRAFT / UNMERGED at HEAD a085be150ebcf1d48b825ed7ac43b172261f3aa0.
- PR #81 was created on a separate branch and stacked directly on PR #80's exact HEAD.
- PR #80 source was not modified by this workstream.
- PR #81 remains OPEN / DRAFT / UNMERGED; no merge or force-push was performed.
- PR #81 exact HEAD is 2130675f1f2723f8e7c895cbc6ac63327d2efd60; base is a085be150ebcf1d48b825ed7ac43b172261f3aa0.
- PR #81 has eight changed files, all directly related to Dock geometry, Islamic shelf anchoring, row navigation, CI artifact eligibility, or regression contracts.

## 3. Audit findings and root causes

### A. Dock card and icon proportions

**Finding:** DockAdapter reused AppAdapter's Grid card-width geometry. The item XML also fixed the icon at 48dp, which is shared by Dock and Grid. A global XML edit would have resized Grid icons unintentionally.

**Decision:** Keep AppAdapter/Grid dimensions and item XML unchanged. Add Dock-only width helpers and apply Dock icon dimensions at the DockAdapter binding layer.

### B. Dock host height and clipping risk

**Finding:** Dock host height includes both tile/item height and additional host space. RecyclerView decoration offsets also contribute to the decorated child extent. Reducing host height alone while retaining the old 6dp/12dp horizontal decoration offsets would risk clipping the child/decorated area.

**Decision:** Calibrate total host height, not just its spare-height constant. The card shrink contributes about 4dp; the spare host height is reduced from 18dp to 14dp for approximately 4dp more. Horizontal decoration offsets are 6dp top / 8dp bottom (14dp total). Vertical Dock offsets remain 6dp / 12dp. The tray remains full width and its bottom placement is maintained through the existing margin geometry.

### C. Islamic shelf movement

**Finding:** showHero() accepted the currently focused card's Y coordinate in horizontal Dock mode. It also derived the Islamic shelf Y using the panel's current measured height during focus handling. Focus and measurement timing were therefore coupled to positioning.

**Decision:** Derive the shelf anchor from the first Dock card (horizontal mode) or a stable virtual Dock baseline (vertical mode), never from the focused card. Preserve the pre-refinement shelf baseline while the Dock is resized. Store the Islamic shelf's bottom anchor and reapply its Y when the panel's measured height changes. showHero()/hideShelf() retain their visibility fades; hideShelf() does not alter Y or translationY.

### D. DOWN/UP row motion

**Finding:** The horizontal overflow Grid used scrollToPositionWithOffset() to change rows, producing an immediate jump rather than a directional content transition.

**Decision:** Add a serialized row-transition path using smoothScrollBy(), pending adapter-position targets, generation guards, and stopScroll() when a newer remote press supersedes the active destination. The signed scroll delta is isolated in DockGeometry.gridRowScrollDeltaPx(); positive values move content upward (DOWN), negative values move content downward (UP). Focus is requested after the target row is visible. Incomplete last rows are clamped to the final valid adapter position. Programmatic restore keeps the existing immediate alignment fallback.

## 4. Implemented geometry

- Dock scale factor: 0.95 of the existing card-width formula.
- Mi TV Stick 1920px / density 320 / 29dp left-right safe frame / five-slot Dock:
  - Safe content width: 1804px.
  - Dock card: 287 × 161px, approximately 143.5 × 80.5dp (target 144 × 81dp).
- Dock icon: 44 × 44dp at Dock binding only.
- Horizontal Dock total host height: approximately 8dp shorter after combining the card-height reduction and 4dp reduction in spare host height.
- Horizontal Dock decoration offsets: 6dp top / 8dp bottom; vertical Dock offsets remain 6dp / 12dp.
- Horizontal Dock card slots use a 32dp nominal inter-card gap, constrained by available capacity width. The glass tray remains full width.
- Grid card geometry remains on the original AppAdapter path; Grid icon XML remains 48 × 48dp.
- Dock capacities 4, 5 and 6 have geometry regression coverage.

## 5. Changed files

1. `.github/workflows/ci.yml` — include PR #81 in the scoped Home visual-parity APK artifact step.
2. `launcher/app/src/main/java/com/gathin/tvhome/DockAdapter.java` — Dock-only tile geometry and 44dp icon sizing.
3. `launcher/app/src/main/java/com/gathin/tvhome/DockDecoration.java` — coordinated horizontal decoration insets.
4. `launcher/app/src/main/java/com/gathin/tvhome/DockGeometry.java` — Dock-only geometry helpers, compact host height, stable shelf baseline helper, signed row-scroll math.
5. `launcher/app/src/main/java/com/gathin/tvhome/MainActivity.java` — stable Islamic shelf anchor, independent visibility animation, smooth serialized row transitions, lifecycle cancellation.
6. `launcher/app/src/test/java/com/gathin/tvhome/DockLayoutModelTest.java` — Dock capacity, 144×81dp target, total host-height, decoration and signed inverse-scroll unit coverage.
7. `tests/test_home_dock_islamic_shelf_navigation_refinement_contract.py` — scoped source/layout regression contracts.
8. `tests/test_home_layout_bounds_contract.py` — update the Dock-specific bottom-margin contract to the separated Dock geometry.

No AppAdapter sizing, item_app.xml Grid icon size, application inventory/order, wallpaper asset/provider, prayer data, or Header/safe-frame code was changed.

## 6. Exact-head CI evidence

**CI:** run #1620 / run ID 42501726878  
**Validated HEAD:** 2130675f1f2723f8e7c895cbc6ac63327d2efd60  
**Result:** SUCCESS — both jobs passed.

- Lists, tools and docs: SUCCESS.
- Python regression suite: 176 tests, OK (1 skipped).
- Android command: `./gradlew --no-daemon testReleaseUnitTest assembleRelease`.
- Android Release unit-test tasks for app and installer: SUCCESS.
- Release assemble: SUCCESS.
- Gradle result: BUILD SUCCESSFUL in 1m 19s; 99 actionable tasks.
- APK size diagnostics and artifact upload: SUCCESS.
- Historical 1.5 MiB APK-size warning is informational/non-blocking, not a CI failure.

Earlier CI iterations found and corrected stale variable references and outdated geometry contract expectations. The final exact-head CI above validates the corrected source and tests.

## 7. Release APK output

- CI artifact name: `glass-tv-home-dock-visual-parity-576aa52086f2d0570cf5a1e7fa7c519998a83311`.
- Artifact ID: 13625911589.
- Artifact ZIP size: 1,428,433 bytes.
- Artifact ZIP SHA-256: `e9dbc0307aa7823d019ccf464e21e633f6dfe1a3a5906407feee3ada6fa1ea41`.
- ZIP was downloaded and inspected; it contains exactly one `app-release.apk`.
- Release APK size: **1,629,469 bytes** (1,591 KB).
- Release APK SHA-256: `caa3289641184e516b5758b8ce647d7bc33616ee55cb5f022576e53add572e24`.
- The separate publisher workflow was skipped by design for this PR. The APK was extracted from the successful CI artifact; it is not a merged/final GitHub release.

## 8. Remaining acceptance gates

The following are **not yet validated** and must not be inferred from CI:

- Rendered 1920×1080 visual review for Dock proportions, spacing, tray clipping, glow/shadow bounds and wallpaper-title clearance.
- Physical Mi TV Stick testing of Home first load, Dock left/right, DOWN/UP single and repeated presses, fast DOWN→UP reversal, final/partial rows, focus identity, panel stability, Dock capacities 4/5/6, and return to Home after launching another app.
- No ADB session, device install, device setting change or physical Mi TV Stick test was performed by the assistant.
- No merge was performed.

**Final status:** SOURCE PASS / EXACT-HEAD CI PASS / APK BUILT AND HASHED / VISUAL AND PHYSICAL ACCEPTANCE PENDING / NOT FINAL.

**Next action:** owner performs the physical Mi TV Stick test using the exact-head APK, records the visual and remote-navigation results, then requests any scoped corrective pass. Keep PR #81 DRAFT and unmerged until acceptance.
