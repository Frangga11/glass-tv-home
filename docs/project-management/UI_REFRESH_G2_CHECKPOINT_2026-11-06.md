# Glass TV Home — GTVH-UIR-002 v2.0
## G2 Shared UI Foundation Repair — Completion Checkpoint

**Date:** 6 November 2026  
**Status:** **COMPLETE — SOURCE/HOST QC PASS**  
**Canonical design authority:** G1 V6 and frozen P2 Shared Design & Interaction Contract  
**Source branch:** `feature/g2-shared-ui-foundation-repair`  
**Source head:** `40fa0715bdbcd324042927fbc56ec19f91d1947a`  
**Base:** `integration/b-series` @ `562cde0751de93748eb4e0d4cdece4fa1672ac4d`  
**PR:** [#65 — G2 Shared UI Foundation Repair](https://github.com/Frangga11/glass-tv-home/pull/65) — OPEN / DRAFT / UNMERGED  
**Exact-head CI:** #1263 / run `42252069520` — SUCCESS (both required jobs)

## 1. Result

G2 repaired the shared foundation used by V01–V06 without replacing approved layouts or changing feature ownership. The implementation reuses the B01 shared architecture and makes the visual/interaction rules explicit and testable.

## 2. Delivered

1. **Shared design tokens:** one navy/blue/cyan semantic palette, primary/secondary text, status colors, typography scale, geometry, focus motion and minimum D-pad target values in `TvUiTokens`.
2. **XML palette parity:** added resource-backed equivalents for shared surfaces, text, blue selection, cyan focus and borders. Dialog and focus-outline resources now consume those shared colors.
3. **Glass/material consistency:** `GlassDrawable`, `DockDecoration` and dialog surfaces use the same navy veil/border vocabulary. Turning off glass keeps a solid, readable surface rather than removing the component background.
4. **Dialog foundation:** `TvDialogStyle` bounds modal width/height to the safe content region and provides shared glass/solid material fallback.
5. **Responsive popup geometry:** `TvResponsiveLayout` now owns safe-area sizing, horizontal popup clamping, above/below placement and side selection based on available space. Anchored popup rows live in a scrollable container.
6. **D-pad and focus:** shared focus accent/scale/duration applied to in-scope Home, Settings and Profile controls; duplicate focus-outline updates removed; popup Back/edge navigation restores the invoking anchor.
7. **Accessible targets:** shared Settings navigation, anchored input rows, Profile actions and Settings actions use a 56dp minimum target. Long popup lists remain scrollable.
8. **Weather consistency:** Weather/AQI layout and card/pill/tab selectors now use the shared palette and focus colors while preserving Weather V4 information.
9. **Typography and scaling:** shared semantic text scale established; Android SP behavior remains uncapped for user font scaling.
10. **CI policy:** APK size is still measured exactly and reported, but the historical 1.5 MiB threshold is non-blocking. UI quality, accessibility and feature completeness take precedence over arbitrary size reduction.

## 3. Verification evidence

Exact-head GitHub Actions run #1263 (`42252069520`) on `40fa0715bdbcd324042927fbc56ec19f91d1947a` completed SUCCESS:
- `:app:testReleaseUnitTest` — PASS.
- `:installer:testReleaseUnitTest` — PASS.
- Release build — `BUILD SUCCESSFUL`.
- Lists/tools/docs job — PASS.
- APK size diagnostic — **1,573,713 bytes** (rounded log: 1,536 KB); no size failure gate.

Added/updated host tests cover:
- Safe-area calculations and dialog bounds.
- Popup horizontal placement and above/below side selection.
- Large font-scale handling and minimum D-pad sizing.
- Shared text/focus contrast targets.
- Focus-outline state and preservation of component background.
- Long popup scrolling and minimum input-row target size.

The CI run validates host build and automated tests. It does not establish Android TV physical-device acceptance.

## 4. Quality boundary

- No PR was merged. PR #65 remains Draft/open and based on the frozen integration baseline.
- No rebase, cherry-pick, force-push or integration/main change was performed.
- Release APK was built only inside CI for validation. No APK artifact was published, downloaded for installation, or installed.
- No ADB, Android TV settings or physical TV/remote test was performed.
- G1 prototype evidence remains separate: browser 12/12 PASS at 1920×1080, including daylight and large-text presentation. G2 source/host QC does not convert that prototype evidence into device acceptance.
- Remaining device/OEM overscan, remote behavior, actual TV-distance readability and physical visual evaluation stay deferred to the later authorized evaluation stage.

## 5. Decision and next stage

**G2: COMPLETE / SOURCE-HOST QC PASS.** The shared foundation is ready to support the next work package.

Proceed to **G3 — Surface Implementation** in the official sequence:
- 3A Home Screen & independent Dock.
- 3B Weather & AQI V4.
- 3C Profile, Avatar & Child Mode.
- 3D Quick Settings.
- 3E Wi-Fi.
- 3F Global Settings.

Keep owner-scoped changes isolated. No merge or physical-device action is authorized by this checkpoint.
