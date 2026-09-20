# PROMPT.md

# StoreOps SLA Breach Alerting

**Artifact Type:** Demonstration Feature Prompt  
**Reference Application:** StoreOps  
**Feature:** SLA Breach Alerting  
**Version:** 2.0  
**Status:** Ready for Planner Invocation

---

## 1. Purpose

Implement **SLA Breach Alerting** for the StoreOps reference application using the governed agentic delivery harness.

This prompt defines the **business intent and expected outcomes** of the feature.

It does not define reusable harness orchestration, technology-specific implementation rules, governance thresholds, evaluation policy, deployment controls, or application architecture.

Those concerns are resolved through:

```text
CLAUDE.md
.harness/config/
.harness/skills/
Existing StoreOps Application
Approved Sprint Contracts
```

The Planner must inspect the active StoreOps configuration, domain rules, architecture rules, existing source, tests, and application conventions before determining the implementation approach.

---

## 2. Business Requirement

StoreOps activities may have due dates and priorities.

When an eligible activity becomes overdue and remains unresolved, StoreOps must identify the condition as an SLA breach and notify the appropriate operational role.

If the breach remains unresolved after the configured grace period, StoreOps must escalate the breach to a Store Manager.

---

## 2.1 SLA Breach Eligibility

An activity is eligible for SLA breach processing when:

```text
Priority = HIGH or CRITICAL
        +
Due Date/Time Has Passed
        +
Activity Remains Unresolved
```

The Planner must determine the precise meaning of **unresolved** from the existing StoreOps domain model and application behavior.

The implementation must not silently invent a new activity-resolution model when an existing StoreOps convention already exists.

---

## 2.2 Department Lead Notification

When an eligible SLA breach is detected:

```text
Eligible SLA Breach
        ↓
Resolve Assigned Department Lead
        ↓
Notify Department Lead
```

The notification must contain sufficient business context for the Department Lead to understand and act on the breached activity.

The Planner must determine:

- how the Department Lead is associated with the activity;
- how the Department Lead is resolved from existing StoreOps data;
- what notification mechanism already exists;
- what information the existing notification model requires; and
- how duplicate notifications are prevented where applicable.

---

## 2.3 Store Manager Escalation

After the Department Lead has been notified:

```text
SLA Breach
        ↓
Department Lead Notification
        ↓
Configured Grace Period
        ↓
Still Unresolved?
   /             \
 NO               YES
 │                 │
 v                 v
No Store       Resolve Store
Manager        Manager
Escalation         ↓
              Escalate Breach
```

If the breached activity is resolved before the configured grace period expires:

```text
No Store Manager Escalation
```

If the activity remains unresolved after the configured grace period:

```text
Store Manager Escalation
```

The Planner must determine how the Store Manager is resolved using existing StoreOps roles, relationships, and application conventions.

---

## 2.4 Configurable Grace Period

The escalation grace period must be configurable.

The feature must not depend on a hard-coded grace-period value unless the existing StoreOps application already defines an approved configuration mechanism that resolves the value.

The Planner must determine:

- where the grace period should be configured;
- how the value is represented;
- how it is accessed by the application; and
- how time-based behavior can be tested deterministically.

---

## 2.5 Negative Paths

The following conditions must not create an SLA breach:

```text
Priority = LOW
```

```text
Priority = MEDIUM
```

```text
Activity Status = DONE
```

```text
Activity Is Not Overdue
```

Equivalent existing StoreOps states representing a resolved activity must also prevent breach processing where applicable.

The Planner must confirm the actual StoreOps status model before finalizing acceptance criteria.

---

## 3. Expected Business Outcomes

The completed feature must demonstrate the following observable outcomes.

| Scenario | Expected Outcome |
|---|---|
| Overdue `HIGH` activity that remains unresolved | SLA breach is detected |
| Overdue `CRITICAL` activity that remains unresolved | SLA breach is detected |
| Eligible SLA breach | Assigned Department Lead is notified |
| Breach remains unresolved after configured grace period | Store Manager is notified/escalated |
| Breach is resolved before configured grace period | No Store Manager escalation |
| `LOW` priority activity | No SLA breach |
| `MEDIUM` priority activity | No SLA breach |
| `DONE` activity | No SLA breach |
| Activity is not overdue | No SLA breach |

The implementation must preserve these business outcomes regardless of the approved technical mechanism used to implement them.

---

## 4. Planner Discovery Requirements

Before producing the implementation specification and sprint contracts, the Planner must inspect the active StoreOps application and resolve implementation-relevant uncertainties from repository evidence.

The Planner must not silently invent material application behavior.

---

## 4.1 Breach-Detection Trigger

Determine how StoreOps should evaluate activities for SLA breaches.

Prefer an existing StoreOps mechanism where one exists.

The detection trigger is determined by Planner discovery from the active StoreOps configuration, domain rules, and existing application patterns.

Document whether detection is triggered through an existing:

```text
Application Operation
Scheduled Process
Domain/Event Flow
Service Operation
Existing Background Mechanism
Other Approved StoreOps Pattern
```

Do not introduce a new scheduling, messaging, or processing mechanism when an existing StoreOps mechanism already satisfies the requirement.

---

## 4.2 Grace-Period Configuration

Determine:

```text
Configuration Source
Configuration Key
Value Type
Default Behavior
Validation Rules
```

for the configured grace period.

The Planner must prefer existing StoreOps configuration conventions.

---

## 4.3 Meaning of Unresolved

Determine how StoreOps represents an unresolved activity.

Inspect:

```text
Activity Status
Completion State
Resolution State
Existing Domain Rules
Existing Tests
```

Do not assume that `DONE` is the only possible resolved state unless repository evidence confirms it.

---

## 4.4 Department Lead Resolution

Determine how the assigned Department Lead is resolved.

Inspect existing:

```text
Activity Relationships
Department Relationships
Staff Model
Role Model
Assignment Rules
Service Interfaces
```

Use existing StoreOps relationships where available.

---

## 4.5 Store Manager Resolution

Determine how the Store Manager is resolved for escalation.

Inspect existing:

```text
Staff Roles
Store Relationships
Role Constants
Assignment Rules
Service Interfaces
```

Use the existing StoreOps `STORE_MANAGER` role semantics where applicable.

---

## 4.6 Duplicate and Idempotency Behavior

Determine how StoreOps prevents repeated processing of the same breach.

The Planner must consider:

```text
Repeated Breach Detection
Repeated Department Lead Notification
Repeated Store Manager Escalation
Application Restart
Repeated Evaluation
Concurrent Processing
```

Reuse existing StoreOps idempotency or state-tracking patterns where available.

---

## 4.7 Time Handling

Determine the existing StoreOps convention for:

```text
Current Time
Due Date/Time
Time Zone
Clock Abstraction
Scheduled Evaluation
Time-Based Testing
```

Prefer deterministic and testable time handling.

Do not introduce unnecessary time abstractions if the application already provides an approved mechanism.

---

## 4.8 Breach State Tracking

Determine whether StoreOps already provides sufficient state to identify:

```text
Breach Detected
Department Lead Notified
Grace Period Started
Breach Resolved
Store Manager Escalated
```

Introduce additional state only when required by the approved design.

---

## 5. Architecture Discovery

The feature prompt does not define the StoreOps implementation architecture.

The Planner must resolve applicable architecture rules from:

```text
Active Application Configuration
StoreOps Domain Skills
StoreOps Architecture Rules
Existing Source
Existing Tests
Existing Application Patterns
```

The Planner must preserve existing StoreOps architecture unless an approved contract explicitly requires a change.

---

## 6. Sprint Decomposition

Sprint decomposition is determined by the Planner.

The Planner must decompose the feature into independently verifiable sprint contracts based on:

```text
Repository Evidence
Business Dependencies
Architecture Boundaries
Existing StoreOps Patterns
Testability
Risk
```

A design or planning artifact may contain an illustrative decomposition, but that decomposition does not override Planner discovery.

The Planner may choose different sprint boundaries when repository evidence supports a better decomposition.

Each sprint must remain:

- bounded in scope;
- independently testable;
- traceable to the feature intent;
- architecture-aware; and
- suitable for independent Generator/Evaluator execution.

---

## 7. Acceptance Criteria

The Planner must convert this feature intent into explicit acceptance criteria.

Acceptance criteria must:

- have unique identifiers;
- describe observable behavior;
- include positive paths;
- include negative paths;
- identify applicable architecture constraints;
- identify required tests;
- avoid prescribing unnecessary implementation details; and
- be independently evaluable.

Use:

```text
GIVEN
WHEN
THEN
```

where appropriate.

---

## 8. Testing Intent

The implementation must include automated tests sufficient to demonstrate the approved business behavior.

Tests should cover, where applicable:

```text
HIGH Priority Breach
CRITICAL Priority Breach
LOW Priority Negative Path
MEDIUM Priority Negative Path
DONE Activity Negative Path
Non-Overdue Negative Path
Department Lead Notification
Grace-Period Behavior
Store Manager Escalation
Resolution Before Escalation
Duplicate / Idempotency Behavior
Time-Based Behavior
Error Behavior
```

Exact test frameworks, coverage thresholds, commands, and quality gates are resolved from the active technology and governance configuration.

This prompt does not define those values.

---

## 9. Existing Application Compatibility

The implementation must integrate with the existing StoreOps application rather than create a parallel application model.

The Planner and Generator must prefer existing:

```text
Domain Models
Services
Repositories
Events
Configuration Patterns
Error Handling
Role Definitions
Testing Conventions
Module Boundaries
```

where they satisfy the approved contract.

Avoid unnecessary:

- framework changes;
- package restructuring;
- new dependencies;
- duplicate domain models;
- duplicate role models;
- duplicate notification mechanisms; and
- unrelated refactoring.

---

## 10. Scope Control

The feature scope is limited to the behavior required to demonstrate SLA Breach Alerting.

Do not expand the feature into unrelated capabilities such as:

```text
General Workflow Engine
Generic Rules Engine
Enterprise Notification Platform
New Identity Platform
New Scheduling Platform
New Event Platform
StoreOps-Wide Refactoring
```

unless repository evidence demonstrates that an existing approved mechanism must be extended to satisfy the feature.

Material scope changes require explicit review through the governed harness workflow.

---

## 11. Harness Integration

This prompt defines:

```text
WHAT business outcome is required
```

The governed harness determines:

```text
HOW the feature is planned
HOW configuration is resolved
HOW architecture rules are applied
HOW implementation is generated
HOW implementation is evaluated
HOW retries are controlled
HOW evidence is preserved
HOW deployment is governed
```

The following remain authoritative for engineering and governance behavior:

```text
CLAUDE.md
Active Harness Configuration
Application Configuration
Technology Configuration
Governance Policy
Domain Skills
Technology Skills
Approved Sprint Contract
```

If this prompt conflicts with a mandatory governance or architecture rule:

```text
Mandatory Governance / Architecture Rule Wins
```

The conflict must be surfaced rather than silently ignored.

---

## 12. Required Planner Outputs

The Planner must convert this prompt into:

```text
spec.md
sprint-N-contract.md
```

The specification should capture:

```text
Business Objective
Scope
Out of Scope
Impacted Components
Repository Findings
Assumptions
Open Questions
Architecture Constraints
Sprint Decomposition
Dependencies
Risks
```

Each sprint contract should capture:

```text
Run ID
Sprint ID
Contract ID
Objective
Scope
Out of Scope
Dependencies
Assumptions
Applicable Architecture Rules
Acceptance Criteria
Required Tests
Hard Gates
Completion Conditions
```

The Planner must not begin implementation.

---

## 13. Human Approval

After producing the specification and sprint contracts, Planner must stop with:

```text
STATUS: AWAITING_APPROVAL
```

Implementation begins only after the configured human approval command is received.

The default approval command is:

```text
APPROVED
```

If Planner discovery identifies a material ambiguity that cannot be safely resolved from repository evidence, the ambiguity must be surfaced for human review rather than silently assumed.

Material changes to an approved contract require re-approval through the governed harness workflow.

---

## 14. Completion Intent

The feature is complete when the approved implementation demonstrates the following business flow:

```text
StoreOps Activity
        ↓
Priority HIGH / CRITICAL?
       / \
     NO   YES
     │     │
     │     v
     │   Overdue?
     │    / \
     │  NO   YES
     │  │     │
     │  │     v
     │  │  Unresolved?
     │  │    / \
     │  │  NO   YES
     │  │  │     │
     │  │  │     v
     │  │  │  SLA Breach
     │  │  │     ↓
     │  │  │  Department Lead
     │  │  │  Notification
     │  │  │     ↓
     │  │  │  Configured Grace Period
     │  │  │     ↓
     │  │  │  Still Unresolved?
     │  │  │      /       \
     │  │  │    NO         YES
     │  │  │    │           │
     │  │  │    v           v
     │  │  │   Stop      Store Manager
     │  │  │             Escalation
     │  │  │
     v  v  v
   No SLA Breach
```

The implementation mechanism (event-driven, service-mediated, or other approved StoreOps pattern), architecture, testing, evaluation, deployment, rollback, and governance mechanisms used to achieve these outcomes must be determined by the active configuration, approved contracts, existing StoreOps design, and governed harness workflow.

**This prompt does not prescribe the implementation mechanism.**

The final implementation must preserve the business intent while remaining compliant with the active StoreOps architecture and governed harness controls.