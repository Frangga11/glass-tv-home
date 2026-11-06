# GTVH-UIR-002 v2.0 — G3B Checkpoint
Checkpoint date: 7 November 2026

## STATUS
**G3B Weather & AQI V4: COMPLETE / SOURCE-HOST QC PASS.** Physical Android TV/remote/OEM overscan acceptance: DEFERRED / NOT RUN.

## Position and exact repository state
- Owner branch: feature/g3b-weather-aqi-v4
- Current branch HEAD: 8cfc5c886950cc9d27d4530ba901df872b8e5c44
- CI-verified functional commit: 8f2162036991ded803c5ca0981ad1f3e777cfb54
- Difference between current HEAD and CI-verified commit: one documentation comment in tests/test_weather_cache_location_contract.py only.
- PR #67: OPEN / DRAFT / UNMERGED; base feature/g3a-home-independent-dock @ 87302f52de844954392bac5438cbe82ec5d14a30.
- G2 branch: feature/g2-shared-ui-foundation-repair @ 40fa0715bdbcd324042927fbc56ec19f91d1947a; PR #65 OPEN/DRAFT/UNMERGED.
- G3A branch: feature/g3a-home-independent-dock @ 87302f52de844954392bac5438cbe82ec5d14a30; PR #66 OPEN/DRAFT/UNMERGED.
- Integration branch: integration/b-series @ 562cde0751de93748eb4e0d4cdece4fa1672ac4d; unchanged.
- Documentation branch at start: docs/project-master-inventory @ 6df9f773bc225c1f64e79cc425c48bf36b3d7ef2.

## Files changed in G3B
- launcher/app/src/main/java/com/gathin/tvhome/WeatherAirQuality.java
- launcher/app/src/main/java/com/gathin/tvhome/WeatherActivity.java
- launcher/app/src/main/java/com/gathin/tvhome/MoonPhaseView.java
- launcher/app/src/main/java/com/gathin/tvhome/SunArcView.java
- launcher/app/src/main/java/com/gathin/tvhome/AqiSpectrumBarView.java
- launcher/app/src/main/res/layout/activity_weather.xml
- launcher/app/src/main/res/values/strings.xml
- launcher/app/src/main/res/values-id/strings.xml
- launcher/app/src/main/res/values-en/strings.xml
- launcher/app/src/test/java/com/gathin/tvhome/WeatherAirQualityFreshnessTest.java
- launcher/app/src/test/java/com/gathin/tvhome/WeatherV4LayoutContractTest.java
- launcher/app/src/test/java/com/gathin/tvhome/WeatherActivityFocusRestoreTest.java
- tests/test_weather_cache_location_contract.py

## Implementation and decisions
- Open-Meteo Forecast API supplies weather fields; Open-Meteo Air Quality API supplies CAMS-based pollutant values and US AQI.
- AQI is clearly identified as US AQI model estimate, not official JAS/APIMS IPU or Indonesian ISPU station data.
- PM2.5, PM10, NO₂ and SO₂ display in µg/m³; CO converts from provider µg/m³ to mg/m³ to match G1 V6.
- Missing provider values remain unavailable and are not replaced by zero.
- Cache freshness is coordinate-scoped; legacy JAS/APIMS cache cannot suppress CAMS refresh.
- Separate weather/AQI source timestamps and stale/offline/unavailable/refresh-failed states are visible.
- Moon phase/rise/set, sunrise/sunset, dew point, daily rainfall and UV advice use provider data.
- Pollutants are separate metric values; G2 shared UI foundation is reused; focus is restored across refresh and callbacks are guarded by Activity lifecycle.

## Tests and QC
- CI run 42274182438: SUCCESS; both required jobs SUCCESS.
- Launcher release build, release unit tests, lint-vital and repository contract tests PASS.
- XML audit: one root, balanced tags, no duplicate IDs, required IDs/resources resolved.
- APK diagnostic: 1,600,893 bytes (+11,068 bytes / ~0.7% versus G3A); informational under D-023.
- No physical Android TV, remote, OEM overscan, daylight/glare or large-font acceptance test was performed.

## Risks / not completed
- HomeWeather home-screen summary and WeatherActivity retain separate data/cache ownership; no proven regression was found, but integration consistency remains a follow-up audit item.
- Connectivity state is recalculated on resume/data refresh; no live NetworkCallback is registered while the dashboard stays foregrounded.
- Settings label “Location API Haze” is legacy terminology; clarify/rename in G3F Global Settings.
- Physical device acceptance remains open and must not be claimed PASS.

## Governance
- PR #65/#66/#67 remain OPEN/DRAFT/UNMERGED.
- No merge, rebase, cherry-pick, force-push, integration/main modification, APK publication/installation, ADB or device mutation.
- No G1/G2/G3A rework was performed.

## NEXT
Resume at **G3C — Profile, Avatar & Child Mode** after this checkpoint. Do not reopen G3B absent new defect evidence.
