# Sprint Contract: SPRINT-2 — Department Lead Notification

Runtime instance of: `.harness/templates/sprint-contract.template.md`

Status: APPROVED (Sprint 2 only). Approval resolves blocking Open Question
OQ-3 and non-blocking OQ-4 exactly as stated in this contract's Assumptions
section (below), scoped to Sprint 2 only. Sprint 3 remains AWAITING_APPROVAL
and is not authorized by this approval.

---

## Approval Record

```text
Approval Command:   APPROVED
Approved Scope:     Sprint 2 (SPRINT-2) only — scope, acceptance criteria,
                     architecture constraints, required tests, dependencies,
                     assumptions, and completion conditions of this contract
                     as written.
Not Approved:       SPRINT-3, or any other module, endpoint, event, domain
                     model, infrastructure, refactoring, or contract change
                     beyond this document.
Approved By:        uttambhatia@outlook.com (session user)
Approved At:        2026-09-21
```

---

## Identifiers

```text
Run ID:            RUN-20260920-8e88a0
Sprint ID:         SPRINT-2
Contract ID:       RUN-20260920-8e88a0-SPRINT-2
Application:       storeops
Technology:        java-spring
Governance Policy: default-governance
Feature Prompt:    PROMPT.md
Specification:     .harness/output/RUN-20260920-8e88a0/spec.md
Approved By:       uttambhatia@outlook.com (session user)
Approved At:        2026-09-21
Baseline:          v2.0
```

---

## Objective

When Sprint 1 detects an eligible SLA breach, StoreOps resolves the Department
Lead assigned to the breached activity's programme and notifies them, with
enough business context to act, exactly once per breach.

---

## Scope

- New read method on the existing `ProgrammeService` interface exposing a
  given programme's `members` (reusing `ProgrammeDto`, which already includes
  `members: List<ProjectMember>`), used to resolve the Department Lead.
- Resolution of the Department Lead as the `ProjectMember`(s) with
  `role == ProjectRole.DEPARTMENT_LEAD` in the breached activity's programme
  (per approved resolution of Open Question OQ-4).
- New create/notify capability on `AlertService`/`AlertRepository` producing a
  `Notification` with `type = AlertType.SLA_BREACH`, containing sufficient
  business context (at minimum: activity id, activity title, priority, due
  date, programme id) in the notification `message`/payload.
- New `referenceId` field on `Notification`/`NotificationDto` (per approved
  resolution of Open Question OQ-3), populated with the breached activity's id.
- Duplicate-notification prevention: at most one `SLA_BREACH` notification is
  created per activity per breach (checked via `referenceId` + `type` before
  creating a new one).
- Wiring from Sprint 1's detection output (service call or DomainEvent, per
  Sprint 1's approved mechanism) into this sprint's notification creation.

## Out of Scope

- Grace-period tracking and Store Manager escalation (Sprint 3).
- Any change to `NotificationChannel` delivery behavior (IN_APP/EMAIL sending
  itself is unchanged; this sprint only creates the `Notification` record).
- Any change to `reports` or `staff` authentication.

---

## Dependencies

```text
Dependency:               Sprint 1 (SPRINT-1) approved and implemented,
                          providing an observable "breach newly detected"
                          output for a given activity.
Nature:                   technical
Status:                   pending (Sprint 1 not yet approved/built)
Impact if Unavailable:    Sprint 2 has no detection signal to react to.

Dependency:               Human approval of Open Question OQ-3 (Notification
                          correlation field) from spec.md.
Nature:                   domain / architecture
Status:                   pending
Impact if Unavailable:    Duplicate-notification prevention cannot be
                          implemented as specified.

Dependency:               Human approval (or acceptance of the proposed
                          default) of Open Question OQ-4 (Department Lead
                          resolution source) from spec.md.
Nature:                   domain
Status:                   pending
Impact if Unavailable:    Notifications could be resolved to the wrong
                          recipients.
```

---

## Assumptions

```text
Assumption:      Department Lead == ProjectMember(s) with role ==
                  ProjectRole.DEPARTMENT_LEAD in the Project referenced by the
                  breached activity's programmeId. If zero such members exist,
                  the condition is logged and no notification is created for
                  that activity (no exception thrown). If multiple exist, all
                  are notified.
Basis:            spec.md Open Question OQ-4 and related Assumptions.
Impact if False:  Wrong recipient(s), or an unhandled exception on an
                  unassigned role.
Affected Acceptance Criteria: AC-S2-001, AC-S2-002, AC-S2-004

Assumption:      Notification.referenceId (new nullable field) holds the
                  breached activity's id for SLA_BREACH notifications;
                  duplicate check queries existing notifications by
                  referenceId + type == SLA_BREACH.
Basis:            spec.md Open Question OQ-3 (Proposed Assumption).
Impact if False:  Duplicate-prevention and Sprint 3's grace-period anchor
                  would need a different correlation mechanism.
Affected Acceptance Criteria: AC-S2-003, AC-S2-004
```

---

## Applicable Domain and Application Context

```text
Application Context (reference):   .harness/skills/domains/storeops/app-context/SKILL.md
Domain Rules (reference):          .harness/skills/domains/storeops/domain-rules/SKILL.md
Business Event Knowledge (ref):    .harness/skills/domains/storeops/business-events/SKILL.md
Feature-Specific Domain Behavior:  AlertType.SLA_BREACH (existing enum value,
                                    currently unused) is reused, not
                                    reinvented, for this notification. The
                                    notification message must contain enough
                                    context (activity id/title/priority/due
                                    date/programme id) for the Department Lead
                                    to act without needing to separately query
                                    the activity, per PROMPT.md §2.2.
```

---

## Applicable Architecture Rules

```text
Architecture Rules (reference):    .harness/skills/domains/storeops/architecture-rules/SKILL.md
Approved Interaction Mechanism:    Cross-module read: Alerts (or a new
                                    coordinating component) -> new
                                    ProgrammeService read method -> Programmes,
                                    following the approved-service-interface
                                    pattern in architecture-rules/SKILL.md §4
                                    (no direct ProgrammeRepository access from
                                    outside the programmes module).
                                    Detection-to-notification: whichever
                                    mechanism Sprint 1's contract fixes
                                    (service call or DomainEvent) is reused
                                    unchanged; this sprint does not introduce
                                    a second, competing mechanism.
Feature-Specific Constraints:      No direct AlertRepository access from
                                    outside alerts; no direct
                                    ActivityRepository/ProgrammeRepository
                                    access from alerts. AppError contract
                                    preserved for any new user-facing failure
                                    path (unlikely to be exercised, since this
                                    is primarily an internal flow).
```

---

## Acceptance Criteria

```text
AC ID:            AC-S2-001
Description:      Eligible SLA breach results in the assigned Department Lead
                   being notified with sufficient business context.
GIVEN:            An activity identified as an SLA breach by Sprint 1's
                   detection, whose programme has exactly one ProjectMember
                   with role DEPARTMENT_LEAD.
WHEN:             Department Lead notification processing runs for that
                   breach.
THEN:             Exactly one Notification is created with
                   userId = the Department Lead's user id, type =
                   AlertType.SLA_BREACH, and a message/payload containing at
                   least the activity id, title, priority, and due date.
Mandatory:        Yes
Positive/Negative: positive
Applicable Domain / Architecture References: PROMPT.md §2.2; domains/storeops/architecture-rules/SKILL.md §4
Feature Evidence Expectations: Unit test on the new notification-creation
                   logic with a mocked ProgrammeService/AlertRepository.

AC ID:            AC-S2-002
Description:      When a programme has multiple Department Leads, all are
                   notified.
GIVEN:            An activity identified as an SLA breach, whose programme has
                   two or more ProjectMembers with role DEPARTMENT_LEAD.
WHEN:             Department Lead notification processing runs for that
                   breach.
THEN:             One Notification per matching Department Lead is created,
                   each with type = AlertType.SLA_BREACH.
Mandatory:        Yes
Positive/Negative: positive
Applicable Domain / Architecture References: spec.md Assumptions (multiple
                   matching members)
Feature Evidence Expectations: Unit test with two ProjectMembers holding
                   DEPARTMENT_LEAD in the same programme.

AC ID:            AC-S2-003
Description:      A breach that is not eligible (per Sprint 1's negative
                   paths) never produces a Department Lead notification.
GIVEN:            An activity not identified as an SLA breach by Sprint 1
                   (e.g., LOW priority, or DONE, or not overdue).
WHEN:             Department Lead notification processing runs.
THEN:             No Notification is created for that activity.
Mandatory:        Yes
Positive/Negative: negative
Applicable Domain / Architecture References: PROMPT.md §2.5
Feature Evidence Expectations: Unit test asserting no AlertRepository.save
                   invocation for a non-breaching activity.

AC ID:            AC-S2-004
Description:      Repeated detection of the same, still-eligible breach does
                   not create duplicate Department Lead notifications.
GIVEN:            An activity already has an existing Notification with
                   referenceId = activity id and type = AlertType.SLA_BREACH.
WHEN:             Department Lead notification processing runs again for the
                   same activity while it is still an eligible breach.
THEN:             No additional SLA_BREACH Notification is created for that
                   activity (duplicate suppressed).
Mandatory:        Yes
Positive/Negative: negative
Applicable Domain / Architecture References: PROMPT.md §2.2, §4.6
Feature Evidence Expectations: Unit test seeding an existing matching
                   Notification and asserting no second save occurs.

AC ID:            AC-S2-005
Description:      When a breached activity's programme has zero
                   ProjectMembers with role DEPARTMENT_LEAD, processing
                   degrades gracefully.
GIVEN:            An activity identified as an SLA breach, whose programme has
                   no ProjectMember with role DEPARTMENT_LEAD.
WHEN:             Department Lead notification processing runs for that
                   breach.
THEN:             No Notification is created and no exception propagates from
                   the notification-processing step (per spec.md Assumptions).
Mandatory:        Yes
Positive/Negative: negative
Applicable Domain / Architecture References: spec.md Assumptions (zero
                   matching members)
Feature Evidence Expectations: Unit test with an empty DEPARTMENT_LEAD member
                   set.
```

---

## Required Tests

```text
Test Expectation:       Unit tests covering AC-S2-001 through AC-S2-005 using
                         mocked ProgrammeService and AlertRepository (no real
                         cross-module wiring required in the test).
Type:                   unit
Applies to AC:          AC-S2-001, AC-S2-002, AC-S2-003, AC-S2-004, AC-S2-005
Existing Application Convention Reference: JUnit 5 + Mockito style from
                         AlertServiceTest.java / ActivityServiceTest.java.
Deterministic Time Handling Required: No (no time-based logic in this sprint
                         beyond reusing Sprint 1's already-evaluated result).

Test Expectation:       New ProgrammeService read method has its own unit
                         test(s) verifying it returns member data without
                         mutating state (read-only).
Type:                   unit
Applies to AC:          AC-S2-001, AC-S2-002, AC-S2-005
Existing Application Convention Reference: ProgrammeServiceTest.java
Deterministic Time Handling Required: No
```

---

## Hard Gates (Reference)

```text
Hard Gate Source:          .harness/config/policies/default-governance.yaml
Applicable Hard Gates:     build (mandatory), tests (mandatory),
                            acceptanceCriteria (mandatory), governanceEvidence
                            (mandatory). coverage/staticAnalysis/architecture/
                            security not mandatory this run (see spec.md
                            Risks re: pom-level JaCoCo check).
Sprint-Specific Emphasis:  Cross-module architecture compliance (no direct
                            repository access across activities/programmes/
                            alerts); duplicate-prevention negative path
                            (AC-S2-004) is mandatory.
```

---

## Verification Expectations

```text
Verification Command Source: .harness/config/technologies/java-spring.yaml
                              (mvn clean verify)
Applicable Configured Checks:
  - Build
  - Tests
  - Coverage        (configured, not mandatory this run)
  - Static Analysis (configured, not mandatory this run)
  - Architecture    (configured, not mandatory this run)
  - Security        (not configured)
Evaluator Order Reference:   .harness/skills/core/evaluation-framework/SKILL.md
```

---

## Completion Conditions

```text
Required Verdict For Completion:  PASS or CONDITIONAL_PASS as permitted by
                                   default-governance.yaml.
Required Acceptance Criteria:     AC-S2-001, AC-S2-002, AC-S2-003, AC-S2-004,
                                   AC-S2-005 (all mandatory).
Required Evidence Artifacts:      generator-summary-iteration-N.md
                                   evaluator-feedback-iteration-N.md
                                   run-log.md
Non-Blocking Findings Permitted:  Per default-governance.yaml CONDITIONAL_PASS
                                   routing (requiresHumanIntervention: false).
```

---

## Contract Change Handling

If the approved Department Lead resolution source (OQ-4) or Notification
correlation field (OQ-3) cannot be implemented as specified, `STATUS:
CONTRACT_CHANGE_REQUIRED` applies per `CLAUDE.md` §12/§16.

---

## Traceability

```text
PROMPT.md:                   PROMPT.md §2.2, §2.5, §3, §4.4, §4.6, §8
spec.md:                      .harness/output/RUN-20260920-8e88a0/spec.md
Application Configuration:   storeops
Technology Configuration:    java-spring
Governance Policy:           default-governance
Domain Skills Consulted:     domains/storeops/app-context, domain-rules,
                              architecture-rules, business-events
Approved Human Decisions:    Pending (OQ-3, OQ-4); depends on SPRINT-1
```
