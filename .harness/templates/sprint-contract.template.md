# Sprint Contract Template

Runtime instantiation: `.harness/output/<run-id>/sprint-<SPRINT_ID>-contract.md`

This template represents an approved, independently evaluable sprint contract.

Do not treat this template as an authoritative source of governance thresholds, scoring weights, verdict thresholds, `maxIterations`, technology commands, deployment/rollback controls, or orchestration statuses.

The active governance policy and resolved configuration remain authoritative for those values. Where hard gates are represented below, they must reference the active governance policy rather than restate authoritative thresholds.

---

## Identifiers

```text
Run ID:            <RUN_ID>
Sprint ID:         <SPRINT_ID>
Contract ID:       <CONTRACT_ID>
Application:       <resolved application id>
Technology:        <resolved technology profile id>
Governance Policy: <resolved governance policy id>
Feature Prompt:    <path/to/PROMPT.md>
Specification:     .harness/output/<RUN_ID>/spec.md
Approved By:       <human approver identity>
Approved At:       <timestamp>
Baseline:          v2.0
```

---

## Objective

Describe the approved objective of this sprint.

Objective should describe observable behavior and expected value, not implementation.

---

## Scope

Describe the approved in-scope behavior for this sprint.

---

## Out of Scope

Describe behavior explicitly excluded from this sprint.

---

## Dependencies

List dependencies for this sprint.

Example structure:

```text
Dependency:               <text>
Nature (technical/domain/data/configuration/other): <text>
Status:                   <resolved/pending/at-risk>
Impact if Unavailable:    <text>
```

---

## Assumptions

List assumptions approved as part of this sprint.

Example structure:

```text
Assumption:      <text>
Basis:           <repository / configuration / domain / feature intent>
Impact if False: <text>
Affected Acceptance Criteria: <AC IDs>
```

Do not use assumptions to silently redefine business behavior established in `spec.md`.

---

## Applicable Domain and Application Context

Reference the active domain and application context relevant to this sprint.

Do not re-author application/domain rules here.

Example structure:

```text
Application Context (reference):   <path to app-context skill>
Domain Rules (reference):          <path to domain-rules skill>
Business Event Knowledge (ref):    <path to business-events skill>
Feature-Specific Domain Behavior:  <text derived from spec.md and approval>
```

---

## Applicable Architecture Rules

Reference the active domain architecture skill for structural/integration constraints applicable to this sprint.

Do not re-author architecture rules here.

Example structure:

```text
Architecture Rules (reference):    <path to architecture-rules skill>
Approved Interaction Mechanism:    <mechanism approved via Planner discovery, or Not Applicable>
Feature-Specific Constraints:      <text derived from spec.md and approval>
```

Implementation-mechanism neutrality applies: mechanism prescriptions here must be justified by resolved architecture and approved discovery.

---

## Acceptance Criteria

Each acceptance criterion must have a unique identifier and must be measurable and independently evaluable.

Prefer GIVEN / WHEN / THEN where suitable.

Example structure:

```text
AC ID:            <AC-CONTRACT_ID-001>
Description:      <short observable outcome>
GIVEN:            <precondition>
WHEN:             <trigger>
THEN:             <observable outcome>
Mandatory:        <Yes/No>
Positive/Negative: <positive|negative>
Applicable Domain / Architecture References: <list>
Feature Evidence Expectations: <list>
```

Repeat for each acceptance criterion.

Negative-path acceptance criteria must be represented explicitly where applicable to the feature.

---

## Required Tests

List test coverage expectations required by this contract.

Test-framework selection, coverage thresholds, and hard-gate enforcement remain configuration- and governance-owned.

Example structure:

```text
Test Expectation:       <text>
Type:                   <unit/integration/api-controller/architecture/security/regression/other>
Applies to AC:          <list of AC IDs>
Existing Application Convention Reference: <text>
Deterministic Time Handling Required: <Yes/No>
```

Do not enumerate specific tools or thresholds here.

---

## Hard Gates (Reference)

Reference the active governance policy for the authoritative hard-gate set applicable to this sprint.

Example structure:

```text
Hard Gate Source:          <path/to/default-governance.yaml>
Applicable Hard Gates:     <named references resolved from governance policy>
Sprint-Specific Emphasis:  <text summarizing which gates the sprint particularly stresses>
```

Do not restate authoritative thresholds. If the baseline explicitly requires a resolved hard-gate snapshot, populate it exactly from the resolved governance policy.

---

## Verification Expectations

Describe the deterministic verification expected during Generator execution and Evaluator independent verification.

Example structure:

```text
Verification Command Source: <resolved technology profile>
Applicable Configured Checks:
  - Build
  - Tests
  - Coverage        (if configured)
  - Static Analysis (if configured)
  - Architecture    (if configured)
  - Security        (if configured)
Evaluator Order Reference:   core/evaluation-framework/SKILL.md
```

Do not embed specific commands or tool selection in the contract.

---

## Completion Conditions

Describe conditions under which this sprint is considered complete.

Verdict semantics remain owned by Evaluator and governance policy.

Example structure:

```text
Required Verdict For Completion:  PASS or CONDITIONAL_PASS as permitted by governance policy
Required Acceptance Criteria:     <list of mandatory AC IDs>
Required Evidence Artifacts:      generator-summary-iteration-N.md
                                  evaluator-feedback-iteration-N.md
                                  run-log.md
Non-Blocking Findings Permitted:  <Yes/No, per governance policy>
```

Do not encode `maxIterations`, retry authorization, escalation policy, or run-completion behavior here.

---

## Contract Change Handling

If Generator or Evaluator determines that a material contract change is required, the canonical `CONTRACT_CHANGE_REQUIRED` behavior established in `CLAUDE.md` applies.

Do not silently expand scope in this contract during implementation.

---

## Traceability

Link this contract to authoritative sources.

Example structure:

```text
PROMPT.md:                   <path>
spec.md:                     <path>
Application Configuration:   <application profile id>
Technology Configuration:    <technology profile id>
Governance Policy:           <governance policy id>
Domain Skills Consulted:     <list of skill paths>
Approved Human Decisions:    <references>
```
