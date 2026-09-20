# StoreOps v2.0 — Sequential Feature Delivery Runbook

## 1. Purpose

This runbook defines the execution model for adding the four StoreOps demonstration features through the **Configuration-Driven Governed Agentic Delivery Harness v2.0** using Claude Code.

The four features are delivered **sequentially** against an evolving StoreOps codebase:

1. **SLA Breach Alerting**
2. **Shift Handover Bulk Update**
3. **Regional Rollup Report**
4. **Planogram Task Template**

The operating model is:

```text
main
  ↓
Feature Branch
  ↓
Planner
  ↓
Planner-Derived Sprints
  ↓
Approve One Sprint
  ↓
Generator
  ↓
Evaluator
  ↓
Governed Retry if Required
  ↓
Monitor
  ↓
Archive
  ↓
Commit Concluded Sprint
  ↓
Approve Next Sprint
  ↓
...
  ↓
All Feature Sprints Complete
  ↓
STATUS: COMPLETED
  ↓
Final Verification
  ↓
Feature PR
  ↓
Independent CI/CD
  ↓
Merge to main
  ↓
Create Next Feature Branch from Updated main
```

---

# 2. Core Delivery Rules

The following rules apply to every feature.

## 2.1 One Feature = One Feature Branch

Each feature is implemented on its own Git branch.

Recommended branches:

```text
feature/sla-breach-alerting
feature/shift-handover-bulk-update
feature/regional-rollup-report
feature/planogram-task-template
```

---

## 2.2 One Feature = One Harness Run

Each feature receives a new:

```text
RUN_ID
```

All Planner-derived sprints for that feature remain under the same run.

Example:

```text
RUN_ID: RUN-SLA-001

Sprint 1
Sprint 2
Sprint 3
```

Evidence remains grouped under:

```text
.harness/output/<RUN_ID>/
```

and concluded review evidence under:

```text
.harness/reviews/<RUN_ID>/
```

or the equivalent paths resolved by the active harness configuration.

---

## 2.3 One Sprint Approved at a Time

Planner may generate multiple sprint contracts:

```text
sprint-1-contract.md
sprint-2-contract.md
sprint-3-contract.md
```

Review all generated contracts to understand the complete feature plan.

However, approve only one sprint at a time.

```text
Sprint 1
   ↓
Approve
   ↓
Implement
   ↓
Evaluate
   ↓
Conclude
   ↓
Archive
   ↓
Commit
   ↓
Approve Sprint 2
```

Do not approve all sprints together.

---

## 2.4 Commit Only Concluded Sprints

Do not commit immediately after Generator.

Use:

```text
Generator
   ↓
Evaluator
   ↓
PASS / governance-accepted CONDITIONAL_PASS
   ↓
Monitor
   ↓
Archive
   ↓
Git Commit
```

This creates traceability between:

```text
Sprint Contract
      ↓
Generator Evidence
      ↓
Evaluator Verdict
      ↓
Monitor Evidence
      ↓
Archived Evidence
      ↓
Git Commit SHA
```

---

## 2.5 Merge Only Completed Features

Do not merge a feature branch while feature sprints remain incomplete.

Required feature merge gate:

```text
All Planner-Generated Sprints Concluded
        +
No RETRY_REQUIRED
        +
No CONTRACT_CHANGE_REQUIRED
        +
No Unresolved ESCALATION
        +
Required Evidence Archived
        +
STATUS: COMPLETED
        +
Final Local Verification PASS
        +
Feature PR
        +
Independent CI/CD PASS
        ↓
Merge to main
```

---

# 3. Recommended Feature Sequence

Execute the features in this order:

```text
1. SLA Breach Alerting
        ↓
2. Shift Handover Bulk Update
        ↓
3. Regional Rollup Report
        ↓
4. Planogram Task Template
```

Each subsequent feature starts from the latest successfully merged `main`.

Therefore:

```text
Feature 1 Planner sees:
Original StoreOps

Feature 2 Planner sees:
Original StoreOps
+ SLA Breach Alerting

Feature 3 Planner sees:
Original StoreOps
+ SLA Breach Alerting
+ Shift Handover Bulk Update

Feature 4 Planner sees:
Original StoreOps
+ SLA Breach Alerting
+ Shift Handover Bulk Update
+ Regional Rollup Report
```

This demonstrates that the harness can repeatedly operate against an **evolving existing application**.

---

# 4. Initial Main Baseline

Before Feature 1:

```bash
git checkout main
git pull origin main
git status
```

The working tree should be clean.

Record the baseline:

```bash
git rev-parse HEAD
```

Optionally create a baseline tag:

```bash
git tag storeops-harness-v2-baseline
```

Run the StoreOps verification command resolved by the active Java/Spring technology configuration.

For example, if the configured command is:

```bash
mvn clean verify
```

execute it from the appropriate StoreOps Maven root.

Proceed only when:

```text
main
+
StoreOps baseline
+
configured verification
=
PASS
```

---

# 5. Feature 1 — SLA Breach Alerting

## 5.1 Create Feature Branch

```bash
git checkout main
git pull origin main

git checkout -b feature/sla-breach-alerting
```

Verify:

```bash
git branch --show-current
git status
```

Expected:

```text
feature/sla-breach-alerting
working tree clean
```

---

## 5.2 Select Feature Prompt

Place the finalized SLA Breach Alerting prompt at the configured:

```text
PROMPT.md
```

Verify:

```bash
git diff -- PROMPT.md
```

Ensure only the intended feature prompt is active.

---

# 6. Bootstrap + Planner

Start a new Claude Code conversation.

Use:

```text
Run the governed harness for the configured feature defined in @PROMPT.md.

Follow CLAUDE.md and the active harness configuration.

Execute only:

1. bootstrap;
2. configuration validation and resolution; and
3. Planner workflow.

If mandatory configuration is missing, invalid, contradictory, or cannot
be resolved:

- make no StoreOps application or test changes;
- identify the configuration problem;
- stop with:

STATUS: CONFIGURATION_ERROR

If configuration is valid:

- generate a unique RUN_ID;
- create the immutable non-secret resolved-config.yaml;
- inspect existing StoreOps source and tests as required;
- create spec.md;
- create Planner-derived sprint-N-contract.md files;
- surface material ambiguity rather than inventing behavior.

Do not modify StoreOps source or tests.
Do not invoke Generator, Evaluator, or Monitor.
Do not begin implementation.
Do not self-approve.

Stop with:

STATUS: AWAITING_APPROVAL
```

---

# 7. Verify Planner Isolation

After Planner completes:

```bash
git status
git diff
```

Confirm:

```text
[ ] No StoreOps application source modified
[ ] No StoreOps tests modified
[ ] resolved-config.yaml created
[ ] spec.md created
[ ] sprint-N-contract.md files created
[ ] RUN_ID created
[ ] Repository evidence used
[ ] Material ambiguity surfaced
[ ] Architecture ownership identified
[ ] Acceptance criteria have unique IDs
[ ] Positive paths covered
[ ] Negative paths covered
[ ] Required tests identified
```

---

# 8. Review All Feature Sprints

Suppose Planner creates:

```text
sprint-1-contract.md
sprint-2-contract.md
sprint-3-contract.md
```

Review all three.

Confirm:

```text
Business Objective
Scope
Out of Scope
Dependencies
Assumptions
Architecture Constraints
Acceptance Criteria
Required Tests
Applicable Hard Gates
Completion Conditions
```

Do not approve all three.

---

# 9. Approve Sprint 1 Only

Use:

```text
APPROVED: Sprint 1 only.

Run ID: <RUN_ID>
Sprint ID: <SPRINT_1_ID>
Contract ID: <CONTRACT_1_ID>

Only the scope, acceptance criteria, architecture constraints, required
tests, dependencies, assumptions, and completion conditions contained in
this Sprint 1 contract are approved.

No other sprint, module, endpoint, event, domain model, infrastructure,
refactoring, or contract change is approved.

Freeze the approved specification and Sprint 1 contract.

Proceed with Generator iteration 1 for Sprint 1 only.

Do not expand scope.
Do not execute another sprint without separate human approval.
```

---

# 10. Execute Generator

Prefer a fresh Claude Code conversation.

Use:

```text
Execute Generator for the approved sprint only.

Run ID: <RUN_ID>
Sprint ID: <SPRINT_ID>
Contract ID: <CONTRACT_ID>
Iteration: 1

Follow:

- CLAUDE.md;
- resolved-config.yaml;
- the frozen approved sprint contract;
- active StoreOps skills;
- active technology skills;
- existing StoreOps source and tests.

Implement the smallest change satisfying the approved contract.

Modify only approved source/tests.

Preserve:

- StoreOps architecture;
- module ownership;
- application error behavior;
- authorization;
- backward compatibility;
- approved cross-module interaction mechanisms.

Execute configured verification.

Self-assess every acceptance criterion by ID.

Create:

generator-summary-iteration-1.md

Do not:

- modify resolved-config.yaml;
- modify spec.md;
- modify the approved sprint contract;
- weaken governance;
- expand scope;
- perform unrelated refactoring;
- issue PASS, CONDITIONAL_PASS, or FAIL;
- invoke Evaluator;
- invoke Monitor;
- deploy;
- reflect.

If implementation requires a material contract change, stop with:

STATUS: CONTRACT_CHANGE_REQUIRED

Otherwise stop with:

STATUS: READY_FOR_EVALUATION
```

---

# 11. Execute Independent Evaluator

Start a **fresh Claude Code conversation**.

Use:

```text
Execute the independent Evaluator.

Run ID: <RUN_ID>
Sprint ID: <SPRINT_ID>
Contract ID: <CONTRACT_ID>
Iteration: 1

Follow:

- CLAUDE.md;
- resolved-config.yaml;
- the frozen approved sprint contract;
- generator-summary-iteration-1.md;
- active governance policy;
- active technology configuration;
- applicable architecture rules;
- review guidelines;
- evaluation framework;
- changed source and tests.

Do not accept Generator self-assessment as proof.

Do not modify source, tests, configuration, specification, contracts,
or Generator evidence.

Independently execute configured applicable deterministic checks before
semantic review.

A named tool is not proof that it executed.

For every enabled mandatory gate, require concrete executable evidence.

Verify every acceptance criterion independently by ID.

Create:

evaluator-feedback-iteration-1.md

Every failed finding must contain:

RULE
CHECK
FILE
LINE or N/A
OBSERVED
EXPECTED
REMEDIATION

Issue exactly one:

VERDICT: PASS

or:

VERDICT: CONDITIONAL_PASS

or:

VERDICT: FAIL

Do not remediate.
Do not authorize retry.
Do not route.
Do not invoke Monitor.
Do not deploy.
Do not reflect.
```

---

# 12. Apply Governance Routing

Use the Evaluator verdict as input to harness-owned routing.

```text
PASS
  ↓
Sprint concluded
  ↓
Monitor
  ↓
Archive
  ↓
Commit
  ↓
Next Sprint Approval
```

```text
CONDITIONAL_PASS
  ↓
Apply active governance policy
  ↓
Human disposition if required
  ↓
Monitor
  ↓
Archive
  ↓
Commit
  ↓
Next Sprint Approval
```

```text
FAIL
+
Iteration Available
  ↓
STATUS: RETRY_REQUIRED
  ↓
Generator N+1
  ↓
Evaluator N+1
```

```text
FAIL
+
maxIterations Exhausted
  ↓
STATUS: ESCALATED
  ↓
escalation.md
  ↓
STOP
```

```text
CONTRACT_CHANGE_REQUIRED
  ↓
Planner / Human Review
  ↓
Revised Contract
  ↓
Human Re-Approval
```

```text
CONFIGURATION_ERROR
  ↓
Correct Configuration
  ↓
Restart Bootstrap
```

Evaluator does not authorize retry.

Monitor does not determine routing.

---

# 13. Generator Retry

If the harness returns:

```text
STATUS: RETRY_REQUIRED
```

use:

```text
Execute the governance-authorized Generator retry.

Run ID: <RUN_ID>
Sprint ID: <SPRINT_ID>
Contract ID: <CONTRACT_ID>
Iteration: <N+1>

The approved sprint contract remains frozen.

Read:

- resolved-config.yaml;
- approved sprint contract;
- generator-summary-iteration-<N>.md;
- evaluator-feedback-iteration-<N>.md;
- current relevant source/tests;
- applicable active skills.

Correct only:

- failed areas identified by Evaluator; and
- directly impacted areas required to preserve correctness.

Do not expand scope.
Do not rewrite the contract.
Do not weaken governance.
Do not perform unrelated refactoring.

Rerun configured verification.

Preserve all previous evidence.

Create:

generator-summary-iteration-<N+1>.md

Never overwrite previous iteration evidence.

If remediation requires a material contract change:

STATUS: CONTRACT_CHANGE_REQUIRED

Otherwise:

STATUS: READY_FOR_EVALUATION
```

---

# 14. Evaluator Retry

Start another fresh Evaluator conversation.

```text
Execute independent Evaluator iteration <N+1>.

Run ID: <RUN_ID>
Sprint ID: <SPRINT_ID>
Contract ID: <CONTRACT_ID>
Iteration: <N+1>

The approved sprint contract remains frozen.

Read:

- resolved-config.yaml;
- approved sprint contract;
- generator-summary-iteration-<N+1>.md;
- evaluator-feedback-iteration-<N>.md;
- current changed source/tests;
- active governance and technology configuration.

Independently rerun all applicable configured deterministic checks.

Do not assume a previously passing check still passes.

Verify remediation of prior findings.

Verify applicable acceptance criteria.

Do not modify implementation.

Create:

evaluator-feedback-iteration-<N+1>.md

Never overwrite previous iteration evidence.

Issue exactly one:

VERDICT: PASS
VERDICT: CONDITIONAL_PASS
VERDICT: FAIL

Do not remediate.
Do not authorize another retry.
Do not route.
Do not invoke Monitor.
```

Repeat governed retry only while permitted by the active governance policy.

---

# 15. Execute Monitor

After a concluded sprint outcome:

```text
Execute Monitor for the harness-concluded sprint outcome.

Run ID: <RUN_ID>
Sprint ID: <SPRINT_ID>
Contract ID: <CONTRACT_ID>

Follow CLAUDE.md and the configured run-log template.

Use only canonical Generator/Evaluator evidence and harness metadata.

Create or update:

run-log.md

Record:

- Run ID;
- Sprint ID;
- Contract ID;
- final Evaluator verdict exactly as issued;
- iterations used;
- Generator evidence references;
- Evaluator evidence references;
- deterministic-check summary by reference;
- acceptance-criterion summary by reference;
- findings summary by reference;
- harness-determined escalation status;
- available observability evidence.

Use:

N/A

for unavailable telemetry, timestamps, token usage, cost, or other
non-existent evidence.

Do not:

- evaluate;
- rerun checks;
- create findings;
- change the Evaluator verdict;
- authorize retry;
- authorize escalation;
- determine progression;
- determine completion;
- deploy;
- rollback;
- reflect.
```

---

# 16. Archive Concluded Sprint

Use:

```text
Perform the harness-owned archive step for the concluded sprint.

Run ID: <RUN_ID>
Sprint ID: <SPRINT_ID>
Contract ID: <CONTRACT_ID>

Archive the canonical concluded evidence into the configured review
location.

Preserve:

- approved sprint contract;
- every generator-summary-iteration-N.md;
- every evaluator-feedback-iteration-N.md;
- run-log.md;
- escalation.md where applicable.

Do not overwrite previous iteration evidence.

Do not rewrite immutable review evidence.

Preserve Run ID, Sprint ID, Contract ID, iteration numbers, and
acceptance-criterion identifiers exactly.

After archive, stop at the next human approval boundary if another
Planner-generated sprint exists.
```

---

# 17. Commit the Concluded Sprint

After:

```text
Evaluator PASS / accepted CONDITIONAL_PASS
        ↓
Monitor
        ↓
Archive
```

prepare the sprint commit.

Use Claude Code:

```text
Prepare the concluded sprint for Git commit.

Run ID: <RUN_ID>
Sprint ID: <SPRINT_ID>
Contract ID: <CONTRACT_ID>

Verify:

- the sprint has a concluded PASS or governance-accepted
  CONDITIONAL_PASS;
- Monitor evidence exists;
- sprint evidence has been archived;
- Git diff contains only approved sprint implementation and tests;
- no unrelated changes exist;
- no secrets or credentials are present;
- no generated/local files prohibited by repository policy are included.

Do not commit automatically.

Provide:

1. files recommended for commit;
2. files that should remain uncommitted;
3. proposed commit message;
4. Run ID;
5. Sprint ID;
6. Contract ID;
7. acceptance criteria satisfied.

Stop for human Git commit approval.
```

Then inspect:

```bash
git status
git diff --stat
git diff
```

Commit manually.

Example:

```bash
git add <APPROVED_FILES>

git commit -m "feat(sla): implement sprint 1 breach detection"
```

---

# 18. Approve Next Sprint

Remain on the **same feature branch**.

Do not merge.

Do not create another feature branch.

Do not rerun Planner merely because the previous sprint completed.

Use:

```text
APPROVED: Sprint <N> only.

Run ID: <SAME_RUN_ID>
Sprint ID: <SPRINT_N_ID>
Contract ID: <CONTRACT_N_ID>

The prior sprint has concluded and its evidence has been archived.

Only this sprint contract is now approved.

Proceed with Generator iteration 1 for this sprint only.

Do not reopen completed sprint scope.
Do not modify previously approved or archived evidence.
Do not execute any later sprint without separate human approval.
```

Then repeat:

```text
Generator
   ↓
Evaluator
   ↓
Governed Retry if Required
   ↓
Monitor
   ↓
Archive
   ↓
Commit
```

---

# 19. Complete All Sprints for the Feature

Example:

```text
Feature: SLA Breach Alerting
Run: RUN-SLA-001

Sprint 1
   ↓
PASS
   ↓
Monitor
   ↓
Archive
   ↓
Commit

Sprint 2
   ↓
PASS
   ↓
Monitor
   ↓
Archive
   ↓
Commit

Sprint 3
   ↓
PASS
   ↓
Monitor
   ↓
Archive
   ↓
Commit
```

Only after all feature sprints conclude should the harness evaluate feature-run completion.

---

# 20. Feature Completion

Use:

```text
Evaluate harness run completion.

Run ID: <RUN_ID>

Verify that:

1. configuration resolved successfully;
2. resolved-config.yaml exists;
3. spec.md exists;
4. all approved sprint contracts exist;
5. every approved sprint has concluded;
6. every Generator iteration has preserved evidence;
7. every Evaluator iteration has preserved evidence;
8. every concluded sprint has Monitor evidence;
9. required archive evidence exists;
10. no unresolved retry remains;
11. no unresolved contract-change request remains;
12. no unresolved human approval remains;
13. no unresolved escalation permits autonomous continuation;
14. traceability identifiers are consistent.

If all feature sprints have successfully concluded according to the
active governance policy, return:

STATUS: COMPLETED

Do not deploy.
Do not treat harness completion as CI/CD evidence.
```

---

# 21. Final Local Verification

After:

```text
STATUS: COMPLETED
```

run:

```bash
git status
git diff --stat
```

Then execute the configured StoreOps verification command.

For example:

```bash
mvn clean verify
```

Use the actual command resolved by the active technology configuration.

Confirm:

```text
[ ] Build passes
[ ] Tests pass
[ ] Configured coverage checks pass
[ ] Configured static analysis passes
[ ] Configured architecture checks pass
[ ] Configured security checks pass where executable
[ ] No unrelated changes
[ ] No secrets
[ ] All sprint evidence preserved
[ ] STATUS: COMPLETED
```

---

# 22. Push Completed Feature Branch

Example for SLA:

```bash
git push -u origin feature/sla-breach-alerting
```

Create a Pull Request:

```text
feature/sla-breach-alerting
        ↓
main
```

The PR should reference:

```text
Feature
RUN_ID
Sprint IDs
Contract IDs
Commit SHAs
Acceptance Criteria
Harness Verdicts
Known Limitations
```

---

# 23. Independent CI/CD

CI/CD must independently validate the committed revision.

Expected checks may include, where configured:

```text
Build
Tests
Coverage
Static Analysis
Architecture Checks
Security Checks
```

Important:

```text
Harness PASS
    ≠
CI PASS
```

Harness evidence and CI evidence remain separate.

Merge only after required CI checks pass.

---

# 24. Merge Feature into Main

After PR approval and CI success:

```bash
git checkout main
git pull origin main
```

Merge through the repository's normal PR process.

If performing a local demonstration merge:

```bash
git merge --no-ff feature/sla-breach-alerting
git push origin main
```

Verify:

```bash
git log --oneline --graph --decorate -10
```

Now `main` contains Feature 1.

---

# 25. Start Feature 2 from Updated Main

After Feature 1 is merged:

```bash
git checkout main
git pull origin main
git status
```

Run baseline verification again.

Then:

```bash
git checkout -b feature/shift-handover-bulk-update
```

The Shift Handover Planner now treats the merged SLA implementation as part of the existing StoreOps application.

Select the Shift Handover `PROMPT.md`.

Start a **new harness run** with a **new RUN_ID**.

Repeat the complete lifecycle.

---

# 26. Merge Feature 2

After all Shift Handover sprints conclude:

```text
STATUS: COMPLETED
```

Then:

```text
Final Local Verification
        ↓
Push Feature Branch
        ↓
PR
        ↓
Independent CI/CD
        ↓
Merge
```

Result:

```text
main
├── Original StoreOps
├── SLA Breach Alerting
└── Shift Handover Bulk Update
```

---

# 27. Start Feature 3 from Updated Main

```bash
git checkout main
git pull origin main

git checkout -b feature/regional-rollup-report
```

Select:

```text
Regional Rollup Report PROMPT.md
```

Start a new harness run.

Complete all Planner-derived sprints.

Then:

```bash
git push -u origin feature/regional-rollup-report
```

PR → CI → Merge.

Result:

```text
main
├── Original StoreOps
├── SLA Breach Alerting
├── Shift Handover Bulk Update
└── Regional Rollup Report
```

---

# 28. Start Feature 4 from Updated Main

```bash
git checkout main
git pull origin main

git checkout -b feature/planogram-task-template
```

Select:

```text
Planogram Task Template PROMPT.md
```

Start a new harness run.

Complete all Planner-derived sprints.

Then:

```bash
git push -u origin feature/planogram-task-template
```

PR → CI → Merge.

Final result:

```text
main
├── Original StoreOps
├── SLA Breach Alerting
├── Shift Handover Bulk Update
├── Regional Rollup Report
└── Planogram Task Template
```

---

# 29. Recommended Git History

The final repository history should resemble:

```text
*   Merge feature/planogram-task-template
|\
| * feat(planogram): implement sprint 2
| * feat(planogram): implement sprint 1
|/
*
*   Merge feature/regional-rollup-report
|\
| * feat(rollup): implement sprint 2
| * feat(rollup): implement sprint 1
|/
*
*   Merge feature/shift-handover-bulk-update
|\
| * feat(bulk): implement sprint 2
| * feat(bulk): implement sprint 1
|/
*
*   Merge feature/sla-breach-alerting
|\
| * feat(sla): implement sprint 3
| * feat(sla): implement sprint 2
| * feat(sla): implement sprint 1
|/
*
* StoreOps harness v2 baseline
```

This provides strong traceability between:

```text
Feature
  ↓
RUN_ID
  ↓
Sprint
  ↓
Contract
  ↓
Generator
  ↓
Evaluator
  ↓
Verdict
  ↓
Monitor
  ↓
Archive
  ↓
Commit SHA
  ↓
Feature PR
  ↓
CI
  ↓
Merge
```

---

# 30. Context-Isolation Rule Between Features

When starting the next feature, the Planner should learn from:

```text
Updated StoreOps Source
Updated StoreOps Tests
Active StoreOps Skills
Active Configuration
Current PROMPT.md
```

Do not automatically feed previous feature-specific artifacts such as:

```text
Previous spec.md
Previous sprint contracts
Previous Generator summaries
Previous Evaluator feedback
Previous REFLECTION.md
```

into the next Planner context.

Previous feature implementation that has been merged into `main` is now legitimate:

```text
Existing Application Evidence
```

Previous feature planning/evaluation artifacts are not automatically permanent StoreOps domain knowledge.

---

# 31. When to Rerun Planner Within a Feature

Do not rerun Planner simply because a sprint completed.

Normal progression:

```text
Planner
   ↓
Sprint 1 Contract
Sprint 2 Contract
Sprint 3 Contract
   ↓
Approve Sprint 1
   ↓
Complete Sprint 1
   ↓
Approve Sprint 2
   ↓
Complete Sprint 2
   ↓
Approve Sprint 3
```

Rerun Planner only when a material issue requires replanning, such as:

```text
CONTRACT_CHANGE_REQUIRED

Material Repository Discovery

Architecture Conflict

Requirement Change

Invalidated Assumption

New Human Decision
```

Any revised contract must return to:

```text
STATUS: AWAITING_APPROVAL
```

before Generator continues.

---

# 32. Deployment and Runtime Validation

Deployment remains separate from pre-deployment harness governance.

Recommended feature flow:

```text
STATUS: COMPLETED
        ↓
Final Local Verification
        ↓
Feature PR
        ↓
Independent CI/CD
        ↓
Merge to main
        ↓
Deployment Readiness
        ↓
Explicit Human Deployment Approval
        ↓
Deploy using DEPLOYMENT.md
        ↓
Runtime Validation
```

Do not invent deployment commands.

Use only the repository's documented deployment procedure.

Do not expose secrets.

Do not bypass authorization.

---

# 33. Reflection

After actual harness, CI/CD, and deployment/runtime evidence exists, complete reflection if required by the repository process.

Use:

```text
Complete REFLECTION.md for:

Run ID: <RUN_ID>
Feature: <FEATURE_NAME>

Use only actual evidence from:

- resolved-config.yaml;
- spec.md;
- approved sprint contracts;
- Generator iteration evidence;
- Evaluator iteration evidence;
- run-log.md;
- escalation evidence where applicable;
- CI/CD evidence;
- deployment/runtime evidence where applicable.

Document:

1. what worked well;
2. human interventions;
3. Planner discovery quality;
4. contract quality;
5. Generator effectiveness;
6. Evaluator effectiveness;
7. deterministic-check effectiveness;
8. retry/escalation behavior;
9. context-isolation effectiveness;
10. evidence/traceability quality;
11. observed harness limitations;
12. concrete evidence-linked improvements;
13. reusable lessons for the next feature.

Do not fabricate evidence.

Do not modify canonical run evidence.
```

---

# 34. Complete Four-Feature Execution Flow

```text
INITIAL MAIN
StoreOps Baseline
      │
      ▼
feature/sla-breach-alerting
      │
      ├── Planner
      ├── Sprint 1 → Evaluate → Conclude → Commit
      ├── Sprint 2 → Evaluate → Conclude → Commit
      ├── Sprint N → Evaluate → Conclude → Commit
      │
      └── STATUS: COMPLETED
              ↓
         Local Verify
              ↓
             PR
              ↓
             CI
              ↓
            MERGE
              ↓
MAIN + SLA
      │
      ▼
feature/shift-handover-bulk-update
      │
      ├── Planner
      ├── Sprint 1 → Evaluate → Conclude → Commit
      ├── Sprint N → Evaluate → Conclude → Commit
      │
      └── STATUS: COMPLETED
              ↓
             PR
              ↓
             CI
              ↓
            MERGE
              ↓
MAIN + SLA + BULK
      │
      ▼
feature/regional-rollup-report
      │
      ├── Planner
      ├── Sprint 1 → Evaluate → Conclude → Commit
      ├── Sprint N → Evaluate → Conclude → Commit
      │
      └── STATUS: COMPLETED
              ↓
             PR
              ↓
             CI
              ↓
            MERGE
              ↓
MAIN + SLA + BULK + ROLLUP
      │
      ▼
feature/planogram-task-template
      │
      ├── Planner
      ├── Sprint 1 → Evaluate → Conclude → Commit
      ├── Sprint N → Evaluate → Conclude → Commit
      │
      └── STATUS: COMPLETED
              ↓
             PR
              ↓
             CI
              ↓
            MERGE
              ↓
FINAL MAIN
```

---

# 35. Per-Feature Checklist

Use this checklist for every feature.

```text
FEATURE START

[ ] Checkout main
[ ] Pull latest main
[ ] Confirm clean working tree
[ ] Run baseline verification
[ ] Create feature branch from updated main
[ ] Select correct PROMPT.md
[ ] Start new Claude Code conversation
[ ] Bootstrap/configuration validation
[ ] Planner only
[ ] Receive RUN_ID
[ ] Review resolved-config.yaml
[ ] Review spec.md
[ ] Review all sprint contracts
[ ] Confirm no source/test changes from Planner

SPRINT LOOP

[ ] Approve one sprint only
[ ] Generator iteration 1
[ ] READY_FOR_EVALUATION
[ ] Fresh Evaluator conversation
[ ] Evaluator verdict

If FAIL:
[ ] Harness determines retry eligibility
[ ] RETRY_REQUIRED if allowed
[ ] Generator N+1
[ ] Evaluator N+1
[ ] Preserve all evidence

If CONTRACT_CHANGE_REQUIRED:
[ ] Stop implementation
[ ] Return to Planner/human review
[ ] Revise contract
[ ] Re-approve

If ESCALATED:
[ ] Preserve evidence
[ ] Create escalation.md
[ ] Stop autonomous execution

If PASS / accepted CONDITIONAL_PASS:
[ ] Monitor
[ ] Archive
[ ] Inspect Git diff
[ ] Commit concluded sprint

NEXT SPRINT

[ ] Review next existing sprint contract
[ ] Approve next sprint only
[ ] Repeat Generator/Evaluator/Monitor/Archive/Commit

FEATURE COMPLETION

[ ] All sprints concluded
[ ] No unresolved retry
[ ] No unresolved contract change
[ ] No unresolved escalation
[ ] All evidence preserved
[ ] STATUS: COMPLETED
[ ] Final local verification
[ ] Push feature branch
[ ] Create PR
[ ] Independent CI/CD passes
[ ] Merge feature into main

NEXT FEATURE

[ ] Checkout updated main
[ ] Pull latest main
[ ] Verify merged feature
[ ] Run baseline verification
[ ] Create next feature branch
[ ] Select next PROMPT.md
[ ] Generate new RUN_ID
[ ] Repeat lifecycle
```

---

# 36. Golden Operating Rule

```text
One Feature
    =
One Feature Branch
    +
One Harness RUN_ID
    +
Multiple Planner-Derived Sprints
    +
One Human Approval per Sprint
    +
One Governed Commit per Concluded Sprint
    +
One Feature PR
    +
Independent CI/CD
    +
One Merge to main
```

The next feature branch is always created from the **newly updated `main`**.

Therefore:

```text
PROMPT.md
    ↓
defines WHAT is requested

Planner
    ↓
discovers how the feature fits the CURRENT StoreOps codebase

Human Approval
    ↓
freezes one sprint contract

Generator
    ↓
implements only that contract

Evaluator
    ↓
independently judges it

Harness + Governance
    ↓
route retry / escalation / progression

Monitor
    ↓
records concluded evidence

Git Commit
    ↓
captures the governed sprint increment

Feature PR + CI
    ↓
independently validate the completed feature

Merge to main
    ↓
makes the feature part of the next Planner's existing application evidence
```

This sequential model demonstrates that the governed harness can repeatedly add new capabilities to an **evolving StoreOps codebase** while preserving sprint-level approval, evidence, traceability, independent evaluation, CI/CD separation, and feature-level Git isolation.
