package com.storeops.alerts.service;

import com.storeops.activities.dto.ActivityDto;
import com.storeops.activities.model.TaskStatus;
import com.storeops.activities.service.ActivityService;
import com.storeops.alerts.config.SlaProperties;
import com.storeops.alerts.model.AlertType;
import com.storeops.alerts.model.Notification;
import com.storeops.alerts.model.NotificationChannel;
import com.storeops.alerts.model.NotificationStatus;
import com.storeops.alerts.repository.AlertRepository;
import com.storeops.programmes.dto.ProgrammeDto;
import com.storeops.programmes.model.ProjectRole;
import com.storeops.programmes.service.ProgrammeService;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class EscalationEvaluationServiceImpl implements EscalationEvaluationService {

    private final AlertRepository alertRepository;
    private final ProgrammeService programmeService;
    private final ActivityService activityService;
    private final SlaProperties slaProperties;
    private final Clock clock;

    public EscalationEvaluationServiceImpl(final AlertRepository alertRepository,
            final ProgrammeService programmeService, final ActivityService activityService,
            final SlaProperties slaProperties, final Clock clock) {
        this.alertRepository = alertRepository;
        this.programmeService = programmeService;
        this.activityService = activityService;
        this.slaProperties = slaProperties;
        this.clock = clock;
    }

    @Override
    public void evaluateEscalations() {
        final Instant now = Instant.now(clock);
        for (final Notification breachNotification : alertRepository.findAllByType(AlertType.SLA_BREACH)) {
            evaluateBreachNotification(breachNotification, now);
        }
    }

    private void evaluateBreachNotification(final Notification breachNotification, final Instant now) {
        final String activityId = breachNotification.getReferenceId();
        if (activityId == null) {
            return;
        }
        if (alertRepository.findByReferenceIdAndType(activityId, AlertType.ESCALATION).isPresent()) {
            return;
        }
        final Instant deadline = breachNotification.getCreatedAt().plus(slaProperties.getGracePeriod());
        if (now.isBefore(deadline)) {
            return;
        }

        final ActivityDto activity = activityService.getById(activityId);
        if (activity.status() == TaskStatus.DONE) {
            return;
        }

        escalateToStoreManagers(activity);
    }

    private void escalateToStoreManagers(final ActivityDto activity) {
        final ProgrammeDto programme = programmeService.getById(activity.programmeId());
        final List<String> storeManagerUserIds = programme.members().stream()
                .filter(member -> member.getRole() == ProjectRole.STORE_MANAGER)
                .map(member -> member.getUserId())
                .toList();

        if (storeManagerUserIds.isEmpty()) {
            return;
        }

        final String message = buildMessage(activity);
        for (final String userId : storeManagerUserIds) {
            alertRepository.save(newNotification(userId, message, activity.id()));
        }
    }

    private Notification newNotification(final String userId, final String message, final String activityId) {
        final Notification notification = new Notification();
        notification.setId(UUID.randomUUID().toString());
        notification.setUserId(userId);
        notification.setType(AlertType.ESCALATION);
        notification.setChannel(NotificationChannel.IN_APP);
        notification.setStatus(NotificationStatus.PENDING);
        notification.setMessage(message);
        notification.setReferenceId(activityId);
        notification.setCreatedAt(Instant.now());
        return notification;
    }

    private String buildMessage(final ActivityDto activity) {
        return "SLA escalation: activity '" + activity.title() + "' (id=" + activity.id() + ", priority="
                + activity.priority() + ") was due at " + activity.dueAt()
                + " and remains unresolved after the configured grace period.";
    }
}
