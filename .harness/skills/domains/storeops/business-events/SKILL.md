\# StoreOps Business Events



\## Purpose



Provide reusable StoreOps business-event knowledge to the harness agents that consume domain context: Planner, Generator, and Evaluator.



This skill describes business events that are established by the current StoreOps application/domain context.



It does not:



\- require a feature to use an event-driven implementation;

\- define feature-specific business rules;

\- define architecture constraints;

\- define technology-specific event infrastructure;

\- define governance policy; or

\- treat a feature-specific event as permanent StoreOps domain truth unless it is established by the current application or approved through the sprint contract.



Feature-specific event behavior is determined through:



```text

PROMPT.md

&#x20;   ↓

Planner Discovery

&#x20;   ↓

Approved Sprint Contract

```



\---



\## Scope



These rules apply when the resolved application/domain context identifies StoreOps as the active application.



Business-event knowledge must be interpreted together with:



```text

StoreOps Application Context

StoreOps Domain Rules

StoreOps Architecture Rules

Existing StoreOps Source

Existing StoreOps Tests

Resolved Application Configuration

Approved Sprint Contract

```



This skill describes:



```text

StoreOps Business-Event Knowledge

```



It does not independently select an implementation mechanism.



\---



\## Business-Event Boundary



A StoreOps business event represents a meaningful business occurrence recognized by the current application/domain.



Examples may include occurrences such as:



```text

Task Created

Task Completed

Task Blocked

Programme Closed

```



The exact event names, identifiers, payloads, publishers, consumers, and semantics are determined by the current StoreOps application.



This skill must not cause Planner, Generator, or Evaluator to assume that a particular event exists merely because it is shown as an example.



Repository and application evidence remain authoritative.



\---



\## Existing Business Events



The current StoreOps application may contain established events such as:



```text

TASK\_CREATED

TASK\_COMPLETED

TASK\_BLOCKED

PROGRAMME\_CLOSED

```



These names are valid only where confirmed by:



```text

Existing Event Definitions

Existing Publishers

Existing Consumers

Existing Tests

Resolved Application Context

```



Planner must verify the actual supported events during repository discovery.



If repository evidence uses different names or semantics, the existing application remains authoritative.



\---



\## Event Discovery



Planner must inspect the current StoreOps application before determining whether a feature:



```text

Uses an Existing Event

Introduces a New Approved Event

Uses a Service-Mediated Interaction

Uses Scheduled Processing

Uses Another Approved StoreOps Pattern

```



Discovery should consider:



```text

Existing Event Definitions

Existing Publishers

Existing Consumers

Existing Event Infrastructure

Existing Module Interaction Patterns

Existing Tests

Resolved Application Configuration

StoreOps Architecture Rules

StoreOps Domain Rules

```



Planner must prefer an existing approved StoreOps pattern when it satisfies the feature requirements.



If the appropriate interaction mechanism cannot be determined from available evidence, Planner must surface the ambiguity rather than silently invent an event or event-driven flow.



\---



\## Feature-Specific Events



Feature-specific events must not be promoted into permanent StoreOps business-event knowledge unless they are established by the current application or explicitly approved as reusable StoreOps behavior.



For example:



```text

SLA\_BREACH

```



must not be assumed to exist solely because the SLA Breach Alerting feature is being implemented.



The feature may resolve to:



```text

Existing Event

New Approved Event

Service-Mediated Interaction

Scheduled Processing

Another Approved StoreOps Pattern

```



depending on:



```text

PROMPT.md

Existing StoreOps Behavior

Planner Discovery

Resolved Application Configuration

StoreOps Architecture Rules

Approved Sprint Contract

```



If Planner determines that a new event is required, the event and its applicable semantics must be represented in the approved sprint contract before Generator implements it.



\---



\## SLA Breach Alerting



SLA Breach Alerting does not inherently require an event-driven implementation.



Feature-specific business rules such as:



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



are not defined by this skill.



Those requirements are supplied through:



```text

PROMPT.md

&#x20;   ↓

Planner Discovery

&#x20;   ↓

spec.md

&#x20;   ↓

Approved Sprint Contract

```



If the approved sprint contract resolves SLA Breach Alerting to an event-driven mechanism, the interaction may take the form:



```text

Activities

&#x20;   ↓

Approved SLA Breach Event

&#x20;   ↓

Approved Event Mechanism

&#x20;   ↓

Alerts

```



The exact:



```text

Event Name

Publisher

Consumer

Payload

Trigger Condition

Processing Behavior

```



must be derived from the approved sprint contract and existing StoreOps patterns.



This skill does not mandate:



```text

SLA\_BREACH

Event Bus

Message Broker

Scheduler

Polling

Direct Service Invocation

```



as the implementation mechanism.



\---



\## Event Semantics



Where an event is established or approved, it must represent an observable StoreOps business occurrence.



Prefer semantic event concepts describing something that has occurred rather than implementation commands.



Examples may include:



```text

<Business Concept> Created

<Business Concept> Completed

<Business Concept> Blocked

<Business Condition> Detected

```



where those concepts are supported by the current StoreOps application.



The exact naming convention must follow existing StoreOps conventions.



This skill does not introduce a new naming convention when one already exists.



\---



\## Event Payload



Where an approved StoreOps interaction uses an event, the payload must contain sufficient context for the intended consumer to perform its approved responsibility without introducing prohibited architectural coupling.



Payload requirements must be derived from:



```text

Approved Sprint Contract

Existing Event Conventions

Consumer Requirements

StoreOps Architecture Rules

Active Technology Conventions

```



The payload should contain only information required by the approved business interaction.



This skill does not prescribe a fixed event schema.



\---



\## Consumer Context



Where an event-driven mechanism is approved, consumers must receive sufficient business context to perform the approved behavior.



A consumer must not rely on prohibited cross-module repository access merely because required context was omitted from the event.



The exact consumer-context requirement must be determined from:



```text

Approved Sprint Contract

Existing StoreOps Event Patterns

StoreOps Architecture Rules

```



Architectural restrictions on cross-module access remain authoritative in:



```text

domains/storeops/architecture-rules/SKILL.md

```



\---



\## Event Ownership



Where the current StoreOps architecture establishes event ownership, the event belongs to the module responsible for the business occurrence represented by that event.



The producing module is responsible for producing the approved business event when the approved business condition occurs.



The consuming module is responsible only for its approved reaction.



Event ownership must preserve the module boundaries defined by the StoreOps architecture.



This skill does not redefine module ownership established by the current application.



\---



\## Event Side Effects



An event consumer may perform only side effects permitted by:



```text

Approved Sprint Contract

StoreOps Domain Rules

StoreOps Architecture Rules

Existing Application Conventions

```



A consumer must not use an event as justification to:



\- expand feature scope;

\- introduce new business rules;

\- modify unrelated modules;

\- bypass module boundaries;

\- introduce unapproved notifications or escalations; or

\- create additional downstream effects not required by the approved contract.



Feature-specific side effects remain contract-owned.



\---



\## Duplicate and Idempotency Semantics



This skill does not assume a universal duplicate-processing or idempotency strategy for StoreOps events.



Where a feature requires duplicate prevention or idempotent processing, Planner must determine the applicable semantics from:



```text

Feature Requirements

Existing StoreOps Behavior

Existing Event Infrastructure

Repository Evidence

Resolved Configuration

```



The approved sprint contract must capture the required behavior.



Generator implements the approved behavior.



Evaluator verifies the approved behavior.



This skill must not invent:



```text

Deduplication Keys

Idempotency Keys

Retry Counts

Delivery Guarantees

Exactly-Once Processing

At-Least-Once Processing

```



unless those semantics are established by the current StoreOps application or approved sprint contract.



\---



\## Ordering and Delivery Semantics



This skill does not assume universal event-ordering, consistency, retry, or delivery guarantees.



Do not assume:



```text

Synchronous Delivery

Asynchronous Delivery

Exactly-Once Delivery

At-Least-Once Delivery

Ordered Delivery

Transactional Delivery

```



without supporting application, architecture, configuration, or contract evidence.



Where these semantics matter to a feature, Planner must discover the existing StoreOps behavior and capture the applicable requirement in the approved sprint contract.



\---



\## Time-Based Events



StoreOps domain concepts may include time-dependent behavior, but the existence of a time-dependent business rule does not automatically require:



```text

Scheduler

Timer

Polling

Scheduled Event

```



The exact:



```text

Date/Time Representation

Time Zone

Clock Abstraction

Scheduling Mechanism

```



is determined by the current StoreOps application, consistent with:



```text

domains/storeops/domain-rules/SKILL.md

```



Feature-specific concepts such as:



```text

Configured Grace Period

Grace-Period Expiry

Escalation Timing

```



remain feature-specific unless explicitly established as reusable StoreOps domain behavior.



Planner must discover the applicable mechanism and capture it in the approved sprint contract.



\---



\## Event Evolution



Changes to an existing StoreOps event must preserve compatibility requirements established by the current application architecture.



Planner must identify affected publishers and consumers before approving a material event-contract change.



Generator must not silently:



\- rename an existing event;

\- remove required payload fields;

\- change event meaning;

\- change publisher/consumer responsibilities;

\- change delivery semantics; or

\- introduce incompatible payload behavior.



A material event-contract change requires the applicable contract-change and approval workflow.



\---



\## Existing Application Preservation



Planner and Generator must inspect existing StoreOps event behavior before introducing new event semantics.



Prefer:



```text

Existing StoreOps Events

Existing Publisher Patterns

Existing Consumer Patterns

Existing Payload Conventions

Existing Interaction Mechanisms

Existing Delivery Semantics

```



over introducing new event behavior.



A new event or material event-contract change must not be introduced merely because it is technically possible.



Feature-specific extensions must be captured through:



```text

PROMPT.md

&#x20;   ↓

Planner Discovery

&#x20;   ↓

Approved Sprint Contract

```



\---



\## Planner Usage



Planner uses this skill to understand existing StoreOps business-event concepts during discovery.



Planner must:



\- inspect existing event definitions and usage;

\- determine whether the feature requires an event at all;

\- prefer existing approved patterns where appropriate;

\- identify affected publishers and consumers when an event is used;

\- identify event-related assumptions and open questions;

\- identify applicable payload and interaction requirements; and

\- capture the approved mechanism and event behavior in the sprint contract.



Planner must not treat this skill as evidence that a feature must be event-driven.



\---



\## Generator Usage



Generator uses this skill only when the approved sprint contract requires interaction with StoreOps business events.



Generator must:



\- preserve existing event semantics;

\- preserve existing event conventions;

\- implement only approved event behavior;

\- preserve StoreOps architecture constraints;

\- provide required consumer context;

\- add or update tests required by the approved contract; and

\- avoid introducing additional events, consumers, or side effects outside approved scope.



Generator must not use this skill to expand feature-specific business behavior.



If implementation requires a material event-contract or mechanism change, Generator must use the approved contract-change workflow rather than silently changing the design.



\---



\## Evaluator Usage



Evaluator uses this skill to verify event-related behavior only when events are applicable to the approved sprint contract.



Evaluator may verify, where applicable:



```text

Approved Event Semantics

Publisher Behavior

Consumer Behavior

Required Payload Context

Required Business Side Effects

Required Negative Paths

Approved Duplicate / Idempotency Behavior

Approved Ordering / Delivery Behavior

```



Architecture compliance is evaluated against:



```text

domains/storeops/architecture-rules/SKILL.md

```



Feature-specific business behavior is evaluated against:



```text

Approved Sprint Contract

\+

Applicable StoreOps Domain Rules

```



Evaluator must not:



\- fail an implementation merely because it does not use an event when another mechanism is approved;

\- introduce new event requirements during evaluation; or

\- promote feature-specific event behavior into permanent StoreOps domain truth.



\---



\## Monitor Usage



Monitor does not require this skill for governance evidence recording.



Monitor records canonical execution evidence and preserves Evaluator verdicts without interpreting StoreOps business-event semantics.



\---



\## Domain-Rules Boundary



Permanent StoreOps business concepts and rules belong to:



```text

domains/storeops/domain-rules/SKILL.md

```



This business-events skill must not redefine:



```text

Activity Priority Semantics

Activity Lifecycle Semantics

StoreOps Roles

Feature-Specific Role Bindings

Feature-Specific Eligibility

Feature-Specific Grace Periods

Feature-Specific Escalation Rules

```



unless those concepts are necessary to describe an already-established event and are supported by the authoritative StoreOps context.



Feature-specific business rules remain owned by:



```text

PROMPT.md

&#x20;   ↓

Planner Discovery

&#x20;   ↓

Approved Sprint Contract

```



\---



\## Architecture-Rules Boundary



Architecture constraints governing event usage belong to:



```text

domains/storeops/architecture-rules/SKILL.md

```



This skill must not redefine:



```text

Controller → Service → Repository Layering

Cross-Module Repository Rules

Cross-Module Read Rules

Reports Read-Only Constraints

Error Contracts

Approved Integration Mechanism

```



This skill may reference architecture rules where necessary to explain event semantics, but the architecture-rules skill remains authoritative for architectural constraints.



\---



\## Technology Boundary



This skill does not prescribe technology-specific event implementation.



It must not require:



```text

Spring Events

Kafka

Azure Service Bus

RabbitMQ

JMS

Specific Java Classes

Specific Annotations

Specific Serialization Libraries

```



Technology-specific implementation guidance belongs to the active technology profile and technology skills.



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



Canonical evidence naming and locations are controlled by the harness baseline and resolved configuration.



This skill does not redefine:



```text

generator-summary-iteration-N.md

evaluator-feedback-iteration-N.md

run-log.md

escalation.md

```



as business-event concepts.



\---



\## Responsibility Boundaries



This skill provides reusable StoreOps business-event knowledge.



It does not:



\- require every feature to use events;

\- prescribe a feature implementation mechanism;

\- define feature-specific business rules;

\- redefine StoreOps domain rules;

\- redefine StoreOps architecture rules;

\- define technology implementation;

\- define governance thresholds;

\- define orchestration behavior;

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

Event Infrastructure Configuration

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

StoreOps Business-Event Knowledge

```



The StoreOps domain-rules skill defines:



```text

Permanent StoreOps Domain Concepts and Rules

```



The StoreOps architecture-rules skill defines:



```text

StoreOps Architectural Constraints

```



Configuration defines:



```text

Applicable Runtime and Technology Values

```



Governance policy defines:



```text

Evaluation and Decision Policy

```



The approved sprint contract defines:



```text

Feature-Specific Approved Scope

\+

Applicable Domain Behavior

\+

Approved Interaction Mechanism

\+

Applicable Event Behavior

```

