# Specification: SLA Breach Alerting

Runtime instance of: `.harness/templates/spec.template.md`

---

## Metadata

```text
Run ID:            RUN-20260920-8e88a0
Feature Prompt:    PROMPT.md
Application:       storeops
Technology:        java-spring
Governance Policy: default-governance
Author Agent:      Planner
Status:            PARTIALLY APPROVED — SPRINT-1 approved 2026-09-20;
                    SPRINT-2 and SPRINT-3 remain AWAITING_APPROVAL.
Baseline:          v2.0
```

**Approval Record (SPRINT-1):** Approved by uttambhatia@outlook.com (session
user) on 2026-09-20 via approval command `APPROVED`, scoped explicitly to
Sprint 1 only (`sprint-SPRINT-1-contract.md`). This specification (`spec.md`)
itself, and Sprint 2/Sprint 3 contracts, are frozen as planned but not yet
approved; no material change has been made to them as part of this approval.

---

## Feature

SLA Breach Alerting for StoreOps. When an eligible activity (`HIGH`/`CRITICAL` priority) becomes overdue and remains unresolved, StoreOps must detect the condition as an SLA breach, notify the assigned Department Lead, and — if the breach remains unresolved after a configured grace period — escalate to the Store Manager.

Reference: `PROMPT.md`.

---

## Business Objective

Ensure high-priority operational work that has missed its due date is surfaced to the accountable Department Lead promptly, and automatically escalated to the Store Manager if it continues to go unaddressed, so that breaches of service-level expectations are never silently missed.

Observable outcomes required (from `PROMPT.md` §3):

| Scenario | Expected Outcome |
|---|---|
| Overdue `HIGH` activity, unresolved | SLA breach detected |
| Overdue `CRITICAL` activity, unresolved | SLA breach detected |
| Eligible SLA breach | Assigned Department Lead notified |
| Breach unresolved after grace period | Store Manager notified/escalated |
| Breach resolved before grace period | No Store Manager escalation |
| `LOW` priority | No SLA breach |
| `MEDIUM` priority | No SLA breach |
| `DONE` activity | No SLA breach |
| Activity not overdue | No SLA breach |

---

## Scope

- Detecting SLA-breach-eligible activities (`activities` module).
- Resolving and notifying the assigned Department Lead for an eligible breach (`activities` + `programmes` + `alerts`).
- Tracking a configured grace period following Department Lead notification.
- Resolving and escalating to the Store Manager if the breach remains unresolved after the grace period (`alerts` + `programmes`).
- Preventing duplicate detection/notification/escalation for the same breach across repeated evaluation, application restart, and concurrent processing, to the extent existing repository evidence and this run's approved decisions support.
- Automated tests demonstrating the positive and negative paths listed in `PROMPT.md` §8.

## Out of Scope

- Any change to the `reports` module (Reports remains read-only per `domains/storeops/architecture-rules/SKILL.md` §6; SLA Breach Alerting introduces no reporting requirement).
- Any new identity/authentication mechanism, new event-bus/broker technology, new scheduling platform (e.g., Quartz), or general-purpose workflow/rules engine (`PROMPT.md` §10).
- Delivery-channel implementation details beyond what the existing `NotificationChannel` enum (`IN_APP`, `EMAIL`) and `AlertService`/`AlertRepository` already support.
- Any change to authentication/authorization (`AuthController`, `StaffService.login`) or to `StaffRole`/global user-role semantics.
- Retroactive SLA evaluation of historical data beyond what the approved due-date attribute (see Open Questions) makes representable going forward.
- UI/front-end changes (none exist in this repository).

---

## Impacted Components

```text
Component:       activities
Responsibility:  Owns Task (Activity) data, including priority, status, and (pending approval) due date; owns SLA-breach eligibility evaluation and its trigger.
Discovery Basis: applications/storeops/src/main/java/com/storeops/activities/**

Component:       programmes
Responsibility:  Owns Project (Programme) data and ProjectMember role assignments (STORE_MANAGER, DEPARTMENT_LEAD, ASSOCIATE) used to resolve the Department Lead and Store Manager for a given activity's programme.
Discovery Basis: applications/storeops/src/main/java/com/storeops/programmes/**

Component:       alerts
Responsibility:  Owns Notification data and delivery; already defines AlertType.SLA_BREACH and AlertType.ESCALATION. Will own creation of Department Lead and Store Manager notifications and (pending approval) the state used for duplicate/idempotency detection.
Discovery Basis: applications/storeops/src/main/java/com/storeops/alerts/**

Component:       staff
Responsibility:  Owns User/StaffRole. Referenced only for role vocabulary (STORE_MANAGER, DEPARTMENT_LEAD); no functional change currently anticipated (see Open Question OQ-4 on resolution source).
Discovery Basis: applications/storeops/src/main/java/com/storeops/staff/**

Component:       reports
Responsibility:  Not impacted. Out of scope.
Discovery Basis: domains/storeops/architecture-rules/SKILL.md §6 (Reports read-only constraint)

Component:       common (errors, events)
Responsibility:  Provides AppError contract and DomainEvent/EventBus infrastructure that any new SLA-breach signal must use if an event-based mechanism is approved.
Discovery Basis: applications/storeops/src/main/java/com/storeops/common/**
```

---

## Discovered Application and Domain Context

```text
Application Context Findings:
  StoreOps modules confirmed present: activities, programmes, staff, alerts, reports
  (matches storeops.yaml `modules` list exactly).

Domain Rule Findings:
  - Task (Activity) model (activities/model/Task.java) has fields: id, programmeId,
    title, description, status, priority, category, assigneeId, ownerId, createdAt,
    updatedAt. There is NO due-date/deadline field anywhere on Task, and no such
    field exists elsewhere in the activities module. See Open Question OQ-1.
  - TaskStatus = { TODO, IN_PROGRESS, DONE, BLOCKED }. TaskPriority = { LOW, MEDIUM,
    HIGH, CRITICAL }. Both match the values PROMPT.md assumes.
  - No domain evidence defines a second "resolved" status besides DONE; BLOCKED is
    a distinct, non-terminal status already wired to a different existing signal
    (TaskOverdueEvent, see below). See Open Question OQ-6 on "unresolved" semantics.
  - Department Lead / Store Manager are not modeled as a global "Department"
    entity anywhere in StoreOps. The concrete existing relationship is
    Project.members: List<ProjectMember>, where ProjectMember.role is a
    ProjectRole in { STORE_MANAGER, DEPARTMENT_LEAD, ASSOCIATE }, scoped to the
    Project (Programme) referenced by Task.programmeId. A separate, independent
    role concept also exists: User.role (StaffRole, global, not project-scoped),
    with StaffRole also containing STORE_MANAGER and DEPARTMENT_LEAD (plus
    REGIONAL_MANAGER, ASSOCIATE). These are two distinct existing relationships;
    Planner recommends the project-scoped ProjectMember relationship because it
    directly ties to Task.programmeId with no further lookup needed and does not
    require assuming a 1:1 mapping between global role and store. See Open
    Question OQ-4.
  - AlertType already defines SLA_BREACH and ESCALATION values (currently unused
    by any producer). This is a strong existing precedent to reuse rather than
    inventing a new alert type.
  - Notification (alerts/model/Notification.java) has no field correlating a
    notification to the entity it concerns (no taskId/referenceId). AlertService
    currently exposes only `listForUser`; no create/send capability is exposed
    at the service layer (AlertRepository.save exists but is unused by
    AlertService). See Open Question OQ-3.
  - No grace-period, timer, or SLA configuration exists anywhere in
    application.yml (which currently only sets server.port and
    spring.application.name) or in any Java class. See Open Question OQ-5.

Architecture Rule Findings:
  - Controller -> Service -> Repository layering is consistently followed by all
    existing modules (ActivityController -> ActivityService -> ActivityRepository,
    etc.). No controller accesses a repository directly.
  - No cross-module repository access exists anywhere in the current codebase.
    The one existing cross-module-shaped need (Alerts listing notifications
    "for a user", Programmes listing "for a store") is always satisfied within
    the owning module's own repository — there is no existing example of Module
    A's service calling Module B's service. Planner must therefore introduce the
    first such interaction for this feature, following the "approved application
    interface" pattern described in architecture-rules/SKILL.md §4 (e.g.
    extending ProgrammeService with a read method returning ProgrammeDto, which
    already includes `members`).
  - EventBus / DomainEvent / EventHandler / InMemoryEventBus exist as
    infrastructure, but a repository-wide search found zero existing
    `subscribe(...)` call sites — no module currently consumes any event
    (TaskOverdueEvent and ProgrammeClosedEvent are both emitted but never
    consumed anywhere in source or tests). There is therefore no established
    producer-to-consumer wiring pattern to imitate if an event-driven mechanism
    is chosen; it would be a first-of-its-kind wiring in this codebase, even
    though the abstraction itself is pre-existing and approved for this purpose.
  - No @Scheduled annotation, TaskScheduler, or any other background/periodic
    processing mechanism exists anywhere in the codebase. See Open Question OQ-2.
  - Existing error contract: AppError (abstract) with NotFoundError,
    ConflictError, ValidationError, UnauthorizedError subclasses, translated by
    AppExceptionMapper. Must be preserved for any new failure path introduced
    by this feature (unlikely to be heavily exercised since SLA evaluation is
    primarily a background/internal flow, not a new user-facing endpoint).

Business Event Findings:
  - TaskOverdueEvent(taskId, programmeId) exists and is emitted by
    ActivityServiceImpl.update() specifically when status is set to BLOCKED.
    Despite the name, this event is not due-date-driven and is a distinct,
    already-established signal unrelated to this feature's "overdue" (due date
    passed) concept. It must not be reused or conflated with SLA breach
    detection.
  - ProgrammeClosedEvent(programmeId, storeId) exists, emitted on Project close;
    not relevant to this feature.
  - No SLA_BREACH domain event exists (only the unrelated AlertType.SLA_BREACH
    enum value, which is alert-payload metadata, not a DomainEvent).
  - Per domains/storeops/business-events/SKILL.md, SLA Breach Alerting does not
    inherently require an event-driven implementation; the mechanism must be
    resolved through Planner discovery and approved by the sprint contract.

Existing Application Patterns Reused:
  - Plain mutable JavaBean-style domain models (Task, Project, Notification,
    User) with UUID string ids generated in the service layer via
    UUID.randomUUID().toString().
  - Record-based DTOs and request objects (ActivityDto, CreateActivityRequest,
    UpdateActivityRequest, ProgrammeDto, NotificationDto).
  - Constructor injection into @Service classes; @Repository-annotated
    in-memory (ConcurrentHashMap-backed) repository implementations.
  - Service-level `getById` throwing NotFoundError, mirrored by
    ActivityService.getById and StaffService.getById; recommended for the new
    ProgrammeService read method (see Impacted Components).

Mechanism Determination (Planner recommendation, pending approval):
  - Breach-detection trigger: new periodic evaluation (see Open Question OQ-2).
  - Department Lead / Store Manager resolution: existing Project.members /
    ProjectRole relationship (see Open Question OQ-4).
  - Department Lead notification and Store Manager escalation: extend the
    existing AlertService/AlertRepository (Notification, AlertType.SLA_BREACH /
    AlertType.ESCALATION) — no new notification mechanism.
  - Cross-module interaction: new narrow read method(s) on the existing
    ProgrammeService interface (approved-interface pattern), not repository
    sharing, and not necessarily an event (see Applicable Architecture
    Constraints and Open Question OQ-2 for whether an internal DomainEvent is
    also used to decouple detection from notification within/across modules).
```

---

## Assumptions

```text
Assumption:            "Unresolved" means TaskStatus != DONE (i.e. TODO,
                        IN_PROGRESS, and BLOCKED are all breach-eligible
                        statuses; only DONE prevents/ends a breach).
Basis:                 DONE is the only status in the existing TaskStatus enum
                        that domain evidence (naming, and domain-rules skill
                        guidance to preserve an established DONE = completed
                        meaning) supports as terminal/completed. No second
                        terminal status exists in the enum.
Impact if False:       Breach eligibility and resolution-before-escalation
                        logic would use the wrong status set, causing false
                        positives or missed breaches/escalations.
Affected Acceptance Criteria: AC-S1-003, AC-S1-004, AC-S3-004
Requires Approval:     Yes (also tracked as Open Question OQ-6)

Assumption:            Department Lead and Store Manager are resolved via
                        Project.members (ProjectMember.role == ProjectRole
                        .DEPARTMENT_LEAD / STORE_MANAGER) for the Project
                        identified by the breached Task's programmeId, not via
                        the separate global User.role (StaffRole) field.
Basis:                 Project.members is the only existing relationship that
                        directly connects an Activity (via programmeId) to a
                        role-bearing person without an additional, unverified
                        join (e.g., matching UserProfile.storeId to
                        Project.storeId).
Impact if False:       Notifications/escalations would be resolved and sent to
                        the wrong recipient population.
Affected Acceptance Criteria: AC-S2-001, AC-S2-002, AC-S3-002
Requires Approval:     Yes (also tracked as Open Question OQ-4)

Assumption:            When zero ProjectMembers hold the required role
                        (DEPARTMENT_LEAD or STORE_MANAGER) for the relevant
                        Project, StoreOps records/logs the condition and takes
                        no further action for that recipient step, rather than
                        throwing an error that could destabilize the
                        evaluation of other activities.
Basis:                 PROMPT.md does not define an error path for an
                        unassigned role; AppError is reserved for user-facing
                        request failures, and this is an internal/background
                        evaluation, not a request a caller can retry.
Impact if False:       An unassigned role could throw and abort a whole
                        evaluation batch instead of degrading gracefully for
                        just the affected activity.
Affected Acceptance Criteria: AC-S2-004, AC-S3-005
Requires Approval:     Yes

Assumption:            When more than one ProjectMember holds the required
                        role for the relevant Project, all matching members are
                        notified/escalated (no single "primary" selection
                        logic is invented).
Basis:                 ProjectMember role assignment already supports multiple
                        members per role (it is a List); PROMPT.md does not
                        require picking a single recipient.
Impact if False:       Some accountable leads/managers could be silently
                        excluded from notification/escalation.
Affected Acceptance Criteria: AC-S2-001, AC-S3-002
Requires Approval:     Yes

Assumption:            A new java.time.Clock Spring bean (default
                        Clock.systemUTC()) is introduced and used only by the
                        new SLA-evaluation code path, to support deterministic
                        time-based tests; no existing Instant.now() call site
                        elsewhere in the codebase is refactored.
Basis:                 No Clock abstraction exists anywhere in the repository
                        today; the technology profile marks deterministic time
                        handling as "preferred"; introducing a narrowly-scoped
                        bean is additive and does not alter existing behavior.
Impact if False:       Grace-period and overdue tests would depend on wall-
                        clock time, making them flaky/non-deterministic.
Affected Acceptance Criteria: AC-S1-006, AC-S3-006
Requires Approval:     Yes
```

---

## Open Questions

```text
Question:                     Activity has no due-date/deadline attribute
                               anywhere in the current domain model. How should
                               it be represented?
Proposed Assumption:           Add a nullable `dueAt` field of type
                               java.time.Instant to Task, exposed via
                               ActivityDto, CreateActivityRequest, and
                               UpdateActivityRequest (optional on both create
                               and update, consistent with how priority/
                               category/assigneeId are already optional on
                               UpdateActivityRequest). A null dueAt means the
                               activity is never overdue.
Rationale:                     Instant is already the timestamp type used
                               throughout Task (createdAt/updatedAt) and the
                               rest of the codebase; nullable preserves
                               backward compatibility with any existing
                               activities that predate this field.
Impact:                        This is a material, additive change to a core
                               domain model/DTO surface and must be explicitly
                               approved before Generator implements it.
Affected Acceptance Criteria: AC-S1-001 through AC-S1-006 (all Sprint 1 ACs)
Decision Required From Human: Yes
Blocking:                      Yes

Question:                     No scheduling or background-processing
                               mechanism exists anywhere in StoreOps. What
                               should trigger periodic SLA-breach evaluation
                               so breaches are detected even without a user
                               interacting with the affected activity?
Proposed Assumption:           Introduce a new, minimal Spring @Scheduled
                               fixed-rate job (interval itself exposed as a
                               configuration property, mirroring the grace-
                               period configuration approach in OQ-5) inside
                               the activities module, invoking a new SLA
                               evaluation service method. The scheduled method
                               itself contains no business logic (it only
                               triggers the evaluation), and Generator/Evaluator
                               must be able to invoke the evaluation service
                               directly and deterministically in tests without
                               waiting on the scheduler.
Rationale:                     Spring Boot's @Scheduled capability ships with
                               spring-boot-starter (already a dependency) and
                               requires no new external dependency, broker, or
                               platform, so it does not constitute a "new
                               scheduling platform" in the sense excluded by
                               PROMPT.md §10. No existing StoreOps mechanism
                               (event, service call, or otherwise) currently
                               fires without an explicit user-initiated request,
                               so time-driven detection has no alternative
                               existing hook to reuse.
Impact:                        This introduces the first background/periodic
                               processing mechanism in StoreOps. Architecture
                               rules require this to be surfaced for approval
                               rather than silently introduced.
Affected Acceptance Criteria: AC-S1-001 through AC-S1-005
Decision Required From Human: Yes
Blocking:                      Yes

Question:                     Notification has no field correlating it to the
                               Task it concerns, so duplicate-notification
                               prevention and grace-period-start anchoring
                               cannot be computed from existing Alerts data.
                               Where should this correlation/state live?
Proposed Assumption:           Add a nullable `referenceId` field to
                               Notification and NotificationDto, populated
                               with the source Task's id for SLA_BREACH and
                               ESCALATION notifications. Duplicate detection
                               queries for "does an SLA_BREACH notification
                               already exist for referenceId=<taskId>"; grace-
                               period expiry is computed from that
                               notification's existing `createdAt` field.
Rationale:                     Keeps new state inside the module that already
                               owns notification persistence (Alerts), avoiding
                               cross-module field pollution on Task, and reuses
                               the existing `createdAt` timestamp rather than
                               inventing a new timestamp field.
Impact:                        This is a material, additive change to the
                               Notification domain model/DTO and to
                               AlertRepository's query surface (a new lookup by
                               referenceId + type is needed). Alternative
                               considered and rejected by Planner as a default:
                               tracking breach state directly on Task (rejected
                               because it mixes alerting-specific state into
                               the Activities module).
Affected Acceptance Criteria: AC-S2-003, AC-S2-004, AC-S3-001, AC-S3-003
Decision Required From Human: Yes
Blocking:                      Yes

Question:                     Should Department Lead / Store Manager
                               resolution use Project.members (ProjectRole,
                               scoped to the Task's programme) or the separate
                               global User.role (StaffRole) field?
Proposed Assumption:           Use Project.members (see Assumptions section).
Rationale:                     Directly reachable from Task.programmeId with
                               no further join; StoreOps evidence does not
                               establish a reliable 1:1 mapping from a global
                               StaffRole to a specific store/programme.
Impact:                        Determines which existing relationship the
                               Generator must query and which module gains a
                               new read method.
Affected Acceptance Criteria: AC-S2-001, AC-S2-002, AC-S3-002
Decision Required From Human: Yes
Blocking:                      No

Question:                     What is the grace-period value, its
                               representation, and its configuration key?
Proposed Assumption:           A new Spring `@ConfigurationProperties`-bound
                               property, e.g. `storeops.sla.grace-period`
                               (java.time.Duration, ISO-8601 string such as
                               `PT4H`), added to application.yml with an
                               explicit default pending business confirmation.
Rationale:                     No existing StoreOps configuration convention
                               beyond `server.port`/`spring.application.name`
                               exists to extend; @ConfigurationProperties with
                               Duration is the idiomatic Spring Boot mechanism
                               and introduces no new dependency.
Impact:                        Determines the exact configuration key/type
                               Generator implements and Evaluator verifies.
Affected Acceptance Criteria: AC-S3-001, AC-S3-002, AC-S3-003, AC-S3-004
Decision Required From Human: Yes
Blocking:                      No

Question:                     Is "Unresolved" == TaskStatus != DONE (i.e. does
                               BLOCKED also count as unresolved/breach-
                               eligible, alongside TODO and IN_PROGRESS)?
Proposed Assumption:           Yes (see Assumptions section).
Rationale:                     DONE is the only status with clear completion
                               semantics in the existing four-value enum.
Impact:                        Determines the exact status set the eligibility
                               and resolution-before-escalation checks use.
Affected Acceptance Criteria: AC-S1-003, AC-S1-004, AC-S3-004
Decision Required From Human: Yes
Blocking:                      No
```

Blocking Open Questions (OQ-1, OQ-2, OQ-3) must be resolved — via approval of the proposed assumption, or a substitute human decision — before Generator begins Sprint 1 implementation.

---

## Applicable Architecture Constraints

```text
Constraint Source:  domains/storeops/architecture-rules/SKILL.md
Applicable Rules:   Controller -> Service -> Repository layering; cross-module
                     repository access prohibited; cross-module reads must use
                     an approved service/query interface; cross-module side
                     effects must use an established business event, approved
                     application interface, scheduled processing, or another
                     existing StoreOps pattern (mechanism resolved by this
                     spec's Open Questions, not mandated as Event Bus by
                     default); Reports read-only constraint (not implicated —
                     Reports is out of scope); AppError contract must be
                     preserved for any new failure path.
Feature Implications: New code must not have Alerts (or any consumer) read
                     Activities' or Programmes' repositories directly. The
                     Activities module must expose the breach signal (however
                     Sprint 1/2 approve it) through its own service or an
                     approved event, and the Programmes module must expose
                     member/role data through a new ProgrammeService read
                     method (not repository access) rather than have Alerts
                     reach into ProgrammeRepository.

Constraint Source:  domains/storeops/business-events/SKILL.md
Applicable Rules:   SLA Breach Alerting does not inherently require an event-
                     driven implementation; a feature-specific event, if used,
                     must be approved via the sprint contract before
                     implementation and must not be assumed to be named
                     SLA_BREACH merely because that AlertType value exists.
Feature Implications: If Sprint 1/2 approval selects an internal DomainEvent
                     to decouple detection from notification, its name and
                     payload must be defined explicitly in the approved sprint
                     contract, not invented ad hoc by Generator.

Constraint Source:  .harness/config/technologies/java-spring.yaml
Applicable Rules:   Constructor dependency injection; Maven build via
                     `mvn clean verify`; JUnit 5 + MockMvc; JaCoCo; Checkstyle;
                     SpotBugs; ArchUnit (tool availability only — thresholds
                     and enablement are governance-owned, see Risks).
Feature Implications: New services/components must use constructor injection
                     and follow existing package-per-layer structure
                     (controller/service/repository/model/dto/events).
```

---

## Dependencies

```text
Dependency:                 Human resolution of blocking Open Questions
                             OQ-1 (due-date attribute), OQ-2 (breach-detection
                             trigger), and OQ-3 (notification correlation /
                             idempotency state).
Nature:                      domain / architecture / configuration
Status:                      needs-decision
Impact if Unavailable:       Sprint 1 cannot be implemented; Sprints 2 and 3
                             cannot be implemented in turn.

Dependency:                 Sprint 2 (Department Lead notification) depends on
                             Sprint 1 producing an observable "breach newly
                             detected" signal for a given Task.
Nature:                      technical
Status:                      existing (once Sprint 1 is approved and built)
Impact if Unavailable:       Sprint 2 has nothing to react to.

Dependency:                 Sprint 3 (grace period / Store Manager escalation)
                             depends on Sprint 2's persisted SLA_BREACH
                             notification (specifically its correlation field
                             from OQ-3 and its createdAt timestamp) to compute
                             grace-period expiry and to confirm the Department
                             Lead notification precondition.
Nature:                      technical / data
Status:                      needs-decision (depends on OQ-3 resolution)
Impact if Unavailable:       Store Manager escalation cannot determine timing
                             or confirm the required precondition.

Dependency:                 New ProgrammeService read method (to expose
                             Project.members for a given programmeId) must
                             exist before Sprint 2 can resolve Department Lead
                             / Store Manager without cross-module repository
                             access.
Nature:                      technical / architecture
Status:                      needs-verification (not yet implemented; natural
                             minimal extension of an existing interface)
Impact if Unavailable:       Sprint 2/3 would be forced into a prohibited
                             cross-module repository dependency.
```

---

## Risks

```text
Risk:        The StoreOps pom.xml already binds a JaCoCo `check` execution to
             the Maven `verify` phase with a mandatory 70% BUNDLE line-coverage
             minimum. Because the configured build/verification command is
             `mvn clean verify`, insufficient coverage on new SLA code can fail
             that command outright, which would fail the mandatory `build`/
             `tests` hard gates for this run even though governance's separate
             `coverage` hard gate is disabled/non-mandatory here.
Likelihood:  Medium
Impact:      High
Mitigation:  Generator must write sufficient unit tests for all new SLA code
             paths regardless of the disabled governance coverage gate, since
             the pom-level check is independent of that governance flag.
Owner:       Generator / Evaluator

Risk:        This feature introduces the first cross-module service-to-service
             read (Activities/Alerts -> Programmes) and the first background/
             periodic processing (@Scheduled) in this codebase, so there is no
             established precedent to imitate; a poor first implementation
             could set an undesirable architectural precedent for future
             features.
Likelihood:  Medium
Impact:      Medium
Mitigation:  Sprint contracts must specify the exact new interface/method
             signatures and constrain Generator to those, rather than allowing
             ad hoc invention.
Owner:       Planner (contract precision) / Evaluator (architecture review)

Risk:        No existing concurrency-control precedent (e.g., locking,
             optimistic versioning) exists anywhere in StoreOps for repeated or
             concurrent evaluation runs. Relying on "does a correlated
             SLA_BREACH/ESCALATION notification already exist" as the sole
             dedup check has a narrow race window if the scheduled evaluation
             were ever run concurrently with itself.
Likelihood:  Low (single in-memory instance, fixed-rate scheduling in the
             reference implementation)
Impact:      Low
Mitigation:  Accept as a documented residual risk for this reference
             implementation; do not introduce a new distributed-locking
             mechanism, which would exceed feature scope (PROMPT.md §10).
Owner:       Human reviewer (accept risk) / Planner (documented)

Risk:        Pre-existing Task records (if any exist at runtime) will have
             `dueAt = null` once the field is introduced, and must be treated
             as never-overdue to avoid a mass false-positive breach event at
             rollout.
Likelihood:  High (certain for any pre-existing data)
Impact:      Low if handled explicitly in eligibility logic; Medium if not.
Mitigation:  Eligibility check must explicitly treat null `dueAt` as "not
             overdue."
Owner:       Generator
```

---

## Sprint Decomposition

```text
Sprint ID:       SPRINT-1
Objective:       Detect SLA-breach-eligible activities (HIGH/CRITICAL priority,
                 overdue, unresolved) and make the detection observable to
                 downstream sprints, without yet notifying anyone.
Scope Summary:   activities module: due-date attribute, eligibility evaluation,
                 breach-detection trigger.
Rationale:       Isolates the "detect" half of the business flow, which is
                 independently testable purely with Task data (priority,
                 status, due date) and has no dependency on Alerts/Programmes.
                 Also carries the two most architecturally material,
                 blocking decisions (OQ-1, OQ-2), so approving/building them
                 first de-risks the rest of the feature.
Dependencies:    Blocking Open Questions OQ-1, OQ-2 (and non-blocking OQ-6).
Contract Ref:    sprint-SPRINT-1-contract.md

Sprint ID:       SPRINT-2
Objective:       When Sprint 1 detects an eligible SLA breach, resolve the
                 assigned Department Lead and notify them exactly once per
                 breach.
Scope Summary:   programmes module: new read method exposing Project.members.
                 alerts module: new create/notify capability using
                 AlertType.SLA_BREACH; new correlation field on Notification;
                 duplicate-notification prevention.
Rationale:       Bounded to the "notify" half of the flow; independently
                 testable against Sprint 1's detection signal via mocks/stubs,
                 without needing the grace-period timer.
Dependencies:    Sprint 1 (detection signal). Blocking Open Question OQ-3.
                 Non-blocking OQ-4.
Contract Ref:    sprint-SPRINT-2-contract.md

Sprint ID:       SPRINT-3
Objective:       Track the configured grace period after Department Lead
                 notification and escalate to the Store Manager if the
                 activity remains unresolved when the grace period expires;
                 no escalation if resolved first.
Scope Summary:   alerts module: grace-period configuration, escalation
                 evaluation and AlertType.ESCALATION notification, escalation
                 idempotency. programmes module: Store Manager resolution
                 (reuses Sprint 2's read method).
Rationale:       Depends on Sprint 2's persisted notification (timestamp +
                 correlation) as its timing anchor and precondition; kept
                 separate from Sprint 2 because grace-period/time-based
                 testing and escalation resolution are a distinct, higher-risk
                 concern (deterministic Clock usage) from immediate
                 notification.
Dependencies:    Sprint 1, Sprint 2. Non-blocking OQ-4, OQ-5, OQ-6.
Contract Ref:    sprint-SPRINT-3-contract.md
```

---

## Traceability

```text
PROMPT.md:                   PROMPT.md
Application Configuration:   .harness/config/applications/storeops.yaml (storeops)
Technology Configuration:    .harness/config/technologies/java-spring.yaml (java-spring)
Governance Policy:           .harness/config/policies/default-governance.yaml (default-governance)
Domain Skills Consulted:     .harness/skills/domains/storeops/app-context/SKILL.md
                              .harness/skills/domains/storeops/domain-rules/SKILL.md
                              .harness/skills/domains/storeops/architecture-rules/SKILL.md
                              .harness/skills/domains/storeops/business-events/SKILL.md
                              .harness/skills/core/architecture-principles/SKILL.md
                              .harness/skills/core/sprint-decomposition/SKILL.md
Repository Evidence:         applications/storeops/src/main/java/com/storeops/activities/**
                              applications/storeops/src/main/java/com/storeops/programmes/**
                              applications/storeops/src/main/java/com/storeops/alerts/**
                              applications/storeops/src/main/java/com/storeops/staff/**
                              applications/storeops/src/main/java/com/storeops/common/**
                              applications/storeops/src/main/resources/application.yml
                              applications/storeops/pom.xml
                              applications/storeops/src/test/java/com/storeops/**
Approved Human Decisions:    Pending (this run is AWAITING_APPROVAL)
```

---

## Planner Completion

```text
STATUS: AWAITING_APPROVAL
```

Implementation does not begin until the approval mechanism defined by `.harness/config/harness.yaml` (`approvalCommand: APPROVED`) is satisfied, and blocking Open Questions OQ-1, OQ-2, and OQ-3 are resolved.
