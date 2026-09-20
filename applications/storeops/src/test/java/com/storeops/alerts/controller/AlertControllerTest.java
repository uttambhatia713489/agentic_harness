package com.storeops.alerts.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.storeops.alerts.dto.NotificationDto;
import com.storeops.alerts.model.AlertType;
import com.storeops.alerts.model.NotificationChannel;
import com.storeops.alerts.model.NotificationStatus;
import com.storeops.alerts.service.AlertService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;

@WebMvcTest(AlertController.class)
class AlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlertService alertService;

    @Test
    void testListAlertsForUser() throws Exception {
        final NotificationDto alert = new NotificationDto("notif-1", "user-1", AlertType.INVENTORY,
                NotificationChannel.IN_APP, NotificationStatus.PENDING, "Inventory low on aisle 5", null);
        when(alertService.listForUser("user-1")).thenReturn(Arrays.asList(alert));

        mockMvc.perform(get("/api/alerts")
                .header("X-User-Id", "user-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("notif-1"))
                .andExpect(jsonPath("$[0].type").value("INVENTORY"))
                .andExpect(jsonPath("$[0].message").value("Inventory low on aisle 5"));
    }
}
