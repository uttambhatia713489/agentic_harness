package com.storeops.common.events;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Component;

@Component
public class InMemoryEventBus implements EventBus {

    private final Map<Class<?>, List<EventHandler<?>>> handlers = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T extends DomainEvent> void emit(final T event) {
        final List<EventHandler<?>> eventHandlers = handlers.get(event.getClass());
        if (eventHandlers == null) {
            return;
        }
        for (final EventHandler<?> handler : eventHandlers) {
            ((EventHandler<T>) handler).handle(event);
        }
    }

    @Override
    public <T extends DomainEvent> void subscribe(final Class<T> eventType, final EventHandler<T> handler) {
        handlers.computeIfAbsent(eventType, key -> new CopyOnWriteArrayList<>()).add(handler);
    }
}
