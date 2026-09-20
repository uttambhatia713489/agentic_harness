package com.storeops.activities.scheduler;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.storeops.activities.model.Task;
import com.storeops.activities.model.TaskPriority;
import com.storeops.activities.service.SlaBreachEvaluationService;
import com.storeops.alerts.service.EscalationEvaluationService;
import com.storeops.alerts.service.SlaBreachNotificationService;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SlaBreachSchedulerTest {

    @Mock
    private SlaBreachEvaluationService slaBreachEvaluationService;

    @Mock
    private SlaBreachNotificationService slaBreachNotificationService;

    @Mock
    private EscalationEvaluationService escalationEvaluationService;

    private SlaBreachScheduler slaBreachScheduler;

    @BeforeEach
    void setUp() {
        slaBreachScheduler = new SlaBreachScheduler(slaBreachEvaluationService, slaBreachNotificationService,
                escalationEvaluationService);
    }

    @Test
    void testEvaluateSlaBreachesDelegatesToDetectionServiceWithoutRequiringAUserRequest() {
        final Task breach = new Task();
        breach.setId("breach-1");
        breach.setPriority(TaskPriority.CRITICAL);
        when(slaBreachEvaluationService.detectBreaches()).thenReturn(List.of(breach));

        slaBreachScheduler.evaluateSlaBreaches();

        verify(slaBreachEvaluationService).detectBreaches();
    }

    @Test
    void testEvaluateSlaBreachesForwardsEachDetectedBreachToNotificationService() {
        final Instant dueAt = Instant.parse("2026-09-20T10:00:00Z");
        final Task breach = new Task();
        breach.setId("breach-1");
        breach.setTitle("Restock shelf 4");
        breach.setPriority(TaskPriority.HIGH);
        breach.setDueAt(dueAt);
        breach.setProgrammeId("prog-1");
        when(slaBreachEvaluationService.detectBreaches()).thenReturn(List.of(breach));

        slaBreachScheduler.evaluateSlaBreaches();

        verify(slaBreachNotificationService).notifyDepartmentLead("breach-1", "Restock shelf 4", "HIGH", dueAt,
                "prog-1");
    }

    @Test
    void testEvaluateSlaBreachesNotifiesNothingWhenNoBreachesDetected() {
        when(slaBreachEvaluationService.detectBreaches()).thenReturn(List.of());

        slaBreachScheduler.evaluateSlaBreaches();

        verify(slaBreachNotificationService, never()).notifyDepartmentLead(
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any());
    }

    @Test
    void testEvaluateSlaBreachesAlwaysTriggersEscalationEvaluation() {
        when(slaBreachEvaluationService.detectBreaches()).thenReturn(List.of());

        slaBreachScheduler.evaluateSlaBreaches();

        verify(escalationEvaluationService).evaluateEscalations();
    }
}
