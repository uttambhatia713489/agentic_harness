# StoreOps Spec

## Overview
**Project**: StoreOps REST API  
**Domain**: Retail store operations management  
**Goal**: Provide a compile-ready scaffold with stub implementations sufficient to compile, run tests, and pass linting. Use in-memory storage only.

## STACK
**Language**: Java 21 LTS  
**Framework**: Spring Boot 3.x  
**Testing**: JUnit 5 + MockMvc  
**Linting**: Checkstyle + SpotBugs  
**Auto check**: `mvn test` or `./gradlew test`

## Modules and Key Types
Each module follows the three-layer model **Routes → Service → Repository**. Repositories are interfaces with in-memory implementations for tests.

- **activities**: `Task`, `TaskStatus` (TODO | IN_PROGRESS | DONE | BLOCKED), `TaskPriority` (LOW | MEDIUM | HIGH | CRITICAL), `TaskCategory` (RESTOCKING | PLANOGRAM | AUDIT | COMPLIANCE | GENERAL)  
- **programmes**: `Project`, `ProjectMember`, `ProjectRole` (STORE_MANAGER | DEPARTMENT_LEAD | ASSOCIATE)  
- **staff**: `User`, `UserProfile`, `StaffRole` (REGIONAL_MANAGER | STORE_MANAGER | DEPARTMENT_LEAD | ASSOCIATE), `AuthToken`  
- **alerts**: `Notification`, `NotificationChannel` (IN_APP | EMAIL), `NotificationStatus`, `AlertType` (INVENTORY | SLA_BREACH | SHIFT_HANDOVER | ESCALATION)  
- **reports**: `Report`, `ReportType` (STORE_SUMMARY | REGIONAL_ROLLUP | DEPARTMENT_PERFORMANCE), `ReportStatus` (PENDING | READY | FAILED) — **read-only**

## Architecture Rules
These rules are non-negotiable and must be enforced by the generator and evaluator.

- **Module boundary**  
  - No module may import another module's **repository**. Cross-module reads must call the target module's **service** layer only.  
  - Automated check: dependency analyzer (depcruiser or equivalent) must report zero cross-module repository imports.

- **Event bus only for side effects**  
  - Any cross-module side effect must be raised via `EventBus.emit()`; never by direct service-to-service import.  
  - Evaluator checks code for `EventBus.emit()` usage where cross-module triggers occur and for absence of direct service imports for side effects.

- **Error contract**  
  - All errors thrown in **routes** and **services** must be typed and extend `AppError` with fields `code`, `message`, `statusCode`.  
  - **No** raw `new Error()` or unchecked raw exceptions thrown from services or controllers.  
  - Automated linter rule plus LLM check to ensure no raw throws in services/routes.

- **Layer separation**  
  - Routes must contain only HTTP handling and validation. Services contain business logic. Repositories contain data access only and must not call external services or perform HTTP logic.  
  - LLM-assessed checks and static analysis to ensure no skipping of layers.

- **Reports read-only**  
  - The `reports` module may aggregate and read data from other modules but must not perform write operations against other modules. LLM-assessed verification required.

## Base API Surface (9 endpoints)
All routes are under `/api`. Map below to modules as indicated.

**Activities**
- `GET /api/activities` — List activities. Support optional `programme` and `status` filters.  
- `POST /api/activities` — Create a new activity.  
- `GET /api/activities/:id` — Get activity by ID.  
- `PATCH /api/activities/:id` — Update activity fields: `status`, `priority`, `category`, `assignee`.  
- `DELETE /api/activities/:id` — Delete an activity. Authorization: owner or store manager only.

**Programmes**
- `GET /api/programmes` — List programmes for the authenticated store.  
- `POST /api/programmes` — Create a new programme.  
- `POST /api/programmes/:id/members` — Add a staff member to a programme.

**Alerts**
- `GET /api/alerts` — Get alerts for the authenticated user.

**Notes**
- `staff` module is auth-only; no CRUD endpoints in this baseline.  
- `reports` module is included as a read-only demonstration and not part of the 9 base endpoints.

## Testing and Coverage Thresholds
Minimum automated test coverage thresholds that must be met by the generated scaffold and sample tests.

- **Service layer**: **80%** line coverage minimum  
- **Route / controller layer**: **70%** line coverage minimum  
- **Shared utilities**: **60%** line coverage minimum  
- **Overall project**: **70%** line coverage minimum

**Testing requirements**
- Provide unit tests for service methods and integration tests for controllers using JUnit5 + MockMvc.  
- Include JaCoCo configuration and a sample coverage assertion step in the build.

## Linting and Static Analysis
- Include **Checkstyle** and **SpotBugs** configuration files.  
- CI or local check command should fail the build if Checkstyle or SpotBugs violations exceed zero critical issues.  
- Ensure code compiles with `mvn -q -DskipTests=false test` or `./gradlew test`.

## Error Handling and Types
- Implement `AppError` base class with fields: `code` (string), `message` (string), `statusCode` (int).  
- Provide a small typed error set (e.g., `NotFoundError`, `ValidationError`, `UnauthorizedError`, `ConflictError`) that extend `AppError`.  
- Provide an exception mapper or controller advice to convert `AppError` into proper HTTP responses.

## Event Bus
- Provide an `EventBus` interface and a simple in-memory implementation with `emit(event)` and `subscribe(eventType, handler)` stubs.  
- Cross-module side effects must call `EventBus.emit()` with a typed event object. Example events: `TaskOverdueEvent`, `ProgrammeClosedEvent`.

## Module File Layout Recommendation
/src/main/java/com/storeops
/activities
controller/ActivityController.java
service/ActivityService.java
repository/ActivityRepository.java
repository/InMemoryActivityRepository.java
dto/ActivityDto.java
model/Task.java
/programmes
controller/ProgrammeController.java
service/ProgrammeService.java
repository/ProgrammeRepository.java
repository/InMemoryProgrammeRepository.java
dto/ProgrammeDto.java
model/Project.java
/staff
controller/AuthController.java
service/StaffService.java
repository/StaffRepository.java
repository/InMemoryStaffRepository.java
dto/UserDto.java
model/User.java
/alerts
controller/AlertController.java
service/AlertService.java
repository/AlertRepository.java
repository/InMemoryAlertRepository.java
dto/NotificationDto.java
model/Notification.java
/reports
controller/ReportController.java
service/ReportService.java
repository/ReportRepository.java
dto/ReportDto.java
model/Report.java
/common
errors/AppError.java
errors/NotFoundError.java
events/EventBus.java
events/InMemoryEventBus.java
config/CheckstyleConfig.xml
config/SpotBugsConfig.xml


## In-memory storage rules
- Repositories must be simple thread-safe in-memory stores suitable for tests.  
- No external DB drivers or persistence frameworks required.

## Evaluator Checklist
The generator or CI evaluator must verify:
- No repository imports across module boundaries.  
- All cross-module side effects use `EventBus.emit()`.  
- No raw `new Error()` or unchecked raw throws in services or controllers.  
- Routes contain no business logic beyond validation and request/response mapping.  
- Reports module performs no write operations to other modules.  
- Tests exist and coverage thresholds are met.  
- Checkstyle and SpotBugs configs are present and build fails on violations.

## Commands
- Build and test: `mvn test` or `./gradlew test`  
- Run coverage report: `mvn jacoco:report` or Gradle equivalent  
- Run static analysis: `mvn spotbugs:check` and `mvn checkstyle:check`

## Deliverable Requirements
- **Stubs only**: DTOs, controllers, service interfaces and minimal implementations, repository interfaces and in-memory implementations, AppError hierarchy, EventBus stub, sample tests to meet coverage thresholds, Checkstyle and SpotBugs configs.  
- **Outputs**: Provide file tree and code blocks for changed files only when generating. Prefer diffs for iterative updates.
