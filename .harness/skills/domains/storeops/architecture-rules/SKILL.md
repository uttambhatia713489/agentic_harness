# StoreOps Architecture Rules

**Version:** 2.0  
**Skill Type:** Authoritative StoreOps Architecture Guidance  
**Logical Skill ID:** `domains/storeops/architecture-rules`  
**Reference Implementation:** Java 21 + Spring Boot 3.x

---

## 1. Purpose and Ownership

Define the permanent architecture constraints for StoreOps and concise Java/Spring guidance for applying them.

Used by:

```text
Planner
Generator
Evaluator
```

This skill owns:

- StoreOps layering and dependency rules;
- module-boundary constraints;
- cross-module interaction constraints;
- Reports read-only constraint; and
- StoreOps application error-contract constraint.

It does **not** own:

```text
Business/domain semantics       → domains/storeops/domain-rules
Application/module context      → domains/storeops/app-context
Business-event semantics        → domains/storeops/business-events
Technology/tools/commands       → java-spring.yaml
Hard gates/thresholds/retries   → default-governance.yaml
Feature-specific decisions      → approved sprint contract
```

Repository evidence remains authoritative for the actual StoreOps implementation.

---

## 2. Module Boundaries

StoreOps modules include:

```text
activities
programmes
staff
alerts
reports
```

The exact package structure and implementation must be confirmed from the repository.

Each module owns its:

- application behavior;
- persistence access;
- internal implementation;
- module-specific state; and
- approved external interfaces.

A module must not directly access another module's repository or internal implementation.

### Java/Spring Guidance

Follow the existing StoreOps package structure. A typical module may contain:

```text
activities/
├── controller/
├── service/
├── repository/
├── model/
└── dto/
```

Do not reorganize existing packages merely to match this example.

---

## 3. Layering and Dependency Direction

The StoreOps dependency direction is:

```text
Controller
    ↓
Service
    ↓
Repository
```

### Controllers

Controllers may handle HTTP/API concerns and delegate behavior to services.

Controllers must not:

- contain business logic;
- access repositories directly;
- perform persistence operations; or
- coordinate cross-module side effects directly.

Preferred:

```java
@RestController
@RequestMapping("/activities")
class ActivityController {

    private final ActivityService activityService;

    ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }
}
```

Prohibited:

```java
@RestController
class ActivityController {

    private final ActivityRepository activityRepository;
}
```

A controller-to-repository dependency is an architecture violation.

### Services and Repositories

Services coordinate application behavior.

Repositories provide persistence access only for their owning module.

Preferred:

```java
@Service
class ActivityService {

    private final ActivityRepository activityRepository;

    ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }
}
```

Repositories must not become cross-module integration interfaces.

---

## 4. Cross-Module Access

### Repository Access

Direct cross-module repository access is prohibited.

```text
Activities Service
      ↓
Alerts Repository
      ✗
```

Required relationship:

```text
Calling Module
      ↓
Approved Module Interface
      ↓
Owning Module
```

For example, avoid:

```java
class ActivityService {
    private final AlertRepository alertRepository;
}
```

A service or query interface may be used only when it is an established StoreOps pattern or explicitly approved by the sprint contract.

### Cross-Module Reads

Cross-module reads must use an established application-facing interface:

```text
Module A
    ↓
Approved Service / Query Interface
    ↓
Module B
```

For example:

```java
public interface StaffQueryService {
    Optional<StaffSummary> findById(Long staffId);
}
```

Do not expose another module's repository merely to support a read.

---

## 5. Cross-Module Side Effects

Cross-module side effects must use the interaction mechanism established by StoreOps and approved in the sprint contract.

Possible mechanisms include:

```text
Established Business Event
Approved Application Interface
Scheduled Processing
Another Existing StoreOps Pattern
```

This skill does **not** automatically mandate:

```text
Event Bus
SLA_BREACH
Scheduler
Polling
Direct Service Invocation
```

The mechanism is determined through:

```text
Existing StoreOps Evidence
        ↓
Planner Discovery
        ↓
Approved Sprint Contract
        ↓
Generator Implementation
        ↓
Evaluator Verification
```

If an approved event-driven mechanism exists, use the application's established event abstraction, conceptually:

```java
eventPublisher.publish(event);
```

Do not introduce a new event mechanism, broker, dispatcher, or direct service dependency without supporting StoreOps evidence and contract approval.

`SLA_BREACH` remains feature-specific unless existing StoreOps evidence establishes it as a permanent event.

---

## 6. Reports Read-Only Constraint

Where established by StoreOps, Reports is read-only.

Reports may:

- query approved information;
- compose read models;
- calculate report output; and
- expose reporting capabilities.

Reports must not:

- mutate Activities, Programmes, Staff, or Alerts;
- own operational workflow transitions; or
- initiate state-changing side effects.

Expected:

```java
@Service
class ReportService {

    public ReportView generateReport(...) {
        ...
    }
}
```

Operational writes from Reports require an explicitly approved architecture change.

---

## 7. Application Error Contract

StoreOps uses the established application error contract based on:

```text
AppError
```

Implementation must preserve that contract.

Do not:

- introduce unrelated error structures;
- expose internal exceptions directly;
- silently swallow exceptions;
- return inconsistent error responses; or
- replace the established error hierarchy within feature scope.

Use existing StoreOps typed errors, for example:

```java
throw new AppError(...);
```

or established subclasses/factories where present.

Use the existing controller-level error translation mechanism, such as an established:

```java
@RestControllerAdvice
```

if present.

Exact constructors, mappings, and response schemas must be discovered from the repository.

---

## 8. Prohibited Architecture Patterns

Unless an explicitly approved architecture change states otherwise:

```text
Controller → Repository                         PROHIBITED

Module A Service → Module B Repository          PROHIBITED

Direct Dependency on Another Module's Internals PROHIBITED

Circular Module Dependency                      PROHIBITED

Reports → Operational Mutation                  PROHIBITED

Unapproved Cross-Module Side Effect             PROHIBITED

Feature Event Without Contract Approval         PROHIBITED

Bypassing AppError Contract                     PROHIBITED

Generator-Invented Architecture Mechanism       PROHIBITED
```

A functioning feature does not make a prohibited dependency acceptable.

---

## 9. Existing-Pattern Preservation

Planner and Generator must inspect StoreOps before introducing new architecture.

Prefer:

```text
Existing Service
Existing Module Interface
Existing Event Mechanism
Existing Error Contract
Existing DTO Pattern
Existing Persistence Pattern
Existing Test Pattern
```

over creating a new abstraction.

A material architecture change requires:

```text
Planner Discovery
      ↓
Sprint Contract
      ↓
Human Approval
```

Generator must not redesign StoreOps during implementation.

---

## 10. Feature-Specific Decisions

The following are **not permanent StoreOps architecture rules** unless existing StoreOps evidence establishes them:

```text
SLA_BREACH event
Breach-detection trigger
Grace-period configuration
Escalation timing
Department Lead resolution
Store Manager resolution
Idempotency strategy
State-tracking model
Clock/time-zone strategy
Scheduling mechanism
Notification mechanism
Event payload
```

These belong to:

```text
PROMPT.md
      ↓
Planner Discovery
      ↓
spec.md
      ↓
Approved sprint-N-contract.md
```

Generator implements the approved decision.

Evaluator verifies the approved decision.

---

## 11. Agent Responsibilities

### Planner

Planner must:

1. identify impacted StoreOps modules;
2. inspect existing dependency and interaction patterns;
3. determine required cross-module reads or side effects;
4. identify applicable architecture constraints;
5. capture feature-specific architecture decisions in the sprint contract; and
6. surface unresolved architecture ambiguity for approval.

Planner must not invent modules, repositories, services, events, or integration mechanisms.

### Generator

Generator must:

- preserve `Controller → Service → Repository`;
- preserve module ownership;
- avoid cross-module repository access;
- use only the approved cross-module mechanism;
- preserve Reports read-only behavior;
- preserve the `AppError` contract; and
- follow existing Java/Spring conventions.

If a material architecture change is required:

```text
STATUS: CONTRACT_CHANGE_REQUIRED
```

Generator must stop rather than silently redesign StoreOps.

### Evaluator

Evaluator independently verifies:

```text
Layering
Module Boundaries
Repository Ownership
Cross-Module Dependencies
Approved Read Mechanism
Approved Side-Effect Mechanism
Reports Read-Only Constraint
AppError Preservation
Approved Feature Architecture
```

---

## 12. Deterministic Verification

This skill defines:

```text
WHAT StoreOps architecture requires
```

The technology profile defines:

```text
HOW it is checked
```

The governance policy defines:

```text
WHETHER it is mandatory
and
WHAT result is acceptable
```

Therefore:

```text
architecture-rules/SKILL.md
        ↓
Architecture Rule
        ↓
java-spring.yaml
        ↓
Tool / Command
        ↓
default-governance.yaml
        ↓
Mandatory Status / Policy
        ↓
Evaluator
        ↓
Compliance Decision
```

For Java/Spring, deterministic evidence may include configured:

```text
Maven
JUnit
ArchUnit
Checkstyle
SpotBugs
```

Where executable ArchUnit rules exist, they should enforce applicable module and dependency boundaries.

The `.harness/checks/architecture` directory remains an extension point only. Its existence is not evidence that an executable architecture check exists.

A named tool is not proof that a hard gate executed.

---

## 13. Evaluator Findings

Architecture findings must be actionable.

Where applicable, Evaluator records:

```text
RULE
CHECK
FILE
LINE
OBSERVED
EXPECTED
REMEDIATION
```

Example:

```text
RULE:
Cross-module repository access is prohibited.

CHECK:
ArchUnit / dependency inspection.

FILE:
src/main/java/.../ActivityService.java

LINE:
42

OBSERVED:
ActivityService directly depends on AlertRepository.

EXPECTED:
Activities must interact with Alerts through the
approved module interaction mechanism.

REMEDIATION:
Remove the direct AlertRepository dependency and use
the mechanism approved by the sprint contract.
```

Vague findings such as:

```text
Code does not follow architecture.
```

are insufficient.

---

## 14. Responsibility Summary

```text
storeops.yaml
    → selects this skill

architecture-rules/SKILL.md
    → permanent StoreOps architecture constraints

Existing StoreOps Repository
    → actual implementation evidence

Planner
    → discovers and contracts applicable architecture

Generator
    → implements approved architecture

Evaluator
    → independently verifies architecture

java-spring.yaml
    → Java/Spring tools and commands

default-governance.yaml
    → mandatory status, thresholds, and verdict policy

Monitor
    → records/references resulting evidence
```

> **Governing principle:** Preserve StoreOps module ownership and established architecture first; introduce feature-specific architecture only through Planner discovery and an approved sprint contract.
