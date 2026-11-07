# Home Dock Visual Parity V2 — 7 November 2026

## Decision and scope

The user resumed the Home Screen only and approved the supplied 1024×576 reference image as the visual master. Preserve the screenshot's Dock position, icon proportions, five-slot horizontal composition, glass tray appearance, wallpaper/hero, ayah/prayer shelf and header. This checkpoint is limited to Home Screen and Dock; do not broaden scope into other modules.

## Source baseline audit

- Patch 4.0.0 source commit: 71e5b26205c068ebd866bd39af503bfd20769498.
- Patch 4.1.0 source commit: cca6a3b450cc13fa16e9287164083a465796f57b.
- Patch 4.0 and 4.1 MainActivity.java are byte-identical (SHA 7a59c8c050c2be84c58588417f61b91b3d75e70b).
- The 4.0 item_app.xml baseline is byte-identical to the current G3A line (SHA 52e6c977119b5de0a112637fe96768d9d3a6a22b). 4.1 adds a separate DockBreathingBorderView; it was not imported because the current G3A Glider already owns focus feedback and the accepted reference must not gain an additional competing focus treatment.
- Legacy tile sizing contract: 180×101dp design tile; item height is tile height +45dp; Dock item offsets 6dp/12dp; tray inset 14dp; horizontal Dock height = item height +18dp; Grid bottom reservation = largest (capacity-4) item height +34dp. Preserve legacy density rounding/truncation semantics.

## Implementation

- Owner branch: fix/home-dock-visual-parity-v2.
- Exact HEAD: 4ba1d7f14ba26e0c4051fcdf958f289234b6e7ae.
- PR #74: OPEN / DRAFT / UNMERGED; base feature/g4-parent-pin-hardening @ 7146de58953325fdfc6c98501828676f143e64cf.
- Added DockGeometry as the shared geometry contract used by MainActivity, DockAdapter and DockDecoration.
- Added regression coverage for 4/5/6 capacities, 180:101 tile ratio, tray bounds, legacy density rounding and Grid/Dock spacing.
- The independent G3A DockAdapter/AppAdapter architecture, Dock orientation/capacity preferences, ordering, focus and remote navigation remain intact.
- Horizontal Dock is anchored to the bottom of the Home content host; Grid viewport remains above the Dock with a capacity-independent reserve. Existing PR #73 fix is retained in this branch lineage.

## Exact-head validation

- CI run #1407 / run ID 42378227691, exact HEAD 4ba1d7f14ba26e0c4051fcdf958f289234b6e7ae: both required jobs SUCCESS.
- app:testReleaseUnitTest, installer:testReleaseUnitTest, release builds, and repository lists/tools/docs checks passed.
- Release APK size: 1,622,573 bytes (1,584 KB).
- Artifact ID: 13576552604; ZIP SHA-256: e07de0449f1ab31d25ce5df631f642a6b9ca8ffed00e3e243da283a2d672e75d.
- Extracted APK: app-release.apk, 1,622,573 bytes; SHA-256 19d1435c7c20491b9093747d65c8dbab6a0627971461bee401702990538a142c.
- Artifact download: https://github.com/Frangga11/glass-tv-home/actions/runs/42378227691/artifacts/13576552604
- Artifact retention expires 14 November 2026.

## Acceptance boundary

SOURCE-HOST QC PASS / READY FOR CONTROLLED DEVICE ACCEPTANCE. Physical TV visual, remote focus, OEM overscan and orientation/capacity acceptance remain PENDING / NOT RUN. No APK install, ADB, device setting mutation, merge, rebase, cherry-pick, force-push or integration-branch change was performed. PR #74 remains Draft and unmerged.

## Device acceptance checklist

1. Horizontal Dock is at the same vertical position and has the same glass tray dimensions/appearance as the approved reference.
2. Five default icons are centered and evenly spaced; verify capacities 4, 5 and 6.
3. Dock card width/height and labels/icons remain aligned; no overlap, clipping or extra app row below the Dock.
4. Focus/glass highlight follows the selected Dock icon without shifting the tray.
5. DPAD left/right traverses Dock; Up enters the nearest Grid tile; Grid Down from its last row returns to Dock.
6. Switch horizontal/vertical Dock and back; Home hero, ayah and prayer schedule do not jump.
7. Return from an external app and cold-start Home; Dock remains parked on the first visible frame.
8. Check 1920×1080 at device density 320 and inspect overscan/safe edges.

