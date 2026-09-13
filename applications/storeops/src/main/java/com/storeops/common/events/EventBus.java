package com.storeops.common.events;

public interface EventBus {

    <T extends DomainEvent> void emit(T event);

    <T extends DomainEvent> void subscribe(Class<T> eventType, EventHandler<T> handler);
}
