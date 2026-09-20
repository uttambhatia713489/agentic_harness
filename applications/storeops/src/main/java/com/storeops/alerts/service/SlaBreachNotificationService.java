package com.storeops.alerts.service;

import java.time.Instant;

public interface SlaBreachNotificationService {

    void notifyDepartmentLead(String activityId, String activityTitle, String priority, Instant dueAt,
            String programmeId);
}
