# StoreOps Application Context

## Purpose

Provide reusable StoreOps application-context knowledge to the harness agents that consume domain context: Planner, Generator, and Evaluator.

This skill describes the StoreOps application structure and the responsibilities of its modules as established by the current StoreOps application.

It does not encode:

- feature-specific business rules;
- feature-specific role bindings;
- feature-specific event mechanisms;
- feature-specific configuration keys or values;
- architecture rules;
- technology-specific implementation details;
- governance policy;
- orchestration behavior;
- deployment or rollback controls; or
- reflection.

Feature-specific application usage flows through:

```text
PROMPT.md
   ↓
Planner Discovery
   ↓
spec.md
   ↓
Approved Sprint Contract
```

---

## Scope

This skill applies when the resolved application/domain context identifies StoreOps as the active application.

It must be interpreted together with:

```text
Resolved Application Configuration
Existing StoreOps Source
Existing StoreOps Tests
Existing Application Conventions
StoreOps Domain Rules
StoreOps Architecture Rules
StoreOps Business Events
Active Technology Profile
Active Governance Policy
Approved Sprint Contract
```

Repository and resolved application evidence remain authoritative for actual module names, responsibilities, relationships, source locations, capabilities, APIs, persistence ownership, and configuration surface.

Where a conflict exists between this skill and the current StoreOps application, the authoritative current application context governs.

---

## Application-Context Boundary

This skill contains:

```text
StoreOps Application Structure Overview
StoreOps Module Inventory (as established by the current application)
StoreOps Module Responsibilities (at an application-context level)
StoreOps Module Relationships (at an application-context level)
Reusable Application-Context Vocabulary
```

This skill does not contain:

```text
Feature-Specific Business Rules
Feature-Specific Role Bindings
Feature-Specific Notification / Escalation Rules
Feature-Specific Grace Periods or Timers
Feature-Specific Configuration Keys
Feature-Specific Idempotency / Duplicate-Handling Rules
Feature-Specific State Tracking
Architecture Mechanisms
Technology-Specific Implementation Details
Governance Thresholds
Orchestration Statuses
Deployment or Rollback Controls
```

The corresponding responsibilities remain owned by:

```text
domains/storeops/domain-rules/SKILL.md
domains/storeops/architecture-rules/SKILL.md
domains/storeops/business-events/SKILL.md
Active Technology Skills
Active Governance Policy
Harness Orchestration and Agent Definitions
DEPLOYMENT.md
REFLECTION.md
```

---

## Application Overview

StoreOps is the reference application used by the governed agentic delivery harness.

StoreOps supports store operations expressed through concepts such as:

```text
Activities
Programmes
Staff
Alerts
Reports
```

Additional modules or capabilities may exist in the current StoreOps application and must be discovered from the resolved application context and repository.

This skill describes application context at the level of module identity and responsibility.

Detailed business behavior belongs to the StoreOps domain-rules skill.

Detailed structural constraints belong to the StoreOps architecture-rules skill.

Detailed event semantics belong to the StoreOps business-events skill.

---

## Module Inventory

The following modules represent StoreOps application-context concepts.

Their existence and exact identity must be confirmed by the current StoreOps application/resolved configuration:

```text
Activities
Programmes
Staff
Alerts
Reports
```

Additional modules that exist in the current StoreOps application must be discovered from repository and configuration evidence.

Planner must not assume that a module exists solely because it is listed here.

---

## Activities Module

Responsibility (application-context level):

- represents operational units of work in StoreOps;
- exposes application capabilities related to those units of work;
- owns activity-related persistence within its module boundary as established by the current application.

Detailed activity attributes, lifecycle, and semantics belong to:

```text
domains/storeops/domain-rules/SKILL.md
```

Architectural constraints belong to:

```text
domains/storeops/architecture-rules/SKILL.md
```

Event semantics belong to:

```text
domains/storeops/business-events/SKILL.md
```

---

## Programmes Module

Responsibility (application-context level):

- represents programme-oriented grouping or organization of related operational work where supported by the current application;
- exposes application capabilities related to programmes;
- owns programme-related persistence within its module boundary as established by the current application.

Detailed programme business behavior belongs to the StoreOps domain-rules skill.

Detailed structural interaction rules belong to the StoreOps architecture-rules skill.

---

## Staff Module

Responsibility (application-context level):

- represents people participating in StoreOps operational processes;
- exposes application capabilities related to staff, roles, and organizational context;
- owns staff-related persistence within its module boundary as established by the current application.

Role vocabulary and role semantics used across StoreOps belong to:

```text
domains/storeops/domain-rules/SKILL.md
```

Feature-specific role bindings are supplied through:

```text
PROMPT.md
   ↓
Planner Discovery
   ↓
Approved Sprint Contract
```

Identity, authentication, and authorization implementation details are not defined by this skill.

---

## Alerts Module

Responsibility (application-context level):

- represents operational notifications or alert records where supported by the current application;
- exposes application capabilities related to creating, storing, retrieving, or delivering alerts as established by the current application;
- owns alert-related persistence within its module boundary as established by the current application.

Detailed alert type semantics, recipients, and lifecycle behavior belong to the StoreOps domain-rules skill.

Approved integration mechanisms between Alerts and other modules belong to the StoreOps architecture-rules skill.

Approved event flows between modules belong to the StoreOps business-events skill.

Feature-specific alerting behavior, including for SLA Breach Alerting, remains outside this skill.

---

## Reports Module

Responsibility (application-context level):

- represents read-oriented views of StoreOps operational information;
- exposes application capabilities related to reporting as established by the current application.

Architectural constraints on the Reports module, including any read-only restrictions, belong to:

```text
domains/storeops/architecture-rules/SKILL.md
```

This skill does not redefine Reports architecture.

---

## Cross-Module Relationships

StoreOps modules may collaborate to satisfy application capabilities.

Application-context level relationships include, where established by the current application:

```text
Activities may relate to Programmes
Activities may relate to Staff (assignment / ownership)
Programmes may relate to Staff
Alerts may relate to Activities or other business subjects
Reports may consume information from other modules in a read-oriented manner
```

Exact relationship semantics, integration mechanisms, and dependency direction are governed by:

```text
domains/storeops/architecture-rules/SKILL.md
```

This skill does not prescribe how modules interact.

It does not prescribe:

```text
Event Bus
Direct Service Invocation
Scheduler
Message Broker
Polling
Repository-Level Sharing
```

as a required interaction mechanism.

The approved sprint contract determines feature-specific interaction, subject to StoreOps architecture rules.

---

## APIs, Endpoints, and Persistence

The exact StoreOps:

```text
API Surface
REST Endpoints
Resource Naming
Persistence Technology
Persistence Schemas
```

are determined by the current StoreOps application and resolved configuration.

This skill does not define specific:

- REST paths;
- resource names;
- persistence technologies;
- schema names;
- indexing strategies;
- database types; or
- serialization formats.

Planner must inspect existing application evidence before assuming any specific API or persistence detail.

Technology-specific implementation guidance remains owned by the active technology skills and technology profile.

---

## Configuration Surface

StoreOps behavior may be influenced by application configuration.

The exact:

```text
Configuration Surface
Configuration Keys
Default Values
Environment Overrides
Secret Handling
```

are determined by the current StoreOps application and resolved harness configuration.

This skill does not define:

```text
Specific Configuration Keys
Specific Configuration Values
Feature-Specific Grace Periods
Feature-Specific Timers
Feature-Specific Thresholds
```

Feature-specific configuration requirements are supplied through:

```text
PROMPT.md
   ↓
Planner Discovery
   ↓
Approved Sprint Contract
```

---

## Feature Context

Feature-specific behavior must not be encoded as permanent StoreOps application context unless it is explicitly established as reusable application semantics by the authoritative StoreOps baseline.

For example, SLA Breach Alerting requirements including:

```text
HIGH / CRITICAL Eligibility
Overdue Conditions
Unresolved Conditions
Department Lead Notification
Store Manager Escalation
Configured Grace Period
Duplicate / Idempotency Behavior
State Tracking
Time Handling
Implementation Mechanism (event-driven, service-mediated, scheduled, or other)
```

must not be treated as permanent StoreOps app-context.

Those concerns remain feature-specific and flow through:

```text
PROMPT.md
   ↓
Planner Discovery
   ↓
spec.md
   ↓
Approved Sprint Contract
```

Approved feature implementation must:

- preserve existing StoreOps application modules and responsibilities;
- preserve StoreOps architecture rules;
- preserve StoreOps domain rules;
- preserve StoreOps business-event knowledge; and
- use the mechanism approved through Planner discovery and the sprint contract.

---

## Existing Application Preservation

Planner and Generator must inspect the existing StoreOps application before introducing new application-context assumptions.

Prefer:

```text
Existing Modules
Existing Module Responsibilities
Existing Application Vocabulary
Existing API Surface
Existing Persistence Ownership
Existing Configuration Patterns
Existing Cross-Module Interaction Patterns
```

over introducing new application concepts.

A new module or new application-context relationship must not be introduced merely because it is technically convenient.

Feature-specific application changes must be captured through:

```text
PROMPT.md
   ↓
Planner Discovery
   ↓
Approved Sprint Contract
```

and must not be promoted into this skill unless explicitly established as reusable StoreOps application context.

---

## Planner Usage

Planner uses this skill for repository and application-context discovery.

Planner must combine this skill with:

```text
Feature Prompt
Resolved Application Configuration
Existing Source
Existing Tests
Existing Application Conventions
StoreOps Domain Rules
StoreOps Architecture Rules
StoreOps Business Events
```

Planner must:

- identify impacted modules;
- identify existing responsibilities;
- identify existing cross-module relationships relevant to the feature;
- identify assumptions and open questions;
- surface material ambiguity rather than invent application structure; and
- capture approved feature-specific application usage in the sprint contract.

Planner must not treat this skill as evidence that any specific module, capability, API, or configuration key exists in the current application beyond the level of application context.

---

## Generator Usage

Generator uses this skill to locate and preserve approved StoreOps application responsibilities while implementing an approved sprint contract.

Generator must:

- implement only approved application behavior;
- respect approved module boundaries;
- reuse existing application capabilities where appropriate;
- avoid introducing new modules, endpoints, or persistence surfaces outside approved scope; and
- avoid unrelated refactoring.

Generator must not use this skill to expand feature scope or invent application structure.

If implementation requires a material application-context change, Generator must use the applicable contract-change workflow rather than silently modifying the application.

---

## Evaluator Usage

Evaluator uses this skill to understand relevant StoreOps application/module context for independent verification.

Evaluator may verify:

```text
Whether Implementation Uses Approved Modules
Whether Implementation Preserves Module Responsibilities
Whether Implementation Respects Approved Application-Context Relationships
Whether Implementation Preserves Existing Application Conventions Applicable to the Approved Contract
```

where applicable.

Detailed structural checks belong to:

```text
domains/storeops/architecture-rules/SKILL.md
```

Detailed business behavior belongs to:

```text
domains/storeops/domain-rules/SKILL.md
```

Detailed event semantics belong to:

```text
domains/storeops/business-events/SKILL.md
```

Evaluator must not fail implementation for behavior that was never required by the approved sprint contract or authoritative StoreOps application context.

---

## Monitor Usage

Monitor does not require this skill for governance-observability recording.

Monitor records canonical execution evidence and preserves Evaluator verdicts without interpreting StoreOps application-context semantics.

---

## Boundary With Other StoreOps Skills

### Domain-Rules Boundary

Permanent StoreOps business concepts and rules belong to:

```text
domains/storeops/domain-rules/SKILL.md
```

This skill must not redefine:

```text
Activity Attributes
Activity Lifecycle Semantics
Priority Semantics
Role Semantics
Time-Dependent Business Behavior
```

### Architecture-Rules Boundary

StoreOps structural and integration constraints belong to:

```text
domains/storeops/architecture-rules/SKILL.md
```

This skill must not redefine:

```text
Controller → Service → Repository Layering
Cross-Module Repository Rules
Cross-Module Read Rules
Cross-Module Side-Effect Mechanisms
Reports Read-Only Architecture
Error Contracts
Approved Integration Mechanisms
```

### Business-Events Boundary

StoreOps event knowledge belongs to:

```text
domains/storeops/business-events/SKILL.md
```

This skill must not require:

```text
Specific Event Names
Publisher / Consumer Bindings
Event Payloads
Delivery Semantics
```

as permanent application-context truth.

---

## Technology Boundary

This skill does not prescribe technology-specific implementation.

It must not define:

```text
Java
Spring Boot
Frameworks
Annotations
Serialization Libraries
Persistence Technologies
Build Tools
Test Frameworks
```

Technology-specific guidance belongs to the active technology profile and technology skills.

---

## Governance Boundary

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

---

## Orchestration Boundary

This skill does not define or emit harness orchestration states such as:

```text
STATUS: AWAITING_APPROVAL
STATUS: READY_FOR_EVALUATION
STATUS: CONTRACT_CHANGE_REQUIRED
STATUS: COMPLETED
```

It does not define:

```text
PASS
CONDITIONAL_PASS
FAIL
```

verdict semantics.

Agent definitions, `CLAUDE.md`, and the active governance policy remain authoritative for orchestration and evaluation behavior.

---

## Evidence Boundary

This skill does not define runtime evidence filenames, output paths, or review/archive paths.

Canonical evidence naming and locations remain controlled by the harness baseline and resolved configuration.

This skill does not redefine:

```text
generator-summary-iteration-N.md
evaluator-feedback-iteration-N.md
run-log.md
escalation.md
```

as application-context concepts.

---

## Responsibility Boundaries

This skill provides reusable StoreOps application-context knowledge.

It does not:

- prescribe feature-specific business rules;
- prescribe feature-specific role bindings;
- prescribe implementation mechanisms;
- redefine StoreOps domain rules;
- redefine StoreOps architecture rules;
- redefine StoreOps business events;
- prescribe technology-specific implementation;
- prescribe governance thresholds;
- prescribe scoring rules;
- prescribe iteration limits;
- prescribe evidence naming;
- prescribe orchestration states;
- authorize retries;
- authorize escalation;
- authorize contract changes;
- deploy software;
- perform rollback; or
- perform reflection.

The responsibility flow remains:

```text
PROMPT.md
   ↓
Planner Discovery
   ↓
Approved Sprint Contract
   ↓
Generator Implementation
   ↓
Evaluator Verification
   ↓
Monitor Evidence Recording
```

---

## Configuration Ownership

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
StoreOps Application-Context Knowledge
```

The StoreOps domain-rules skill defines:

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
+
Applicable StoreOps Application-Context Usage
+
Applicable Domain Behavior
+
Approved Interaction Mechanism
+
Applicable Event Behavior
```
