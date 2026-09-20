package com.storeops.alerts.repository;

import com.storeops.alerts.model.AlertType;
import com.storeops.alerts.model.Notification;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryAlertRepository implements AlertRepository {

    private final Map<String, Notification> store = new ConcurrentHashMap<>();

    @Override
    public List<Notification> findAllByUserId(final String userId) {
        return store.values().stream()
                .filter(notification -> userId == null || userId.equals(notification.getUserId()))
                .toList();
    }

    @Override
    public Optional<Notification> findById(final String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Notification> findByReferenceIdAndType(final String referenceId, final AlertType type) {
        return store.values().stream()
                .filter(notification -> referenceId != null && referenceId.equals(notification.getReferenceId()))
                .filter(notification -> type == notification.getType())
                .findFirst();
    }

    @Override
    public List<Notification> findAllByType(final AlertType type) {
        return store.values().stream()
                .filter(notification -> type == notification.getType())
                .toList();
    }

    @Override
    public Notification save(final Notification notification) {
        store.put(notification.getId(), notification);
        return notification;
    }
}
