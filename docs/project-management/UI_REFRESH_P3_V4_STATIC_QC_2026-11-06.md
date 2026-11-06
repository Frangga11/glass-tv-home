# Glass TV Home — P3 V4 Static Interaction & Source QC

**Document ID:** GTVH-UIR-P3-V4-QC-001  
**Date:** 6 November 2026  
**Artifact:** `prototypes/UI_REFRESH_P3_INTERACTIVE_PROTOTYPE_V4.html`  
**Artifact blob:** `081d48f35a9e0eb5228642909be7a5697e3cdb27`

## 1. Result

**V4 STATIC SOURCE QC: PASS.**  
**BROWSER WALKTHROUGH / VISUAL ACCEPTANCE: NOT RUN.**  
**P3: IN PROGRESS / PARTIAL.**

V4 preserves V3 and adds three targeted corrections found during deeper source inspection.

## 2. Corrections

| ID | Finding in V3 | V4 correction | Static result |
|---|---|---|---|
| V4-R01 | Weather refresh disabled the focused button during loading, risking focus loss | Keep the control enabled, set `aria-busy`, reject duplicate activation with an internal busy flag, clear busy after completion | PASS |
| V4-R02 | Wi-Fi permission/location/condition could change during a pending scan; stale timeout could overwrite the new state | Increment request generation on every prerequisite change; stale completion callback exits without changing current UI | PASS |
| V4-R03 | Modal arrow navigation existed but Tab/Shift+Tab could move focus outside the dialog; Backspace could close while typing in a text field | Add modal-scoped Tab/Shift+Tab wrap and preserve Backspace editing in INPUT/TEXTAREA; Escape remains close/back | PASS |

## 3. Static quality checks

- Exactly one embedded JavaScript script block; syntax parsed successfully using a JavaScript parser without executing browser code.
- No duplicate HTML IDs.
- Weather refresh does not disable its invoking control; busy state is cleared.
- Wi-Fi scan completion is guarded by request identity; condition changes invalidate prior pending completion.
- Modal Tab and Shift+Tab focus wrapping is present.
- Backspace does not close a dialog while editing INPUT/TEXTAREA.
- All V3 corrections remain present: nested dialog stack, modal D-pad, Language, Wallpaper Interval, Next Photo failure/unavailable, Wi-Fi scan completion, profile switch and distinct Restart/Power Off confirmations.
- Dock orientation/capacity controls and fixed four-column app grid remain present.
- Weather V4 metric families remain present in the prototype source.
- Static checks do not prove actual browser behavior, rendered geometry, readability or remote-control behavior.

## 4. Execution limitation and remaining tests

No browser session was executed in this run. The browser runtime could not be connected to the repository artifact in this environment; therefore no interaction scenario is marked passed based on a simulated or inferred result.

The 12-scenario walkthrough remains NOT RUN:
1. Popup / nested dialog focus
2. D-pad / modal traversal
3. Long labels / large values
4. Wallpaper contrast
5. Loading / success / failure
6. Permission / offline / unavailable
7. Weather refresh focus stability
8. Wi-Fi throttled / stale
9. Next Photo 0/1/many
10. Dock orientation × capacity
11. Exact-control search
12. Reduced motion

Visual comparison against the six approved V01–V06 reference images and 100/125/150% zoom readability review also remain NOT RUN.

## 5. Change boundary

- V3 remains preserved unchanged.
- V4 is a new versioned prototype-only artifact.
- No production Android source, feature branch, PR, integration/main, APK, ADB or physical device changed.
- P4 remains blocked pending actual browser walkthrough, visual acceptance and implementation-readiness review.

**Disposition:** V4 STATIC SOURCE QC PASS; P3 remains PARTIAL.


## 6. P3-01 to P3-12 static evidence mapping

| ID | Static source evidence | Static disposition | Browser execution |
|---|---|---|---|
| P3-01 | Parent dialog stack, action-index restoration, root invoking-element restoration | SOURCE PRESENT | NOT RUN |
| P3-02 | Spatial D-pad movement inside modal; Tab/Shift+Tab wrap; Escape closes; Backspace in text input is safe | SOURCE PRESENT | NOT RUN |
| P3-03 | Clamp-based text sizes, flexible grid and min-width rules are present | PARTIAL — no worst-case locale/zoom/geometry test | NOT RUN |
| P3-04 | Normal/Day/Night modes and day contrast variables are present | SOURCE PRESENT — visual legibility not proven | NOT RUN |
| P3-05 | Weather loading/result states, cache result dialog, Next Photo unavailable/failure path | SOURCE PRESENT | NOT RUN |
| P3-06 | Wi-Fi permission, Location off, offline, throttled, stale and empty states | SOURCE PRESENT | NOT RUN |
| P3-07 | Weather Refresh retains enabled invoking button with busy guard | SOURCE PRESENT | NOT RUN |
| P3-08 | Wi-Fi request-generation guard invalidates stale scan callback after prerequisite changes | SOURCE PRESENT | NOT RUN |
| P3-09 | Next Photo disabled for 0/1; success and failure branches for 2/5 | SOURCE PRESENT | NOT RUN |
| P3-10 | Horizontal/Vertical selector × capacity 4/5/6; app grid fixed to four columns | SOURCE PRESENT — all six combinations need visual walkthrough | NOT RUN |
| P3-11 | Seven-category control dataset, query routing, search-focus retention and Down-to-first-match | SOURCE PRESENT | NOT RUN |
| P3-12 | Reduced-motion class disables transitions/animations; OS reduced-motion media rule exists | SOURCE PRESENT | NOT RUN |

**Important:** “SOURCE PRESENT” is not a test PASS. P3 remains open until the browser walkthrough produces actual observations and evidence. P3-03/P3-04/P3-10 particularly require rendered visual review.
