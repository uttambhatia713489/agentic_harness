package com.storeops.activities.scheduler;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.storeops.activities.model.Task;
import com.storeops.activities.model.TaskPriority;
import com.storeops.activities.service.SlaBreachEvaluationService;
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

    private SlaBreachScheduler slaBreachScheduler;

    @BeforeEach
    void setUp() {
        slaBreachScheduler = new SlaBreachScheduler(slaBreachEvaluationService);
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
}
