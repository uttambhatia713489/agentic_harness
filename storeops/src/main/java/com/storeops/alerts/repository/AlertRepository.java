package com.storeops.alerts.repository;

import com.storeops.alerts.model.Notification;
import java.util.List;
import java.util.Optional;

public interface AlertRepository {

    List<Notification> findAllByUserId(String userId);

    Optional<Notification> findById(String id);

    Notification save(Notification notification);
}
