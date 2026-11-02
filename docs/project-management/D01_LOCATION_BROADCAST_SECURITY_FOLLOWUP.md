# D01 follow-up — Location broadcast sender authorization

## Finding
F02-SEC-001 identified that `.LocationBroadcastReceiver` was exported without a sender permission. Its two actions can change persistent location configuration or set a test-weather override.

## Targeted change
- Keep the receiver exported so the documented `adb shell am broadcast` configuration workflow remains available.
- Require `android.permission.DUMP` at the receiver boundary. This is a privileged shell/system permission, not a permission ordinary third-party apps can obtain.
- Preserve both existing actions and their intent extras; no location storage schema, station resolution, weather state, or UI behavior is changed.
- Add a manifest regression test that asserts the permission and both actions remain present.

## Security and compatibility boundary
This change supports callers running as Android shell/system (including the documented ADB shell command path). A normal third-party assistant app sending broadcasts directly is not an authorized caller under this contract; it must invoke the supported shell bridge or a separately reviewed authenticated integration. Do not weaken the permission to restore arbitrary app access.

## Verification
- Host-side manifest regression: added as `tests/test_location_broadcast_manifest_security.py`.
- Android TV / ADB-shell compatibility: **DEFERRED_TO_G**. CI can verify manifest intent and permission declarations, but cannot prove the actual shell UID permission behavior on the target TV firmware.
- No physical device test, APK installation, or APK publication performed.

## Scope protection
No receiver logic, AppLocationManager persistence behavior, station matching, prayer-zone behavior, or SET_WEATHER semantics changed. The fix is limited to the Android component sender authorization boundary.


## Additional component-boundary correction — LocationActivity
- Manifest audit found `.LocationActivity` exported despite having no intent filter and no documented external entry contract.
- In-repository entry points are explicit intents owned by MainActivity. The activity is a location picker, not a public launcher surface.
- Changed `android:exported` to `false`; internal explicit intents continue to resolve inside the same application.
- Extended `tests/test_location_broadcast_manifest_security.py` to assert the activity remains non-exported, has no intent filter, and is still referenced by MainActivity.
- This is a separate F02-SEC-003 remediation carried by the D01 follow-up PR. Exact-head CI is pending after this addition.
