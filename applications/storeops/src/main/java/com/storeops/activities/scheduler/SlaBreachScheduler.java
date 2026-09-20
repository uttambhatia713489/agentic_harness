package com.storeops.activities.scheduler;

import com.storeops.activities.model.Task;
import com.storeops.activities.service.SlaBreachEvaluationService;
import com.storeops.alerts.service.EscalationEvaluationService;
import com.storeops.alerts.service.SlaBreachNotificationService;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SlaBreachScheduler {

    private final SlaBreachEvaluationService slaBreachEvaluationService;
    private final SlaBreachNotificationService slaBreachNotificationService;
    private final EscalationEvaluationService escalationEvaluationService;

    public SlaBreachScheduler(final SlaBreachEvaluationService slaBreachEvaluationService,
            final SlaBreachNotificationService slaBreachNotificationService,
            final EscalationEvaluationService escalationEvaluationService) {
        this.slaBreachEvaluationService = slaBreachEvaluationService;
        this.slaBreachNotificationService = slaBreachNotificationService;
        this.escalationEvaluationService = escalationEvaluationService;
    }

    @Scheduled(fixedRateString = "${storeops.sla.detection-fixed-rate-ms:60000}")
    public void evaluateSlaBreaches() {
        final List<Task> breaches = slaBreachEvaluationService.detectBreaches();
        for (final Task breach : breaches) {
            slaBreachNotificationService.notifyDepartmentLead(
                    breach.getId(),
                    breach.getTitle(),
                    breach.getPriority() == null ? null : breach.getPriority().name(),
                    breach.getDueAt(),
                    breach.getProgrammeId());
        }
        escalationEvaluationService.evaluateEscalations();
    }
}
