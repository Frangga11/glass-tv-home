# F02 — System Validation Findings Register

- Validation branch: `integration/project-f02-system-validation`
- Baseline reviewed: `89b94bfaa1e2a9ae866fda9243e1997ef002fc41`
- Review type: source-level integration and security review; no physical Android TV testing.
- Status: OPEN — findings require owner review, remediation, and regression evidence.

## F02-UI-001 — Global modal dialog contract is not yet consistently enforced

- Severity: P2 / Major quality and consistency risk.
- Owning area: Project B (global UI/dialog contract), with implementation coordination in MainActivity and ModernSettingsDialog.
- Evidence:
  - `TvDialogStyle.apply` centralizes surface, dim amount, viewport-bounded dimensions, and centering.
  - `MainActivity.styleGlassDialog` is a separate styling helper focused on typography, buttons, and list selectors; it does not itself enforce the shared viewport/dimming/surface sizing contract.
  - MainActivity also applies direct per-dialog `Window.setLayout` sizing in several custom dialogs.
  - `ModernSettingsDialog` search-result AlertDialog is created and shown via a chained `.show()` without an explicit call to `TvDialogStyle.apply` or the MainActivity device-settings wrapper.
  - MainActivity dialog creation paths include a mixture of `TvDialogStyle.apply`, `applyCustom`, `styleDeviceSettingsPopup`, `styleGlassDialog`, and bespoke window sizing.
- Risk:
  - Modal sizing, viewport bounds, dimming, material treatment, and focus behavior can drift between tabs and subdialogs.
  - Some dialogs may not obey the same safe margins and sizing rules on different display sizes/densities.
  - This is not proof of a visible clipping defect on the target TV; that acceptance remains for G.
- Required remediation:
  1. Project B defines one explicit modal presentation API and documents intentional exceptions (e.g. full Settings shell, anchored PopupWindow, custom transparent overlay).
  2. Replace accidental duplicate sizing paths with the shared contract.
  3. Ensure every app-owned modal/subdialog is registered in a dialog inventory and classified as shared, intentional custom, or non-modal.
  4. Add source-level/unit checks or deterministic UI tests that prevent unclassified raw modal creation.
- Exit evidence: dialog inventory complete; every exception justified; regression coverage passes; visual device acceptance deferred to G.

## F02-SEC-001 — Exported configuration broadcast receiver has no sender authorization

- Severity: P2 / Security and state-integrity review required.
- Owning area: Cross-module review between Project C (external communication entry point) and Project D (persistent location state); manifest/security owner must approve the final policy.
- Evidence:
  - `AndroidManifest.xml` declares `.LocationBroadcastReceiver` with `android:exported="true"` and no receiver-level permission.
  - The receiver accepts `com.gathin.tvhome.SET_LOCATION` and `com.gathin.tvhome.SET_WEATHER`.
  - `SET_LOCATION` can persist the selected station/custom location through `AppLocationManager.setStation` / `setCustomLocation`.
  - `SET_WEATHER` can set the in-memory test weather override on the active MainActivity.
- Risk:
  - Another installed application may be able to send these broadcasts and alter launcher location configuration or force a test-weather override without user interaction.
  - The receiver comments document ADB/assistant-script use, so simply disabling external access could break an intentional integration contract.
- Required remediation / decision:
  1. Confirm which external callers are officially supported (ADB shell only, a trusted companion app, or arbitrary assistant integrations).
  2. Choose a least-privilege mechanism compatible with that contract: non-exported receiver plus supported shell workflow, signature-level permission for a trusted companion, or a narrowly scoped authenticated command channel.
  3. Separate test-only weather override from production configuration commands where possible.
  4. Add tests for unauthorized caller rejection and authorized caller compatibility.
- Exit evidence: approved trust boundary, manifest review, caller authorization tests, and regression proof that the supported configuration workflow still works.

## F02-TEST-001 — No Android instrumentation/UI test source set found

- Severity: P2 / Verification coverage gap.
- Owning area: Project F test strategy, with Project B supplying stable UI contracts.
- Evidence:
  - Repository tree contains JVM tests under `launcher/app/src/test/java`.
  - No `launcher/app/src/androidTest` source set or Espresso/UIAutomator instrumentation setup was found in the reviewed integration tree.
  - Existing tests include `TvDialogStyleTest`, `TvResponsiveLayoutTest`, `SettingsFocusPathTest`, and `NavigationStateTest`, but these do not establish end-to-end Android view rendering/focus behavior.
- Risk:
  - JVM tests can verify pure policies and helper calculations but cannot by themselves prove Android window rendering, view hierarchy focus traversal, actual dialog dismissal, or lifecycle interactions.
- Required remediation:
  1. Assess whether deterministic Robolectric tests can cover view/window behavior on the host CI.
  2. Add instrumentation/UI tests only where they can run reliably in CI without a physical Android TV device.
  3. Keep remote-control, actual TV firmware, HDMI, and physical display acceptance cases explicitly deferred to Project G.
- Exit evidence: documented coverage decision, reproducible host-side UI tests where feasible, and a G device acceptance matrix for the remaining cases.

## F02 baseline checks

- CI run `41501458214` completed successfully for both jobs: Lists/tools/docs and Launcher build/unit tests.
- Release compilation and APK size guard passed as validation-only checks.
- APK artifact publication was removed from the Project F workflow. No APK was published for device testing.
- These CI results do not close the findings above.

## Triage order

1. F02-SEC-001 — agree the trust boundary before making changes that could break supported ADB/assistant integrations.
2. F02-UI-001 — consolidate the global dialog contract under Project B and inventory all modal paths.
3. F02-TEST-001 — establish host-side UI coverage and G-only deferred acceptance cases.

No finding is closed until its owning branch/PR contains the fix, regression evidence, and a successful retest on the integrated F branch.


## F02-SEC-002 — Prayer alert activity is externally launchable

- Severity: P2 / Security and user-experience integrity review required.
- Owning area: Project C/D coordination with Android manifest/security owner.
- Evidence:
  - `AndroidManifest.xml` declares `.AzanActivity` with `android:exported="true"` and no intent filter or component-level permission.
  - The activity reads an incoming `prayer` extra, displays a full-screen/foreground prayer alert card, sets `FLAG_KEEP_SCREEN_ON`, and is configured to show while locked / turn the screen on.
  - The documented internal alarm flow already has an app-owned `AzanReceiver`; the intended external caller contract for directly starting `AzanActivity` is not documented in the manifest.
- Risk:
  - An unrelated installed application may be able to launch the alert activity on demand, interrupting the current screen and keeping the display awake.
- Required remediation / decision:
  1. Confirm whether any legitimate external caller needs to start this activity directly.
  2. If it is internal-only, set `android:exported="false"` and verify the app-owned alarm PendingIntent still launches it.
  3. If external invocation is required, document and enforce a narrowly scoped authorization contract.
  4. Add a manifest/component security regression check.
- Exit evidence: approved caller boundary, manifest verification, and an automated test/check that rejects unintended external launches.


## Remediation update — 3 November 2026

The following fixes have been implemented on owning source branches and mirrored into the F02 validation branch. They remain **OPEN / RETEST PENDING** until exact-head CI and integrated regression evidence are available.

### F02-UI-001 — Shared Settings search modal contract
- Root cause confirmed by ancestry comparison: the D03/D04 versions applied `TvDialogStyle.apply` to both Settings search dialogs; the D05 revision changed the result dialog to a builder-chained `.show()` and removed the shared style call from the search-input dialog.
- Owner correction: D05 source branch `feature/d05-standby-photo-atomic-persistence`, follow-up head `8b08076d11569ca91557d25e9d5e58965f2f2de0`, tracked in Draft PR #29.
- Restored `create → show → TvDialogStyle.apply` for results and applied the same shared contract to the search-input dialog. Search behavior, tab routing, focus restoration and state handling are unchanged.
- Added `tests/test_dialog_modal_contract.py`; mirrored to F02 integration.
- D06 dependent branch synchronized at `6ed01d737ff23ce9721dcf92bf48af99794eb3e7`.
- Status: FIX COMMITTED / CI AND INTEGRATED RETEST PENDING.

### F02-SEC-001 — Location broadcast sender authorization
- Owner follow-up branch: `fix/d01-location-broadcast-authorization`, head `bbb13dd107c86cda85bd1482c2f050ca69dfc4e2`, Draft PR #51 targeting the D01 branch.
- Added `android:permission="android.permission.DUMP"` to the exported `LocationBroadcastReceiver`. Both existing actions remain present; receiver logic and persistent state handling are unchanged.
- This preserves the documented ADB shell/system caller route while denying ordinary third-party app UIDs. Any direct third-party assistant integration is outside this trust contract and must use an authenticated, separately reviewed bridge.
- Added `tests/test_location_broadcast_manifest_security.py` and documented the boundary in `D01_LOCATION_BROADCAST_SECURITY_FOLLOWUP.md`.
- Actual ADB-shell compatibility on target TV firmware: DEFERRED_TO_G.
- Status: FIX COMMITTED / CI AND DEVICE-COMPATIBILITY RETEST PENDING.

### F02-SEC-002 — Prayer alert activity export
- Owner branch: `feature/d06-process-death-recovery`, head `6ed01d737ff23ce9721dcf92bf48af99794eb3e7`, Draft PR #30.
- Changed `.AzanActivity` to `android:exported="false"`. The app-owned explicit intent path from `AzanReceiver` and full-screen notification remains unchanged.
- Added `tests/test_prayer_component_manifest_security.py` covering AzanActivity, AzanReceiver and AzanBootReceiver manifest boundaries.
- No prayer alarm scheduling, wake-lock, timing, or recovery policy was changed.
- Status: FIX COMMITTED / CI AND INTEGRATED RETEST PENDING.

### Integrated validation rule
- All three regression tests are mirrored into this F02 branch and must pass on its exact final head.
- No finding closes on source commit alone. Require source-branch CI, F02 integrated CI, diff review, and explicit G deferrals where host CI cannot prove device behavior.
- Project F still does not install, launch, or test on Android TV and does not publish APK artifacts.


### Cross-branch dependency QC — 3 November 2026
- The first D06 CI retest after restoring the modal calls failed at Java compilation because the D05/D06 source branches were based on a D04 checkpoint that predates the B01-owned `TvDialogStyle` class. The compiler reported `cannot find symbol TvDialogStyle` at the two restored calls.
- This was a real dependency-sync defect, not a reason to remove the calls or weaken the test.
- Corrective action: explicitly synchronized the B01-owned `TvDialogStyle.java` and its geometry unit test into both D05 and D06 source branches. The pre-existing `bg_glass_dialog` drawable was verified present. The integration branch already contained the current helper.
- D05 source retest head: `39f2f208fe0ea1c1973c299af9e9661913c0089f` — CI in progress at last check.
- D06 source retest head: `caa0fd85f19aea5e6bdacb84068c69681acfa984` — CI queued at last check.
- F02 integration exact head `27ecd5e4e5e283f5d1675cb28a75b2f1cbb0a6c7`: both required jobs passed, including all three new regression guards. This does not replace the owner-branch exact-head gates.
