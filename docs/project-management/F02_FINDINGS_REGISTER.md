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


### MainActivity modal inventory and enforcement — 3 November 2026
- Cross-file inventory covered `MainActivity`, `ModernSettingsDialog`, `UserSwitchDialog`, and `WeatherActivity`.
- Found that MainActivity's `styleGlassDialog` applied typography/list styling only. Several call sites relied on local/manual window sizing and therefore did not necessarily receive the B01 shared surface, dim and viewport-bound contract.
- B01 owner fix: `styleGlassDialog(dialog)` now delegates to `styleGlassDialog(dialog, 520, 0)`; the sized overload applies `TvDialogStyle.apply` with a guarded fallback before the existing typography/list styling.
- Integrated MainActivity retains explicit custom geometry after the shared helper. Custom 16:9 Azan sizing, weather/prayer summary sizing and wallpaper selector sizing are preserved; they are not replaced with a generic width/height.
- Device Settings wrapper now delegates to the sized overload, avoiding a second independent style implementation.
- Mirrored into D05/D06 dependent source branches and F02 integration. Added `tests/test_main_activity_modal_contract.py` to guard default delegation and sized application.
- Intentional non-AlertDialog exceptions identified: anchored `PopupWindow` menus (their own anchored-glass contract), the custom module-card prompt `Dialog` (custom transparent surface/focus pair), and profile-switch custom transparent surfaces using `TvDialogStyle.applyCustom`. These must remain documented exceptions, not be mechanically restyled.
- `WeatherActivity` dialogs use `TvDialogStyle.apply`; `ModernSettingsDialog` search/result, profile and custom picker paths use the shared helper or the MainActivity Device Settings wrapper; `UserSwitchDialog` uses shared/custom helper paths.
- B01 exact-head CI: running on `b1c23024d0de740c36e1794ab775fdc43c7a3a20`.
- D05 exact-head CI: running on `7f043943ab0c1d2d1217fc56845d81ac7583c96d`.
- D06 exact-head CI: running on `1bb6ce9bf04d72f3fe33b4e1630af561e99fe0f7`.
- F02 integration exact head: `b7ff46edd7fa628c541df0b5a8f51d6613c9e8af`; both CI jobs running.
- Status for F02-UI-001: FIX COMMITTED / EXACT-HEAD OWNER + INTEGRATION CI PENDING. No closure claimed.


### F02-TEST-001 — Host-side UI verification improvement — 3 November 2026
- Added Robolectric 4.14.1 as a `testImplementation` dependency only and enabled Android resources for local JVM tests; no runtime/APK dependency is introduced.
- Added `TvDialogStyleRobolectricTest.java` to instantiate real AlertDialog windows under API 30 Robolectric and assert shared modal centering, dim-behind flag/amount, viewport-bounded width, wrap-content height, custom height/width bounds, and dim clamping.
- This complements the source-level modal contract guards and existing pure JVM geometry tests. It does not claim actual TV rendering, remote focus behavior, OEM window policy, HDMI/display scaling, or firmware compatibility.
- Exact-head CI for the Robolectric dependency/test is pending. If dependency resolution or tests fail, fix the cause or remove only the experimental test with documented rationale; never skip or weaken it to obtain green CI.
- Status: PARTIAL / ROBOLECTRIC EXACT-HEAD CI PENDING / DEVICE ACCEPTANCE DEFERRED_TO_G.


## F02-FUNC-001 — Home double-press Recent Apps routing — 3 November 2026

- Severity: P2 functional regression. Owner: B01 global navigation / HomeKeyService.
- Reproduction from source contract: HomeKeyService intercepts both `KEYCODE_HOME` and `KEYCODE_ALL_APPS`, consumes each key event, and previously executed the single-Home action immediately on every ACTION_UP. It therefore never routed a double-press to RecentAppsActivity; while Recent Apps was foreground, the first Home release dismissed it before its own double-press-to-Close-All handler could receive the second key.
- This contradicted the product guide/RecentAppsActivity contract: double Home outside Recent Apps opens the deck; double Home inside it runs Close All. The dedicated `KEYCODE_APP_SWITCH` remains disabled per the existing request and is not repurposed.
- B01 correction: added Android-free `HomeKeyPressTracker` with a 350 ms double-press window and 400 ms single-action delay. HomeKeyService now resolves:
  - single Home outside Recent Apps → return to Home after the double-press window;
  - double Home outside Recent Apps → explicitly launch RecentAppsActivity;
  - single Home inside Recent Apps → dismiss to Home after the window;
  - double Home inside Recent Apps → invoke `triggerCloseAllFromGlobal()`;
  - long Home → preserve the existing 450 ms Settings-popup behavior and cancel pending single/double state.
- Cancellation, long-press and service lifecycle cleanup are included. The app-switch key remains consumed as before.
- Added `HomeKeyPressTrackerTest.java` for first/single/double/late/cancel timing boundaries and `tests/test_home_key_recent_apps_contract.py` for service routing and policy guard.
- Source branch: `feature/b01-global-navigation-retention`; PR #10. F02 integration mirrors the change.
- Exact-head CI and physical remote acceptance: PENDING. Timing and OEM key-event delivery remain DEFERRED_TO_G.
- Status: FIX COMMITTED / OWNER + F02 CI PENDING / DEVICE REMOTE ACCEPTANCE DEFERRED_TO_G.


## Additional manifest and interaction audit — 3 November 2026

### F02-SEC-003 — LocationActivity exported without an external contract
- Severity: P3. Owner: D01 location module.
- Manifest audit found `.LocationActivity` exported although it has no intent filter and is reached by explicit MainActivity intents only.
- Changed to `android:exported="false"` on D01 follow-up PR #51 and mirrored into F02. The internal explicit-intent route remains valid.
- Extended `tests/test_location_broadcast_manifest_security.py` to assert non-exported state, no intent filter, and an in-app MainActivity reference.
- D01 latest head `66352838e6155a42a9ea61477643a238f360434f`: CI run #41506847521 SUCCESS (both jobs).
- Status: SOURCE FIX / CI PASS / F02 RETEST PENDING.

### F02-SEC-004 — Exported RecentAppsActivity exposes destructive Close All action
- Severity: P2. Owner: B01 Recent Apps / manifest boundary.
- Confirmed defect: `.RecentAppsActivity` was exported and handled the explicit action `com.gathin.tvhome.ACTION_CLOSE_ALL` in `onCreate()` by posting `onCloseAllClicked()`. That path calls `RecentAppsManager.closeAll(...)` and can force-stop the apps represented in the recent-app list. Any installed app able to construct an explicit component Intent could launch this destructive action without an app-level permission.
- No external caller contract was found. HomeKeyService opens RecentAppsActivity through an explicit in-app Intent; Close All is invoked through its in-app UI/global service route.
- Correction: set `android:exported="false"` and remove the unnecessary MAIN intent filter. Preserve the activity, its internal explicit launch, and its Close All UI behavior.
- Added `tests/test_recent_apps_manifest_security.py` to assert non-exported/no-filter manifest state and continued explicit in-app HomeKeyService reachability.
- Owner branch: B01 `feature/b01-global-navigation-retention`, PR #10; mirrored to F02 integration.
- Exact-head owner/integration CI pending. Project G must confirm the actual double-Home/Close All interaction still works through the internal route.
- Status: FIX COMMITTED / EXACT-HEAD CI PENDING / DEVICE ACCEPTANCE DEFERRED_TO_G.

### F02-FUNC-001 — Home double-press Recent Apps routing
- B01 now implements a 350 ms double-press classifier and 400 ms single-action delay. Single Home returns/dismisses; double Home opens RecentAppsActivity or triggers Close All when already foreground; long Home still opens Settings at 450 ms. Settings-key input cancels any pending Home action to prevent delayed Home from overriding Settings.
- `RecentAppsActivity` fallback uses the same `HomeKeyPressTracker.DOUBLE_PRESS_WINDOW_MS` threshold. The dedicated App Switch key remains disabled per existing policy.
- B01 branch `feature/b01-global-navigation-retention`, PR #10; F02 mirrors the change. Added pure JVM tracker tests and source contract tests.
- F02 previous head `5f6b0c60949c1284e0e904e614e85c2e0ca657f2` passed both CI jobs before the latest timing/Settings-key refinements. Latest F02 head `8dde2c1ba84050abae6b99e0eb30661ca59f4c93` is queued.
- B01 latest head `867c7a9d0977e21b69c8078a400458b00ba2d2dd` is in CI.
- Status: FIX COMMITTED / LATEST OWNER + F02 CI PENDING / REMOTE ACCEPTANCE DEFERRED_TO_G.

### Latest CI snapshot
- B01 #10 latest head `867c7a9d0977e21b69c8078a400458b00ba2d2dd`: run #41507890630 in progress.
- D05 #29 head `7f043943ab0c1d2d1217fc56845d81ac7583c96d`: run #41505852041 SUCCESS.
- D06 #30 head `1bb6ce9bf04d72f3fe33b4e1630af561e99fe0f7`: run #41505865772 SUCCESS.
- D01 #51 latest head `66352838e6155a42a9ea61477643a238f360434f`: run #41506847521 SUCCESS.
- F02 previous head `5f6b0c60949c1284e0e904e614e85c2e0ca657f2`: run #41507621908 SUCCESS; latest head `8dde2c1ba84050abae6b99e0eb30661ca59f4c93`: queued.
- Robolectric API 30 window tests passed on the previous F02 head; latest head retest remains mandatory.


## F02-FUNC-002 — Force Stop queue can cancel unrelated global gesture callbacks — 3 November 2026

- Severity: P2 cross-feature reliability defect. Owner: B01 / HomeKeyService shared event scheduling.
- Root cause: `HomeKeyService` used one main-thread `homeHandler` for Home single/double/long-press gestures, power-action timeout, and Force Stop queue orchestration. Three Force Stop paths called `homeHandler.removeCallbacksAndMessages(null)`, which clears every callback posted to that Handler—not only Force Stop work. A Force Stop operation could silently cancel a pending Home single/double gesture, the 450 ms long-press Settings popup, or the power-action timeout.
- Correction: introduced a dedicated `forceStopHandler`. All Force Stop timeout and queue-continuation callbacks now use it; its broad callback cleanup is scoped to Force Stop only. The Home gesture and power-action callbacks remain on `homeHandler`. Both handlers are explicitly cleaned up in `onDestroy()`.
- Added `tests/test_home_key_force_stop_handler_isolation.py` to guard the separate Handler, prohibit broad clearing of `homeHandler`, verify Force Stop scheduling uses its own Handler, and verify lifecycle cleanup.
- Owner branch: B01 `feature/b01-global-navigation-retention`, PR #10. F02 integration mirrors the fix.
- B01 exact-head CI run #41508765896 and F02 exact-head CI runs #41508777206/#41508763391 were in progress at checkpoint; previous F02 head `c9496d4e051980e640bc269ffce695b95ba71ad4` passed both jobs before this new fix.
- Status: FIX COMMITTED / EXACT-HEAD OWNER + INTEGRATION CI PENDING / TV ACCESSIBILITY EVENT ACCEPTANCE DEFERRED_TO_G.


### Additional security validation
- The Recent Apps exported entry was not merely a public UI surface: its exported `ACTION_CLOSE_ALL` branch was a remotely triggerable destructive app-stop operation. The manifest boundary was hardened immediately, with the explicit in-app HomeKeyService route retained and a regression test added.


## F02-PLAT-001 — Invalid AccessibilityService action ID used for restart — 3 November 2026

- Severity: P2 platform-action defect. Owner: B01 / HomeKeyService power-action entry.
- Cross-branch audit found the B01 source branch still called `performGlobalAction(12)` when requesting Android TV restart, with a comment claiming ID 12 was reboot. In the F02 integration source, this call had already been corrected: ID 12 is the accessibility-button chooser, not a reboot action. Keeping the source branch stale would reintroduce the invalid action when branches are reconciled.
- Correction in B01 owner branch: removed the invalid global-action call and consistently use the existing visible system power-menu flow via `openPowerDialogAndSelect(POWER_ACTION_RESTART)`. This preserves the existing power menu selection/fallback design rather than inventing a new reboot mechanism.
- Added `tests/test_home_key_power_action_contract.py` to assert restart never calls action ID 12 and the 12-second power-menu timeout remains registered/cleared through the power-action handler.
- F02 integration already had the corrected source implementation; mirrored the regression test so the integration gate protects it.
- Owner B01 PR #10 latest retest pending; F02 latest retest pending.
- Physical vendor power-menu labels and restart acceptance remain DEFERRED_TO_G.
- Status: OWNER SOURCE FIX + REGRESSION TEST COMMITTED / EXACT-HEAD CI PENDING / TV POWER-MENU ACCEPTANCE DEFERRED_TO_G.


### Exact-head retest results — 3 November 2026
- F02 integration PR #50 head `5dd787ba136dfb070eaa2e833af653e57ec66015`: Actions run #41509632784 (Launcher build/unit tests) SUCCESS and run #41509626957 (Lists/tools/docs) SUCCESS.
- The F02 exact-head run includes the new Force Stop/Home Handler isolation test, RecentAppsActivity internal-only manifest test, Home-key timing/routing tests, power-action contract test, manifest security tests, and Robolectric window tests.
- B01 owner PR #10 head `791620ce4628ee9e909af7ce2f4758b6df77afeb`: run #41509576279 SUCCESS on both jobs, including the Force Stop/Home isolation and restart action-ID tests. The owner branch then received a comment-only alignment commit `389e289e939276ce9e20d7e6c82c293d3f83c70f`; exact-head CI run #41509724289 was still in progress.
- B01 owner source and F02 integrated `HomeKeyService.java` now have identical file content/blob SHA `b3cd4b2b4092a90f4f1faefb1072c15e2203b314`.
- F02-SEC-004, F02-FUNC-002 and F02-PLAT-001 integrated retests: PASS at the exact F02 head above. B01 latest comment-only exact-head CI pending; real remote/power-menu behavior remains DEFERRED_TO_G.


## F02-C07-001 — Wallpaper management API mutation can race QR-session shutdown — 3 November 2026

- Severity: P2 integrity / session-revocation race. Owner: C07 upload/session resilience.
- Root cause: WallpaperUploadServer serialized final wallpaper/StandBy upload commits with synchronized `stop()`, but the separate management POST handlers for `/api/select`, `/api/delete`, `/api/slideshow`, and `/api/clear` only relied on the outer request-dispatch session check. A worker that passed that check could continue selecting/deleting/clearing after the QR session was revoked.
- Correction: C07 now makes all four mutation handlers synchronized on the same server monitor as `stop()` and rechecks session validity inside the critical section before any listener callback or file/state mutation. Shutdown that acquires the monitor first returns HTTP 410 Gone; a mutation that acquires it first completes before stop returns. This closes the race without changing API routes, payloads, upload limits, browser progress/cancel UX, or StandBy persistence.
- Added `tests/test_wallpaper_session_shutdown_atomicity.py` to guard shared monitor use, in-lock session rechecks, explicit 410 JSON response, and preservation of upload commit serialization.
- Owner branch: `feature/c07-upload-session-resilience`, PR #24. F02 integration mirrors the fix/test.
- Owner exact-head CI and latest F02 exact-head CI pending.
- Physical phone-to-TV cancellation/expiry and network disconnect acceptance remains DEFERRED_TO_G.
- Status: FIX COMMITTED / OWNER + F02 EXACT-HEAD CI PENDING / DEVICE ACCEPTANCE DEFERRED_TO_G.


## F02-C07-002 — Upload session deadlines depended on wall-clock time — 3 November 2026

- Severity: P3 timing correctness / resilience. Owner: C07 upload/session layer.
- Root cause: Avatar/Wallpaper QR session expiry, per-request deadlines, and per-read deadline checks used `System.currentTimeMillis()`. Android time synchronization or a manual clock adjustment can move this clock forward/backward, prematurely expiring or unintentionally extending an upload session.
- Correction: switched all Avatar/Wallpaper expiry and request-deadline calculations/checks to `SystemClock.elapsedRealtime()`, including `UploadDeadlineInputStream`. The monotonic clock includes deep sleep and is unaffected by wall-clock/time-zone changes. Timeout durations, QR session lifetime, browser XHR timeout, and HTTP behavior remain unchanged.
- Added `tests/test_upload_monotonic_deadline_clock.py` to require elapsed-realtime usage and prohibit wall-clock deadline comparisons in all three classes.
- Owner: C07 PR #24, `feature/c07-upload-session-resilience`; mirrored into F02.
- Exact-head owner and F02 CI pending.
- Status: FIX COMMITTED / EXACT-HEAD CI PENDING / PHONE-TO-TV EXPIRY ACCEPTANCE DEFERRED_TO_G.


### C07 monotonic-clock retest correction — 3 November 2026
- First CI after production clock conversion correctly failed two existing `UploadDeadlineInputStreamTest` cases because the test fixtures still created deadlines using `System.currentTimeMillis()` while production now compares `SystemClock.elapsedRealtime()`.
- Corrected the fixtures to use the same monotonic clock domain; no test was removed or weakened.
- Added source guard `tests/test_upload_monotonic_deadline_clock.py` to prohibit wall-clock deadline comparisons in AvatarUploadServer, WallpaperUploadServer and UploadDeadlineInputStream.
- Latest owner and F02 exact-head retests pending after this test correction.


## C07/F02 upload deadline retest and dependency-chain reconciliation — 3 November 2026

### F02-C07-002 — monotonic upload deadline verification
- The first CI after introducing the injectable `SocketTimeoutController` still failed one assertion in `UploadDeadlineInputStreamTest.appliesRemainingDeadlineToEachRead`.
- Exact failure: the test expected 3000 ms at fake monotonic time 2500 ms with an absolute deadline of 6000 ms. The production policy correctly computes 3500 ms (`6000 - 2500`); this was a test arithmetic error, not a production defect.
- Corrected the assertion to 3500 ms on C07 owner and F02 integration branches. No production behavior changed; no test was skipped, removed, or weakened.
- C07 owner PR #24 head `80a92264c51631fb107c75f32a3cd8e4b8aa9d9a`: CI run #41512386827 SUCCESS (Launcher build/unit tests and Lists/tools/docs).
- F02 PR #50 head `ddf130a348182546b28ff59c292c9b5ceebacb2e`: push CI run #41512382568 SUCCESS (both required jobs). PR CI run #41512391849: both substantive job steps passed, including release build, APK diagnostics and size budget; final job cleanup was still in progress at this checkpoint.
- The injectable timeout seam remains: production delegates to `Socket.setSoTimeout`; JVM tests use a fake controller and monotonic clock.
- Status: SOURCE FIX / OWNER CI PASS / F02 PUSH CI PASS / PHYSICAL NETWORK ACCEPTANCE DEFERRED_TO_G.

### C07 ↔ D05 dependency-chain reconciliation — intentional source divergence
- C07 PR #24 is based on C06 and owns upload-session resilience. D05 PR #29 is a downstream dependency and owns atomic StandBy photo persistence.
- D05 changes `StandbyPhotoStore.addUploadedFile` from a boolean result to `UploadResult` and adapts the StandBy upload response path in `WallpaperUploadServer`, preserving files on persistence failure so live URI references do not dangle.
- F02 integration contains that D05 `UploadResult` adapter while also retaining the later C07 monotonic deadlines and synchronized QR-session mutation/shutdown protections. The C07↔F02 file difference is therefore an expected downstream contract adaptation, not a missing C07 fix.
- Do not copy the older C07 boolean-based StandBy block over the F02/D05 implementation. Preserve the dependency order C07 → D01–D05 and the F02 combined behavior.
- D05 PR #29 exact head `7f043943ab0c1d2d1217fc56845d81ac7583c96d`: CI run #41505852041 SUCCESS. F02 exact-head validation is recorded above.

### Other owner-branch exact-head checks — 3 November 2026
- B01 PR #10 head `389e289e939276ce9e20d7e6c82c293d3f83c70f`: run #41509724289 SUCCESS.
- D06 PR #30 head `1bb6ce9bf04d72f3fe33b4e1630af561e99fe0f7`: run #41505865772 SUCCESS.
- D01 follow-up PR #51 head `66352838e6155a42a9ea61477643a238f360434f`: run #41506847521 SUCCESS.
- Host CI results do not replace Project G acceptance of real Android TV rendering, DPAD/remote timing, OEM power menu, ADB permission behavior, phone-to-TV LAN disconnect/cancel/retry/expiry, or firmware behavior.


## Authoritative finding status reconciliation — 3 November 2026

This table supersedes earlier per-section statements marked “CI pending” where newer exact-head evidence below exists. It does not waive any Project G acceptance.

| Finding | Host/source remediation evidence | Current disposition |
|---|---|---|
| F02-UI-001 Global modal contract | B01 shared dialog helper/MainActivity enforcement; D05 Settings search dialog calls restored; modal contract source tests and Robolectric API 30 window tests pass on F02 source/test head `ddf130a`. B01 #10 run #41509724289 and D05 #29 run #41505852041 passed. | HOST CONTRACT PASS / G VISUAL + DPAD ACCEPTANCE DEFERRED |
| F02-SEC-001 Location broadcast authorization | D01 #51 applies `android.permission.DUMP`; manifest regression test and owner CI #41506847521 pass; F02 source/test CI #41512382568 pass. | MANIFEST/HOST PASS / G ADB-SHELL COMPATIBILITY DEFERRED |
| F02-SEC-002 AzanActivity external launch | D06 #30 sets internal-only component; manifest security regression and owner CI #41505865772 pass; F02 source/test CI pass. | MANIFEST/HOST PASS / G INTERNAL ALARM ACCEPTANCE DEFERRED |
| F02-TEST-001 Host UI coverage | Robolectric 4.14.1 API 30 AlertDialog window tests run in the launcher JVM test suite; F02 release unit-test/build job passed. | HOST UI WINDOW TEST PASS / PHYSICAL RENDERING + FOCUS DEFERRED |
| F02-FUNC-001 Home double-press routing | B01 #10 timing/routing regression tests and source guard pass in owner CI #41509724289 and F02 source/test CI. | HOST ROUTING PASS / G REMOTE TIMING DEFERRED |
| F02-SEC-003 LocationActivity export | D01 #51 sets non-exported; manifest guard and owner CI #41506847521 pass; F02 source/test CI pass. | MANIFEST/HOST PASS |
| F02-SEC-004 RecentAppsActivity exported Close All | Non-exported manifest and internal route guard pass in B01 owner CI #41509724289 and F02 source/test CI. | MANIFEST/HOST PASS / G REAL REMOTE CLOSE-ALL DEFERRED |
| F02-FUNC-002 Force Stop/Home Handler collision | Dedicated Force Stop Handler isolation and lifecycle cleanup regression guard pass in B01 owner CI and F02 source/test CI. | HOST REGRESSION PASS / G ACCESSIBILITY EVENT ACCEPTANCE DEFERRED |
| F02-PLAT-001 Invalid restart action ID | B01 uses visible system power-menu path, removes invalid global action ID 12; owner CI #41509724289 and F02 source/test CI pass. | SOURCE/HOST PASS / G OEM POWER-MENU ACCEPTANCE DEFERRED |
| F02-C07-001 QR mutation versus session shutdown | Synchronized select/delete/slideshow/clear handlers share stop monitor and recheck active session; C07 owner CI #41512386827 and F02 source/test CI pass. | SOURCE/HOST PASS / G PHONE-TO-TV REVOCATION ACCEPTANCE DEFERRED |
| F02-C07-002 Monotonic upload deadline | Injectable timeout controller, monotonic clock and corrected 3500 ms test expectation; C07 owner CI #41512386827 and F02 source/test CI pass. | SOURCE/HOST PASS / G NETWORK/DEVICE ACCEPTANCE DEFERRED |

### Current integrated evidence and exit condition
- F02 source/test head `ddf130a348182546b28ff59c292c9b5ceebacb2e`: exact-head push CI #41512382568 passed both required jobs. Matching PR run #41512391849 passed all substantive test/build/size-gate steps; cleanup was in progress at last poll.
- The current F02 head after this status-register documentation update must be revalidated on exact SHA. Documentation-only changes do not inherit the prior green result.
- No physical Android TV behavior is marked PASS. PR #50 remains Draft/open/unmerged; no APK artifact is published or installed.
- F02 remains PARTIAL until latest exact-head CI, remaining risk-based source/contract audits, and G handoff evidence are complete.


## F02-SEC-005 — InputKeyService cannot be system-bound while non-exported — 3 November 2026

- Severity: P2 functional / platform integration defect. Owner: Project E01 (Device Settings and remote Input action), with F manifest regression coverage.
- Evidence: `.InputKeyService` extends `AccessibilityService`, declares the `android.accessibilityservice.AccessibilityService` intent action, and is enabled only through the opt-in installer flag `INPUT_KEY_SERVICE=1` / Windows `-InputKeyService`. Its manifest had `android:exported="false"` while `HomeKeyService` correctly used `exported="true"`.
- Android's service manifest contract states that a non-exported service can be started/bound only by the same app or same user ID; an AccessibilityService must be discoverable and bindable by the Android system. Keep `android.permission.BIND_ACCESSIBILITY_SERVICE` as the system-only binding restriction.
- Owner correction: E01 PR #34 changes only InputKeyService to `android:exported="true"`; keeps the BIND permission, metadata, and opt-in installer behavior. Owner source/test head `2ab1b231a94bbd079b1c52413c4a10c108c28ce5`: CI run #41513629734 SUCCESS (both jobs). E01 audit-document follow-up head `edd6461c932705a109ed9768649cae7e3c162c59`: exact-head CI run #41513716451 pending at checkpoint.
- Added `tests/test_accessibility_service_manifest_security.py` on E01 and mirrored it into F02. It asserts every manifest service handling the AccessibilityService action is exported and protected by BIND_ACCESSIBILITY_SERVICE, including HomeKeyService and InputKeyService.
- F02 integration now includes the same manifest correction and regression test; exact-head F02 CI is required before closing.
- Project G must verify InputKeyService appears in Android Accessibility Settings, can be enabled/disabled, and maps KEYCODE_TV to the input switcher on target firmware. Host CI cannot prove OEM binding or remote delivery.
- Status: OWNER SOURCE/TEST CI PASS / F02 FIX MIRRORED / F02 EXACT-HEAD RETEST PENDING / DEVICE ACCEPTANCE DEFERRED_TO_G.

## F02-BUILD-001 — APK size budget has minimal headroom — 3 November 2026

- Severity: P3 build-budget watchlist; not a current CI failure.
- Exact F02 source/test head `ddf130a348182546b28ff59c292c9b5ceebacb2e` and docs/status head `99489d954bc32ba449028c8512181d9ca1b375e6` both built a release-validation APK of 1,564,981 bytes against the strict 1,572,864-byte cap: 7,883 bytes remaining (about 0.5%). The size diagnostic and cap both passed.
- R8 minification and resource shrinking are already enabled. Largest compressed entries include `classes.dex` (349,533 bytes), `res/SR.ttf` (200,452 bytes), `assets/kabut-pagi-dempo.jpg` (230,997 bytes), `resources.arsc` (170,696 bytes), and `res/rU.ttf` (77,642 bytes).
- No low-risk asset/font removal is authorized from size diagnostics alone: wallpaper and typeface quality/localization are product assets. Monitor every subsequent exact-head build; if the cap is crossed, profile resource/code contribution and propose a measured optimization without degrading visuals or removing required features.
- Status: WITHIN BUDGET / LOW HEADROOM / MONITOR THROUGH PROJECT G.


## Final exact-head retest for this checkpoint — 3 November 2026

- F02 PR #50 head `558762994dec05a7ae03a924c33a3ed11665c224`: push CI run #41513940620 completed SUCCESS for both required jobs; matching PR run #41513947375 also completed SUCCESS for both jobs.
- The source/test checkpoint immediately before the latest documentation sync, `5a01785cfd4ab81e816a955bd7647c0e766c6b2f`, passed PR CI #41513906117. The Lists/tools/docs job ran 35 Python tests; the new AccessibilityService manifest regression test passed. Launcher unit tests, release validation build, APK diagnostics and size cap passed.
- F02-SEC-005 is now HOST/SOURCE VERIFIED: regression failed before the InputKeyService manifest correction and passed after it. E01 owner PR #34 latest head `edd6461c932705a109ed9768649cae7e3c162c59`, run #41513716451 both jobs SUCCESS. Device KEYCODE_TV and system service binding acceptance remains DEFERRED_TO_G.
- Latest F02 APK measurement from the corrected source/test checkpoint: 1,564,977 bytes against 1,572,864-byte cap; 7,887 bytes headroom. F02-BUILD-001 remains a P3 watchlist, not a CI blocker.
- PR #50 remains Draft/open/unmerged. No APK artifact publication, install, or physical Android TV test.
