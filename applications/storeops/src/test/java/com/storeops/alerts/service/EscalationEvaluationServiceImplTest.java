package com.storeops.alerts.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.storeops.activities.dto.ActivityDto;
import com.storeops.activities.model.TaskCategory;
import com.storeops.activities.model.TaskPriority;
import com.storeops.activities.model.TaskStatus;
import com.storeops.activities.service.ActivityService;
import com.storeops.alerts.config.SlaProperties;
import com.storeops.alerts.model.AlertType;
import com.storeops.alerts.model.Notification;
import com.storeops.alerts.repository.AlertRepository;
import com.storeops.programmes.dto.ProgrammeDto;
import com.storeops.programmes.model.ProjectMember;
import com.storeops.programmes.model.ProjectRole;
import com.storeops.programmes.service.ProgrammeService;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EscalationEvaluationServiceImplTest {

    private static final Instant NOW = Instant.parse("2026-09-21T12:00:00Z");
    private static final Duration GRACE_PERIOD = Duration.ofHours(4);

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private ProgrammeService programmeService;

    @Mock
    private ActivityService activityService;

    private EscalationEvaluationService escalationEvaluationService;

    @BeforeEach
    void setUp() {
        final Clock clock = Clock.fixed(NOW, ZoneOffset.UTC);
        final SlaProperties slaProperties = new SlaProperties();
        slaProperties.setGracePeriod(GRACE_PERIOD);
        escalationEvaluationService = new EscalationEvaluationServiceImpl(alertRepository, programmeService,
                activityService, slaProperties, clock);
    }

    private Notification slaBreachNotification(final String activityId, final Instant createdAt) {
        final Notification notification = new Notification();
        notification.setId("breach-notif-" + activityId);
        notification.setReferenceId(activityId);
        notification.setType(AlertType.SLA_BREACH);
        notification.setCreatedAt(createdAt);
        return notification;
    }

    private ActivityDto activity(final String id, final TaskStatus status) {
        return new ActivityDto(id, "prog-1", "Restock shelf 4", "Description", status, TaskPriority.HIGH,
                TaskCategory.RESTOCKING, "user-1", "owner-1", NOW.minus(GRACE_PERIOD).minusSeconds(3600));
    }

    private ProgrammeDto programmeWithMembers(final ProjectMember... members) {
        return new ProgrammeDto("prog-1", "store-1", "Q4 Planning", "Desc", List.of(members));
    }

    private void stubNoExistingEscalation(final String activityId) {
        lenient().when(alertRepository.findByReferenceIdAndType(activityId, AlertType.ESCALATION))
                .thenReturn(Optional.empty());
    }

    @Test
    void testBreachUnresolvedAfterGracePeriodEscalatesToStoreManager() {
        final String activityId = "activity-1";
        stubNoExistingEscalation(activityId);
        when(alertRepository.findAllByType(AlertType.SLA_BREACH))
                .thenReturn(List.of(slaBreachNotification(activityId, NOW.minus(GRACE_PERIOD).minusSeconds(60))));
        when(activityService.getById(activityId)).thenReturn(activity(activityId, TaskStatus.TODO));
        when(programmeService.getById("prog-1")).thenReturn(
                programmeWithMembers(new ProjectMember("manager-1", ProjectRole.STORE_MANAGER)));

        escalationEvaluationService.evaluateEscalations();

        verify(alertRepository).save(argThat(notification -> "manager-1".equals(notification.getUserId())
                && notification.getType() == AlertType.ESCALATION
                && activityId.equals(notification.getReferenceId())));
    }

    @Test
    void testMultipleStoreManagersAreAllEscalatedTo() {
        final String activityId = "activity-1";
        stubNoExistingEscalation(activityId);
        when(alertRepository.findAllByType(AlertType.SLA_BREACH))
                .thenReturn(List.of(slaBreachNotification(activityId, NOW.minus(GRACE_PERIOD).minusSeconds(60))));
        when(activityService.getById(activityId)).thenReturn(activity(activityId, TaskStatus.TODO));
        when(programmeService.getById("prog-1")).thenReturn(
                programmeWithMembers(
                        new ProjectMember("manager-1", ProjectRole.STORE_MANAGER),
                        new ProjectMember("manager-2", ProjectRole.STORE_MANAGER),
                        new ProjectMember("lead-1", ProjectRole.DEPARTMENT_LEAD)));

        escalationEvaluationService.evaluateEscalations();

        verify(alertRepository, times(2)).save(any());
        verify(alertRepository).save(argThat(n -> "manager-1".equals(n.getUserId())));
        verify(alertRepository).save(argThat(n -> "manager-2".equals(n.getUserId())));
    }

    @Test
    void testGracePeriodNotYetExpiredDoesNotEscalate() {
        final String activityId = "activity-1";
        stubNoExistingEscalation(activityId);
        when(alertRepository.findAllByType(AlertType.SLA_BREACH))
                .thenReturn(List.of(slaBreachNotification(activityId, NOW.minus(GRACE_PERIOD).plusSeconds(60))));

        escalationEvaluationService.evaluateEscalations();

        verify(alertRepository, never()).save(any());
        verify(activityService, never()).getById(any());
    }

    @Test
    void testGracePeriodExpiredButActivityDoneDoesNotEscalate() {
        final String activityId = "activity-1";
        stubNoExistingEscalation(activityId);
        when(alertRepository.findAllByType(AlertType.SLA_BREACH))
                .thenReturn(List.of(slaBreachNotification(activityId, NOW.minus(GRACE_PERIOD).minusSeconds(60))));
        when(activityService.getById(activityId)).thenReturn(activity(activityId, TaskStatus.DONE));

        escalationEvaluationService.evaluateEscalations();

        verify(alertRepository, never()).save(any());
        verify(programmeService, never()).getById(any());
    }

    @Test
    void testActivityResolvedBeforeDeadlineStillDoesNotEscalateOnLaterReEvaluation() {
        final String activityId = "activity-1";
        stubNoExistingEscalation(activityId);
        when(alertRepository.findAllByType(AlertType.SLA_BREACH))
                .thenReturn(List.of(slaBreachNotification(activityId, NOW.minus(GRACE_PERIOD).minusSeconds(120))));
        when(activityService.getById(activityId)).thenReturn(activity(activityId, TaskStatus.DONE));

        escalationEvaluationService.evaluateEscalations();

        verify(alertRepository, never()).save(any());
    }

    @Test
    void testZeroStoreManagersDegradesGracefullyWithoutException() {
        final String activityId = "activity-1";
        stubNoExistingEscalation(activityId);
        when(alertRepository.findAllByType(AlertType.SLA_BREACH))
                .thenReturn(List.of(slaBreachNotification(activityId, NOW.minus(GRACE_PERIOD).minusSeconds(60))));
        when(activityService.getById(activityId)).thenReturn(activity(activityId, TaskStatus.TODO));
        when(programmeService.getById("prog-1")).thenReturn(
                programmeWithMembers(new ProjectMember("lead-1", ProjectRole.DEPARTMENT_LEAD)));

        assertDoesNotThrow(() -> escalationEvaluationService.evaluateEscalations());

        verify(alertRepository, never()).save(any());
    }

    @Test
    void testExistingEscalationSuppressesDuplicate() {
        final String activityId = "activity-1";
        final Notification existingEscalation = new Notification();
        existingEscalation.setId("esc-existing");
        existingEscalation.setReferenceId(activityId);
        existingEscalation.setType(AlertType.ESCALATION);
        when(alertRepository.findAllByType(AlertType.SLA_BREACH))
                .thenReturn(List.of(slaBreachNotification(activityId, NOW.minus(GRACE_PERIOD).minusSeconds(60))));
        when(alertRepository.findByReferenceIdAndType(activityId, AlertType.ESCALATION))
                .thenReturn(Optional.of(existingEscalation));

        escalationEvaluationService.evaluateEscalations();

        verify(alertRepository, never()).save(any());
        verify(activityService, never()).getById(any());
    }

    @Test
    void testBreachNotificationWithNullReferenceIdIsSkipped() {
        final Notification withoutReferenceId = new Notification();
        withoutReferenceId.setId("breach-no-ref");
        withoutReferenceId.setReferenceId(null);
        withoutReferenceId.setType(AlertType.SLA_BREACH);
        withoutReferenceId.setCreatedAt(NOW.minus(GRACE_PERIOD).minusSeconds(60));
        when(alertRepository.findAllByType(AlertType.SLA_BREACH)).thenReturn(List.of(withoutReferenceId));

        assertDoesNotThrow(() -> escalationEvaluationService.evaluateEscalations());

        verify(alertRepository, never()).save(any());
        verify(activityService, never()).getById(any());
    }

    @Test
    void testSlaPropertiesDefaultsWhenUnset() {
        final SlaProperties defaults = new SlaProperties();
        assertEquals(Duration.ofHours(4), defaults.getGracePeriod());
    }

    @Test
    void testSlaPropertiesBindsConfiguredValue() {
        final SlaProperties properties = new SlaProperties();
        properties.setGracePeriod(Duration.ofHours(8));
        assertEquals(Duration.ofHours(8), properties.getGracePeriod());
    }
}
