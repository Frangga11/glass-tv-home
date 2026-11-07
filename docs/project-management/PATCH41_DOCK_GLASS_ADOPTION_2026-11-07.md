# Patch 4.1 Dock & Liquid Glass Adoption — 7 November 2026

## Device-test finding and root cause

The user's TV photo showed two app rows but no visible bottom Dock tray or Dock focus treatment. The previous V2 device result was rejected.

Patch 4.1 uses a first-row Dock contract: configured favourites lead the ordered app inventory, and remaining visible Dock slots are filled from that same inventory up to the configured column count. V2's separate Dock adapter used only configured/user-pinned component keys; with a short or empty pin list the Dock could collapse to zero/too few items, leaving all applications in the Grid. This matches the reported symptom.

## Patch 4.1 adoption

- Restored first-row Dock fill semantics using DockLayoutModel.fillDockSlots: preferred pins first, then remaining ordered inventory; explicit unpins remain excluded; overflow remains in Grid.
- Adopted exact Patch 4.1 GlassRim, including thin-film cyan/violet/gold iridescent gradient and Window Light.
- Adopted exact Patch 4.1 GlassView, including hardware-layer focus rim and Window Light.
- Adopted DockBreathingBorderView and inserted it into item_app.xml; breathing rim is wired to Dock holders only.
- Restored Window Light reflection in DockDecoration, GlassDrawable and Glider.
- Restored Patch 4.1 bottom safe offset: horizontal Dock host bottomMargin = 16dp; Grid reserve already includes the same 16dp so regions align. This moves the Dock host up 16dp from the previous V2 implementation and matches Patch 4.1's bottom placement.
- Preserved independent G3A DockAdapter/AppAdapter, stable identity, Dock orientation and capacity 4/5/6.

## Source and PR

- Owner branch: fix/home-dock-visual-parity-v2.
- Exact HEAD: 2d1f744a4b77406712e7868a7e7c6747a3bb88ed.
- PR #74: OPEN / DRAFT / UNMERGED; base feature/g4-parent-pin-hardening @ 7146de58953325fdfc6c98501828676f143e64cf.

## Exact-head validation

- CI #1416 / run ID 42379710925, exact HEAD 2d1f744a4b77406712e7868a7e7c6747a3bb88ed: both required jobs SUCCESS.
- Launcher app and installer release unit tests/builds passed; repository lists/tools/docs checks passed.
- Release APK: 1,624,733 bytes.
- APK SHA-256: 5da75cfd30956224bb9bf7c61ced34a11068356d83227cdb28d2fd9fb6d00684.
- Artifact ID: 13577033231; ZIP SHA-256: 32f4f27cc73cc734e9d029741edf4b5443bdc401d31f290ea2fb193d710c8f9e.
- Artifact download: https://github.com/Frangga11/glass-tv-home/actions/runs/42379710925/artifacts/13577033231
- Artifact expires 14 November 2026.

## Acceptance boundary

SOURCE-HOST QC PASS / READY FOR CONTROLLED TV ACCEPTANCE. The previous device test was FAIL; this new build has not yet been tested physically. Do not claim visual PASS until a new TV photo confirms Dock placement, tray glass, breathing rim and remote behavior. No APK install, ADB, device setting mutation, merge, rebase, cherry-pick or force-push was performed.

## Next device checks

1. Confirm Dock fills five visible slots even when configured favourites are fewer than five.
2. Confirm the horizontal Dock host is bottom-anchored with the Patch 4.1 16dp bottom safe offset.
3. Confirm the App Grid occupies only the region above the Dock, with no duplicate row below it.
4. Confirm blurred wallpaper tray, veil, iridescent rim, top Window Light and drop shadow are visible.
5. Focus Dock icons: breathing cyan rim appears only on Dock cards, not Grid cards.
6. Verify capacities 4/5/6, horizontal/vertical orientation and DPAD up/down transitions.
7. Confirm cold-start and return-from-app retain the same Dock placement.
