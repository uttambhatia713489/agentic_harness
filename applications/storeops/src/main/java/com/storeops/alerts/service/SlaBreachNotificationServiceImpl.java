package com.storeops.alerts.service;

import com.storeops.alerts.model.AlertType;
import com.storeops.alerts.model.Notification;
import com.storeops.alerts.model.NotificationChannel;
import com.storeops.alerts.model.NotificationStatus;
import com.storeops.alerts.repository.AlertRepository;
import com.storeops.programmes.dto.ProgrammeDto;
import com.storeops.programmes.model.ProjectRole;
import com.storeops.programmes.service.ProgrammeService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class SlaBreachNotificationServiceImpl implements SlaBreachNotificationService {

    private final AlertRepository alertRepository;
    private final ProgrammeService programmeService;

    public SlaBreachNotificationServiceImpl(final AlertRepository alertRepository,
            final ProgrammeService programmeService) {
        this.alertRepository = alertRepository;
        this.programmeService = programmeService;
    }

    @Override
    public void notifyDepartmentLead(final String activityId, final String activityTitle, final String priority,
            final Instant dueAt, final String programmeId) {
        if (alertRepository.findByReferenceIdAndType(activityId, AlertType.SLA_BREACH).isPresent()) {
            return;
        }

        final ProgrammeDto programme = programmeService.getById(programmeId);
        final List<String> departmentLeadUserIds = programme.members().stream()
                .filter(member -> member.getRole() == ProjectRole.DEPARTMENT_LEAD)
                .map(member -> member.getUserId())
                .toList();

        if (departmentLeadUserIds.isEmpty()) {
            return;
        }

        final String message = buildMessage(activityId, activityTitle, priority, dueAt);
        for (final String userId : departmentLeadUserIds) {
            alertRepository.save(newNotification(userId, message, activityId));
        }
    }

    private Notification newNotification(final String userId, final String message, final String activityId) {
        final Notification notification = new Notification();
        notification.setId(UUID.randomUUID().toString());
        notification.setUserId(userId);
        notification.setType(AlertType.SLA_BREACH);
        notification.setChannel(NotificationChannel.IN_APP);
        notification.setStatus(NotificationStatus.PENDING);
        notification.setMessage(message);
        notification.setReferenceId(activityId);
        notification.setCreatedAt(Instant.now());
        return notification;
    }

    private String buildMessage(final String activityId, final String activityTitle, final String priority,
            final Instant dueAt) {
        return "SLA breach: activity '" + activityTitle + "' (id=" + activityId + ", priority=" + priority
                + ") was due at " + dueAt + " and remains unresolved.";
    }
}
