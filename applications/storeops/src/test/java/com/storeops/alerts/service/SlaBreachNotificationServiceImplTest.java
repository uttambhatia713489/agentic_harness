package com.storeops.alerts.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.storeops.alerts.model.AlertType;
import com.storeops.alerts.model.Notification;
import com.storeops.alerts.repository.AlertRepository;
import com.storeops.programmes.dto.ProgrammeDto;
import com.storeops.programmes.model.ProjectMember;
import com.storeops.programmes.model.ProjectRole;
import com.storeops.programmes.service.ProgrammeService;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SlaBreachNotificationServiceImplTest {

    private static final Instant DUE_AT = Instant.parse("2026-09-20T10:00:00Z");

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private ProgrammeService programmeService;

    private SlaBreachNotificationService slaBreachNotificationService;

    @BeforeEach
    void setUp() {
        slaBreachNotificationService = new SlaBreachNotificationServiceImpl(alertRepository, programmeService);
    }

    private ProgrammeDto programmeWithMembers(final ProjectMember... members) {
        return new ProgrammeDto("prog-1", "store-1", "Q4 Planning", "Desc", List.of(members));
    }

    @Test
    void testEligibleBreachNotifiesAssignedDepartmentLeadWithBusinessContext() {
        when(alertRepository.findByReferenceIdAndType("activity-1", AlertType.SLA_BREACH))
                .thenReturn(Optional.empty());
        when(programmeService.getById("prog-1")).thenReturn(
                programmeWithMembers(new ProjectMember("lead-1", ProjectRole.DEPARTMENT_LEAD)));

        slaBreachNotificationService.notifyDepartmentLead("activity-1", "Restock shelf 4", "HIGH", DUE_AT, "prog-1");

        final ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(alertRepository).save(captor.capture());
        final Notification saved = captor.getValue();
        assertEquals("lead-1", saved.getUserId());
        assertEquals(AlertType.SLA_BREACH, saved.getType());
        assertEquals("activity-1", saved.getReferenceId());
        assertNotNull(saved.getMessage());
        assertTrue(saved.getMessage().contains("activity-1"));
        assertTrue(saved.getMessage().contains("Restock shelf 4"));
        assertTrue(saved.getMessage().contains("HIGH"));
        assertTrue(saved.getMessage().contains(DUE_AT.toString()));
    }

    @Test
    void testMultipleDepartmentLeadsAreAllNotified() {
        when(alertRepository.findByReferenceIdAndType("activity-1", AlertType.SLA_BREACH))
                .thenReturn(Optional.empty());
        when(programmeService.getById("prog-1")).thenReturn(
                programmeWithMembers(
                        new ProjectMember("lead-1", ProjectRole.DEPARTMENT_LEAD),
                        new ProjectMember("lead-2", ProjectRole.DEPARTMENT_LEAD),
                        new ProjectMember("manager-1", ProjectRole.STORE_MANAGER)));

        slaBreachNotificationService.notifyDepartmentLead("activity-1", "Restock shelf 4", "CRITICAL", DUE_AT,
                "prog-1");

        verify(alertRepository, times(2)).save(any());
        verify(alertRepository).save(argThat(notification -> "lead-1".equals(notification.getUserId())));
        verify(alertRepository).save(argThat(notification -> "lead-2".equals(notification.getUserId())));
    }

    @Test
    void testDuplicateBreachNotificationIsSuppressed() {
        final Notification existing = new Notification();
        existing.setId("notif-existing");
        existing.setReferenceId("activity-1");
        existing.setType(AlertType.SLA_BREACH);
        when(alertRepository.findByReferenceIdAndType("activity-1", AlertType.SLA_BREACH))
                .thenReturn(Optional.of(existing));

        slaBreachNotificationService.notifyDepartmentLead("activity-1", "Restock shelf 4", "HIGH", DUE_AT, "prog-1");

        verify(alertRepository, never()).save(any());
        verify(programmeService, never()).getById(any());
    }

    @Test
    void testZeroDepartmentLeadsDegradesGracefullyWithoutException() {
        when(alertRepository.findByReferenceIdAndType("activity-1", AlertType.SLA_BREACH))
                .thenReturn(Optional.empty());
        when(programmeService.getById("prog-1")).thenReturn(
                programmeWithMembers(new ProjectMember("manager-1", ProjectRole.STORE_MANAGER)));

        assertDoesNotThrow(() -> slaBreachNotificationService.notifyDepartmentLead("activity-1", "Restock shelf 4",
                "HIGH", DUE_AT, "prog-1"));

        verify(alertRepository, never()).save(any());
    }
}
