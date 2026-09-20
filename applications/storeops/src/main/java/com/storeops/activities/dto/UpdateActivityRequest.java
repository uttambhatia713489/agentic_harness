package com.storeops.activities.dto;

import com.storeops.activities.model.TaskCategory;
import com.storeops.activities.model.TaskPriority;
import com.storeops.activities.model.TaskStatus;
import java.time.Instant;

public record UpdateActivityRequest(
        TaskStatus status,
        TaskPriority priority,
        TaskCategory category,
        String assigneeId,
        Instant dueAt
) {
}
