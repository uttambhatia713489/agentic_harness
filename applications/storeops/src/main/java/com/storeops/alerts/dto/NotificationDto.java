package com.storeops.alerts.dto;

import com.storeops.alerts.model.AlertType;
import com.storeops.alerts.model.NotificationChannel;
import com.storeops.alerts.model.NotificationStatus;

public record NotificationDto(
        String id,
        String userId,
        AlertType type,
        NotificationChannel channel,
        NotificationStatus status,
        String message,
        String referenceId
) {
}
