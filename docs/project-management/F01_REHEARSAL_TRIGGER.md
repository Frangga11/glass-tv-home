# Project F01 isolated integration rehearsal

- Branch: `integration/f01-rehearsal-only`
- Starting tree: GitHub synthetic merge ref for PR #41 at `b820cd01ee30253862030441fe17fb3cacb3714c`.
- Purpose: run the repository CI build and unit-test workflow against the current E08 PR merge candidate without merging any feature PR.
- This is an isolated rehearsal branch only. It is not `integration/b-series`, not `main`, and must not be treated as an approved merge or release branch.
- Workflow dispatch/publish input is not used. The CI workflow's push trigger runs the launcher unit tests, release build, APK size guard, and repository checks. The manual APK artifact publication step is not eligible on a push event.
- Known limitation: the PR #41 merge candidate inherits the current E07/E08 branch chain but does not by itself prove every upstream base-branch update has been reconciled into the chain. F01's parent-branch drift register remains applicable.
- No APK is published or installed. No physical TV acceptance is claimed.

- F01 follow-up: a second documentation-only commit is used to ensure the draft PR receives a synchronize event; source files remain unchanged by this trigger.
