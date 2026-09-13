package com.storeops.alerts.controller;

import com.storeops.alerts.dto.NotificationDto;
import com.storeops.alerts.service.AlertService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(final AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationDto>> list(@RequestHeader(name = "X-User-Id") final String userId) {
        return ResponseEntity.ok(alertService.listForUser(userId));
    }
}
