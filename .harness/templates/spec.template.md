# Specification Template

Runtime instantiation: `.harness/output/<run-id>/spec.md`

Do not treat this template as an authoritative source of governance thresholds, technology commands, application/domain rules, orchestration statuses, deployment controls, or reflection.

---

## Metadata

```text
Run ID:            <RUN_ID>
Feature Prompt:    <path/to/PROMPT.md>
Application:       <resolved application id (for example, storeops)>
Technology:        <resolved technology profile id (for example, java-spring)>
Governance Policy: <resolved governance policy id (for example, default-governance)>
Author Agent:      Planner
Status:            AWAITING_APPROVAL
Baseline:          v2.0
```

---

## Feature

Describe the feature at a business-intent level. Do not restate implementation.

Reference:

```text
PROMPT.md
```

---

## Business Objective

Describe the business outcome the feature is intended to enable.

State outcomes as observable behavior rather than implementation.

---

## Scope

Describe the intended in-scope behavior.

---

## Out of Scope

Describe behavior that is intentionally excluded from this feature or from the current run.

---

## Impacted Components

List modules/components that appear impacted based on Planner discovery of the resolved application context.

Reference the applicable application/domain skills for module concepts, not this specification.

Example structure:

```text
Component:       <module or component name from application context>
Responsibility:  <brief responsibility relevant to this feature>
Discovery Basis: <repository / configuration / domain skill / approved intent>
```

Do not invent components that are not confirmed by the resolved application context.

---

## Discovered Application and Domain Context

Summarize the Planner discovery relevant to this feature.

Use configuration- and domain-owned values by reference, not by re-defining them.

Example structure:

```text
Application Context Findings: <summary>
Domain Rule Findings:         <summary>
Architecture Rule Findings:   <summary>
Business Event Findings:      <summary>
Existing Application Patterns Reused: <summary>
```

If a mechanism (event-driven, service-mediated, scheduled, or other) is required by the approved architecture, record the determination here. If discovery is inconclusive, capture it as an Open Question, not as an assumption.

---

## Assumptions

List material assumptions that were made during Planner discovery.

Example structure:

```text
Assumption:            <text>
Basis:                 <repository / configuration / domain / feature intent>
Impact if False:       <text>
Affected Acceptance Criteria: <AC IDs>
Requires Approval:     <Yes/No>
```

Do not use assumptions to silently resolve material ambiguity that requires human decision.

---

## Open Questions

List unresolved material questions that must be answered before or during approval.

Example structure:

```text
Question:                     <text>
Proposed Assumption (if any): <text>
Rationale:                    <text>
Impact:                       <text>
Affected Acceptance Criteria: <AC IDs>
Decision Required From Human: <Yes/No>
Blocking:                     <Yes/No>
```

Blocking Open Questions must be resolved before implementation begins.

---

## Applicable Architecture Constraints

Reference the active application/domain architecture constraints applicable to this feature.

Do not re-author architecture rules in this specification.

Example structure:

```text
Constraint Source:  <domain architecture skill / configuration / approved contract>
Applicable Rules:   <summary references to authoritative rules>
Feature Implications: <text>
```

---

## Dependencies

List external or internal dependencies discovered during Planner analysis.

Example structure:

```text
Dependency:                 <text>
Nature (technical/domain/data/configuration/other): <text>
Status:                     <existing/needs-verification/needs-decision>
Impact if Unavailable:      <text>
```

---

## Risks

List material risks identified during Planner analysis.

Example structure:

```text
Risk:        <text>
Likelihood:  <low/medium/high>
Impact:      <low/medium/high>
Mitigation:  <text>
Owner:       <role or agent>
```

Do not turn risks into speculative implementation decisions.

---

## Sprint Decomposition

Sprint decomposition is Planner-derived. Do not treat any illustrative decomposition from design or planning documents as authoritative.

Each sprint entry must be bounded, independently testable/evaluable, architecture-aware, and traceable to the feature.

Example structure:

```text
Sprint ID:       <SPRINT-01>
Objective:       <text>
Scope Summary:   <text>
Rationale:       <why this sprint boundary was chosen>
Dependencies:    <other sprints or discoveries>
Contract Ref:    sprint-<SPRINT-ID>-contract.md
```

Repeat for each derived sprint.

---

## Traceability

Show traceability of this specification to the feature prompt and approved sources.

Example structure:

```text
PROMPT.md:                   <path>
Application Configuration:   <application profile id>
Technology Configuration:    <technology profile id>
Governance Policy:           <governance policy id>
Domain Skills Consulted:     <list of skill paths>
Repository Evidence:         <references>
Approved Human Decisions:    <references or Pending>
```

---

## Planner Completion

Mark completion using the canonical status marker:

```text
STATUS: AWAITING_APPROVAL
```

Do not proceed to implementation until the approval mechanism defined by the active harness configuration is satisfied.
