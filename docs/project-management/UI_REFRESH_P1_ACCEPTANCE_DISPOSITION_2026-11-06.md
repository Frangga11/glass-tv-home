# Glass TV Home — P1 Inventory Acceptance & Carry-Forward Disposition

**Document ID:** GTVH-UIR-P1-ACCEPT-001  
**Date:** 6 November 2026  
**Authority:** User's instruction to complete the current Trigger without repeated approval unless scope or risk changes, plus prior explicit approval of references V01–V06 and agreed functional boundaries.

## 1. Decision

**P1 INVENTORY: ACCEPTED FOR PROGRESSION TO P3 COMPLETION.**

This acceptance closes the inventory/requirements-capture gate only. It does not claim runtime parity, implementation completion, device acceptance or waiver of known gaps.

The six user-provided references V01–V06 remain the approved visual source of truth. Preserve the approved blue-glass direction, restrained cyan focus treatment, high readability over wallpaper and full Weather V4 information scope.

## 2. Binding scope decisions carried forward

1. **V01 Home/Dock:** Dock is an independent presentation region from the app grid. Horizontal/Vertical orientation and 4/5/6 capacity are separate controls; default capacity 5. Dock changes must not alter app-grid columns or silently drop apps.
2. **V02 Weather/AQI:** retain the full approved Weather V4 metric scope. Do not delete metrics to imitate a compact reference. Source, location, update time and modeled-vs-official AQI wording must remain truthful.
3. **V03 Profile:** active profile and D-pad focus are distinct. Child Mode/PIN and profile-scoped data boundaries remain intact.
4. **V04 Quick Settings:** retain a compact quick-action surface distinct from the full Settings shell. Next Photo is owned by the canonical E08 wallpaper controller; do not create a second wallpaper source.
5. **V05 Wi-Fi:** status and connected-network information where Android exposes them; conditional nearby scan/list/rescan only with required permissions and Location prerequisites; Android Wi-Fi Settings handoff for system-managed radio/network operations. No fake radio toggle, unrestricted saved-network management or claim of direct connection.
6. **V06 Global Settings:** retain the approved seven-category taxonomy and control-level search demonstration; feature ownership remains with the owning modules.
7. Shared focus, dialogs, surface/contrast, responsive layout, localization, feedback and Back restoration use one B01/B02–B07 contract; no parallel module-specific visual systems.

## 3. Accepted carry-forward gaps — not waived

| Gap | Owner | Carry-forward gate |
|---|---|---|
| Weather V4 field-by-field source traceability and Indonesia/provider attribution | E05/E01 | P3/P4 surface contract and P5 data QA |
| QR upload authorization/token, profile no-profile/capacity invariants | E02 | E02 owner source/security audit |
| GIPHY current policy/rating/attribution, HTTP disconnect cancellation, preview choice, GIF lifecycle/memory | E02 | E02 implementation and owner tests |
| Dock independent model/view/adapter, settings, overflow, focus/scroll/reorder | E06 | E06 architecture before styling/integration |
| Next Photo canonical controller and failure states | E08 | E08 owner integration |
| Wi-Fi Android/OEM permission, scan throttle/stale and handoff runtime behavior | E01 | P5 target-device validation |
| Global overscan/text scaling and physical TV-distance legibility | B01/B04/QA | P3 browser/visual review then P5 device validation |
| E02/E06 branch divergence and APK budget | Integration/QA | P4 branch reconciliation and exact combined build |

The unresolved gaps remain explicit requirements and blockers where specified. Acceptance does not mean they are implemented or waived.

## 4. P1 exit disposition

- Scope mapping, owners, data boundaries, reuse/build/replace decisions and dependency map: **PASS**.
- Six visual reference approval and agreed functional boundaries: **ACCEPTED**.
- Source/runtime/device parity: **NOT CLAIMED / carried forward**.
- P1 inventory gate: **CLOSED FOR PROGRESSION**.
- P3 prototype review: remains a separate open gate.
- P4 production implementation: remains blocked until P3 visual/interaction acceptance and implementation readiness checks.

No production source, feature branch, PR metadata, integration/main, APK, ADB or device changed by this acceptance record.
