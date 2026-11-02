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
