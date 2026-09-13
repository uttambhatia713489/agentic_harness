package com.storeops.alerts.service;

import com.storeops.alerts.dto.NotificationDto;
import com.storeops.alerts.model.Notification;
import com.storeops.alerts.repository.AlertRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;

    public AlertServiceImpl(final AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Override
    public List<NotificationDto> listForUser(final String userId) {
        return alertRepository.findAllByUserId(userId).stream()
                .map(this::toDto)
                .toList();
    }

    private NotificationDto toDto(final Notification notification) {
        return new NotificationDto(
                notification.getId(),
                notification.getUserId(),
                notification.getType(),
                notification.getChannel(),
                notification.getStatus(),
                notification.getMessage());
    }
}
