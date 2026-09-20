package com.storeops.activities.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.storeops.activities.model.Task;
import com.storeops.activities.model.TaskPriority;
import com.storeops.activities.model.TaskStatus;
import com.storeops.activities.repository.ActivityRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SlaBreachEvaluationServiceTest {

    private static final Instant NOW = Instant.parse("2026-09-20T12:00:00Z");
    private static final Instant PAST = NOW.minusSeconds(3600);
    private static final Instant FUTURE = NOW.plusSeconds(3600);

    @Mock
    private ActivityRepository activityRepository;

    private Clock clock;

    private SlaBreachEvaluationService slaBreachEvaluationService;

    @BeforeEach
    void setUp() {
        clock = Clock.fixed(NOW, ZoneOffset.UTC);
        slaBreachEvaluationService = new SlaBreachEvaluationServiceImpl(activityRepository, clock);
    }

    private Task task(final TaskPriority priority, final TaskStatus status, final Instant dueAt) {
        final Task task = new Task();
        task.setId("task-1");
        task.setPriority(priority);
        task.setStatus(status);
        task.setDueAt(dueAt);
        return task;
    }

    @Test
    void testOverdueHighPriorityUnresolvedTaskIsBreach() {
        final Task task = task(TaskPriority.HIGH, TaskStatus.TODO, PAST);

        assertTrue(slaBreachEvaluationService.isBreach(task));
    }

    @Test
    void testOverdueCriticalPriorityUnresolvedTaskIsBreach() {
        final Task task = task(TaskPriority.CRITICAL, TaskStatus.IN_PROGRESS, PAST);

        assertTrue(slaBreachEvaluationService.isBreach(task));
    }

    @Test
    void testLowPriorityTaskIsNeverABreach() {
        final Task task = task(TaskPriority.LOW, TaskStatus.TODO, PAST);

        assertFalse(slaBreachEvaluationService.isBreach(task));
    }

    @Test
    void testMediumPriorityTaskIsNeverABreach() {
        final Task task = task(TaskPriority.MEDIUM, TaskStatus.TODO, PAST);

        assertFalse(slaBreachEvaluationService.isBreach(task));
    }

    @Test
    void testDoneTaskIsNeverABreachRegardlessOfPriorityOrDueDate() {
        final Task task = task(TaskPriority.CRITICAL, TaskStatus.DONE, PAST);

        assertFalse(slaBreachEvaluationService.isBreach(task));
    }

    @Test
    void testBlockedHighPriorityOverdueTaskIsStillABreach() {
        final Task task = task(TaskPriority.HIGH, TaskStatus.BLOCKED, PAST);

        assertTrue(slaBreachEvaluationService.isBreach(task));
    }

    @Test
    void testNullDueAtTaskIsNeverABreach() {
        final Task task = task(TaskPriority.HIGH, TaskStatus.TODO, null);

        assertFalse(slaBreachEvaluationService.isBreach(task));
    }

    @Test
    void testFutureDueAtTaskIsNeverABreach() {
        final Task task = task(TaskPriority.CRITICAL, TaskStatus.TODO, FUTURE);

        assertFalse(slaBreachEvaluationService.isBreach(task));
    }

    @Test
    void testDetectBreachesReturnsOnlyEligibleBreachesFromRepository() {
        final Task breach = task(TaskPriority.HIGH, TaskStatus.TODO, PAST);
        breach.setId("breach-1");
        final Task notOverdue = task(TaskPriority.HIGH, TaskStatus.TODO, FUTURE);
        notOverdue.setId("not-overdue");
        final Task lowPriority = task(TaskPriority.LOW, TaskStatus.TODO, PAST);
        lowPriority.setId("low-priority");
        when(activityRepository.findAll(null, null)).thenReturn(List.of(breach, notOverdue, lowPriority));

        final List<Task> result = slaBreachEvaluationService.detectBreaches();

        assertEquals(1, result.size());
        assertEquals("breach-1", result.get(0).getId());
    }
}
