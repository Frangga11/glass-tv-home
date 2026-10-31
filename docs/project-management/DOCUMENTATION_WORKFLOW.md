# Documentation Operating Standard — Glass TV Home

**Version:** 1.0  
**Approved:** 31 October 2026  
**Applies to:** all project work, audits, releases, and handovers

## Purpose

Keep project knowledge reliable without creating paperwork that slows delivery. GitHub is the canonical, version-controlled record. ChatGPT Library is the backup copy. This standard replaces duplicate reporting and ad-hoc documentation habits.

## Three connected layers

### Layer 1 — MASTER: project memory and navigation

The Master layer is the entry point. In this repository it consists of `MASTER_PROJECT.md`, `INVENTORY.md`, `DECISION_LOG.md`, and `HANDOVER.md`. It records:
- product purpose, scope, device baseline, and key requirements;
- feature inventory and current status;
- approved decisions and unresolved decisions;
- links to the relevant audit/SOP and current handover.

It must summarize and link, not repeat detailed audit findings. Update it only when project-wide facts, scope, baseline, or status changes.

### Layer 2 — AUDIT & SOP: feature evidence and repeatable procedures

The audit layer consists of `AUDIT_REGISTER.md` plus one focused audit file per substantial feature. An audit contains:
- scope and acceptance criteria;
- exact branch/commit and files inspected or changed;
- findings with severity;
- corrective action and verification result;
- remaining blockers, device tests, and rollback.

Create an SOP only when a procedure will be repeated or must be followed consistently. Do not create an SOP for a one-off task.

### Layer 3 — OPERATIONAL LOG: concise change history

`OPERATIONS_LOG.md` is the single chronological log. Record only the event needed to reconstruct the work:
- date, issue/audit ID, branch and commit/PR;
- what changed and why;
- CI/build/test result;
- decision or next action.

Do not copy audit content into the log. Link to the audit instead. A commit/PR may serve as the operational record when it already contains these details; add a log entry only if the context or decision would otherwise be lost.

## Connection rules

1. Master links to the relevant audit/SOP and current handover.
2. Audit/SOP links to the source commit, PR, CI run, and test evidence.
3. Operational log links back to the audit ID.
4. Every fact has one authoritative home. Other layers link to it.
5. Update only the files affected by a change; do not refresh every document after every small task.
6. At session end, update HANDOVER only if the next action, branch/commit, blockers, or verification state changed.
7. Use GitHub as the working source. Export to ChatGPT Library at meaningful milestones (for example, after an audit is accepted or a release), not after every edit.

## Minimum workflow — one pass, no repeated work

1. **Trigger:** identify the user's requested outcome and the relevant audit ID.
2. **Input:** inspect only the current source branch, existing audit, and prior evidence needed for that outcome.
3. **Verify:** confirm branch/commit and distinguish code evidence, CI evidence, and device evidence.
4. **Work:** fix confirmed defects; do not re-investigate already evidenced checks unless code or conditions changed.
5. **Decision point:** record any user choice that changes scope or behavior; ask only when a decision is genuinely blocking.
6. **Output:** implement or document the result and link its evidence.
7. **Quality check:** run the smallest relevant tests, then broader CI only when code changes or risk requires it.
8. **Disposition:** mark PASS / PARTIAL / BLOCKED / NOT TESTED / FINAL. Never call a feature final without all required acceptance evidence.

## Status vocabulary

- **PLANNED:** agreed, not implemented.
- **CODE-VERIFIED:** inspected in the named branch/commit.
- **CI-VERIFIED:** named CI checks passed for the exact commit.
- **DEVICE-VERIFIED:** tested on the actual target device with recorded result.
- **PARTIAL:** some requirements pass, others remain open.
- **BLOCKED:** a dependency prevents the next meaningful step.
- **NOT TESTED:** the test has not been performed.
- **FINAL:** all agreed acceptance criteria pass, evidence is linked, and no release-blocking issue remains.

Never use "final" as a synonym for "code written" or "build succeeded."

## Audit closure — objective gate

Close an audit only when:
- every acceptance criterion has a result;
- critical/high findings are fixed or explicitly accepted by the user;
- relevant CI checks pass for the exact reviewed commit;
- device tests pass when the feature depends on TV/phone/network behavior;
- rollback or recovery is clear;
- Master inventory and handover reflect the accepted state.

A checklist item needs only a short result and evidence link. Screenshots/video are required only when visual behavior or a device-specific claim cannot be verified otherwise.

## Efficiency guardrails

- One feature = one active audit file.
- One finding = one ID; do not duplicate it across documents.
- Do not create separate reports, spreadsheets, or checklists unless they solve a concrete operational need.
- Do not repeat a test without a reason: code changed, test failed, environment changed, or evidence is missing/ambiguous.
- Batch documentation updates at the end of a work unit.
- Prefer a short table and links over long narrative.
- Keep unresolved items visible; do not hide them to make a report look complete.

## Session handover format

HANDOVER should contain only:
1. current phase and authoritative branch/commit;
2. what is verified;
3. what remains and why;
4. the single next action;
5. any required user/device action.

This is the resume point. The Master is the map; audits are the evidence; the log is the history.
