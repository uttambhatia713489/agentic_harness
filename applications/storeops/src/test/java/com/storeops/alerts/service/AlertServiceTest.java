package com.storeops.alerts.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.storeops.alerts.model.AlertType;
import com.storeops.alerts.model.Notification;
import com.storeops.alerts.model.NotificationChannel;
import com.storeops.alerts.model.NotificationStatus;
import com.storeops.alerts.repository.AlertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class AlertServiceTest {

    @Mock
    private AlertRepository alertRepository;

    private AlertService alertService;

    @BeforeEach
    void setUp() {
        alertService = new AlertServiceImpl(alertRepository);
    }

    @Test
    void testListForUserReturnsEmptyList() {
        when(alertRepository.findAllByUserId("user-1")).thenReturn(new ArrayList<>());

        final var result = alertService.listForUser("user-1");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testListForUserReturnsNotifications() {
        final Notification notif = new Notification();
        notif.setId("notif-1");
        notif.setUserId("user-1");
        notif.setType(AlertType.INVENTORY);
        notif.setChannel(NotificationChannel.IN_APP);
        notif.setStatus(NotificationStatus.PENDING);
        notif.setMessage("Stock low");
        notif.setCreatedAt(Instant.now());
        when(alertRepository.findAllByUserId("user-1")).thenReturn(Arrays.asList(notif));

        final var result = alertService.listForUser("user-1");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("notif-1", result.get(0).id());
        assertEquals(AlertType.INVENTORY, result.get(0).type());
    }

    @Test
    void testListForUserFiltersCorrectly() {
        final List<Notification> notifications = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            final Notification notif = new Notification();
            notif.setId("notif-" + i);
            notif.setUserId("user-1");
            notif.setType(AlertType.INVENTORY);
            notif.setChannel(NotificationChannel.EMAIL);
            notif.setStatus(NotificationStatus.SENT);
            notif.setMessage("Message " + i);
            notif.setCreatedAt(Instant.now());
            notifications.add(notif);
        }
        when(alertRepository.findAllByUserId("user-1")).thenReturn(notifications);

        final var result = alertService.listForUser("user-1");

        assertEquals(3, result.size());
    }
}
