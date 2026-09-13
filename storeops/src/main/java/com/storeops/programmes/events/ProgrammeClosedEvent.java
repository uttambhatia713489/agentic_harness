package com.storeops.programmes.events;

import com.storeops.common.events.DomainEvent;

public record ProgrammeClosedEvent(String programmeId, String storeId) implements DomainEvent {
}
