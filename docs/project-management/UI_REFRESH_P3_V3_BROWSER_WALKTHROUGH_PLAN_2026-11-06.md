# Glass TV Home — P3 V3 Browser Walkthrough Plan

**Document ID:** GTVH-UIR-P3-WALK-001  
**Date:** 6 November 2026  
**Prototype:** `prototypes/UI_REFRESH_P3_INTERACTIVE_PROTOTYPE_V3.html`  
**Purpose:** reproducible manual browser/keyboard validation. This is a test plan, not evidence that tests have been run.

## Test setup

- Open V3 in a desktop browser at a 16:9 viewport approximating 1920×1080.
- Use keyboard arrows as a D-pad proxy; Enter activates; Esc/Back closes.
- Keep browser zoom at 100% for baseline, then repeat readability checks at 125% and 150%.
- Record actual viewport, browser, test date, pass/fail and screenshot for each scenario.
- Sample data is illustrative. Do not treat Wi-Fi/weather results as live device data.
- Run both Normal and Day-glare modes; include Night mode and reduced motion where applicable.

## 12-scenario walkthrough matrix

| ID | Steps | Expected result | Evidence to capture |
|---|---|---|---|
| P3-01 Popup / nested dialog focus | Home → focus Wi-Fi → Enter → Kelola Wi-Fi → Back → close Wi-Fi popup | Back returns to parent Wi-Fi dialog; closing parent returns focus to original Home Wi-Fi pill | Focus status and screenshot at each return |
| P3-02 D-pad / modal traversal | Open confirmation, move Left/Right between actions, Enter, repeat with nested Wi-Fi handoff | Focus remains within modal; action activation is deterministic; no focus jump behind modal | Action focus sequence and edge behavior |
| P3-03 Long labels / large values | Increase viewport/zoom; inspect long Indonesian labels, large weather/AQI values and long profile names | No clipping, overlap, hidden action or obscured focus ring | Screenshots at 100/125/150% |
| P3-04 Wallpaper contrast | Toggle Normal, Day-glare and Night on bright/high-detail and dark wallpaper contexts | Text and focus remain readable; dense panels remain legible; semantic colors unchanged | Screenshots of each mode and focus state |
| P3-05 Loading / success / failure | Refresh Weather; run cache success/failure; Next Photo success/unavailable/load failure | Loading resolves; failure preserves previous state; success only follows state change | Before/loading/after screenshots and status |
| P3-06 Permission / offline / unavailable | Set Wi-Fi permission denied; Location off; offline; no network | Scan list is hidden/labelled appropriately; system handoff remains clear; no fake success | Screenshots and state labels |
| P3-07 Weather refresh focus stability | Focus refresh button, activate, wait for result | Focus remains on invoking refresh control; no scroll jump | Focus before/after refresh |
| P3-08 Wi-Fi throttled / stale | Select throttled and stale; request rescan | No repeated scan claim; stale timestamp retained; status explains limitation | Screenshots and status text |
| P3-09 Next Photo 0/1/many | Select 0, 1, 2, 5 photos; invoke Next Photo where enabled; test source unavailable/failure | Disabled at 0/1; valid index advances at 2/5; failure preserves active index | State text and selected index |
| P3-10 Dock orientation × capacity | Test Horizontal/Vertical × 4/5/6 | Six combinations render; app grid stays four columns and independent; no clipping/overlap | Six screenshots and grid comparison |
| P3-11 Exact-control search | Search unique controls (e.g. “Avatar GIF”, “Kapasitas Dock”, “Lokasi”); Down; Enter; clear query; search no match | Search retains focus while typing; correct owning category and first matching control receive focus; empty/no-match states clear | Search term, category, focus and result count |
| P3-12 Reduced motion | Toggle reduced motion on/off; traverse focus and open/close dialog | Nonessential motion is removed; focus and state changes remain visible and immediate | Paired screenshots/video or written observations |

## Exit criteria

- All 12 scenarios have actual execution evidence, not source-presence inference.
- No High-severity focus/dialog failure remains.
- No clipping, overlap, inaccessible focus or misleading state remains in reviewed viewport/zoom cases.
- All six user-approved reference surfaces are compared against V3; any intentional deviation is recorded against P2.
- V3 revision and test evidence are versioned in canonical docs.
- P3 owner/QA review is recorded. P4 stays blocked until the user-approved visual direction is preserved and implementation-readiness checks pass.

**Current status:** NOT RUN. Static V3 source QC is PASS; this plan does not claim browser execution.
