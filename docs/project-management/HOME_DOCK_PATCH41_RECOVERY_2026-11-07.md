# Home Dock — Patch 4.1 overflow/navigation recovery

Date: 7 November 2026
Scope: PR #74, horizontal Home Dock, overflow-app reachability.

## Trigger
The user's physical Mi TV photo rejected the previous PR #74 build: Dock composition remained incorrect and installed apps outside the five Dock slots appeared to be missing.

## Source verification
- Patch 4.1 baseline: cca6a3b450cc13fa16e9287164083a465796f57b.
- PR #74 before recovery: 348958f975418adb8a0d223931cd1e8617f4877e.
- Patch 4.1 uses one AppAdapter inventory. Its first row is the Dock; overflow apps remain in that inventory and are reached by DPAD Down. The original parkDock() uses measured tile/item height, rows_below_dock, and a 16dp bottom safe gap.
- PR #74's split DockAdapter/Grid architecture had hidden the Grid in horizontal mode. Its horizontal onDockTileKey(DPAD_DOWN) also consumed Down without opening the Grid. These two conditions jointly made every non-Dock app unreachable. This is a confirmed functional defect.
- The earlier root-width change corrected horizontal Dock holder sizing but did not restore overflow-app reachability.

## Recovery changes
Branch: fix/home-dock-visual-parity-v2
Latest source commit: 48f07a983ebee4370bbf13a80f85edd947c2eb31

- Horizontal Grid is collapsed at rest, preserving the Dock-only idle Home.
- DPAD Down from Dock reveals the existing overflow Grid and focuses the app in the aligned column.
- DPAD Up from the first overflow row returns to the corresponding Dock slot.
- DPAD Up from Dock returns to the top shelf.
- Returning focus to a Dock tile collapses the overflow Grid again.
- Focus restoration now follows stable app identity when the previous app was in horizontal overflow.
- Vertical Dock mode remains visible and uses its existing side-by-side Grid.
- Added a unit test for horizontal Dock/Grid column mapping.
- Existing inventory split contract keeps pinned overflow apps in Grid; no app should be discarded by capacity.

## Validation
- Latest CI run: 42382202545, head 48f07a983ebee4370bbf13a80f85edd947c2eb31; status was IN PROGRESS at documentation time. No PASS claim yet.
- Prior CI success on 348958f975418adb8a0d223931cd1e8617f4877e does not validate these navigation changes.
- Physical Mi TV Stick visual/remote acceptance remains NOT RUN. No TV install, ADB, device setting mutation, merge or PR promotion.
- PR #74 remains OPEN / DRAFT / UNMERGED.

## Remaining acceptance gates
1. Exact-head CI and unit tests PASS.
2. TV idle state: five Dock icons are distinct, aligned, and in the bottom tray; no overflow app icons clutter the middle.
3. DPAD Down reveals all remaining installed apps; horizontal navigation works; DPAD Up returns to Dock and then top shelf.
4. Confirm 4/5/6 capacity, pin/unpin, app install/update/remove refresh, resume focus restoration, and vertical Dock mode.
5. Compare against Patch 4.1 reference and only then request visual acceptance.

## Validation update — exact-head CI PASS

- Exact latest source head 48f07a983ebee4370bbf13a80f85edd947c2eb31 completed CI run 42382202545 with SUCCESS; both required jobs passed, including launcher release build, unit tests, repository checks and APK publication.
- Release APK: 1,624,937 bytes. The workflow emitted a non-blocking historical 1.5 MiB diagnostic warning; this is +204 bytes versus the prior 1,624,733-byte build.
- Test artifact #13578301246; ZIP size 1,424,751 bytes; SHA-256 5c9ea538f64743a0b31c91b14add41b47f091c3d57110d2f0bce83c86e537922; expires 14 Nov 2026.
- Physical TV visual/remote acceptance remains NOT RUN. CI PASS is not visual/device PASS.
