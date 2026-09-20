package com.storeops.activities.scheduler;

import com.storeops.activities.service.SlaBreachEvaluationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SlaBreachScheduler {

    private final SlaBreachEvaluationService slaBreachEvaluationService;

    public SlaBreachScheduler(final SlaBreachEvaluationService slaBreachEvaluationService) {
        this.slaBreachEvaluationService = slaBreachEvaluationService;
    }

    @Scheduled(fixedRateString = "${storeops.sla.detection-fixed-rate-ms:60000}")
    public void evaluateSlaBreaches() {
        slaBreachEvaluationService.detectBreaches();
    }
}
