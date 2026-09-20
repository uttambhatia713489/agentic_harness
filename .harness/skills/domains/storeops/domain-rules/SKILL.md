\# StoreOps Domain Rules



\## Purpose



Provide reusable StoreOps business/domain knowledge to the harness agents that consume domain context: Planner, Generator, and Evaluator.



This skill describes permanent StoreOps domain concepts and rules that apply across features.



It does not encode:



\- feature-specific business intent;

\- feature-specific eligibility rules;

\- feature-specific notification or escalation recipients;

\- feature-specific workflows;

\- feature-specific state transitions;

\- feature-specific timing behavior;

\- feature-specific configuration;

\- architecture mechanisms;

\- technology-specific implementation details;

\- governance policy; or

\- harness orchestration.



Feature-specific behavior is supplied through:



```text

PROMPT.md

&#x20;   ↓

Planner Discovery

&#x20;   ↓

spec.md

&#x20;   ↓

Approved Sprint Contract

```



\---



\## Scope



These rules apply when the resolved application/domain context identifies StoreOps as the active application.



They must be interpreted together with:



```text

StoreOps Application Context

StoreOps Architecture Rules

StoreOps Business Events

Existing StoreOps Source

Existing StoreOps Tests

Resolved Application Configuration

Active Technology Profile

Active Governance Policy

Approved Sprint Contract

```



Repository and resolved application evidence remain authoritative for the actual StoreOps domain model.



Where an approved sprint contract applies these domain concepts to a specific feature, the approved sprint contract governs that feature implementation.



\---



\## Domain-Knowledge Boundary



This skill contains:



```text

Permanent StoreOps Domain Concepts

Permanent StoreOps Business Semantics

Reusable StoreOps Domain Vocabulary

Existing StoreOps Lifecycle Concepts

Existing StoreOps Role Concepts

Existing StoreOps Time-Dependent Concepts

```



This skill must not promote feature-specific requirements into permanent StoreOps domain truth unless those requirements are explicitly established by the authoritative StoreOps application/domain baseline.



\---



\## Application Domain



StoreOps supports store operations involving concepts such as:



```text

Activities

Programmes

Staff

Alerts

Reports

```



These concepts represent the existing StoreOps business domain.



Additional domain concepts may exist in the current StoreOps application and must be discovered from the resolved application context and repository.



Planner must inspect the existing StoreOps implementation before introducing or assuming additional domain semantics.



\---



\## Activities



Activities represent operational units of work in StoreOps.



Activities may contain domain attributes such as:



```text

Owner / Assignee

Department

Priority

Due Date / Time

Lifecycle Status

Audit Information

```



The exact:



```text

Attribute Names

Data Types

Allowed Values

Lifecycle Semantics

Ownership Semantics

```



are determined by the current StoreOps application.



This skill does not redefine the existing Activity model.



\---



\## Programmes



Programmes represent StoreOps concepts that group or organize related operational work where supported by the current application.



The exact relationship between:



```text

Programme

Activity

Staff

Department

```



must be derived from the existing StoreOps domain model.



This skill does not invent programme lifecycle rules or feature-specific programme behavior.



\---



\## Staff



Staff represent people participating in StoreOps operational processes.



Staff may be associated with:



```text

Roles

Departments

Activities

Programmes

Operational Responsibilities

```



where those relationships exist in the current StoreOps application.



The exact staff model and authorization semantics are determined by the resolved application context.



This skill does not redefine identity, authentication, authorization, or RBAC implementation.



\---



\## Alerts



Alerts represent operational notifications or alert records where supported by the current StoreOps application.



The exact:



```text

Alert Types

Alert States

Recipients

Lifecycle

Persistence Semantics

Delivery Semantics

```



are determined by the existing StoreOps application and the approved feature contract.



This skill does not assume that every business condition produces an Alert.



Feature-specific alert creation, notification recipients, escalation recipients, or delivery behavior must be defined through:



```text

PROMPT.md

&#x20;   ↓

Planner Discovery

&#x20;   ↓

Approved Sprint Contract

```



\---



\## Reports



Reports represent read-oriented views of StoreOps operational information.



Business semantics associated with reports are derived from the current StoreOps application.



Architectural constraints governing whether the Reports module is read-only belong to:



```text

domains/storeops/architecture-rules/SKILL.md

```



This skill does not redefine Reports architecture.



\---



\## Priorities



StoreOps activities may use priority classifications such as:



```text

LOW

MEDIUM

HIGH

CRITICAL

```



where those values are confirmed by the current StoreOps application.



The exact supported priority set and semantics remain application-defined.



Higher priority values may represent greater operational significance where established by the StoreOps domain.



This skill does not attach feature-specific behavior to a priority.



In particular, this skill does not define:



```text

SLA Eligibility

Notification Eligibility

Escalation Eligibility

Processing Order

Timing Thresholds

```



based solely on priority.



Feature-specific interpretation of priority is supplied by the feature prompt, Planner discovery, and approved sprint contract.



\---



\## Activity Lifecycle



StoreOps activities may progress through an application-defined lifecycle.



The lifecycle may include statuses such as:



```text

DONE

```



where confirmed by the current StoreOps application.



The exact:



```text

Supported Statuses

Allowed Transitions

Completion Semantics

Resolution Semantics

Terminal States

```



are determined by the existing StoreOps domain model.



This skill must not redefine those semantics.



\---



\## Completed and Unresolved Semantics



Where the current StoreOps application establishes `DONE` as a completed state, that existing meaning must be preserved.



This skill does not automatically define:



```text

status != DONE

```



as the universal meaning of:



```text

Unresolved

Active

Open

SLA Eligible

Escalatable

```



for every feature.



Feature-specific interpretation of completed or unresolved state must be determined from:



```text

Feature Requirements

Existing StoreOps Lifecycle

Planner Discovery

Approved Sprint Contract

```



\---



\## Roles



StoreOps recognizes operational roles used across the application.



Examples may include:



```text

Department Lead

Store Manager

STORE\_MANAGER

```



where those roles are confirmed by the current StoreOps application.



The exact:



```text

Role Names

Role Identifiers

Role Responsibilities

Authorization Semantics

```



are determined by the resolved StoreOps application context.



This skill describes reusable role concepts only.



It does not define feature-specific role bindings such as:



```text

Notification Recipient

Escalation Recipient

Approval Role

Feature Workflow Participant

```



Feature-specific role bindings must be determined through Planner discovery and captured in the approved sprint contract.



\---



\## Time Awareness



StoreOps contains time-dependent domain concepts such as:



```text

Due Dates

Due Times

Scheduled Work

Creation Timestamps

Update Timestamps

Completion Timestamps

```



where supported by the current application.



The exact:



```text

Date/Time Representation

Time Zone

Clock Abstraction

Timestamp Semantics

```



must be derived from the existing StoreOps application.



This skill does not define:



```text

Scheduler

Polling

Timer

Grace Period

Escalation Timer

SLA Calculation

Time Zone

Clock Implementation

```



for a feature.



Feature-specific time semantics must be determined through Planner discovery and captured in the approved sprint contract.



\---



\## Configuration-Dependent Domain Behavior



StoreOps behavior may depend on application configuration.



The exact:



```text

Configuration Surface

Configuration Keys

Configuration Values

Default Values

Environment Overrides

```



are determined by the resolved application configuration.



This skill does not define feature-specific:



```text

Thresholds

Grace Periods

Timers

Retry Counts

Escalation Delays

Configuration Keys

Default Values

```



unless those values are explicitly established as permanent StoreOps domain semantics.



\---



\## Feature-Specific Business Rules



Feature-specific business rules must remain outside this reusable domain skill unless explicitly established as permanent StoreOps domain behavior.



Examples include:



```text

Priority Eligibility

Overdue Conditions

Unresolved Conditions

Notification Recipients

Escalation Recipients

Configured Grace Period

Duplicate Prevention

Idempotency

State Tracking

Time-Based Processing

```



For example, SLA Breach Alerting requirements involving:



```text

HIGH / CRITICAL Eligibility

Overdue Activities

Unresolved Activities

Department Lead Notification

Store Manager Escalation

Configured Grace Period

```



must not automatically become permanent StoreOps domain rules.



They remain feature-specific and flow through:



```text

PROMPT.md

&#x20;   ↓

Planner Discovery

&#x20;   ↓

spec.md

&#x20;   ↓

Approved Sprint Contract

&#x20;   ↓

Generator Implementation

&#x20;   ↓

Evaluator Verification

```



\---



\## Duplicate and Idempotency Semantics



This skill does not assume a universal duplicate-prevention or idempotency model for StoreOps.



Where a feature requires duplicate prevention or idempotent behavior, Planner must determine the applicable semantics from:



```text

Feature Requirements

Existing StoreOps Behavior

Repository Evidence

Existing Tests

Resolved Configuration

```



The approved sprint contract must capture the required behavior.



This skill must not invent:



```text

Deduplication Keys

Idempotency Keys

Duplicate Windows

Retry Counts

Exactly-Once Semantics

At-Least-Once Semantics

```



unless those concepts are established by the current StoreOps application.



\---



\## State Tracking



This skill does not prescribe feature-specific state-tracking mechanisms.



Where a feature requires tracking state such as:



```text

Notification Sent

Escalation Sent

Condition Detected

Condition Resolved

Processing Completed

```



Planner must determine whether such state already exists and how it is represented.



The approved sprint contract must capture any required feature-specific state behavior.



This skill must not invent new persistent state merely because a feature may require duplicate prevention or workflow tracking.



\---



\## Existing Application Preservation



Planner and Generator must inspect existing StoreOps domain behavior before introducing new domain semantics.



Prefer:



```text

Existing StoreOps Concepts

Existing StoreOps Vocabulary

Existing StoreOps Role Usage

Existing StoreOps Lifecycle Rules

Existing StoreOps Time Handling

Existing StoreOps Configuration Patterns

Existing StoreOps State Semantics

```



over introducing new domain concepts.



A new domain concept must not be introduced merely because it is technically convenient.



Feature-specific extensions must be captured through:



```text

PROMPT.md

&#x20;   ↓

Planner Discovery

&#x20;   ↓

Approved Sprint Contract

```



and must not be promoted into this skill unless explicitly established as reusable StoreOps domain behavior.



\---



\## Planner Usage



Planner uses this skill to understand existing StoreOps domain concepts and semantics during discovery.



Planner must combine this skill with:



```text

Feature Prompt

Resolved Application Configuration

Existing Source

Existing Tests

StoreOps Application Context

StoreOps Architecture Rules

StoreOps Business Events

```



Planner must:



\- preserve existing StoreOps terminology;

\- identify applicable domain concepts;

\- identify feature-specific business rules;

\- identify assumptions and open questions;

\- discover unresolved lifecycle, role, time, configuration, duplicate, and state semantics; and

\- capture approved feature-specific behavior in the sprint contract.



Planner must not treat this skill as a substitute for repository discovery or feature intent.



\---



\## Generator Usage



Generator uses this skill to preserve StoreOps domain semantics while implementing an approved sprint contract.



Generator must:



\- implement only approved feature behavior;

\- preserve existing StoreOps domain concepts;

\- preserve existing lifecycle semantics;

\- preserve approved role semantics;

\- preserve approved time semantics;

\- preserve approved configuration behavior; and

\- avoid inventing additional business rules.



Generator must not use this skill to expand feature scope.



If implementation requires a material change to approved domain behavior, Generator must use the applicable contract-change workflow.



\---



\## Evaluator Usage



Evaluator uses this skill to verify that implementation preserves applicable StoreOps domain semantics and satisfies the approved sprint contract.



Evaluator may verify:



```text

Domain Concept Usage

Priority Semantics

Lifecycle Semantics

Role Semantics

Time Semantics

Configuration-Dependent Behavior

Feature-Specific Domain Behavior from the Approved Contract

```



where applicable.



Evaluator must not:



\- introduce new StoreOps domain requirements;

\- promote feature-specific behavior into permanent domain truth;

\- reinterpret the approved sprint contract; or

\- fail an implementation for behavior that was never required by the resolved StoreOps context or approved contract.



\---



\## Monitor Usage



Monitor does not require this skill for governance-observability evidence recording.



Monitor records canonical execution evidence and preserves Evaluator verdicts without interpreting StoreOps domain semantics.



\---



\## Architecture Boundary



StoreOps architecture constraints belong to:



```text

domains/storeops/architecture-rules/SKILL.md

```



This skill must not redefine:



```text

Controller → Service → Repository Layering

Module Boundaries

Cross-Module Repository Rules

Cross-Module Read Rules

Cross-Module Side-Effect Mechanisms

Reports Read-Only Architecture

Error Contracts

Approved Integration Mechanisms

```



Domain rules describe business meaning.



Architecture rules describe structural and interaction constraints.



\---



\## Business-Events Boundary



StoreOps business-event knowledge belongs to:



```text

domains/storeops/business-events/SKILL.md

```



This skill must not require:



```text

SLA\_BREACH

Event Bus

Specific Domain Event

Publisher

Consumer

Event Payload

Delivery Semantics

```



as part of permanent StoreOps domain behavior.



Feature-specific event behavior is determined through Planner discovery and the approved sprint contract.



\---



\## Technology Boundary



This skill does not prescribe technology-specific implementation.



It must not define:



```text

Java

Spring Boot

Spring Events

Kafka

Azure Service Bus

Database Technology

Framework Classes

Annotations

Serialization Libraries

Build Tools

Test Frameworks

```



Technology-specific guidance belongs to the active technology profile and technology skills.



\---



\## Governance Boundary



This skill does not define:



```text

Hard Gates

Coverage Thresholds

Scoring Dimensions

Scoring Weights

Verdict Thresholds

Fail-Closed Policy

maxIterations

Retry Policy

Escalation Policy

```



Those values and policies are supplied by the active governance configuration.



\---



\## Orchestration Boundary



This skill does not define or emit harness orchestration states such as:



```text

STATUS: AWAITING\_APPROVAL

STATUS: READY\_FOR\_EVALUATION

STATUS: CONTRACT\_CHANGE\_REQUIRED

STATUS: COMPLETED

```



It does not define:



```text

PASS

CONDITIONAL\_PASS

FAIL

```



verdict semantics.



Agent definitions, `CLAUDE.md`, and the active governance policy remain authoritative for orchestration and evaluation behavior.



\---



\## Evidence Boundary



This skill does not define runtime evidence filenames, output paths, or review/archive paths.



Canonical evidence naming and locations remain controlled by the harness baseline and resolved configuration.



This skill does not redefine:



```text

generator-summary-iteration-N.md

evaluator-feedback-iteration-N.md

run-log.md

escalation.md

```



as domain concepts.



\---



\## Responsibility Boundaries



This skill provides reusable StoreOps domain knowledge.



It does not:



\- prescribe feature-specific business rules;

\- prescribe feature-specific eligibility;

\- prescribe feature-specific notification recipients;

\- prescribe feature-specific escalation recipients;

\- prescribe implementation mechanisms;

\- redefine StoreOps architecture rules;

\- redefine StoreOps business-event rules;

\- prescribe technology-specific implementation;

\- prescribe governance thresholds;

\- prescribe scoring rules;

\- prescribe iteration limits;

\- prescribe evidence naming;

\- prescribe orchestration states;

\- authorize retries;

\- authorize escalation;

\- authorize contract changes;

\- deploy software;

\- perform rollback; or

\- perform reflection.



The responsibility flow remains:



```text

PROMPT.md

&#x20;   ↓

Planner Discovery

&#x20;   ↓

Approved Sprint Contract

&#x20;   ↓

Generator Implementation

&#x20;   ↓

Evaluator Verification

&#x20;   ↓

Monitor Evidence Recording

```



\---



\## Configuration Ownership



This skill must not duplicate authoritative values owned elsewhere.



Configuration- or governance-owned examples include:



```text

Application Paths

Technology Stack

Build Commands

Test Commands

Coverage Commands

Coverage Thresholds

Static Analysis Commands

Architecture Verification Commands

Security Commands

Hard Gates

Scoring Dimensions

Scoring Weights

Verdict Thresholds

Fail-Closed Policy

maxIterations

Approval Commands

Output Paths

Review Paths

Deployment Controls

Rollback Controls

```



This skill defines:



```text

Permanent StoreOps Domain Concepts and Rules

```



The StoreOps architecture-rules skill defines:



```text

StoreOps Architectural Constraints

```



The StoreOps business-events skill defines:



```text

StoreOps Business-Event Knowledge

```



Configuration defines:



```text

Applicable Runtime Values

```



Governance policy defines:



```text

Evaluation and Decision Policy

```



The approved sprint contract defines:



```text

Feature-Specific Approved Scope

\+

Applicable StoreOps Domain Behavior

```

