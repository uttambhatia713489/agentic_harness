package com.storeops.activities.dto;

import com.storeops.activities.model.TaskCategory;
import com.storeops.activities.model.TaskPriority;
import com.storeops.activities.model.TaskStatus;

public record ActivityDto(
        String id,
        String programmeId,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        TaskCategory category,
        String assigneeId,
        String ownerId
) {
}
