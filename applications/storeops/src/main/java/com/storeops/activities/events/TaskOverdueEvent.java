package com.storeops.activities.events;

import com.storeops.common.events.DomainEvent;

public record TaskOverdueEvent(String taskId, String programmeId) implements DomainEvent {
}
